package com.mypersonalupdates.log;

import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.filters.Filter;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;
import org.skife.jdbi.v2.exceptions.ResultSetException;
import org.skife.jdbi.v2.exceptions.UnableToCreateStatementException;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.util.LongColumnMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

/**
 * Esta clase representa la consulta SQL que se realizará en
 * la base de datos a fin de obtener los resultados de una
 * búsqueda de actualizaciones realizada en el log {@link Log}.
 *
 * La consulta es contruida y ejecutada dinámicamente mediante
 * un filtro {@link Filter} y restricciones
 * de búsqueda {@link Restrictions}.
 * */
public final class LogSQLQuery {
    private String sql = "";
    private String conditions = "";
    private String postConditions = "";
    private final Map<Integer, String> stringParams = new LinkedHashMap<>();
    private final Map<Integer, Long> longParams = new LinkedHashMap<>();
    private final Map<Integer, Instant> timestampParams = new LinkedHashMap<>();
    private final Map<String, String> replacements = new Hashtable<>();

    private boolean closed = false;

    private void writeAttempt() throws SealedException {
        if(this.closed)
            throw new SealedException();
    }

    private Integer pos = 0;

    public LogSQLQuery() {}

    public LogSQLQuery(String baseSQL) {
        this.sql = baseSQL;
    }

    public void appendToCondition(String sql) throws SealedException {
        this.appendToCondition(null, sql);
    }

    public void appendToCondition(String combiner, String sql) throws SealedException {
        this.writeAttempt();

        this.conditions += (
                this.conditions.equals("") || combiner == null || combiner.equals("")
                        ? ""
                        : " " + combiner + " "
        ) + sql;
    }

    public void addStringParam(String param) throws SealedException {
        this.writeAttempt();

        this.stringParams.put(pos++, param);
    }

    public void addLongParam(Long param) throws SealedException {
        this.writeAttempt();

        this.longParams.put(pos++, param);
    }

    public void addTimestampParam(Instant param) throws SealedException {
        this.writeAttempt();

        this.timestampParams.put(pos++, param);
    }

    public void appendToPostCondition(String sql) throws SealedException {
        this.writeAttempt();

        this.postConditions += (this.postConditions.equals("") ? "" : " ") + sql;
    }

    public String addReplacement(String regex, String replacement) {
        return this.replacements.put(regex, replacement);
    }

    public List<Long> executeQuery(Handle handle)
            throws UnableToCreateStatementException, UnableToExecuteStatementException, ResultSetException
    {
        this.closed = true;

        final String finalSQL = this.doReplacements(
                this.sql + " "
                        + (this.conditions.equals("") ? "" : "WHERE "+this.conditions+" ")
                        + this.postConditions
        );

        Query<Map<String, Object>> query = handle.createQuery(finalSQL);

        this.bindParameters(query);

        return query.map(LongColumnMapper.WRAPPER).list();
    }

    private String doReplacements(String sql) {
        for (Map.Entry<String, String> replacement : this.replacements.entrySet())
            sql = sql.replaceAll(replacement.getKey(), replacement.getValue());

        return sql;
    }

    private void bindParameters(Query<Map<String, Object>> query) {
        Iterator<Map.Entry<Integer, String>>  itString  = this.stringParams.entrySet().iterator();
        Iterator<Map.Entry<Integer, Long>>    itLong    = this.longParams.entrySet().iterator();
        Iterator<Map.Entry<Integer, Instant>> itInstant = this.timestampParams.entrySet().iterator();

        Map.Entry<Integer, String>  stringEntry = null;
        Map.Entry<Integer, Long> longEntry = null;
        Map.Entry<Integer, Instant> instantEntry = null;

        int i = 0;
        while (stringEntry  != null || itString .hasNext() ||
               longEntry    != null || itLong   .hasNext() ||
               instantEntry != null || itInstant.hasNext() )
        {
            if (stringEntry  == null && itString .hasNext()) stringEntry  = itString.next();
            if (longEntry    == null && itLong   .hasNext()) longEntry = itLong.next();
            if (instantEntry == null && itInstant.hasNext()) instantEntry = itInstant.next();

            if (stringEntry != null && stringEntry.getKey().equals(i)) {
                query.bind(i, stringEntry.getValue());
                stringEntry = null;
            } else if (longEntry != null && longEntry.getKey().equals(i)) {
                query.bind(i, longEntry.getValue());
                longEntry = null;
            } else if(instantEntry != null && instantEntry.getKey().equals(i)) {
                query.bind(i, new Timestamp(instantEntry.getValue().toEpochMilli()));
                instantEntry = null;
            } else
                throw new AssertionError();
            i++;
        }
    }
}

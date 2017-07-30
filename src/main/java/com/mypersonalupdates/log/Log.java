package com.mypersonalupdates.log;

import com.mypersonalupdates.Config;
import com.mypersonalupdates.filters.Filter;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBConnection;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.db.actions.CategoryActions;
import com.mypersonalupdates.exceptions.SealedException;
import com.mypersonalupdates.exceptions.UserNotLoggedInToProviderException;
import com.mypersonalupdates.realtime.RealTimeStreamsManager;
import com.mypersonalupdates.realtime.UpdatesConsumer;
import com.mypersonalupdates.users.Category;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Esta clase representa un log de actualizaciones.
 *
 * Se encarga de recibir las actualizaciones en tiempo
 * real de todas las categorías {@link Category} creadas
 * en el sistema y almacenar sus actualizaciones {@link Update}
 * en la base de datos. También permite buscar las
 * actualizaciones almacenadas mediante un filtro {@link Filter}
 * y restricciones de búsqueda {@link Restrictions}.
 *
 * Es posible desactivar la funcionalidad de almacenado de
 * actualizaciones recibidas en tiempo real mediante el
 * archivo de configuración del sistema (ver {@link Config}).
 */
public final class Log implements UpdatesConsumer{
    private static final boolean LOG_ENABLED = Config.get().getBoolean("log.enabled");

    private final ConcurrentLinkedQueue<Update> queue;
    private final Thread thread;

    // TODO: hacer singleton en diagrama
    private static Log instance = null;
    public static Log getInstance() {
        if(Log.instance == null)
            Log.instance = new Log();
        return Log.instance;
    }

    public static void setup() {
        if(Log.instance == null)
            Log.instance = new Log();
    }

    private Log() {
        this.queue = new ConcurrentLinkedQueue<>();
        this.thread = new Thread(() -> {
            try {
                LogUpdate.create(this.queue);
            } catch (DBException e) {
                e.printStackTrace();
            }
        });

        if(LOG_ENABLED) {
            try {
                DBConnection.getInstance().withHandle((DBConnection.HandleCallback<Void>) handle -> {
                    Iterator<Category> iterator = handle.attach(CategoryActions.class).getAllCategories();

                    if (iterator == null)
                        return null;

                    while (iterator.hasNext())
                        RealTimeStreamsManager.getInstance().subscribe(iterator.next(), this);

                    return null;
                });
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: log
                // No se pudo inicializar el log
                System.exit(1);
            }
        }
    }

    public void subscribeToCategoryUpdates(Category category) throws DBException, UserNotLoggedInToProviderException {
        if(!LOG_ENABLED) return;

        RealTimeStreamsManager.getInstance().subscribe(category, this);

    }

    // TODO: quitar runThread del diagrama

    @Override
    public void handleUpdate(Update update) {
        this.queue.add(update);

        if(!this.thread.isAlive())
            this.thread.run();
    }

    public Collection<Update> getUpdates(Filter filter, Restrictions restrictions) throws Exception {
        if(filter == null) return null;

        //language=MySQL
        LogSQLQuery query = new LogSQLQuery(
                "SELECT U.ID FROM `update` U INNER JOIN update_attribute A ON U.ID = A.updateID"
        );

        query.addReplacement("#ATTRS_TABLE#", "A");
        filter.injectSQLConditions(query);
        this.injectRestrictionsConditions(query, restrictions);

        Collection<Long> ids = DBConnection.getInstance().withHandle(query::executeQuery);

        if(ids == null) return null;

        Collection<Update> updates = new ArrayList<>(ids.size());

        for (Long id : ids) {
            if(id == null) continue;

            Update update = LogUpdate.create(id);
            if(update != null)
                updates.add(update);
        }

        return updates;
    }

    private void injectRestrictionsConditions(LogSQLQuery query, Restrictions restrictions) throws SealedException {
        if(restrictions == null) return;

        /* Resultados a partir de un ID */
        if(restrictions.getFromID() != null) {
            query.appendToCondition("AND", "(U.ID >= ?)");
            query.addLongParam(restrictions.getFromID());
        }

        /* Resultados entre dos fechas */
        if(restrictions.getStartTimestamp() != null && restrictions.getEndTimestamp() != null) {
            query.appendToCondition("AND", "(U.timestamp BETWEEN ? AND ?)");
            query.addTimestampParam(restrictions.getStartTimestamp());
            query.addTimestampParam(restrictions.getEndTimestamp());

        /* Resultados a partir de una fecha */
        } else if(restrictions.getStartTimestamp() != null) {
            query.appendToCondition("AND", "(U.timestamp >= ?)");
            query.addTimestampParam(restrictions.getStartTimestamp());

        /* Resultados hasta una fecha */
        } else if(restrictions.getEndTimestamp() != null) {
            query.appendToCondition("AND", "(U.timestamp <= ?)");
            query.addTimestampParam(restrictions.getEndTimestamp());
        }

        /* Resultados ordenados por fecha */
        if(restrictions.getOrder() != null)
            query.appendToPostCondition("ORDER BY U.timestamp " + (restrictions.getOrder() ? "ASC" : "DESC"));

        /* Cantidad de resultados limitada */
        if(restrictions.getLimit() != null)
            query.appendToPostCondition("LIMIT " + restrictions.getLimit());
    }
}

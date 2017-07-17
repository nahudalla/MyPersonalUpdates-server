package com.mypersonalupdates.filters;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.db.DBException;

public class AndFilter extends CompoundFilter{

   private AndFilter(Integer ID, Filter filter1, Filter filter2) {
       super(ID, filter1, filter2);
   }

   public static AndFilter create(Filter filter1, Filter filter2) throws DBException {
       Integer filterID = null;

       try {
           filterID = CompoundFilter.create(filter1, filter2, "AndFilter");
       } catch (DBException e) {
           try {
               throw new DBException(e);
           } catch (DBException e1) {
               throw new DBException(e);
           }
       }

       return filterID == null ? null : new AndFilter(filterID, filter1, filter2);
   }

    @Override
    public boolean test(Update update) {
        return this.filter1.test(update) && this.filter2.test(update);
    }
}

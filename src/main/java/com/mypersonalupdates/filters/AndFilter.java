package com.mypersonalupdates.filters;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.Update;

public class AndFilter extends CompoundFilter{

   private AndFilter(Integer ID, Filter filter1, Filter filter2) {
       super(ID, filter1, filter2);
   }

   public AndFilter create(Filter filter1, Filter filter2) {
       //TODO: Hace con la base
       return null;
   }

    @Override
    public boolean test(Update update) {
        return this.filter1.test(update) && this.filter2.test(update);
    }
}

package com.mypersonalupdates.realtime;

import com.mypersonalupdates.filters.Filter;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.exceptions.UserNotLoggedInToProviderException;
import com.mypersonalupdates.users.Category;
import com.mypersonalupdates.users.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase contiene los datos de un consumidor de
 * actualizaciones en tiempo real. Estos datos están
 * asociados a un usuario {@link User}, una categoría
 * sobre la que se está suscrito {@link Category} y
 * el consumidor que se encarga de procesar las
 * actualizaciones recibidas {@link UpdatesConsumer}.
 */
/*package*/ final class RealTimeListener {
    private final UpdatesConsumer consumer;
    private final Category category;
    private final User user;
    private final Map<UpdatesProvider, Long> subscriptionIDs = new HashMap<>();

    /*package*/ RealTimeListener(Category category, UpdatesConsumer consumer) {
        this.consumer = consumer;
        this.user = category.getUser();
        this.category = category;
    }

    /*package*/ Category getCategory() {
        return category;
    }

    /*package*/ void renewSubscription() throws DBException, UserNotLoggedInToProviderException {
        this.unsubscribe();
        this.subscribe();
    }

    /*package*/ void subscribe() throws DBException, UserNotLoggedInToProviderException {
        Filter filter = this.category.getFilter();
        for (UpdatesProvider provider : category.getProviders()) {
            Long id = provider.subscribe(this.user, filter, this.consumer);
            this.subscriptionIDs.put(provider, id);
        }
    }

    /*package*/ void unsubscribe() {
        for (Map.Entry<UpdatesProvider, Long> entry : this.subscriptionIDs.entrySet()) {
            entry.getKey().unsubscribe(this.user, entry.getValue());
        }

        this.subscriptionIDs.clear();
    }
}

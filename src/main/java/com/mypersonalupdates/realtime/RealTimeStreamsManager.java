package com.mypersonalupdates.realtime;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.exceptions.UserNotLoggedInToProviderException;
import com.mypersonalupdates.users.Category;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Esta clase singleton se encarga de administrar
 * todas los streams de actualizaciones en tiempo
 * real que hay en el sistema. Los consumidores
 * le solicidan suscribirse a las actualizaciones
 * de una categoría y la instancia de esta clase
 * se encarga de realizar la suscripción. Cuando
 * el filtro de actualizaciones de una categoría
 * cambia, esta le avisa a la instancia de esta
 * clase para renovar la suscripción de los
 * consumidores con los proveedores. De manera
 * similar se procede cuando una categoría es
 * eliminada, para desuscribir a todos los
 * consumidores de esa categoría.
 *
 * Es posible suspender y volver a reanudar todas
 * las actualizaciones en tiempo real de una
 * categoría específica. Esto es útil por ejemplo
 * cuando se realizarán muchos cambios en una
 * categoría que provocaría que las suscripciones
 * se renueven múltiples veces hasta finalizar los
 * cambios.
 * */
public final class RealTimeStreamsManager {
    private static final RealTimeStreamsManager instance = new RealTimeStreamsManager();
    private RealTimeStreamsManager() {}
    public static RealTimeStreamsManager getInstance() {
        return RealTimeStreamsManager.instance;
    }

    private final Map<Category, Set<RealTimeListener>> listenersByCategory = new HashMap<>();
    private final Map<Long, RealTimeListener> listenersByID = new HashMap<>();

    private Long nextSubscriberID = 0L;

    private final Set<Category> suspendedCategories = new HashSet<>();

    public void resumeCategoryUpdates(Category category) throws DBException, UserNotLoggedInToProviderException {
        synchronized (this.listenersByID) {
            if(!this.suspendedCategories.contains(category)) return;

            Set<RealTimeListener> listeners = this.listenersByCategory.get(category);

            if(listeners != null) {
                for (RealTimeListener listener : listeners)
                    listener.subscribe();
            }

            this.suspendedCategories.remove(category);
        }
    }

    public void suspendCategoryUpdates(Category category) {
        synchronized (this.listenersByID) {
            if(suspendedCategories.contains(category)) return;

            Set<RealTimeListener> listeners = this.listenersByCategory.get(category);

            if(listeners != null) {
                for (RealTimeListener listener : listeners)
                    listener.unsubscribe();
            }

            this.suspendedCategories.add(category);
        }
    }

    public Long subscribe(Category category, UpdatesConsumer consumer) throws DBException, UserNotLoggedInToProviderException {
        if(category == null || consumer == null)
            throw new NullPointerException();

        synchronized (this.listenersByID) {
            RealTimeListener listener = new RealTimeListener(category, consumer);
            Long id = this.nextSubscriberID++;

            Set<RealTimeListener> listeners = this.listenersByCategory.computeIfAbsent(category, _p -> new HashSet<>());
            listeners.add(listener);

            this.listenersByID.put(id, listener);



            if(!this.suspendedCategories.contains(category))
                listener.subscribe();



            System.out.println("Client subscribed to stream: "+id);

            return id;
        }
    }

    public boolean unsubscribe(Long subscriptionID) {
        if(subscriptionID == null)
            throw new NullPointerException();

        synchronized (this.listenersByID) {
            RealTimeListener listener = this.listenersByID.remove(subscriptionID);

            if(listener == null) return false;

            listener.unsubscribe();

            Set<RealTimeListener> listeners = this.listenersByCategory.get(listener.getCategory());

            if(listeners == null)
                throw new IllegalStateException();

            listeners.remove(listener);

            if(listeners.size() == 0)
                this.listenersByCategory.remove(listener.getCategory());

            System.out.println("Client unsubscribed from stream: "+subscriptionID);

            return true;
        }
    }

    public void categoryUpdated(Category category) throws DBException, UserNotLoggedInToProviderException {
        if (category == null)
            throw new NullPointerException();

        synchronized (this.listenersByID) {
            if(this.suspendedCategories.contains(category)) return;

            Set<RealTimeListener> listeners = this.listenersByCategory.get(category);

            if(listeners == null) return;

            for (RealTimeListener listener : listeners)
                listener.renewSubscription();
        }
    }

    public void categoryDeleted(Category category) {
        if(category == null)
            throw new NullPointerException();

        synchronized (this.listenersByID) {
            Set<RealTimeListener> listeners = this.listenersByCategory.remove(category);

            if(listeners == null) return;

            if(!this.suspendedCategories.remove(category)) {
                for (RealTimeListener listener : listeners)
                    listener.unsubscribe();
            }
        }
    }
}

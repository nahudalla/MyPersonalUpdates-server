package com.mypersonalupdates;

import com.mypersonalupdates.providers.UpdatesProvider;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

/**
 * Esta clase se encarga de almacenar los proveedores
 * disponibles en el sistema.
 */
public class UpdatesProvidersManager {
    private final Map<Long, UpdatesProvider> providers = new Hashtable<>();

    private static final UpdatesProvidersManager ourInstance = new UpdatesProvidersManager();
    public static UpdatesProvidersManager getInstance() {
        return ourInstance;
    }

    private UpdatesProvidersManager() {}

    public Collection<UpdatesProvider> getProviders() {
        synchronized (this.providers) {
            return this.providers.values();
        }
    }

    public UpdatesProvider getProvider(Long ID){
        synchronized (this.providers) {
            return this.providers.get(ID);
        }
    }

    public void addProvider(UpdatesProvider updatesProvider) {
        synchronized (this.providers) {
            this.providers.put(updatesProvider.getID(), updatesProvider);
        }
    }
}

package com.mypersonalupdates;

import com.mypersonalupdates.providers.UpdatesProvider;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

public class UpdatesProvidersManager {
    private final Map<Integer, UpdatesProvider> providers = new Hashtable<>();

    private static UpdatesProvidersManager ourInstance = new UpdatesProvidersManager();
    public static UpdatesProvidersManager getInstance() {
        return ourInstance;
    }
    // TODO: falta en diagrama el constructor privado
    private UpdatesProvidersManager() {}

    // TODO: actualizar signature en diagrama
    public Collection<UpdatesProvider> getProviders() {
        synchronized (this.providers) {
            return this.providers.values();
        }
    }

    public UpdatesProvider getProvider(Integer ID){
        synchronized (this.providers) {
            return this.providers.get(ID);
        }
    }

    // TODO: actualizar signature en diagrama
    public void addProvider(UpdatesProvider updatesProvider) {
        synchronized (this.providers) {
            this.providers.put(updatesProvider.getID(), updatesProvider);
        }
    }
}

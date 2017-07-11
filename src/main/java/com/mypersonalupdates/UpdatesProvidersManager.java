package com.mypersonalupdates;

import com.mypersonalupdates.providers.UpdatesProvider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UpdatesProvidersManager {
    // TODO: usar hashtable
    private Map<Integer, UpdatesProvider> providers = new HashMap<>();

    private static UpdatesProvidersManager ourInstance = new UpdatesProvidersManager();
    public static UpdatesProvidersManager getInstance() {
        return ourInstance;
    }
    // TODO: falta en diagrama el constructor privado
    private UpdatesProvidersManager() {}

    // TODO: actualizar signature en diagrama
    public Collection<UpdatesProvider> getProviders() {
        return this.providers.values();
    }

    public UpdatesProvider getProvider(Integer ID){
        return this.providers.get(ID);
    }

    // TODO: actualizar signature
    public void addProvider(UpdatesProvider updatesProvider) {
        this.providers.put(updatesProvider.getID(), updatesProvider);
    }

}

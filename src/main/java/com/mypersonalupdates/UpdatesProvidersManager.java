package com.mypersonalupdates;

import com.mypersonalupdates.providers.UpdatesProvider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UpdatesProvidersManager {
    private static UpdatesProvidersManager ourInstance = new UpdatesProvidersManager();

    public static UpdatesProvidersManager getInstance() {
        return ourInstance;
    }

    private UpdatesProvidersManager() {     }

    private Map<Integer, UpdatesProvider> providers = new HashMap<>();

    public Collection<UpdatesProvider> getProviders() {
        return this.providers.values();
    }

    public UpdatesProvider getProvider(Integer ID){
        return this.providers.get(ID);
    }

    public void addProvider(UpdatesProvider updatesProvider) {
        this.providers.put(updatesProvider.getID(), updatesProvider);
    }

}

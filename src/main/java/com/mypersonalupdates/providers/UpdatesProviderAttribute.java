package com.mypersonalupdates.providers;

public class UpdatesProviderAttribute {

    private Integer ID;
    private UpdatesProvider provider;
    private boolean multi;

    //TODO: Cambiar en el diagrama signature (dice Boolean y debería ser boolean)
    private UpdatesProviderAttribute(Integer ID, UpdatesProvider provider, boolean multi) {
        this.ID = ID;
        this.provider = provider;
        this.multi = multi;
    }

    public static UpdatesProviderAttribute create(Integer ID) {
        Integer attr = null;

        return null;
    }

    public static UpdatesProviderAttribute create(UpdatesProvider provider, String name) {
        return null;
    }

    public Integer getID() {
        return this.ID;
    }

    public UpdatesProvider getProvider() {
        return this.provider;
    }

    public String getName() {
        return null;
    }

    public String getDescription() {
        return null;
    }

    boolean isMultivalued() {
        return this.multi;
    }
}

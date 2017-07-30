package com.mypersonalupdates.webserver.responses.builders;

import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.webserver.responses.BuilderBase;

/**
 * Esta clase se encarga de crear una respuesta a una petición
 * que incluye los datos solicitados sobre una instancia de la
 * clase UpdatesProvider.
 */
public final class ProviderResponseBuilder extends BuilderBase<ProviderResponseBuilder> {
    private final UpdatesProvider provider;

    public ProviderResponseBuilder(UpdatesProvider provider) {
        super("UpdatesProvider");
        this.provider = provider;
    }

    public ProviderResponseBuilder includeID() {
        this.jsonObject.addProperty("id", this.provider.getID());
        return this;
    }

    public ProviderResponseBuilder includeName() {
        this.jsonObject.addProperty("name", this.provider.getName());
        return this;
    }

    public ProviderResponseBuilder includeDescription() {
        this.jsonObject.addProperty("description", this.provider.getDescription());
        return this;
    }

    public ProviderResponseBuilder includeActions() {
        this.addStringCollection("actions", this.provider.getActions().keySet());
        return this;
    }
}

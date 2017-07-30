package com.mypersonalupdates.webserver.responses.builders;

import com.google.gson.JsonArray;
import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.users.Category;
import com.mypersonalupdates.webserver.responses.BuilderBase;

import java.util.Collection;

/**
 * Esta clase se encarga de crear una respuesta a una petición
 * que incluye los datos solicitados sobre una instancia de la
 * clase Category.
 */
public final class CategoryResponseBuilder extends BuilderBase<CategoryResponseBuilder> {
    private final Category category;

    public CategoryResponseBuilder(Category category) {
        super("Category");
        this.category = category;
    }

    public CategoryResponseBuilder includeName() {
        this.jsonObject.addProperty("name", this.category.getName());
        return this;
    }

    public CategoryResponseBuilder includeFilter() throws DBException {
        this.jsonObject.add("filter", this.category.getFilter().toJSON());
        return this;
    }

    public CategoryResponseBuilder includeProviders() throws DBException {
        Collection<UpdatesProvider> providers = category.getProviders();
        JsonArray array = new JsonArray(providers.size());

        for (UpdatesProvider provider : providers) {
            array.add(provider.getID());
        }

        this.jsonObject.add("providers", array);

        return this;
    }
}

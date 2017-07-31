package com.mypersonalupdates.webserver.handlers.category;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mypersonalupdates.UpdatesProvidersManager;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.webserver.Request;

import java.util.HashSet;
import java.util.Set;

/**
 * Esta clase contiene métodos estáticos que son usados por
 * más de un servicio de categorías.
 */
public final class CategoryCommons {
    public static String getBodyName(Request request) {
        JsonElement bodyName = request.getBodyItem("name");

        boolean bodyOK = bodyName != null &&
                bodyName.isJsonPrimitive() &&
                bodyName.getAsJsonPrimitive().isString();

        return !bodyOK ? null : bodyName.getAsString();
    }

    public static Set<UpdatesProvider> getProviders(Request request) {
        return getProvidersFrom(request.getBodyItem("providers"));
    }

    public static Set<UpdatesProvider> getProvidersToRemove(Request request) {
        return getProvidersFrom(request.getBodyItem("providersToRemove"));
    }

    public static Set<UpdatesProvider> getProvidersToAdd(Request request) {
        return getProvidersFrom(request.getBodyItem("providersToAdd"));
    }

    private static Set<UpdatesProvider> getProvidersFrom(JsonElement JSONProviders) {
        if(JSONProviders != null && JSONProviders.isJsonArray()) {
            JsonArray array = JSONProviders.getAsJsonArray();
            final int size = array.size();

            Set<UpdatesProvider> providers = new HashSet<>(size, 1);

            for (int i = 0; i < size; i++) {
                JsonElement element = array.get(i);
                UpdatesProvider provider;

                if(     element.isJsonPrimitive() &&
                        element.getAsJsonPrimitive().isNumber() &&
                        null != (
                                provider = UpdatesProvidersManager.getInstance().getProvider(element.getAsLong())
                        )
                  ) {
                    providers.add(provider);
                } else {
                    return null;
                }
            }

            return providers;
        }

        return null;
    }
}

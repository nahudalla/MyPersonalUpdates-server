package com.mypersonalupdates.providers.reddit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mypersonalupdates.Config;
import com.mypersonalupdates.exceptions.UserNotLoggedInToProviderException;
import com.mypersonalupdates.filters.Filter;
import com.mypersonalupdates.realtime.UpdatesConsumer;

import java.time.Instant;

public class RedditUpdatesFetcher implements Runnable {
    private static final Long POLLING_WAIT_TIME = Config.get().getLong("providers.reddit.pollingWaitTime");

    private final RedditResource resource;
    private final UpdatesConsumer updatesConsumer;
    private final Filter filter;
    private boolean running = true;

    public RedditUpdatesFetcher(Filter filter, UpdatesConsumer updatesConsumer, RedditResource redditResource) {
        this.updatesConsumer = updatesConsumer;
        this.filter = filter;
        this.resource = redditResource;
    }

    @Override
    public void run() {
        String lastID = null;
        JsonObject jsonObject;
        Instant wakeUpAt;

        while (this.running) {
            try {
                jsonObject = this.resource.fetchData(lastID);
            } catch (UserNotLoggedInToProviderException e) {
                e.printStackTrace();
                return;
            }

            if (jsonObject != null) {
                JsonElement aux = jsonObject.get("children");
                if (aux != null && aux.isJsonArray()) {
                    boolean firstFound = false;
                    for (JsonElement element : aux.getAsJsonArray()) {
                        if (element != null && element.isJsonObject()) {
                            RedditUpdate update = new RedditUpdate(element.getAsJsonObject(), this.resource);

                            if (!firstFound) {
                                firstFound = true;
                                lastID = update.getIDFromProvider();
                            }

                            if (this.filter.test(update))
                                this.updatesConsumer.handleUpdate(update);
                        }
                    }
                }
            }

            wakeUpAt = Instant.now().plusMillis(POLLING_WAIT_TIME);
            do {
                try {
                    Thread.sleep(POLLING_WAIT_TIME);
                } catch (InterruptedException e) {}
            } while (!wakeUpAt.isBefore(Instant.now()));
        }
    }

    public synchronized void stop(){
        this.running = false;
    }
}

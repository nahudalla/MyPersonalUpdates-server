package com.mypersonalupdates.providers.twitter;

import com.google.common.collect.Lists;
import com.mypersonalupdates.Config;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import twitter4j.Status;

import java.time.Instant;
import java.util.Collection;

/**
 * Esta clase representa una actualización del sistema
 * {@link Update}. Encapsula una actualizacion del
 * proveedor de Twitter.com.
 */
public final class TwitterUpdate implements Update {
    private static final int TEXT_ATTR_ID = Config.get().getInt("providers.twitter.UpdateAttributesIDs.text");

    private final Status status;

    TwitterUpdate(Status status) {
        this.status = status;
    }

    @Override
    public UpdatesProvider getProvider() {
        return TwitterProvider.getInstance();
    }

    @Override
    public Instant getTimestamp() {
        return status.getCreatedAt().toInstant();
    }

    @Override
    public Collection<String> getAttributeValues(UpdatesProviderAttribute attr) {
        if (attr.getAttrID() == TwitterUpdate.TEXT_ATTR_ID) {
            return Lists.newArrayList(this.status.getText());
        }

        return null;
    }

    public String getIDFromProvider() {
        return String.valueOf(this.status.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TwitterUpdate)) return false;

        TwitterUpdate that = (TwitterUpdate) o;

        return status.equals(that.status);
    }

    @Override
    public int hashCode() {
        return status.hashCode();
    }
}

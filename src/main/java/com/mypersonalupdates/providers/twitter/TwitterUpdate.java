package com.mypersonalupdates.providers.twitter;

import com.google.common.collect.Lists;
import com.mypersonalupdates.Config;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;
import twitter4j.Status;

import java.util.Collection;
import java.util.Date;

public class TwitterUpdate implements Update {
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
    public Date getTimestamp() {
        return status.getCreatedAt();
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
}

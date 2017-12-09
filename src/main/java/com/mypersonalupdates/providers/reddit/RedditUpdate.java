package com.mypersonalupdates.providers.reddit;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mypersonalupdates.Config;
import com.mypersonalupdates.Update;
import com.mypersonalupdates.providers.UpdatesProvider;
import com.mypersonalupdates.providers.UpdatesProviderAttribute;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RedditUpdate implements Update{

    private static final Long SOURCE_USERNAME_ATTR_ID = Config.get().getLong("providers.reddit.UpdateAttributesIDs.source_username");
    private static final Long SOURCE_SUBREDDITNAME_ATTR_ID = Config.get().getLong("providers.reddit.UpdateAttributesIDs.source_subredditname");
    private static final Long TITLE_ATTR_ID = Config.get().getLong("providers.reddit.UpdateAttributesIDs.title");
    private static final Long KIND_ATTR_ID = Config.get().getLong("providers.reddit.UpdateAttributesIDs.kind");
    private static final Long PREVIEW_IMAGES_ATTR_ID = Config.get().getLong("providers.reddit.UpdateAttributesIDs.preview_images");
    private static final Long BODY_ATTR_ID = Config.get().getLong("providers.reddit.UpdateAttributesIDs.body");
    private static final Long AUTHOR_ATTR_ID = Config.get().getLong("providers.reddit.UpdateAttributesIDs.author");

    private JsonObject data = null;
    private String kind = null;
    private RedditResource source;

    public RedditUpdate (JsonObject jsonObject, RedditResource redditResource){
        try {
        this.kind = jsonObject.get("kind").getAsString();
        } catch (Exception e) {}
        try {
            this.data = jsonObject.get("data").getAsJsonObject();
        } catch (Exception e) {}
        this.source = redditResource;
    };

    @Override
    public Long getID() {
        return null;
    }

    @Override
    public UpdatesProvider getProvider() {
        return RedditProvider.getInstance();
    }

    @Override
    public Instant getTimestamp() {

        Long seg;

        try {
            seg = this.data.get("created_utc").getAsLong();
        } catch (Exception e){
            return null;
        }

        return Instant.ofEpochMilli(seg * 1000);
    }

    @Override
    public Collection<String> getAttributeValues(UpdatesProviderAttribute attr) {
        JsonElement valueAttr = null;

        if (attr.getAttrID().equals(TITLE_ATTR_ID))
            valueAttr = this.data.get("title");

        else if (attr.getAttrID().equals(KIND_ATTR_ID))
            return Lists.newArrayList(this.kind);

        else if (attr.getAttrID().equals(PREVIEW_IMAGES_ATTR_ID))
            return this.getPreviewImages();

        else if (attr.getAttrID().equals(BODY_ATTR_ID)) {
            if(this.kind.equals("t1"))
                valueAttr = this.data.get("body");
            else
                valueAttr = this.data.get("selftext");
        }else if (attr.getAttrID().equals(AUTHOR_ATTR_ID))
            valueAttr = this.data.get("author");

        else if (attr.getAttrID().equals(SOURCE_USERNAME_ATTR_ID) && this.source.isUserResource())
            return Lists.newArrayList(this.source.getResourceName());

        else if (attr.getAttrID().equals(SOURCE_SUBREDDITNAME_ATTR_ID) && this.source.isSubredditResource())
            return Lists.newArrayList(this.source.getResourceName());

        try {
            return valueAttr != null ? Lists.newArrayList(valueAttr.getAsString()) : null;
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public String getIDFromProvider() {
        try {
            return this.data.get("name").getAsString();
        } catch (Exception e){
            return null;
        }
    }

    private Collection<String> getPreviewImages(){
        JsonObject object;
        List<String> preview_images = new ArrayList<>();

        JsonElement aux = this.data.get("preview");
        if (aux == null || !aux.isJsonObject())
            return null;

        object = this.data.getAsJsonObject("preview");

        aux = object.get("images");
        if (aux != null && aux.isJsonArray()){
            JsonArray array = aux.getAsJsonArray();

            for (JsonElement element : array){
                if (!element.isJsonObject())
                    continue;

                JsonObject obj = element.getAsJsonObject();

                aux = obj.get("source");
                if (aux == null || !aux.isJsonObject())
                    continue;

                JsonElement url = aux.getAsJsonObject().get("url");

                try{
                    preview_images.add(url.getAsString());
                } catch (Exception e){}
            }
        }

        return preview_images;
    }
}

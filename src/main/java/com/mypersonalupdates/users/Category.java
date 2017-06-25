package com.mypersonalupdates.users;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.providers.UpdatesProvider;

import java.util.ArrayList;
import java.util.List;

public class Category {

    //TODO : Modificarlo para que los datos esten en la base

    private String name;
    private Filter filter;
    private List<UpdatesProvider> updatesProviders = new ArrayList<>();
    private Integer ID = null;

    private Category() { }

    private Category(Integer ID) {
        this.ID = ID;
    }

    public Category create(String name, Filter filter){
        return null;
        //TODO: Implementarlo con la base
    }

    public Category fromID(Integer ID) {
        return null;

        //TODO: Implementarlo con la base
    }

    public Integer getID() {
        return this.ID;
    }

    public String getName() {
        return  this.name;
    }

    public List<UpdatesProvider> getProviders(){
        return this.updatesProviders;
    }

    public boolean remove() {
        return true;
        //TODO: Implementarlo con la base
    }

    public Filter getFilter() {
        return this.filter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  void setFilter(Filter filter) {
        this.filter = filter;
    }

    public void addProvider(UpdatesProvider provider) {
        this.updatesProviders.add(provider);
    }

    public void removeProvider(UpdatesProvider provider) {
        this.updatesProviders.remove(provider);
    }
}

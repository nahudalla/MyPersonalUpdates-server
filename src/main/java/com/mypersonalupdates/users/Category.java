package com.mypersonalupdates.users;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.providers.UpdatesProvider;

import java.util.ArrayList;
import java.util.List;

public class Category {

    //TODO : Modificarlo para que los datos esten en la base

    // TODO: actualizar atributos
    private String name;
    private Filter filter;
    private List<UpdatesProvider> updatesProviders = new ArrayList<>();
    private Integer ID = null;

    // TODO: actualizar signature
    private Category(Integer ID) {
        this.ID = ID;
    }

    // TODO: arreglar create(s)
    public Category create(String name, Filter filter){
        return null;
        //TODO: Implementarlo con la base
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

    // TODO: actualizar signature
    public void setName(String name) {
        this.name = name;
    }

    // TODO: actualizar signature
    public  void setFilter(Filter filter) {
        this.filter = filter;
    }

    // TODO: actualizar signature
    public void addProvider(UpdatesProvider provider) {
        this.updatesProviders.add(provider);
    }

    // TODO: actualizar signature
    public void removeProvider(UpdatesProvider provider) {
        this.updatesProviders.remove(provider);
    }

    // TODO: implementar getName
}

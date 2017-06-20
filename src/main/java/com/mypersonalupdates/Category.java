package com.mypersonalupdates;

import java.util.ArrayList;

public class Category {

    //TODO : Modificarlo para que los datos esten en la base

    private String name;
    private Filter filter;
    private ArrayList<UpdatesProvider> updatesProviders = new ArrayList<>();
    private Integer ID = null;

    private Category() { }

    private Category(Integer ID) {
        this.ID = ID;
    }

    public void create(String name, Filter filter){
        //Le puse que retorna void para que no tire error, pero va Category
    }

    public void fromID(Integer ID) {
        //Le puse que retorna void para que no tire error, pero va Category
    }

    public Integer getID() {
        return this.ID;
    }

    public String getName() {
        return  this.name;
    }

    public ArrayList<UpdatesProvider> getProviders(){
        return this.updatesProviders;
    }

    public void remove() {
        //Le puse que retorna void para que no tire error, pero va Boolean
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

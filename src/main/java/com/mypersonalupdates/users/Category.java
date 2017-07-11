package com.mypersonalupdates.users;

import com.mypersonalupdates.Filter;
import com.mypersonalupdates.providers.UpdatesProvider;

import java.util.Collection;

public class Category {

    //TODO : Modificarlo para que los datos esten en la base

    private String name;
    private User user;

    private Category(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public static Category create(User user, String name, Filter filter) {
        System.err.println("Category.create no implementado.");
        return null;
    }

    public static Category create(User user, String name) {
        System.err.println("Category.create no implementado.");
        return null;
    }

    public String getName() {
        return  this.name;
    }

    public Collection<UpdatesProvider> getProviders(){
        System.err.println("Category.getProviders no implementado.");
        return null;
        // TODO: implementar con la base
    }

    public boolean remove() {
        return true;
        //TODO: Implementarlo con la base
    }

    public Filter getFilter() {
        System.err.println("Category.getFilter no implementado.");
        return null;
        // TODO: sacar de la base
    }

    public boolean setName(String name) {
        this.name = name;
        //TODO: actualizar con la base
        System.err.println("Category.setName no implementado.");
        return false;
    }

    public boolean setFilter(Filter filter) {
        //TODO: actualizar con la base
        System.err.println("Category.setFilter no implementado.");
        return false;
    }

    public boolean addProvider(UpdatesProvider provider) {
        //TODO: actualizar con la base
        System.err.println("Category.addProvider no implementado.");
        return false;
    }

    public boolean removeProvider(UpdatesProvider provider) {
        //TODO: actualizar con la base
        System.err.println("Category.removeProvider no implementado.");
        return false;
    }
}

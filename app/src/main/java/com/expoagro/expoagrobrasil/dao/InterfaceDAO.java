package com.expoagro.expoagrobrasil.dao;

/**
 * Created by Fabricio on 6/17/2017.
 */

public interface InterfaceDAO<T> {
    public void save(T object);
    public void update(T object);
    public void delete(String id);
}

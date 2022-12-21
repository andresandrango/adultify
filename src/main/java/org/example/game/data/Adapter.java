package org.example.game.data;

import java.util.List;

public interface Adapter<T> {

    // TODO set pagination
    List<T> list();

    T get(String id);

    T create(String name);

    T create(T obj);

    boolean delete(String id);
}

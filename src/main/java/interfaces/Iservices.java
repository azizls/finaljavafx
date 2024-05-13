package interfaces;

import entities.reactions;

import java.util.List;

public interface Iservices<T> {



    void addEntity(T t);
    void updateEntity(T t);
    void deleteEntity(T t);
    List<T> getAllData();

    void update(reactions r);

    void delete(reactions r);

    List<reactions> readAll();

    reactions readById(int id);

    reactions get_annonce(int id);

    void insert(reactions t);
}


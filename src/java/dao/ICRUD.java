package dao;

import java.util.List;

public interface ICRUD<T> {
    boolean create(T obj);
    List<T> readAll();
    T readById(int id);
    boolean update(T obj);
    boolean delete(int id);
}
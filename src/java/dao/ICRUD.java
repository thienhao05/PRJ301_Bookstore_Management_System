package dao;

import java.util.List;

public interface ICRUD<T, K> {
    List<T> getAll();
    T getById(K id);
    boolean insert(T obj);
    boolean update(T obj);
    boolean delete(K id);
}
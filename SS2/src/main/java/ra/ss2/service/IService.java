package ra.ss2.service;

import java.util.List;

public interface IService<T, ID> {
    void save(T entity);
    T findById(ID id);
    T update(T entity);
    void delete(ID id);
    List<T> findAll();
}
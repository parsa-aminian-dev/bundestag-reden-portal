package de.uni.ppr.uebung3.factory;

import java.util.Collection;
import java.util.Optional;

public interface Repository<T> {

    void save(T entity);

    Optional<T> findById(String id);

    Collection<T> findAll();
}

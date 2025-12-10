package de.uni.ppr.uebung3.factory;

import de.uni.ppr.uebung3.model.Entity;

import java.util.*;

class InMemoryRepository<T extends Entity> implements Repository<T> {

    private final Map<String, T> store = new LinkedHashMap<>();

    @Override
    public void save(T entity) {
        store.put(entity.getId(), entity);
    }

    @Override
    public Optional<T> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Collection<T> findAll() {
        return Collections.unmodifiableCollection(store.values());
    }
}

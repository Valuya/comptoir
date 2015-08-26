package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.ExternalEntity;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 * @param <K>
 * @param <V>
 */
public class ExternalEntityStore<K, V> {

    private final Map<K, V> cacheMap = new HashMap<>();

    public V put(K key, V value) {
        return cacheMap.put(key, value);
    }

    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return cacheMap.computeIfAbsent(key, mappingFunction);
    }

    public void clear() {
        cacheMap.clear();
    }

    public V get(K key) {
        return Optional.of(
                cacheMap.get(key)
        ).orElseThrow(IllegalArgumentException::new);
    }

    public Set<K> keySet() {
        return cacheMap.keySet();
    }

    public Collection<V> values() {
        return cacheMap.values();
    }

    public Stream<ExternalEntity<K, V>> getBackendEntityStream() {
        return cacheMap.entrySet()
                .stream()
                .map(this::createBackendEntity);
    }

    private ExternalEntity<K, V> createBackendEntity(Map.Entry<K, V> entry) {
        K key = entry.getKey();
        V value = entry.getValue();
        return new ExternalEntity(key, value);
    }

}

package ch.mfrey.thymeleaf.extras.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.cache.ICache;
import org.thymeleaf.cache.ICacheEntryValidityChecker;
import org.thymeleaf.cache.StandardCache;

/**
 * {discussion here}
 *
 * @author msmyers
 * @version 1.0.0
 * @since 9/30/15
 */
public class MemoryCache<K, V> implements ICache<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryCache.class);

    private final StandardCache<K, V> cache;

    public MemoryCache() {
        cache = new StandardCache<K, V>("MEMORY_CACHE", true, 100, 10000, LOGGER);
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public V get(K key) {
        return cache.get(key);
    }

    public V get(K key, ICacheEntryValidityChecker<? super K, ? super V> validityChecker) {
        return cache.get(key, validityChecker);
    }

    public void clear() {
        cache.clear();;
    }

    public void clearKey(K key) {
        cache.clearKey(key);
    }
}

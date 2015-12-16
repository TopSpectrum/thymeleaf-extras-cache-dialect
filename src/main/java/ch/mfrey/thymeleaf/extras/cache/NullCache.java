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
public class NullCache<K, V> implements ICache<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NullCache.class);

    public NullCache() {

    }

    public void put(K key, V value) {

    }

    public V get(K key) {
        return null;
    }

    public V get(K key, ICacheEntryValidityChecker<? super K, ? super V> validityChecker) {
        return null;
    }

    public void clear() {

    }

    public void clearKey(K key) {

    }
}

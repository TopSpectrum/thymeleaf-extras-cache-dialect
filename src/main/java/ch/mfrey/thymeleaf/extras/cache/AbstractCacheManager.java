package ch.mfrey.thymeleaf.extras.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.thymeleaf.cache.ICacheEntryValidityChecker;
import org.thymeleaf.dom.Node;

import java.util.List;
import java.util.Locale;

/**
 * Shared base-class for our CacheManager.
 *
 * The goal of this class is extensibility. The old CacheManager implementation was sealed.
 *
 * @author msmyers
 * @version 1.0.0
 * @since 9/30/15
 */
public abstract class AbstractCacheManager implements ICacheManager {

    // Subclasses can redefine what caching they'd like to do.
    protected LoadingCache<Integer, ICacheEntryValidityChecker<String, List<Node>>> validityCheckerCache = CacheBuilder
            .newBuilder()
            .concurrencyLevel(10)
            .maximumSize(1000)
            .build(new CacheLoader<Integer, ICacheEntryValidityChecker<String, List<Node>>>() {
                @Override
                public ICacheEntryValidityChecker<String, List<Node>> load(Integer key) throws Exception {
                    // This method cannot return null and it cannot accept null.
                    // But just in-case, i'll do a null-check.
                    return new TTLCacheValidityChecker(ExpressionSupport.defaultInteger(key));
                }
            });

    protected ICacheEntryValidityChecker<String, List<Node>> getValidityChecker(Integer ttl) {
        if (ExpressionSupport.isNullOrZero(ttl)) {
            return null;
        }

        return validityCheckerCache.getUnchecked(ttl);
    }

    protected String getCacheName(final String name, final String templateMode, final Locale locale) {
        // This is a lot of string concatination... We should performance test this vs String.concat()
        return name + "_" + templateMode + "_" + locale;
    }
}

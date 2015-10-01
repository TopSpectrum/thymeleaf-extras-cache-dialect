package ch.mfrey.thymeleaf.extras.cache;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Macro;

/**
 * The interface for caching in this library.
 *
 * The goal of this interface is to abstract out as much as we can from the internals of the processors. The ultimate
 * vision is to support external/redis caching of templates.
 *
 * I really wanted to input/output strings, though upon thinking further, it seems that returning a Macro lets some
 * cache implementations do stronger object caching instead of creating a new instance of Macro every time.
 *
 * Redis caching would need to create a new Macro instance upon every request, though the "StandardCacheManager" uses
 * an internal HashMap for caching, which would not need to create a new Macro instance.
 *
 * @author msmyers
 * @version 1.0.0
 * @since 9/30/15
 */
public interface ICacheManager {

    void put(Arguments arguments, String cacheName, Macro macro);

    Macro get(Arguments arguments, String cacheName, int cacheTTLs);

    void evict(Arguments arguments, String cacheName);

}

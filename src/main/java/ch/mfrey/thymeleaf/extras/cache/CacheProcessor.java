package ch.mfrey.thymeleaf.extras.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Macro;
import org.thymeleaf.processor.ProcessorResult;

/**
 * The class responsible for replacing the element by a cached version if such
 * content is found in the cache. Resolves the attribute value to get the final
 * cache name to be able to dynamically generate the cache name if desired.
 * <p/>
 * Supports an additional "cache:ttl=''" attribute to define the time-to-live of
 * the cached content in seconds. TTL is not extended on a cache hit.
 * <p/>
 * If no cached content is found yet this processor adds an additional
 * div-element into the current element. This new element the CacheAddProcessor
 * will use as a trigger to finally generate the content string to be put into
 * the cache.
 *
 * @author Martin Frey
 */
public class CacheProcessor extends AbstractCacheProcessor {

    private static final String CACHE_TTL = "cache:ttl";

    public static final Logger LOGGER = LoggerFactory.getLogger(CacheProcessor.class);

    public CacheProcessor(StandardCacheManager cacheManager) {
        super(cacheManager, "name");
    }

    @Override
    protected ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {
        final String cacheName = ExpressionSupport.takeAndResolveArgument(arguments, element, attributeName);

        if (ExpressionSupport.isNullOrEmpty(cacheName)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Cache name not resolvable: {}", attributeName);
            }

            return ProcessorResult.OK;
        }

        /**
         * Fetch from the cache. If anything is amiss, it will return null.
         * Ex: if there is more than 1 item returned. If the wrong type is returned. etc.
         */
        Macro macro = getCachedElement(arguments, element, cacheName);

        if (null != macro) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Cache found {}. Replacing element.", cacheName);
            }

            // The object is the cached string representation.
            // We are taking out the complex template and returning a static 'pre-compiled' string.
            ExpressionSupport.replace(element, macro);
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Cache not found. Adding add processor element.");
            }

            element.addChild(ExpressionSupport.div("cache:add", cacheName));
        }

        return ProcessorResult.OK;
    }

    private Macro getCachedElement(Arguments arguments, Element element, String cacheName) {
        int cacheTTLs = ExpressionSupport.getInteger(ExpressionSupport.takeAndResolveArgument(arguments, element, CACHE_TTL), 0);

        return fetchFromCache(arguments, cacheName, cacheTTLs);
    }

    private Macro fetchFromCache(Arguments arguments, String cacheName, int cacheTTLs) {
        return cacheManager.get(arguments, cacheName, cacheTTLs);
    }

}

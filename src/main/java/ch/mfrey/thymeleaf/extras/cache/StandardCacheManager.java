package ch.mfrey.thymeleaf.extras.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.cache.ICache;
import org.thymeleaf.dom.Macro;
import org.thymeleaf.dom.Node;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class StandardCacheManager extends AbstractCacheManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(StandardCacheManager.class);

    private ICache<String, List<Node>> cache;

    public void put(final Arguments arguments, final String cacheName, final Macro macro) {
        put(arguments, cacheName, Collections.singletonList((Node) macro));
    }

    public void put(final Arguments arguments, final String cacheName, final List<Node> content) {
        put(cacheName, arguments.getTemplateResolution().getTemplateMode(), arguments.getContext().getLocale(), content);
    }

    public void put(final String cacheName, final String templateMode, final Locale locale, final List<Node> content) {
        getCache().put(getCacheName(cacheName, templateMode, locale), content);
    }

    public Macro getViaTTL(final Arguments arguments, final String cacheName, final int cacheTTLs) {
        List<Node> nodes = getViaTTL(
                cacheName,
                arguments.getTemplateResolution().getTemplateMode(),
                arguments.getContext().getLocale(),
                cacheTTLs);

        return ExpressionSupport.optCast(
                ExpressionSupport.optSingle(nodes),
                Macro.class
        );
    }

    @Override
    public Macro getViaTimestamp(Arguments arguments, String cacheName, long timestamp) {
        List<Node> nodes = getViaTimestamp(
                cacheName,
                arguments.getTemplateResolution().getTemplateMode(),
                arguments.getContext().getLocale(),
                timestamp);

        return ExpressionSupport.optCast(
                ExpressionSupport.optSingle(nodes),
                Macro.class
        );
    }

    public List<Node> getViaTTL(final String cacheName, final String templateMode, final Locale locale, final Integer cacheTTLs) {
        // TODO: Object pooling here with validity checker.
        return getCache().get(getCacheName(cacheName, templateMode, locale), getValidityCheckerViaTTL(cacheTTLs));
    }

    public List<Node> getViaTimestamp(final String cacheName, final String templateMode, final Locale locale, final Long timestamp) {
        // TODO: Object pooling here with validity checker.
        return getCache().get(getCacheName(cacheName, templateMode, locale), getValidityCheckerViaTimestamp(timestamp));
    }

    public void evict(final Arguments arguments, final String cacheName) {
        evict(cacheName, arguments.getTemplateResolution().getTemplateMode(), arguments.getContext().getLocale());
    }

    public void evict(final String cacheName, final String templateMode, final Locale locale) {
        getCache().clearKey(getCacheName(cacheName, templateMode, locale));
    }

    public void setCache(ICache<String, List<Node>> cache) {
        this.cache = cache;
    }

    private ICache<String, List<Node>> getCache() {
        return this.cache;
    }
}

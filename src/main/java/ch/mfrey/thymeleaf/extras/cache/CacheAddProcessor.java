package ch.mfrey.thymeleaf.extras.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Macro;
import org.thymeleaf.dom.NestableNode;
import org.thymeleaf.processor.ProcessorResult;

public class CacheAddProcessor extends AbstractCacheProcessor {

    public static final Logger LOGGER = LoggerFactory.getLogger(CacheAddProcessor.class);

    public CacheAddProcessor(ICacheManager cacheManager) {
        super(cacheManager, "add");
    }

    @Override
    protected ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {
        final String cacheName = ExpressionSupport.takeAttribute(element, attributeName);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Caching element {}", cacheName);
        }

        final NestableNode parent = ExpressionSupport.takeChildReturnParent(element);

        final String fragment = ExpressionSupport.writeFragmentOrFail(arguments, parent);

        cacheManager.put(arguments, cacheName, new Macro(fragment));

        return ProcessorResult.OK;
    }
}

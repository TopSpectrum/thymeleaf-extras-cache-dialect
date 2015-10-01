package ch.mfrey.thymeleaf.extras.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;

public class CacheEvictProcessor extends AbstractCacheProcessor {

	public static final Logger LOGGER = LoggerFactory.getLogger(CacheEvictProcessor.class);

	public CacheEvictProcessor(ICacheManager cacheManager) {
		super(cacheManager, "evict");

		setPrecedence(CacheProcessor.PRECEDENCE - 1);
	}

	@Override
	protected ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {
		final String cacheName = ExpressionSupport.takeAndResolveArgument(arguments, element, attributeName);

		if (ExpressionSupport.isNullOrEmpty(cacheName)) {
			// Nothing to evict.
			return ProcessorResult.OK;
		}

		cacheManager.evict(arguments, cacheName);

		return ProcessorResult.OK;
	}

}

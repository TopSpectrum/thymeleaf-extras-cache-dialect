package ch.mfrey.thymeleaf.extras.cache;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

public class CacheDialect extends AbstractDialect {

	public static final String DIALECT_NAMESPACE = "http://www.thymeleaf.org/extras/cache";
	public static final String DIALECT_PREFIX = "cache";

	private ICacheManager cacheManager;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<IProcessor> getProcessors() {
        if (null == cacheManager) {
            throw new IllegalStateException("CacheManager cannot be null");
        }

		HashSet<IProcessor> processors = new HashSet<IProcessor>();

		processors.add(new CacheProcessor(cacheManager));
		processors.add(new CacheAddProcessor(cacheManager));
		processors.add(new CacheEvictProcessor(cacheManager));

		return processors;
	}

	public String getPrefix() {
		return DIALECT_PREFIX;
	}

	public boolean isLenient() {
		return false;
	}

	public ICacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(ICacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
}

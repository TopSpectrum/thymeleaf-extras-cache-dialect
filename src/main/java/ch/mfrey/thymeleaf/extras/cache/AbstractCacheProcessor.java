package ch.mfrey.thymeleaf.extras.cache;

import org.thymeleaf.processor.attr.AbstractAttrProcessor;

/**
 * {discussion here}
 *
 * @author msmyers
 * @version 1.0.0
 * @since 9/30/15
 */
public abstract class AbstractCacheProcessor extends AbstractAttrProcessor {

    public static final int PRECEDENCE = 10;

    protected final ICacheManager cacheManager;
    protected int precedence = PRECEDENCE;

    public AbstractCacheProcessor(ICacheManager cacheManager, String attributeName) {
        super(attributeName);

        this.cacheManager = cacheManager;
    }

    public ICacheManager getCacheManager() {
        return cacheManager;
    }

    @Override
    public int getPrecedence() {
        return precedence;
    }

    public void setPrecedence(int precedence) {
        this.precedence = precedence;
    }
}

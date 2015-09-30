package ch.mfrey.thymeleaf.extras.cache;

import org.junit.Test;
import org.thymeleaf.dom.Macro;
import org.thymeleaf.dom.Node;
import org.thymeleaf.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * {discussion here}
 *
 * @author msmyers
 * @version 1.0.0
 * @since 9/30/15
 */
public class ExpressionSupportTest {

    @Test
    public void testOptCast() throws Exception {
        assertNull(ExpressionSupport.optCast(new Object(), List.class));

        assertNotNull(ExpressionSupport.optCast(new ArrayList(), List.class));
        assertNotNull(ExpressionSupport.optCast(new ArrayList(), Collection.class));

        assertNotNull(ExpressionSupport.optCast(new Macro("", ""), Macro.class));
        assertNull(ExpressionSupport.optCast(new Text(""), Macro.class));
    }

    @Test
    public void testIsInteger() throws Exception {
        assertTrue(ExpressionSupport.isInteger("1"));
        assertTrue(ExpressionSupport.isInteger("0"));
        assertTrue(ExpressionSupport.isInteger("-1"));
        assertTrue(ExpressionSupport.isInteger(String.valueOf(Integer.MAX_VALUE)));
        assertTrue(ExpressionSupport.isInteger(String.valueOf(Integer.MIN_VALUE)));

        assertFalse(ExpressionSupport.isInteger("1a"));
        assertFalse(ExpressionSupport.isInteger("1 "));
        assertFalse(ExpressionSupport.isInteger(" 1"));
        assertFalse(ExpressionSupport.isInteger(""));
        assertFalse(ExpressionSupport.isInteger(" "));
        assertFalse(ExpressionSupport.isInteger("-"));
        assertFalse(ExpressionSupport.isInteger("a"));
        assertFalse(ExpressionSupport.isInteger(String.valueOf(Integer.MAX_VALUE) + "1"));
        assertFalse(ExpressionSupport.isInteger(String.valueOf(Integer.MIN_VALUE) + "1"));


    }
}
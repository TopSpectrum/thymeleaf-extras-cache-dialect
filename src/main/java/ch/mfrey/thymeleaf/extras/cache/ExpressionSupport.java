package ch.mfrey.thymeleaf.extras.cache;

import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Macro;
import org.thymeleaf.dom.NestableNode;
import org.thymeleaf.exceptions.ConfigurationException;
import org.thymeleaf.exceptions.TemplateOutputException;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.ITemplateModeHandler;
import org.thymeleaf.templatewriter.AbstractGeneralTemplateWriter;
import org.thymeleaf.templatewriter.ITemplateWriter;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ExpressionSupport {

	public static Object getEvaluatedAttributeValue(final Arguments arguments, final String attributeValue) {
        if (StringUtils.isEmpty(attributeValue)) {
            return null;
        }

		final Configuration configuration = arguments.getConfiguration();
		final IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);
		final IStandardExpression expression = expressionParser.parseExpression(configuration, arguments, attributeValue);

		return expression.execute(configuration, arguments);
	}

	public static String getEvaluatedAttributeValueAsString(final Arguments arguments, final String attributeValue) {
		return defaultString(toString(getEvaluatedAttributeValue(arguments, attributeValue)));
	}

	public static String defaultString(String value) {
		return defaultString(value, "");
	}

	public static String defaultStringIfEmpty(String value, String defaultValue) {
		if (isNullOrEmpty(value)) {
			return defaultValue;
		}

		return value;
	}

	public static String defaultString(String value, String defaultValue) {
		if (null == value) {
			return defaultValue;
		}

		return value;
	}

	public static String toString(Object object) {
		if (null == object) {
			return null;
		}

		return object.toString();
	}

	public static boolean isNullOrEmpty(String value) {
		return (null == value) || (value.isEmpty());
	}

	public static String takeAttribute(Element element, String attributeName) {
		String result = element.getAttributeValue(attributeName);

		{
			element.removeAttribute(attributeName);
		}

		return result;
	}

	public static boolean isInteger(String string) {
		if (isNullOrEmpty(string)) {
			return false;
		}

		// http://stackoverflow.com/questions/237159/whats-the-best-way-to-check-to-see-if-a-string-represents-an-integer-in-java
		// Difficult to read, but extremely fast "isNumeric" check.
		// Added max-length check to prevent overflow: 2147483647
		int length = string.length();

		int i = 0;
		if (string.charAt(0) == '-') {
			if (length == 1 || length > 11) {
				// Either only a - sign, or too large for an integer.
				return false;
			}
			i = 1;
		} else {
			if (length > 10) {
				// Too large for an int.
				return false;
			}
		}
		for (; i < length; i++) {
			char c = string.charAt(i);
			if (c <= '/' || c >= ':') {
				return false;
			}
		}
		return true;
	}

	/**
	 * Return the first item IF-AND-ONLY-IF it is the only item.
	 *
	 * @param list
	 * @param <T>
	 * @return
	 */
	public static <T> T optSingle(List<T> list) {
		if (null == list || list.size() != 1) {
			return null;
		}

		try {
			return list.get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public static boolean isNullOrEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static <K, V> V optCast(K object, Class<V> clazz) {
		if (null == object) {
			return null;
		}

		if (clazz.isAssignableFrom(object.getClass())) {
			return clazz.cast(object);
		}

		return null;
	}

	public static int getInteger(String string, int defaultValue) {
		if (!isInteger(string)) {
			return defaultValue;
		}

		// Should not need to catch an exception, since we already determined it is safe.
		// The isInteger is faster than relying on the NumberFormatException
		return Integer.parseInt(string);
	}

	public static Long optLong(Object object) {
		if (null == object) {
			return null;
		}

		if (object instanceof Date) {
			return ((Date)object).getTime();
		}

		if (object instanceof Long) {
			return (Long) object;
		}

		return optLong((String.valueOf(object)));
	}

	public static Long optLong(String string) {
		if (isNullOrEmpty(string)) {
			return null;
		}

		try {
			// TODO: check isNumeric() on the string

			// Set your JVM settings appropriately to ensure the Integer is cached appropriately.
			//	http://stackoverflow.com/questions/15052216/how-large-is-the-integer-cache
			// -Djava.lang.Integer.IntegerCache.high=<size>
			// -XX:AutoBoxCacheMax=<size>
			// Java 7+
			return Long.valueOf(string);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Integer optInteger(String string) {
		if (isNullOrEmpty(string)) {
			return null;
		}

		try {
			// Set your JVM settings appropriately to ensure the Integer is cached appropriately.
			//	http://stackoverflow.com/questions/15052216/how-large-is-the-integer-cache
			// -Djava.lang.Integer.IntegerCache.high=<size>
			// -XX:AutoBoxCacheMax=<size>
			// Java 7+
			return Integer.valueOf(string);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Object takeAndResolveArgumentAsObject(Arguments arguments, Element element, String attributeName) {
		final String attributeValue = ExpressionSupport.takeAttribute(element, attributeName);

		return getEvaluatedAttributeValue(arguments, attributeValue);
	}

	public static String takeAndResolveArgument(Arguments arguments, Element element, String attributeName) {
		// This attribute name will be "name" because our constructor declared this attribute was ours.
		// In the actual template, it will be prefixed with our dialect prefix (probably 'cache').
		// The resulting attribute you see in the template will be "cache:$name"
		final String attributeValue = ExpressionSupport.takeAttribute(element, attributeName);
		// The raw 'attributeValue' might be a complex Thymeleaf expression.
		// Resolve it.
        return ExpressionSupport.getEvaluatedAttributeValueAsString(arguments, attributeValue);
	}

	public static void replace(Element element, Macro macro) {
		element.clearChildren();
		element.getParent().insertAfter(element, macro);
		element.getParent().removeChild(element);
	}

	public static Element div(String attributeName, String attributeValue) {
		Element element = new Element("div");

		element.setAttribute(attributeName, attributeValue);

		return element;
	}

	public static boolean isNullOrZero(Integer i) {
		return null == i || i.equals(0);
	}

	public static boolean isNullOrZero(Long i) {
		return null == i || i.equals((long)0);
	}

	public static ITemplateWriter getTemplateWriter(Arguments arguments) {
		final String templateMode = arguments.getTemplateResolution().getTemplateMode();
		final ITemplateModeHandler templateModeHandler = arguments.getConfiguration().getTemplateModeHandler(templateMode);

		return templateModeHandler.getTemplateWriter();
	}

	public static NestableNode takeChildReturnParent(Element element) {
		NestableNode parent = element.getParent();

		{
			parent.removeChild(element);
		}

		return parent;
	}


	public static String writeFragmentOrFail(Arguments arguments, NestableNode parent) {
		final AbstractGeneralTemplateWriter templateWriter = getWriterOrFail(arguments);
		final StringWriter writer = new StringWriter();

		{
			try {
				templateWriter.writeNode(arguments, writer, parent);
			} catch (IOException e) {
				throw new TemplateOutputException("Error during creation of output", e);
			}
		}

		return writer.toString();
	}

	public static AbstractGeneralTemplateWriter getWriterOrFail(Arguments arguments) {
		final AbstractGeneralTemplateWriter templateWriter = ExpressionSupport.optCast(ExpressionSupport.getTemplateWriter(arguments), AbstractGeneralTemplateWriter.class);

		if (templateWriter == null) {
			throw new ConfigurationException("No template writer defined, or is of wrong type.");
		}

		return templateWriter;
	}

	public static Integer defaultInteger(Integer key) {
		if (null == key) {
			return 0;
		}

		return key;
	}
}

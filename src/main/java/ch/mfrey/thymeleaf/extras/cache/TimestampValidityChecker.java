package ch.mfrey.thymeleaf.extras.cache;

import org.thymeleaf.cache.ICacheEntryValidityChecker;
import org.thymeleaf.dom.Node;

import java.util.List;

public class TimestampValidityChecker implements ICacheEntryValidityChecker<String, List<Node>> {

	private static final long serialVersionUID = 1571820788337791209L;

	private final long timestamp;

	public TimestampValidityChecker(long timestamp) {
		super();
		this.timestamp = timestamp;
	}

	public boolean checkIsValueStillValid(String key, List<Node> value, long entryCreationTimestamp) {
		return this.timestamp < entryCreationTimestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}
}
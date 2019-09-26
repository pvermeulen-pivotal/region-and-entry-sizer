package datatx.geode.size.function;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class IndexSizes implements Serializable {

	private static final long serialVersionUID = 4469970659685632471L;

	private String regionName;

	private final Map<String, Long> indexSizes;

	public IndexSizes(String regionName) {
		this.regionName = regionName;
		this.indexSizes = new HashMap<String, Long>();
	}

	public void addIndexSize(String indexName, Long indexSize) {
		this.indexSizes.put(indexName, indexSize);
	}

	public Map<String, Long> getIndexSizes() {
		return this.indexSizes;
	}

	public String toString() {
		return new StringBuilder().append("regionName=").append(regionName).append("; indexSizes=")
				.append(this.indexSizes).toString();
	}
}
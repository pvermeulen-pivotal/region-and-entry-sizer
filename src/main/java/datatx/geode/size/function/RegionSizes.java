package datatx.geode.size.function;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RegionSizes implements Serializable {

	private static final long serialVersionUID = 5129610977909577953L;

	private String regionName;
	
	private final Map<String, Long> regionSizes;

	public RegionSizes(String regionName) {
		this.regionName = regionName;
		this.regionSizes = new HashMap<String, Long>();
	}

	public void addRegionSize(String regionName, Long regionSize) {
		this.regionSizes.put(regionName, regionSize);
	}

	public Map<String, Long> getRegionSizes() {
		return this.regionSizes;
	}

	public String toString() {
		return new StringBuilder().append("regionName=").append(regionName).append("; regionSizes=")
				.append(this.regionSizes).toString();
	}
}
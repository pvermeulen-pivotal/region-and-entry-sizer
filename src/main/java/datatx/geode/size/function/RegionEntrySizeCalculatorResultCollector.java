package datatx.geode.size.function;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.geode.cache.execute.FunctionException;
import org.apache.geode.cache.execute.ResultCollector;
import org.apache.geode.distributed.DistributedMember;

public class RegionEntrySizeCalculatorResultCollector
		implements ResultCollector<RegionEntrySizes, Map<DistributedMember, RegionEntrySizes>> {

	private Map<DistributedMember, RegionEntrySizes> regionEntrySizes = new ConcurrentHashMap<DistributedMember, RegionEntrySizes>();

	public void addResult(DistributedMember member, RegionEntrySizes regionEntrySizes) {
		if (regionEntrySizes != null) {
			this.regionEntrySizes.put(member, regionEntrySizes);
		}
	}

	public Map<DistributedMember, RegionEntrySizes> getResult() throws FunctionException {
		return this.regionEntrySizes;
	}

	public void endResults() {
	}

	public Map<DistributedMember, RegionEntrySizes> getResult(long timeout, TimeUnit unit) throws FunctionException {
		return this.regionEntrySizes;
	}

	public void clearResults() {
		this.regionEntrySizes.clear();
	}
}

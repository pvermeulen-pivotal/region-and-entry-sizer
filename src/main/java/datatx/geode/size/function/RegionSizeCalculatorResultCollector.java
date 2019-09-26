package datatx.geode.size.function;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.geode.cache.execute.FunctionException;
import org.apache.geode.cache.execute.ResultCollector;
import org.apache.geode.distributed.DistributedMember;

public class RegionSizeCalculatorResultCollector
		implements ResultCollector<RegionSizes, Map<DistributedMember, RegionSizes>> {

	private Map<DistributedMember, RegionSizes> regionSizes = new ConcurrentHashMap<DistributedMember, RegionSizes>();

	public void addResult(DistributedMember member, RegionSizes regionSizes) {
		if (regionSizes != null) {
			this.regionSizes.put(member, regionSizes);
		}
	}

	public Map<DistributedMember, RegionSizes> getResult() throws FunctionException {
		return this.regionSizes;
	}

	public void endResults() {
	}

	public Map<DistributedMember, RegionSizes> getResult(long timeout, TimeUnit unit) throws FunctionException {
		return this.regionSizes;
	}

	public void clearResults() {
		this.regionSizes.clear();
	}
}

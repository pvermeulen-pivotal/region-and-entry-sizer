package datatx.geode.size.function;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.geode.cache.execute.FunctionException;
import org.apache.geode.cache.execute.ResultCollector;
import org.apache.geode.distributed.DistributedMember;

public class IndexSizeCalculatorResultCollector
		implements ResultCollector<IndexSizes, Map<DistributedMember, IndexSizes>> {

	private Map<DistributedMember, IndexSizes> indexSizes = new ConcurrentHashMap<DistributedMember, IndexSizes>();

	public void addResult(DistributedMember member, IndexSizes indexSizes) {
		if (indexSizes != null) {
			this.indexSizes.put(member, indexSizes);
		}
	}

	public Map<DistributedMember, IndexSizes> getResult() throws FunctionException {
		return this.indexSizes;
	}

	public void endResults() {
	}

	public Map<DistributedMember, IndexSizes> getResult(long timeout, TimeUnit unit) throws FunctionException {
		return this.indexSizes;
	}

	public void clearResults() {
		this.indexSizes.clear();
	}
}

package datatx.geode.size.function;

import java.util.Properties;

import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.geode.cache.query.Index;
import org.apache.geode.cache.query.QueryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("rawtypes")
public class IndexSizeCalculator implements Function, Declarable {

	private static final long serialVersionUID = -6668191712387325598L;

	private Logger LOG = LogManager.getLogger(IndexSizeCalculator.class);

	private final QueryService queryService;

	public IndexSizeCalculator() {
		this.queryService = CacheFactory.getAnyInstance().getQueryService();
	}

	@SuppressWarnings("unchecked")
	public void execute(FunctionContext context) {
		boolean histogram = false;
		String[] args = (String[]) context.getArguments();
		if (args != null && args.length > 0)
			histogram = Boolean.valueOf(args[0]);

		// Calculate sizes of indexes
		long indexSize = 0;
		RegionFunctionContext rfc = (RegionFunctionContext) context;
		IndexSizes indexSizes = new IndexSizes(rfc.getDataSet().getName());
		for (Index index : this.queryService.getIndexes(rfc.getDataSet())) {
			indexSize = ObjectSizer.calculateSize(index, false);
			indexSizes.addIndexSize(index.getName(), indexSize);
		}

		if (histogram) {
			for (Index index : this.queryService.getIndexes(rfc.getDataSet())) {
				indexSize = ObjectSizer.calculateSize(index, histogram);
			}
		}
		// Return result
		LOG.info(indexSizes);
		context.getResultSender().lastResult(indexSizes);
	}

	public String getId() {
		return getClass().getSimpleName();
	}

	public boolean optimizeForWrite() {
		return true;
	}

	public boolean hasResult() {
		return true;
	}

	public boolean isHA() {
		return true;
	}

	public void init(Properties properties) {
	}
}
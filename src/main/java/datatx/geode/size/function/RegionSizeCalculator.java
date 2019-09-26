package datatx.geode.size.function;

import java.util.Properties;

import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("rawtypes")
public class RegionSizeCalculator implements Function, Declarable {

	private static final long serialVersionUID = 3519398685016890144L;

	private Logger LOG = LogManager.getLogger(RegionSizeCalculator.class);

	@SuppressWarnings("unchecked")
	public void execute(FunctionContext context) {
		boolean histogram = false;
		String[] args = (String[]) context.getArguments();
		if (args != null && args.length > 0)
			histogram = Boolean.valueOf(args[0]);

		// Get the region
		RegionFunctionContext rfc = (RegionFunctionContext) context;
		Region region = rfc.getDataSet();

		// Calculate size
		RegionSizes regionSizes = new RegionSizes(region.getName());
		regionSizes.addRegionSize(region.getName(), ObjectSizer.calculateSize(region, histogram));

		// Return result
		LOG.info(regionSizes);
		context.getResultSender().lastResult(regionSizes);
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
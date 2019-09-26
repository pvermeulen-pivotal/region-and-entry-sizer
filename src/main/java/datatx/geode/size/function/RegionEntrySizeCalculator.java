package datatx.geode.size.function;

import java.util.Iterator;

import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.geode.cache.partition.PartitionRegionHelper;
import org.apache.geode.internal.cache.BucketRegion;
import org.apache.geode.internal.cache.LocalRegion;
import org.apache.geode.internal.cache.PartitionedRegion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("rawtypes")
public class RegionEntrySizeCalculator implements Function, Declarable {

	private static final long serialVersionUID = -8645146412269564481L;

	private Logger LOG = LogManager.getLogger(RegionEntrySizeCalculator.class);

	@SuppressWarnings("unchecked")
	public void execute(FunctionContext context) {
		boolean histogram = false;
		RegionEntrySizes regionEntrySizes;

		String[] args = (String[]) context.getArguments();
		if (args != null && args.length > 0)
			histogram = Boolean.valueOf(args[0]);

		RegionFunctionContext rfc = (RegionFunctionContext) context;
		Region region = rfc.getDataSet();

		regionEntrySizes = new RegionEntrySizes(region.getFullPath());

		if (PartitionRegionHelper.isPartitionedRegion(region)) {
			processPartitionedValues(region, regionEntrySizes);
		} else {
			processReplicatedValues(region, regionEntrySizes);
		}

		// Return result
		LOG.info(regionEntrySizes);
		processHistogram(regionEntrySizes, histogram);
		context.getResultSender().lastResult(regionEntrySizes);
	}

	private void processPartitionedValues(Region region, RegionEntrySizes regionEntrySizes) {
		PartitionedRegion pr = (PartitionedRegion) region;
		for (BucketRegion br : pr.getDataStore().getAllLocalBucketRegions()) {
			for (Iterator i = br.entrySet().iterator(); i.hasNext();) {
				processRegionEntry(region, regionEntrySizes, i.next());
			}
		}
	}

	private void processReplicatedValues(Region region, RegionEntrySizes regionEntrySizes) {
		for (Iterator i = region.entrySet().iterator(); i.hasNext();) {
			processRegionEntry(region, regionEntrySizes, i.next());
		}
	}

	private void processRegionEntry(Region region, RegionEntrySizes regionEntrySizes, Object obj) {
		LocalRegion.NonTXEntry entry = (LocalRegion.NonTXEntry) obj;
		try {
			regionEntrySizes.calculateEntrySize(entry.getRegionEntry());
		} catch (Exception e) {
			LOG.error("Caught the following exception attempting to calculate the size of " + entry.getRegionEntry()
					+ ":" + e);
			throw e;
		}
	}

	private void processHistogram(RegionEntrySizes regionEntrySizes, boolean histogram) {
		try {
			regionEntrySizes.doHistogram(histogram);
		} catch (Exception e) {
			LOG.error("Caught the following exception attempting to do histogram "
					+ regionEntrySizes.getLargestRegionEntry() + ":" + e);
			throw e;
		}
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
}
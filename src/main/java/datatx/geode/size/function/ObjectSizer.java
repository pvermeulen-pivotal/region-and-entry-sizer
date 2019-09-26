package datatx.geode.size.function;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.query.Index;
import org.apache.geode.internal.cache.RegionEntry;
import org.apache.geode.internal.size.ObjectGraphSizer;
import org.apache.geode.internal.size.ObjectGraphSizer.ObjectFilter;
import org.apache.geode.internal.size.ReflectionObjectSizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ObjectSizer {

	private static Logger LOG = LogManager.getLogger(ObjectSizer.class);

	@SuppressWarnings("rawtypes")
	public static long calculateSize(Region region, boolean dumpHistogram) {
		long size = 0l;
		ObjectFilter filter = new RegionObjectFilter();
		try {
			size = ObjectGraphSizer.size(region, filter, false);
			if (dumpHistogram) {
				dumpHistogram(region, filter);
				LOG.info("Size of " + region.getFullPath() + " (an instance of " + region.getClass().getName() + "): "
						+ size);
			}
		} catch (Exception e) {
			LOG.error("Caught the following exception attempting to dump the size of region " + region.getFullPath()
					+ ":" + e);
		}
		return size;
	}

	public static long calculateSize(RegionEntry regionEntry, boolean dumpHistogram) {
		long size = 0l;
		ObjectFilter filter = new RegionEntryObjectFilter();
		try {
			size = ObjectGraphSizer.size(regionEntry, filter, false);
			if (dumpHistogram) {
				dumpHistogram(regionEntry, filter);
				LOG.info("Size of " + regionEntry + " (an instance of " + regionEntry.getClass().getName() + "): "
						+ size);
			}
		} catch (Exception e) {
			LOG.error("Caught the following exception attempting to dump the size of " + regionEntry + ":" + e);
		}
		return size;
	}

	public static long calculateSize(Index index, boolean dumpHistogram) {
		ObjectFilter filter = new IndexObjectFilter();
		long size = 0l;
		try {
			size = ObjectGraphSizer.size(index, filter, false);
			if (dumpHistogram) {
				dumpHistogram(index, filter);
				LOG.info("Size of " + index + " (an instance of " + index.getClass().getName() + "): " + size);
			}
		} catch (Exception e) {
			LOG.error("Caught the following exception attempting to dump the size of " + index + ":" + e);
		}
		return size;
	}

	public static int calculateSize(Object obj) {
		int size = 0;
		try {
			size = ReflectionObjectSizer.getInstance().sizeof(obj);
		} catch (Exception e) {
			LOG.error("Caught the following exception attempting to dump the size of " + obj + ":" + e);
		}
		return size;
	}

	private static void dumpHistogram(Object obj, ObjectFilter filter) throws IllegalAccessException {
		LOG.info("Histogram for " + obj + " (an instance of " + obj.getClass().getName() + ")");
		LOG.info(ObjectGraphSizer.histogram(obj, filter, false));
	}

//	@SuppressWarnings("unused")
//	private static final ObjectFilter LOGGING_FILTER = new ObjectFilter() {
//		public boolean accept(Object parent, Object object) {
//			String parentClassName = null;
//			if (parent != null) {
//				parentClassName = parent.getClass().getName();
//			}
//			LOG.info("Filtering parent=" + parentClassName + "; object=" + object.getClass().getName());
//			return true;
//		}
//	};
}
package datatx.geode.size.function;

import org.apache.geode.LogWriter;
import org.apache.geode.cache.Cache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.query.IndexStatistics;
import org.apache.geode.cache.query.IndexType;
import org.apache.geode.internal.cache.RegionEntry;
import org.apache.geode.internal.size.ObjectGraphSizer.ObjectFilter;
import org.apache.geode.pdx.internal.PdxString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("deprecation")
public class IndexObjectFilter implements ObjectFilter {

	private Logger LOG = LogManager.getLogger(IndexObjectFilter.class);
	
	private boolean logAllClasses = false;

	private boolean logRejectedClasses = false;

	private boolean logAcceptedClasses = false;

	public boolean accept(Object parent, Object object) {
		boolean accept = true;
		String parentClassName = null;
		if (this.logAllClasses || this.logRejectedClasses || this.logAcceptedClasses) {
			if (parent != null) {
				parentClassName = parent.getClass().getName();
			}
		}
		if (object instanceof Cache || object instanceof Class || object instanceof IndexStatistics
				|| object instanceof IndexType || object instanceof Region || object instanceof RegionEntry
				|| object instanceof LogWriter) {
			if (this.logAllClasses || this.logRejectedClasses) {
				LOG.info("Rejecting parent=" + parentClassName + "; object=" + object.getClass().getName());
			}
			accept = false;
		} else if (parent instanceof PdxString && object instanceof byte[]) {
			if (this.logAllClasses || this.logRejectedClasses) {
				LOG.info("Rejecting parent=" + parentClassName + "; object=" + object.getClass().getName());
			}
			accept = false;
		} else {
			if (this.logAllClasses || this.logAcceptedClasses) {
				LOG.info("Accepting object=" + object + " objectIdentity=" + System.identityHashCode(object)
						+ " (an instance of " + object.getClass().getName() + ")");
			}
		}
		return accept;
	}
}

package datatx.geode.size.function;

import org.apache.geode.internal.cache.RegionEntry;
import org.apache.geode.internal.size.ObjectGraphSizer.ObjectFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegionEntryObjectFilter implements ObjectFilter {

	private Logger LOG = LogManager.getLogger(RegionEntryObjectFilter.class);
	
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
		if (object instanceof RegionEntry && parent != null && parent instanceof RegionEntry) {
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
package datatx.geode.size.function;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.geode.cache.CacheFactory;
import org.apache.geode.internal.cache.RegionEntry;
import org.apache.geode.internal.cache.RegionEntryContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegionEntrySizes implements Serializable {

	private static final long serialVersionUID = -948068137013491395L;

	private Logger LOG = LogManager.getLogger(RegionEntrySizes.class);

	private final String regionName;

	private final List<RegionEntrySize> regionEntrySizes;

	private long totalRegionEntrySize;

	private int totalKeySize;

	private int totalValueSize;

	private static final DecimalFormat FORMAT = new DecimalFormat("#0.##");

	private RegionEntry largestRegionEntry;
	private long largestEntrySize;

	public RegionEntrySizes(String regionName) {
		this.regionName = regionName;
		this.regionEntrySizes = new ArrayList<RegionEntrySize>();
	}

	public int getNumberOfEntries() {
		return this.regionEntrySizes.size();
	}

	public RegionEntry getLargestRegionEntry() {
		return largestRegionEntry;
	}

	public long getTotalRegionEntrySize() {
		return this.totalRegionEntrySize;
	}

	public int getTotalKeySize() {
		return this.totalKeySize;
	}

	public int getTotalValueSize() {
		return this.totalValueSize;
	}

	public double getAverageRegionEntrySize() {
		return getNumberOfEntries() == 0 ? 0 : this.totalRegionEntrySize * 1.0 / getNumberOfEntries();
	}

	public double getAverageKeySize() {
		return getNumberOfEntries() == 0 ? 0 : this.totalKeySize * 1.0 / getNumberOfEntries();
	}

	public double getAverageValueSize() {
		return getNumberOfEntries() == 0 ? 0 : this.totalValueSize * 1.0 / getNumberOfEntries();
	}

	public void calculateEntrySize(RegionEntry regionEntry) {
		long regionEntrySize = ObjectSizer.calculateSize(regionEntry);
		if (regionEntrySize > this.largestEntrySize) {
			this.largestEntrySize = regionEntrySize;
			this.largestRegionEntry = regionEntry;
		}
		this.totalRegionEntrySize += regionEntrySize;

		int keySize = ObjectSizer.calculateSize(regionEntry.getKey());
		this.totalKeySize += keySize;

		int valueSize = ObjectSizer.calculateSize(
				regionEntry.getValueInVM((RegionEntryContext) CacheFactory.getAnyInstance().getRegion(regionName)));
		this.totalValueSize += valueSize;

		this.regionEntrySizes.add(new RegionEntrySize(regionEntrySize, keySize, valueSize));
	}

	public void doHistogram(boolean histogram) {
		if (histogram) {
			LOG.info("Size " + this.largestRegionEntry + " (an instance of "
					+ this.largestRegionEntry.getClass().getName() + "): "
					+ ObjectSizer.calculateSize(this.largestRegionEntry, histogram));
		}
	}

	public String toString() {
		return new StringBuilder().append("regionName=").append(this.regionName).append("; numberOfEntries=")
				.append(NumberFormat.getInstance().format(getNumberOfEntries())).append("; totalRegionEntrySize=")
				.append(NumberFormat.getInstance().format(this.totalRegionEntrySize)).append("\ntotalKeySize=")
				.append(NumberFormat.getInstance().format(this.totalKeySize)).append("; totalValueSize=")
				.append(NumberFormat.getInstance().format(this.totalValueSize)).append("; averageRegionEntrySize=")
				.append(FORMAT.format(getAverageRegionEntrySize())).append("\naverageKeySize=")
				.append(FORMAT.format(getAverageKeySize())).append("; averageValueSize=")
				.append(FORMAT.format(getAverageValueSize())).toString();
	}
}
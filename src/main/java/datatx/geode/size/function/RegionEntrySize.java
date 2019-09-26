package datatx.geode.size.function;

import java.io.Serializable;

public class RegionEntrySize implements Serializable {

	private static final long serialVersionUID = 9009844613714514462L;

	private final long regionEntrySize;
	private final int keySize;
	private final int valueSize;

	public RegionEntrySize(long regionEntrySize, int keySize, int valueSize) {
		this.regionEntrySize = regionEntrySize;
		this.keySize = keySize;
		this.valueSize = valueSize;
	}

	public long getRegionEntrySize() {
		return this.regionEntrySize;
	}

	public int getKeySize() {
		return this.keySize;
	}

	public int getValueSize() {
		return this.valueSize;
	}

	public String toString() {
		return new StringBuilder().append(getClass().getSimpleName()).append("\n[").append("regionEntrySize=")
				.append(this.regionEntrySize)
				.append("\nkeySize=").append(this.keySize)
				.append("\nvalueSize=").append(this.valueSize).append("]").toString();
	}
}

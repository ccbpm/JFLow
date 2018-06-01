package BP.Sys;

public abstract class BuessUnitBase {

	private long WorkID = 0;

	public long getWorkID() {
		return WorkID;
	}

	public void setWorkID(long workID) {
		WorkID = workID;
	}

	public abstract String getTitle();

	public String DoIt() {
		return null;
	}

	@Override
	public String toString() {
		return this.getClass().getName();
	}

}

package BP.Sys;

public abstract class BuessUnitBase {

	private int WorkID = 0;

	public int getWorkID() {
		return WorkID;
	}

	public void setWorkID(int workID) {
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

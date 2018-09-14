package BP.Demo.BuessUnit;

import BP.DA.Log;
import BP.Sys.BuessUnitBase;

public class BuessUnitDemo extends BuessUnitBase {

	@Override
	public String getTitle() {
		return "业务单元测试";
	}

	@Override
	public String DoIt() {
		
		Log.DebugWriteInfo("调用业务单元[" + this.toString() + "] WorkID: " + this.getWorkID());
		return super.DoIt();
	}

}

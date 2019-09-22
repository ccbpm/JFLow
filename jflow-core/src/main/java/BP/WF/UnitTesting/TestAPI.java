package BP.WF.UnitTesting;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import java.util.*;

/** 
  测试过程
*/
public class TestAPI extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 测试过程
	*/
	public TestAPI()
	{
	}
	/** 
	 测试过程
	 
	 @param _No
	*/
	public TestAPI(String _No)
	{
		super(_No);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 测试过程Map
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_TestAPI");
		map.EnDesc = "测试过程";

		map.AddTBStringPK(TestAPIAttr.No, null, "编号", true, false, 1, 92, 2);
		map.AddTBString(TestAPIAttr.Name, null, "名称", true, false, 1, 50, 20);
		this.set_enMap(map);
		return this.get_enMap();
	}
}
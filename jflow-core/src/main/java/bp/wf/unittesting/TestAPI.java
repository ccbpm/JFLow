package bp.wf.unittesting;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.wf.*;
import java.util.*;

/** 
  测试过程
*/
public class TestAPI extends EntityNoName
{

		///构造方法
	/** 
	 测试过程
	*/
	public TestAPI()
	{
	}
	/** 
	 测试过程
	 
	 @param _No
	 * @throws Exception 
	*/
	public TestAPI(String _No) throws Exception
	{
		super(_No);
	}

		///

	/** 
	 测试过程Map
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_TestAPI");
		map.setEnDesc("测试过程");

		map.AddTBStringPK(TestAPIAttr.No, null, "编号", true, false, 1, 92, 2);
		map.AddTBString(TestAPIAttr.Name, null, "名称", true, false, 1, 50, 20);
		this.set_enMap(map);
		return this.get_enMap();
	}
}
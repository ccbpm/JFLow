package bp.wf.unittesting;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.wf.*;
import java.util.*;

/** 
  测试版本
*/
public class TestVer extends EntityNoName
{

		///构造方法
	/** 
	 测试版本
	*/
	public TestVer()
	{
	}
	/** 
	 测试版本
	 
	 @param _No
	 * @throws Exception 
	*/
	public TestVer(String _No) throws Exception
	{
		super(_No);
	}

		///

	/** 
	 测试版本Map
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_TestVer");
		map.setEnDesc("测试版本");

		map.setDepositaryOfEntity( Depositary.Application);
		map.setDepositaryOfMap(Depositary.Application);

		map.AddTBStringPK(TestVerAttr.No, null, "编号", true, false, 1, 92, 2);
		map.AddTBString(TestVerAttr.Name, null, "名称", true, false, 1, 50, 20);
		this.set_enMap(map);
		return this.get_enMap();
	}
}
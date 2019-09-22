package BP.WF.UnitTesting;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import java.util.*;

/** 
  测试版本
*/
public class TestVer extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 测试版本
	*/
	public TestVer()
	{
	}
	/** 
	 测试版本
	 
	 @param _No
	*/
	public TestVer(String _No)
	{
		super(_No);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 测试版本Map
	*/
	@Override
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("WF_TestVer");
		map.EnDesc = "测试版本";

		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		map.AddTBStringPK(TestVerAttr.No, null, "编号", true, false, 1, 92, 2);
		map.AddTBString(TestVerAttr.Name, null, "名称", true, false, 1, 50, 20);
		this._enMap = map;
		return this._enMap;
	}
}
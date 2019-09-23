package BP.WF.UnitTesting;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.*;
import java.util.*;

/** 
  测试明细
*/
public class TestSample extends EntityMyPK
{

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	public final String getFK_API() throws Exception
	{
		return this.GetValStrByKey(TestSampleAttr.FK_API);
	}
	public final void setFK_API(String value) throws Exception
	{
		this.SetValByKey(TestSampleAttr.FK_API,value);
	}
	public final String getFK_Ver() throws Exception
	{
		return this.GetValStrByKey(TestSampleAttr.FK_Ver);
	}
	public final void setFK_Ver(String value) throws Exception
	{
		this.SetValByKey(TestSampleAttr.FK_Ver, value);
	}
	public final String getName() throws Exception
	{
		return this.GetValStrByKey(TestSampleAttr.Name);
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(TestSampleAttr.Name, value);
	}
	public final String getDTFrom() throws Exception
	{
		return this.GetValStrByKey(TestSampleAttr.DTFrom);
	}
	public final void setDTFrom(String value) throws Exception
	{
		this.SetValByKey(TestSampleAttr.DTFrom, value);
	}
	public final String getDTTo() throws Exception
	{
		return this.GetValStrByKey(TestSampleAttr.DTTo);
	}
	public final void setDTTo(String value) throws Exception
	{
		this.SetValByKey(TestSampleAttr.DTTo, value);
	}
	public final double getTimeUse() throws Exception
	{
		return this.GetValDoubleByKey(TestSampleAttr.TimeUse);
	}
	public final void setTimeUse(double value) throws Exception
	{
		this.SetValByKey(TestSampleAttr.TimeUse, value);
	}
	public final double getTimesPerSecond() throws Exception
	{
		return this.GetValDoubleByKey(TestSampleAttr.TimesPerSecond);
	}
	public final void setTimesPerSecond(double value) throws Exception
	{
		this.SetValByKey(TestSampleAttr.TimesPerSecond, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造方法

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 测试明细
	*/
	public TestSample()
	{
	}
	/** 
	 测试明细
	 
	 @param _No
	 * @throws Exception 
	*/
	public TestSample(String _No) throws Exception
	{
		super(_No);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 测试明细Map
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("WF_TestSample");
		map.setEnDesc("测试明细");
		map.Java_SetCodeStruct("2");

		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		map.AddMyPK();

		map.AddTBString(TestSampleAttr.Name, null, "测试名称", true, false, 1, 50, 20);

		map.AddDDLEntities(TestSampleAttr.FK_API, null, "测试的API", new TestAPIs(), false);
		map.AddDDLEntities(TestSampleAttr.FK_Ver, null, "测试的版本", new TestVers(), false);

		map.AddTBDateTime(TestSampleAttr.DTFrom, null, "从", true, false);
		map.AddTBDateTime(TestSampleAttr.DTTo, null, "到", true, false);
		map.AddTBFloat(TestSampleAttr.TimeUse, 0, "用时(毫秒)", true, false);
		map.AddTBFloat(TestSampleAttr.TimesPerSecond, 0, "每秒跑多少个?", true, false);

		map.AddSearchAttr(TestSampleAttr.FK_API);
		this.set_enMap(map);
		return this.get_enMap();
	}
}
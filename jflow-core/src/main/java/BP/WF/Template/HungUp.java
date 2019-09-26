package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.*;
import BP.WF.Template.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 挂起
*/
public class HungUp extends EntityMyPK
{

		///#region 属性
	public final HungUpWay getHungUpWay() throws Exception
	{
		return HungUpWay.forValue(this.GetValIntByKey(HungUpAttr.HungUpWay));
	}
	public final void setHungUpWay(HungUpWay value) throws Exception
	{
		this.SetValByKey(HungUpAttr.HungUpWay, value.getValue());
	}
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(HungUpAttr.FK_Node);
	}
	public final void setFK_Node(int value) throws Exception
	{
		this.SetValByKey(HungUpAttr.FK_Node, value);
	}
	public final long getWorkID() throws Exception
	{
		return this.GetValInt64ByKey(HungUpAttr.WorkID);
	}
	public final void setWorkID(long value) throws Exception
	{
		this.SetValByKey(HungUpAttr.WorkID, value);
	}
	/** 
	 挂起标题
	 * @throws Exception 
	*/
	public final String getTitle() throws Exception
	{
		String s = this.GetValStringByKey(HungUpAttr.Title);
		if (DataType.IsNullOrEmpty(s))
		{
			s = "来自@Rec的挂起信息.";
		}
		return s;
	}
	public final void setTitle(String value) throws Exception
	{
		this.SetValByKey(HungUpAttr.Title, value);
	}
	/** 
	 挂起原因
	 * @throws Exception 
	*/
	public final String getNote() throws Exception
	{
	   return this.GetValStringByKey(HungUpAttr.Note);
	}
	public final void setNote(String value) throws Exception
	{
		this.SetValByKey(HungUpAttr.Note, value);
	}
	public final String getRec() throws Exception
	{
		return this.GetValStringByKey(HungUpAttr.Rec);
	}
	public final void setRec(String value) throws Exception
	{
		this.SetValByKey(HungUpAttr.Rec, value);
	}
	/** 
	 解除挂起时间
	 * @throws Exception 
	*/
	public final String getDTOfUnHungUp() throws Exception
	{
		return this.GetValStringByKey(HungUpAttr.DTOfUnHungUp);
	}
	public final void setDTOfUnHungUp(String value) throws Exception
	{
		this.SetValByKey(HungUpAttr.DTOfUnHungUp, value);
	}
	/** 
	 预计解除挂起时间
	 * @throws Exception 
	*/
	public final String getDTOfUnHungUpPlan() throws Exception
	{
		return this.GetValStringByKey(HungUpAttr.DTOfUnHungUpPlan);
	}
	public final void setDTOfUnHungUpPlan(String value) throws Exception
	{
		this.SetValByKey(HungUpAttr.DTOfUnHungUpPlan, value);
	}
	/** 
	 解除挂起时间
	 * @throws Exception 
	*/
	public final String getDTOfHungUp() throws Exception
	{
		return this.GetValStringByKey(HungUpAttr.DTOfHungUp);
	}
	public final void setDTOfHungUp(String value) throws Exception
	{
		this.SetValByKey(HungUpAttr.DTOfHungUp, value);
	}

		///#endregion


		///#region 构造函数
	/** 
	 挂起
	*/
	public HungUp()
	{
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_HungUp", "挂起");
		map.Java_SetEnType(EnType.Admin);
		map.IndexField = HungUpAttr.WorkID;


		map.AddMyPK();
		map.AddTBInt(HungUpAttr.FK_Node, 0, "节点ID", true, true);
		map.AddTBInt(HungUpAttr.WorkID, 0, "WorkID", true, true);
		map.AddDDLSysEnum(HungUpAttr.HungUpWay, 0, "挂起方式", true, true, HungUpAttr.HungUpWay, "@0=无限挂起@1=按指定的时间解除挂起并通知我自己@2=按指定的时间解除挂起并通知所有人");

		map.AddTBStringDoc(HungUpAttr.Note, null, "挂起原因(标题与内容支持变量)", true, false, true);

		map.AddTBString(HungUpAttr.Rec, null, "挂起人", true, false, 0, 50, 10, true);

		map.AddTBDateTime(HungUpAttr.DTOfHungUp, null, "挂起时间", true, false);
		map.AddTBDateTime(HungUpAttr.DTOfUnHungUp, null, "实际解除挂起时间", true, false);
		map.AddTBDateTime(HungUpAttr.DTOfUnHungUpPlan, null, "预计解除挂起时间", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
	/** 
	 执行释放挂起
	*/
	public final void DoRelease()
	{
	}

		///#endregion
}
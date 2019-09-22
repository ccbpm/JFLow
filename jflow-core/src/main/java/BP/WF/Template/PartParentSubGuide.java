package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 配件.	 
*/
public class PartParentSubGuide extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsUpdate = true;
		uac.IsDelete = true;
		return uac;
	}
	/** 
	 配件的事务编号
	*/
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(PartAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		SetValByKey(PartAttr.FK_Flow, value);
	}
	/** 
	 类型
	*/
	public final String getPartType()
	{
		return this.GetValStringByKey(PartAttr.PartType);
	}
	public final void setPartType(String value)
	{
		SetValByKey(PartAttr.PartType, value);
	}
	/** 
	 节点ID
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(PartAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		SetValByKey(PartAttr.FK_Node, value);
	}
	/** 
	 字段存储0
	*/
	public final String getTag0()
	{
		return this.GetValStringByKey(PartAttr.Tag0);
	}
	public final void setTag0(String value)
	{
		SetValByKey(PartAttr.Tag0, value);
	}
	/** 
	 字段存储1
	*/
	public final String getTag1()
	{
		return this.GetValStringByKey(PartAttr.Tag1);
	}
	public final void setTag1(String value)
	{
		SetValByKey(PartAttr.Tag1, value);
	}
	/** 
	 字段存储2
	*/
	public final String getTag2()
	{
		return this.GetValStringByKey(PartAttr.Tag2);
	}
	public final void setTag2(String value)
	{
		SetValByKey(PartAttr.Tag2, value);
	}
	/** 
	 字段存储3
	*/
	public final String getTag3()
	{
		return this.GetValStringByKey(PartAttr.Tag3);
	}
	public final void setTag3(String value)
	{
		SetValByKey(PartAttr.Tag3, value);
	}
	/** 
	 字段存储4
	*/
	public final String getTag4()
	{
		return this.GetValStringByKey(PartAttr.Tag4);
	}
	public final void setTag4(String value)
	{
		SetValByKey(PartAttr.Tag4, value);
	}
	/** 
	 字段存储5
	*/
	public final String getTag5()
	{
		return this.GetValStringByKey(PartAttr.Tag5);
	}
	public final void setTag5(String value)
	{
		SetValByKey(PartAttr.Tag5, value);
	}
	/** 
	 字段存储6
	*/
	public final String getTag6()
	{
		return this.GetValStringByKey(PartAttr.Tag6);
	}
	public final void setTag6(String value)
	{
		SetValByKey(PartAttr.Tag6, value);
	}
	/** 
	 字段存储7
	*/
	public final String getTag7()
	{
		return this.GetValStringByKey(PartAttr.Tag7);
	}
	public final void setTag7(String value)
	{
		SetValByKey(PartAttr.Tag7, value);
	}
	/** 
	 字段存储8
	*/
	public final String getTag8()
	{
		return this.GetValStringByKey(PartAttr.Tag8);
	}
	public final void setTag8(String value)
	{
		SetValByKey(PartAttr.Tag8, value);
	}
	/** 
	 字段存储9
	*/
	public final String getTag9()
	{
		return this.GetValStringByKey(PartAttr.Tag9);
	}
	public final void setTag9(String value)
	{
		SetValByKey(PartAttr.Tag9, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 配件
	*/
	public PartParentSubGuide()
	{
	}
	/** 
	 配件
	 
	 @param _oid 配件ID	
	*/
	public PartParentSubGuide(String mypk)
	{
		this.MyPK = mypk;
		this.Retrieve();
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("WF_Part", "前置导航-父子流程");

		map.AddMyPK();

		map.AddTBString(PartAttr.FK_Flow, null, "流程编号", true, true, 0, 100, 10);
		map.AddTBString(PartAttr.PartType, null, "类型", false, true, 0, 100, 10);

		map.AddTBString(PartAttr.Tag0, null, "父流程编号", true, true, 0, 2000, 10);
		map.AddTBString(PartAttr.Tag1, null, "流程名称", true, true, 0, 2000, 10);

		map.AddTBString(PartAttr.Tag2, null, "隐藏查询条件", true, false, 0, 2000, 10,true);
		map.SetHelperAlert(PartAttr.Tag2, "格式为: WFState=3 AND FlowStarter=＠WebUser.No  这些列都在NDxxRpt表里可以通过SELECT * FROM Sys_MapAttr WHERE FK_MapData=NDxxxRpt 找到. ");

		map.AddTBString(PartAttr.Tag3, null, "显示的列", true, false, 0, 2000, 10, true);
		map.SetHelperAlert(PartAttr.Tag3, "格式为: ＠Title,标题＠Tel,电话＠Email,邮件  这些列都在NDxxRpt表里可以通过SELECT * FROM Sys_MapAttr WHERE FK_MapData=NDxxxRpt 找到.");

			//map.AddTBString(PartAttr.Tag4, null, "Tag4", false, true, 0, 2000, 10);
			//map.AddTBString(PartAttr.Tag5, null, "Tag5", false, true, 0, 2000, 10);
			//map.AddTBString(PartAttr.Tag6, null, "Tag6", false, true, 0, 2000, 10);
			//map.AddTBString(PartAttr.Tag7, null, "Tag7", false, true, 0, 2000, 10);
			//map.AddTBString(PartAttr.Tag8, null, "Tag8", false, true, 0, 2000, 10);
			//map.AddTBString(PartAttr.Tag9, null, "Tag9", false, true, 0, 2000, 10);

		this._enMap = map;
		return this._enMap;
	}
	@Override
	protected boolean beforeUpdateInsertAction()
	{
		this.setPartType(BP.WF.Template.PartType.ParentSubFlowGuide);
		return super.beforeUpdateInsertAction();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
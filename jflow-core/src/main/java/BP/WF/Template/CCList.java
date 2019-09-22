package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 抄送
*/
public class CCList extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 状态
	*/
	public final CCSta getHisSta()
	{
		return CCSta.forValue(this.GetValIntByKey(CCListAttr.Sta));
	}
	public final void setHisSta(CCSta value)
	{
		if (value == CCSta.Read)
		{
			this.setCDT(DataType.CurrentDataTime);
		}
		this.SetValByKey(CCListAttr.Sta, value.getValue());
	}
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{

		UAC uac = new UAC();
		if (!BP.Web.WebUser.No.equals("admin"))
		{
			uac.IsView = false;
			return uac;
		}
		uac.IsDelete = false;
		uac.IsInsert = false;
		uac.IsUpdate = true;
		return uac;
	}
	public final String getCCTo()
	{
		return this.GetValStringByKey(CCListAttr.CCTo);
	}
	public final void setCCTo(String value)
	{
		this.SetValByKey(CCListAttr.CCTo, value);
	}
	/** 
	 抄送部门
	*/
	public final String getCCToDept()
	{
		return this.GetValStringByKey(CCListAttr.CCToDept);
	}
	public final void setCCToDept(String value)
	{
		this.SetValByKey(CCListAttr.CCToDept, value);
	}
	/** 
	 抄送给Name
	*/
	public final String getCCToName()
	{
		String s = this.GetValStringByKey(CCListAttr.CCToName);
		if (DataType.IsNullOrEmpty(s))
		{
			s = this.getCCTo();
		}
		return s;
	}
	public final void setCCToName(String value)
	{
		this.SetValByKey(CCListAttr.CCToName, value);
	}

	/** 
	 抄送给部门名称
	*/
	public final String getCCToDeptName()
	{
		String s = this.GetValStringByKey(CCListAttr.CCToDeptName);
		if (DataType.IsNullOrEmpty(s))
		{
			return "无";
		}
		return s;
	}
	public final void setCCToDeptName(String value)
	{
		this.SetValByKey(CCListAttr.CCToDeptName, value);
	}
	/** 
	 抄送给部门名称
	*/
	public final String getCCToDeptNameHtml()
	{
		String s = this.GetValStringByKey(CCListAttr.CCToDeptName);
		if (DataType.IsNullOrEmpty(s))
		{
			return "无";
		}
		return DataType.ParseText2Html(s);
	}
	/** 
	 读取时间
	*/
	public final String getCDT()
	{
		return this.GetValStringByKey(CCListAttr.CDT);
	}
	public final void setCDT(String value)
	{
		this.SetValByKey(CCListAttr.CDT, value);
	}
	/** 
	 抄送人所在的节点编号
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(CCListAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		this.SetValByKey(CCListAttr.FK_Node, value);
	}
	//public int NDFrom
	//{
	//    get
	//    {
	//        return this.GetValIntByKey(CCListAttr.NDFrom);
	//    }
	//    set
	//    {
	//        this.SetValByKey(CCListAttr.NDFrom, value);
	//    }
	//}
	public final long getWorkID()
	{
		return this.GetValInt64ByKey(CCListAttr.WorkID);
	}
	public final void setWorkID(long value)
	{
		this.SetValByKey(CCListAttr.WorkID, value);
	}
	public final long getFID()
	{
		return this.GetValInt64ByKey(CCListAttr.FID);
	}
	public final void setFID(long value)
	{
		this.SetValByKey(CCListAttr.FID, value);
	}
	/** 
	 父流程工作ID
	*/
	public final long getPWorkID()
	{
		return this.GetValInt64ByKey(CCListAttr.PWorkID);
	}
	public final void setPWorkID(long value)
	{
		this.SetValByKey(CCListAttr.PWorkID, value);
	}
	/** 
	 父流程编号
	*/
	public final String getPFlowNo()
	{
		return this.GetValStringByKey(CCListAttr.PFlowNo);
	}
	public final void setPFlowNo(String value)
	{
		this.SetValByKey(CCListAttr.PFlowNo, value);
	}
	/** 
	 流程编号
	*/
	public final String getFK_FlowT()
	{
		return this.GetValRefTextByKey(CCListAttr.FK_Flow);
	}
	public final String getFlowName()
	{
		return this.GetValStringByKey(CCListAttr.FlowName);
	}
	public final void setFlowName(String value)
	{
		this.SetValByKey(CCListAttr.FlowName, value);
	}
	public final String getNodeName()
	{
		return this.GetValStringByKey(CCListAttr.NodeName);
	}
	public final void setNodeName(String value)
	{
		this.SetValByKey(CCListAttr.NodeName, value);
	}
	/** 
	 抄送标题
	*/
	public final String getTitle()
	{
		return this.GetValStringByKey(CCListAttr.Title);
	}
	public final void setTitle(String value)
	{
		this.SetValByKey(CCListAttr.Title, value);
	}
	/** 
	 抄送内容
	*/
	public final String getDoc()
	{
		return this.GetValStringByKey(CCListAttr.Doc);
	}
	public final void setDoc(String value)
	{
		this.SetValByKey(CCListAttr.Doc, value);
	}
	public final String getDocHtml()
	{
		return this.GetValHtmlStringByKey(CCListAttr.Doc);
	}
	/** 
	 抄送对象
	*/
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(CCListAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		this.SetValByKey(CCListAttr.FK_Flow, value);
	}
	public final String getRec()
	{
		return this.GetValStringByKey(CCListAttr.Rec);
	}
	public final void setRec(String value)
	{
		this.SetValByKey(CCListAttr.Rec, value);
	}
	public final String getRDT()
	{
		return this.GetValStringByKey(CCListAttr.RDT);
	}
	public final void setRDT(String value)
	{
		this.SetValByKey(CCListAttr.RDT, value);
	}
	/** 
	 是否加入待办列表
	*/
	public final boolean getInEmpWorks()
	{
		return this.GetValBooleanByKey(CCListAttr.InEmpWorks);
	}
	public final void setInEmpWorks(boolean value)
	{
		this.SetValByKey(CCListAttr.InEmpWorks, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 CCList
	*/
	public CCList()
	{
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
		Map map = new Map("WF_CCList", "抄送列表");



		map.AddMyPK();

		map.AddTBString(CCListAttr.Title, null, "标题", true, true, 0, 500, 10, true);
		map.AddTBInt(CCListAttr.Sta, 0, "状态", true, true);

		map.AddTBString(CCListAttr.FK_Flow, null, "流程编号", true, true, 0, 3, 10, true);
		map.AddTBString(CCListAttr.FlowName, null, "流程名称", true, true, 0, 200, 10, true);
		map.AddTBInt(CCListAttr.FK_Node, 0, "节点", true, true);
		map.AddTBString(CCListAttr.NodeName, null, "节点名称", true, true, 0, 500, 10, true);

		map.AddTBInt(CCListAttr.WorkID, 0, "工作ID", true, true);
		map.AddTBInt(CCListAttr.FID, 0, "FID", true, true);

		map.AddTBStringDoc();

		map.AddTBString(CCListAttr.Rec, null, "抄送人员", true, true, 0, 50, 10, true);
		map.AddTBDateTime(CCListAttr.RDT, null, "记录日期", true, false);


		map.AddTBString(CCListAttr.CCTo, null, "抄送给", true, false, 0, 50, 10, true);
		map.AddTBString(CCListAttr.CCToName, null, "抄送给(人员名称)", true, false, 0, 50, 10, true);

		map.AddTBString(CCListAttr.CCToDept, null, "抄送到部门", true, false, 0, 50, 10, true);
		map.AddTBString(CCListAttr.CCToDeptName, null, "抄送给部门名称", true, false, 0, 600, 10, true);

		map.AddTBDateTime(CCListAttr.CDT, null, "打开时间", true, false);

		map.AddTBString(CCListAttr.PFlowNo, null, "父流程编号", true, true, 0, 100, 10, true);
		map.AddTBInt(CCListAttr.PWorkID, 0, "父流程WorkID", true, true);
			//added by liuxc,2015.7.6，标识是否在待办列表里显示
		map.AddBoolean(CCListAttr.InEmpWorks, false, "是否加入待办列表", true, true);

		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
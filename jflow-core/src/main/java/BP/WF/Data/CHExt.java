package BP.WF.Data;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Port.*;
import BP.Web.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 时效考核
*/
public class CHExt extends EntityMyPK
{

		///#region 基本属性
	/** 
	 考核状态
	 * @throws Exception 
	*/
	public final CHSta getCHSta() throws Exception
	{
		return CHSta.forValue(this.GetValIntByKey(CHAttr.CHSta));
	}
	public final void setCHSta(CHSta value) throws Exception
	{
		this.SetValByKey(CHAttr.CHSta, value.getValue());
	}
	/** 
	 时间到
	 * @throws Exception 
	*/
	public final String getDTTo() throws Exception
	{
		return this.GetValStringByKey(CHAttr.DTTo);
	}
	public final void setDTTo(String value) throws Exception
	{
		this.SetValByKey(CHAttr.DTTo, value);
	}
	/** 
	 时间从
	 * @throws Exception 
	*/
	public final String getDTFrom() throws Exception
	{
		return this.GetValStringByKey(CHAttr.DTFrom);
	}
	public final void setDTFrom(String value) throws Exception
	{
		this.SetValByKey(CHAttr.DTFrom, value);
	}
	/** 
	 应完成日期
	 * @throws Exception 
	*/
	public final String getSDT() throws Exception
	{
		return this.GetValStringByKey(CHAttr.SDT);
	}
	public final void setSDT(String value) throws Exception
	{
		this.SetValByKey(CHAttr.SDT, value);
	}
	/** 
	 流程标题
	 * @throws Exception 
	*/
	public final String getTitle() throws Exception
	{
		return this.GetValStringByKey(CHAttr.Title);
	}
	public final void setTitle(String value) throws Exception
	{
		this.SetValByKey(CHAttr.Title, value);
	}
	/** 
	 流程编号
	 * @throws Exception 
	*/
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStringByKey(CHAttr.FK_Flow);
	}
	public final void setFK_Flow(String value) throws Exception
	{
		this.SetValByKey(CHAttr.FK_Flow, value);
	}
	/** 
	 流程
	 * @throws Exception 
	*/
	public final String getFK_FlowT() throws Exception
	{
		return this.GetValStringByKey(CHAttr.FK_FlowT);
	}
	public final void setFK_FlowT(String value) throws Exception
	{
		this.SetValByKey(CHAttr.FK_FlowT, value);
	}
	/** 
	 限期
	 * @throws Exception 
	*/
	public final int getTimeLimit() throws Exception
	{
		return this.GetValIntByKey(CHAttr.TimeLimit);
	}
	public final void setTimeLimit(int value) throws Exception
	{
		this.SetValByKey(CHAttr.TimeLimit, value);
	}
	/** 
	 操作人员
	 * @throws Exception 
	*/
	public final String getFK_Emp() throws Exception
	{
		return this.GetValStringByKey(CHAttr.FK_Emp);
	}
	public final void setFK_Emp(String value) throws Exception
	{
		this.SetValByKey(CHAttr.FK_Emp, value);
	}
	/** 
	 人员
	 * @throws Exception 
	*/
	public final String getFK_EmpT() throws Exception
	{
		return this.GetValStringByKey(CHAttr.FK_EmpT);
	}
	public final void setFK_EmpT(String value) throws Exception
	{
		this.SetValByKey(CHAttr.FK_EmpT, value);
	}
	/** 
	 部门
	 * @throws Exception 
	*/
	public final String getFK_Dept() throws Exception
	{
		return this.GetValStrByKey(CHAttr.FK_Dept);
	}
	public final void setFK_Dept(String value) throws Exception
	{
		this.SetValByKey(CHAttr.FK_Dept, value);
	}
	/** 
	 部门名称
	 * @throws Exception 
	*/
	public final String getFK_DeptT() throws Exception
	{
		return this.GetValStrByKey(CHAttr.FK_DeptT);
	}
	public final void setFK_DeptT(String value) throws Exception
	{
		this.SetValByKey(CHAttr.FK_DeptT, value);
	}
	/** 
	 年月
	 * @throws Exception 
	*/
	public final String getFK_NY() throws Exception
	{
		return this.GetValStrByKey(CHAttr.FK_NY);
	}
	public final void setFK_NY(String value) throws Exception
	{
		this.SetValByKey(CHAttr.FK_NY, value);
	}
	/** 
	 工作ID
	 * @throws Exception 
	*/
	public final long getWorkID() throws Exception
	{
		return this.GetValInt64ByKey(CHAttr.WorkID);
	}
	public final void setWorkID(long value) throws Exception
	{
		this.SetValByKey(CHAttr.WorkID, value);
	}
	/** 
	 流程ID
	 * @throws Exception 
	*/
	public final long getFID() throws Exception
	{
		return this.GetValInt64ByKey(CHAttr.FID);
	}
	public final void setFID(long value) throws Exception
	{
		this.SetValByKey(CHAttr.FID, value);
	}
	/** 
	 节点ID
	 * @throws Exception 
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(CHAttr.FK_Node);
	}
	public final void setFK_Node(int value) throws Exception
	{
		this.SetValByKey(CHAttr.FK_Node, value);
	}
	/** 
	 节点名称
	 * @throws Exception 
	*/
	public final String getFK_NodeT() throws Exception
	{
		return this.GetValStrByKey(CHAttr.FK_NodeT);
	}
	public final void setFK_NodeT(String value) throws Exception
	{
		this.SetValByKey(CHAttr.FK_NodeT, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsDelete = false;
		uac.IsInsert = false;
		uac.IsUpdate = false;
		uac.IsView = true;
		return uac;
	}
	/** 
	 时效考核
	*/
	public CHExt()
	{
	}
	/** 
	 
	 
	 @param pk
	 * @throws Exception 
	*/
	public CHExt(String pk) throws Exception
	{
		super(pk);
	}

		///#endregion


		///#region Map
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_CH", "时效考核");

		map.AddTBString(CHAttr.Title, null, "标题", true, true, 0, 900, 5,true);

		map.AddDDLEntities(CHAttr.FK_Flow, null, "流程", new Flows(), false);

		map.AddTBString(CHAttr.FK_NodeT, null, "节点名称", true, true, 0, 50, 5);

		map.AddTBString(CHAttr.DTFrom, null, "时间从", true, true, 0, 50, 5);
		map.AddTBString(CHAttr.DTTo, null, "到", true, true, 0, 50, 5);
		map.AddTBString(CHAttr.SDT, null, "应完成日期", true, true, 0, 50, 5);

		map.AddTBString(CHAttr.TimeLimit, null, "限期", true, true, 0, 50, 5);

		map.AddTBString(CHAttr.UseDays, null, "用时", true, true, 0, 50, 5);
		map.AddTBString(CHAttr.OverDays, null, "逾期", true, true, 0, 50, 5);

		map.AddDDLSysEnum(CHAttr.CHSta, 0, "状态", true, true, CHAttr.CHSta, "@0=及时完成@1=按期完成@2=逾期完成@3=超期完成");

		map.AddDDLEntities(CHAttr.FK_Dept, null, "隶属部门", new BP.Port.Depts(), false);
		map.AddDDLEntities(CHAttr.FK_Emp, null, "当事人", new BP.Port.Emps(), false);
		map.AddTBString(CHAttr.FK_NY, null, "月份", true, true, 0, 50, 5);
			//map.AddDDLEntities(CHAttr.FK_NY, null, "月份", new BP.Pub.NYs(), false);

		map.AddTBInt(CHAttr.WorkID, 0, "工作ID", false, true);
		map.AddTBInt(CHAttr.FID, 0, "FID", false, false);

		map.AddTBStringPK(CHAttr.MyPK, null, "MyPK", false, false, 0, 50, 5);
			//map.AddMyPK();

			//查询条件.
		map.AddSearchAttr(CHAttr.FK_Dept);
		   // map.AddSearchAttr(CHAttr.FK_NY);
		map.AddSearchAttr(CHAttr.CHSta);
		map.AddSearchAttr(CHAttr.FK_Flow);

			//方法.
		RefMethod rm = new RefMethod();
		rm.Title = "打开流程轨迹";
		rm.ClassMethodName = this.toString() + ".DoOpen";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "../../WF/Img/FileType/doc.gif";
		rm.IsForEns = false;
		map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "打开";
			//rm.ClassMethodName = this.ToString() + ".DoOpenPDF";
			//rm.Icon = "/WF/Img/FileType/pdf.gif";
			//map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	public final String DoOpen() throws Exception
	{
		return "../../WFRpt.htm?FK_Flow" + this.getFK_Flow() + "&WorkID=" + this.getWorkID() + "&OID=" + this.getWorkID();
	}
}
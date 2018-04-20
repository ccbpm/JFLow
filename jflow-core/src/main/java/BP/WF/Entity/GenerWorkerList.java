package BP.WF.Entity;

import BP.En.Entity;
import BP.En.Map;
import BP.Port.Dept;
import BP.Port.Emp;

/**
 * 工作者列表
 */
public class GenerWorkerList extends Entity
{
	
	private Map _enMap;
	
	// 参数属性.
	/**
	 * 是否是抄送.
	 */
	public final boolean getIsCC()
	{
		return this.GetParaBoolen("IsCC", false);
	}
	
	public final void setIsCC(boolean value)
	{
		this.SetPara("IsCC", value);
	}
	
	/**
	 * 分组标记
	 */
	public final String getGroupMark()
	{
		return this.GetParaString(GenerWorkerListAttr.GroupMark);
	}
	
	public final void setGroupMark(String value)
	{
		this.SetPara(GenerWorkerListAttr.GroupMark, value);
	}
	
	/**
	 * 表单ID(对于动态表单树有效.)
	 */
	public final String getFrmIDs()
	{
		return this.GetParaString(GenerWorkerListAttr.FrmIDs);
	}
	
	public final void setFrmIDs(String value)
	{
		this.SetPara(GenerWorkerListAttr.FrmIDs, value);
	}
	
	// 参数属性.
	
	// 基本属性
	/**
	 * 谁来执行它
	 */
	public final int getWhoExeIt()
	{
		return this.GetValIntByKey(GenerWorkerListAttr.WhoExeIt);
	}
	
	public final void setWhoExeIt(int value)
	{
		SetValByKey(GenerWorkerListAttr.WhoExeIt, value);
	}
	
	public final int getPressTimes()
	{
		return this.GetValIntByKey(GenerWorkerListAttr.PressTimes);
	}
	
	public final void setPressTimes(int value)
	{
		SetValByKey(GenerWorkerListAttr.PressTimes, value);
	}
	
	/**
	 * 优先级
	 */
	public final int getPRI()
	{
		return this.GetValIntByKey(GenerWorkFlowAttr.PRI);
	}
	
	public final void setPRI(int value)
	{
		SetValByKey(GenerWorkFlowAttr.PRI, value);
	}
	
	/**
	 * WorkID
	 */
	@Override
	public String getPK()
	{
		return "WorkID,FK_Emp,FK_Node";
	}
	
	/**
	 * 是否可用(在分配工作时有效)
	 */
	public final boolean getIsEnable()
	{
		return this.GetValBooleanByKey(GenerWorkerListAttr.IsEnable);
	}
	
	public final void setIsEnable(boolean value)
	{
		this.SetValByKey(GenerWorkerListAttr.IsEnable, value);
	}
	
	/**
	 * 是否通过(对审核的会签节点有效)
	 */
	public final boolean getIsPass()
	{
		return this.GetValBooleanByKey(GenerWorkerListAttr.IsPass);
	}
	
	public final void setIsPass(boolean value)
	{
		this.SetValByKey(GenerWorkerListAttr.IsPass, value);
	}
	
	/**
	 * 0=未处理. 1=已经通过. -2= 标志该节点是干流程人员处理的节点，目的为了让分流节点的人员可以看到待办.
	 */
	public final int getIsPassInt()
	{
		return this.GetValIntByKey(GenerWorkerListAttr.IsPass);
	}
	
	public final void setIsPassInt(int value)
	{
		this.SetValByKey(GenerWorkerListAttr.IsPass, value);
	}
	
	public final boolean getIsRead()
	{
		return this.GetValBooleanByKey(GenerWorkerListAttr.IsRead);
	}
	
	public final void setIsRead(boolean value)
	{
		this.SetValByKey(GenerWorkerListAttr.IsRead, value);
	}
	
	/**
	 * WorkID
	 */
	public final long getWorkID()
	{
		return this.GetValInt64ByKey(GenerWorkerListAttr.WorkID);
	}
	
	public final void setWorkID(long value)
	{
		this.SetValByKey(GenerWorkerListAttr.WorkID, value);
	}
	
	/**
	 * Node
	 */
	public final int getFK_Node()
	{
		return this.GetValIntByKey(GenerWorkerListAttr.FK_Node);
	}
	
	public final void setFK_Node(int value)
	{
		this.SetValByKey(GenerWorkerListAttr.FK_Node, value);
	}
	
	public final String getFK_DeptT()
	{
		try
		{
			Dept d = new Dept(this.getFK_Dept());
			return d.getName();
		} catch (java.lang.Exception e)
		{
			return "";
		}
	}
	
	public final String getFK_Dept()
	{
		return this.GetValStrByKey(GenerWorkerListAttr.FK_Dept);
	}
	
	public final void setFK_Dept(String value)
	{
		this.SetValByKey(GenerWorkerListAttr.FK_Dept, value);
	}
	
	/**
	 * 发送人
	 */
	public final String getSender()
	{
		return this.GetValStrByKey(GenerWorkerListAttr.Sender);
	}
	
	public final void setSender(String value)
	{
		this.SetValByKey(GenerWorkerListAttr.Sender, value);
	}
	
	/**
	 * 节点名称
	 */
	public final String getFK_NodeText()
	{
		return this.GetValStrByKey(GenerWorkerListAttr.FK_NodeText);
	}
	
	public final void setFK_NodeText(String value)
	{
		this.SetValByKey(GenerWorkerListAttr.FK_NodeText, value);
	}
	
	/**
	 * 流程ID
	 */
	public final long getFID()
	{
		return this.GetValInt64ByKey(GenerWorkerListAttr.FID);
	}
	
	public final void setFID(long value)
	{
		this.SetValByKey(GenerWorkerListAttr.FID, value);
	}
	
	/**
	 * 警告天
	 */
	public final float getWarningDays()
	{
		return this.GetValFloatByKey(GenerWorkerListAttr.WarningDays);
	}
	
	public final void setWarningDays(float value)
	{
		this.SetValByKey(GenerWorkerListAttr.WarningDays, value);
	}
	
	/**
	 * 工作人员
	 * @throws Exception 
	 */
	public final Emp getHisEmp() throws Exception
	{
		return new Emp(this.getFK_Emp());
	}
	
	/**
	 * 发送日期
	 */
	public final String getRDT()
	{
		return this.GetValStringByKey(GenerWorkerListAttr.RDT);
	}
	
	public final void setRDT(String value)
	{
		this.SetValByKey(GenerWorkerListAttr.RDT, value);
	}
	
	/**
	 * 完成时间
	 */
	public final String getCDT()
	{
		return this.GetValStringByKey(GenerWorkerListAttr.CDT);
	}
	
	public final void setCDT(String value)
	{
		this.SetValByKey(GenerWorkerListAttr.CDT, value);
	}
	
	/**
	 * 应该完成日期
	 */
	public final String getSDT()
	{
		return this.GetValStringByKey(GenerWorkerListAttr.SDT);
	}
	
	public final void setSDT(String value)
	{
		this.SetValByKey(GenerWorkerListAttr.SDT, value);
	}
	
	/**
	 * 警告日期
	 */
	public final String getDTOfWarning()
	{
		return this.GetValStringByKey(GenerWorkerListAttr.DTOfWarning);
	}
	
	public final void setDTOfWarning(String value)
	{
		this.SetValByKey(GenerWorkerListAttr.DTOfWarning, value);
	}
	
	/**
	 * 人员
	 */
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(GenerWorkerListAttr.FK_Emp);
	}
	
	public final void setFK_Emp(String value)
	{
		this.SetValByKey(GenerWorkerListAttr.FK_Emp, value);
	}
	
	/**
	 * 人员名称
	 */
	public final String getFK_EmpText()
	{
		return this.GetValStrByKey(GenerWorkerListAttr.FK_EmpText);
	}
	
	public final void setFK_EmpText(String value)
	{
		this.SetValByKey(GenerWorkerListAttr.FK_EmpText, value);
	}
	
	/**
	 * 流程编号
	 */
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(GenerWorkerListAttr.FK_Flow);
	}
	
	public final void setFK_Flow(String value)
	{
		this.SetValByKey(GenerWorkerListAttr.FK_Flow, value);
	}
	
	public final String getGuestNo()
	{
		return this.GetValStringByKey(GenerWorkerListAttr.GuestNo);
	}
	
	public final void setGuestNo(String value)
	{
		this.SetValByKey(GenerWorkerListAttr.GuestNo, value);
	}
	
	public final String getGuestName()
	{
		return this.GetValStringByKey(GenerWorkerListAttr.GuestName);
	}
	
	public final void setGuestName(String value)
	{
		this.SetValByKey(GenerWorkerListAttr.GuestName, value);
	}
	
	// /#endregion
	
	// /#region 挂起属性
	/**
	 * 挂起时间
	 */
	public final String getDTOfHungUp()
	{
		return this.GetValStringByKey(GenerWorkerListAttr.DTOfHungUp);
	}
	
	public final void setDTOfHungUp(String value)
	{
		this.SetValByKey(GenerWorkerListAttr.DTOfHungUp, value);
	}
	
	/**
	 * 解除挂起时间
	 */
	public final String getDTOfUnHungUp()
	{
		return this.GetValStringByKey(GenerWorkerListAttr.DTOfUnHungUp);
	}
	
	public final void setDTOfUnHungUp(String value)
	{
		this.SetValByKey(GenerWorkerListAttr.DTOfUnHungUp, value);
	}
	
	/**
	 * 挂起次数
	 */
	public final int getHungUpTimes()
	{
		return this.GetValIntByKey(GenerWorkerListAttr.HungUpTimes);
	}
	
	public final void setHungUpTimes(int value)
	{
		this.SetValByKey(GenerWorkerListAttr.HungUpTimes, value);
	}
	
	// 构造函数
	/**
	 * 主键
	 */
	@Override
	public String getPKField()
	{
		return "WorkID,FK_Emp,FK_Node";
	}
	
	/**
	 * 工作者
	 */
	public GenerWorkerList()
	{
	}
	
	public GenerWorkerList(long workid, int FK_Node, String FK_Emp) throws Exception
	{
		if (this.getWorkID() == 0)
		{
			return;
		}
		
		this.setWorkID(workid);
		this.setFK_Node(FK_Node);
		this.setFK_Emp(FK_Emp);
		this.Retrieve();
	}
	
	/**
	 * 重写基类方法
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		
		Map map = new Map("WF_GenerWorkerlist");
		map.setEnDesc("工作者");
		map.AddTBIntPK(GenerWorkerListAttr.WorkID, 0, "工作ID", true, true);
		map.AddTBStringPK(GenerWorkerListAttr.FK_Emp, null, "人员", true, false,
				0, 20, 100);
		map.AddTBIntPK(GenerWorkerListAttr.FK_Node, 0, "节点ID", true, false);
		map.AddTBInt(GenerWorkerListAttr.FID, 0, "流程ID", true, false);
		
		map.AddTBString(GenerWorkerListAttr.FK_EmpText, null, "人员名称", true,
				false, 0, 30, 100);
		map.AddTBString(GenerWorkerListAttr.FK_NodeText, null, "节点名称", true,
				false, 0, 100, 100);
		
		map.AddTBString(GenerWorkerListAttr.FK_Flow, null, "流程", true, false,
				0, 3, 100);
		map.AddTBString(GenerWorkerListAttr.FK_Dept, null, "使用部门", true, false,
				0, 100, 100);
		
		// 如果是流程属性来控制的就按流程属性来计算。
		map.AddTBDateTime(GenerWorkerListAttr.SDT, "应完成日期", false, false);
		map.AddTBDateTime(GenerWorkerListAttr.DTOfWarning, "警告日期", false, false);
		map.AddTBFloat(GenerWorkerListAttr.WarningDays, 0, "预警天", true, false);
		map.AddTBDateTime(GenerWorkerListAttr.RDT, "记录时间", false, false);
		map.AddTBDateTime(GenerWorkerListAttr.CDT, "完成时间", false, false);
		map.AddBoolean(GenerWorkerListAttr.IsEnable, true, "是否可用", true, true);
		
		// add for 上海 2012-11-30
		map.AddTBInt(GenerWorkerListAttr.IsRead, 0, "是否读取", true, true);
		
		// 对会签节点有效
		map.AddTBInt(GenerWorkerListAttr.IsPass, 0, "是否通过(对合流节点有效)", false,
				false);
		
		// 谁执行它？
		map.AddTBInt(GenerWorkerListAttr.WhoExeIt, 0, "谁执行它", false, false);
		
		// 发送人. 2011-11-12 为天津用户增加。
		map.AddTBString(GenerWorkerListAttr.Sender, null, "发送人", true, false,
				0, 200, 100);
		
		// 优先级，2012-06-15 为青岛用户增加。
		map.AddTBInt(GenerWorkFlowAttr.PRI, 1, "优先级", true, true);
		
		// 催办次数. 为亿阳信通加.
		map.AddTBInt(GenerWorkerListAttr.PressTimes, 0, "催办次数", true, false);
		
		// 挂起
		map.AddTBDateTime(GenerWorkerListAttr.DTOfHungUp, null, "挂起时间", false,
				false);
		map.AddTBDateTime(GenerWorkerListAttr.DTOfUnHungUp, null, "预计解除挂起时间",
				false, false);
		map.AddTBInt(GenerWorkerListAttr.HungUpTimes, 0, "挂起次数", true, false);
		
		// 外部用户. 203-08-30
		map.AddTBString(GenerWorkerListAttr.GuestNo, null, "外部用户编号", true,
				false, 0, 30, 100);
		map.AddTBString(GenerWorkerListAttr.GuestName, null, "外部用户名称", true,
				false, 0, 100, 100);
		
		// 参数标记 2014-04-05.
		map.AddTBAtParas(4000);
		
		this.set_enMap(map);
		return this.get_enMap();
	}
	
	@Override
	protected boolean beforeInsert() throws Exception
	{
		if (this.getFID() != 0)
		{
			if (this.getFID() == this.getWorkID())
			{
				this.setFID(0);
			}
		}
		// this.Sender = BP.Web.WebUser.getNo();
		return super.beforeInsert();
	}
}
package BP.WF.Entity;

import BP.DA.DataType;
import BP.En.Attrs;
import BP.En.EnType;
import BP.En.Entity;
import BP.En.Map;
import BP.En.RefMethod;
import BP.Port.Emps;

/**
 * 追加时间申请
 */
public class DataApply extends Entity
{
	
	private Map _enMap;
	
	// 基本属性
	/**
	 * MyPK
	 */
	public final String getMyPK()
	{
		return this.GetValStrByKey(DataApplyAttr.MyPK);
	}
	
	public final void setMyPK(String value)
	{
		this.SetValByKey(DataApplyAttr.MyPK, value);
	}
	
	/**
	 * 工作ID
	 */
	public final long getWorkID()
	{
		return this.GetValInt64ByKey(DataApplyAttr.WorkID);
	}
	
	public final void setWorkID(long value)
	{
		SetValByKey(DataApplyAttr.WorkID, value);
	}
	
	/**
	 * NodeId
	 */
	public final int getNodeId()
	{
		return this.GetValIntByKey(DataApplyAttr.NodeId);
	}
	
	public final void setNodeId(int value)
	{
		SetValByKey(DataApplyAttr.NodeId, value);
	}
	
	public final int getApplyDays()
	{
		return this.GetValIntByKey(DataApplyAttr.ApplyDays);
	}
	
	public final void setApplyDays(int value)
	{
		SetValByKey(DataApplyAttr.ApplyDays, value);
	}
	
	public final int getCheckerDays()
	{
		return this.GetValIntByKey(DataApplyAttr.CheckerDays);
	}
	
	public final void setCheckerDays(int value)
	{
		SetValByKey(DataApplyAttr.CheckerDays, value);
	}
	
	public final String getApplyer()
	{
		return this.GetValStringByKey(DataApplyAttr.Applyer);
	}
	
	public final void setApplyer(String value)
	{
		SetValByKey(DataApplyAttr.Applyer, value);
	}
	
	public final String getApplyNote1()
	{
		return this.GetValStringByKey(DataApplyAttr.ApplyNote1);
	}
	
	public final void setApplyNote1(String value)
	{
		SetValByKey(DataApplyAttr.ApplyNote1, value);
	}
	
	public final String getApplyNote2()
	{
		return this.GetValStringByKey(DataApplyAttr.ApplyNote2);
	}
	
	public final void setApplyNote2(String value)
	{
		SetValByKey(DataApplyAttr.ApplyNote2, value);
	}
	
	public final String getApplyData()
	{
		return this.GetValStringByKey(DataApplyAttr.ApplyData);
	}
	
	public final void setApplyData(String value)
	{
		SetValByKey(DataApplyAttr.ApplyData, value);
	}
	
	public final String getChecker()
	{
		return this.GetValStringByKey(DataApplyAttr.Checker);
	}
	
	public final void setChecker(String value)
	{
		SetValByKey(DataApplyAttr.Checker, value);
	}
	
	public final int getRunState()
	{
		return this.GetValIntByKey(DataApplyAttr.RunState);
	}
	
	public final void setRunState(int value)
	{
		SetValByKey(DataApplyAttr.RunState, value);
	}
	
	// 构造函数
	/**
	 * 追加时间申请
	 */
	public DataApply()
	{
	}
	
	public DataApply(int workid, int nodeid)
	{
		this.setWorkID(workid);
		this.setNodeId(nodeid);
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
		
		Map map = new Map("WF_DataApply");
		map.setEnDesc("追加时间申请");
		map.setEnType(EnType.App);
		
		map.AddMyPK();
		
		map.AddTBInt(DataApplyAttr.WorkID, 0, "工作ID", true, true);
		map.AddTBInt(DataApplyAttr.NodeId, 0, "NodeId", true, true);
		map.AddTBInt(DataApplyAttr.RunState, 0,
				"运行状态0,没有提交，1，提交申请执行审批中，2，审核完毕。", true, true);
		
		map.AddTBInt(DataApplyAttr.ApplyDays, 0, "申请天数", true, true);
		map.AddTBDate(DataApplyAttr.ApplyData, "申请日期", true, true);
		map.AddDDLEntities(DataApplyAttr.Applyer, null, "申请人", new Emps(),
				false);
		map.AddTBStringDoc(DataApplyAttr.ApplyNote1, "", "申请原因", true, true);
		map.AddTBStringDoc(DataApplyAttr.ApplyNote2, "", "申请备注", true, true);
		
		map.AddDDLEntities(DataApplyAttr.Checker, null, "审批人", new Emps(),
				false);
		map.AddTBDate(DataApplyAttr.CheckerData, "审批日期", true, true);
		map.AddTBInt(DataApplyAttr.CheckerDays, 0, "批准天数", true, true);
		map.AddTBStringDoc(DataApplyAttr.CheckerNote1, "", "审批意见", true, true);
		map.AddTBStringDoc(DataApplyAttr.CheckerNote2, "", "审批备注", true, true);
		
		RefMethod rm = new RefMethod();
		rm.Title = "提出追加时限申请";
		rm.Warning = "您确定要向您的领导提出追加时限申请吗？";
		rm.ClassMethodName = this.toString() + ".DoApply";
		// rm.ClassMethodName = "BP.WF.DataApply.DoApply";
		
		Attrs attrs = new Attrs();
		attrs.AddTBInt(DataApplyAttr.ApplyDays, 0, "追加天数", true, false);
		attrs.AddDDLEntities(DataApplyAttr.Checker, null, "审批人", new Emps(),
				false);
		attrs.AddTBStringDoc(DataApplyAttr.ApplyNote1, "", "申请原因", true, false);
		attrs.AddTBStringDoc(DataApplyAttr.ApplyNote2, "", "申请备注", true, false);
		map.AddRefMethod(rm);
		
		// rm = new RefMethod();
		// rm.Title = "审批时限";
		
		// Attrs attrs = new Attrs();
		// attrs.AddTBInt(DataApplyAttr.CheckerDays, 0, "批准天数", true, false);
		// map.AddTBDate(DataApplyAttr.CheckerData, "审批日期", true, true);
		
		// attrs.AddDDLEntities(DataApplyAttr.Checker, null, "审批人", new Emps(),
		// false);
		// attrs.AddTBStringDoc(DataApplyAttr.ApplyNote1, "", "申请原因", true,
		// false);
		// attrs.AddTBStringDoc(DataApplyAttr.ApplyNote2, "", "申请备注", true,
		// false);
		// map.AddRefMethod(rm);
		
		this.set_enMap(map);
		return this.get_enMap();
	}
	
	public final String DoApply(int days, String checker, String note1,
			String note2)
	{
		this.setApplyDays(days);
		
		this.setChecker(checker);
		this.setApplyNote1(note1);
		this.setApplyNote2(note2);
		this.setApplyData(DataType.getCurrentData());
		
		this.setRunState(1); // 进入提交审核状态
		this.Update();
		
		return "执行成功，已经交给" + checker + "审核。";
		
	}
	
	public final String DoCheck(int days, String checker, String note1,
			String note2)
	{
		this.setApplyDays(days);
		
		this.setChecker(checker);
		this.setApplyNote1(note1);
		this.setApplyNote2(note2);
		
		// 调整当前预警与
		GenerWorkerLists wls = new GenerWorkerLists(this.getWorkID(),
				this.getNodeId());
		wls.Retrieve(GenerWorkerListAttr.WorkID, this.getWorkID(),
				GenerWorkerListAttr.FK_Node, this.getNodeId());
		
		for (Entity obj : GenerWorkerLists.convertEntities(wls))
		{
			GenerWorkerList wl = (GenerWorkerList) obj;
			// wl.DTOfWarning = DataType.AddDays(wl.DTOfWarning, days);
			// wl.SDT = DataType.AddDays(wl.SDT, days);
			wl.DirectUpdate();
		}
		
		this.setRunState(2); // 进入提交审核状态
		this.Update();
		
		return "执行成功，已经交给" + checker + "审核。";
	}
}
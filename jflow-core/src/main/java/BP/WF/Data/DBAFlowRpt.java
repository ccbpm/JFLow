package BP.WF.Data;

import BP.Rpt.Rpt2Attr;
import BP.Rpt.Rpt2Attrs;
import BP.Rpt.Rpt2Base;
import BP.Web.WebUser;

public class DBAFlowRpt extends Rpt2Base
{
	/**
	 * 报表的标题(return null 就不显示.)
	 */
	@Override
	public String getTitle()
	{
		return "流程分析";
	}
	
	/**
	 * 默认选择的维度
	 */
	@Override
	public int getAttrDefSelected()
	{
		return 0;
	}
	
	/**
	 * 要分析的属性列表
	 */
	@Override
	public Rpt2Attrs getAttrsOfGroup()
	{
		Rpt2Attrs attrs = new Rpt2Attrs();
		
		Rpt2Attr attr = new Rpt2Attr();
		attr.setTitle("流程分布分析");
		if (WebUser.getNo().equals("admin"))
		{
			attr.DBSrc = "SELECT B.Name as '流程名称', A.FK_Flow as _FK_FLOW, Count(A.WorkID) AS '发起数量' FROM WF_GenerWorkFlow A, WF_Flow B WHERE A.FK_Flow=B.No AND A.WFState!=0 GROUP BY B.Name, A.FK_Flow";
			attr.DESC = "全部运行的流程分析,<a href=\"javascript:WinOpen('/WF/Comm/Group.htm?EnsName=BP.WF.Data.GenerWorkFlowViews')\">高级查询/分析</a>."; // 报表底部说明,可以为空.
			attr.DBSrcOfDtl = "SELECT WorkID as '工作ID', FlowName as '流程名称', Title as '标题',  WFSta as '状态', StarterName as '发起人', TodoEmps as '当前处理人' FROM WF_GenerWorkFlow WHERE FK_FLOW='@_FK_FLOW'";
		} else
		{
			attr.DBSrc = "SELECT B.Name as '流程名称', A.FK_Flow as _FK_FLOW, Count(A.WorkID) AS '发起数量' FROM WF_GenerWorkFlow A, WF_Flow B WHERE A.FK_Flow=B.No AND A.WFState!=0 AND A.FK_Dept='@BP.Web.WebUser.getFK_Dept()' GROUP BY B.Name, A.FK_Flow";
			attr.DESC = "本部门的流程分析, <a href=\"javascript:WinOpen('/WF/Comm/Group.htm?EnsName=BP.WF.Data.GenerWorkFlowViews')\">高级查询/分析</a>."; // 报表底部说明,可以为空.
			attr.DBSrcOfDtl = "SELECT WorkID as '工作ID', FlowName as '流程名称', Title as '标题',  WFSta as '状态', StarterName as '发起人', TodoEmps as '当前处理人' FROM WF_GenerWorkFlow WHERE FK_FLOW='@_FK_FLOW' AND A.FK_Dept='@BP.Web.WebUser.getFK_Dept()'";
		}
		attrs.Add(attr);
		
		attr = new Rpt2Attr();
		attr.setTitle("流程状态统计");
		attr.DBSrc = "SELECT B.Lab as '流程状态', a.WFState as _WFSTATE, Count(A.WorkID)  AS '发起数量' FROM WF_GenerWorkFlow A, Sys_Enum B ";
		attr.DBSrc += " WHERE A.WFState=B.IntKey";
		attr.DBSrc += " AND B.EnumKey='WFState'  ";
		attr.DBSrc += " GROUP BY B.Lab, a.WFState ";
		attr.DESC = ""; // 报表底部说明,可以为空.
		
		// 设置明细表信息.
		attr.DBSrcOfDtl = "SELECT WorkID as '工作ID', FlowName as '流程名称', Title as '标题',  WFSta as '状态', StarterName as '发起人', TodoEmps as '当前处理人' FROM WF_GenerWorkFlow WHERE WFSTATE=@_WFSTATE";
		attrs.Add(attr);
		return attrs;
	}
	
}
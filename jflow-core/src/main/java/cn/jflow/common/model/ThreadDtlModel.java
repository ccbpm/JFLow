package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.En.QueryObject;
import BP.Tools.StringHelper;
import BP.WF.GenerWorkerList;
import BP.WF.GenerWorkerListAttr;
import BP.WF.GenerWorkerLists;
import BP.WF.Node;
import BP.WF.NodeWorkType;
import BP.WF.ThreadKillRole;
import BP.WF.Work;

public class ThreadDtlModel extends BaseModel{
	
	public StringBuffer pub1 = new StringBuffer();

	public ThreadDtlModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	public void pageLoad(HttpServletRequest request,HttpServletResponse response){
		
		Node nd = new Node(this.getFK_Node(request));
		Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID(request));
		wk.Retrieve();
		if (nd.getHisNodeWorkType() == NodeWorkType.WorkHL || nd.getHisNodeWorkType() == NodeWorkType.WorkFHL)
		{
		}
		else
		{
			pub1.append(AddFieldSetRed("err", "当前的节点(" + nd.getName() + ")非合流点，您不能查看子线程."));
			return;
		}

		GenerWorkerLists wls = new GenerWorkerLists();
		QueryObject qo = new QueryObject(wls);
		qo.AddWhere(GenerWorkerListAttr.FID, wk.getOID());
		qo.addAnd();
		qo.AddWhere(GenerWorkerListAttr.IsEnable, 1);
		qo.addAnd();
		qo.AddWhere(GenerWorkerListAttr.IsPass, "!=", -2);

		int i = qo.DoQuery();
		if (i == 1)
		{
			wls.clear();
			qo.clear();
			qo.AddWhere(GenerWorkerListAttr.FID, wk.getOID());
			qo.addAnd();
			qo.AddWhere(GenerWorkerListAttr.IsEnable, 1);
			qo.addAnd();
			qo.AddWhere(GenerWorkerListAttr.IsPass, "!=", -2);
			qo.DoQuery();
		}

		//如果没有子流程就不让它显示
		if (wls.size() > 0)
		{
			pub1.append(AddTable("border=0"));
			pub1.append(AddTR());
			pub1.append(AddTDTitle("IDX"));
			pub1.append(AddTDTitle("节点"));
			pub1.append(AddTDTitle("处理人"));
			pub1.append(AddTDTitle("名称"));
			pub1.append(AddTDTitle("部门"));
			pub1.append(AddTDTitle("状态"));
			pub1.append(AddTDTitle("应完成日期"));
			pub1.append(AddTDTitle("实际完成日期"));
			pub1.append(AddTDTitle(""));
			pub1.append(AddTREnd());
	
			boolean is1 = false;
			int idx = 0;
			for (GenerWorkerList wl : wls.ToJavaList())
			{
				idx++;
				pub1.append(AddTR(is1));
	
				pub1.append(AddTDIdx(idx));
				pub1.append(AddTD(wl.getFK_NodeText()));
				pub1.append(AddTD(wl.getFK_Emp()));
	
				pub1.append(AddTD(wl.getFK_EmpText()));
				pub1.append(AddTD(wl.getFK_DeptT()));
	
				if (wl.getIsPass())
				{
					pub1.append(AddTD("已完成"));
					pub1.append(AddTD(wl.getSDT()));
					pub1.append(AddTD(wl.getRDT()));
				}
				else
				{
					pub1.append(AddTD("<font color=red>未完成</font>"));
					pub1.append(AddTD(wl.getSDT()));
					pub1.append(AddTD());
				}
	
				if (wl.getIsPass() == false)
				{
					if (nd.getThreadKillRole() == ThreadKillRole.ByHand)
					{
						pub1.append(AddTD("<a href=\"javascript:DoDelSubFlow('" + wl.getFK_Flow() + "','" + wl.getWorkID() + "')\"><img src='" + BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Btn/Delete.gif' border=0/>终止</a>"));
					}
					else
					{
						pub1.append(AddTD());
					}
				}
				else
				{
					pub1.append(AddTD("<a href=\"javascript:WinOpen('" + BP.WF.Glo.getCCFlowAppPath() + "WF/WorkOpt/fhlflow.jsp?WorkID=" + wl.getWorkID() + "&FID=" + wl.getFID() + "&FK_Flow=" + nd.getFK_Flow() + "&FK_Node=" + this.getFK_Node(request) + "','po9')\">打开</a>"));
				}
				pub1.append(AddTREnd());
			}
			pub1.append(AddTableEnd());
		}
	}
	
	/** 
	 节点编号
	*/
	public final int getFK_Node(HttpServletRequest request)
	{
		try
		{
			return Integer.parseInt(request.getParameter("FK_Node"));
		}
		catch (java.lang.Exception e)
		{
			return DBAccess.RunSQLReturnValInt("SELECT FK_Node FROM WF_GenerWorkFlow WHERE WorkID=" + this.getWorkID(request));
		}
	}
	
	public final int getWorkID(HttpServletRequest request)
	{
		String workId = request.getParameter("WorkID");
		try
		{
			if(!StringHelper.isNullOrEmpty(workId))
			{
				return Integer.parseInt(workId);
			}
		}
		catch (java.lang.Exception e)
		{
			if(!StringHelper.isNullOrEmpty(workId))
			{
				return Integer.parseInt(workId);
			}
		}
		return 0;
	}


}

package BP;

import BP.DA.DBAccess;
import BP.Port.Emp;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.SendReturnObjs;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Entity.GenerWorkerList;
import BP.WF.Entity.GenerWorkerListAttr;
import BP.WF.Entity.GenerWorkerLists;
import BP.Web.WebUser;


/** 
 发送多人测试
 
*/
public class SendCase 
{
	/** 
	 发送多人测试
	 
	*/
	public SendCase()
	{
//		this.Title = "发送多人测试";
//		this.DescIt = "流程: 023 测试发送给多个人,执行发送后的数据是否符合预期要求.";
//		this.EditState = CT.EditState.Passed;
	}

		///#region 全局变量
	/** 
	 流程编号
	 
	*/
	public String fk_flow = "";
	/** 
	 用户编号
	 
	*/
	public String userNo = "";
	/** 
	 所有的流程
	 
	*/
	public Flow fl = null;
	/** 
	 主线程ID
	 
	*/
	public long workid = 0;
	/** 
	 发送后返回对象
	 
	*/
	public SendReturnObjs objs = null;
	/** 
	 工作人员列表
	 
	*/
	public GenerWorkerList gwl = null;
	/** 
	 流程注册表
	 
	*/
	public GenerWorkFlow gwf = null;
		///#endregion 变量

	/** 
	 发送测试
	 
	*/
	//@Override
	public void Do()
	{
		//初始化变量.
		fk_flow = "023";
		userNo = "zhanghaicheng";
		fl = new Flow(fk_flow);

		//执行登录.
		BP.WF.Dev2Interface.Port_Login(userNo);

		//执行第1步检查，创建工作与发送.
		try {
			this.workid = BP.WF.Dev2Interface.Node_CreateBlankWork(fk_flow, null, null, userNo, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//下一步骤的接受人员定义多个.
		String toEmps = "zhoushengyu,zhangyifan,";

		//向发送多个人.
		objs = BP.WF.Dev2Interface.Node_SendWork(this.fk_flow, this.workid, null, null, 0, toEmps);


		///#region 检查发送变量是否正确?
		if (!toEmps.equals(objs.getVarAcceptersID()))
		{
			throw new RuntimeException("@应该是接受者ID多人，现在是:" + objs.getVarAcceptersID());
		}

		if ( ! objs.getVarAcceptersName().equals("周升雨,张一帆,"))
		{
			throw new RuntimeException("@应该是接受者Name多人，现在是:" + objs.getVarAcceptersID());
		}

		if (objs.getVarCurrNodeID() != 2301)
		{
			throw new RuntimeException("@当前节点应该是 2301 ，现在是:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarToNodeID() != 2302)
		{
			throw new RuntimeException("@到达节点应该是 2302 ，现在是:" + objs.getVarToNodeID());
		}
		///#endregion 检查发送变量是否正确?

		///#region 检查流程引擎表是否正确?
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(this.workid);
		if (gwf.RetrieveFromDBSources() != 1)
		{
			throw new RuntimeException("@丢失了流程引擎注册表数据");
		}
		if (gwf.getFK_Dept() != WebUser.getFK_Dept())
		{
			throw new RuntimeException("@隶属部门错误应当是:" + WebUser.getFK_Dept() + ",现在是:" + gwf.getFK_Dept());
		}

		if (!fk_flow.equals(gwf.getFK_Flow()))
		{
			throw new RuntimeException("@流程编号错误应当是:" + fk_flow + ",现在是:" + gwf.getFK_Flow());
		}

		if (gwf.getFK_Node() != 2302)
		{
			throw new RuntimeException("@当前节点错误应当是:" + 2302 + ",现在是:" + gwf.getFK_Node());
		}

		if (!userNo.equals(gwf.getStarter()))
		{
			throw new RuntimeException("@当前 Starter 错误应当是:" + userNo + ",现在是:" + gwf.getStarter());
		}

		if (gwf.getStarterName() != WebUser.getName())
		{
			throw new RuntimeException("@当前 StarterName 错误应当是:" + WebUser.getName() + ",现在是:" + gwf.getStarterName());
		}

		if (StringHelper.isNullOrEmpty(gwf.getTitle()))
		{
			throw new RuntimeException("@ Title 错误, 不能为空. ");
		}

		// 检查工作人员列表.
		GenerWorkerLists wls = new GenerWorkerLists();
		wls.Retrieve(GenerWorkerListAttr.WorkID, this.workid);
		if (wls.size() != 3)
		{
			throw new RuntimeException("@应当有3条数据，现在是:" + wls.size());
		}
		for (GenerWorkerList wl : GenerWorkerLists.convertGenerWorkerLists(wls))
		{
			if (wl.getFID() != 0)
			{
				throw new RuntimeException("@ FID 错误,应该是" + 0 + ",现在是:" + wl.getFID());
			}

			if (wl.getFK_Emp().equals("zhanghaicheng"))
			{
				if (wl.getFK_Dept() != WebUser.getFK_Dept())
				{
					throw new RuntimeException("@部门错误,应该是" + WebUser.getFK_Dept() + ",现在是:" + wl.getFK_Dept());
				}

				if (!fk_flow.equals(wl.getFK_Flow()))
				{
					throw new RuntimeException("@ FK_Flow 错误,应该是" + fk_flow + ",现在是:" + wl.getFK_Flow());
				}

				if (wl.getFK_Node() != 2301)
				{
					throw new RuntimeException("@ FK_Node 错误,应该是" + 2301 + ",现在是:" + wl.getFK_Node());
				}

				if (!wl.getIsEnable())
				{
					throw new RuntimeException("@ IsEnable 错误,应该是true,现在是:" + wl.getIsEnable());
				}

				if (wl.getIsRead())
				{
					throw new RuntimeException("@ IsRead 错误,应该是false,现在是:" + wl.getIsEnable());
				}

				if (!wl.getIsPass())
				{
					throw new RuntimeException("@ IsPass 错误,应该是true,现在是:" + wl.getIsEnable());
				}

				if (wl.getSender() != WebUser.getNo())
				{
					throw new RuntimeException("@ Sender 错误,应该是:" + WebUser.getNo() + ",现在是:" + wl.getSender());
				}
			}

			if (wl.getFK_Emp().equals("zhoushengyu") || wl.getFK_Emp().equals("zhangyifan"))
			{
				BP.Port.Emp emp = new Emp(wl.getFK_Emp());

				if (wl.getFK_Dept() != emp.getFK_Dept())
				{
					throw new RuntimeException("@部门错误,应该是" + emp.getFK_Dept() + ",现在是:" + wl.getFK_Dept());
				}

				if (!fk_flow.equals(wl.getFK_Flow()))
				{
					throw new RuntimeException("@ FK_Flow 错误,应该是" + fk_flow + ",现在是:" + wl.getFK_Flow());
				}

				if (wl.getFK_Node() != 2302)
				{
					throw new RuntimeException("@ FK_Node 错误,应该是" + 2302 + ",现在是:" + wl.getFK_Node());
				}

				if (!wl.getIsEnable())
				{
					throw new RuntimeException("@ IsEnable 错误,应该是true,现在是:" + wl.getIsEnable());
				}

				if (wl.getIsRead())
				{
					throw new RuntimeException("@ IsRead 错误,应该是false,现在是:" + wl.getIsEnable());
				}

				if (wl.getIsPass())
				{
					throw new RuntimeException("@ IsPass 错误,应该是false,现在是:" + wl.getIsEnable());
				}

				if ( ! wl.getSender().equals("zhanghaicheng"))
				{
					throw new RuntimeException("@ Sender 错误,应该是" + WebUser.getNo() + ",现在是:" + wl.getSender());
				}
			}
		}

		String sql = "SELECT COUNT(*) FROM WF_EmpWorks WHERE WorkID=" + this.workid + " AND FK_Emp='zhoushengyu'";
		if (DBAccess.RunSQLReturnValInt(sql) != 1)
		{
			throw new RuntimeException("@不应该查询不到 zhoushengyu 的待办.");
		}

		sql = "SELECT COUNT(*) FROM WF_EmpWorks WHERE WorkID=" + this.workid + " AND FK_Emp='zhangyifan'";
		if (DBAccess.RunSQLReturnValInt(sql) != 1)
		{
			throw new RuntimeException("@不应该查询不到 zhangyifan 的待办.");
		}
		///#endregion 检查流程引擎表是否正确?

		// 让其中的一个人登录.
		BP.WF.Dev2Interface.Port_Login("zhoushengyu");
		//让他发送.
		BP.WF.Dev2Interface.Node_SendWork(this.fk_flow, this.workid);


		///#region 检查流程引擎表是否正确?
		sql = "SELECT COUNT(*) FROM WF_EmpWorks WHERE WorkID=" + this.workid + " AND FK_Emp='zhoushengyu'";
		if (DBAccess.RunSQLReturnValInt(sql) != 0)
		{
			throw new RuntimeException("@不应该在查询到 zhoushengyu 的待办.");
		}

		sql = "SELECT COUNT(*) FROM WF_EmpWorks WHERE WorkID=" + this.workid + " AND FK_Emp='zhangyifan'";
		if (DBAccess.RunSQLReturnValInt(sql) != 0)
		{
			throw new RuntimeException("@不应该在查询到 zhangyifan 的待办.");
		}
		///#endregion 检查流程引擎表是否正确?


		//删除该测试数据.
		BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(this.fk_flow, workid, true);
	}
}
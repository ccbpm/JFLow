package BP;

import BP.DA.DBAccess;
import BP.DA.DataColumn;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.Port.Emp;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.SendReturnObjs;
import BP.WF.UserInfoShowModel;
import BP.WF.WFState;
import BP.WF.Data.GERptAttr;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Entity.GenerWorkerList;
import BP.Web.WebUser;


/** 
 线性节点的流程发送
 
*/
public class Send024
{
		///#region 变量
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
	public long workID = 0;
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
	public BP.Port.Emp starterEmp = null;
		///#endregion 变量

	/** 
	 线性节点的流程发送
	 
	*/
	public Send024()
	{
//		this.Title = "线性节点的流程发送";
//		this.DescIt = "流程:024简单3节点(合并模式).";
//		this.EditState = EditState.Passed;
	}
	/** 
	 过程说明：
	 1，以流程 024最简单的3节点(轨迹模式)， 为测试用例。
	 2，仅仅测试发送功能，与检查发送后的数据是否完整.
	 3, 此测试有三个节点发起点、中间点、结束点，对应三个测试方法。
	 
	*/
	//@Override
	public void Do()
	{
		///#region 定义变量.
		fk_flow = "024";
		userNo = "zhanghaicheng";
		fl = new Flow(fk_flow);
		///#endregion 定义变量.

		//执行第1步骤. 让 zhanghaicheng 发起流程.
		this.Step1();

		//执行第2步骤. 让zhoupeng 处理.
		this.Step2();

		//执行第3步骤. 让zhanghaicheng 结束.
		this.Step3();
	}
	/** 
	 步骤1 让zhanghaicheng 发起流程.
	 
	*/
	public final void Step1()
	{
		//给发起人赋值.
		starterEmp = new Emp(userNo);

		//让 userNo 登录.
		BP.WF.Dev2Interface.Port_Login(userNo);

		//创建空白工作, 发起开始节点.
		workID = BP.WF.Dev2Interface.Node_CreateBlankWork(fk_flow);

		///#region 检查创建工作是否符合预期.
		//检查流程表的数据.
		String sql = "SELECT * FROM " + fl.getPTable() + " WHERE OID=" + workID;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@流程报表数据被删除了.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(GERptAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("@应当是0");
				}
			}else if(dc.ColumnName.equals(GERptAttr.FK_Dept)){
				if (!val.equals(WebUser.getFK_Dept()))
				{
					throw new RuntimeException("@应当是" + WebUser.getFK_Dept() + ", 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FK_NY)){
				if (!val.equals(DataType.getCurrentYearMonth()))
				{
					throw new RuntimeException("@应当是" + DataType.getCurrentYearMonth() + ", 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowDaySpan)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("@应当是 0 , 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowEmps)){
				if (BP.WF.Glo.getUserInfoShowModel() != UserInfoShowModel.UserNameOnly)
				{
					if (!val.contains(WebUser.getNo()))
					{
						throw new RuntimeException("@应当是包含当前人员, 现在是:" + val);
					}
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowEnder)){
				if (!val.equals(WebUser.getNo()))
				{
					throw new RuntimeException("@应当是 当前人员, 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowEnderRDT)){
				if (val.contains(DataType.getCurrentData()) == false)
				{
					throw new RuntimeException("@应当是 当前日期, 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowEndNode)){
				if (!val.equals("2401"))
				{
					throw new RuntimeException("@应当是 2401, 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowStarter)){
				if (!val.equals(WebUser.getNo()))
				{
					throw new RuntimeException("@应当是  WebUser.getNo(), 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowStartRDT)){
				if (StringHelper.isNullOrEmpty(val))
				{
					throw new RuntimeException("@应当不能为空,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.Title)){
				if (StringHelper.isNullOrEmpty(val))
				{
					throw new RuntimeException("@不能为空title" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.WFState)){
				WFState sta = WFState.forValue(Integer.parseInt(val));
				if (sta != WFState.Blank)
				{
					throw new RuntimeException("@应当是  WFState.Blank 现在是" + sta.toString());
				}
			} 
		}
		///#endregion 检查创建工作是否符合预期

		//执行发送.
		objs = BP.WF.Dev2Interface.Node_SendWork(fk_flow, workID, null, null, 0, "zhoupeng");

		///#region 第1步: 检查发送对象.
		//从获取的发送对象里获取到下一个工作者. zhangyifan(张一帆)、zhoushengyu(周升雨).
		if ( ! objs.getVarAcceptersID().equals("zhoupeng"))
		{
			throw new RuntimeException("@下一步的接受人不正确, 应当是: zhoupeng.现在是:" + objs.getVarAcceptersID());
		}

		if (objs.getVarToNodeID() != 2402)
		{
			throw new RuntimeException("@应该是 2401 节点. 现在是:" + objs.getVarToNodeID());
		}

		if (objs.getVarWorkID() != workID)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 2401)
		{
			throw new RuntimeException("@当前节点的编号不能变化:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarTreadWorkIDs() != null)
		{
			throw new RuntimeException("@不应当获得子线程WorkID.");
		}
		///#endregion 第1步: 检查发送对象.

		///#region 第2步: 检查流程引擎表.
		//检查创建这个空白是否有数据完整?
		sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + workID + " AND FK_Emp='" + objs.getVarAcceptersID() + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该找不到当前人员的待办.");
		}

		gwf = new GenerWorkFlow(workID);
		if (gwf.getStarter() != WebUser.getNo() || gwf.getStarterName() != WebUser.getName())
		{
			throw new RuntimeException("没有写入发起人的信息.");
		}

		if (gwf.getFK_Dept() != starterEmp.getFK_Dept())
		{
			throw new RuntimeException("@发起人的部门有变化，应当是" + starterEmp.getFK_Dept() + ",现在是:" + gwf.getFK_Dept());
		}

		if (gwf.getStarter() != starterEmp.getNo())
		{
			throw new RuntimeException("@发起人的 No 有变化，应当是" + starterEmp.getNo() + ",现在是:" + gwf.getStarter());
		}

		//判断当前点.
		if (gwf.getFK_Node() != 2402)
		{
			throw new RuntimeException("@当前点应该是 2402 现在是:" + gwf.getFK_Node());
		}

		//判断当前点.
		if (gwf.getFID() != 0)
		{
			throw new RuntimeException("@当前点应该是 FID=0  现在是:" + gwf.getFID());
		}

		//判断PWorkID，没有谁调用它，应当是 0. 
		if (gwf.getPWorkID() != 0)
		{
			throw new RuntimeException("@没有谁调用它, 当前点应该是 PWorkID=0  现在是:" + gwf.getPWorkID());
		}

		//判断 WFState . 
		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@应当是 WFState=Runing 现在是:" + gwf.getWFState().toString());
		}

		//检查开始节点 发送人的WF_GenerWorkerList 的.
		gwl = new GenerWorkerList();
		gwl.setFK_Emp(WebUser.getNo());
		gwl.setFK_Node(2401);
		gwl.setWorkID(workID);
		gwl.Retrieve();

		// 没有分合流应当是 0 .
		if (gwl.getFID() != 0)
		{
			throw new RuntimeException("@没有分合流应当是 0.");
		}

		if (!gwl.getIsEnable())
		{
			throw new RuntimeException("@应该是启用的状态 ");
		}

		if (!gwl.getIsPass())
		{
			throw new RuntimeException("@应该是通过的状态 ");
		}

		if (!gwl.getSender().contains(WebUser.getNo()))
		{
			throw new RuntimeException("@应该是 包含当前状态 . ");
		}


		//检查接受人的 WF_GenerWorkerList 的.
		gwl = new GenerWorkerList();
		gwl.setFK_Emp(objs.getVarAcceptersID());
		gwl.setFK_Node(2402);
		gwl.setWorkID(workID);
		gwl.Retrieve();

		// 没有分合流应当是 0 .
		if (gwl.getFID() != 0)
		{
			throw new RuntimeException("@没有分合流应当是 0.");
		}

		if (!gwl.getIsEnable())
		{
			throw new RuntimeException("@应该是启用的状态 ");
		}

		if (gwl.getIsPass())
		{
			throw new RuntimeException("@应该是未通过的状态 ");
		}

		if (!gwl.getSender().contains(WebUser.getNo()))
		{
			throw new RuntimeException("@应该是 当前人发送的，现在是: " + gwl.getSender());
		}
		///#endregion 第2步: 检查流程引擎表.

		///#region 第3步: 检查节点数据表.
		//检查流程表的数据.
		sql = "SELECT * FROM " + fl.getPTable() + " WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@流程报表数据被删除了.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(GERptAttr.PWorkID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("@PWorkID应当是0, 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.PFlowNo)){
				if (!val.equals(""))
				{
					throw new RuntimeException("@PFlowNo应当是 '' 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("@应当是0");
				}
			}else if(dc.ColumnName.equals(GERptAttr.FK_Dept)){
				if (!val.equals(starterEmp.getFK_Dept()))
				{
					throw new RuntimeException("@应当是" + starterEmp.getFK_Dept() + ", 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FK_NY)){
				if (!val.equals(DataType.getCurrentYearMonth()))
				{
					throw new RuntimeException("@应当是" + DataType.getCurrentYearMonth() + ", 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowDaySpan)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("@应当是 0 , 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowEmps)){
				if (BP.WF.Glo.getUserInfoShowModel() != UserInfoShowModel.UserNameOnly)
				{
					if (val.contains(WebUser.getNo()) == false)
					{
						throw new RuntimeException("@应当是包含当前人员, 现在是:" + val);
					}
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowEnder)){
				if (!val.equals(WebUser.getNo()))
				{
					throw new RuntimeException("@应当是 当前人员, 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowEnderRDT)){
				if (val.contains(DataType.getCurrentData()) == false)
				{
					throw new RuntimeException("@应当是 当前日期, 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowEndNode)){
				if (!val.equals("2402"))
				{
					throw new RuntimeException("@应当是 2402, 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowStarter)){
				if (!val.equals(starterEmp.getNo()))
				{
					throw new RuntimeException("@应当是  " + starterEmp.getNo() + ", 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowStartRDT)){
				if (StringHelper.isNullOrEmpty(val))
				{
					throw new RuntimeException("@应当不能为空,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.Title)){
				if (StringHelper.isNullOrEmpty(val))
				{
					throw new RuntimeException("@不能为空title" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.WFState)){
				WFState sta = WFState.forValue(Integer.parseInt(val));
				if (sta != WFState.Runing)
				{
					throw new RuntimeException("@应当是  WFState.Runing 现在是" + sta.toString());
				}
			}
		}
		///#endregion 第3步: 检查节点数据表.
	}

	/** 
	 步骤1 让zhoupeng 登录去处理.
	 
	*/
	public final void Step2()
	{
		//让 zhouepng 登录.
		BP.WF.Dev2Interface.Port_Login("zhoupeng");

		//让他向下发送.
		objs = BP.WF.Dev2Interface.Node_SendWork(fk_flow, workID, null, null, 0, "zhanghaicheng");

		///#region 第1步: 检查发送对象.
		//从获取的发送对象里获取到下一个工作者. zhangyifan(张一帆)、zhoushengyu(周升雨).
		if ( ! objs.getVarAcceptersID().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@下一步的接受人不正确, 应当是: zhanghaicheng.现在是:" + objs.getVarAcceptersID());
		}

		if (objs.getVarToNodeID() != 2499)
		{
			throw new RuntimeException("@应该是 2499 节点. 现在是:" + objs.getVarToNodeID());
		}

		if (objs.getVarWorkID() != workID)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 2402)
		{
			throw new RuntimeException("@当前节点的编号不能变化,现在是:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarTreadWorkIDs() != null)
		{
			throw new RuntimeException("@不应当获得子线程WorkID.");
		}

		///#endregion 第1步: 检查发送对象.

		///#region 第2步: 检查流程引擎表.
		//检查待办是否存在。
		String sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + workID + " AND FK_Emp='" + objs.getVarAcceptersID() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该找不到当前人员的待办.");
		}

		gwf = new GenerWorkFlow(workID);
		if ( ! gwf.getStarter().equals("zhanghaicheng"))
		{
			throw new RuntimeException("发起人信息有变化.");
		}

		if (gwf.getFK_Dept() != starterEmp.getFK_Dept())
		{
			throw new RuntimeException("@发起人的部门有变化，应当是" + starterEmp.getFK_Dept() + ",现在是:" + gwf.getFK_Dept());
		}

		if (gwf.getStarter() != starterEmp.getNo())
		{
			throw new RuntimeException("@发起人的 No 有变化，应当是" + starterEmp.getNo() + ",现在是:" + gwf.getStarter());
		}


		//判断当前点.
		if (gwf.getFK_Node() != 2499)
		{
			throw new RuntimeException("@当前点应该是 2499 现在是:" + gwf.getFK_Node());
		}

		//判断当前点.
		if (gwf.getFID() != 0)
		{
			throw new RuntimeException("@当前点应该是 FID=0  现在是:" + gwf.getFID());
		}

		//判断PWorkID，没有谁调用它，应当是 0. 
		if (gwf.getPWorkID() != 0)
		{
			throw new RuntimeException("@没有谁调用它, 当前点应该是 PWorkID=0  现在是:" + gwf.getPWorkID());
		}

		//判断 WFState . 
		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@应当是 WFState=Runing 现在是:" + gwf.getWFState().toString());
		}

		//检查开始节点 发送人的WF_GenerWorkerList 的.
		gwl = new GenerWorkerList();
		gwl.setFK_Emp(WebUser.getNo());
		gwl.setFK_Node(2402);
		gwl.setWorkID(workID);
		gwl.Retrieve();

		// 没有分合流应当是 0 .
		if (gwl.getFID() != 0)
		{
			throw new RuntimeException("@没有分合流应当是 0.");
		}

		if (!gwl.getIsEnable())
		{
			throw new RuntimeException("@应该是启用的状态 ");
		}

		if (!gwl.getIsPass())
		{
			throw new RuntimeException("@应该是通过的状态 ");
		}

		if (!gwl.getSender().contains("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是 包含当前状态 . ");
		}

		//检查接受人的 WF_GenerWorkerList 的.
		gwl = new GenerWorkerList();
		gwl.setFK_Emp(objs.getVarAcceptersID());
		gwl.setFK_Node(2499);
		gwl.setWorkID(workID);
		gwl.Retrieve();

		// 没有分合流应当是 0 .
		if (gwl.getFID() != 0)
		{
			throw new RuntimeException("@没有分合流应当是 0.");
		}

		if (!gwl.getIsEnable())
		{
			throw new RuntimeException("@应该是启用的状态 ");
		}

		if (gwl.getIsPass())
		{
			throw new RuntimeException("@应该是未通过的状态 ");
		}

		if (!gwl.getSender().contains(WebUser.getNo()))
		{
			throw new RuntimeException("@应该是 当前人发送的，现在是: " + gwl.getSender());
		}
		///#endregion 第2步: 检查流程引擎表.

		///#region 第3步: 检查节点数据表.
		//检查流程表的数据.
		sql = "SELECT * FROM " + fl.getPTable() + " WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@流程报表数据被删除了.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(GERptAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("@应当是0");
				}
			}else if(dc.ColumnName.equals(GERptAttr.FK_Dept)){
				BP.Port.Emp emp = new Emp("zhanghaicheng");
				if (!val.equals(emp.getFK_Dept()))
				{
					throw new RuntimeException("@应当是" + emp.getFK_Dept() + ", 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FK_NY)){
				if (!val.equals(DataType.getCurrentYearMonth()))
				{
					throw new RuntimeException("@应当是" + DataType.getCurrentYearMonth() + ", 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowDaySpan)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("@应当是 0 , 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowEmps)){
				if (BP.WF.Glo.getUserInfoShowModel() != UserInfoShowModel.UserNameOnly)
				{
					if (val.contains(WebUser.getNo()) == false)
					{
						throw new RuntimeException("@应当是包含当前人员, 现在是:" + val);
					}
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowEnder)){
				if (!val.equals(WebUser.getNo()))
				{
					throw new RuntimeException("@应当是 当前人员, 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowEnderRDT)){
				if (val.contains(DataType.getCurrentData()) == false)
				{
					throw new RuntimeException("@应当是 当前日期, 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowEndNode)){
				if (!val.equals("2499"))
				{
					throw new RuntimeException("@应当是 2499, 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowStarter)){
				if (val.equals(WebUser.getNo()))
				{
					throw new RuntimeException("@应当是  WebUser.getNo(), 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowStartRDT)){
				if (StringHelper.isNullOrEmpty(val))
				{
					throw new RuntimeException("@应当不能为空,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.Title)){
				if (StringHelper.isNullOrEmpty(val))
				{
					throw new RuntimeException("@不能为空title" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.WFState)){
				if (Integer.parseInt(val) != Integer.parseInt(WFState.Runing.toString()))
				{
					throw new RuntimeException("@应当是  WFState.Runing 现在是" + val);
				}
			}
		}
		///#endregion 第3步: 检查节点数据表.
	}

	/** 
	 步骤3 让zhanghaicheng 结束流程.
	 
	*/
	public final void Step3()
	{
		//让 zhanghaicheng 登录.
		BP.WF.Dev2Interface.Port_Login("zhanghaicheng");

		//让他向下发送.
		objs = BP.WF.Dev2Interface.Node_SendWork(fk_flow, workID);

		///#region 第1步: 检查发送对象.
		//从获取的发送对象里获取到下一个工作者. zhangyifan(张一帆)、zhoushengyu(周升雨).
		if (objs.getVarAcceptersID() != null)
		{
			throw new RuntimeException("@接受人员应当为空." + objs.getVarAcceptersID());
		}

		if (objs.getVarToNodeID() != 0)
		{
			throw new RuntimeException("@应当是 0  现在是:" + objs.getVarToNodeID());
		}

		if (objs.getVarWorkID() != workID)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 2499)
		{
			throw new RuntimeException("@当前节点的编号不能变化,现在是:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarTreadWorkIDs() != null)
		{
			throw new RuntimeException("@不应当获得子线程WorkID.");
		}

		///#endregion 第1步: 检查发送对象.

		///#region 第2步: 检查流程引擎表.
		//检查待办是否存在。
		String sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + workID;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@不应该有待办.");
		}


		sql = "SELECT * FROM WF_GenerWorkFlow WHERE WorkID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@流程信息未删除.");
		}

		sql = "SELECT * FROM WF_GenerWorkerList  WHERE WorkID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@工作人员信息未删除..");
		}
		///#endregion 第2步: 检查流程引擎表.

		///#region 第3步: 检查节点数据表.
		BP.Port.Emp emp = new Emp("zhanghaicheng");

		//检查流程表的数据.
		sql = "SELECT * FROM " + fl.getPTable() + " WHERE OID=" + workID;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@流程报表数据被删除了.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(GERptAttr.Title)){
				if (StringHelper.isNullOrEmpty(val))
				{
					throw new RuntimeException("@流程走完后标题丢失了");
				}
			}else if(dc.ColumnName.equals(GERptAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("@应当是0");
				}
			}else if(dc.ColumnName.equals(GERptAttr.FK_Dept)){
				if (!val.equals(emp.getFK_Dept()))
				{
					throw new RuntimeException("@发起人的部门发生了变化，应当是("+emp.getFK_Dept()+"),现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FK_NY)){
				if (!val.equals(DataType.getCurrentYearMonth()))
				{
					throw new RuntimeException("@应当是" + DataType.getCurrentYearMonth() + ", 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowDaySpan)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("@应当是 0 , 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowEmps)){
				if (BP.WF.Glo.getUserInfoShowModel() != UserInfoShowModel.UserNameOnly)
				{
					if (val.contains("zhanghaicheng") == false || val.contains("zhoupeng") == false)
					{
						throw new RuntimeException("@应当包含的人员，现在不存在, 现在是:" + val);
					}
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowEnder)){
				if (!val.equals("zhanghaicheng"))
				{
					throw new RuntimeException("@应当是 zhanghaicheng , 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowEnderRDT)){
				if (val.contains(DataType.getCurrentData()) == false)
				{
					throw new RuntimeException("@应当是 当前日期, 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowEndNode)){
				if (!val.equals("2499"))
				{
					throw new RuntimeException("@应当是 2499, 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowStarter)){
				if (!val.equals("zhanghaicheng"))
				{
					throw new RuntimeException("@应当是 zhanghaicheng, 现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.FlowStartRDT)){
				if (StringHelper.isNullOrEmpty(val))
				{
					throw new RuntimeException("@应当不能为空,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.WFState)){
				if (Integer.parseInt(val) != Integer.parseInt(WFState.Complete.toString()))
				{
					throw new RuntimeException("@应当是  WFState.Complete 现在是" + val);
				}
			}
		}
		///#endregion 第3步: 检查节点数据表.
	}
}
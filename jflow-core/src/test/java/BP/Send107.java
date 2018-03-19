package BP;

import org.junit.Before;
import org.junit.Test;

import BP.DA.DBAccess;
import BP.DA.DataColumn;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.SendReturnObjs;
import BP.WF.WFState;
import BP.WF.WorkAttr;
import BP.WF.Data.GERptAttr;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Entity.GenerWorkerList;
import BP.Web.WebUser;

public class Send107 
{
	@Before
	public void setUp() throws Exception {
		SystemConfig.ReadConfigFile(this.getClass().getResourceAsStream("/conf/web.config"));
	}
	/** 
	 父子流程
	 
	*/
	public Send107()
	{
//		this.Title = "科技计划审核审批流程";
//		this.DescIt = "流程:科技计划审核审批流程（父子流程）,执行发送后的数据是否符合预期要求.";
//		this.EditState = CT.EditState.Passed;
	}
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
	public long workid = 0;
	public long newworkid = 0;
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
	 测试案例说明:
	 1, 此流程针对于科技计划审核审批流程进行
	 2, 测试方法体分成三大部分. 发起，子流程处理，执行结束。
	 3，针对发送测试，不涉及到其它的功能.
	 
	*/
	//@Override
	@Test
	public void Do()
	{
		//初始化变量.
		fk_flow = "123";
		userNo = "zhanghaicheng";

		fl = new Flow(fk_flow);

		Step1();
		Step2();
		Step3();
		Step4();
		Step5();
		Step6();
		Step7();
		Step8_1();
		Step8_2();
		Step8_3();
		Step8_4();
		Step9_1();
		Step9_2();
		Step9_3();
		Step9_4();
		Step10();
		Step11();
	}
	public final void Step1()
	{
		// 让zhanghaicheng 登录.
		BP.WF.Dev2Interface.Port_Login(userNo);

		//创建空白工作, 发起开始节点.
		workid = BP.WF.Dev2Interface.Node_CreateBlankWork(fk_flow);

		///#region 检查 创建流程后的数据是否完整 ？
		// "检查创建这个空白是否有数据完整?;
		String sql = "SELECT * FROM " + fl.getPTable() + " WHERE OID=" + workid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到报表数据.");
		}

		// 检查节点表单表是否有数据?;
		sql = "SELECT * FROM ND12301 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该在开始节点表单表中找不到数据，");
		}

		if (!dt.getValue(0, "Rec").toString().equals(WebUser.getNo()))
		{
			throw new RuntimeException("@记录人应该是当前人员.");
		}

		// 检查创建这个空白是否有数据完整?;
		sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + workid + " AND FK_Emp='" + WebUser.getNo() + "'";
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@找到当前人员的待办就是错误的.");
		}
		///#endregion 检查发起流程后的数据是否完整？

		//开始节点:执行发送,并获取发送对象.
		SendReturnObjs objs = BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid);

		///#region 第1步: 检查【开始节点】发送对象返回的信息是否完整？
		//从获取的发送对象里获取到下一个工作者. zhangyifan(张一帆)
		if ( ! objs.getVarAcceptersID().equals("zhangyifan"))
		{
			throw new RuntimeException("@下一步的接受人不正确,  zhangyifan 现在是:" + objs.getVarAcceptersID());
		}

		if (objs.getVarToNodeID() != 12302)
		{
			throw new RuntimeException("@应该是 12302节点. 现在是:" + objs.getVarToNodeID());
		}

		if (objs.getVarWorkID() != workid)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 12301)
		{
			throw new RuntimeException("@当前节点的编号不能变化:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarTreadWorkIDs() != null)
		{
			throw new RuntimeException("@不应当获得子线程WorkID.");
		}
		///#endregion  检查【开始节点】发送对象返回的信息是否完整？

		///#region 第2步: 检查流程引擎控制系统表是否符合预期.
		gwf = new GenerWorkFlow(workid);

		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@WFState 应该 WFState.Runing ：" + gwf.getWFState().toString());
		}

		if (gwf.getStarter() != WebUser.getNo())
		{
			throw new RuntimeException("@应该是发起人员，现在是:" + gwf.getStarter());
		}
		///#endregion end 检查流程引擎控制系统表是否符合预期.

		///#region 第3步: 检查【开始节点】发送节点表单-数据信息否完整？
		//检查节点表单表是否有数据，以及数据是否正确？
		// 检查节点表单表是否有数据?;
		sql = "SELECT * FROM ND12301 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该在开始节点表单表中找不到数据，");
		}

		if (!dt.getValue(0, "Rec").toString().equals(WebUser.getNo()))
		{
			throw new RuntimeException("@记录人应该是zhanghaicheng.");
		}

		sql = "SELECT * FROM ND12302 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12302 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("zhangyifan"))
				{
					throw new RuntimeException("第二步骤的处理人员,应当zhangyifan ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.Rec)){
				if (!val.equals("zhanghaicheng"))
				{
					throw new RuntimeException("应当 Rec=zhanghaicheng,现在是:" + val);
				}
			}
			
		}
		sql = "SELECT * FROM  ND123Rpt WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.getValue(0, GERptAttr.FlowEnder).toString().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是 zhanghaicheng 是 FlowEnder .");
		}

		if (!dt.getValue(0, GERptAttr.FlowStarter).toString().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是 zhanghaicheng 是 FlowStarter .");
		}

		if (!dt.getValue(0, GERptAttr.FlowEndNode).toString().equals("12302"))
		{
			throw new RuntimeException("@应该是 12302 是 FlowEndNode .");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.WFState).toString()) != Integer.parseInt(WFState.Runing.toString()))
		{
			throw new RuntimeException("@应该是 WFState.Runing 是当前的状态。");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}
		///#endregion  检查【开始节点】发送数据信息否完整？
	}
	public final void Step2()
	{
		// 让zhangyifan 登录.
		BP.WF.Dev2Interface.Port_Login("zhangyifan");
		//让他向下发送.
		objs = BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid);

		///#region 第1步: 检查发送对象返回的信息是否完整？
		//从获取的发送对象里获取到下一个工作者. zhoushengyu(周升雨)
		if ( ! objs.getVarAcceptersID().equals("zhoushengyu"))
		{
			throw new RuntimeException("@下一步的接受人不正确,  zhoushengyu 现在是:" + objs.getVarAcceptersID());
		}

		if (objs.getVarToNodeID() != 12303)
		{
			throw new RuntimeException("@应该是 12303节点. 现在是:" + objs.getVarToNodeID());
		}

		if (objs.getVarWorkID() != workid)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 12302)
		{
			throw new RuntimeException("@当前节点的编号不能变化:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarTreadWorkIDs() != null)
		{
			throw new RuntimeException("@不应当获得子线程WorkID.");
		}
		///#endregion  检查发送对象返回的信息是否完整？

		///#region 第2步: 检查流程引擎控制系统表是否符合预期.
		String sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + workid + " AND FK_Emp='" + objs.getVarAcceptersID() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该找不到当前人员的待办.");
		}

		gwf = new GenerWorkFlow(workid);
		//判断当前节点ID
		if (gwf.getFK_Node() != 12303)
		{
			throw new RuntimeException("@当前节点FK_Node应该不变化，现在：" + gwf.getFK_Node());
		}
		//判断流程运行状态
		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@WFState 应该 WFState.Runing ：" + gwf.getWFState().toString());
		}
		//判断流程发起人是否变化
		if ( ! gwf.getStarter().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是发起人员 zhanghaicheng，现在是:" + gwf.getStarter());
		}
		//判断当前点.
		if (gwf.getFID() != 0)
		{
			throw new RuntimeException("@当前点应该是 FID=0  现在是:" + gwf.getFID());
		}
		///#endregion end 检查流程引擎控制系统表是否符合预期.

		///#region 第3步: 检查发送节点表单-数据信息否完整？
		//检查节点表单表是否有数据，以及数据是否正确？
		sql = "SELECT * FROM ND12302 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12302 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("zhangyifan"))
				{
					throw new RuntimeException("第二步骤的处理人员,应当zhangyifan ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12303 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12303 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (val.contains("zhoushengyu") == false)
				{
					throw new RuntimeException("第三步骤的处理人员,应当zhoushengyu ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM  ND123Rpt WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.getValue(0, GERptAttr.FlowEnder).toString().equals("zhangyifan"))
		{
			throw new RuntimeException("@应该是 zhangyifan 是 FlowEnder .");
		}

		if (!dt.getValue(0, GERptAttr.FlowStarter).toString().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是 zhanghaicheng 是 FlowStarter .");
		}

		if (!dt.getValue(0, GERptAttr.FlowEndNode).toString().equals("12303"))
		{
			throw new RuntimeException("@应该是 12303 是 FlowEndNode .");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.WFState).toString()) != Integer.parseInt(WFState.Runing.toString()))
		{
			throw new RuntimeException("@应该是 WFState.Runing 是当前的状态。");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}
		///#endregion  检查【开始节点】发送数据信息否完整？
	}
	public final void Step3()
	{
		//让zhoushengyu登录
		BP.WF.Dev2Interface.Port_Login("zhoushengyu");
		//让他执行发送
		objs=BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid);

		///#region 第1步: 检查发送对象返回的信息是否完整？
		//从获取的发送对象里获取到下一个工作者. liyan(李言)
		if ( ! objs.getVarAcceptersID().equals("liyan"))
		{
			throw new RuntimeException("@下一步的接受人不正确,  liyan 现在是:" + objs.getVarAcceptersID());
		}

		if (objs.getVarToNodeID() != 12304)
		{
			throw new RuntimeException("@应该是 12304节点. 现在是:" + objs.getVarToNodeID());
		}

		if (objs.getVarWorkID() != workid)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 12303)
		{
			throw new RuntimeException("@当前节点的编号不能变化:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarTreadWorkIDs() != null)
		{
			throw new RuntimeException("@不应当获得子线程WorkID.");
		}
		///#endregion  检查发送对象返回的信息是否完整？

		///#region 第2步: 检查流程引擎控制系统表是否符合预期.
		String sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + workid + " AND FK_Emp='" + objs.getVarAcceptersID() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该找不到当前人员的待办.");
		}

		gwf = new GenerWorkFlow(workid);
		//判断当前节点ID
		if (gwf.getFK_Node() != 12304)
		{
			throw new RuntimeException("@当前节点FK_Node应该不变化，现在：" + gwf.getFK_Node());
		}
		//判断流程运行状态
		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@WFState 应该 WFState.Runing ：" + gwf.getWFState().toString());
		}
		//判断流程发起人是否变化
		if ( ! gwf.getStarter().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是发起人员 zhanghaicheng，现在是:" + gwf.getStarter());
		}
		//判断当前点.
		if (gwf.getFID() != 0)
		{
			throw new RuntimeException("@当前点应该是 FID=0  现在是:" + gwf.getFID());
		}
		///#endregion end 检查流程引擎控制系统表是否符合预期.

		///#region 第3步: 检查发送节点表单-数据信息否完整？
		//检查节点表单表是否有数据，以及数据是否正确？
		sql = "SELECT * FROM ND12302 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12302 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("zhangyifan"))
				{
					throw new RuntimeException("第二步骤的处理人员,应当zhangyifan ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12303 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12303 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("zhoushengyu"))
				{
					throw new RuntimeException("第三步骤的处理人员,应当zhoushengyu ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12304 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12304 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("liyan"))
				{
					throw new RuntimeException("第四步骤的处理人员,应当liyan ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM  ND123Rpt WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.getValue(0, GERptAttr.FlowEnder).toString().equals("zhoushengyu"))
		{
			throw new RuntimeException("@应该是 zhoushengyu 是 FlowEnder .");
		}

		if (!dt.getValue(0, GERptAttr.FlowStarter).toString().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是 zhanghaicheng 是 FlowStarter .");
		}

		if (!dt.getValue(0, GERptAttr.FlowEndNode).toString().equals("12304"))
		{
			throw new RuntimeException("@应该是 12304 是 FlowEndNode .");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.WFState).toString()) != Integer.parseInt(WFState.Runing.toString()))
		{
			throw new RuntimeException("@应该是 WFState.Runing 是当前的状态。");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}
		///#endregion  检查【开始节点】发送数据信息否完整？
	}
	public final void Step4()
	{
		//让liyan登录
		BP.WF.Dev2Interface.Port_Login("liyan");
		//让他执行发送
		objs=BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid);

		///#region 第1步: 检查发送对象返回的信息是否完整？
		//从获取的发送对象里获取到下一个工作者. liping(李萍)
		if ( ! objs.getVarAcceptersID().equals("liping"))
		{
			throw new RuntimeException("@下一步的接受人不正确,  liping 现在是:" + objs.getVarAcceptersID());
		}

		if (objs.getVarToNodeID() != 12305)
		{
			throw new RuntimeException("@应该是 12305节点. 现在是:" + objs.getVarToNodeID());
		}

		if (objs.getVarWorkID() != workid)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 12304)
		{
			throw new RuntimeException("@当前节点的编号不能变化:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarTreadWorkIDs() != null)
		{
			throw new RuntimeException("@不应当获得子线程WorkID.");
		}
		///#endregion  检查发送对象返回的信息是否完整？

		///#region 第2步: 检查流程引擎控制系统表是否符合预期.
		String sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + workid + " AND FK_Emp='" + objs.getVarAcceptersID() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该找不到当前人员的待办.");
		}

		gwf = new GenerWorkFlow(workid);
		//判断当前节点ID
		if (gwf.getFK_Node() != 12305)
		{
			throw new RuntimeException("@当前节点FK_Node应该不变化，现在：" + gwf.getFK_Node());
		}
		//判断流程运行状态
		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@WFState 应该 WFState.Runing ：" + gwf.getWFState().toString());
		}
		//判断流程发起人是否变化
		if ( ! gwf.getStarter().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是发起人员 zhanghaicheng，现在是:" + gwf.getStarter());
		}
		//判断当前点.
		if (gwf.getFID() != 0)
		{
			throw new RuntimeException("@当前点应该是 FID=0  现在是:" + gwf.getFID());
		}
		///#endregion end 检查流程引擎控制系统表是否符合预期.

		///#region 第3步: 检查发送节点表单-数据信息否完整？
		//检查节点表单表是否有数据，以及数据是否正确？
		sql = "SELECT * FROM ND12302 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12302 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("zhangyifan"))
				{
					throw new RuntimeException("第二步骤的处理人员,应当zhangyifan ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12303 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12303 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (val.contains("zhoushengyu") == false)
				{
					throw new RuntimeException("第三步骤的处理人员,应当zhoushengyu ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12304 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12304 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (val.contains("liyan") == false)
				{
					throw new RuntimeException("第四步骤的处理人员,应当liyan ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12305 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12305 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("liping"))
				{
					throw new RuntimeException("第五步骤的处理人员,应当liping ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM  ND123Rpt WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.getValue(0, GERptAttr.FlowEnder).toString().equals("liyan"))
		{
			throw new RuntimeException("@应该是 liyan 是 FlowEnder .");
		}

		if (!dt.getValue(0, GERptAttr.FlowStarter).toString().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是 zhanghaicheng 是 FlowStarter .");
		}

		if (!dt.getValue(0, GERptAttr.FlowEndNode).toString().equals("12305"))
		{
			throw new RuntimeException("@应该是 12305 是 FlowEndNode .");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.WFState).toString()) != Integer.parseInt(WFState.Runing.toString()))
		{
			throw new RuntimeException("@应该是 WFState.Runing 是当前的状态。");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}
		///#endregion  检查【开始节点】发送数据信息否完整？
	}
	public final void Step5()
	{
		//让liping登录
		BP.WF.Dev2Interface.Port_Login("liping");
		//让他执行发送
		objs=BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid);

		///#region 第1步: 检查发送对象返回的信息是否完整？
		//从获取的发送对象里获取到下一个工作者. fuhui(福惠)
		if ( ! objs.getVarAcceptersID().equals("fuhui"))
		{
			throw new RuntimeException("@下一步的接受人不正确,  fuhui 现在是:" + objs.getVarAcceptersID());
		}

		if (objs.getVarToNodeID() != 12306)
		{
			throw new RuntimeException("@应该是 12306节点. 现在是:" + objs.getVarToNodeID());
		}

		if (objs.getVarWorkID() != workid)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 12305)
		{
			throw new RuntimeException("@当前节点的编号不能变化:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarTreadWorkIDs() != null)
		{
			throw new RuntimeException("@不应当获得子线程WorkID.");
		}
		///#endregion  检查发送对象返回的信息是否完整？

		///#region 第2步: 检查流程引擎控制系统表是否符合预期.
		String sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + workid + " AND FK_Emp='" + objs.getVarAcceptersID() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该找不到当前人员的待办.");
		}

		gwf = new GenerWorkFlow(workid);
		//判断当前节点ID
		if (gwf.getFK_Node() != 12306)
		{
			throw new RuntimeException("@当前节点FK_Node应该不变化，现在：" + gwf.getFK_Node());
		}
		//判断流程运行状态
		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@WFState 应该 WFState.Runing ：" + gwf.getWFState().toString());
		}
		//判断流程发起人是否变化
		if ( ! gwf.getStarter().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是发起人员 zhanghaicheng，现在是:" + gwf.getStarter());
		}
		//判断当前点.
		if (gwf.getFID() != 0)
		{
			throw new RuntimeException("@当前点应该是 FID=0  现在是:" + gwf.getFID());
		}
		///#endregion end 检查流程引擎控制系统表是否符合预期.

		///#region 第3步: 检查发送节点表单-数据信息否完整？
		//检查节点表单表是否有数据，以及数据是否正确？
		sql = "SELECT * FROM ND12302 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12302 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("zhangyifan"))
				{
					throw new RuntimeException("第二步骤的处理人员,应当zhangyifan ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12303 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12303 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("zhoushengyu"))
				{
					throw new RuntimeException("第三步骤的处理人员,应当zhoushengyu ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12304 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12304 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("liyan"))
				{
					throw new RuntimeException("第四步骤的处理人员,应当liyan ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			} 
		}
		sql = "SELECT * FROM ND12305 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12305 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("liping"))
				{
					throw new RuntimeException("第五步骤的处理人员,应当liping ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12306 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12306 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("fuhui"))
				{
					throw new RuntimeException("第六步骤的处理人员,应当fuhui ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM  ND123Rpt WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.getValue(0, GERptAttr.FlowEnder).toString().equals("liping"))
		{
			throw new RuntimeException("@应该是 liping 是 FlowEnder .");
		}

		if (!dt.getValue(0, GERptAttr.FlowStarter).toString().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是 zhanghaicheng 是 FlowStarter .");
		}

		if (!dt.getValue(0, GERptAttr.FlowEndNode).toString().equals("12306"))
		{
			throw new RuntimeException("@应该是 12306 是 FlowEndNode .");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.WFState).toString()) != Integer.parseInt(WFState.Runing.toString()))
		{
			throw new RuntimeException("@应该是 WFState.Runing 是当前的状态。");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}
		///#endregion  检查【开始节点】发送数据信息否完整？
	}
	public final void Step6()
	{
		//让fuhui登录
		BP.WF.Dev2Interface.Port_Login("fuhui");
		//让他执行发送
		objs=BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid);

		///#region 第1步: 检查发送对象返回的信息是否完整？
		//从获取的发送对象里获取到下一个工作者. zhoutianjiao(周天娇)
		if ( ! objs.getVarAcceptersID().equals("zhoutianjiao"))
		{
			throw new RuntimeException("@下一步的接受人不正确,  zhoutianjiao 现在是:" + objs.getVarAcceptersID());
		}

		if (objs.getVarToNodeID() != 12307)
		{
			throw new RuntimeException("@应该是 12307节点. 现在是:" + objs.getVarToNodeID());
		}

		if (objs.getVarWorkID() != workid)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 12306)
		{
			throw new RuntimeException("@当前节点的编号不能变化:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarTreadWorkIDs() != null)
		{
			throw new RuntimeException("@不应当获得子线程WorkID.");
		}
		///#endregion  检查发送对象返回的信息是否完整？

		///#region 第2步: 检查流程引擎控制系统表是否符合预期.
		String sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + workid + " AND FK_Emp='" + objs.getVarAcceptersID() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该找不到当前人员的待办.");
		}

		gwf = new GenerWorkFlow(workid);
		//判断当前节点ID
		if (gwf.getFK_Node() != 12307)
		{
			throw new RuntimeException("@当前节点FK_Node应该不变化，现在：" + gwf.getFK_Node());
		}
		//判断流程运行状态
		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@WFState 应该 WFState.Runing ：" + gwf.getWFState().toString());
		}
		//判断流程发起人是否变化
		if ( ! gwf.getStarter().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是发起人员 zhanghaicheng，现在是:" + gwf.getStarter());
		}
		//判断当前点.
		if (gwf.getFID() != 0)
		{
			throw new RuntimeException("@当前点应该是 FID=0  现在是:" + gwf.getFID());
		}
		///#endregion end 检查流程引擎控制系统表是否符合预期.

		///#region 第3步: 检查发送节点表单-数据信息否完整？
		//检查节点表单表是否有数据，以及数据是否正确？
		sql = "SELECT * FROM ND12302 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12302 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("zhangyifan"))
				{
					throw new RuntimeException("第二步骤的处理人员,应当zhangyifan ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12303 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12303 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("zhoushengyu"))
				{
					throw new RuntimeException("第三步骤的处理人员,应当zhoushengyu ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12304 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12304 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("liyan"))
				{
					throw new RuntimeException("第四步骤的处理人员,应当liyan ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12305 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12305 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("liping"))
				{
					throw new RuntimeException("第五步骤的处理人员,应当liping ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12306 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12306 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("fuhui"))
				{
					throw new RuntimeException("第六步骤的处理人员,应当fuhui ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12307 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12307 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("zhoutianjiao"))
				{
					throw new RuntimeException("第七步骤的处理人员,应当zhoutianjiao ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM  ND123Rpt WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.getValue(0, GERptAttr.FlowEnder).toString().equals("fuhui"))
		{
			throw new RuntimeException("@应该是 fuhui 是 FlowEnder .");
		}

		if (!dt.getValue(0, GERptAttr.FlowStarter).toString().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是 zhanghaicheng 是 FlowStarter .");
		}

		if (!dt.getValue(0, GERptAttr.FlowEndNode).toString().equals("12307"))
		{
			throw new RuntimeException("@应该是 12307 是 FlowEndNode .");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.WFState).toString()) != Integer.parseInt(WFState.Runing.toString()))
		{
			throw new RuntimeException("@应该是 WFState.Runing 是当前的状态。");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}
		///#endregion  检查【开始节点】发送数据信息否完整？
	}
	public final void Step7()
	{
		//让zhoutianjiao登录
		BP.WF.Dev2Interface.Port_Login("zhoutianjiao");
		//让他执行发送
		objs=BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid);

		///#region 第1步: 检查发送对象返回的信息是否完整？
		//从获取的发送对象里获取到下一个工作者. qifenglin(祁凤林)
		if ( ! objs.getVarAcceptersID().equals("qifenglin"))
		{
			throw new RuntimeException("@下一步的接受人不正确,  qifenglin 现在是:" + objs.getVarAcceptersID());
		}

		if (objs.getVarToNodeID() != 12313)
		{
			throw new RuntimeException("@下一步节点ID应该是 12313节点. 现在是:" + objs.getVarToNodeID());
		}

		if (objs.getVarWorkID() != workid)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 12307)
		{
			throw new RuntimeException("@当前节点的编号不能变化:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarTreadWorkIDs() != null)
		{
			throw new RuntimeException("@不应当获得子线程WorkID.");
		}
		///#endregion  检查发送对象返回的信息是否完整？

		///#region 第2步: 检查流程引擎控制系统表是否符合预期.
		String sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + workid + " AND FK_Emp='" + objs.getVarAcceptersID() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该找不到当前人员的待办.");
		}

		gwf = new GenerWorkFlow(workid);
		//判断当前节点ID
		if (gwf.getFK_Node() != 12313)
		{
			throw new RuntimeException("@当前节点FK_Node应该不变化，现在：" + gwf.getFK_Node());
		}
		//判断流程运行状态
		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@WFState 应该 WFState.Runing ：" + gwf.getWFState().toString());
		}
		//判断流程发起人是否变化
		if ( ! gwf.getStarter().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是发起人员 zhanghaicheng，现在是:" + gwf.getStarter());
		}
		//判断当前点.
		if (gwf.getFID() != 0)
		{
			throw new RuntimeException("@当前点应该是 FID=0  现在是:" + gwf.getFID());
		}
		///#endregion end 检查流程引擎控制系统表是否符合预期.

		///#region 第3步: 检查发送节点表单-数据信息否完整？
		//检查节点表单表是否有数据，以及数据是否正确？
		sql = "SELECT * FROM ND12302 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12302 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("zhangyifan"))
				{
					throw new RuntimeException("第二步骤的处理人员,应当zhangyifan ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12303 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12303 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("zhoushengyu"))
				{
					throw new RuntimeException("第三步骤的处理人员,应当zhoushengyu ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12304 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12304 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("liyan"))
				{
					throw new RuntimeException("第四步骤的处理人员,应当liyan ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			} 
		}
		sql = "SELECT * FROM ND12305 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12305 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("liping"))
				{
					throw new RuntimeException("第五步骤的处理人员,应当liping ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12306 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12306 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("fuhui"))
				{
					throw new RuntimeException("第六步骤的处理人员,应当fuhui ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12307 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12307 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("zhoutianjiao"))
				{
					throw new RuntimeException("第七步骤的处理人员,应当zhoutianjiao ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM ND12313 WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12313 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("qifenglin"))
				{
					throw new RuntimeException("第七步骤的处理人员,应当qifenglin ,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.FID)){
				if (!val.equals("0"))
				{
					throw new RuntimeException("应当 = 0,现在是:" + val);
				}
			}else if(dc.ColumnName.equals(WorkAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("应当 = 1,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM  ND123Rpt WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.getValue(0, GERptAttr.FlowEnder).toString().equals("zhoutianjiao"))
		{
			throw new RuntimeException("@应该是 zhoutianjiao 是 FlowEnder .");
		}

		if (!dt.getValue(0, GERptAttr.FlowStarter).toString().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是 zhanghaicheng 是 FlowStarter .");
		}

		if (!dt.getValue(0, GERptAttr.FlowEndNode).toString().equals("12313"))
		{
			throw new RuntimeException("@应该是 12313 是 FlowEndNode .");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.WFState).toString()) != Integer.parseInt(WFState.Runing.toString()))
		{
			throw new RuntimeException("@应该是 WFState.Runing 是当前的状态。");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}
		///#endregion  检查【开始节点】发送数据信息否完整？
	}
	public final void Step8_1()
	{
		//让qifenglin登录
		BP.WF.Dev2Interface.Port_Login("qifenglin");
		//创建一个子流程ID
		newworkid = BP.WF.Dev2Interface.Node_CreateBlankWork("124");
		//让他发起一个子流程,发送给liyan（李言）
		objs=BP.WF.Dev2Interface.Node_SendWork("124", newworkid);

	   //BP.WF.Dev2Interface.flow_se

		///#region 第1步: 检查【子流程开始节点】发送对象返回的信息是否完整？
		if ( ! objs.getVarAcceptersID().equals("liyan"))
		{
			throw new RuntimeException("@下一步的接受人不正确,  liyan 现在是:" + objs.getVarAcceptersID());
		}

		if (objs.getVarToNodeID() != 12402)
		{
			throw new RuntimeException("@应该是 12402节点. 现在是:" + objs.getVarToNodeID());
		}

		if (objs.getVarWorkID() != newworkid)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 12401)
		{
			throw new RuntimeException("@当前节点的编号不能变化:" + objs.getVarCurrNodeID());
		}

		///#endregion  检查【子流程开始节点】发送对象返回的信息是否完整？

		///#region 第2步: 检查流程引擎控制系统表是否符合预期.
		gwf = new GenerWorkFlow(newworkid);
		if (gwf.getFK_Node() != 12402)
		{
			throw new RuntimeException("@FK_Node应该不变化，现在：" + gwf.getFK_Node());
		}

		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@WFState 应该 WFState.Runing ：" + gwf.getWFState().toString());
		}

		if (gwf.getStarter() != WebUser.getNo())
		{
			throw new RuntimeException("@应该是发起人员qifenglin，现在是:" + gwf.getStarter());
		}
		///#endregion end 检查流程引擎控制系统表是否符合预期.

		///#region 第3步: 检查【子流程开始节点】发送节点表单-数据信息否完整？
		//检查节点表单表是否有数据，以及数据是否正确？
		// 检查节点表单表是否有数据?;
		String sql = "SELECT * FROM ND12401 WHERE OID=" + newworkid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该在开始节点表单表中找不到数据，");
		}

		if (!dt.getValue(0, "Rec").toString().equals(WebUser.getNo()))
		{
			throw new RuntimeException("@记录人应该是qifenglin.");
		}

		sql = "SELECT * FROM ND12402 WHERE OID=" + newworkid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND12402 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("liyan"))
				{
					throw new RuntimeException("第二步骤的处理人员,应当liyan ,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM  ND123Rpt WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.getValue(0, GERptAttr.FlowStarter).toString().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是 zhanghaicheng 是 FlowStarter .");
		}

		if (!dt.getValue(0, GERptAttr.FlowEndNode).toString().equals("12313"))
		{
			throw new RuntimeException("@应该是 12313 是 FlowEndNode .");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.WFState).toString()) != Integer.parseInt(WFState.Runing.toString()))
		{
			throw new RuntimeException("@应该是 WFState.Runing 是当前的状态。");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}

		sql = "SELECT * FROM  ND124Rpt WHERE OID=" + newworkid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.getValue(0, GERptAttr.PWorkID).toString().equals((new Long(workid)).toString()))
		{
			throw new RuntimeException("@应该是 "+workid+" 是 PWorkID .");
		}

		if (!dt.getValue(0, GERptAttr.PNodeID).toString().equals("12313"))
		{
			throw new RuntimeException("@应该是 12313 是 PNodeID .");
		}

		if (!dt.getValue(0, GERptAttr.PFlowNo).toString().equals("123"))
		{
			throw new RuntimeException("@应该是 123 是 PFlowNo .");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.WFState).toString()) != Integer.parseInt(WFState.Runing.toString()))
		{
			throw new RuntimeException("@应该是 WFState.Runing 是当前的状态。");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}
		///#endregion  检查【子流程开始节点】发送数据信息否完整？
	}
	public final void Step8_2()
	{
		//让liyan登录
		BP.WF.Dev2Interface.Port_Login("liyan");

		//让他发起一个子流程,发送给liping（李萍）
		objs=BP.WF.Dev2Interface.Node_SendWork("124", newworkid);

		///#region 第3步: 检查【子流程开始节点】发送节点表单-数据信息否完整？
		String sql = "SELECT * FROM  ND124Rpt WHERE OID=" + newworkid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.getValue(0, GERptAttr.PWorkID).toString().equals((new Long(workid)).toString()))
		{
			throw new RuntimeException("@应该是 " + workid + " 是 PWorkID .");
		}

		if (!dt.getValue(0, GERptAttr.PNodeID).toString().equals("12313"))
		{
			throw new RuntimeException("@应该是 12313 是 PNodeID .");
		}

		if (!dt.getValue(0, GERptAttr.PFlowNo).toString().equals("123"))
		{
			throw new RuntimeException("@应该是 123 是 PFlowNo .");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.WFState).toString()) != Integer.parseInt(WFState.Runing.toString()))
		{
			throw new RuntimeException("@应该是 WFState.Runing 是当前的状态。");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}
		///#endregion  检查【子流程开始节点】发送数据信息否完整？
	}
	public final void Step8_3()
	{
		//让liping登录
		BP.WF.Dev2Interface.Port_Login("liping");

		//让他发送给zhangyifan（张一凡）
		objs=BP.WF.Dev2Interface.Node_SendWork("124", newworkid);

		///#region 第3步: 检查【子流程开始节点】发送节点表单-数据信息否完整？
		String sql = "SELECT * FROM  ND124Rpt WHERE OID=" + newworkid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.getValue(0, GERptAttr.PWorkID).toString().equals((new Long(workid)).toString()))
		{
			throw new RuntimeException("@应该是 " + workid + " 是 PWorkID .");
		}

		if (!dt.getValue(0, GERptAttr.PNodeID).toString().equals("12313"))
		{
			throw new RuntimeException("@应该是 12313 是 PNodeID .");
		}

		if (!dt.getValue(0, GERptAttr.PFlowNo).toString().equals("123"))
		{
			throw new RuntimeException("@应该是 123 是 PFlowNo .");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.WFState).toString()) != Integer.parseInt(WFState.Runing.toString()))
		{
			throw new RuntimeException("@应该是 WFState.Runing 是当前的状态。");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}
		///#endregion  检查【子流程开始节点】发送数据信息否完整？
	}
	public final void Step8_4()
	{
		//让zhangyifan登录
		BP.WF.Dev2Interface.Port_Login("zhangyifan");

		//让他执行发送zhoupeng
		objs=BP.WF.Dev2Interface.Node_SendWork("124", newworkid);

		///#region 第3步: 检查【子流程开始节点】发送节点表单-数据信息否完整？
		String sql = "SELECT * FROM  ND124Rpt WHERE OID=" + newworkid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.getValue(0, GERptAttr.PWorkID).toString().equals((new Long(workid)).toString()))
		{
			throw new RuntimeException("@应该是 " + workid + " 是 PWorkID .");
		}

		if (!dt.getValue(0, GERptAttr.PNodeID).toString().equals("12313"))
		{
			throw new RuntimeException("@应该是 12313 是 PNodeID .");
		}

		if (!dt.getValue(0, GERptAttr.PFlowNo).toString().equals("123"))
		{
			throw new RuntimeException("@应该是 123 是 PFlowNo .");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.WFState).toString()) != Integer.parseInt(WFState.Runing.toString()))
		{
			throw new RuntimeException("@应该是 WFState.Complete 是当前的状态。");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}
		///#endregion  检查【子流程开始节点】发送数据信息否完整？
	}
	public final void Step9_1()
	{
		//让zhoupeng登录
		BP.WF.Dev2Interface.Port_Login("zhoupeng");
		//创建一个子流程ID
		newworkid = BP.WF.Dev2Interface.Node_CreateBlankWork("125");
		//让他发起一个子流程,发送给liyan（李言）
		objs=BP.WF.Dev2Interface.Node_SendWork("125", newworkid);

		///#region 第1步: 检查【子流程开始节点】发送对象返回的信息是否完整？
		if ( ! objs.getVarAcceptersID().equals("liyan"))
		{
			throw new RuntimeException("@下一步的接受人不正确,  liyan 现在是:" + objs.getVarAcceptersID());
		}

		if (objs.getVarToNodeID() != 12502)
		{
			throw new RuntimeException("@应该是 12502节点. 现在是:" + objs.getVarToNodeID());
		}

		if (objs.getVarWorkID() != newworkid)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 12501)
		{
			throw new RuntimeException("@当前节点的编号不能变化:" + objs.getVarCurrNodeID());
		}

		///#endregion  检查【子流程开始节点】发送对象返回的信息是否完整？

		///#region 第2步: 检查流程引擎控制系统表是否符合预期.
		gwf = new GenerWorkFlow(newworkid);
		if (gwf.getFK_Node() != 12502)
		{
			throw new RuntimeException("@FK_Node应该不变化，现在：" + gwf.getFK_Node());
		}

		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@WFState 应该 WFState.Runing ：" + gwf.getWFState().toString());
		}

		if (gwf.getStarter() != WebUser.getNo())
		{
			throw new RuntimeException("@应该是发起人员qifenglin，现在是:" + gwf.getStarter());
		}
		///#endregion end 检查流程引擎控制系统表是否符合预期.

		///#region 第3步: 检查【子流程开始节点】发送节点表单-数据信息否完整？
		//检查节点表单表是否有数据，以及数据是否正确？
		// 检查节点表单表是否有数据?;
		String sql = "SELECT * FROM ND12501 WHERE OID=" + newworkid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该在开始节点表单表中找不到数据，");
		}

		if (!dt.getValue(0, "Rec").toString().equals(WebUser.getNo()))
		{
			throw new RuntimeException("@记录人应该是qifenglin.");
		}

		sql = "SELECT * FROM ND12502 WHERE OID=" + newworkid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 1)
		{
			throw new RuntimeException("@发起流程出错误,不应该找不到 ND10802 的数据.");
		}
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(WorkAttr.Emps)){
				if (!val.contains("liyan"))
				{
					throw new RuntimeException("第二步骤的处理人员,应当liyan ,现在是:" + val);
				}
			}
		}
		sql = "SELECT * FROM  ND123Rpt WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.getValue(0, GERptAttr.FlowStarter).toString().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是 zhanghaicheng 是 FlowStarter .");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.WFState).toString()) != Integer.parseInt(WFState.Runing.toString()))
		{
			throw new RuntimeException("@应该是 WFState.Runing 是当前的状态。");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}

		sql = "SELECT * FROM  ND125Rpt WHERE OID=" + newworkid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.getValue(0, GERptAttr.PWorkID).toString().equals((new Long(workid)).toString()))
		{
			throw new RuntimeException("@应该是 " + workid + " 是 PWorkID .");
		}

		if (!dt.getValue(0, GERptAttr.PNodeID).toString().equals("12309"))
		{
			throw new RuntimeException("@应该是 12309 是 PNodeID .");
		}

		if (!dt.getValue(0, GERptAttr.PFlowNo).toString().equals("123"))
		{
			throw new RuntimeException("@应该是 123 是 PFlowNo .");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.WFState).toString()) != Integer.parseInt(WFState.Runing.toString()))
		{
			throw new RuntimeException("@应该是 WFState.Runing 是当前的状态。");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}
		///#endregion  检查【子流程开始节点】发送数据信息否完整？
	}
	public final void Step9_2()
	{
		//让liyan登录
		BP.WF.Dev2Interface.Port_Login("liyan");

		//让他发起一个子流程,发送给liping（李萍）
		objs=BP.WF.Dev2Interface.Node_SendWork("125", newworkid);

		///#region 第3步: 检查【子流程开始节点】发送节点表单-数据信息否完整？
		String sql = "SELECT * FROM  ND125Rpt WHERE OID=" + newworkid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.getValue(0, GERptAttr.PWorkID).toString().equals((new Long(workid)).toString()))
		{
			throw new RuntimeException("@应该是 " + workid + " 是 PWorkID .");
		}

		if (!dt.getValue(0, GERptAttr.PNodeID).toString().equals("12309"))
		{
			throw new RuntimeException("@应该是 12309 是 PNodeID .");
		}

		if (!dt.getValue(0, GERptAttr.PFlowNo).toString().equals("123"))
		{
			throw new RuntimeException("@应该是 123 是 PFlowNo .");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.WFState).toString()) != Integer.parseInt(WFState.Runing.toString()))
		{
			throw new RuntimeException("@应该是 WFState.Runing 是当前的状态。");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}
		///#endregion  检查【子流程开始节点】发送数据信息否完整？
	}
	public final void Step9_3()
	{
		//让liping登录
		BP.WF.Dev2Interface.Port_Login("liping");

		//让他发送给zhangyifan（张一凡）
		objs=BP.WF.Dev2Interface.Node_SendWork("125", newworkid);

		///#region 第3步: 检查【子流程开始节点】发送节点表单-数据信息否完整？
		String sql = "SELECT * FROM  ND125Rpt WHERE OID=" + newworkid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.getValue(0, GERptAttr.PWorkID).toString().equals((new Long(workid)).toString()))
		{
			throw new RuntimeException("@应该是 " + workid + " 是 PWorkID .");
		}

		if (!dt.getValue(0, GERptAttr.PNodeID).toString().equals("12309"))
		{
			throw new RuntimeException("@应该是 12309 是 PNodeID .");
		}

		if (!dt.getValue(0, GERptAttr.PFlowNo).toString().equals("123"))
		{
			throw new RuntimeException("@应该是 123 是 PFlowNo .");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.WFState).toString()) != Integer.parseInt(WFState.Runing.toString()))
		{
			throw new RuntimeException("@应该是 WFState.Runing 是当前的状态。");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}
		///#endregion  检查【子流程开始节点】发送数据信息否完整？
	}
	public final void Step9_4()
	{
		//让zhangyifan登录
		BP.WF.Dev2Interface.Port_Login("zhangyifan");

		//让他执行发送
		objs = BP.WF.Dev2Interface.Node_SendWork("125", newworkid);

		///#region 第3步: 检查【子流程开始节点】发送节点表单-数据信息否完整？
		String sql = "SELECT * FROM  ND125Rpt WHERE OID=" + newworkid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (!dt.getValue(0, GERptAttr.PWorkID).toString().equals((new Long(workid)).toString()))
		{
			throw new RuntimeException("@应该是 " + workid + " 是 PWorkID .");
		}

		if (!dt.getValue(0, GERptAttr.PNodeID).toString().equals("12309"))
		{
			throw new RuntimeException("@应该是 12309 是 PNodeID .");
		}

		if (!dt.getValue(0, GERptAttr.PFlowNo).toString().equals("123"))
		{
			throw new RuntimeException("@应该是 123 是 PFlowNo .");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.WFState).toString()) != Integer.parseInt(WFState.Complete.toString()))
		{
			throw new RuntimeException("@应该是 WFState.Complete 是当前的状态。");
		}

		if (Integer.parseInt(dt.getValue(0, GERptAttr.FID).toString()) != 0)
		{
			throw new RuntimeException("@应该是 FID =0 ");
		}

		if (!dt.getValue(0, "FK_NY").toString().equals(DataType.getCurrentYearMonth()))
		{
			throw new RuntimeException("@ FK_NY 字段填充错误. ");
		}
		///#endregion  检查【子流程开始节点】发送数据信息否完整？
	}
	public final void Step10()
	{
		//让fuhui登录
		BP.WF.Dev2Interface.Port_Login("fuhui");

		//让他执行发送zhoupeng
		objs = BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid);

		///#region 第1步: 检查发送对象返回的信息是否完整？
		if ( ! objs.getVarAcceptersID().equals("zhoupeng"))
		{
			throw new RuntimeException("@下一步的接受人不正确,  zhoupeng 现在是:" + objs.getVarAcceptersID());
		}

		if (objs.getVarToNodeID() != 12311)
		{
			throw new RuntimeException("@下一步节点ID应该是 12311节点. 现在是:" + objs.getVarToNodeID());
		}

		if (objs.getVarWorkID() != workid)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 12310)
		{
			throw new RuntimeException("@当前节点的编号不能变化:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarTreadWorkIDs() != null)
		{
			throw new RuntimeException("@不应当获得子线程WorkID.");
		}
		///#endregion  检查发送对象返回的信息是否完整？

		///#region 第2步: 检查流程引擎控制系统表是否符合预期.
		String sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + workid + " AND FK_Emp='" + objs.getVarAcceptersID() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() == 0)
		{
			throw new RuntimeException("@不应该找不到当前人员的待办.");
		}

		gwf = new GenerWorkFlow(workid);
		//判断当前节点ID
		if (gwf.getFK_Node() != 12311)
		{
			throw new RuntimeException("@当前节点FK_Node应该不变化，现在：" + gwf.getFK_Node());
		}
		//判断流程运行状态
		if (gwf.getWFState() != WFState.Runing)
		{
			throw new RuntimeException("@WFState 应该 WFState.Runing ：" + gwf.getWFState().toString());
		}
		//判断流程发起人是否变化
		if ( ! gwf.getStarter().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是发起人员 zhanghaicheng，现在是:" + gwf.getStarter());
		}
		///#endregion end 检查流程引擎控制系统表是否符合预期.

		// 检查报表数据是否完整。
		sql = "SELECT * FROM ND123Rpt WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.equals(GERptAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("@ MyNum 应当是1  . ");
				}
			}else if(dc.ColumnName.equals(GERptAttr.Title)){
				if (StringHelper.isNullOrEmpty(val))
				{
					throw new RuntimeException("@ Title 不应当是空 " + val);
				}
			}else if(dc.ColumnName.equals(GERptAttr.WFState)){
				if (Integer.parseInt(val) != Integer.parseInt(WFState.Runing.toString()))
				{
					throw new RuntimeException("@应该是 WFState.Runing 是当前的状态。");
				}
			}
		}
	}
	public final void Step11()
	{
		//让fuhui登录
		BP.WF.Dev2Interface.Port_Login("zhoupeng");

		//让他执行发送
		objs = BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid);

		///#region 第1步: 检查发送对象返回的信息是否完整？
		//从获取的发送对象里获取到下一个工作者. qifenglin(祁凤林)
		if (objs.getVarAcceptersID() != null)
		{
			throw new RuntimeException("@下一步的接受人不正确,  null 现在是:" + objs.getVarAcceptersID());
		}

		if (objs.getVarWorkID() != workid)
		{
			throw new RuntimeException("@主线程的workid不应该变化:" + objs.getVarWorkID());
		}

		if (objs.getVarCurrNodeID() != 12311)
		{
			throw new RuntimeException("@当前节点的编号不能变化:" + objs.getVarCurrNodeID());
		}

		if (objs.getVarTreadWorkIDs() != null)
		{
			throw new RuntimeException("@不应当获得子线程WorkID.");
		}
		///#endregion  检查发送对象返回的信息是否完整？

		///#region 第2步: 检查流程引擎控制系统表是否符合预期.

		gwf = new GenerWorkFlow(workid);
		//判断当前节点ID
		if (gwf.getFK_Node() != 12311)
		{
			throw new RuntimeException("@当前节点FK_Node应该不变化，现在：" + gwf.getFK_Node());
		}
		//判断流程运行状态
		if (gwf.getWFState() != WFState.Complete)
		{
			throw new RuntimeException("@WFState 应该 WFState.Complete ：" + gwf.getWFState().toString());
		}
		//判断流程发起人是否变化
		if ( ! gwf.getStarter().equals("zhanghaicheng"))
		{
			throw new RuntimeException("@应该是发起人员 zhanghaicheng，现在是:" + gwf.getStarter());
		}
		//判断当前点.
		if (gwf.getFID() != 0)
		{
			throw new RuntimeException("@当前点应该是 FID=0  现在是:" + gwf.getFID());
		}

		//检查待办是否存在。
		String sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + workid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@不应该有待办.");
		}

		sql = "SELECT * FROM WF_GenerWorkerList  WHERE WorkID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 0)
		{
			throw new RuntimeException("@工作人员信息未删除..");
		}
		///#endregion end 检查流程引擎控制系统表是否符合预期.

		// 检查报表数据是否完整。
		sql = "SELECT * FROM ND123Rpt WHERE OID=" + workid;
		dt = DBAccess.RunSQLReturnTable(sql);
		for (DataColumn dc : dt.Columns)
		{
			String val = dt.getValue(0, dc.ColumnName).toString();
			if(dc.ColumnName.endsWith(GERptAttr.FlowEndNode)){
				if (!val.equals("12311"))
				{
					throw new RuntimeException("@应该是 12311 是 FlowEndNode,现在是:" + val);
				}
			}else if(dc.ColumnName.endsWith(GERptAttr.MyNum)){
				if (!val.equals("1"))
				{
					throw new RuntimeException("@ MyNum 应当是1  . ");
				}
			}else if(dc.ColumnName.endsWith(GERptAttr.Title)){
				if (StringHelper.isNullOrEmpty(val))
				{
					throw new RuntimeException("@ Title 不应当是空 " + val);
				}
			}else if(dc.ColumnName.endsWith(GERptAttr.WFState)){
				if (Integer.parseInt(val) != Integer.parseInt(WFState.Complete.toString()))
				{
					throw new RuntimeException("@应该是 WFState.Complete 是当前的状态。");
				}
			}
		}
		throw new RuntimeException("@测试成功！！！！");
	}
}
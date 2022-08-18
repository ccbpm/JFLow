package bp.wf.httphandler;

import bp.da.*;
import bp.difference.handler.CommonUtils;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.web.*;
import bp.en.*;
import bp.wf.Glo;
import bp.wf.template.*;
import bp.difference.*;
import bp.wf.template.sflow.*;
import bp.*;
import bp.wf.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.math.*;

/** 
 流程处理类
*/
public class WF_MyFlow extends WebContralBase
{

		///#region  运行变量
	/** 
	 从节点.
	*/
	public final String getFromNode() throws Exception {
		return this.GetRequestVal("FromNode");
	}
	/** 
	 是否抄送
	*/
	public final boolean isCC() throws Exception {
		String str = this.GetRequestVal("Paras");

		if (DataType.IsNullOrEmpty(str) == false)
		{
			String myps = str;

			if (myps.contains("IsCC=1") == true)
			{
				return true;
			}
		}

		str = this.GetRequestVal("AtPara");
		if (DataType.IsNullOrEmpty(str) == false)
		{
			if (str.contains("IsCC=1") == true)
			{
				return true;
			}
		}
		return false;
	}

	/** 
	 轨迹ID
	*/
	public final String getTrackID() throws Exception {
		return this.GetRequestVal("TrackeID");
	}
	/** 
	 到达的节点ID
	*/
	public final int getToNode() throws Exception {
		return this.GetRequestValInt("ToNode");
	}
	private int _FK_Node = 0;
	/** 
	 当前的 NodeID ,在开始时间,nodeID,是地一个,流程的开始节点ID.
	*/
	public final int getFK_Node()  {
		String fk_nodeReq = this.GetRequestVal("FK_Node");
		if (DataType.IsNullOrEmpty(fk_nodeReq)==true || fk_nodeReq.equals("undefined")==true)
			fk_nodeReq = this.GetRequestVal("NodeID");

		if (DataType.IsNullOrEmpty(fk_nodeReq) == false)
		{
			return Integer.parseInt(fk_nodeReq);
		}

		if (_FK_Node == 0)
		{
			if (this.getWorkID() != 0)
			{
				Paras ps = new Paras();
				ps.SQL = "SELECT FK_Node FROM WF_GenerWorkFlow WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID";
				ps.Add("WorkID", this.getWorkID());
				_FK_Node = DBAccess.RunSQLReturnValInt(ps, 0);
			}
			else
			{
				_FK_Node = Integer.parseInt(this.getFK_Flow() + "01");
			}
		}
		return _FK_Node;
	}
	public final void setFK_Node(int value)throws Exception
	{_FK_Node = value;
	}

	private String _width = "";
	/** 
	 表单宽度
	*/
	public final String getWidth() throws Exception {
		return _width;
	}
	public final void setWidth(String value)throws Exception
	{_width = value;
	}
	private String _height = "";
	/** 
	 表单高度
	*/
	public final String getHeight() throws Exception {
		return _height;
	}
	public final void setHeight(String value)throws Exception
	{_height = value;
	}
	public String _btnWord = "";
	public final String getBtnWord() throws Exception {
		return _btnWord;
	}
	public final void setBtnWord(String value)throws Exception
	{_btnWord = value;
	}
	private GenerWorkFlow _HisGenerWorkFlow = null;
	public final GenerWorkFlow getHisGenerWorkFlow() throws Exception {
		if (_HisGenerWorkFlow == null)
		{
			_HisGenerWorkFlow = new GenerWorkFlow(this.getWorkID());
		}
		return _HisGenerWorkFlow;
	}
	private Node _currNode = null;
	public final Node getCurrND() throws Exception {
		if (_currNode == null)
		{
			_currNode = new Node(this.getFK_Node());
		}
		return _currNode;
	}
	public final void setCurrND(Node value)throws Exception
	{_currNode = value;
	}
	private Flow _currFlow = null;
	public final Flow getCurrFlow() throws Exception {
		if (_currFlow == null)
		{
			_currFlow = new Flow(this.getFK_Flow());
		}
		return _currFlow;
	}
	public final void setCurrFlow(Flow value)throws Exception
	{_currFlow = value;
	}
	/** 
	 定义跟路径
	*/
	public String appPath = "/";


	//杨玉慧
	public final String getDoType1() throws Exception {
		return this.GetRequestVal("DoType1");
	}

		///#endregion

	public final String Focus() throws Exception {
		Dev2Interface.Flow_Focus(this.getWorkID());
		return "设置成功.";
	}
	/** 
	 确认
	 
	 @return 
	*/
	public final String Confirm() throws Exception {
		Dev2Interface.Flow_Confirm(this.getWorkID());
		return "设置成功.";
	}
	/** 
	 删除子流程
	 
	 @return 
	*/
	public final String DelSubFlow() throws Exception {
		Dev2Interface.Flow_DeleteSubThread(this.getWorkID(), "手工删除");
		return "删除成功.";
	}
	/** 
	 加载前置导航数据
	 
	 @return 
	*/
	public final String StartGuide_Init() throws Exception {
		String josnData = "";
		//流程编号
		String fk_flow = this.GetRequestVal("FK_Flow");
		//查询的关键字
		String skey = this.GetRequestVal("Keys");
		try
		{
			//获取流程实例
			Flow fl = new Flow(fk_flow);
			//获取设置的前置导航的sql
			Object tempVar = fl.getStartGuidePara2();
			String sql = tempVar instanceof String ? (String)tempVar : null;
			//判断是否有查询条件
			if (!DataType.IsNullOrEmpty(skey))
			{
				Object tempVar2 = fl.getStartGuidePara1();
				sql = tempVar2 instanceof String ? (String)tempVar2 : null;
				sql = sql.replace("@Key", skey);
			}
			sql = sql.replace("~", "'");
			//替换约定参数
			sql = sql.replace("@WebUser.No", WebUser.getNo());
			sql = sql.replace("@WebUser.Name", WebUser.getName());
			sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
			sql = sql.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());

			if (sql.contains("@") == true)
			{
				for (String key : CommonUtils.getRequest().getParameterMap().keySet())
				{
					sql = sql.replace("@" + key, this.GetRequestVal(key));
				}

				for (String key : CommonUtils.getRequest().getParameterMap().keySet())
				{
					sql = sql.replace("@" + key, this.GetRequestVal(key));
				}
			}

			//获取数据
			DataTable dt = DBAccess.RunSQLReturnTable(sql);

			//判断前置导航的类型
			switch (fl.getStartGuideWay())
			{
				case BySQLOne:
				case BySystemUrlOneEntity:
					josnData = bp.tools.Json.ToJson(dt);
					break;
				case BySQLMulti:
					josnData = bp.tools.Json.ToJson(dt);
					break;
				default:
					break;
			}
			return josnData;
		}
		catch (RuntimeException ex)
		{
			return "err@:" + ex.getMessage().toString();
		}
	}
	/** 
	 没有WorkID
	 
	 @return 
	*/
	public final String MyFlow_Init_NoWorkID() throws Exception {
		String isStartSameLevelFlow = this.GetRequestVal("IsStartSameLevelFlow");


			///#region 判断是否可以否发起流程.
		try
		{
			if (Dev2Interface.Flow_IsCanStartThisFlow(this.getFK_Flow(), WebUser.getNo(), this.getPFlowNo(), this.getPNodeID(), this.getPWorkID()) == false)
			{
				/*是否可以发起流程？ */
				throw new RuntimeException("err@您(" + WebUser.getNo() + ")没有发起或者处理该流程的权限.");
			}
		}
		catch (RuntimeException ex)
		{
			if (ex.getMessage().contains("外部用户") == true)
			{
				//判断是否是开始节点？这里要发起流程.
				Node nd = new Node(this.getFK_Node());
				if (nd.isStartNode() == true && nd.getHisDeliveryWay() == DeliveryWay.ByGuest)
				{
					return "url@./WorkOpt/GuestStartFlow/GenerCode.htm";
				}


			}

			throw new RuntimeException("err@" + ex.getMessage());
		}

		/*如果是开始节点, 先检查是否启用了流程限制。*/
		if (Glo.CheckIsCanStartFlow_InitStartFlow(this.getCurrFlow()) == false)
		{
			/* 如果启用了限制就把信息提示出来. */
			String msg = Glo.DealExp(this.getCurrFlow().getStartLimitAlert(), null, null);
			return "err@" + msg;
		}

			///#endregion 判断是否可以否发起流程


			///#region 判断前置导航.
		//生成workid.
		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(this.getFK_Flow(), null, null, WebUser.getNo(), null, this.getPWorkID(), this.getPFID(), this.getPFlowNo(), this.getPNodeID(), null, 0, null, null, isStartSameLevelFlow);

		String hostRun = this.getCurrFlow().GetValStrByKey(FlowAttr.HostRun);
		if (DataType.IsNullOrEmpty(hostRun) == false)
		{
			hostRun += "/WF/";
		}

		this.setWorkID(workid); //给workid赋值.

		switch (this.getCurrFlow().getStartGuideWay())
		{
			case None:
				break;
			case SubFlowGuide:
			case SubFlowGuideEntity:
				return "url@" + hostRun + "WorkOpt/StartGuide/Guide.htm?FK_Flow=" + this.getCurrFlow().getNo() + "&WorkID=" + workid;
			case ByHistoryUrl: // 历史数据.
				if (this.getCurrFlow().isLoadPriData() == true)
				{
					return "err@流程设计错误，您不能同时启用前置导航，自动装载上一笔数据两个功能。";
				}
				return "url@" + hostRun + "WorkOpt/StartGuide/Guide.htm?FK_Flow=" + this.getCurrFlow().getNo() + "&WorkID=" + workid;
			case BySystemUrlOneEntity:
				return "url@" + hostRun + "WorkOpt/StartGuide/GuideEntities.htm?StartGuideWay=BySystemUrlOneEntity&WorkID=" + workid + "" + this.getRequestParasOfAll();
			case BySQLOne:
				return "url@" + hostRun + "WorkOpt/StartGuide/Entities.htm?StartGuideWay=BySQLOne&WorkID=" + workid + "" + this.getRequestParasOfAll();
			case BySQLMulti:
				return "url@" + hostRun + "WorkOpt/StartGuide/Entities.htm?StartGuideWay=BySQLMulti&WorkID=" + workid + "" + this.getRequestParasOfAll();
			case BySelfUrl: //按照定义的url.
				return "url@" + this.getCurrFlow().getStartGuidePara1() + this.getRequestParasOfAll() + "&WorkID=" + workid;
			case ByFrms: //选择表单.
				return "url@" + hostRun + "WorkOpt/StartGuide/Frms.htm?FK_Flow=" + this.getCurrFlow().getNo() + "&WorkID=" + workid;
			case ByParentFlowModel: //选择父流程.
				return "url@" + hostRun + "WorkOpt/StartGuide/ParentFlowModel.htm?FK_Flow=" + this.getCurrFlow().getNo() + "&WorkID=" + workid;
			default:
				throw new RuntimeException("没有解析的发起导航模式:" + this.getCurrFlow().getStartGuideWay());
		}

			///#endregion 判断前置导航

		return null; //生成了workid.
	}
	public final String DictFlow_Init() throws Exception {

			///#region 判断是否可以否发起流程.
		try
		{
			if (Dev2Interface.Flow_IsCanStartThisFlow(this.getFK_Flow(), WebUser.getNo(), this.getPFlowNo(), this.getPNodeID(), this.getPWorkID()) == false)
			{
				/*是否可以发起流程？ */
				throw new RuntimeException("err@您(" + WebUser.getNo() + ")没有发起或者处理该流程的权限.");
			}
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("err@" + ex.getMessage());
		}

		/*如果是开始节点, 先检查是否启用了流程限制。*/
		if (Glo.CheckIsCanStartFlow_InitStartFlow(this.getCurrFlow()) == false)
		{
			/* 如果启用了限制就把信息提示出来. */
			String msg = Glo.DealExp(this.getCurrFlow().getStartLimitAlert(), null, null);
			return "err@" + msg;
		}

			///#endregion 判断是否可以否发起流程

		//生成workid.
		long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(this.getFK_Flow(), GuestUser.getNo());


			///#region 设置流程实体关系
		GERpt rpt = new GERpt("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt");
		rpt.setOID(workid);
		if (rpt.RetrieveFromDBSources() != 0)
		{
			rpt.setPFlowNo(this.GetRequestVal("FrmID"));
			rpt.setPWorkID(this.GetRequestValInt64("FrmOID"));
			rpt.Update();
		}
		GenerWorkFlow gwf = new GenerWorkFlow(workid);
		gwf.setPWorkID(this.GetRequestValInt64("FrmOID"));
		;
		gwf.setPFlowNo(this.GetRequestVal("FrmID"));
		gwf.Update();

			///#endregion 设置流程实体关系


			///#region 判断前置导航.
		String hostRun = this.getCurrFlow().GetValStrByKey(FlowAttr.HostRun);
		if (DataType.IsNullOrEmpty(hostRun) == false)
		{
			hostRun += "/WF/";
		}

		this.setWorkID(workid); //给workid赋值.

		switch (this.getCurrFlow().getStartGuideWay())
		{
			case None:
				break;
			case SubFlowGuide:
			case SubFlowGuideEntity:
				return "url@" + hostRun + "StartGuide.htm?FK_Flow=" + this.getCurrFlow().getNo() + "&WorkID=" + workid;
			case ByHistoryUrl: // 历史数据.
				if (this.getCurrFlow().isLoadPriData() == true)
				{
					return "err@流程设计错误，您不能同时启用前置导航，自动装载上一笔数据两个功能。";
				}
				return "url@" + hostRun + "StartGuide.htm?FK_Flow=" + this.getCurrFlow().getNo() + "&WorkID=" + workid;
			case BySystemUrlOneEntity:
				return "url@" + hostRun + "StartGuideEntities.htm?StartGuideWay=BySystemUrlOneEntity&WorkID=" + workid + "" + this.getRequestParasOfAll();
			case BySQLOne:
				return "url@" + hostRun + "StartGuideEntities.htm?StartGuideWay=BySQLOne&WorkID=" + workid + "" + this.getRequestParasOfAll();
			case BySQLMulti:
				return "url@" + hostRun + "StartGuideEntities.htm?StartGuideWay=BySQLMulti&WorkID=" + workid + "" + this.getRequestParasOfAll();
			case BySelfUrl: //按照定义的url.
				return "url@" + this.getCurrFlow().getStartGuidePara1() + this.getRequestParasOfAll() + "&WorkID=" + workid;
			case ByFrms: //选择表单.
				return "url@" + hostRun + "./WorkOpt/StartGuideFrms.htm?FK_Flow=" + this.getCurrFlow().getNo() + "&WorkID=" + workid;
			case ByParentFlowModel: //选择父流程
				return "url@" + hostRun + "./WorkOpt/StartGuideParentFlowModel.htm?FK_Flow=" + this.getCurrFlow().getNo() + "&WorkID=" + workid;
			default:
				break;
		}

			///#endregion 判断前置导航

		this.setWorkID(workid);
		return MyFlow_Init();
	}
	/** 
	 初始化(处理分发)
	 
	 @return 
	*/
	public final String MyFlow_Init() throws Exception {
		if (this.getWorkID() == 0 && this.getFID() == 0)
		{
			String val = MyFlow_Init_NoWorkID();
			if (val != null)
			{
				return val;
			}
		}

		//子线程退回分流节点
		if (Dev2Interface.Flow_IsCanToFLTread(this.getWorkID(), this.getFID(), this.getFK_Node()) == true)
		{
			GenerWorkFlow mgwf = new GenerWorkFlow(this.getFID());
			//返回子线程综处理页面
			return "url@MyFLDealThread.htm?WorkID=0&FK_Flow=" + mgwf.getFK_Flow() + "&FK_Node=" + this.getFK_Node() + "&PWorkID=" + mgwf.getPWorkID() + "&FID=" + this.getFID();
		}
		//定义变量.
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(this.getWorkID());
		if (gwf.RetrieveFromDBSources() == 0)
		{
			return ("err@该流程ID{" + this.getWorkID() + "}不存在，或者已经被删除.");
		}




		//手动启动子流程的标志 0父子流程 1 同级子流程
		String isStartSameLevelFlow = this.GetRequestVal("IsStartSameLevelFlow");
		this.setCurrND(new Node(gwf.getFK_Node()));


			///#region 做权限判断.
		//授权人
		String auther = this.GetRequestVal("Auther");
		if (DataType.IsNullOrEmpty(auther) == false)
		{
			WebUser.setAuth ( auther);
			WebUser.setAuthName(DBAccess.RunSQLReturnString("SELECT Name FROM Port_Emp WHERE No='" + auther + "'"));
		}
		else
		{
			WebUser.setAuth("");
			WebUser.setAuthName(""); // bp.da.DBAccess.RunSQLReturnString("SELECT Name FROM Port_Emp WHERE No='" + auther + "'");
		}

		//判断是否有执行该工作的权限.
		String todEmps = ";" + gwf.getTodoEmps();
		boolean isCanDo = false;
		if (String.valueOf(gwf.getFK_Node()).endsWith("01") == true)
		{
			if (gwf.getStarter().equals(WebUser.getNo()) == false)
			{
				isCanDo = false; //处理开始节点发送后，撤销的情况，第2个节点打开了，第1个节点撤销了,造成第2个节点也可以发送下去.
			}
			else
			{
				isCanDo = true; // 开始节点不判断权限.
			}
		}
		else
		{
			isCanDo = todEmps.contains(";" + WebUser.getNo() + ",");
			if (isCanDo == false)
			{
				isCanDo = Dev2Interface.Flow_IsCanDoCurrentWork(this.getWorkID(), WebUser.getNo());
			}
		}

		if (isCanDo == false)
		{
			return "err@您[" + WebUser.getNo() + "," + WebUser.getName() + "]不能执行当前工作, 当前工作已经运转到[" + gwf.getNodeName() + "],处理人[" + gwf.getTodoEmps() + "]。";
		}

		String frms = this.GetRequestVal("Frms");
		if (DataType.IsNullOrEmpty(frms) == false)
		{
			gwf.setParasFrms(frms);
			gwf.Update();
		}

			///#endregion 做权限判断.


			///#region 判断是否是混合执行.
		if (this.getCurrND().getWhoExeIt() == 2)
		{
			/*如果当前节点是混合执行，就执行一下发送。*/
			try
			{
				//这里可能会出现异常,有异常就是要阻止发送动作.
				SendReturnObjs objs = Dev2Interface.Node_SendWork(gwf.getFK_Flow(), gwf.getWorkID(), 0, null);

				gwf = new GenerWorkFlow(this.getWorkID());
				this.setCurrND(new Node(gwf.getFK_Node()));
				this.setFK_Node(gwf.getFK_Node());

				//判断是否移动到下一个节点？
				if (objs.getVarToNodeID() != gwf.getFK_Node())
				{
					return "url@MyFlow.htm?WorkID=" + gwf.getWorkID() + "&FK_Node=" + objs.getVarToNodeID() + "&FID=" + gwf.getFID();
				}
			}
			catch (RuntimeException ex)
			{
				Log.DebugWriteInfo("myflow_int@判断是否是混合执行,提示未发送成功信息:" + ex.getMessage());
			}
		}

			///#endregion 判断是否是混合执行.


			///#region 处理打开既阅读.
		//判断当前节点是否是打开即阅读
		//获取当前节点信息
		if (this.getCurrND().isOpenOver() == true)
		{
			//如果是结束节点执行流程结束功能
			if (this.getCurrND().isStartNode() == false)
			{
				//如果启用审核组件
				if (this.getCurrND().getFrmWorkCheckSta() == FrmWorkCheckSta.Enable)
				{
					//判断一下审核意见是否有默认值
					NodeWorkCheck workCheck = new NodeWorkCheck("ND" + this.getCurrND().getNodeID());
					String msg = Glo.getDefValWFNodeFWCDefInfo(); // 设置默认值;
					if (workCheck.getFWCIsFullInfo() == true)
					{
						msg = workCheck.getFWCDefInfo();
					}
					Dev2Interface.WriteTrackWorkCheck(gwf.getFK_Flow(), this.getCurrND().getNodeID(), gwf.getWorkID(), gwf.getFID(), msg, workCheck.getFWCOpLabel(), null);
				}

				Dev2Interface.Node_SendWork(gwf.getFK_Flow(), gwf.getWorkID());
				return "url@" + "./MyView.htm?WorkID=" + gwf.getWorkID() + "&FK_Flow=" + gwf.getFK_Flow() + "&FK_Node=" + gwf.getFK_Node() + "&PWorkID=" + gwf.getPWorkID() + "&FID=" + gwf.getFID();
			}
		}

			///#endregion 处理打开既阅读.



			///#region 处理打开跳转.
		if (this.getCurrND().getSkipTime() == 1)
		{
			// string info = CheckSkipTime(this.currND);
			// if (info != null)
			//   return info;

		}

			///#endregion 处理打开跳转.



			///#region 前置导航数据拷贝到第一节点
		if (this.GetRequestVal("IsCheckGuide") != null)
		{
			String key = this.GetRequestVal("KeyNo");
			DataTable dt = Glo.StartGuidEnties(this.getWorkID(), this.getFK_Flow(), this.getFK_Node(), key);

			/*如果父流程编号，就要设置父子关系。*/
			if (dt != null && dt.Rows.size() > 0 && dt.Columns.contains("PFlowNo") == true)
			{
				String pFlowNo = dt.Rows.get(0).getValue("PFlowNo").toString();
				int pNodeID = Integer.parseInt(dt.Rows.get(0).getValue("PNodeID").toString());
				long pWorkID = Long.parseLong(dt.Rows.get(0).getValue("PWorkID").toString());
				String pEmp = ""; // dt.Rows.get(0).getValue("PEmp"].ToString();
				if (DataType.IsNullOrEmpty(pEmp))
				{
					pEmp = WebUser.getNo();
				}

				//设置父子关系.
				Dev2Interface.SetParentInfo(this.getFK_Flow(), this.getWorkID(), pWorkID);
			}
		}

			///#endregion


			///#region 启动同级子流程的信息存储
		if (isStartSameLevelFlow != null && isStartSameLevelFlow.equals("1") == true)
		{
			String slFlowNo = GetRequestVal("SLFlowNo");
			int slNode = GetRequestValInt("SLNodeID");
			long slWorkID = GetRequestValInt("SLWorkID");
			gwf.SetPara("SLFlowNo", slFlowNo);
			gwf.SetPara("SLNodeID", slNode);
			gwf.SetPara("SLWorkID", slWorkID);
			gwf.SetPara("SLEmp", WebUser.getNo());
			gwf.Update();
		}

			///#endregion 启动同级子流程的信息存储


		if (this.getCurrND().isStartNode())
		{
			/*如果是开始节点, 先检查是否启用了流程限制。*/
			if (Glo.CheckIsCanStartFlow_InitStartFlow(this.getCurrFlow()) == false)
			{
				/* 如果启用了限制就把信息提示出来. */
				String msg = Glo.DealExp(this.getCurrFlow().getStartLimitAlert(), null, null);
				return "err@" + msg;
			}
		}


			///#region 处理表单类型.
		if (this.getCurrND().getHisFormType() == NodeFormType.SheetTree
				|| this.getCurrND().getHisFormType() == NodeFormType.SheetAutoTree
				|| this.getCurrFlow().getFlowDevModel() == FlowDevModel.FrmTree)
		{
			String toUrl = "";
			if (this.getIsMobile() == true)
			{
				if (gwf.getParasFrms().equals("") == false)
					toUrl = "MyFlowGener.htm?WorkID=" + this.getWorkID() + "&NodeID=" + gwf.getFK_Node() + "&FK_Node=" + gwf.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo=" + WebUser.getNo() + "&FID=" + this.getFID() + "&Token=" + WebUser.getToken() + "&PFlowNo=" + gwf.getPFlowNo() + "&PNodeID=" + gwf.getPNodeID() + "&PWorkID=" + gwf.getPWorkID() + "&Frms=" + gwf.getParasFrms();
				else
					toUrl = "MyFlowGener.htm?WorkID=" + this.getWorkID() + "&NodeID=" + gwf.getFK_Node() + "&FK_Node=" + gwf.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo=" + WebUser.getNo() + "&FID=" + this.getFID() + "&Token=" + WebUser.getToken() + "&PFlowNo=" + gwf.getPFlowNo() + "&PNodeID=" + gwf.getPNodeID() + "&PWorkID=" + gwf.getPWorkID();
			}
			else
			{
				if (gwf.getParasFrms().equals("") == false)
					toUrl = "MyFlowTree.htm?WorkID=" + this.getWorkID() + "&NodeID=" + gwf.getFK_Node() + "&FK_Node=" + gwf.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo=" + WebUser.getNo() + "&FID=" + this.getFID() + "&Token=" + WebUser.getToken() + "&PFlowNo=" + gwf.getPFlowNo() + "&PNodeID=" + gwf.getPNodeID() + "&PWorkID=" + gwf.getPWorkID() + "&Frms=" + gwf.getParasFrms();
				else
					toUrl = "MyFlowTree.htm?WorkID=" + this.getWorkID() + "&NodeID=" + gwf.getFK_Node() + "&FK_Node=" + gwf.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo=" + WebUser.getNo() + "&FID=" + this.getFID() + "&Token=" + WebUser.getToken() + "&PFlowNo=" + gwf.getPFlowNo() + "&PNodeID=" + gwf.getPNodeID() + "&PWorkID=" + gwf.getPWorkID();
			}

			String[] strs = this.getRequestParas().split("[&]", -1);
			for (String str : strs)
			{
				if (toUrl.contains(str) == true)
				{
					continue;
				}
				if (str.contains("DoType=") == true)
				{
					continue;
				}
				if (str.contains("DoMethod=") == true)
				{
					continue;
				}
				if (str.contains("HttpHandlerName=") == true)
				{
					continue;
				}
				if (str.contains("IsLoadData=") == true)
				{
					continue;
				}
				if (str.contains("IsCheckGuide=") == true)
				{
					continue;
				}

				toUrl += "&" + str;
			}
			for (String key : CommonUtils.getRequest().getParameterMap().keySet())
			{
				if (toUrl.contains(key + "=") == true)
				{
					continue;
				}
				toUrl += "&" + key + "=" + ContextHolderUtils.getRequest().getParameter(key);
			}


				///#endregion 开始组合url.

			//增加fk_node
			if (toUrl.contains("&FK_Node=") == false)
			{
				toUrl += "&FK_Node=" + this.getCurrND().getNodeID();
			}

			//如果是开始节点.
			if (getCurrND().isStartNode() == true)
			{
				if (toUrl.contains("PrjNo") == true && toUrl.contains("PrjName") == true)
				{
					String sql = "UPDATE " + this.getCurrFlow().getPTable() + " SET PrjNo='" + this.GetRequestVal("PrjNo") + "', PrjName='" + this.GetRequestVal("PrjName") + "' WHERE OID=" + this.getWorkID();
					DBAccess.RunSQL(sql);
				}
			}
			return "url@" + toUrl;
		}

		if (this.getCurrND().getHisFormType() == NodeFormType.SDKForm
			|| this.getCurrFlow().getFlowDevModel() == FlowDevModel.SDKFrm)
		{
			String url = getCurrND().getFormUrl();
			if (DataType.IsNullOrEmpty(url))
			{
				return "err@设置读取状流程设计错误态错误,没有设置表单url.";
			}

			//处理连接.
			url = this.MyFlow_Init_DealUrl(getCurrND(), url);

			//sdk表单就让其跳转.
			return "url@" + url;
		}

			///#endregion 处理表单类型.

		//求出当前节点frm的类型.
		NodeFormType frmtype = this.getCurrND().getHisFormType();
		if (frmtype != NodeFormType.RefOneFrmTree)
		{
			getCurrND().WorkID = this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.

			if (this.getCurrND().getNodeFrmID().contains(String.valueOf(this.getCurrND().getNodeID())) == false)
			{
				/*如果当前节点引用的其他节点的表单.*/
				String nodeFrmID = getCurrND().getNodeFrmID();
				String refNodeID = nodeFrmID.replace("ND", "");
				Node nd = new Node(Integer.parseInt(refNodeID));

				//表单类型.
				frmtype = nd.getHisFormType();
			}
		}


			///#region 内置表单类型的判断.

		if (frmtype == NodeFormType.FoolTruck)
		{
			String url = "MyFlowGener.htm";

			//处理连接.
			url = this.MyFlow_Init_DealUrl(getCurrND(), url);
			return "url@" + url;
		}

		if (frmtype == NodeFormType.FoolForm && this.getIsMobile() == false)
		{
			/*如果是傻瓜表单，就转到傻瓜表单的解析执行器上。*/
			String url = "MyFlowGener.htm";
			if (this.getIsMobile())
			{
				url = "MyFlowGener.htm";
			}

			//处理连接.
			url = this.MyFlow_Init_DealUrl(getCurrND(), url);

			url = url.replace("DoType=MyFlow_Init&", "");
			url = url.replace("&DoWhat=StartClassic", "");
			return "url@" + url;
		}

		//自定义表单
		if ((frmtype == NodeFormType.SelfForm || this.getCurrFlow().getFlowDevModel() == FlowDevModel.SelfFrm) && this.getIsMobile() == false)
		{
			String url = "MyFlowSelfForm.htm";

			//处理连接.
			url = this.MyFlow_Init_DealUrl(getCurrND(), url);

			url = url.replace("DoType=MyFlow_Init&", "");
			url = url.replace("&DoWhat=StartClassic", "");
			return "url@" + url;
		}

			///#endregion 内置表单类型的判断.

		String myurl = "MyFlowGener.htm";
		MapData md = new MapData(this.getCurrND().getNodeFrmID());
//		if (md.getHisFrmType() == FrmType.ChapterFrm)
//		{
//			myurl = "MyFlowTree.htm?NodeFrmType=11";
//		}
		//处理连接.
		myurl = this.MyFlow_Init_DealUrl(getCurrND(), myurl);
		myurl = myurl.replace("DoType=MyFlow_Init&", "");
		myurl = myurl.replace("&DoWhat=StartClassic", "");
		return "url@" + myurl;
	}

	private String MyFlow_Init_DealUrl(bp.wf.Node currND) throws UnsupportedEncodingException {
		return MyFlow_Init_DealUrl(currND, null);
	}

//ORIGINAL LINE: private string MyFlow_Init_DealUrl(BP.WF.Node currND, string url = null)
	private String MyFlow_Init_DealUrl(Node currND, String url) throws UnsupportedEncodingException {
		if (url == null)
		{
			url = currND.getFormUrl();
		}
		String urlExt = "";

		//如果是分流点/分河流。且FID!=0
		if ((currND.getHisRunModel() == RunModel.FL || currND.getHisRunModel() == RunModel.FHL) && this.getFID() != 0)
		{
			urlExt += "WorkID=" + this.getFID() + "&SubWorkID=" + this.getWorkID();
		}
		else
		{
			urlExt += "WorkID=" + this.getWorkID();
		}

		urlExt += "&NodeID=" + currND.getNodeID();
		urlExt += "&FK_Node=" + currND.getNodeID();
		urlExt += "&FID=" + this.getFID();
		urlExt += "&UserNo=" + URLEncoder.encode(WebUser.getNo(), "UTF-8");
		urlExt += "&Token=" + WebUser.getToken();


		//SDK表单上服务器地址,应用到使用ccflow的时候使用的是sdk表单,该表单会存储在其他的服务器上,珠海高凌提出. 
		url = url.replace("@SDKFromServHost", SystemConfig.getAppSettings().get("SDKFromServHost").toString());

		if (url.contains("?") == true)
		{
			url += "&" + urlExt;
		}
		else
		{
			url += "?" + urlExt;
		}

		for (String str : CommonUtils.getRequest().getParameterMap().keySet())
		{
			if (DataType.IsNullOrEmpty(str) == true || str.equals("T") == true || str.equals("t") == true)
			{
				continue;
			}
			if (url.contains(str + "=") == true)
			{
				continue;
			}
			url += "&" + str + "=" + this.GetRequestVal(str);
		}

		url = url.replace("?&", "?");
		url = url.replace("&&", "&");
		return url;
	}

	/** 
	 构造函数
	*/
	public WF_MyFlow() throws Exception {

	}
	/** 
	 结束流程.
	 
	 @return 
	*/
	public final String MyFlow_StopFlow() throws Exception {
		try
		{
			String str = Dev2Interface.Flow_DoFlowOver(this.getWorkID(), "流程成功结束");
			if (DataType.IsNullOrEmpty(str) == true)
			{
				return "流程成功结束";
			}
			return str;
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 删除流程
	 
	 @return 
	*/
	public final String MyFlow_DeleteFlowByReal() throws Exception {
		try
		{
			String str = Dev2Interface.Flow_DoDeleteFlowByReal(this.getWorkID());
			if (DataType.IsNullOrEmpty(str) == true)
			{
				return "流程删除成功";
			}
			return str;
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 保存发送参数.
	 
	 @return 
	*/
	public final String SaveParas() throws Exception {
		Dev2Interface.Flow_SaveParas(this.getWorkID(), this.GetRequestVal("Paras"));
		return "保存成功";
	}
	/** 
	 子线程退回到分流的时候工具栏.
	 
	 param gwf
	 param dt
	 param nd
	 @return 
	*/
	public final String InitToolBar_ForFenLiu(GenerWorkFlow gwf, DataTable dt, Node nd) throws Exception {
		DataRow dr = null;

		dr = dt.NewRow();
		dr.setValue("No", "OpenFrm");
		dr.setValue("Name", "查看表单");
		dr.setValue("Oper", "");
		dt.Rows.add(dr);

		//dr = dt.NewRow();
		//dr["No"] = "KillThread";
		//dr["Name"] = "取消子线程";
		//dr["Oper"] = "KillThread()";
		//dt.Rows.add(dr);
		if (nd.getThreadIsCanDel() == true)
		{
			dr = dt.NewRow();
			dr.setValue("No", "UnSendAllThread");
			dr.setValue("Name", "撤销整体发送");
			dr.setValue("Oper", "UnSendAllThread()");
			dt.Rows.add(dr);
		}


		if (nd.getThreadIsCanAdd() == true)
		{
			boolean isCanAdd = false;
			//判断分流点到达的节点是同表单子线程还是异表单子线程
			Nodes nds = nd.getHisToNodes();
			if (nds.size() == 1)
				isCanAdd = true;
			if (isCanAdd == true)
			{
				dr = dt.NewRow();
				dr.setValue("No", "AddThread");
				dr.setValue("Name", "增加子线程");
				dr.setValue("Oper", "AddThread()");
				dt.Rows.add(dr);
			}
		}

		dr = dt.NewRow();
		dr.setValue("No", "Track");
		dr.setValue("Name", "轨迹");
		dr.setValue("Oper", "");
		dt.Rows.add(dr);

		DataSet ds = new DataSet();
		ds.Tables.add(dt);

		return bp.tools.Json.ToJson(ds);
	}
	/** 
	 初始化toolbar.
	 
	 @return 
	*/
	public final String InitToolBar() throws Exception {
		DataSet ds = new DataSet();
		//创建一个DataTable，返回按钮信息
		DataTable dt = new DataTable("ToolBar");
		dt.Columns.Add("No");
		dt.Columns.Add("Name");
		dt.Columns.Add("Oper");
		dt.Columns.Add("Role", Integer.class);
		dt.Columns.Add("Icon");


			///#region 处理是否是加签，或者是否是会签模式.
		boolean isAskForOrHuiQian = false;
		BtnLab btnLab = new BtnLab(this.getFK_Node());
		Node nd = new Node(this.getFK_Node());
		long workId = this.getWorkID();
		if (workId == 0)
		{
			workId = this.getFID();
		}
		GenerWorkFlow gwf = new GenerWorkFlow(workId);


			///#region 分流点，是否是发送多个子线程，单个子线程退回
		if (Dev2Interface.Flow_IsCanToFLTread(this.getWorkID(), this.getFID(), this.getFK_Node()) == true)
		{

			return InitToolBar_ForFenLiu(gwf, dt, nd);
		}

			///#endregion 分流点，是否是发送多个子线程，单个子线程退回

		if (String.valueOf(this.getFK_Node()).endsWith("01") == false)
		{
			if (gwf.getWFState() == WFState.Askfor)
			{
				isAskForOrHuiQian = true;
			}

			/*判断是否是加签状态，如果是，就判断是否是主持人，如果不是主持人，就让其 isAskFor=true ,屏蔽退回等按钮.*/
			/**说明：针对于组长模式的会签，协作模式的会签加签人仍可以加签*/
			if (gwf.getHuiQianTaskSta() == HuiQianTaskSta.HuiQianing)
			{
				//初次打开会签节点时
				if (DataType.IsNullOrEmpty(gwf.getHuiQianZhuChiRen()) == true)
				{
					if (gwf.getTodoEmps().contains(WebUser.getNo() + ",") == false)
					{
						isAskForOrHuiQian = true;
					}
				}

				//执行会签后的状态
				if (btnLab.getHuiQianRole() == HuiQianRole.TeamupGroupLeader && btnLab.getHuiQianLeaderRole().equals(0))
				{
					if (!gwf.getHuiQianZhuChiRen().equals(WebUser.getNo()) && gwf.GetParaString("AddLeader").contains(WebUser.getNo() + ",") == false)
					{
						isAskForOrHuiQian = true;
					}
				}
				else
				{
					if (gwf.getHuiQianZhuChiRen().contains(WebUser.getNo() + ",") == false && gwf.GetParaString("AddLeader").contains(WebUser.getNo() + ",") == false)
					{
						isAskForOrHuiQian = true;
					}
				}

			}
		}

			///#endregion 处理是否是加签，或者是否是会签模式，.

		DataRow dr = dt.NewRow();

		String toolbar = "";
		try
		{

				///#region 是否是会签？.
			if (isAskForOrHuiQian == true && SystemConfig.getCustomerNo().equals("LIMS"))
			{
				return "";
			}

			if (isAskForOrHuiQian == true)
			{
				dr.setValue("No", "Send");
				dr.setValue("Name", "确定/完成");
				dr.setValue("Oper", btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;Send(false, " + nd.getFormType().getValue() + "); ");
				dt.Rows.add(dr);
				if (btnLab.getPrintZipEnable() == true)
				{
					dr = dt.NewRow();
					dr.setValue("No", "PackUp");
					dr.setValue("Name", btnLab.getPrintZipLab());
					dr.setValue("Oper", "");
					dt.Rows.add(dr);
				}

				if (btnLab.getTrackEnable())
				{
					dr = dt.NewRow();
					dr.setValue("No", "Track");
					dr.setValue("Name", btnLab.getTrackLab());
					dr.setValue("Oper", "");
					dt.Rows.add(dr);
				}

				return bp.tools.Json.ToJson(dt);
			}

				///#endregion 是否是会签.




				///#region 是否是抄送.
			if (this.isCC())
			{
				dr = dt.NewRow();
				dr.setValue("No", "Track");
				dr.setValue("Name", "流程运行轨迹");
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

				// 判断审核组件在当前的表单中是否启用，如果启用了.
				NodeWorkCheck fwc = new NodeWorkCheck(this.getFK_Node());
				if (fwc.getHisFrmWorkCheckSta() != FrmWorkCheckSta.Enable)
				{
					dr = dt.NewRow();
					/*如果不等于启用, */
					dr.setValue("No", "CCWorkCheck");
					dr.setValue("Name", "填写审核意见");
					dr.setValue("Oper", "");
					dt.Rows.add(dr);

					//toolbar += "<input type=button  value='填写审核意见' enable=true onclick=\"WinOpen('" + appPath + "WF/WorkOpt/CCCheckNote.htm?WorkID=" + this.WorkID + "&FK_Flow=" + this.FK_Flow + "&FID=" + this.FID + "&FK_Node=" + this.FK_Node + "&s=" + tKey + "','ds'); \" />";
				}
				return toolbar;
			}

				///#endregion 是否是抄送.


				///#region 如果当前节点启用了协作会签.
			//if (btnLab.HuiQianRole == HuiQianRole.Teamup)
			//{
			//    dr = dt.NewRow();
			//    dr["No"] = "SendHuiQian";
			//    dr["Name"] = "会签发送";
			//    dr["Oper"] = btnLab.SendJS + " if(SysCheckFrm()==false) return false;Send(true, " + (int)nd.FormType + ");";
			//    dt.Rows.add(dr);

			//}

				///#endregion 如果当前节点启用了协作会签


				///#region 加载流程控制器 - 按钮
			if (this.getCurrND().getHisFormType() == NodeFormType.SelfForm)
			{
				/*如果是嵌入式表单.*/
				if (getCurrND().isEndNode())
				{
					/*如果当前节点是结束节点.*/
					if (btnLab.getSendEnable() && getCurrND().getHisBatchRole() != BatchRole.Group)
					{
						dr = dt.NewRow();
						/*如果启用了发送按钮.*/
						dr.setValue("No", "Send");
						dr.setValue("Name", btnLab.getSendLab());
						dr.setValue("Oper", btnLab.getSendJS() + " if (SysCheckFrm()==false) return false;Send(false, " + nd.getFormType().getValue() + ");");
						dt.Rows.add(dr);
					}
				}
				else
				{
					if (btnLab.getSendEnable() && getCurrND().getHisBatchRole() != BatchRole.Group)
					{
						dr = dt.NewRow();
						dr.setValue("No", "Send");
						dr.setValue("Name", btnLab.getSendLab());
						dr.setValue("Oper", btnLab.getSendJS() + " if ( SysCheckFrm()==false) return false; Send(false, " + nd.getFormType().getValue() + ");");
						dt.Rows.add(dr);
					}
				}

				/*处理保存按钮.*/
				if (btnLab.getSaveEnable())
				{
					dr = dt.NewRow();
					dr.setValue("No", "Save");
					dr.setValue("Name", btnLab.getSaveLab());
					dr.setValue("Oper", "if(SysCheckFrm()==false) return false;SaveOnly();SaveEnd(" + nd.getFormType().getValue() + ");");
					dt.Rows.add(dr);
				}
			}

			if (this.getCurrND().getHisFormType() != NodeFormType.SelfForm)
			{
				/*启用了其他的表单.*/
				if (getCurrND().isEndNode())
				{
					/*如果当前节点是结束节点.*/
					if (btnLab.getSendEnable() && getCurrND().getHisBatchRole() != BatchRole.Group)
					{
						/*如果启用了选择人窗口的模式是【选择既发送】.*/
						dr = dt.NewRow();
						dr.setValue("No", "Send");
						dr.setValue("Name", btnLab.getSendLab());
						dr.setValue("Oper", btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;Send(false, " + nd.getFormType().getValue() + ");");
						dt.Rows.add(dr);
					}
				}
				else
				{
					if (btnLab.getSendEnable() && getCurrND().getHisBatchRole() != BatchRole.Group)
					{
						/*如果启用了发送按钮.
						 * 1. 如果是加签的状态，就不让其显示发送按钮，因为在加签的提示。
						 */
						dr = dt.NewRow();
						dr.setValue("No", "Send");
						dr.setValue("Name", btnLab.getSendLab());
						dr.setValue("Oper", btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;Send(false, " + nd.getFormType().getValue() + ");");
						dt.Rows.add(dr);
					}
				}

				/* 处理保存按钮.*/
				if (btnLab.getSaveEnable())
				{
					dr = dt.NewRow();
					dr.setValue("No", "Save");
					dr.setValue("Name", btnLab.getSaveLab());
					dr.setValue("Oper", "if (SysCheckFrm() == false) return false; SaveOnly();SaveEnd(" + nd.getFormType().getValue() + "); ");
					dt.Rows.add(dr);
				}
			}

			//发起会签子流程
			if (nd.isSendDraftSubFlow() == true)
			{
				dr = dt.NewRow();
				dr.setValue("No", "StartThread");
				dr.setValue("Name", "发起会签");
				dr.setValue("Oper", "StartThread()");
				dt.Rows.add(dr); //发起会签子流程
			}

			//是否启用 挂起.
			if (btnLab.getHungEnable() == true)
			{
				dr = dt.NewRow();
				dr.setValue("No", "Hungup");
				dr.setValue("Name", btnLab.getHungLab()); //挂起.
				dr.setValue("Oper", "");
				dt.Rows.add(dr); //挂起
			}


			//if (btnLab.WorkCheckEnable)
			//{
			//    dr = dt.NewRow();
			//    dr["No"] = "workcheckBtn";
			//    dr["Name"] = btnLab.WorkCheckLab;
			//    dr["Oper"] = "";
			//    dt.Rows.add(dr);/*审核*/
			//}

			if (btnLab.getThreadEnable())
			{
				/*如果要查看子线程.*/
				dr = dt.NewRow();
				dr.setValue("No", "Thread");
				dr.setValue("Name", btnLab.getThreadLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getShowParentFormEnable() && this.getPWorkID() != 0)
			{
				/*如果要查看父流程.*/
				dr = dt.NewRow();
				dr.setValue("No", "ParentForm");
				dr.setValue("Name", btnLab.getShowParentFormLab());
				dr.setValue("Oper", "");

				dt.Rows.add(dr);
			}

			if (btnLab.getTCEnable() == true)
			{
				/*流转自定义..*/
				dr = dt.NewRow();
				dr.setValue("No", "TransferCustom");
				dr.setValue("Name", btnLab.getTCLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getHelpRole() != 0)
			{
				dr = dt.NewRow();
				dr.setValue("No", "Help");
				dr.setValue("Name", btnLab.getHelpLab());
				dr.setValue("Oper", "HelpAlter()");
				dr.setValue("Role", btnLab.getHelpRole());
				dt.Rows.add(dr);
			}

			if (btnLab.getJumpWayEnum() != JumpWay.CanNotJump)
			{
				/*跳转*/
				dr = dt.NewRow();
				dr.setValue("No", "JumpWay");
				dr.setValue("Name", btnLab.getJumpWayLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getReturnEnable())
			{
				/*退回*/
				dr = dt.NewRow();
				dr.setValue("No", "Return");
				dr.setValue("Name", btnLab.getReturnLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}



			if (btnLab.getShiftEnable())
			{
				/*移交*/
				dr = dt.NewRow();
				dr.setValue("No", "Shift");
				dr.setValue("Name", btnLab.getShiftLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if ((btnLab.getCCRole() == CCRole.HandCC || btnLab.getCCRole() == CCRole.HandAndAuto))
			{

				// 抄送 
				dr = dt.NewRow();
				dr.setValue("No", "CC");
				dr.setValue("Name", btnLab.getCCLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			if (btnLab.getDeleteEnable() != 0)
			{
				dr = dt.NewRow();
				dr.setValue("No", "Delete");
				dr.setValue("Name", btnLab.getDeleteLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getEndFlowEnable() && this.getCurrND().isStartNode() == false)
			{
				dr = dt.NewRow();
				dr.setValue("No", "EndFlow");
				dr.setValue("Name", btnLab.getEndFlowLab());
				dr.setValue("Oper", "DoStop('" + btnLab.getEndFlowLab() + "','" + this.getFK_Flow() + "','" + this.getWorkID() + "')");
				dt.Rows.add(dr);

			}

			if (btnLab.getPrintDocEnable() == true)
			{
				dr = dt.NewRow();
				dr.setValue("No", "PrintDoc");
				dr.setValue("Name", btnLab.getPrintDocLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);


			}

			if (btnLab.getTrackEnable())
			{
				dr = dt.NewRow();
				dr.setValue("No", "Track");
				dr.setValue("Name", btnLab.getTrackLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}


			if (btnLab.getSearchEnable())
			{
				dr = dt.NewRow();
				dr.setValue("No", "Search");
				dr.setValue("Name", btnLab.getSearchLab());
				dr.setValue("Oper", "WinOpen('./RptDfine/Default.htm?RptNo=ND" + Integer.parseInt(this.getFK_Flow()) + "MyRpt&FK_Flow=" + this.getFK_Flow() + "&SearchType=My')");
				dt.Rows.add(dr);
			}

			if (btnLab.getBatchEnable())
			{
				String urlr = appPath + "WF/Batch.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow();

				/*批量处理*/
				dr = dt.NewRow();
				dr.setValue("No", "Batch");
				dr.setValue("Name", btnLab.getBatchLab());
				dr.setValue("Oper", "To('" + urlr + "');");
				dt.Rows.add(dr);

			}

			//if (btnLab.AskforEnable)
			//{
			//    /*加签 */
			//    dr = dt.NewRow();
			//    dr["No"] = "Askfor";
			//    dr["Name"] = btnLab.AskforLab;
			//    dr["Oper"] = "";
			//    dt.Rows.add(dr);

			//}

			if (btnLab.getHuiQianRole() != HuiQianRole.None)
			{
				/*会签 */
				dr = dt.NewRow();
				dr.setValue("No", "HuiQian");
				dr.setValue("Name", btnLab.getHuiQianLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			//原始会签主持人可以增加组长
			if (btnLab.getHuiQianRole() != HuiQianRole.None && btnLab.getAddLeaderEnable() == true)
			{
				/*增加组长 */
				dr = dt.NewRow();
				dr.setValue("No", "AddLeader");
				dr.setValue("Name", btnLab.getAddLeaderLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}


			//if (btnLab.WebOfficeWorkModel == WebOfficeWorkModel.Button)
			//{
			//    /*公文正文 */
			//    dr = dt.NewRow();
			//    dr["No"] = "WebOffice";
			//    dr["Name"] = btnLab.WebOfficeLab;
			//    dr["Oper"] = "";
			//    dt.Rows.add(dr);

			//}

			// 需要翻译.
			if (this.getCurrFlow().isResetData() == true && this.getCurrND().isStartNode())
			{
				/* 启用了数据重置功能 */
				dr = dt.NewRow();
				dr.setValue("No", "ReSet");
				dr.setValue("Name", "数据重置");
				dr.setValue("Oper", "resetData();");
				dt.Rows.add(dr);
			}


			if (btnLab.getCHRole() != 0)
			{
				/* 节点时限设置 */
				dr = dt.NewRow();
				dr.setValue("No", "CH");
				dr.setValue("Name", btnLab.getCHLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);

			}

			if (btnLab.getNoteEnable() != 0)
			{
				/* 备注设置 */
				dr = dt.NewRow();
				dr.setValue("No", "Note");
				dr.setValue("Name", btnLab.getNoteLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}


			if (btnLab.getPRIEnable() != 0)
			{
				/* 优先级设置 */
				dr = dt.NewRow();
				dr.setValue("No", "PR");
				dr.setValue("Name", btnLab.getPRILab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			/* 关注 */
			if (btnLab.getFocusEnable() == true)
			{
				dr = dt.NewRow();
				dr.setValue("No", "Focus");
				if (getHisGenerWorkFlow().getParasFocus() == true)
				{
					dr.setValue("Name", "取消关注");
				}
				else
				{
					dr.setValue("Name", btnLab.getFocusLab());
				}
				dr.setValue("Oper", "FocusBtn(this,'" + this.getWorkID() + "');");
				dt.Rows.add(dr);
			}

			/* 分配工作 */
			if (btnLab.getAllotEnable() == true)
			{
				/*分配工作*/
				dr = dt.NewRow();
				dr.setValue("No", "Allot");
				dr.setValue("Name", btnLab.getAllotLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			/* 确认 */
			if (btnLab.getConfirmEnable() == true)
			{
				dr = dt.NewRow();
				dr.setValue("No", "Confirm");
				if (getHisGenerWorkFlow().getParasConfirm() == true)
				{
					dr.setValue("Name", "取消确认");
				}
				else
				{
					dr.setValue("Name", btnLab.getConfirmLab());
				}

				dr.setValue("Oper", "ConfirmBtn(this,'" + this.getWorkID() + "');");
				dt.Rows.add(dr);
			}

			// 需要翻译.

			/* 打包下载zip */
			if (btnLab.getPrintZipEnable() == true)
			{
				dr = dt.NewRow();
				dr.setValue("No", "PackUp_zip");
				dr.setValue("Name", btnLab.getPrintZipLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			/* 打包下载html */
			if (btnLab.getPrintHtmlEnable() == true)
			{
				dr = dt.NewRow();
				dr.setValue("No", "PackUp_html");
				dr.setValue("Name", btnLab.getPrintHtmlLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			/* 打包下载pdf */
			if (btnLab.getPrintPDFEnable() == true)
			{
				dr = dt.NewRow();
				dr.setValue("No", "PackUp_pdf");
				dr.setValue("Name", btnLab.getPrintPDFLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

			if (btnLab.getFrmDBVerEnable() == true)
			{
				dr = dt.NewRow();
				dr.setValue("No", "FrmDBVer");
				dr.setValue("Name", btnLab.getFrmDBVerLab());
				dr.setValue("Oper", "FrmDBVer_Init()");
				dt.Rows.add(dr);
			}
			//小纸条
			if (btnLab.getScripRole() == 1)
			{
				dr = dt.NewRow();
				dr.setValue("No", "Scrip");
				dr.setValue("Name", btnLab.getScripLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}
			//数据批阅
			if (btnLab.getFrmDBRemarkEnable() != 0)
			{
				dr = dt.NewRow();
				dr.setValue("No", "FrmDBRemark");
				dr.setValue("Name", btnLab.getFrmDBRemarkLab());
				dr.setValue("Oper", "FrmDBRemark(" + btnLab.getFrmDBRemarkEnable() + ")");
				dt.Rows.add(dr);
			}

			if (btnLab.getFlowBBSRole() != 0)
			{
				dr = dt.NewRow();
				dr.setValue("No", "FlowBBS");
				dr.setValue("Name", btnLab.getFlowBBSLab());
				dr.setValue("Oper", btnLab.getFlowBBSRole());
				dt.Rows.add(dr);
			}

			if (btnLab.getIMEnable() == true)
			{
				dr = dt.NewRow();
				dr.setValue("No", "IM");
				dr.setValue("Name", btnLab.getIMLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}


			if (this.getCurrND().isStartNode() == true)
			{
				if (this.getCurrFlow().isDBTemplate() == true)
				{
					dr = dt.NewRow();
					dr.setValue("No", "DBTemplate");
					dr.setValue("Name", "模版");
					dr.setValue("Oper", "");
					dt.Rows.add(dr);
				}
			}

			/* 公文标签 */
			if (btnLab.getOfficeBtnEnable() == true && btnLab.getOfficeBtnLocal() == 0)
			{
				dr = dt.NewRow();
				dr.setValue("No", "DocWord");
				dr.setValue("Name", btnLab.getOfficeBtnLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}
			boolean getIsMobile = this.GetRequestValBoolen("IsMobile");
			if (getIsMobile == false && btnLab.getQRCodeRole() != 0)
			{
				dr = dt.NewRow();
				dr.setValue("No", "QRCode");
				dr.setValue("Name", DataType.IsNullOrEmpty(btnLab.getQRCodeLab()) == true ? "生成二维码" : btnLab.getQRCodeLab());
				dr.setValue("Oper", "");
				dt.Rows.add(dr);
			}

				///#endregion


				///#region 发起子流程

			if (getIsMobile == false)
			{
				SubFlowHands subFlows = new SubFlowHands(this.getFK_Node());
				for (SubFlowHand subFlow : subFlows.ToJavaList())
				{
					if (subFlow.getSubFlowStartModel() != 0 && subFlow.getSubFlowSta() == FrmSubFlowSta.Enable)
					{
						dr = dt.NewRow();
						dr.setValue("No", "SubFlow");
						dr.setValue("Name", DataType.IsNullOrEmpty(subFlow.getSubFlowLab()) == true ? "发起" + subFlow.getSubFlowName() : subFlow.getSubFlowLab());
						dr.setValue("Oper", "SendSubFlow(\'" + subFlow.getSubFlowNo() + "\',\'" + subFlow.getMyPK() + "\')");
						dt.Rows.add(dr);
					}
				}
			}

				///#endregion


				///#region  加载自定义的button.
			NodeToolbars bars = new NodeToolbars();
			bars.Retrieve(NodeToolbarAttr.FK_Node, this.getFK_Node(), NodeToolbarAttr.IsMyFlow, 1, NodeToolbarAttr.Idx);
			for (NodeToolbar bar : bars.ToJavaList())
			{
				if (bar.getExcType() == 1 || (!DataType.IsNullOrEmpty(bar.getTarget()) == false && bar.getTarget().toLowerCase().equals("javascript")))
				{
					dr = dt.NewRow();
					dr.setValue("No", "NodeToolBar");
					dr.setValue("Name", bar.getTitle());
					dr.setValue("Oper", bar.getUrl());
					//判断按钮图片路径是否有值
					String IconPath = bar.getIconPath();
					if (DataType.IsNullOrEmpty(IconPath))
					{
						dr.setValue("Icon", bar.getRow().get("WebPath"));
					}
					else
					{
						dr.setValue("Icon", IconPath);
					}
					dt.Rows.add(dr);
				}
				else
				{
					String urlr3 = bar.getUrl() + "&FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow();

					dr = dt.NewRow();
					dr.setValue("No", "NodeToolBar");
					dr.setValue("Name", bar.getTitle());
					dr.setValue("Oper", "WinOpen('" + urlr3 + "')");
					//判断按钮图片路径是否有值
					String IconPath = bar.getIconPath();
					if (DataType.IsNullOrEmpty(IconPath))
					{
						dr.setValue("Icon", bar.getRow().get("WebPath"));
					}
					else
					{
						dr.setValue("Icon", IconPath);
					}
					dt.Rows.add(dr);

				}
			}
			ds.Tables.add(dt);

				///#endregion //加载自定义的button.


				///#region 加载到达节点下拉框数据源.
			DataTable dtNodes = GenerDTOfToNodes(gwf, nd);
			if (dtNodes != null)
			{
				ds.Tables.add(dtNodes);
			}

				///#endregion 加载到达节点下拉框数据源.


				///#region 当前节点的流程信息.
			dt = nd.ToDataTableField("WF_Node");
			dt.Columns.Add("IsBackTrack", Integer.class);
			dt.Rows.get(0).setValue("IsBackTrack",0);
			if (gwf.getWFState() == WFState.ReturnSta && nd.GetParaInt("IsShowReturnNodeInToolbar", 0) == 0)
			{
				//当前节点是退回状态，是否原路返回
				Paras ps = new Paras();
				ps.SQL = "SELECT ReturnNode,Returner,ReturnerName,IsBackTracking FROM WF_ReturnWork WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID  ORDER BY RDT DESC";
				ps.Add(ReturnWorkAttr.WorkID, this.getWorkID());
				DataTable mydt = DBAccess.RunSQLReturnTable(ps);

				//说明退回并原路返回.
				if (mydt.Rows.size() == 0)
				{
					throw new RuntimeException("err@没有找到退回信息..");
				}

				//设置当前是否是退回并原路返回? IsBackTracking
				dt.Rows.get(0).setValue("IsBackTrack",mydt.Rows.get(0).getValue(3)); //是否发送并返回.
			}
			ds.Tables.add(dt);

				///#endregion  当前节点的流程信息
		}
		catch (RuntimeException ex)
		{
			Log.DebugWriteError(ex);
			new RuntimeException("err@" + ex.getMessage());
		}
		return bp.tools.Json.ToJson(ds);
	}
	/** 
	 批量处理
	 
	 @return 
	*/
	public final String Batch_InitDDL() throws Exception {
		GenerWorkFlow gwf = new GenerWorkFlow();
		Node nd = new Node(this.getFK_Node());
		gwf.setTodoEmps(WebUser.getNo() + ",");
		DataTable mydt = GenerDTOfToNodes(gwf, nd);
		return bp.tools.Json.ToJson(mydt);
	}

	public final DataTable GenerDTOfToNodes(GenerWorkFlow gwf, Node nd) throws Exception {
		//增加转向下拉框数据.
		if (nd.getCondModel() == DirCondModel.ByDDLSelected || nd.getCondModel() == DirCondModel.ByButtonSelected)
		{
		}
		else
		{
			return null;
		}
		DataTable dtToNDs = new DataTable("ToNodes");
		dtToNDs.Columns.Add("No", String.class); //节点ID.
		dtToNDs.Columns.Add("Name", String.class); //到达的节点名称.
		dtToNDs.Columns.Add("IsSelectEmps", String.class); //是否弹出选择人的对话框？
		dtToNDs.Columns.Add("IsSelected", String.class); //是否选择？
		dtToNDs.Columns.Add("DeliveryParas", String.class); //自定义URL

		DataRow dr = dtToNDs.NewRow();

		if (nd.isStartNode() == true || (gwf.getTodoEmps().contains(WebUser.getNo() + ",") == true))
		{
			/*如果当前不是主持人,如果不是主持人，就不让他显示下拉框了.*/

			/*如果当前节点，是可以显示下拉框的.*/
			//Nodes nds = nd.HisToNodes;

			NodeSimples nds = nd.getHisToNodeSimples();


				///#region 增加到达延续子流程节点。 @lizhen.
			if (nd.getSubFlowYanXuNum() >= 1)
			{
				SubFlowYanXus ygflows = new SubFlowYanXus(this.getFK_Node());
				for (SubFlowYanXu item : ygflows.ToJavaList())
				{
					String[] yanxuToNDs = item.getYanXuToNode().split("[,]", -1);
					for (String str : yanxuToNDs)
					{
						if (DataType.IsNullOrEmpty(str) == true)
						{
							continue;
						}

						int toNodeID = Integer.parseInt(str);

						Node subNode = new Node(toNodeID);

						dr = dtToNDs.NewRow(); //创建行。 @lizhen.

						//延续子流程跳转过了开始节点
						if (toNodeID == Integer.parseInt(Integer.parseInt(item.getSubFlowNo()) + "01"))
						{
							dr.setValue("No", String.valueOf(toNodeID));
							dr.setValue("Name", "启动:" + item.getSubFlowName() + " - " + subNode.getName());
							dr.setValue("IsSelectEmps", "1");
							dr.setValue("IsSelected", "0");
							dtToNDs.Rows.add(dr);
						}
						else
						{

							dr.setValue("No", String.valueOf(toNodeID));
							dr.setValue("Name", "启动:" + item.getSubFlowName() + " - " + subNode.getName());
							if (subNode.getHisDeliveryWay() == DeliveryWay.BySelected)
							{
								dr.setValue("IsSelectEmps", "1");
							}
							else
							{
								dr.setValue("IsSelectEmps", "0");
							}
							dr.setValue("IsSelected", "0");
							dtToNDs.Rows.add(dr);
						}
					}
				}
			}

				///#endregion 增加到达延续子流程节点。


				///#region 到达其他节点.
			//上一次选择的节点.
			int defalutSelectedNodeID = 0;
			if (nds.size() > 1)
			{
				String mysql = "";
				// 找出来上次发送选择的节点.
				if (SystemConfig.getAppCenterDBType( ) == DBType.MSSQL)
				{
					mysql = "SELECT  top 1 NDTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + this.getFK_Node() + " AND ActionType=1 ORDER BY WorkID DESC";
				}
				else if (SystemConfig.getAppCenterDBType( ) == DBType.Oracle || SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR3 ||SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR6)
				{
					mysql = "SELECT * FROM ( SELECT  NDTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + this.getFK_Node() + " AND ActionType=1 ORDER BY WorkID DESC ) WHERE ROWNUM =1";
				}
				else if (SystemConfig.getAppCenterDBType( ) == DBType.MySQL)
				{
					mysql = "SELECT  NDTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + this.getFK_Node() + " AND ActionType=1 ORDER BY WorkID  DESC limit 1,1";
				}
				else if (SystemConfig.getAppCenterDBType( ) == DBType.PostgreSQL || SystemConfig.getAppCenterDBType( ) == DBType.UX)
				{
					mysql = "SELECT  NDTo FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "Track A WHERE A.NDFrom=" + this.getFK_Node() + " AND ActionType=1 ORDER BY WorkID  DESC limit 1";
				}

				//获得上一次发送到的节点.
				defalutSelectedNodeID = DBAccess.RunSQLReturnValInt(mysql, 0);
			}


				///#region 为天业集团做一个特殊的判断.
			if (SystemConfig.getCustomerNo().equals("TianYe") && nd.getName().contains("董事长") == true)
			{
				/*如果是董事长节点, 如果是下一个节点默认的是备案. */
				for (NodeSimple item : nds.ToJavaList())
				{
					if (item.getName().contains("备案") == true && item.getName().contains("待") == false)
					{
						defalutSelectedNodeID = item.getNodeID();
						break;
					}
				}
			}

				///#endregion 为天业集团做一个特殊的判断.


				///#region 是否增加退回的节点
			int returnNode = 0;
			if (gwf.getWFState() == WFState.ReturnSta && nd.GetParaInt("IsShowReturnNodeInToolbar", 0) == 1)
			{
				String mysql = "";
				ReturnWorks returnWorks = new ReturnWorks();
				QueryObject qo = new QueryObject(returnWorks);
				qo.AddWhere(ReturnWorkAttr.WorkID, this.getWorkID());
				qo.addAnd();
				qo.AddWhere(ReturnWorkAttr.ReturnToNode, this.getFK_Node());
				qo.addAnd();
				qo.AddWhere(ReturnWorkAttr.ReturnToEmp, WebUser.getNo());
				qo.addOrderByDesc(ReturnWorkAttr.RDT);
				qo.DoQuery();
				if (returnWorks.size() != 0)
				{
					ReturnWork returnWork = returnWorks.get(0) instanceof ReturnWork ? (ReturnWork)returnWorks.get(0) : null;
					dr = dtToNDs.NewRow();
					dr.setValue("No", returnWork.getReturnNode());
					dr.setValue("Name", returnWork.getReturnNodeName() + "(退回)");
					dr.setValue("IsSelected", "1");
					dr.setValue("IsSelectEmps", "0");
					dtToNDs.Rows.add(dr);
					returnNode = returnWork.getReturnNode();
					defalutSelectedNodeID = 0; //设置默认。
				}
			}

				///#endregion 是否增加退回的节点.

			for (NodeSimple item : nds.ToJavaList())
			{
				if (item.getNodeID() == returnNode)
				{
					continue;
				}

				dr = dtToNDs.NewRow();
				dr.setValue("No", item.getNodeID());
				dr.setValue("Name", item.getName());

				if (item.getHisDeliveryWay() == DeliveryWay.BySelected)
				{
					dr.setValue("IsSelectEmps", "1");
				}
				else if (item.getHisDeliveryWay() == DeliveryWay.BySelfUrl)
				{
					dr.setValue("IsSelectEmps", "2");
					dr.setValue("DeliveryParas", item.getDeliveryParas());
				}
				else if (item.getHisDeliveryWay() == DeliveryWay.BySelectedEmpsOrgModel)
				{
					dr.setValue("IsSelectEmps", "3");
				}
				else
				{
					dr.setValue("IsSelectEmps", "0"); //是不是，可以选择接受人.
				}

				//设置默认选择的节点.
				if (defalutSelectedNodeID == item.getNodeID())
				{
					dr.setValue("IsSelected", "1");
				}
				else
				{
					dr.setValue("IsSelected", "0");
				}

				dtToNDs.Rows.add(dr);
			}

				///#endregion 到达其他节点。
		}

		return dtToNDs;
	}

	/** 
	 获取主表的方法.
	 
	 @return 
	*/
	private Hashtable GetMainTableHT() throws Exception {
		Hashtable htMain = new Hashtable();
		for (String key : CommonUtils.getRequest().getParameterMap().keySet())
		{
			if (key == null)
			{
				continue;
			}

			String myKey = key;
			String val = ContextHolderUtils.getRequest().getParameter(key);
			myKey = myKey.replace("TB_", "");
			myKey = myKey.replace("DDL_", "");
			myKey = myKey.replace("CB_", "");
			myKey = myKey.replace("RB_", "");
			val = URLDecoder.decode(val, "UTF-8");

			if (htMain.containsKey(myKey) == true)
			{
				htMain.put(myKey, val);
			}
			else
			{
				htMain.put(myKey, val);
			}
		}
		return htMain;
	}
	/** 
	 删除流程
	 
	 @return 
	*/
	public final String DeleteFlow() throws Exception {
		try
		{
			String msg = this.GetRequestVal("Msg");
			if (DataType.IsNullOrEmpty(msg) == true)
			{
				msg = "无";
			}
			DelWorkFlowRole role = DelWorkFlowRole.forValue(this.GetRequestValInt("DelEnable"));
			switch (role)
			{
				case DeleteByFlag:
					return Dev2Interface.Flow_DoDeleteFlowByFlag(this.getWorkID(), msg, true);
				case DeleteAndWriteToLog:
					return Dev2Interface.Flow_DoDeleteFlowByWriteLog(this.getFK_Flow(), this.getWorkID(), msg, true);
				case DeleteReal:
					return Dev2Interface.Flow_DoDeleteFlowByReal(this.getWorkID(), true);
			}
			return Dev2Interface.Flow_DoDeleteFlowByFlag(this.getWorkID(), msg, true);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 发送
	 
	 @return 
	*/
	public final String Send() throws Exception {
		try
		{
			Hashtable ht = this.GetMainTableHT();
			SendReturnObjs objs = null;
			String msg = "";

			//判断当前流程工作的GenerWorkFlow是否存在
			GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.setWorkID(this.getWorkID());
			int i = gwf.RetrieveFromDBSources();
			if (i == 0)
			{
				return "该流程的工作已删除,请联系管理员.WorkID=" + this.getWorkID();
			}

			long workid = this.getWorkID();
			//如果包含subWorkID
			long subWorkID = this.GetRequestValInt64("SubWorkID");
			if (subWorkID != 0)
			{
				workid = subWorkID;
			}


				///#region 处理授权人.
			//授权人
			String auther = this.GetRequestVal("Auther");
			if (DataType.IsNullOrEmpty(auther) == false)
			{
				//  bp.web.WebUser.IsAuthorize = true;
				WebUser.setAuth(auther);
				WebUser.setAuthName(DBAccess.RunSQLReturnString("SELECT Name FROM Port_Emp WHERE No='" + auther + "'"));
			} else {
				// BP.Web.WebUser.IsAuthorize = true;
				WebUser.setAuth("");
				WebUser.setAuthName(""); // bp.da.DBAccess.RunSQLReturnString("SELECT Name FROM Port_Emp WHERE No='" + auther + "'");
			}

				///#endregion 处理授权人.



			objs = Dev2Interface.Node_SendWork(this.getFK_Flow(), workid, ht, null, this.getToNode(), null, WebUser.getNo(), WebUser.getName() , WebUser.getFK_Dept(), WebUser.getFK_DeptName(), null, this.getFID(), this.getPWorkID(), this.GetRequestValBoolen("IsReturnNode"));

			msg = objs.ToMsgOfHtml();
			Glo.setSessionMsg(msg);


				///#region 处理授权
			if (DataType.IsNullOrEmpty(auther) == false)
			{
				gwf = new GenerWorkFlow(this.getWorkID());
				gwf.SetPara("Auth", WebUser.getAuthName() + "授权");
				gwf.Update();
			}

				///#endregion 处理授权


			//当前节点.
			Node currNode = new Node(this.getFK_Node());


				///#region 处理发送后转向.
			/*处理转向问题.*/
			switch (currNode.getHisTurnToDeal())
			{
				case SpecUrl:
					String myurl = currNode.getTurnToDealDoc().toString();
					if (myurl.contains("?") == false)
					{
						myurl += "?1=1";
					}
					Attrs myattrs = currNode.getHisWork().getEnMap().getAttrs();
					Work hisWK = currNode.getHisWork();
					for (Attr attr : myattrs)
					{
						if (myurl.contains("@") == false)
						{
							break;
						}
						myurl = myurl.replace("@" + attr.getKey(), hisWK.GetValStrByKey(attr.getKey()));
					}
					myurl = myurl.replace("@WebUser.No", WebUser.getNo());
					myurl = myurl.replace("@WebUser.Name", WebUser.getName());
					myurl = myurl.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

					if (myurl.contains("@"))
					{
						Dev2Interface.Port_SendMsg("admin", getCurrFlow().getName() + "在" + getCurrND().getName() + "节点处，出现错误", "流程设计错误，在节点转向url中参数没有被替换下来。Url:" + myurl, "Err" + getCurrND().getNo() + "_" + this.getWorkID(), SMSMsgType.Err, this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID());
						throw new RuntimeException("流程设计错误，在节点转向url中参数没有被替换下来。Url:" + myurl);
					}

					if (myurl.contains("&WorkID") == false)
					{
						myurl += "&WorkID=" + this.getWorkID();
					}

					if (myurl.contains("&PWorkID") == false)
					{
						myurl += "&PWorkID=" + this.getPWorkID();
					}


					myurl += "&FromFlow=" + this.getFK_Flow() + "&FromNode=" + this.getFK_Node() + "&UserNo=" + WebUser.getNo() + "&Token=" + WebUser.getToken();
					return "TurnUrl@" + myurl;

				case TurnToByCond:
					//TurnTos tts = new TurnTos(this.FK_Flow);
					//if (tts.size() == 0)
					//{
					//    BP.WF.Dev2Interface.Port_SendMsg("admin", currFlow.Name + "在" + currND.Name + "节点处，出现错误", "您没有设置节点完成后的转向条件。", "Err" + currND.No + "_" + this.WorkID, SMSMsgType.Err, this.FK_Flow, this.FK_Node, this.WorkID, this.FID);
					//    throw new Exception("@您没有设置节点完成后的转向条件。");
					//}

					//foreach (TurnTo tt in tts)
					//{
					//    tt.HisWork = currNode.HisWork;
					//    if (tt.IsPassed == true)
					//    {
					//        string url = tt.TurnToURL.ToString();
					//        if (url.contains("?") == false)
					//            url += "?1=1";
					//        Attrs attrs = currNode.HisWork.getEnMap().Attrs;
					//        Work hisWK1 = currNode.HisWork;
					//        foreach (Attr attr in attrs)
					//        {
					//            if (url.contains("@") == false)
					//                break;
					//            url = url.Replace("@" + attr.getKey(), hisWK1.GetValStrByKey(attr.getKey()));
					//        }
					//        if (url.contains("@"))
					//            throw new Exception("流程设计错误，在节点转向url中参数没有被替换下来。Url:" + url);

					//        url += "&PFlowNo=" + this.FK_Flow + "&FromNode=" + this.FK_Node + "&PWorkID=" + this.WorkID + "&UserNo=" + WebUser.getNo() + "&Token=" + WebUser.SID;
					//        return "url@" + url;
					//    }
					//}
					return msg;
				default:
					msg = msg.replace("@WebUser.No", WebUser.getNo());
					msg = msg.replace("@WebUser.Name", WebUser.getName());
					msg = msg.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
					return msg;
			}

				///#endregion

		}
		catch (RuntimeException ex)
		{
			if (ex.getMessage().indexOf("url@") == 0)
			{
				return ex.getMessage();
			}

			//清楚上次选择的节点信息.
			if (DataType.IsNullOrEmpty(this.getHisGenerWorkFlow().getParasToNodes()) == false)
			{
				this.getHisGenerWorkFlow().setParasToNodes("");
				this.getHisGenerWorkFlow().Update();
			}

			if (ex.getMessage().contains("请选择下一步骤工作") == true || ex.getMessage().contains("用户没有选择发送到的节点") == true)
			{
				if (this.getCurrND().getCondModel() == DirCondModel.ByDDLSelected || this.getCurrND().getCondModel() == DirCondModel.ByButtonSelected)
				{
					/*如果抛出异常，我们就让其转入选择到达的节点里, 在节点里处理选择人员. */
					return "SelectNodeUrl@./WorkOpt/ToNodes.htm?FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID();

				}

				//if (this.currND.CondModel != CondModel.SendButtonSileSelect)
				//{
				//    currND.CondModel = CondModel.SendButtonSileSelect;
				//    currND.Update();
				//}

				return "err@下一个节点的接收人规则是，当前节点选择来选择，在当前节点属性里您没有启动接受人按钮，系统自动帮助您启动了，请关闭窗口重新打开。" + ex.getMessage();
			}

			//绑定独立表单，表单自定义方案验证错误弹出窗口进行提示.
			if (ex.getMessage().contains("提交前检查到如下必填字段填写不完整") == true || ex.getMessage().contains("您没有上传附件") == true || ex.getMessage().contains("您没有上传图片附件") == true)
			{
				return "err@" + ex.getMessage().replace("@@", "@").replace("@", "<BR>@");
			}

			//防止发送失败丢失接受人，导致不能出现下拉方向选择框. @杜.
			if (this.getHisGenerWorkFlow() != null)
			{
				//如果是会签状态.
				if (this.getHisGenerWorkFlow().getHuiQianTaskSta() == HuiQianTaskSta.HuiQianing)
				{
					//如果是主持人.
					if (this.getHisGenerWorkFlow().getHuiQianZhuChiRen().equals(WebUser.getNo()))
					{
						if (this.getHisGenerWorkFlow().getTodoEmps().contains(WebUser.getNo() + ",") == false)
						{
							this.getHisGenerWorkFlow().setTodoEmps(this.getHisGenerWorkFlow().getTodoEmps() + WebUser.getNo() + "," + WebUser.getName() + ";");
							this.getHisGenerWorkFlow().Update();
						}
					}
					else
					{
						//非主持人.
						String empStr = WebUser.getNo() + "," + WebUser.getName() + ";";
						if (this.getHisGenerWorkFlow().getTodoEmps().contains(empStr) == false && WebUser.getNo().equals("Guest") == false)
						{
							this.getHisGenerWorkFlow().setTodoEmps(this.getHisGenerWorkFlow().getTodoEmps() + empStr); // bp.web.WebUser.getNo() +","+bp.web.WebUser.getName() + ";";
							this.getHisGenerWorkFlow().Update();
						}
					}
				}


				if (this.getHisGenerWorkFlow().getHuiQianTaskSta() != HuiQianTaskSta.HuiQianing)
				{
					String empStr = WebUser.getNo() + "," + WebUser.getName() + ";";
					if (this.getHisGenerWorkFlow().getTodoEmps().contains(empStr) == false)
					{
						this.getHisGenerWorkFlow().setTodoEmps(this.getHisGenerWorkFlow().getTodoEmps() + empStr);
						this.getHisGenerWorkFlow().Update();
					}
				}
			}

			//如果错误，就写标记.
			String msg = ex.getMessage();
			if (msg.indexOf("err@") == -1 && msg.indexOf("url@") != 0)
			{
				msg = "err@" + msg;
			}
			return msg;
		}
	}
	/** 
	 批量发送
	 
	 @return 
	*/
	public final String StartGuide_MulitSend() throws Exception {
		//获取设置的数据源
		Flow fl = new Flow(this.getFK_Flow());
		String key = this.GetRequestVal("Key");
		String SKey = this.GetRequestVal("Keys");
		String sql = "";
		//判断是否有查询条件
		Object tempVar = fl.getStartGuidePara2();
		sql = tempVar instanceof String ? (String)tempVar : null;
		if (!DataType.IsNullOrEmpty(key))
		{
			Object tempVar2 = fl.getStartGuidePara1();
			sql = tempVar2 instanceof String ? (String)tempVar2 : null;
			sql = sql.replace("@Key", key);
		}
		//替换变量
		sql = sql.replace("~", "'");
		sql = sql.replace("@WebUser.No", WebUser.getNo());
		sql = sql.replace("@WebUser.Name", WebUser.getName());
		sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
		sql = sql.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		//获取选中的数据源
		DataRow[] drArr = dt.Select("No in(" + StringHelper.trimEnd(SKey, ',') + ")");

		//获取Nos
		String Nos = "";
		for (int i = 0; i < drArr.length; i++)
		{
			DataRow row = drArr[i];
			Nos += row.getValue("No") + ",";
		}
		return StringHelper.trimEnd(Nos, ',');
	}
	/** 
	 保存
	 
	 @return 
	*/
	public final String Save() throws Exception {
		try
		{
			String str = Dev2Interface.Node_SaveWork(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.GetMainTableHT(), null, this.getFID(), this.getPWorkID());

			if (this.getPWorkID() != 0)
			{
				GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
				Dev2Interface.SetParentInfo(this.getFK_Flow(), this.getWorkID(), this.getPWorkID(), gwf.getPEmp(), gwf.getPNodeID());
			}
			return str;
		}
		catch (RuntimeException ex)
		{
			return "err@保存失败:" + ex.getMessage();
		}
	}
	public final String MyFlowSelfForm_Init() throws Exception {
		return this.GenerWorkNode();
	}

		public final String SaveFlow_ToDraftRole() throws Exception {

		Node nd = new Node(this.getFK_Node());
		Work wk = nd.getHisWork();
		if (this.getWorkID() != 0)
		{
			wk.setOID(this.getWorkID());
			wk.RetrieveFromDBSources();
		}

		//获取表单树的数据
		WorkNode workNode = new WorkNode(this.getWorkID(), this.getFK_Node());
		Work treeWork = workNode.CopySheetTree();
		if (treeWork != null)
		{
			wk.Copy(treeWork);
			wk.Update();
		}

		//获取该节点是是否是绑定表单方案, 如果流程节点中的字段与绑定表单的字段相同时赋值 
		//if (nd.FormType == NodeFormType.SheetTree || nd.FormType == NodeFormType.RefOneFrmTree)
		//{
		//    FrmNodes nds = new FrmNodes(this.FK_Flow, this.FK_Node);
		//    foreach (FrmNode item in nds)
		//    {
		//        if (item.FrmEnableRole == FrmEnableRole.Disable)
		//            continue;
		//        if (item.FK_Frm.Equals("ND"+this.FK_Node) == true)
		//            continue;
		//        GEEntity en = null;
		//        try
		//        {
		//            en = new GEEntity(item.FK_Frm);
		//            en.PKVal = this.WorkID;
		//            if (en.RetrieveFromDBSources() == 0)
		//            {
		//                continue;
		//            }
		//        }
		//        catch (Exception ex)
		//        {
		//            continue;
		//        }

		//        Attrs frmAttrs = en.getEnMap().getAttrs();
		//        Attrs wkAttrs = wk.getEnMap().getAttrs();
		//        foreach (Attr wkattr in wkAttrs)
		//        {
		//            if (wkattr.getKey().Equals(GERptAttr.OID) || wkattr.getKey().Equals(GERptAttr.FID) || wkattr.getKey().Equals(GERptAttr.CDT)
		//                || wkattr.getKey().Equals(GERptAttr.RDT) || wkattr.getKey().Equals(GERptAttr.MD5) || wkattr.getKey().Equals(GERptAttr.Emps)
		//                || wkattr.getKey().Equals(GERptAttr.FK_Dept) || wkattr.getKey().Equals(GERptAttr.PRI) || wkattr.getKey().Equals(GERptAttr.Rec)
		//                || wkattr.getKey().Equals(GERptAttr.Title) || wkattr.getKey().Equals(Data.GERptAttr.FK_NY) || wkattr.getKey().Equals(Data.GERptAttr.FlowEmps)
		//                || wkattr.getKey().Equals(Data.GERptAttr.FlowStarter) || wkattr.getKey().Equals(Data.GERptAttr.FlowStartRDT) || wkattr.getKey().Equals(Data.GERptAttr.WFState))
		//            {
		//                continue;
		//            }

		//            foreach (Attr attr in frmAttrs)
		//            {
		//                if (wkattr.getKey().Equals(attr.getKey()))
		//                {
		//                    wk.SetValByKey(wkattr.getKey(), en.GetValStrByKey(attr.getKey()));
		//                    break;
		//                }

		//            }

		//        }

		//    }
		//    wk.Update();
		//}


			///#region 为开始工作创建待办.
		if (nd.isStartNode() == true)
		{
			GenerWorkFlow gwf = new GenerWorkFlow();
			Flow fl = new Flow(this.getFK_Flow());
			if (fl.getDraftRole() == DraftRole.None && this.GetRequestValInt("SaveType") != 1)
			{
				return "保存成功";
			}

			//规则设置为写入待办，将状态置为运行中，其他设置为草稿.
			WFState wfState = WFState.Blank;
			if (fl.getDraftRole() == DraftRole.SaveToDraftList)
			{
				wfState = WFState.Draft;
			}
			if (fl.getDraftRole() == DraftRole.SaveToTodolist)
			{
				wfState = WFState.Runing;
			}

			//设置标题.
			String title = WorkFlowBuessRole.GenerTitle(fl, wk);

			//修改RPT表的标题
			wk.SetValByKey(GERptAttr.Title, title);
			wk.Update();

			gwf.setWorkID(this.getWorkID());
			int count = gwf.RetrieveFromDBSources();

			gwf.setTitle(title); //标题.
			if (count == 0)
			{
				gwf.setFlowName(fl.getName());
				gwf.setFK_Flow(this.getFK_Flow());
				gwf.setFK_FlowSort(fl.getFK_FlowSort());
				gwf.setSysType(fl.getSysType());

				gwf.setFK_Node(this.getFK_Node());
				gwf.setNodeName(nd.getName());
				gwf.setWFState(wfState);

				gwf.setFK_Dept(WebUser.getFK_Dept());
				gwf.setDeptName(WebUser.getFK_DeptName());
				gwf.setStarter(WebUser.getNo());
				gwf.setStarterName(WebUser.getName());
				gwf.setRDT(DataType.getCurrentDateTimess());
				gwf.Insert();

				// 产生工作列表.
				GenerWorkerList gwl = new GenerWorkerList();
				gwl.setWorkID(this.getWorkID());
				gwl.setFK_Emp(WebUser.getNo());
				gwl.setFK_EmpText(WebUser.getName());

				gwl.setFK_Node(gwf.getFK_Node());
				gwl.setFK_NodeText(nd.getName());
				gwl.setFID(0);

				gwl.setFK_Flow(gwf.getFK_Flow());
				gwl.setFK_Dept(WebUser.getFK_Dept());
				gwl.setFK_DeptT(WebUser.getFK_DeptName());

				gwl.setSDT("无");
				gwl.setDTOfWarning(DataType.getCurrentDateTimess());
				gwl.setEnable(true);

				gwl.setIsPass(false);
				//  gwl.Sender = WebUser.getNo();
				gwl.setPRI(gwf.getPRI());
				gwl.Insert();
			}
			else
			{
				gwf.setWFState(wfState);
				gwf.DirectUpdate();
			}

		}

			///#endregion 为开始工作创建待办

		return "保存到待办";
	}


		///#region 表单树操作
	public final String FlowFormTree2021_Init() throws Exception {
		//树形表单的类别
		FlowFormTrees formTree = new FlowFormTrees();

		//表单
		FlowFormTrees forms = new FlowFormTrees();

		FlowFormTree root = new FlowFormTree();
		root.setNo("1");
		root.setParentNo("0");
		root.setName("目录");
		root.setNodeType("root");


			///#region 添加表单及文件夹
		//当前节点绑定的表单集合
		FrmNodes frmNodes = new FrmNodes();
		frmNodes.Retrieve(FrmNodeAttr.FK_Node, this.getFK_Node(), FrmNodeAttr.Idx);

		//所有表单集合信息
		MapDatas mds = new MapDatas();
		mds.RetrieveInSQL("SELECT FK_Frm FROM WF_FrmNode WHERE FK_Node=" + this.getFK_Node());


			///#region 检查是否有没有目录的表单?
		boolean isHave = false;
		String treeNo = "";
		for (MapData md : mds.ToJavaList())
		{
			if (DataType.IsNullOrEmpty(md.getFK_FormTree()) == true)
			{
				isHave = true;
			}
			if (DataType.IsNullOrEmpty(md.getFK_FormTree()) == false)
			{
				treeNo = md.getFK_FormTree();
			}
		}
		if (isHave == true && DataType.IsNullOrEmpty(treeNo) == true)
		{
			treeNo = "1";
			formTree.AddEntity(root);
		}

			///#endregion 检查是否有没有目录的表单?

		//从外部参数获取.
		String frms = this.GetRequestVal("Frms");
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		if (DataType.IsNullOrEmpty(frms) == true)
		{
			frms = gwf.getParasFrms();
		}
		else
		{
			gwf.setParasFrms(frms);
			gwf.Update();
		}

		//如果有参数.
		if (DataType.IsNullOrEmpty(frms) == false)
		{
			frms = frms.trim();
			frms = "," + frms + ","; //特殊处理
			frms = frms.replace(" ", "");
			frms = frms.replace(" ", "");
			frms = frms.replace(" ", "");

			String[] strs = frms.split("[,]", -1);
			isHave = false; //检查是不否存在。
			for (String str : strs)
			{
				if (DataType.IsNullOrEmpty(str) == true)
				{
					continue;
				}

				//如果集合包含参数里面的表单，就不处理.
				if (frmNodes.contains(FrmNodeAttr.FK_Frm, str) == true)
				{
					continue;
				}

				//把有参数的表单插入到数据库里.
				FrmNode fn = new FrmNode();
				fn.setFKFrm(str);
				fn.setFK_Node(gwf.getFK_Node());
				fn.setFK_Flow(gwf.getFK_Flow());
				fn.setFrmEnableRole(FrmEnableRole.WhenHaveFrmPara); //设置有参数的时候启用.
				fn.setMyPK(str + "_" + gwf.getFK_Node() + "_" + gwf.getFK_Flow());

				if (String.valueOf(fn.getFK_Node()).endsWith("01") == true)
				{
					fn.setFrmSln(FrmSln.Default); //设置编辑方案, 为默认方案.
				}
				else
				{
					fn.setFrmSln(FrmSln.Readonly); //设置编辑方案, 为默认方案.
				}


				fn.Insert();
				isHave = true; //标记存在，用于更新查询的数据源.
			}
			if (isHave == true)
			{
				frmNodes.Retrieve(FrmNodeAttr.FK_Node, this.getFK_Node(), FrmNodeAttr.Idx);
				mds.RetrieveInSQL("SELECT FK_Frm FROM WF_FrmNode WHERE FK_Node=" + this.getFK_Node());
			}

		}

		//求出来要显示的表单集合.
		for (FrmNode frmNode : frmNodes.ToJavaList())
		{

				///#region 增加判断是否启用规则.
			switch (frmNode.getFrmEnableRole())
			{
				case Allways:
					break;
				case WhenHaveData: //判断是否有数据.
					Object tempVar = mds.GetEntityByKey(frmNode.getFKFrm());
					MapData mapData = tempVar instanceof MapData ? (MapData)tempVar : null;
					if (mapData == null)
					{
						continue;
					}
					long pk = this.getWorkID();
					switch (frmNode.getWhoIsPK())
					{
						case FID:
							pk = this.getFID();
							break;
						case PWorkID:
							pk = this.getPWorkID();
							break;
						case CWorkID:
							pk = this.getCWorkID();
							break;
						case OID:
						default:
							pk = this.getWorkID();
							break;
					}
					if (DBAccess.RunSQLReturnValInt("SELECT COUNT(*) as Num FROM " + mapData.getPTable() + " WHERE OID=" + pk) == 0)
					{
						continue;
					}
					break;
				case WhenHaveFrmPara: //判断是否有参数.
					if (DataType.IsNullOrEmpty(frms) == true)
					{
						continue;
					}
					if (frms.contains("," + frmNode.getFKFrm() + ",") == false)
					{
						continue;
					}
					break;
				case ByFrmFields:
					throw new RuntimeException("@这种类型的判断，ByFrmFields 还没有完成。");
				case BySQL: // 按照SQL的方式.
					Object tempVar2 = frmNode.getFrmEnableExp();
					String mysql = tempVar2 instanceof String ? (String)tempVar2 : null;
					if (DataType.IsNullOrEmpty(mysql) == true)
					{
						Object tempVar3 = mds.GetEntityByKey(frmNode.getFKFrm());
						MapData FrmMd = tempVar3 instanceof MapData ? (MapData)tempVar3 : null;
						return "err@表单" + frmNode.getFKFrm() + ",[" + FrmMd.getName() + "]在节点[" + frmNode.getFK_Node() + "]启用方式按照sql启用但是您没有给他设置sql表达式.";
					}

					mysql = mysql.replace("@OID", String.valueOf(this.getWorkID()));
					mysql = mysql.replace("@WorkID", String.valueOf(this.getWorkID()));

					mysql = mysql.replace("@NodeID", String.valueOf(this.getFK_Node()));
					mysql = mysql.replace("@FK_Node", String.valueOf(this.getFK_Node()));

					mysql = mysql.replace("@FK_Flow", this.getFK_Flow());

					mysql = mysql.replace("@WebUser.No", WebUser.getNo());
					mysql = mysql.replace("@WebUser.Name", WebUser.getName());
					mysql = mysql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

					//替换特殊字符.
					mysql = mysql.replace("~", "'");

					if (DBAccess.RunSQLReturnValFloat(mysql) <= 0)
					{
						continue;
					}
					break;

				case ByStation: //当前人员包含这个岗位
					Object tempVar4 = frmNode.getFrmEnableExp();
					String exp = tempVar4 instanceof String ? (String)tempVar4 : null;
					String Sql = "SELECT FK_Station FROM Port_DeptEmpStation where FK_Emp='" + WebUser.getNo() + "'";
					String station = DBAccess.RunSQLReturnString(Sql);
					if (DataType.IsNullOrEmpty(station) == true)
					{
						continue;
					}
					String[] stations = station.split("[;]", -1);
					boolean isExit = false;
					for (String s : stations)
					{
						if (exp.contains(s) == true)
						{
							isExit = true;
							break;
						}
					}
					if (isExit == false)
					{
						continue;
					}
					break;

				case ByDept:
					Object tempVar5 = frmNode.getFrmEnableExp();
					exp = tempVar5 instanceof String ? (String)tempVar5 : null;
					Sql = "SELECT FK_Dept FROM Port_DeptEmp where FK_Emp='" + WebUser.getNo() + "'";
					String dept = DBAccess.RunSQLReturnString(Sql);
					if (DataType.IsNullOrEmpty(dept) == true)
					{
						continue;
					}
					String[] deptStrs = dept.split("[;]", -1);
					isExit = false;
					for (String s : deptStrs)
					{
						if (exp.contains(s) == true)
						{
							isExit = true;
							break;
						}
					}
					if (isExit == false)
					{
						continue;
					}

					break;
				case Disable: // 如果禁用了，就continue出去..
					continue;
				default:
					throw new RuntimeException("err@没有判断的规则." + frmNode.getFrmEnableRole());
			}

				///#endregion

			Object tempVar6 = mds.GetEntityByKey(frmNode.getFKFrm());
			MapData md = tempVar6 instanceof MapData ? (MapData)tempVar6 : null;
			if (md == null)
			{
				frmNode.Delete();
				continue;
			}

			if (DataType.IsNullOrEmpty(md.getFK_FormTree()) == true)
			{
				md.setFK_FormTree(treeNo);
			}

			//增加目录.
			if (formTree.contains("Name", md.getFK_FormTreeText()) == false)
			{
				FlowFormTree nodeFolder = new FlowFormTree();
				nodeFolder.setNo(md.getFK_FormTree());
				nodeFolder.setParentNo("1");
				nodeFolder.setName(md.getFK_FormTreeText());
				nodeFolder.setNodeType("folder");
				formTree.AddEntity(nodeFolder);
			}

			//检查必填项.
			boolean IsNotNull = false;
			FrmFields formFields = new FrmFields();
			QueryObject obj = new QueryObject(formFields);
			obj.AddWhere(FrmFieldAttr.FK_Node, this.getFK_Node());
			obj.addAnd();
			obj.AddWhere(FrmFieldAttr.FK_MapData, md.getNo());
			obj.addAnd();
			obj.AddWhere(FrmFieldAttr.IsNotNull, 1);
			obj.DoQuery();
			if (formFields != null && formFields.size() > 0)
			{
				IsNotNull = true;
			}

			FlowFormTree nodeForm = new FlowFormTree();
			nodeForm.setNo(md.getNo());
			nodeForm.setParentNo(md.getFK_FormTree());

			//设置他的表单显示名字. 2019.09.30
			String frmName = md.getName();
			bp.en.Entity tempVar7 = frmNodes.GetEntityByKey(FrmNodeAttr.FK_Frm, md.getNo());
			FrmNode fn = tempVar7 instanceof FrmNode ? (FrmNode)tempVar7 : null;
			if (fn != null)
			{
				String str = fn.getFrmNameShow();
				if (DataType.IsNullOrEmpty(str) == false)
				{
					frmName = str;
				}
			}
			nodeForm.setName(frmName);
			nodeForm.setICON(md.getICON());
			nodeForm.setNodeType(IsNotNull ? "form|1" : "form|0");
			nodeForm.setEdit(String.valueOf(frmNode.isEditInt())); // Convert.ToString(Convert.ToInt32(frmNode.IsEdit));
			nodeForm.setCloseEtcFrm(String.valueOf(frmNode.isCloseEtcFrmInt()));
			forms.AddEntity(nodeForm);
		}

			///#endregion

		DataSet ds = new DataSet();
		ds.Tables.add(formTree.ToDataTableField("FormTree"));
		DataTable formdt = forms.ToDataTableField("Forms");
		formdt.Columns.Add("ICON");
//		for (int i = 0; i < forms.size(); i++)
//		{
//			if (DataType.IsNullOrEmpty(forms.get(i).ICON) == true)
//			{
//				formdt.Rows.get(i).setValue("ICON","icon-doc");
//			}
//			else
//			{
//				formdt.Rows.get(i).setValue("ICON",forms.get(i).ICON);
//			}
//		}

		ds.Tables.add(formdt);
		ds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));


			///#region 处理流程-消息提示.
		boolean isReadonly = this.GetRequestValBoolen("IsReadonly");
		if (isReadonly == true)
		{
			return bp.tools.Json.ToJson(ds);
		}

		DataTable dtAlert = new DataTable();
		dtAlert.TableName = "AlertMsg";
		dtAlert.Columns.Add("Title", String.class);
		dtAlert.Columns.Add("Msg", String.class);
		dtAlert.Columns.Add("URL", String.class);
		DataRow drMsg = null;
		String sql = "";
		int sta = gwf.GetParaInt("HungupSta", 0);
		switch (gwf.getWFState())
		{
			case Runing:
				drMsg = dtAlert.NewRow();
				drMsg.setValue("Title", "挂起信息");
				if (sta == 2 && gwf.getFK_Node() == gwf.GetParaInt("HungupNodeID", 0))
				{
					drMsg.setValue("Msg", "您的工单在挂起被拒绝，拒绝原因:" + gwf.GetParaString("HungupCheckMsg"));
					dtAlert.Rows.add(drMsg);
				}
				break;
			case HungUp:
				if (sta == -1)
				{
					break;
				}
				drMsg = dtAlert.NewRow();
				drMsg.set("Title", "挂起信息");
				if (sta == 0)
				{
					drMsg.setValue("Msg", "您的工单在挂起状态，等待审批，挂起原因：" + gwf.GetParaString("HungupNote"));
				}

				if (sta == 1)
				{
					drMsg.setValue("Msg", "您的工单在挂起获得同意.");
				}

				if (sta == 2)
				{
					drMsg.setValue("Msg", "您的工单在挂起被拒绝，拒绝原因:" + gwf.GetParaString("HungupCheckMsg"));
				}

				dtAlert.Rows.add(drMsg);
				break;
			case AskForReplay: // 返回加签的信息.
				String mysql = "SELECT Msg,EmpFrom,EmpFromT,RDT FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE WorkID=" + this.getWorkID() + " AND " + TrackAttr.ActionType + "=" + ActionType.ForwardAskfor.getValue();
				DataTable mydt = DBAccess.RunSQLReturnTable(mysql);

				for (DataRow dr : mydt.Rows)
				{
					String msgAskFor = dr.getValue(0).toString();
					String worker = dr.getValue(1).toString();
					String workerName = dr.getValue(2).toString();
					String rdt = dr.getValue(3).toString();

					drMsg = dtAlert.NewRow();
					drMsg.setValue("Title", worker + "," + workerName + "回复信息:");
					drMsg.setValue("Msg", DataType.ParseText2Html(msgAskFor) + "<br>" + rdt);
					dtAlert.Rows.add(drMsg);
				}
				break;
			case Askfor: //加签.

				sql = "SELECT * FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE WorkID=" + this.getWorkID() + " AND " + TrackAttr.ActionType + "=" + ActionType.AskforHelp.getValue();
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				for (DataRow dr : dt.Rows)
				{
					String msgAskFor = dr.getValue(TrackAttr.Msg).toString();
					String worker = dr.getValue(TrackAttr.EmpFrom).toString();
					String workerName = dr.getValue(TrackAttr.EmpFromT).toString();
					String rdt = dr.getValue(TrackAttr.RDT).toString();

					drMsg = dtAlert.NewRow();
					drMsg.setValue("Title", worker + "," + workerName + "请求加签:");
					drMsg.setValue("Msg", DataType.ParseText2Html(msgAskFor) + "<br>" + rdt + "<a href='./WorkOpt/AskForRe.htm?FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + gwf.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + gwf.getFID() + "' >回复加签意见</a> --");
					dtAlert.Rows.add(drMsg);

				}
				break;
			case ReturnSta:
				/* 如果工作节点退回了*/
				Node nd = new Node(this.getFK_Node());
				ReturnWorks ens = new ReturnWorks();
				if (nd.getHisRunModel() == RunModel.FL || nd.getHisRunModel() == RunModel.FHL)
				{
					QueryObject qo = new QueryObject(ens);
					qo.addLeftBracket();
					qo.AddWhere(ReturnWorkAttr.WorkID, this.getWorkID());
					qo.addOr();
					qo.AddWhere(ReturnWorkAttr.FID, this.getWorkID());
					qo.addRightBracket();
					qo.addAnd();
					qo.AddWhere(ReturnWorkAttr.ReturnToNode, nd.getNodeID());
					qo.addOrderBy("RDT");
					qo.DoQuery();
				}
				else
				{
					ens.Retrieve(ReturnWorkAttr.WorkID, this.getWorkID(), ReturnWorkAttr.ReturnToNode, nd.getNodeID(), "RDT");
				}


				String msgInfo = "";
				for (ReturnWork rw : ens.ToJavaList())
				{
					if (rw.getReturnToEmp().contains(WebUser.getNo()) == true)
					{
						msgInfo += "来自节点：" + rw.getReturnNodeName() + "@退回人：" + rw.getReturnerName() + "@退回日期：" + rw.getRDT();
						msgInfo += "@退回原因：" + rw.getBeiZhu();
						msgInfo += "<hr/>";
					}
				}

				msgInfo = msgInfo.replace("@", "<br>");
				if (DataType.IsNullOrEmpty(msgInfo) == false)
				{
					String str = nd.getReturnAlert();
					if (!str.equals(""))
					{
						str = str.replace("~", "'");
						str = str.replace("@PWorkID", String.valueOf(this.getWorkID()));
						str = str.replace("@PNodeID", String.valueOf(nd.getNodeID()));
						str = str.replace("@FK_Node", String.valueOf(nd.getNodeID()));

						str = str.replace("@PFlowNo", gwf.getFK_Flow());
						str = str.replace("@FK_Flow", gwf.getFK_Flow());
						str = str.replace("@PWorkID", String.valueOf(this.getWorkID()));

						str = str.replace("@WorkID", String.valueOf(this.getWorkID()));
						str = str.replace("@OID", String.valueOf(this.getWorkID()));

						drMsg = dtAlert.NewRow();
						drMsg.setValue("Title", "退回信息");
						drMsg.setValue("Msg", msgInfo + "\t\n" + str);
						dtAlert.Rows.add(drMsg);
					}
					else
					{
						drMsg = dtAlert.NewRow();
						drMsg.setValue("Title", "退回信息");
						drMsg.setValue("Msg", msgInfo + "\t\n" + str);
						dtAlert.Rows.add(drMsg);
					}
				}
				break;
			case Shift:
				/* 判断移交过来的。 */
				String sqlshift = "SELECT * FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE ACTIONTYPE=3 AND WorkID=" + this.getWorkID() + " AND NDFrom='" + gwf.getFK_Node() + "' ORDER BY RDT DESC ";
				DataTable dtshift = DBAccess.RunSQLReturnTable(sqlshift);
				String msg = "";
				if (dtshift.Rows.size() >= 1)
				{
					msg = "";
					drMsg = dtAlert.NewRow();
					drMsg.setValue("Title", "移交信息");
					//  msg = "<h3>移交信息 </h3><hr/>";
					for (DataRow dr : dtshift.Rows)
					{
						if (dr.getValue(TrackAttr.EmpTo).toString().equals(WebUser.getNo()))
						{
							String empFromT = dr.getValue(TrackAttr.EmpFromT).toString();
							String empToT = dr.getValue(TrackAttr.EmpToT).toString();
							String msgShift = dr.getValue(TrackAttr.Msg).toString();
							String rdt = dr.getValue(TrackAttr.RDT).toString();
							if (msgShift.equals("undefined"))
							{
								msgShift = "无";
							}

							msg += "移交人：" + empFromT + "@接受人：" + empToT + "@移交日期：" + rdt;
							msg += "@移交原因：" + msgShift;
							msg += "<hr/>";
						}
					}

					msg = msg.replace("@", "<br>");

					drMsg.setValue("Msg", msg);
					if (!DataType.IsNullOrEmpty(msg))
					{
						dtAlert.Rows.add(drMsg);
					}
				}
				break;
			default:
				break;
		}
		ds.Tables.add(dtAlert);

			///#endregion 处理流程-消息提示.

		return bp.tools.Json.ToJson(ds);
	}



	/** 
	 获取表单树数据
	 
	 @return 
	*/
	public final String FlowFormTree_Init() throws Exception {
		FlowFormTrees appFlowFormTree = new FlowFormTrees();

		//add root
		FlowFormTree root = new FlowFormTree();
		root.setNo("1");
		root.setParentNo("0");
		root.setName("目录");
		root.setNodeType("root");
		appFlowFormTree.AddEntity(root);


			///#region 添加表单及文件夹

		//节点表单
		Node nd = new Node(this.getFK_Node());

		FrmNodes frmNodes = new FrmNodes();
		frmNodes.Retrieve(FrmNodeAttr.FK_Node, this.getFK_Node(), FrmNodeAttr.Idx);

		//文件夹
		//SysFormTrees formTrees = new SysFormTrees();
		//formTrees.RetrieveAll(SysFormTreeAttr.getName());

		//所有表单集合. 为了优化效率,这部分重置了一下.
		MapDatas mds = new MapDatas();
		if (frmNodes.size() <= 3)
		{
			for (FrmNode fn : frmNodes.ToJavaList())
			{
				MapData md = new MapData(fn.getFKFrm());
				mds.AddEntity(md);
			}
		}
		else
		{
			mds.RetrieveInSQL("SELECT FK_Frm FROM WF_FrmNode WHERE FK_Node=" + this.getFK_Node());
		}


		String frms = this.GetRequestVal("Frms");
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		if (DataType.IsNullOrEmpty(frms) == true)
		{
			frms = gwf.getParasFrms();
		}
		else
		{
			gwf.setParasFrms(frms);
			gwf.Update();
		}

		for (FrmNode frmNode : frmNodes.ToJavaList())
		{

				///#region 增加判断是否启用规则.
			switch (frmNode.getFrmEnableRole())
			{
				case Allways:
					break;
				case WhenHaveData: //判断是否有数据.
					Object tempVar = mds.GetEntityByKey(frmNode.getFKFrm());
					MapData md = tempVar instanceof MapData ? (MapData)tempVar : null;
					if (md == null)
					{
						continue;
					}
					long pk = this.getWorkID();
					switch (frmNode.getWhoIsPK())
					{
						case FID:
							pk = this.getFID();
							break;
						case PWorkID:
							pk = this.getPWorkID();
							break;
						case CWorkID:
							pk = this.getCWorkID();
							break;
						case OID:
						default:
							pk = this.getWorkID();
							break;
					}
					if (DBAccess.RunSQLReturnValInt("SELECT COUNT(*) as Num FROM " + md.getPTable() + " WHERE OID=" + pk) == 0)
					{
						continue;
					}
					break;
				case WhenHaveFrmPara: //判断是否有参数.

					frms = frms.trim();
					frms = frms.replace(" ", "");
					frms = frms.replace(" ", "");

					if (DataType.IsNullOrEmpty(frms) == true)
					{
						continue;
					}

					if (frms.contains(",") == false && frms.equals(frmNode.getFKFrm()) == false)
					{
						continue;
					}

					if (frms.contains(",") == true && frms.contains(frmNode.getFKFrm() + ",") == false)
					{
						continue;
					}

					break;
				case ByFrmFields:
					throw new RuntimeException("@这种类型的判断，ByFrmFields 还没有完成。");

				case BySQL: // 按照SQL的方式.
					Object tempVar2 = frmNode.getFrmEnableExp();
					String mysql = tempVar2 instanceof String ? (String)tempVar2 : null;

					if (DataType.IsNullOrEmpty(mysql) == true)
					{
						MapData FrmMd = new MapData(frmNode.getFKFrm());
						return "err@表单" + frmNode.getFKFrm() + ",[" + FrmMd.getName() + "]在节点[" + frmNode.getFK_Node() + "]启用方式按照sql启用但是您没有给他设置sql表达式.";
					}


					mysql = mysql.replace("@OID", String.valueOf(this.getWorkID()));
					mysql = mysql.replace("@WorkID", String.valueOf(this.getWorkID()));

					mysql = mysql.replace("@NodeID", String.valueOf(this.getFK_Node()));
					mysql = mysql.replace("@FK_Node", String.valueOf(this.getFK_Node()));

					mysql = mysql.replace("@FK_Flow", this.getFK_Flow());

					mysql = mysql.replace("@WebUser.No", WebUser.getNo());
					mysql = mysql.replace("@WebUser.Name", WebUser.getName());
					mysql = mysql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());


					//替换特殊字符.
					mysql = mysql.replace("~", "'");

					if (DBAccess.RunSQLReturnValFloat(mysql) <= 0)
					{
						continue;
					}
					break;

				case ByStation:
					String exp = frmNode.getFrmEnableExp();
					String Sql = "SELECT FK_Station FROM Port_DeptEmpStation where FK_Emp='" + WebUser.getNo() + "'";
					String station = DBAccess.RunSQLReturnString(Sql);
					if (DataType.IsNullOrEmpty(station) == true)
					{
						continue;
					}
					String[] stations = station.split("[;]", -1);
					boolean isExit = false;
					for (String s : stations)
					{
						if (exp.contains(s) == true)
						{
							isExit = true;
							break;
						}
					}
					if (isExit == false)
					{
						continue;
					}
					break;

				case ByDept:
					Object tempVar3 = frmNode.getFrmEnableExp();
					exp = tempVar3 instanceof String ? (String)tempVar3 : null;
					Sql = "SELECT FK_Dept FROM Port_DeptEmp where FK_Emp='" + WebUser.getNo() + "'";
					String dept = DBAccess.RunSQLReturnString(Sql);
					if (DataType.IsNullOrEmpty(dept) == true)
					{
						continue;
					}
					String[] deptStrs = dept.split("[;]", -1);
					isExit = false;
					for (String s : deptStrs)
					{
						if (exp.contains(s) == true)
						{
							isExit = true;
							break;
						}
					}
					if (isExit == false)
					{
						continue;
					}

					break;
				case Disable: // 如果禁用了，就continue出去..
					continue;
				default:
					throw new RuntimeException("@没有判断的规则." + frmNode.getFrmEnableRole());
			}

				///#endregion


				///#region 检查是否有没有目录的表单?
			boolean isHave = false;
			for (MapData md : mds.ToJavaList())
			{
				if (md.getFK_FormTree().equals(""))
				{
					isHave = true;
					break;
				}
			}

			String treeNo = "0";
			if (isHave && mds.size() == 1)
			{
				treeNo = "00";
			}
			else if (isHave == true)
			{
				for (MapData md : mds.ToJavaList())
				{
					if (!md.getFK_FormTree().equals(""))
					{
						treeNo = md.getFK_FormTree();
						break;
					}
				}
			}

				///#endregion 检查是否有没有目录的表单?

			for (MapData md : mds.ToJavaList())
			{
				if (!frmNode.getFKFrm().equals(md.getNo()))
				{
					continue;
				}

				if (md.getFK_FormTree().equals(""))
				{
					md.setFK_FormTree(treeNo);
				}

				//给他增加目录.
				if (appFlowFormTree.contains("Name", md.getFK_FormTreeText()) == false)
				{
					FlowFormTree nodeFolder = new FlowFormTree();
					nodeFolder.setNo(md.getFK_FormTree());
					nodeFolder.setParentNo("1");
					nodeFolder.setName(md.getFK_FormTreeText());
					nodeFolder.setNodeType("folder");
					appFlowFormTree.AddEntity(nodeFolder);
				}

				//检查必填项.
				boolean IsNotNull = false;
				FrmFields formFields = new FrmFields();
				QueryObject obj = new QueryObject(formFields);
				obj.AddWhere(FrmFieldAttr.FK_Node, this.getFK_Node());
				obj.addAnd();
				obj.AddWhere(FrmFieldAttr.FK_MapData, md.getNo());
				obj.addAnd();
				obj.AddWhere(FrmFieldAttr.IsNotNull, 1);
				obj.DoQuery();
				if (formFields != null && formFields.size() > 0)
				{
					IsNotNull = true;
				}

				FlowFormTree nodeForm = new FlowFormTree();
				nodeForm.setNo(md.getNo());
				nodeForm.setParentNo(md.getFK_FormTree());

				//设置他的表单显示名字. 2019.09.30
				String frmName = md.getName();
				bp.en.Entity tempVar4 = frmNodes.GetEntityByKey(FrmNodeAttr.FK_Frm, md.getNo());
				FrmNode fn = tempVar4 instanceof FrmNode ? (FrmNode)tempVar4 : null;
				if (fn != null)
				{
					String str = fn.getFrmNameShow();
					if (DataType.IsNullOrEmpty(str) == false)
					{
						frmName = str;
					}
				}
				nodeForm.setName(frmName);
				nodeForm.setNodeType(IsNotNull ? "form|1" : "form|0");
				nodeForm.setEdit(String.valueOf(frmNode.isEditInt())); // Convert.ToString(Convert.ToInt32(frmNode.IsEdit));
				nodeForm.setCloseEtcFrm(String.valueOf(frmNode.isCloseEtcFrmInt()));
				appFlowFormTree.AddEntity(nodeForm);
				break;
			}
		}

			///#endregion
		//增加到数据结构上去.
		TansEntitiesToGenerTree(appFlowFormTree, root.getNo(), "");


		return appendMenus.toString();
	}
	/** 
	 将实体转为树形
	 
	 param ens
	 param rootNo
	 param checkIds
	*/
	private StringBuilder appendMenus = new StringBuilder();
	private StringBuilder appendMenuSb = new StringBuilder();
	public final void TansEntitiesToGenerTree(Entities ens, String rootNo, String checkIds) throws Exception {
		Object tempVar = ens.GetEntityByKey(rootNo);
		EntityTree root = tempVar instanceof EntityTree ? (EntityTree)tempVar : null;
		if (root == null)
		{
			throw new RuntimeException("@没有找到rootNo=" + rootNo + "的entity.");
		}
		appendMenus.append("[{");
		appendMenus.append("\"id\":\"" + rootNo + "\"");
		appendMenus.append(",\"text\":\"" + root.getName() + "\"");

		//attributes
		FlowFormTree formTree = root instanceof FlowFormTree ? (FlowFormTree)root : null;
		if (formTree != null)
		{
			String url = formTree.getUrl() == null ? "" : formTree.getUrl();
			url = url.replace("/", "|");
			appendMenus.append(",\"attributes\":{\"NodeType\":\"" + formTree.getNodeType() + "\",\"IsEdit\":\"" + formTree.isEdit() + "\",\"IsCloseEtcFrm\":\"" + formTree.isCloseEtcFrm() + "\",\"Url\":\"" + url + "\"}");
		}
		appendMenus.append(",iconCls:\"icon-Wave\"");
		// 增加它的子级.
		appendMenus.append(",\"children\":");
		AddChildren(root, ens, checkIds);

		appendMenus.append(appendMenuSb);
		appendMenus.append("}]");
	}

	private void AddChildren(EntityTree parentEn, Entities ens, String checkIds)
	{
		appendMenus.append(appendMenuSb);
		appendMenuSb.setLength(0);

		appendMenuSb.append("[");
		for (Entity en : ens.ToJavaListEn()) {
			EntityTree item = (EntityTree) en;
			if (!parentEn.getNo().equals(item.getParentNo()))
			{
				continue;
			}

			if (checkIds.contains("," + item.getNo() + ","))
			{
				appendMenuSb.append("{\"id\":\"" + item.getNo() + "\",\"text\":\"" + item.getName() + "\",\"checked\":true");
			}
			else
			{
				appendMenuSb.append("{\"id\":\"" + item.getNo() + "\",\"text\":\"" + item.getName() + "\",\"checked\":false");
			}


			//attributes
			FlowFormTree formTree = item instanceof FlowFormTree ? (FlowFormTree)item : null;
			if (formTree != null)
			{
				String url = formTree.getUrl() == null ? "" : formTree.getUrl();
				String ico = "icon-tree_folder";
				if (SystemConfig.getSysNo().equals("YYT"))
				{
					ico = "icon-boat_16";
				}
				url = url.replace("/", "|");
				appendMenuSb.append(",\"attributes\":{\"NodeType\":\"" + formTree.getNodeType() + "\",\"IsEdit\":\"" + formTree.isEdit() + "\",\"IsCloseEtcFrm\":\"" + formTree.isCloseEtcFrm() + "\",\"Url\":\"" + url + "\"}");
				//图标
				if (formTree.getNodeType().equals("form|0"))
				{
					ico = "form0";
					if (SystemConfig.getSysNo().equals("YYT"))
					{
						ico = "icon-Wave";
					}
				}
				if (formTree.getNodeType().equals("form|1"))
				{
					ico = "form1";
					if (SystemConfig.getSysNo().equals("YYT"))
					{
						ico = "icon-Shark_20";
					}
				}
				if (formTree.getNodeType().contains("tools"))
				{
					ico = "icon-4";
					if (SystemConfig.getSysNo().equals("YYT"))
					{
						ico = "icon-Wave";
					}
				}
				appendMenuSb.append(",iconCls:\"");
				appendMenuSb.append(ico);
				appendMenuSb.append("\"");
			}
			// 增加它的子级.
			appendMenuSb.append(",\"children\":");
			AddChildren(item, ens, checkIds);
			appendMenuSb.append("},");
		}
		if (appendMenuSb.length() > 1)
		{
			appendMenuSb = appendMenuSb.deleteCharAt(appendMenuSb.length() - 1);
		}
		appendMenuSb.append("]");
		appendMenus.append(appendMenuSb);
		appendMenuSb.setLength(0);
	}

		///#endregion

	/** 
	 产生一个工作节点
	 
	 @return 
	*/
	public final String GenerWorkNode() throws Exception {
		String json = "";
		DataSet ds = new DataSet();
		long workID = this.getWorkID(); //表单的主表.


			///#region 判断当前的节点类型,获得表单的ID.
		try
		{
			if (this.getCurrND().getHisFormType() == NodeFormType.RefOneFrmTree)
			{
				MapData md = new MapData(this.getCurrND().getNodeFrmID());
				if (md.getHisFrmType() == FrmType.ChapterFrm)
				{
					String url = "Frm.htm?FK_MapData=" + md.getNo();
					url = MyFlow_Init_DealUrl(this.getCurrND(), url);
					return "url@"+url;
				}
				//获取绑定的表单
				FrmNode frmnode = new FrmNode(this.getFK_Node(), this.getCurrND().getNodeFrmID());
				switch (frmnode.getWhoIsPK())
				{
					case FID:
						workID = this.getFID();
						break;
					case PWorkID:
						workID = this.getPWorkID();
						break;
					case P2WorkID:
						GenerWorkFlow gwff = new GenerWorkFlow(this.getPWorkID());
						workID = gwff.getPWorkID();
						break;
					case P3WorkID:
						String sqlId = "Select PWorkID From WF_GenerWorkFlow Where WorkID=(Select PWorkID From WF_GenerWorkFlow Where WorkID=" + this.getPWorkID() + ")";
						workID = DBAccess.RunSQLReturnValInt(sqlId, 0);
						break;
					case RootFlowWorkID:
						workID = Dev2Interface.GetRootWorkIDBySQL(this.getWorkID(), this.getPWorkID());
						break;
					default:
						break;
				}
			}
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}

			///#endregion 判断当前的节点类型,获得表单的ID.


			///#region 主题方法.
		try
		{
			ds = CCFlowAPI.GenerWorkNode(this.getFK_Flow(), this.getCurrND(), workID, this.getFID(), WebUser.getNo(), this.getWorkID());

			json = bp.tools.Json.ToJson(ds);

			//ds.WriteXml("c:\\generWorkNodeJS.xml");
			//bp.da.DataType.WriteFile("c:\\generWorkNodeJS.txt", json);
			//ds.Tables.add(wf_generWorkFlowDt);

			if (WebUser.getSysLang().equals("CH") == true)
			{
				return bp.tools.Json.ToJson(ds);
			}


				///#region 处理多语言.
			if (WebUser.getSysLang().equals("CH") == false)
			{
				Langues langs = new Langues();
				langs.Retrieve(LangueAttr.Model, LangueModel.CCForm, LangueAttr.Sort, "Fields", LangueAttr.Langue, WebUser.getSysLang(), null); //查询语言.
			}

				///#endregion 处理多语言.

			return json;
		}
		catch (RuntimeException ex)
		{
			Log.DebugWriteError(ex);
			return "err@" + ex.getMessage();
		}


			///#endregion 主题方法.

	}

	/** 
	 初始化子线程信息
	 
	 @return 
	*/
	public final String ThreadDtl_Init() throws Exception {
		DataSet ds = new DataSet();
		//当前节点的信息
		Node nd = new Node(this.getFK_Node());
		ds.Tables.add(nd.ToDataTableField("WF_Node"));

		GenerWorkFlow gwf = new GenerWorkFlow(this.getFID());
		Nodes nds = nd.getHisToNodes();
		ds.Tables.add(nds.ToDataTableField("WF_ThreadNode"));
		//发起子流程的workIds;
		 DataTable dt = DBAccess.RunSQLReturnTable("SELECT DISTINCT(WorkID) FROM WF_GenerWorkerlist WHERE FID=" + this.getFID() + " AND FK_Node=" + this.getFK_Node() + " AND IsPass IN(0,-2)");
		if (dt.Rows.size() == 0)
		{
			return "url@MyFlow";
		}
		String workIds = "";
		for (DataRow dr : dt.Rows)
		{
			workIds += dr.getValue(0).toString() + ",";
		}
		if (DataType.IsNullOrEmpty(workIds) == false)
		{
			workIds = workIds.substring(0, workIds.length() - 1);
		}
		//子线程流程实例信息
		GenerWorkFlows gwfs = new GenerWorkFlows();
		QueryObject qo = new QueryObject(gwfs);
		qo.AddWhereIn(GenerWorkFlowAttr.WorkID, "(" + workIds + ")");
		qo.addAnd();
		qo.AddWhere(GenerWorkFlowAttr.FID, this.getFID());
		qo.DoQuery();
		ds.Tables.add(gwfs.ToDataTableField("WF_GenerWorkFlow"));

		//子线程执行人员信息
		GenerWorkerLists gwls = new GenerWorkerLists();
		qo = new QueryObject(gwls);
		qo.AddWhereIn(GenerWorkerListAttr.WorkID,"(" + workIds + ")");
		qo.addAnd();
		qo.AddWhere(GenerWorkerListAttr.FID, this.getFID());
		qo.DoQuery();
		ds.Tables.add(gwls.ToDataTableField("WF_GenerWorkerList"));


		if (nd.getHisRunModel() == RunModel.FL || nd.getHisRunModel() == RunModel.FHL)
		{
			//获取退回信息
			ReturnWorks rws = new ReturnWorks();
			qo = new QueryObject(rws);
			qo.AddWhereInSQL(ReturnWorkAttr.WorkID, "SELECT WorkID From WF_GenerWorkFlow Where FID=" + this.getFID() + " AND WFState=5");
			qo.addOrderByDesc("RDT");
			qo.DoQuery();
			ds.Tables.add(rws.ToDataTableField("WF_ReturnWork"));
		}

		return bp.tools.Json.ToJson(ds);
	}

	/** 
	 发送单个子线程
	 
	 @return 
	*/
	public final String ThreadDtl_SendSubThread() throws Exception {
		int toNodeID = this.GetRequestValInt("ToNodeID");
		return Dev2Interface.Node_SendSubTread(this.getWorkID(), this.getFK_Node(), toNodeID);
	}

	/** 
	 删除子线程
	 
	 @return 
	*/
	public final String ThreadDtl_DelSubThread() throws Exception {

		//子线程的流程实例
		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.SetValByKey(GenerWorkFlowAttr.WorkID, this.getWorkID());
		int count = gwf.RetrieveFromDBSources();
		if (count == 0)
		{
			return "删除成功";
		}
		//干流程的流程实例
		GenerWorkFlow fgwf = new GenerWorkFlow(gwf.getFID());
		if (gwf.RetrieveFromDBSources() > 0)
		{
			WorkFlow wf = new WorkFlow(this.getWorkID());
			String msg = wf.DoDeleteWorkFlowByReal(false);

			Dev2Interface.WriteTrackInfo(gwf.getFK_Flow(), gwf.getFK_Node(), gwf.getNodeName(), gwf.getFID(), 0, "分流点手工删除", "删除子线程");

			//发起子线程的数量
			int threadCount = DBAccess.RunSQLReturnValInt("SELECT Count(*) FROM WF_GenerWorkerlist WHERE FID=" + fgwf.getWorkID() + " AND FK_Node=" + fgwf.getFK_Node() + " AND IsPass IN(0,-2)");
			//发起的子线程已经全部取消，
			if (threadCount == 0)
			{
				Paras ps = new Paras();
				ps.SQL = "UPDATE WF_GenerWorkerList SET IsPass=0 WHERE FK_Node=" + SystemConfig.getAppCenterDBVarStr() + "FK_Node AND WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID AND FK_Emp=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
				ps.Add("FK_Node", fgwf.getFK_Node());
				ps.Add("WorkID", gwf.getFID());
				ps.Add("FK_Emp", WebUser.getNo(), false);
				DBAccess.RunSQL(ps);
				fgwf.setWFState(WFState.Runing);
				fgwf.setTodoEmps(WebUser.getNo() + "," + WebUser.getName() + ";");
				fgwf.Update();
				return "url@MyFlow.htm";
			}

			//获取子线程对应的合流节点
			String sql = "SELECT DISTINCT(FK_Node) FROM WF_GenerWorkerlist WHERE WorkID=" + gwf.getFID() + " AND IsPass=3";
			int hlNodeID = DBAccess.RunSQLReturnValInt(sql,0);
			if (hlNodeID == 0 && threadCount != 0)
			{
				return "删除成功";
			}
			//获取已完成的子线程
			sql = "SELECT COUNT(*) FROM WF_GenerWorkFlow  WHERE FID=" + gwf.getFID() + " AND FK_Node =" + hlNodeID;
			//已经到合流点的子线程
			int toHLCount = DBAccess.RunSQLReturnValInt(sql);
			//合流点通过的比例
			Node nd = new Node(hlNodeID);
			BigDecimal passRate = BigDecimal.valueOf(toHLCount / threadCount * 100);
			if (nd.getPassRate().compareTo(passRate) <= 0)
			{
				//分流点的待办改成已办
				Paras ps = new Paras();
				ps.SQL = "UPDATE WF_GenerWorkerList SET IsPass=1 WHERE FK_Node=" + SystemConfig.getAppCenterDBVarStr() + "FK_Node AND WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID";
				ps.Add("FK_Node", fgwf.getFK_Node());
				ps.Add("WorkID", fgwf.getWorkID());
				/* 这时已经通过,可以让主线程看到待办. */
				ps = new Paras();
				ps.SQL = "UPDATE WF_GenerWorkerList SET IsPass=0 WHERE FK_Node=" + SystemConfig.getAppCenterDBVarStr() + "FK_Node AND WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID";
				ps.Add("FK_Node", nd.getNodeID());
				ps.Add("WorkID", fgwf.getWorkID());
				int num = DBAccess.RunSQL(ps);
				if (num == 0)
				{
					throw new RuntimeException("@不应该更新不到它.");
				}

				fgwf.setFK_Node(hlNodeID);
				fgwf.SetPara("ThreadCount", 0);
				fgwf.Update();
				return "url@MyView.htm";
			}
		}
		return "删除成功";
	}


	/**
	 * 驳回给子线程
	 */
	public final String ReSend() throws Exception {
		SendReturnObjs objs = Dev2Interface.Node_SendWork(this.getFK_Flow(), this.getWorkID());
		return objs.ToMsgOfHtml();
	}
	/**
	 * 删除指定ID的子线程
	 */
	public final String KillThread() throws Exception {
		Node nd = new Node(this.getFK_Node());
		if ((nd.getHisRunModel() != RunModel.FL && nd.getHisRunModel() != RunModel.FHL) || this.getFID() != 0)
		{
			return "err@该节点不是子线程返回的分流节点，不能删除子线程";
		}
		//首先要检查，当前的处理人是否是分流节点的处理人？如果是，就要把，未走完的所有子线程都删除掉。
		GenerWorkerList gwl = new GenerWorkerList();

		//查询已经走得分流节点待办.
		int i = gwl.Retrieve(GenerWorkerListAttr.WorkID, this.getWorkID(), GenerWorkerListAttr.FK_Node, this.getFK_Node(), GenerWorkerListAttr.FK_Emp, WebUser.getNo());
		if (i == 0)
		{
			return "err@您不能执行子线程的操作，因为当前分流工作不是您发送的。";
		}
		gwl.setIsPassInt(1);
		gwl.setRead(true);
		gwl.setSDT(DataType.getCurrentDateTimess());
		gwl.Update();


		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.setWorkID(this.getWorkID());
		i = gwf.RetrieveFromDBSources();
		if (i == 0)
		{
			return "err@没有获取到子线程操作的流程数据GenerWorkFlow[" + this.getWorkID() + "]";
		}

		Node thredNode = new Node(gwf.getFK_Node());
		//删除子线程的操作
		Dev2Interface.Flow_DeleteSubThread(this.getWorkID(), "分流节点删除子线程.");

		//删除子线程的数据
		Works wks = thredNode.getHisWorks();
		if ((new Flow(this.getFK_Flow())).getHisDataStoreModel() == DataStoreModel.ByCCFlow)
		{
			wks.Delete(GenerWorkerListAttr.WorkID, this.getWorkID());
		}

		return "";
	}

	/**
	 * 删除所有的子线程
	**/
	public final String UnSendAllTread() throws Exception {
		Node nd = new Node(this.getFK_Node());
		if ((nd.getHisRunModel() != RunModel.FL && nd.getHisRunModel() != RunModel.FHL) && this.getFID() != 0)
		{
			return "err@该节点不是子线程返回的分流节点，不能删除子线程";
		}

		GenerWorkFlow gwf = new GenerWorkFlow(this.getFID());
		//首先要检查，当前的处理人是否是分流节点的处理人？如果是，就要把，未走完的所有子线程都删除掉。
		GenerWorkerList gwl = new GenerWorkerList();

		//查询已经走得分流节点待办.
		int i = gwl.Retrieve(GenerWorkerListAttr.WorkID, this.getFID(), GenerWorkerListAttr.FK_Node, this.getFK_Node(), GenerWorkerListAttr.FK_Emp, WebUser.getNo());
		if (i == 0)
		{
			return "err@您不能执行删除子线程的操作，因为当前分流工作不是您发送的。";
		}

		// 更新分流节点，让其出现待办.
		gwl.setIsPassInt(0);
		gwl.setRead(false);
		gwl.setSDT(DataType.getCurrentDateTimess()); //这里计算时间有问题.
		gwl.Update();

		// 把设置当前流程运行到分流流程上.
		gwf.setFK_Node(this.getFK_Node());

		gwf.setNodeName(nd.getName());
		gwf.setSender(WebUser.getNo() + "," + WebUser.getName() + ";");
		gwf.setSendDT(DataType.getCurrentDateTimess());
		gwf.SetPara("ThreadCount", 0);
		gwf.setWFState(WFState.Runing);
		gwf.Update();


		Work wk = nd.getHisWork();
		wk.setOID(gwf.getWorkID());
		wk.RetrieveFromDBSources();

		// 记录日志..
		WorkNode wn = new WorkNode(wk, nd);
		wn.AddToTrack(ActionType.DeleteSubThread, WebUser.getNo(), WebUser.getName() , gwf.getFK_Node(), gwf.getNodeName(), "删除分流节点" + nd.getName() + "[" + nd.getNodeID() + "],发起的所有子线程");


		//删除上一个节点的数据。
		for (Node ndNext : nd.getHisToNodes().ToJavaList())
		{
			i = DBAccess.RunSQL("DELETE FROM WF_GenerWorkerList WHERE FID=" + this.getFID() + " AND FK_Node=" + ndNext.getNodeID());
			if (i == 0)
			{
				continue;
			}

			if (ndNext.getHisRunModel() == RunModel.SubThread)
			{
				/*如果到达的节点是子线程,就查询出来发起的子线程。*/
				GenerWorkFlows gwfs = new GenerWorkFlows();
				gwfs.Retrieve(GenerWorkFlowAttr.FID, this.getFID(), null);
				for (GenerWorkFlow en : gwfs.ToJavaList())
				{
					Dev2Interface.Flow_DeleteSubThread(en.getWorkID(), "分流节点删除子线程.");
				}

				continue;
			}

			// 删除工作记录。
			Works wks = ndNext.getHisWorks();
			if ((new Flow(this.getFK_Flow())).getHisDataStoreModel() == DataStoreModel.ByCCFlow)
			{
				wks.Delete(GenerWorkerListAttr.FID, this.getFID());
			}


		}

		return "url@MyFlow.htm?FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getFID();
	}

	public final String MyFlow_StartThread() throws Exception {
		Node nd = new Node(this.getFK_Node());

		//查询出来该流程实例下的所有草稿子流程.
		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.Retrieve(GenerWorkFlowAttr.PWorkID, this.getWorkID(), GenerWorkFlowAttr.WFState, 1, null);

		//子流程配置信息.
		SubFlowHandGuide sf = null;
		SendReturnObjs returnObjs;
		String msgHtml = "";

		//开始发送子流程.
		for (GenerWorkFlow gwfSubFlow : gwfs.ToJavaList())
		{
			//获得配置信息.
			if (sf == null || !sf.getFK_Flow().equals(gwfSubFlow.getFK_Flow()))
			{
				String pkval = this.getFK_Flow() + "_" + gwfSubFlow.getFK_Flow() + "_0";
				sf = new SubFlowHandGuide();
				sf.setMyPK(pkval);
				sf.RetrieveFromDBSources();
			}

			//把草稿移交给当前人员. - 更新控制表.
			gwfSubFlow.setStarter(WebUser.getNo());
			gwfSubFlow.setStarterName(WebUser.getName());
			gwfSubFlow.Update();
			//把草稿移交给当前人员. - 更新工作人员列表.
			DBAccess.RunSQL("UPDATE WF_GenerWorkerList SET FK_Emp='" + WebUser.getNo() + "',FK_EmpText='" + WebUser.getName() + "' WHERE WorkID=" + gwfSubFlow.getWorkID());
			//更新track表.
			//DBAccess.RunSQL("UPDATE ND"+int.Parse(gwfSubFlow.FK_Flow) +"Track SET FK_Emp='" + WebUser.getNo() + "',FK_EmpText='" + WebUser.getName() + "' WHERE WorkID=" + gwfSubFlow.WorkID);

			//启动子流程. 并把两个字段，写入子流程.
			returnObjs = Dev2Interface.Node_SendWork(gwfSubFlow.getFK_Flow(), gwfSubFlow.getWorkID(), null, null);
			msgHtml += returnObjs.ToMsgOfHtml() + "</br>";
		}
		return "启动的子流程信息如下:</br>" + msgHtml;
	}
}
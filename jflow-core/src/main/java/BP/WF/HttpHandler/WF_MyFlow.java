package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.util.*;
import java.time.*;

/** 
 初始化函数
*/
public class WF_MyFlow extends DirectoryPageBase
{

		///#region  运行变量
	/** 
	 从节点.
	*/
	public final String getFromNode()
	{
		return this.GetRequestVal("FromNode");
	}
	/** 
	 是否抄送
	*/
	public final boolean getIsCC()
	{
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
	public final String getTrackID()
	{
		return this.GetRequestVal("TrackeID");
	}
	/** 
	 到达的节点ID
	*/
	public final int getToNode()
	{
		return this.GetRequestValInt("ToNode");
	}
	private int _FK_Node = 0;
	/** 
	 当前的 NodeID ,在开始时间,nodeID,是地一个,流程的开始节点ID.
	*/
	public final int getFK_Node()
	{
		String fk_nodeReq = this.GetRequestVal("FK_Node"); //this.Request.Form["FK_Node"];
		if (DataType.IsNullOrEmpty(fk_nodeReq))
		{
			fk_nodeReq = this.GetRequestVal("NodeID"); // this.Request.Form["NodeID"];
		}

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

	private String _width = "";
	/** 
	 表单宽度
	*/
	public final String getWidth()
	{
		return _width;
	}
	public final void setWidth(String value)
	{
		_width = value;
	}
	private String _height = "";
	/** 
	 表单高度
	*/
	public final String getHeight()
	{
		return _height;
	}
	public final void setHeight(String value)
	{
		_height = value;
	}
	public String _btnWord = "";
	public final String getBtnWord()
	{
		return _btnWord;
	}
	public final void setBtnWord(String value)
	{
		_btnWord = value;
	}
	private GenerWorkFlow _HisGenerWorkFlow = null;
	public final GenerWorkFlow getHisGenerWorkFlow()
	{
		if (_HisGenerWorkFlow == null)
		{
			_HisGenerWorkFlow = new GenerWorkFlow(this.getWorkID());
		}
		return _HisGenerWorkFlow;
	}
	private Node _currNode = null;
	public final Node getcurrND()
	{
		if (_currNode == null)
		{
			_currNode = new Node(this.getFK_Node());
		}
		return _currNode;
	}
	private Flow _currFlow = null;
	public final Flow getcurrFlow()
	{
		if (_currFlow == null)
		{
			_currFlow = new Flow(this.getFK_Flow());
		}
		return _currFlow;
	}
	/** 
	 定义跟路径
	*/
	public String appPath = "/";


	//杨玉慧
	public final String getDoType1()
	{
		return HttpContextHelper.RequestParams("DoType1");
	}

		///#endregion

	public final String Focus()
	{
		BP.WF.Dev2Interface.Flow_Focus(this.getWorkID());
		return "设置成功.";
	}
	/** 
	 确认
	 
	 @return 
	*/
	public final String Confirm()
	{
		BP.WF.Dev2Interface.Flow_Confirm(this.getWorkID());
		return "设置成功.";
	}
	/** 
	 删除子流程
	 
	 @return 
	*/
	public final String DelSubFlow()
	{
		BP.WF.Dev2Interface.Flow_DeleteSubThread(this.getFK_Flow(), this.getWorkID(), "手工删除");
		return "删除成功.";
	}
	/** 
	 加载前置导航数据
	 
	 @return 
	*/
	public final String StartGuide_Init()
	{
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
			Object tempVar = fl.getStartGuidePara2().Clone();
			String sql = tempVar instanceof String ? (String)tempVar : null;
			//判断是否有查询条件
			if (!DataType.IsNullOrEmpty(skey))
			{
				Object tempVar2 = fl.getStartGuidePara1().Clone();
				sql = tempVar2 instanceof String ? (String)tempVar2 : null;
				sql = sql.replace("@Key", skey);
			}
			sql = sql.replace("~", "'");
			//替换约定参数
			sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
			sql = sql.replace("@WebUser.getName()", WebUser.getName());
			sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
			sql = sql.replace("@WebUser.getFK_DeptName", WebUser.getFK_DeptName);

			if (sql.contains("@") == true)
			{
				for (String key : HttpContextHelper.RequestParamKeys)
				{
					sql = sql.replace("@" + key, this.GetRequestVal(key));
				}

				for (String key : HttpContextHelper.RequestParamKeys)
				{
					sql = sql.replace("@" + key, this.GetRequestVal(key));
				}
			}

			//获取数据
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

			//判断前置导航的类型
			switch (fl.getStartGuideWay())
			{
				case BySQLOne:
				case BySystemUrlOneEntity:
					josnData = BP.Tools.Json.ToJson(dt);
					break;
				case BySQLMulti:
					josnData = BP.Tools.Json.ToJson(dt);
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
	 初始化(处理分发)
	 
	 @return 
	*/
	public final String MyFlow_Init()
	{
		String isCC = this.GetRequestVal("IsCC");
		//手动启动子流程的标志 0父子流程 1 同级子流程
		String isStartSameLevelFlow = this.GetRequestVal("IsStartSameLevelFlow");
		if (isCC != null && isCC.equals("1"))
		{
			return "url@WFRpt.htm?1=2" + this.getRequestParasOfAll();
		}

		GenerWorkFlow gwf = new GenerWorkFlow();
		boolean IsExistGWF = false;

		if (this.getWorkID() != 0)
		{
			//判断是否有执行该工作的权限.
			boolean isCanDo = Dev2Interface.Flow_IsCanDoCurrentWork(this.getWorkID(), WebUser.getNo());
			if (isCanDo == false)
			{
				GenerWorkFlow mygwf = new GenerWorkFlow(this.getWorkID());
				return "err@您[" + WebUser.getNo() + "," + WebUser.getName() + "]不能执行当前工作, 当前工作已经运转到[" + mygwf.getNodeName() + "],处理人[" + mygwf.getTodoEmps() + "]。";
			}

			gwf = new GenerWorkFlow();
			gwf.setWorkID(this.getWorkID());
			if (gwf.RetrieveFromDBSources() == 0)
			{
				return ("err@该流程ID{" + this.getWorkID() + "}不存在，或者已经被删除.");
			}
			IsExistGWF = true;
		}

		//判断当前节点是否是打开即阅读
		if (IsExistGWF == true)
		{
			//获取当前节点信息
			Node nd = new Node(gwf.getFK_Node());
			if (nd != null && nd.getIsOpenOver() == true)
			{
				//如果是结束节点执行流程结束功能
				if (nd.getIsStartNode() == false)
				{
					//如果启用审核组件
					if (nd.getFrmWorkCheckSta() == FrmWorkCheckSta.Enable)
					{
						//判断一下审核意见是否有默认值
						FrmWorkCheck workCheck = new FrmWorkCheck("ND" + nd.getNodeID());
						String msg = "同意";
						if (workCheck.getFWCIsFullInfo() == true)
						{
							msg = workCheck.getFWCDefInfo();
						}
						BP.WF.Dev2Interface.WriteTrackWorkCheck(gwf.getFK_Flow(), nd.getNodeID(), gwf.getWorkID(), gwf.getFID(), msg, workCheck.getFWCOpLabel());
					}

					BP.WF.Dev2Interface.Node_SendWork(gwf.getFK_Flow(), gwf.getWorkID());
					Node toNode = new Node(gwf.getFK_Node());
					if (nd.getHisFormType() != NodeFormType.SheetTree && nd.getHisFormType() != NodeFormType.SheetAutoTree)
					{
						//跳转到查看页面
						return "url@" + "./CCForm/Frm.htm?WorkID=" + gwf.getWorkID() + "&FK_Flow=" + gwf.getFK_Flow() + "&FK_Node=" + gwf.getFK_Node() + "&FK_MapData=" + toNode.getNodeFrmID() + "&IsReadonly=1";
					}
					else
					{
						//跳转到查看页面
						return "url@./MyFlowTreeReadonly.htm?WorkID=" + gwf.getWorkID() + "&FID=" + gwf.getFID() + "&OID=" + gwf.getWorkID() + "&FK_Flow=" + gwf.getFK_Flow() + "&FK_Node=" + nd.getNodeID() + "&PK=OID&PKVal=" + gwf.getWorkID() + "&IsEdit=0&IsLoadData=0&IsReadonly=1";
					}
				}
			}
		}

		//当前工作.
		Work currWK = this.getcurrND().getHisWork();


			///#region 判断前置导航.
		if (this.getcurrND().getIsStartNode() && this.getIsCC() == false && this.getWorkID() == 0)
		{
			try
			{
				if (BP.WF.Dev2Interface.Flow_IsCanStartThisFlow(this.getFK_Flow(), WebUser.getNo(), this.getPFlowNo(), this.getPNodeID(), this.getPWorkID()) == false)
				{
					/*是否可以发起流程？ @李国文. */
					return "err@您(" + WebUser.getNo() + ")没有发起或者处理该流程的权限.";
				}
			}
			catch (RuntimeException ex)
			{
				return "err@" + ex.getMessage();
			}
		}

		//第一次加载.
		if (this.getWorkID() == 0 && this.getcurrND().getIsStartNode() && this.GetRequestVal("IsCheckGuide") == null)
		{

			long workid = BP.WF.Dev2Interface.Node_CreateBlankWork(this.getFK_Flow(), null, null, WebUser.getNo(), null, this.getPWorkID(), this.getPFID(), this.getPFlowNo(), this.getPNodeID(), null, 0, null,null,isStartSameLevelFlow);

			String hostRun = this.getcurrFlow().GetValStrByKey(FlowAttr.HostRun);
			if (DataType.IsNullOrEmpty(hostRun) == false)
			{
				hostRun += "/WF/";
			}

			this.setWorkID(workid);

			switch (this.getcurrFlow().getStartGuideWay())
			{
				case None:
					break;
				case SubFlowGuide:
				case SubFlowGuideEntity:
					return "url@" + hostRun + "StartGuide.htm?FK_Flow=" + this.getcurrFlow().No + "&WorkID=" + workid;
				case ByHistoryUrl: // 历史数据.
					if (this.getcurrFlow().getIsLoadPriData() == true)
					{
						return "err@流程配置错误，您不能同时启用前置导航，自动装载上一笔数据两个功能。";
					}
					return "url@" + hostRun + "StartGuide.htm?FK_Flow=" + this.getcurrFlow().No + "&WorkID=" + workid;
				case BySystemUrlOneEntity:
					return "url@" + hostRun + "StartGuideEntities.htm?StartGuideWay=BySystemUrlOneEntity&WorkID=" + workid + this.getRequestParasOfAll();
				case BySQLOne:
					return "url@" + hostRun + "StartGuideEntities.htm?StartGuideWay=BySQLOne&WorkID=" + workid + this.getRequestParasOfAll();
				case BySQLMulti:
					return "url@" + hostRun + "StartGuideEntities.htm?StartGuideWay=BySQLMulti&WorkID=" + workid + this.getRequestParasOfAll();
				case BySelfUrl: //按照定义的url.
					return "url@" + this.getcurrFlow().getStartGuidePara1() + this.getRequestParasOfAll() + "&WorkID=" + workid;
				case ByFrms: //选择表单.
					return "url@" + hostRun + "./WorkOpt/StartGuideFrms.htm?FK_Flow=" + this.getcurrFlow().No + "&WorkID=" + workid;
				case ByParentFlowModel: //选择父流程 @yuanlina.
					return "url@" + hostRun + "./WorkOpt/StartGuideParentFlowModel.htm?FK_Flow=" + this.getcurrFlow().No + "&WorkID=" + workid;
				default:
					break;
			}
		}

			///#endregion 判断前置导航


			///#region 前置导航数据拷贝到第一节点
		if (this.getWorkID() != 0 && this.GetRequestVal("IsCheckGuide") != null)
		{
			String key = this.GetRequestVal("KeyNo");
			DataTable dt = BP.WF.Glo.StartGuidEnties(this.getWorkID(), this.getFK_Flow(), this.getFK_Node(), key);

			/*如果父流程编号，就要设置父子关系。*/
			if (dt != null && dt.Rows.size() > 0 && dt.Columns.Contains("PFlowNo") == true)
			{
				String pFlowNo = dt.Rows[0]["PFlowNo"].toString();
				int pNodeID = Integer.parseInt(dt.Rows[0]["PNodeID"].toString());
				long pWorkID = Long.parseLong(dt.Rows[0]["PWorkID"].toString());
				String pEmp = ""; // dt.Rows[0]["PEmp"].ToString();
				if (DataType.IsNullOrEmpty(pEmp))
				{
					pEmp = WebUser.getNo();
				}

				//设置父子关系.
				BP.WF.Dev2Interface.SetParentInfo(this.getFK_Flow(), this.getWorkID(), pWorkID);
			}
		}

			///#endregion


			///#region 启动同级子流程的信息存储
		if (isStartSameLevelFlow != null && isStartSameLevelFlow.equals("1") == true && this.getWorkID() != 0)
		{
			gwf.setWorkID(this.getWorkID());
			gwf.RetrieveFromDBSources();
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


			///#region 处理表单类型.
		if (this.getcurrND().getHisFormType() == NodeFormType.SheetTree || this.getcurrND().getHisFormType() == NodeFormType.SheetAutoTree)
		{

			if (this.getWorkID() == 0)
			{
				this.setWorkID(BP.WF.Dev2Interface.Node_CreateBlankWork(this.getFK_Flow(), null, null, WebUser.getNo(), null));
				currWK = getcurrND().getHisWork();
				currWK.setOID(this.getWorkID());
				currWK.Retrieve();
			}
			else
			{
				gwf.setWorkID(this.getWorkID());
				gwf.RetrieveFromDBSources();
			}

			if (gwf.getPWorkID() == 0 && this.getWorkID() != 0)
			{
				gwf.setWorkID(this.getWorkID());
				gwf.setPWorkID(this.getPWorkID());
				if (DataType.IsNullOrEmpty(gwf.getPFlowNo()) == true)
				{
					gwf.setPFlowNo(this.getPFlowNo());
				}
				gwf.Update();
			}

			if (this.getcurrND().getIsStartNode())
			{
				/*如果是开始节点, 先检查是否启用了流程限制。*/
				if (BP.WF.Glo.CheckIsCanStartFlow_InitStartFlow(this.getcurrFlow()) == false)
				{
					/* 如果启用了限制就把信息提示出来. */
					String msg = BP.WF.Glo.DealExp(this.getcurrFlow().getStartLimitAlert(), currWK, null);
					return "err@" + msg;
				}
			}




				///#region 开始组合url.
			String toUrl = "";

			if (this.getIsMobile() == true)
			{
				if (gwf.getParas_Frms().equals("") == false)
				{
					toUrl = "MyFlowGener.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo=" + WebUser.getNo() + "&FID=" + this.getFID() + "&SID=" + WebUser.SID + "&PFlowNo=" + gwf.getPFlowNo() + "&PNodeID=" + gwf.getPNodeID() + "&PWorkID=" + gwf.getPWorkID() + "&Frms=" + gwf.getParas_Frms();
				}
				else
				{
					toUrl = "MyFlowGener.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo=" + WebUser.getNo() + "&FID=" + this.getFID() + "&SID=" + WebUser.SID + "&PFlowNo=" + gwf.getPFlowNo() + "&PNodeID=" + gwf.getPNodeID() + "&PWorkID=" + gwf.getPWorkID();
				}
			}
			else
			{
				if (gwf.getParas_Frms().equals("") == false)
				{
					toUrl = "MyFlowTree.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo=" + WebUser.getNo() + "&FID=" + this.getFID() + "&SID=" + WebUser.SID + "&PFlowNo=" + gwf.getPFlowNo() + "&PNodeID=" + gwf.getPNodeID() + "&PWorkID=" + gwf.getPWorkID() + "&Frms=" + gwf.getParas_Frms();
				}
				else
				{
					toUrl = "MyFlowTree.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&UserNo=" + WebUser.getNo() + "&FID=" + this.getFID() + "&SID=" + WebUser.SID + "&PFlowNo=" + gwf.getPFlowNo() + "&PNodeID=" + gwf.getPNodeID() + "&PWorkID=" + gwf.getPWorkID();
				}
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
			for (String key : HttpContextHelper.RequestParamKeys)
			{
				if (toUrl.contains(key + "=") == true)
				{
					continue;
				}
				toUrl += "&" + key + "=" + HttpContextHelper.RequestParams(key);
			}


				///#endregion 开始组合url.

			if (gwf == null)
			{
				gwf = new GenerWorkFlow();
				gwf.setWorkID(this.getWorkID());
				gwf.RetrieveFromDBSources();
			}
			//设置url.
			if (gwf.getWFState() == WFState.Runing || gwf.getWFState() == WFState.Blank || gwf.getWFState() == WFState.Draft)
			{
				if (toUrl.contains("IsLoadData") == false)
				{
					toUrl += "&IsLoadData=1";
				}
				else
				{
					toUrl = toUrl.replace("&IsLoadData=0", "&IsLoadData=1");
				}
			}
			//SDK表单上服务器地址,应用到使用ccflow的时候使用的是sdk表单,该表单会存储在其他的服务器上,珠海高凌提出. 
			toUrl = toUrl.replace("@SDKFromServHost", SystemConfig.AppSettings["SDKFromServHost"]);

			//增加fk_node
			if (toUrl.contains("&FK_Node=") == false)
			{
				toUrl += "&FK_Node=" + this.getcurrND().getNodeID();
			}


			//如果是开始节点.
			if (getcurrND().getIsStartNode() == true)
			{
				if (toUrl.contains("PrjNo") == true && toUrl.contains("PrjName") == true)
				{
					String sql = "UPDATE " + currWK.EnMap.PhysicsTable + " SET PrjNo='" + this.GetRequestVal("PrjNo") + "', PrjName='" + this.GetRequestVal("PrjName") + "' WHERE OID=" + this.getWorkID();
					BP.DA.DBAccess.RunSQL(sql);
				}
			}
			return "url@" + toUrl;
		}

		if (this.getcurrND().getHisFormType() == NodeFormType.SDKForm)
		{
			if (this.getWorkID() == 0)
			{
				currWK = this.getcurrFlow().NewWork();
				this.setWorkID(currWK.getOID());
			}

			String url = getcurrND().getFormUrl();
			if (DataType.IsNullOrEmpty(url))
			{
				return "err@设置读取状流程设计错误态错误,没有设置表单url.";
			}

			//处理连接.
			url = this.MyFlow_Init_DealUrl(getcurrND(), currWK);

			//sdk表单就让其跳转.
			return "url@" + url;
		}

			///#endregion 处理表单类型.

		//求出当前节点frm的类型.
		NodeFormType frmtype = this.getcurrND().getHisFormType();
		if (frmtype != NodeFormType.RefOneFrmTree)
		{
			getcurrND().WorkID = this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.

			if (this.getcurrND().getNodeFrmID().contains(String.valueOf(this.getcurrND().getNodeID())) == false)
			{
				/*如果当前节点引用的其他节点的表单.*/
				String nodeFrmID = getcurrND().getNodeFrmID();
				String refNodeID = nodeFrmID.replace("ND", "");
				BP.WF.Node nd = new Node(Integer.parseInt(refNodeID));

				//表单类型.
				frmtype = nd.getHisFormType();
			}
		}


			///#region 内置表单类型的判断.
		/*如果是傻瓜表单，就转到傻瓜表单的解析执行器上，为软通动力改造。*/
		if (this.getWorkID() == 0)
		{
			currWK = this.getcurrFlow().NewWork();
			this.setWorkID(currWK.getOID());
		}

		if (frmtype == NodeFormType.FoolTruck)
		{
			/*如果是傻瓜表单，就转到傻瓜表单的解析执行器上，为软通动力改造。*/
			if (this.getWorkID() == 0)
			{
				currWK = this.getcurrFlow().NewWork();
				this.setWorkID(currWK.getOID());
			}

			//string url = "MyFlowFoolTruck.htm";
			String url = "MyFlowGener.htm";

			//处理连接.
			url = this.MyFlow_Init_DealUrl(getcurrND(), currWK, url);
			return "url@" + url;
		}

		if (frmtype == NodeFormType.WebOffice)
		{
			/*如果是公文表单，就转到公文表单的解析执行器上，为软通动力改造。*/
			if (this.getWorkID() == 0)
			{
				currWK = this.getcurrFlow().NewWork();
				this.setWorkID(currWK.getOID());
			}

			//string url = "MyFlowFoolTruck.htm";
			String url = "MyFlowWebOffice.htm";

			//处理连接.
			url = this.MyFlow_Init_DealUrl(getcurrND(), currWK, url);
			return "url@" + url;
		}

		if (frmtype == NodeFormType.FoolForm && this.getIsMobile() == false)
		{
			/*如果是傻瓜表单，就转到傻瓜表单的解析执行器上。*/
			if (this.getWorkID() == 0)
			{
				currWK = this.getcurrFlow().NewWork();
				this.setWorkID(currWK.getOID());
			}

			String url = "MyFlowGener.htm";
			if (this.getIsMobile())
			{
				url = "MyFlowGener.htm";
			}

			//处理连接.
			url = this.MyFlow_Init_DealUrl(getcurrND(), currWK, url);

			url = url.replace("DoType=MyFlow_Init&", "");
			url = url.replace("&DoWhat=StartClassic", "");
			return "url@" + url;
		}

		//自定义表单
		if (frmtype == NodeFormType.SelfForm && this.getIsMobile() == false)
		{
			if (this.getWorkID() == 0)
			{
				currWK = this.getcurrFlow().NewWork();
				this.setWorkID(currWK.getOID());
			}

			String url = "MyFlowSelfForm.htm";

			//处理连接.
			url = this.MyFlow_Init_DealUrl(getcurrND(), currWK, url);

			url = url.replace("DoType=MyFlow_Init&", "");
			url = url.replace("&DoWhat=StartClassic", "");
			return "url@" + url;
		}

			///#endregion 内置表单类型的判断.

		String myurl = "MyFlowGener.htm";

		//处理连接.
		myurl = this.MyFlow_Init_DealUrl(getcurrND(), currWK, myurl);
		myurl = myurl.replace("DoType=MyFlow_Init&", "");
		myurl = myurl.replace("&DoWhat=StartClassic", "");



		return "url@" + myurl;
	}

	private String MyFlow_Init_DealUrl(BP.WF.Node currND, Work currWK)
	{
		return MyFlow_Init_DealUrl(currND, currWK, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: private string MyFlow_Init_DealUrl(BP.WF.Node currND, Work currWK, string url = null)
	private String MyFlow_Init_DealUrl(BP.WF.Node currND, Work currWK, String url)
	{
		if (url == null)
		{
			url = currND.getFormUrl();
		}

		String urlExt = this.getRequestParas();
		//防止查询不到.
		urlExt = urlExt.replace("?WorkID=", "&WorkID=");
		if (urlExt.contains("&WorkID") == false)
		{
			urlExt += "&WorkID=" + this.getWorkID();
		}
		else
		{
			urlExt = urlExt.replace("&WorkID=0", "&WorkID=" + this.getWorkID());
			urlExt = urlExt.replace("&WorkID=&", "&WorkID=" + this.getWorkID() + "&");
		}

		//SDK表单上服务器地址,应用到使用ccflow的时候使用的是sdk表单,该表单会存储在其他的服务器上,珠海高凌提出. 
		url = url.replace("@SDKFromServHost", SystemConfig.AppSettings["SDKFromServHost"]);

		if (urlExt.contains("&NodeID") == false)
		{
			urlExt += "&NodeID=" + currND.getNodeID();
		}

		if (urlExt.contains("FK_Node") == false)
		{
			urlExt += "&FK_Node=" + currND.getNodeID();
		}

		if (urlExt.contains("&FID") == false && currWK != null)
		{
			//urlExt += "&FID=" + currWK.FID;
			urlExt += "&FID=" + this.getFID();
		}

		if (urlExt.contains("&UserNo") == false)
		{
			urlExt += "&UserNo=" + HttpUtility.UrlEncode(WebUser.getNo());
		}

		if (urlExt.contains("&SID") == false)
		{
			urlExt += "&SID=" + WebUser.SID;
		}

		if (url.contains("?") == true)
		{
			url += "&" + urlExt;
		}
		else
		{
			url += "?" + urlExt;
		}

		for (String str : HttpContextHelper.RequestParamKeys)
		{
			if (DataType.IsNullOrEmpty(str) == true)
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
	public WF_MyFlow()
	{

	}
	/** 
	 结束流程.
	 
	 @return 
	*/
	public final String MyFlow_StopFlow()
	{
		try
		{
			String str = BP.WF.Dev2Interface.Flow_DoFlowOver(this.getFK_Flow(), this.getWorkID(), "流程成功结束");
			if (str.equals("") || str == null)
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
	public final String MyFlow_DeleteFlowByReal()
	{
		try
		{
			String str = BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(this.getFK_Flow(), this.getWorkID());
			if (str.equals("") || str == null)
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
	 保存发送参数.
	 
	 @return 
	*/
	public final String SaveParas()
	{
		BP.WF.Dev2Interface.Flow_SaveParas(this.getWorkID(), this.GetRequestVal("Paras"));
		return "保存成功";
	}
	/** 
	 工具栏
	 
	 @return 
	*/
	public final String InitToolBar()
	{

			///#region 处理是否是加签，或者是否是会签模式.
		boolean isAskForOrHuiQian = false;
		BtnLab btnLab = new BtnLab(this.getFK_Node());
		if (String.valueOf(this.getFK_Node()).endsWith("01") == false)
		{
			GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
			if (gwf.getWFState() == WFState.Askfor)
			{
				isAskForOrHuiQian = true;
			}

			/*判断是否是加签状态，如果是，就判断是否是主持人，如果不是主持人，就让其 isAskFor=true ,屏蔽退回等按钮.*/
			/**说明：针对于组长模式的会签，协作模式的会签加签人仍可以加签*/
			if (gwf.getHuiQianTaskSta() == HuiQianTaskSta.HuiQianing)
			{
				if (btnLab.getHuiQianRole() == HuiQianRole.TeamupGroupLeader)
				{
					if (btnLab.getHuiQianLeaderRole() == 0)
					{
						if (!gwf.getHuiQianZhuChiRen().equals(WebUser.getNo()))
						{
							isAskForOrHuiQian = true;
						}
					}
					else
					{
						//不是主持人
						if (gwf.getHuiQianZhuChiRen().contains(WebUser.getNo() + ",") == false)
						{
							isAskForOrHuiQian = true;
						}
					}
				}
			}
		}

			///#endregion 处理是否是加签，或者是否是会签模式，.

		String tKey = LocalDateTime.now().toString("MM-dd-hh:mm:ss");
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
				toolbar += "<input name='Send' type=button value='确定/完成' data-type='isAskFor' enable=true onclick=\" " + btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;SaveDtlAll();Send(); \" />";
				// toolbar += "<input name='Send' type=button  value='" + btnLab.SendLab + "' enable=true onclick=\"" + btnLab.SendJS + " if ( SendSelfFrom()==false) return false; Send(); this.disabled=true;\" />";
				if (btnLab.getPrintZipEnable() == true)
				{
					String packUrl = "./WorkOpt/Packup.htm?FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
					toolbar += "<input type=button name='PackUp'  value='" + btnLab.getPrintZipLab() + "' enable=true/>";
				}

				if (btnLab.getTrackEnable())
				{
					toolbar += "<input type=button name='Track'  value='" + btnLab.getTrackLab() + "' enable=true onclick=\"WinOpen('" + appPath + "WF/WorkOpt/OneWork/OneWork.htm?CurrTab=Truck&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&FK_Node=" + this.getFK_Node() + "&s=" + tKey + "','ds'); \" />";
				}

				return toolbar;
			}

				///#endregion 是否是会签.


				///#region 是否是抄送.
			if (this.getIsCC())
			{
				toolbar += "<input type=button  value='流程运行轨迹' enable=true onclick=\"WinOpen('" + appPath + "WF/WorkOpt/OneWork/OneWork.htm?CurrTab=Truck&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&FK_Node=" + this.getFK_Node() + "&s=" + tKey + "','ds'); \" />";
				// 判断审核组件在当前的表单中是否启用，如果启用了.
				FrmWorkCheck fwc = new FrmWorkCheck(this.getFK_Node());
				if (fwc.getHisFrmWorkCheckSta() != FrmWorkCheckSta.Enable)
				{
					/*如果不等于启用, */
					toolbar += "<input type=button  value='填写审核意见' enable=true onclick=\"WinOpen('" + appPath + "WF/WorkOpt/CCCheckNote.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&FK_Node=" + this.getFK_Node() + "&s=" + tKey + "','ds'); \" />";
				}
				return toolbar;
			}

				///#endregion 是否是抄送.


				///#region 如果当前节点启用了协作会签.
			if (btnLab.getHuiQianRole() == HuiQianRole.Teamup)
			{
				if (this.getIsMobile() == true)
				{
					toolbar += "<input name='SendHuiQian' type=button value='会签发送' enable=true onclick=\" " + btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;SaveDtlAll();SendIt(true); \" />";
				}
				else
				{
					toolbar += "<input name='SendHuiQian' type=button value='会签发送' enable=true onclick=\" " + btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;SaveDtlAll();Send(true); \" />";
				}
			}

				///#endregion 如果当前节点启用了协作会签


				///#region 加载流程控制器 - 按钮
			if (this.getcurrND().getHisFormType() == NodeFormType.SelfForm)
			{
				/*如果是嵌入式表单.*/
				if (getcurrND().getIsEndNode())
				{
					/*如果当前节点是结束节点.*/
					if (btnLab.getSendEnable() && getcurrND().getHisBatchRole() != BatchRole.Group)
					{
						/*如果启用了发送按钮.*/
						toolbar += "<input name='Send' type=button value='" + btnLab.getSendLab() + "' enable=true onclick=\"" + btnLab.getSendJS() + " if (SendSelfFrom()==false) return false; this.disabled=true;\" />";
					}
				}
				else
				{
					if (btnLab.getSendEnable() && getcurrND().getHisBatchRole() != BatchRole.Group)
					{
						toolbar += "<input name='Send' type=button  value='" + btnLab.getSendLab() + "' enable=true onclick=\"" + btnLab.getSendJS() + " if ( SendSelfFrom()==false) return false; this.disabled=true;\" />";
					}
				}

				/*处理保存按钮.*/
				if (btnLab.getSaveEnable())
				{
					toolbar += "<input name='Save' type=button value='" + btnLab.getSaveLab() + "' enable=true onclick=\"SaveSelfFrom();\" />";
				}
			}

			if (this.getcurrND().getHisFormType() != NodeFormType.SelfForm)
			{
				/*启用了其他的表单.*/
				if (getcurrND().getIsEndNode())
				{
					/*如果当前节点是结束节点.*/
					if (btnLab.getSendEnable() && getcurrND().getHisBatchRole() != BatchRole.Group)
					{
						/*如果启用了选择人窗口的模式是【选择既发送】.*/
						if (this.getIsMobile())
						{
							toolbar += "<input name='Send' type=button value='" + btnLab.getSendLab() + "' enable=true onclick=\" " + btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;SaveDtlAll();SendIt(); \" />";
						}
						else
						{
							toolbar += "<input name='Send' type=button value='" + btnLab.getSendLab() + "' enable=true onclick=\" " + btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;SaveDtlAll();Send(); \" />";
						}

					}
				}
				else
				{
					if (btnLab.getSendEnable() && getcurrND().getHisBatchRole() != BatchRole.Group)
					{
						/*如果启用了发送按钮.
						 * 1. 如果是加签的状态，就不让其显示发送按钮，因为在加签的提示。
						 */
						if (this.getIsMobile())
						{
							toolbar += "<input name='Send' type=button  value='" + btnLab.getSendLab() + "' enable=true onclick=\" " + btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;SendIt();\" />";
						}
						else
						{
							toolbar += "<input name='Send' type=button  value='" + btnLab.getSendLab() + "' enable=true onclick=\" " + btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;Send();\" />";
						}
					}
				}

				/* 处理保存按钮.*/
				if (btnLab.getSaveEnable())
				{
					if (this.getIsMobile())
					{
						toolbar += "<input name='Save' type=button  value='" + btnLab.getSaveLab() + "' enable=true onclick=\"   if(SysCheckFrm()==false) return false; SaveIt();\" />";
					}
					else
					{
						toolbar += "<input name='Save' type=button  value='" + btnLab.getSaveLab() + "' enable=true onclick=\"   if(SysCheckFrm()==false) return false;Save();\" />";
					}
				}
			}

			if (btnLab.getWorkCheckEnable())
			{
				/*审核*/
				//     string urlr1 = "./WorkOpt/WorkCheck.htm?FK_Node=" + this.FK_Node + "&FID=" + this.FID + "&WorkID=" + this.WorkID + "&FK_Flow=" + this.FK_Flow + "&s=" + tKey;
				//   toolbar += "<input  name='Btn_WorkCheck' type=button  value='" + btnLab.WorkCheckLab + "' enable=true onclick=\"WinOpen('" + urlr1 + "','dsdd'); \" />";
				toolbar += "<input  name='workcheckBtn' type=button  value='" + btnLab.getWorkCheckLab() + "' enable=true />";
			}

			if (btnLab.getThreadEnable())
			{
				/*如果要查看子线程.*/
				String ur2 = "./WorkOpt/ThreadDtl.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button  value='" + btnLab.getThreadLab() + "' enable=true onclick=\"WinOpen('" + ur2 + "'); \" />";
			}

			if (btnLab.getShowParentFormEnable() && this.getPWorkID() != 0)
			{
				/*如果要查看父流程.*/
				GenerWorkFlow gwf = new GenerWorkFlow(this.getPWorkID());
				String ur2 = "./WorkOpt/OneWork/FrmGuide.htm?FK_Node=" + gwf.getFK_Node() + "&FID=" + gwf.getFID() + "&WorkID=" + gwf.getWorkID() + "&FK_Flow=" + gwf.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button  value='" + btnLab.getShowParentFormLab() + "' enable=true onclick=\"WinOpen('" + ur2 + "'); \" />";
			}

			if (btnLab.getTCEnable() == true)
			{
				/*流转自定义..*/
				String ur3 = "./WorkOpt/TransferCustom.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button name='TransferCustom'  value='" + btnLab.getTCLab() + "' enable=true onclick=\"TransferCustom('" + ur3 + "'); \" />";
			}

			if (btnLab.getHelpRole() != 0)
			{
				toolbar += "<input type=button  value='" + btnLab.getHelpLab() + "' enable=true onclick=\"HelpAlter(); \" />";
			}

			if (btnLab.getJumpWayEnable() && 1 == 2)
			{
				/*跳转*/
				String urlr = "./WorkOpt/JumpWay.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button  value='" + btnLab.getJumpWayLab() + "' enable=true onclick=\"To('" + urlr + "'); \" />";
			}

			if (btnLab.getReturnEnable())
			{
				/*退回*/
				String urlr = "./WorkOpt/ReturnWork.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input name='Return' type=button  value='" + btnLab.getReturnLab() + "' enable=true onclick=\"ReturnWork('" + urlr + "','" + btnLab.getReturnField() + "'); \" />";
			}

			//  if (btnLab.HungEnable && this.currND.IsStartNode == false)
			if (btnLab.getHungEnable())
			{
				/*挂起*/
				String urlr = "./WorkOpt/HungUp.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button  value='" + btnLab.getHungLab() + "' enable=true onclick=\"WinOpen('" + urlr + "'); \" />";
			}

			if (btnLab.getShiftEnable())
			{
				/*移交*/
				String url12 = "./WorkOpt/Forward.htm?FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&Info=" + "移交原因.";
				toolbar += "<input name='Shift' type=button  value='" + btnLab.getShiftLab() + "' enable=true onclick=\"To('" + url12 + "'); \" />";
			}

			if ((btnLab.getCCRole() == CCRole.HandCC || btnLab.getCCRole() == CCRole.HandAndAuto))
			{
				if (this.getIsMobile())
				{
					String urlrDel = "./WorkOpt/CC.htm?WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&s=" + tKey;
					toolbar += "<input name='CC' type=button  value='" + btnLab.getCCLab() + "' enable=true onclick=\"To('" + urlrDel + "'); \" />";
				}
				else
				{
					// 抄送 
					toolbar += "<input name='CC' type=button  value='" + btnLab.getCCLab() + "' enable=true onclick=\"WinOpen('" + "./WorkOpt/CC.htm?WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&s=" + tKey + "','ds'); \" />";
				}
			}

			if (btnLab.getDeleteEnable() != 0)
			{
				String urlrDel = appPath + "WF/MyFlowInfo.htm?DoType=DeleteFlow&FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input name='Delete' type=button  value='" + btnLab.getDeleteLab() + "' enable=true onclick=\"To('" + urlrDel + "'); \" />";
			}

			if (btnLab.getEndFlowEnable() && this.getcurrND().getIsStartNode() == false)
			{
				toolbar += "<input type=button name='EndFlow'  value='" + btnLab.getEndFlowLab() + "' enable=true onclick=\"javascript:DoStop('" + btnLab.getEndFlowLab() + "','" + this.getFK_Flow() + "','" + this.getWorkID() + "');\" />";
			}

			// @李国文.
			if (btnLab.getPrintDocEnable() == true)
			{
				String urlr = appPath + "WF/WorkOpt/PrintDoc.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button name='PrintDoc' value='" + btnLab.getPrintDocLab() + "' enable=true onclick=\"WinOpen('" + urlr + "','dsdd'); \" />";
			}

			if (btnLab.getTrackEnable())
			{
				toolbar += "<input type=button name='Track'  value='" + btnLab.getTrackLab() + "' enable=true onclick=\"WinOpen('" + appPath + "WF/WorkOpt/OneWork/OneWork.htm?CurrTab=Truck&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&FK_Node=" + this.getFK_Node() + "&s=" + tKey + "','ds'); \" />";
			}


			if (btnLab.getSearchEnable())
			{
				toolbar += "<input type=button name='Search'  value='" + btnLab.getSearchLab() + "' enable=true onclick=\"WinOpen('./RptDfine/Default.htm?RptNo=ND" + Integer.parseInt(this.getFK_Flow()) + "MyRpt&FK_Flow=" + this.getFK_Flow() + "&SearchType=My&s=" + tKey + "','dsd0'); \" />";
			}

			if (btnLab.getBatchEnable())
			{
				/*批量处理*/
				String urlr = appPath + "WF/Batch.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button name='Batch' value='" + btnLab.getBatchLab() + "' enable=true onclick=\"To('" + urlr + "'); \" />";
			}

			if (btnLab.getAskforEnable())
			{
				/*加签 */
				String urlr3 = appPath + "WF/WorkOpt/Askfor.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button name='Askfor'  value='" + btnLab.getAskforLab() + "' enable=true onclick=\"To('" + urlr3 + "'); \" />";
			}

			if (btnLab.getHuiQianRole() == HuiQianRole.TeamupGroupLeader)
			{
				/*会签 */
				String urlr3 = appPath + "WF/WorkOpt/HuiQian.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button name='HuiQian'  value='" + btnLab.getHuiQianLab() + "' enable=true onclick=\"To('" + urlr3 + "'); \" />";
			}


			if (btnLab.getWebOfficeWorkModel() == WebOfficeWorkModel.Button)
			{
				/*公文正文 */
				String urlr = appPath + "WF/WorkOpt/WebOffice.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button name='WebOffice'  value='" + btnLab.getWebOfficeLab() + "' enable=true onclick=\"WinOpen('" + urlr + "','公文正文'); \" />";
			}

			// 需要翻译.
			if (this.getcurrFlow().getIsResetData() == true && this.getcurrND().getIsStartNode())
			{
				/* 启用了数据重置功能 */
				String urlr3 = appPath + "WF/MyFlow.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&IsDeleteDraft=1&s=" + tKey;
				toolbar += "<input type=button  value='数据重置' enable=true onclick=\"To('" + urlr3 + "','ds'); \" />";
			}

			//if (btnLab.SubFlowEnable == true )
			//{
			//    /* 子流程 */
			//    string urlr3 = appPath + "WF/WorkOpt/SubFlow.htm?FK_Node=" + this.FK_Node + "&FID=" + this.FID + "&WorkID=" + this.WorkID + "&FK_Flow=" + this.FK_Flow + "&s=" + tKey;
			//    toolbar += "<input type=button name='SubFlow'  value='" + btnLab.SubFlowLab + "' enable=true onclick=\"WinOpen('" + urlr3 + "'); \" />";
			//}

			if (btnLab.getCHRole() != 0)
			{
				/* 节点时限设置 */
				String urlr3 = appPath + "WF/WorkOpt/CH.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button name='CH'  value='" + btnLab.getCHLab() + "' enable=true onclick=\"WinOpen('" + urlr3 + "'); \" />";
			}

			if (btnLab.getNoteEnable() != 0)
			{
				/* 备注设置 */
				toolbar += "<input type=button name='Note'  value='" + btnLab.getNoteLab() + "' enable=true  />";
			}


			if (btnLab.getPRIEnable() == true)
			{
				/* 优先级设置 */
				String urlr3 = appPath + "WF/WorkOpt/PRI.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<input type=button name='PR'  value='" + btnLab.getPRILab() + "' enable=true onclick=\"WinOpen('" + urlr3 + "'); \" />";
			}

			/* 关注 */
			if (btnLab.getFocusEnable() == true)
			{
				if (getHisGenerWorkFlow().getParas_Focus() == true)
				{
					toolbar += "<input type=button  value='取消关注' enable=true onclick=\"FocusBtn(this,'" + this.getWorkID() + "'); \" />";
				}
				else
				{
					toolbar += "<input type=button name='Focus' value='" + btnLab.getFocusLab() + "' enable=true onclick=\"FocusBtn(this,'" + this.getWorkID() + "'); \" />";
				}
			}

			/* 分配工作 */
			if (btnLab.getAllotEnable() == true)
			{
				/*分配工作*/
				String urlAllot = "./WorkOpt/AllotTask.htm?FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&Info=" + "移交原因.";
				toolbar += "<input name='Allot' type=button  value='" + btnLab.getAllotLab() + "' enable=true onclick=\"To('" + urlAllot + "'); \" />";
			}

			/* 确认 */
			if (btnLab.getConfirmEnable() == true)
			{
				if (getHisGenerWorkFlow().getParas_Confirm() == true)
				{
					toolbar += "<input type=button  value='取消确认' enable=true onclick=\"ConfirmBtn(this,'" + this.getWorkID() + "'); \" />";
				}
				else
				{
					toolbar += "<input type=button name='Confirm' value='" + btnLab.getConfirmLab() + "' enable=true onclick=\"ConfirmBtn(this,'" + this.getWorkID() + "'); \" />";
				}
			}

			// 需要翻译.

			/* 打包下载zip */
			if (btnLab.getPrintZipEnable() == true)
			{
				String packUrl = "./WorkOpt/Packup.htm?FileType=zip&FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
				toolbar += "<input type=button name='PackUp_zip'  value='" + btnLab.getPrintZipLab() + "' enable=true/>";
			}

			/* 打包下载html */
			if (btnLab.getPrintHtmlEnable() == true)
			{
				String packUrl = "./WorkOpt/Packup.htm?FileType=html&FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
				toolbar += "<input type=button name='PackUp_html'  value='" + btnLab.getPrintHtmlLab() + "' enable=true/>";
			}

			/* 打包下载pdf */
			if (btnLab.getPrintPDFEnable() == true)
			{
				String packUrl = "./WorkOpt/Packup.htm?FileType=pdf&FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
				toolbar += "<input type=button name='PackUp_pdf'  value='" + btnLab.getPrintPDFLab() + "' enable=true/>";
			}

			if (this.getcurrND().getIsStartNode() == true)
			{
				if (this.getcurrFlow().getIsDBTemplate() == true)
				{
					String packUrl = "./WorkOpt/DBTemplate.htm?FileType=pdf&FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
					toolbar += "<input type=button name='DBTemplate'  value='模版' enable=true/>";
				}
			}

			/* 公文标签 */
			if (btnLab.getOfficeBtnEnable() == true)
			{
				toolbar += "<input type=button name='Btn_Office'  onclick='OpenOffice();'  value='" + btnLab.getOfficeBtnLab() + "' enable=true/>";
			}

				///#endregion



				///#region  加载自定义的button.
			BP.WF.Template.NodeToolbars bars = new NodeToolbars();
			bars.Retrieve(NodeToolbarAttr.FK_Node, this.getFK_Node());
			for (NodeToolbar bar : bars)
			{
				if (bar.getShowWhere() != ShowWhere.Toolbar)
				{
					continue;
				}

				if (bar.getExcType() == 1 || (!DataType.IsNullOrEmpty(bar.getTarget()) == false && bar.getTarget().toLowerCase().equals("javascript")))
				{
					toolbar += "<input type=button  value='" + bar.getTitle() + "' enable=true onclick='" + bar.getUrl() + "' />";
				}
				else
				{
					String urlr3 = bar.getUrl() + "&FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
					toolbar += "<input type=button  value='" + bar.getTitle() + "' enable=true onclick=\"WinOpen('" + urlr3 + "'); \" />";
				}
			}

				///#endregion //加载自定义的button.

		}
		catch (RuntimeException ex)
		{
			BP.DA.Log.DefaultLogWriteLineError(ex);
			toolbar = "err@" + ex.getMessage();
		}
		return toolbar;
	}

	/** 
	 工具栏
	 
	 @return 
	*/
	public final String InitToolBarForMobile()
	{
		String str = InitToolBar();
		str = str.replace("Send()", "SendIt()");
		return str;


			///#region 处理是否是加签，或者是否是会签模式，.
		boolean isAskForOrHuiQian = false;
		if (String.valueOf(this.getFK_Node()).endsWith("01") == false)
		{
			GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
			if (gwf.getWFState() == WFState.Askfor)
			{
				isAskForOrHuiQian = true;
			}
			else
			{
				/*判断是否是加签状态，如果是，就判断是否是主持人，如果不是主持人，就让其 isAskFor=true ,屏蔽退回等按钮. */
				if (gwf.getTodoEmps().contains(WebUser.getNo() + ",") == false)
				{
					isAskForOrHuiQian = true;
				}
			}
		}

			///#endregion 处理是否是加签，或者是否是会签模式，.

		String tKey = LocalDateTime.now().toString("yyyy-MM-dd - hh:mm:ss");
		BtnLab btnLab = new BtnLab(this.getFK_Node());
		String toolbar = "";
		try
		{

				///#region 是否是会签？.
			if (isAskForOrHuiQian == true)
			{
				toolbar += "<a data-role='button' name='Send'  value='" + btnLab.getSendLab() + "' enable=true onclick=\" " + btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;SaveDtlAll();SendIt(); \" ></a>";
				if (btnLab.getPrintZipEnable() == true)
				{
					String packUrl = "./WorkOpt/Packup.htm?FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
					toolbar += "<a data-role='button' type=button name='PackUp'  value='" + btnLab.getPrintZipLab() + "' enable=true></a>";
				}
				return toolbar;
			}

				///#endregion 是否是抄送.


				///#region 是否是抄送.
			if (this.getIsCC())
			{
				toolbar += "<a data-role='button'    value='流程运行轨迹' enable=true onclick=\"WinOpen('" + appPath + "WF/WorkOpt/OneWork/OneWork.htm?CurrTab=Truck&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&FK_Node=" + this.getFK_Node() + "&s=" + tKey + "','ds'); \" ></a>";
				// 判断审核组件在当前的表单中是否启用，如果启用了.
				FrmWorkCheck fwc = new FrmWorkCheck(this.getFK_Node());
				if (fwc.getHisFrmWorkCheckSta() != FrmWorkCheckSta.Enable)
				{
					/*如果不等于启用, */
					toolbar += "<a data-role='button' type=button  value='填写审核意见' enable=true onclick=\"WinOpen('" + appPath + "WF/WorkOpt/CCCheckNote.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&FK_Node=" + this.getFK_Node() + "&s=" + tKey + "','ds'); \" ></a>";
				}
				return toolbar;
			}

				///#endregion 是否是抄送.


				///#region 加载流程控制器 - 按钮
			if (this.getcurrND().getHisFormType() == NodeFormType.SelfForm)
			{
				/*如果是嵌入式表单.*/
				if (getcurrND().getIsEndNode())
				{
					/*如果当前节点是结束节点.*/
					if (btnLab.getSendEnable() && getcurrND().getHisBatchRole() != BatchRole.Group)
					{
						/*如果启用了发送按钮.*/
						toolbar += "<a data-role='button' name='Send'   value='" + btnLab.getSendLab() + "' enable=true onclick=\"" + btnLab.getSendJS() + " if (SendSelfFrom()==false) return false; SendIt(); this.disabled=true;\" ></a>";
					}
				}
				else
				{
					if (btnLab.getSendEnable() && getcurrND().getHisBatchRole() != BatchRole.Group)
					{
						toolbar += "<a data-role='button' name='Send'  value='" + btnLab.getSendLab() + "' enable=true onclick=\"" + btnLab.getSendJS() + " if ( SendSelfFrom()==false) return false; SendIt(); this.disabled=true;\" ></a>";
					}
				}

				/*处理保存按钮.*/
				if (btnLab.getSaveEnable())
				{
					toolbar += "<a data-role='button' name='Save'   value='" + btnLab.getSaveLab() + "' enable=true onclick=\"SaveSelfFrom();\" />";
				}
			}

			if (this.getcurrND().getHisFormType() == NodeFormType.FoolForm || this.getcurrND().getHisFormType() == NodeFormType.FreeForm)
			{
				/*启用了其他的表单.*/
				if (getcurrND().getIsEndNode())
				{
					/*如果当前节点是结束节点.*/
					if (btnLab.getSendEnable() && getcurrND().getHisBatchRole() != BatchRole.Group)
					{
						/*如果启用了选择人窗口的模式是【选择既发送】.*/
						toolbar += "<a data-role='button' name='Send' value='" + btnLab.getSendLab() + "' enable=true onclick=\" " + btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;SaveDtlAll();SendIt(); \" ></a>";
					}
				}
				else
				{
					if (btnLab.getSendEnable() && getcurrND().getHisBatchRole() != BatchRole.Group)
					{
						/*如果启用了发送按钮.
						 * 1. 如果是加签的状态，就不让其显示发送按钮，因为在加签的提示。
						 */
						toolbar += "<a data-role='button' name='Send'   value='" + btnLab.getSendLab() + "' enable=true onclick=\" " + btnLab.getSendJS() + " if(SysCheckFrm()==false) return false;SendIt();\" ></a>";
					}
				}

				/* 处理保存按钮.*/
				if (btnLab.getSaveEnable())
				{
					toolbar += "<a data-role='button' name='Save'    value='" + btnLab.getSaveLab() + "' enable=true onclick=\"   if(SysCheckFrm()==false) return false; SaveIt();\" ></a>";
				}
			}

			if (btnLab.getWorkCheckEnable())
			{
				/*审核*/
				String urlr1 = "./WorkOpt/WorkCheck.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<a data-role='button' id='Btn_WorkCheck'   value='" + btnLab.getWorkCheckLab() + "' enable=true onclick=\"WinOpen('" + urlr1 + "','dsdd'); \" ></a>";
			}

			if (btnLab.getThreadEnable())
			{
				/*如果要查看子线程.*/
				String ur2 = "./WorkOpt/ThreadDtl.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<a data-role='button' value='" + btnLab.getThreadLab() + "' enable=true onclick=\"WinOpen('" + ur2 + "'); \" ></a>";
			}

			if (btnLab.getTCEnable() == true)
			{
				/*流转自定义..*/
				String ur3 = "./WorkOpt/TransferCustom.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<a data-role='button' type=button  value='" + btnLab.getTCLab() + "' enable=true onclick=\"To('" + ur3 + "'); \" ></a>";
			}



			if (btnLab.getJumpWayEnable())
			{
				/*如果没有焦点字段*/
				String urlr = "./WorkOpt/JumpWay.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<a data-role='button' type=button  value='" + btnLab.getJumpWayLab() + "' enable=true onclick=\"To('" + urlr + "'); \" ></a>";
			}

			if (btnLab.getReturnEnable())
			{
				/*如果没有焦点字段*/
				String urlr = "./WorkOpt/ReturnWork.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<a data-role='button' name='Return' type=button  value='" + btnLab.getReturnLab() + "' enable=true onclick=\"ReturnWork('" + urlr + "','" + btnLab.getReturnField() + "'); \" ></a>";
			}

			//  if (btnLab.HungEnable && this.currND.IsStartNode == false)
			if (btnLab.getHungEnable())
			{
				/*挂起*/
				String urlr = "./WorkOpt/HungUp.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<a data-role='button' type=button  value='" + btnLab.getHungLab() + "' enable=true onclick=\"WinOpen('" + urlr + "'); \" ></a>";
			}

			if (btnLab.getShiftEnable())
			{
				/*移交*/
				String url12 = "./WorkOpt/Forward.htm?FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&Info=" + "移交原因.";
				toolbar += "<a data-role='button' name='Shift' type=button  value='" + btnLab.getShiftLab() + "' enable=true onclick=\"To('" + url12 + "'); \" ></a>";
			}

			if ((btnLab.getCCRole() == CCRole.HandCC || btnLab.getCCRole() == CCRole.HandAndAuto))
			{
				/* 抄送 */
				toolbar += "<a data-role='button' name='CC' type=button  value='" + btnLab.getCCLab() + "' enable=true onclick=\"WinOpen('" + appPath + "WF/WorkOpt/CC.htm?WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&s=" + tKey + "','ds'); \" ></a>";
			}

			if (btnLab.getDeleteEnable() != 0)
			{
				String urlrDel = appPath + "WF/MyFlowInfo.htm?DoType=DeleteFlow&FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<a data-role='button' name='Delete' type=button  value='" + btnLab.getDeleteLab() + "' enable=true onclick=\"To('" + urlrDel + "'); \" ></a>";
			}

			if (btnLab.getEndFlowEnable() && this.getcurrND().getIsStartNode() == false)
			{
				toolbar += "<a data-role='button' type=button name='EndFlow'  value='" + btnLab.getEndFlowLab() + "' enable=true onclick=\"DoStop('" + btnLab.getEndFlowLab() + "','" + this.getFK_Flow() + "','" + this.getWorkID() + "');\" ></a>";
			}

			if (btnLab.getPrintDocEnable())
			{
				String urlr = appPath + "WF/WorkOpt/PrintDoc.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<a data-role='button' type=button name='PrintDoc' value='" + btnLab.getPrintDocLab() + "' enable=true onclick=\"WinOpen('" + urlr + "','dsdd'); \" ></a>";

			}

			if (btnLab.getTrackEnable())
			{
				toolbar += "<a data-role='button' type=button name='Track'  value='" + btnLab.getTrackLab() + "' enable=true onclick=\"WinOpen('" + appPath + "WF/WorkOpt/OneWork/OneWork.htm?CurrTab=Truck&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FID=" + this.getFID() + "&FK_Node=" + this.getFK_Node() + "&s=" + tKey + "','ds'); \" ></a>";
			}


			if (btnLab.getSearchEnable())
			{
				toolbar += "<a data-role='button' type=button name='Search'  value='" + btnLab.getSearchLab() + "' enable=true onclick=\"WinOpen('" + appPath + "WF/Rpt/Search.htm?EnsName=ND" + Integer.parseInt(this.getFK_Flow()) + "MyRpt&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey + "','dsd0'); \" ></a>";
			}

			if (btnLab.getBatchEnable())
			{
				/*批量处理*/
				String urlr = appPath + "WF/Batch.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<a data-role='button' type=button name='Batch' value='" + btnLab.getBatchLab() + "' enable=true onclick=\"To('" + urlr + "'); \" ></a>";
			}

			if (btnLab.getAskforEnable())
			{
				/*加签 */
				String urlr3 = appPath + "WF/WorkOpt/Askfor.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<a data-role='button' type=button name='Askfor'  value='" + btnLab.getAskforLab() + "' enable=true onclick=\"To('" + urlr3 + "'); \" ></a>";
			}

			if (btnLab.getHuiQianRole() != HuiQianRole.None)
			{
				/*会签 */
				String urlr3 = appPath + "WF/WorkOpt/HuiQian.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<a data-role='button' type=button name='HuiQian'  value='" + btnLab.getHuiQianLab() + "' enable=true onclick=\"To('" + urlr3 + "'); \" ></a>";
			}


			if (btnLab.getWebOfficeWorkModel() == WebOfficeWorkModel.Button)
			{
				/*公文正文 */
				String urlr = appPath + "WF/WorkOpt/WebOffice.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<a data-role='button' type=button name='WebOffice'  value='" + btnLab.getWebOfficeLab() + "' enable=true onclick=\"WinOpen('" + urlr + "','公文正文'); \" ></a>";
			}

			if (this.getcurrFlow().getIsResetData() == true && this.getcurrND().getIsStartNode())
			{
				/* 启用了数据重置功能 */
				String urlr3 = appPath + "WF/MyFlow.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&IsDeleteDraft=1&s=" + tKey;
				toolbar += "<a data-role='button' type=button  value='数据重置' enable=true onclick=\"To('" + urlr3 + "','ds'); \" ></a>";
			}

			//if (1==2 && btnLab.SubFlowEnable == true)
			//{
			//    /* 子流程 */
			//    string urlr3 = appPath + "WF/WorkOpt/SubFlow.htm?FK_Node=" + this.FK_Node + "&FID=" + this.FID + "&WorkID=" + this.WorkID + "&FK_Flow=" + this.FK_Flow + "&s=" + tKey;
			//    toolbar += "<a data-role='button' type=button name='SubFlow'  value='" + btnLab.SubFlowLab + "' enable=true onclick=\"WinOpen('" + urlr3 + "'); \" ></a>";
			//}

			if (btnLab.getCHRole() != 0)
			{
				/* 节点时限设置 */
				String urlr3 = appPath + "WF/WorkOpt/CH.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<a data-role='button' type=button name='CH'  value='" + btnLab.getCHLab() + "' enable=true onclick=\"WinShowModalDialog('" + urlr3 + "'); \" ></a>";
			}



			if (btnLab.getPRIEnable() == true)
			{
				/* 优先级设置 */
				String urlr3 = appPath + "WF/WorkOpt/PRI.htm?FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
				toolbar += "<a data-role='button' type=button name='PR'  value='" + btnLab.getPRILab() + "' enable=true onclick=\"WinShowModalDialog('" + urlr3 + "'); \" ></a>";
			}

			/* 关注 */
			if (btnLab.getFocusEnable() == true)
			{
				if (getHisGenerWorkFlow().getParas_Focus() == true)
				{
					toolbar += "<a data-role='button' type=button  value='取消关注' enable=true onclick=\"FocusBtn(this,'" + this.getWorkID() + "'); \" ></a>";
				}
				else
				{
					toolbar += "<a data-role='button' type=button name='Focus' value='" + btnLab.getFocusLab() + "' enable=true onclick=\"FocusBtn(this,'" + this.getWorkID() + "'); \" ></a>";
				}
			}

			/* 分配工作 */
			if (btnLab.getAllotEnable() == true)
			{
				/*分配工作*/
				String urlAllot = "./WorkOpt/AllotTask.htm?FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&Info=" + "移交原因.";
				toolbar += "<a data-role='button' name='Allot' type=button  value='" + btnLab.getAllotLab() + "' enable=true onclick=\"To('" + urlAllot + "'); \" ></a>";
			}

			/* 确认 */
			if (btnLab.getConfirmEnable() == true)
			{
				if (getHisGenerWorkFlow().getParas_Confirm() == true)
				{
					toolbar += "<a data-role='button' type=button  value='取消确认' enable=true onclick=\"ConfirmBtn(this,'" + this.getWorkID() + "'); \" ></a>";
				}
				else
				{
					toolbar += "<a data-role='button' type=button name='Confirm' value='" + btnLab.getConfirmLab() + "' enable=true onclick=\"ConfirmBtn(this,'" + this.getWorkID() + "'); \" ></a>";
				}
			}

			if (!SystemConfig.getCustomerNo().equals("XJTY"))
			{
				/* 打包下载zip */
				if (btnLab.getPrintZipEnable() == true)
				{
					String packUrl = "./WorkOpt/Packup.htm?FileType=zip&FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
					toolbar += "<input type=button name='PackUp_zip'  value='" + btnLab.getPrintZipLab() + "' enable=true/>";
				}

				/* 打包下载html */
				if (btnLab.getPrintHtmlEnable() == true)
				{
					String packUrl = "./WorkOpt/Packup.htm?FileType=html&FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
					toolbar += "<input type=button name='PackUp_html'  value='" + btnLab.getPrintHtmlLab() + "' enable=true/>";
				}

				/* 打包下载pdf */
				if (btnLab.getPrintPDFEnable() == true)
				{
					String packUrl = "./WorkOpt/Packup.htm?FileType=pdf&FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow();
					toolbar += "<input type=button name='PackUp_pdf'  value='" + btnLab.getPrintPDFLab() + "' enable=true/>";
				}
			}

			/*** 打包下载 */
			*/
			//if (btnLab.PrintZipEnable == true)
			//{
			//    string packUrl = "./WorkOpt/Packup.htm?FK_Node=" + this.FK_Node + "&WorkID=" + this.WorkID + "&FID=" + this.FID + "&FK_Flow=" + this.FK_Flow;
			//    toolbar += "<a data-role='button' type=button name='PackUp'  value='" + btnLab.PrintZipLab + "' enable=true></a>";
			//}


				///#endregion


				///#region //加载自定义的button.
			BP.WF.Template.NodeToolbars bars = new NodeToolbars();
			bars.Retrieve(NodeToolbarAttr.FK_Node, this.getFK_Node());
			for (NodeToolbar bar : bars)
			{
				if (bar.getShowWhere() != ShowWhere.Toolbar)
				{
					continue;
				}

				//如果是script.
				if (bar.getExcType() == 1 || (!DataType.IsNullOrEmpty(bar.getTarget()) && bar.getTarget().toLowerCase().equals("javascript")))
				{
					toolbar += "<a data-role='button' type=button  value='" + bar.getTitle() + "' enable=true onclick=\"" + bar.getUrl() + "\" ></a>";
				}
				else
				{
					String urlr3 = bar.getUrl() + "&FK_Node=" + this.getFK_Node() + "&FID=" + this.getFID() + "&WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&s=" + tKey;
					toolbar += "<a data-role='button' type=button  value='" + bar.getTitle() + "' enable=true onclick=\"WinOpen('" + urlr3 + "'); \" ></a>";
				}
			}

				///#endregion //加载自定义的button.

		}
		catch (RuntimeException ex)
		{
			BP.DA.Log.DefaultLogWriteLineError(ex);
			toolbar = "err@" + ex.getMessage();
		}
		return toolbar;
	}
	/** 
	 获取主表的方法.
	 
	 @return 
	*/
	private Hashtable GetMainTableHT()
	{
		Hashtable htMain = new Hashtable();
		for (String key : HttpContextHelper.RequestParamKeys)
		{
			if (key == null)
			{
				continue;
			}

			if (key.contains("TB_"))
			{

				String val = HttpContextHelper.RequestParams(key);
				if (htMain.containsKey(key.replace("TB_", "")) == false)
				{
					val = HttpUtility.UrlDecode(val, Encoding.UTF8);
					htMain.put(key.replace("TB_", ""), val);
				}
				continue;
			}

			if (key.contains("DDL_"))
			{
				htMain.put(key.replace("DDL_", ""), HttpContextHelper.RequestParams(key));
				continue;
			}

			if (key.contains("CB_"))
			{
				htMain.put(key.replace("CB_", ""), HttpContextHelper.RequestParams(key));
				continue;
			}

			if (key.contains("RB_"))
			{
				htMain.put(key.replace("RB_", ""), HttpContextHelper.RequestParams(key));
				continue;
			}
		}
		return htMain;
	}
	/** 
	 删除流程
	 
	 @return 
	*/
	public final String DeleteFlow()
	{
		try
		{
			return BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(this.getFK_Flow(), this.getWorkID(), true);
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
	public final String Send()
	{
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
				return "该流程的工作已删除,请联系管理员";
			}

			objs = BP.WF.Dev2Interface.Node_SendWork(this.getFK_Flow(), this.getWorkID(), ht, null, this.getToNode(), null);
			msg = objs.ToMsgOfHtml();
			BP.WF.Glo.setSessionMsg(msg);

			//当前节点.
			Node currNode = new Node(this.getFK_Node());


				///#region 处理发送后转向.
			/*处理转向问题.*/
			switch (currNode.getHisTurnToDeal())
			{
				case SpecUrl:
					String myurl = currNode.getTurnToDealDoc().Clone().toString();
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
						myurl = myurl.replace("@" + attr.Key, hisWK.GetValStrByKey(attr.getKey()));
					}
					myurl = myurl.replace("@WebUser.getNo()", WebUser.getNo());
					myurl = myurl.replace("@WebUser.getName()", WebUser.getName());
					myurl = myurl.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());

					if (myurl.contains("@"))
					{
						BP.WF.Dev2Interface.Port_SendMsg("admin", getcurrFlow().Name + "在" + getcurrND().getName() + "节点处，出现错误", "流程设计错误，在节点转向url中参数没有被替换下来。Url:" + myurl, "Err" + getcurrND().getNo() + "_" + this.getWorkID(), SMSMsgType.Err, this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID());
						throw new RuntimeException("流程设计错误，在节点转向url中参数没有被替换下来。Url:" + myurl);
					}

					if (myurl.contains("PWorkID") == false)
					{
						myurl += "&PWorkID=" + this.getWorkID();
					}

					myurl += "&FromFlow=" + this.getFK_Flow() + "&FromNode=" + this.getFK_Node() + "&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.SID;
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
					//        string url = tt.TurnToURL.Clone().ToString();
					//        if (url.Contains("?") == false)
					//            url += "?1=1";
					//        Attrs attrs = currNode.HisWork.getEnMap().getAttrs();
					//        Work hisWK1 = currNode.HisWork;
					//        foreach (Attr attr in attrs)
					//        {
					//            if (url.Contains("@") == false)
					//                break;
					//            url = url.Replace("@" + attr.Key, hisWK1.GetValStrByKey(attr.getKey()));
					//        }
					//        if (url.Contains("@"))
					//            throw new Exception("流程设计错误，在节点转向url中参数没有被替换下来。Url:" + url);

					//        url += "&PFlowNo=" + this.FK_Flow + "&FromNode=" + this.FK_Node + "&PWorkID=" + this.WorkID + "&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.SID;
					//        return "url@" + url;
					//    }
					//}
					return msg;
				default:
					msg = msg.replace("@WebUser.getNo()", WebUser.getNo());
					msg = msg.replace("@WebUser.getName()", WebUser.getName());
					msg = msg.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
					return msg;
			}

				///#endregion

		}
		catch (RuntimeException ex)
		{
			if (ex.getMessage().Contains("请选择下一步骤工作") == true || ex.getMessage().Contains("用户没有选择发送到的节点") == true)
			{
				if (this.getcurrND().getCondModel() == CondModel.ByUserSelected)
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
			if (ex.getMessage().Contains("提交前检查到如下必填字段填写不完整") == true || ex.getMessage().Contains("您没有上传附件") == true || ex.getMessage().Contains("您没有上传图片附件") == true)
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
						if (this.getHisGenerWorkFlow().getTodoEmps().contains(empStr) == false)
						{
							this.getHisGenerWorkFlow().setTodoEmps(this.getHisGenerWorkFlow().getTodoEmps() + empStr); // WebUser.getNo() +","+WebUser.getName() + ";";
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
			return ex.getMessage();
		}
	}
	/** 
	 批量发送
	 
	 @return 
	*/
	public final String StartGuide_MulitSend()
	{
		//获取设置的数据源
		Flow fl = new Flow(this.getFK_Flow());
		String key = this.GetRequestVal("Key");
		String SKey = this.GetRequestVal("Keys");
		String sql = "";
		//判断是否有查询条件
		Object tempVar = fl.getStartGuidePara2().Clone();
		sql = tempVar instanceof String ? (String)tempVar : null;
		if (!DataType.IsNullOrEmpty(key))
		{
			Object tempVar2 = fl.getStartGuidePara1().Clone();
			sql = tempVar2 instanceof String ? (String)tempVar2 : null;
			sql = sql.replace("@Key", key);
		}
		//替换变量
		sql = sql.replace("~", "'");
		sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
		sql = sql.replace("@WebUser.getName()", WebUser.getName());
		sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
		sql = sql.replace("@WebUser.getFK_DeptName", WebUser.getFK_DeptName);

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		//获取选中的数据源
		DataRow[] drArr = dt.Select("No in(" + tangible.StringHelper.trimEnd(SKey, ',') + ")");

		//获取Nos
		String Nos = "";
		for (int i = 0; i < drArr.length; i++)
		{
			DataRow row = drArr[i];
			Nos += row.get("No") + ",";
		}
		return tangible.StringHelper.trimEnd(Nos, ',');
	}
	/** 
	 保存
	 
	 @return 
	*/
	public final String Save()
	{
		try
		{
			String str = BP.WF.Dev2Interface.Node_SaveWork(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.GetMainTableHT(), null);

			if (this.getPWorkID() != 0)
			{
				GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
				BP.WF.Dev2Interface.SetParentInfo(this.getFK_Flow(), this.getWorkID(), this.getPWorkID(), gwf.getPEmp(), gwf.getPNodeID());
			}


			return str;
		}
		catch (RuntimeException ex)
		{
			return "err@保存失败:" + ex.getMessage();
		}
	}
	public final String MyFlowSelfForm_Init()
	{
		return this.GenerWorkNode();
	}

	public final String SaveFlow_ToDraftRole()
	{

		Node nd = new Node(this.getFK_Node());
		Work wk = nd.getHisWork();
		if (this.getWorkID() != 0)
		{
			wk.setOID(this.getWorkID());
			wk.RetrieveFromDBSources();
		}

		//获取表单树的数据
		BP.WF.WorkNode workNode = new WorkNode(this.getWorkID(), this.getFK_Node());
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
		//            if (wkattr.Key.Equals(StartWorkAttr.OID) || wkattr.Key.Equals(StartWorkAttr.FID) || wkattr.Key.Equals(StartWorkAttr.CDT)
		//                || wkattr.Key.Equals(StartWorkAttr.RDT) || wkattr.Key.Equals(StartWorkAttr.MD5) || wkattr.Key.Equals(StartWorkAttr.Emps)
		//                || wkattr.Key.Equals(StartWorkAttr.FK_Dept) || wkattr.Key.Equals(StartWorkAttr.PRI) || wkattr.Key.Equals(StartWorkAttr.Rec)
		//                || wkattr.Key.Equals(StartWorkAttr.Title) || wkattr.Key.Equals(Data.GERptAttr.FK_NY) || wkattr.Key.Equals(Data.GERptAttr.FlowEmps)
		//                || wkattr.Key.Equals(Data.GERptAttr.FlowStarter) || wkattr.Key.Equals(Data.GERptAttr.FlowStartRDT) || wkattr.Key.Equals(Data.GERptAttr.WFState))
		//            {
		//                continue;
		//            }

		//            foreach (Attr attr in frmAttrs)
		//            {
		//                if (wkattr.Key.Equals(attr.getKey()))
		//                {
		//                    wk.SetValByKey(wkattr.Key, en.GetValStrByKey(attr.getKey()));
		//                    break;
		//                }

		//            }

		//        }

		//    }
		//    wk.Update();
		//}


			///#region 为开始工作创建待办.
		if (nd.getIsStartNode() == true)
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
			String title = BP.WF.WorkFlowBuessRole.GenerTitle(fl, wk);

			//修改RPT表的标题
			wk.SetValByKey(BP.WF.Data.GERptAttr.Title, title);
			wk.Update();

			gwf.setWorkID(this.getWorkID());
			int count = gwf.RetrieveFromDBSources();

			gwf.setTitle(title); //标题.
			if (count == 0)
			{
				gwf.setFlowName(fl.Name);
				gwf.setFK_Flow(this.getFK_Flow());
				gwf.setFK_FlowSort(fl.getFK_FlowSort());
				gwf.setSysType(fl.getSysType());

				gwf.setFK_Node(this.getFK_Node());
				gwf.setNodeName(nd.getName());
				gwf.setWFState(wfState);

				gwf.setFK_Dept(WebUser.getFK_Dept());
				gwf.setDeptName(WebUser.getFK_DeptName);
				gwf.setStarter(WebUser.getNo());
				gwf.setStarterName(WebUser.getName());
				gwf.setRDT(DataType.getCurrentDataTime());
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
				gwl.setFK_DeptT(WebUser.getFK_DeptName);

				gwl.setSDT("无");
				gwl.setDTOfWarning(DataType.getCurrentDataTime());
				gwl.setIsEnable(true);

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
	/** 
	 获取表单树数据
	 
	 @return 
	*/
	private BP.WF.Template.FlowFormTrees appFlowFormTree = new FlowFormTrees();
	public final String FlowFormTree_Init()
	{
		//add root
		BP.WF.Template.FlowFormTree root = new BP.WF.Template.FlowFormTree();
		root.No = "00";
		root.setParentNo("0");
		root.Name = "目录";
		root.setNodeType("root");
		appFlowFormTree.Clear();
		appFlowFormTree.AddEntity(root);


			///#region 添加表单及文件夹

		//节点表单
		BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());

		FrmNodes frmNodes = new FrmNodes();
		frmNodes.Retrieve(FrmNodeAttr.FK_Node, this.getFK_Node(), FrmNodeAttr.Idx);

		//文件夹
		SysFormTrees formTrees = new SysFormTrees();
		formTrees.RetrieveAll(SysFormTreeAttr.Name);

		//所有表单集合.
		MapDatas mds = new MapDatas();
		mds.RetrieveInSQL("SELECT FK_Frm FROM WF_FrmNode WHERE FK_Node=" + this.getFK_Node());


		String frms = HttpContextHelper.RequestParams("Frms");
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		if (DataType.IsNullOrEmpty(frms) == true)
		{
			frms = gwf.getParas_Frms();
		}
		else
		{
			gwf.setParas_Frms(frms);
			gwf.Update();
		}

		for (FrmNode frmNode : frmNodes)
		{

				///#region 增加判断是否启用规则.
			switch (frmNode.getFrmEnableRole())
			{
				case Allways:
					break;
				case WhenHaveData: //判断是否有数据.
					MapData md = new MapData(frmNode.getFK_Frm());
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
					if (DBAccess.RunSQLReturnValInt("SELECT COUNT(*) as Num FROM " + md.PTable + " WHERE OID=" + pk) == 0)
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
						//return "err@当前表单设置为仅有参数的时候启用,但是没有传递来参数.";
					}

					if (frms.contains(",") == false)
					{
						if (!frmNode.getFK_Frm().equals(frms))
						{
							continue;
						}
					}

					if (frms.contains(",") == true)
					{
						if (frms.contains(frmNode.getFK_Frm() + ",") == false)
						{
							continue;
						}
					}

					break;
				case ByFrmFields:
					throw new RuntimeException("@这种类型的判断，ByFrmFields 还没有完成。");

				case BySQL: // 按照SQL的方式.
					Object tempVar = frmNode.getFrmEnableExp().Clone();
					String mysql = tempVar instanceof String ? (String)tempVar : null;

					if (DataType.IsNullOrEmpty(mysql) == true)
					{
						MapData FrmMd = new MapData(frmNode.getFK_Frm());
						return "err@表单" + frmNode.getFK_Frm() + ",[" + FrmMd.Name + "]在节点[" + frmNode.getFK_Node() + "]启用方式按照sql启用但是您没有给他设置sql表达式.";
					}


					mysql = mysql.replace("@OID", String.valueOf(this.getWorkID()));
					mysql = mysql.replace("@WorkID", String.valueOf(this.getWorkID()));

					mysql = mysql.replace("@NodeID", String.valueOf(this.getFK_Node()));
					mysql = mysql.replace("@FK_Node", String.valueOf(this.getFK_Node()));

					mysql = mysql.replace("@FK_Flow", this.getFK_Flow());

					mysql = mysql.replace("@WebUser.getNo()", WebUser.getNo());
					mysql = mysql.replace("@WebUser.getName()", WebUser.getName());
					mysql = mysql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());


					//替换特殊字符.
					mysql = mysql.replace("~", "'");

					if (DBAccess.RunSQLReturnValFloat(mysql) <= 0)
					{
						continue;
					}
					break;
				//@袁丽娜
				case ByStation:
					Object tempVar2 = frmNode.getFrmEnableExp().Clone();
					String exp = tempVar2 instanceof String ? (String)tempVar2 : null;
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
				//@袁丽娜
				case ByDept:
					Object tempVar3 = frmNode.getFrmEnableExp().Clone();
					exp = tempVar3 instanceof String ? (String)tempVar3 : null;
					Sql = "SELECT FK_Dept FROM Port_DeptEmp where FK_Emp='" + WebUser.getNo() + "'";
					String dept = DBAccess.RunSQLReturnString(Sql);
					if (DataType.IsNullOrEmpty(dept) == true)
					{
						continue;
					}
					String[] depts = dept.split("[;]", -1);
					isExit = false;
					for (String s : depts)
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
			for (MapData md : mds)
			{
				if (md.FK_FormTree.equals(""))
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
				for (MapData md : mds)
				{
					if (!md.FK_FormTree.equals(""))
					{
						treeNo = md.FK_FormTree;
						break;
					}
				}
			}

				///#endregion 检查是否有没有目录的表单?

			for (MapData md : mds)
			{
				if (!frmNode.getFK_Frm().equals(md.No))
				{
					continue;
				}

				if (md.FK_FormTree.equals(""))
				{
					md.FK_FormTree = treeNo;
				}

				for (SysFormTree formTree : formTrees)
				{
					if (md.FK_FormTree != formTree.No)
					{
						continue;
					}
					if (appFlowFormTree.Contains("No", formTree.No) == false)
					{
						BP.WF.Template.FlowFormTree nodeFolder = new BP.WF.Template.FlowFormTree();
						nodeFolder.No = formTree.No;
						nodeFolder.ParentNo = formTree.getParentNo();
						nodeFolder.Name = formTree.Name;
						nodeFolder.setNodeType("folder");
						appFlowFormTree.AddEntity(nodeFolder);
						break;
					}
				}

				//检查必填项.
				boolean IsNotNull = false;
				FrmFields formFields = new FrmFields();
				QueryObject obj = new QueryObject(formFields);
				obj.AddWhere(FrmFieldAttr.FK_Node, this.getFK_Node());
				obj.addAnd();
				obj.AddWhere(FrmFieldAttr.FK_MapData, md.No);
				obj.addAnd();
				obj.AddWhere(FrmFieldAttr.IsNotNull, 1);
				obj.DoQuery();
				if (formFields != null && formFields.size() > 0)
				{
					IsNotNull = true;
				}

				BP.WF.Template.FlowFormTree nodeForm = new BP.WF.Template.FlowFormTree();
				nodeForm.No = md.No;
				nodeForm.ParentNo = md.FK_FormTree;
				nodeForm.Name = md.Name;
				nodeForm.setNodeType(IsNotNull ? "form|1" : "form|0");
				nodeForm.setIsEdit(String.valueOf(frmNode.getIsEditInt())); // Convert.ToString(Convert.ToInt32(frmNode.IsEdit));
				nodeForm.setIsCloseEtcFrm(String.valueOf(frmNode.getIsCloseEtcFrmInt()));
				appFlowFormTree.AddEntity(nodeForm);
				break;
			}
		}
		//找上级表单文件夹
		AppendFolder(formTrees);

			///#endregion

		//扩展工具，显示位置为表单树类型.
		NodeToolbars extToolBars = new NodeToolbars();
		QueryObject info = new QueryObject(extToolBars);
		info.AddWhere(NodeToolbarAttr.FK_Node, this.getFK_Node());
		info.addAnd();
		info.AddWhere(NodeToolbarAttr.ShowWhere, ShowWhere.Tree.getValue());
		info.DoQuery();

		for (NodeToolbar item : extToolBars)
		{
			String url = "";
			if (DataType.IsNullOrEmpty(item.getUrl()))
			{
				continue;
			}

			url = item.getUrl();

			BP.WF.Template.FlowFormTree formTree = new BP.WF.Template.FlowFormTree();
			formTree.No = item.OID.toString();
			formTree.ParentNo = "01";
			formTree.Name = item.getTitle();
			formTree.setNodeType("tools|0");
			if (!DataType.IsNullOrEmpty(item.getTarget()) && item.getTarget().toUpperCase().equals("_BLANK"))
			{
				formTree.setNodeType("tools|1");
			}

			formTree.setUrl(url);
			appFlowFormTree.AddEntity(formTree);
		}
		TansEntitiesToGenerTree(appFlowFormTree, root.No, "");
		return appendMenus.toString();
	}
	/** 
	 拼接文件夹
	 
	 @param formTrees
	*/
	private void AppendFolder(SysFormTrees formTrees)
	{
		BP.WF.Template.FlowFormTrees parentFolders = new BP.WF.Template.FlowFormTrees();
		//二级目录
		for (BP.WF.Template.FlowFormTree folder : appFlowFormTree)
		{
			if (DataType.IsNullOrEmpty(folder.getNodeType()) || !folder.getNodeType().equals("folder"))
			{
				continue;
			}

			for (SysFormTree item : formTrees)
			{
				//排除根节点
				if (item.getParentNo().equals("0") || item.No.equals("0"))
				{
					continue;
				}
				if (parentFolders.Contains("No", item.No) == true)
				{
					continue;
				}
				//文件夹
				if (folder.ParentNo.equals(item.No))
				{
					if (parentFolders.Contains("No", item.No) == true)
					{
						continue;
					}
					if (item.getParentNo().equals("0") == true)
					{
						continue;
					}

					BP.WF.Template.FlowFormTree nodeFolder = new BP.WF.Template.FlowFormTree();
					nodeFolder.No = item.No;
					nodeFolder.ParentNo = item.getParentNo();
					nodeFolder.Name = item.Name;
					nodeFolder.setNodeType("folder");
					parentFolders.AddEntity(nodeFolder);
				}
			}
		}
		//找到父级目录添加到集合
		for (BP.WF.Template.FlowFormTree folderapp : parentFolders)
		{
			if (appFlowFormTree.Contains(folderapp) == false)
			{
				appFlowFormTree.AddEntity(folderapp);
			}
		}
		//求出没有父节点的文件夹
		parentFolders.Clear();
		for (BP.WF.Template.FlowFormTree folder : appFlowFormTree)
		{
			if (DataType.IsNullOrEmpty(folder.getNodeType()) || folder.getNodeType().equals("folder") == false)
			{
				continue;
			}

			boolean bHave = false;
			for (BP.WF.Template.FlowFormTree child : appFlowFormTree)
			{
				if (folder.ParentNo.equals(child.No) == true)
				{
					bHave = true;
					break;
				}
			}
			//没有父节点的文件夹
			if (bHave == false && parentFolders.Contains("No", folder.No) == false)
			{
				parentFolders.AddEntity(folder);
			}
		}
		//修改根节点编号
		for (BP.WF.Template.FlowFormTree folder : parentFolders)
		{
			for (BP.WF.Template.FlowFormTree folderApp : appFlowFormTree)
			{
				if (folderApp.No.equals(folder.No) == false)
				{
					continue;
				}
				folderApp.ParentNo = "00";
			}
		}
	}
	/** 
	 将实体转为树形
	 
	 @param ens
	 @param rootNo
	 @param checkIds
	*/
	private StringBuilder appendMenus = new StringBuilder();
	private StringBuilder appendMenuSb = new StringBuilder();
	public final void TansEntitiesToGenerTree(Entities ens, String rootNo, String checkIds)
	{
		Object tempVar = ens.GetEntityByKey(rootNo);
		EntityTree root = tempVar instanceof EntityTree ? (EntityTree)tempVar : null;
		if (root == null)
		{
			throw new RuntimeException("@没有找到rootNo=" + rootNo + "的entity.");
		}
		appendMenus.append("[{");
		appendMenus.append("\"id\":\"" + rootNo + "\"");
		appendMenus.append(",\"text\":\"" + root.Name + "\"");

		//attributes
		BP.WF.Template.FlowFormTree formTree = root instanceof BP.WF.Template.FlowFormTree ? (BP.WF.Template.FlowFormTree)root : null;
		if (formTree != null)
		{
			String url = formTree.getUrl() == null ? "" : formTree.getUrl();
			url = url.replace("/", "|");
			appendMenus.append(",\"attributes\":{\"NodeType\":\"" + formTree.getNodeType() + "\",\"IsEdit\":\"" + formTree.getIsEdit() + "\",\"IsCloseEtcFrm\":\"" + formTree.getIsCloseEtcFrm() + "\",\"Url\":\"" + url + "\"}");
		}
		appendMenus.append(",iconCls:\"icon-Wave\"");
		// 增加它的子级.
		appendMenus.append(",\"children\":");
		AddChildren(root, ens, checkIds);
		appendMenus.append(appendMenuSb);
		appendMenus.append("}]");
	}

	public final void AddChildren(EntityTree parentEn, Entities ens, String checkIds)
	{
		appendMenus.append(appendMenuSb);
		appendMenuSb.setLength(0);

		appendMenuSb.append("[");
		for (EntityTree item : ens)
		{
			if (item.ParentNo != parentEn.No)
			{
				continue;
			}

			if (checkIds.contains("," + item.No + ","))
			{
				appendMenuSb.append("{\"id\":\"" + item.No + "\",\"text\":\"" + item.Name + "\",\"checked\":true");
			}
			else
			{
				appendMenuSb.append("{\"id\":\"" + item.No + "\",\"text\":\"" + item.Name + "\",\"checked\":false");
			}


			//attributes
			BP.WF.Template.FlowFormTree formTree = item instanceof BP.WF.Template.FlowFormTree ? (BP.WF.Template.FlowFormTree)item : null;
			if (formTree != null)
			{
				String url = formTree.getUrl() == null ? "" : formTree.getUrl();
				String ico = "icon-tree_folder";
				if (SystemConfig.SysNo.equals("YYT"))
				{
					ico = "icon-boat_16";
				}
				url = url.replace("/", "|");
				appendMenuSb.append(",\"attributes\":{\"NodeType\":\"" + formTree.getNodeType() + "\",\"IsEdit\":\"" + formTree.getIsEdit() + "\",\"IsCloseEtcFrm\":\"" + formTree.getIsCloseEtcFrm() + "\",\"Url\":\"" + url + "\"}");
				//图标
				if (formTree.getNodeType().equals("form|0"))
				{
					ico = "form0";
					if (SystemConfig.SysNo.equals("YYT"))
					{
						ico = "icon-Wave";
					}
				}
				if (formTree.getNodeType().equals("form|1"))
				{
					ico = "form1";
					if (SystemConfig.SysNo.equals("YYT"))
					{
						ico = "icon-Shark_20";
					}
				}
				if (formTree.getNodeType().contains("tools"))
				{
					ico = "icon-4";
					if (SystemConfig.SysNo.equals("YYT"))
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
	public final String GenerWorkNode()
	{
		String json = "";
		try
		{
			DataSet ds = new DataSet();

			ds = BP.WF.CCFlowAPI.GenerWorkNode(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), this.getFID(), WebUser.getNo());

			//Node nd = new Node(this.FK_Node);
			//if (nd.HisFormType == NodeFormType.SheetTree)
			//{
			//    /*把树形表单的表单信息加载到ds里面.*/
			//}
			//把他转化小写,适应多个数据库.
			//   wf_generWorkFlowDt = DBAccess.ToLower(wf_generWorkFlowDt);
			// ds.Tables.add(wf_generWorkFlowDt);
			// ds.WriteXml("c:\\xx.xml");


				///#region 如果是移动应用就考虑多表单的问题.
			if (getcurrND().getHisFormType() == NodeFormType.SheetTree && this.getIsMobile() == true)
			{
				/*如果是表单树并且是，移动模式.*/


				FrmNodes fns = new FrmNodes();
				QueryObject qo = new QueryObject(fns);

				qo.AddWhere(FrmNodeAttr.FK_Node, getcurrND().getNodeID());
				qo.addAnd();
				qo.AddWhere(FrmNodeAttr.FrmEnableRole, "!=", FrmEnableRole.Disable.getValue());
				qo.addOrderBy("Idx");
				qo.DoQuery();


				//把节点与表单的关联管理放入到系统.
				ds.Tables.add(fns.ToDataTableField("FrmNodes"));
			}

				///#endregion 如果是移动应用就考虑多表单的问题.

			if (WebUser.SysLang.equals("CH") == true)
			{
				return BP.Tools.Json.ToJson(ds);
			}


				///#region 处理多语言.
			if (WebUser.SysLang.equals("CH") == false)
			{
				Langues langs = new Langues();
				langs.Retrieve(LangueAttr.Model, LangueModel.CCForm, LangueAttr.Sort, "Fields", LangueAttr.Langue, WebUser.SysLang); //查询语言.
			}

				///#endregion 处理多语言.

			return BP.Tools.Json.ToJson(ds);


		}
		catch (RuntimeException ex)
		{
			BP.DA.Log.DefaultLogWriteLineError(ex);
			return "err@" + ex.getMessage();
		}
	}


}
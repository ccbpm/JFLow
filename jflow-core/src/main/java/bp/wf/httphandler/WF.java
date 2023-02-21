package bp.wf.httphandler;

import bp.da.*;
import bp.difference.handler.CommonUtils;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.sys.CCFormAPI;
import bp.web.*;
import bp.difference.*;
import bp.wf.Glo;
import bp.wf.port.*;
import bp.wf.template.*;
import bp.wf.template.sflow.*;
import bp.*;
import bp.wf.*;
import java.util.*;
import java.io.*;
import bp.wf.data.GERpt;

public class WF extends WebContralBase
{
	public final String DealErrInfo_Save() throws Exception {
		return "";
	}


		///#region 单表单查看.
	/** 
	 流程单表单查看
	 
	 @return 
	*/
	public final String FrmView_Init() throws Exception {
		Node nd = new Node(this.getFK_Node());
		nd.WorkID = this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.

		MapData md = new MapData();
		md.setNo(nd.getNodeFrmID());
		if (md.RetrieveFromDBSources() == 0)
		{
			throw new RuntimeException("装载错误，该表单ID=" + md.getNo() + "丢失，请修复一次流程重新加载一次.");
		}

		//获得表单模版.
		DataSet myds = CCFormAPI.GenerHisDataSet(md.getNo(), null, null);


			///#region 把主从表数据放入里面.
		//.工作数据放里面去, 放进去前执行一次装载前填充事件.
		Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.RetrieveFromDBSources();

		//重设默认值.
		//wk.ResetDefaultVal();

		DataTable mainTable = wk.ToDataTableField("MainTable");
		mainTable.TableName = "MainTable";
		myds.Tables.add(mainTable);

			///#endregion

		//加入WF_Node.
		DataTable WF_Node = nd.ToDataTableField("WF_Node").copy();
		myds.Tables.add(WF_Node);


			///#region 加入组件的状态信息, 在解析表单的时候使用.
		nd.WorkID = this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.
		FrmNodeComponent fnc = new FrmNodeComponent(nd.getNodeID());
		if (!nd.getNodeFrmID().equals("ND" + nd.getNodeID()))
		{
			/*说明这是引用到了其他节点的表单，就需要把一些位置元素修改掉.*/
			int refNodeID = 0;
			if (nd.getNodeFrmID().indexOf("ND") == -1)
			{
				refNodeID = nd.getNodeID();
			}
			else
			{
				refNodeID = Integer.parseInt(nd.getNodeFrmID().replace("ND", ""));
			}

			FrmNodeComponent refFnc = new FrmNodeComponent(refNodeID);

			fnc.SetValByKey(NodeWorkCheckAttr.FWC_H, refFnc.GetValFloatByKey(NodeWorkCheckAttr.FWC_H));

			fnc.SetValByKey(FrmSubFlowAttr.SF_H, refFnc.GetValFloatByKey(FrmSubFlowAttr.SF_H));


			fnc.SetValByKey(FrmTrackAttr.FrmTrack_H, refFnc.GetValFloatByKey(FrmTrackAttr.FrmTrack_H));

			fnc.SetValByKey(FTCAttr.FTC_H, refFnc.GetValFloatByKey(FTCAttr.FTC_H));
		}
		myds.Tables.add(fnc.ToDataTableField("WF_FrmNodeComponent").copy());

			///#endregion 加入组件的状态信息, 在解析表单的时候使用.


			///#region 增加附件信息.
		FrmAttachments athDescs = new FrmAttachments();

		nd.WorkID = this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.
		athDescs.Retrieve(FrmAttachmentAttr.FK_MapData, nd.getNodeFrmID(), null);
		if (!athDescs.isEmpty())
		{
			FrmAttachment athDesc = athDescs.get(0) instanceof FrmAttachment ? (FrmAttachment)athDescs.get(0) : null;

			//查询出来数据实体.
			FrmAttachmentDBs dbs = new FrmAttachmentDBs();
			if (athDesc.getHisCtrlWay() == AthCtrlWay.PWorkID)
			{
				Paras ps = new Paras();
				ps.SQL = "SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID";
				ps.Add("WorkID", this.getWorkID());
				String pWorkID = String.valueOf(DBAccess.RunSQLReturnValInt(ps, 0));
				if (pWorkID == null || pWorkID.equals("0"))
				{
					pWorkID = String.valueOf(this.getWorkID());
				}

				if (athDesc.getAthUploadWay() == AthUploadWay.Inherit)
				{
					/* 继承模式 */
					bp.en.QueryObject qo = new bp.en.QueryObject(dbs);
					qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, pWorkID);
					qo.addOr();
					qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, String.valueOf(this.getWorkID()));
					qo.addOrderBy("RDT");
					qo.DoQuery();
				}

				if (athDesc.getAthUploadWay() == AthUploadWay.Interwork)
				{
					/*共享模式*/
					dbs.Retrieve(FrmAttachmentDBAttr.RefPKVal, pWorkID, null);
				}
			}
			else if (athDesc.getHisCtrlWay() == AthCtrlWay.WorkID)
			{
				/* 继承模式 */
				bp.en.QueryObject qo = new bp.en.QueryObject(dbs);
				qo.AddWhere(FrmAttachmentDBAttr.NoOfObj, athDesc.getNoOfObj());
				qo.addAnd();
				qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, String.valueOf(this.getWorkID()));
				qo.addOrderBy("RDT");
				qo.DoQuery();
			}

			//增加一个数据源.
			myds.Tables.add(dbs.ToDataTableField("Sys_FrmAttachmentDB").copy());
		}

			///#endregion


			///#region 把外键表加入DataSet
		DataTable dtMapAttr = myds.GetTableByName("Sys_MapAttr");
		DataTable dt = new DataTable();
		MapExts mes = md.getMapExts();
		MapExt me = new MapExt();
		DataTable ddlTable = new DataTable();
		ddlTable.Columns.Add("No");
		for (DataRow dr : dtMapAttr.Rows)
		{
			String lgType = dr.getValue("LGType").toString();
			String uiBindKey = dr.getValue("UIBindKey").toString();

			if (DataType.IsNullOrEmpty(uiBindKey) == true)
			{
				continue; //为空就continue.
			}

			if (lgType.equals("1") == true)
			{
				continue; //枚举值就continue;
			}

			String uiIsEnable = dr.getValue("UIIsEnable").toString();
			if (uiIsEnable.equals("0") == true && lgType.equals("1") == true)
			{
				continue; //如果是外键，并且是不可以编辑的状态.
			}

			if (uiIsEnable.equals("1") == true && lgType.equals("0") == true)
			{
				continue; //如果是外部数据源，并且是不可以编辑的状态.
			}



			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.getValue("KeyOfEn").toString();
			String fk_mapData = dr.getValue("FK_MapData").toString();


				///#region 处理下拉框数据范围. for 小杨.
			Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
			me = tempVar instanceof MapExt ? (MapExt)tempVar : null;
			if (me != null)
			{
				Object tempVar2 = me.getDoc();
				String fullSQL = tempVar2 instanceof String ? (String)tempVar2 : null;
				fullSQL = fullSQL.replace("~", ",");
				fullSQL = Glo.DealExp(fullSQL, wk, null);
				dt = DBAccess.RunSQLReturnTable(fullSQL);
				//重构新表
				DataTable dt_FK_Dll = new DataTable();
				dt_FK_Dll.TableName = keyOfEn; //可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
				dt_FK_Dll.Columns.Add("No", String.class);
				dt_FK_Dll.Columns.Add("Name", String.class);
				for (DataRow dllRow : dt.Rows)
				{
					DataRow drDll = dt_FK_Dll.NewRow();
					drDll.setValue("No", dllRow.getValue("No"));
					drDll.setValue("Name", dllRow.getValue("Name"));
					dt_FK_Dll.Rows.add(drDll);
				}
				myds.Tables.add(dt_FK_Dll);
				continue;
			}

				///#endregion 处理下拉框数据范围.

			// 判断是否存在.
			if (myds.contains(uiBindKey) == true)
			{
				continue;
			}

			DataTable mydt = bp.pub.PubClass.GetDataTableByUIBineKey(uiBindKey, null);
			if (mydt == null)
			{
				DataRow ddldr = ddlTable.NewRow();
				ddldr.setValue("No", uiBindKey);
				ddlTable.Rows.add(ddldr);
			}
			else
			{
				myds.Tables.add(mydt);
			}
		}
		ddlTable.TableName = "UIBindKey";
		myds.Tables.add(ddlTable);

			///#endregion End把外键表加入DataSet


			///#region 图片附件
		nd.WorkID = this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.
		FrmImgAthDBs imgAthDBs = new FrmImgAthDBs(nd.getNodeFrmID(), String.valueOf(this.getWorkID()));
		if (imgAthDBs != null && !imgAthDBs.isEmpty())
		{
			DataTable dt_ImgAth = imgAthDBs.ToDataTableField("Sys_FrmImgAthDB");
			myds.Tables.add(dt_ImgAth);
		}

			///#endregion

		return bp.tools.Json.ToJson(myds);
	}

		///#endregion


		///#region 综合查询
	/** 
	 综合查询
	 
	 @return 
	*/
	public final String SearchZongHe_Init() throws Exception {
		String key = this.GetRequestVal("Key");
		String dtFrom = this.GetRequestVal("DTFrom");
		String dtTo = this.GetRequestVal("DTTo");
		String flowNo = this.GetRequestVal("FlowNo");
		String wfState = this.GetRequestVal("WFState");

		String sql = "SELECT WorkID,FlowName,NodeName,StarterName,RDT,SendDT,WFState,Title FROM WF_GenerWorkFlow ";
		sql += "  WHERE (Starter='" + WebUser.getNo() + "' OR  Emps like '%" + WebUser.getNo() + "%')  ";

		//如果关键字key.
		if (DataType.IsNullOrEmpty(key) == false)
		{
			sql += " AND Title LIKE '%" + key + "%'";
		}

		//如果有日期从到.
		if (DataType.IsNullOrEmpty(dtFrom) == false)
		{
			sql += " AND ( RDT >= '" + dtFrom + "' AND RDT <='" + dtTo + "' ) ";
		}

		//如果有流程编号.
		if (DataType.IsNullOrEmpty(flowNo) == false)
		{
			sql += " AND  FK_Flow= '" + flowNo + "' ";
		}

		//如果有流程状态.
		if (DataType.IsNullOrEmpty(wfState) == true || wfState.equals("all") == false)
		{
			sql += " AND  WFState= '" + wfState + "' ";
		}
		else
		{
			sql += " AND  WFState >1 ";
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return bp.tools.Json.ToJson(dt);
	}

		///#endregion

	/** 
	 流程数据
	 
	 @return 
	*/
	public final String Watchdog_Init() throws Exception {
		String sql = " SELECT FK_Flow,FlowName, COUNT(workid) as Num FROM V_MyFlowData WHERE MyEmpNo='" + WebUser.getNo() + "' ";
		sql += " GROUP BY  FK_Flow,FlowName ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Group";
		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 流程数据初始化
	 
	 @return 
	*/
	public final String Watchdog_InitFlows() throws Exception {
		String sql = " SELECT *  FROM V_MyFlowData WHERE MyEmpNo='" + WebUser.getNo() + "' AND FK_Flow='" + this.getFK_Flow() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Flows";
		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 构造函数
	*/
	public WF() throws Exception {

	}
	/** 
	 为宁海特殊处理
	 
	 @return 
	*/
	public final String GoToMyFlow_Init() throws Exception {
		String userNo = this.GetRequestVal("UserNo");
		Dev2Interface.Port_Login(userNo);
		return "登录成功";
	}
	@Override
	protected String DoDefaultMethod() throws Exception {
		return super.DoDefaultMethod();
	}
	public final String HasSealPic() throws Exception {
		String no = GetRequestVal("No");
		if (DataType.IsNullOrEmpty(no))
		{
			return "";
		}

		String path = "/DataUser/Siganture/" + no + ".jpg";
		//如果文件存在

		if ((new File(SystemConfig.getPathOfWebApp() + (path))).isFile() == false)
		{
			path = "/DataUser/Siganture/" + no + ".JPG";
			if (SystemConfig.getIsJarRun()){
				if ((new File(SystemConfig.getPhysicalPath() + (path))).isFile() == true)
				{
					return "";
				}
			}
			if ((new File(SystemConfig.getPathOfWebApp() + (path))).isFile() == true)
			{
				return "";
			}

			//如果不存在，就返回名称
			bp.port.Emp emp = new bp.port.Emp(no);
			return emp.getName();
		}
		return "";
	}
	/** 
	 执行的方法.
	 
	 @return 
	*/
	public final String Do_Init() throws Exception {
		String at = this.GetRequestVal("ActionType");
		if (DataType.IsNullOrEmpty(at))
		{
			at = this.GetRequestVal("DoType");
		}

		if (DataType.IsNullOrEmpty(at) && this.getSID() != null)
		{
			at = "Track";
		}
		String sid = this.getSID();
		try
		{
			switch (at)
			{

				case "Focus": //把任务放入任务池.
					Dev2Interface.Flow_Focus(this.getWorkID());
					return "info@Close";
				case "PutOne": //把任务放入任务池.
					Dev2Interface.Node_TaskPoolPutOne(this.getWorkID());
					return "info@Close";
				case "DoAppTask": // 申请任务.
					Dev2Interface.Node_TaskPoolTakebackOne(this.getWorkID());
					return "info@Close";
				case "DoOpenCC":
				case "WFRpt":
					String Sta = this.GetRequestVal("Sta");
					if (Sta.equals("0"))
					{
						CCList cc1 = new CCList();
						cc1.setMyPK(this.getMyPK());
						cc1.Retrieve();
						cc1.setHisSta(CCSta.Read);
						cc1.Update();
					}

					if (DataType.IsNullOrEmpty(sid) == false)
					{
						String[] strss = sid.split("[_]", -1);
						GenerWorkFlow gwfl = new GenerWorkFlow(Long.parseLong(strss[1]));
						return "url@./WorkOpt/OneWork/OneWork.htm?CurrTab=Track&FK_Flow=" + gwfl.getFK_Flow() + "&FK_Node=" + gwfl.getFK_Node() + "&WorkID=" + gwfl.getWorkID() + "&FID=" + gwfl.getFID();
					}

					return "url@./WorkOpt/OneWork/OneWork.htm?CurrTab=Track&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID();
				case "DelCC": //删除抄送.
					CCList cc = new CCList();
					cc.setMyPK(this.getMyPK());
					cc.Retrieve();
					cc.setHisSta(CCSta.Del);
					cc.Update();
					return "info@Close";
				case "DelSubFlow": //删除进程。
					try
					{
						Dev2Interface.Flow_DeleteSubThread(this.getWorkID(), "手工删除");
						return "info@Close";
					}
					catch (RuntimeException ex)
					{
						return "err@" + ex.getMessage();
					}

				case "DelDtl":
					GEDtls dtls = new GEDtls(this.getEnsName());
					GEDtl dtl = (GEDtl)dtls.getGetNewEntity();
					dtl.setOID(this.getRefOID());
					if (dtl.RetrieveFromDBSources() == 0)
					{
						return "info@Close";
					}
					FrmEvents fes = new FrmEvents(this.getEnsName()); //获得事件.

					// 处理删除前事件.
					try
					{
						fes.DoEventNode(EventListFrm.DtlRowDelBefore, dtl);
					}
					catch (RuntimeException ex)
					{
						return "err@" + ex.getMessage();
					}
					dtl.Delete();

					// 处理删除后事件.
					try
					{
						fes.DoEventNode(EventListFrm.DtlRowDelAfter, dtl);
					}
					catch (RuntimeException ex)
					{
						return "err@" + ex.getMessage();
					}
					return "info@Close";
				case "EmpDoUp":
					WFEmp ep = new WFEmp(this.GetRequestVal("RefNo"));
					ep.DoUp();

					WFEmps emps111 = new WFEmps();
					//  emps111.RemoveCash();
					emps111.RetrieveAll();
					return "info@Close";
				case "EmpDoDown":
					WFEmp ep1 = new WFEmp(this.GetRequestVal("RefNo"));
					ep1.DoDown();

					WFEmps emps11441 = new WFEmps();
					//  emps11441.RemoveCash();
					emps11441.RetrieveAll();
					return "info@Close";
				case "Track": //通过一个串来打开一个工作.
					String mySid = this.getSID(); // this.Request.QueryString["Token"];
					String[] mystrs = mySid.split("[_]", -1);

					long myWorkID = Integer.parseInt(mystrs[1]);
					String fk_emp = mystrs[0];
					int fk_node = Integer.parseInt(mystrs[2]);
					Node mynd = new Node();
					mynd.setNodeID(fk_node);
					mynd.RetrieveFromDBSources();

					String fk_flow = mynd.getFK_Flow();
					String myurl = "./WorkOpt/OneWork/OneWork.htm?CurrTab=Track&FK_Node=" + mynd.getNodeID() + "&WorkID=" + myWorkID + "&FK_Flow=" + fk_flow;
					WebUser.SignInOfGener(new bp.port.Emp(fk_emp), "CH", false, false, null, null);

					return "url@" + myurl;
				case "OF": //通过一个串来打开一个工作.
					//sid 格式为: guid+"_"+workid+"_"+empNo+"_"+nodeID;
					String[] strs = sid.split("[_]", -1);

					String workID = strs[1];
					String empNo = strs[2];

					GenerWorkerList wl = new GenerWorkerList();
					int i = wl.Retrieve(GenerWorkerListAttr.FK_Emp, empNo, GenerWorkerListAttr.WorkID, workID, GenerWorkerListAttr.IsPass, 0);

					if (i == 0)
					{
						return "info@此工作已经被别人处理或者此流程已删除";
					}

					GenerWorkFlow gwf = new GenerWorkFlow(wl.getWorkID());

					//设置他的组织.
					WebUser.setOrgNo(gwf.getOrgNo());

					bp.port.Emp empOF = new bp.port.Emp(wl.getFK_Emp());
					WebUser.SignInOfGener(empOF, "CH", false, false, null, null);
					String u = "MyFlow.htm?FK_Flow=" + wl.getFK_Flow() + "&WorkID=" + wl.getWorkID() + "&FK_Node=" + wl.getFK_Node() + "&FID=" + wl.getFID() + "&PWorkID=" + gwf.getPWorkID();

					return "url@" + u;
				case "ExitAuth":
					bp.port.Emp emp = new bp.port.Emp(this.getFK_Emp());
					//首先退出，再进行登录
					WebUser.Exit();
					WebUser.SignInOfGener(emp, WebUser.getSysLang(), false, false, null, null);
					return "info@Close";

				case "UnSend": //执行撤消发送。
					String url = "./WorkOpt/UnSend.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow();
					return "url@" + url;
				case "SetBillState":
					break;
				case "WorkRpt":
					break;
				case "PrintBill":
					break;
				//删除流程中第一个节点的数据，包括待办工作
				case "DeleteFlow":
					//调用DoDeleteWorkFlowByReal方法
					WorkFlow wf = new WorkFlow(new Flow(this.getFK_Flow()), this.getWorkID());
					wf.DoDeleteWorkFlowByReal(true);
					return "流程删除成功";
				case "DownFlowSearchExcel": //下载流程查询结果，转到下面的逻辑，不放在此try..catch..中
					break;
				case "DownFlowSearchToTmpExcel": //导出到模板
					break;
				default:
					throw new RuntimeException("没有判断的at标记:" + at);
			}
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
		//此处之所以再加一个switch，是因为在下载文件逻辑中，调用Response.End()方法，如果此方法放在try..catch..中，会报线程中止异常
		switch (at)
		{
			case "DownFlowSearchExcel":
				//  DownMyStartFlowExcel();
				break;
			case "DownFlowSearchToTmpExcel": //导出到模板
				// DownMyStartFlowToTmpExcel();
				break;
		}
		return "";
	}

	/** 
	 获取设置的PC端和移动端URL
	 
	 @return 
	*/
	public final String PCAndMobileUrl() throws Exception {
		Hashtable ht = new Hashtable();
		ht.put("PCUrl", SystemConfig.getHostURL());
		ht.put("MobileUrl", SystemConfig.getMobileURL());
		return bp.tools.Json.ToJson(ht);
	}

	/** 
	 页面调整移动端OR手机端
	 
	 @return 
	*/
	public final String Do_Direct()  {
		//获取地址
		String baseUrl = this.GetRequestVal("DirectUrl");
		////判断是移动端还是PC端打开的页面
		//Regex RegexMobile = new Regex(@"(iemobile|iphone|ipod|android|nokia|sonyericsson|blackberry|samsung|sec\-|windows ce|motorola|mot\-|up.b|midp\-)",
		//RegexOptions.IgnoreCase | RegexOptions.Compiled);
		//移动端打开
		String userAgent = CommonUtils.getRequest().getHeader("user-agent");
		if (userAgent.indexOf("Android") != -1) {
			return SystemConfig.getMobileURL() + baseUrl;
		} else if (userAgent.indexOf("iPhone") != -1 || userAgent.indexOf("iPad") != -1) {
			return SystemConfig.getMobileURL() + baseUrl;
		} else {
			return SystemConfig.getHostURL() + baseUrl;
		}

	}


		///#region 我的关注流程.
	/** 
	 我的关注流程
	 
	 @return 
	*/
	public final String Focus_Init() throws Exception {
		String flowNo = this.GetRequestVal("FK_Flow");
		String domain = this.GetRequestVal("Domain");

		int idx = 0;
		//获得关注的数据.
		DataTable dt = Dev2Interface.DB_Focus(flowNo, WebUser.getNo(), domain);
		SysEnums stas = new SysEnums("WFSta");
		String[] tempArr;
		for (DataRow dr : dt.Rows)
		{
			int wfsta = Integer.parseInt(dr.getValue("WFSta").toString());
			//edit by liuxc,2016-10-22,修复状态显示不正确问题
			Object tempVar = stas.GetEntityByKey(SysEnumAttr.IntKey, wfsta);
			String wfstaT = (tempVar instanceof SysEnum ? (SysEnum)tempVar : null).getLab();
			String currEmp = "";

			if (wfsta != WFSta.Complete.getValue())
			{
				//edit by liuxc,2016-10-24,未完成时，处理当前处理人，只显示处理人姓名
				for (String emp : dr.getValue("ToDoEmps").toString().split("[;]", -1))
				{
					tempArr = emp.split("[,]", -1);

					currEmp += tempArr.length > 1 ? tempArr[1] : tempArr[0] + ",";
				}

				currEmp = StringHelper.trimEnd(currEmp, ',');

				//currEmp = dr["ToDoEmps"].ToString();
				//currEmp = currEmp.TrimEnd(';');
			}
			dr.setValue("ToDoEmps", currEmp);
			dr.setValue("FlowNote", wfstaT);
			dr.setValue("AtPara", (wfsta == WFSta.Complete.getValue() ? StringHelper.trimEnd(StringHelper.trimStart(dr.getValue("Sender").toString(), '('), ')').split("[,]", -1)[1] : ""));
		}
		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 取消关注
	 
	 @return 
	*/
	public final String Focus_Delete() throws Exception {
		Dev2Interface.Flow_Focus(this.getWorkID());
		return "执行成功";
	}

		///#endregion 我的关注.

	/** 
	 方法
	 
	 @return 
	*/
	public final String HandlerMapExt() throws Exception {
		WF_CCForm wf = new WF_CCForm();
		return wf.HandlerMapExt();
	}
	/** 
	 最近发起的流程.
	 
	 @return 
	*/
	public final String StartEaryer_Init() throws Exception {
		//定义容器.
		DataSet ds = new DataSet();

		//获得能否发起的流程.
		String sql = "SELECT FK_Flow as No,FlowName as Name, FK_FlowSort,B.Name as FK_FlowSortText,B.Domain, COUNT(WorkID) as Num ";
		sql += " FROM WF_GenerWorkFlow A, WF_FlowSort B  ";
		sql += " WHERE Starter='" + WebUser.getNo() + "'  AND A.FK_FlowSort=B.No  ";
		sql += " GROUP BY FK_Flow, FlowName, FK_FlowSort, B.Name,B.Domain ";

		DataTable dtStart = DBAccess.RunSQLReturnTable(sql);
		dtStart.TableName = "Start";
		ds.Tables.add(dtStart);

		DataTable dtSort = new DataTable("Sort");
		dtSort.Columns.Add("No", String.class);
		dtSort.Columns.Add("Name", String.class);
		dtSort.Columns.Add("Domain", String.class);

		String nos = "";
		for (DataRow dr : dtStart.Rows)
		{
			String no = dr.getValue("FK_FlowSort").toString();
			if (nos.contains(no) == true)
			{
				continue;
			}

			String name = dr.getValue("FK_FlowSortText").toString();
			String domain = dr.getValue("Domain").toString();

			nos += "," + no;

			DataRow mydr = dtSort.NewRow();
			mydr.setValue(0, no);
			mydr.setValue(1, name);
			mydr.setValue(String.valueOf(2), domain);
			dtSort.Rows.add(mydr);
		}

		dtSort.TableName = "Sort";
		ds.Tables.add(dtSort);

		return bp.tools.Json.ToJson(ds);
	}
	/** 
	 获得发起列表 
	 
	 @return 
	*/
	public final String Start_Init() throws Exception {
		String json = "";

		WFEmp em = new WFEmp();
		em.setNo(WebUser.getNo());
		if (DataType.IsNullOrEmpty(em.getNo()) == true)
			return "err@登录信息丢失,请重新登录.";

		if (em.RetrieveFromDBSources() == 0)
		{
			em.setFK_Dept(WebUser.getFK_Dept());
			em.setName(bp.web.WebUser.getName());
			em.Insert();
		}

		json = DBAccess.GetBigTextFromDB("WF_Emp", "No", WebUser.getNo(), "StartFlows");
		if (DataType.IsNullOrEmpty(json) == false)
			return json;

		//定义容器.
		DataSet ds = new DataSet();

		//获得能否发起的流程.
		DataTable dtStart = Dev2Interface.DB_StarFlows(WebUser.getNo());
		dtStart.TableName = "Start";
		ds.Tables.add(dtStart);


			///#region 动态构造 流程类别.
		DataTable dtSort = new DataTable("Sort");
		dtSort.Columns.Add("No", String.class);
		dtSort.Columns.Add("Name", String.class);
		dtSort.Columns.Add("Domain", String.class);

		String nos = "";
		for (DataRow dr : dtStart.Rows)
		{
			String no = dr.getValue("FK_FlowSort").toString();
			if (nos.contains(no) == true)
			{
				continue;
			}

			String name = dr.getValue("FK_FlowSortText").toString();
			String domain = dr.getValue("Domain").toString();

			nos += "," + no;

			DataRow mydr = dtSort.NewRow();
			mydr.setValue(0, no);
			mydr.setValue(1, name);
			mydr.setValue(String.valueOf(2), domain);
			dtSort.Rows.add(mydr);
		}

		dtSort.TableName = "Sort";
		ds.Tables.add(dtSort);

			///#endregion 动态构造 流程类别.

		//返回组合
		json = bp.tools.Json.ToJson(ds);

		//把json存入数据表，避免下一次再取.
		if (dtStart.Rows.size() > 0)
		{
			DBAccess.SaveBigTextToDB(json, "WF_Emp", "No", WebUser.getNo(), "StartFlows");
		}

		//返回组合
		return json;
	}
	/** 
	 获得发起列表
	 
	 @return 
	*/
	public final String FlowSearch_Init() throws Exception {
		DataSet ds = new DataSet();

		//流程类别.
		FlowSorts fss = new FlowSorts();
		fss.RetrieveAll();

		DataTable dtSort = fss.ToDataTableField("Sort");
		dtSort.TableName = "Sort";
		ds.Tables.add(dtSort);

		//获得能否发起的流程.
		DataTable dtStart = DBAccess.RunSQLReturnTable("SELECT No,Name, FK_FlowSort FROM WF_Flow ORDER BY FK_FlowSort,Idx");
		dtStart.TableName = "Start";

		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
		{
			dtStart.Columns.get("NO").ColumnName = "No";
			dtStart.Columns.get("NAME").ColumnName = "Name";
			dtStart.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";
		}

		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
		{
			dtStart.Columns.get("no").ColumnName = "No";
			dtStart.Columns.get("name").ColumnName = "Name";
			dtStart.Columns.get("fk_flowsort").ColumnName = "FK_FlowSort";
		}

		ds.Tables.add(dtStart);



		//返回组合
		return bp.tools.Json.ToJson(ds);
	}

		///#region 获得列表.
	/** 
	 运行
	 
	 param UserNo 人员编号
	 param fk_flow 流程编号
	 @return 运行中的流程
	*/
	public final String Runing_Init() throws Exception {
		DataTable dt = null;
		boolean isContainFuture = this.GetRequestValBoolen("IsContainFuture");
		dt = Dev2Interface.DB_GenerRuning(WebUser.getNo(), this.getFK_Flow(), false, this.getDomain(), isContainFuture); //获得指定域的在途.
		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 我参与的已经完成的工作.
	 
	 @return 
	*/
	public final String Complete_Init() throws Exception {
		/* 如果不是删除流程注册表. */
		Paras ps = new Paras();
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		ps.SQL = "SELECT  * FROM WF_GenerWorkFlow  WHERE (Emps LIKE '%@" + WebUser.getNo() + "@%' OR Emps LIKE '%@" + WebUser.getNo() + ",%' OR Emps LIKE '%," + WebUser.getNo() + "@%') and WFState=" + WFState.Complete.getValue() + " ORDER BY  RDT DESC";
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		//添加oracle的处理
		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
		{
			dt.Columns.get("PRI").ColumnName = "PRI";
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("FID").ColumnName = "FID";
			dt.Columns.get("WFSTATE").ColumnName = "WFState";
			dt.Columns.get("WFSTA").ColumnName = "WFSta";
			dt.Columns.get("WEEKNUM").ColumnName = "WeekNum";
			dt.Columns.get("TSPAN").ColumnName = "TSpan";
			dt.Columns.get("TODOSTA").ColumnName = "TodoSta";
			dt.Columns.get("DEPTNAME").ColumnName = "DeptName";
			dt.Columns.get("TODOEMPSNUM").ColumnName = "TodoEmpsNum";
			dt.Columns.get("TODOEMPS").ColumnName = "TodoEmps";
			dt.Columns.get("TITLE").ColumnName = "Title";
			dt.Columns.get("TASKSTA").ColumnName = "TaskSta";
			dt.Columns.get("SYSTYPE").ColumnName = "SysType";
			dt.Columns.get("STARTERNAME").ColumnName = "StarterName";
			dt.Columns.get("STARTER").ColumnName = "Starter";
			dt.Columns.get("SENDER").ColumnName = "Sender";
			dt.Columns.get("SENDDT").ColumnName = "SendDT";
			dt.Columns.get("SDTOFNODE").ColumnName = "SDTOfNode";
			dt.Columns.get("SDTOFFLOW").ColumnName = "SDTOfFlow";
			dt.Columns.get("RDT").ColumnName = "RDT";
			dt.Columns.get("PWORKID").ColumnName = "PWorkID";
			dt.Columns.get("PFLOWNO").ColumnName = "PFlowNo";
			dt.Columns.get("PFID").ColumnName = "PFID";
			dt.Columns.get("PEMP").ColumnName = "PEmp";
			dt.Columns.get("NODENAME").ColumnName = "NodeName";


			dt.Columns.get("GUID").ColumnName = "Guid";
			dt.Columns.get("GUESTNO").ColumnName = "GuestNo";
			dt.Columns.get("GUESTNAME").ColumnName = "GuestName";
			dt.Columns.get("FLOWNOTE").ColumnName = "FlowNote";
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
			dt.Columns.get("FK_NY").ColumnName = "FK_NY";
			dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
			dt.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FK_DEPT").ColumnName = "FK_Dept";
			dt.Columns.get("EMPS").ColumnName = "Emps";
			dt.Columns.get("DOMAIN").ColumnName = "Domain";
			dt.Columns.get("DEPTNAME").ColumnName = "DeptName";
			dt.Columns.get("BILLNO").ColumnName = "BillNo";
		}

		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
		{
			dt.Columns.get("pri").ColumnName = "PRI";
			dt.Columns.get("workid").ColumnName = "WorkID";
			dt.Columns.get("fid").ColumnName = "FID";
			dt.Columns.get("wfstate").ColumnName = "WFState";
			dt.Columns.get("wfsta").ColumnName = "WFSta";
			dt.Columns.get("weeknum").ColumnName = "WeekNum";
			dt.Columns.get("tspan").ColumnName = "TSpan";
			dt.Columns.get("todosta").ColumnName = "TodoSta";
			dt.Columns.get("deptname").ColumnName = "DeptName";
			dt.Columns.get("todoempsnum").ColumnName = "TodoEmpsNum";
			dt.Columns.get("todoemps").ColumnName = "TodoEmps";
			dt.Columns.get("title").ColumnName = "Title";
			dt.Columns.get("tasksta").ColumnName = "TaskSta";
			dt.Columns.get("systype").ColumnName = "SysType";
			dt.Columns.get("startername").ColumnName = "StarterName";
			dt.Columns.get("starter").ColumnName = "Starter";
			dt.Columns.get("sender").ColumnName = "Sender";
			dt.Columns.get("senddt").ColumnName = "SendDT";
			dt.Columns.get("sdtofnode").ColumnName = "SDTOfNode";
			dt.Columns.get("sdtofflow").ColumnName = "SDTOfFlow";
			dt.Columns.get("rdt").ColumnName = "RDT";
			dt.Columns.get("pworkid").ColumnName = "PWorkID";
			dt.Columns.get("pflowno").ColumnName = "PFlowNo";
			dt.Columns.get("pfid").ColumnName = "PFID";
			dt.Columns.get("pemp").ColumnName = "PEmp";
			dt.Columns.get("nodename").ColumnName = "NodeName";
			dt.Columns.get("guid").ColumnName = "Guid";
			dt.Columns.get("guestno").ColumnName = "GuestNo";
			dt.Columns.get("guestname").ColumnName = "GuestName";
			dt.Columns.get("flownote").ColumnName = "FlowNote";
			dt.Columns.get("flowname").ColumnName = "FlowName";
			dt.Columns.get("fk_ny").ColumnName = "FK_NY";
			dt.Columns.get("fk_node").ColumnName = "FK_Node";
			dt.Columns.get("fk_flowsort").ColumnName = "FK_FlowSort";
			dt.Columns.get("fk_flow").ColumnName = "FK_Flow";
			dt.Columns.get("fk_dept").ColumnName = "FK_Dept";
			dt.Columns.get("emps").ColumnName = "Emps";
			dt.Columns.get("domain").ColumnName = "Domain";
			dt.Columns.get("deptname").ColumnName = "DeptName";
			dt.Columns.get("billno").ColumnName = "BillNo";
		}

		return bp.tools.Json.ToJson(dt);
	}

	/** 
	 执行撤销
	 
	 @return 
	*/
	public final String Runing_UnSend() throws Exception {
		try
		{
			//获取撤销到的节点
			int unSendToNode = this.GetRequestValInt("UnSendToNode");
			return Dev2Interface.Flow_DoUnSend(this.getFK_Flow(), this.getWorkID(), unSendToNode, this.getFID());
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 撤销发送
	 
	 @return 
	*/
	public final String Runing_UnSendCC() throws Exception {
		String checkboxs = GetRequestVal("CCPKs");
		CCLists ccs = new CCLists();
		ccs.RetrieveIn("MyPK", "'" + checkboxs.replace(",", "','") + "'");
		ccs.Delete();
		return "撤销抄送成功";
	}
	/** 
	 执行催办
	 
	 @return 
	*/
	public final String Runing_Press() throws Exception {
		try
		{
			return Dev2Interface.Flow_DoPress(this.getWorkID(), this.GetRequestVal("Msg"), false);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 打开表单
	 
	 @return 
	*/
	public final String Runing_OpenFrm() throws Exception {
		int nodeID = this.getFK_Node();
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		if (nodeID == 0)
		{
			gwf = new GenerWorkFlow(this.getWorkID());
			nodeID = gwf.getFK_Node();
		}

		String appPath = Glo.getCCFlowAppPath();
		Node nd = null;
		Track tk = new Track();
		tk.FK_Flow = this.getFK_Flow();


		tk.setWorkID(this.getWorkID());
		if (this.getMyPK() != null)
		{
			tk = new Track(this.getFK_Flow(), this.getMyPK());
			nd = new Node(tk.getNDFrom());
		}
		else
		{
			nd = new Node(nodeID);
		}

		Flow fl = new Flow(this.getFK_Flow());
		long workid = 0;
		if (nd.getHisRunModel() == RunModel.SubThread)
		{
			if (tk.getFID() == 0)
			{
				if (gwf == null)
				{
					gwf = new GenerWorkFlow(this.getWorkID());
				}

				workid = gwf.getFID();
			}
			else
			{
				workid = tk.getFID();
			}
		}
		else
		{
			workid = tk.getWorkID();
		}

		long fid = this.getFID();
		if (this.getFID() == 0)
		{
			fid = tk.getFID();
		}

		if (fid > 0)
		{
			workid = fid;
		}

		if (workid == 0)
		{
			workid = this.getWorkID();
		}

		String urlExt = "";

		// gwf.atPara.HisHT

		DataTable ndrpt = DBAccess.RunSQLReturnTable("SELECT PFlowNo,PWorkID FROM " + fl.getPTable() + " WHERE OID=" + workid);
		if (ndrpt.Rows.size() == 0)
		{
			urlExt = "&PFlowNo=0&PWorkID=0&IsToobar=0&IsHidden=true&CCSta=" + this.GetRequestValInt("CCSta");
		}
		else
		{
			urlExt = "&PFlowNo=" + ndrpt.Rows.get(0).getValue("PFlowNo") + "&PWorkID=" + ndrpt.Rows.get(0).getValue("PWorkID") + "&IsToobar=0&IsHidden=true&CCSta=" + this.GetRequestValInt("CCSta");
		}

		urlExt += "&From=CCFlow&TruckKey=" + tk.GetValStrByKey("MyPK") + "&DoType=" + this.getDoType() + "&UserNo=" + WebUser.getNo() != null ? WebUser.getNo() : "" + "&Token=" + WebUser.getToken() != null ? WebUser.getToken() : "";

		urlExt = urlExt.replace("PFlowNo=null", "");
		urlExt = urlExt.replace("PWorkID=null", "");

		if (!gwf.getatPara().getHisHT().isEmpty())
		{
			for (String item : gwf.getatPara().getHisHT().keySet())
			{
				urlExt += "&" + item + "=" + gwf.getatPara().getHisHT().get(item);
			}
		}


		if (nd.getHisFormType() == NodeFormType.SDKForm || nd.getHisFormType() == NodeFormType.SelfForm)
		{
			//added by liuxc,2016-01-25
			if (nd.getFormUrl().contains("?"))
			{
				return "urlForm@" + nd.getFormUrl() + "&IsReadonly=1&WorkID=" + workid + "&FK_Node=" + nd.getNodeID() + "&FK_Flow=" + nd.getFK_Flow() + "&FID=" + fid + urlExt;
			}

			return "urlForm@" + nd.getFormUrl() + "?IsReadonly=1&WorkID=" + workid + "&FK_Node=" + nd.getNodeID() + "&FK_Flow=" + nd.getFK_Flow() + "&FID=" + fid + urlExt;
		}

		if (nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.SheetAutoTree)
		{
			if (Glo.getPlatform() == Platform.CCFlow)
			{
				return "url@/WF/MyViewTree.htm?3=4&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&OID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + nd.getNodeID() + "&PK=OID&PKVal=" + this.getWorkID() + "&IsEdit=0&IsLoadData=0&IsReadonly=1" + urlExt;
			}
			else
			{
				return "url@/jflow-web/MyViewTree.htm?3=4&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&OID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + nd.getNodeID() + "&PK=OID&PKVal=" + this.getWorkID() + "&IsEdit=0&IsLoadData=0&IsReadonly=1" + urlExt;
			}
		}

		Work wk = nd.getHisWork();
		wk.setOID(workid);
		if (wk.RetrieveFromDBSources() == 0)
		{
			GERpt rtp = nd.getHisFlow().getHisGERpt();
			rtp.setOID(workid);
			if (rtp.RetrieveFromDBSources() == 0)
			{
				String info = "打开(" + nd.getName() + ")错误";
				info += "当前的节点数据已经被删除！！！<br> 造成此问题出现的原因如下。";
				info += "1、当前节点数据被非法删除。";
				info += "2、节点数据是退回人与被退回人中间的节点，这部分节点数据查看不支持。";
				info += "技术信息:表" + wk.getEnMap().getPhysicsTable() + " WorkID=" + workid;
				return "err@" + info;
			}
			wk.setRow(rtp.getRow());
		}

		if (nd.getHisFlow().isMD5() && wk.IsPassCheckMD5() == false)
		{
			String err = "打开(" + nd.getName() + ")错误";
			err += "当前的节点数据已经被篡改，请报告管理员。";
			return "err@" + err;
		}
		nd.WorkID = this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.
		if (nd.getHisFormType() == NodeFormType.Develop)
		{
			MapData md = new MapData(nd.getNodeFrmID());
			if (md.getHisFrmType() != FrmType.Develop)
			{
				md.setHisFrmType( FrmType.Develop);
				md.Update();
			}
		}
		else if (nd.getHisFormType() == NodeFormType.FoolForm)
		{
			nd.WorkID = this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.
			MapData md = new MapData(nd.getNodeFrmID());
			if (md.getHisFrmType() != FrmType.FoolForm)
			{
				md.setHisFrmType( FrmType.FoolForm);
				md.Update();
			}
		}
		String endUrl = "";

		if (!gwf.getatPara().getHisHT().isEmpty())
		{
			for (String item : gwf.getatPara().getHisHT().keySet())
			{
				endUrl += "&" + item + "=" + gwf.getatPara().getHisHT().get(item);
			}
		}

		//加入是累加表单的标志，目的是让附件可以看到.

		if (nd.getHisFormType() == NodeFormType.FoolTruck)
		{
			endUrl = "&FormType=10&FromWorkOpt=" + this.GetRequestVal("FromWorkOpt");
		}

		//return "url@./CCForm/Frm.htm?FK_MapData=" + nd.NodeFrmID + "&OID=" + wk.OID + "&FK_Flow=" + this.FK_Flow + "&FK_Node=" + nd.NodeID + "&PK=OID&PKVal=" + wk.OID + "&IsEdit=0&IsLoadData=0&IsReadonly=1" + endUrl+"&CCSta="+this.GetRequestValInt("CCSta");

		if (Glo.getPlatform() == Platform.CCFlow)
		{
			return "url@/WF/MyView.htm?FK_MapData=" + nd.getNodeFrmID() + "&OID=" + wk.getOID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + nd.getNodeID() + "&PK=OID&PKVal=" + wk.getOID() + "&IsEdit=0&IsLoadData=0&IsReadonly=1" + endUrl + "&CCSta=" + this.GetRequestValInt("CCSta");
		}
		else
		{
			return "url@/jflow-web/WF/MyView.htm?FK_MapData=" + nd.getNodeFrmID() + "&OID=" + wk.getOID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + nd.getNodeID() + "&PK=OID&PKVal=" + wk.getOID() + "&IsEdit=0&IsLoadData=0&IsReadonly=1" + endUrl + "&CCSta=" + this.GetRequestValInt("CCSta");
		}
	}
	/** 
	 草稿
	 
	 @return 
	*/
	public final String Draft_Init() throws Exception {
		DataTable dt = null;
		String domain = this.GetRequestVal("Domain");
		String flowNo = this.GetRequestVal("FK_Flow");

		dt = Dev2Interface.DB_GenerDraftDataTable(flowNo, domain);
		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 删除草稿.
	 
	 @return 
	*/
	public final String Draft_Delete() throws Exception {
		return Dev2Interface.Flow_DoDeleteDraft(this.getFK_Flow(), this.getWorkID(), false);
	}
	/** 
	 获得会签列表
	 
	 @return 
	*/
	public final String HuiQianList_Init() throws Exception {
		String sql = "SELECT A.WorkID, A.Title,A.FK_Flow, A.FlowName, A.Starter, A.StarterName, A.Sender, A.Sender,A.FK_Node,A.NodeName,A.SDTOfNode,A.TodoEmps";
		sql += " FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B ";
		sql += " WHERE A.WorkID=B.WorkID and a.FK_Node=b.FK_Node ";
		sql += " AND (B.IsPass=90 OR A.AtPara LIKE '%HuiQianZhuChiRen=" + WebUser.getNo() + "%') ";
		sql += " AND B.FK_Emp=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp";

		Paras ps = new Paras();
		ps.Add("FK_Emp", WebUser.getNo(), false);
		ps.SQL = sql;
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
		{
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("TITLE").ColumnName = "Title";
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";

			dt.Columns.get("STARTER").ColumnName = "Starter";
			dt.Columns.get("STARTERNAME").ColumnName = "StarterName";

			dt.Columns.get("SENDER").ColumnName = "Sender";
			dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
			dt.Columns.get("NODENAME").ColumnName = "NodeName";
			dt.Columns.get("SDTOFNODE").ColumnName = "SDTOfNode";
			dt.Columns.get("TODOEMPS").ColumnName = "TodoEmps";
		}
		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
		{
			dt.Columns.get("workid").ColumnName = "WorkID";
			dt.Columns.get("title").ColumnName = "Title";
			dt.Columns.get("fk_flow").ColumnName = "FK_Flow";
			dt.Columns.get("flowname").ColumnName = "FlowName";

			dt.Columns.get("starter").ColumnName = "Starter";
			dt.Columns.get("startername").ColumnName = "StarterName";

			dt.Columns.get("sender").ColumnName = "Sender";
			dt.Columns.get("fk_node").ColumnName = "FK_Node";
			dt.Columns.get("nodename").ColumnName = "NodeName";
			dt.Columns.get("sdtofnode").ColumnName = "SDTOfNode";
			dt.Columns.get("todoemps").ColumnName = "TodoEmps";
		}
		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 协作模式待办
	 
	 @return 
	*/
	public final String TeamupList_Init() throws Exception {
		String sql = "SELECT A.WorkID, A.Title,A.FK_Flow, A.FlowName, A.Starter, A.StarterName, A.Sender, A.Sender,A.FK_Node,A.NodeName,A.SDTOfNode,A.TodoEmps";
		sql += " FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B, WF_Node C ";
		sql += " WHERE A.WorkID=B.WorkID and a.FK_Node=b.FK_Node AND A.FK_Node=C.NodeID AND C.TodolistModel=1 ";
		sql += " AND B.IsPass=0 AND B.FK_Emp=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
		//   sql += " AND B.IsPass=0 AND B.FK_Emp=" + bp.difference.SystemConfig.getAppCenterDBVarStr() + "FK_Emp";

		Paras ps = new Paras();
		ps.Add("FK_Emp", WebUser.getNo(), false);
		ps.SQL = sql;
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
		{
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("TITLE").ColumnName = "Title";
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";

			dt.Columns.get("STARTER").ColumnName = "Starter";
			dt.Columns.get("STARTERNAME").ColumnName = "StarterName";

			dt.Columns.get("SENDER").ColumnName = "Sender";
			dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
			dt.Columns.get("NODENAME").ColumnName = "NodeName";
			dt.Columns.get("SDTOFNODE").ColumnName = "SDTOfNode";
			dt.Columns.get("TODOEMPS").ColumnName = "TodoEmps";
		}
		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
		{
			dt.Columns.get("workid").ColumnName = "WorkID";
			dt.Columns.get("title").ColumnName = "Title";
			dt.Columns.get("fk_flow").ColumnName = "FK_Flow";
			dt.Columns.get("flowname").ColumnName = "FlowName";

			dt.Columns.get("starter").ColumnName = "Starter";
			dt.Columns.get("startername").ColumnName = "StarterName";

			dt.Columns.get("sender").ColumnName = "Sender";
			dt.Columns.get("fk_node").ColumnName = "FK_Node";
			dt.Columns.get("nodename").ColumnName = "NodeName";
			dt.Columns.get("sdtofnode").ColumnName = "SDTOfNode";
			dt.Columns.get("todoemps").ColumnName = "TodoEmps";
		}
		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 获得加签人的待办
	 @LQ 
	 
	 @return 
	*/
	public final String HuiQianAdderList_Init() throws Exception {
		String sql = "SELECT A.WorkID, A.Title,A.FK_Flow, A.FlowName, A.Starter, A.StarterName, A.Sender, A.Sender,A.FK_Node,A.NodeName,A.SDTOfNode,A.TodoEmps";
		sql += " FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B, WF_Node C ";
		sql += " WHERE A.WorkID=B.WorkID and a.FK_Node=b.FK_Node AND B.IsPass=0 AND B.FK_Emp=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
		sql += " AND B.AtPara LIKE '%IsHuiQian=1%' ";
		sql += " AND A.FK_Node=C.NodeID ";
		sql += " AND C.TodolistModel= 4";

		Paras ps = new Paras();
		ps.Add("FK_Emp", WebUser.getNo(), false);
		ps.SQL = sql;
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
		{
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("TITLE").ColumnName = "Title";
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";

			dt.Columns.get("STARTER").ColumnName = "Starter";
			dt.Columns.get("STARTERNAME").ColumnName = "StarterName";

			dt.Columns.get("SENDER").ColumnName = "Sender";
			dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
			dt.Columns.get("NODENAME").ColumnName = "NodeName";
			dt.Columns.get("SDTOFNODE").ColumnName = "SDTOfNode";
			dt.Columns.get("TODOEMPS").ColumnName = "TodoEmps";
		}

		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
		{
			dt.Columns.get("workid").ColumnName = "WorkID";
			dt.Columns.get("title").ColumnName = "Title";
			dt.Columns.get("fk_flow").ColumnName = "FK_Flow";
			dt.Columns.get("flowname").ColumnName = "FlowName";

			dt.Columns.get("starter").ColumnName = "Starter";
			dt.Columns.get("startername").ColumnName = "StarterName";

			dt.Columns.get("sender").ColumnName = "Sender";
			dt.Columns.get("fk_node").ColumnName = "FK_Node";
			dt.Columns.get("nodename").ColumnName = "NodeName";
			dt.Columns.get("sdtofnode").ColumnName = "SDTOfNode";
			dt.Columns.get("todoemps").ColumnName = "TodoEmps";
		}
		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 初始化待办.
	 
	 @return 
	*/
	public final String Todolist_Init() throws Exception {
		String wfState = this.GetRequestVal("ShowWhat"); //比如：WFSTate=1,状态.

		DataTable dt = Dev2Interface.DB_GenerEmpWorksOfDataTable(WebUser.getNo(), this.getFK_Node(), wfState, this.getDomain(), this.getFK_Flow());
		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 获得授权人的待办.
	 
	 @return 
	*/
	public final String Todolist_Author() throws Exception {
		DataTable dt = null;
		dt = Dev2Interface.DB_GenerEmpWorksOfDataTable(this.getNo(), this.getFK_Node());

		//转化大写的toJson.
		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 初始化
	 
	 @return 
	*/
	public final String TodolistOfAuth_Init() throws Exception {
		return "err@尚未重构完成.";

		//DataTable dt = null;
		//foreach (BP.WF.Port.WFEmp item in ems)
		//{
		//    if (dt == null)
		//    {
		//        dt = BP.WF.Dev2Interface.DB_GenerEmpWorksOfDataTable(item.No, null);
		//    }
		//    else
		//    {
		//    }
		//}

		//    return BP.Tools.Json.DataTableToJson(dt, false);

		//string fk_emp = this.FK_Emp;
		//if (fk_emp == null)
		//{
		//    //移除不需要前台看到的数据.
		//    DataTable dt = ems.ToDataTableField();
		//    dt.Columns.Remove("Token");
		//    dt.Columns.Remove("Stas");
		//    dt.Columns.Remove("Depts");
		//    dt.Columns.Remove("Msg");
		//    return BP.Tools.Json.DataTableToJson(dt, false);
		//}
	}
	/** 
	 获得挂起列表
	 
	 @return 
	*/
	public final String HungupList_Init() throws Exception {
		return Dev2Interface.DB_GenerHungupList();
	}
	/** 
	 同意挂起
	 
	 @return 
	*/
	public final String HungupList_Agree() throws Exception {
		return Dev2Interface.Node_HungupWorkAgree(this.getWorkID());
	}
	/** 
	 拒绝挂起
	 
	 @return 
	*/
	public final String HungupList_Reject() throws Exception {
		return Dev2Interface.Node_HungupWorkReject(this.getWorkID(), this.GetRequestVal("Msg"));
	}

	public final String FutureTodolist_Init() throws Exception {
		DataTable dt = null;
		dt = Dev2Interface.DB_FutureTodolist();

		//转化大写的toJson.
		return bp.tools.Json.ToJson(dt);
	}



		///#endregion 获得列表.



		///#region 共享任务池.
	/** 
	 初始化共享任务
	 
	 @return 
	*/
	public final String TaskPoolSharing_Init() throws Exception {
		DataTable dt = Dev2Interface.DB_TaskPool();

		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 申请任务.
	 
	 @return 
	*/
	public final String TaskPoolSharing_Apply() throws Exception {
		boolean b = Dev2Interface.Node_TaskPoolTakebackOne(this.getWorkID());
		if (b == true)
		{
			return "申请成功.";
		}
		else
		{
			return "err@申请失败...";
		}
	}
	/** 
	 我申请下来的任务
	 
	 @return 
	*/
	public final String TaskPoolApply_Init() throws Exception {
		DataTable dt = Dev2Interface.DB_TaskPoolOfMyApply();

		return bp.tools.Json.ToJson(dt);
	}
	public final String TaskPoolApply_PutOne() throws Exception {
		Dev2Interface.Node_TaskPoolPutOne(this.getWorkID());
		return "放入成功,其他的同事可以看到这件工作.您可以在任务池里看到它并重新申请下来.";
	}

		///#endregion


		///#region 登录相关.
	/** 
	 返回当前会话信息.
	 1. token 用于ccflow内部访问.
	 2. sid 用于集成.
	 
	 @return 
	*/
	public final String Login_Init() throws Exception {

			///#region 检查一下是否有 token、sid ? 如果有就直接登录.
		String token = this.GetRequestVal("Token");
		if (DataType.IsNullOrEmpty(token) == false)
		{
			Dev2Interface.Port_LoginByToken(token);
			return "url@Home.htm?Token=" + token;
		}

		String sid = this.GetRequestVal("Token");
		if (DataType.IsNullOrEmpty(sid) == false)
		{
			Dev2Interface.Port_LoginByToken(sid);
			return "url@Home.htm?Token=" + sid;
		}

			///#endregion 检查一下是否有token ?

		Hashtable ht = new Hashtable();
		if (WebUser.getNoOfRel() == null)
		{
			ht.put("UserNo", "");
		}
		else
		{
			ht.put("UserNo", WebUser.getNo());
		}
		return bp.tools.Json.ToJson(ht);
	}
	/** 
	 执行登录.
	 
	 @return 
	*/
	public final String LoginSubmit() throws Exception {
		bp.port.Emp emp = new bp.port.Emp();
		emp.setUserID (this.GetValFromFrmByKey("TB_No"));

		if (emp.RetrieveFromDBSources() == 0)
		{
			return "err@用户名或密码错误.";
		}

		String pass = this.GetValFromFrmByKey("TB_PW");
		if (emp.getPass().equals(pass) == false)
		{
			return "err@用户名或密码错误.";
		}

		//让其登录.
		Dev2Interface.Port_Login(emp.getUserID());

		return "登录成功.";
	}
	/** 
	 执行授权登录
	 
	 @return 
	*/
	public final String AuthorList_LoginAs() throws Exception {
		WFEmp wfemp = new WFEmp(this.getNo());

		//if (wfemp.AuthorIsOK == false)
		//   return "err@授权登录失败！";

		bp.port.Emp emp1 = new bp.port.Emp(this.getNo());
		WebUser.SignInOfGener(emp1, "CH", false, false, WebUser.getNo(), WebUser.getName());

		return "授权登录成功！";
	}
	/** 
	 批处理审批
	 
	 @return 
	*/
	public final String Batch_Init() throws Exception {
		String sql = "SELECT a.NodeID, a.Name,a.FlowName, a.BatchRole, COUNT(WorkID) AS NUM  FROM  WF_Node a, WF_EmpWorks b ";
		sql += " WHERE A.NodeID=b.FK_Node AND B.FK_Emp='" + WebUser.getNo() + "' AND a.BatchRole!=0 ";
		sql += " AND b.WFState!=7 GROUP BY A.NodeID, a.Name,a.FlowName,a.BatchRole ";
		//sql += " AND b.WFState NOT IN (7) AND a.BatchRole!=0 GROUP BY A.NodeID, a.Name,a.FlowName,a.BatchRole ";

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 授权列表
	 
	 @return 
	*/
	public final String AuthorTodolist_Init() throws Exception {

		return "";
	}

	/** 
	 授权列表
	 
	 @return 
	*/
	public final String AuthorTodolist_Todolist() throws Exception {

		return "";
	}
	/** 
	 退出登录
	 
	 param UserNo
	 param Author
	 @return 
	*/
	public final String AuthExitAndLogin(String UserNo, String Author)throws Exception
	{
		String msg = "suess@退出成功！";
		try
		{
			bp.port.Emp emp = new bp.port.Emp(UserNo);
			//首先退出
			WebUser.Exit();
			//再进行登录
			bp.port.Emp emp1 = new bp.port.Emp(Author);
			WebUser.SignInOfGener(emp1, "CH", false, false, null, null);
		}
		catch (RuntimeException ex)
		{
			msg = "err@退出时发生错误:" + ex.getMessage();
		}
		return msg;
	}
	/** 
	 获取授权人列表
	 
	 @return 
	*/
	public final String AuthorList_Init() throws Exception {
		Paras ps = new Paras();
		ps.SQL = "SELECT No,Name,AuthorDate FROM WF_Emp WHERE AUTHOR=" + SystemConfig.getAppCenterDBVarStr() + "AUTHOR";
		ps.Add("AUTHOR", WebUser.getNo(), false);
		DataTable dt = DBAccess.RunSQLReturnTable(ps);

		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("AUTHORDATE").ColumnName = "AuthorDate";
		}
		if (SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.Lowercase)
		{
			dt.Columns.get("no").ColumnName = "No";
			dt.Columns.get("name").ColumnName = "Name";
			dt.Columns.get("authordate").ColumnName = "AuthorDate";
		}
		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 当前登陆人是否有授权
	 
	 @return 
	*/
	public final String IsHaveAuthor() throws Exception {
		Auths ens = new Auths();
		ens.Retrieve(AuthAttr.Auther, WebUser.getNo(), null);
		if (ens.size() > 0)
		{
			return "suess@有授权";
		}
		return "err@没有授权";
	}
	/** 
	 退出.
	 
	 @return 
	*/
	public final String LoginExit() throws Exception {
		Dev2Interface.Port_SigOut();
		return null;
	}
	/** 
	 授权退出.
	 
	 @return 
	*/
	public final String AuthExit() throws Exception {
		return this.AuthExitAndLogin(this.getNo(), WebUser.getAuth());
	}

		///#endregion 登录相关.

	/** 
	 获得抄送列表
	 
	 @return 
	*/
	public final String CC_Init() throws Exception {
		String sta = this.GetRequestVal("Sta");
		if (sta == null || sta.equals(""))
		{
			sta = "-1";
		}

		int pageSize = 6; // int.Parse(pageSizeStr);

		String pageIdxStr = this.GetRequestVal("PageIdx");
		if (pageIdxStr == null)
		{
			pageIdxStr = "1";
		}

		int pageIdx = Integer.parseInt(pageIdxStr);

		//实体查询.
		//BP.WF.SMSs ss = new BP.WF.SMSs();
		//bp.en.QueryObject qo = new bp.en.QueryObject(ss);

		DataTable dt = null;
		if (sta.equals("-1"))
		{
			dt = Dev2Interface.DB_CCList("");
		}

		if (sta.equals("0"))
		{
			dt = Dev2Interface.DB_CCList_UnRead(WebUser.getNo());
		}

		if (sta.equals("1"))
		{
			dt = Dev2Interface.DB_CCList_Read();
		}

		if (sta.equals("2"))
		{
			dt = Dev2Interface.DB_CCList_Delete(WebUser.getNo());
		}

		//int allNum = qo.GetCount();
		//qo.DoQuery(BP.WF.SMSAttr.MyPK, pageSize, pageIdx);

		return bp.tools.Json.ToJson(dt);
	}

	/** 
	 我的流程的查询条件
	 
	 @return 
	*/
	public final String Search_Conds() throws Exception {
		if (WebUser.getNo() == null)
		{
			throw new RuntimeException("err@登录信息丢失.");
		}
		DataSet ds = new DataSet();
		String tSpan = this.GetRequestVal("TSpan");
		String keyWord = this.GetRequestVal("KeyWord");


			///#region 1、获取时间段枚举/总数.
		SysEnums ses = new SysEnums("TSpan");
		DataTable dtTSpan = ses.ToDataTableField("dt");
		dtTSpan.TableName = "TSpan";
		ds.Tables.add(dtTSpan);
		String sqlWhere = "";
		if (DataType.IsNullOrEmpty(keyWord) == false)
		{
			sqlWhere += " AND Title like '%" + keyWord + "%' ";
		}
		if (DataType.IsNullOrEmpty(this.getFK_Flow()) == false)
		{
			sqlWhere += " AND FK_Flow='" + this.getFK_Flow() + "'";
		}
		String sql = " SELECT  TSpan as No, COUNT(WorkID) as Num FROM WF_GenerWorkFlow WHERE (Emps LIKE '%" + WebUser.getNo() + "%' OR Starter='" + WebUser.getNo() + "') AND FID = 0 AND WFState > 1 " + sqlWhere + " GROUP BY TSpan ";

		DataTable dtTSpanNum = DBAccess.RunSQLReturnTable(sql);
		for (DataRow drEnum : dtTSpan.Rows)
		{
			String no = drEnum.get("IntKey").toString();
			for (DataRow dr : dtTSpanNum.Rows)
			{
				if (dr.getValue("No").toString().equals(no))
				{
					drEnum.setValue("Lab", drEnum.get("Lab").toString() + "(" + dr.getValue("Num") + ")");
					break;
				}
			}
		}

			///#endregion


			///#region 2、处理流程类别列表.
		sqlWhere = "";
		if (DataType.IsNullOrEmpty(keyWord) == false)
		{
			sqlWhere += " AND Title like '%" + keyWord + "%' ";
		}
		if (!tSpan.equals("-1"))
		{
			sqlWhere += " AND TSpan=" + tSpan;
		}

		sql = "SELECT  FK_Flow as No, FlowName as Name, COUNT(WorkID) as Num FROM WF_GenerWorkFlow WHERE (Emps LIKE '%" + WebUser.getNo() + "%' OR TodoEmps LIKE '%" + WebUser.getNo() + ",%' OR Starter='" + WebUser.getNo() + "')  AND WFState > 1 AND FID = 0 " + sqlWhere + " GROUP BY FK_Flow, FlowName";

		DataTable dtFlows = DBAccess.RunSQLReturnTable(sql);
		if (SystemConfig.AppCenterDBFieldCaseModel() != FieldCaseModel.None)
		{
			dtFlows.Columns.get(0).setColumnName("No");
			dtFlows.Columns.get(1).setColumnName("Name");
			dtFlows.Columns.get(2).setColumnName("Num");
		}


		dtFlows.TableName = "Flows";
		ds.Tables.add(dtFlows);

			///#endregion
		return bp.tools.Json.ToJson(ds);
	}
	/** 
	 查询总条数
	 
	 @return 
	*/
	public final String Search_Count() throws Exception {
		if (WebUser.getNo() == null)
		{
			return "";
		}
		String tSpan = this.GetRequestVal("TSpan");
		if (DataType.IsNullOrEmpty(tSpan) == true)
		{
			tSpan = null;
		}
		//查询关键字
		String keyWord = this.GetRequestVal("KeyWord");
		if (DataType.IsNullOrEmpty(keyWord) == true)
		{
			keyWord = null;
		}

		String sqlWhere = "(Emps LIKE '%" + WebUser.getNo() + "%' OR TodoEmps LIKE '%" + WebUser.getNo() + "%' OR Starter = '" + WebUser.getNo() + "') AND FID = 0 AND WFState > 1 ";
		if (!tSpan.equals("-1"))
		{
			sqlWhere += "AND TSpan = '" + tSpan + "' ";
		}
		if (keyWord != null)
		{
			sqlWhere += "AND Title like '%" + keyWord + "%' ";
		}
		if (this.getFK_Flow() != null)
		{
			sqlWhere += "AND FK_Flow = '" + this.getFK_Flow() + "' ";
		}

		//获取总条数
		String totalNumSql = "SELECT count(*) from WF_GenerWorkFlow where " + sqlWhere;
		int totalNum = DBAccess.RunSQLReturnValInt(totalNumSql);
		return String.valueOf(totalNum);
	}
	/** 
	 初始化
	 
	 @return 
	*/
	public final String Search_Data() throws Exception {
		if (WebUser.getNo() == null)
		{
			return "";
		}
		String sql = "";

		String tSpan = this.GetRequestVal("TSpan");
		if (DataType.IsNullOrEmpty(tSpan) == true)
		{
			tSpan = null;
		}
		//查询关键字
		String keyWord = this.GetRequestVal("KeyWord");
		if (DataType.IsNullOrEmpty(keyWord) == true)
		{
			keyWord = null;
		}



			///#region 处理查询
		//当前页
		int pageIdx = this.getPageIdx();
		//每页条数
		int pageSize = this.getPageSize();

		int startIndex = (pageIdx - 1) * pageSize;
		int num = pageSize * (pageIdx - 1);
		String sqlWhere = "(Emps LIKE '%" + WebUser.getNo() + "%' OR TodoEmps LIKE '%" + WebUser.getNo() + "%' OR Starter = '" + WebUser.getNo() + "') AND FID = 0 AND WFState > 1 ";
		if (!tSpan.equals("-1"))
		{
			sqlWhere += "AND TSpan = '" + tSpan + "' ";
		}
		if (keyWord != null)
		{
			sqlWhere += "AND Title like '%" + keyWord + "%' ";
		}
		if (this.getFK_Flow() != null)
		{
			sqlWhere += "AND FK_Flow = '" + this.getFK_Flow() + "' ";
		}

		sqlWhere += " ORDER BY RDT DESC ";
		if (SystemConfig.getAppCenterDBType( ) == DBType.Oracle || SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR3 ||SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR6)
		{
			sql = "SELECT NVL(WorkID, 0) WorkID,NVL(FID, 0) FID ,FK_Flow,FlowName,Title, NVL(WFSta, 0) WFSta,WFState,  Starter, StarterName,Sender,NVL(RDT, '2018-05-04 19:29') RDT,NVL(FK_Node, 0) FK_Node,NodeName, TodoEmps " + "FROM (select A.*, rownum r from (select * from WF_GenerWorkFlow where " + sqlWhere + ") A) where r between " + (pageIdx * pageSize - pageSize + 1) + " and " + (pageIdx * pageSize);
		}
		else if (SystemConfig.getAppCenterDBType( ) == DBType.MSSQL)
		{
			sql = "SELECT  TOP " + pageSize + " ISNULL(WorkID, 0) WorkID,ISNULL(FID, 0) FID ,FK_Flow,FlowName,Title, ISNULL(WFSta, 0) WFSta,WFState,  Starter, StarterName,Sender,ISNULL(RDT, '2018-05-04 19:29') RDT,ISNULL(FK_Node, 0) FK_Node,NodeName, TodoEmps FROM WF_GenerWorkFlow " + "where WorkID not in (select top(" + num + ") WorkID from WF_GenerWorkFlow where " + sqlWhere + ") AND" + sqlWhere;
		}
		else if (SystemConfig.getAppCenterDBType( ) == DBType.MySQL)
		{
			sql = "SELECT IFNULL(WorkID, 0) WorkID,IFNULL(FID, 0) FID ,FK_Flow,FlowName,Title, IFNULL(WFSta, 0) WFSta,WFState,  Starter, StarterName,Sender,IFNULL(RDT, '2018-05-04 19:29') RDT,IFNULL(FK_Node, 0) FK_Node,NodeName, TodoEmps FROM WF_GenerWorkFlow where (1=1) AND " + sqlWhere + " LIMIT " + startIndex + "," + pageSize;
		}
		else if (SystemConfig.getAppCenterDBType( ) == DBType.PostgreSQL || SystemConfig.getAppCenterDBType( ) == DBType.UX)
		{
			sql = "SELECT COALESCE(WorkID, 0) WorkID,COALESCE(FID, 0) FID ,FK_Flow,FlowName,Title, COALESCE(WFSta, 0) WFSta,WFState,  Starter, StarterName,Sender,COALESCE(RDT, '2018-05-04 19:29') RDT,COALESCE(FK_Node, 0) FK_Node,NodeName, TodoEmps FROM WF_GenerWorkFlow where (1=1) AND " + sqlWhere + " LIMIT " + pageSize + " offset " + startIndex;
		}
		DataTable mydt = DBAccess.RunSQLReturnTable(sql);

		if (SystemConfig.AppCenterDBFieldCaseModel() != FieldCaseModel.None)
		{
			mydt.Columns.get(0).setColumnName("WorkID");
			mydt.Columns.get(1).setColumnName("FID");
			mydt.Columns.get(2).setColumnName("FK_Flow");
			mydt.Columns.get(3).setColumnName("FlowName");
			mydt.Columns.get(4).setColumnName("Title");
			mydt.Columns.get(5).setColumnName("WFSta");
			mydt.Columns.get(6).setColumnName("WFState");
			mydt.Columns.get(7).setColumnName("Starter");
			mydt.Columns.get(8).setColumnName("StarterName");
			mydt.Columns.get(9).setColumnName("Sender");
			mydt.Columns.get(10).setColumnName("RDT");
			mydt.Columns.get(11).setColumnName("FK_Node");
			mydt.Columns.get(12).setColumnName("NodeName");
			mydt.Columns.get(13).setColumnName("TodoEmps");


		}
		mydt.TableName = "WF_GenerWorkFlow";
		if (mydt != null)
		{
			mydt.Columns.Add("TDTime");
			for (DataRow dr : mydt.Rows)
			{
				dr.setValue("TDTime", GetTraceNewTime(dr.getValue("FK_Flow").toString(), Integer.parseInt(dr.getValue("WorkID").toString()), Integer.parseInt(dr.getValue("FID").toString())));
			}
		}

			///#endregion

		return bp.tools.Json.ToJson(mydt);
	}
	public static String GetTraceNewTime(String fk_flow, long workid, long fid) throws Exception {

			///#region 获取track数据.
		String sqlOfWhere2 = "";
		String sqlOfWhere1 = "";
		String dbStr = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		if (fid == 0)
		{
			sqlOfWhere1 = " WHERE (FID=" + dbStr + "WorkID11 OR WorkID=" + dbStr + "WorkID12 )  ";
			ps.Add("WorkID11", workid);
			ps.Add("WorkID12", workid);
		}
		else
		{
			sqlOfWhere1 = " WHERE (FID=" + dbStr + "FID11 OR WorkID=" + dbStr + "FID12 ) ";
			ps.Add("FID11", fid);
			ps.Add("FID12", fid);
		}

		String sql = "";
		sql = "SELECT MAX(RDT) FROM ND" + Integer.parseInt(fk_flow) + "Track " + sqlOfWhere1;
		sql = "SELECT RDT FROM  ND" + Integer.parseInt(fk_flow) + "Track  WHERE RDT=(" + sql + ")";
		ps.SQL = sql;

		try
		{
			return DBAccess.RunSQLReturnString(ps);
		}
		catch (java.lang.Exception e)
		{
			// 处理track表.
			Track.CreateOrRepairTrackTable(fk_flow);
			return DBAccess.RunSQLReturnString(ps);
		}

			///#endregion 获取track数据.
	}

		///#region 处理page接口.
	/** 
	 执行的内容
	*/
	public final String getDoWhat() throws Exception {
		String str = this.GetRequestVal("DoWhat");
		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("DoType");
		}
		return str;
	}
	/** 
	 当前的用户
	*/
	public final String getUserNo()  {
		String str = this.GetRequestVal("UserNo");
		if (DataType.IsNullOrEmpty(str) == true)
			str = this.GetRequestVal("UserID");
		return str;
	}
	/** 
	 用户的安全校验码(请参考集成章节)
	*/
	public final String getSID() throws Exception {
		String str = this.GetRequestVal("Token");
		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("Token");
		}
		return str;
	}
	/** 
	 调用页面入口
	 
	 @return 
	*/
	public final String Port_Init() throws Exception {

		if (this.getDoWhat() == null)
		{
			return "err@必要的参数没有传入，请参考接口规则。DoWhat";
		}


			///#region 安全性校验. Token 模式.

			///#endregion 安全性校验. Token 模式.


			///#region 安全性校验. SID 模式.
		if (this.getSID() == null)
		{
			return "err@必要的参数没有传入，请参考接口规则。SID";
		}

		if (Dev2Interface.Port_CheckUserLogin(this.getUserNo(), this.getSID()) == false)
		{
			return "err@非法的访问，请与管理员联系。SID=" + this.getSID();
		}

		if (DataType.IsNullOrEmpty(this.getUserNo()) == false)
		{
			Dev2Interface.Port_Login(this.getUserNo());
			Dev2Interface.Port_GenerToken("PC");

		}

		if (DataType.IsNullOrEmpty(this.getSID()) == false)
		{
			Dev2Interface.Port_LoginByToken(this.getSID());
		}



			///#endregion 安全性校验. SID 模式.

		if (this.getDoWhat().equals("PortLogin") == true)
		{
			return "登陆成功";
		}


			///#region 生成参数串.
		String paras = "";
		for (Object str : CommonUtils.getRequest().getParameterMap().keySet())
		{
			String val = this.GetRequestVal(String.valueOf(str));
			if (val.indexOf('@') != -1)
			{
				return "err@您没有能参数: [ " + str + " ," + val + " ] 给值 ，URL 将不能被执行。";
			}

			switch (str.toString())
			{
				case DoWhatList.DoNode:
				case DoWhatList.Emps:
				case DoWhatList.EmpWorks:
				case DoWhatList.FlowSearch:
				case DoWhatList.Login:
				case DoWhatList.MyFlow:
				case DoWhatList.MyWork:
				case DoWhatList.Start:
				case DoWhatList.Start5:
				case DoWhatList.StartSimple:
				case DoWhatList.FlowFX:
				case DoWhatList.DealWork:
				case "FK_Flow":
				case "WorkID":
				case "FK_Node":
				case "Token":
					break;
				default:
					paras += "&" + str + "=" + val;
					break;
			}
		}
		String nodeID = String.valueOf(Integer.parseInt(this.getFK_Flow() + "01"));

			///#endregion 生成参数串.

		//发起流程.
		if (this.getDoWhat().equals("StartClassic") == true)
		{
			if (this.getFK_Flow() == null)
			{
				return "url@./AppClassic/Home.htm";
			}
			else
			{
				return "url@./AppClassic/Home.htm?FK_Flow=" + this.getFK_Flow() + paras + "&FK_Node=" + nodeID;
			}
		}

		//打开工作轨迹。
		if (this.getDoWhat().equals(DoWhatList.OneWork) == true)
		{
			if (this.getFK_Flow() == null || this.getWorkID() == 0)
			{
				throw new RuntimeException("@参数 FK_Flow 或者 WorkID 为 Null 。");
			}
			return "url@MyView.htm?FK_Flow=" + this.getFK_Flow() + "&WorkID=" + this.getWorkID() + "&o2=1" + paras;
		}

		//发起页面.
		if (this.getDoWhat().equals(DoWhatList.Start) == true)
		{
			if (this.getFK_Flow() == null)
			{
				return "url@Start.htm";
			}
			else
			{
				return "url@MyFlow.htm?FK_Flow=" + this.getFK_Flow() + paras + "&FK_Node=" + nodeID;
			}
		}

		//处理工作.
		if (this.getDoWhat().equals(DoWhatList.DealWork) == true)
		{
			if (DataType.IsNullOrEmpty(this.getFK_Flow()) || this.getWorkID() == 0)
			{
				return "err@参数 FK_Flow 或者 WorkID 为Null 。";
			}
			return "url@MyFlow.htm?FK_Flow=" + this.getFK_Flow() + "&WorkID=" + this.getWorkID() + "&o2=1" + paras;
		}

		//请求在途.
		if (this.getDoWhat().equals(DoWhatList.Runing) == true)
		{
			return "url@Runing.htm?FK_Flow=" + this.getFK_Flow();
		}

		//请求在途.
		if (this.getDoWhat().equals("Home") == true)
		{
			return "url@Home.htm?FK_Flow=" + this.getFK_Flow();
		}

		//请求在途.
		if (this.getDoWhat().equals(DoWhatList.Runing) == true)
		{
			return "url@Runing.htm?FK_Flow=" + this.getFK_Flow();
		}

		//请求待办。
		if (this.getDoWhat().equals(DoWhatList.EmpWorks) == true || this.getDoWhat().equals("Todolist") == true)
		{
			if (DataType.IsNullOrEmpty(this.getFK_Flow()))
			{
				return "url@Todolist.htm";
			}
			else
			{
				return "url@Todolist.htm?FK_Flow=" + this.getFK_Flow();
			}
		}

		//请求流程查询。
		if (this.getDoWhat().equals(DoWhatList.FlowSearch) == true)
		{
			if (DataType.IsNullOrEmpty(this.getFK_Flow()))
			{
				return "url@./RptSearch/Default.htm";
			}
			else
			{
				return "url@./RptDfine/Search.htm?2=1&FK_Flow=001&EnsName=ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt" + paras;
			}
		}

		//流程查询小页面.
		if (this.getDoWhat().equals(DoWhatList.FlowSearchSmall) == true)
		{
			if (this.getFK_Flow() == null)
			{
				return "url@./RptSearch/Default.htm";
			}
			else
			{
				return "url./Comm/Search.htm?EnsName=ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt" + paras;
			}
		}

		//打开消息.
		if (this.getDoWhat().equals(DoWhatList.DealMsg) == true)
		{
			String guid = this.GetRequestVal("GUID");
			SMS sms = new SMS();
			sms.setMyPK(guid);
			sms.Retrieve();

			//判断当前的登录人员.
			if (!sms.getSendToEmpNo().equals(WebUser.getNo()))
			{
				Dev2Interface.Port_Login(sms.getSendToEmpNo());
			}

			AtPara ap = new AtPara(sms.getAtPara());
			switch (sms.getMsgType())
			{
				case SMSMsgType.SendSuccess: // 发送成功的提示.

					if (Dev2Interface.Flow_IsCanDoCurrentWork(ap.GetValInt64ByKey("WorkID"), WebUser.getNo()) == true)
					{
						return "url@MyFlow.htm?FK_Flow=" + ap.GetValStrByKey("FK_Flow") + "&WorkID=" + ap.GetValStrByKey("WorkID") + "&o2=1" + paras;
					}
					else
					{
						return "url@MyView.htm?FK_Flow=" + ap.GetValStrByKey("FK_Flow") + "&WorkID=" + ap.GetValStrByKey("WorkID") + "&o2=1" + paras;
					}

				default: //其他的情况都是查看工作报告.
					return "url@MyView.htm?FK_Flow=" + ap.GetValStrByKey("FK_Flow") + "&WorkID=" + ap.GetValStrByKey("WorkID") + "&o2=1" + paras;
			}
		}
		//  return "err@没有判断的标记.";
		return "err@没有约定的标记:DoWhat=" + this.getDoWhat();
	}

		///#endregion 处理page接口.
	/**
	 Author_InitLeft
	 @return
	 */
	public final String Author_InitLeft() throws Exception {

		String sql = "SELECT Auther,AuthName, FK_Flow,FlowName, COUNT(FK_Flow) as Num   FROM V_WF_AuthTodolist ";
		sql += "WHERE AutherToEmpNo = '" + WebUser.getNo() + "'";
		sql += " AND TakeBackDT >= '" + this.GetRequestVal("nowDate") + "'";
		sql += "     GROUP BY Auther, AuthName, FK_Flow, FlowName  ";

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return bp.tools.Json.ToJson(dt);
	}
	/**
	 Author_InitDocs
	 @return
	 */
	public final String Author_InitDocs() throws Exception {

		String sql = "SELECT *  FROM V_WF_AuthTodolist ";
		sql += "WHERE AutherToEmpNo = '" + WebUser.getNo() + "' and Auther ='" + this.GetRequestVal("author")
				+ "' and FK_Flow ='" + this.GetRequestVal("flowNo") + "'";
		sql += "  AND TakeBackDT >= '" + this.GetRequestVal("nowDate") + "'";

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return bp.tools.Json.ToJson(dt);
	}
}
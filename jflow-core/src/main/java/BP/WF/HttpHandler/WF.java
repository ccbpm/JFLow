package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.WF.*;
import BP.WF.Data.*;
import BP.WF.Port.*;
import BP.WF.Rpt.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.util.*;
import java.io.*;

public class WF extends DirectoryPageBase
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 单表单查看.
	/** 
	 流程单表单查看
	 
	 @return 
	*/
	public final String FrmView_Init()
	{
		Node nd = new Node(this.getFK_Node());
		nd.WorkID = this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.

		MapData md = new MapData();
		md.No = nd.getNodeFrmID();
		if (md.RetrieveFromDBSources() == 0)
		{
			throw new RuntimeException("装载错误，该表单ID=" + md.No + "丢失，请修复一次流程重新加载一次.");
		}



		//获得表单模版.
		DataSet myds = BP.Sys.CCFormAPI.GenerHisDataSet(md.No);


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 把主从表数据放入里面.
		//.工作数据放里面去, 放进去前执行一次装载前填充事件.
		BP.WF.Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.RetrieveFromDBSources();

		//重设默认值.
		//wk.ResetDefaultVal();

		DataTable mainTable = wk.ToDataTableField("MainTable");
		mainTable.TableName = "MainTable";
		myds.Tables.add(mainTable);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion


		//加入WF_Node.
		DataTable WF_Node = nd.ToDataTableField("WF_Node").Copy();
		myds.Tables.add(WF_Node);


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 加入组件的状态信息, 在解析表单的时候使用.
		nd.WorkID = this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.
		BP.WF.Template.FrmNodeComponent fnc = new FrmNodeComponent(nd.getNodeID());
		if (!nd.getNodeFrmID().equals("ND" + nd.getNodeID()))
		{
			/*说明这是引用到了其他节点的表单，就需要把一些位置元素修改掉.*/
			int refNodeID = Integer.parseInt(nd.getNodeFrmID().replace("ND", ""));

			BP.WF.Template.FrmNodeComponent refFnc = new FrmNodeComponent(refNodeID);

			fnc.SetValByKey(FrmWorkCheckAttr.FWC_H, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_H));
			fnc.SetValByKey(FrmWorkCheckAttr.FWC_W, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_W));
			fnc.SetValByKey(FrmWorkCheckAttr.FWC_X, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_X));
			fnc.SetValByKey(FrmWorkCheckAttr.FWC_Y, refFnc.GetValFloatByKey(FrmWorkCheckAttr.FWC_Y));


			fnc.SetValByKey(FrmSubFlowAttr.SF_H, refFnc.GetValFloatByKey(FrmSubFlowAttr.SF_H));
			fnc.SetValByKey(FrmSubFlowAttr.SF_W, refFnc.GetValFloatByKey(FrmSubFlowAttr.SF_W));
			fnc.SetValByKey(FrmSubFlowAttr.SF_X, refFnc.GetValFloatByKey(FrmSubFlowAttr.SF_X));
			fnc.SetValByKey(FrmSubFlowAttr.SF_Y, refFnc.GetValFloatByKey(FrmSubFlowAttr.SF_Y));

			fnc.SetValByKey(FrmThreadAttr.FrmThread_H, refFnc.GetValFloatByKey(FrmThreadAttr.FrmThread_H));
			fnc.SetValByKey(FrmThreadAttr.FrmThread_W, refFnc.GetValFloatByKey(FrmThreadAttr.FrmThread_W));
			fnc.SetValByKey(FrmThreadAttr.FrmThread_X, refFnc.GetValFloatByKey(FrmThreadAttr.FrmThread_X));
			fnc.SetValByKey(FrmThreadAttr.FrmThread_Y, refFnc.GetValFloatByKey(FrmThreadAttr.FrmThread_Y));

			fnc.SetValByKey(FrmTrackAttr.FrmTrack_H, refFnc.GetValFloatByKey(FrmTrackAttr.FrmTrack_H));
			fnc.SetValByKey(FrmTrackAttr.FrmTrack_W, refFnc.GetValFloatByKey(FrmTrackAttr.FrmTrack_W));
			fnc.SetValByKey(FrmTrackAttr.FrmTrack_X, refFnc.GetValFloatByKey(FrmTrackAttr.FrmTrack_X));
			fnc.SetValByKey(FrmTrackAttr.FrmTrack_Y, refFnc.GetValFloatByKey(FrmTrackAttr.FrmTrack_Y));

			fnc.SetValByKey(FTCAttr.FTC_H, refFnc.GetValFloatByKey(FTCAttr.FTC_H));
			fnc.SetValByKey(FTCAttr.FTC_W, refFnc.GetValFloatByKey(FTCAttr.FTC_W));
			fnc.SetValByKey(FTCAttr.FTC_X, refFnc.GetValFloatByKey(FTCAttr.FTC_X));
			fnc.SetValByKey(FTCAttr.FTC_Y, refFnc.GetValFloatByKey(FTCAttr.FTC_Y));
		}
		myds.Tables.add(fnc.ToDataTableField("WF_FrmNodeComponent").Copy());
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 加入组件的状态信息, 在解析表单的时候使用.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 增加附件信息.
		BP.Sys.FrmAttachments athDescs = new FrmAttachments();

		nd.WorkID = this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.
		athDescs.Retrieve(FrmAttachmentAttr.FK_MapData, nd.getNodeFrmID());
		if (athDescs.size() != 0)
		{
			FrmAttachment athDesc = athDescs[0] instanceof FrmAttachment ? (FrmAttachment)athDescs[0] : null;

			//查询出来数据实体.
			BP.Sys.FrmAttachmentDBs dbs = new BP.Sys.FrmAttachmentDBs();
			if (athDesc.HisCtrlWay == AthCtrlWay.PWorkID)
			{
				Paras ps = new Paras();
				ps.SQL = "SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID";
				ps.Add("WorkID", this.getWorkID());
				String pWorkID = BP.DA.DBAccess.RunSQLReturnValInt(ps, 0).toString();
				if (pWorkID == null || pWorkID.equals("0"))
				{
					pWorkID = String.valueOf(this.getWorkID());
				}

				if (athDesc.AthUploadWay == AthUploadWay.Inherit)
				{
					/* 继承模式 */
					BP.En.QueryObject qo = new BP.En.QueryObject(dbs);
					qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, pWorkID);
					qo.addOr();
					qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, String.valueOf(this.getWorkID()));
					qo.addOrderBy("RDT");
					qo.DoQuery();
				}

				if (athDesc.AthUploadWay == AthUploadWay.Interwork)
				{
					/*共享模式*/
					dbs.Retrieve(FrmAttachmentDBAttr.RefPKVal, pWorkID);
				}
			}
			else if (athDesc.HisCtrlWay == AthCtrlWay.WorkID)
			{
				/* 继承模式 */
				BP.En.QueryObject qo = new BP.En.QueryObject(dbs);
				qo.AddWhere(FrmAttachmentDBAttr.NoOfObj, athDesc.NoOfObj);
				qo.addAnd();
				qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, String.valueOf(this.getWorkID()));
				qo.addOrderBy("RDT");
				qo.DoQuery();
			}

			//增加一个数据源.
			myds.Tables.add(dbs.ToDataTableField("Sys_FrmAttachmentDB").Copy());
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 把外键表加入DataSet
		DataTable dtMapAttr = myds.Tables["Sys_MapAttr"];
		DataTable dt = new DataTable();
		MapExts mes = md.MapExts;
		MapExt me = new MapExt();
		DataTable ddlTable = new DataTable();
		ddlTable.Columns.Add("No");
		for (DataRow dr : dtMapAttr.Rows)
		{
			String lgType = dr.get("LGType").toString();
			String uiBindKey = dr.get("UIBindKey").toString();

			if (DataType.IsNullOrEmpty(uiBindKey) == true)
			{
				continue; //为空就continue.
			}

			if (lgType.equals("1") == true)
			{
				continue; //枚举值就continue;
			}

			String uiIsEnable = dr.get("UIIsEnable").toString();
			if (uiIsEnable.equals("0") == true && lgType.equals("1") == true)
			{
				continue; //如果是外键，并且是不可以编辑的状态.
			}

			if (uiIsEnable.equals("1") == true && lgType.equals("0") == true)
			{
				continue; //如果是外部数据源，并且是不可以编辑的状态.
			}



			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.get("KeyOfEn").toString();
			String fk_mapData = dr.get("FK_MapData").toString();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 处理下拉框数据范围. for 小杨.
			Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
			me = tempVar instanceof MapExt ? (MapExt)tempVar : null;
			if (me != null)
			{
				Object tempVar2 = me.Doc.Clone();
				String fullSQL = tempVar2 instanceof String ? (String)tempVar2 : null;
				fullSQL = fullSQL.replace("~", ",");
				fullSQL = BP.WF.Glo.DealExp(fullSQL, wk, null);
				dt = DBAccess.RunSQLReturnTable(fullSQL);
				//重构新表
				DataTable dt_FK_Dll = new DataTable();
				dt_FK_Dll.TableName = keyOfEn; //可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
				dt_FK_Dll.Columns.Add("No", String.class);
				dt_FK_Dll.Columns.Add("Name", String.class);
				for (DataRow dllRow : dt.Rows)
				{
					DataRow drDll = dt_FK_Dll.NewRow();
					drDll.set("No", dllRow.get("No"));
					drDll.set("Name", dllRow.get("Name"));
					dt_FK_Dll.Rows.add(drDll);
				}
				myds.Tables.add(dt_FK_Dll);
				continue;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 处理下拉框数据范围.

			// 判断是否存在.
			if (myds.Tables.Contains(uiBindKey) == true)
			{
				continue;
			}

			DataTable mydt = BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey);
			if (mydt == null)
			{
				DataRow ddldr = ddlTable.NewRow();
				ddldr.set("No", uiBindKey);
				ddlTable.Rows.add(ddldr);
			}
			else
			{
				myds.Tables.add(mydt);
			}
		}
		ddlTable.TableName = "UIBindKey";
		myds.Tables.add(ddlTable);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion End把外键表加入DataSet

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 图片附件
		nd.WorkID = this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.
		FrmImgAthDBs imgAthDBs = new FrmImgAthDBs(nd.getNodeFrmID(), String.valueOf(this.getWorkID()));
		if (imgAthDBs != null && imgAthDBs.size() > 0)
		{
			DataTable dt_ImgAth = imgAthDBs.ToDataTableField("Sys_FrmImgAthDB");
			myds.Tables.add(dt_ImgAth);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion


		return BP.Tools.Json.ToJson(myds);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion



	/** 
	 流程数据
	 
	 @return 
	*/
	public final String Watchdog_Init()
	{
		String sql = " SELECT FK_Flow,FlowName, COUNT(workid) as Num FROM V_MyFlowData WHERE MyEmpNo='" + WebUser.getNo() + "' ";
		sql += " GROUP BY  FK_Flow,FlowName ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Group";
		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 流程数据初始化
	 
	 @return 
	*/
	public final String Watchdog_InitFlows()
	{
		String sql = " SELECT *  FROM V_MyFlowData WHERE MyEmpNo='" + WebUser.getNo() + "' AND FK_Flow='" + this.getFK_Flow() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Flows";
		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 构造函数
	*/
	public WF()
	{

	}
	@Override
	protected String DoDefaultMethod()
	{
		return super.DoDefaultMethod();
	}
	public final String HasSealPic()
	{
		String no = GetRequestVal("No");
		if (DataType.IsNullOrEmpty(no))
		{
			return "";
		}

		String path = "/DataUser/Siganture/" + no + ".jpg";
		//如果文件存在

		if ((new File(SystemConfig.PathOfWebApp + (path))).isFile() == false)
		{
			path = "/DataUser/Siganture/" + no + ".JPG";
			if ((new File(SystemConfig.PathOfWebApp + (path))).isFile() == true)
			{
				return "";
			}

			//如果不存在，就返回名称
			BP.Port.Emp emp = new BP.Port.Emp(no);
			return emp.Name;
		}
		return "";
	}
	/** 
	 执行的方法.
	 
	 @return 
	*/
	public final String Do_Init()
	{
		String at = this.GetRequestVal("ActionType");
		if (DataType.IsNullOrEmpty(at))
		{
			at = this.GetRequestVal("DoType");
		}

		if (DataType.IsNullOrEmpty(at) && this.getSID() != null)
		{
			at = "Track";
		}

		try
		{
			switch (at)
			{

				case "Focus": //把任务放入任务池.
					BP.WF.Dev2Interface.Flow_Focus(this.getWorkID());
					return "info@Close";
				case "PutOne": //把任务放入任务池.
					BP.WF.Dev2Interface.Node_TaskPoolPutOne(this.getWorkID());
					return "info@Close";
					break;
				case "DoAppTask": // 申请任务.
					BP.WF.Dev2Interface.Node_TaskPoolTakebackOne(this.getWorkID());
					return "info@Close";
				case "DoOpenCC":
				case "WFRpt":
					String Sta = this.GetRequestVal("Sta");
					if (Sta.equals("0"))
					{
						BP.WF.Template.CCList cc1 = new BP.WF.Template.CCList();
						cc1.setMyPK( this.getMyPK();
						cc1.Retrieve();
						cc1.setHisSta(CCSta.Read);
						cc1.Update();
					}
					return "url@./WorkOpt/OneWork/OneWork.htm?CurrTab=Track&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID();
				case "DelCC": //删除抄送.
					CCList cc = new CCList();
					cc.setMyPK( this.getMyPK();
					cc.Retrieve();
					cc.setHisSta(CCSta.Del);
					cc.Update();
					return "info@Close";
				case "DelSubFlow": //删除进程。
					try
					{
						BP.WF.Dev2Interface.Flow_DeleteSubThread(this.getFK_Flow(), this.getWorkID(), "手工删除");
						return "info@Close";
					}
					catch (RuntimeException ex)
					{
						return "err@" + ex.getMessage();
					}
					break;
				case "DownBill":
					Bill b = new Bill(this.getMyPK());
					b.DoOpen();
					break;
				case "DelDtl":
					GEDtls dtls = new GEDtls(this.getEnsName());
					GEDtl dtl = (GEDtl)dtls.GetNewEntity;
					dtl.OID = this.getRefOID();
					if (dtl.RetrieveFromDBSources() == 0)
					{
						return "info@Close";
					}
					FrmEvents fes = new FrmEvents(this.getEnsName()); //获得事件.

					// 处理删除前事件.
					try
					{
						fes.DoEventNode(BP.WF.XML.EventListDtlList.DtlItemDelBefore, dtl);
					}
					catch (RuntimeException ex)
					{
						return "err@" + ex.getMessage();
					}
					dtl.Delete();

					// 处理删除后事件.
					try
					{
						fes.DoEventNode(BP.WF.XML.EventListDtlList.DtlItemDelAfter, dtl);
					}
					catch (RuntimeException ex)
					{
						return "err@" + ex.getMessage();
						break;
					}
					return "info@Close";
					break;
				case "EmpDoUp":
					WFEmp ep = new WFEmp(this.GetRequestVal("RefNo"));
					ep.DoUp();

					WFEmps emps111 = new WFEmps();
					//  emps111.RemoveCash();
					emps111.RetrieveAll();
					return "info@Close";
					break;
				case "EmpDoDown":
					WFEmp ep1 = new WFEmp(this.GetRequestVal("RefNo"));
					ep1.DoDown();

					WFEmps emps11441 = new WFEmps();
					//  emps11441.RemoveCash();
					emps11441.RetrieveAll();
					return "info@Close";
				case "Track": //通过一个串来打开一个工作.
					String mySid = this.getSID(); // this.Request.QueryString["SID"];
					String[] mystrs = mySid.split("[_]", -1);

					long myWorkID = Integer.parseInt(mystrs[1]);
					String fk_emp = mystrs[0];
					int fk_node = Integer.parseInt(mystrs[2]);
					Node mynd = new Node();
					mynd.setNodeID(fk_node);
					mynd.RetrieveFromDBSources();

					String fk_flow = mynd.getFK_Flow();
					String myurl = "./WorkOpt/OneWork/OneWork.htm?CurrTab=Track&FK_Node=" + mynd.getNodeID() + "&WorkID=" + myWorkID + "&FK_Flow=" + fk_flow;
					Web.WebUser.SignInOfGener(new BP.Port.Emp(fk_emp));

					return "url@" + myurl;
				case "OF": //通过一个串来打开一个工作.
					String sid = this.getSID();
					String[] strs = sid.split("[_]", -1);
					GenerWorkerList wl = new GenerWorkerList();
					int i = wl.Retrieve(GenerWorkerListAttr.FK_Emp, strs[0], GenerWorkerListAttr.WorkID, strs[1], GenerWorkerListAttr.IsPass, 0);

					if (i == 0)
					{
						return "info@此工作已经被别人处理或者此流程已删除";
					}

					BP.Port.Emp empOF = new BP.Port.Emp(wl.getFK_Emp());
					Web.WebUser.SignInOfGener(empOF);
					String u = "MyFlow.htm?FK_Flow=" + wl.getFK_Flow() + "&WorkID=" + wl.getWorkID() + "&FK_Node=" + wl.getFK_Node() + "&FID=" + wl.getFID();

					return "url@" + u;
				case "ExitAuth":
					BP.Port.Emp emp = new BP.Port.Emp(this.getFK_Emp());
					//首先退出，再进行登录
					WebUser.Exit();
					WebUser.SignInOfGener(emp, WebUser.SysLang);
					return "info@Close";
				case "LogAs":
					WFEmp wfemp = new WFEmp(this.getFK_Emp());
					if (wfemp.getAuthorIsOK() == false)
					{
						return "err@授权失败";
					}
					BP.Port.Emp emp1 = new BP.Port.Emp(this.getFK_Emp());
					WebUser.SignInOfGener(emp1, "CH", false, false, wfemp.getAuthor(), WebUser.getName());
					return "info@Close";
				case "TakeBack": // 取消授权。
					WFEmp myau = new WFEmp(WebUser.getNo());
					BP.DA.Log.DefaultLogWriteLineInfo("取消授权:" + WebUser.getNo() + "取消了对(" + myau.getAuthor() + ")的授权。");
					myau.setAuthor("");
					myau.setAuthorWay(0);
					myau.Update();
					return "info@Close";
				case "AutoTo": // 执行授权。
					WFEmp au = new WFEmp();
					au.No = WebUser.getNo();
					au.RetrieveFromDBSources();
					au.setAuthorDate(BP.DA.DataType.CurrentData);
					au.setAuthor(this.getFK_Emp());
					au.setAuthorWay(1);
					au.Save();
					BP.DA.Log.DefaultLogWriteLineInfo("执行授权:" + WebUser.getNo() + "执行了对(" + au.getAuthor() + ")的授权。");
					return "info@Close";
				case "UnSend": //执行撤消发送。
					String url = "./WorkOpt/UnSend.htm?WorkID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow();
					return "url@" + url;
				case "SetBillState":
					break;
				case "WorkRpt":
					//  Bill bk1 = new Bill(this.OID);
					//  Node nd = new Node(bk1.FK_Node);
					// this.Response.Redirect("WFRpt.htm?WorkID=" + bk1.WorkID + "&FID=" + bk1.FID + "&FK_Flow=" + nd.FK_Flow + "&NodeId=" + bk1.FK_Node, false);
					//this.WinOpen();
					//this.WinClose();
					break;
				case "PrintBill":
					//Bill bk2 = new Bill(this.Request.QueryString["OID"]);
					//Node nd2 = new Node(bk2.FK_Node);
					//this.Response.Redirect("NodeRefFunc.aspx?NodeId=" + bk2.FK_Node + "&FlowNo=" + nd2.FK_Flow + "&NodeRefFuncOID=" + bk2.FK_NodeRefFunc + "&WorkFlowID=" + bk2.WorkID);
					////this.WinClose();
					break;
				//删除流程中第一个节点的数据，包括待办工作
				case "DeleteFlow":
					//调用DoDeleteWorkFlowByReal方法
					WorkFlow wf = new WorkFlow(new Flow(this.getFK_Flow()), this.getWorkID());
					wf.DoDeleteWorkFlowByReal(true);
					//  Glo.ToMsg("流程删除成功");
					BP.WF.Glo.ToMsg("流程删除成功");

					//this.ToWFMsgPage("流程删除成功");
					break;
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
	public final String PCAndMobileUrl()
	{
		Hashtable ht = new Hashtable();
		ht.put("PCUrl", SystemConfig.HostURL);
		ht.put("MobileUrl", SystemConfig.MobileURL);
		return BP.Tools.Json.ToJson(ht);
	}

	/** 
	 页面调整移动端OR手机端
	 
	 @return 
	*/
	public final String Do_Direct()
	{
		//获取地址
		String baseUrl = this.GetRequestVal("DirectUrl");
		////判断是移动端还是PC端打开的页面
		//Regex RegexMobile = new Regex(@"(iemobile|iphone|ipod|android|nokia|sonyericsson|blackberry|samsung|sec\-|windows ce|motorola|mot\-|up.b|midp\-)",
		//RegexOptions.IgnoreCase | RegexOptions.Compiled);
		//移动端打开
		if (HttpContextHelper.RequestIsFromMobile)
		{
			return SystemConfig.MobileURL + baseUrl;
		}
		else
		{
			return SystemConfig.HostURL + baseUrl;
		}

	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 我的关注流程.
	/** 
	 我的关注流程
	 
	 @return 
	*/
	public final String Focus_Init()
	{
		String flowNo = this.GetRequestVal("FK_Flow");

		int idx = 0;
		//获得关注的数据.
		System.Data.DataTable dt = BP.WF.Dev2Interface.DB_Focus(flowNo, WebUser.getNo());
		SysEnums stas = new SysEnums("WFSta");
		String[] tempArr;
		for (System.Data.DataRow dr : dt.Rows)
		{
			int wfsta = Integer.parseInt(dr.get("WFSta").toString());
			//edit by liuxc,2016-10-22,修复状态显示不正确问题
			Object tempVar = stas.GetEntityByKey(SysEnumAttr.IntKey, wfsta);
			String wfstaT = (tempVar instanceof SysEnum ? (SysEnum)tempVar : null).Lab;
			String currEmp = "";

			if (wfsta != BP.WF.WFSta.Complete.getValue())
			{
				//edit by liuxc,2016-10-24,未完成时，处理当前处理人，只显示处理人姓名
				for (String emp : dr.get("ToDoEmps").toString().split("[;]", -1))
				{
					tempArr = emp.split("[,]", -1);

					currEmp += tempArr.length > 1 ? tempArr[1] : tempArr[0] + ",";
				}

				currEmp = tangible.StringHelper.trimEnd(currEmp, ',');

				//currEmp = dr["ToDoEmps"].ToString();
				//currEmp = currEmp.TrimEnd(';');
			}
			dr.set("ToDoEmps", currEmp);
			dr.set("FlowNote", wfstaT);
			dr.set("AtPara", (wfsta == BP.WF.WFSta.Complete.getValue() ? tangible.StringHelper.trimEnd(tangible.StringHelper.trimStart(dr.get("Sender").toString(), '('), ')').split("[,]", -1)[1] : ""));
		}
		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 取消关注
	 
	 @return 
	*/
	public final String Focus_Delete()
	{
		BP.WF.Dev2Interface.Flow_Focus(this.getWorkID());
		return "执行成功";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 我的关注.

	/** 
	 方法
	 
	 @return 
	*/
	public final String HandlerMapExt()
	{
		WF_CCForm wf = new WF_CCForm();
		return wf.HandlerMapExt();
	}
	/** 
	 节水公司
	 
	 @return 
	*/
	public final String Start_InitTianYe_JieShui()
	{
		//获得当前人员的部门,根据部门获得该人员的组织集合.
		Paras ps = new Paras();
		ps.SQL = "SELECT FK_Dept FROM Port_DeptEmp WHERE FK_Emp=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
		ps.AddFK_Emp();
		DataTable dt = DBAccess.RunSQLReturnTable(ps);

		//找到当前人员所在的部门集合, 应该找到他的组织集合为了减少业务逻辑.
		String orgNos = "'18099','103'"; //空的数据.
		for (DataRow dr : dt.Rows)
		{
			String deptNo = dr.get(0).toString();
			orgNos += ",'" + deptNo + "'";
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 获取类别列表(根据当前人员所在组织结构进行过滤类别.)
		FlowSorts fss = new FlowSorts();
		BP.En.QueryObject qo = new En.QueryObject(fss);
		qo.AddWhereIn(FlowSortAttr.OrgNo, "(" + orgNos + ")"); //指定的类别.
		qo.addOr();
		qo.AddWhere(FlowSortAttr.Name, " LIKE ", "%节水%"); //指定的类别.

		//排序.
		qo.addOrderBy(FlowSortAttr.No, FlowSortAttr.Idx);

		DataTable dtSort = qo.DoQueryToTable();
		dtSort.TableName = "Sort";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dtSort.Columns.get("NO").ColumnName = "No";
			dtSort.Columns.get("NAME").ColumnName = "Name";
			dtSort.Columns.get("PARENTNO").ColumnName = "ParentNo";
			dtSort.Columns.get("ORGNO").ColumnName = "OrgNo";
		}

		//定义容器.
		DataSet ds = new DataSet();
		ds.Tables.add(dtSort); //增加到里面去.
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 获取类别列表.

		//构造流程实例数据容器。
		DataTable dtStart = new DataTable();
		dtStart.TableName = "Start";
		dtStart.Columns.Add("No");
		dtStart.Columns.Add("Name");
		dtStart.Columns.Add("FK_FlowSort");
		dtStart.Columns.Add("IsBatchStart");
		dtStart.Columns.Add("IsStartInMobile");
		dtStart.Columns.Add("Note");

		//获得所有的流程（包含了所有子公司与集团的可以发起的流程但是没有根据组织结构进行过滤.）
		DataTable dtAllFlows = Dev2Interface.DB_StarFlows(Web.WebUser.getNo());

		//按照当前用户的流程类别权限进行过滤.
		for (DataRow drSort : dtSort.Rows)
		{
			for (DataRow drFlow : dtAllFlows.Rows)
			{
				if (!drSort.get("No").toString().equals(drFlow.get("FK_FlowSort").toString()))
				{
					continue;
				}

				DataRow drNew = dtStart.NewRow();

				drNew.set("No", drFlow.get("No"));
				drNew.set("Name", drFlow.get("Name"));
				drNew.set("FK_FlowSort", drFlow.get("FK_FlowSort"));
				drNew.set("IsBatchStart", drFlow.get("IsBatchStart"));
				drNew.set("IsStartInMobile", drFlow.get("IsStartInMobile"));
				drNew.set("Note", drFlow.get("Note"));
				dtStart.Rows.add(drNew); //增加到里里面去.
			}
		}

		//把经过权限过滤的流程实体放入到集合里.
		ds.Tables.add(dtStart); //增加到里面去.

		//返回组合
		String json = BP.Tools.Json.DataSetToJson(ds, false);

		//放入缓存里面去.
		WFEmp em = new WFEmp();
		em.No = WebUser.getNo();

		//把json存入数据表，避免下一次再取.
		if (json.length() > 40)
		{
			em.setStartFlows(json);
			em.Update();
		}
		return json;
	}
	/** 
	 天业集团的发起，特殊处理.
	 
	 @return 
	*/
	public final String Start_InitTianYe()
	{
		//如果请求了刷新.
		if (this.GetRequestVal("IsRef") != null)
		{
			//清除权限.
			DBAccess.RunSQL("UPDATE WF_Emp SET StartFlows='' WHERE No='" + WebUser.getNo() + "' ");

			//处理权限,为了防止未知的错误.
			DBAccess.RunSQL("UPDATE WF_FLOWSORT SET ORGNO='0' WHERE ORGNO='' OR ORGNO IS NULL OR ORGNO='101'");

			DBAccess.RunSQL("UPDATE wf_flowsort SET ORGNO = REPLACE(NO,'Inc','') where  no like 'Inc%'");
		}

		//需要翻译.
		WFEmp em = new WFEmp();
		em.No = WebUser.getNo();
		if (em.RetrieveFromDBSources() == 0)
		{
			em.setFK_Dept(WebUser.getFK_Dept());
			em.Name = Web.WebUser.getName();
			em.setEmail((new BP.GPM.Emp(WebUser.getNo())).Email);
			em.Insert();
		}
		String json = em.getStartFlows();
		if (DataType.IsNullOrEmpty(json) == false)
		{
			return json;
		}

		//如果是节水公司的，就特别处理.
		if (WebUser.getFK_Dept().indexOf("18099") == 0)
		{
			return Start_InitTianYe_JieShui();
		}

		//获得当前人员的部门,根据部门获得该人员的组织集合.
		Paras ps = new Paras();
		ps.SQL = "SELECT FK_Dept FROM Port_DeptEmp WHERE FK_Emp=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
		ps.AddFK_Emp();
		DataTable dt = DBAccess.RunSQLReturnTable(ps);

		//找到当前人员所在的部门集合, 应该找到他的组织集合为了减少业务逻辑.
		String orgNos = "'0'";
		for (DataRow dr : dt.Rows)
		{
			String deptNo = dr.get(0).toString();
			orgNos += ",'" + deptNo + "'";
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 获取类别列表(根据当前人员所在组织结构进行过滤类别.)
		FlowSorts fss = new FlowSorts();
		BP.En.QueryObject qo = new En.QueryObject(fss);
		if (orgNos.contains(",") == false)
		{
			qo.AddWhere(FlowSortAttr.OrgNo, "0"); //..
			qo.addOr();
			qo.AddWhere(FlowSortAttr.OrgNo, ""); //..
		}
		else
		{
			qo.AddWhereIn(FlowSortAttr.OrgNo, "(" + orgNos + ")"); //指定的类别.
		}

		//排序.
		qo.addOrderBy(FlowSortAttr.No, FlowSortAttr.Idx);

		DataTable dtSort = qo.DoQueryToTable();
		dtSort.TableName = "Sort";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dtSort.Columns.get("NO").ColumnName = "No";
			dtSort.Columns.get("NAME").ColumnName = "Name";
			dtSort.Columns.get("PARENTNO").ColumnName = "ParentNo";
			dtSort.Columns.get("ORGNO").ColumnName = "OrgNo";
		}

		//定义容器.
		DataSet ds = new DataSet();
		ds.Tables.add(dtSort); //增加到里面去.
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 获取类别列表.

		//构造流程实例数据容器。
		DataTable dtStart = new DataTable();
		dtStart.TableName = "Start";
		dtStart.Columns.Add("No");
		dtStart.Columns.Add("Name");
		dtStart.Columns.Add("FK_FlowSort");
		dtStart.Columns.Add("IsBatchStart");
		dtStart.Columns.Add("IsStartInMobile");
		dtStart.Columns.Add("Note");

		//获得所有的流程（包含了所有子公司与集团的可以发起的流程但是没有根据组织结构进行过滤.）
		DataTable dtAllFlows = Dev2Interface.DB_StarFlows(Web.WebUser.getNo());

		//按照当前用户的流程类别权限进行过滤.
		for (DataRow drSort : dtSort.Rows)
		{
			for (DataRow drFlow : dtAllFlows.Rows)
			{
				if (!drSort.get("No").toString().equals(drFlow.get("FK_FlowSort").toString()))
				{
					continue;
				}

				DataRow drNew = dtStart.NewRow();

				drNew.set("No", drFlow.get("No"));
				drNew.set("Name", drFlow.get("Name"));
				drNew.set("FK_FlowSort", drFlow.get("FK_FlowSort"));
				drNew.set("IsBatchStart", drFlow.get("IsBatchStart"));
				drNew.set("IsStartInMobile", drFlow.get("IsStartInMobile"));
				drNew.set("Note", drFlow.get("Note"));
				dtStart.Rows.add(drNew); //增加到里里面去.
			}
		}

		//把经过权限过滤的流程实体放入到集合里.
		ds.Tables.add(dtStart); //增加到里面去.

		//返回组合
		json = BP.Tools.Json.DataSetToJson(ds, false);

		//把json存入数据表，避免下一次再取.
		if (json.length() > 40)
		{
			em.setStartFlows(json);
			em.Update();
		}

		return json;
	}
	/** 
	 获得发起列表 
	 
	 @return 
	*/
	public final String Start_Init()
	{
		//通用的处理器.
		if (BP.Sys.SystemConfig.getCustomerNo().equals("TianYe"))
		{
			return Start_InitTianYe();
		}
		String json = "";

		WFEmp em = new WFEmp();
		em.No = WebUser.getNo();
		if (DataType.IsNullOrEmpty(em.No) == true)
		{
			return "err@登录信息丢失,请重新登录.";
		}

		if (em.RetrieveFromDBSources() == 0)
		{
			em.setFK_Dept(WebUser.getFK_Dept());
			em.Name = Web.WebUser.getName();
			em.Insert();
		}
		String sql = "";
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{

			 sql = "SELECT dbms_lob.substr(StartFlows) as StartFlows From WF_Emp WHERE No='" + WebUser.getNo() + "'";
		}
		else
		{
			 sql = "SELECT StartFlows as StartFlows From WF_Emp WHERE No='" + WebUser.getNo() + "'";
		}
		json = DBAccess.RunSQLReturnString(sql);
		if (DataType.IsNullOrEmpty(json) == false)
		{
			return json;
		}


		//定义容器.
		DataSet ds = new DataSet();

		//流程类别.
		FlowSorts fss = new FlowSorts();
		fss.RetrieveAll();

		DataTable dtSort = fss.ToDataTableField("Sort");
		dtSort.TableName = "Sort";
		ds.Tables.add(dtSort);

		//获得能否发起的流程.
		DataTable dtStart = Dev2Interface.DB_StarFlows(Web.WebUser.getNo());
		dtStart.TableName = "Start";
		ds.Tables.add(dtStart);

		//返回组合
		json = BP.Tools.Json.DataSetToJson(ds, false);

		//把json存入数据表，避免下一次再取.
		if (json.length() > 40)
		{
			Paras ps = new Paras();
			ps.SQL = "UPDATE WF_Emp SET StartFlows=" + ps.DBStr + "StartFlows WHERE No=" + ps.DBStr + "No";
			ps.Add("StartFlows", json);
			ps.Add("No", WebUser.getNo());
			DBAccess.RunSQL(ps);
		}

		//返回组合
		return json;
	}
	/** 
	 获得发起列表
	 
	 @return 
	*/
	public final String FlowSearch_Init()
	{
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

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dtStart.Columns.get("NO").ColumnName = "No";
			dtStart.Columns.get("NAME").ColumnName = "Name";
			dtStart.Columns.get("FK_FLOWSORT").ColumnName = "FK_FlowSort";
		}

		ds.Tables.add(dtStart);



		//返回组合
		return BP.Tools.Json.DataSetToJson(ds, false);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 获得列表.
	/** 
	 运行
	 
	 @param UserNo 人员编号
	 @param fk_flow 流程编号
	 @return 运行中的流程
	*/
	public final String Runing_Init()
	{
		DataTable dt = null;
		dt = BP.WF.Dev2Interface.DB_GenerRuning();

		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 我参与的已经完成的工作.
	 
	 @return 
	*/
	public final String Complete_Init()
	{
		/* 如果不是删除流程注册表. */
		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		ps.SQL = "SELECT  * FROM WF_GenerWorkFlow  WHERE Emps LIKE '%@" + WebUser.getNo() + "@%' and WFState=" + WFState.Complete.getValue() + " ORDER BY  RDT DESC";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		//添加oracle的处理
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
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
			dt.Columns.get("MYNUM").ColumnName = "MyNum";
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

		if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
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
			dt.Columns.get("mynum").ColumnName = "MyNum";
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

		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 执行撤销
	 
	 @return 
	*/
	public final String Runing_UnSend()
	{
		try
		{
			//获取撤销到的节点
			int unSendToNode = this.GetRequestValInt("UnSendToNode");
			return BP.WF.Dev2Interface.Flow_DoUnSend(this.getFK_Flow(), this.getWorkID(), unSendToNode, this.getFID());
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 执行催办
	 
	 @return 
	*/
	public final String Runing_Press()
	{
		try
		{
			return BP.WF.Dev2Interface.Flow_DoPress(this.getWorkID(), this.GetRequestVal("Msg"), false);
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
	public final String Runing_OpenFrm()
	{
		int nodeID = this.getFK_Node();
		GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
		if (nodeID == 0)
		{
			gwf = new GenerWorkFlow(this.getWorkID());
			nodeID = gwf.getFK_Node();
		}

		String appPath = BP.WF.Glo.getCCFlowAppPath();
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
			urlExt = "&PFlowNo=0&PWorkID=0&IsToobar=0&IsHidden=true";
		}
		else
		{
			urlExt = "&PFlowNo=" + ndrpt.Rows[0]["PFlowNo"] + "&PWorkID=" + ndrpt.Rows[0]["PWorkID"] + "&IsToobar=0&IsHidden=true";
		}

		urlExt += "&From=CCFlow&TruckKey=" + tk.GetValStrByKey("MyPK") + "&DoType=" + this.getDoType() + "&UserNo=" + WebUser.getNo() != null ? WebUser.getNo() : "" + "&SID=" + WebUser.SID != null ? WebUser.SID : "";

		urlExt = urlExt.replace("PFlowNo=null", "");
		urlExt = urlExt.replace("PWorkID=null", "");

		if (gwf.atPara.HisHT.size() > 0)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			for (var item : gwf.atPara.HisHT.keySet())
			{
				urlExt += "&" + item + "=" + gwf.atPara.HisHT[item];
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
			return "url@./MyFlowTreeReadonly.htm?3=4&WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&OID=" + this.getWorkID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + nd.getNodeID() + "&PK=OID&PKVal=" + this.getWorkID() + "&IsEdit=0&IsLoadData=0&IsReadonly=1" + urlExt;
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
				info += "技术信息:表" + wk.EnMap.PhysicsTable + " WorkID=" + workid;
				return "err@" + info;
			}
			wk.Row = rtp.Row;
		}

		if (nd.getHisFlow().getIsMD5() && wk.IsPassCheckMD5() == false)
		{
			String err = "打开(" + nd.getName() + ")错误";
			err += "当前的节点数据已经被篡改，请报告管理员。";
			return "err@" + err;
		}

		nd.WorkID = this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.



		if (nd.getHisFormType() == NodeFormType.FreeForm)
		{
			MapData md = new MapData(nd.getNodeFrmID());
			if (md.HisFrmType != FrmType.FreeFrm)
			{
				md.HisFrmType = FrmType.FreeFrm;
				md.Update();
			}
		}
		else if (nd.getHisFormType() == NodeFormType.FoolForm)
		{
			nd.WorkID = this.getWorkID(); //为获取表单ID ( NodeFrmID )提供参数.
			MapData md = new MapData(nd.getNodeFrmID());
			if (md.HisFrmType != FrmType.FoolForm)
			{
				md.HisFrmType = FrmType.FoolForm;
				md.Update();
			}
		}
		String endUrl = "";

		if (gwf.atPara.HisHT.size() > 0)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			for (var item : gwf.atPara.HisHT.keySet())
			{
				endUrl += "&" + item + "=" + gwf.atPara.HisHT[item];
			}
		}



		//加入是累加表单的标志，目的是让附件可以看到.

		if (nd.getHisFormType() == NodeFormType.FoolTruck)
		{
			endUrl = "&FormType=10&FromWorkOpt=" + this.GetRequestVal("FromWorkOpt");
		}

		return "url@./CCForm/Frm.htm?FK_MapData=" + nd.getNodeFrmID() + "&OID=" + wk.getOID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + nd.getNodeID() + "&PK=OID&PKVal=" + wk.getOID() + "&IsEdit=0&IsLoadData=0&IsReadonly=1" + endUrl;
	}
	/** 
	 草稿
	 
	 @return 
	*/
	public final String Draft_Init()
	{
		DataTable dt = null;
		dt = BP.WF.Dev2Interface.DB_GenerDraftDataTable();
		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 删除草稿.
	 
	 @return 
	*/
	public final String Draft_Delete()
	{
		return BP.WF.Dev2Interface.Flow_DoDeleteDraft(this.getFK_Flow(), this.getWorkID(), false);
	}
	/** 
	 获得会签列表
	 
	 @return 
	*/
	public final String HuiQianList_Init()
	{
		String sql = "SELECT A.WorkID, A.Title,A.FK_Flow, A.FlowName, A.Starter, A.StarterName, A.Sender, A.Sender,A.FK_Node,A.NodeName,A.SDTOfNode,A.TodoEmps";
		sql += " FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B ";
		sql += " WHERE A.WorkID=B.WorkID and a.FK_Node=b.FK_Node ";
		sql += " AND (B.IsPass=90 OR A.AtPara LIKE '%HuiQianZhuChiRen=" + WebUser.getNo() + "%') ";
		sql += " AND B.FK_Emp=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp";

		Paras ps = new Paras();
		ps.Add("FK_Emp", WebUser.getNo());
		ps.SQL = sql;
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
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
		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 协作模式待办
	 
	 @return 
	*/
	public final String TeamupList_Init()
	{
		String sql = "SELECT A.WorkID, A.Title,A.FK_Flow, A.FlowName, A.Starter, A.StarterName, A.Sender, A.Sender,A.FK_Node,A.NodeName,A.SDTOfNode,A.TodoEmps";
		sql += " FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B, WF_Node C ";
		sql += " WHERE A.WorkID=B.WorkID and a.FK_Node=b.FK_Node AND A.FK_Node=C.NodeID AND C.TodolistModel=1 ";
		sql += " AND B.IsPass=0 AND B.FK_Emp=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
		//   sql += " AND B.IsPass=0 AND B.FK_Emp=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp";

		Paras ps = new Paras();
		ps.Add("FK_Emp", WebUser.getNo());
		ps.SQL = sql;
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
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
		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 获得加签人的待办
	 @LQ 
	 
	 @return 
	*/
	public final String HuiQianAdderList_Init()
	{
		String sql = "SELECT A.WorkID, A.Title,A.FK_Flow, A.FlowName, A.Starter, A.StarterName, A.Sender, A.Sender,A.FK_Node,A.NodeName,A.SDTOfNode,A.TodoEmps";
		sql += " FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B, WF_Node C ";
		sql += " WHERE A.WorkID=B.WorkID and a.FK_Node=b.FK_Node AND B.IsPass=0 AND B.FK_Emp=" + SystemConfig.getAppCenterDBVarStr() + "FK_Emp";
		sql += " AND B.AtPara LIKE '%IsHuiQian=1%' ";
		sql += " AND A.FK_Node=C.NodeID ";
		sql += " AND C.TodolistModel= 4";

		Paras ps = new Paras();
		ps.Add("FK_Emp", WebUser.getNo());
		ps.SQL = sql;
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
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
		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 初始化待办.
	 
	 @return 
	*/
	public final String Todolist_Init()
	{
		String fk_node = this.GetRequestVal("FK_Node");
		String showWhat = this.GetRequestVal("ShowWhat");
		DataTable dt = BP.WF.Dev2Interface.DB_GenerEmpWorksOfDataTable(WebUser.getNo(), this.getFK_Node(), showWhat);
		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 获得授权人的待办.
	 
	 @return 
	*/
	public final String Todolist_Author()
	{
		DataTable dt = null;
		dt = BP.WF.Dev2Interface.DB_GenerEmpWorksOfDataTable(this.getNo(), this.getFK_Node());

		//转化大写的toJson.
		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 初始化
	 
	 @return 
	*/
	public final String TodolistOfAuth_Init()
	{
		return "err@尚未重构完成.";

		//DataTable dt = null;
		//foreach (WFEmp item in ems)
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
		//    dt.Columns.Remove("SID");
		//    dt.Columns.Remove("Stas");
		//    dt.Columns.Remove("Depts");
		//    dt.Columns.Remove("Msg");
		//    return BP.Tools.Json.DataTableToJson(dt, false);
		//}

	}
	/** 
	 获得挂起.
	 
	 @return 
	*/
	public final String HungUpList_Init()
	{
		DataTable dt = null;
		dt = BP.WF.Dev2Interface.DB_GenerHungUpList();

		//转化大写的toJson.
		return BP.Tools.Json.ToJson(dt);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 获得列表.


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 共享任务池.
	/** 
	 初始化共享任务
	 
	 @return 
	*/
	public final String TaskPoolSharing_Init()
	{
		DataTable dt = BP.WF.Dev2Interface.DB_TaskPool();

		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 申请任务.
	 
	 @return 
	*/
	public final String TaskPoolSharing_Apply()
	{
		boolean b = BP.WF.Dev2Interface.Node_TaskPoolTakebackOne(this.getWorkID());
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
	public final String TaskPoolApply_Init()
	{
		DataTable dt = BP.WF.Dev2Interface.DB_TaskPoolOfMyApply();

		return BP.Tools.Json.ToJson(dt);
	}
	public final String TaskPoolApply_PutOne()
	{
		BP.WF.Dev2Interface.Node_TaskPoolPutOne(this.getWorkID());
		return "放入成功,其他的同事可以看到这件工作.您可以在任务池里看到它并重新申请下来.";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 登录相关.
	/** 
	 返回当前会话信息.
	 
	 @return 
	*/
	public final String Login_Init()
	{
		Hashtable ht = new Hashtable();

		if (WebUser.getNo()OfRel == null)
		{
			ht.put("UserNo", "");
		}
		else
		{
			ht.put("UserNo", WebUser.getNo());
		}

		if (WebUser.getIsAuthorize())
		{
			ht.put("Auth", WebUser.Auth);
		}
		else
		{
			ht.put("Auth", "");
		}

		return BP.Tools.Json.ToJson(ht);
	}
	/** 
	 执行登录.
	 
	 @return 
	*/
	public final String LoginSubmit()
	{
		BP.Port.Emp emp = new BP.Port.Emp();
		emp.setNo (this.GetValFromFrmByKey("TB_No");

		if (emp.RetrieveFromDBSources() == 0)
		{
			return "err@用户名或密码错误.";
		}

		String pass = this.GetValFromFrmByKey("TB_PW");
		if (emp.Pass.equals(pass) == false)
		{
			return "err@用户名或密码错误.";
		}

		//让其登录.
		BP.WF.Dev2Interface.Port_Login(emp.No);

		return "登录成功.";
	}
	/** 
	 执行授权登录
	 
	 @return 
	*/
	public final String AuthorList_LoginAs()
	{
		WFEmp wfemp = new WFEmp(this.getNo());

		//if (wfemp.AuthorIsOK == false)
		//   return "err@授权登录失败！";

		BP.Port.Emp emp1 = new BP.Port.Emp(this.getNo());
		WebUser.SignInOfGener(emp1, "CH", false, false, WebUser.getNo(), WebUser.getName());

		return "授权登录成功！";
	}
	/** 
	 批处理审批
	 
	 @return 
	*/
	public final String Batch_Init()
	{
		String fk_node = GetRequestVal("FK_Node");

		//没有传FK_Node
		if (DataType.IsNullOrEmpty(fk_node))
		{
			String sql = "SELECT a.NodeID, a.Name,a.FlowName, COUNT(WorkID) AS NUM  FROM WF_Node a, WF_EmpWorks b WHERE A.NodeID=b.FK_Node AND B.FK_Emp='" + WebUser.getNo() + "' AND b.WFState NOT IN (7) AND a.BatchRole!=0 GROUP BY A.NodeID, a.Name,a.FlowName ";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			return BP.Tools.Json.ToJson(dt);
		}


		return "";
	}

	public final String BatchList_Init()
	{
		DataSet ds = new DataSet();

		String FK_Node = GetRequestVal("FK_Node");

		//获取节点信息
		BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());
		Flow fl = nd.getHisFlow();
		ds.Tables.add(nd.ToDataTableField("WF_Node"));

		String sql = "";

		if (nd.getHisRunModel() == RunModel.SubThread)
		{
			sql = "SELECT a.*, b.Starter,b.ADT,b.WorkID FROM " + fl.getPTable() + " a , WF_EmpWorks b WHERE a.OID=B.FID AND b.WFState Not IN (7) AND b.FK_Node=" + nd.getNodeID() + " AND b.FK_Emp='" + WebUser.getNo() + "'";
		}
		else
		{
			sql = "SELECT a.*, b.Starter,b.ADT,b.WorkID FROM " + fl.getPTable() + " a , WF_EmpWorks b WHERE a.OID=B.WorkID AND b.WFState Not IN (7) AND b.FK_Node=" + nd.getNodeID() + " AND b.FK_Emp='" + WebUser.getNo() + "'";
		}

		//获取待审批的流程信息集合
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Batch_List";
		ds.Tables.add(dt);

		//获取按钮权限
		BtnLab btnLab = new BtnLab(this.getFK_Node());

		ds.Tables.add(btnLab.ToDataTableField("Sys_BtnLab"));

		//获取报表数据
		String inSQL = "SELECT WorkID FROM WF_EmpWorks WHERE FK_Emp='" + WebUser.getNo() + "' AND WFState!=7 AND FK_Node=" + this.getFK_Node();
		Works wks = nd.getHisWorks();
		wks.RetrieveInSQL(inSQL);

		ds.Tables.add(wks.ToDataTableField("WF_Work"));

		//获取字段属性
		MapAttrs attrs = new MapAttrs("ND" + this.getFK_Node());

		//获取实际中需要展示的列
		String batchParas = nd.getBatchParas();
		MapAttrs realAttr = new MapAttrs();
		if (DataType.IsNullOrEmpty(batchParas) == false)
		{
			String[] strs = batchParas.split("[,]", -1);
			for (String str : strs)
			{
				if (DataType.IsNullOrEmpty(str) || str.contains("@PFlowNo") == true)
				{
					continue;
				}

				for (MapAttr attr : attrs)
				{
					if (!str.equals(attr.KeyOfEn))
					{
						continue;
					}

					realAttr.AddEntity(attr);
				}
			}
		}

		ds.Tables.add(realAttr.ToDataTableField("Sys_MapAttr"));

		return BP.Tools.Json.ToJson(ds);
	}

	/** 
	 批量发送
	 
	 @return 
	*/
	public final String Batch_Send()
	{
		BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());
		String[] strs = nd.getBatchParas().split("[,]", -1);

		MapAttrs attrs = new MapAttrs("ND" + this.getFK_Node());

		//获取数据
		String sql = String.format("SELECT Title,RDT,ADT,SDT,FID,WorkID,Starter FROM WF_EmpWorks WHERE FK_Emp='%1$s' and FK_Node='%2$s'", WebUser.getNo(), this.getFK_Node());

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		int idx = -1;
		String msg = "";
		for (DataRow dr : dt.Rows)
		{
			idx++;
			if (idx == nd.getBatchListCount())
			{
				break;
			}

			long workid = Long.parseLong(dr.get("WorkID").toString());
			String cb = this.GetValFromFrmByKey("CB_" + workid, "0");
			if (cb.equals("on"))
			{
				cb = "1";
			}
			else
			{
				cb = "0";
			}

			if (cb.equals("0")) //没有选中
			{
				continue;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 给字段赋值
			Hashtable ht = new Hashtable();
			for (String str : strs)
			{
				if (DataType.IsNullOrEmpty(str))
				{
					continue;
				}

				for (MapAttr attr : attrs)
				{
					if (!str.equals(attr.KeyOfEn))
					{
						continue;
					}

					if (attr.MyDataType == DataType.AppDateTime || attr.MyDataType == DataType.AppDate)
					{

						String val = this.GetValFromFrmByKey("TB_" + workid + "_" + attr.KeyOfEn, null);
						ht.put(str, val);
						continue;
					}


					if (attr.UIContralType == BP.En.UIContralType.TB && attr.UIIsEnable == true)
					{
						String val = this.GetValFromFrmByKey("TB_" + workid + "_" + attr.KeyOfEn, null);
						ht.put(str, val);
						continue;
					}

					if (attr.UIContralType == BP.En.UIContralType.DDL && attr.UIIsEnable == true)
					{
						String val = this.GetValFromFrmByKey("DDL_" + workid + "_" + attr.KeyOfEn);
						ht.put(str, val);
						continue;
					}

					if (attr.UIContralType == BP.En.UIContralType.CheckBok && attr.UIIsEnable == true)
					{
						String val = this.GetValFromFrmByKey("CB_" + +workid + "_" + attr.KeyOfEn, "-1");
						if (val.equals("-1"))
						{
							ht.put(str, 0);
						}
						else
						{
							ht.put(str, 1);
						}

						continue;
					}
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 给字段赋值

			//获取审核意见的值

			String checkNote = this.GetValFromFrmByKey("TB_" + workid + "_WorkCheck_Doc", null);
			if (DataType.IsNullOrEmpty(checkNote) == false)
			{
				BP.WF.Dev2Interface.WriteTrackWorkCheck(nd.getFK_Flow(), nd.getNodeID(), workid, Long.parseLong(dr.get("FID").toString()), checkNote, null);
			}

			msg += "@对工作(" + dr.get("Title") + ")处理情况如下";
			BP.WF.SendReturnObjs objs = BP.WF.Dev2Interface.Node_SendWork(nd.getFK_Flow(), workid, ht);
			msg += objs.ToMsgOfHtml();
			msg += "<br/>";
		}

		if (msg.equals(""))
		{
			msg = "没有选择需要处理的工作";
		}

		return msg;
	}

	/** 
	 批量退回 待定
	 
	 @return 
	*/
	public final String Batch_Return()
	{
		BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());
		String[] strs = nd.getBatchParas().split("[,]", -1);

		MapAttrs attrs = new MapAttrs("ND" + this.getFK_Node());

		//获取数据
		String sql = String.format("SELECT Title,RDT,ADT,SDT,FID,WorkID,Starter FROM WF_EmpWorks WHERE FK_Emp='%1$s' and FK_Node='%2$s'", WebUser.getNo(), this.getFK_Node());

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		int idx = -1;
		String msg = "";
		for (DataRow dr : dt.Rows)
		{
			idx++;
			if (idx == nd.getBatchListCount())
			{
				break;
			}

			long workid = Long.parseLong(dr.get("WorkID").toString());
			String cb = this.GetValFromFrmByKey("CB_" + workid, "0");
			if (cb.equals("0")) //没有选中
			{
				continue;
			}

			msg += "@对工作(" + dr.get("Title") + ")处理情况如下。<br>";
			BP.WF.SendReturnObjs objs = null; // BP.WF.Dev2Interface.Node_ReturnWork(nd.FK_Flow, workid,fid,this.FK_Node,"批量退回");
			msg += objs.ToMsgOfHtml();
			msg += "<hr>";

		}
		return "工作在完善中";
	}

	/** 
	 授权列表
	 
	 @return 
	*/
	public final String AuthorTodolist_Init()
	{

		return "";
	}

	/** 
	 授权列表
	 
	 @return 
	*/
	public final String AuthorTodolist_Todolist()
	{

		return "";
	}

	/** 
	 批量删除
	 
	 @return 
	*/
	public final String Batch_Delete()
	{
		BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());
		String[] strs = nd.getBatchParas().split("[,]", -1);

		MapAttrs attrs = new MapAttrs("ND" + this.getFK_Node());

		//获取数据
		String sql = String.format("SELECT Title,RDT,ADT,SDT,FID,WorkID,Starter FROM WF_EmpWorks WHERE FK_Emp='%1$s' and FK_Node='%2$s'", WebUser.getNo(), this.getFK_Node());

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		int idx = -1;
		String msg = "";
		for (DataRow dr : dt.Rows)
		{
			idx++;
			if (idx == nd.getBatchListCount())
			{
				break;
			}

			long workid = Long.parseLong(dr.get("WorkID").toString());
			String cb = this.GetValFromFrmByKey("CB_" + workid, "0");
			if (cb.equals("0")) //没有选中
			{
				continue;
			}

			msg += "@对工作(" + dr.get("Title") + ")处理情况如下。<br>";
			String mes = BP.WF.Dev2Interface.Flow_DoDeleteFlowByFlag(nd.getFK_Flow(), workid, "批量退回", true);
			msg += mes;
			msg += "<hr>";

		}
		if (msg.equals(""))
		{
			msg = "没有选择需要处理的工作";
		}

		return "批量删除成功" + msg;
	}


	/** 
	 退出登录
	 
	 @param UserNo
	 @param Author
	 @return 
	*/
	public final String AuthExitAndLogin(String UserNo, String Author)
	{
		String msg = "suess@退出成功！";
		try
		{
			BP.Port.Emp emp = new BP.Port.Emp(UserNo);
			//首先退出
			WebUser.Exit();
			//再进行登录
			BP.Port.Emp emp1 = new BP.Port.Emp(Author);
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
	public final String AuthorList_Init()
	{
		Paras ps = new Paras();
		ps.SQL = "SELECT No,Name,AuthorDate FROM WF_Emp WHERE AUTHOR=" + SystemConfig.getAppCenterDBVarStr() + "AUTHOR";
		ps.Add("AUTHOR", WebUser.getNo());
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("AUTHORDATE").ColumnName = "AuthorDate";
		}
		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 当前登陆人是否有授权
	 
	 @return 
	*/
	public final String IsHaveAuthor()
	{
		Paras ps = new Paras();
		ps.SQL = "SELECT * FROM WF_EMP WHERE AUTHOR=" + SystemConfig.getAppCenterDBVarStr() + "AUTHOR";
		ps.Add("AUTHOR", WebUser.getNo());
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		WFEmp em = new WFEmp();
		em.Retrieve(WFEmpAttr.Author, WebUser.getNo());

		if (dt.Rows.size() > 0 && WebUser.getIsAuthorize() == false)
		{
			return "suess@有授权";
		}
		else
		{
			return "err@没有授权";
		}
	}
	/** 
	 退出.
	 
	 @return 
	*/
	public final String LoginExit()
	{
		BP.WF.Dev2Interface.Port_SigOut();
		return null;
	}
	/** 
	 授权退出.
	 
	 @return 
	*/
	public final String AuthExit()
	{
		return this.AuthExitAndLogin(this.getNo(), WebUser.Auth);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 登录相关.

	/** 
	 获得抄送列表
	 
	 @return 
	*/
	public final String CC_Init()
	{
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
		//BP.En.QueryObject qo = new BP.En.QueryObject(ss);

		System.Data.DataTable dt = null;
		if (sta.equals("-1"))
		{
			dt = BP.WF.Dev2Interface.DB_CCList(WebUser.getNo());
		}

		if (sta.equals("0"))
		{
			dt = BP.WF.Dev2Interface.DB_CCList_UnRead(WebUser.getNo());
		}

		if (sta.equals("1"))
		{
			dt = BP.WF.Dev2Interface.DB_CCList_Read(WebUser.getNo());
		}

		if (sta.equals("2"))
		{
			dt = BP.WF.Dev2Interface.DB_CCList_Delete(WebUser.getNo());
		}

		//int allNum = qo.GetCount();
		//qo.DoQuery(BP.WF.SMSAttr.MyPK, pageSize, pageIdx);

		return BP.Tools.Json.ToJson(dt);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 处理page接口.
	/** 
	 执行的内容
	*/
	public final String getDoWhat()
	{
		return this.GetRequestVal("DoWhat");
	}
	/** 
	 当前的用户
	*/
	public final String getUserNo()
	{
		return this.GetRequestVal("UserNo");
	}
	/** 
	 用户的安全校验码(请参考集成章节)
	*/
	public final String getSID()
	{
		return this.GetRequestVal("SID");
	}
	/** 
	 调用页面入口
	 
	 @return 
	*/
	public final String Port_Init()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 安全性校验.
		//if (this.UserNo == null )
		//    return "err@必要的参数没有传入，请参考接口规则。UserNo";

		if (this.getSID() == null)
		{
			return "err@必要的参数没有传入，请参考接口规则。SID";
		}

		if (this.getDoWhat() == null)
		{
			return "err@必要的参数没有传入，请参考接口规则。DoWhat";
		}

		if (BP.WF.Dev2Interface.Port_CheckUserLogin(this.getUserNo(), this.getSID()) == false)
		{
			return "err@非法的访问，请与管理员联系。SID=" + this.getSID();
		}

		if (DataType.IsNullOrEmpty(WebUser.getNo()) == true || WebUser.getNo().equals(this.getUserNo()) == false)
		{
			BP.WF.Dev2Interface.Port_SigOut();
			try
			{
				BP.WF.Dev2Interface.Port_Login(this.getUserNo());
			}
			catch (RuntimeException ex)
			{
				return "err@安全校验出现错误:" + ex.getMessage();
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 安全性校验.

		if (this.getDoWhat().equals("PortLogin") == true)
		{
			return "登陆成功";
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 生成参数串.
		String paras = "";
		for (String str : HttpContextHelper.RequestQueryStringKeys)
		{
			String val = this.GetRequestVal(str);
			if (val.indexOf('@') != -1)
			{
				return "err@您没有能参数: [ " + str + " ," + val + " ] 给值 ，URL 将不能被执行。";
			}

			switch (str)
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
				case "SID":
					break;
				default:
					paras += "&" + str + "=" + val;
					break;
			}
		}
		String nodeID = Integer.parseInt(this.getFK_Flow() + "01").toString();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
			if (this.getFK_Flow() == null || this.getWorkID() == null)
			{
				throw new RuntimeException("@参数 FK_Flow 或者 WorkID 为 Null 。");
			}

			return "url@WFRpt.htm?FK_Flow=" + this.getFK_Flow() + "&WorkID=" + this.getWorkID() + "&o2=1" + paras;
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
			BP.WF.SMS sms = new SMS();
			sms.setMyPK( guid;
			sms.Retrieve();

			//判断当前的登录人员.
			if (!sms.getSendToEmpNo().equals(WebUser.getNo()))
			{
				BP.WF.Dev2Interface.Port_Login(sms.getSendToEmpNo());
			}

			BP.DA.AtPara ap = new AtPara(sms.getAtPara());
			switch (sms.getMsgType())
			{
				case SMSMsgType.SendSuccess: // 发送成功的提示.

					if (BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(ap.GetValInt64ByKey("WorkID"), WebUser.getNo()) == true)
					{
						return "url@MyFlow.htm?FK_Flow=" + ap.GetValStrByKey("FK_Flow") + "&WorkID=" + ap.GetValStrByKey("WorkID") + "&o2=1" + paras;
					}
					else
					{
						return "url@WFRpt.htm?FK_Flow=" + ap.GetValStrByKey("FK_Flow") + "&WorkID=" + ap.GetValStrByKey("WorkID") + "&o2=1" + paras;
					}

				default: //其他的情况都是查看工作报告.
					return "url@WFRpt.htm?FK_Flow=" + ap.GetValStrByKey("FK_Flow") + "&WorkID=" + ap.GetValStrByKey("WorkID") + "&o2=1" + paras;
			}
		}

		return "err@没有约定的标记:DoWhat=" + this.getDoWhat();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 处理page接口.

}
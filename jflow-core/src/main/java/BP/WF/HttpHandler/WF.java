package BP.WF.HttpHandler;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.swing.Spring;

import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HttpContext;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Log;
import BP.DA.Paras;
import BP.En.QueryObject;
import BP.Sys.AthCtrlWay;
import BP.Sys.AthUploadWay;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentAttr;
import BP.Sys.FrmAttachmentDBAttr;
import BP.Sys.FrmAttachments;
import BP.Sys.FrmImgAthDBs;
import BP.Sys.FrmSubFlowAttr;
import BP.Sys.FrmWorkCheckAttr;
import BP.Sys.MapData;
import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import BP.Sys.MapExts;
import BP.Sys.SysEnum;
import BP.Sys.SysEnumAttr;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.WF.Dev2Interface;
import BP.WF.DoWhatList;
import BP.WF.DotNetToJavaStringHelper;
import BP.WF.Flow;
import BP.WF.GenerWorkFlow;
import BP.WF.Node;
import BP.WF.NodeFormType;
import BP.WF.RunModel;
import BP.WF.SMS;
import BP.WF.SMSMsgType;
import BP.WF.Track;
import BP.WF.WFSta;
import BP.WF.Work;
import BP.WF.Data.GERpt;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Port.Inc;
import BP.WF.Port.WFEmp;
import BP.WF.Port.WFEmpAttr;
import BP.WF.Template.FTCAttr;
import BP.WF.Template.FlowSortAttr;
import BP.WF.Template.FlowSorts;
import BP.WF.Template.FrmNodeComponent;
import BP.WF.Template.FrmThreadAttr;
import BP.WF.Template.FrmTrackAttr;
import BP.WF.Template.Frms;
import BP.Web.WebUser;

public class WF extends WebContralBase
{
	/**
	 * 初始化数据
	 * @param mycontext
	 */
    public WF(HttpContext mycontext)
    {
        this.context = mycontext;
    }
  
	/**
	 * 初始化实体
	 * 无参构造器
	 */
    public WF() {
	}


	/**
     *  入口函数
     */
    public String DoDefaultMethod()
    {
        String msg = "";
        try
        {
            if("LoginExit".equals(getDoType())) //退出安全登录.
            {
            	BP.WF.Dev2Interface.Port_SigOut();
            }else  if("AuthExit".equals(getDoType())) 
            {
            	 msg = this.AuthExitAndLogin(this.getNo(), BP.Web.WebUser.getAuth());
            }else 
            {
            	msg = "err@没有判断的标记:" + this.getDoType();
            }
        }
        catch (Exception ex)
        {
            msg = "err@" + ex.getMessage();
        }
		return msg;
    }
    
    public boolean getIsReusable()
    {
    	return false;
    }

    /**
     * 运行
     * <param name="UserNo">人员编号</param>
     * <param name="fk_flow">流程编号</param>
     * <returns>运行中的流程</returns>
     */
    public String Runing_Init()
    {
    	AppACE page = new AppACE(context);
        return page.Runing_Init();
    }
    
    /// <summary>
    /// 打开表单
    /// </summary>
    /// <returns></returns>
    public String Runing_OpenFrm()
    {
    	//return "err@于庆海翻译 Runing_OpenFrm";
    	
    	
        //String appPath = BP.WF.Glo.CCFlowAppPath;
        Node nd = null;
        Track tk = new Track();
        tk.setFK_Flow( this.getFK_Flow());
        tk.setNDFrom( this.getFK_Node());

        tk.setWorkID( this.getWorkID());
        if (this.getMyPK() != null)
        {
            tk = new Track(this.getFK_Flow(), this.getMyPK());
            nd = new Node( tk.getNDFrom() );
        }
        else
        {
            nd = new Node(this.getFK_Node());
        }

        Flow fl = new Flow(this.getFK_Flow());
        
        long workid = 0;
        
        if (nd.getHisRunModel() == RunModel.SubThread)
            workid = tk.getFID();
        else
            workid = tk.getWorkID();

        long fid = this.getFID();
        if (this.getFID() == 0)
            fid = tk.getFID();

        if (fid > 0)
            workid = fid;

        String urlExt = "";
        DataTable ndrpt = DBAccess.RunSQLReturnTable("SELECT PFlowNo,PWorkID FROM " + fl.getPTable() + " WHERE OID=" + workid);
        if (ndrpt.Rows.size() == 0)
            urlExt = "&PFlowNo=0&PWorkID=0&IsToobar=0&IsHidden=true";
        else
            urlExt = "&PFlowNo=" + ndrpt.Rows.get(0).getValue("PFlowNo") + "&PWorkID=" + ndrpt.Rows.get(0).getValue("PWorkID") + "&IsToobar=0&IsHidden=true";
        urlExt += "&From=CCFlow&TruckKey=" + tk.GetValStrByKey("MyPK") + "&DoType=" + this.getDoType() + "&UserNo=" +  WebUser.getNo() + "&SID=" + WebUser.getSID() ;

        if (nd.getHisFormType() == NodeFormType.SDKForm || nd.getHisFormType() == NodeFormType.SelfForm)
        {
            //added by liuxc,2016-01-25.
            if (nd.getFormUrl().contains("?"))
                return "url@" + nd.getFormUrl() + "&IsReadonly=1&WorkID=" + workid + "&FK_Node=" + nd.getNodeID() + "&FK_Flow=" + nd.getFK_Flow() + "&FID=" + fid + urlExt;

            return "url@" + nd.getFormUrl() + "?IsReadonly=1&WorkID=" + workid + "&FK_Node=" + nd.getNodeID() + "&FK_Flow=" + nd.getFK_Flow() + "&FID=" + fid + urlExt;
        }

        Work wk = nd.getHisWork();
        wk.setOID( workid);
        if (wk.RetrieveFromDBSources() == 0)
        {
            GERpt rtp = nd.getHisFlow().getHisGERpt();
            rtp.setOID( workid);
            if (rtp.RetrieveFromDBSources() == 0)
            {
                String info = "打开(" + nd.getName() + ")错误";
                info += "当前的节点数据已经被删除！！！<br> 造成此问题出现的原因如下。";
                info += "1、当前节点数据被非法删除。";
                info += "2、节点数据是退回人与被退回人中间的节点，这部分节点数据查看不支持。";
                info += "技术信息:表" + wk.getEnMap().getPhysicsTable() + " WorkID=" + workid;
                return "err@" + info;
            }
            wk.setRow( rtp.getRow());
        }

        GenerWorkFlow gwf = new GenerWorkFlow();
        gwf.setWorkID( wk.getOID());

        if (nd.getHisFlow().getIsMD5() && wk.IsPassCheckMD5() == false)
        {
            String err = "打开(" + nd.getName() + ")错误";
            err += "当前的节点数据已经被篡改，请报告管理员。";
            return "err@" + err;
        }

        Frms frms = nd.getHisFrms();
        if (frms.size() == 0)
            return "url@./CCForm/Frm.htm?FK_MapData=" + nd.getNodeFrmID() + "&OID=" + wk.getOID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node() + "&PK=OID&PKVal=" + wk.getOID() + "&IsEdit=0&IsLoadData=0&IsReadonly=1";

        return "url@./MyFlowTreeReadonly.htm?3=3" + this.getRequestParas();
        
        
    }
    
    /**
     * 挂起列表
     * <param name="userNo">用户编号</param>
     * <param name="fk_flow">流程编号</param>
     * <returns>挂起列表</returns>
     */
    public String HungUpList_Init()
    {
        DataTable dt = null;
        dt = BP.WF.Dev2Interface.DB_GenerHungUpList();
        return BP.Tools.Json.ToJson(dt);
    }
    
    /**
     * 草稿
     * @return
     */
    public String Draft_Init()
    {
        DataTable dt = null;
        dt = BP.WF.Dev2Interface.DB_GenerDraftDataTable();

        //转化大写.
        return BP.Tools.Json.DataTableToJson(dt,false);
    }
  
    
    /** 
	 获得会签列表
	 
	 @return 
*/
	public final String HuiQianList_Init()
	{
		String sql = "SELECT A.WorkID, A.Title,A.FK_Flow, A.FlowName, A.Starter, A.StarterName, A.Sender, A.Sender,A.FK_Node,A.NodeName,A.SDTOfNode,A.TodoEmps";
		sql += " FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID and a.FK_Node=b.FK_Node AND B.IsPass=90 AND B.FK_Emp='"+BP.Web.WebUser.getNo()+"'";

		DataTable dt=DBAccess.RunSQLReturnTable(sql);
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
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
     * 获得授权人的待办.
     * @return
     * 111111
     */
    public String Todolist_Author()
    {
        DataTable dt = null;
        try {
			dt = BP.WF.Dev2Interface.DB_GenerEmpWorksOfDataTable(this.getNo(), this.getFK_Node());
		} catch (Exception e) {
			Log.DebugWriteError("WF Todolist_Author():"+e.getMessage());
			e.printStackTrace();
		}

        //转化大写的toJson.
        return BP.Tools.Json.DataTableToJson(dt,true);
    }            
    /**
     * 获得待办.
     * @return
     */
    public String Todolist_Init()
    {
        DataTable dt = null;

        try {
			dt = BP.WF.Dev2Interface.DB_GenerEmpWorksOfDataTable(BP.Web.WebUser.getNo(), this.getFK_Node());
		} catch (Exception e) {
			Log.DebugWriteError("WF Todolist_Init():"+e.getMessage());
			e.printStackTrace();
		}

        return BP.Tools.Json.DataTableToJson(dt,false,false,true);
    }

    
//    public final String Todolist_Init()
//	{
//		AppACE en = new AppACE(context);
//		return en.Todolist_Init();
//	}
    
    /**
     * 返回当前会话信息.
     * @return
     */
    public String LoginInit()
    {
        Hashtable ht = new Hashtable();

        if (BP.Web.WebUser.getNo() == null)
            ht.put("UserNo", "");
        else
            ht.put("UserNo", BP.Web.WebUser.getNo());

        if (BP.Web.WebUser.getIsAuthorize())
            ht.put("Auth", BP.Web.WebUser.getAuth());
        else
            ht.put("Auth", "");
        return BP.Tools.Json.ToJsonEntityModel(ht);
    }

    /**
     * 执行登录.
     * @return
     */
    public String LoginSubmit()
    {
        BP.Port.Emp emp = new BP.Port.Emp();
        emp.setNo(this.GetValFromFrmByKey("TB_UserNo"));

        if (emp.RetrieveFromDBSources() == 0)
            return "err@用户名或密码错误.";
        String pass = this.GetValFromFrmByKey("TB_Pass");
        if (emp.getPass().equals(pass) == false)
            return "err@用户名或密码错误.";

        //让其登录.
        String sid = BP.WF.Dev2Interface.Port_Login(emp.getNo());
        return sid;
    }

    /**
     * 执行授权登录
     * @return
     */
    public String LoginAs()
    {
        BP.WF.Port.WFEmp wfemp = new BP.WF.Port.WFEmp(this.getNo());
        if (wfemp.getAuthorIsOK() == false)
            return "err@授权登录失败！";
        BP.Port.Emp emp1 = new BP.Port.Emp(this.getNo());
        try {
			BP.Web.WebUser.SignInOfGener(emp1, "CH", false, false, BP.Web.WebUser.getNo(), BP.Web.WebUser.getName());
		} catch (UnsupportedEncodingException e) {
			Log.DebugWriteError("WF LoginAs():" + e.getMessage());
			e.printStackTrace();
		}
        return "success@授权登录成功！";
    }
    
    /**
     * 退出登录
     * @param UserNo
     * @param Author
     * @return
     */
    public String AuthExitAndLogin(String UserNo, String Author)
    {
        String msg = "suess@退出成功！";
        try
        {
            BP.Port.Emp emp = new BP.Port.Emp(UserNo);
            //首先退出
            BP.Web.WebUser.Exit();
            //再进行登录
            BP.Port.Emp emp1 = new BP.Port.Emp(Author);
            BP.Web.WebUser.SignInOfGener(emp1, "CH", false, false, null, null);
        }
        catch (Exception ex)
        {
            msg = "err@退出时发生错误。" + ex.getMessage();
        }
        return msg;
    }
    
    /**
     * 获取授权人列表
     * @return
     */
    public String Load_Author()
    {
        DataTable dt = BP.DA.DBAccess.RunSQLReturnTable("SELECT * FROM WF_EMP WHERE AUTHOR='" + BP.Web.WebUser.getNo() + "'");
        return BP.Tools.FormatToJson.ToJson(dt);
    }
    
    /**
     * 当前登陆人是否有授权
     * @return
     */
    public String IsHaveAuthor()
    {
        DataTable dt = BP.DA.DBAccess.RunSQLReturnTable("SELECT * FROM WF_EMP WHERE AUTHOR='" + BP.Web.WebUser.getNo() + "'");
        WFEmp em = new WFEmp();
        em.Retrieve(WFEmpAttr.Author, BP.Web.WebUser.getNo());

        if (dt.Rows.size() > 0 && BP.Web.WebUser.getIsAuthorize() == false)
            return "suess@有授权";
        else
            return "err@没有授权";
    }
    
    /** 获得发起列表
     * yqh add
	 @return 
     */
	public final String Start_Init()
	{
		//通用的处理器.
		if (BP.Sys.SystemConfig.getCustomerNo().equals("TianYe"))
		{
			
		}else{
			return Start_Init2016();
		}

		//如果请求了刷新.
		if (this.GetRequestVal("IsRef") != null)
		{
			//清除权限.
			DBAccess.RunSQL("UPDATE WF_Emp SET StartFlows='' WHERE No='" + BP.Web.WebUser.getNo() + "' ");

			//处理权限,为了防止未知的错误.
			DBAccess.RunSQL("UPDATE WF_FLOWSORT SET ORGNO='0' WHERE ORGNO='' OR ORGNO IS NULL OR ORGNO='101'");
		}
		// 周朋@于庆海需要翻译.

		BP.WF.Port.WFEmp em = new WFEmp();
		em.setNo(BP.Web.WebUser.getNo());
		if (em.RetrieveFromDBSources() == 0)
		{
			em.setFK_Dept(BP.Web.WebUser.getFK_Dept());
			em.setName(WebUser.getName());
			em.Insert();
		}
		String json = em.getStartFlows();
		if (!StringUtils.isEmpty(json))
		{
			return json;
		}



		//获得当前人员的部门,根据部门获得该人员的组织集合.
		Paras ps = new Paras();
		ps.SQL ="SELECT FK_Dept FROM Port_DeptEmp WHERE FK_Emp="+SystemConfig.getAppCenterDBVarStr()+"FK_Emp";
		ps.AddFK_Emp();
		DataTable dt = DBAccess.RunSQLReturnTable(ps);

		//为当前的人员找组织编号集合.
		String orgNos = "'0'";
		for (DataRow dr : dt.Rows)
		{
			String deptNo = dr.getValue(0).toString();
			orgNos += ",'" + deptNo + "'";
		}

		///#region 获取类别列表(根据当前人员所在组织结构进行过滤类别.)
		FlowSorts fss = new FlowSorts();
		QueryObject qo = new QueryObject(fss);
		if (orgNos.contains(",")==false)
		{
			qo.AddWhere(FlowSortAttr.OrgNo, "0"); //..
			qo.addOr();
			qo.AddWhere(FlowSortAttr.OrgNo, ""); //..
		}
		else
		{
			qo.AddWhereIn(FlowSortAttr.OrgNo, "("+orgNos+")"); //指定的类别.
		}

		//排序.
		qo.addOrderBy(FlowSortAttr.No, FlowSortAttr.Idx);


		DataTable dtSort=qo.DoQueryToTable();
		dtSort.TableName = "Sort";

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
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
		dtStart.Columns.Add("No",String.class);
		dtStart.Columns.Add("Name",String.class);
		dtStart.Columns.Add("FK_FlowSort",String.class);
		dtStart.Columns.Add("IsBatchStart",Integer.class);
		dtStart.Columns.Add("IsStartInMobile",Integer.class);

		//获得所有的流程（包含了所有子公司与集团的可以发起的流程但是没有根据组织结构进行过滤.）
		DataTable dtAllFlows = Dev2Interface.DB_StarFlows(WebUser.getNo());
		//按照当前用户的流程类别权限进行过滤.
		for (DataRow drSort : dtSort.Rows)
		{
			for (DataRow drFlow : dtAllFlows.Rows)
			{
				if (!drSort.getValue("No").toString().equals(drFlow.getValue("FK_FlowSort").toString()))
				{
					continue;
				}

				DataRow drNew = dtStart.NewRow();

				drNew.setValue("No", drFlow.getValue("No"));
				drNew.setValue("Name", drFlow.getValue("Name"));
				drNew.setValue("FK_FlowSort", drFlow.getValue("FK_FlowSort"));
				drNew.setValue("IsBatchStart", drFlow.getValue("IsBatchStart"));		
				drNew.setValue("IsStartInMobile", drFlow.getValue("IsStartInMobile"));
				dtStart.Rows.add(drNew); //增加到里里面去.
			}
		}

		//把经过权限过滤的流程实体放入到集合里.
		ds.Tables.add(dtStart); //增加到里面去.

		//返回组合
		json= BP.Tools.Json.DataSetToJson(ds, false);

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
	 yqh add
	 @return 
     */
	public final String Start_Init2016()
	{
		DataSet ds = new DataSet();

		//流程类别.
		FlowSorts fss = new FlowSorts();
		fss.RetrieveAll();
		DataTable dtSort = fss.ToDataTableField("Sort");
		dtSort.TableName = "Sort";
		ds.Tables.add(dtSort);

		//获得能否发起的流程.
		DataTable dtStart = Dev2Interface.DB_GenerCanStartFlowsOfDataTable(BP.Web.WebUser.getNo());
		dtStart.TableName = "Start";
		ds.Tables.add(dtStart);

		//返回组合
		return BP.Tools.Json.DataSetToJson(ds, false);
	}
	
	/** 初始化共享任务
	 yqh add
	 @return 
	 */
	public final String TaskPoolSharing_Init()
	{
	   DataTable dt = BP.WF.Dev2Interface.DB_TaskPool();

	   return BP.Tools.Json.DataTableToJson(dt, false);
	}
	
	/** 我申请下来的任务
	 yqh add
	 @return 
	 */
	public final String TaskPoolApply_Init()
	{
		DataTable dt = BP.WF.Dev2Interface.DB_TaskPoolOfMyApply();

		return BP.Tools.Json.DataTableToJson(dt, false);
	}
	
	/** 获得发起列表
	 
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
		ds.Tables.add(dtStart);

		//返回组合
		return BP.Tools.Json.DataSetToJson(ds, false);
	}
	
	/// <summary>
    /// 方法
    /// </summary>
    /// <returns></returns>
    public String HandlerMapExt()
    {
    	try
    	{
    		
         WF_CCForm wf = new WF_CCForm(context);
         return wf.HandlerMapExt();
         
    	}catch(Exception ex)
    	{
    		return "err@"+ex.getMessage();
    	}
    }
    
    /** 执行撤销
	 
	 @return 
*/
	public final String Runing_UnSend()
	{
		try
		{
			return BP.WF.Dev2Interface.Flow_DoUnSend(this.getFK_Flow(), this.getWorkID());
		}
		catch(RuntimeException ex)
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
			return BP.WF.Dev2Interface.Flow_DoPress(this.getWorkID(), this.GetRequestVal("Msg"),false);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	
	/** 获得抄送列表
	 
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

		DataTable dt = null;
		if (sta.equals("-1"))
		{
			dt = BP.WF.Dev2Interface.DB_CCList(BP.Web.WebUser.getNo());
		}
		if (sta.equals("0"))
		{
			dt = BP.WF.Dev2Interface.DB_CCList_UnRead(BP.Web.WebUser.getNo());
		}
		if (sta.equals("1"))
		{
			dt = BP.WF.Dev2Interface.DB_CCList_Read(BP.Web.WebUser.getNo());
		}
		if (sta.equals("2"))
		{
			dt = BP.WF.Dev2Interface.DB_CCList_Delete(BP.Web.WebUser.getNo());
		}

		//int allNum = qo.GetCount();
		//qo.DoQuery(BP.WF.SMSAttr.MyPK, pageSize, pageIdx);

		return BP.Tools.Json.DataTableToJson(dt, false);
	}
	
	/** 删除草稿.
	 
	 @return 
	 */
	public final String Draft_Delete()
	{
		return BP.WF.Dev2Interface.Flow_DoDeleteDraft(this.getFK_Flow(), this.getWorkID(), false);
	}
	
	/** 我的关注流程
	 
	 @return 
*/
	public final String Focus_Init()
	{
		String flowNo = this.GetRequestVal("FK_Flow");

		int idx = 0;
		//获得关注的数据.
		DataTable dt = BP.WF.Dev2Interface.DB_Focus(flowNo, BP.Web.WebUser.getNo());
		SysEnums stas = new SysEnums("WFSta");
		String[] tempArr;
		for (DataRow dr : dt.Rows)
		{
			int wfsta = Integer.parseInt(dr.getValue("WFSta").toString());
			//edit by liuxc,2016-10-22,修复状态显示不正确问题
			SysEnum tempVar = (SysEnum) stas.GetEntityByKey(SysEnumAttr.IntKey, wfsta);
			String wfstaT = tempVar.getLab();
			String currEmp = "";

			if (wfsta != WFSta.Complete.getValue())
			{
				//edit by liuxc,2016-10-24,未完成时，处理当前处理人，只显示处理人姓名
				for (String emp : dr.getValue("ToDoEmps").toString().split("[;]", -1))
				{
					tempArr = emp.split("[,]", -1);

					currEmp += tempArr.length > 1 ? tempArr[1] : tempArr[0] + ",";
				}

				currEmp = DotNetToJavaStringHelper.trimEnd(currEmp, ',');

				//currEmp = dr["ToDoEmps"].ToString();
				//currEmp = currEmp.TrimEnd(';');
			}
			dr.setValue("ToDoEmps",currEmp);
			dr.setValue("FlowNote",wfstaT);
			dr.setValue("AtPara", (wfsta == BP.WF.WFSta.Complete.getValue() ? DotNetToJavaStringHelper.trimEnd(DotNetToJavaStringHelper.trimStart(dr.getValue("Sender").toString(), '('), ')').split("[,]", -1)[1] : ""));
		}

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("NO").ColumnName = "";
			dt.Columns.get("NAME").ColumnName = "";
			dt.Columns.get("").ColumnName = "";
			dt.Columns.get("").ColumnName = "";
			dt.Columns.get("").ColumnName = "";
		}

		return BP.Tools.Json.DataTableToJson(dt, false);
	}
	
	/** 流程单表单查看
	 
	 @return 
*/
	public final String FrmView_Init()
	{
		Node nd = new Node(this.getFK_Node());


		MapData md = new MapData();
		md.setNo(nd.getNodeFrmID());
		if (md.RetrieveFromDBSources() == 0)
		{
			throw new RuntimeException("装载错误，该表单ID=" + md.getNo() + "丢失，请修复一次流程重新加载一次.");
		}



		//获得表单模版.
		DataSet myds = BP.Sys.CCFormAPI.GenerHisDataSet_2017(md.getNo());


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 把主从表数据放入里面.
		//.工作数据放里面去, 放进去前执行一次装载前填充事件.
		BP.WF.Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.RetrieveFromDBSources();

		//重设默认值.
		wk.ResetDefaultVal();

		DataTable mainTable = wk.ToDataTableField("MainTable");
		mainTable.TableName = "MainTable";
		myds.Tables.add(mainTable);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


		//加入WF_Node.
		DataTable WF_Node = nd.ToDataTableField("WF_Node");
		myds.Tables.add(WF_Node);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 加入组件的状态信息, 在解析表单的时候使用.
		BP.WF.Template.FrmNodeComponent fnc = new FrmNodeComponent(nd.getNodeID());
		if ( ! nd.getNodeFrmID().equals("ND" + nd.getNodeID()))
		{
			//说明这是引用到了其他节点的表单，就需要把一些位置元素修改掉.
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

		myds.Tables.add(fnc.ToDataTableField("WF_FrmNodeComponent"));
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 加入组件的状态信息, 在解析表单的时候使用.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 增加附件信息.
		BP.Sys.FrmAttachments athDescs = new FrmAttachments();
		athDescs.Retrieve(FrmAttachmentAttr.FK_MapData, nd.getNodeFrmID());
		if (athDescs.size() != 0)
		{
			FrmAttachment athDesc = (FrmAttachment)((athDescs.get(0) instanceof FrmAttachment) ? athDescs.get(0) : null);

			//查询出来数据实体.
			BP.Sys.FrmAttachmentDBs dbs = new BP.Sys.FrmAttachmentDBs();
			if (athDesc.getHisCtrlWay() == AthCtrlWay.PWorkID)
			{
				String pWorkID = DBAccess.RunSQLReturnValInt("SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + this.getWorkID(), 0) + "";
				if (pWorkID == null || pWorkID.equals("0"))
				{
					pWorkID = this.getWorkID() + "";
				}

				if (athDesc.getAthUploadWay() == AthUploadWay.Inherit)
				{
					// 继承模式 
					BP.En.QueryObject qo = new BP.En.QueryObject(dbs);
					qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, pWorkID);
					qo.addOr();
					qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, this.getWorkID());
					qo.addOrderBy("RDT");
					qo.DoQuery();
				}

				if (athDesc.getAthUploadWay() == AthUploadWay.Interwork)
				{
					//共享模式
					dbs.Retrieve(FrmAttachmentDBAttr.RefPKVal, pWorkID);
				}
			}
			else if (athDesc.getHisCtrlWay() == AthCtrlWay.WorkID)
			{
				// 继承模式 
				BP.En.QueryObject qo = new BP.En.QueryObject(dbs);
				qo.AddWhere(FrmAttachmentDBAttr.NoOfObj, athDesc.getNoOfObj());
				qo.addAnd();
				qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, this.getWorkID());
				qo.addOrderBy("RDT");
				qo.DoQuery();
			}

			//增加一个数据源.
			myds.Tables.add(dbs.ToDataTableField("Sys_FrmAttachmentDB"));
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 把外键表加入DataSet
		List<DataTable> dtMapAttrList = myds.getTables();
		DataTable dtMapAttr = null;
		for(DataTable dt:dtMapAttrList){
			if(dt.getTableName().equals("Sys_MapAttr"))
				dtMapAttr=dt;
		}
		MapExts mes = md.getMapExts();
		MapExt me = new MapExt();
		DataTable dt = new DataTable();
		for (DataRow dr : dtMapAttr.Rows)
		{
			String lgType = dr.get("LGType").toString();
			if (!lgType.equals("2"))
			{
				continue;
			}

			String UIIsEnable = dr.get("UIVisible").toString();
			if (UIIsEnable.equals("0"))
			{
				continue;
			}

			String uiBindKey = dr.get("UIBindKey").toString();
			if (DotNetToJavaStringHelper.isNullOrEmpty(uiBindKey) == true)
			{
				String myPK = dr.get("MyPK").toString();
				//如果是空的
				//   throw new Exception("@属性字段数据不完整，流程:" + fl.No + fl.Name + ",节点:" + nd.NodeID + nd.Name + ",属性:" + myPK + ",的UIBindKey IsNull ");
			}

			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.get("KeyOfEn").toString();
			String fk_mapData = dr.get("FK_MapData").toString();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理下拉框数据范围. for 小杨.
			Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
			me = (MapExt)((tempVar instanceof MapExt) ? tempVar : null);
			if (me != null)
			{
				Object tempVar2 = me.getDoc();
				String fullSQL = (String)((tempVar2 instanceof String) ? tempVar2 : null);
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
					drDll.setValue2017("No",dllRow.getValue_2017("No"));
					drDll.setValue2017("Name", dllRow.getValue_2017("Name"));
					dt_FK_Dll.Rows.AddRow(drDll);
				}
				myds.Tables.add(dt_FK_Dll);
				continue;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理下拉框数据范围.

			// 判断是否存在.
			if (myds.Tables.contains(uiBindKey) == true)
			{
				continue;
			}

			myds.Tables.add(BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey));
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion End把外键表加入DataSet

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 图片附件
		FrmImgAthDBs imgAthDBs = new FrmImgAthDBs(nd.getNodeFrmID(), this.getWorkID() + "");
		if (imgAthDBs != null && imgAthDBs.size() > 0)
		{
			DataTable dt_ImgAth = imgAthDBs.ToDataTableField("Sys_FrmImgAthDB");
			myds.Tables.add(dt_ImgAth);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


		return BP.Tools.Json.ToJson(myds);
	}

	/** 初始化
	 
	 @return 
*/
	public final String TodolistOfAuth_Init()
	{
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
		//    dt.Columns.Remove("SID");
		//    dt.Columns.Remove("Stas");
		//    dt.Columns.Remove("Depts");
		//    dt.Columns.Remove("Msg");
		//    return BP.Tools.Json.DataTableToJson(dt, false);
		//}

	}
	
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

			public final String Port_Init()
			{
	
				///#region 安全性校验.
				if (this.getUserNo() == null || this.getSID() == null || this.getDoWhat() == null)
				{
					return "err@必要的参数没有传入，请参考接口规则。";
				}

				if (BP.WF.Dev2Interface.Port_CheckUserLogin(this.getUserNo(), this.getSID()) == false)
				{
					return "err@非法的访问，请与管理员联系。SID=" + this.getSID();
				}

				if (BP.Web.WebUser.getNo().equals(this.getUserNo()) == false)
				{
					BP.WF.Dev2Interface.Port_SigOut();
					try
					{
						BP.WF.Dev2Interface.Port_Login(this.getUserNo(), this.getSID());
					}
					catch (RuntimeException ex)
					{
						return "err@安全校验出现错误:" + ex.getMessage();
					}
				}
	
				///#endregion 安全性校验.

	
				///#region 生成参数串.
				String paras = "";
				Enumeration<String> paraNames = getRequest().getParameterNames();
				for (Enumeration<String> e = paraNames;e.hasMoreElements();)
				{
					String str = e.nextElement().toString();
					String val = this.GetRequestVal(str);
					if (val.indexOf('@') != -1)
					{
						return "err@您没有能参数: [ " + str + " ," + val + " ] 给值 ，URL 将不能被执行。";
					}

	
//					switch (str)
	//ORIGINAL LINE: case DoWhatList.DoNode:
					if (str.equals(DoWhatList.DoNode) || str.equals(DoWhatList.Emps) || str.equals(DoWhatList.EmpWorks) || str.equals(DoWhatList.FlowSearch) || str.equals(DoWhatList.Login) || str.equals(DoWhatList.MyFlow) || str.equals(DoWhatList.MyWork) || str.equals(DoWhatList.Start) || str.equals(DoWhatList.Start5) || str.equals(DoWhatList.StartSimple) || str.equals(DoWhatList.FlowFX) || str.equals(DoWhatList.DealWork) || str.equals("FK_Flow") || str.equals("WorkID") || str.equals("FK_Node") || str.equals("SID"))
					{
					}
					else
					{
							paras += "&" + str + "=" + val;
					}
				}
				String nodeID = this.getFK_Flow() + "01";
	
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
						return "url@./RptDfine/FlowSearch.htm?2=1&FK_Flow=001&EnsName=ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt" + paras;
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
					sms.setMyPK(guid);
					sms.Retrieve();

					//判断当前的登录人员.
					if (BP.Web.WebUser.getNo() != sms.getSendToEmpNo())
					{
						BP.WF.Dev2Interface.Port_Login(sms.getSendToEmpNo());
					}

					BP.DA.AtPara ap = new AtPara(sms.getAtPara());
					if(sms.getMsgType() == SMSMsgType.SendSuccess){ // 发送成功的提示.

						if (BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(ap.GetValStrByKey("FK_Flow"), ap.GetValIntByKey("FK_Node"), ap.GetValInt64ByKey("WorkID"), BP.Web.WebUser.getNo()) == true)
						{
							return "url@MyFlow.htm?FK_Flow=" + ap.GetValStrByKey("FK_Flow") + "&WorkID=" + ap.GetValStrByKey("WorkID") + "&o2=1" + paras;
						}
						else
						{
							return "url@WFRpt.htm?FK_Flow=" + ap.GetValStrByKey("FK_Flow") + "&WorkID=" + ap.GetValStrByKey("WorkID") + "&o2=1" + paras;
						}
					}else{ //其他的情况都是查看工作报告.
						return "url@WFRpt.htm?FK_Flow=" + ap.GetValStrByKey("FK_Flow") + "&WorkID=" + ap.GetValStrByKey("WorkID") + "&o2=1" + paras;
					}
				}

				return "err@没有约定的标记:DoWhat=" + this.getDoWhat();
			}
	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理page接口.

		}


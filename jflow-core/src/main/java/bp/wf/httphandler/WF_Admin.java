package bp.wf.httphandler;

import bp.da.*;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.web.*;
import bp.port.*;
import bp.wf.Glo;
import bp.wf.template.*;
import bp.difference.*;
import bp.wf.*;
import java.util.*;

/** 
 页面功能实体
*/
public class WF_Admin extends WebContralBase
{

		///#region 属性.
	public final String getRefNo() throws Exception {
		return this.GetRequestVal("RefNo");
	}

		///#endregion

	/** 
	 构造函数
	*/
	public WF_Admin() throws Exception {
	}


		///#region 测试页面.
	/** 
	 获得运行的集成平台.
	 
	 @return 
	*/
	public final String TestFlow_GetRunOnPlant() throws Exception {
		return SystemConfig.getRunOnPlant();
	}
//	/**
//	 加密工具.
//
//	 @return
//	*/
//	public final String Encrypto_Exe() throws Exception {
//		DecryptAndEncryptionHelper.decode decode = new DecryptAndEncryptionHelper.decode();
//		String mstr = this.GetRequestVal("mstr").trim();
//		String encryptoStr = decode.encrypto_exe(mstr);
//		return encryptoStr;
//	}
//
//	/**
//	 解密方法.
//
//	 @return
//	*/
//	public final String Decode_Exe() throws Exception {
//		DecryptAndEncryptionHelper.decode decode = new DecryptAndEncryptionHelper.decode();
//		String sstr = this.GetRequestVal("sstr").trim();
//		String encryptoStr = decode.decode_exe(sstr);
//		return encryptoStr;
//	}
	/** 
	 初始化界面.
	 
	 @return 
	*/
	public final String TestFlow_Init() throws Exception {
		//清除缓存.
		SystemConfig.DoClearCash();

		if (1 == 2 && WebUser.getIsAdmin() == false)
		{
			return "err@您不是管理员，无法执行该操作.";
		}

		// 让admin 登录.
		//   BP.WF.Dev2Interface.Port_Login("admin");

		if (this.getRefNo() != null)
		{
			Emp emp = new Emp(this.getRefNo());
			WebUser.SignInOfGener(emp, "CH", false, false, null, null);
			bp.sys.Glo.getRequest().getSession().setAttribute("FK_Flow", this.getFK_Flow());
			return "url@../MyFlow.htm?FK_Flow=" + this.getFK_Flow();
		}

		FlowExt fl = new FlowExt(this.getFK_Flow());

		if (1 == 2 && !WebUser.getNo().equals("admin") && fl.getTester().length() <= 1)
		{
			String msg = "err@二级管理员[" + WebUser.getName() + "]您好,您尚未为该流程配置测试人员.";
			msg += "您需要在流程属性里的底部[设置流程发起测试人]的属性里，设置可以发起的测试人员,多个人员用逗号分开.";
			return msg;
		}

		/* 检查是否设置了测试人员，如果设置了就按照测试人员身份进入
		 * 设置测试人员的目的是太多了人员影响测试效率.
		 * */
		if (fl.getTester().length() > 2)
		{
			// 构造人员表.
			DataTable dtEmps = new DataTable();
			dtEmps.Columns.Add("No");
			dtEmps.Columns.Add("Name");
			dtEmps.Columns.Add("FK_DeptText");

			String[] strs = fl.getTester().split("[,]", -1);
			for (String str : strs)
			{
				if (DataType.IsNullOrEmpty(str) == true)
				{
					continue;
				}

				Emp emp = new Emp();
				emp.SetValByKey("No", str);
				int i = emp.RetrieveFromDBSources();
				if (i == 0)
				{
					continue;
				}

				DataRow dr = dtEmps.NewRow();
				dr.setValue("No", emp.getUserID());
				dr.setValue("Name", emp.getName());
				dr.setValue("FK_DeptText", emp.getFK_DeptText());
				dtEmps.Rows.add(dr);
			}
			return bp.tools.Json.ToJson(dtEmps);
		}

		//fl.DoCheck();

		int nodeid = Integer.parseInt(this.getFK_Flow() + "01");
		DataTable dt = null;
		String sql = "";
		Node nd = new Node(nodeid);

		if (nd.isGuestNode())
		{
			/*如果是guest节点，就让其跳转到 guest登录界面，让其发起流程。*/
			//这个地址需要配置.
			return "url@/SDKFlowDemo/GuestApp/Login.htm?FK_Flow=" + this.getFK_Flow();
		}

		try
		{

			switch (nd.getHisDeliveryWay())
			{
				case ByStation:
				case ByStationOnly:
					sql = "SELECT Port_Emp.No  FROM Port_Emp LEFT JOIN Port_Dept   Port_Dept_FK_Dept ON  Port_Emp.FK_Dept=Port_Dept_FK_Dept.No  join Port_DeptEmpStation on (fk_emp=Port_Emp.No)   join WF_NodeStation on (WF_NodeStation.fk_station=Port_DeptEmpStation.fk_station) WHERE (1=1) AND  FK_Node=" + nd.getNodeID();
					// emps.RetrieveInSQL_Order("select fk_emp from Port_Empstation WHERE fk_station in (select fk_station from WF_NodeStation WHERE FK_Node=" + nodeid + " )", "FK_Dept");
					break;
				case ByDept:
					sql = "SELECT No,Name FROM Port_Emp where FK_Dept in (select FK_Dept from WF_NodeDept where FK_Node='" + nodeid + "') ";
					//emps.RetrieveInSQL("");
					break;

				case ByBindEmp:
					//sql = "SELECT No,Name from Port_Emp where No in (select FK_Emp from WF_NodeEmp where FK_Node='" + nodeid + "') ";
					if (CCBPMRunModel.SAAS == SystemConfig.getCCBPMRunModel())
					{
						sql = "SELECT UserID,Name FROM Port_Emp A, WF_NodeEmp B WHERE A.UserID=B.FK_Emp AND B.FK_Node=" + nodeid;
					}
					else
					{
						sql = "SELECT No,Name FROM Port_Emp A, WF_NodeEmp B WHERE A.No=B.FK_Emp AND  B.FK_Node=" + nodeid;
					}

					//emps.RetrieveInSQL("select fk_emp from wf_NodeEmp WHERE fk_node=" + int.Parse(this.FK_Flow + "01") + " ");
					break;
				case ByDeptAndStation:
					//added by liuxc,2015.6.30.

					sql = "SELECT pdes.FK_Emp AS No" + " FROM   Port_DeptEmpStation pdes" + "        INNER JOIN WF_NodeDept wnd" + "             ON  wnd.FK_Dept = pdes.FK_Dept" + "             AND wnd.FK_Node = " + nodeid + "        INNER JOIN WF_NodeStation wns" + "             ON  wns.FK_Station = pdes.FK_Station" + "             AND wnd.FK_Node =" + nodeid + " ORDER BY" + "        pdes.FK_Emp";
					break;
				case BySelected: //所有的人员多可以启动, 2016年11月开始约定此规则.
					switch (SystemConfig.getAppCenterDBType( ))
					{
						case MSSQL:
							if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
							{
								sql = "SELECT top 300 No as FK_Emp FROM Port_Emp ";
							}
							else
							{
								sql = "SELECT top 300 No as FK_Emp FROM Port_Emp WHERE OrgNo='" + WebUser.getOrgNo() + "' ";
							}
							break;
						case Oracle:
						case KingBaseR3:
						case KingBaseR6:
							if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
							{
								sql = "SELECT  No as FK_Emp FROM Port_Emp WHERE ROWNUM < 300 ";
							}
							else
							{
								sql = "SELECT  No as FK_Emp FROM Port_Emp WHERE ROWNUM < 300 AND OrgNo='" + WebUser.getOrgNo() + "'";
							}
							break;
						case MySQL:
							if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
							{
								sql = "SELECT  No as FK_Emp FROM Port_Emp limit 0,300 ";
							}
							else
							{
								sql = "SELECT  No as FK_Emp FROM Port_Emp limit 0,300 WHERE   OrgNo='" + WebUser.getOrgNo() + "' ";
							}
							break;
						default:
							return "err@没有判断的数据库类型.";
					}
					break;
				case BySQL:
					if (DataType.IsNullOrEmpty(nd.getDeliveryParas()))
					{
						return "err@您设置的按SQL访问开始节点，但是您没有设置sql.";
					}
					break;
				default:
					break;
			}

			dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0)
			{
				return "err@您按照:" + nd.getHisDeliveryWay() + "的方式设置的开始节点的访问规则，但是开始节点没有人员。" + sql;
			}

			if (dt.Rows.size() > 2000)
			{
				return "err@可以发起开始节点的人员太多，会导致系统崩溃变慢，您需要在流程属性里设置可以发起的测试用户.";
			}

			// 构造人员表.
			DataTable dtMyEmps = new DataTable();
			dtMyEmps.Columns.Add("No");
			dtMyEmps.Columns.Add("Name");
			dtMyEmps.Columns.Add("FK_DeptText");

			//处理发起人数据.
			String emps = "";
			Emp emp = new Emp();
			for (DataRow dr : dt.Rows)
			{
				String myemp = dr.getValue(0).toString();
				if (emps.contains("," + myemp + ",") == true)
				{
					continue;
				}

				emps += "," + myemp + ",";

				if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
				{
					emp = new Emp();
					emp.setNo(myemp);
					emp.Retrieve();
				}
				else
				{
					emp = new Emp(myemp);
				}



				DataRow drNew = dtMyEmps.NewRow();

				drNew.setValue("No", emp.getUserID());
				drNew.setValue("Name", emp.getName());
				drNew.setValue("FK_DeptText", emp.getFK_DeptText());

				dtMyEmps.Rows.add(drNew);
			}

			//返回数据源.
			return bp.tools.Json.ToJson(dtMyEmps);
		}
		catch (RuntimeException ex)
		{
			return "err@<h3>您没有正确的设置开始节点的访问规则，这样导致没有可启动的人员，请在开始节点上右键设置接受人 </h3> 详细的错误信息：" + ex.getMessage();
		}
	}


	/** 
	 转到指定的url.
	 
	 @return 
	*/
	public final String TestFlow_ReturnToUser() throws Exception {
		String userNo = this.GetRequestVal("UserNo");
		Dev2Interface.Port_Login(userNo);
		String sid = Dev2Interface.Port_GenerToken(userNo);
		String url = "../../WF/Port.htm?UserNo=" + userNo + "&Token=" + sid + "&DoWhat=" + this.GetRequestVal("DoWhat") + "&FK_Flow=" + this.getFK_Flow() + "&IsMobile=" + this.GetRequestVal("IsMobile");
		return "url@" + url;
	}

		///#endregion 测试页面.


		///#region 安装.
	/** 
	 初始化安装包
	 
	 @return 
	*/
	public final String DBInstall_Init() throws Exception {
		try
		{
			if (DBAccess.TestIsConnection() == false)
			{
				return "err@数据库连接配置错误,请参考手册查看数据库配置连接.";
			}

			//   DBAccess.IsCaseSensitive

			//判断是否可以安装,不能安装就抛出异常.
			Glo.IsCanInstall();

			//判断是不是有.
			if (DBAccess.IsExitsObject("WF_Flow") == true)
			{
				return "err@info数据库已经安装上了，您不必在执行安装. 点击:<a href='../Portal/Login.htm' >这里直接登录流程设计器</a>";
			}

			Hashtable ht = new Hashtable();
			ht.put("OSModel", 1); //组织结构类型.
			ht.put("DBType", SystemConfig.getAppCenterDBType( ).toString()); //数据库类型.
			ht.put("Ver", Glo.Ver); //版本号.

			return bp.tools.Json.ToJson(ht);
		}
		catch (RuntimeException ex)
		{

			return "err@" + ex.getMessage();
		}
	}
	public final String DBInstall_Submit() throws Exception {
		String lang = "CH";

		//是否要安装demo.
		int demoTye = this.GetRequestValInt("DemoType");

		//运行ccflow的安装.
		Glo.DoInstallDataBase(lang, demoTye);

		//执行ccflow的升级。
		Glo.UpdataCCFlowVer();

		//加注释.
		bp.pub.PubClass.AddComment();

		if (DBAccess.IsExitsTableCol("Port_Emp", "EmpSta") == true)
		{
			DBAccess.DropTableColumn("Port_Emp", "EmpSta");
			//string sql = "UPDATE Port_Emp SET EmpSta=1 ";
			//if (DBAccess.RunSQLReturnValInt(sql, 1) == 0)
			//    return "err@该用户已经被禁用.";
		}

		return "info@系统成功安装 点击:<a href='../../Portal/Standard/Login.htm' >这里直接登录流程设计器</a>";
		// this.Response.Redirect("DBInstall.aspx?DoType=OK", true);
	}

		///#endregion

	public final String ReLoginSubmit() throws Exception {
		String userNo = this.GetValFromFrmByKey("TB_No");
		String password = this.GetValFromFrmByKey("TB_PW");

		Emp emp = new Emp();
		emp.setUserID (userNo);
		if (emp.RetrieveFromDBSources() == 0)
		{
			return "err@用户名或密码错误.";
		}

		if (emp.CheckPass(password) == false)
		{
			return "err@用户名或密码错误.";
		}

		WebUser.SignInOfGener(emp, "CH", false, false, null, null);

		return "登录成功.";
	}
	/** 
	 加载模版.
	 
	 @return 
	*/
	public final String SettingTemplate_Init() throws Exception {
		//类型.
		String templateType = this.GetRequestVal("TemplateType");
		String condType = this.GetRequestVal("CondType");

		SQLTemplates sqls = new SQLTemplates();
		//sqls.Retrieve(BP.WF.Template.SQLTemplateAttr.SQLType, sqlType);

		DataTable dt = null;
		String sql = "";


			///#region 节点方向条件模版.
		if (templateType.equals("CondBySQL"))
		{
			/*方向条件, 节点方向条件.*/
			sql = "SELECT MyPK,Note,OperatorValue FROM WF_Cond WHERE CondType=" + condType + " AND DataFrom=" + ConnDataFrom.SQL.getValue();
		}

		if (templateType.equals("CondByUrl"))
		{
			/*方向条件, 节点方向url条件.*/
			sql = "SELECT MyPK,Note,OperatorValue FROM WF_Cond WHERE CondType=" + condType + " AND DataFrom=" + ConnDataFrom.Url.getValue();
		}

		if (templateType.equals("CondByPara"))
		{
			/*方向条件, 节点方向url条件.*/
			sql = "SELECT MyPK,Note,OperatorValue FROM WF_Cond WHERE CondType=" + condType + " AND DataFrom=" + ConnDataFrom.Paras.getValue();
		}

			///#endregion 节点方向条件模版.


			///#region 表单扩展设置.

		String add = "+";

		if (SystemConfig.getAppCenterDBType( ) == DBType.Oracle || SystemConfig.getAppCenterDBType( ) == DBType.PostgreSQL || SystemConfig.getAppCenterDBType( ) == DBType.UX || SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR3 ||SystemConfig.getAppCenterDBType( ) == DBType.KingBaseR6)
		{
			add = "||";
		}

		if (templateType.equals("DDLFullCtrl"))
		{
			sql = "SELECT MyPK, '下拉框:'" + add + " a.AttrOfOper as Name,Doc FROM Sys_MapExt a  WHERE ExtType='DDLFullCtrl'";
		}

		if (templateType.equals("ActiveDDL"))
		{
			sql = "SELECT MyPK, '下拉框:'" + add + " a.AttrOfOper as Name,Doc FROM Sys_MapExt a  WHERE ExtType='ActiveDDL'";
		}

		//显示过滤.
		if (templateType.equals("AutoFullDLL"))
		{
			sql = "SELECT MyPK, '下拉框:'" + add + " a.AttrOfOper as Name,Doc FROM Sys_MapExt a  WHERE ExtType='AutoFullDLL'";
		}

		//文本框自动填充..
		if (templateType.equals("TBFullCtrl"))
		{
			sql = "SELECT MyPK, '文本框:'" + add + " a.AttrOfOper as Name,Doc FROM Sys_MapExt a  WHERE ExtType='TBFullCtrl'";
		}

		//自动计算.
		if (templateType.equals("AutoFull"))
		{
			sql = "SELECT MyPK, 'ID:'" + add + " a.AttrOfOper as Name,Doc FROM Sys_MapExt a  WHERE ExtType='AutoFull'";
		}

			///#endregion 表单扩展设置.


			///#region 节点属性的模版.
		//自动计算.
		if (templateType.equals("NodeAccepterRole"))
		{
			sql = "SELECT NodeID, FlowName +' - '+Name, a.DeliveryParas as Docs FROM WF_Node a WHERE  a.DeliveryWay=" + DeliveryWay.BySQL.getValue();
		}

			///#endregion 节点属性的模版.

		if (sql.equals(""))
		{
			return "err@没有涉及到的标记[" + templateType + "].";
		}

		dt = DBAccess.RunSQLReturnTable(sql);
		String strs = "";
		for (DataRow dr : dt.Rows)
		{
			SQLTemplate en = new SQLTemplate();
			en.setNo(dr.getValue(0).toString());
			en.setName(dr.getValue(1).toString());
			en.setDocs(dr.getValue(2).toString());

			if (strs.contains(en.getDocs().trim() + ";") == true)
			{
				continue;
			}
			strs += en.getDocs().trim() + ";";
			sqls.AddEntity(en);
		}

		return sqls.ToJson("dt");
	}
}
package bp.wf.httphandler;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.web.*;
import bp.port.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.template.*;
import java.util.*;

/** 
 页面功能实体
*/
public class WF_Admin extends WebContralBase
{

		///属性.
	public final String getRefNo()
	{
		return this.GetRequestVal("RefNo");
	}

		///

	/** 
	 构造函数
	*/
	public WF_Admin()
	{
	}


		///测试页面.
	/** 
	 获得运行的集成平台.
	 
	 @return 
	*/
	public final String TestFlow_GetRunOnPlant()
	{
		return SystemConfig.getRunOnPlant();
	}
	/** 
	 初始化界面.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String TestFlow_Init() throws Exception
	{
		//清除缓存.
		SystemConfig.DoClearCash();

	
		
		if (this.getRefNo() != null)
		{
			Emp emp = new Emp(this.getRefNo());
			WebUser.SignInOfGener(emp);
			WebUser.SetSessionByKey("FK_Flow", this.getFK_Flow());
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
				dr.setValue("No", emp.getNo());
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
		bp.wf.Node nd = new bp.wf.Node(nodeid);

		if (nd.getIsGuestNode())
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

					sql = "SELECT Port_Emp.No  FROM Port_Emp LEFT JOIN Port_Dept   Port_Dept_FK_Dept ON  Port_Emp.FK_Dept=Port_Dept_FK_Dept.No  join Port_DeptEmpStation on (fk_emp=Port_Emp.getNo())   join WF_NodeStation on (WF_NodeStation.fk_station=Port_DeptEmpStation.fk_station) WHERE (1=1) AND  FK_Node=" + nd.getNodeID();
					// emps.RetrieveInSQL_Order("select fk_emp from Port_Empstation WHERE fk_station in (select fk_station from WF_NodeStation WHERE FK_Node=" + nodeid + " )", "FK_Dept");
					break;

				case ByDept:
					sql = "SELECT No,Name FROM Port_Emp where FK_Dept in (select FK_Dept from WF_NodeDept where FK_Node='" + nodeid + "') ";
					//emps.RetrieveInSQL("");
					break;

				case ByBindEmp:
					sql = "select No,Name from Port_Emp where No in (select FK_Emp from WF_NodeEmp where FK_Node='" + nodeid + "') ";
					//emps.RetrieveInSQL("select fk_emp from wf_NodeEmp WHERE fk_node=" + int.Parse(this.FK_Flow + "01") + " ");
					break;
				case ByDeptAndStation:
					//added by liuxc,2015.6.30.

					sql = "SELECT pdes.FK_Emp AS No" + " FROM   Port_DeptEmpStation pdes" + "        INNER JOIN WF_NodeDept wnd" + "             ON  wnd.FK_Dept = pdes.FK_Dept" + "             AND wnd.FK_Node = " + nodeid + "        INNER JOIN WF_NodeStation wns" + "             ON  wns.FK_Station = pdes.FK_Station" + "             AND wnd.FK_Node =" + nodeid + " ORDER BY" + "        pdes.FK_Emp";
					break;
				case BySelected: //所有的人员多可以启动, 2016年11月开始约定此规则.
					sql = "SELECT No as FK_Emp FROM Port_Emp ";
					dt = DBAccess.RunSQLReturnTable(sql);
					if (dt.Rows.size() > 300)
					{
						if (SystemConfig.getAppCenterDBType() == bp.da.DBType.MSSQL)
						{
							sql = "SELECT top 300 No as FK_Emp FROM Port_Emp ";
						}

						if (SystemConfig.getAppCenterDBType() == bp.da.DBType.Oracle)
						{
							sql = "SELECT  No as FK_Emp FROM Port_Emp WHERE ROWNUM <300 ";
						}

						if (SystemConfig.getAppCenterDBType() == bp.da.DBType.MySQL)
						{
							sql = "SELECT  No as FK_Emp FROM Port_Emp   limit 0,300 ";
						}
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
				return "err@您按照:" + nd.getHisDeliveryWay() + "的方式设置的开始节点的访问规则，但是开始节点没有人员。";
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
			for (DataRow dr : dt.Rows)
			{
				String myemp = dr.getValue(0).toString();
				if (emps.contains("," + myemp + ",") == true)
				{
					continue;
				}

				emps += "," + myemp + ",";
				bp.port.Emp emp = new Emp(myemp);

				DataRow drNew = dtMyEmps.NewRow();

				drNew.setValue("No", emp.getNo());
				drNew.setValue("Name", emp.getName());
				drNew.setValue("FK_DeptText", emp.getFK_DeptText());

				dtMyEmps.Rows.add(drNew);
			}


			//检查物理表,避免错误.
			Nodes nds = new Nodes(this.getFK_Flow());
			for (Node mynd : nds.ToJavaList())
			{
				mynd.getHisWork().CheckPhysicsTable();
			}


			//返回数据源.
			return bp.tools.Json.ToJson(dtMyEmps);
		}
		catch (RuntimeException ex)
		{
			return "err@<h2>您没有正确的设置开始节点的访问规则，这样导致没有可启动的人员，<a href='http://bbs.ccflow.org/showtopic-4103.aspx' target=_blank ><font color=red>点击这查看解决办法</font>.</a>。</h2> 系统错误提示:" + ex.getMessage() + "<br><h3>也有可能你你切换了OSModel导致的，什么是OSModel,请查看在线帮助文档 <a href='http://ccbpm.mydoc.io' target=_blank>http://ccbpm.mydoc.io</a>  .</h3>";
		}
	}


	/** 
	 转到指定的url.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String TestFlow_ReturnToUser() throws Exception
	{
		String userNo = this.GetRequestVal("UserNo");
		bp.wf.Dev2Interface.Port_Login(userNo);
		String sid = bp.wf.Dev2Interface.Port_GenerSID(userNo);
		String url = "../../WF/Port.htm?UserNo=" + userNo + "&SID=" + sid + "&DoWhat=" + this.GetRequestVal("DoWhat") + "&FK_Flow=" + this.getFK_Flow() + "&IsMobile=" + this.GetRequestVal("IsMobile");
		return "url@" + url;
	}

		/// 测试页面.



		///安装.
	/** 
	 初始化安装包
	 
	 @return 
	*/
	public final String DBInstall_Init()
	{
		try
		{
			if (DBAccess.TestIsConnection() == false)
			{
				return "err@数据库连接配置错误,请参考手册查看数据库配置连接.";
			}


			//   DBAccess.IsCaseSensitive

			//判断是否可以安装,不能安装就抛出异常.
			bp.wf.Glo.IsCanInstall();

			//判断是不是有.
			if (DBAccess.IsExitsObject("WF_Flow") == true)
			{
				return "err@info数据库已经安装上了，您不必在执行安装. 点击:<a href='./CCBPMDesigner/Login.htm' >这里直接登录流程设计器</a>";
			}


			Hashtable ht = new Hashtable();
			ht.put("OSModel", 1); //组织结构类型.
			ht.put("DBType", SystemConfig.getAppCenterDBType().toString()); //数据库类型.
			ht.put("Ver", bp.wf.Glo.Ver); //版本号.

			return bp.tools.Json.ToJson(ht);
		}
		catch (RuntimeException ex)
		{

			return "err@" + ex.getMessage();
		}
	}
	public final String DBInstall_Submit() throws Exception
	{
		String lang = "CH";

		//是否要安装demo.
		int demoTye = this.GetRequestValInt("DemoType");

		//运行ccflow的安装.
		bp.wf.Glo.DoInstallDataBase(lang, demoTye);

		//执行ccflow的升级。
		bp.wf.Glo.UpdataCCFlowVer();

		//加注释.
		PubClass.AddComment();

		return "info@系统成功安装 点击:<a href='./CCBPMDesigner/Login.htm' >这里直接登录流程设计器</a>";
		// this.Response.Redirect("DBInstall.aspx?DoType=OK", true);
	}

		///


	public final String ReLoginSubmit() throws Exception
	{
		String userNo = this.GetValFromFrmByKey("TB_No");
		String password = this.GetValFromFrmByKey("TB_PW");

		bp.port.Emp emp = new bp.port.Emp();
		emp.setNo(userNo);
		if (emp.RetrieveFromDBSources() == 0)
		{
			return "err@用户名或密码错误.";
		}

		if (emp.CheckPass(password) == false)
		{
			return "err@用户名或密码错误.";
		}

		WebUser.SignInOfGener(emp);

		return "登录成功.";
	}
	/** 
	 加载模版.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SettingTemplate_Init() throws Exception
	{
		//类型.
		String templateType = this.GetRequestVal("TemplateType");
		String condType = this.GetRequestVal("CondType");

		bp.wf.template.SQLTemplates sqls = new SQLTemplates();

		DataTable dt = null;
		String sql = "";


			///节点方向条件模版.
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

			/// 节点方向条件模版.


			///表单扩展设置.

		String add = "+";

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
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

			/// 表单扩展设置.


			///节点属性的模版.
		//自动计算.
		if (templateType.equals("NodeAccepterRole"))
		{
			sql = "SELECT NodeID, FlowName +' - '+Name, a.DeliveryParas as Docs FROM WF_Node a WHERE  a.DeliveryWay=" + DeliveryWay.BySQL.getValue();
		}

			/// 节点属性的模版.

		if (sql.equals(""))
		{
			return "err@没有涉及到的标记[" + templateType + "].";
		}

		dt = DBAccess.RunSQLReturnTable(sql);
		String strs = "";
		for (DataRow dr : dt.Rows)
		{
			bp.wf.template.SQLTemplate en = new SQLTemplate();
			en.setNo(dr.getValue(0).toString());
			en.setName( dr.getValue(1).toString());
			en.setDocs(dr.getValue(2).toString());

			if (strs.contains(en.getDocs().trim() + ";") == true)
			{
				continue;
			}
			strs += en.getDocs().trim() + ";";
			sqls.AddEntity(en);
		}

		return sqls.ToJson();
	}
}
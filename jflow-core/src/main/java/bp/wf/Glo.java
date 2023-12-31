package bp.wf;

import bp.difference.handler.CommonUtils;
import bp.sys.*;
import bp.da.*;
import bp.en.*;
import bp.sys.FrmImgAth;
import bp.sys.SFTable;
import bp.sys.frmui.ExtImg;
import bp.tools.DateUtils;
import bp.tools.FileAccess;
import bp.tools.FtpUtil;
import bp.tools.SftpUtil;
import bp.web.*;
import bp.port.*;
import bp.wf.data.*;
import bp.wf.template.*;
import bp.sys.frmui.*;
import bp.difference.*;
import bp.wf.template.sflow.*;
import bp.wf.template.frm.*;
import bp.*;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;
import java.time.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 全局(方法处理)
*/
public class Glo
{
	/** 
	 单据编号对应字段SQL
	*/
	public static String getSQLOfBillNo()
	{
		String sql = "";
		switch (SystemConfig.getAppCenterDBType())
		{
			case MSSQL:
			case MySQL:
				sql = "SELECT '' AS No, '-请选择-' as Name ";
				break;
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				sql = "SELECT '' AS No, '-请选择-' as Name FROM DUAL ";
				break;
			case PostgreSQL:
			case UX:
			case HGDB:
			default:
				sql = "SELECT '' AS No, '-请选择-' as Name FROM Port_Emp WHERE 1=2 ";
				break;
		}
		sql += " UNION ";
		sql += " SELECT KeyOfEn AS No,Name FROM Sys_MapAttr WHERE UIContralType=0 AND UIVisible=1 AND UIIsEnable=1 AND FK_MapData='@FK_Frm'";
		return sql;
	}
	/** 
	 签批组件SQL
	*/
	public static String getSQLOfCheckField()
	{
		String sql = "";
		switch (SystemConfig.getAppCenterDBType())
		{
			case MSSQL:
			case MySQL:
				sql = "SELECT '' AS No, '-请选择-' as Name ";
				break;
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				sql = "SELECT '' AS No, '-请选择-' as Name FROM DUAL ";
				break;
			case PostgreSQL:
			case UX:
			case HGDB:
			default:
				sql = "SELECT '' AS No, '-请选择-' as Name FROM Port_Emp WHERE 1=2 ";
				break;
		}
		sql += " UNION ";
		sql += " SELECT KeyOfEn AS No,Name From Sys_MapAttr WHERE UIContralType=14 AND FK_MapData='@FK_Frm'";
		return sql;
	}


		///#region 获取[新建-节点-流程]默认值.
	/** 
	 新建节点的审核意见默认值.
	*/
	public static String getDefValWFNodeFWCDefInfo()
	{
		return SystemConfig.GetValByKey("DefVal_WF_Node_FWCDefInfo", "同意");
	}

		///#endregion 获取[新建流程]默认值.



		///#region 多语言处理.
	private static Hashtable _Multilingual_Cache = null;
	public static DataTable getMultilingual_DT(String className)
	{
		if (_Multilingual_Cache == null)
		{
			_Multilingual_Cache = new Hashtable();
		}

		if (_Multilingual_Cache.containsKey(className) == false)
		{
			DataSet ds = DataType.CXmlFileToDataSet(SystemConfig.getPathOfData() + "lang/xml/" + className + ".xml");
			DataTable dt = ds.Tables.get(0);

			_Multilingual_Cache.put(className, dt);
		}

		return _Multilingual_Cache.get(className) instanceof DataTable ? (DataTable)_Multilingual_Cache.get(className) : null;
	}
	/** 
	 转换语言.
	*/

	public static String multilingual(String defaultMsg, String className, String key, String p0, String p1, String p2)
	{
		return multilingual(defaultMsg, className, key, p0, p1, p2, null);
	}

	public static String multilingual(String defaultMsg, String className, String key, String p0, String p1)
	{
		return multilingual(defaultMsg, className, key, p0, p1, null, null);
	}

	public static String multilingual(String defaultMsg, String className, String key, String p0)
	{
		return multilingual(defaultMsg, className, key, p0, null, null, null);
	}

	public static String multilingual(String defaultMsg, String className, String key)
	{
		return multilingual(defaultMsg, className, key, null, null, null, null);
	}

	public static String multilingual(String defaultMsg, String className, String key, String p0, String p1, String p2, String p3)
	{
		int num = 4;
		String[] paras = new String[num];
		if (p0 != null)
		{
			paras[0] = p0;
		}

		if (p1 != null)
		{
			paras[1] = p1;
		}

		if (p2 != null)
		{
			paras[2] = p2;
		}

		if (p3 != null)
		{
			paras[3] = p3;
		}

		return multilingual(defaultMsg, className, key, paras);
	}
	/** 
	 获取多语言
	 
	 @param key
	 @param paramList
	 @return 
	*/
	public static String multilingual(String defaultMsg, String className, String key, String[] paramList)
	{
		if (WebUser.getSysLang().equals("zh-cn") || WebUser.getSysLang().equals("CH"))
		{
			defaultMsg = defaultMsg.replace("{0}","%1$s").replace("{1}","%2$s").replace("{2}","%3$s").replace("{3}","%4$s");
			return String.format(defaultMsg, paramList);
		}

		DataTable dt = getMultilingual_DT(className);

		String val = "";
		for (DataRow dr : dt.Rows)
		{
			if (key.equals((String) dr.getValue(0)))
			{
				switch (WebUser.getSysLang())
				{
					case "zh-cn":
						val = (String) dr.getValue(1);
						break;
					case "zh-tw":
						val = (String) dr.getValue(2);
						break;
					case "zh-hk":
						val = (String) dr.getValue(3);
						break;
					case "en-us":
						val = (String) dr.getValue(4);
						break;
					case "ja-jp":
						val = (String) dr.getValue(5);
						break;
					case "ko-kr":
						val = (String) dr.getValue(6);
						break;
					default:
						val = (String) dr.getValue(1);
						break;
				}
				break;
			}
		}
		return String.format(val, paramList);
	}

	///#region 公共属性.
	/** 
	 打印文件
	*/
	public static String getPrintBackgroundWord() throws Exception {
		String s = SystemConfig.GetValByKey("PrintBackgroundWord","");
		if (DataType.IsNullOrEmpty(s))
		{
			s = "驰骋工作流引擎@开源驰骋 - ccflow@openc";
		}
		return s;
	}
	/** 
	 运行平台.
	*/
	public static Platform getPlatform()
	{
		return Platform.CCFlow;
	}

	/** 
	 短消息写入类型
	*/
	public static ShortMessageWriteTo getShortMessageWriteTo()
	{
		return ShortMessageWriteTo.forValue(SystemConfig.GetValByKeyInt("ShortMessageWriteTo", 0));
	}
	/** 
	 当前选择的流程.
	*/
	public static String getCurrFlow() throws Exception {
		Object tempVar = bp.sys.Glo.getRequest().getSession().getAttribute("CurrFlow");
		return tempVar instanceof String ? (String) tempVar : null;
	}
	public static void setCurrFlow(String value)
	{
		bp.sys.Glo.getRequest().getSession().setAttribute("CurrFlow", value);
	}
	/** 
	 用户编号.
	*/
	public static String UserNo = null;
	/** 
	 运行平台(用于处理不同的平台，调用不同的URL)
	*/
	public static Plant Plant = bp.wf.Plant.CCFlow;

		///#endregion 公共属性.

	/** 
	 CCBPMRunModel
	*/
	public static CCBPMRunModel getCCBPMRunModel()
	{
		return SystemConfig.getCCBPMRunModel();
	}


		///#region 执行安装/升级.
	/** 
	 当前版本号-为了升级使用.
	*/
	public static int Ver = 20230627;
	/** 
	 执行升级
	 
	 @return 执行升级的结果
	*/
	public static String UpdataCCFlowVer() throws Exception {
		if (bp.wf.Glo.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			return "info@SAAS模式需要手工升级.";
		}


			///#region 检查是否需要升级，并更新升级的业务逻辑.
		String updataNote = "";
		/*
		 * 升级版本记录:
		 * 20150330: 优化发起列表的效率, by:zhoupeng.
		 * 2, 升级表单树,支持动态表单树.
		 * 1, 执行一次Sender发送人的升级，原来由GenerWorkerList 转入WF_GenerWorkFlow.
		 * 0, 静默升级启用日期.2014-12
		 */
		if (DBAccess.IsExitsObject("Sys_Serial") == false)
			return "";

		//升级SQL
		UpdataCCFlowVerSQLScript();
		if (DBAccess.IsExitsTableCol("WF_Node", "UIWidth") == true)
			//执行sql.升级节点高度.
			DBAccess.RunSQL("UPDATE WF_Node SET UIWidth=120,UIHeight=60 WHERE UIWidth=0 OR UIWidth Is Null ");

		//判断数据库的版本.
		String sql = "SELECT IntVal FROM Sys_Serial WHERE CfgKey='Ver'";
		int currDBVer = DBAccess.RunSQLReturnValInt(sql, 0);
		if (currDBVer != 0 && currDBVer != 0 && currDBVer >= Ver)
		{
			return null; //不需要升级.
		}

			///#endregion 检查是否需要升级，并更新升级的业务逻辑.

			///#region 2023.07.25 - 翻译java的兼容性.
		if (DBAccess.IsExitsTableCol("WF_GenerWorkerList", "FK_NodeText") == true)
		{
			DBAccess.RenameTableField("WF_GenerWorkerList", "FK_NodeText", "NodeName");
			DBAccess.RenameTableField("WF_GenerWorkerList", "FK_EmpTex", "EmpName");
			DBAccess.RenameTableField("WF_GenerWorkerList", "FK_DeptT", "DeptName");
		}

			///#endregion 2023.07.25



			///#region 2023.07.02 升级字典表,查询.
		SFSearch search = new SFSearch();
		search.CheckPhysicsTable();
		bp.sys.SFTable table = new bp.sys.SFTable();
		table.CheckPhysicsTable();
		SFProcedure enProduce = new SFProcedure();
		enProduce.CheckPhysicsTable();

			///#endregion 2023.07.02


			///#region 升级SFTable中SrcType为DBSrcType
		if (DBAccess.IsExitsTableCol("Sys_SFTable", "SrcType") == true)
		{
			if (DBAccess.IsExitsTableCol("Sys_SFTable", "DBSrcType") == false)
			{
				switch (SystemConfig.getAppCenterDBType())
				{
					case MSSQL:
						DBAccess.RunSQL("ALTER TABLE Sys_SFTable ADD DBSrcType NVARCHAR(20) DEFAULT 'BPClass' NULL");
						break;
					case Oracle:
					case Informix:
					case PostgreSQL:
					case HGDB:
					case UX:
					case KingBaseR3:
					case KingBaseR6:
						DBAccess.RunSQL("ALTER TABLE Sys_SFTable ADD DBSrcType VARCHAR(20) DEFAULT 'BPClass' NULL");
						break;
					case MySQL:
						DBAccess.RunSQL("ALTER TABLE Sys_SFTable ADD DBSrcType NVARCHAR(20) DEFAULT 'BPClass' NULL");
						break;
					default:
						break;
				}

			}
			DBAccess.RunSQL("UPDATE Sys_SFTable SET DBSrcType=(CASE SrcType WHEN  0 THEN 'BPClass' WHEN 1 THEN 'CreateTable' WHEN 1 THEN 'CreateTable' " + "WHEN 2 THEN 'TableOrView' WHEN 3 THEN 'SQL' WHEN 4 THEN 'WebServices' WHEN 5 THEN 'Handler' WHEN 6 THEN 'JQuery' " + "WHEN 7 THEN 'SysDict' ELSE 'WebApi' END)");
			DBAccess.DropTableColumn("Sys_SFTable", "SrcType");

		}

			///#endregion 升级SFTable中SrcType为DBSrcType


			///#region 升级流程模式的存储方式
		if (DBAccess.IsExitsTableCol("WF_Flow", "FlowDevModel") == false)
		{
			switch (SystemConfig.getAppCenterDBType())
			{
				case MSSQL:
					DBAccess.RunSQL("ALTER TABLE WF_Flow ADD FlowDevModel INT NULL");
					break;
				case Oracle:
				case Informix:
				case PostgreSQL:
				case HGDB:
				case UX:
				case KingBaseR3:
				case KingBaseR6:
					DBAccess.RunSQL("ALTER TABLE WF_Flow ADD FlowDevModel INTEGER NULL");
					break;
				case MySQL:
					DBAccess.RunSQL("ALTER TABLE WF_Flow ADD FlowDevModel INT NULL");
					break;
				default:
					break;
			}
		}
		Flows flows = new Flows();
		QueryObject qo = new QueryObject(flows);
		qo.AddWhere("AtPara", "Like", "%FlowDevModel=%");
		qo.DoQuery();
		for (Flow flow : flows.ToJavaList())
		{
			int flowDevModel = flow.GetParaInt("FlowDevModel", 0);
			flow.setFlowDevModel(FlowDevModel.forValue(flowDevModel));
			String atPara = flow.GetValStringByKey("AtPara");
			atPara = atPara.replace("@FlowDevModel=" + flowDevModel, "");
			flow.SetValByKey("AtPara", atPara);
			flow.DirectUpdate();
		}

			///#endregion 升级流程模式的存储方式


			///#region 升级文本框字段类型.  TextModel=0普通文本，1密码，2=TextArea,3=富文本.
		//说明没有升级. TextModel=0
		if (DBAccess.IsExitsTableCol("Sys_MapAttr", "IsRichText") == true)
		{
			MapAttr ma = new MapAttr();
			ma.CheckPhysicsTable();

			sql = "UPDATE Sys_MapAttr SET TextModel=3 WHERE IsRichText=1 OR AtPara LIKE '%IsRichText=1%'";
			DBAccess.RunSQL(sql);

			sql = "UPDATE Sys_MapAttr SET TextModel=2 WHERE UIHeight >=40 OR IsSupperText=1";
			DBAccess.RunSQL(sql);

			sql = "UPDATE Sys_MapAttr SET TextModel=1 WHERE IsSecret=1";
			DBAccess.RunSQL(sql);

			DBAccess.DropTableColumn("Sys_MapAttr", "IsRichText");
			DBAccess.DropTableColumn("Sys_MapAttr", "IsSecret");
		}

			///#endregion 升级文本框字段类型


			///#region 统一升级主键. 给多对多的实体增加主键.
		if (DBAccess.IsExitsTableCol("WF_NodeStation", "MyPK") == false)
		{
			//1.首先要删除主键.
			DBAccess.DropTablePK("WF_NodeStation");
			if (Objects.equals(SystemConfig.getAppCenterDBType().toString(), "MySQL"))
			{
				DBAccess.RunSQL("ALTER TABLE WF_NodeStation ADD COLUMN MyPK VARCHAR(100)");
				DBAccess.RunSQL("UPDATE WF_NodeStation SET MyPK= CONCAT(FK_Node,'_',FK_Station)");
			}
			else
			{
				if (Objects.equals(SystemConfig.getAppCenterDBType().toString(), "MSSQL"))
				{
				DBAccess.RunSQL("ALTER TABLE WF_NodeStation ADD  MyPK VARCHAR(100)");
				DBAccess.RunSQL("UPDATE WF_NodeStation SET MyPK= CONVERT(varchar,FK_Node)+'_'+FK_Station");
				}
			}

			//2. 自动创建.
			NodeStation ns = new NodeStation();
			ns.CheckPhysicsTable();
		}

		if (DBAccess.IsExitsObject("WF_NodeTeam") == false)
		{
			NodeTeam nt = new NodeTeam();
			nt.CheckPhysicsTable();
		}

		if (DBAccess.IsExitsTableCol("WF_NodeTeam", "MyPK") == false)
		{
			DBAccess.DropTablePK("WF_NodeTeam");
			if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
			{
				DBAccess.RunSQL("ALTER TABLE WF_NodeTeam ADD COLUMN MyPK VARCHAR(100)");
				DBAccess.RunSQL("UPDATE WF_NodeTeam SET MyPK= CONCAT(FK_Node,'_',FK_Team)");
			}
			else if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
			{
				DBAccess.RunSQL("ALTER TABLE WF_NodeTeam ADD  MyPK VARCHAR(100)");
				DBAccess.RunSQL("UPDATE WF_NodeTeam SET MyPK= CONVERT(varchar,FK_Node)+'_'+FK_Team");
			}
			//3. 执行更新.
		}

		if (DBAccess.IsExitsTableCol("WF_NodeEmp", "MyPK") == false)
		{

			DBAccess.DropTablePK("WF_NodeEmp");
			if (Objects.equals(SystemConfig.getAppCenterDBType().toString(), "MySQL"))
			{
				DBAccess.RunSQL("ALTER TABLE WF_NodeEmp ADD COLUMN MyPK VARCHAR(100)");
				DBAccess.RunSQL("UPDATE WF_NodeEmp SET MyPK= CONCAT(FK_Node,'_',FK_Emp)");
			}
			else
			{
	   if (Objects.equals(SystemConfig.getAppCenterDBType().toString(), "MSSQL"))
	   {

				DBAccess.RunSQL("ALTER TABLE WF_NodeEmp ADD  MyPK VARCHAR(100)");
				DBAccess.RunSQL("UPDATE WF_NodeEmp SET MyPK= CONVERT(varchar,FK_Node)+'_'+FK_Emp");
	   }
			}
			NodeEmp ne1 = new NodeEmp();
			ne1.CheckPhysicsTable();
			//3. 执行更新.

		}

			///#endregion 统一升级主键.


			///#region 系统更新.
		//升级支持ts.
		// UpdataTSModel();
		//升级日志.
		UserLogLevel ul = new UserLogLevel();
		ul.CheckPhysicsTable();
		UserLogType ut = new UserLogType();
		ut.CheckPhysicsTable();

		//添加IsEnable
		FlowTab fb = new FlowTab();
		fb.CheckPhysicsTable();

		if (DBAccess.IsExitsTableCol("Sys_GroupField", "EnName") == true)
		{
			GroupField groupField = new GroupField();
			groupField.CheckPhysicsTable();
			DBAccess.RunSQL("UPDATE Sys_GroupField SET FrmID=enName WHERE FrmID IS null");
		}

		//升级.
		Auth ath = new Auth();
		ath.CheckPhysicsTable();

		//检查BPM.现在暂时不使用原菜单结构
		// if (!SystemConfig.OrganizationIsView)
		//    CheckGPM();

		MapData mapData = new MapData();
		mapData.CheckPhysicsTable();

		Direction dir = new Direction();
		dir.CheckPhysicsTable();

			///#endregion 系统更新.


			///#region 升级优化集团版的应用. 2020.04.03

		//--2020.05.28 升级方向条件;
		Cond cond = new Cond();
		cond.CheckPhysicsTable();
		if (DBAccess.IsExitsTableCol("WF_Cond", "PRI") == true)
		{
			DBAccess.RunSQL("UPDATE WF_Cond SET Idx=PRI ");
			DBAccess.DropTableColumn("WF_Cond", "PRI");
		}

		//修改节点类型,合并属性.
		/*if (DBAccess.IsExitsTableCol("WF_Node", "SubThreadType") == true)
		{
		    DBAccess.RunSQLReturnTable("UPDATE WF_Node SET RunModel=5 WHERE SubThreadType=1 ");
		    DBAccess.DropTableColumn("WF_Node", "SubThreadType");
		}*/

		//--2020.05.25 修改节点自定义按钮功能;
		NodeToolbar bar = new NodeToolbar();
		bar.CheckPhysicsTable();
		if (DBAccess.IsExitsTableCol("WF_NodeToolbar", "ShowWhere") == true)
		{
			DBAccess.RunSQL("UPDATE WF_NodeToolbar SET IsMyFlow = 1 Where ShowWhere = 1");
			DBAccess.RunSQL("UPDATE WF_NodeToolbar SET IsMyCC = 1 Where ShowWhere = 2");

			DBAccess.DropTableColumn("WF_NodeToolbar", "ShowWhere");
		}
		switch (SystemConfig.getAppCenterDBType())
		{
			case Oracle:
				DBAccess.RunSQL("UPDATE Sys_MapAttr set tag=''");
				break;
			default:
				break;
		}
		Direction direction = new Direction();
		direction.CheckPhysicsTable();

		MapAttr myattr = new MapAttr();
		myattr.CheckPhysicsTable();

		MapDtlExt mde = new MapDtlExt();
		mde.CheckPhysicsTable();

		NodeExt ne = new NodeExt();
		ne.CheckPhysicsTable();

		FlowExt fe = new FlowExt();
		fe.CheckPhysicsTable();

		//检查frmTrack.
		bp.ccbill.Track tk = new bp.ccbill.Track();
		tk.CheckPhysicsTable();

		DeptEmpStation des = new DeptEmpStation();
		des.CheckPhysicsTable();

		DeptEmp de = new DeptEmp();
		de.CheckPhysicsTable();

		Emp emp1 = new Emp();
		emp1.CheckPhysicsTable();

		bp.wf.port.admin2group.Org org = new bp.wf.port.admin2group.Org();
		org.CheckPhysicsTable();

		FlowSort fs = new FlowSort();
		fs.CheckPhysicsTable();

		FlowOrg fo = new FlowOrg();
		fo.CheckPhysicsTable();

		SysEnumMain sem = new SysEnumMain();
		sem.CheckPhysicsTable();

		SysEnum myse = new SysEnum();
		myse.CheckPhysicsTable();

		//检查表.
		GloVar gv = new GloVar();
		gv.CheckPhysicsTable();

		//检查表.
		EnCfg cfg = new EnCfg();
		cfg.CheckPhysicsTable();

		//检查表.
		SysFormTree frmTree = new SysFormTree();
		frmTree.CheckPhysicsTable();

		SFTable sf = new SFTable();
		sf.CheckPhysicsTable();

		FrmSubFlow sb = new FrmSubFlow();
		sb.CheckPhysicsTable();

		PushMsg pm = new PushMsg();
		pm.CheckPhysicsTable();

		//修复数据表.
		GroupField gf = new GroupField();
		gf.CheckPhysicsTable();


			///#endregion 升级优化集团版的应用


			///#region 升级子流程.
		//检查子流程表.
		if (DBAccess.IsExitsObject("WF_NodeSubFlow") == true)
		{
			if (DBAccess.IsExitsTableCol("WF_NodeSubFlow", "OID") == true)
			{
				DBAccess.RunSQL("DROP TABLE WF_NodeSubFlow");
				SubFlowHand sub = new SubFlowHand();
				sub.CheckPhysicsTable();
			}
		}

			///#endregion 升级子流程.


			///#region 升级方向条件. 2020.06.02
		if (DBAccess.IsExitsTableCol("WF_Cond", "CondOrAnd") == true)
		{
			DataTable dt = DBAccess.RunSQLReturnTable("SELECT DISTINCT FK_Node, toNodeID, CondOrAnd, CondType  FROM wf_cond WHERE DataFrom!=100 ");
			for (DataRow dr : dt.Rows)
			{
				int nodeID = Integer.parseInt(dr.getValue("FK_Node").toString());
				int toNodeID = Integer.parseInt(dr.getValue("toNodeID").toString());

				Conds conds = new Conds();
				conds.Retrieve(CondAttr.FK_Node, nodeID, CondAttr.ToNodeID, toNodeID, CondAttr.Idx);

				//判断是否需要修复？
				if (conds.size() == 1 || conds.size() == 0)
				{
					continue;
				}

				//判断是否有？
				boolean isHave = false;
				for (Cond myCond : conds.ToJavaList())
				{
					if (myCond.getHisDataFrom() == ConnDataFrom.CondOperator)
					{
						isHave = true;
					}
				}
				if (isHave == true)
				{
					continue;
				}

				//获得类型.
				int OrAndType = DBAccess.RunSQLReturnValInt("SELECT  CondOrAnd  FROM wf_cond WHERE FK_Node=" + nodeID, -1);
				if (OrAndType == -1)
				{
					continue;
				}

				int idx = 0;
				int idxSave = 0;
				int count = conds.size();
				for (Cond item : conds.ToJavaList())
				{
					idx++;

					idxSave++;
					item.setIdx(idxSave);
					item.Update();

					if (count == idx)
					{
						continue;
					}

					Cond operCond = new Cond();
					operCond.Copy(item);
					operCond.setMyPK(DBAccess.GenerGUID(0, null, null));
					operCond.setHisDataFrom(ConnDataFrom.CondOperator);

					if (OrAndType == 0)
					{
						operCond.setOperatorValue(" OR ");
						operCond.setNote(" OR ");
						operCond.setOperatorValue(" OR ");
						operCond.setNote(" OR ");
					}
					else
					{
						operCond.setOperatorValue(" AND ");
						operCond.setNote(" AND ");
						operCond.setOperatorValue(" AND ");
						operCond.setNote(" AND ");
					}

					idxSave++;
					operCond.setIdx(idxSave);
					operCond.Insert();
				}
			}

			//升级后删除指定的列.
			DBAccess.DropTableColumn("WF_Cond", "CondOrAnd");
			DBAccess.DropTableColumn("WF_Cond", "NodeID");
		}

			///#endregion 升级方向条件.


			///#region 升级视图. 解决厦门信息港的 - 流程监控与授权.
		if (DBAccess.IsExitsObject("V_MyFlowData") == false)
		{
			PowerModel pm11 = new PowerModel();
			pm11.CheckPhysicsTable();

			sql = "CREATE VIEW V_MyFlowData ";
			sql += " AS ";
			sql += " SELECT A.*, B.EmpNo as MyEmpNo FROM WF_GenerWorkflow A, WF_PowerModel B ";
			sql += " WHERE  A.FK_Flow=B.FlowNo AND B.PowerCtrlType=1 AND A.WFState>= 2";
			sql += "    UNION  ";
			sql += " SELECT A.*, c.No as MyEmpNo FROM WF_GenerWorkflow A, WF_PowerModel B, Port_Emp C, Port_DeptEmpStation D";
			sql += " WHERE  A.FK_Flow=B.FlowNo  AND B.PowerCtrlType=0 AND C.No=D.FK_Emp AND B.StaNo=D.FK_Station AND A.WFState>=2";
			DBAccess.RunSQL(sql);
		}

		//if (DBAccess.IsExitsObject("V_WF_AuthTodolist") == false)
		//{
		//    BP.WF.Auth en = new Auth();
		//    en.CheckPhysicsTable();

		//    sql = "CREATE VIEW V_WF_AuthTodolist ";
		//    sql += " AS ";
		//    sql += " SELECT B.FK_Emp Auther,B.EmpName AuthName,A.PWorkID,A.FK_Node,A.FID,A.WorkID,C.AutherToEmpNo,  C.TakeBackDT, A.FK_Flow, A.FlowName,A.Title ";
		//    sql += " FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B, WF_Auth C";
		//    sql += " WHERE A.WorkID=B.WorkID AND C.AuthType=1 AND B.FK_Emp=C.Auther AND B.IsPass=0 AND B.IsEnable=1 AND A.NodeID = B.FK_Node AND A.WFState >=2";
		//    sql += "    UNION  ";
		//    sql += " SELECT B.FK_Emp Auther,B.EmpName AuthName,A.PWorkID,A.FK_Node,A.FID,A.WorkID, C.AutherToEmpNo, C.TakeBackDT, A.FK_Flow, A.FlowName,A.Title";
		//    sql += " FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B, WF_Auth C";
		//    sql += " WHERE A.WorkID=B.WorkID AND C.AuthType=2 AND B.FK_Emp=C.Auther AND B.IsPass=0 AND B.IsEnable=1 AND A.NodeID = B.FK_Node AND A.WFState >=2 AND A.FK_Flow=C.FlowNo";
		//    DBAccess.RunSQL(sql);
		//}

			///#endregion 升级视图.

		//升级从表的 fk_node .
		//获取需要修改的从表
		String dtlNos = DBAccess.RunSQLReturnString("SELECT B.NO  FROM SYS_GROUPFIELD A, SYS_MAPDTL B WHERE A.CTRLTYPE='Dtl' AND A.CTRLID=B.NO AND FK_NODE!=0");
		if (DataType.IsNullOrEmpty(dtlNos) == false)
		{
			dtlNos = dtlNos.replace(",", "','");
			dtlNos = "('" + dtlNos + "')";
			DBAccess.RunSQL("UPDATE SYS_MAPDTL SET FK_NODE=0 WHERE NO IN " + dtlNos);
		}
		FrmNode nff = new FrmNode();
		nff.CheckPhysicsTable();


			///#region 更新节点名称.
		switch (SystemConfig.getAppCenterDBType())
		{
			case MSSQL:
			case PostgreSQL:
			case HGDB:
			case KingBaseR3:
			case KingBaseR6:
				sql = " UPDATE WF_Direction SET ToNodeName = WF_Node.Name FROM WF_Node ";
				sql += " WHERE WF_Direction.ToNode = WF_Node.NodeID ";
				break;
			case Oracle:
				sql = "UPDATE WF_Direction E SET ToNodeName=(SELECT U.Name FROM WF_Node U WHERE E.ToNode=U.NodeID) WHERE EXISTS (SELECT 1 FROM WF_Node U WHERE E.ToNode=U.NodeID)";
				break;
			default:
				sql = "UPDATE WF_Direction A, WF_Node B SET A.ToNodeName=B.Name WHERE A.ToNode=B.NodeID ";
				break;
		}
		DBAccess.RunSQL(sql);

		//更新groupField.
		switch (SystemConfig.getAppCenterDBType())
		{
			case MySQL:
				sql = "UPDATE Sys_MapDtl, Sys_GroupField B SET Sys_MapDtl.GroupField=B.OID WHERE Sys_MapDtl.No=B.CtrlID AND Sys_MapDtl.GroupField=''";
				break;
			case Oracle:
				sql = "UPDATE Sys_MapDtl E SET GroupField=(SELECT U.OID FROM Sys_GroupField U WHERE E.No=U.CtrlID) WHERE EXISTS (SELECT 1 FROM Sys_GroupField U WHERE E.No=U.CtrlID AND E.GroupField='')";
				DBAccess.RunSQL("UPDATE Sys_MapAttr set tag=''");
				break;
			case MSSQL:
			default:
				sql = "UPDATE Sys_MapDtl SET GroupField=Sys_GroupField.OID FROM Sys_GroupField WHERE Sys_MapDtl.No=Sys_GroupField.CtrlID AND Sys_MapDtl.GroupField=''";
				break;
		}
		DBAccess.RunSQL(sql);

			///#endregion 更新节点名称.


			///#region 升级审核组件
		if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
		{
			sql = "UPDATE WF_FrmNode F INNER JOIN(SELECT FWCSta,NodeID FROM WF_Node ) N on F.FK_Node = N.NodeID and  F.IsEnableFWC =1 SET F.IsEnableFWC = N.FWCSta;";
		}
		if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
		{
			sql = "UPDATE    F SET IsEnableFWC = N. FWCSta  FROM WF_FrmNode F,WF_Node N    WHERE F.FK_Node = N.NodeID AND F.IsEnableFWC =1";
		}
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.KingBaseR3 || SystemConfig.getAppCenterDBType() == DBType.KingBaseR6)
		{
			sql = "UPDATE WF_FrmNode F  SET (IsEnableFWC)=(SELECT FWCSta FROM WF_Node N WHERE F.FK_Node = N.NodeID AND F.IsEnableFWC =1)";
		}
		if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL || SystemConfig.getAppCenterDBType() == DBType.HGDB || SystemConfig.getAppCenterDBType() == DBType.HGDB || SystemConfig.getAppCenterDBType() == DBType.UX)
		{
			sql = "UPDATE WF_FrmNode SET IsEnableFWC=(SELECT FWCSta FROM WF_Node N Where N.NodeID=WF_FrmNode.FK_Node AND WF_FrmNode.IsEnableFWC=1)";
		}

		DBAccess.RunSQL(sql);

			///#endregion 升级审核组件


			///#region 升级填充数据.
		//pop自动填充
		MapExts exts = new MapExts();
		qo = new QueryObject(exts);
		qo.AddWhere(MapExtAttr.ExtType, " LIKE ", "Pop%");
		qo.DoQuery();
		for (MapExt ext : exts.ToJavaList())
		{
			String mypk = ext.getFrmID() + "_" + ext.getAttrOfOper();
			MapAttr ma = new MapAttr();
			ma.setMyPK(mypk);
			if (ma.RetrieveFromDBSources() == 0)
			{
				ext.Delete();
				continue;
			}

			if (Objects.equals(ma.GetParaString("PopModel"), ext.getExtType()))
			{
				continue; //已经修复了，或者配置了.
			}

			ma.SetPara("PopModel", ext.getExtType());
			ma.Update();

			if (DataType.IsNullOrEmpty(ext.getTag4()) == true)
			{
				continue;
			}

			MapExt extP = new MapExt();
			extP.setMyPK(ext.getMyPK() + "_FullData");
			int i = extP.RetrieveFromDBSources();
			if (i == 1)
			{
				continue;
			}

			extP.setExtType("FullData");
			extP.setFrmID(ext.getFrmID());
			extP.setAttrOfOper(ext.getAttrOfOper());
			extP.setDBType(ext.getDBType());
			extP.setDoc(ext.getTag4());
			extP.Insert(); //执行插入.
		}


		//文本自动填充
		exts = new MapExts();
		exts.Retrieve(MapExtAttr.ExtType, MapExtXmlList.TBFullCtrl, null);
		for (MapExt ext : exts.ToJavaList())
		{
			String mypk = ext.getFrmID() + "_" + ext.getAttrOfOper();
			MapAttr ma = new MapAttr();
			ma.setMyPK(mypk);
			if (ma.RetrieveFromDBSources() == 0)
			{
				ext.Delete();
				continue;
			}
			String modal = ma.GetParaString("TBFullCtrl");
			if (DataType.IsNullOrEmpty(modal) == false)
			{
				continue; //已经修复了，或者配置了.
			}

			if (DataType.IsNullOrEmpty(ext.getTag3()) == false)
			{
				ma.SetPara("TBFullCtrl", "Simple");
			}
			else
			{
				ma.SetPara("TBFullCtrl", "Table");
			}

			ma.Update();

			if (DataType.IsNullOrEmpty(ext.getTag4()) == true)
			{
				continue;
			}

			MapExt extP = new MapExt();
			extP.setMyPK(ext.getMyPK() + "_FullData");
			int i = extP.RetrieveFromDBSources();
			if (i == 1)
			{
				continue;
			}

			extP.setExtType("FullData");
			extP.setFrmID(ext.getFrmID());
			extP.setAttrOfOper(ext.getAttrOfOper());
			extP.setDBType(ext.getDBType());
			extP.setDoc(ext.getTag4());

			//填充从表
			extP.setTag1(ext.getTag1());
			//填充下拉框
			extP.setTag(ext.getTag());

			extP.Insert(); //执行插入.
		}

		//下拉框填充其他控件
		//文本自动填充
		exts = new MapExts();
		exts.Retrieve(MapExtAttr.ExtType, MapExtXmlList.DDLFullCtrl, null);
		for (MapExt ext : exts.ToJavaList())
		{
			String mypk = ext.getFrmID() + "_" + ext.getAttrOfOper();
			MapAttr ma = new MapAttr();
			ma.setMyPK(mypk);
			if (ma.RetrieveFromDBSources() == 0)
			{
				ext.Delete();
				continue;
			}
			String modal = ma.GetParaString("IsFullData");
			if (DataType.IsNullOrEmpty(modal) == false)
			{
				continue; //已经修复了，或者配置了.
			}

			//启用填充其他控件
			ma.SetPara("IsFullData", 1);
			ma.Update();


			MapExt extP = new MapExt();
			extP.setMyPK(ext.getMyPK() + "_FullData");
			int i = extP.RetrieveFromDBSources();
			if (i == 1)
			{
				continue;
			}

			extP.setExtType("FullData");
			extP.setFrmID(ext.getFrmID());
			extP.setAttrOfOper(ext.getAttrOfOper());
			extP.setDBType(ext.getDBType());
			extP.setDoc(ext.getDoc());


			//填充从表
			extP.setTag1(ext.getTag1());
			//填充下拉框
			extP.setTag(ext.getTag());

			extP.Insert(); //执行插入.

		}

		//装载填充
		exts = new MapExts();
		exts.Retrieve(MapExtAttr.ExtType, MapExtXmlList.PageLoadFull, null);
		for (MapExt ext : exts.ToJavaList())
		{
			mapData.setNo(ext.getFrmID());
			if (mapData.RetrieveFromDBSources() == 0)
			{
				ext.Delete();
				continue;
			}
			if (DataType.IsNullOrEmpty(mapData.GetParaString("IsPageLoadFull")) == false)
			{
				continue;
			}
			mapData.setItIsPageLoadFull(true);
			mapData.Update();

			//修改填充数据的值
			ext.setDoc(ext.getTag());

			String tag1 = ext.getTag1();
			if (DataType.IsNullOrEmpty(tag1) == true)
			{
				ext.Update();
				continue;
			}
			MapDtls dtls = mapData.getMapDtls();
			for (MapDtl dtl : dtls.ToJavaList())
			{
				tag1 = tag1.replace("*" + dtl.getNo() + "=", "$" + dtl.getNo() + ":");
			}
			ext.setTag1(tag1);
			ext.Update();
		}

			///#endregion 升级 填充数据.

		String msg = "";
		try
		{


				///#region 升级事件.
			if (DBAccess.IsExitsTableCol("Sys_FrmEvent", "DoType") == true)
			{
				FrmEvent frmevent = new FrmEvent();
				frmevent.CheckPhysicsTable();

				DBAccess.RunSQL("UPDATE Sys_FrmEvent SET EventDoType=DoType  ");
				DBAccess.RunSQL("ALTER TABLE Sys_FrmEvent  DROP COLUMN	DoType  ");
			}

				///#endregion


				///#region 修复丢失的发起人.
			Flows fls = new Flows();
			fls.getNewEntity().CheckPhysicsTable();

			for (Flow item : fls.ToJavaList())
			{
				if (DBAccess.IsExitsObject(item.getPTable()) == false)
				{
					continue;
				}
				try
				{
					DBAccess.RunSQL(" UPDATE " + item.getPTable() + " SET FlowStarter =(SELECT Starter FROM WF_GENERWORKFLOW WHERE " + item.getPTable() + ".Oid=WF_GENERWORKFLOW.WORKID)");
				}
				catch (RuntimeException ex)
				{
					//   GERpt rpt=new GERpt(
				}
			}

				///#endregion 修复丢失的发起人.


				///#region 给city 设置拼音.
			if (DBAccess.IsExitsObject("CN_City") == true && 1 == 2)
			{
				if (DBAccess.IsExitsTableCol("CN_City", "PinYin") == true)
				{
					/*为cn_city 设置拼音.*/
					sql = "SELECT No,Names FROM CN_City ";
					DataTable dtCity = DBAccess.RunSQLReturnTable(sql);

					for (DataRow dr : dtCity.Rows)
					{
						String no = dr.getValue("No").toString();
						String name = dr.getValue("Names").toString();
						String pinyin1 = DataType.ParseStringToPinyin(name);
						String pinyin2 = DataType.ParseStringToPinyinJianXie(name);
						String pinyin = "," + pinyin1 + "," + pinyin2 + ",";
						DBAccess.RunSQL("UPDATE CN_City SET PinYin='" + pinyin + "' WHERE NO='" + no + "'");
					}
				}
			}

				///#endregion 给city 设置拼音.

			//增加列FlowStars
			bp.wf.port.WFEmp wfemp = new bp.wf.port.WFEmp();
			wfemp.CheckPhysicsTable();

			DBType dbtype = SystemConfig.getAppCenterDBType();

			FrmRB rb = new FrmRB();
			rb.CheckPhysicsTable();


			MapDtlExt dtlExt = new MapDtlExt();
			dtlExt.CheckPhysicsTable();

			//删除枚举.
			DBAccess.RunSQL("DELETE FROM " + bp.sys.base.Glo.SysEnum() + " WHERE EnumKey IN ('SelectorModel','CtrlWayAth')");

			//2017.5.19 打印模板字段修复
			FrmPrintTemplate bt = new FrmPrintTemplate();
			bt.CheckPhysicsTable();
			if (DBAccess.IsExitsTableCol("Sys_FrmPrintTemplate", "url") == true)
			{
				DBAccess.RunSQL("UPDATE Sys_FrmPrintTemplate SET TempFilePath = Url WHERE TempFilePath IS null");
			}

			//规范升级根目录.
			DataTable dttree = DBAccess.RunSQLReturnTable("SELECT No FROM Sys_FormTree WHERE ParentNo='-1' ");
			if (dttree.Rows.size() == 1)
			{
				DBAccess.RunSQL("UPDATE Sys_FormTree SET ParentNo='1' WHERE ParentNo='0' ");
				DBAccess.RunSQL("UPDATE Sys_FormTree SET No='1' WHERE No='0'  ");
				DBAccess.RunSQL("UPDATE Sys_FormTree SET ParentNo='0' WHERE No='1'");
			}

			//删除垃圾数据.
			MapExt.DeleteDB();

			//升级傻瓜表单.
			MapFrmFool mff = new MapFrmFool();
			mff.CheckPhysicsTable();


				///#region 表单方案中的不可编辑, 旧版本如果包含了这个列.
			if (DBAccess.IsExitsTableCol("WF_FrmNode", "IsEdit") == true)
			{
				/*如果存在这个列,就查询出来=0的设置，就让其设置为不可以编辑的。*/
				sql = "UPDATE WF_FrmNode SET FrmSln=1 WHERE IsEdit=0 ";
				DBAccess.RunSQL(sql);

				sql = "UPDATE WF_FrmNode SET IsEdit=100000";
				DBAccess.RunSQL(sql);
			}

				///#endregion

			//执行升级 2016.04.08 
			Cond cnd = new Cond();
			cnd.CheckPhysicsTable();


				///#region  增加week字段,方便按周统计.
			sql = "SELECT WorkID,RDT FROM WF_GenerWorkFlow WHERE WeekNum=0 or WeekNum is null ";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows)
			{
				sql = "UPDATE WF_GenerWorkFlow SET WeekNum=" + DataType.getCurrentWeekGetWeekByDay(dr.getValue(1).toString().replace("+", " ")) + " WHERE WorkID=" + dr.getValue(0).toString();
				DBAccess.RunSQL(sql);
			}

			//查询.
			CH ch = new CH();
			ch.CheckPhysicsTable();

			sql = "SELECT MyPK,DTFrom FROM WF_CH WHERE WeekNum=0 or WeekNum is null ";
			dt = DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows)
			{
				sql = "UPDATE WF_CH SET WeekNum=" + DataType.getCurrentWeekGetWeekByDay(dr.getValue(1).toString()) + " WHERE MyPK='" + dr.getValue(0).toString() + "'";
				DBAccess.RunSQL(sql);
			}

				///#endregion  增加week字段.


				///#region 检查数据源.
			SFDBSrc src = new SFDBSrc();
			src.setNo("local");
			src.setName("本机数据源");
			if (src.RetrieveFromDBSources() == 0)
			{
				src.Insert();
			}

				///#endregion 检查数据源.


				///#region 20170613.增加审核组件配置项"是否显示退回的审核信息”对应字段 by:liuxianchen
			try
			{
				if (DBAccess.IsExitsTableCol("WF_Node", "FWCIsShowReturnMsg") == false)
				{
					switch (src.getHisDBType())
					{
						case MSSQL:
							DBAccess.RunSQL("ALTER TABLE WF_Node ADD FWCIsShowReturnMsg INT NULL");
							break;
						case Oracle:
						case Informix:
						case PostgreSQL:
						case HGDB:
						case UX:
						case KingBaseR3:
						case KingBaseR6:
							DBAccess.RunSQL("ALTER TABLE WF_Node ADD FWCIsShowReturnMsg INTEGER NULL");
							break;
						case MySQL:
							DBAccess.RunSQL("ALTER TABLE WF_Node ADD FWCIsShowReturnMsg INT NULL");
							break;
						default:
							break;
					}

					DBAccess.RunSQL("UPDATE WF_Node SET FWCIsShowReturnMsg = 0");
				}
			}
			catch (java.lang.Exception e)
			{
			}

				///#endregion


				///#region 20170522.增加SL表单设计器中对单选/复选按钮进行字体大小调节的功能 by:liuxianchen

			FrmRB frmRB = new FrmRB();
			frmRB.CheckPhysicsTable();

			try
			{
				DataTable columns = src.GetColumns("Sys_FrmRB");
				if (columns.Select("No='AtPara'").length == 0)
				{
					switch (src.getHisDBType())
					{
						case MSSQL:
							DBAccess.RunSQL("ALTER TABLE Sys_FrmRB ADD AtPara NVARCHAR(1000) NULL");
							break;
						case Oracle:
						case KingBaseR3:
						case KingBaseR6:
							DBAccess.RunSQL("ALTER TABLE Sys_FrmRB ADD AtPara NVARCHAR2(1000) NULL");
							break;
						case PostgreSQL:
						case UX:
						case HGDB:
							DBAccess.RunSQL("ALTER TABLE Sys_FrmRB ADD AtPara VARCHAR2(1000) NULL");
							break;
						case MySQL:
						case Informix:
							DBAccess.RunSQL("ALTER TABLE Sys_FrmRB ADD AtPara TEXT NULL");
							break;
						default:
							break;
					}
				}
			}
			catch (java.lang.Exception e2)
			{
			}

				///#endregion


				///#region 其他.
			// 更新 PassRate.
			sql = "UPDATE WF_Node SET PassRate=100 WHERE PassRate IS NULL";
			DBAccess.RunSQL(sql);

				///#endregion 其他.


				///#region 升级统一规则.

				///#region 检查必要的升级。
			NodeWorkCheck fwc = new NodeWorkCheck();
			fwc.CheckPhysicsTable();

			Flow myfl = new Flow();
			myfl.CheckPhysicsTable();

			Node nd = new Node();
			nd.CheckPhysicsTable();

			//Sys_SFDBSrc
			SFDBSrc sfDBSrc = new SFDBSrc();
			sfDBSrc.CheckPhysicsTable();

				///#endregion 检查必要的升级。
			MapExt mapExt = new MapExt();
			mapExt.CheckPhysicsTable();

			try
			{
				String sqls = "";

				if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.HGDB || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL || SystemConfig.getAppCenterDBType() == DBType.UX || SystemConfig.getAppCenterDBType() == DBType.KingBaseR3 || SystemConfig.getAppCenterDBType() == DBType.KingBaseR6)
				{
					sqls += "UPDATE Sys_MapExt SET MyPK= ExtType||'_'||FK_Mapdata||'_'||AttrOfOper WHERE ExtType='" + MapExtXmlList.TBFullCtrl + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK= ExtType||'_'||FK_Mapdata||'_'||AttrOfOper WHERE ExtType='" + MapExtXmlList.PopVal + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK= ExtType||'_'||FK_Mapdata||'_'||AttrOfOper WHERE ExtType='" + MapExtXmlList.DDLFullCtrl + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK= ExtType||'_'||FK_Mapdata||'_'||AttrsOfActive WHERE ExtType='" + MapExtXmlList.ActiveDDL + "'";
				}
				else if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
				{
					sqls += "UPDATE Sys_MapExt SET MyPK=CONCAT(ExtType,'_',FK_Mapdata,'_',AttrOfOper) WHERE ExtType='" + MapExtXmlList.TBFullCtrl + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK=CONCAT(ExtType,'_',FK_Mapdata,'_',AttrOfOper) WHERE ExtType='" + MapExtXmlList.PopVal + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK=CONCAT(ExtType,'_',FK_Mapdata,'_',AttrOfOper) WHERE ExtType='" + MapExtXmlList.DDLFullCtrl + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK=CONCAT(ExtType,'_',FK_Mapdata,'_',AttrOfOper) WHERE ExtType='" + MapExtXmlList.ActiveDDL + "'";
				}
				else
				{
					sqls += "UPDATE Sys_MapExt SET MyPK= ExtType+'_'+FK_Mapdata+'_'+AttrOfOper WHERE ExtType='" + MapExtXmlList.TBFullCtrl + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK= ExtType+'_'+FK_Mapdata+'_'+AttrOfOper WHERE ExtType='" + MapExtXmlList.PopVal + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK= ExtType+'_'+FK_Mapdata+'_'+AttrOfOper WHERE ExtType='" + MapExtXmlList.DDLFullCtrl + "'";
					sqls += "@UPDATE Sys_MapExt SET MyPK= ExtType+'_'+FK_Mapdata+'_'+AttrsOfActive WHERE ExtType='" + MapExtXmlList.ActiveDDL + "'";

				}

				DBAccess.RunSQLs(sqls);
			}
			catch (RuntimeException ex)
			{
				Log.DebugWriteError(ex.getMessage());
			}

				///#endregion


				///#region 其他.
			//升级表单树. 2015.10.05
			SysFormTree sft = new SysFormTree();
			sft.CheckPhysicsTable();


			//表单信息表.
			MapDataExt mapext = new MapDataExt();
			mapext.CheckPhysicsTable();

			TransferCustom tc = new TransferCustom();
			tc.CheckPhysicsTable();

			//增加部门字段。
			CCList cc = new CCList();
			cc.CheckPhysicsTable();

				///#endregion 其他.


				///#region 升级sys_sftable
			//升级
			SFTable tab = new SFTable();
			tab.CheckPhysicsTable();

				///#endregion


				///#region 基础数据更新.
			Node wf_Node = new Node();
			wf_Node.CheckPhysicsTable();
			// 设置节点ICON.
			sql = "UPDATE WF_Node SET ICON='审核.png' WHERE ICON IS NULL";
			DBAccess.RunSQL(sql);

			NodeSheet nodeSheet = new NodeSheet();
			nodeSheet.CheckPhysicsTable();


				///#endregion 基础数据更新.


				///#region 升级SelectAccper

			SelectAccper selectAccper = new SelectAccper();
			selectAccper.CheckPhysicsTable();

				///#endregion


				///#region  升级 NodeToolbar
			FrmField ff = new FrmField();
			ff.CheckPhysicsTable();

			SysFormTree ssft = new SysFormTree();
			ssft.CheckPhysicsTable();

			FrmAttachment myath = new FrmAttachment();
			myath.CheckPhysicsTable();

			FrmField ffs = new FrmField();
			ffs.CheckPhysicsTable();

				///#endregion


				///#region 执行sql．
			DBAccess.RunSQL("delete  from " + bp.sys.base.Glo.SysEnum() + " WHERE EnumKey in ('PrintFileType','EventDoType','FormType','BatchRole','StartGuideWay','NodeFormType')");
			DBAccess.RunSQL("UPDATE Sys_FrmSln SET FK_Flow =(SELECT FK_FLOW FROM WF_Node WHERE NODEID=Sys_FrmSln.FK_Node) WHERE FK_Flow IS NULL");

			if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
			{
				DBAccess.RunSQL("UPDATE WF_FrmNode SET MyPK=FK_Frm+'_'+convert(varchar,FK_Node )+'_'+FK_Flow");
			}

			if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.HGDB || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL || SystemConfig.getAppCenterDBType() == DBType.UX || SystemConfig.getAppCenterDBType() == DBType.KingBaseR3 || SystemConfig.getAppCenterDBType() == DBType.KingBaseR6)
			{
				DBAccess.RunSQL("UPDATE WF_FrmNode SET MyPK=FK_Frm||'_'||FK_Node||'_'||FK_Flow");
			}

			if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
			{
				DBAccess.RunSQL("UPDATE WF_FrmNode SET MyPK=CONCAT(FK_Frm,'_',FK_Node,'_',FK_Flow)");
			}


				///#endregion


				///#region 执行更新.wf_node
			sql = "UPDATE WF_Node SET FWCType=0 WHERE FWCType IS NULL";
			sql += "@UPDATE WF_Node SET FWC_H=0 WHERE FWC_H IS NULL";
			DBAccess.RunSQLs(sql);

			sql = "UPDATE WF_Node SET SFSta=0 WHERE SFSta IS NULL";
			sql += "@UPDATE WF_Node SET SF_H=0 WHERE SF_H IS NULL";
			DBAccess.RunSQLs(sql);

				///#endregion 执行更新.


				///#region 升级站内消息表 2013-10-20
			bp.wf.SMS sms = new SMS();
			sms.CheckPhysicsTable();

				///#endregion


				///#region 重新生成view WF_EmpWorks,  2013-08-06.
			if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
			{
				if (DBAccess.IsExitsObject("WF_EmpWorks") == true)
				{
					DBAccess.RunSQL("DROP VIEW WF_EmpWorks");
				}

				if (DBAccess.IsExitsObject("V_FlowStarterBPM") == true)
				{
					DBAccess.RunSQL("DROP VIEW V_FlowStarterBPM");
				}

				if (DBAccess.IsExitsObject("V_TOTALCH") == true)
				{
					DBAccess.RunSQL("DROP VIEW V_TOTALCH");
				}

				if (DBAccess.IsExitsObject("V_TOTALCHYF") == true)
				{
					DBAccess.RunSQL("DROP VIEW V_TOTALCHYF");
				}

				if (DBAccess.IsExitsObject("V_TotalCHWeek") == true)
				{
					DBAccess.RunSQL("DROP VIEW V_TotalCHWeek");
				}

				if (DBAccess.IsExitsObject("V_WF_Delay") == true)
				{
					DBAccess.RunSQL("DROP VIEW V_WF_Delay");
				}

				String sqlscript = "";
				//执行必须的sql.
				switch (SystemConfig.getAppCenterDBType())
				{
					case Oracle:
					case KingBaseR3:
					case KingBaseR6:
						sqlscript = SystemConfig.getPathOfData() + "Install/SQLScript/InitView_Ora.sql";
						break;
					case MSSQL:
					case Informix:
						sqlscript = SystemConfig.getPathOfData() + "Install/SQLScript/InitView_SQL.sql";
						break;
					case MySQL:
						sqlscript = SystemConfig.getPathOfData() + "Install/SQLScript/InitView_MySQL.sql";
						break;
					case PostgreSQL:
					case UX:
					case HGDB:
						sqlscript = SystemConfig.getPathOfData() + "Install/SQLScript/InitView_PostgreSQL.sql";
						break;
					default:
						break;
				}
				DBAccess.RunSQLScript(sqlscript);
			}

				///#endregion


				///#region 升级Img
			FrmImg img = new FrmImg();
			img.CheckPhysicsTable();

			ExtImg myimg = new ExtImg();
			myimg.CheckPhysicsTable();
			if (DBAccess.IsExitsTableCol("Sys_FrmImg", "SrcType") == true)
			{
				DBAccess.RunSQL("UPDATE Sys_FrmImg SET ImgSrcType = SrcType WHERE ImgSrcType IS NULL");
				DBAccess.RunSQL("UPDATE Sys_FrmImg SET ImgSrcType = 0 WHERE ImgSrcType IS NULL");
			}

				///#endregion


				///#region 修复 mapattr UIHeight, UIWidth 类型错误.
			switch (SystemConfig.getAppCenterDBType())
			{
				case Oracle:
				case KingBaseR3:
				case KingBaseR6:
					msg = "@Sys_MapAttr 修改字段";
					break;
				case MSSQL:
					msg = "@修改sql server控件高度和宽度字段。";
					DBAccess.RunSQL("ALTER TABLE Sys_MapAttr ALTER COLUMN UIWidth float");
					DBAccess.RunSQL("ALTER TABLE Sys_MapAttr ALTER COLUMN UIHeight float");
					break;
				default:
					break;
			}

				///#endregion


				///#region 升级常用词汇
			switch (SystemConfig.getAppCenterDBType())
			{
				case Oracle:
				case KingBaseR3:
				case KingBaseR6:
					int i = DBAccess.RunSQLReturnCOUNT("SELECT * FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'SYS_DEFVAL' AND COLUMN_NAME = 'PARENTNO'");
					if (i == 0)
					{
						if (DBAccess.IsExitsObject("SYS_DEFVAL") == true)
						{
							DBAccess.RunSQL("drop table Sys_DefVal");
						}

						DefVal dv = new DefVal();
						dv.CheckPhysicsTable();
					}
					msg = "@Sys_DefVal 修改字段";
					break;
				case MSSQL:
					msg = "@修改 Sys_DefVal。";
					i = DBAccess.RunSQLReturnCOUNT("SELECT * FROM SYSCOLUMNS WHERE ID=OBJECT_ID('Sys_DefVal') AND NAME='ParentNo'");
					if (i == 0)
					{
						if (DBAccess.IsExitsObject("Sys_DefVal") == true)
						{
							DBAccess.RunSQL("drop table Sys_DefVal");
						}

						DefVal dv = new DefVal();
						dv.CheckPhysicsTable();
					}
					break;
				default:
					break;
			}

				///#endregion


				///#region 登陆更新错误
			msg = "@登陆时错误。";
			DBAccess.RunSQL("DELETE FROM " + bp.sys.base.Glo.SysEnum() + " WHERE EnumKey IN ('DeliveryWay','RunModel','OutTimeDeal','FlowAppType')");

				///#endregion 登陆更新错误


				///#region 升级表单树
			// 首先检查是否升级过.
			//sql = "SELECT * FROM Sys_FormTree WHERE No = '1'";
			//DataTable formTree_dt = DBAccess.RunSQLReturnTable(sql);
			//if (formTree_dt.Rows.size() == 0)
			//{
			//    /*没有升级过.增加根节点*/
			//    SysFormTree formTree = new SysFormTree();
			//    formTree.setNo("1";
			//    formTree.setName("表单库";
			//    formTree.ParentNo = "0";
			//    // formTree.TreeNo = "0";
			//    formTree.setIdx(0;
			//    formTree.IsDir = true;
			//        formTree.DirectInsert();

			//    //将表单库中的数据转入表单树
			//    SysFormTrees formSorts = new SysFormTrees();
			//    formSorts.RetrieveAll();

			//    foreach (SysFormTree item in formSorts)
			//    {
			//        if (item.getNo() == "0")
			//            continue;
			//        SysFormTree subFormTree = new SysFormTree();
			//        subFormTree.setNo(item.getNo();
			//        subFormTree.setName(item.Name;
			//        subFormTree.ParentNo = "0";
			//        subFormTree.Save();
			//    }
			//    //表单于表单树进行关联
			//    sql = "UPDATE Sys_MapData SET FK_FormTree=FK_FrmSort WHERE FK_FrmSort <> '' AND FK_FrmSort IS not null";
			//    DBAccess.RunSQL(sql);
			//}

				///#endregion


				///#region 执行admin登陆. 2012-12-25 新版本.
			Emp emp = new Emp("admin");
			if (emp.RetrieveFromDBSources() == 1)
			{
				WebUser.SignInOfGener(emp, "CH", false, false, null, null);
			}
			else
			{
				emp.SetValByKey("No", "admin");
				emp.setName("admin");
				emp.setDeptNo("01");
				emp.setPass("123");
				emp.Insert();
				WebUser.SignInOfGener(emp, "CH", false, false, null, null);
				//throw new Exception("admin 用户丢失，请注意大小写。");
			}

				///#endregion 执行admin登陆.


				///#region 修复 Sys_FrmImg 表字段 ImgAppType Tag0
			switch (SystemConfig.getAppCenterDBType())
			{
				case Oracle:
				case KingBaseR3:
				case KingBaseR6:
					int i = DBAccess.RunSQLReturnCOUNT("SELECT * FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'SYS_FRMIMG' AND COLUMN_NAME = 'TAG0'");
					if (i == 0)
					{
						DBAccess.RunSQL("ALTER TABLE SYS_FRMIMG ADD (ImgAppType number,TAG0 nvarchar(500))");
					}
					msg = "@Sys_FrmImg 修改字段";
					break;
				case MSSQL:
					msg = "@修改 Sys_FrmImg。";
					i = DBAccess.RunSQLReturnCOUNT("SELECT * FROM SYSCOLUMNS WHERE ID=OBJECT_ID('Sys_FrmImg') AND NAME='Tag0'");
					if (i == 0)
					{
						DBAccess.RunSQL("ALTER TABLE Sys_FrmImg ADD ImgAppType int");
						DBAccess.RunSQL("ALTER TABLE Sys_FrmImg ADD Tag0 nvarchar(500)");
					}
					break;
				default:
					break;
			}
			FrmImgAth imgAth = new FrmImgAth();
			imgAth.CheckPhysicsTable();

			sql = "UPDATE Sys_FrmImgAth SET IsRequired = 0 WHERE IsRequired IS NULL";
			DBAccess.RunSQL(sql);
			try
			{
				DataTable columns = src.GetColumns("Sys_FrmAttachment");
				if (columns.Select("No='DeleteWay'").length > 0 && columns.Select("No='IsDelete'").length > 0)
				{
					DBAccess.RunSQL("UPDATE SYS_FRMATTACHMENT SET DeleteWay=IsDelete WHERE DeleteWay IS NULL");
				}
			}
			catch (java.lang.Exception e3)
			{
			}
				///#region 密码加密
			if (SystemConfig.getIsEnablePasswordEncryption() == true && DBAccess.IsView("Port_Emp", SystemConfig.getAppCenterDBType()) == false)
			{
				Emps emps = new Emps();
				emps.RetrieveAllFromDBSource();
				for (Emp empEn : emps.ToJavaList())
				{
					if (DataType.IsNullOrEmpty(empEn.getPass()) || empEn.getPass().length() < 30)
					{
						empEn.setPass(bp.tools.MD5Utill.MD5Encode(empEn.getPass(),"UTF8"));
						empEn.DirectUpdate();
					}
				}
			}

				///#endregion

			// 最后更新版本号，然后返回.
			sql = "UPDATE Sys_Serial SET IntVal=" + Ver + " WHERE CfgKey='Ver'";
			if (DBAccess.RunSQL(sql) == 0)
			{
				sql = "INSERT INTO Sys_Serial (CfgKey,IntVal) VALUES('Ver'," + Ver + ") ";
				DBAccess.RunSQL(sql);
			}
			// 返回版本号.
			return "旧版本:(" + currDBVer + ")新版本:(" + Ver + ")"; // +"\t\n解决问题:" + updataNote;
		}
		catch (RuntimeException ex)
		{
			String err = "问题出处:" + ex.getMessage() + "<hr>" + msg + "<br>详细信息:@" + ex.getStackTrace() + "<br>@<a href='../DBInstall.htm' >点这里到系统升级界面。</a>";
			Log.DebugWriteError("系统升级错误:" + err);
			return err;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/** 
	 如果发现升级sql文件日期变化了，就自动升级.
	 就是说该文件如果被修改了就会自动升级.
	*/
	public static void UpdataCCFlowVerSQLScript()
	{

		String sql = "SELECT IntVal FROM Sys_Serial WHERE CfgKey='UpdataCCFlowVer'";
		String currDBVer = DBAccess.RunSQLReturnStringIsNull(sql, "");

		String sqlScript = SystemConfig.getPathOfData() + "UpdataCCFlowVer.sql";
		File fi = new File(sqlScript);
		if ((new File(sqlScript)).isFile() == false)
		{
			return;
		}
		long lastModified = fi.lastModified();
		Date date = new Date(lastModified);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		String myVer = sdf.format(date);

		//判断是否可以执行，当文件发生变化后，才执行。
		if (Objects.equals(currDBVer, "") || Integer.parseInt(currDBVer) < Integer.parseInt(myVer))
		{
			DBAccess.RunSQLScript(sqlScript);
			sql = "UPDATE Sys_Serial SET IntVal=" + myVer + " WHERE CfgKey='UpdataCCFlowVer'";

			if (DBAccess.RunSQL(sql) == 0)
			{
				sql = "INSERT INTO Sys_Serial (CfgKey,IntVal) VALUES('UpdataCCFlowVer'," + myVer + ") ";
				DBAccess.RunSQL(sql);
			}
		}
	}
	/** 
	 CCFlowAppPath
	*/
	public static String getCCFlowAppPath()
	{
		return SystemConfig.GetValByKey("CCFlowAppPath", "/");
	}
	/** 
	 
	*/
	public static boolean isEnableHuiQianList()
	{
		if (Objects.equals(SystemConfig.getCustomerNo(), "TianYe"))
		{
			return true;
		}

		return SystemConfig.GetValByKeyBoolen("IsEnableHuiQianList", false);
	}
	/** 
	 检查是否可以安装驰骋BPM系统
	 
	 @return 
	*/
	public static boolean IsCanInstall()
	{
		String sql = "";
		String errInfo = "";
		try
		{
			errInfo = " 当前用户没有[查询系统表]的权限. ";

			if (DBAccess.IsExitsObject("AA"))
			{
				errInfo = " 当前用户没有[删除表]的权限. ";
				sql = "DROP TABLE AA";
				DBAccess.RunSQL(sql);
			}

			errInfo = " 当前用户没有[创建表]的权限. ";
			sql = "CREATE TABLE AA (OID int NOT NULL)"; //检查是否可以创建表.
			DBAccess.RunSQL(sql);

			errInfo = " 当前用户没有[插入数据]的权限. ";
			sql = "INSERT INTO AA (OID) VALUES(100)";
			DBAccess.RunSQL(sql);

			try
			{
				//检查是否可以批量执行sql.
				sql = "UPDATE AA SET OID=0 WHERE OID=1;UPDATE AA SET OID=0 WHERE OID=1;";
				DBAccess.RunSQL(sql);
			}
			catch (java.lang.Exception e)
			{
				throw new RuntimeException("err@需要让数据库链接支持批量执行SQL语句，请修改数据库链接配置：&allowMultiQueries=true");
			}

			errInfo = " 当前用户没有[update 表数据]的权限. ";
			sql = "UPDATE AA SET OID=101";
			DBAccess.RunSQL(sql);

			errInfo = " 当前用户没有[delete 表数据]的权限. ";
			sql = "DELETE FROM AA";
			DBAccess.RunSQL(sql);

			errInfo = " 当前用户没有[创建表主键]的权限. ";
			DBAccess.CreatePK("AA", "OID", SystemConfig.getAppCenterDBType());

			errInfo = " 当前用户没有[创建索引]的权限. ";
			DBAccess.CreatIndex("AA", "OID"); //可否创建索引.

			errInfo = " 当前用户没有[查询数据表]的权限. ";
			sql = "select * from AA "; //检查是否有查询的权限.
			DBAccess.RunSQLReturnTable(sql);

			errInfo = " 当前数据库设置区分了大小写，不能对大小写敏感，如果是mysql数据库请参考 https://blog.csdn.net/ccflow/article/details/100079825 ";
			sql = "select * from aa "; //检查是否区分大小写.
			DBAccess.RunSQLReturnTable(sql);

			if (DBAccess.IsExitsObject("AAVIEW"))
			{
				errInfo = " 当前用户没有[删除视图]的权限. ";
				sql = "DROP VIEW AAVIEW";
				DBAccess.RunSQL(sql);
			}

			errInfo = " 当前用户没有[创建视图]的权限.";
			sql = "CREATE VIEW AAVIEW AS SELECT * FROM AA "; //检查是否可以创建视图.
			DBAccess.RunSQL(sql);

			errInfo = " 当前用户没有[删除视图]的权限.";
			sql = "DROP VIEW AAVIEW"; //检查是否可以删除视图.
			DBAccess.RunSQL(sql);

			errInfo = " 当前用户没有[删除表]的权限.";
			sql = "DROP TABLE AA"; //检查是否可以删除表.
			DBAccess.RunSQL(sql);

			if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
			{
				try
				{
					sql = " set @@global.sql_mode ='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';";
					DBAccess.RunSQL(sql);
				}
				catch (java.lang.Exception e2)
				{
				}
			}

			if (SystemConfig.getAppCenterDBDatabase().contains("-") == true)
			{
				throw new RuntimeException("err@数据库名称不能包含 '-' 号，您可以使用 '_' .");
			}

			return true;
		}
		catch (RuntimeException ex)
		{
			if (DBAccess.IsExitsObject("AA") == true)
			{
				sql = "DROP TABLE AA";
				DBAccess.RunSQL(sql);
			}

			if (DBAccess.IsExitsObject("AAVIEW") == true)
			{
				sql = "DROP VIEW AAVIEW";
				DBAccess.RunSQL(sql);
			}

			String info = "驰骋工作流引擎 - 检查数据库安装权限出现错误:";
			info += "\t\n1. 当前登录的数据库帐号，必须有创建、删除视图或者table的权限。";
			info += "\t\n2. 必须对表有增、删、改、查的权限。 ";
			info += "\t\n3. 必须有删除创建索引主键的权限。 ";
			info += "\t\n4. 我们建议您设置当前数据库连接用户为管理员权限。 ";
			info += "\t\n ccbpm检查出来的信息如下：" + errInfo;
			info += "\t\n etc: 数据库测试异常信息:" + ex.getMessage();
			throw new RuntimeException("err@" + info);
			//  return false;
		}
	}

	/** 
	 安装包
	 
	 @param lang 语言
	 @param demoType 0安装Demo, 1 不安装Demo.
	*/
	public static void DoInstallDataBase(String lang, int demoType) throws Exception {
		boolean isInstallFlowDemo = true;
		if (demoType == 2)
		{
			isInstallFlowDemo = false;
		}


			///#region 首先创建Port类型的表, 让admin登录.

		SFDBSrc sf = new SFDBSrc();
		sf.CheckPhysicsTable();

		FrmRB rb = new FrmRB();
		rb.CheckPhysicsTable();

		Emp portEmp = new Emp();
		portEmp.CheckPhysicsTable();


		Dept mydept = new Dept();
		mydept.CheckPhysicsTable();

		Station mySta = new Station();
		mySta.CheckPhysicsTable();

		StationType myStaType = new StationType();
		myStaType.CheckPhysicsTable();

		DeptEmp myde = new DeptEmp();
		myde.CheckPhysicsTable();

		DeptEmpStation mydes = new DeptEmpStation();
		mydes.CheckPhysicsTable();

		bp.wf.port.WFEmp wfemp = new bp.wf.port.WFEmp();
		wfemp.CheckPhysicsTable();

		//@hongyan.
		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			bp.wf.port.admingroup.Org org = new bp.wf.port.admingroup.Org();
			org.CheckPhysicsTable();

			bp.wf.port.admin2group.OrgAdminer oa = new bp.wf.port.admin2group.OrgAdminer();
			oa.CheckPhysicsTable();

			FlowSort fs = new FlowSort();
			fs.CheckPhysicsTable();

			SysFormTree ft = new SysFormTree();
			ft.CheckPhysicsTable();
		}

		if (DBAccess.IsExitsTableCol("WF_Emp", "StartFlows") == false)
		{
			String sql = "";
			//增加StartFlows这个字段
			switch (SystemConfig.getAppCenterDBType())
			{
				case MSSQL:
					sql = "ALTER TABLE WF_Emp ADD StartFlows Text DEFAULT  NULL";
					break;
				case Oracle:
				case KingBaseR3:
				case KingBaseR6:
					sql = "ALTER TABLE  WF_EMP add StartFlows BLOB";
					break;
				case MySQL:
					sql = "ALTER TABLE WF_Emp ADD StartFlows TEXT COMMENT '可以发起的流程'";
					break;
				case Informix:
					sql = "ALTER TABLE WF_Emp ADD StartFlows VARCHAR(4000) DEFAULT  NULL";
					break;
				case PostgreSQL:
				case UX:
				case HGDB:
					sql = "ALTER TABLE WF_Emp ADD StartFlows Text DEFAULT  NULL";
					break;
				default:
					throw new RuntimeException("@没有涉及到的数据库类型");
			}
			DBAccess.RunSQL(sql);
		}

			///#endregion 首先创建Port类型的表.


			///#region 3, 执行基本的 sql
		String sqlscript = "";

		Emp empGPM = new Emp();
		empGPM.CheckPhysicsTable();

		DeptEmp de = new DeptEmp();
		de.CheckPhysicsTable();


		if (DBAccess.IsExitsTableCol("Port_Emp", "Pass") == false)
		{
			if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
			{
				DBAccess.RunSQL("ALTER TABLE Port_Emp ADD Pass VARCHAR(90) DEFAULT '123' NULL ");
			}
			else if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL || SystemConfig.getAppCenterDBType() == DBType.HGDB)
			{
				DBAccess.RunSQL("ALTER TABLE Port_Emp ADD Pass VARCHAR(90) DEFAULT '123' NULL ");
			}
			else
			{
				DBAccess.RunSQL("ALTER TABLE Port_Emp ADD Pass NVARCHAR(90) DEFAULT '123' NULL ");
			}
		}
		//初始化数据
		sqlscript = SystemConfig.getCCFlowAppPath() + "WF/Data/Install/SQLScript/Port_Inc_CH_RunModel_" + SystemConfig.getCCBPMRunModel().getValue() + ".sql";
		DBAccess.RunSQLScript(sqlscript);


		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			Emp empAdmin = new Emp("admin");
			WebUser.SignInOfGener(empAdmin, "CH", false, false, null, null);
		}

		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			bp.wf.Dev2Interface.Port_Login("admin", "100", null);
		}

		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.GroupInc)
		{
			bp.wf.Dev2Interface.Port_Login("admin", "100", null);
		}

			///#endregion 执行基本的 sql

		ArrayList al = null;
		String info = "BP.En.Entity";
		al = ClassFactory.GetObjects(info);


			///#region 先创建表，否则列的顺序就会变化.
		FlowExt fe = new FlowExt();
		fe.CheckPhysicsTable();

		NodeExt ne = new NodeExt();
		ne.CheckPhysicsTable();

		MapDtl mapdtl = new MapDtl();
		mapdtl.CheckPhysicsTable();

		MapData mapData = new MapData();
		mapData.CheckPhysicsTable();

		SysEnum sysenum = new SysEnum();
		sysenum.CheckPhysicsTable();

		NodeWorkCheck fwc = new NodeWorkCheck();
		fwc.CheckPhysicsTable();

		MapAttr attr = new MapAttr();
		attr.CheckPhysicsTable();

		GenerWorkFlow gwf = new GenerWorkFlow();
		gwf.CheckPhysicsTable();

		CH ch = new CH();
		ch.CheckPhysicsTable();


			///#endregion 先创建表，否则列的顺序就会变化.


			///#region 1, 创建or修复表
		for (Object obj : al)
		{
			Entity en = null;
			en = obj instanceof Entity ? (Entity)obj : null;
			if (en == null)
			{
				continue;
			}

			//获得类名.
			String clsName = en.toString();
			if (DataType.IsNullOrEmpty(clsName) == true)
			{
				continue;
			}

			if (clsName.contains("FlowSheet") == true)
			{
				continue;
			}
			if (clsName.contains("NodeSheet") == true)
			{
				continue;
			}
			if (clsName.contains("FlowFormTree") == true)
			{
				continue;
			}

			//不安装CCIM的表.
			if (clsName.contains("BP.CCIM"))
			{
				continue;
			}

			//抽象的类不允许创建表.
			switch (clsName.toUpperCase())
			{
				case "BP.WF.STARTWORK":
				case "BP.WF.WORK":
				case "BP.WF.GESTARTWORK":
				case "BP.EN.GENONAME":
				case "BP.EN.GETREE":
				case "BP.WF.GERpt":
				case "BP.WF.GEENTITY":
				case "BP.SYS.TSENTITYNONAME":
					continue;
				default:
					break;
			}

			if (isInstallFlowDemo == false)
			{
				/*如果不安装demo.*/
				if (clsName.contains("BP.CN") || clsName.contains("BP.Demo"))
				{
					continue;
				}
			}

			String table = null;
			try
			{
				table = en.getEnMap().getPhysicsTable();
				if (table == null)
				{
					continue;
				}
			}
			catch (java.lang.Exception e)
			{
				continue;
			}

			try
			{
				switch (table)
				{
					case "WF_EmpWorks":
					case "WF_GenerEmpWorkDtls":
					case "WF_GenerEmpWorks":
						continue;
					case "Sys_Enum":
						en.CheckPhysicsTable();
						break;
					default:
						en.CheckPhysicsTable();
						break;
				}
				en.CheckPhysicsTable();
			}
			catch (java.lang.Exception e2)
			{
			}
		}

			///#endregion 修复


			///#region 2, 注册枚举类型 SQL
		// 2, 注册枚举类型。
		bp.sys.xml.EnumInfoXmls xmls = new bp.sys.xml.EnumInfoXmls();
		xmls.RetrieveAll();
		for (bp.sys.xml.EnumInfoXml xml : xmls.ToJavaList())
		{
			SysEnums ses = new SysEnums();
			int count = ses.RetrieveByAttr(SysEnumAttr.EnumKey, xml.getKey());
			if (count > 0)
			{
				continue;
			}
			ses.RegIt(xml.getKey(), xml.getVals());
		}

			///#endregion 注册枚举类型


			///#region 2.1 重新构建视图. //删除视图.
		if (DBAccess.IsExitsObject("WF_EmpWorks") == true)
		{
			DBAccess.RunSQL("DROP VIEW WF_EmpWorks");
		}

		if (DBAccess.IsExitsObject("V_WF_Delay") == true)
		{
			DBAccess.RunSQL("DROP VIEW V_WF_Delay");
		}

		if (DBAccess.IsExitsObject("V_FlowStarter") == true)
		{
			DBAccess.RunSQL("DROP VIEW V_FlowStarter");
		}

		if (DBAccess.IsExitsObject("V_FlowStarterBPM") == true)
		{
			DBAccess.RunSQL("DROP VIEW V_FlowStarterBPM");
		}

		if (DBAccess.IsExitsObject("V_TOTALCH") == true)
		{
			DBAccess.RunSQL("DROP VIEW V_TOTALCH");
		}

		if (DBAccess.IsExitsObject("V_TOTALCHYF") == true)
		{
			DBAccess.RunSQL("DROP VIEW V_TOTALCHYF");
		}

		if (DBAccess.IsExitsObject("V_TotalCHWeek") == true)
		{
			DBAccess.RunSQL("DROP VIEW V_TotalCHWeek");
		}

			///#endregion 重新构建视图. //删除视图.


			///#region 4, 创建视图与数据.
		String prix = "";
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			prix = "";
		}

		//执行必须的sql.
		sqlscript = "";
		//执行必须的sql.
		switch (SystemConfig.getAppCenterDBType())
		{
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
				sqlscript = SystemConfig.getCCFlowAppPath() + "WF/Data/Install/SQLScript/InitView_Ora" + prix + ".sql";
				break;
			case MSSQL:
			case Informix:
				sqlscript = SystemConfig.getCCFlowAppPath() + "WF/Data/Install/SQLScript/InitView_SQL" + prix + ".sql";
				break;
			case MySQL:
				sqlscript = SystemConfig.getCCFlowAppPath() + "WF/Data/Install/SQLScript/InitView_MySQL" + prix + ".sql";
				break;
			case PostgreSQL:
			case UX:
			case HGDB:
				sqlscript = SystemConfig.getCCFlowAppPath() + "WF/Data/Install/SQLScript/InitView_PostgreSQL" + prix + ".sql";
				break;
			default:
				break;
		}
		DBAccess.RunSQLScript(sqlscript);

			///#endregion 创建视图与数据


			///#region 5, 初始化数据.
		if (isInstallFlowDemo)
		{
			// sqlscript =  bp.difference.SystemConfig.getPathOfData() + "Install/SQLScript/InitPublicData.sql";
			// DBAccess.RunSQLScript(sqlscript);
		}
		// else
		// {
		// FlowSort fs = new FlowSort();
		// fs.setNo("1";
		// fs.ParentNo = "0";
		// fs.setName("流程树";
		// fs.DirectInsert();
		// }

			///#endregion 初始化数据


			///#region 6, 生成临时的 wf_emp 数据。
		if (isInstallFlowDemo == true)
		{
			Emps emps = new Emps();
			emps.RetrieveAllFromDBSource();
			int i = 0;
			for (Emp emp : emps.ToJavaList())
			{
				i++;
				bp.wf.port.WFEmp wfEmp = new bp.wf.port.WFEmp();
				wfEmp.Copy(emp);
				wfEmp.setNo(emp.getUserID());

				/* if (wfEmp.Email.length() == 0)
				     wfEmp.Email = emp.getUserID() + "@ccbpm.cn";*/

				if (wfEmp.getTel().length() == 0)
				{
					wfEmp.setTel("82374939-6" + StringHelper.padLeft(String.valueOf(i), 2, '0'));
				}

				if (wfEmp.getIsExits())
				{
					wfEmp.Update();
				}
				else
				{
					wfEmp.Insert();
				}
			}

			// 生成简历数据.
			for (Emp emp : emps.ToJavaList())
			{
				for (int myIdx = 0; myIdx < 6; myIdx++)
				{
					String sql = "";
					sql = "INSERT INTO Demo_Resume (OID,RefPK,NianYue,GongZuoDanWei,ZhengMingRen,BeiZhu,QT) ";
					sql += "VALUES(" + DBAccess.GenerOID("Demo_Resume") + ",'" + emp.getUserID() + "','200" + myIdx + "-01','成都.驰骋" + myIdx + "公司','张三','表现良好','其他-" + myIdx + "无')";
					DBAccess.RunSQL(sql);
				}
			}

			DataTable dtStudent = DBAccess.RunSQLReturnTable("SELECT No FROM Demo_Student");
			for (DataRow dr : dtStudent.Rows)
			{
				String no = dr.getValue(0).toString();
				for (int myIdx = 0; myIdx < 6; myIdx++)
				{
					String sql = "";
					sql = "INSERT INTO Demo_Resume (OID,RefPK,NianYue,GongZuoDanWei,ZhengMingRen,BeiZhu,QT) ";
					sql += "VALUES(" + DBAccess.GenerOID("Demo_Resume") + ",'" + no + "','200" + myIdx + "-01','成都.驰骋" + myIdx + "公司','张三','表现良好','其他-" + myIdx + "无')";
					DBAccess.RunSQL(sql);
				}
			}
			GenerWorkFlowViewNY ny = new GenerWorkFlowViewNY();
			ny.CheckPhysicsTable();
			// 生成年度月份数据.
			String sqls = "";
			LocalDateTime dtNow = LocalDateTime.now();
			for (int num = 0; num < 12; num++)
			{
				sqls = "@ INSERT INTO Pub_NY (No,Name) VALUES ('" + DateUtils.format(new Date(), "yyyy-MM")+ "','" + DateUtils.format(new Date(), "yyyy-MM") + "')  ";
				dtNow = dtNow.plusMonths(1);
			}
			DBAccess.RunSQLs(sqls);
		}

			///#endregion 初始化数据


			///#region 7, 装载 demo.flow
		if (isInstallFlowDemo == true && SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			Emp emp = new Emp("admin");
			WebUser.SignInOfGener(emp, "CH", false, false, null, null);
			bp.sys.base.Glo.WriteLineInfo("开始装载模板...");
			String msg = "";

			//装载数据模版.
			bp.wf.dts.LoadTemplete l = new bp.wf.dts.LoadTemplete();
			Object tempVar = l.Do();
			msg = tempVar instanceof String ? (String)tempVar : null;
		}
		//@hongyan.
		if (isInstallFlowDemo == false && SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			//创建一个空白的流程，不然，整个结构就出问题。
			//    FlowSorts fss = new FlowSorts();
			// fss.RetrieveAll();
			//  fss.Delete();
			DBAccess.RunSQL("DELETE FROM WF_FlowSort ");
			FlowSort fs = new FlowSort();
			fs.setName("流程树");
			fs.setNo("1");
			fs.setParentNo("0");
			fs.Insert();

			fs = new FlowSort();
			fs.setNo("101");
			fs.setParentNo("1");
			fs.setName("日常办公类");
			fs.Insert();

			//加载一个模版,不然用户不知道如何新建流程.
			TemplateGlo.LoadFlowTemplate(fs.getNo(), SystemConfig.getPathOfData() + "Install/QingJiaFlowDemoInit.xml", ImpFlowTempleteModel.AsTempleteFlowNo);

			Flow fl = new Flow("001");
			fl.DoCheck(); //做一下检查.

			fs.setNo("102");
			fs.setParentNo("1");
			fs.setName("财务类");
			fs.Insert();

			fs.setNo("103");
			fs.setParentNo("1");
			fs.setName("人力资源类");
			fs.Insert();

			DBAccess.RunSQL("DELETE FROM Sys_FormTree ");
			SysFormTree ftree = new SysFormTree();
			ftree.setName("表单树");
			ftree.setNo("1");
			ftree.setParentNo("0");
			ftree.Insert();

			SysFormTree subFrmTree = new SysFormTree();
			subFrmTree.setName("流程独立表单");
			subFrmTree.setParentNo("1");
			subFrmTree.setNo("101");
			subFrmTree.Insert();

			subFrmTree = new SysFormTree();
			;
			subFrmTree.setNo("102");
			subFrmTree.setName("常用信息管理");
			subFrmTree.setParentNo("1");
			subFrmTree.Insert();

			subFrmTree = new SysFormTree();
			;
			subFrmTree.setName("常用单据");
			subFrmTree.setNo("103");
			subFrmTree.setParentNo("1");
			subFrmTree.Insert();
		}

			///#endregion 装载demo.flow


			///#region 增加图片签名
		if (isInstallFlowDemo == true)
		{
			try
			{
				//增加图片签名
				bp.wf.dts.GenerSiganture gs = new bp.wf.dts.GenerSiganture();
				gs.Do();
			}
			catch (java.lang.Exception e3)
			{
			}
		}

			///#endregion 增加图片签名.

		//生成拼音，以方便关键字查找.
		bp.wf.dts.GenerPinYinForEmp pinyin = new bp.wf.dts.GenerPinYinForEmp();
		pinyin.Do();


			///#region 执行补充的sql, 让外键的字段长度都设置成100.
		DBAccess.RunSQL("UPDATE Sys_MapAttr SET maxlen=100 WHERE LGType=2 AND MaxLen<100");
		DBAccess.RunSQL("UPDATE Sys_MapAttr SET maxlen=100 WHERE KeyOfEn='FK_Dept'");

			///#endregion 执行补充的sql, 让外键的字段长度都设置成100.


			///#region 如果是第一次运行，就执行检查。
		if (isInstallFlowDemo == true)
		{
			Flows fls = new Flows();
			fls.RetrieveAllFromDBSource();
			for (Flow fl : fls.ToJavaList())
			{
				try
				{
					fl.DoCheck();
				}
				catch (RuntimeException ex)
				{
					Log.DebugWriteError(ex.getMessage());
				}
			}
		}

			///#endregion 如果是第一次运行，就执行检查。


			///#region 增加大文本字段列.
		try
		{
			DBAccess.GetBigTextFromDB("Sys_MapData", "No", "001", "HtmlTemplateFile");
		}
		catch (java.lang.Exception e4)
		{
		}

			///#endregion 增加大文本字段列.

		//#region 处理密码加密.
		String pass = SystemConfig.getUserDefaultPass();
		if (SystemConfig.getIsEnablePasswordEncryption() == true)
			pass = bp.tools.MD5Utill.MD5Encode(pass, "UTF8");//bp.tools.Encodes.encodeBase64(pass);
		DBAccess.RunSQL("UPDATE Port_Emp SET Pass='" + pass + "'");
        //#endregion 处理密码加密.
	}

	public static void KillProcess(String processName) //杀掉进程的方法
	{
		/*System.Diagnostics.Process[] processes = System.Diagnostics.Process.GetProcesses();
		for (System.Diagnostics.Process pro : processes)
		{
			String name = pro.ProcessName + ".exe";
			if (Objects.equals(name.toLowerCase(), processName.toLowerCase()))
			{
				pro.Kill();
			}
		}*/
		try {
			// 执行系统命令
			Process process = Runtime.getRuntime().exec("taskkill /F /IM " + processName);
			// 获取命令执行结果
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			// 等待命令执行完成
			int exitCode = process.waitFor();
			System.out.println("Exited with error code: " + exitCode);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

		///#endregion 执行安装.


		///#region 流程模版的ftp服务器配置.
	public static String getTemplateFTPHost()
	{
		return SystemConfig.GetValByKey("TemplateFTPHost", "116.239.32.14");
	}
	public static int getTemplateFTPPort()
	{
		return SystemConfig.GetValByKeyInt("TemplateFTPPort", 9997);
	}
	public static String getTemplateFTPUser()
	{
		return SystemConfig.GetValByKey("TemplateFTPUser", "oa");
	}
	public static String getTemplateFTPPassword()
	{
		return SystemConfig.GetValByKey("TemplateFTPPassword", "Jszx1234");
	}

		///#endregion 流程模版的ftp服务器配置.


		///#region 全局的方法处理
	/** 
	 流程数据表系统字段,中间用,分开.
	*/
	public static String getFlowFields()
	{
		String str = "";
		str += GERptAttr.OID + ",";
		str += GERptAttr.AtPara + ",";
		str += GERptAttr.BillNo + ",";
		//  str += GERptAttr.CFlowNo + ",";
		//  str += GERptAttr.CWorkID + ",";
		str += GERptAttr.FID + ",";
		str += GERptAttr.FK_Dept + ",";
		str += GERptAttr.FK_DeptName + ",";
		str += GERptAttr.FK_NY + ",";
		str += GERptAttr.FlowDaySpan + ",";
		str += GERptAttr.FlowEmps + ",";
		str += GERptAttr.FlowEnder + ",";
		str += GERptAttr.FlowEnderRDT + ",";
		str += GERptAttr.FlowEndNode + ",";
		str += GERptAttr.FlowStarter + ",";
		str += GERptAttr.FlowStartRDT + ",";
		str += GERptAttr.GuestName + ",";
		str += GERptAttr.GuestNo + ",";
		str += GERptAttr.GUID + ",";
		str += GERptAttr.PEmp + ",";
		str += GERptAttr.PFID + ",";
		str += GERptAttr.PFlowNo + ",";
		str += GERptAttr.PNodeID + ",";
		str += GERptAttr.PrjName + ",";
		str += GERptAttr.PrjNo + ",";
		str += GERptAttr.PWorkID + ",";
		str += GERptAttr.Title + ",";
		str += GERptAttr.WFSta + ",";
		str += GERptAttr.WFState + ",";
		str += "Rec,";
		str += "CDT,RDT,WFStateText";
		return str;
	}

		///#endregion 全局的方法处理


		///#region 与流程事件实体相关.
	private static Hashtable Htable_FlowFEE = null;
	/** 
	 获得节点事件实体
	 
	 @param enName 实例名称
	 @return 获得节点事件实体,如果没有就返回为空.
	*/
	public static FlowEventBase GetFlowEventEntityByEnName(String enName)
	{
		if (Htable_FlowFEE == null || Htable_FlowFEE.isEmpty())
		{
			Htable_FlowFEE = new Hashtable();

			String name = "BP.WF.FlowEventBase";

			ArrayList<FlowEventBase> al = bp.en.ClassFactory.GetObjects("bp.wf.FlowEventBase");
			for (FlowEventBase en : al)
			{
				if (Htable_FlowFEE.containsKey(en.toString()) == true)
				{
					continue;
				}
				Htable_FlowFEE.put(en.toString(), en);
			}
		}
		FlowEventBase myen = Htable_FlowFEE.get(enName) instanceof FlowEventBase ? (FlowEventBase)Htable_FlowFEE.get(enName) : null;
		if (myen == null)
		{
			//throw new Exception("@根据类名称获取流程事件实体实例出现错误:" + enName + ",没有找到该类的实体.");
			Log.DebugWriteError("@根据类名称获取流程事件实体实例出现错误:" + enName + ",没有找到该类的实体.");
			return null;
		}
		return myen;
	}
	/** 
	 获得事件实体String，根据编号或者流程标记
	 
	 @param flowMark 流程标记
	 @return null, 或者流程实体.
	*/
	public static String GetFlowEventEntityStringByFlowMark(String flowMark)
	{
		FlowEventBase en = GetFlowEventEntityByFlowMark(flowMark);
		if (en == null)
		{
			return "";
		}
		return en.toString();
	}
	/** 
	 获得事件实体，根据编号或者流程标记.
	 
	 @param flowMark 流程标记
	 @return null, 或者流程实体.
	*/
	public static FlowEventBase GetFlowEventEntityByFlowMark(String flowMark)
	{

		if (Htable_FlowFEE == null || Htable_FlowFEE.isEmpty())
		{
			Htable_FlowFEE = new Hashtable();

			String name = "";
			name = "BP.WF.FlowEventBase";

			ArrayList<FlowEventBase> al = bp.en.ClassFactory.GetObjects("bp.wf.FlowEventBase");
			Htable_FlowFEE.clear();
			for (FlowEventBase en : al)
			{
				if (Htable_FlowFEE.containsKey(en.toString()) == true)
				{
					continue;
				}
				Htable_FlowFEE.put(en.toString(), en);
			}
		}

		for (Object key : Htable_FlowFEE.keySet())
		{
			FlowEventBase fee = Htable_FlowFEE.get(key) instanceof FlowEventBase ? (FlowEventBase)Htable_FlowFEE.get(key) : null;

			String mark = "," + fee.getFlowMark() + ",";
			if (mark.contains("," + flowMark + ",") == true)
			{
				return fee;
			}
		}
		return null;
	}

		///#endregion 与流程事件实体相关.


		///#region web.config 属性.
	public static boolean isEnableTrackRec()
	{
		String s = SystemConfig.GetValByKey("IsEnableTrackRec", "");
		if (DataType.IsNullOrEmpty(s))
		{
			return false;
		}
		if (s.equals("0")) {
			return false;
		}

		return true;
	}
	public static String MapDataLikeKey(String flowNo, String colName)
	{
		flowNo = String.valueOf(Integer.parseInt(flowNo));
		String len = SystemConfig.getAppCenterDBLengthStr();

		//edited by liuxc,2016-02-22,合并逻辑，原来分流程编号的位数，现在统一处理
		return " (" + colName + " LIKE 'ND" + flowNo + "%' AND " + len + "(" + colName + ")=" + ("ND".length() + flowNo.length() + 2) + ") OR (" + colName + " = 'ND" + flowNo + "Rpt' ) OR (" + colName + " LIKE 'ND" + flowNo + "__Dtl%' AND " + len + "(" + colName + ")>" + ("ND".length() + flowNo.length() + 2 + "Dtl".length()) + ")";
	}


		///#endregion webconfig属性.


		///#region 常用方法
	/** 
	 加入track
	 
	 @param at 事件类型
	 @param flowNo 流程编号
	 @param workID 工作ID
	 @param fid 流程ID
	 @param fromNodeID 从节点编号
	 @param fromNodeName 从节点名称
	 @param fromEmpID 从人员ID
	 @param fromEmpName 从人员名称
	 @param toNodeID 到节点编号
	 @param toNodeName 到节点名称
	 @param toEmpID 到人员ID
	 @param toEmpName 到人员名称
	 @param note 消息
	 @param tag 参数用@分开
	*/
	public static String AddToTrack(ActionType at, String flowNo, long workID, long fid, int fromNodeID, String fromNodeName, String fromEmpID, String fromEmpName, int toNodeID, String toNodeName, String toEmpID, String toEmpName, String note, String tag) throws Exception {
		if (toNodeID == 0)
		{
			toNodeID = fromNodeID;
			toNodeName = fromNodeName;
		}

		Track t = new Track();
		t.setWorkID(workID);
		t.setFID(fid);
		t.setRDT(DataType.getCurrentDateTimess());
		t.setHisActionType(at);

		t.setNDFrom(fromNodeID);
		t.setNDFromT(fromNodeName);

		t.setEmpFrom(fromEmpID);
		t.setEmpFromT(fromEmpName);

		t.setNDTo(toNodeID);
		t.setNDToT(toNodeName);
		t.FlowNo = flowNo;

		String[] empNos = toEmpID.split("[,]", -1);
		if (empNos.length <= 100)
		{
			t.setEmpTo(toEmpID);
			t.setEmpToT(toEmpName);
		}
		else
		{
			String[] empNames = toEmpName.split("[、]", -1);
			String[] takes = Arrays.copyOfRange(empNos, 0, Math.min(100, empNos.length));
			//获取
			t.setEmpTo(StringHelper.join(",", takes) + "..." + empNos[empNos.length - 1]);
			t.setEmpToT(StringHelper.join("'、", takes) + "..." + empNames[empNames.length - 1]);
		}

		t.setMsg(note);
		t.setNodeData("@DeptNo=" + WebUser.getDeptNo() + "@DeptName=" + WebUser.getDeptName());
		//参数.
		if (tag != null)
		{
			t.setTag(tag);
		}

		try
		{
			t.Insert();
		}
		catch (java.lang.Exception e)
		{
			t.CheckPhysicsTable();
			t.Insert();
		}
		return t.getMyPK();
	}

	/** 
	 SQL表达式是否正确
	 
	 @param sqlExp
	 @param ht
	 @return 
	*/
	public static boolean CondExpSQL(String sqlExp, Hashtable ht, long myWorkID) throws Exception {
		String sql = sqlExp;
		sql = sql.replace("~", "'");
		sql = sql.replace("@WebUser.No", WebUser.getNo());
		sql = sql.replace("@WebUser.Name", WebUser.getName());
		sql = sql.replace("@WebUser.FK_Dept", WebUser.getDeptNo());

		for (Object key : ht.keySet())
		{
			if (Objects.equals(key, "OID"))
			{
				sql = sql.replace("@WorkID", ht.get("OID").toString());
				sql = sql.replace("@OID", ht.get("OID").toString());
				continue;
			}
			sql = sql.replace("@" + key, ht.get(key).toString());
		}

		//从工作流参数里面替换
		if (sql.contains("@") == true && myWorkID != 0)
		{
			GenerWorkFlow gwf = new GenerWorkFlow(myWorkID);
			AtPara ap = gwf.getatPara();
			for (String str : ap.getHisHT().keySet())
			{
				sql = sql.replace("@" + str, ap.GetValStrByKey(str));
			}
		}

		int result = DBAccess.RunSQLReturnValInt(sql, -1);
		if (result <= 0)
		{
			return false;
		}
		return true;
	}
	/** 
	 判断表达式是否成立
	 
	 @param exp 表达式
	 @param ht 变量
	 @return 是否成立
	*/
	public static boolean CondExpPara(String exp, Hashtable ht, long myWorkID)
	{
		try
		{
			String[] strs = exp.trim().split("[ ]", -1);

			String key = strs[0].trim();
			String oper = strs[1].trim();
			String val = strs[2].trim();

			val = val.replace("'", "");
			val = val.replace("%", "");
			val = val.replace("~", "");

			String valPara = null;
			if (ht.containsKey(key) == false)
			{

				boolean isHave = false;
				if (myWorkID != 0)
				{
					//把外部传来的参数传入到 rptGE 让其做方向条件的判断.
					GenerWorkFlow gwf = new GenerWorkFlow(myWorkID);
					AtPara at = gwf.getatPara();
					for (String str : at.getHisHT().keySet())
					{
						if (key.equals(str) == false)
						{
							continue;
						}

						valPara = at.GetValStrByKey(key);
						isHave = true;
						break;
					}
				}

				if (isHave == false)
				{
					try
					{
						if (SystemConfig.isBSsystem() == true && CommonUtils.getRequest().getParameterMap().keySet().contains(key) == true)
						{
							valPara = ContextHolderUtils.getRequest().getParameter(key);
						}
						else
						{
							throw new RuntimeException("@判断条件时错误,请确认参数是否拼写错误,没有找到对应的表达式:" + exp + " Key=(" + key + ") oper=(" + oper + ")Val=(" + val + ")");
						}
					}
					catch (java.lang.Exception e)
					{
						//有可能是常量. 
						valPara = key;
					}
				}
			}
			else
			{
				valPara = ht.get(key).toString().trim();
			}


				///#region 开始执行判断.
			if (Objects.equals(oper, "="))
			{
				if (Objects.equals(valPara, val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}

			if (Objects.equals(oper.toUpperCase(), "LIKE"))
			{
				if (valPara.contains(val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			if (Objects.equals(oper, "!="))
			{
				if (!Objects.equals(valPara, val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}


			if (DataType.IsNumStr(valPara) == false)
			{
				throw new RuntimeException("err@表达式错误:[" + exp + "]没有找到参数[" + valPara + "]的值，导致无法计算。");
			}

			if (Objects.equals(oper, ">"))
			{
				if (Float.parseFloat(valPara) > Float.parseFloat(val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			if (Objects.equals(oper, ">="))
			{
				if (Float.parseFloat(valPara) >= Float.parseFloat(val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			if (Objects.equals(oper, "<"))
			{
				if (Float.parseFloat(valPara) < Float.parseFloat(val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			if (Objects.equals(oper, "<="))
			{
				if (Float.parseFloat(valPara) <= Float.parseFloat(val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}

			if (Objects.equals(oper, "!="))
			{
				if (Float.parseFloat(valPara) != Float.parseFloat(val))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			throw new RuntimeException("@参数格式错误:" + exp + " Key=" + key + " oper=" + oper + " Val=" + val);

				///#endregion 开始执行判断.

		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("计算参数的时候出现错误:" + ex.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/** 
	 表达式替换
	 
	 @param exp
	 @param en
	 @return 
	*/

	public static String DealExp(String exp, Entity en) throws Exception {
		return DealExp(exp, en, "");
	}

	public static String DealExp(String exp, Entity en, String errInfo)  {
		//替换字符
		exp = exp.replace("~", "'");

		if (exp.contains("@") == false)
		{
			return exp;
		}

		//首先替换加; 的。
		exp = exp.replace("@WebUser.getNo();", WebUser.getNo());
		exp = exp.replace("@WebUser.Name;", WebUser.getName());
		exp = exp.replace("@WebUser.FK_DeptName;", WebUser.getDeptName());
		exp = exp.replace("@WebUser.FK_Dept;", WebUser.getDeptNo());

		// 替换没有 ; 的 .
		exp = exp.replace("@WebUser.No", WebUser.getNo());
		exp = exp.replace("@WebUser.Name", WebUser.getName());
		exp = exp.replace("@WebUser.FK_DeptName", WebUser.getDeptName());
		exp = exp.replace("@WebUser.FK_Dept", WebUser.getDeptNo());
		exp = exp.replace("@WebUser.OrgNo", WebUser.getOrgNo());

		exp = exp.replace("@RDT", DataType.getCurrentDateTime());

		if (exp.contains("@") == false)
		{
			return exp;
		}

		//增加对新规则的支持. @MyField; 格式.
		if (en != null)
		{
			Attrs attrs = en.getEnMap().getAttrs();
			Row row = en.getRow();
			//特殊判断.
			if (row.containsKey("OID") == true)
			{
				exp = exp.replace("@WorkID", row.get("OID").toString());
			}

			if (exp.contains("@") == false)
			{
				return exp;
			}


			boolean isHaveFenHao = exp.contains(";");


			for (String key : row.keySet())
			{
				//值为空或者null不替换
				if (row.get(key) == null)
				{
					continue;
				}
				if (exp.contains("@" + key + ";"))
				{
					//先替换有单引号的.
					exp = exp.replace("'@" + key + ";'", "'" + row.get(key).toString() + "'");
					//在更新没有单引号的.
					exp = exp.replace("@" + key + ";", row.get(key).toString());
				}
				if (exp.contains("@" + key))
				{
					//先替换有单引号的.
					exp = exp.replace("'@" + key + "'", "'" + row.get(key).toString() + "'");
					//在更新没有单引号的.
					exp = exp.replace("@" + key, row.get(key).toString());
				}
				//不包含@则返回SQL语句
				if (exp.contains("@") == false)
				{
					return exp;
				}
			}
		}

		if (exp.contains("@") && SystemConfig.isBSsystem() == true)
		{
			/*如果是bs*/
			for (String key : ContextHolderUtils.getRequest().getParameterMap().keySet())
			{
				if (DataType.IsNullOrEmpty(key))
				{
					continue;
				}
				exp = exp.replace("@" + key, ContextHolderUtils.getRequest().getParameter(key));
			}

		}

		exp = exp.replace("~", "'");
		return exp;
	}
	//
	/** 
	 处理表达式
	 
	 @param exp 表达式
	 @param en 数据源
	 @param errInfo 错误
	 @return 
	*/
	public static String DealSQLExp(String exp, Entity en, String errInfo) throws Exception {
		//替换字符
		exp = exp.replace("~", "'");

		//替换我们只处理WHERE 后面的内容
		//需要判断SQL 中含有几个WHERE字符
		String regex = "\\bWHERE\\b";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(exp.toUpperCase());
		int count = 0;
		while (matcher.find()) {
			count++;
		}
		int num = count;
		if (num == 0)
		{
			return exp;
		}
		//我们暂时处理含有一个WHERE的情况
		String expFrom = "";
		if (num == 1)
		{
			expFrom = exp.substring(0, exp.toUpperCase().indexOf("WHERE"));
			exp = exp.substring(expFrom.length());
		}

		String expWhere = "";

		if (exp.contains("@") == false)
		{
			return expFrom + exp;
		}

		//首先替换加; 的。
		exp = exp.replace("@WebUser.getNo();", WebUser.getNo());
		exp = exp.replace("@WebUser.Name;", WebUser.getName());
		exp = exp.replace("@WebUser.FK_DeptName;", WebUser.getDeptName());
		exp = exp.replace("@WebUser.FK_Dept;", WebUser.getDeptNo());


		// 替换没有 ; 的 .
		exp = exp.replace("@WebUser.No", WebUser.getNo());
		exp = exp.replace("@WebUser.Name", WebUser.getName());
		exp = exp.replace("@WebUser.FK_DeptName", WebUser.getDeptName());
		exp = exp.replace("@WebUser.FK_Dept", WebUser.getDeptNo());

		if (bp.wf.Glo.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			exp = exp.replace("@WebUser.OrgNo", WebUser.getOrgNo());
		}

		if (exp.contains("@") == false)
		{
			return expFrom + exp;
		}

		//增加对新规则的支持. @MyField; 格式.
		if (en != null)
		{
			Row row = en.getRow();
			//特殊判断.
			if (row.containsKey("OID") == true)
			{
				exp = exp.replace("@WorkID", row.get("OID").toString());
			}

			if (exp.contains("@") == false)
			{
				return expFrom + exp;
			}

			for (String key : row.keySet())
			{
				//值为空或者null不替换
				if (row.get(key) == null || row.get(key).equals("") == true)
				{
					continue;
				}

				if (exp.contains("@" + key + ";"))
				{
					exp = exp.replace("@" + key + ";", row.get(key).toString());
				}

				//不包含@则返回SQL语句
				if (exp.contains("@") == false)
				{
					return expFrom + exp;
				}
			}



				///#region 解决排序问题.
			Attrs attrs = en.getEnMap().getAttrs();
			String mystrs = "";
			for (Attr attr : attrs)
			{
				if (attr.getMyDataType() == DataType.AppString)
				{
					mystrs += "@" + attr.getKey() + ",";
				}
				else
				{
					mystrs += "@" + attr.getKey();
				}
			}
			String[] strs = mystrs.split("[@]", -1);
			DataTable dt = new DataTable();
			dt.Columns.Add(new DataColumn("No", String.class));
			for (String str : strs)
			{
				if (DataType.IsNullOrEmpty(str))
				{
					continue;
				}

				DataRow dr = dt.NewRow();
				dr.setValue(0, str);
				dt.Rows.add(dr);
			}

				///#endregion  解决排序问题.


				///#region 替换变量.
			for (DataRow dr : dt.Rows)
			{
				String key = dr.getValue(0).toString();
				boolean isStr = key.contains(",");
				if (isStr == true)
				{
					key = key.replace(",", "");
				}

				if (DataType.IsNullOrEmpty(en.GetValStrByKey(key)))
				{
					continue;
				}

				exp = exp.replace("@" + key, en.GetValStrByKey(key));
			}

				///#endregion

			if (exp.contains("@") == false)
			{
				return expFrom + exp;
			}
		}

		if (exp.contains("@") && SystemConfig.isBSsystem() == true)
		{
			/*如果是bs*/
			for (String key : ContextHolderUtils.getRequest().getParameterMap().keySet())
			{
				if (DataType.IsNullOrEmpty(key))
				{
					continue;
				}
				exp = exp.replace("@" + key, ContextHolderUtils.getRequest().getParameter(key));
			}
		}
		exp = exp.replace("~", "'");
		exp = exp.replace("\r", "");
		exp = exp.replace("\n", "");
		return expFrom + exp;
	}
	/** 
	 加密MD5
	 
	 @param wk
	 @return 
	*/
	public static String GenerMD5(bp.wf.Work wk) throws Exception {
		String s = null;
		for (Attr attr : wk.getEnMap().getAttrs())
		{
			switch (attr.getKey())
			{
				case WorkAttr.MD5:
				case WorkAttr.Rec:
				case GERptAttr.Title:
				// case GERptAttr.Emps:
				case GERptAttr.FK_Dept:
				//case GERptAttr.PRI:
				case GERptAttr.FID:
					continue;
				default:
					break;
			}

			Object tempVar = attr.getDefaultVal();
			String obj = tempVar instanceof String ? (String)tempVar : null;
			//if (obj == null)
			//    continue;
			if (obj != null && obj.contains("@"))
			{
				continue;
			}

			s += wk.GetValStrByKey(attr.getKey());
		}
		s += "ccflow";

		return GetMD5Hash(s);
	}
	/** 
	 取得MD5加密串
	 
	 @param input 源明文字符串
	 @return 密文字符串
	*/
	public static String GetMD5Hash(String input)
	{
		/*System.Security.Cryptography.MD5CryptoServiceProvider md5 = new System.Security.Cryptography.MD5CryptoServiceProvider();
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] bs = System.Text.Encoding.UTF8.GetBytes(input);
		byte[] bs = input.getBytes(java.nio.charset.StandardCharsets.UTF_8);
		bs = md5.ComputeHash(bs);
		StringBuilder s = new StringBuilder();
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: foreach (byte b in bs)
		for (byte b : bs)
		{
			s.append(String.format("%1$.2x", b).toLowerCase());
		}
		return s.toString();*/
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
			BigInteger number = new BigInteger(1, hashBytes);
			String hashString = number.toString(16);
			while (hashString.length() < 32) {
				hashString = "0" + hashString;
			}
			return hashString;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

		///#endregion 常用方法


		///#region 其他配置.
	public static String getFlowFileBill()
	{
		return Glo.getIntallPath() + "DataUser/Bill/";
	}
	private static String _IntallPath = null;
	public static String getIntallPath()
	{
		if (_IntallPath == null)
		{
			if (SystemConfig.isBSsystem() == true)
			{
				_IntallPath = SystemConfig.getPathOfWebApp();
			}
		}

		if (_IntallPath == null)
		{
			throw new RuntimeException("@没有实现如何获得 cs 下的根目录.");
		}

		return _IntallPath;
	}
	public static void setIntallPath(String value)
	{
		_IntallPath = value;
	}
	/** 
	 语言
	*/
	public static String Language = "CH";
	/** 
	 是否启用共享任务池？
	*/
	public static boolean isEnableTaskPool()
	{
		return SystemConfig.GetValByKeyBoolen("IsEnableTaskPool", false);
	}
	/** 
	 用户信息显示格式
	*/
	public static UserInfoShowModel getUserInfoShowModel()
	{
		return UserInfoShowModel.forValue(SystemConfig.GetValByKeyInt("UserInfoShowModel", 0));
	}
	/** 
	 处理显示格式
	 
	 @param no
	 @param name
	 @return 现实格式
	*/
	public static String DealUserInfoShowModel(String no, String name)
	{
		switch (bp.wf.Glo.getUserInfoShowModel())
		{
			case UserIDOnly:
				return no;
			case UserIDUserName:
				// return "(" + no + "," + name + ")";
				return no + "," + name;
			case UserNameOnly:
				//return "(" + name + ")";
				return name;
			default:
				throw new RuntimeException("@没有判断的格式类型.");
		}
	}
	/** 
	 钉钉是否启用
	*/
	public static boolean isEnableDingDing()
	{
		//如果两个参数都不为空说明启用
		String corpid = SystemConfig.getDing_CorpID();
		String corpsecret = SystemConfig.getDing_CorpSecret();
		if (DataType.IsNullOrEmpty(corpid) || DataType.IsNullOrEmpty(corpsecret))
		{
			return false;
		}

		return true;
	}
	/** 
	 微信是否启用
	*/
	public static boolean isEnableWeiXin()
	{
		//如果两个参数都不为空说明启用
		String corpid = SystemConfig.getWX_CorpID();
		String corpsecret = SystemConfig.getWX_AppSecret();
		if (DataType.IsNullOrEmpty(corpid) || DataType.IsNullOrEmpty(corpsecret))
		{
			return false;
		}

		return true;
	}
	/** 
	 是否启用消息系统消息。
	*/
	public static boolean isEnableSysMessage()
	{
		return SystemConfig.GetValByKeyBoolen("IsEnableSysMessage", true);
	}
	/** 
	 主机
	*/
	public static String getHostURL()
	{
		if (SystemConfig.isBSsystem())
		{
			/* 如果是BS 就要求 路径.*/
		}

		String baseUrl = SystemConfig.getAppSettings().get("HostURL").toString();
		if (DataType.IsNullOrEmpty(baseUrl) == true)
		{
			baseUrl = "http://127.0.0.1/";
		}

		if (!Objects.equals(baseUrl.substring(baseUrl.length() - 1), "/"))
		{
			baseUrl = baseUrl + "/";
		}
		return baseUrl;
	}

		///#endregion


		///#region 时间计算.
	/** 
	 设置成工作时间
	 
	 @param dt
	 @return 
	*/
	public static Date SetToWorkTime(Date dt) throws Exception {
		if (bp.sys.GloVar.getHolidays().contains(DateUtils.format(dt, "MM-dd")))
		{
			dt = DateUtils.addDay(dt, 1);
			/*如果当前是节假日，就要从下一个有效期计算。*/
			while (true) {
				if (bp.sys.GloVar.getHolidays().contains(DateUtils.format(dt, "MM-dd")) == false) {
					{
						break;
					}
				}
				// 从下一个上班时间计算.
				dt = DataType.ParseSysDate2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getAMFrom());
				return dt;
			}
		}

		int timeInt = Integer.parseInt(DateUtils.format(dt, "HHmm"));

		//判断是否在A区间, 如果是，就返回A区间的时间点.
		if (Glo.getAMFromInt() >= timeInt) {
			return DataType.ParseSysDate2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getPMFrom());
		}

		// 判断是否在E区间, 如果是就返回第2天的上班时间点.
		if (Glo.getPMToInt() <= timeInt) {
			return DataType.ParseSysDate2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getPMTo());
		}

		// 如果在午休时间点中间.
		if (Glo.getAMToInt() <= timeInt && Glo.getPMFromInt() > timeInt) {
			return DataType.ParseSysDate2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getPMFrom());
		}
		return dt;
	}
	/** 
	 在指定的日期上增加小时数。
	 1，扣除午休。
	 2，扣除节假日。
	 
	 @param dt
	 @param hh
	 @param minutes
	 @return
	*/
	private static Date AddMinutes (Date dt,int hh, int minutes) throws Exception {
		if (1 == 1) {
			Calendar c = Calendar.getInstance();
			c.setTime(dt);
			c.add(Calendar.HOUR, hh);
			c.add(Calendar.MINUTE, minutes);
			return c.getTime();
		}

		//如果没有设置,就返回.
		if (minutes == 0 && hh == 0) {
			return dt;
		}

		//设置成工作时间.
		dt = SetToWorkTime(dt);

		//首先判断是否是在一天整的时间完成.
		if (minutes == Glo.getAMPMHours() * 60) {
			/*如果需要在一天完成*/
			dt = DataType.AddDays(dt, 1, TWay.Holiday);
			return dt;
		}

		//判断是否是AM.
		boolean isAM = false;
		int timeInt = Integer.parseInt(DateUtils.format(dt, "HHmm"));
		if (Glo.getAMToInt() > timeInt) {
			isAM = true;
		}


		///#region 如果是当天的情况.
		//如果规定的时间在 1天之内.
		if (minutes / 60 / Glo.getAMPMHours() < 1) {
			if (isAM == true) {
				/* 如果是中午, 中午到中午休息之间的时间. */

				long ts = DataType.ParseSysDateTime2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getAMTo())
						.getTime() - dt.getTime();
				if (ts / (60 * 1000) >= minutes) {
					/* 如果剩余的分钟大于 要增加的分钟数，就是说+上分钟后，仍然在中午，就直接增加上这个分钟，让其返回。 */
					return DateUtils.addMinutes(dt, minutes);
				} else {
					// 求出到下班时间的分钟数。
					long myts = DataType
							.ParseSysDateTime2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getAMTo())
							.getTime() - dt.getTime();

					// 扣除午休的时间.
					int leftMuit = (int) (myts / (60 * 1000) - Glo.getAMPMTimeSpan() * 60);
					if (leftMuit - minutes >= 0) {
						/* 说明还是在当天的时间内. */
						java.util.Date mydt = DataType
								.ParseSysDateTime2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getPMTo());
						return DateUtils.addMinutes(mydt, (minutes - leftMuit));
					}

					// 说明要跨到第2天上去了.
					dt = DataType.AddDays(dt, 1, TWay.Holiday);
					// return Glo.AddMinutes(DateUtils.format(dt,"yyyy-MM-dd") +
					// " " + Glo.getAMFrom(), minutes - leftMuit);
				}

				// 把当前的时间加上去.
				dt = DateUtils.addMinutes(dt, minutes);

				// 判断是否是中午.
				boolean isInAM = false;
				timeInt = Integer.parseInt(DateUtils.format(dt, "HHmm"));
				if (Glo.getAMToInt() >= timeInt) {
					isInAM = true;
				}

				if (isInAM == true) {
					// 加上时间后仍然是中午就返回.
					return dt;
				}

				// 延迟一个午休时间.
				dt = DateUtils.addHours(dt, (int) Glo.getAMPMTimeSpan());

				// 判断时间点是否落入了E区间.
				timeInt = Integer.parseInt(DateUtils.format(dt, "HHmm"));
				if (Glo.getPMToInt() <= timeInt) {
					/* 如果落入了E区间. */

					// 求出来时间点到，下班之间的分钟数.
					long tsE = dt.getTime() - DataType
							.ParseSysDate2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getPMTo()).getTime();

					// 从次日的上班时间计算+ 这个时间差.
					dt = DataType.ParseSysDate2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getPMTo());
					return DateUtils.addMinutes(dt, (int) tsE / (60 * 1000));
				} else {
					/* 过了第2天的情况很少，就不考虑了. */
					return dt;
				}
			} else {
				// 如果是下午, 计算出来到下午下班还需多少分钟，与增加的分钟数据相比较.
				long ts = DataType.ParseSysDateTime2DateTime(DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getPMTo())
						.getTime() - dt.getTime();
				if (ts / (60 * 1000) >= minutes) {
					// 如果剩余的分钟大于 要增加的分钟数，就直接增加上这个分钟，让其返回。
					return DateUtils.addMinutes(dt, minutes);
				} else {

					// 剩余的分钟数 = 总分钟数 - 今天下午剩余的分钟数.
					int leftMin = minutes - (int) ts / (60 * 1000);

					// 否则要计算到第2天上去了， 计算时间要从下一个有效的工作日上班时间开始.
					dt = DataType
							.AddDays(
									DataType.ParseSysDateTime2DateTime(
											DateUtils.format(dt, "yyyy-MM-dd") + " " + Glo.getAMFrom()),
									1, TWay.Holiday);
					// 递归调用,让其在次日的上班时间在增加，分钟数。
					return Glo.AddMinutes(dt, 0, leftMin);
				}

			}
		}

		///#endregion 如果是当天的情况.

		return dt;
	}
	/** 
	 增加分钟数.
	 
	 @param sysdt
	 @param minutes
	 @return 
	*/
	public static Date AddMinutes (String sysdt,int minutes) throws Exception {
		Date dt = DataType.ParseSysDate2DateTime(sysdt);
		return AddMinutes(dt, 0, minutes);
	}
	/** 
	 在指定的日期上增加n天n小时，并考虑节假日
	 
	 @param specDT 指定的日期
	 @param day 天数
	 @param minutes 分钟数
	 @return 返回计算后的日期
	*/
	public static Date AddDayHoursSpan (String specDT,int day, int hh, int minutes, TWay tway) throws Exception {
		Date mydt = DataType.AddDays(specDT, day, tway);
		return Glo.AddMinutes(mydt, hh, minutes);
	}
	/** 
	 在指定的日期上增加n天n小时，并考虑节假日
	 
	 @param specDT 指定的日期
	 @param day 天数
	 @param minutes 分钟数
	 @return 返回计算后的日期
	*/
	public static Date AddDayHoursSpan (Date specDT,int day, int hh, int minutes, TWay tway) throws Exception {
		Date mydt = bp.da.DataType.AddDays(specDT, day, tway);
		mydt = AddMinutes(mydt, 0, minutes);
		return mydt;
	}

		///#endregion ssxxx.


		///#region 与考核相关.
	/** 
	 当流程发送下去以后，就开始执行考核。
	 
	 @param fl
	 @param nd
	 @param workid
	 @param fid
	 @param title
	*/

	public static void InitCH(Flow fl, Node nd, long workid, long fid, String title) throws Exception {
		InitCH(fl, nd, workid, fid, title, null);
	}

	public static void InitCH(Flow fl, Node nd, long workid, long fid, String title, GenerWorkerList gwl) throws Exception {
		InitCH2017(fl, nd, workid, fid, title, null, null, new Date(), gwl);
	}
	/** 
	 执行考核
	 
	 @param fl 流程
	 @param nd 节点
	 @param workid 工作ID
	 @param fid FID
	 @param title 标题
	 @param prvRDT 上一个时间点
	 @param sdt 应完成日期
	 @param dtNow 当前日期
	*/
	private static void InitCH2017(Flow fl, Node nd, long workid, long fid, String title, String prvRDT, String sdt, Date dtNow, GenerWorkerList gwl) throws Exception {

		// 开始节点不考核.
		if (nd.getItIsStartNode() || nd.getHisCHWay() == CHWay.None)
		{
			return;
		}

		//如果设置为0,则不考核.
		if (nd.getTimeLimit() == 0 && nd.getTimeLimitHH() == 0 && nd.getTimeLimitMM() == 0)
		{
			return;
		}

		if (dtNow == null)
		{
			dtNow = new Date();
		}


			///#region 求参与人员 todoEmps ，应完成日期 sdt ，与工作派发日期 prvRDT.
		//参与人员.
		String todoEmps = "";
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		if (nd.getItIsEndNode() == true && gwl == null)
		{
			/* 如果是最后一个节点，可以使用这样的方式来求人员信息 , */


				///#region 求应完成日期，与参与的人集合.
			Paras ps = new Paras();
			switch (SystemConfig.getAppCenterDBType())
			{
				case MSSQL:
					ps.SQL = "SELECT TOP 1 SDTOfNode, TodoEmps FROM WF_GenerWorkFlow  WHERE WorkID=" + dbstr + "WorkID ";
					break;
				case Oracle:
				case KingBaseR3:
				case KingBaseR6:
					ps.SQL = "SELECT SDTOfNode, TodoEmps FROM WF_GenerWorkFlow  WHERE WorkID=" + dbstr + "WorkID  ";
					break;
				case MySQL:
					ps.SQL = "SELECT SDTOfNode, TodoEmps FROM WF_GenerWorkFlow  WHERE WorkID=" + dbstr + "WorkID  ";
					break;
				case PostgreSQL:
				case UX:
				case HGDB:
					ps.SQL = "SELECT SDTOfNode, TodoEmps FROM WF_GenerWorkFlow  WHERE WorkID=" + dbstr + "WorkID  ";
					break;
				default:
					throw new RuntimeException("err@没有判断的数据库类型.");
			}

			ps.Add("WorkID", workid);
			DataTable dt = DBAccess.RunSQLReturnTable(ps);
			if (dt.Rows.size() == 0)
			{
				return;
			}
			sdt = dt.Rows.get(0).getValue("SDTOfNode").toString(); //应完成日期.
			todoEmps = dt.Rows.get(0).getValue("TodoEmps").toString(); //参与人员.

				///#endregion 求应完成日期，与参与的人集合.


				///#region 求上一个节点的日期.
			dt = Dev2Interface.Flow_GetPreviousNodeTrack(workid, nd.getNodeID());
			if (dt.Rows.size() == 0)
			{
				return;
			}
			//上一个节点的活动日期.
			prvRDT = dt.Rows.get(0).getValue("RDT").toString();

				///#endregion
		}


		if (nd.getItIsEndNode() == false)
		{
			if (gwl == null)
			{
				gwl = new GenerWorkerList();
				gwl.Retrieve(GenerWorkerListAttr.WorkID, workid, GenerWorkerListAttr.FK_Node, nd.getNodeID(), GenerWorkerListAttr.FK_Emp, WebUser.getNo());
			}

			prvRDT = gwl.getRDT(); // dt.Rows.get(0).getValue("RDT").toString(); //上一个时间点的记录日期.
			sdt = gwl.getSDT(); //  dt.Rows.get(0).getValue("SDT").toString(); //应完成日期.
			todoEmps = WebUser.getNo() + "," + WebUser.getName() + ";";
		}

			///#endregion 求参与人员，应完成日期，与工作派发日期.


			///#region 求 preSender上一个发送人，preSenderText 发送人姓名
		String preSender = "";
		String preSenderText = "";
		DataTable dt_Sender = Dev2Interface.Flow_GetPreviousNodeTrack(workid, nd.getNodeID());
		if (dt_Sender.Rows.size() > 0)
		{
			preSender = dt_Sender.Rows.get(0).getValue("EmpFrom").toString();
			preSenderText = dt_Sender.Rows.get(0).getValue("EmpFromT").toString();
		}

			///#endregion


			///#region 初始化基础数据.
		CH ch = new CH();
		ch.setWorkID(workid);
		ch.setFID(fid);
		ch.setTitle(title);

		//记录当时设定的值.
		ch.setTimeLimit(nd.getTimeLimit());

		ch.setFK_NY(DateUtils.format(dtNow, "yyyy-MM"));

		ch.setDTFrom(prvRDT); //任务下达时间.
		ch.setDTTo(DateUtils.format(dtNow,"yyyy-MM-dd HH:mm:ss")); //时间到.

		ch.setSDT(sdt); //应该完成时间.

		ch.setFK_Flow(nd.getFlowNo()); //流程信息.
		ch.setFK_FlowT(nd.getFlowName());

		ch.setNodeID(nd.getNodeID()); //节点.
		ch.setFK_NodeT(nd.getName());

		ch.setFK_Dept(WebUser.getDeptNo()); //部门.
		ch.setFK_DeptT(WebUser.getDeptName());

		ch.setFK_Emp(WebUser.getNo()); //当事人.
		ch.setFK_EmpT(WebUser.getName());

		// 处理相关联的当事人.
		ch.setGroupEmpsNames(todoEmps);
		//上一步发送人
		ch.setSender(preSender);
		ch.setSenderT(preSenderText);
		//考核状态
		ch.setDTSWay(nd.getHisCHWay().getValue());

		//求参与人员数量.
		String[] strs = todoEmps.split("[;]", -1);
		ch.setGroupEmpsNum(strs.length - 1); //个数.

		//求参与人的ids.
		String empids = ",";
		for (String str : strs)
		{
			if (DataType.IsNullOrEmpty(str))
			{
				continue;
			}

			String[] mystr = str.split("[,]", -1);
			empids += mystr[0] + ",";
		}
		ch.setGroupEmps(empids);

		// mypk.
		ch.setMyPK(nd.getNodeID()+ "_" + workid + "_" + fid + "_" + WebUser.getNo());

			///#endregion 初始化基础数据.


			///#region 求计算属性.
		//求出是第几个周.
		ch.setWeekNum(DataType.WeekOfYear(dtNow));

		// UseDays . 求出实际使用天数.
		Date dtFrom = DataType.ParseSysDate2DateTime(ch.getDTFrom());
		Date dtTo = DataType.ParseSysDate2DateTime(ch.getDTTo());

		long ts = dtTo.getTime() - dtFrom.getTime();
		ch.setUseDays(ts / 1000 / 60 / 60 / 24); // 用时，天数
		ch.setUseMinutes(ts / 1000 / 60); // 用时，分钟
		//int hour = ts.Hours;
		//ch.UseDays += ts.Hours / 8; //使用的天数.
		if (DataType.IsNullOrEmpty(ch.getSDT()) == false && ch.getSDT().equals("无") == false)
		{
			// OverDays . 求出 逾期天 数.
			Date sdtOfDT = DataType.ParseSysDate2DateTime(ch.getSDT());

			long myts = dtTo.getTime() - sdtOfDT.getTime();
			ch.setOverDays(myts / 1000 / 60 / 60 / 24); // 逾期的天数.
			ch.setOverMinutes(myts / 1000 / 60); // 逾期的分钟数
			if (sdtOfDT.compareTo(dtTo) >= 0)
			{
				/* 正常完成 */
				ch.setCHSta(CHSta.AnQi); //按期完成.
				ch.setPoints(0);
			}
			else
			{
				/*逾期完成.*/
				ch.setCHSta(CHSta.YuQi); //逾期完成.
				float sum = ch.getOverDays() * nd.getTCent();
				ch.setPoints((float) (Math.round(sum * 100)) / 100);
			}
		}
		else
		{
			/* 正常完成 */
			ch.setCHSta(CHSta.AnQi); //按期完成.
			ch.setPoints(0);
		}


			///#endregion 求计算属性.
		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			ch.SetValByKey(CHAttr.OrgNo, WebUser.getOrgNo());
		}
		//执行保存.
		try
		{
			ch.DirectInsert();
		}
		catch (java.lang.Exception e)
		{
			if (ch.getIsExits() == true)
			{
				ch.Update();
			}
			else
			{
				//如果遇到退回的情况就可能涉及到主键重复的问题.
				ch.setMyPK(DBAccess.GenerGUID(0, null, null));
				ch.Insert();
			}
		}
	}
	/** 
	 中午时间从
	*/
	public static String getAMFrom()
	{
		return SystemConfig.GetValByKey("AMFrom", "08:30");
	}
	/** 
	 中午时间从
	*/
	public static int getAMFromInt()
	{
		return Integer.parseInt(Glo.getAMFrom().replace(":", ""));
	}
	/** 
	 一天有效的工作小时数
	 是中午工作小时+下午工作小时.
	*/
	public static float getAMPMHours()
	{
		return SystemConfig.GetValByKeyFloat("AMPMHours", 8);
	}
	/** 
	 中午间隔的小时数
	*/
	public static float getAMPMTimeSpan()
	{
		return SystemConfig.GetValByKeyFloat("AMPMTimeSpan", 1);
	}
	/** 
	 中午时间到
	*/
	public static String getAMTo()
	{
		return SystemConfig.GetValByKey("AMTo", "11:30");
	}
	/** 
	 中午时间到
	*/
	public static int getAMToInt()
	{
		return Integer.parseInt(Glo.getAMTo().replace(":", ""));
	}
	/** 
	 下午时间从
	*/
	public static String getPMFrom()
	{
		return SystemConfig.GetValByKey("PMFrom", "13:30");
	}
	/** 
	 到
	*/
	public static int getPMFromInt()
	{
		return Integer.parseInt(Glo.getPMFrom().replace(":", ""));
	}
	/** 
	 到
	*/
	public static String getPMTo()
	{
		return SystemConfig.GetValByKey("PMTo", "17:30");
	}
	/** 
	 到
	*/
	public static int getPMToInt()
	{
		return Integer.parseInt(Glo.getPMTo().replace(":", ""));
	}

		///#endregion 与考核相关.


		///#region 其他方法。

	/** 
	 删除临时文件
	*/
	public static void DeleteTempFiles()
	{
		try
		{
			//删除目录.
			String temp = SystemConfig.getPathOfTemp();
			FileAccess.deletesFile(new File(temp));

			//创建目录.
			(new File(temp)).mkdirs();

			//删除pdf 目录.
			temp = SystemConfig.getPathOfDataUser() + "InstancePacketOfData/";
			File info = new File(temp);
			File[] dirs = info.listFiles();
			for (File dir : dirs)
			{
				if (dir.getName().indexOf("ND") == 0)
				{
					dir.delete();
				}
			}
		}
		catch (RuntimeException ex)
		{

		}
	}

	/** 
	 复制表单权限-从一个节点到另一个节点.
	 
	 @param fk_flow 流程编号
	 @param frmID 表单ID
	 @param currNodeID 当前节点
	 @param fromNodeID 从节点
	*/
	public static void CopyFrmSlnFromNodeToNode(String fk_flow, String frmID, int currNodeID, int fromNodeID) throws Exception {

			///#region 处理字段.
		//删除现有的.
		FrmFields frms = new FrmFields();
		frms.Delete(FrmFieldAttr.FK_Node, currNodeID, FrmFieldAttr.FrmID, frmID);

		//查询出来,指定的权限方案.
		frms.Retrieve(FrmFieldAttr.FK_Node, fromNodeID, FrmFieldAttr.FrmID, frmID, null);

		//开始复制.
		for (FrmField item : frms.ToJavaList())
		{
			item.setMyPK(frmID + "_" + fk_flow + "_" + currNodeID + "_" + item.getKeyOfEn());
			item.setNodeID(currNodeID);
			item.Insert(); // 插入数据库.
		}

			///#endregion 处理字段.

		//没有考虑到附件的权限 20161020 hzm

			///#region 附件权限

		FrmAttachments fas = new FrmAttachments();
		//删除现有节点的附件权限
		fas.Delete(FrmAttachmentAttr.FK_Node, currNodeID, FrmAttachmentAttr.FK_MapData, frmID);
		//查询出 现在表单上是否有附件的情况
		fas.Retrieve(FrmAttachmentAttr.FK_Node, fromNodeID, FrmAttachmentAttr.FK_MapData, frmID, null);

		//复制权限
		for (FrmAttachment fa : fas.ToJavaList())
		{
			fa.setMyPK(fa.getFrmID() + "_" + fa.getNoOfObj() + "_" + currNodeID);
			fa.setNodeID(currNodeID);
			fa.Insert();
		}

	}

	private static final String StrRegex = "-|;|,|/|(|)|[|]|}|{|%|@|*|!|'|`|~|#|$|^|&|.|?";
	private static final String StrKeyWord = "select|insert|delete|from|count(|drop table|update|truncate|asc(|mid(|char(|xp_cmdshell|exec master|netlocalgroup administrators|:|net user|\"|or|and";
	/** 
	 检查KeyWord是否包涵特殊字符
	 
	 @param KeyWord 需要检查的字符串
	 @return 
	*/
	public static String CheckKeyWord(String KeyWord)
	{
		//特殊符号
		String[] strRegx = StrRegex.split("|");
		//特殊符号 的注入情况
		for (String key : strRegx)
		{
			if (KeyWord.indexOf(key) >= 0)
			{
				//替换掉特殊字符
				KeyWord = KeyWord.replace(key, "");
			}
		}
		return KeyWord;
	}
	/** 
	 检查_sword是否包涵SQL关键字
	 
	 @param _sWord 需要检查的字符串
	 @return 存在SQL注入关键字时返回 true，否则返回 false
	*/
	public static boolean CheckKeyWordInSql(String _sWord)
	{
		boolean result = false;
		//Sql注入de可能关键字
		String[] patten1 = StrKeyWord.split("[|]", -1);
		//Sql注入的可能关键字 的注入情况
		for (String sqlKey : patten1)
		{
			if (_sWord.indexOf(" " + sqlKey) >= 0 || _sWord.indexOf(sqlKey + " ") >= 0)
			{
				//只要存在一个可能出现Sql注入的参数,则直接退出
				result = true;
				break;
			}
		}
		return result;
	}

		///#endregion 其他方法。
	/**
	 * 获得ftp连接对象
	 *
	 * @throws Exception
	 */
	public static FtpUtil getFtpUtil() throws Exception {
		// 获取
		String ip = bp.sys.Glo.String_JieMi_FTP(SystemConfig.getFTPServerIP());

		String userNo = bp.sys.Glo.String_JieMi_FTP(SystemConfig.getFTPUserNo());
		String pass = bp.sys.Glo.String_JieMi_FTP(SystemConfig.getFTPUserPassword());
		String port=bp.sys.Glo.String_JieMi_FTP(String.valueOf(SystemConfig.getFTPServerPort()));

		if(DataType.IsNullOrEmpty(port)){
			port="21";
		}

		FtpUtil ftp = new FtpUtil(ip, Integer.parseInt(port), userNo, pass);
		return ftp;

		// return Platform.JFlow;
	}
	/**
	 * 获得ftp连接对象
	 *
	 * @throws Exception
	 */
	public static SftpUtil getSftpUtil() throws Exception {
		// 获取
		String ip = SystemConfig.getFTPServerIP();

		String userNo = SystemConfig.getFTPUserNo();
		String pass = bp.sys.base.Glo.String_JieMi_FTP(SystemConfig.getFTPUserPassword());

		SftpUtil ftp = new SftpUtil(ip, SystemConfig.getFTPServerPort(), userNo, pass);
		return ftp;

	}
}

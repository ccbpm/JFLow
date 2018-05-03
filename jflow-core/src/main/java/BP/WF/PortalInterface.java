package BP.WF;

import BP.DA.DBAccess;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.Port.Emp;
import BP.Sys.GEDtl;
import BP.Sys.GEDtlAttr;
import BP.Sys.GEDtls;
import BP.Sys.MapDtl;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Data.GERptAttr;
import BP.Web.WebUser;

/** 
 门户接口
 
*/
public class PortalInterface
{
	/** 
	 创建WorkID
	 
	 @param flowNo 流程编号
	 @param ht 表单参数，可以为null。
	 @param workDtls 明细表参数，可以为null。
	 @param nextWorker 操作员，如果为null就是当前人员。
	 @param title 创建工作时的标题，如果为null，就按设置的规则生成。
	 @return 为开始节点创建工作后产生的WorkID.
	 * @throws Exception 
	*/
	public static long Node_CreateBlankWork(String flowNo, java.util.Hashtable ht, DataSet workDtls, String guestNo, String title) throws Exception
	{
		return Node_CreateBlankWork(flowNo, ht, workDtls, guestNo, title, 0, null, 0, null);
	}
	/** 
	 创建WorkID
	 
	 @param flowNo 流程编号
	 @param ht 表单参数，可以为null。
	 @param workDtls 明细表参数，可以为null。
	 @param starter 流程的发起人
	 @param title 创建工作时的标题，如果为null，就按设置的规则生成。
	 @param parentWorkID 父流程的WorkID,如果没有父流程就传入为0.
	 @param parentFlowNo 父流程的流程编号,如果没有父流程就传入为null.
	 @return 为开始节点创建工作后产生的WorkID.
	 * @throws Exception 
	*/
	public static long Node_CreateBlankWork(String flowNo, java.util.Hashtable ht, DataSet workDtls, String guestNo, String title, long parentWorkID, String parentFlowNo, int parentNodeID, String parentEmp) throws Exception
	{
		//if (BP.Web.WebUser.getNo() != "Guest")
		//    throw new Exception("@必须是Guest登陆才能发起.");

		// 转化成编号.
		flowNo = TurnFlowMarkToFlowNo(flowNo);

		//转化成编号
		parentFlowNo = TurnFlowMarkToFlowNo(parentFlowNo);

		String dbstr = SystemConfig.getAppCenterDBVarStr();

		Flow fl = new Flow(flowNo);
		Node nd = new Node(fl.getStartNodeID());


		//把一些其他的参数也增加里面去,传递给ccflow.
		java.util.Hashtable htPara = new java.util.Hashtable();
		if (parentWorkID != 0)
		{
			htPara.put(StartFlowParaNameList.PWorkID, parentWorkID);
		}
		if (parentFlowNo != null)
		{
			htPara.put(StartFlowParaNameList.PFlowNo, parentFlowNo);
		}
		if (parentNodeID != 0)
		{
			htPara.put(StartFlowParaNameList.PNodeID, parentNodeID);
		}
		if (parentEmp != null)
		{
			htPara.put(StartFlowParaNameList.PEmp, parentEmp);
		}



		Emp empStarter = new Emp(BP.Web.WebUser.getNo());
		Work wk = fl.NewWork(empStarter, htPara);
		long workID = wk.getOID();

			///#region 给各个属性-赋值
		if (ht != null)
		{
			for (Object str : ht.keySet())
			{
				wk.SetValByKey(str.toString(), ht.get(str));
			}
		}
		wk.setOID(workID);
		if (workDtls != null)
		{
			//保存从表
			for (DataTable dt : workDtls.Tables)
			{
				for (MapDtl dtl : wk.getHisMapDtls().ToJavaList())
				{
					if (dt.TableName != dtl.getNo())
					{
						continue;
					}
					//获取dtls
					GEDtls daDtls = new GEDtls(dtl.getNo());
					daDtls.Delete(GEDtlAttr.RefPK, wk.getOID()); // 清除现有的数据.

					GEDtl daDtl = (GEDtl)((daDtls.getGetNewEntity() instanceof GEDtl) ? daDtls.getGetNewEntity() : null);
					daDtl.setRefPK((new Long(wk.getOID())).toString());

					// 为从表复制数据.
					for (DataRow dr : dt.Rows)
					{
						daDtl.ResetDefaultVal();
						daDtl.setRefPK((new Long(wk.getOID())).toString());

						//明细列.
						for (DataColumn dc : dt.Columns)
						{
							//设置属性.
							daDtl.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName));
						}
						daDtl.InsertAsOID(DBAccess.GenerOID("Dtl")); //插入数据.
					}
				}
			}
		}
		Paras ps = new Paras();
		// 执行对报表的数据表WFState状态的更新,让它为runing的状态.
		if (StringHelper.isNullOrEmpty(title) == false)
		{
			ps = new Paras();
			ps.SQL = "UPDATE " + fl.getPTable() + " SET WFState=" + dbstr + "WFState,Title=" + dbstr + "Title WHERE OID=" + dbstr + "OID";
			ps.Add(GERptAttr.WFState, WFState.Blank.getValue());
			ps.Add(GERptAttr.Title, title);
			ps.Add(GERptAttr.OID, wk.getOID());
			DBAccess.RunSQL(ps);
		}
		else
		{
			ps = new Paras();
			ps.SQL = "UPDATE " + fl.getPTable() + " SET WFState=" + dbstr + "WFState,FK_Dept=" + dbstr + "FK_Dept,Title=" + dbstr + "Title WHERE OID=" + dbstr + "OID";
			ps.Add(GERptAttr.WFState, WFState.Blank.getValue());
			ps.Add(GERptAttr.FK_Dept, empStarter.getFK_Dept());
			ps.Add(GERptAttr.Title, BP.WF.WorkFlowBuessRole.GenerTitle(fl, wk));
			ps.Add(GERptAttr.OID, wk.getOID());
			DBAccess.RunSQL(ps);
		}

		// 删除有可能产生的垃圾数据,比如上一次没有发送成功，导致数据没有清除.
		ps = new Paras();
		ps.SQL = "DELETE FROM WF_GenerWorkFlow  WHERE WorkID=" + dbstr + "WorkID1 OR FID=" + dbstr + "WorkID2";
		ps.Add("WorkID1", wk.getOID());
		ps.Add("WorkID2", wk.getOID());
		DBAccess.RunSQL(ps);

		ps = new Paras();
		ps.SQL = "DELETE FROM WF_GenerWorkerList  WHERE WorkID=" + dbstr + "WorkID1 OR FID=" + dbstr + "WorkID2";
		ps.Add("WorkID1", wk.getOID());
		ps.Add("WorkID2", wk.getOID());
		DBAccess.RunSQL(ps);

		// 设置流程信息
		if (parentWorkID != 0)
		{
			BP.WF.Dev2Interface.SetParentInfo(flowNo, workID, parentFlowNo, parentWorkID, parentNodeID, parentEmp);
		}

		return wk.getOID();
	}


		///#region 门户。
	/** 
	 登陆
	 
	 @param guestNo 客户编号
	 @param guestName 客户名称
	*/
	public static void Port_Login(String guestNo, String guestName)
	{
		//登陆.
		BP.Web.GuestUser.SignInOfGener(guestNo, guestName, "CH", true);
	}
	/** 
	 登陆
	 
	 @param guestNo 客户编号
	 @param guestName 客户名称
	 @param deptNo 客户的部门编号
	 @param deptName 客户的部门名称
	*/
	public static void Port_Login(String guestNo, String guestName, String deptNo, String deptName)
	{
		//登陆.
		BP.Web.GuestUser.SignInOfGener(guestNo, guestName, deptNo,deptName,"CH", true);
	}
	/** 
	 退出登陆.
	 
	*/
	public static void Port_LoginOunt()
	{
		//登陆.
		BP.Web.GuestUser.Exit();
	}
	
	

		///#endregion 门户。



		///#region 获取Guest的待办
	/** 
	 获取Guest的待办
	 
	 @param fk_flow 流程编号,流程编号为空表示所有的流程.
	 @param guestNo 客户编号
	 @return 结果集合
	*/
	public static DataTable DB_GenerEmpWorksOfDataTable(String fk_flow, String guestNo)
	{
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		Paras ps = new Paras();
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		String sql;

		//不是授权状态
		if (StringHelper.isNullOrEmpty(fk_flow))
		{
			ps.SQL = "SELECT * FROM WF_EmpWorks WHERE GuestNo=" + dbstr + "GuestNo AND FK_Emp='Guest' ORDER BY FK_Flow,ADT DESC ";
			ps.Add("GuestNo", guestNo);
		}
		else
		{
			ps.SQL = "SELECT * FROM WF_EmpWorks WHERE GuestNo=" + dbstr + "GuestNo AND FK_Emp='Guest' AND FK_Flow=" + dbstr + "FK_Flow ORDER BY  ADT DESC ";
			ps.Add("FK_Flow", fk_flow);
			ps.Add("GuestNo", guestNo);
		}
		return BP.DA.DBAccess.RunSQLReturnTable(ps);
	}
	/** 
	 获取未完成的流程(也称为在途流程:我参与的但是此流程未完成)
	 
	 @param fk_flow 流程编号
	 @return 返回从数据视图WF_GenerWorkflow查询出来的数据.
	 * @throws Exception 
	*/
	public static DataTable DB_GenerRuning(String fk_flow, String guestNo) throws Exception
	{
		// 转化成编号.
		fk_flow = TurnFlowMarkToFlowNo(fk_flow);

		String sql;
		int state = WFState.Runing.getValue();

		if (StringHelper.isNullOrEmpty(fk_flow))
		{
			sql = "SELECT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo() + "' AND B.IsEnable=1 AND B.IsPass=1 AND A.GuestNo='" + guestNo + "' ";
		}
		else
		{
			sql = "SELECT a.WorkID FROM WF_GenerWorkFlow A, WF_GenerWorkerlist B WHERE A.FK_Flow='" + fk_flow + "'  AND A.WorkID=B.WorkID AND B.FK_Emp='" + WebUser.getNo() + "' AND B.IsEnable=1 AND B.IsPass=1  AND A.GuestNo='" + guestNo + "'";
		}

		GenerWorkFlows gwfs = new GenerWorkFlows();
		gwfs.RetrieveInSQL(GenerWorkFlowAttr.WorkID, "(" + sql + ")");
		return gwfs.ToDataTableField();
	}

		///#endregion


		///#region 功能
	/** 
	 设置用户信息
	 
	 @param flowNo 流程编号
	 @param workID 工作ID
	 @param guestNo 客户编号
	 @param guestName 客户名称
	 * @throws Exception 
	*/
	public static void SetGuestInfo(String flowNo, long workID, String guestNo, String guestName) throws Exception
	{
		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkFlow SET GuestNo=" + dbstr + "GuestNo, GuestName=" + dbstr + "GuestName WHERE WorkID=" + dbstr + "WorkID";
		ps.Add("GuestNo", guestNo);
		ps.Add("GuestName", guestName);
		ps.Add("WorkID", workID);
		BP.DA.DBAccess.RunSQL(ps);

		Flow fl = new Flow(flowNo);
		ps = new Paras();
		ps.SQL = "UPDATE " + fl.getPTable() + " SET GuestNo=" + dbstr + "GuestNo, GuestName=" + dbstr + "GuestName WHERE OID=" + dbstr + "OID";
		ps.Add("GuestNo", guestNo);
		ps.Add("GuestName", guestName);
		ps.Add("OID", workID);
		BP.DA.DBAccess.RunSQL(ps);
	}
	/** 
	 设置当前用户的待办
	 
	 @param workID 工作ID
	 @param guestNo 客户编号
	 @param guestName 客户名称
	*/
	public static void SetGuestToDoList(long workID, String guestNo, String guestName)
	{
		if (guestNo.equals(""))
		{
			throw new RuntimeException("@设置外部用户待办信息失败:参数guestNo不能为空.");
		}
		if (workID == 0)
		{
			throw new RuntimeException("@设置外部用户待办信息失败:参数workID不能为0.");
		}

		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkerList SET GuestNo=" + dbstr + "GuestNo, GuestName=" + dbstr + "GuestName WHERE WorkID=" + dbstr + "WorkID AND IsPass=0";
		ps.Add("GuestNo", guestNo);
		ps.Add("GuestName", guestName);
		ps.Add("WorkID", workID);
		int i = BP.DA.DBAccess.RunSQL(ps);
		if (i == 0)
		{
			throw new RuntimeException("@设置外部用户待办信息失败:参数workID不能为空.");
		}

		ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkFlow SET GuestNo=" + dbstr + "GuestNo, GuestName=" + dbstr + "GuestName WHERE WorkID=" + dbstr + "WorkID ";
		ps.Add("GuestNo", guestNo);
		ps.Add("GuestName", guestName);
		ps.Add("WorkID", workID);
		i = BP.DA.DBAccess.RunSQL(ps);
		if (i == 0)
		{
			throw new RuntimeException("@WF_GenerWorkFlow - 设置外部用户待办信息失败:参数WorkID不能为空.");
		}
	}

		///#endregion



		///#region 通用方法
	public static String TurnFlowMarkToFlowNo(String FlowMark)
	{
		if (StringHelper.isNullOrEmpty(FlowMark))
		{
			return null;
		}

		// 如果是编号，就不用转化.
		if (DataType.IsNumStr(FlowMark))
		{
			return FlowMark;
		}

		String s = DBAccess.RunSQLReturnStringIsNull("SELECT No FROM WF_Flow WHERE FlowMark='" + FlowMark + "'", null);
		if (s == null)
		{
			throw new RuntimeException("@FlowMark错误:" + FlowMark + ",没有找到它的流程编号.");
		}
		return s;
	}
	/**
	 * 发送消息接口. 需要与web.config中 ShortMessageWriteTo 配置才能起作用。
	 * 发送短信接口(二次开发需要重写这个接口) 
	 * @param msgPK 消息主键，是对应的Sys_SMS的MyPK。
	 * @param sender 发送人(内部帐号，可以为空.)
	 * @param sendToEmpNo 发送给(内部帐号，可以为空.)
	 * @param tel 手机号码
	 * @param msgInfo 短消息
	 * @return 是否发送成功
	 */
	public static boolean SendToWebServices(String msgPK, String sender, String sendToEmpNo, String tel, String msgInfo)
	{
		BP.DA.Log.DefaultLogWriteLineInfo("接口调用成功: SendToWebServices  MyPK" + msgPK +" UserNo:"+sender+ " Tel:" + tel + " msgInfo:" + msgInfo);

		if (SystemConfig.getIsEnableCCIM() && sendToEmpNo != null)
		{
			Glo.SendMessageToCCIM(sender, sendToEmpNo, msgInfo, DataType.getCurrentDataTime());
		}
		return true;
	}
	
	/** 
	 * 发送丁丁的接口
	 * @param msgPK 消息主键，是对应的Sys_SMS的MyPK。
	 * @param sender 发送人(内部帐号，可以为空.)
	 * @param sendToEmpNo 发送给(内部帐号，可以为空.)
	 * @param tel 电话
	 * @param msgInfo 消息内容
	 * @return 是否发送成功
	 */
	public static boolean SendToDingDing(String mypk, String sender, String sendToEmpNo, String tel, String msgInfo)
	{
	 //   BP.DA.Log.DefaultLogWriteLineInfo("接口调用成功: SendToWebServices  MyPK" + mypk +" UserNo:"+userNo+ " Tel:" + tel + " msgInfo:" + msgInfo);

		if (BP.Sys.SystemConfig.getIsEnableCCIM() && sendToEmpNo != null)
		{
			BP.WF.Glo.SendMessageToCCIM(sender, sendToEmpNo, msgInfo, BP.DA.DataType.getCurrentDataTime());
		}
		return true;
	}
	
	/** 
	 * 发送微信的接口
	 * @param mypk 消息主键，是对应的Sys_SMS的MyPK。
	 * @param sender 发送人(内部帐号，可以为空.)
	 * @param sendToEmpNo 发送给(内部帐号，可以为空.)
	 * @param tel
	 * @param msgInfo
	 * @return 是否发送成功
	 */
	public static boolean SendToWeiXin(String mypk, String sender, String sendToEmpNo, String tel, String msgInfo)
	{
	   // BP.DA.Log.DefaultLogWriteLineInfo("接口调用成功: SendToWeiXin  MyPK" + mypk + " UserNo:" + userNo + " Tel:" + tel + " msgInfo:" + msgInfo);

		if (BP.Sys.SystemConfig.getIsEnableCCIM() && sendToEmpNo != null)
		{
			BP.WF.Glo.SendMessageToCCIM(sender, sendToEmpNo, msgInfo, BP.DA.DataType.getCurrentDataTime());
		}
		return true;
	}
	
	
	/** 
	 * 发送邮件接口
	 * @param mypk 消息主键，是对应的Sys_SMS的MyPK。
	 * @param sender 发送人(内部帐号，可以为空.)
	 * @param sendToEmpNo 发送给(内部帐号，可以为空.)
	 * @param email 邮件地址
	 * @param title 标题
	 * @param maildoc 内容
	 * @param sendToEmpNo 接收人编号
	 * @return 是否发送成功
	 */
	public static boolean SendToEmail(String mypk, String sender, String sendToEmpNo, String email, String title, String maildoc)
	{
	    BP.DA.Log.DefaultLogWriteLineInfo("接口调用成功: SendToEmail  MyPK" + mypk + " email:" + email + " title:" + title + " maildoc:" + maildoc);
		if (BP.Sys.SystemConfig.getIsEnableCCIM() && sendToEmpNo != null)
		{
			BP.WF.Glo.SendMessageToCCIM(sender, sendToEmpNo, title + " \t\n " + maildoc, DataType.getCurrentDataTime());
		}
		return true;
	}
	/** 
	 * 发送到CCIM即时通讯
	 * @param mypk 主键
	 * @param email 邮件
	 * @param title 标题
	 * @param maildoc 内容
	 * @return 返回发送结果
	 * @throws Exception 
	 */
	public static boolean SendToCCIM(String mypk, String userNo, String msg, String sourceUserNo) throws Exception
	{
	 //   BP.DA.Log.DefaultLogWriteLineInfo("接口调用成功: SendToEmail  MyPK" + mypk + " userNo:" + userNo + " msg:" + msg);

		if (BP.Sys.SystemConfig.getIsEnableCCIM() && userNo != null)
		{
			BP.WF.Glo.SendMessageToCCIM(WebUser.getNo(), userNo, msg, DataType.getCurrentDataTime());
		}
		return true;
	}
	 
			
			/** 
			 打印文件在处理.
			 
			 @param billFilePath
			*/
	
			
			public final void Print(String billFilePath)
			{
			}
	
		
			/** 
			 用于单点登录的写入SID
			 
			 @param miyue 配置在web.config中的密码，用于两个系统的握手.
			 @param userNo 用户ID , 对应Port_Emp的No列.
			 @param sid 用户SID , 对应Port_Emp的SID列.
			 @return 
			*/
	
			public final boolean WriteUserSID(String miyue, String userNo, String sid)
			{
	
				try
				{
					if (!miyue.equals("xxweerwerew"))
					{
						return false;
					}

					if (userNo.contains(" ") == true)
					{
						return false;
					}

					//判断是否视图，如果为视图则不进行修改 @于庆海 需要翻译
					if (BP.DA.DBAccess.IsView("Port_Emp") == true)
					{
						return false;
					}
					String sql = "UPDATE Port_Emp SET SID='" + sid + "' WHERE No='" + userNo + "'";
					BP.DA.DBAccess.RunSQL(sql);
					return true;
				}
				catch (RuntimeException ex)
				{
					return false;
				}
	
			}
	
	public DataTable GenerEmpsBySpecDeptAndStats(String deptNo, String stations) {
        return GenerEmpsBySpecDeptAndStats(deptNo, stations);
    }
}
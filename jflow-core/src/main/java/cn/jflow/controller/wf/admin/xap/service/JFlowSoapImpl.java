package cn.jflow.controller.wf.admin.xap.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import BP.DA.DBAccess;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Log;
import BP.DA.Paras;
import BP.En.QueryObject;
import BP.Port.Emp;
import BP.Sys.FrmAttachmentDB;
import BP.Sys.FrmEventList;
import BP.Sys.GEDtlAttr;
import BP.Sys.GEDtls;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Tools.FormatToJson;
import BP.Tools.StringHelper;
import BP.WF.AskforHelpSta;
import BP.WF.Dev2Interface;
import BP.WF.Flow;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.SendReturnObjs;
import BP.WF.WFState;
import BP.WF.Work;
import BP.WF.WorkFlow;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Entity.GenerWorkerListAttr;
import BP.WF.Entity.ReturnWorkAttr;
import BP.WF.Entity.ReturnWorks;
import BP.WF.Entity.ShiftWorkAttr;
import BP.WF.Entity.ShiftWorks;
import BP.WF.Port.SMSAttr;
import BP.WF.Template.CCSta;
import BP.WF.Template.HungUpAttr;
import BP.WF.Template.HungUps;
import BP.WF.Template.NodeAttr;
import BP.WF.XML.Tools;
import BP.Web.WebUser;
import cn.jflow.common.util.ConvertTools;
import cn.jflow.common.util.FileUtils;

@Deprecated
//@WebService(endpointInterface = "cn.jflow.controller.wf.admin.xap.service.JFlowSoap")
public class JFlowSoapImpl implements JFlowSoap {
	
	// /#region 流程api
	/**
	 * 催办
	 * 
	 * @param workid
	 *            工作编号
	 * @param msg
	 *            消息
	 * @return
	 */
	public String Flow_DoPress(long workid, String msg, String userNo) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}
		DataSet ds = new DataSet();
		DataTable dataTable = new DataTable(Dev2Interface.Flow_DoPress(workid,
				msg, true));
		ds.Tables.add(dataTable);
		return DataType.ToJson(ds.Tables.get(0));
	}

	// /#endregion

	// /#region Port API

	/**
	 * 退出登录
	 * 
	 * @return
	 */

	public void Port_SigOut(String userNo) {
		Dev2Interface.Port_SigOut();
	}

	/**
	 * 获取菜单
	 * 
	 * @param userNo
	 *            用户编号
	 */

	public String Port_Menu(String userNo) {
		Tools xmls = new Tools();
		xmls.RetrieveAll();

		DataSet ds = new DataSet();
		ds.Tables.add(xmls.ToDataTable());
		// ds.WriteXml("c:\\Port_Menu获取菜单.xml");
		// return Connector.ToXml(ds);
		return BP.DA.DataType.ToJson(ds.Tables.get(0));
	}

	/**
	 * 修改密码
	 * 
	 * @param userNo
	 *            用户名
	 * @param oldPass
	 *            旧密码
	 * @param newPass
	 *            新密码
	 * @return
	 */

	public String Port_ChangePassword(String userNo, String oldPass,
			String newPass) {
		Emp emp = new Emp(userNo);
		if (oldPass.equals(emp.getPass())) {
			emp.setPass(newPass);
			emp.Update();
			return "修改成功，请牢记您的新密码。";
		} else {
			return "密码修改失败，旧密码错误。";
		}
	}

	/**
	 * 获取站内信 返回MsgType, Num 两个列. MsgType 在 BP.Sys.SMSMsgType中定义.
	 * 
	 * @param userNo
	 *            人员编号
	 * @param lastTime
	 *            上一次访问的时间
	 * @return
	 */
	public String Port_SMS(String userNo, String lastTime) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}

		Paras ps = new Paras();
		ps.SQL = "SELECT MsgType , Count(*) as Num FROM Sys_SMS WHERE SendTo='"
				+ userNo + "' AND  RDT >'" + lastTime
				+ "' AND MsgType IS NOT NULL Group By MsgType";
		ps.Add(SMSAttr.SendTo, userNo);

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		return BP.DA.DataType.ToJson(dt);
	}

	/**
	 * 获得当前操作员的系统消息
	 * 
	 * @param userNo
	 * @param lastTime
	 * @return
	 */
	public String Port_SMS_DB(String userNo, String lastTime) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}

		Paras ps = new Paras();
		ps.SQL = "SELECT * FROM Sys_SMS WHERE SendTo='" + userNo
				+ "' AND  RDT >'" + lastTime + "' ORDER BY RDT ";
		ps.Add(SMSAttr.SendTo, userNo);

		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		DataSet ds = new DataSet();
		ds.Tables.add(dt);
		return BP.DA.DataType.ToJson(ds.Tables.get(0));
		// return Connector.ToXml(ds);
	}

	// /#endregion Port API

	// /#region 与数据源相关的接口.
	/**
	 * 获得移动菜单
	 * 
	 * @param userNo
	 *            人员编号
	 * @return 菜单json
	 */
	public String DB_MobileMenu(String userNo) {
		DataSet ds = new DataSet();
		ds.readXml(Glo.getIntallPath() + "/Xml/Mobile.xml");
		return BP.DA.DataType.ToJson(ds.Tables.get(0));
	}

	/**
	 * 获取通讯录
	 * 
	 * @param DeptNo
	 * @return
	 */
	public String Address_List() {
		// *---小周鹏修改 2014-11-05---START*
		// string sql =
		// " select A.No, A.Name as UserName,B.Name as DeptName,A.Tel, A.Email from WF_Emp as A,Port_Dept as B where A.FK_Dept=B.No order by B.No ";
		String sql = " select A.No, A.Name as UserName,B.Name as DeptName,A.Tel, A.Email from Port_Emp  A,Port_Dept  B where A.FK_Dept=B.No order by B.No ";
		// *---小周鹏修改 2014-11-05---END*
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.Columns.Add("Img", String.class);
		for (DataRow dr : dt.Rows) {

			// *---小周鹏修改 2014-09-02---START*
			// dr["Img"] = "/DataUser/UserIcon/" + dr["No"] + ".png";
			dr.setValue("Img", Glo.getCCFlowAppPath() + "DataUser/UserIcon/"
					+ dr.getValue("No") + "Smaller.png");
			// *---小周鹏修改 2014-09-02---END*
		}
		return BP.DA.DataType.ToJson(dt);
	}

	/**
	 * 获取个人信息
	 * 
	 * @param DeptNo
	 * @return
	 */
	public String UserInfoByNo(String userNo) {
		String sql = " select A.No, A.Name as UserName,B.Name as DeptName,A.Tel,A.Email from Port_Emp A,Port_Dept B where A.FK_Dept=B.No and A.NO='"
				+ userNo + "' order by B.No  ";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.Columns.Add("Img", String.class);
		for (DataRow dr : dt.Rows) {
			dr.setValue("Img", Glo.getCCFlowAppPath() + "DataUser/UserIcon/"
					+ dr.getValue("No") + "Smaller.png");

		}
		return BP.DA.DataType.ToJson(dt);
	}

	/**
	 * 更改个人信息
	 * 
	 * @param DeptNo
	 * @return
	 */
	public String UserInfoChange(String userNo, String userName, String tel,
			String email) {

		String sSql = "Update Port_Emp set Name='" + userName + "',Tel='" + tel
				+ "',Email='" + email + "' where No='" + userNo + "'";
		int i = BP.DA.DBAccess.RunSQL(sSql);
		if (i > 0) {
			return " 修改成功！ ";
		} else {
			return " 修改失败！ ";
		}
	}

	/**
	 * 意见反馈
	 * 
	 * @param userNo
	 * @param msg
	 */
	public String WriteUserMsg(String userNo, String msg) {
		String path = Glo.getCCFlowAppPath() + "DataUser/LogOfUser";
		File file = new File(path);
		if (file.exists() == false) {
			file.mkdirs();
		}

		String filePath = path + "/" + userNo + "_"
				+ DataType.getCurrentDateByFormart("yyyy_MM_dd_HH_mm_ss")
				+ ".txt";
		DataType.WriteFile(filePath, msg);
		return "反馈成功.";
	}

	/**
	 * 上传图片
	 * 
	 * @param workid
	 *            工作ID
	 * @param bytestr
	 *            图片
	 * @return
	 */
	public String FileUploadImage(String userNo, String bytestr,
			String smaller, String byImg) {
		if (bytestr.trim().equals("")) {
			return "err:@文件上传失败！";
		}

		try {
			String filePath = Glo.getCCFlowAppPath() + "UserIcon/LogOfUser";
			File file = new File(filePath);
			if (file.exists() == false) {
				file.mkdirs();
			}
			String imgBName = filePath + "" + userNo + "Biger.png";
			String imgSName = filePath + "" + userNo + "Smaller.png";
			String imgName = filePath + "" + userNo + ".png";
			boolean imgB = StringToFile(bytestr, imgBName);
			boolean imgS = StringToFile(smaller, imgSName);
			boolean img = StringToFile(byImg, imgName);
		} catch (RuntimeException ex) {
			return "err:@" + ex.getMessage();
		}

		return "操作成功";

	}

	/**
	 * 把经过base64编码的字符串保存为文件
	 * 
	 * @param base64String
	 *            经base64加码后的字符串
	 * @param fileName
	 *            保存文件的路径和文件名
	 * @return 保存文件是否成功
	 */
	public static boolean StringToFile(String base64String, String fileName) {
		try {
			FileUtils.decoderBase64File(base64String, fileName);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * 获取可以退回的节点集合
	 * 
	 * @param nodeID
	 *            节点ID
	 * @param workid
	 *            工作ID
	 * @param fid
	 *            流程ID
	 * @return 返回退回的信息
	 */
	public String DB_GenerWillReturnNodes(int nodeID, long workid, long fid,
			String userNo) {
		try {
			Emp emp = new Emp(userNo);
			WebUser.SignInOfGener(emp);

			DataSet ds = new DataSet();
			DataTable table = Dev2Interface.DB_GenerWillReturnNodes(nodeID,
					workid, fid);
			ds.Tables.add(table);
			return BP.DA.DataType.ToJson(ds.Tables.get(0));
			// return Connector.ToXml(ds);
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 获得流程树
	 * 
	 * @param userNo
	 * @param sid
	 * @return
	 */
	public String DB_FlowTree(String userNo, String sid) {
		try {

			Emp emp = new Emp(userNo);
			WebUser.SignInOfGener(emp);

			String sql = "SELECT No, Name, ParentNo FROM WF_FlowSort ";
			DataTable sort = DBAccess.RunSQLReturnTable(sql);
			sort.TableName = "WF_FlowSort";

			String sql1 = "SELECT No, Name, FK_FlowSort as ParentNo FROM WF_Flow ";
			DataTable flow = DBAccess.RunSQLReturnTable(sql1);
			flow.TableName = "WF_Flow";

			DataSet ds = new DataSet();
			ds.Tables.add(sort);
			ds.Tables.add(flow);

			return BP.Tools.FormatToJson.ToJson(ds);
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 获得已经完成的流程列表.
	 * 
	 * @param userNo
	 *            用户编号
	 * @param sid
	 *            SID
	 * @return 返回No,Name,Num三个列
	 */
	public String DB_FlowCompleteGroup(String userNo, String sid) {
		try {
			Emp emp = new Emp(userNo);
			WebUser.SignInOfGener(emp);

			DataSet ds = new DataSet();
			DataTable table = Dev2Interface.DB_FlowCompleteGroup(userNo);
			ds.Tables.add(table);
			return BP.DA.DataType.ToJson(ds.Tables.get(0));
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 获得已经完成的流程数据
	 * 
	 * @param userNo
	 * @param sid
	 * @param fk_flow
	 *            流程编号
	 * @param pageSize
	 *            每页的数量
	 * @param pageIdx
	 *            第n页
	 * @return 返回WF_GenerWorklist数据,
	 *         WorkID,Title,Starter,StarterName,WFState,FK_Node
	 */
	public String DB_FlowComplete(String userNo, String sid, String fk_flow,
			int pageSize, int pageIdx) {
		try {

			Emp emp = new Emp(userNo);
			WebUser.SignInOfGener(emp);

			DataTable table = Dev2Interface.DB_FlowComplete(userNo, fk_flow,
					pageSize, pageIdx);
			return BP.DA.DataType.ToJson(table);
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 可以退回到的节点
	 * 
	 * @param nodeID
	 * @param workid
	 * @param fid
	 * @param userNo
	 * @return
	 */
	public String DataTable_DB_GenerWillReturnNodes(int nodeID, long workid,
			long fid, String userNo) {
		try {
			Emp emp = new Emp(userNo);
			WebUser.SignInOfGener(emp);
			DataSet ds = new DataSet();
			DataTable table = Dev2Interface.DB_GenerWillReturnNodes(nodeID,
					workid, fid);
			ds.Tables.add(table);
			return BP.DA.DataType.ToJson(ds.Tables.get(0));
			// return Connector.ToXml(ds);
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	/**
	 * 获得任务池的工作列表
	 * 
	 * @param userNo
	 *            人员编号
	 * @return 获得任务池的工作列表xml
	 */
	public String DB_TaskPool(String userNo) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}

		DataSet ds = new DataSet();
		ds.Tables.add(Dev2Interface.DB_TaskPool());
		ds.WriteXml("c:\\DB_TaskPool获得任务池的工作列表.xml");
		return BP.DA.DataType.ToJson(ds.Tables.get(0));
		// return Connector.ToXml(ds);
	}

	/**
	 * 获得我从任务池里申请下来的工作列表
	 * 
	 * @param userNo
	 *            人员编号
	 * @return 获得我从任务池里申请下来的工作列表xml
	 */
	public String DB_TaskPoolOfMyApply(String userNo) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}

		DataSet ds = new DataSet();
		ds.Tables.add(Dev2Interface.DB_TaskPoolOfMyApply());
		// ds.WriteXml("c:\\DB_TaskPoolOfMyApply获得我从任务池里申请下来的工作列表.xml");
		return BP.DA.DataType.ToJson(ds.Tables.get(0));
		// return Connector.ToXml(ds);
	}

	/**
	 * 获取当前操作员可以发起的流程集合
	 * 
	 * @param userNo
	 *            人员编号
	 * @return 可以发起的xml
	 */
	public String DB_GenerCanStartFlowsOfDataTable(String userNo) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}

		DataSet ds = new DataSet();
		ds.Tables.add(Dev2Interface.DB_GenerCanStartFlowsOfDataTable(userNo));

		// DataType.WriteFile("c:\\DB_GenerCanStartFlowsOfDataTable发起.xml",
		// Connector.ToXml(ds));
		// ds.WriteXml("c:\\aa.xml");
		// string strs = BP.DA.DataType.ReadTextFile("c:\\aa.xml");
		// return strs;
		return BP.DA.DataType.ToJson(ds.Tables.get(0));
		// return Connector.ToXml(ds);
	}

	/**
	 * @param userNo
	 * @return
	 */
	public String DataTable_DB_GenerCanStartFlowsOfDataTable(String userNo) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}

		DataSet ds = new DataSet();
		ds.Tables.add(Dev2Interface.DB_GenerCanStartFlowsOfDataTable(userNo));
		return BP.DA.DataType.ToJson(ds.Tables.get(0));

		// ds.WriteXml("c:\\aa.xml");
		// string strs = BP.DA.DataType.ReadTextFile("c:\\aa.xml");
		// return strs;
		// return Connector.ToXml(ds);
	}

	/**
	 * 待办列表
	 * 
	 * @param userNo
	 *            人员编号
	 * @return 待办列表xml
	 */
	public String DataTable_DB_GenerEmpWorksOfDataTable(String userNo) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}
		DataTable dt = Dev2Interface.DB_GenerEmpWorksOfDataTable();

		return BP.DA.DataType.ToJson(dt);
	}

	/**
	 * 待办列表
	 * 
	 * @param userNo
	 *            人员编号
	 * @return 待办列表xml
	 */
	public String DB_GenerEmpWorksOfDataTable(String userNo) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}

		DataSet ds = new DataSet();
		ds.Tables.add(Dev2Interface.DB_GenerEmpWorksOfDataTable());
		return BP.DA.DataType.ToJson(ds.Tables.get(0));
		// // ds.WriteXml("c:\\DB_GenerEmpWorksOfDataTable待办.xml");
		// string str = Connector.ToXml(ds);
		// // BP.DA.DataType.WriteFile("c:\\aaa.xml", str);
		// return str;
	}

	/**
	 * 抄送列表
	 * 
	 * @param userNo
	 *            人员编号
	 * @return 操送列表xml
	 */
	public String DB_CCList(String userNo) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}

		DataSet ds = new DataSet();
		ds.Tables.add(Dev2Interface.DB_CCList(userNo));
		// ds.WriteXml("c:\\DB_CCList抄送.xml");
		return BP.DA.DataType.ToJson(ds.Tables.get(0));
		// return Connector.ToXml(ds);
	}

	/**
	 * 执行抄送已阅
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param fk_node
	 *            流程节点
	 * @param workID
	 *            工作id
	 * @param fid
	 *            流程id
	 * @param msge
	 *            填写意见
	 */
	public String Node_DoCCCheckNote(String userNo, String sid, String fk_flow,
			int fk_node, long workID, long fid, String msge) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}

		// Dev2Interface.Node_DoCCCheckNote(fk_flow, fk_node, workID, fid,
		// msge);
		return "已阅完成";
	}

	/**
	 * @param flowNo
	 * @param WorkID
	 * @param FID
	 * @return
	 */
	public String DB_Truck(String flowNo, long WorkID, long FID) {
		String sqlOfWhere2 = "";
		String sqlOfWhere1 = "";

		String dbStr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras prs = new Paras();
		if (FID == 0) {
			sqlOfWhere1 = " WHERE (FID=" + dbStr + "WorkID11 OR WorkID="
					+ dbStr + "WorkID12 )  ";
			prs.Add("WorkID11", WorkID);
			prs.Add("WorkID12", WorkID);
		} else {
			sqlOfWhere1 = " WHERE (FID=" + dbStr + "FID11 OR WorkID=" + dbStr
					+ "FID12 ) ";
			prs.Add("FID11", FID);
			prs.Add("FID12", FID);
		}

		String sql = "";
		sql = "SELECT MyPK,ActionType,ActionTypeText,FID,WorkID,NDFrom,NDFromT,NDTo,NDToT,EmpFrom,EmpFromT,EmpTo,EmpToT,RDT,WorkTimeSpan,Msg,NodeData,Exer FROM ND"
				+ Integer.parseInt(flowNo)
				+ "Track "
				+ sqlOfWhere1
				+ " ORDER BY RDT";
		prs.SQL = sql;

		DataTable dt = DBAccess.RunSQLReturnTable(prs);
		DataSet ds = new DataSet();
		ds.Tables.add(dt);
		return BP.DA.DataType.ToJson(ds.Tables.get(0));
		// return Connector.ToXml(ds);
	}

	/**
	 * 挂起列表
	 * 
	 * @param userNo
	 *            人员编号
	 * @return 挂起列表xml
	 */
	public String DB_GenerHungUpList(String userNo) {
		DataSet ds = new DataSet();
		ds.Tables.add(Dev2Interface.DB_GenerHungUpList(userNo));
		// ds.WriteXml("c:\\DB_GenerCanStartFlowsOfDataTable挂起.xml");
		return BP.DA.DataType.ToJson(ds.Tables.get(0));
		// return Connector.ToXml(ds);
	}

	/**
	 * 在途列表
	 * 
	 * @param userNo
	 *            人员编号
	 * @return 在途列表xml
	 */
	public String DB_GenerRuning(String userNo) {

		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}

		DataSet ds = new DataSet();
		ds.Tables.add(Dev2Interface.DB_GenerRuning());
		// ds.WriteXml("c:\\DB_GenerRuning在途列表.xml");
		return BP.DA.DataType.ToJson(ds.Tables.get(0));
		// return Connector.ToXml(ds);
	}

	// /#endregion 与数据源相关的接口.

	/**
	 * 创建空白工作WorkID
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param starter
	 *            发起人
	 * @param title
	 *            标题
	 * @return workid
	 */
	public String Node_CreateBlankWork(String flowNo, String starter,
			String title) {
		if (!starter.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(starter);
			// throw new Exception("@当前登录用户非(" + WebUser.getNo() + ")");
		}
		DataSet ds = new DataSet();
		long tableName = Dev2Interface.Node_CreateBlankWork(flowNo, null, null,
				starter, title);
		DataTable dt = new DataTable(String.valueOf(tableName));
		ds.Tables.add(dt);

		return BP.DA.DataType.ToJson(ds.Tables.get(0));
	}

	/**
	 * 执行删除
	 * 
	 * @param mypk
	 * @return 小周鹏修改 2014-09-02 修改追加返回值
	 */
	public String Node_CC_DoDel(String mypk) {
		Dev2Interface.Node_CC_DoDel(mypk);
		// *---小周鹏修改 2014-09-02---START*
		return "删除成功！";
		// *---小周鹏修改 2014-09-02---END*
	}

	/**
	 * 设置读取了
	 * 
	 * @param mypk
	 * @return
	 */
	public String Node_CC_SetRead(String mypks) {
		String[] strs = mypks.split("[,]", -1);
		for (String str : strs) {
			Dev2Interface.Node_CC_SetRead(str);
		}
		return null;
	}

	/**
	 * 执行抄送
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param fk_node
	 *            节点编号
	 * @param workID
	 *            工作ID
	 * @param toEmpNo
	 *            抄送给人员编号,多个用逗号分开比如 zhangsan,lisi
	 * @param msgTitle
	 *            消息标题
	 * @param msgDoc
	 *            消息内容
	 * @param pFlowNo
	 *            父流程编号(可以为null)
	 * @param pWorkID
	 *            父流程WorkID(可以为0)
	 * @return
	 */
	public String Node_CC(String userNo, String sid, String fk_flow,
			int fk_node, long workID, String toEmpNos, String msgTitle,
			String msgDoc, String pFlowNo, long pWorkID) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}

		toEmpNos = toEmpNos.replace(";", ",");
		toEmpNos = toEmpNos.replace("；", ",");
		toEmpNos = toEmpNos.replace("，", ",");

		String[] toEmps = toEmpNos.split("[,]", -1);
		String strs = "";
		for (String item : toEmps) {
			if (StringHelper.isNullOrEmpty(item) == true) {
				continue;
			}
			Emp emp = new Emp(item);
			strs += emp.getName() + " ";

			Dev2Interface.Node_CC(fk_flow, fk_node, workID, emp.getNo(),
					emp.getName(), msgTitle, msgDoc, pFlowNo, pWorkID);
		}

		return "执行抄送成功,抄送给:" + strs;
	}

	/**
	 * 设置当前工作状态为草稿,如果启用了草稿,请在开始节点的表单保存按钮下增加上它. 必须是在开始节点时调用.
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workID
	 *            工作ID
	 */
	public void Node_SetDraft(String fk_flow, long workID) {
		Dev2Interface.Node_SetDraft(fk_flow, workID);
	}

	/**
	 * 设置工作已读
	 * 
	 * @param nodeID
	 * @param workids
	 * @return
	 */
	public String Node_SetWorkRead(int nodeID, String workids) {
		String[] strs = workids.split("[,]", -1);
		for (String str : strs) {
			if (StringHelper.isNullOrEmpty(str)) {
				continue;
			}

			Dev2Interface.Node_SetWorkRead(nodeID, Long.parseLong(str));

		}
		return null;
	}

	/**
	 * 节点工作取消挂起
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workid
	 *            工作ID
	 * @param msg
	 *            取消挂起原因
	 * @return 执行信息
	 */
	public void Node_UnHungUpWork(String fk_flow, long workid, String msg) {
		Dev2Interface.Node_UnHungUpWork(fk_flow, workid, msg);
	}

	/**
	 * 撤销发送
	 * 
	 * @param fk_flow
	 * @param workid
	 */
	public String Flow_DoUnSend(String fk_flow, long workid, String userNo) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}
		try {
			Dev2Interface.Flow_DoUnSend(fk_flow, workid);
			return "撤销成功.";
		} catch (RuntimeException ex) {
			return "撤销失败:" + ex.getMessage();
		}
	}

	/**
	 * 节点工作挂起
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workid
	 *            工作ID
	 * @param way
	 *            挂起方式
	 * @param reldata
	 *            解除挂起日期(可以为空)
	 * @param hungNote
	 *            挂起原因
	 * @return 返回执行信息
	 */
	public String Node_HungUpWork(String fk_flow, long workid, int wayInt,
			String reldata, String hungNote) {
		return Dev2Interface.Node_HungUpWork(fk_flow, workid, wayInt, reldata,
				hungNote);
	}

	/**
	 * 申请共享任务
	 * 
	 * @param workid
	 *            工作ID
	 * @param toEmp
	 *            移交到人员(只给移交给一个人)
	 * @param msg
	 *            移交消息
	 */
	public String Node_TaskPoolTakebackOne(long workID, String userNo) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}

		Dev2Interface.Node_TaskPoolTakebackOne(workID);
		return "申请成功！";
	}

	/**
	 * 申请共享任务
	 * 
	 * @param workid
	 *            工作ID
	 * @param toEmp
	 *            移交到人员(只给移交给一个人)
	 * @param msg
	 *            移交消息
	 */

	public String Node_TaskPoolPutOne(long workID, String userNo) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}

		Dev2Interface.Node_TaskPoolPutOne(workID);
		return "申请成功！";
	}

	/**
	 * 工作移交
	 * 
	 * @param workid
	 *            工作ID
	 * @param toEmp
	 *            移交到人员(只给移交给一个人)
	 * @param msg
	 *            移交消息
	 */
	public String Node_Shift(String flowNo, int nodeID, long workID, long fid,
			String toEmp, String msg, String userNo, String sid) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}
		return Dev2Interface
				.Node_Shift(flowNo, nodeID, workID, fid, toEmp, msg);
	}

	// *---小周鹏添加 2014-09-22---START*
	/**
	 * 撤销移交
	 * 
	 * @param fk_flow
	 *            流程
	 * @param workid
	 *            工作ID
	 * @param userNo
	 *            用户
	 * @param sid
	 *            安全码
	 */

	public String Un_Node_Shift(String userNo, String sid, String fk_flow,
			long workID) {
		this.LetUserLogin(userNo, sid);

		String resultMsg = null;
		try {
			WorkFlow mwf = new WorkFlow(fk_flow, workID);
			String str = mwf.DoUnShift();

			resultMsg = str;
		} catch (RuntimeException ex) {
			resultMsg = "err: @执行撤消失败，失败信息：" + ex.getMessage();
		}
		return resultMsg;
	}

	// *---小周鹏添加 2014-09-22---END*
	/**
	 * 执行工作退回(退回指定的点)
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @param fid
	 *            流程ID
	 * @param currentNodeID
	 *            当前节点ID
	 * @param returnToNodeID
	 *            退回到的工作ID
	 * @param msg
	 *            退回原因
	 * @param isBackToThisNode
	 *            退回后是否要原路返回？
	 * @return 执行结果，此结果要提示给用户。
	 */

	public String Node_ReturnWork(String fk_flow, long workID, long fid,
			int currentNodeID, int returnToNodeID, String returnToEmp,
			String msg, boolean isBackToThisNode, String userNo, String sid) {
		try {
			// 让用户登录.
			LetUserLogin(userNo, sid);
			return Dev2Interface.Node_ReturnWork(fk_flow, workID, fid,
					currentNodeID, returnToNodeID, returnToEmp, msg,
					isBackToThisNode);
		} catch (RuntimeException ex) {
			return "err@" + ex.getMessage();
		}
	}

	public String DataSetToXml(DataSet ds) {
		String strs = "";
		strs += "<DataSet>";
		for (DataTable dt : ds.Tables) {
			strs += "\t\n<" + dt.TableName + ">";
			for (DataRow dr : dt.Rows) {
				strs += "\t\n< ";
				for (DataColumn dc : dt.Columns) {
					strs += dc.ColumnName + "='" + dr.getValue(dc.ColumnName)
							+ "' ";
				}
				strs += "/>";
			}
			strs += "\t\n</" + dt.TableName + ">";
		}
		strs += "\t\n</DataSet>";
		return strs;
	}

	/**
	 * 待办提示
	 * 
	 * @param userNo
	 * @return
	 */

	// [WebMethod]
	public String AlertString(String userNo) {
		return "@EmpWorks=12@CC=34";
	}

	/**
	 * 用户登录 0,密码用户名错误 返回一个长的字符串标识登录成功，标识本地登录的安全验证码. 2,服务器错误.
	 * 
	 * @param userNo
	 * @param pass
	 * @return
	 */
	public String Port_Login(String userNo, String pass) {
		try {
			Emp emp = new Emp();
			emp.setNo(userNo);

			if (emp.RetrieveFromDBSources() == 0) {
				return "0"; // 没有查询到。
			}

			if (emp.CheckPass(pass) == false) {
				return "1"; // 密码错误.
			}

			return Dev2Interface.Port_GetSIDName(userNo);
		} catch (RuntimeException ex) {
			// 数据库连接不上.
			Log.DefaultLogWriteLineError(ex.getMessage());
			return "2";
		}
	}

	/**
	 * 设置SID
	 * 
	 * @param userNo
	 *            用户编号
	 * @param sid
	 *            SID号
	 */
	public void Port_SetSID(String userNo, String sid) {
		Dev2Interface.Port_SetSID(userNo, sid);
	}

	/**
	 * 信息执行
	 * 
	 * @param flag
	 *            执行的标记
	 * @param val0
	 * @param val1
	 * @param val2
	 * @param val3
	 * @param val4
	 * @param val5
	 * @return
	 */

	public String DoIt(String flag, String userNo, String sid, String fk_flow,
			String workID, String msg, String delModel, String val4, String val5) {
		LetUserLogin(userNo, sid);
		try {

			if (flag.equals("UnSend")) // 撤销发送..
			{
				return Dev2Interface.Flow_DoUnSend(fk_flow,
						Long.parseLong(workID));
			} else if (flag.equals("EndWorkFlow")) // 结束流程.
			{
				return Dev2Interface.Flow_DoFlowOver(fk_flow,
						Long.parseLong(workID), msg);
			} else if (flag.equals("DelWorkFlow")) // 删除流程.
			{
				String model = delModel;
				if (model.equals("1")) {
					// 逻辑删除
					return Dev2Interface.Flow_DoDeleteFlowByFlag(fk_flow,
							Long.parseLong(workID), msg, false);
				}
				if (model.equals("2")) {
					// 写入日志方式删除
					return Dev2Interface.Flow_DoDeleteFlowByWriteLog(fk_flow,
							Long.parseLong(workID), msg, false);
				}
				if (model.equals("3")) {
					// 彻底删除
					return Dev2Interface.Flow_DoDeleteFlowByReal(fk_flow,
							Long.parseLong(workID), false);
				}
				throw new RuntimeException("@没有判断的删除模式." + delModel);
			} else {
				throw new RuntimeException("@没有约定的标记:" + flag);
			}
		} catch (RuntimeException ex) {
			return "err:" + ex.getMessage();
		}
	}

	/**
	 * 获取产生流程轨迹流程数据表的track.
	 * 
	 * @param fk_flow
	 * @param workID
	 * @param fid
	 * @param userNo
	 * @param sid
	 * @return 返回流程需要用的东西
	 */

	public String GenerFlowTrack_Josn(String fk_flow, long workID, long fid,
			String userNo, String sid) {
		DataSet ds = Dev2Interface.DB_GenerTrack(fk_flow, workID, fid);
		return BP.Tools.FormatToJson.ToJson(ds);
	}

	/**
	 * 获取一条待办工作
	 * 
	 * @param fk_flow
	 *            工作编号
	 * @param fk_node
	 *            节点编号
	 * @param workID
	 *            工作ID
	 * @param userNo
	 *            操作员编号
	 * @return string的json
	 */

	public String GenerWorkNode_JSON(String fk_flow, int fk_node, long workID,
			long fid, String userNo, String sid) {
		this.LetUserLogin(userNo, sid);
		DataSet ds = this.GenerWorkNode(fk_flow, fk_node, workID, fid, userNo);
		return BP.Tools.FormatToJson.ToJson(ds);
	}

	private DataSet GenerWorkNode(String fk_flow, int fk_node, long workID,
			long fid, String userNo) {
		if (fk_node == 0) {
			fk_node = Integer.parseInt(fk_flow + "01");
		}

		if (workID == 0) {
			workID = Dev2Interface.Node_CreateBlankWork(fk_flow, null, null,
					userNo, null);
		}

		try {
			Emp emp = new Emp(userNo);
			WebUser.SignInOfGener(emp);

			MapData md = new MapData();
			md.setNo("ND" + fk_node);
			if (md.RetrieveFromDBSources() == 0) {
				throw new RuntimeException("装载错误，该表单ID=" + md.getNo()
						+ "丢失，请修复一次流程重新加载一次.");
			}

			DataSet myds = md.GenerHisDataSet();

			// /#region 流程设置信息.
			Node nd = new Node(fk_node);

			if (nd.getIsStartNode() == false) {
				Dev2Interface.Node_SetWorkRead(fk_node, workID);
			}

			// 节点数据.
			String sql = "SELECT * FROM WF_Node WHERE NodeID=" + fk_node;
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = "WF_NodeBar";
			myds.Tables.add(dt);

			// 流程数据.
			Flow fl = new Flow(fk_flow);
			myds.Tables.add(fl.ToDataTableField("WF_Flow"));

			// /#endregion 流程设置信息.

			// /#region 把主从表数据放入里面.
			// .工作数据放里面去, 放进去前执行一次装载前填充事件.
			Work wk = nd.getHisWork();
			wk.setOID(workID);
			wk.RetrieveFromDBSources();

			// 处理传递过来的参数。
			ArrayList<String> keys = BP.Sys.Glo.getQueryStringKeys();
			for (String key : keys) {
				wk.SetValByKey(key, BP.Sys.Glo.getRequest().getParameter(key));
			}
			// for (String k :
			// System.Web.HttpContext.Current.Request.QueryString.AllKeys)
			// {
			// wk.SetValByKey(k,
			// System.Web.HttpContext.Current.Request.QueryString[k]);
			// }

			// 执行一次装载前填充.
			String msg = md.getFrmEvents().DoEventNode(
					FrmEventList.FrmLoadBefore, wk);
			if (StringHelper.isNullOrEmpty(msg) == false) {
				throw new RuntimeException("错误:" + msg);
			}

			wk.ResetDefaultVal();
			myds.Tables.add(wk.ToDataTableField(md.getNo()));

			// 把附件的数据放入.
			if (md.getFrmAttachments().size() > 0) {
				sql = "SELECT * FROM Sys_FrmAttachmentDB where RefPKVal="
						+ workID + " AND FK_MapData='ND" + fk_node + "'";
				dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				dt.TableName = "Sys_FrmAttachmentDB";
				myds.Tables.add(dt);
			}
			// 图片附件数据放入
			if (md.getFrmImgAths().size() > 0) {
				sql = "SELECT * FROM Sys_FrmImgAthDB where RefPKVal=" + workID
						+ " AND FK_MapData='ND" + fk_node + "'";
				dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				dt.TableName = "Sys_FrmImgAthDB";
				myds.Tables.add(dt);
			}

			// 把从表的数据放入.
			if (md.getMapDtls().size() > 0) {
				ArrayList<MapDtl> mapDtls = MapDtls.convertMapDtls(md
						.getMapDtls());
				for (MapDtl dtl : mapDtls) {
					GEDtls dtls = new GEDtls(dtl.getNo());
					QueryObject qo = null;
					try {
						qo = new QueryObject(dtls);
						switch (dtl.getDtlOpenType()) {
						case ForEmp: // 按人员来控制.
							qo.AddWhere(GEDtlAttr.RefPK, workID);
							qo.addAnd();
							qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
							break;
						case ForWorkID: // 按工作ID来控制
							qo.AddWhere(GEDtlAttr.RefPK, workID);
							break;
						case ForFID: // 按流程ID来控制.
							qo.AddWhere(GEDtlAttr.FID, workID);
							break;
						}
					} catch (java.lang.Exception e) {
						dtls.getGetNewEntity().CheckPhysicsTable();
					}
					DataTable dtDtl = qo.DoQueryToTable();

					// 为明细表设置默认值.
					MapAttrs dtlAttrs = new MapAttrs(dtl.getNo());
					for (MapAttr attr : dtlAttrs.ToJavaList()) {
						// 处理它的默认值.
						if (attr.getDefValReal().contains("@") == false) {
							continue;
						}

						for (DataRow dr : dtDtl.Rows) {
							dr.setValue(attr.getKeyOfEn(), attr.getDefVal());
						}
					}

					dtDtl.TableName = dtl.getNo(); // 修改明细表的名称.
					myds.Tables.add(dtDtl); // 加入这个明细表, 如果没有数据，xml体现为空.
				}
			}

			// /#endregion

			// /#region 把外键表加入DataSet
			DataTable dtMapAttr = myds.Tables.get(myds.Tables
					.indexOf("Sys_MapAttr"));
			for (DataRow dr : dtMapAttr.Rows) {
				String lgType = dr.getValue("LGType").toString();
				if (!lgType.equals("2")) {
					continue;
				}

				String UIIsEnable = dr.getValue("UIIsEnable").toString();
				if (UIIsEnable.equals("0")) {
					continue;
				}

				String uiBindKey = dr.getValue("UIBindKey").toString();
				if (StringHelper.isNullOrEmpty(uiBindKey) == true) {
					String myPK = dr.getValue("MyPK").toString();
					// 如果是空的
					throw new RuntimeException("@属性字段数据不完整，流程:" + fl.getNo()
							+ fl.getName() + ",节点:" + nd.getNodeID()
							+ nd.getName() + ",属性:" + myPK
							+ ",的UIBindKey IsNull ");
				}

				// 判断是否存在.
				if (myds.Tables.contains(uiBindKey) == true) {
					continue;
				}

				myds.Tables.add(BP.Sys.PubClass
						.GetDataTableByUIBineKey(uiBindKey));
			}

			// /#endregion End把外键表加入DataSet

			// /#region 把流程信息放入里面.
			// 把流程信息表发送过去.
			GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.setWorkID(workID);
			gwf.RetrieveFromDBSources();

			myds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));

			if (gwf.getWFState() == WFState.Shift) {
				// 如果是转发.
				ShiftWorks fws = new ShiftWorks();
				fws.Retrieve(ShiftWorkAttr.WorkID, workID,
						ShiftWorkAttr.FK_Node, fk_node);
				myds.Tables.add(fws.ToDataTableField("WF_ShiftWork"));
			}

			if (gwf.getWFState() == WFState.ReturnSta) {
				// 如果是退回.
				ReturnWorks rts = new ReturnWorks();
				rts.Retrieve(ReturnWorkAttr.WorkID, workID,
						ReturnWorkAttr.ReturnToNode, fk_node,
						ReturnWorkAttr.RDT);
				myds.Tables.add(rts.ToDataTableField("WF_ReturnWork"));
			}

			if (gwf.getWFState() == WFState.HungUp) {
				// 如果是挂起.
				HungUps hups = new HungUps();
				hups.Retrieve(HungUpAttr.WorkID, workID, HungUpAttr.FK_Node,
						fk_node);
				myds.Tables.add(hups.ToDataTableField("WF_HungUp"));
			}

			// if (gwf.WFState == WFState.Askfor)
			// {
			// //如果是加签.
			// ShiftWorks fws = new ShiftWorks();
			// fws.Retrieve(ShiftWorkAttr.WorkID, workID, ShiftWorkAttr.FK_Node,
			// fk_node);
			// myds.Tables.add(fws.ToDataTableField("WF_ShiftWork"));
			// }

			long wfid = workID;
			if (fid != 0) {
				wfid = fid;
			}

			// 放入track信息.
			Paras ps = new Paras();
			ps.SQL = "SELECT * FROM ND" + Integer.parseInt(fk_flow)
					+ "Track WHERE WorkID="
					+ BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID";
			ps.Add("WorkID", wfid);
			DataTable dtNode = DBAccess.RunSQLReturnTable(ps);
			dtNode.TableName = "Track";
			myds.Tables.add(dtNode);

			// 工作人员列表，用于审核组件.
			ps = new Paras();
			ps.SQL = "SELECT * FROM  WF_GenerWorkerlist WHERE WorkID="
					+ BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID";
			ps.Add("WorkID", wfid);
			DataTable dtGenerWorkerlist = DBAccess.RunSQLReturnTable(ps);
			dtGenerWorkerlist.TableName = "WF_GenerWorkerlist";
			myds.Tables.add(dtGenerWorkerlist);

			// 放入CCList信息. 用于审核组件.
			ps = new Paras();
			ps.SQL = "SELECT * FROM WF_CCList WHERE WorkID="
					+ BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID";
			ps.Add("WorkID", wfid);
			DataTable dtCCList = DBAccess.RunSQLReturnTable(ps);
			dtCCList.TableName = "WF_CCList";
			myds.Tables.add(dtCCList);

			// 放入WF_SelectAccper信息. 用于审核组件.
			ps = new Paras();
			ps.SQL = "SELECT * FROM WF_SelectAccper WHERE WorkID="
					+ BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID";
			ps.Add("WorkID", wfid);
			DataTable dtSelectAccper = DBAccess.RunSQLReturnTable(ps);
			dtSelectAccper.TableName = "WF_SelectAccper";
			myds.Tables.add(dtSelectAccper);

			// 放入所有的节点信息. 用于审核组件.
			ps = new Paras();
			ps.SQL = "SELECT * FROM WF_Node WHERE FK_Flow="
					+ BP.Sys.SystemConfig.getAppCenterDBVarStr()
					+ "FK_Flow ORDER BY " + NodeAttr.Step;
			ps.Add("FK_Flow", fk_flow);
			DataTable dtNodes = DBAccess.RunSQLReturnTable(ps);
			dtNodes.TableName = "Nodes";
			myds.Tables.add(dtNodes);

			// /#endregion 把流程信息放入里面.

			return myds;
		} catch (RuntimeException ex) {
			Log.DebugWriteError(ConvertTools.getStackTraceString(ex
					.getStackTrace()));
			throw new RuntimeException(ex.getMessage());
		}
	}

	/**
	 * 获取一条待办工作
	 * 
	 * @param fk_flow
	 *            工作编号
	 * @param fk_node
	 *            节点编号
	 * @param workID
	 *            工作ID
	 * @param userNo
	 *            操作员编号
	 * @return string的json
	 */

	public String GenerWorkNode_JSONV2(String fk_flow, int fk_node,
			long workID, long fid, boolean isCc, float srcWidth,
			float srcHeight, String userNo, String sid) {
		this.LetUserLogin(userNo, sid);
		DataSet ds = this.GenerWorkNodeV2(fk_flow, fk_node, workID, fid, isCc,
				srcWidth, srcHeight);
		return BP.Tools.FormatToJson.ToJson(ds);
	}

	private DataSet GenerWorkNodeV2(String fk_flow, int fk_node, long workID,
			long fid, boolean iscc, float srcWidth, float srcHeight) {
		if (fk_node == 0) {
			fk_node = Integer.parseInt(fk_flow + "01");
		}

		if (workID == 0) {
			workID = Dev2Interface.Node_CreateBlankWork(fk_flow, null, null,
					WebUser.getNo(), null);
		}

		try {
			MapData md = new MapData();
			md.setNo("ND" + fk_node);
			if (md.RetrieveFromDBSources() == 0) {
				throw new RuntimeException("装载错误，该表单ID=" + md.getNo()
						+ "丢失，请修复一次流程重新加载一次.");
			}

			DataSet myds = new DataSet(); // md.GenerHisDataSet();

			// /#region 流程设置信息.
			Node nd = new Node(fk_node);

			// 流程数据. 计算出来表单的位移.
			String sql;
			try {
				sql = "SELECT  Ver as FlowVer, '" + md.getVer()
						+ "' as FormVer, " + MapData.GenerSpanWeiYi(md, srcWidth)
						+ " as WeiYi, " + MapData.GenerSpanHeight(md, srcHeight)
						+ " as SrcH, " + MapData.GenerSpanWidth(md, srcWidth)
						+ " as SrcW  FROM WF_Flow WHERE No='" + fk_flow + "'";
				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				dt.TableName = "BaseInfo";

				// 增加参数。
				dt.Columns.Add("WeiYi2", Float.class);
				dt.Columns.Add("SrcH2", Float.class);
				dt.Columns.Add("SrcW2", Float.class);

				dt.Rows.get(0).setValue("WeiYi2",
						MapData.GenerSpanWeiYi(md, srcHeight));
				dt.Rows.get(0).setValue("SrcH2",
						MapData.GenerSpanHeight(md, srcWidth));
				dt.Rows.get(0).setValue("SrcW2",
						MapData.GenerSpanWidth(md, srcHeight));

				myds.Tables.add(dt);
				Work wk = nd.getHisWork();
				wk.setOID(workID);
				wk.RetrieveFromDBSources();

				// 处理传递过来的参数。
				ArrayList<String> keys = BP.Sys.Glo.getQueryStringKeys();
				for (String key : keys) {
					wk.SetValByKey(key, BP.Sys.Glo.getRequest().getParameter(key));
				}

				// for (String k :
				// System.Web.HttpContext.Current.Request.QueryString.AllKeys)
				// {
				// wk.SetValByKey(k,
				// System.Web.HttpContext.Current.Request.QueryString[k]);
				// }

				// 执行一次装载前填充.
				String msg = md.getFrmEvents().DoEventNode(
						FrmEventList.FrmLoadBefore, wk);
				if (StringHelper.isNullOrEmpty(msg) == false) {
					throw new RuntimeException("错误:" + msg);
				}

				wk.ResetDefaultVal();
				myds.Tables.add(wk.ToDataTableField(md.getNo()));

				// 把附件的数据放入.
				if (md.getFrmAttachments().size() > 0) {
					sql = "SELECT * FROM Sys_FrmAttachmentDB where RefPKVal="
							+ workID + " AND FK_MapData='ND" + fk_node + "'";
					dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
					dt.TableName = "Sys_FrmAttachmentDB";
					myds.Tables.add(dt);
				}
				// 图片附件数据放入
				if (md.getFrmImgAths().size() > 0) {
					sql = "SELECT * FROM Sys_FrmImgAthDB where RefPKVal=" + workID
							+ " AND FK_MapData='ND" + fk_node + "'";
					dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
					dt.TableName = "Sys_FrmImgAthDB";
					myds.Tables.add(dt);
				}
			} catch (Exception e1) {
				Log.DebugWriteError("JflowSoapImpl GenerWorkNodeV2" + e1);
			}
			// 把从表的数据放入.
			if (md.getMapDtls().size() > 0) {
				ArrayList<MapDtl> mds = MapDtls.convertMapDtls(md.getMapDtls());
				for (MapDtl dtl : mds) {
					GEDtls dtls = new GEDtls(dtl.getNo());
					QueryObject qo = null;
					try {
						qo = new QueryObject(dtls);
						switch (dtl.getDtlOpenType()) {
						case ForEmp: // 按人员来控制.
							qo.AddWhere(GEDtlAttr.RefPK, workID);
							qo.addAnd();
							qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
							break;
						case ForWorkID: // 按工作ID来控制
							qo.AddWhere(GEDtlAttr.RefPK, workID);
							break;
						case ForFID: // 按流程ID来控制.
							qo.AddWhere(GEDtlAttr.FID, workID);
							break;
						}
					} catch (java.lang.Exception e) {
						dtls.getGetNewEntity().CheckPhysicsTable();
					}
					DataTable dtDtl = qo.DoQueryToTable();

					// 为明细表设置默认值.
					MapAttrs dtlAttrs = new MapAttrs(dtl.getNo());
					for (MapAttr attr : dtlAttrs.ToJavaList()) {
						// 处理它的默认值.
						if (attr.getDefValReal().contains("@") == false) {
							continue;
						}

						for (DataRow dr : dtDtl.Rows) {
							dr.setValue(attr.getKeyOfEn(), attr.getDefVal());
						}
					}

					dtDtl.TableName = dtl.getNo(); // 修改明细表的名称.
					myds.Tables.add(dtDtl); // 加入这个明细表, 如果没有数据，xml体现为空.
				}
			}
			// /#region 把外键表加入 DataSet

			// sql =
			// "@SELECT UIVisible,UIIsEnable,FK_MapData,MyPK,KeyOfEn,Name,DefVal,UIContralType,MyDataType,LGType,X,Y,UIBindKey,UIWidth,UIHeight "
			// + " FROM Sys_MapAttr WHERE " + where +
			// " AND KeyOfEn NOT IN('WFState') ORDER BY FK_MapData,IDX ";

			// DataTable dtMapAttr = DBAccess.RunSQLReturnTable("");
			// md.GenerHisDataSet().Tables["Sys_MapAttr"];

			DataTable dtMapAttr = md.GenerHisDataSet().Tables.get(md
					.GenerHisDataSet().Tables.indexOf("Sys_MapAttr"));

			for (DataRow dr : dtMapAttr.Rows) {
				String lgType = dr.getValue("LGType").toString();
				if (!lgType.equals("2")) {
					continue;
				}

				String UIIsEnable = dr.getValue("UIIsEnable").toString();
				if (UIIsEnable.equals("0")) {
					continue;
				}

				String uiBindKey = dr.getValue("UIBindKey").toString();
				if (StringHelper.isNullOrEmpty(uiBindKey) == true) {
					String myPK = dr.getValue("MyPK").toString();
					// 如果是空的
					throw new RuntimeException("@属性字段数据不完整，流程:" + fk_flow
							+ ",节点:" + nd.getNodeID() + nd.getName() + ",属性:"
							+ myPK + ",的UIBindKey IsNull ");
				}

				// 判断是否存在.
				if (myds.Tables.contains(uiBindKey) == true) {
					continue;
				}

				myds.Tables.add(BP.Sys.PubClass
						.GetDataTableByUIBineKey(uiBindKey));
			}
			// /#region 把流程信息放入里面.
			// 把流程信息表发送过去.
			GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.setWorkID(workID);
			gwf.RetrieveFromDBSources();

			myds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));

			if (gwf.getWFState() == WFState.Shift) {
				// 如果是转发.
				ShiftWorks fws = new ShiftWorks();
				fws.Retrieve(ShiftWorkAttr.WorkID, workID,
						ShiftWorkAttr.FK_Node, fk_node);
				myds.Tables.add(fws.ToDataTableField("WF_ShiftWork"));
			}

			if (gwf.getWFState() == WFState.ReturnSta) {
				// 如果是退回.
				ReturnWorks rts = new ReturnWorks();
				rts.Retrieve(ReturnWorkAttr.WorkID, workID,
						ReturnWorkAttr.ReturnToNode, fk_node,
						ReturnWorkAttr.RDT);
				myds.Tables.add(rts.ToDataTableField("WF_ReturnWork"));
			}

			if (gwf.getWFState() == WFState.HungUp) {
				// 如果是挂起.
				HungUps hups = new HungUps();
				hups.Retrieve(HungUpAttr.WorkID, workID, HungUpAttr.FK_Node,
						fk_node);
				myds.Tables.add(hups.ToDataTableField("WF_HungUp"));
			}
			long wfid = workID;
			if (fid != 0) {
				wfid = fid;
			}

			// 放入track信息.
			Paras ps = new Paras();
			ps.SQL = "SELECT * FROM ND" + Integer.parseInt(fk_flow)
					+ "Track WHERE WorkID="
					+ BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID";
			ps.Add("WorkID", wfid);
			DataTable dtNode = DBAccess.RunSQLReturnTable(ps);
			dtNode.TableName = "Track";
			myds.Tables.add(dtNode);

			// 工作人员列表，用于审核组件.
			ps = new Paras();
			ps.SQL = "SELECT * FROM  WF_GenerWorkerlist WHERE WorkID="
					+ BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID";
			ps.Add("WorkID", wfid);
			DataTable dtGenerWorkerlist = DBAccess.RunSQLReturnTable(ps);
			dtGenerWorkerlist.TableName = "WF_GenerWorkerlist";
			myds.Tables.add(dtGenerWorkerlist);

			if (dtGenerWorkerlist.Rows.size() != 0
					&& nd.getIsStartNode() == false && iscc == false) {
				for (DataRow dr : dtGenerWorkerlist.Rows) {
					if (dr.getValue(GenerWorkerListAttr.IsRead).toString()
							.equals("1")
							&& dr.getValue(GenerWorkerListAttr.FK_Emp)
									.toString().equals(WebUser.getNo())) {
						Dev2Interface.Node_SetWorkRead(fk_node, workID);
						break;
					}
				}
			}

			// 放入CCList信息. 用于审核组件.
			ps = new Paras();
			ps.SQL = "SELECT * FROM WF_CCList WHERE WorkID="
					+ BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID";
			ps.Add("WorkID", wfid);
			DataTable dtCCList = DBAccess.RunSQLReturnTable(ps);
			dtCCList.TableName = "WF_CCList";
			myds.Tables.add(dtCCList);

			// 放入WF_SelectAccper信息. 用于审核组件.
			ps = new Paras();
			ps.SQL = "SELECT * FROM WF_SelectAccper WHERE WorkID="
					+ BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID";
			ps.Add("WorkID", wfid);
			DataTable dtSelectAccper = DBAccess.RunSQLReturnTable(ps);
			dtSelectAccper.TableName = "WF_SelectAccper";
			myds.Tables.add(dtSelectAccper);

			// /#endregion 把流程信息放入里面.

			// myds.WriteXml("c:\\22xxx.xml", XmlWriteMode.IgnoreSchema);
			// BP.DA.DataType.WriteFile( "c:\\ss.xml",

			return myds;
		} catch (RuntimeException ex) {
			Log.DebugWriteError(ConvertTools.getStackTraceString(ex
					.getStackTrace()));
			throw new RuntimeException("Para:FK_Node=" + fk_node + ",workid:"
					+ workID + ",UserNo  Ext:" + ex.getMessage());
		}
	}

	// 小周鹏修改-------------------------------START
	/**
	 * 获取模板文件
	 * 
	 * @param fk_flow
	 * @return
	 */
	public String GetFlowTemplete(String fk_flow, String fk_node, String ver)
	// public string GetFlowTemplete(string fk_flow, string ver)
	{

		String resultJson = "";

		Flow fl = new Flow(fk_flow);
		String path = BP.Sys.SystemConfig.getPathOfDataUser() + "/FlowDesc/"
				+ fl.getNo() + "." + fl.getName();

		// string fileName = path+ "\\Flow.xml";
		Node node = new Node(fk_node);
		String fileName = path + "/" + fk_node + "." + node.getName() + ".xml";
		// 小周鹏修改-------------------------------END
		File file = new File(path);
		if (file.exists() == false) {
			// 如果不存在, 就要生成他。
			DataSet dstemp = fl.GetFlow(path);
			dstemp.WriteXml(fileName);
			resultJson = FormatToJson.ToJson(dstemp);
		}

		DataSet ds = new DataSet();
		ds.readXml(fileName);
		DataTable dtFlow = ds.Tables.get(ds.Tables.indexOf("WF_Flow"));
		if (!dtFlow.Rows.get(0).getValue("Ver").toString().equals(ver)) {
			// 如果不存在, 就要生成他。
			DataSet dstemp = fl.GetFlow(path);
			dstemp.WriteXml(fileName);
			resultJson = FormatToJson.ToJson(dstemp);
		} else {
			resultJson = FormatToJson.ToJson(ds);
		}

		return resultJson;
	}

	// *---小周鹏添加 2014-09-13---START*
	/**
	 * 获取选择下一节点数据源
	 * 
	 * @param userNo
	 *            用户
	 * @param sid
	 *            安全码
	 * @param fk_flow
	 *            工作流程
	 * @param fk_node
	 *            节点编号
	 * @param workID
	 *            工作ID
	 * @return string的json
	 */

	public String WorkOpt_GetToNodes(String userNo, String sid, String fk_flow,
			int fk_node, long workID, long fid) {
		this.LetUserLogin(userNo, sid);

		Nodes nodes = Dev2Interface.WorkOpt_GetToNodes(fk_flow, fk_node,
				workID, fid);

		return BP.Tools.Entitis2Json.ConvertEntities2ListJson(nodes);
	}

	/**
	 * 选择下一节点发送
	 * 
	 * @param userNo
	 *            用户
	 * @param sid
	 *            安全码
	 * @param fk_flow
	 *            工作编号
	 * @param fk_node
	 *            节点编号
	 * @param workID
	 *            工作ID
	 * @param fid
	 *            流程ID
	 * @param to_node
	 *            到达的节点
	 * @return 发送结果
	 */
	public String WorkOpt_SendToNodes(String userNo, String sid,
			String fk_flow, int fk_node, long workID, long fid, String to_node) {
		this.LetUserLogin(userNo, sid);

		// 执行发送.
		String msg = "";
		try {
			msg = Dev2Interface.WorkOpt_SendToNodes(fk_flow, fk_node, workID,
					fid, to_node).ToMsgOfText();
		} catch (RuntimeException ex) {
			msg = "发送出现错误:" + ex.getMessage();
		}

		return msg;
	}

	// / <summary>
	// / 让他登录
	// / </summary>
	// / <param name="user"></param>
	// / <param name="sid"></param>
	public void LetUserLogin(String user, String sid) {
		BP.Port.Emp emp = new BP.Port.Emp(user);
		WebUser.SignInOfGener(emp);
	}

	/**
	 * 获得接收人的数据源
	 * 
	 * @param userNo
	 *            用户
	 * @param sid
	 *            安全码
	 * @param fk_flow
	 *            工作编号
	 * @param fk_node
	 *            节点编号
	 * @param workID
	 *            工作ID
	 * @param fid
	 *            流程ID
	 * @return 接收人的Json数据
	 */

	public String WorkOpt_AccepterDB(String userNo, String sid, String fk_flow,
			int fk_node, long workID, long fid) {
		try {
			this.LetUserLogin(userNo, sid);

			// 获取接收人DataSet
			DataSet ds = Dev2Interface.WorkOpt_AccepterDB(//fk_flow,
					fk_node, workID, fid);

			return BP.Tools.FormatToJson.ToJson(ds);
		} catch (RuntimeException ex) {
			return "err:" + ex.getMessage();
		}
	}

	/**
	 * 设置指定的节点接受人
	 * 
	 * @param userNo
	 *            用户
	 * @param sid
	 *            安全码
	 * @param fk_node
	 *            节点ID
	 * @param workID
	 *            工作ID
	 * @param fid
	 *            流程ID
	 * @param emps
	 *            指定的人员集合zhangsan,lisi,wangwu
	 * @param isNextTime
	 *            是否下次自动设置
	 */

	public String WorkOpt_SetAccepter(String userNo, String sid, int fk_node,
			long workID, long fid, String emps, boolean isNextTime) {
		this.LetUserLogin(userNo, sid);

		Dev2Interface.WorkOpt_SetAccepter(fk_node, workID, fid, emps,
				isNextTime);

		return "接受人员设置成功！";
	}

	// *---小周鹏添加 2014-09-15---END*

	// *---小周鹏添加 2014-09-17---START*
	/**
	 * 删除流程
	 * 
	 * @param userNo
	 *            用户
	 * @param sid
	 *            安全码
	 * @param fk_flow
	 *            流程编号
	 * @param workID
	 *            工作ID
	 * @param isDelSubFlow
	 *            是否要删除它的子流程
	 */

	public String Flow_DoDeleteFlowByReal(String userNo, String sid,
			String fk_flow, long workID, boolean isDelSubFlow) {
		this.LetUserLogin(userNo, sid);

		Dev2Interface.Flow_DoDeleteFlowByReal(fk_flow, workID, isDelSubFlow);

		return "删除成功！";
	}

	// *---小周鹏添加 2014-09-17---END*

	// *---小周鹏添加 2014-09-19---START*
	/**
	 * 回复加签内容
	 * 
	 * @param userNo
	 *            用户
	 * @param sid
	 *            安全码
	 * @param fk_flow
	 *            流程编号
	 * @param fk_node
	 *            节点编号
	 * @param workID
	 *            工作ID
	 * @param fid
	 *            FID
	 * @param replyNote
	 *            答复信息
	 * @return 答复结果
	 */

	public String Node_AskforReply(String userNo, String sid, String fk_flow,
			int fk_node, long workID, long fid, String replyNote) {
		this.LetUserLogin(userNo, sid);

		String info = Dev2Interface.Node_AskforReply(fk_flow, fk_node, workID,
				fid, replyNote);

		return info;
	}

	// *---小周鹏添加 2014-09-19---END*

	private DataSet GenerWorkNode_FlowDataOnly(String fk_flow, int fk_node,
			long workID, long fid, String userNo) {
		if (fk_node == 0) {
			fk_node = Integer.parseInt(fk_flow + "01");
		}

		try {
			Emp emp = new Emp(userNo);
			WebUser.SignInOfGener(emp);

			DataSet myds = new DataSet();

			// 节点
			Node nd = new Node(fk_node);

			// /#region 把主从表数据放入里面.
			// .工作数据放里面去, 放进去前执行一次装载前填充事件.
			Work wk = nd.getHisWork();
			wk.setOID(workID);
			wk.RetrieveFromDBSources();
			wk.ResetDefaultVal();

			// /#region 设置默认值
			MapAttrs mattrs = nd.getMapData().getMapAttrs();
			for (MapAttr attr : mattrs.ToJavaList()) {
				if (attr.getUIIsEnable()) {
					continue;
				}

				if (attr.getDefValReal().contains("@") == false) {
					continue;
				}

				wk.SetValByKey(attr.getKeyOfEn(), attr.getDefVal());
			}

			// /#endregion 设置默认值。

			// 描述.
			MapData md = new MapData("ND" + fk_node);

			// 执行一次装载前填充.
			String msg = md.getFrmEvents().DoEventNode(
					FrmEventList.FrmLoadBefore, wk);
			if (StringHelper.isNullOrEmpty(msg) == false) {
				throw new RuntimeException("错误:" + msg);
			}

			myds.Tables.add(wk.ToDataTableField(md.getNo()));
			if (md.getMapDtls().size() > 0) {
				for (MapDtl dtl : md.getMapDtls().ToJavaList()) {
					GEDtls dtls = new GEDtls(dtl.getNo());
					QueryObject qo = null;
					try {
						qo = new QueryObject(dtls);
						switch (dtl.getDtlOpenType()) {
						case ForEmp: // 按人员来控制.
							qo.AddWhere(GEDtlAttr.RefPK, workID);
							qo.addAnd();
							qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
							break;
						case ForWorkID: // 按工作ID来控制
							qo.AddWhere(GEDtlAttr.RefPK, workID);
							break;
						case ForFID: // 按流程ID来控制.
							qo.AddWhere(GEDtlAttr.FID, workID);
							break;
						}
					} catch (java.lang.Exception e) {
						dtls.getGetNewEntity().CheckPhysicsTable();
					}
					DataTable dtDtl = qo.DoQueryToTable();

					// 为明细表设置默认值.
					MapAttrs dtlAttrs = new MapAttrs(dtl.getNo());
					for (MapAttr attr : dtlAttrs.ToJavaList()) {
						// 处理它的默认值.
						if (attr.getDefValReal().contains("@") == false) {
							continue;
						}

						for (DataRow dr : dtDtl.Rows) {
							dr.setValue(attr.getKeyOfEn(), attr.getDefVal());
						}
					}

					dtDtl.TableName = dtl.getNo(); // 修改明细表的名称.
					myds.Tables.add(dtDtl); // 加入这个明细表, 如果没有数据，xml体现为空.
				}
			}

			// /#endregion

			// /#region 把外键表加入DataSet
			DataTable dtMapAttr = myds.Tables.get(myds.Tables
					.indexOf("Sys_MapAttr"));
			for (DataRow dr : dtMapAttr.Rows) {
				String lgType = dr.getValue("LGType").toString();
				if (!lgType.equals("2")) {
					continue;
				}

				String UIIsEnable = dr.getValue("UIIsEnable").toString();
				if (UIIsEnable.equals("0")) {
					continue;
				}

				String uiBindKey = dr.getValue("UIBindKey").toString();

				if (StringHelper.isNullOrEmpty(uiBindKey)) {
					String myPK = dr.getValue("MyPK").toString();
					// 如果是空的
					throw new RuntimeException("@属性字段数据不完整，流程:"
							+ nd.getFK_Flow() + nd.getFlowName() + ",节点:"
							+ nd.getNodeID() + nd.getName() + ",属性:" + myPK
							+ ",的UIBindKey IsNull ");
				}

				// 判断是否存在.
				if (myds.Tables.contains(uiBindKey) == true) {
					continue;
				}

				myds.Tables.add(BP.Sys.PubClass
						.GetDataTableByUIBineKey(uiBindKey));
			}

			// /#endregion End把外键表加入DataSet

			// /#region 把流程信息放入里面.
			// 把流程信息表发送过去.
			GenerWorkFlow gwf = new GenerWorkFlow();
			gwf.setWorkID(workID);
			myds.Tables.add(gwf.ToDataTableField("WF_GenerWorkFlow"));

			if (gwf.getWFState() == WFState.Shift) {
				// 如果是转发.
				ShiftWorks fws = new ShiftWorks();
				fws.Retrieve(ShiftWorkAttr.WorkID, workID,
						ShiftWorkAttr.FK_Node, fk_node);
				myds.Tables.add(fws.ToDataTableField("WF_ShiftWork"));
			}

			if (gwf.getWFState() == WFState.ReturnSta) {
				// 如果是退回.
				ReturnWorks rts = new ReturnWorks();
				rts.Retrieve(ReturnWorkAttr.WorkID, workID,
						ReturnWorkAttr.ReturnToNode, fk_node);
				myds.Tables.add(rts.ToDataTableField("WF_ShiftWork"));
			}

			if (gwf.getWFState() == WFState.HungUp) {
				// 如果是挂起.
				HungUps hups = new HungUps();
				hups.Retrieve(HungUpAttr.WorkID, workID, HungUpAttr.FK_Node,
						fk_node);
				myds.Tables.add(hups.ToDataTableField("WF_HungUp"));
			}

			// 放入track信息.
			Paras ps = new Paras();
			ps.SQL = "SELECT * FROM ND" + Integer.parseInt(fk_flow)
					+ "Track WHERE WorkID="
					+ BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID";
			ps.Add("WorkID", workID);
			DataTable dtNode = DBAccess.RunSQLReturnTable(ps);
			dtNode.TableName = "Track";
			myds.Tables.add(dtNode);

			// /#endregion 把流程信息放入里面.

			myds.WriteXml("c:\\GenerWorkNode_FlowDataOnly.xml");
			return myds;
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex.getMessage());
			// return "@生成工作FK_Flow=" + fk_flow + ",FK_Node=" + fk_node +
			// ",WorkID=" + workID + ",FID=" + fid + "错误,错误信息:" + ex.Message;
		}
	}

	/**
	 * @param fk_flow
	 * @param fk_node
	 * @param workID
	 * @param fid
	 * @param userNo
	 * @return
	 */

	public String GenerFlowTemplete_Json(String fk_flow) {
		Flow fl = new Flow(fk_flow);

		String path = BP.Sys.SystemConfig.getPathOfDataUser() + "FlowDesc/";

		DataSet myds = fl.DoExpFlowXmlTemplete(path);
		myds.WriteXml("c:\\GenerFlowTemplete_Json.xml");

		String strs = BP.Tools.FormatToJson.ToJson(myds);
		DataType.WriteFile("c:\\GenerFlowTemplete_Json.txt", strs);
		return strs;
	}

	/**
	 * 获取一条待办工作
	 * 
	 * @param fk_flow
	 *            工作编号
	 * @param fk_node
	 *            节点编号
	 * @param workID
	 *            工作ID
	 * @param userNo
	 *            操作员编号
	 * @return
	 */

	public String Node_SaveWork(String fk_flow, int fk_node, long workID,
			String userNo, String dsXml) {
		try {
			Emp emp = new Emp(userNo);
			WebUser.SignInOfGener(emp);

			DataSet ds = new DataSet();
			ds.readXml(dsXml);
			java.util.Hashtable htMain = new java.util.Hashtable();
			DataTable dtMain = ds.Tables.get(ds.Tables.indexOf("ND" + fk_node)); // 获得约定的主表数据.
			for (DataRow dr : dtMain.Rows) {
				htMain.put(dr.getValue(0).toString(), dr.getValue(1).toString());
			}
			return Dev2Interface.Node_SaveWork(fk_flow, fk_node, workID,
					htMain, null);
		} catch (RuntimeException ex) {
			return "@保存工作出现错误:" + ex.getMessage();
		}
	}

	/**
	 * 保存数据
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param fk_node
	 *            节点编号
	 * @param workID
	 *            工作ID
	 * @param FID
	 *            FID
	 * @param userNo
	 *            用户
	 * @param sid
	 *            验证码
	 * @param jsonStr
	 *            json
	 * @return 返回执行结果
	 */

	public String Node_SaveWork_Json(String fk_flow, int fk_node, long workID,
			long fid, String userNo, String sid, String jsonStr) {
		try {
			if (!userNo.equals(WebUser.getNo())) {
				Emp emp = new Emp(userNo);
				WebUser.SignInOfGener(emp);
			}

			// /#region 此部分代码与 send 相同.
			// 接受数据.
			DataSet ds = BP.Tools.FormatToJson.JsonToDataSet(jsonStr);

			// 求出主表数据.
			String frm = "ND" + fk_node;
			DataTable dtMain = ds.Tables.get(ds.Tables.indexOf(frm));
			java.util.Hashtable htMain = new java.util.Hashtable();
			for (DataColumn dc : dtMain.Columns) {
				htMain.put(dc.ColumnName,
						dtMain.Rows.get(0).getValue(dc.ColumnName).toString());
			}

			// 判断是否有审核数据表.
			if (ds.Tables.contains("FrmCheck")) {
				DataTable dtfrm = ds.Tables.get(ds.Tables.indexOf("FrmCheck"));
				String note = (String) ((dtfrm.Rows.get(0).getValue(0) instanceof String) ? dtfrm.Rows
						.get(0).getValue(0) : null);
				String opName = (String) ((dtfrm.Rows.get(0).getValue(1) instanceof String) ? dtfrm.Rows
						.get(0).getValue(1) : null);
				if (note != null) {
					Dev2Interface.WriteTrackWorkCheck(fk_flow, fk_node, workID,
							fid, note, opName);
				}
			}

			// /#endregion 此部分代码与 send 相同.

			// 执行保存.
			Dev2Interface.Node_SaveWork(fk_flow, fk_node, workID, htMain, ds);

			// 把保存后的主表从表数据返回过去，有可能导致业务计算需要显示新的数据.

			// /#region 保存数据后仅仅返回主从表数据。
			// 节点
			Node nd = new Node(fk_node);

			// 定义数据容器.
			DataSet myds = new DataSet();

			// 把主从表数据放入里面.
			Work wk = nd.getHisWork();
			wk.setOID(workID);
			QueryObject qoEn = new QueryObject(wk);
			qoEn.AddWhere("OID", workID);
			dtMain = qoEn.DoQueryToTable(1); // wk.ToDataTableField("ND" +
												// fk_node);
			dtMain.TableName = "ND" + fk_node;
			myds.Tables.add(dtMain);

			// 描述.n
			MapData md = new MapData("ND" + fk_node);
			if (md.getMapDtls().size() > 0) {
				ArrayList<MapDtl> mds = MapDtls.convertMapDtls(md.getMapDtls());
				for (MapDtl dtl : mds) {
					GEDtls dtls = new GEDtls(dtl.getNo());
					QueryObject qo = null;
					try {
						qo = new QueryObject(dtls);
						switch (dtl.getDtlOpenType()) {
						case ForEmp: // 按人员来控制.
							qo.AddWhere(GEDtlAttr.RefPK, workID);
							qo.addAnd();
							qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
							break;
						case ForWorkID: // 按工作ID来控制
							qo.AddWhere(GEDtlAttr.RefPK, workID);
							break;
						case ForFID: // 按流程ID来控制.
							qo.AddWhere(GEDtlAttr.FID, workID);
							break;
						}
					} catch (java.lang.Exception e) {
						dtls.getGetNewEntity().CheckPhysicsTable();
					}
					DataTable dtDtl = qo.DoQueryToTable();

					dtDtl.TableName = dtl.getNo(); // 修改明细表的名称.
					myds.Tables.add(dtDtl); // 加入这个明细表, 如果没有数据，xml体现为空.
				}
			}

			// /#endregion

			// 返回保存后的数据, 因为保存前后，需要执行事件，执行后就要发生数据的变化。
			// return BP.DA.DataTableConvertJson.Dataset2Json(myds);
			return FormatToJson.ToJson(myds);
		} catch (RuntimeException ex) {
			return "@保存工作出现错误:" + ex.getMessage();
		}
	}

	/**
	 * 执行发送
	 * 
	 * @param fk_flow
	 *            流程编号
	 * @param fk_node
	 *            节点编号
	 * @param workID
	 *            工作ID
	 * @param fid
	 *            流程ID
	 * @param userNo
	 *            操作员
	 * @param sid
	 *            sid
	 * @param jsonStr
	 *            json
	 * @return 发送执行信息
	 */

	public String Node_SendWork_Json(String fk_flow, int fk_node, long workID,
			long fid, String userNo, String sid, String jsonStr) {
		this.LetUserLogin(userNo, sid);
		try {
			SendReturnObjs objs = null;
			if (jsonStr != null) {

				// /#region 此部分代码与 send 相同.
				// 接受数据.
				DataSet ds = BP.Tools.FormatToJson.JsonToDataSet(jsonStr);

				// 求出主表数据.
				String frm = "ND" + fk_node;
				DataTable dtMain = ds.Tables.get(ds.Tables.indexOf(frm));
				java.util.Hashtable htMain = new java.util.Hashtable();
				for (DataColumn dc : dtMain.Columns) {
					htMain.put(dc.ColumnName,
							dtMain.Rows.get(0).getValue(dc.ColumnName)
									.toString());
				}

				// 判断是否有审核数据表.
				// 判断是否有审核数据表.
				if (ds.Tables.contains("FrmCheck")) {
					DataTable dtfrm = ds.Tables.get(ds.Tables
							.indexOf("FrmCheck"));
					String note = (String) ((dtfrm.Rows.get(0).getValue(0) instanceof String) ? dtfrm.Rows
							.get(0).getValue(0) : null);
					String opName = (String) ((dtfrm.Rows.get(0).getValue(1) instanceof String) ? dtfrm.Rows
							.get(0).getValue(1) : null);
					if (note != null) {
						Dev2Interface.WriteTrackWorkCheck(fk_flow, fk_node,
								workID, fid, note, opName);
					}
				}

				// /#endregion 此部分代码与 send 相同.

				// 执行发送.
				objs = Dev2Interface.Node_SendWork(fk_flow, workID, htMain, ds);
			} else {
				objs = Dev2Interface.Node_SendWork(fk_flow, workID, null, null);
			}
			return objs.ToMsgOfText();
		} catch (RuntimeException ex) {
			return "@发送工作出现错误:" + ex.getMessage();
		}
	}

	/**
	 * 执行发送
	 * 
	 * @param fk_flow
	 * @param fk_node
	 * @param workID
	 * @param dsXml
	 * @return
	 */

	public String Node_SendWork(String fk_flow, int fk_node, long workID,
			String dsXml, String currUserNo) {
		try {
			if (!currUserNo.equals(WebUser.getNo())) {
				Dev2Interface.Port_Login(currUserNo);
			}

			SendReturnObjs objs = null;
			if (dsXml != null) {
				// StringReader sr = new StringReader(dsXml);
				DataSet ds = new DataSet();
				ds.readXml(dsXml);
				ds.WriteXml("c:\\GenerSendXml.xml");

				java.util.Hashtable htMain = new java.util.Hashtable();
				DataTable dtMain = ds.Tables.get(ds.Tables.indexOf("ND"
						+ fk_node));
				for (DataRow dr : dtMain.Rows) {
					htMain.put(dr.getValue(0).toString(), dr.getValue(1)
							.toString());
				}
				objs = Dev2Interface.Node_SendWork(fk_flow, workID, htMain, ds);
			} else {
				objs = Dev2Interface.Node_SendWork(fk_flow, workID, null, null);
			}
			return objs.ToMsgOfText();
		} catch (RuntimeException ex) {
			return "err@发送工作出现错误:" + ex.getMessage();
		}
	}

	/**
	 * 执行加签
	 * 
	 * @param userNo
	 *            当前登录人
	 * @param sid
	 *            校验码
	 * @param workID
	 *            工作ID
	 * @param _askforHelpSta
	 *            @5=加签后直接发送@6=加签后由我直接发送
	 * @param toEmpNo
	 *            加签人
	 * @param note
	 *            信息
	 * @return 执行结果
	 */

	public String Node_Askfor(String userNo, String sid, long workID,
			int _askforHelpSta, String toEmpNo, String note) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}

		AskforHelpSta sta = AskforHelpSta.forValue(_askforHelpSta);
		return Dev2Interface.Node_Askfor(workID, sta, toEmpNo, note);
	}

	// [WebMethod]
	public String GetNoName(String SQL) {
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(SQL);
		DataSet ds = new DataSet();
		ds.Tables.add(dt);
		return DataType.ToJson(ds.Tables.get(0));
	}

	/**
	 * 大文件上传
	 * 
	 * @param fileName
	 *            上传文件名
	 * @param offSet
	 *            偏移
	 * @param intoBuffer
	 *            每次上传字节数组 单位KB
	 * @return 上传是否成功
	 */

	// [WebMethod]
	public boolean Upload(String fileName, long offSet, byte[] intoBuffer) {
		// 指定上传文件夹+文件名(相对路径)
		String strPath = "D:\\value-added\\CCFlow\\DataUser\\UploadFile\\"
				+ fileName;
		// 将相对路径转换成服务器的绝对路径
		// strPath = Server.MapPath(strPath);

		// if (offSet < 0)
		// {
		// offSet = 0;
		// }

		byte[] buffer = intoBuffer;

		if (buffer != null) {
			// 读写文件的文件流,支持同步读写也支持异步读写
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(
						new File(strPath));
				fileOutputStream.write(buffer, 0, buffer.length);
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	// [WebMethod]
	public String ParseExp(String strExp) {
		DataTable dt = DBAccess.RunSQLReturnTable("select " + strExp);
		if (dt != null && dt.Rows.size() > 0) {
			return dt.Rows.get(0).getValue(0).toString();
		}
		return "";
	}

	// /#region 获得审核信息. edity by xiaozhoupeng. 2014-07-26
	/**
	 * 获得审核信息
	 * 
	 * @param userNo
	 *            当前操作员编号
	 * @param sid
	 *            SID
	 * @param fk_flow
	 *            流程编号
	 * @param fk_node
	 *            节点编号
	 * @param workID
	 *            工作ID
	 * @return 审核信息
	 */

	public String GetCheckInfo(String userNo, String sid, String fk_flow,
			int fk_node, long workID) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}

		return Dev2Interface.GetCheckInfo(fk_flow, workID, fk_node);
	}

	/**
	 * 写入工作审核日志:
	 * 
	 * @param flowNo
	 *            流程编号
	 * @param nodeID
	 *            节点从
	 * @param workid
	 *            工作ID
	 * @param FID
	 *            FID
	 * @param msg
	 *            审核信息
	 * @param optionName
	 *            操作名称(比如:科长审核、部门经理审批),如果为空就是"审核".
	 */

	public String WriteTrackWorkCheck(String userNo, String sid, String flowNo,
			int nodeFrom, long workid, long fid, String msg, String optionName) {
		if (!userNo.equals(WebUser.getNo())) {
			Dev2Interface.Port_Login(userNo);
		}

		Dev2Interface.WriteTrackWorkCheck(flowNo, nodeFrom, workid, fid, msg,
				optionName);

		// 设置审核完成.
		Dev2Interface.Node_CC_SetSta(nodeFrom, workid, WebUser.getNo(),
				CCSta.CheckOver);

		return "审核成功！";
	}

	// /#endregion 获得审核信息.

	// /#region 与表单相关的api.
	/**
	 * 删除附件
	 * 
	 * @param userNo
	 * @param sid
	 * @param mypk
	 * @return
	 */
	public String CCFrom_DelFrmAttachment(String userNo, String sid, String mypk) {
		FrmAttachmentDB db = new FrmAttachmentDB();
		db.setMyPK(mypk);
		db.Delete();
		return "删除成功";
	}

	/**
	 * 上传附件
	 * 
	 * @param userNo
	 * @param sid
	 * @param intoBuffer
	 * @return
	 */
	public String CCForm_UploadFrmAttachment(String userNo, String sid,
			String fk_frmath, byte[] intoBuffer) {

		return "上传成功";
	}

	/**
	 * 表单全部信息
	 * 
	 * @param fk_mapdata
	 * @return
	 */

	public String CCForm_FrmTemplete(String fk_mapdata) {
		MapData md = new MapData(fk_mapdata);
		DataSet ds = md.GenerHisDataSet();
		return BP.Tools.FormatToJson.ToJson(ds);
	}

	/**
	 * 流程信息
	 * 
	 * @param fk_mapdata
	 * @return
	 */

	public String CCFlow_FlowTemplete(String fk_flow) {
		DataSet ds = new DataSet();

		String sql = "";
		sql = "SELECT * FROM WF_Flow WHERE No='" + fk_flow + "'";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_Flow";
		ds.Tables.add(dt);

		sql = "SELECT * FROM WF_Node WHERE FK_Flow='" + fk_flow + "'";
		dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_Node";
		ds.Tables.add(dt);

		return BP.Tools.FormatToJson.ToJson(ds);
	}

	// /#endregion
}

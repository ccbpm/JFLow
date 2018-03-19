package cn.jflow.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import BP.DA.DBAccess;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Log;
import BP.DA.LogType;
import BP.En.Attr;
import BP.Port.Emp;
import BP.Sys.GEDtl;
import BP.Sys.GEDtlAttr;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;
import BP.Sys.MapExtXmlList;
import BP.Sys.SystemConfig;
import BP.WF.Flow;
import BP.WF.GenerWorkFlowAttr;
import BP.WF.GenerWorkerList;
import BP.WF.GenerWorkerListAttr;
import BP.WF.MsgSta;
import BP.WF.Node;
import BP.WF.SMS;
import BP.WF.SMSAttr;
import BP.WF.SMSs;
import BP.WF.SendReturnObjs;
import BP.WF.Work;
import BP.WF.WorkNode;
import BP.WF.Template.NodeAttr;
import BP.WF.Template.Task;
import BP.WF.Template.TaskAttr;
import BP.Web.WebUser;
import cn.jflow.common.model.ScanSta;

@Service
@Lazy(false)
public class FrmMain {
	private boolean CB_IsWriteToCCIM = false;
	private boolean ISAutoNodeDTS = true;
	long AutoNodeDTSTimeSpanMinutes = 1;

	@PostConstruct
	public void run(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				//CB_IsWriteToCCIM = SystemConfig.GetValByKeyBoolen("CB_IsWriteToCCIM", false);
				CB_IsWriteToCCIM=false;						
				ISAutoNodeDTS = false; //SystemConfig.GetValByKeyBoolen("ISAutoNodeDTS", false);
				AutoNodeDTSTimeSpanMinutes =1; // SystemConfig.GetValByKeyInt("AutoNodeDTSTimeSpanMinutes", 1);
				
				Log.DefaultLogWriteLineInfo("@读取服务配置信息 \\jflow-web\\src\\main\\resources\\jflow.properties 如果数据库配置不正确，就不能启动ccbpm.");
				//Log.DefaultLogWriteLineInfo("数据库类型:"+systecon);
				
				Log.DefaultLogWriteLineInfo("[IsWriteToCCIM]"+CB_IsWriteToCCIM+"[ISAutoNodeDTS]"+ISAutoNodeDTS+"[AutoNodeDTSTimeSpanMinutes]"+AutoNodeDTSTimeSpanMinutes);
				if(ISAutoNodeDTS){
					RunIt();
				}
			}
		}).start();
	}
	
	/**
	 * 执行线程.
	 */
	public final void RunIt() {
		BP.WF.Flows fls = new BP.WF.Flows();
		fls.RetrieveAll();

		HisScanSta = ScanSta.Working;

		//最后一次调度自动工作节点的时间.
		java.util.Date dtOfAutoNode = new java.util.Date();
		boolean isFirstRun = true;
		while (true) {
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			while (this.HisScanSta == ScanSta.Pause) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				/*if (this.checkBox1.Checked) {
					Console.Beep();
				}*/
			}

			//this.SetText("********************************");
			Log.DefaultLogWriteLineInfo("********************************");

			//this.SetText("扫描触发式自动发起流程表......");
			Log.DefaultLogWriteLineInfo("扫描触发式自动发起流程表......");
			this.DoTask();

			//this.SetText("扫描定时发起流程....");
			Log.DefaultLogWriteLineInfo("扫描定时发起流程....");
			this.DoAutuFlows(fls);

			//this.SetText("扫描消息表,想外发送消息....");
			Log.DefaultLogWriteLineInfo("扫描消息表,想外发送消息....");
			this.DoSendMsg();

			//this.SetText("扫描逾期流程数据，处理逾期流程.");
			Log.DefaultLogWriteLineInfo("扫描逾期流程数据，处理逾期流程.");
			this.DoOverDueFlow();

			//获取上次的 执行自动任务的间隔. 
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date now = new Date();
			Date date = dtOfAutoNode;
			String now1 = df.format(now);
			String date1 = df.format(date);
			try {
				now = df.parse(now1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			try {
				date = df.parse(date1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			long l = now.getTime() - date.getTime();
			long day = l / (24 * 60 * 60 * 1000);
			long hour = (l / (60 * 60 * 1000) - day * 24);
			long tsAuto = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			if (tsAuto > AutoNodeDTSTimeSpanMinutes || isFirstRun == true) {
				isFirstRun = false;

				dtOfAutoNode = new java.util.Date();
				//this.SetText("检索自动节点任务....");
				Log.DefaultLogWriteLineInfo("检索自动节点任务....");
				this.DoAutoNode();
			}

			//this.SetText("向CCIM里发送消息...");
			Log.DefaultLogWriteLineInfo("向CCIM里发送消息...");
			this.DoSendMsg();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//					switch (this.toolStripStatusLabel1.Text)
			//ORIGINAL LINE: case "服务启动":
			/*if (this.toolStripStatusLabel1.getText().equals("服务启动")) {
				this.toolStripStatusLabel1.setText("服务启动..");
			}
			//ORIGINAL LINE: case "服务启动..":
			else if (this.toolStripStatusLabel1.getText().equals("服务启动..")) {
				this.toolStripStatusLabel1.setText("服务启动........");
			}
			//ORIGINAL LINE: case "服务启动....":
			else if (this.toolStripStatusLabel1.getText().equals("服务启动....")) {
				this.toolStripStatusLabel1.setText("服务启动.............");
			} else {
				this.toolStripStatusLabel1.setText("服务启动");
			}*/
		}
	}

	public final void DoTask() {
		String sql = "SELECT * FROM WF_Task WHERE TaskSta=0 ORDER BY Starter";
		DataTable dt = null;
		try {
			dt = DBAccess.RunSQLReturnTable(sql);
		} catch (java.lang.Exception e) {
			Task ta = new Task();
			ta.CheckPhysicsTable();
			dt = DBAccess.RunSQLReturnTable(sql);
		}

		if (dt.Rows.size() == 0) {
			return;
		}

		///#region 自动启动流程
		for (DataRow dr : dt.Rows) {
			String mypk = dr.getValue("MyPK").toString();//dr["MyPK"].toString();
			String taskSta = dr.getValue("TaskSta").toString();//dr["TaskSta"].toString();
			String paras = dr.getValue("Paras").toString();//dr["Paras"].toString();
			String starter = dr.getValue("Starter").toString();//dr["Starter"].toString();
			String fk_flow = dr.getValue("FK_Flow").toString();//dr["FK_Flow"].toString();

			String startDT = dr.getValue(TaskAttr.StartDT).toString();//dr[TaskAttr.StartDT].toString();
			if (isNullOrEmpty(startDT) == false) {
				//如果设置了发起时间,就检查当前时间是否与现在的时间匹配.
				if (getDateStr("yyyy-MM-dd HH:mm").contains(startDT) == false) {
					continue;
				}
			}

			Flow fl = new Flow(fk_flow);
			//this.SetText("开始执行(" + starter + ")发起(" + fl.getName() + ")流程.");
			Log.DefaultLogWriteLineInfo("开始执行(" + starter + ")发起(" + fl.getName() + ")流程.");
			try {
				String fTable = "ND" + fl.getNo() + "01";
				MapData md = new MapData(fTable);
				sql = "";
				//   sql = "SELECT * FROM " + md.PTable + " WHERE MainPK='" + mypk + "' AND WFState=1";
				try {
					if (DBAccess.RunSQLReturnTable(sql).Rows.size() != 0) {
						continue;
					}
				} catch (java.lang.Exception e2) {
					//this.SetText("开始节点表单表:" + fTable + "没有设置的默认字段MainPK. " + sql);
					Log.DefaultLogWriteLineInfo("开始节点表单表:" + fTable + "没有设置的默认字段MainPK. " + sql);
					continue;
				}

				if (!starter.equals(BP.Web.WebUser.getNo())) {
					BP.Web.WebUser.Exit();
					BP.Port.Emp empadmin = new BP.Port.Emp(starter);
					BP.Web.WebUser.SignInOfGener(empadmin);
				}

				Work wk = fl.NewWork();
				String[] strs = paras.split("@");
				for (String str : strs) {
					if (isNullOrEmpty(str)) {
						continue;
					}

					if (str.contains("=") == false) {
						continue;
					}

					String[] kv = str.split("=");
					wk.SetValByKey(kv[0], kv[1]);
				}

				wk.SetValByKey("MainPK", mypk);
				wk.Update();

				WorkNode wn = new WorkNode(wk, fl.getHisStartNode());
				String msg = wn.NodeSend().ToMsgOfText();
				msg = msg.replace("'", "~");
				DBAccess.RunSQL("UPDATE WF_Task SET TaskSta=1,Msg='" + msg + "' WHERE MyPK='" + mypk + "'");
			} catch (RuntimeException ex) {
				//如果发送错误。
				//this.SetText(ex.getMessage());
				Log.DefaultLogWriteLineError(ex.getMessage());
				String msg = ex.getMessage();
				try {
					DBAccess.RunSQL("UPDATE WF_Task SET TaskSta=2,Msg='" + msg + "' WHERE MyPK='" + mypk + "'");
				} catch (java.lang.Exception e3) {
					Task TK = new Task();
					TK.CheckPhysicsTable();
				}
			}
		}
	}

	///#endregion 自动启动流程

	private void DoAutuFlows(BP.WF.Flows fls) {
		///#region 自动启动流程
		for (BP.WF.Flow fl : fls.ToJavaList()) {
			if (fl.getHisFlowRunWay().equals(BP.WF.FlowRunWay.HandWork)) {
				continue;
			}

			if (getDateStr("HH:mm").equals(fl.Tag)) {
				continue;
			}

			if (fl.getRunObj() == null || fl.getRunObj().equals("")) {
				String msg = "您设置自动运行流程错误，没有设置流程内容，流程编号：" + fl.getNo() + ",流程名称:" + fl.getName();
				//this.SetText(msg);
				Log.DefaultLogWriteLineError(msg);
				continue;
			}

			///#region 判断当前时间是否可以运行它。
			String[] strs = fl.getRunObj().split("@"); //破开时间串。
			boolean IsCanRun = false;
			for (String str : strs) {
				if (isNullOrEmpty(str)) {
					continue;
				}
				if (getDateStr("yyyy-MM-dd,HH:mm").contains(str)) {
					IsCanRun = true;
				}
			}

			if (IsCanRun == false) {
				continue;
			}

			// 设置时间.
			fl.Tag = getDateStr("HH:mm");
			///#endregion 判断当前时间是否可以运行它。

			// 以此用户进入.
			switch (fl.getHisFlowRunWay()) {
			case SpecEmp: //指定人员按时运行。
				String RunObj = fl.getRunObj();
				String fk_emp = RunObj.substring(0, RunObj.indexOf('@'));

				BP.Port.Emp emp = new BP.Port.Emp();
				emp.setNo(fk_emp);
				if (emp.RetrieveFromDBSources() == 0) {
					//this.SetText("启动自动启动流程错误：发起人(" + fk_emp + ")不存在。");
					Log.DefaultLogWriteLineError("启动自动启动流程错误：发起人(" + fk_emp + ")不存在。");
					continue;
				}

				try {
					//让 userNo 登录.
					BP.WF.Dev2Interface.Port_Login(emp.getNo());

					//创建空白工作, 发起开始节点.
					long workID = BP.WF.Dev2Interface.Node_CreateBlankWork(fl.getNo());

					//执行发送.
					SendReturnObjs objs = BP.WF.Dev2Interface.Node_SendWork(fl.getNo(), workID);
					//string info_send= BP.WF.Dev2Interface.Node_StartWork(fl.No,);
					//this.SetText("流程:" + fl.No + fl.getName() + "的定时任务\t\n -------------- \t\n" + objs.ToMsgOfText());
					Log.DefaultLogWriteLineInfo("流程:" + fl.getNo() + fl.getName() + "的定时任务\t\n -------------- \t\n" + objs.ToMsgOfText());
				} catch (RuntimeException ex) {
					//this.SetText("流程:" + fl.getNo() + fl.getName() + "自动发起错误:\t\n -------------- \t\n" + ex.getMessage());
					Log.DefaultLogWriteLineError("流程:" + fl.getNo() + fl.getName() + "自动发起错误:\t\n -------------- \t\n" + ex.getMessage());
				}
				continue;
			case DataModel: //按数据集合驱动的模式执行。
				//this.SetText("@开始执行数据驱动流程调度:" + fl.getName());
				Log.DefaultLogWriteLineInfo("@开始执行数据驱动流程调度:" + fl.getName());
				this.DTS_Flow(fl);
				continue;
			default:
				break;
			}
		}
		if (!BP.Web.WebUser.getNo().equals("admin")) {
			BP.Port.Emp empadmin = new BP.Port.Emp("admin");
			BP.Web.WebUser.SignInOfGener(empadmin);
		}
		///#endregion 发送消息
	}

	public final void DTS_Flow(BP.WF.Flow fl) {
		///#region 读取数据.
		MapExt me = new MapExt();
		me.setMyPK("ND" + Integer.parseInt(fl.getNo()) + "01" + "_" + MapExtXmlList.StartFlow);
		int i = me.RetrieveFromDBSources();
		if (i == 0) {
			BP.DA.Log.DefaultLogWriteLineError("没有为流程(" + fl.getName() + ")的开始节点设置发起数据,请参考说明书解决.");
			return;
		}
		if (isNullOrEmpty(me.getTag())) {
			BP.DA.Log.DefaultLogWriteLineError("没有为流程(" + fl.getName() + ")的开始节点设置发起数据,请参考说明书解决.");
			return;
		}

		// 获取从表数据.
		DataSet ds = new DataSet();
		String[] dtlSQLs = me.getTag1().split("*");
		for (String sql : dtlSQLs) {
			if (isNullOrEmpty(sql)) {
				continue;
			}

			String[] tempStrs = sql.split("[=]", -1);
			String dtlName = tempStrs[0];
			DataTable dtlTable = BP.DA.DBAccess.RunSQLReturnTable(sql.replace(dtlName + "=", ""));
			dtlTable.TableName = dtlName;
			ds.Tables.add(dtlTable);
		}
		///#endregion 读取数据.

		///#region 检查数据源是否正确.
		String errMsg = "";
		// 获取主表数据.
		DataTable dtMain = BP.DA.DBAccess.RunSQLReturnTable(me.getTag());
		if (dtMain.Rows.size() == 0) {
			//this.SetText("流程(" + fl.getName() + ")此时无任务.");
			Log.DefaultLogWriteLineInfo("流程(" + fl.getName() + ")此时无任务.");
			return;
		}

		//this.SetText("@查询到(" + dtMain.Rows.size() + ")条任务.");
		Log.DefaultLogWriteLineInfo("@查询到(" + dtMain.Rows.size() + ")条任务.");

		if (dtMain.Columns.contains("Starter") == false) {
			errMsg += "@配值的主表中没有Starter列.";
		}

		if (dtMain.Columns.contains("MainPK") == false) {
			errMsg += "@配值的主表中没有MainPK列.";
		}

		if (errMsg.length() > 2) {
			//this.SetText(errMsg);
			BP.DA.Log.DefaultLogWriteLineError("流程(" + fl.getName() + ")的开始节点设置发起数据,不完整." + errMsg);
			return;
		}
		///#endregion 检查数据源是否正确.

		///#region 处理流程发起.
		String nodeTable = "ND" + Integer.parseInt(fl.getNo()) + "01";
		int idx = 0;
		for (DataRow dr : dtMain.Rows) {
			idx++;

			String mainPK = dr.getValue("MainPK").toString();
			String sql = "SELECT OID FROM " + nodeTable + " WHERE MainPK='" + mainPK + "'";
			if (DBAccess.RunSQLReturnTable(sql).Rows.size() != 0) {
				//this.SetText("@" + fl.getName() + ",第" + idx + "条,此任务在之前已经完成。");
				Log.DefaultLogWriteLineWarning("@" + fl.getName() + ",第" + idx + "条,此任务在之前已经完成。");
				continue; //说明已经调度过了
			}

			String starter = dr.getValue("Starter").toString();
			if (!starter.equals(WebUser.getNo())) {
				BP.Web.WebUser.Exit();
				BP.Port.Emp emp = new BP.Port.Emp();
				emp.setNo(starter);
				if (emp.RetrieveFromDBSources() == 0) {
					//this.SetText("@" + fl.getName() + ",第" + idx + "条,设置的发起人员:" + emp.getNo() + "不存在.");
					Log.DefaultLogWriteLineError("@" + fl.getName() + ",第" + idx + "条,设置的发起人员:" + emp.getNo() + "不存在.");
					BP.DA.Log.DefaultLogWriteLineInfo("@数据驱动方式发起流程(" + fl.getName() + ")设置的发起人员:" + emp.getNo() + "不存在。");
					continue;
				}
				WebUser.SignInOfGener(emp);
			}

			///#region  给值.
			//System.Collections.Hashtable ht = new Hashtable();

			Work wk = fl.NewWork();

			String err = "";
			///#region 检查用户拼写的sql是否正确？
			for (DataColumn dc : dtMain.Columns) {
				String f = dc.ColumnName.toLowerCase();
				//				switch (f)
				//ORIGINAL LINE: case "starter":
				if (f.equals("starter") || f.equals("mainpk") || f.equals("refmainpk") || f.equals("tonode")) {} else {
					boolean isHave = false;
					for (Attr attr : wk.getEnMap().getAttrs()) {
						if (attr.getKey().toLowerCase().equals(f)) {
							isHave = true;
							break;
						}
					}
					if (isHave == false) {
						err += " " + f + " ";
					}
				}
			}
			if (isNullOrEmpty(err) == false) {
				throw new RuntimeException("您设置的字段:" + err + "不存在开始节点的表单中，设置的sql:" + me.getTag());
			}

			///#endregion 检查用户拼写的sql是否正确？

			for (DataColumn dc : dtMain.Columns) {
				wk.SetValByKey(dc.ColumnName, dr.getValue(dc.ColumnName).toString());
			}

			if (ds.Tables.size() != 0) {
				// MapData md = new MapData(nodeTable);
				MapDtls dtls = new MapDtls(nodeTable);
				for (MapDtl dtl : dtls.ToJavaList()) {
					for (DataTable dt : ds.Tables) {
						if (dt.TableName != dtl.getNo()) {
							continue;
						}

						//删除原来的数据。
						GEDtl dtlEn = dtl.getHisGEDtl();
						dtlEn.Delete(GEDtlAttr.RefPK, wk.getOID() + "");

						// 执行数据插入。
						for (DataRow drDtl : dt.Rows) {
							if (!drDtl.getValue("RefMainPK").toString().equals(mainPK)) {
								continue;
							}

							dtlEn = dtl.getHisGEDtl();
							for (DataColumn dc : dt.Columns) {
								dtlEn.SetValByKey(dc.ColumnName, drDtl.getValue(dc.ColumnName).toString());
							}

							dtlEn.setRefPK(wk.getOID() + "");
							dtlEn.setOID(0);
							dtlEn.Insert();
						}
					}
				}
			}
			///#endregion  给值.

			int toNodeID = 0;
			try {
				toNodeID = Integer.parseInt(dr.getValue("ToNode").toString());
			} catch (java.lang.Exception e) {
				//有可能在4.5以前的版本中没有tonode这个约定.
			}

			// 处理发送信息.
			//  Node nd =new Node();
			String msg = "";
			try {
				if (toNodeID == 0) {
					WorkNode wn = new WorkNode(wk, fl.getHisStartNode());
					msg = wn.NodeSend().ToMsgOfText();
				}

				if (toNodeID == fl.getStartNodeID()) {
					// 发起后让它停留在开始节点上，就是为开始节点创建一个待办。
					long workID = BP.WF.Dev2Interface.Node_CreateBlankWork(fl.getNo(), null, null, WebUser.getNo(), null);
					if (workID != wk.getOID()) {
						throw new RuntimeException("@异常信息:不应该不一致的workid.");
					} else {
						wk.Update();
					}
					msg = "已经为(" + WebUser.getNo() + ") 创建了开始工作节点. ";
				}

				BP.DA.Log.DefaultLogWriteLineInfo(msg);
				//this.SetText("@" + fl.getName() + ",第" + idx + "条,发起人员:" + WebUser.getNo() + "-" + WebUser.getName() + "已完成.\r\n" + msg);
				Log.DefaultLogWriteLineWarning("@" + fl.getName() + ",第" + idx + "条,发起人员:" + WebUser.getNo() + "-" + WebUser.getName() + "已完成.\r\n" + msg);
			} catch (RuntimeException ex) {
				//this.SetText("@" + fl.getName() + ",第" + idx + "条,发起人员:" + WebUser.getNo() + "-" + WebUser.getName() + "发起时出现错误.\r\n" + ex.getMessage());
				Log.DefaultLogWriteLineError("@" + fl.getName() + ",第" + idx + "条,发起人员:" + WebUser.getNo() + "-" + WebUser.getName() + "发起时出现错误.\r\n"
						+ ex.getMessage());
				BP.DA.Log.DefaultLogWriteLineWarning(ex.getMessage());
			}
		}
		///#endregion 处理流程发起.
	}

	public ScanSta HisScanSta = ScanSta.Pause;

	/**
	 * 发送消息
	 */
	private void DoSendMsg() {
		int idx = 0;
		///#region 发送消息
		SMSs sms = new SMSs();
		BP.En.QueryObject qo = new BP.En.QueryObject(sms);
		sms.Retrieve(SMSAttr.EmailSta, MsgSta.UnRun.getValue());
		for (SMS sm : sms.ToJavaList()) {
			if (this.HisScanSta == ScanSta.Stop) {
				return;
			}

			while (this.HisScanSta == ScanSta.Pause) {
				if (this.HisScanSta == ScanSta.Stop) {
					return;
				}

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				/*if (this.checkBox1.Checked)
				{
					Console.Beep();
				}*/
			}

			if (sm.getEmail().length() == 0) {
				sm.setHisEmailSta(MsgSta.RunOK);
				sm.Update();
				continue;
			}
			try {
				//this.SetText("@执行：send email: " + sm.getEmail());
				Log.DefaultLogWriteLineInfo("@执行：send email: " + sm.getEmail());
				this.SendMail(sm);

				idx++;
				//this.SetText("已完成 , 第:" + idx + " 个.");
				Log.DefaultLogWriteLineInfo("已完成 , 第:" + idx + " 个.");
				//this.SetText("--------------------------------");
				Log.DefaultLogWriteLineInfo("--------------------------------");

				/*if (this.checkBox1.Checked)
				{
					Console.Beep();
				}*/
			} catch (RuntimeException ex) {
				//this.SetText("@错误：" + ex.getMessage());
				Log.DefaultLogWriteLineError("@错误：" + ex.getMessage());
			}
		}
		///#endregion 发送消息
	}

	/**
	 * 发送邮件。
	 * @param sms
	 */
	public final void SendMail(SMS sms) {
		///#region 向ccim写入信息。
		//如果向 ccim 写入消息。
		if (this.CB_IsWriteToCCIM == true) {
			//如果被选择了，就是要向ccim里面写入信息. 
			try {
				Glo.SendMessage_CCIM(sms.getMyPK(), new java.util.Date().toString(), sms.getTitle() + "\t\n" + sms.getDocOfEmail(),
						sms.getSendToEmpNo());
			} catch (RuntimeException ex) {
				//JOptionPane.showConfirmDialog(null, ex.getMessage(), "错误", JOptionPane.DEFAULT_OPTION);
				return;
			}
		}
		///#endregion 向ccim写入信息。

		///#region 发送邮件.
		if (isNullOrEmpty(sms.getEmail())) {
			BP.WF.Port.WFEmp emp = new BP.WF.Port.WFEmp(sms.getSendToEmpNo());
			sms.setEmail(emp.getEmail());
		}

		/*MailMessage myEmail = new MailMessage();
		myEmail.From = new MailAddress("ccbpmtester@tom.com", "ccbpm123", System.Text.Encoding.UTF8);

		myEmail.To.Add(sms.Email);
		myEmail.Subject = sms.Title;
		myEmail.SubjectEncoding = System.Text.Encoding.UTF8; //邮件标题编码

		myEmail.Body = sms.DocOfEmail;
		myEmail.BodyEncoding = System.Text.Encoding.UTF8; //邮件内容编码
		myEmail.IsBodyHtml = true; //是否是HTML邮件

		myEmail.Priority = MailPriority.High; //邮件优先级

		SmtpClient client = new SmtpClient();

		//邮件地址.
		String emailAddr = SystemConfig.GetValByKey("SendEmailAddress", null);
		if (emailAddr == null) {
			emailAddr = "ccbpmtester@tom.com";
		}

		String emailPassword = SystemConfig.GetValByKey("SendEmailPass", null);
		if (emailPassword == null) {
			emailPassword = "ccbpm123";
		}

		//是否启用ssl? 
		boolean isEnableSSL = false;
		String emailEnableSSL = SystemConfig.GetValByKey("SendEmailEnableSsl", null);
		if (emailEnableSSL == null || emailEnableSSL.equals("0")) {
			isEnableSSL = false;
		} else {
			isEnableSSL = true;
		}

		client.Credentials = new System.Net.NetworkCredential(emailAddr, emailPassword);

		//上述写你的邮箱和密码
		client.Port = SystemConfig.GetValByKeyInt("SendEmailPort", 25); //使用的端口
		client.Host = SystemConfig.GetValByKey("SendEmailHost", "smtp.tom.com");

		//是否启用加密,有的邮件服务器发送配置不成功就是因为此参数的错误。
		client.EnableSsl = SystemConfig.GetValByKeyBoolen("SendEmailEnableSsl", isEnableSSL);

		Object userState = myEmail;
		try {
			client.SendAsync(myEmail, userState);
			sms.HisEmailSta = MsgSta.RunOK;
			sms.Update();
		} catch (System.Net.Mail.SmtpException ex) {
			throw ex;
		}
		///#endregion 发送邮件.*/

	}

	/**
	 * 逾期流程
	 */
	private void DoOverDueFlow() {
		//特殊处理天津的需求.
		DoTianJinSpecFunc();

		///#region 找到要逾期的数据.
		DataTable generTab = null;
		String sql = "SELECT a.FK_Flow,a.WorkID,a.Title,a.FK_Node,a.SDTOfNode,a.Starter,a.TodoEmps ";
		sql += "FROM WF_GenerWorkFlow a, WF_Node b";
		sql += " WHERE a.SDTOfNode<='" + DataType.getCurrentDataTime() + "' ";
		sql += " AND WFState=2 and b.OutTimeDeal!=0";
		sql += " AND a.FK_Node=b.NodeID";
		generTab = DBAccess.RunSQLReturnTable(sql);
		///#endregion 找到要逾期的数据.

		// 遍历循环,逾期表进行处理.
		String msg = "";
		for (DataRow row : generTab.Rows) {
			String fk_flow = row.getValue("FK_Flow") + "";
			String fk_node = row.getValue("FK_Node") + "";
			long workid = Long.parseLong(row.getValue("WorkID") + "");
			String title = row.getValue("Title") + "";
			String compleateTime = row.getValue("SDTOfNode") + "";
			String starter = row.getValue("Starter") + "";
			try {
				Node node = new Node(Integer.parseInt(fk_node));
				if (node.getIsStartNode()) {
					continue;
				}

				//获得该节点的处理内容.
				String doOutTime = node.GetValStrByKey(NodeAttr.DoOutTime);
				switch (node.getHisOutTimeDeal()) {
				case None: //逾期不处理.
					continue;
				case AutoJumpToSpecNode: //跳转到指定的节点.
					try {
						String[] jumps = doOutTime.split("[,]", -1);

						String jumpNode = jumps[0];
						String jumpEmp = jumps[1];

						Emp emp = new Emp(jumpEmp);
						if (isNullOrEmpty(emp.getNo())) {
							msg = "流程: '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName()
									+ "'超时处理规则为'自动跳转',系统中并没有编号为'" + jumpEmp + "'的人员.";
							//SetText(msg);
							BP.DA.Log.DefaultLogWriteLine(LogType.Info, msg);
						}

						Node jumpToNode = new Node(jumpNode);
						if (isNullOrEmpty(jumpToNode.getName())) {
							msg = "流程 '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName()
									+ "'超时处理规则为'自动跳转',系统中并没有编号为'" + jumpNode + "'的人员.";
							//SetText(msg);
							BP.DA.Log.DefaultLogWriteLine(LogType.Info, msg);
							return;
						}
						//执行发送.
						//string info =  BP.WF.Dev2Interface.Node_SendWork(this.FK_Flow, this.WorkID, null, null, nd.NodeID, emp.No).ToMsgOfText();
						String jumpInfo = BP.WF.Dev2Interface.Flow_Schedule(workid, jumpToNode.getNodeID(), emp.getNo());
						msg = "流程 '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName()
								+ "'超时处理规则为'自动跳转'," + jumpInfo;
						//SetText(msg);
						//Log.DefaultLogWriteLineInfo(msg);
						BP.DA.Log.DefaultLogWriteLine(LogType.Info, msg);

					} catch (RuntimeException ex) {
						msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName()
								+ "'超时处理规则为'自动跳转',跳转异常:" + ex.getMessage();
						//SetText(msg);
						BP.DA.Log.DefaultLogWriteLine(LogType.Error, msg);
					}
					break;
				case AutoShiftToSpecUser: //走动移交给.
					// 判断当前的处理人是否是.
					Emp empShift = new Emp(doOutTime);
					try {
						BP.WF.Dev2Interface.Node_Shift(fk_flow, Integer.parseInt(fk_node), workid, 0, empShift.getNo(), "流程节点已经逾期,系统自动移交");

						msg = "流程 '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName()
								+ "'超时处理规则为'移交到指定的人',已经自动移交给'" + empShift.getName() + ".";
						//SetText(msg);
						BP.DA.Log.DefaultLogWriteLine(LogType.Info, msg);
					} catch (RuntimeException ex) {
						msg = "流程 '" + node.getFlowName() + "' ,标题:'" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName()
								+ "'超时处理规则为'移交到指定的人',移交异常：" + ex.getMessage();
						//SetText(msg);
						BP.DA.Log.DefaultLogWriteLine(LogType.Error, msg);
					}
					break;
				case AutoTurntoNextStep:
					try {
						GenerWorkerList workerList = new GenerWorkerList();
						workerList.RetrieveByAttrAnd(GenerWorkerListAttr.WorkID, workid, GenerWorkFlowAttr.FK_Node, fk_node);

						WebUser.SignInOfGener(workerList.getHisEmp());
						WorkNode firstwn = new WorkNode(workid, Integer.parseInt(fk_node));
						String sendIfo = firstwn.NodeSend().ToMsgOfText();
						msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName()
								+ "'超时处理规则为'自动发送到下一节点',发送消息为:" + sendIfo;
						//SetText(msg);
						BP.DA.Log.DefaultLogWriteLine(LogType.Info, msg);
					} catch (RuntimeException ex) {
						msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName()
								+ "'超时处理规则为'自动发送到下一节点',发送异常:" + ex.getMessage();
						//SetText(msg);
						BP.DA.Log.DefaultLogWriteLine(LogType.Error, msg);
					}
					break;
				case DeleteFlow:
					String info = BP.WF.Dev2Interface.Flow_DoDeleteFlowByReal(fk_flow, workid, true);
					msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName()
							+ "'超时处理规则为'删除流程'," + info;
					//SetText(msg);
					BP.DA.Log.DefaultLogWriteLine(LogType.Info, msg);
					break;
				case RunSQL:
					try {
						BP.WF.Work wk = node.getHisWork();
						wk.setOID(workid);
						wk.Retrieve();

						doOutTime = BP.WF.Glo.DealExp(doOutTime, wk, null);

						//替换字符串
						doOutTime.replace("@OID", workid + "");
						doOutTime.replace("@FK_Flow", fk_flow);
						doOutTime.replace("@FK_Node", fk_node);
						doOutTime.replace("@Starter", starter);
						if (doOutTime.contains("@")) {
							msg = "流程 '" + node.getFlowName() + "',标题:  '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName()
									+ "'超时处理规则为'执行SQL'.有未替换的SQL变量.";
							//SetText(msg);
							BP.DA.Log.DefaultLogWriteLine(LogType.Info, msg);
							break;
						}

						//执行sql.
						DBAccess.RunSQL(doOutTime);
					} catch (RuntimeException ex) {
						msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName()
								+ "'超时处理规则为'执行SQL'.运行SQL出现异常:" + ex.getMessage();
						//SetText(msg);
						BP.DA.Log.DefaultLogWriteLine(LogType.Error, msg);
					}
					break;
				case SendMsgToSpecUser:
					try {
						Emp myemp = new Emp(doOutTime);

						boolean boo = BP.WF.Dev2Interface.WriteToSMS(myemp.getNo(), getDateStr("yyyy-MM-dd HH:mm:ss"), "系统发送逾期消息", "您的流程:'" + title
								+ "'的完成时间应该为'" + compleateTime + "',流程已经逾期,请及时处理!", "系统消息");
						if (boo) {
							msg = "'" + title + "'逾期消息已经发送给:'" + myemp.getName() + "'";
						} else {
							msg = "'" + title + "'逾期消息发送未成功,发送人为:'" + myemp.getName() + "'";
						}
						//SetText(msg);
						BP.DA.Log.DefaultLogWriteLine(LogType.Info, msg);
					} catch (RuntimeException ex) {
						msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName()
								+ "'超时处理规则为'执行SQL'.运行SQL出现异常:" + ex.getMessage();
						//SetText(msg);
						BP.DA.Log.DefaultLogWriteLine(LogType.Error, msg);
					}
					break;
				default:
					msg = "流程 '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName()
							+ "'没有找到相应的超时处理规则.";
					//SetText(msg);
					BP.DA.Log.DefaultLogWriteLine(LogType.Error, msg);
					break;
				}
			} catch (RuntimeException ex) {
				//SetText("流程逾期出现异常:" + ex.getMessage());
				BP.DA.Log.DefaultLogWriteLine(LogType.Error, ex.toString());

			}
		}
		BP.DA.Log.DefaultLogWriteLine(LogType.Info, "结束扫描逾期流程数据.");
	}

	/**
	 * 特殊处理天津的流程 当指定的节点，到了10号，15号自动向下发送.
	 */
	private void DoTianJinSpecFunc() {
		if (getDateInt() == 10 || getDateInt() == 15) {
			// 一个是10号自动审批，一个是15号自动审批. 
		} else {
			return;
		}

		///#region 找到要逾期的数据.
		DataTable generTab = null;
		String sql = "SELECT a.FK_Flow,a.WorkID,a.Title,a.FK_Node,a.SDTOfNode,a.Starter,a.TodoEmps ";
		sql += "FROM WF_GenerWorkFlow a, WF_Node b";
		sql += " WHERE  ";
		sql += "   a.FK_Node=b.NodeID  ";

		if (getDateInt() == 10) {
			sql += "   AND  b.NodeID=13304 ";
		}

		if (getDateInt() == 15) {
			sql += "AND b.NodeID=13302 ";
		}

		generTab = DBAccess.RunSQLReturnTable(sql);
		///#endregion 找到要逾期的数据.

		// 遍历循环,逾期表进行处理.
		String msg = "";
		for (DataRow row : generTab.Rows) {
			String fk_flow = row.getValue("FK_Flow") + "";
			String fk_node = row.getValue("FK_Node") + "";
			long workid = Long.parseLong(row.getValue("WorkID") + "");
			String title = row.getValue("Title") + "";
			String compleateTime = row.getValue("SDTOfNode") + "";
			String starter = row.getValue("Starter") + "";
			try {
				Node node = new Node(Integer.parseInt(fk_node));

				try {
					GenerWorkerList workerList = new GenerWorkerList();
					workerList.RetrieveByAttrAnd(GenerWorkerListAttr.WorkID, workid, GenerWorkFlowAttr.FK_Node, fk_node);

					WebUser.SignInOfGener(workerList.getHisEmp());

					WorkNode firstwn = new WorkNode(workid, Integer.parseInt(fk_node));
					String sendIfo = firstwn.NodeSend().ToMsgOfText();
					msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName()
							+ "'超时处理规则为'自动发送到下一节点',发送消息为:" + sendIfo;

					//输出消息.
					//SetText(msg);
					BP.DA.Log.DefaultLogWriteLine(LogType.Info, msg);
				} catch (RuntimeException ex) {
					msg = "流程  '" + node.getFlowName() + "',标题: '" + title + "'的应该完成时间为'" + compleateTime + "',当前节点'" + node.getName()
							+ "'超时处理规则为'自动发送到下一节点',发送异常:" + ex.getMessage();
					//SetText(msg);
					BP.DA.Log.DefaultLogWriteLine(LogType.Error, msg);
				}
			} catch (RuntimeException ex) {
				//SetText("流程逾期出现异常:" + ex.getMessage());
				BP.DA.Log.DefaultLogWriteLine(LogType.Error, "流程逾期出现异常:"+ex.toString());
			}
		}
		BP.DA.Log.DefaultLogWriteLine(LogType.Info, "结束扫描逾期流程数据.");
	}

	/**
	 * 自动执行节点
	 */
	private void DoAutoNode() {
		String sql = "SELECT * FROM WF_GenerWorkerList WHERE FK_Node IN (SELECT NODEID FROM WF_Node WHERE (WhoExeIt=1 OR  WhoExeIt=2) AND IsPass=0 AND IsEnable=1) ORDER BY FK_Emp";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows) {
			long workid = Long.parseLong(dr.getValue("WorkID").toString());
			int fk_node = Integer.parseInt(dr.getValue("FK_Node").toString());
			String fk_emp = dr.getValue("FK_Emp").toString();
			String fk_flow = dr.getValue("FK_Flow").toString();

			try {
				if (!fk_emp.equals(WebUser.getNo())) {
					WebUser.Exit();
					Emp emp = new Emp(fk_emp);
					WebUser.SignInOfGener(emp);
				}
				String msg = BP.WF.Dev2Interface.Node_SendWork(fk_flow, workid).ToMsgOfText();
				//this.SetText("@处理:" + WebUser.getNo() + ",WorkID=" + workid + ",正确处理:" + msg);
				Log.DebugWriteInfo("@处理:" + WebUser.getNo() + ",WorkID=" + workid + ",正确处理:" + msg);
			} catch (RuntimeException ex) {
				//this.SetText("@处理:" + WebUser.getNo() + ",WorkID=" + workid + ",工作信息:" + ex.getMessage());
				Log.DebugWriteInfo("@处理:" + WebUser.getNo() + ",WorkID=" + workid + ",工作信息:" + ex.getMessage());
			}
		}
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'IsNullOrEmpty'.
	//------------------------------------------------------------------------------------
	public static boolean isNullOrEmpty(String string) {
		return string == null || string.equals("");
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'Join' (2 parameter version).
	//------------------------------------------------------------------------------------
	public static String join(String separator, String[] stringarray) {
		if (stringarray == null)
			return null;
		else
			return join(separator, stringarray, 0, stringarray.length);
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'Join' (4 parameter version).
	//------------------------------------------------------------------------------------
	public static String join(String separator, String[] stringarray, int startindex, int count) {
		String result = "";

		if (stringarray == null)
			return null;

		for (int index = startindex; index < stringarray.length && index - startindex < count; index++) {
			if (separator != null && index > startindex)
				result += separator;

			if (stringarray[index] != null)
				result += stringarray[index];
		}

		return result;
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'TrimEnd'.
	//------------------------------------------------------------------------------------
	public static String trimEnd(String string, Character... charsToTrim) {
		if (string == null || charsToTrim == null)
			return string;

		int lengthToKeep = string.length();
		for (int index = string.length() - 1; index >= 0; index--) {
			boolean removeChar = false;
			if (charsToTrim.length == 0) {
				if (Character.isWhitespace(string.charAt(index))) {
					lengthToKeep = index;
					removeChar = true;
				}
			} else {
				for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++) {
					if (string.charAt(index) == charsToTrim[trimCharIndex]) {
						lengthToKeep = index;
						removeChar = true;
						break;
					}
				}
			}
			if (!removeChar)
				break;
		}
		return string.substring(0, lengthToKeep);
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'TrimStart'.
	//------------------------------------------------------------------------------------
	public static String trimStart(String string, Character... charsToTrim) {
		if (string == null || charsToTrim == null)
			return string;

		int startingIndex = 0;
		for (int index = 0; index < string.length(); index++) {
			boolean removeChar = false;
			if (charsToTrim.length == 0) {
				if (Character.isWhitespace(string.charAt(index))) {
					startingIndex = index + 1;
					removeChar = true;
				}
			} else {
				for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++) {
					if (string.charAt(index) == charsToTrim[trimCharIndex]) {
						startingIndex = index + 1;
						removeChar = true;
						break;
					}
				}
			}
			if (!removeChar)
				break;
		}
		return string.substring(startingIndex);
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'Trim' when arguments are used.
	//------------------------------------------------------------------------------------
	public static String trim(String string, Character... charsToTrim) {
		return trimEnd(trimStart(string, charsToTrim), charsToTrim);
	}

	//------------------------------------------------------------------------------------
	//	This method is used for string equality comparisons when the option
	//	'Use helper 'stringsEqual' method to handle null strings' is selected
	//	(The Java String 'equals' method can't be called on a null instance).
	//------------------------------------------------------------------------------------
	public static boolean stringsEqual(String s1, String s2) {
		if (s1 == null && s2 == null)
			return true;
		else
			return s1 != null && s1.equals(s2);
	}

	private String getDateStr(String formate) {
		SimpleDateFormat formatter = new SimpleDateFormat(formate);
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
		String str = formatter.format(curDate);
		return str;
	}

	private int getDateInt() {
		Calendar c = Calendar.getInstance();
		int datenum = c.get(Calendar.DATE);
		return datenum;
	}
}

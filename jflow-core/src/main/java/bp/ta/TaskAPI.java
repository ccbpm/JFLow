package bp.ta;

import bp.da.*;
import bp.difference.StringHelper;
import bp.en.*;
import bp.port.*;
import bp.web.*;

public class TaskAPI
{
		///#region 菜单列表.
	/** 
	 发起
	 
	 @return 
	*/
	public static String DB_Start()
	{
		Templates ens = new Templates();
		return bp.tools.Json.ToJson(ens.ToDataTableField("dt"));
	}
	/** 
	 待办
	 
	 @return 
	*/
	public static String DB_Todolist() throws Exception {
		Tasks tas = new Tasks();
		QueryObject qo = new QueryObject(tas);
		qo.addLeftBracket();
		qo.AddWhere(TaskAttr.EmpNo, WebUser.getNo());
		qo.addAnd();
		qo.AddWhereIn(TaskAttr.TaskSta, "(1,4,6)"); // 待办的, 审核中的,需要重做的.
		qo.addRightBracket();

		qo.addOr();

		qo.addLeftBracket();
		qo.AddWhere(TaskAttr.SenderNo, WebUser.getNo());
		qo.addAnd();
		qo.AddWhereIn(TaskAttr.TaskSta, "(2,5)"); // 审核中, 退回.
		qo.addRightBracket();

		qo.addOrderBy("ADT");

		qo.DoQuery();

		return bp.tools.Json.ToJson(tas.ToDataTableField("dt"));
	}
	/** 
	 在途
	 
	 @return 
	*/
	public static String DB_Running()
	{
		return "";
	}
		///#endregion 菜单列表.

		///#region 项目
	/** 
	 创建工作
	 
	 @param templateNo 模板编号
	 @return 
	*/
	public static String Prj_CreateNo(String templateNo) throws Exception {
		Template template = new Template(templateNo);

		//该模板类型下是否有这个项目.
		Project prj = new Project();
		int i = prj.Retrieve(ProjectAttr.PrjSta, 0, "TemplateNo", template.getNo(), ProjectAttr.StarterNo, WebUser.getNo());
		if (i == 1)
		{
			return prj.getNo();
		}

		prj.setName(template.getName());
		prj.setPrjDesc(template.getPrjDesc()); //任务描述.
		prj.setStarterNo(WebUser.getNo());
		prj.setStarterName(WebUser.getName());
		prj.setPrjSta(0); //空白状态.
		prj.setNo(StringHelper.padLeft(String.valueOf(DBAccess.GenerOID("PrjNo")), 5, '0'));
		prj.setRDT(DataType.getCurrentDateTime());
		prj.setTemplateNo(template.getNo());
		prj.setTemplateName(template.getName());
		prj.Insert();

		if (template.getTaskModel().equals("Daily") == true)
		{
			prj.setPrjSta(2); //运行状态.
			prj.Update();
			return prj.getNo();
		}

		if (template.getTaskModel().equals("Section") == true)
		{
			//获得节点.
			Nodes nds = new Nodes();
			nds.Retrieve(NodeAttr.TemplateNo, templateNo, "Idx");

			//为节点的人员创建工作.
			for (Node nd : nds.ToJavaList())
			{
				Task ta = new Task();

				ta.setNodeNo( nd.getNo());
				ta.setNodeName(nd.getName());


				ta.setTitle(nd.getName());
				ta.setTaskSta(0);
				ta.setItIsRead(0);

				//发送人= 任务的下达人.
				ta.setSenderNo(WebUser.getNo());
				ta.setSenderName(WebUser.getName());

				//生成负责人.
				String fzrEmpNo = nd.GenerFZR();
				if (DataType.IsNullOrEmpty(fzrEmpNo) == false)
				{
					Emp fzr = new Emp();
					ta.setEmpNo(fzr.getNo());
					ta.setEmpName(fzr.getName());
				}

				//Todo: 生成协助人.暂不实现.
				//赋值项目信息.
				ta.setPrjNo(prj.getNo());
				ta.setPrjName(prj.getName());
				ta.setWCL(0);
				ta.setPRI(0);
				ta.setTaskSta(0);
				ta.setStarterNo(WebUser.getNo());
				ta.setStarterName(WebUser.getName());
				ta.InsertAsOID(DBAccess.GenerOID("Task"));
				//ta.EmpNo = item.
			}
			return prj.getNo();
		}
		return "err@没有判断的类型:" + template.getTaskModel();
	}
	public static String Prj_Start(String prjNo) throws Exception {
		Project prj = new Project(prjNo);
		if (prj.getPrjSta() == 0 || prj.getPrjSta() == 1)
		{
		}
		else
		{
			return "err@项目已经启动了,您不能在执行启动。";
		}
		prj.setPrjSta(2); //设置启动状态.
		prj.setRDT(DataType.getCurrentDateTime());

		// 获得所有的任务.
		Tasks tas = new Tasks();
		tas.Retrieve(TaskAttr.PrjNo, prjNo, null);

		// 工作人员.
		WorkerList wl = new WorkerList();
		wl.setPrjNo(prj.getNo());
		wl.setPrjName(prj.getName());
		wl.setRDT(DataType.getCurrentDateTime());

		// 列表.
		String msg = "任务分配给:";
		for (Task ta : tas.ToJavaList())
		{
			ta.setPrjSta(prj.getPrjSta());
			ta.setPrjName(prj.getName());
			ta.setPRI(prj.getPRI());
			ta.setTaskSta(TaskSta.Todolist); //设置启动.
			ta.setRDT(DataType.getCurrentDateTime());
			ta.setADT(DataType.getCurrentDateTime());
			ta.Update();

			//开始创建任务.
			wl.setMyPK(ta.getTaskID() + "_" + ta.getEmpNo());
			wl.setEmpNo(ta.getEmpNo()); //工作人员.
			wl.setEmpName(ta.getEmpName());

			wl.setPrjNo(ta.getPrjNo()); //项目信息.
			wl.setPrjName(prj.getName());

			wl.setSenderNo(WebUser.getNo()); //发送人.
			wl.setSenderName(WebUser.getName());
			wl.setRDT(DataType.getCurrentDateTime()); //记录日期
			wl.setADT(DataType.getCurrentDateTime()); //活动日期.
			wl.Insert(); //插入任务.

			msg += "节点:" + ta.getNodeName() + " 人员:" + ta.getEmpName();

			TaskAPI.Port_WriteTrack(ta.getPrjNo(), ta.getOID(), "Alte", "工作分配", "任务[" + ta.getTitle() + "]分配给[" + ta.getEmpName() + "]", WebUser.getNo(), WebUser.getName());
		}

		prj.setMsg(msg); //更新消息.
		prj.setNumComplete(0);
		prj.setNumTasks(tas.size());
		prj.Update();

		bp.ta.TaskAPI.Port_WriteTrack(prjNo, 0, "Start", "项目启动", msg, WebUser.getNo(), WebUser.getName());
		return "启动成功:" + msg;
	}
	/** 
	 设置完成
	 
	 @param prjNo 项目编号
	 @return 执行结果
	*/
	public static String Prj_Complete(String prjNo) throws Exception {
		DBAccess.RunSQL("UPDATE TA_Project SET PrjSta=" + PrjSta.Complete + ",WCL=100 WHERE No='" + prjNo + "'");
		DBAccess.RunSQL("UPDATE TA_Task SET TaskSta=" + TaskSta.WorkOver + " WHERE PrjNo='" + prjNo + "'");

		bp.ta.TaskAPI.Port_WriteTrack(prjNo, 0, "Complete", "完成", "项目完成.", WebUser.getNo(), WebUser.getName());
		return "项目完成.";
	}
	/** 
	 物理删除
	 
	 @param prjNo 项目编号
	 @return 执行结果
	*/
	public static String Prj_DeleteByRel(String prjNo)
	{
		DBAccess.RunSQL("DELETE FROM TA_Project WHERE No='" + prjNo + "'");
		DBAccess.RunSQL("DELETE FROM TA_Task WHERE PrjNo='" + prjNo + "'");
		DBAccess.RunSQL("DELETE FROM TA_Track WHERE PrjNo='" + prjNo + "'");
		return "删除成功.";
	}
	/** 
	 删除标记
	 
	 @param prjNo
	 @return 
	*/
	public static String Prj_DeleteByFlag(String prjNo) throws Exception {
		DBAccess.RunSQL("UPDATE TA_Project SET PrjSta=7 WHERE No='" + prjNo + "'");
		DBAccess.RunSQL("UPDATE TA_Task SET TaskSta=7 WHERE PrjNo='" + prjNo + "'");

		bp.ta.TaskAPI.Port_WriteTrack(prjNo, 0, "DeleteByFlag", "逻辑删除", "逻辑删除.", WebUser.getNo(), WebUser.getName());
		return "删除成功.";
	}
	/** 
	 写日志
	 
	 @param prjNo 模板编号
	 @param workid 工作ID
	 @param actionType 活动类型
	 @param actionTypeName 活动名称
	 @param docs 执行内容
	*/

	public static void Port_WriteTrack(String prjNo, long workid, String actionType, String actionTypeName, String docs, String empNo, String empName, int wcl, int useHH) throws Exception {
		Port_WriteTrack(prjNo, workid, actionType, actionTypeName, docs, empNo, empName, wcl, useHH, 0);
	}

	public static void Port_WriteTrack(String prjNo, long workid, String actionType, String actionTypeName, String docs, String empNo, String empName, int wcl) throws Exception {
		Port_WriteTrack(prjNo, workid, actionType, actionTypeName, docs, empNo, empName, wcl, 0, 0);
	}

	public static void Port_WriteTrack(String prjNo, long workid, String actionType, String actionTypeName, String docs, String empNo, String empName) throws Exception {
		Port_WriteTrack(prjNo, workid, actionType, actionTypeName, docs, empNo, empName, 0, 0, 0);
	}

	public static void Port_WriteTrack(String prjNo, long workid, String actionType, String actionTypeName, String docs, String empNo, String empName, int wcl, int useHH, int useMM) throws Exception {
		Track ta = new Track();
		ta.setMyPK(DBAccess.GenerGUID(0, null, null));
		ta.setTaskID(workid);
		ta.setPrjNo(prjNo);

		if (empNo == null)
		{
			empNo = WebUser.getNo();
		}
		if (empName == null)
		{
			empName = WebUser.getName();
		}

		//当事人.
		ta.setEmpNo(empNo);
		ta.setEmpName(empName);


		ta.setActionType(actionType);
		ta.setActionName(actionTypeName);
		ta.setDocs(docs);
		ta.setRecNo(WebUser.getNo());
		ta.setRecName(WebUser.getName());
		ta.setRDT(DataType.getCurrentDateTime());

		ta.setUseHH(useHH);
		ta.setUseMM(useMM);

		ta.Insert();
	}
//C# TO JAVA CONVERTER TASK: There is no preprocessor in Java:
		///#endregion 项目


//C# TO JAVA CONVERTER TASK: There is no preprocessor in Java:
		///#region 任务

	/** 
	 任务审核
	 
	 @param taskID 任务ID
	 @param checkResult 审核结果0=不通过,1=通过.
	 @param docs 审核信息
	 @return 
	*/
	public static String Task_CheckSubmit(long taskID, int checkResult, String docs) throws Exception {
		Task ta = new Task(taskID);
		if (ta.getTaskSta() == TaskSta.SubmitChecking)
		{
			return "err@当前不是待审核状态，您不能执行该操作.";
		}

		//不通过.
		if (checkResult == 0)
		{
			TaskAPI.Port_WriteTrack(ta.getPrjNo(), ta.getOID(), "TaskSubmit", "任务审核", "不通过,意见:" + docs, WebUser.getNo(), WebUser.getName());
			ta.setTaskSta(TaskSta.ReTodolist);
			ta.setWCL(50); //完成率设置100%.
		}
		else
		{
			TaskAPI.Port_WriteTrack(ta.getPrjNo(), ta.getOID(), "TaskSubmit", "任务审核", "通过,意见:" + docs, WebUser.getNo(), WebUser.getName());
			ta.setTaskSta(TaskSta.ReTodolist);
		}
		ta.setNowMsg(docs);
		ta.setADT(DataType.getCurrentDateTime()); //活动日期.
		ta.Update();
		return "审核提交成功.";
	}
	public static String Task_CheckReturn(long taskID, int checkResult, String docs, String shiftEmpNo) throws Exception {
		Task ta = new Task(taskID);
		if (ta.getTaskSta() != TaskSta.ReturnWork)
		{
			return "err@当前不是退回审核状态，您不能执行该操作.";
		}

		//不通过.
		if (checkResult == 0)
		{
			TaskAPI.Port_WriteTrack(ta.getPrjNo(), ta.getOID(), "CheckReturn", "任务退回审核", "不通过,意见:" + docs, WebUser.getNo(), WebUser.getName());
			ta.setTaskSta(TaskSta.ReTodolist);
			//  ta.WCL = 0; //完成率设置100%.
		}
		//同意.
		if (checkResult == 1)
		{
			TaskAPI.Port_WriteTrack(ta.getPrjNo(), ta.getOID(), "CheckReturn", "任务退回审核", "通过,意见:" + docs, WebUser.getNo(), WebUser.getName());
			ta.setTaskSta(TaskSta.WorkOver);
			ta.setWCL(0); //完成率设置未 0 .
		}
		//移交给其他人.
		if (checkResult == 2)
		{
			return "err@该功能还没有完成.";
		}

		ta.setNowMsg(docs);
		ta.setADT(DataType.getCurrentDateTime()); //活动日期.
		ta.Update();

		return "退回审核提交成功.";
	}

	/** 
	 任务退回
	 
	 @param taskID
	 @param docs 退回原因
	 @return 退回结果
	*/
	public static String Task_Return(long taskID, String docs) throws Exception {
		Task ta = new Task(taskID);
		if (ta.getTaskSta() == TaskSta.ReturnWork)
		{
			return "err@当前已经是退回,不能重复执行.";
		}

		ta.setWCL(0); //完成率设置 0 .
		ta.setTaskSta(TaskSta.ReturnWork); //让其检查.
		ta.setADT(DataType.getCurrentDateTime()); //活动日期.
		ta.Update();

		TaskAPI.Port_WriteTrack(ta.getPrjNo(), ta.getOID(), "ReturnWork", "退回", "退回原因:" + docs, WebUser.getNo(), WebUser.getName());

		return "已经退回给[" + ta.getSenderName()+ "]，请等待他批准。";
	}
	/** 
	 移交给指定的人
	 
	 @param taskID
	 @param toEmpNo
	 @param docs
	 @return 
	*/
	public static String Task_Shift(long taskID, String toEmpNo, String docs) throws Exception {
		Emp emp = new Emp(toEmpNo);

		Task ta = new Task(taskID);
		ta.setEmpNo(toEmpNo);
		ta.setEmpName(emp.getName());

		ta.setTaskSta(TaskSta.Todolist);

		ta.setWCL(0); //完成率设置 0 .
		ta.setTaskSta(TaskSta.Todolist); //设置待办..
		ta.setADT(DataType.getCurrentDateTime()); //活动日期.
		ta.setNowMsg("移交原因:[" + docs + "]移交人:[" + WebUser.getName() + "]移交日期:[" + ta.getADT() + "]");
		ta.Update();

		TaskAPI.Port_WriteTrack(ta.getPrjNo(), ta.getOID(), "Shift", "移交", ta.getNowMsg(), WebUser.getNo(), WebUser.getName());

		return "移交[" + emp.getName() + "]";
	}
	public static String Task_HuiBao(long taskID, String docs1, String docs2, int wcl, int hh, int mm) throws Exception {
		Task ta = new Task(taskID);
		ta.setWCL(0); //完成率设置 0 .
		if (wcl == 100)
		{
			ta.setTaskSta(TaskSta.SubmitChecking); //提交状态,让其检查.
			ta.setNowMsg(docs1);
		}
		else
		{
			ta.setTaskSta(TaskSta.Todolist);
			ta.setNowMsg("工作内容:[" + docs1 + "]计划:[" + docs2 + "]");
		}

		ta.setADT(DataType.getCurrentDateTime()); //活动日期.
		ta.Update();


		if (wcl == 100)
		{
			TaskAPI.Port_WriteTrack(ta.getPrjNo(), ta.getOID(), "TaskSubmit", "任务提交", ta.getNowMsg(), WebUser.getNo(), WebUser.getName(), wcl, hh, mm);
			return "汇报给[" + ta.getSenderName()+ "]";
		}
		else
		{
			TaskAPI.Port_WriteTrack(ta.getPrjNo(), ta.getOID(), "HuiBao", "汇报工作", ta.getNowMsg(), WebUser.getNo(), WebUser.getName(), wcl, hh, mm);
			return "任务提交给:[" + ta.getSenderName()+ "]审核.";
		}

	}
		///#endregion 任务

}

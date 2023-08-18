package bp.ta;


import bp.da.DataType;
import bp.da.*;

/**
 页面功能实体
*/
public class TA_App extends bp.difference.handler.DirectoryPageBase
{

	/** 
	 构造函数
	*/
	public TA_App()
	{
	}

	//#region 属性.
	public final long getTaskID()
	{
		return this.GetRequestValInt64("TaskID");
	}
	public final String getMsg()
	{
		return this.GetRequestVal("Msg");
	}
	public final String getTemplateNo()
	{
		return this.GetRequestVal("TemplateNo");
	}
	public final String getPrjNo()
	{
		return this.GetRequestVal("PrjNo");
	}
	///#endregion 属性.


	///#region  菜单 .
	/** 
	 
	 
	 @return 
	*/
	public final String GL_TATrack_Init()
	{
		String sql = "SELECT B.MyPK,B.TaskID, A.PrjNo, A.PrjName, a.Title, B.ActionType, B.ActionName, B.EmpNo,B.EmpName, B.RDT, B.Docs ";
		sql += " FROM TA_Task A, TA_Track B WHERE A.OID=B.TaskID ORDER BY A.PrjNo,A.OID,B.RDT ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return bp.tools.Json.ToJson(dt);
	}
	public final String GL_TAMyTrack_Init()
	{
		String sql = "SELECT B.MyPK,B.TaskID, A.PrjNo, A.PrjName, a.Title, B.ActionType, B.ActionName, B.EmpNo,B.EmpName, B.RDT , B.Docs";
		sql += " FROM TA_Task A, TA_Track B WHERE A.OID =B.TaskID AND B.EmpNo='" + bp.web.WebUser.getNo() + "' ORDER BY  A.PrjNo,A.OID,B.RDT  ";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return bp.tools.Json.ToJson(dt);
	}
	public final String Start_Init()
	{
		return TaskAPI.DB_Start();
	}
	public final String Todolist_Init() throws Exception {
		return TaskAPI.DB_Todolist();
	}

		///#endregion 菜单.

		///#region  工作处理器 .
	/** 
	 我的项目初始化
	 
	 @return 
	*/
	public final String MyPrj_Init() throws Exception {
		//如果没有PrjNo = 就创建.
		String prjNo = this.getPrjNo();
		if (DataType.IsNullOrEmpty(prjNo) == true)
		{
			prjNo = TaskAPI.Prj_CreateNo(this.getTemplateNo());
		}

		return "执行成功.";
	}
	public final String Prj_CreateNo() throws Exception {
		return TaskAPI.Prj_CreateNo(this.getTemplateNo());
	}
	public final String Prj_Start() throws Exception {
		//发起
		return TaskAPI.Prj_Start(this.getPrjNo());
	}
	public final String Prj_Complete() throws Exception {
		//发起
		return TaskAPI.Prj_Complete(this.getPrjNo());
	}
	public final String Prj_DeleteByRel()
	{
		//发起
		return TaskAPI.Prj_DeleteByRel(this.getPrjNo());
	}
	public final String Prj_DeleteByFlag() throws Exception {
		//发起
		return TaskAPI.Prj_DeleteByFlag(this.getPrjNo());
	}
		///#endregion 工作处理器.


		///#region  工作处理部件 .
	/** 
	 退回
	 
	 @return 
	*/
	public final String Task_Return() throws Exception {
		//提交工作.
		return TaskAPI.Task_Return(this.getTaskID(), this.getMsg());
	}
	public final String Task_CheckReturn() throws Exception {
		int result = this.GetRequestValInt("CheckedResult"); //审核结果.
		String shiftEmpNo = this.GetRequestVal("ShiftEmpNo"); //要移交的人员.

		//提交工作.
		return TaskAPI.Task_CheckReturn(this.getTaskID(), result, this.getMsg(), shiftEmpNo);
	}
	public final String Task_CheckSubmit() throws Exception {
		int result = this.GetRequestValInt("CheckedResult");
		//提交工作.
		return TaskAPI.Task_CheckSubmit(this.getTaskID(), result, this.getMsg());
	}
	public final String Task_Shift() throws Exception {
		String toEmpNo = this.GetRequestVal("ShiftToEmpNo");

		//提交工作.
		return TaskAPI.Task_Shift(this.getTaskID(), toEmpNo, this.getMsg());
	}
	public final String Task_HuiBao() throws Exception {
		//提交工作.
		return TaskAPI.Task_HuiBao(this.getTaskID(), this.GetRequestVal("Msg1"), this.GetRequestVal("Msg2"), this.GetRequestValInt("WCL"), this.GetRequestValInt("UseHH"), this.GetRequestValInt("UseMM"));
	}
		///#endregion 工作处理部件.


}

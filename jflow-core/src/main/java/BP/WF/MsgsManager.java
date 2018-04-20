package BP.WF;

import BP.DA.DBAccess;
import BP.Web.*;

/** 
 MsgsManager
*/
public class MsgsManager
{
	/**  
	 删除工作by工作ID
	 
	 @param workId
	 * @throws Exception 
	*/
	public static void DeleteByWorkID(long workId) throws Exception
	{
		// System.Web.HttpContext.Current.Application.Lock();
				// Msgs msgs =
				// (Msgs)System.Web.HttpContext.Current.Application["WFMsgs"];
				// if (msgs == null)
				// {
				// msgs = new Msgs();
				// System.Web.HttpContext.Current.Application["WFMsgs"] = msgs;
				// }
				// // 清除全部的工作ID=workid 的消息。
				// msgs.ClearByWorkID(workId);
				// System.Web.HttpContext.Current.Application.UnLock();
				throw new Exception("");
	}
	/** 
	 增加信息
	 @param wls 工作者集合
	 @param flowName 流程名称
	 @param nodeName 节点名称
	 @param title 标题
	*/
	public static void AddMsgs(GenerWorkerLists wls, String flowName, String nodeName, String title)
	{
		/*return;

		System.Web.HttpContext.Current.Application.Lock();
		Msgs msgs = (Msgs)System.Web.HttpContext.Current.Application["WFMsgs"];
		if (msgs == null)
		{
			msgs = new Msgs();
			System.Web.HttpContext.Current.Application["WFMsgs"] = msgs;
		}
		// 清除全部的工作ID=workid 的消息。
		msgs.ClearByWorkID(wls[0].GetValIntByKey("WorkID"));
		for (GenerWorkerList wl : wls.ToJavaList())
		{
			if (wl.getFK_Emp().equals(WebUser.getNo()))
			{
				continue;
			}
			//msgs.AddMsg(wl.WorkID,wl.FK_Node,wl.FK_Emp,"来自流程["+flowName+"]节点["+nodeName+"]工作节点标题为["+title+"]的消息。");
		}
		System.Web.HttpContext.Current.Application.UnLock();*/
	}
	/** 
	 sss
	 
	 @param empId
	 @return 
	*/
	public static Msgs GetMsgsByEmpID_del(int empId)
	{
		return null;
		// Msgs msgs=
		// (Msgs)System.Web.HttpContext.Current.Application["WFMsgs"];
		// if (msgs==null)
		// {
		// msgs= new Msgs();
		// System.Web.HttpContext.Current.Application["WFMsgs"]=msgs;
		// }
		// return msgs.GetMsgsByEmpID_del(empId);
	}
	/** 
	 取出消息的个数
	 
	 @param empId
	 @return 
	 * @throws Exception 
	*/
	public static int GetMsgsCountByEmpID(int empId) throws Exception
	{
		String sql = "select COUNT(*) from v_wf_msg WHERE FK_Emp='"
				+ WebUser.getNo() + "'";
		return DBAccess.RunSQLReturnValInt(sql);
	}
	/** 
	 清除信息
	 @param empId
	*/
	public static void ClearMsgsByEmpID_(int empId)
	{
		/*System.Web.HttpContext.Current.Application.Lock();
		Msgs msgs = (Msgs)System.Web.HttpContext.Current.Application["WFMsgs"];
		msgs.ClearByEmpId_del(empId);
		System.Web.HttpContext.Current.Application.UnLock();*/
	}
	/** 
	 初始化全部的消息。
	 
	*/
	public static void InitMsgs()
	{
	}
}
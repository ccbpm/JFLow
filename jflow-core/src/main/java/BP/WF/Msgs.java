package BP.WF;

import java.util.List;

/** 
 消息集合
*/
public class Msgs extends java.util.ArrayList
{


		///#region 增加消息
	/** 
	 增加消息
	 
	 @param workId
	 @param nodeId
	 @param toEmpId
	 @param info
	*/
	public final void AddMsg(int workId, int nodeId, int toEmpId, String info)
	{
		Msg msg = new Msg();
		msg.setWorkID(workId);
		msg.setNodeId(nodeId);
		msg.setToEmpId(toEmpId);
		msg.setInfo(info);
		this.add(msg);
	}
	/** 
	 增加消息
	 @param msg 消息
	*/
	public final void AddMsg(Msg msg)
	{
		//return;
		this.add(msg);
	}
	/** 
	 安工作ID 清除消息。
	 @param workId
	*/
	public final void ClearByWorkID(long workId)
	{
		//return;
		Msgs ens = this.GetMsgsByWorkID(workId);
		for (Msg msg : ens.ToJavaList())
		{
			this.remove(msg);
		}
	}
	/** 
	 清除工作人员信息
	 
	 @param empId
	*/
	public final void ClearByEmpId_del(int empId)
	{
		//return;
		Msgs ens = this.GetMsgsByEmpID_del(empId);
		for (Msg msg : ens.ToJavaList())
		{
			this.remove(msg);
		}
	}
	/** 
	 清除工作人员信息
	 
	 @param workId
	 @return 
	*/
	public final Msgs GetMsgsByWorkID(long workId)
	{
		//return null;
		Msgs ens = new Msgs();
		for (Msg msg : this.ToJavaList())
		{
			if (msg.getWorkID() == workId)
			{
				ens.AddMsg(msg);
			}
		}
		return ens;
	}
	/** 
	 sss
	 @param empId
	 @return 
	*/
	public final Msgs GetMsgsByEmpID_del(int empId)
	{
		//return ;
		Msgs ens = new Msgs();
		for (Msg msg : this.ToJavaList())
		{
			if (msg.getToEmpId() == empId)
			{
				ens.AddMsg(msg);
			}
		}
		return ens;
	}
	/** 
	 取得信息的数量。
	 
	 @param empId 工作人员
	 @return 信息个数
	*/
	public final int GetMsgsCountByEmpID(int empId)
	{
		//return 0;
		int i = 0;
		//bool isHaveNew=false;
		int newMsgNum = 0;
		for (Msg msg : this.ToJavaList())
		{
			if (msg.getToEmpId() == empId)
			{
				if (msg.getIsOpenSound())
				{
					newMsgNum++;
				}
				i++;
			}
		}
		if (newMsgNum > 0)
		{
			//if (WebUser.IsSoundAlert)				 
			//    System.Web.HttpContext.Current.Response.Write("<bgsound src='"+BP.Sys.Glo.Request.ApplicationPath+Web.WebUser.NoticeSound+"' loop=1 >"  );
			//if (WebUser.IsTextAlert)
			//    BP.Sys.PubClass.ResponseWriteBlueMsg("您有["+newMsgNum+"]个新工作." );
			//System.Web.HttpContext.Current.Response.Write("<bgsound src='"+BP.Sys.Glo.Request.ApplicationPath+Web.WebUser.NoticeSound+"' loop=1 >"  );

		}
		return i;
	}
	/** 
	 消息s
	 
	*/
	public Msgs()
	{
	}

	/** 
	 根据位置取得数据
	*/
	public final Msg getItem(int index)
	{
		return (Msg)this.getItem(index);
	}
	public List<Msg> ToJavaList()
	{
		return (List<Msg>)(Object)this;
	}

}
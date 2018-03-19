package BP.WF;


/** 
 Msg 的摘要说明。
 
*/
public class Msg
{

		
	/** 
	 声音文件
	 
	*/
	private String _SoundUrl = Glo.getCCFlowAppPath() + "WF/Sound/ring.wav";
	/** 
	 声音文件
	 
	*/
	public final String getSoundUrl()
	{
		return _SoundUrl;
	}
	public final void setSoundUrl(String value)
	{
		_SoundUrl = value;
	}
	/** 
	 _IsOpenSound
	 
	*/
	private boolean _IsOpenSound = true;
	/** 
	 IsOpenSound
	 
	*/
	public final boolean getIsOpenSound()
	{
		if (this._IsOpenSound == false)
		{
			return false;
		}
		else
		{
			this._IsOpenSound = false;
			return true;
		}
	}
	/** 
	 _WorkID
	 
	*/
	private int _WorkID = 0;
	/** 
	 _NodeId
	 
	*/
	private int _NodeId = 0;
	/** 
	 _Info
	 
	*/
	private String _Info = "";
	/** 
	 _ToEmpId
	 
	*/
	private int _ToEmpId = 0;
	/** 
	 信息
	 
	*/
	public final String getInfo()
	{
		return this._Info;
	}
	public final void setInfo(String value)
	{
		_Info = value;
	}
	/** 
	 工作ID
	 
	*/
	public final int getWorkID()
	{
		return _WorkID;
	}
	public final void setWorkID(int value)
	{
		_WorkID = value;
	}
	/** 
	 NodeID
	 
	*/
	public final int getNodeId()
	{
		return _NodeId;
	}
	public final void setNodeId(int value)
	{
		_NodeId = value;
	}
	/** 
	 ToEmpId
	 
	*/
	public final int getToEmpId()
	{
		return _ToEmpId;
	}
	public final void setToEmpId(int value)
	{
		_ToEmpId = value;
	}

		///#endregion

	/** 
	 信息
	 
	*/
	public Msg()
	{
	}



	/** 
	 信息
	 
	 @param workId
	 @param nodeId
	 @param toEmpId
	 @param info
	*/
	public Msg(int workId, int nodeId, int toEmpId, String info)
	{
		this.setWorkID(workId);
		this.setNodeId(nodeId);
		this.setToEmpId(toEmpId);
		this.setInfo(info);
	}
}
package BP.WF;


/** 
 用户消息
 
*/
public class UserMsgs
{

		
	/** 
	 _IsOpenSound
	 
	*/
	private boolean _IsOpenSound = false;
	/** 
	 _IsOpenSound
	 
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

		///#endregion


		
	/** 
	 用户消息
	 
	*/
	public UserMsgs()
	{
	}
	/** 
	 用户消息
	 
	 @param empId
	*/
	public UserMsgs(int empId)
	{
	}

		///#endregion
}
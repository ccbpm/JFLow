package bp.gpm.weixin;

import bp.da.*;
import bp.tools.*;
import bp.*;
import java.util.*;

/** 
 简写的User
*/
public class User
{
	private int ErrCode;
	public final int getErrCode()
	{
		return ErrCode;
	}
	public final void setErrCode(int value)
	{
		ErrCode = value;
	}
	private String ErrMsg;
	public final String getErrMsg()
	{
		return ErrMsg;
	}
	public final void setErrMsg(String value)
	{
		ErrMsg = value;
	}
	private String UserId;
	public final String getUserId()
	{
		return UserId;
	}
	public final void setUserId(String value)
	{
		UserId = value;
	}
	private String DeviceId;
	public final String getDeviceId()
	{
		return DeviceId;
	}
	public final void setDeviceId(String value)
	{
		DeviceId = value;
	}
}

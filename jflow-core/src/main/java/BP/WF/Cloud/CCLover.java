package BP.WF.Cloud;

import BP.En.*;
import BP.DA.*;
import BP.Port.*;
import BP.Pub.*;
import BP.Sys.*;
import BP.WF.*;

/** 
 云用户的用户信息。
*/
public class CCLover
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 内存的用户与密码.
	/** 
	 获得当前用户.
	*/
	public static String getUserNo()
	{
		return BP.Sys.GloVars.GetValByKey("CCLoverNo", null);
	}
	public static void setUserNo(String value)
	{
		BP.Sys.GloVars.SetValByKey("CCLoverNo", value);
	}
	/** 
	 密码
	*/
	public static String getPassword()
	{
		return BP.Sys.GloVars.GetValByKey("CCLoverPassword", null);
	}
	public static void setPassword(String value)
	{
		BP.Sys.GloVars.SetValByKey("CCLoverPassword", value);
	}
	/** 
	 GUID
	*/
	public static String getGUID()
	{
		return BP.Sys.GloVars.GetValByKey("CCLoverGUID", null);
	}
	public static void setGUID(String value)
	{
		BP.Sys.GloVars.SetValByKey("CCLoverGUID", value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 内存的用户与密码.

}
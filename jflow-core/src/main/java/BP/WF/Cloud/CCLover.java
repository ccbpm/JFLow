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
	 * @throws Exception 
	*/
	public static String getUserNo() throws Exception
	{
		return BP.Sys.GloVars.GetValByKey("CCLoverNo", null);
	}
	public static void setUserNo(String value) throws Exception
	{
		BP.Sys.GloVars.SetValByKey("CCLoverNo", value);
	}
	/** 
	 密码
	 * @throws Exception 
	*/
	public static String getPassword() throws Exception
	{
		return BP.Sys.GloVars.GetValByKey("CCLoverPassword", null);
	}
	public static void setPassword(String value) throws Exception
	{
		BP.Sys.GloVars.SetValByKey("CCLoverPassword", value);
	}
	/** 
	 GUID
	 * @throws Exception 
	*/
	public static String getGUID() throws Exception
	{
		return BP.Sys.GloVars.GetValByKey("CCLoverGUID", null);
	}
	public static void setGUID(String value) throws Exception
	{
		BP.Sys.GloVars.SetValByKey("CCLoverGUID", value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 内存的用户与密码.

}
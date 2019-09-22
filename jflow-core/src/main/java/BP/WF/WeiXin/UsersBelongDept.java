package BP.WF.WeiXin;

import BP.WF.*;
import java.util.*;

/** 
 部门下的人员
*/
public class UsersBelongDept
{
	/** 
	 返回码
	*/
	private String errcode;
	public final String geterrcode()
	{
		return errcode;
	}
	public final void seterrcode(String value)
	{
		errcode = value;
	}
	/** 
	 对返回码的文本描述内容
	*/
	private String errmsg;
	public final String geterrmsg()
	{
		return errmsg;
	}
	public final void seterrmsg(String value)
	{
		errmsg = value;
	}
	/** 
	 成员列表
	*/
	private ArrayList<UserInfoBelongDept> userlist;
	public final ArrayList<UserInfoBelongDept> getuserlist()
	{
		return userlist;
	}
	public final void setuserlist(ArrayList<UserInfoBelongDept> value)
	{
		userlist = value;
	}
}
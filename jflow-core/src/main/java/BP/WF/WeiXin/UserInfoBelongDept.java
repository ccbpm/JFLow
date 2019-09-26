package BP.WF.WeiXin;

import BP.WF.*;
import java.util.*;

/** 
 部门人员详情
*/
public class UserInfoBelongDept
{
	/** 
	 成员UserID。对应管理端的帐号
	*/
	private String userid;
	public final String getuserid()
	{
		return userid;
	}
	public final void setuserid(String value)
	{
		userid = value;
	}
	/** 
	 成员名称
	*/
	private String name;
	public final String getname()
	{
		return name;
	}
	public final void setname(String value)
	{
		name = value;
	}
	/** 
	 成员所属部门
	*/
	private Object department;
	public final Object getdepartment()
	{
		return department;
	}
	public final void setdepartment(Object value)
	{
		department = value;
	}
	/** 
	 职位信息
	*/
	private String position;
	public final String getposition()
	{
		return position;
	}
	public final void setposition(String value)
	{
		position = value;
	}
	/** 
	 手机号码
	*/
	private String mobile;
	public final String getmobile()
	{
		return mobile;
	}
	public final void setmobile(String value)
	{
		mobile = value;
	}
	/** 
	 性别。0表示未定义，1表示男性，2表示女性
	*/
	private String gender;
	public final String getgender()
	{
		return gender;
	}
	public final void setgender(String value)
	{
		gender = value;
	}
	/** 
	 邮箱
	*/
	private String email;
	public final String getemail()
	{
		return email;
	}
	public final void setemail(String value)
	{
		email = value;
	}
	/** 
	 微信号
	*/
	private String weixinid;
	public final String getweixinid()
	{
		return weixinid;
	}
	public final void setweixinid(String value)
	{
		weixinid = value;
	}
	/** 
	 头像url。注：如果要获取小图将url最后的"/0"改成"/64"即可
	*/
	private String avatar;
	public final String getavatar()
	{
		return avatar;
	}
	public final void setavatar(String value)
	{
		avatar = value;
	}
	/** 
	 关注状态: 1=已关注，2=已冻结，4=未关注 
	*/
	private String status;
	public final String getstatus()
	{
		return status;
	}
	public final void setstatus(String value)
	{
		status = value;
	}
	/** 
	 扩展属性
	*/
	private String extattr;
	public final String getextattr()
	{
		return extattr;
	}
	public final void setextattr(String value)
	{
		extattr = value;
	}
}

	///#endregion

package BP.WF.WeiXin;

import BP.WF.*;
import java.util.*;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 组织结构

/** 
 部门列表
*/
public class DeptMent_GetList
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
	 部门列表数据
	*/
	private ArrayList<DeptMentInfo> department;
	public final ArrayList<DeptMentInfo> getdepartment()
	{
		return department;
	}
	public final void setdepartment(ArrayList<DeptMentInfo> value)
	{
		department = value;
	}
}
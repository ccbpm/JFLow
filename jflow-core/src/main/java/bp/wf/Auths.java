package bp.wf;

import bp.da.*;
import bp.web.*;
import bp.en.*;
import bp.port.*;
import bp.sys.*;
import java.util.*;

/** 
 授权
*/
public class Auths extends EntitiesMyPK
{
	/** 
	 授权
	*/
	public Auths()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Auth();
	}

		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Auth> ToJavaList()
	{
		return (List<Auth>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Auth> Tolist()
	{
		ArrayList<Auth> list = new ArrayList<Auth>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Auth)this.get(i));
		}
		return list;
	}
	public final void doDel(String Auther, String EmpNo, String TakeBackDT)throws Exception
	{
		String sql = "delete FROM WF_Auth where Auther = '" + Auther + "' and EmpNo = '" + EmpNo + "' and TakeBackDT = '" + TakeBackDT + "'";
		DBAccess.RunSQL(sql);
	}
		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
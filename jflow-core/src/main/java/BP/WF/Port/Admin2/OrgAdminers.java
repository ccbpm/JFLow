package BP.WF.Port.Admin2;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.*;
import BP.WF.Port.*;
import java.util.*;

/** 
 组织管理员s
*/
public class OrgAdminers extends EntitiesMM
{
		///#region 构造
	/** 
	 组织s
	*/
	public OrgAdminers()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new OrgAdminer();
	}
		///#endregion

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<OrgAdminer> ToJavaList()
	{
		return (List<OrgAdminer>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<OrgAdminer> Tolist()
	{
		ArrayList<OrgAdminer> list = new ArrayList<OrgAdminer>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((OrgAdminer)this.get(i));
		}
		return list;
	}
}
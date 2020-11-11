package bp.wf.port.admin2;

import bp.da.*;
import bp.web.*;
import bp.en.*;
import bp.port.*;
import bp.wf.*;
import bp.wf.port.*;
import java.util.*;

/** 
 组织管理员s
*/
public class OrgAdminers extends EntitiesMM
{

		///构造
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
	public Entity getGetNewEntity()
	{
		return new OrgAdminer();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
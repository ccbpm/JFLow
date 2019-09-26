package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 超连接s
*/
public class FrmLinks extends EntitiesMyPK
{

		///#region 构造
	/** 
	 超连接s
	*/
	public FrmLinks()
	{
	}
	/** 
	 超连接s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmLinks(String fk_mapdata) throws Exception
	{
		this.Retrieve(FrmLineAttr.FK_MapData, fk_mapdata);
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new FrmLink();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmLink> ToJavaList()
	{
		return (List<FrmLink>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmLink> Tolist()
	{
		ArrayList<FrmLink> list = new ArrayList<FrmLink>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmLink)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
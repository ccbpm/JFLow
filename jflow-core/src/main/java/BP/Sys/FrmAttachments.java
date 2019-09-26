package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 附件s
*/
public class FrmAttachments extends EntitiesMyPK
{

		///#region 构造
	/** 
	 附件s
	*/
	public FrmAttachments()
	{
	}
	/** 
	 附件s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmAttachments(String fk_mapdata) throws Exception
	{
		this.Retrieve(FrmAttachmentAttr.FK_MapData, fk_mapdata, FrmAttachmentAttr.FK_Node, 0);
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new FrmAttachment();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmAttachment> ToJavaList()
	{
		return (List<FrmAttachment>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmAttachment> Tolist()
	{
		ArrayList<FrmAttachment> list = new ArrayList<FrmAttachment>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmAttachment)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
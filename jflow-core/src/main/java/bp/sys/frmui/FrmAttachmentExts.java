package bp.sys.frmui;

import bp.en.*;
import bp.sys.*;
import java.util.*;

/** 
 附件s
*/
public class FrmAttachmentExts extends EntitiesMyPK
{

	private static final long serialVersionUID = 1L;
	///构造
	/** 
	 附件s
	*/
	public FrmAttachmentExts()
	{
	}
	/** 
	 附件s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmAttachmentExts(String fk_mapdata) throws Exception
	{
		this.Retrieve(FrmAttachmentAttr.FK_MapData, fk_mapdata, FrmAttachmentAttr.FK_Node, 0);
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmAttachmentExt();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmAttachment> ToJavaList()
	{
		return (java.util.List<FrmAttachment>)(Object)this;
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
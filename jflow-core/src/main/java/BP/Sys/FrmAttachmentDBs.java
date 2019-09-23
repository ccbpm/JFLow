package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;
import java.io.*;

/** 
 附件数据存储s
*/
public class FrmAttachmentDBs extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 附件数据存储s
	*/
	public FrmAttachmentDBs()
	{
	}
	/** 
	 附件数据存储s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmAttachmentDBs(String fk_mapdata, String pkval) throws Exception
	{
		this.Retrieve(FrmAttachmentDBAttr.FK_MapData, fk_mapdata, FrmAttachmentDBAttr.RefPKVal, pkval);
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new FrmAttachmentDB();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmAttachmentDB> ToJavaList()
	{
		return (List<FrmAttachmentDB>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmAttachmentDB> Tolist()
	{
		ArrayList<FrmAttachmentDB> list = new ArrayList<FrmAttachmentDB>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmAttachmentDB)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
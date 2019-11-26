package BP.Sys.FrmUI;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.En.*;
import BP.Sys.*;
import BP.Sys.*;
import java.util.*;

/** 
 装饰图片s
*/
public class ExtImgs extends EntitiesMyPK
{
	/** 
	 装饰图片s
	*/
	public ExtImgs()
	{
	}
	/** 
	 装饰图片s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public ExtImgs(String fk_mapdata) throws Exception
	{
		if (SystemConfig.getIsDebug())
		{
			this.Retrieve(FrmLineAttr.FK_MapData, fk_mapdata);
		}
		else
		{
			this.RetrieveFromCash(FrmLineAttr.FK_MapData, (Object)fk_mapdata);
		}
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new FrmImg();
	}
	// 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<ExtImg> ToJavaList()
	{
		return (List<ExtImg>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<ExtImg> Tolist()
	{
		ArrayList<ExtImg> list = new ArrayList<ExtImg>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((ExtImg)this.get(i));
		}
		return list;
	}
}
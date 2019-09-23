package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import java.util.*;

/** 
 图片s
*/
public class FrmImgs extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 图片s
	*/
	public FrmImgs()
	{
	}
	/** 
	 图片s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmImgs(String fk_mapdata) throws Exception
	{

	   this.Retrieve(FrmLineAttr.FK_MapData, fk_mapdata);

	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new FrmImg();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmImg> ToJavaList()
	{
		return (List<FrmImg>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmImg> Tolist()
	{
		ArrayList<FrmImg> list = new ArrayList<FrmImg>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmImg)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
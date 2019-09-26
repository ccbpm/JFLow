package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 线s
*/
public class FrmLines extends EntitiesMyPK
{

		///#region 构造
	/** 
	 线s
	*/
	public FrmLines()
	{
	}
	/** 
	 线s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmLines(String fk_mapdata) throws Exception
	{

		this.Retrieve(FrmLineAttr.FK_MapData, fk_mapdata);

	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new FrmLine();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmLine> ToJavaList()
	{
		return (List<FrmLine>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmLine> Tolist()
	{
		ArrayList<FrmLine> list = new ArrayList<FrmLine>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmLine)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 按钮s
*/
public class FrmBtns extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 按钮s
	*/
	public FrmBtns()
	{
	}
	/** 
	 按钮s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmBtns(String fk_mapdata) throws Exception
	{
		this.Retrieve(FrmLineAttr.FK_MapData, fk_mapdata);

	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new FrmBtn();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmBtn> ToJavaList()
	{
		return (List<FrmBtn>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmBtn> Tolist()
	{
		ArrayList<FrmBtn> list = new ArrayList<FrmBtn>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmBtn)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
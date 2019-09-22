package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 纬度报表s
*/
public class FrmRpts extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 纬度报表s
	*/
	public FrmRpts()
	{
	}
	/** 
	 纬度报表s
	 
	 @param fk_mapdata s
	*/
	public FrmRpts(String fk_mapdata)
	{
		this.Retrieve(FrmRptAttr.FK_MapData, fk_mapdata, FrmRptAttr.No);
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmRpt();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmRpt> ToJavaList()
	{
		return (List<FrmRpt>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmRpt> Tolist()
	{
		ArrayList<FrmRpt> list = new ArrayList<FrmRpt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmRpt)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
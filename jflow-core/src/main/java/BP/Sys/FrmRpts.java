package BP.Sys;

import java.util.ArrayList;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/** 
 纬度报表s
 
*/
public class FrmRpts extends EntitiesNoName
{

		
	/** 
	 纬度报表s
	 
	*/
	public FrmRpts()
	{
	}
	/** 
	 纬度报表s
	 
	 @param fk_mapdata s
	 * @throws Exception 
	*/
	public FrmRpts(String fk_mapdata) throws Exception
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
	public static ArrayList<FrmRpt> convertFrmRpts(Object obj)
	{
		return (ArrayList<FrmRpt>) obj;
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmRpt> ToJavaList()
	{
		return (java.util.List<FrmRpt>)(Object)this;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
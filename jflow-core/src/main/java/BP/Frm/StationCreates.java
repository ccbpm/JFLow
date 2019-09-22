package BP.Frm;

import BP.DA.*;
import BP.En.*;
import BP.WF.Port.*;
import java.util.*;

/** 
 单据可创建的工作岗位
*/
public class StationCreates extends EntitiesMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数.
	/** 
	 单据可创建的工作岗位
	*/
	public StationCreates()
	{
	}
	/** 
	 单据可创建的工作岗位
	 
	 @param nodeID 单据ID
	*/
	public StationCreates(int nodeID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(StationCreateAttr.FrmID, nodeID);
		qo.DoQuery();
	}
	/** 
	 单据可创建的工作岗位
	 
	 @param StationNo StationNo 
	*/
	public StationCreates(String StationNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(StationCreateAttr.FK_Station, StationNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new StationCreate();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造函数.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<StationCreate> ToJavaList()
	{
		return (List<StationCreate>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<StationCreate> Tolist()
	{
		ArrayList<StationCreate> list = new ArrayList<StationCreate>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((StationCreate)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
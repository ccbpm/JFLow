package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Port.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 表单字段方案s
*/
public class FrmFields extends EntitiesMyPK
{
	public FrmFields()
	{
	}
	/** 
	 查询
	*/
	public FrmFields(String fk_mapdata, int nodeID)
	{
		this.Retrieve(FrmFieldAttr.FK_MapData, fk_mapdata, FrmFieldAttr.FK_Node, nodeID,FrmFieldAttr.EleType, FrmEleType.Field);
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmField();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmField> ToJavaList()
	{
		return (List<FrmField>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmField> Tolist()
	{
		ArrayList<FrmField> list = new ArrayList<FrmField>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((FrmField)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
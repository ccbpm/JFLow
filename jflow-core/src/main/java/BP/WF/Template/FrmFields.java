package BP.WF.Template;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

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


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmField> ToJavaList()
	{
		return (java.util.List<FrmField>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<FrmField> Tolist()
	{
		java.util.ArrayList<FrmField> list = new java.util.ArrayList<FrmField>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmField)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
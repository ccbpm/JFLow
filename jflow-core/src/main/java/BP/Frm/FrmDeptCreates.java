package BP.Frm;

import BP.DA.*;
import BP.En.*;
import BP.WF.Port.*;
import java.util.*;

/** 
 单据可创建的部门
*/
public class FrmDeptCreates extends EntitiesMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数.
	/** 
	 单据可创建的部门
	*/
	public FrmDeptCreates()
	{
	}
	/** 
	 单据可创建的部门
	 
	 @param nodeID 单据ID
	*/
	public FrmDeptCreates(int nodeID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FrmDeptCreateAttr.FrmID, nodeID);
		qo.DoQuery();
	}
	/** 
	 单据可创建的部门
	 
	 @param StationNo StationNo 
	*/
	public FrmDeptCreates(String StationNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FrmDeptCreateAttr.FK_Dept, StationNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new FrmDeptCreate();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造函数.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmDeptCreate> ToJavaList()
	{
		return (List<FrmDeptCreate>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmDeptCreate> Tolist()
	{
		ArrayList<FrmDeptCreate> list = new ArrayList<FrmDeptCreate>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmDeptCreate)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
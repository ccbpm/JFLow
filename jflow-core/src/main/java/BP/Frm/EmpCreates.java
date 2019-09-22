package BP.Frm;

import BP.DA.*;
import BP.En.*;
import BP.WF.Port.*;
import java.util.*;

/** 
 单据可创建的人员
*/
public class EmpCreates extends EntitiesMM
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数.
	/** 
	 单据可创建的人员
	*/
	public EmpCreates()
	{
	}
	/** 
	 单据可创建的人员
	 
	 @param NodeID 表单IDID
	*/
	public EmpCreates(int NodeID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(EmpCreateAttr.FrmID, NodeID);
		qo.DoQuery();
	}
	/** 
	 单据可创建的人员
	 
	 @param EmpNo EmpNo 
	*/
	public EmpCreates(String EmpNo)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(EmpCreateAttr.FK_Emp, EmpNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new EmpCreate();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造函数.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<EmpCreate> ToJavaList()
	{
		return (List<EmpCreate>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<EmpCreate> Tolist()
	{
		ArrayList<EmpCreate> list = new ArrayList<EmpCreate>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((EmpCreate)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
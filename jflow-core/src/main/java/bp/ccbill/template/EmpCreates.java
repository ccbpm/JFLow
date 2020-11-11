package bp.ccbill.template;
import bp.en.*;
import java.util.*;

/** 
 单据可创建的人员
*/
public class EmpCreates extends EntitiesMM
{
	private static final long serialVersionUID = 1L;
		///构造函数.
	/** 
	 单据可创建的人员
	*/
	public EmpCreates()
	{
	}
	/** 
	 单据可创建的人员
	 
	 @param NodeID 表单IDID
	 * @throws Exception 
	*/
	public EmpCreates(int NodeID) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(EmpCreateAttr.FrmID, NodeID);
		qo.DoQuery();
	}
	/** 
	 单据可创建的人员
	 
	 @param EmpNo EmpNo 
	 * @throws Exception 
	*/
	public EmpCreates(String EmpNo) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(EmpCreateAttr.FK_Emp, EmpNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new EmpCreate();
	}

		/// 构造函数.


		///为了适应自动翻译成java的需要,把实体转换成List.
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
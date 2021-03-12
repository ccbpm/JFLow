package bp.ccbill.template;
import bp.en.*;
import java.util.*;

/** 
 单据可创建的部门
*/
public class FrmDeptCreates extends EntitiesMM
{
	private static final long serialVersionUID = 1L;
	///构造函数.
	/** 
	 单据可创建的部门
	*/
	public FrmDeptCreates()
	{
	}
	/** 
	 单据可创建的部门
	 
	 @param nodeID 单据ID
	 * @throws Exception 
	*/
	public FrmDeptCreates(int nodeID) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FrmDeptCreateAttr.FrmID, nodeID);
		qo.DoQuery();
	}
	/** 
	 单据可创建的部门
	 
	 @param StationNo StationNo 
	 * @throws Exception 
	*/
	public FrmDeptCreates(String StationNo) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FrmDeptCreateAttr.FK_Dept, StationNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmDeptCreate();
	}

		/// 构造函数.


		///为了适应自动翻译成java的需要,把实体转换成List.
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
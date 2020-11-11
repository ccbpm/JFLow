package bp.ccbill;

import bp.da.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.template.*;
import bp.sys.*;
import bp.ccbill.template.*;
import java.util.*;

/** 
 单据控制表s
*/
public class GenerBills extends Entities
{

		///构造
	/** 
	 单据控制表s
	*/
	public GenerBills()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new GenerBill();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GenerBill> ToJavaList()
	{
		return (List<GenerBill>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GenerBill> Tolist()
	{
		ArrayList<GenerBill> list = new ArrayList<GenerBill>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GenerBill)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
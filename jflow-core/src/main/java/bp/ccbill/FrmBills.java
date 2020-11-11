package bp.ccbill;

import bp.da.*;
import bp.en.*;
import bp.wf.*;
import bp.wf.data.*;
import bp.wf.template.*;
import bp.sys.*;
import bp.ccbill.template.*;
import java.util.*;
import java.time.*;

/** 
 单据属性s
*/
public class FrmBills extends EntitiesNoName
{

		///构造
	/** 
	 单据属性s
	*/
	public FrmBills()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmBill();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmBill> ToJavaList()
	{
		return (List<FrmBill>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmBill> Tolist()
	{
		ArrayList<FrmBill> list = new ArrayList<FrmBill>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmBill)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
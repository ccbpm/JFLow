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
 实体表单s
*/
public class FrmDicts extends EntitiesNoName
{

		///构造
	/** 
	 实体表单s
	*/
	public FrmDicts()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmDict();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmDict> ToJavaList()
	{
		return (List<FrmDict>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmDict> Tolist()
	{
		ArrayList<FrmDict> list = new ArrayList<FrmDict>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmDict)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
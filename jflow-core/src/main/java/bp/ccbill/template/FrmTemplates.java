package bp.ccbill.template;

import bp.da.*;
import bp.en.*;
import bp.gpm.*;
import bp.wf.*;
import bp.wf.data.*;
import bp.wf.template.*;
import bp.sys.*;
import bp.port.*;
import bp.ccbill.*;
import java.util.*;

/** 
 单据模版s
*/
public class FrmTemplates extends EntitiesNoName
{

		///构造
	/** 
	 单据模版s
	*/
	public FrmTemplates()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmTemplate();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmTemplate> ToJavaList()
	{
		return (List<FrmTemplate>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmTemplate> Tolist()
	{
		ArrayList<FrmTemplate> list = new ArrayList<FrmTemplate>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmTemplate)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
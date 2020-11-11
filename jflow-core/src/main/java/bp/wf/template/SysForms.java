package bp.wf.template;

import bp.da.*;
import bp.sys.*;
import bp.en.*;
import bp.wf.port.*;
import bp.wf.template.*;
import bp.wf.*;
import bp.wf.*;
import java.util.*;

/** 
 系统表单s
*/
public class SysForms extends EntitiesNoName
{
	/** 
	 Frm
	*/
	public SysForms()
	{

	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SysForm();
	}


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SysForm> ToJavaList()
	{
		return (List<SysForm>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SysForm> Tolist()
	{
		ArrayList<SysForm> list = new ArrayList<SysForm>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SysForm)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}
package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Port.*;
import BP.WF.Template.*;
import BP.WF.*;
import BP.WF.*;
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

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SysForm> ToJavaList()
	{
		return (List<SysForm>)this;
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
			list.add((SysForm)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
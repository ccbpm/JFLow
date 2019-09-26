package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 公文属性控制s
*/
public class BtnLabExtWebOffices extends Entities
{
	/** 
	 公文属性控制s
	*/
	public BtnLabExtWebOffices()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new BtnLabExtWebOffice();
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<BtnLabExtWebOffice> ToJavaList()
	{
		return (List<BtnLabExtWebOffice>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<BtnLabExtWebOffice> Tolist()
	{
		ArrayList<BtnLabExtWebOffice> list = new ArrayList<BtnLabExtWebOffice>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((BtnLabExtWebOffice)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
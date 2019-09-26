package BP.GPM;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import java.util.*;

/** 
 人员菜单功能s
*/
public class VGPMEmpMenus extends EntitiesMyPK
{

		///#region 构造
	/** 
	 菜单s
	*/
	public VGPMEmpMenus()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new VGPMEmpMenu();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<VGPMEmpMenu> ToJavaList()
	{
		return (List<VGPMEmpMenu>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<VGPMEmpMenu> Tolist()
	{
		ArrayList<VGPMEmpMenu> list = new ArrayList<VGPMEmpMenu>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((VGPMEmpMenu)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
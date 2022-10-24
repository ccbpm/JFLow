package bp.wf.admin;

import bp.da.*;
import bp.port.*;
import bp.en.*;
import bp.web.*;
import bp.sys.*;
import bp.wf.data.*;
import bp.wf.template.frm.*;
import java.util.*;

/** 
 流程集合
*/
public class Frms extends EntitiesNoName
{


		///#region 构造方法
	/** 
	 工作流程
	*/
	public Frms()
	{
	}

		///#endregion


		///#region 得到实体
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Frm();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Frm> ToJavaList()
	{
		return (List<Frm>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Frm> Tolist()
	{
		ArrayList<Frm> list = new ArrayList<Frm>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Frm)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
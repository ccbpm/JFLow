package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 配件s
*/
public class Parts extends EntitiesMyPK
{

		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new Part();
	}

		///#endregion


		///#region 构造方法
	/** 
	 配件集合
	*/
	public Parts()
	{
	}
	/** 
	 配件集合.
	 
	 @param FlowNo
	 * @throws Exception 
	*/
	public Parts(String fk_flow) throws Exception
	{
		this.Retrieve(PartAttr.FK_Flow, fk_flow);
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Part> ToJavaList()
	{
		return (List<Part>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Part> Tolist()
	{
		ArrayList<Part> list = new ArrayList<Part>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Part)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
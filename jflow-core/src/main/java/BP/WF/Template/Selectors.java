package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Port.*;
import BP.Web.*;
import BP.WF.*;
import java.util.*;

/** 
 Accpter
*/
public class Selectors extends Entities
{
	/** 
	 Accpter
	*/
	public Selectors()
	{
	}

	public Selectors(String fk_flow)
	{
		String sql = "select NodeId from WF_Node where FK_Flow='" + fk_flow + "'";
		this.RetrieveInSQL(sql);
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Selector();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Selector> ToJavaList()
	{
		return (List<Selector>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Selector> Tolist()
	{
		ArrayList<Selector> list = new ArrayList<Selector>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Selector)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
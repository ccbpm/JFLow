package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 抄送
*/
public class CCLists extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new CCList();
	}
	/** 
	 抄送
	*/
	public CCLists()
	{
	}


	/** 
	 查询出来所有的抄送信息
	 
	 @param flowNo
	 @param workid
	 @param fid
	*/
	public CCLists(String flowNo, long workid, long fid)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(CCListAttr.FK_Flow, flowNo);
		qo.addAnd();
		if (fid != 0)
		{
			qo.AddWhereIn(CCListAttr.WorkID, "(" + workid + "," + fid + ")");
		}
		else
		{
			qo.AddWhere(CCListAttr.WorkID, workid);
		}
		qo.DoQuery();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<CCList> ToJavaList()
	{
		return (List<CCList>)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<CCList> Tolist()
	{
		ArrayList<CCList> list = new ArrayList<CCList>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((CCList)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
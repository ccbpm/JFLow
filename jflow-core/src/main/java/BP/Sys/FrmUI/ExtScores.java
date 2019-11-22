package BP.Sys.FrmUI;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

import java.util.ArrayList;
import java.util.List;

/** 
 评分控件
*/
public class ExtScores extends EntitiesMyPK
{

		///#region 构造
	/**
	 评分控件s
	*/
	public ExtScores()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new ExtScore();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 @return List
	*/
	public final List<ExtScore> ToJavaList()
	{
		return (List<ExtScore>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<ExtScore> Tolist()
	{
		ArrayList<ExtScore> list = new ArrayList<ExtScore>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((ExtScore)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
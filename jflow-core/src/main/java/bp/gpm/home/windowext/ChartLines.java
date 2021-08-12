package bp.gpm.home.windowext;

import java.util.ArrayList;
import java.util.List;

import bp.en.EntitiesNoName;
import bp.en.Entity;

/** 
折线图s
*/
public class ChartLines extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 构造
	/** 
	 折线图s
	*/
	public ChartLines()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new ChartLine();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<ChartLine> ToJavaList()
	{
		return (List<ChartLine>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<ChartLine> Tolist()
	{
		ArrayList<ChartLine> list = new ArrayList<ChartLine>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((ChartLine)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}

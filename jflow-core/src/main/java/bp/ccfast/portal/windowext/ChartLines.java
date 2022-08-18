package bp.ccfast.portal.windowext;

import bp.en.*;
import java.util.*;

/** 
 折线图s
*/
public class ChartLines extends EntitiesNoName
{

		///#region 构造
	/** 
	 折线图s
	*/
	public ChartLines()  {
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity() {
		return new ChartLine();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<ChartLine> ToJavaList() {
		return (java.util.List<ChartLine>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<ChartLine> Tolist() {
		ArrayList<ChartLine> list = new ArrayList<ChartLine>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((ChartLine)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
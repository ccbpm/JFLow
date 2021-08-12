package bp.gpm.home.windowext;

import java.util.*;

import bp.en.EntitiesNoName;
import bp.en.Entity;

/** 
	 百分比扇形图s
*/
	public class ChartRates extends EntitiesNoName
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
		/** 
		 百分比扇形图s
		*/
		public ChartRates()
		{
		}
		/** 
		 得到它的 Entity
		*/
		@Override
		public Entity getGetNewEntity()
		{
			return new ChartRate();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
		/** 
		 转化成 java list,C#不能调用.
		 
		 @return List
		*/
		public final List<ChartRate> ToJavaList()
		{
			return (List<ChartRate>)(Object)this;
		}
		/** 
		 转化成list
		 
		 @return List
		*/
		public final ArrayList<ChartRate> Tolist()
		{
			ArrayList<ChartRate> list = new ArrayList<ChartRate>();
			for (int i = 0; i < this.size(); i++)
			{
				list.add((ChartRate)this.get(i));
			}
			return list;
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
	}

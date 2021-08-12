package bp.gpm.home.windowext;

import java.util.*;

import bp.en.EntitiesNoName;
import bp.en.Entity;

/** 
	 环形图s
*/
	public class ChartRings extends EntitiesNoName
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
		/** 
		 环形图s
		*/
		public ChartRings()
		{
		}
		/** 
		 得到它的 Entity
		*/
		@Override
		public Entity getGetNewEntity()
		{
			return new ChartRing();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
		/** 
		 转化成 java list,C#不能调用.
		 
		 @return List
		*/
		public final List<ChartRing> ToJavaList()
		{
			return (List<ChartRing>)(Object)this;
		}
		/** 
		 转化成list
		 
		 @return List
		*/
		public final ArrayList<ChartRing> Tolist()
		{
			ArrayList<ChartRing> list = new ArrayList<ChartRing>();
			for (int i = 0; i < this.size(); i++)
			{
				list.add((ChartRing)this.get(i));
			}
			return list;
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
	}

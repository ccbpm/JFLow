package bp.wf.data;

import java.util.ArrayList;
import java.util.List;

import bp.en.EntitiesNoName;
import bp.en.Entity;

public class AutoRpts extends EntitiesNoName{

	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
		/** 
		 得到它的 Entity 
		*/
		@Override
		public Entity getGetNewEntity()
		{
			return new AutoRpt();
		}
		/** 
		 自动报表
		*/
//C# TO JAVA CONVERTER WARNING: The following constructor is declared outside of its associated class:
//ORIGINAL LINE: public AutoRpts()
		public AutoRpts()
		{
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
		/** 
		 转化成 java list,C#不能调用.
		 
		 @return List
		*/
		public final List<AutoRpt> ToJavaList()
		{
			return (List<AutoRpt>)(Object)this;
		}
		/** 
		 转化成list
		 
		 @return List
		*/
		public final ArrayList<AutoRpt> Tolist()
		{
			ArrayList<AutoRpt> list = new ArrayList<AutoRpt>();
			for (int i = 0; i < this.size(); i++)
			{
				list.add((AutoRpt)this.get(i));
			}
			return list;
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}

package bp.gpm.home.windowext;

import java.util.ArrayList;
import java.util.List;

import bp.en.EntitiesNoName;
import bp.en.Entity;

/** 
	 表格s
*/
	public class Tables extends EntitiesNoName
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
		/** 
		 表格s
		*/
		public Tables()
		{
		}
		/** 
		 得到它的 Entity
		*/
		@Override
		public Entity getGetNewEntity()
		{
			return new Table();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
		/** 
		 转化成 java list,C#不能调用.
		 
		 @return List
		*/
		public final List<Table> ToJavaList()
		{
			return (List<Table>)(Object)this;
		}
		/** 
		 转化成list
		 
		 @return List
		*/
		public final ArrayList<Table> Tolist()
		{
			ArrayList<Table> list = new ArrayList<Table>();
			for (int i = 0; i < this.size(); i++)
			{
				list.add((Table)this.get(i));
			}
			return list;
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
	}

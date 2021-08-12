package bp.gpm.home.windowext;

import java.util.ArrayList;
import java.util.List;

import bp.en.EntitiesMyPK;
import bp.en.Entity;

/** 
	 变量信息s
*/
	public class TabDtls extends EntitiesMyPK
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
		/** 
		 变量信息s
		*/
		public TabDtls()
		{
		}
		/** 
		 得到它的 Entity
		*/
		@Override
		public Entity getGetNewEntity()
		{
			return new TabDtl();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
		/** 
		 转化成 java list,C#不能调用.
		 
		 @return List
		*/
		public final List<TabDtl> ToJavaList()
		{
			return (List<TabDtl>)(Object)this;
		}
		/** 
		 转化成list
		 
		 @return List
		*/
		public final ArrayList<TabDtl> Tolist()
		{
			ArrayList<TabDtl> list = new ArrayList<TabDtl>();
			for (int i = 0; i < this.size(); i++)
			{
				list.add((TabDtl)this.get(i));

			}
			return list;
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
	}

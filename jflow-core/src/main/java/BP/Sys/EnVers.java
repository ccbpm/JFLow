package BP.Sys;

import java.util.List;

import BP.En.EntitiesMyPK;
import BP.En.Entity;
/** 
实体版本号s
 
*/
public class EnVers extends EntitiesMyPK{
	
		
			/** 
			 得到一个新实体
			 
			*/
			@Override
			public Entity getGetNewEntity()
			{
				return new EnVer();
			}
			/** 
			 实体版本号集合
			 
			*/
			public EnVers()
			{
			}

	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 为了适应自动翻译成java的需要,把实体转换成List.
			/** 
			 转化成 java list,C#不能调用.
			 
			 @return List
			*/
			public final java.util.List<EnVer> ToJavaList()
			{
				return (List<EnVer>)(Object)this;
			}
			/** 
			 转化成list
			 
			 @return List
			*/
			public final java.util.ArrayList<EnVer> Tolist()
			{
				java.util.ArrayList<EnVer> list = new java.util.ArrayList<EnVer>();
				for (int i = 0; i < this.size(); i++)
				{
					list.add((EnVer)this.get(i));
				}
				return list;
			}
	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
		
}

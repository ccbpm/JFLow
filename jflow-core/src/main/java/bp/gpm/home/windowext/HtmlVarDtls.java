package bp.gpm.home.windowext;

import java.util.*;

import bp.en.EntitiesMyPK;
import bp.en.Entity;

public class HtmlVarDtls extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
		/** 
		 变量信息s
		*/
		public HtmlVarDtls()
		{
		}
		/** 
		 得到它的 Entity
		*/
		@Override
		public Entity getGetNewEntity()
		{
			return new HtmlVarDtl();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
		/** 
		 转化成 java list,C#不能调用.
		 
		 @return List
		*/
		public final List<HtmlVarDtl> ToJavaList()
		{
			return (List<HtmlVarDtl>)(Object)this;
		}
		/** 
		 转化成list
		 
		 @return List
		*/
		public final ArrayList<HtmlVarDtl> Tolist()
		{
			ArrayList<HtmlVarDtl> list = new ArrayList<HtmlVarDtl>();
			for (int i = 0; i < this.size(); i++)
			{
				list.add((HtmlVarDtl)this.get(i));

			}
			return list;
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}


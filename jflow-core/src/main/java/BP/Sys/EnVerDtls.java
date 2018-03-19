package BP.Sys;

import java.util.List;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
部门岗位对应 

*/
public class EnVerDtls extends EntitiesMyPK{
	
	

		///#region 构造

		public EnVerDtls()
		{
		}

		public EnVerDtls(String enVerPK)
		{
			this.Retrieve(EnVerDtlAttr.EnVerPK, enVerPK);
		}

		public EnVerDtls(String enVerPK, int ver)
		{
			this.Retrieve(EnVerDtlAttr.EnVerPK, enVerPK, EnVerDtlAttr.EnVer, ver);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
		/** 
		 得到它的 Entity 
		 
		*/
		@Override
		public Entity getGetNewEntity()
		{
			return new EnVerDtl();
		}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
		/** 
		 转化成 java list,C#不能调用.
		 
		 @return List
		*/
		public final java.util.List<EnVerDtl> ToJavaList()
		{
			return (List<EnVerDtl>)(Object)this;
		}
		/** 
		 转化成list
		 
		 @return List
		*/
		public final java.util.ArrayList<EnVerDtl> Tolist()
		{
			java.util.ArrayList<EnVerDtl> list = new java.util.ArrayList<EnVerDtl>();
			for (int i = 0; i < this.size(); i++)
			{
				list.add((EnVerDtl)this.get(i));
			}
			return list;
		}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

	
}

package BP.WF.Port;

import BP.En.EntitiesNoName;
import BP.En.Entity;

public class Incs extends EntitiesNoName
	{
		/** 
		 得到一个新实体
		 
		*/
		@Override
		public Entity getGetNewEntity()
		{
			return new Inc();
		}
		/** 
		 create ens
		 
		*/
		public Incs()
		{
		}

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
		/** 
		 转化成 java list,C#不能调用.
		 
		 @return List
		*/
		public final java.util.List<Inc> ToJavaList()
		{
			return (java.util.List<Inc>)(Object)this;
		}
		/** 
		 转化成list
		 
		 @return List
		*/
		public final java.util.ArrayList<Inc> Tolist()
		{
			java.util.ArrayList<Inc> list = new java.util.ArrayList<Inc>();
			for (int i = 0; i < this.size(); i++)
			{
				list.add((Inc)this.get(i));
			}
			return list;
		}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
	}
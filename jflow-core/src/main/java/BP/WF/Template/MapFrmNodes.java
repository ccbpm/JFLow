package BP.WF.Template;

import BP.En.EntitiesNoName;
import BP.En.Entity;

public class MapFrmNodes extends EntitiesNoName{
	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 构造
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		/** 
		 自由表单属性s
		 
		*/
		public MapFrmNodes()
		{
		}
		/** 
		 得到它的 Entity
		 
		*/
		@Override
		public Entity getGetNewEntity()
		{
			return new MapFrmNode();
		}
	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

	//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 为了适应自动翻译成java的需要,把实体转换成List.
		/** 
		 转化成 java list,C#不能调用.
		 
		 @return List
		*/
		public final java.util.List<MapFrmNode> ToJavaList()
		{
			return (java.util.List<MapFrmNode>)(Object)this;
		}
		/** 
		 转化成list
		 
		 @return List
		*/
		public final java.util.ArrayList<MapFrmNode> Tolist()
		{
			java.util.ArrayList<MapFrmNode> list = new java.util.ArrayList<MapFrmNode>();
			for (int i = 0; i < this.size(); i++)
			{
				list.add((MapFrmNode)this.get(i));
			}
			return list;
		}
}

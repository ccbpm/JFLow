package BP.WF.Template;

import BP.En.EntitiesOID;
import BP.En.Entity;
import BP.WF.Flow;

  /** 
	 延续子流程集合
	 
  */
	public class NodeYGFlows extends EntitiesOID{
	private static final long serialVersionUID = 1L;

		//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法
		/** 
		 得到它的 Entity 
		 
		*/
		@Override
		public Entity getGetNewEntity()
		{
			return new NodeYGFlow();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
		/** 
		 延续子流程集合
		 
		*/
		public NodeYGFlows()
		{
		}
		/** 
		 延续子流程集合.
		 
		 @param fk_node
		 * @throws Exception 
		*/
		public NodeYGFlows(String fk_node) throws Exception
		{
			this.Retrieve(NodeYGFlowAttr.FK_Node, fk_node);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List
		/** 
		 转化成 java list,C#不能调用.
		 
		 @return List
		*/
		public final java.util.List<NodeYGFlow> ToJavaList()
		{
			//return (java.util.List<NodeYGFlow>)this;
			return (java.util.List<NodeYGFlow>)(Object)this;
		}

		/** 
		 转化成list
		 
		 @return List
		*/
		public final java.util.ArrayList<NodeYGFlow> Tolist()
		{
			java.util.ArrayList<NodeYGFlow> list = new java.util.ArrayList<NodeYGFlow>();
			for (int i = 0; i < this.size(); i++)
			{
				list.add((NodeYGFlow)this.get(i));
			}
			return list;
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
	}
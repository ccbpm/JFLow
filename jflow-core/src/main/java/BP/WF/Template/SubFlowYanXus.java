package BP.WF.Template;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

  /** 
	 延续子流程集合
	 
  */
	public class SubFlowYanXus extends EntitiesMyPK{
	private static final long serialVersionUID = 1L;

		@Override
		public Entity getGetNewEntity()
		{
			return new SubFlowYanXu();
		}
		/** 
		 延续子流程集合
		*/
		public SubFlowYanXus()
		{
		}
		
		/** 
		 延续子流程集合.
		 
		 @param fk_node
		 * @throws Exception 
		*/
		public SubFlowYanXus(String fk_node) throws Exception
		{
			this.Retrieve(SubFlowYanXuAttr.FK_Node, fk_node);
		}
		/** 
		 转化成 java list,C#不能调用.
		 
		 @return List
		*/
		public final java.util.List<SubFlowYanXu> ToJavaList()
		{
			return (java.util.List<SubFlowYanXu>)(Object)this;
		}

		/** 
		 转化成list
		 
		 @return List
		*/
		public final java.util.ArrayList<SubFlowYanXu> Tolist()
		{
			java.util.ArrayList<SubFlowYanXu> list = new java.util.ArrayList<SubFlowYanXu>();
			for (int i = 0; i < this.size(); i++)
			{
				list.add((SubFlowYanXu)this.get(i));
			}
			return list;
		}
	}
package bp.wf.template;

/** 
 鎵嬪伐鍚姩瀛愭祦绋嬪睘鎬�
*/
public class SubFlowHandGuideAttr extends SubFlowHandAttr  
{
	  /** 
		 是否启用子流程发起前置导航.
	  */
		public static final String IsSubFlowGuide = "IsSubFlowGuide";
		/**
		 * 是否是树形结构
		 */
		public static final String IsTreeConstruct="IsTreeConstruct";
		/**
		 * 父节点编码
		 */
		public static final String  ParentNo="ParentNo";
		/** 
		 SQL 前置导航列表
		*/
		public static final String SubFlowGuideSQL = "SubFlowGuideSQL";
		/** 
		 编号字段.
		*/
		public static final String SubFlowGuideEnNoFiled = "SubFlowGuideEnNoFiled";
		/** 
		 名称字段
		*/
		public static final String SubFlowGuideEnNameFiled = "SubFlowGuideEnNameFiled";


}
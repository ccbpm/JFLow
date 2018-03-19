package BP.WF.Entity;

import BP.En.EntityNoAttr;

/**
 * 审核组件
 */
public class FrmWorkCheckAttr extends EntityNoAttr
{
	/**
	 * 是否可以审批
	 */
	public static final String FWCSta = "FWCSta";
	/**
	 * X
	 */
	public static final String FWC_X = "FWC_X";
	
	public static final String FWCFields = "FWCFields";
	
	/**
	 * Y
	 */
	public static final String FWC_Y = "FWC_Y";
	/**
	 * H
	 */
	public static final String FWC_H = "FWC_H";
	/**
	 * W
	 */
	public static final String FWC_W = "FWC_W";
	/**
	 * 应用类型
	 */
	public static final String FWCType = "FWCType";
	/**
	 * 附件
	 */
	public static final String FWCAth = "FWCAth";
	/**
	 * 显示方式.
	 */
	public static final String FWCShowModel = "FWCShowModel";
	/**
	 * 轨迹图是否显示?
	 */
	public static final String FWCTrackEnable = "FWCTrackEnable";
	/**
	 * 历史审核信息是否显示?
	 */
	public static final String FWCListEnable = "FWCListEnable";
	/**
	 * 是否显示所有的步骤？
	 */
	public static final String FWCIsShowAllStep = "FWCIsShowAllStep";
	/**
	 * 默认审核信息
	 */
	public static final String FWCDefInfo = "FWCDefInfo";
	/**
	 * 节点意见名称
	 */
	public static final String FWCNodeName = "FWCNodeName";
	/**
	 * 如果用户未审核是否按照默认意见填充？
	 */
	public static final String FWCIsFullInfo = "FWCIsFullInfo";
	/**
	 * 操作名词(审核，审定，审阅，批示)
	 */
	public static final String FWCOpLabel = "FWCOpLabel";
	/**
	 * 操作人是否显示数字签名
	 */
	public static final String SigantureEnabel = "SigantureEnabel";
}
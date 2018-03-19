package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.*;

/** 
 父子流程
 
*/
public class FrmSubFlowAttr extends EntityNoAttr
{
	/** 
	 状态
	 
	*/
	public static final String SFSta = "SFSta";
	/** 
	 X
	 
	*/
	public static final String SF_X = "SF_X";
	/** 
	 Y
	 
	*/
	public static final String SF_Y = "SF_Y";
	/** 
	 H
	 
	*/
	public static final String SF_H = "SF_H";
	/** 
	 W
	 
	*/
	public static final String SF_W = "SF_W";
	/** 
	 应用类型
	 
	*/
	public static final String SFType = "SFType";
	/** 
	 附件
	 
	*/
	public static final String SFAth = "SFAth";
	/** 
	 显示方式.
	 
	*/
	public static final String SFShowModel = "SFShowModel";
	/** 
	 轨迹图是否显示?
	 
	*/
	public static final String SFTrackEnable = "SFTrackEnable";
	/** 
	 历史审核信息是否显示?
	 
	*/
	public static final String SFListEnable = "SFListEnable";
	/** 
	 是否显示所有的步骤？
	 
	*/
	public static final String SFIsShowAllStep = "SFIsShowAllStep";
	/** 
	 默认审核信息
	 
	*/
	public static final String SFDefInfo = "SFDefInfo";
	/** 
	 触发的流程
	 
	*/
	public static final String SFActiveFlows = "SFActiveFlows";
	/** 
	 标题
	 
	*/
	public static final String SFCaption = "SFCaption";
	/** 
	 如果用户未审核是否按照默认意见填充？
	 
	*/
	public static final String SFIsFullInfo = "SFIsFullInfo";
	/** 
	 操作名词(审核，审定，审阅，批示)
	 
	*/
	public static final String SFOpLabel = "SFOpLabel";
	/** 
	 操作人是否显示数字签名
	 
	*/
	public static final String SigantureEnabel = "SigantureEnabel";
	/** 
	 操作字段
	 
	*/
	public static final String SFFields = "SFFields";
	/** 
	 显示控制方式
	*/
	public static final String SFShowCtrl = "SFShowCtrl";
}
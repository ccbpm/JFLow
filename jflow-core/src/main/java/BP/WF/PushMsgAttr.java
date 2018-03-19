package BP.WF;


/**
 * 消息推送属性
 */
public class PushMsgAttr
{
	 /** 
	 流程编号
	 
*/
	public static final String FK_Flow = "FK_Flow";
	/**
	 * 节点
	 */
	public static final String FK_Node = "FK_Node";
	/**
	 * 事件
	 */
	public static final String FK_Event = "FK_Event";
	/**
	 * 推送方式
	 */
	public static final String PushWay = "PushWay";
	/**
	 * 推送处理内容
	 */
	public static final String PushDoc = "PushDoc";
	/**
	 * 推送处理内容 tag.
	 */
	public static final String Tag = "Tag";
	
	// 消息设置.
	/**
	 * 是否启用发送邮件
	 */
	public static final String MsgMailEnable = "MsgMailEnable";
	/**
	 * 消息标题
	 */
	public static final String MailTitle = "MailTitle";
	/**
	 * 消息内容模版
	 */
	public static final String MailDoc = "MailDoc";
	/**
	 * 是否启用短信
	 */
	public static final String SMSEnable = "SMSEnable";
	/**
	 * 短信内容模版
	 */
	public static final String SMSDoc = "SMSDoc";
	/** 
	 邮件字段
	 
	*/
	public static final String MailAddress = "MailAddress";
	/**
	 * 是否推送？
	 */
	public static final String MobilePushEnable = "MobilePushEnable";
	// 消息设置.
}
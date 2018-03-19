package BP.WF.Template;


/** 
 消息推送属性
 
*/
public class PushMsgAttr
{
	/** 
	 流程编号
	 
	*/
	public static final String FK_Flow = "FK_Flow";
	/** 
	 节点
	 
	*/
	public static final String FK_Node = "FK_Node";
	/** 
	 事件
	 
	*/
	public static final String FK_Event = "FK_Event";
	/** 
	 推送方式
	 
	*/
	public static final String PushWay = "PushWay";
	/** 
	 推送处理内容
	 
	*/
	public static final String PushDoc = "PushDoc";
	/** 
	 推送处理内容 tag.
	 
	*/
	public static final String Tag = "Tag";


		///#region 消息设置.
	/** 
	 是否启用发送邮件
	 
	*/
	public static final String MailEnable = "MailEnable";
	/** 
	 消息标题
	 
	*/
	public static final String MailTitle = "MailTitle";
	/** 
	 消息内容模版
	 
	*/
	public static final String MailDoc = "MailDoc";
	/** 
	 是否启用短信
	 
	*/
	public static final String SMSEnable = "SMSEnable";
	/** 
	 短信内容模版
	 
	*/
	public static final String SMSDoc = "SMSDoc";
	/** 
	 是否推送？
	 
	*/
	public static final String MobilePushEnable = "MobilePushEnable";

		///#endregion 消息设置.

	/** 
	 短信字段
	 
	*/
	public static final String SMSField = "SMSField";
	/** 
	 接受短信的节点.
	 
	*/
	public static final String SMSNodes = "SMSNodes";
	/** 
	 推送方式
	 
	*/
	public static final String SMSPushWay = "SMSPushWay";
	/** 
	 邮件字段
	 
	*/
	public static final String MailAddress = "MailAddress";
	/** 
	 邮件推送方式
	 
	*/
	public static final String MailPushWay = "MailPushWay";
	/** 
	 推送邮件的节点s
	 
	*/
	public static final String MailNodes = "MailNodes";

}
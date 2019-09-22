package BP.WF.WeiXin;

import BP.WF.*;
import java.util.*;

/** 
 微信-文本消息
*/
public class WX_Msg_Text extends WX_MsgBase
{
	/** 
	 必须：是- 消息类型，此时固定为：text （支持消息型应用跟主页型应用） 
	*/
	public final String getmsgtype()
	{
		return "text";
	}
	/** 
	 必须：是- 消息内容，最长不超过2048个字节，注意：主页型应用推送的文本消息在微信端最多只显示20个字（包含中英文）
	*/
	private String content;
	public final String getcontent()
	{
		return content;
	}
	public final void setcontent(String value)
	{
		content = value;
	}

	/** 
	 表示是否是保密消息
	*/
	private String _Safe = "0";
	/** 
	 必须：否- 表示是否是保密消息，0表示否，1表示是，默认0
	*/
	public final String getsafe()
	{
		return this._Safe;
	}
	public final void setsafe(String value)
	{
		this._Safe = value;
	}
}
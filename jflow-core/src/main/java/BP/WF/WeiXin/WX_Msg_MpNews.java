package BP.WF.WeiXin;

import BP.WF.*;
import java.util.*;


	///#endregion


	///#region mpnews消息

/** 
 微信-mpnews消息-mpnews消息与news消息类似，不同的是图文消息内容存储在微信后台，并且支持保密选项。每个应用每天最多可以发送100次
*/
public class WX_Msg_MpNews extends WX_MsgBase
{
	/** 
	 必须：是- 消息类型，此时固定为：mpnews （不支持主页型应用） 
	*/
	public final String getmsgtype()
	{
		return "mpnews";
	}
	/** 
	 必须：是- 图文消息，一个图文消息支持1到8条图文
	*/
	private ArrayList<MpNews_Articles> articles;
	public final ArrayList<MpNews_Articles> getarticles()
	{
		return articles;
	}
	public final void setarticles(ArrayList<MpNews_Articles> value)
	{
		articles = value;
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
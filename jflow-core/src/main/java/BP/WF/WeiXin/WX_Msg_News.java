package BP.WF.WeiXin;

import BP.WF.*;
import java.util.*;


	///#region news消息

/** 
 微信-news消息
*/
public class WX_Msg_News extends WX_MsgBase
{
	/** 
	 必须：是- 息类型，此时固定为：news （不支持主页型应用）
	*/
	public final String getmsgtype()
	{
		return "news";
	}

	private ArrayList<News_Articles> _Articles = new ArrayList<News_Articles>();
	/** 
	 必须：是- 图文消息，一个图文消息支持1到8条图文
	*/
	public final ArrayList<News_Articles> getarticles()
	{
		return _Articles;
	}
}
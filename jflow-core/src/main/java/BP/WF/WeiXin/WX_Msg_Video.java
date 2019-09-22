package BP.WF.WeiXin;

import BP.WF.*;
import java.util.*;

/** 
 微信-video消息
*/
public class WX_Msg_Video extends WX_MsgBase
{
	/** 
	 必须：是- 消息类型，此时固定为：video （不支持主页型应用）
	*/
	public final String getmsgtype()
	{
		return "video";
	}
	/** 
	 必须：是- 视频媒体文件id，可以调用上传临时素材或者永久素材接口获取 
	*/
	private String media_id;
	public final String getmedia_id()
	{
		return media_id;
	}
	public final void setmedia_id(String value)
	{
		media_id = value;
	}
	/** 
	 必须：否- 视频消息的标题，不超过128个字节，超过会自动截断
	*/
	private String title;
	public final String gettitle()
	{
		return title;
	}
	public final void settitle(String value)
	{
		title = value;
	}
	/** 
	 必须：否- 视频消息的描述，不超过512个字节，超过会自动截断 
	*/
	private String description;
	public final String getdescription()
	{
		return description;
	}
	public final void setdescription(String value)
	{
		description = value;
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
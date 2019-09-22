package BP.WF.WeiXin;

import BP.WF.*;
import java.util.*;

/** 
 微信-file消息
*/
public class WX_Msg_File extends WX_MsgBase
{
	/** 
	 必须：是- 消息类型，此时固定为：file （不支持主页型应用） 
	*/
	public final String getmsgtype()
	{
		return "file";
	}
	/** 
	 必须：是- 媒体文件id，可以调用上传临时素材或者永久素材接口获取
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
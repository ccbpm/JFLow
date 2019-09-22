package BP.WF.WeiXin;

import BP.WF.*;
import java.util.*;

/** 
 News消息内容
*/
public class News_Articles
{
	/** 
	 必须：否- 标题，不超过128个字节，超过会自动截断
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
	 必须：否- 描述，不超过512个字节，超过会自动截断
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
	 必须：否- 点击后跳转的链接
	*/
	private String url;
	public final String geturl()
	{
		return url;
	}
	public final void seturl(String value)
	{
		url = value;
	}
	/** 
	 必须：否- 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80。如不填，在客户端不显示图片
	*/
	private String picurl;
	public final String getpicurl()
	{
		return picurl;
	}
	public final void setpicurl(String value)
	{
		picurl = value;
	}
}
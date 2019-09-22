package BP.WF.WeiXin;

import BP.WF.*;
import java.util.*;

/** 
 mpnews消息内容
*/
public class MpNews_Articles
{
	/** 
	 必须：是- 图文消息的标题，不超过128个字节，超过会自动截断 
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
	 必须：是- 描述，图文消息缩略图的media_id, 可以在上传多媒体文件接口中获得。此处thumb_media_id即上传接口返回的media_id 
	*/
	private String thumb_media_id;
	public final String getthumb_media_id()
	{
		return thumb_media_id;
	}
	public final void setthumb_media_id(String value)
	{
		thumb_media_id = value;
	}
	/** 
	 必须：否- 描述，不超过512个字节，超过会自动截断
	*/
	private String author;
	public final String getauthor()
	{
		return author;
	}
	public final void setauthor(String value)
	{
		author = value;
	}
	/** 
	 必须：否- 图文消息点击"阅读原文"之后的页面链接
	*/
	private String content_source_url;
	public final String getcontent_source_url()
	{
		return content_source_url;
	}
	public final void setcontent_source_url(String value)
	{
		content_source_url = value;
	}
	/** 
	 必须：是- 图文消息的内容，支持html标签，不超过666 K个字节
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
	 必须：否- 图文消息的描述，不超过512个字节，超过会自动截断
	*/
	private String digest;
	public final String getdigest()
	{
		return digest;
	}
	public final void setdigest(String value)
	{
		digest = value;
	}
	/** 
	 必须：否- 否显示封面，1为显示，0为不显示
	*/
	private String show_cover_pic;
	public final String getshow_cover_pic()
	{
		return show_cover_pic;
	}
	public final void setshow_cover_pic(String value)
	{
		show_cover_pic = value;
	}
}
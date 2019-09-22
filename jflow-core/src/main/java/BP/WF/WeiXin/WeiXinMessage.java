package BP.WF.WeiXin;

import BP.Tools.*;
import BP.En.*;
import BP.WF.*;

/** 
 微信消息处理
*/
public class WeiXinMessage
{
	/** 
	 发送文本消息
	 
	 @param msgText 消息实体类
	 @return 发送消息结果
	*/
	public static MessageErrorModel PostMsgOfText(WX_Msg_Text msgText)
	{

		String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + msgText.getAccess_Token();

		StringBuilder append_Json = new StringBuilder();
		append_Json.append("{");
		append_Json.append("\"msgtype\":\"text\"");
		//按人员
		if (!tangible.StringHelper.isNullOrEmpty(msgText.gettouser()))
		{
			append_Json.append(",\"touser\":\"" + msgText.gettouser() + "\"");
		}

		//按部门
		if (!tangible.StringHelper.isNullOrEmpty(msgText.gettoparty()))
		{
			append_Json.append(",\"toparty\":\"" + msgText.gettoparty() + "\"");
		}

		//标签
		if (!tangible.StringHelper.isNullOrEmpty(msgText.gettotag()))
		{
			append_Json.append(",\"totag\":\"" + msgText.gettotag() + "\"");
		}

		append_Json.append(",\"agentid\":\"" + msgText.getagentid() + "\"");
		append_Json.append(",\"text\":{");
		append_Json.append("\"content\":\"" + msgText.getcontent() + "\"");
		append_Json.append("}");
		append_Json.append(",\"safe\":\"" + msgText.getsafe() + "\"");
		append_Json.append("}");
		String str = (new HttpWebResponseUtility()).HttpResponsePost_Json(url, append_Json.toString());
		MessageErrorModel postVal = FormatToJson.<MessageErrorModel>ParseFromJson(str);
		return postVal;
	}

	/** 
	 发送新闻消息
	 
	 @param msgNews 消息实体类
	 @return 发送消息结果
	*/
	public static MessageErrorModel PostMsgOfNews(WX_Msg_News msgNews)
	{
		String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + msgNews.getAccess_Token();

		StringBuilder append_Json = new StringBuilder();
		append_Json.append("{");
		append_Json.append("\"msgtype\":\"news\"");
		//按人员
		if (!tangible.StringHelper.isNullOrEmpty(msgNews.gettouser()))
		{
			append_Json.append(",\"touser\":\"" + msgNews.gettouser() + "\"");
		}
		//按部门
		if (!tangible.StringHelper.isNullOrEmpty(msgNews.gettoparty()))
		{
			append_Json.append(",\"toparty\":\"" + msgNews.gettoparty() + "\"");
		}
		//标签
		if (!tangible.StringHelper.isNullOrEmpty(msgNews.gettotag()))
		{
			append_Json.append(",\"totag\":\"" + msgNews.gettotag() + "\"");
		}

		append_Json.append(",\"agentid\":\"" + msgNews.getagentid() + "\"");
		append_Json.append(",\"news\":{");

		append_Json.append("\"articles\":[");
		for (News_Articles item : msgNews.getarticles())
		{
			append_Json.append("{");
			append_Json.append("\"title\":\"" + item.gettitle() + "\"");
			append_Json.append(",\"description\":\"" + item.getdescription() + "\"");
			if (!tangible.StringHelper.isNullOrEmpty(item.geturl()))
			{
				append_Json.append(",\"url\":\"" + item.geturl() + "\"");
			}
			if (!tangible.StringHelper.isNullOrEmpty(item.getpicurl()))
			{
				append_Json.append(",\"picurl\":\"" + item.getpicurl() + "\"");
			}
			append_Json.append("},");
		}
		append_Json.deleteCharAt(append_Json.length() - 1);

		append_Json.append("]}");
		append_Json.append("}");
		String str = (new HttpWebResponseUtility()).HttpResponsePost_Json(url, append_Json.toString());
		MessageErrorModel postVal = FormatToJson.<MessageErrorModel>ParseFromJson(str);
		return postVal;
	}
	/** 
	 发送待办消息
	 
	 @param toEmps 到达的人员多个人员用|分开比如: zhangsan|lisi 
	 @param title 标题
	 @param msg 发送内容
	 @param sender 发送人
	 @return 
	*/
	public static MessageErrorModel SendMsgToUsers(String toUsers, String title, String msg, String sender)
	{
		//企业应用必须存在
		String agentId = BP.Sys.SystemConfig.WX_AgentID != null ? BP.Sys.SystemConfig.WX_AgentID : null;
		if (BP.DA.DataType.IsNullOrEmpty(agentId) == true)
		{
			return null;
		}

		String accessToken = (new BP.WF.WeiXin.WeiXin()).GenerAccessToken(); //获取 AccessToken

		News_Articles newArticle = new News_Articles();
		newArticle.settitle(title);
		newArticle.setdescription(msg);

		String New_Url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + BP.Sys.SystemConfig.WX_CorpID + "&redirect_uri=" + BP.Sys.SystemConfig.WX_MessageUrl + "/CCMobile/action.aspx&response_type=code&scope=snsapi_base&state=Todolist#wechat_redirect";
		newArticle.seturl(New_Url);

		//http://discuz.comli.com/weixin/weather/icon/cartoon.jpg
		newArticle.setpicurl(BP.Sys.SystemConfig.WX_MessageUrl + "/DataUser/ICON/" + BP.Sys.SystemConfig.SysNo + "/LogBig.png");

		toUsers = toUsers.replace(',','|');

		WX_Msg_News wxMsg = new WX_Msg_News();
		wxMsg.setAccess_Token(accessToken);
		wxMsg.setagentid(BP.Sys.SystemConfig.WX_AgentID);
		wxMsg.settouser(toUsers);
		wxMsg.getarticles().add(newArticle);
		//执行发送
		return WeiXinMessage.PostMsgOfNews(wxMsg);
	}
}
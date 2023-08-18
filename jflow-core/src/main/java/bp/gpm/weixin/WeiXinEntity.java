package bp.gpm.weixin;

import bp.tools.*;
import bp.da.*;
import bp.wf.*;
import bp.sys.*;
import bp.*;
import java.util.*;
import java.time.*;

/** 
 微信实体类
*/
public class WeiXinEntity
{

		///#region 基本配置.
	/** 
	 微信应用的分配的单位ID.
	 格式:wx8eac6a18c5efec30
	*/
	public static String getAppid()
	{
		return bp.difference.SystemConfig.getWX_CorpID(); // "wx8eac6a18c5efec30";
	}
	/** 
	 微信应用的分配给单位的一个加密字符串, 标识这个值对应的是这个单位的应用.
	 格式:KfFkE9AZ3Zp09zTuKvmqWLgtLj
	 也就是密钥.
	*/
	public static String getAppsecret()
	{
		return bp.difference.SystemConfig.getWX_AppSecret(); // "KfFkE9AZ3Zp09zTuKvmqWLgtLj-_cHMPTvV992apOWgSKJHcbjpbu1jYVXh7gI7K";
	}
	/** 
	 获得token,每间隔x分钟，就会失效.
	 
	 @return token
	*/
	public static String getAccessToken() throws Exception {
		String accessToken = "";
		String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + getAppid() + "&corpsecret=" + getAppsecret();

		AccessToken AT = new AccessToken();
		String str = DataType.ReadURLContext(url, 5000);
		AT = (AccessToken) FormatToJson.<AccessToken>ParseFromJson(str);
		accessToken = AT.getAccessToken();

		return accessToken;
	}

		///#endregion 基本配置.


		///#region 应用方法.
	/** 
	 调用企业号获取地理位置
	 
	 @return 
	*/
	public static String GetWXConfigSetting(String pageUrl) throws Exception {
		//必须是当前页面，如果在CCMobile/Home.htm调用，则传入Home.htm
		String htmlPage = pageUrl;
		Hashtable ht = new Hashtable();

		//生成签名的时间戳
		String timestamp = DataType.getCurrentDateByFormart("yyyyMMDDHHddss");
		//生成签名的随机串
		String nonceStr = DBAccess.GenerGUID(0, null, null);
		//企业号jsapi_ticket
		String jsapi_ticket = "";
		String url1 = htmlPage;
		//获取 AccessToken
		String accessToken = getAccessToken();

		String url = "https://qyapi.weixin.qq.com/cgi-bin/ticket/get?access_token=" + accessToken + "&type=wx_card";
		String str = DataType.ReadURLContext(url, 9999);

		//权限签名算法
		Ticket ticket = new Ticket();
		ticket = (Ticket) FormatToJson.<Ticket>ParseFromJson(str);

		if (Objects.equals(ticket.getErrcode(), "0"))
		{
			jsapi_ticket = ticket.getTicket();
		}
		else
		{
			return "err:@获取jsapi_ticket失败+accessToken=" + str;
		}

		ht.put("timestamp", timestamp);
		ht.put("nonceStr", nonceStr);
		//企业微信的corpID
		ht.put("AppID", bp.difference.SystemConfig.getWX_CorpID());

		//生成签名算法
		String str1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url1 + "";
		UserLog userLog = new UserLog();
		userLog.setMyPK(DBAccess.GenerGUID(0, null, null));

		userLog.setLogFlag("系统定位1");
		userLog.setDocs(str1);
		userLog.setRDT(DataType.getCurrentDateTimess());
		userLog.Insert();
		String Signature = Sha1Signature(str1);
		ht.put("signature", Signature);

		userLog.setMyPK(DBAccess.GenerGUID(0, null, null));

		userLog.setLogFlag("生成签名");
		userLog.setDocs(Signature);
		userLog.setRDT(DataType.getCurrentDateTimess());
		userLog.Insert();

		return Json.ToJson(ht);
	}

		///#endregion 应用方法.


		///#region 发送微信信息.
	public final MessageErrorModel PostWeiXinMsg(StringBuilder sb) throws Exception {
		String wxStr = "";
		String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?";

		MessageErrorModel m = new MessageErrorModel();
		wxStr = PostForWeiXin(sb, url);
		m = (MessageErrorModel) FormatToJson.<MessageErrorModel>ParseFromJson(wxStr);
		return m;
	}
	/** 
	 POST方式请求 微信返回信息
	 
	 @param parameters 参数
	 @param URL 请求地址
	 @return 返回字符
	*/
	public final String PostForWeiXin(StringBuilder parameters, String URL) throws Exception {
		String access_token = getAccessToken();
		String url = URL + "access_token=" + access_token;

		//todo:zqp.该方法没有完善.
		String str = DataType.ReadURLContext(url, 9999);

		//HttpWebResponse response = new HttpWebResponseUtility().WXCreateGetHttpResponse(url, parameters,
		//    10000, null, Encoding.UTF8, null);
		//StreamReader reader = new StreamReader(response.GetResponseStream(), Encoding.UTF8);
		//String str = reader.ReadToEnd();

		Log.DebugWriteInfo(url + "----------------" + parameters + "---------------" + str);
		return str;
	}

		///#endregion

	/** 
	 算法加密
	 
	 @param str
	 @return 
	*/
	public static String Sha1Signature(String str)
	{
		return bp.wf.difference.Glo.Sha1Signature(str);
	}
}

package bp.wf.weixin;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.tools.*;
import bp.wf.weixin.gzh.WeiXinGZHModel;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.util.Date;


public class WeiXinGZHEntity
{

		///#region 基本配置.
	/** 
	 微信公众号应用的分配的单位ID.
	 格式:wx8eac6a18c5efec30
	*/
	public static String getAppid()throws Exception
	{
		return bp.difference.SystemConfig.getWXGZH_Appid(); // "wx8eac6a18c5efec30";
	}
	/** 
	 微信公众号开发则密码.
	*/
	public static String getAppSecret()throws Exception
	{
		return SystemConfig.getWXGZH_AppSecret(); // "KfFkE9AZ3Zp09zTuKvmqWLgtLj-_cHMPTvV992apOWgSKJHcbjpbu1jYVXh7gI7K";
	}

		///#endregion 基本配置.


		///#region 获取用户access_token
	/** 
	 获得token,每间隔x分钟，就会失效.
	 
	 @return token
	*/
	public static WeiXinGZHModel.AccessToken getAccessToken(String code) throws Exception {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + getAppid() + "&secret=" + getAppSecret() + "&code=" + code + "&grant_type=authorization_code";

		WeiXinGZHModel.AccessToken token = new WeiXinGZHModel.AccessToken();
		String json = bp.tools.HttpClientUtil.doGet(url);
		if(DataType.IsNullOrEmpty(json)==false){
			JSONObject jd = JSONObject.fromObject(json);
			token= (WeiXinGZHModel.AccessToken)JSONObject.toBean(jd,WeiXinGZHModel.AccessToken.class);
		}

		return token;
	}

		///#endregion 获取用户access_token


		///#region 获取用户信息
	public static WeiXinGZHModel.GZHUser getUserInfo(String access_token, String openid)
	{
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";

		WeiXinGZHModel.GZHUser user = new WeiXinGZHModel.GZHUser();
		String json = bp.tools.HttpClientUtil.doGet(url);
		if(DataType.IsNullOrEmpty(json)==false){
			JSONObject jd = JSONObject.fromObject(json);
			user= (WeiXinGZHModel.GZHUser)JSONObject.toBean(jd,WeiXinGZHModel.GZHUser.class);

		}
		return user;
	}

		///#endregion 获取用户信息
}
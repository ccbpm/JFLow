package bp.wf.weixin.gzh;

public class WeiXinGZHModel
{
	public static class AccessToken
	{
		/** 
		 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
		*/
		private String access_token;
		public final String getAccessToken() throws Exception {
			return access_token;
		}
		public final void setAccessToken(String value)throws Exception
	{access_token = value;
		}
		/** 
		 access_token接口调用凭证超时时间，单位（秒）
		*/
		private String expires_in;
		public final String getExpiresIn() throws Exception {
			return expires_in;
		}
		public final void setExpiresIn(String value)throws Exception
	{expires_in = value;
		}
		/** 
		 用户刷新access_token
		*/
		private String refresh_token;
		public final String getRefreshToken() throws Exception {
			return refresh_token;
		}
		public final void setRefreshToken(String value)throws Exception
	{refresh_token = value;
		}
		/** 
		 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
		*/
		private String openid;
		public final String getOpenid() throws Exception {
			return openid;
		}
		public final void setOpenid(String value)throws Exception
	{openid = value;
		}
		/** 
		 用户授权的作用域，使用逗号（,）分隔
		*/
		private String scope;
		public final String getScope() throws Exception {
			return scope;
		}
		public final void setScope(String value)throws Exception
	{scope = value;
		}
		/** 
		 返回信息
		*/
		private String errcode;
		public final String getErrcode() throws Exception {
			return errcode;
		}
		public final void setErrcode(String value)throws Exception
	{errcode = value;
		}
	}
	public static class GZHUser
	{
		/** 
		 用户的唯一标识
		*/
		private String openid;
		public final String getOpenid() throws Exception {
			return openid;
		}
		public final void setOpenid(String value)throws Exception
	{openid = value;
		}
		/** 
		 用户昵称
		*/
		private String nickname;
		public final String getNickname() throws Exception {
			return nickname;
		}
		public final void setNickname(String value)throws Exception
	{nickname = value;
		}
		/** 
		 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
		*/
		private String sex;
		public final String getSex() throws Exception {
			return sex;
		}
		public final void setSex(String value)throws Exception
	{sex = value;
		}
		/** 
		 用户个人资料填写的省份
		*/
		private String province;
		public final String getProvince() throws Exception {
			return province;
		}
		public final void setProvince(String value)throws Exception
	{province = value;
		}
		/** 
		 普通用户个人资料填写的城市
		*/
		private String city;
		public final String getCity() throws Exception {
			return city;
		}
		public final void setCity(String value)throws Exception
	{city = value;
		}
		/** 
		 国家，如中国为CN
		*/
		private String country;
		public final String getCountry() throws Exception {
			return country;
		}
		public final void setCountry(String value)throws Exception
	{country = value;
		}
		/** 
		 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像）
		 用户没有头像时该项为空。若用户更换头像，原有头像URL将失效
		*/
		private String headimgurl;
		public final String getHeadimgurl() throws Exception {
			return headimgurl;
		}
		public final void setHeadimgurl(String value)throws Exception
	{headimgurl = value;
		}
		/** 
		 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
		*/
		private String privilege;
		public final String getPrivilege() throws Exception {
			return privilege;
		}
		public final void setPrivilege(String value)throws Exception
	{privilege = value;
		}
		/** 
		 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
		*/
		private String unionid;
		public final String getUnionid() throws Exception {
			return unionid;
		}
		public final void setUnionid(String value)throws Exception
	{unionid = value;
		}
		/** 
		 返回信息
		*/
		private String errcode;
		public final String getErrcode() throws Exception {
			return errcode;
		}
		public final void setErrcode(String value)throws Exception
	{errcode = value;
		}
	}
}
package BP.WF.WeiXin;

import BP.Tools.*;
import BP.WF.*;
import java.io.*;

public class WeiXin
{
	public final String GenerAccessToken()
	{
		String accessToken = "";
		String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + BP.Sys.SystemConfig.WX_CorpID + "&corpsecret=" + BP.Sys.SystemConfig.WX_AppSecret + "";

		AccessToken AT = new AccessToken();
	//	String str = BP.DA.DataType.ReadURLContext(url, 5000, Encoding.UTF8);
		//AT = FormatToJson.<AccessToken>ParseFromJson(str);
		accessToken = AT.getaccess_token();

		return accessToken;
	}


		///#region 发送微信信息
	public final MessageErrorModel PostWeiXinMsg(StringBuilder sb)
	{
		String wxStr = "";
		String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?";
		MessageErrorModel m = new MessageErrorModel();
		wxStr = PostForWeiXin(sb, url);
		m = FormatToJson.<MessageErrorModel>ParseFromJson(wxStr);
		return m;
	}
	/** 
	 POST方式请求 微信返回信息
	 
	 @param parameters 参数
	 @param URL 请求地址
	 @return 返回字符
	*/
	public final String PostForWeiXin(StringBuilder parameters, String URL)
	{
		String access_token = GenerAccessToken();
		String url = URL + "access_token=" + access_token;

		HttpWebResponse response = (new HttpWebResponseUtility()).WXCreateGetHttpResponse(url, parameters, 10000, null, Encoding.UTF8, null);
		InputStreamReader reader = new InputStreamReader(response.GetResponseStream(), java.nio.charset.StandardCharsets.UTF_8);
		String str = reader.ReadToEnd();

		BP.DA.Log.DebugWriteInfo(url + "----------------" + parameters + "---------------" + str);
		return str;
	}

		///#endregion

	/** 
	 下载人员头像
	*/
	public final boolean DownLoadUserIcon(String savePath)
	{
		if ((new File(savePath)).isDirectory() == false)
		{
			(new File(savePath)).mkdirs();
		}

		DeptMent_GetList deptMentList = GetDeptMentList();
		if (deptMentList != null && deptMentList.geterrcode().equals("0"))
		{
			for (DeptMentInfo deptMent : deptMentList.getdepartment())
			{
				UsersBelongDept users = GetUserListByDeptID(deptMent.getid());
				if (users != null && users.geterrcode().equals("0"))
				{
					for (UserInfoBelongDept userInfo : users.getuserlist())
					{
						if (userInfo.getavatar() != null)
						{
							//大图标
							String headimgurl = userInfo.getavatar();
							String UserIcon = savePath + "\\" + userInfo.getuserid() + "Biger.png";
							BP.DA.DataType.HttpDownloadFile(headimgurl, UserIcon);

							//小图标
							String iconSize = userInfo.getavatar().substring(headimgurl.lastIndexOf('/'));
							if (iconSize.equals("/"))
							{
								headimgurl = userInfo.getavatar() + "64";
							}
							else
							{
								headimgurl = userInfo.getavatar().substring(0, headimgurl.lastIndexOf('/')) + "64";
							}
							UserIcon = savePath + "\\" + userInfo.getuserid() + "Smaller.png";
							BP.DA.DataType.HttpDownloadFile(headimgurl, UserIcon);
						}
					}
				}
			}
			return true;
		}
		return false;
	}
	/** 
	 获取指定部门下  指定手机号的人员
	 
	 @param FK_Dept 部门编号
	 @param Tel 手机号
	 @return 
	*/

	public final UserInfoBelongDept GetUserListByDeptIDAndTel(String FK_Dept)
	{
		return GetUserListByDeptIDAndTel(FK_Dept, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public UserInfoBelongDept GetUserListByDeptIDAndTel(string FK_Dept, string Tel = null)
	public final UserInfoBelongDept GetUserListByDeptIDAndTel(String FK_Dept, String Tel)
	{
		String access_token = GenerAccessToken();
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token= " + access_token + "&department_id=" + FK_Dept + "&status=0";
		try
		{
			String str = (new HttpWebResponseUtility()).HttpResponseGet(url);
			UsersBelongDept users = FormatToJson.<UsersBelongDept>ParseFromJson(str);

			//指定人员
			if (Tel != null)
			{
				for (UserInfoBelongDept user : users.getuserlist())
				{
					if (user.getmobile().equals(Tel))
					{
						return user;
					}
				}
			}

		}
		catch (RuntimeException ex)
		{
			BP.DA.Log.DefaultLogWriteLineError(ex.getMessage());
		}
		return null;
	}

	/** 
	 获取部门集合
	*/
	public final DeptMent_GetList GetDeptMentList()
	{
		String access_token = GenerAccessToken();
		String url = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=" + access_token;
		try
		{
			String str = (new HttpWebResponseUtility()).HttpResponseGet(url);
			DeptMent_GetList departMentList = FormatToJson.<DeptMent_GetList>ParseFromJson(str);

			//部门集合
			if (departMentList != null)
			{
				return departMentList;
			}
		}
		catch (RuntimeException ex)
		{
			BP.DA.Log.DefaultLogWriteLineError(ex.getMessage());
		}
		return null;
	}

	/** 
	 获取指定部门下的人员
	 
	 @param FK_Dept 部门编号
	 @return 
	*/
	public final UsersBelongDept GetUserListByDeptID(String FK_Dept)
	{
		String access_token = GenerAccessToken();
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=" + access_token + "&department_id=" + FK_Dept + "&status=0";
		try
		{
			String str = (new HttpWebResponseUtility()).HttpResponseGet(url);
			UsersBelongDept users = FormatToJson.<UsersBelongDept>ParseFromJson(str);

			//人员集合
			if (users != null)
			{
				return users;
			}
		}
		catch (RuntimeException ex)
		{
			BP.DA.Log.DefaultLogWriteLineError(ex.getMessage());
		}
		return null;
	}
}
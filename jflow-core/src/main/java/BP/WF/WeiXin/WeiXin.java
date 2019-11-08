package BP.WF.WeiXin;

import BP.Tools.*;
import BP.WF.*;
import java.io.*;

public class WeiXin
{
	public final String GenerAccessToken() throws Exception
	{
		String accessToken = "";
		String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + BP.Sys.SystemConfig.getWX_CorpID() + "&corpsecret=" + BP.Sys.SystemConfig.getWX_AppSecret() + "";
		String json = BP.Tools.HttpClientUtil.doGet(url);
		AccessToken AT = (AccessToken) FormatToJson.ParseFromJson(json);
		accessToken = AT.getaccess_token();

		return accessToken;
	}

	//获取用户ID
    public User getUserInfo(String code, String accessToken) throws Exception
    {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + accessToken + "&code=" + code + "&agentid=2";
        String json = BP.Tools.HttpClientUtil.doGet(url);
        
        User user = (User) FormatToJson.ParseFromJson(json);
        return user;

    }


		///#region 发送微信信息
	public final MessageErrorModel PostWeiXinMsg(String sb) throws Exception
	{
		String wxStr = "";
		String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?";
		MessageErrorModel m = new MessageErrorModel();
		wxStr = PostForWeiXin(sb, url);
		m = (MessageErrorModel) FormatToJson.ParseFromJson(wxStr);
		return m;
	}
	/** 
	 POST方式请求 微信返回信息
	 
	 @param parameters 参数
	 @param URL 请求地址
	 @return 返回字符
	 * @throws Exception 
	*/
	public final String PostForWeiXin(String parameters, String URL) throws Exception
	{
		String access_token = GenerAccessToken();
		String url = URL + "access_token=" + access_token;
        String str = HttpClientUtil.doPostJson(URL, parameters);
		return str;
	}

		///#endregion

	/** 
	 下载人员头像
	 * @throws Exception 
	*/
	public final boolean DownLoadUserIcon(String savePath) throws Exception
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
							BP.Tools.HttpClientUtil.HttpDownloadFile(headimgurl, UserIcon);

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
							BP.Tools.HttpClientUtil.HttpDownloadFile(headimgurl, UserIcon);
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
	 * @throws Exception 
	*/

	public final UserInfoBelongDept GetUserListByDeptIDAndTel(String FK_Dept) throws Exception
	{
		return GetUserListByDeptIDAndTel(FK_Dept, null);
	}

	public final UserInfoBelongDept GetUserListByDeptIDAndTel(String FK_Dept, String Tel) throws Exception
	{
		String access_token = GenerAccessToken();
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token= " + access_token + "&department_id=" + FK_Dept + "&status=0";
		try
		{
			String str = BP.Tools.HttpClientUtil.doGet(url);
			UsersBelongDept users = (UsersBelongDept) FormatToJson.ParseFromJson(str);

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
	 * @throws Exception 
	*/
	public final DeptMent_GetList GetDeptMentList() throws Exception
	{
		String access_token = GenerAccessToken();
		String url = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=" + access_token;
		try
		{
			String str = BP.Tools.HttpClientUtil.doGet(url);
			DeptMent_GetList departMentList = (DeptMent_GetList) FormatToJson.ParseFromJson(str);

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
	 * @throws Exception 
	*/
	public final UsersBelongDept GetUserListByDeptID(String FK_Dept) throws Exception
	{
		String access_token = GenerAccessToken();
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=" + access_token + "&department_id=" + FK_Dept + "&status=0";
		try
		{
			String str = BP.Tools.HttpClientUtil.doGet(url);
			UsersBelongDept users = (UsersBelongDept) FormatToJson.ParseFromJson(str);

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
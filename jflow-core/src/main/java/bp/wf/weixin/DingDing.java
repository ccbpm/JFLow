package bp.wf.weixin;

import java.util.HashMap;
import java.util.Map;

import bp.da.DataType;
import bp.difference.SystemConfig;
import bp.tools.*;
import net.sf.json.JSONObject;

public class DingDing
{
	public final String GenerAccessToken() throws Exception
	{
		String url = "https://oapi.dingtalk.com/gettoken?appkey="+SystemConfig.getDing_AppKey()+"&appsecret="+ SystemConfig.getDing_AppSecret() + "";
		String json = HttpClientUtil.doGet(url);
		if(DataType.IsNullOrEmpty(json)==false){
			JSONObject jd = JSONObject.fromObject(json);
			if(jd.get("errcode").toString().equals("0")){
				Object token  = jd.get("access_token");
				return token.toString();
			}
		}

		return "err@获取accessToken失败";
	}

	//获取用户ID
    public String getUserInfo(String code, String accessToken) throws Exception
    {
    	String url = "https://oapi.dingtalk.com/user/getuserinfo?access_token=" + accessToken + "&code=" + code;
        String json = HttpClientUtil.doGet(url);
        
        if(DataType.IsNullOrEmpty(json)==false){
			return json;
		}
        return "err@获取UserID失败";
    }


	///#region 发送微信信息
	public final boolean PostDingDingMsg(String sb,String toSendUser) throws Exception
	{
		String wxStr = "";
		String url = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2?";
		wxStr = PostForDingDing(sb,toSendUser, url);
		if(DataType.IsNullOrEmpty(wxStr)==false){
			JSONObject jd = JSONObject.fromObject(wxStr);
			if(jd.get("errcode").toString().equals("0"))
				return true;
			
		}
		return false ;
	}
	/** 
	 POST方式请求 微信返回信息
	 
	 param msg 参数
	 param URL 请求地址
	 @return 返回字符
	 * @throws Exception 
	*/
	public final String PostForDingDing(String msg, String toSendUser,String URL) throws Exception
	{
		String access_token = GenerAccessToken();
		URL = URL + "access_token=" + access_token;
		JSONObject param=new JSONObject();
		param.put("agent_id",SystemConfig.getDing_AgentID());
		param.put("userid_list",toSendUser);
		param.put("msg",msg);
        String str = HttpClientUtil.doPostJson(URL, param.toString());
		return str;
	}

	/** 
	 下载人员头像
	 * @throws Exception 
	*//*
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
	*//** 
	 获取指定部门下  指定手机号的人员
	 
	 param FK_Dept 部门编号
	 param Tel 手机号
	 @return 
	 * @throws Exception 
	*//*

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
			bp.da.Log.DefaultLogWriteLineError(ex.getMessage());
		}
		return null;
	}

	*//** 
	 获取部门集合
	 * @throws Exception 
	*//*
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
			bp.da.Log.DefaultLogWriteLineError(ex.getMessage());
		}
		return null;
	}

	*//*
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
			bp.da.Log.DefaultLogWriteLineError(ex.getMessage());
		}
		return null;
	}*/

	public static String  ResponseMsg(String touser, String toparty, String totag, String msgtype, String msg)
    {
        StringBuilder sbStr = new StringBuilder();
        String msgTypeStr = "";
        switch (msgtype)
        {
            case "text":
                msgTypeStr = " \"text\": { \"content\":\"" + msg + "\"  }";
                break;
            case "image":
                msgTypeStr = " \"image\": { \"media_id\":\"" + msg + "\"  }";
                break;
            case "voice":
                msgTypeStr = " \"voice\": { \"media_id\":\"" + msg + "\"  }";
                break;
            case "video":
                msgTypeStr = " \"video\": { \"media_id\":\"" + msg + "\",\"\":'标题',\"description\":'描述'  }";
                break;
            default:
                msgTypeStr = " \"text\": { \"content\":'数据类型错误！'  }";
                break;
        }
        sbStr.append("{");
        sbStr.append("\"msgtype\":\"" + msgtype + "\",");
        sbStr.append(msgTypeStr);
        sbStr.append("}");
        return sbStr.toString();
    }
}
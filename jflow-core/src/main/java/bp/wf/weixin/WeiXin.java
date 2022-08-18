package bp.wf.weixin;

import java.util.HashMap;

import bp.da.DataTable;
import bp.da.DataType;
import bp.da.Paras;
import bp.difference.SystemConfig;
import bp.port.Emp;

import bp.tools.*;
import bp.wf.weixin.util.crypto.TemplateMessageUtil;
import net.sf.json.JSONObject;

public class WeiXin
{
	//获取企业号的token
	public final String GenerAccessToken() throws Exception
	{
		String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + SystemConfig.getWX_CorpID() + "&corpsecret=" + SystemConfig.getWX_AppSecret() + "";
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
	//获取公众号token
	public final String GetGZHToken() throws Exception{
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=client_credential&appid=" + SystemConfig.getWXGZH_Appid() + "&secret=" + SystemConfig.getWXGZH_AppSecret() + "";
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
    	String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + accessToken + "&code=" + code + "&agentid=2";
        String json = HttpClientUtil.doGet(url);
        
        if(DataType.IsNullOrEmpty(json)==false){
			JSONObject jd = JSONObject.fromObject(json);
			if(jd.get("errcode").toString().equals("0")){
				Object UserId  = jd.get("UserId");
				return UserId.toString();
			}
			
		}
        return "err@获取UserID失败";
    }


	///#region 发送微信信息
	public final boolean PostWeiXinMsg(String sb) throws Exception
	{
		String wxStr = "";
		String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?";
		wxStr = PostForWeiXin(sb, url);
		if(DataType.IsNullOrEmpty(wxStr)==false){
			JSONObject jd = JSONObject.fromObject(wxStr);
			if(jd.get("errcode").toString().equals("0"))
				return true;
			
		}
		return false ;
	}
	//发送公众号消息
	public final boolean PostGZHMsg(String title,String sender,String RDT,String senderTo) throws Exception
	{
		String wxStr = "";
		//公众号发送调用的接口地址
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?";
		
		//先获取接收人的openid
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		Paras ps=new Paras();
		ps.SQL="SELECT Wei_UserID from Port_Emp where No="+dbstr+"No";
		ps.Add("No", senderTo);
        DataTable dt=bp.da.DBAccess.RunSQLReturnTable(ps);
        String Wei_UserID=dt.Rows.get(0).getValue("Wei_UserID").toString();
		
        System.out.println("Wei_UserID:"+Wei_UserID);
        //生成消息模版
		HashMap<String,String> msgMap = new HashMap<String,String>();
		msgMap.put("template_id",SystemConfig.getWeiXin_TemplateId());//消息模版的id
		msgMap.put("touser",Wei_UserID);//接收人的openid
		msgMap.put("url", SystemConfig.getWX_MessageUrl());//打开消息的url
		
		//消息主体模版
		HashMap<String,TemplateMessageUtil> mapdata = new HashMap<>();
		
		//设置消息标题
		TemplateMessageUtil first  = new TemplateMessageUtil();        
        first.setColor("#173177");
        first.setValue(title);
        mapdata.put("first", first);
        
        //发送人
        TemplateMessageUtil text2  = new TemplateMessageUtil();    
        text2.setColor("#173177");
        Emp emp=new Emp(sender);
        text2.setValue(emp.getName());
        mapdata.put("Sender", text2);
        
        //发送时间
        TemplateMessageUtil text3  = new TemplateMessageUtil();    
        text3.setColor("#173177");
        text3.setValue(RDT);
        mapdata.put("SendRDT", text3);
        
        //详情
        TemplateMessageUtil remark = new TemplateMessageUtil();        
        remark.setColor("#173177");
        remark.setValue("请进入系统查看");
        mapdata.put("remark", remark);
        
      //将java对象转换为json对象
        JSONObject json = JSONObject.fromObject(mapdata);
        System.out.println("data:"+mapdata.toString());
        msgMap.put("data",mapdata.toString());      
		
        //执行发送
		wxStr = PostFroGZH(msgMap.toString(), url);
		if(DataType.IsNullOrEmpty(wxStr)==false){
			JSONObject jd = JSONObject.fromObject(wxStr);
			if(jd.get("errcode").toString().equals("0"))
				return true;
			
		}
		return false ;
	}
	/** 
	 POST方式请求 微信返回信息
	 
	 param parameters 参数
	 param URL 请求地址
	 @return 返回字符
	 * @throws Exception 
	*/
	public final String PostForWeiXin(String parameters, String URL) throws Exception
	{
		String access_token = GenerAccessToken();
		URL = URL + "access_token=" + access_token;
        String str = HttpClientUtil.doPostJson(URL, parameters);
		return str;
	}
	//公众号发送
	public final String PostFroGZH(String parameters, String URL) throws Exception
	{
		System.out.println("data:"+parameters);
		String access_token = GetGZHToken();
		URL = URL + "access_token=" + access_token;
		System.out.println("data:"+access_token);
        String str = HttpClientUtil.doPostJson(URL, parameters);
		return str;
	}
	
	public static String  ResponseMsg(String touser, String toparty, String totag, String msgtype, String msg)
    {
        StringBuilder sbStr = new StringBuilder();
        String msgTypeStr = "";
        switch (msgtype)
        {
            case "text":
                msgTypeStr = " \"text\": { \"content\":\"" + msg + "\"  },";
                break;
            case "image":
                msgTypeStr = " \"image\": { \"media_id\":\"" + msg + "\"  },";
                break;
            case "voice":
                msgTypeStr = " \"voice\": { \"media_id\":\"" + msg + "\"  },";
                break;
            case "video":
                msgTypeStr = " \"video\": { \"media_id\":\"" + msg + "\",\"\":'标题',\"description\":'描述'  },";
                break;
            default:
                msgTypeStr = " \"text\": { \"content\":'数据类型错误！'  },";
                break;
        }
        sbStr.append("{");
        sbStr.append("\"touser\":\"" + touser + "\",");
        sbStr.append("\"toparty\":\"" + toparty + "\",");
        sbStr.append("\"totag\":\"" + totag + "\",");
        sbStr.append("\"msgtype\":\"" + msgtype + "\",");
        sbStr.append("\"agentid\":\"" + SystemConfig.getWX_AgentID() + "\",");
        sbStr.append(msgTypeStr);
        sbStr.append("\"safe\":\"0\"");
        sbStr.append("}");
        return sbStr.toString();
    }
}
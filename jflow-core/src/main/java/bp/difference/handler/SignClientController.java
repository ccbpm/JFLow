package bp.difference.handler;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import bp.da.DBAccess;
import bp.da.DataTable;
import bp.da.DataType;
import bp.da.Paras;
import bp.difference.SystemConfig;
import bp.tools.HttpClientUtil;
import bp.wf.weixin.util.crypto.WXBizMsgCrypt;
import net.sf.json.JSONObject;


@Controller
@RequestMapping("/WF/WXZFH")
@Scope("request") 
public class SignClientController {
	@RequestMapping(value="/weChat",method=RequestMethod.GET)
	public void weChat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("开始签名校验");
		String token=SystemConfig.getWX_WeiXinToken();
		String ENCODING_AESKEY=SystemConfig.getWX_EncodingAESKey();
		String CORP_ID=SystemConfig.getWX_CorpID();
		//得到服务器传过来的4个参数
		String signature = request.getParameter("msg_signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");

		WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(token, ENCODING_AESKEY, CORP_ID);
		String sEchoStr = wxcpt.VerifyURL(signature, timestamp,nonce, echostr);
		System.out.println("-----签名校验通过-----");
		response.getWriter().write(sEchoStr);
	}
	@RequestMapping(value = "/weChatLogin")
	public String weChatLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("开始签名校验");
		//第一步，获取菜单配置url中的code
		String code=request.getParameter("code");
		String state=request.getParameter("state");
		//第二步：通过code获取网页授权access_token
		String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+SystemConfig.getWX_CorpID()+"&corpsecret=" + SystemConfig.getWX_AppSecret();
		String json=HttpClientUtil.doGet(url);
		JSONObject jsonObject = JSONObject.fromObject(json);
		String access_token = jsonObject.getString("access_token");
		if(DataType.IsNullOrEmpty(access_token)){
			System.out.println("-----access_token获取失败-----");
			return "err@access_token获取失败-----";
		}
		//第三步：获取用户信息
		String infoUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + access_token + "&code=" + code;
		String info=HttpClientUtil.doGet(infoUrl);
		JSONObject userInfo = JSONObject.fromObject(info);
		String errcode=userInfo.getString("errcode");
		if(errcode.equals("40029")){
			return "err@获取用户信息失败-----";
		}
		//人员在企业号中的ID
		String userID=userInfo.getString("UserId");
		//人员设置的手机号
		String mobile="";

		//第四步：获取人员的完整信息
		String userInfoDtil="https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=" + access_token + "&userid="+userID;
		String userJson=HttpClientUtil.doGet(infoUrl);
		JSONObject userJsonInfo = JSONObject.fromObject(userJson);
		errcode=userJsonInfo.getString("errcode");
		if(errcode.equals("0")){
			mobile=userJsonInfo.getString("mobile");
		}
		else
			return "err@获取用户信息详情失败-----";

		//第五步：验证人员是否存在本系统中
		Paras ps=new Paras();
		String dbstr = SystemConfig.getAppCenterDBVarStr();
		ps.SQL="SELECT No,Name from Port_Emp where Tel="+dbstr+"Wei_UserID or No="+dbstr+"No ";
		ps.Add("Wei_UserID", mobile);
		ps.Add("No", mobile);
		DataTable dt=DBAccess.RunSQLReturnTable(ps);

		if(dt.Rows.size()<=0){
			return "err@不存在此用户信息，userID:"+userID+"-----";
		}
		else{
			//执行登录
			bp.wf.Dev2Interface.Port_Login(dt.Rows.get(0).getValue("No").toString());
			if(state.equals("Start"))
				return "/CCMobile/Start.htm";
			else if(state.equals("Todolist"))
				return "/CCMobile/Todolist.htm";
			else if(state.equals("Runing"))
				return "/CCMobile/Runing.htm";
			else if(state.equals("Complete"))
				return "/CCMobile/Complete.htm";
			else
				return "/CCMobilePortal/Home.htm";
		}

	}
}

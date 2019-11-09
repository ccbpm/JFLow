package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.ContextHolderUtils;
import BP.Difference.Handler.WebContralBase;
import BP.Sys.*;
import BP.Web.*;
import net.sf.json.JSONObject;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.WeiXin.DingDing;
import BP.WF.WeiXin.WeiXin;
import BP.WF.WeiXin.Util.Crypto.AesException;
import BP.WF.WeiXin.Util.Crypto.WXBizMsgCrypt;
import BP.WF.WeiXin.Util.Crypto.WeiXinUtil;
import BP.WF.Data.*;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/** 
 页面功能实体
*/
public class WF_AppClassic extends WebContralBase
{


	/** 
	 构造函数
	*/
	public WF_AppClassic()
	{

	}



		///#region 执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod()
	{
		switch (this.getDoType())
		{
			case "DtlFieldUp": //字段上移
				return "执行成功.";
			default:
				break;
		}
		// 找不不到标记就抛出异常.
		return "err@没有判断的执行标记:" + this.getDoType();
	}

		///#endregion 执行父类的重写方法.


		///#region xxx 界面 .


		///#endregion xxx 界面方法.

	/** 
	 初始化Home
	 
	 @return 
	 * @throws Exception 
	*/
	@SuppressWarnings("unchecked")
	public final String Home_Init() throws Exception
	{
		Hashtable ht = new Hashtable();
		ht.put("UserNo", WebUser.getNo());
		ht.put("UserName", WebUser.getName());

		//系统名称.
		ht.put("SysName", SystemConfig.getSysName());
		ht.put("CustomerName", SystemConfig.getCustomerName());

		ht.put("Todolist_EmpWorks", Dev2Interface.getTodolist_EmpWorks());
		ht.put("Todolist_Runing", Dev2Interface.getTodolist_Runing());
		ht.put("Todolist_Sharing", Dev2Interface.getTodolist_Sharing());
		ht.put("Todolist_CCWorks", Dev2Interface.getTodolist_CCWorks());
		ht.put("Todolist_Apply", Dev2Interface.getTodolist_Apply()); //申请下来的任务个数.
		ht.put("Todolist_Draft", Dev2Interface.getTodolist_Draft()); //草稿数量.
		ht.put("Todolist_Complete", Dev2Interface.getTodolist_Complete()); //完成数量.
		ht.put("UserDeptName", WebUser.getFK_DeptName());

		//我发起
		MyStartFlows myStartFlows = new MyStartFlows();
		QueryObject obj = new QueryObject(myStartFlows);
		obj.AddWhere(MyStartFlowAttr.Starter, WebUser.getNo());
		obj.addAnd();
		//运行中\已完成\挂起\退回\转发\加签\批处理\
		obj.addLeftBracket();
		obj.AddWhere("WFState=2 or WFState=3 or WFState=4 or WFState=5 or WFState=6 or WFState=8 or WFState=10");
		obj.addRightBracket();
		obj.DoQuery();
		ht.put("Todolist_MyStartFlow", myStartFlows.size());

		//我参与
		MyJoinFlows myFlows = new MyJoinFlows();
		obj = new QueryObject(myFlows);
		obj.AddWhere("Emps like '%" + WebUser.getNo() + "%'");
		obj.DoQuery();
		ht.put("Todolist_MyFlow", myFlows.size());

		return BP.Tools.Json.ToJsonEntityModel(ht);
	}
	@SuppressWarnings("unchecked")
	public final String Index_Init() throws Exception
	{
		Hashtable ht = new Hashtable();
		ht.put("Todolist_Runing", Dev2Interface.getTodolist_Runing()); //运行中.
		ht.put("Todolist_EmpWorks", Dev2Interface.getTodolist_EmpWorks()); //待办
		ht.put("Todolist_CCWorks", Dev2Interface.getTodolist_CCWorks()); //抄送.

		//本周.
		ht.put("TodayNum", Dev2Interface.getTodolist_CCWorks()); //抄送.

		return BP.Tools.Json.ToJsonEntityModel(ht);
	}


		///#region 登录界面.
	public final String Portal_Login() throws Exception
	{
		String userNo = this.GetRequestVal("UserNo");

		try
		{
			BP.Port.Emp emp = new Emp(userNo);

			Dev2Interface.Port_Login(emp.getNo());
			return ".";
		}
		catch (RuntimeException ex)
		{
			return "err@用户[" + userNo + "]登录失败." + ex.getMessage();
		}

	}
	/** 
	 登录.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Login_Submit() throws Exception
	{
		try
		{
			String userNo = this.GetRequestVal("TB_No");
			if (userNo == null)
			{
				userNo = this.GetRequestVal("TB_UserNo");
			}

			String pass = this.GetRequestVal("TB_PW");
			if (pass == null)
			{
				pass = this.GetRequestVal("TB_Pass");
			}

			BP.Port.Emp emp = new Emp();
			emp.setNo(userNo);
			if (emp.RetrieveFromDBSources() == 0)
			{
				if (DBAccess.IsExitsTableCol("Port_Emp", "NikeName") == true)
				{
					/*如果包含昵称列,就检查昵称是否存在.*/
					Paras ps = new Paras();
					ps.SQL = "SELECT No FROM Port_Emp WHERE NikeName=" + SystemConfig.getAppCenterDBVarStr() + "NikeName";
					ps.Add("NikeName", userNo);
					String no = DBAccess.RunSQLReturnStringIsNull(ps, null);
					if (no == null)
					{
						return "err@用户名或者密码错误.";
					}

					emp.setNo(no);
					int i = emp.RetrieveFromDBSources();
					if (i == 0)
					{
						return "err@用户名或者密码错误.";
					}
				}
				else if (DBAccess.IsExitsTableCol("Port_Emp", "Name") == true)
				{
					/*如果包含Name列,就检查Name是否存在.*/
					Paras ps = new Paras();
					ps.SQL = "SELECT No FROM Port_Emp WHERE Name=" + SystemConfig.getAppCenterDBVarStr() + "Name";
					ps.Add("Name", userNo);
					String no = DBAccess.RunSQLReturnStringIsNull(ps, null);
					if (no == null)
					{
						return "err@用户名或者密码错误.";
					}

					emp.setNo(no);
					int i = emp.RetrieveFromDBSources();
					if (i == 0)
					{
						return "err@用户名或者密码错误.";
					}


				}
				else
				{
					return "err@用户名或者密码错误.";
				}
			}

			if (emp.CheckPass(pass) == false)
			{
				return "err@用户名或者密码错误.";
			}

			//调用登录方法.
			BP.WF.Dev2Interface.Port_Login(emp.getNo());

			return "";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 执行登录
	 
	 @return 
	 * @throws Exception 
	*/
	@SuppressWarnings("unchecked")
	public final String Login_Init() throws Exception
	{
		Hashtable ht = new Hashtable();
		ht.put("SysName", SystemConfig.getSysName());
		ht.put("ServiceTel", SystemConfig.getServiceTel());
		ht.put("CustomerName", SystemConfig.getCustomerName());
		if (WebUser.getNoOfRel() == null)
		{
			ht.put("UserNo", "");
			ht.put("UserName", "");
		}
		else
		{
			ht.put("UserNo", WebUser.getNo());

			String name = WebUser.getName();

			if (DataType.IsNullOrEmpty(name) == true)
			{
				ht.put("UserName", WebUser.getNo());
			}
			else
			{
				ht.put("UserName", name);
			}
		}

		return BP.Tools.Json.ToJsonEntityModel(ht);
	}
	
	/**
	 * 微信回调/验证
	 * @throws Exception
	 */
	public void WeiXin_Init() throws Exception{

		//微信回调
		//1.获取签名（signature）、时间戳(timestamp)、随机字符串(nonce) 和验证回调的URL
		String signature = this.GetRequestVal("msg_signature");//url中的签名
	
		String timestamp = this.GetRequestVal("timestamp");//url中的时间戳
	
		String nonce = this.GetRequestVal("nonce");//url中的随机字符串
		
		String echostr = this.GetRequestVal("echostr");// 创建套件时验证回调url有效性时传入
		WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(SystemConfig.getWX_WeiXinToken(), SystemConfig.getWX_EncodingAESKey(), SystemConfig.getWX_CorpID());
        
		String sEchoStr  = wxcpt.VerifyURL(signature, timestamp, nonce, echostr);
        //必须要返回解密之后的明文
        getResponse().getWriter().write(sEchoStr);
        
        //根据 InfoType得到不同类型的回调数据
		 String postData = getStringInputstream(getRequest());
		 String returnMsg = wxcpt.DecryptMsg(signature, timestamp, nonce, postData);
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	     DocumentBuilder db = dbf.newDocumentBuilder();
	     StringReader sr = new StringReader(returnMsg);
	     InputSource is = new InputSource(sr);
	     Document document = db.parse(is);
	
	     Element root = document.getDocumentElement();
	     String msgType = root.getElementsByTagName("MsgType").item(0).toString();//信息类型
	     String userID = root.getElementsByTagName("FromUserName").item(0).toString();//员工的ID
	     String CorpID = root.getElementsByTagName("ToUserName").item(0).toString();//企业的 CorpID
         
         String eventStr = "";
         String sb="";
         if (msgType.equals("event") || msgType == "event")
         {
             eventStr = root.getElementsByTagName("Event").item(0).toString();
             if (eventStr.equals("subscribe"))
             {
                 //关注我
                 sb = WeiXinUtil.ResponseMsg(userID, null, null, "text", " 关注了");
             }
             if (eventStr.equals("unsubscribe"))
             {
                 //取消关注了我们
             }
             

             if (eventStr.equals("enter_agent"))
             {
                 sb = WeiXinUtil.ResponseMsg(userID, null, null, "text", "欢迎您：" + userID + "<a href=\"www.baidu.com\">点击跳转</a>");
             }

         }
         if(DataType.IsNullOrEmpty(sb)==true){
        	//发送消息或者登陆信息
             boolean IsSend = new WeiXin().PostWeiXinMsg(sb);
             if (IsSend == true)
            	 getResponse().getWriter().write("数据请求成功");
             else	 
            	 getResponse().getWriter().write("err@数据请求失败");
         }
         
	}
   
	
	public String WeiXin_Login() throws Exception{
		//获取code
		String code = this.GetRequestVal("code");
		System.out.println("code="+code);
		if(DataType.IsNullOrEmpty(code) == true)
			return "err@临时登录凭证code为空";
		//获取token
		String access_token = new WeiXin().GenerAccessToken();
		if(access_token.contains("err@")==true)
			return access_token;
		if(DataType.IsNullOrEmpty(WebUser.getNo()) == true){
			//根据token获取用户信息(用户名/手机号)
			String userId = new WeiXin().getUserInfo(code, access_token);
			if(DataType.IsNullOrEmpty(userId)==true)
				return "err@用户没有权限或者数据请求失败，请联系管理员";
			Emp emp = new Emp();
			emp.setNo(userId);
			if(emp.RetrieveFromDBSources() == 0){ 
				BP.GPM.Emps emps = new BP.GPM.Emps();
				int num = emps.Retrieve(BP.GPM.EmpAttr.Tel, userId);
				if(num ==0)
					return "err@在系统中没有找到账号为"+userId+"的信息，请联系管理员";
				
				BP.WF.Dev2Interface.Port_Login(emps.getItem(0).getNo());
				int expiry = 60 * 60 * 24 * 2;
				ContextHolderUtils.addCookie("Token", expiry, access_token);
				WebUser.setToken(access_token);
				return "登陆成功";
			}
			BP.WF.Dev2Interface.Port_Login(userId);
			int expiry = 60 * 60 * 24 * 2;
			ContextHolderUtils.addCookie("Token", expiry, access_token);
			WebUser.setToken(access_token);
		}
		
		return "登陆成功";
		
	}
	
	public String DingDing_Login() throws Exception{
		DingDing ding = new DingDing();
		//获取code
		String code = this.GetRequestVal("code");
		System.out.println("code="+code);
		if(DataType.IsNullOrEmpty(code) == true)
			return "err@临时登录凭证code为空";
		//获取token
		String access_token = ding.GenerAccessToken();
		if(access_token.contains("err@")==true)
			return access_token;
		if(DataType.IsNullOrEmpty(WebUser.getNo()) == true){
			//根据token获取用户信息(用户名/手机号)
			String json = ding.getUserInfo(code, access_token);
			if(DataType.IsNullOrEmpty(json)==true)
				return "err@用户没有权限或者数据请求失败，请联系管理员";
			String userId="";
			JSONObject jd = JSONObject.fromObject(json);
			if(jd.get("errcode").toString().equals("0") == false)
				return "err@用户没有权限或者数据请求失败，请联系管理员";
			
			userId  = jd.get("userid").toString();
			Emp emp = new Emp();
			emp.setNo(userId);
			if(emp.RetrieveFromDBSources() == 0){ 
				//获取name
				String name = jd.get("name").toString();
				BP.GPM.Emps emps = new BP.GPM.Emps();
				int num = emps.Retrieve(BP.GPM.EmpAttr.Name, name);
				if(num ==0)
					return "err@在系统中没有找到账号为"+userId+"的信息，请联系管理员";
				
				BP.WF.Dev2Interface.Port_Login(emps.getItem(0).getNo());
				int expiry = 60 * 60 * 24 * 2;
				ContextHolderUtils.addCookie("Token", expiry, access_token);
				WebUser.setToken(access_token);
				return "登陆成功";
			}
			BP.WF.Dev2Interface.Port_Login(userId);
			int expiry = 60 * 60 * 24 * 2;
			ContextHolderUtils.addCookie("Token", expiry, access_token);
			WebUser.setToken(access_token);
		}
		
		return "登陆成功";
		
	}
	
    private  String getStringInputstream(HttpServletRequest request) {
        StringBuffer strb = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String str = null;
            while (null != (str = reader.readLine())) {
                strb.append(str);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strb.toString();
    }

}
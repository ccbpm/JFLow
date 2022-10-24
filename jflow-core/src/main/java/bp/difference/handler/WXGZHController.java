package bp.difference.handler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import bp.difference.SystemConfig;



@Controller
@RequestMapping("/WF/WXZFH")
@Scope("request") 
public class WXGZHController {
	@RequestMapping(value="Start",method=RequestMethod.GET)
	public void start(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("开始签名校验");
        String token=SystemConfig.getWXGZH_WeiXinToken();
        //得到服务器传过来的4个参数
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        ArrayList<String> array = new ArrayList<String>();
        array.add(signature);
        array.add(timestamp);
        array.add(nonce);

        // 1.将token、timestamp、nonce三个参数进行字典序排序
        String sortString = sort(token, timestamp, nonce);
        // 2. 将三个参数字符串拼接成一个字符串进行sha1加密
        String mytoken = SHA1(sortString);
        // 3.将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        if (mytoken != null && mytoken != "" && mytoken.equals(signature)) {
            System.out.println("签名校验通过。");
            response.getWriter().println(echostr); //如果检验成功输出echostr，微信服务器接收到此输出，才会确认检验完成。

        } else {
            System.out.println("签名校验失败。");
        }
    } 
	
	
	
	/**
     * 排序方法
     * param token
     * param timestamp
     * param nonce
     * @return
     */
    public static String sort(String token, String timestamp, String nonce) {
        String[] strArray = { token, timestamp, nonce };
        Arrays.sort(strArray);

        StringBuilder sbuilder = new StringBuilder();
        for (String str : strArray) {
            sbuilder.append(str);
        }

        return sbuilder.toString();
    }
    /**
     * 
     * param decript待加密字段
     * @return
     */
    public static String SHA1(String decript) {
    	try {
    		MessageDigest digest = MessageDigest.getInstance("SHA-1");
    		digest.update(decript.getBytes());
    		byte messageDigest[] = digest.digest();
    		// Create Hex String
    		StringBuffer hexString = new StringBuffer();
    		// 字节数组转换为 十六进制 数
    		for (int i = 0; i < messageDigest.length; i++) {
    			String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
    			if (shaHex.length() < 2) {
    				hexString.append(0);
    			}
    			hexString.append(shaHex);
    		}
    		return hexString.toString();

    	} catch (NoSuchAlgorithmException e) {
    		e.printStackTrace();
    	}
    	return "";
    }

}

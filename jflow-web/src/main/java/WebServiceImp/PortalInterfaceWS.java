package WebServiceImp;

import javax.jws.WebMethod;
import javax.jws.WebService;

import WebService.PortalInterfaceWSI;

/**
 * 调用接口的地方
 * @author Administrator
 *
 */

public class PortalInterfaceWS  implements PortalInterfaceWSI{
	/**
	 * 发送消息接口
	 * @param sender 发送者
	 * @param sendToEmpNo 接收者
	 * @param title 标题
	 * @param msgInfo 内容
	 * @param OpenUrl 详情URL
	 * @param msgType 消息类型
	 * @return
	 * @throws Exception
	 */
	@Override

	public boolean SendToWebServices(String sender, String sendToEmpNo, String title,String msgInfo, String OpenUrl, String msgType)throws Exception{
		return true;
	}
}

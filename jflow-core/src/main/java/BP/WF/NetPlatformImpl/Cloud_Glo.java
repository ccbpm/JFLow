package BP.WF.NetPlatformImpl;

import BP.WF.*;

public class Cloud_Glo
{
	/** 
	 获得Soap
	 
	 @return 
	*/
	public static BP.WF.CloudWS.WSSoapClient GetSoap()
	{
		TimeSpan ts = new TimeSpan(0, 1, 0);
		BasicHttpBinding basicBinding = new BasicHttpBinding();
		basicBinding.setReceiveTimeout(ts);
		basicBinding.setSendTimeout(ts);
		basicBinding.setMaxBufferSize(2147483647);
		basicBinding.setMaxReceivedMessageSize(2147483647);
		basicBinding.setName("WSSoapClient");
		basicBinding.Security.Mode = BasicHttpSecurityMode.None;
		String url = "http://online.ccflow.org/App/TemplateInterface/WS.asmx";
		//string url = "http://localhost:8482/App/TemplateInterface/WS.asmx";

		EndpointAddress endPoint = new EndpointAddress(url);
		java.lang.reflect.Constructor ctor = BP.WF.CloudWS.WSSoapClient.class.GetConstructor(new java.lang.Class[] {Binding.class, EndpointAddress.class});

		return (BP.WF.CloudWS.WSSoapClient)ctor.newInstance(new Object[] {basicBinding, endPoint});
	}
}
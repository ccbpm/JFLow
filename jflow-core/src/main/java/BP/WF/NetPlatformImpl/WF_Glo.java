package BP.WF.NetPlatformImpl;

import BP.WF.*;
import BP.WF.*;

public class WF_Glo
{
	/** 
	 得到WebService对象 
	 
	 @return 
	*/
	public static WF.CCInterface.PortalInterfaceSoapClient GetPortalInterfaceSoapClient()
	{
		TimeSpan ts = new TimeSpan(0, 5, 0);
		BasicHttpBinding basicBinding = new BasicHttpBinding();
		basicBinding.setReceiveTimeout(ts);
		basicBinding.setSendTimeout(ts);
		basicBinding.setMaxBufferSize(2147483647);
		basicBinding.setMaxReceivedMessageSize(2147483647);
		basicBinding.setName("PortalInterfaceSoap");
		basicBinding.Security.Mode = BasicHttpSecurityMode.None;

		String url = "";
		if (Glo.getPlatform() == Platform.CCFlow)
		{
			url = "/DataUser/PortalInterface.asmx";
			url = Glo.getHostURL() + url;
		}
		else
		{
			//  url = string.Format("/{0}webservices/webservice.*", AppName != string.Empty ? AppName + "/" : string.Empty);
			//    url = new Uri(App.Current.Host.Source, "../").ToString() + "service/Service?wsdl";
		}

		url = url.replace("//", "/");
		url = url.replace(":/", "://");

		//  MessageBox.Show(url);

		EndpointAddress endPoint = new EndpointAddress(url);
		java.lang.reflect.Constructor ctor = WF.CCInterface.PortalInterfaceSoapClient.class.GetConstructor(new java.lang.Class[] {Binding.class, EndpointAddress.class});
		return (WF.CCInterface.PortalInterfaceSoapClient)ctor.newInstance(new Object[] {basicBinding, endPoint});
	}
}
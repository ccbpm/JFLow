package BP.NetPlatformImpl;

import BP.DA.*;
import BP.En30.ccportal.*;
import java.io.*;

public class DA_DataType
{
	public static PortalInterfaceSoapClient GetPortalInterfaceSoapClientInstance()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if DEBUG
		 TimeSpan ts = new TimeSpan(0, 10, 0);
//#else
		TimeSpan ts = new TimeSpan(0, 1, 0);
//#endif
		BasicHttpBinding basicBinding = new BasicHttpBinding();
		basicBinding.setReceiveTimeout(ts);
		basicBinding.setSendTimeout(ts);
		basicBinding.setMaxBufferSize(2147483647);
		basicBinding.setMaxReceivedMessageSize(2147483647);
		basicBinding.setName("PortalInterfaceSoapClient");
		basicBinding.Security.Mode = BasicHttpSecurityMode.None;

		//url.
		String url = DataType.getBPMHost() + "/DataUser/PortalInterface.asmx";

		EndpointAddress endPoint = new EndpointAddress(url);
		java.lang.reflect.Constructor ctor = BP.En30.ccportal.PortalInterfaceSoapClient.class.GetConstructor(new java.lang.Class[] {Binding.class, EndpointAddress.class});
		return (BP.En30.ccportal.PortalInterfaceSoapClient)ctor.newInstance(new Object[] {basicBinding, endPoint});
	}
}
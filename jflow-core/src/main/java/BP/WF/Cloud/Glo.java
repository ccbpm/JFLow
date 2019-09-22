package BP.WF.Cloud;

import BP.WF.*;

public class Glo
{
	/** 
	 获得Soap
	 
	 @return 
	*/
	public static BP.WF.CloudWS.WSSoapClient GetSoap()
	{
		// xs 2019-7-29
		return BP.WF.NetPlatformImpl.Cloud_Glo.GetSoap();
	}
}
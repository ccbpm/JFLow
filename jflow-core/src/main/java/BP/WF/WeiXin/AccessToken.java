package BP.WF.WeiXin;

import BP.WF.*;
import java.util.*;

public class AccessToken
{
	private String access_token;
	public final String getaccess_token()
	{
		return access_token;
	}
	public final void setaccess_token(String value)
	{
		access_token = value;
	}
	private String expires_in;
	public final String getexpires_in()
	{
		return expires_in;
	}
	public final void setexpires_in(String value)
	{
		expires_in = value;
	}
}
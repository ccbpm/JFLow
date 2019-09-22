package BP.WF.WeiXin;

import BP.WF.*;
import java.util.*;

public class MessageErrorModel
{
	private String errcode;
	public final String geterrcode()
	{
		return errcode;
	}
	public final void seterrcode(String value)
	{
		errcode = value;
	}
	private String errmsg;
	public final String geterrmsg()
	{
		return errmsg;
	}
	public final void seterrmsg(String value)
	{
		errmsg = value;
	}
	private String invaliduser;
	public final String getinvaliduser()
	{
		return invaliduser;
	}
	public final void setinvaliduser(String value)
	{
		invaliduser = value;
	}
	private String invalidparty;
	public final String getinvalidparty()
	{
		return invalidparty;
	}
	public final void setinvalidparty(String value)
	{
		invalidparty = value;
	}
	private String invalidtag;
	public final String getinvalidtag()
	{
		return invalidtag;
	}
	public final void setinvalidtag(String value)
	{
		invalidtag = value;
	}
}
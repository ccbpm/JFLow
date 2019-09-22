package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Port.*;
import BP.En.*;
import BP.Tools.*;
import BP.WF.*;
import BP.Web.*;
import BP.Sys.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.util.*;

public class WSMethod
{
	private String No;
	public final String getNo()
	{
		return No;
	}
	public final void setNo(String value)
	{
		No = value;
	}

	private String Name;
	public final String getName()
	{
		return Name;
	}
	public final void setName(String value)
	{
		Name = value;
	}

	private HashMap<String, String> ParaMS;
	public final HashMap<String, String> getParaMS()
	{
		return ParaMS;
	}
	public final void setParaMS(HashMap<String, String> value)
	{
		ParaMS = value;
	}

	private String Return;
	public final String getReturn()
	{
		return Return;
	}
	public final void setReturn(String value)
	{
		Return = value;
	}
}
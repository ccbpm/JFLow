package bp.wf.httphandler;

import java.util.HashMap;

public class WSMethod
{
	private String No;
	public final String getNo()
	{
		return No;
	}
	public final void setNo(String value) throws Exception
	{
		No = value;
	}

	private String Name;
	public final String getName()
	{
		return Name;
	}
	public final void setName(String value) throws Exception
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
	public final void setReturn(String value) throws Exception
	{
		Return = value;
	}
}
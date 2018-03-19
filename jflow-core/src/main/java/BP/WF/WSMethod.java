package BP.WF;
public class WSMethod
{
	private String privateNO;
	public final String getNO()
	{
		return privateNO;
	}
	public final void setNO(String value)
	{
		privateNO = value;
	}

	private String privateNAME;
	public final String getNAME()
	{
		return privateNAME;
	}
	public final void setNAME(String value)
	{
		privateNAME = value;
	}

	private java.util.HashMap<String, String> privatePARAMS;
	public final java.util.HashMap<String, String> getPARAMS()
	{
		return privatePARAMS;
	}
	public final void setPARAMS(java.util.HashMap<String, String> value)
	{
		privatePARAMS = value;
	}

	private String privateRETURN;
	public final String getRETURN()
	{
		return privateRETURN;
	}
	public final void setRETURN(String value)
	{
		privateRETURN = value;
	}
}
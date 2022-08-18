package bp.wf.httphandler;

import bp.da.*;
import bp.en.*;
import bp.web.*;
import bp.sys.*;
import bp.wf.template.*;
import bp.wf.template.frm.*;
import bp.difference.*;
import bp.*;
import bp.wf.*;
import java.util.*;

public class WSMethod
{
	private String No;
	public final String getNo() throws Exception {
		return No;
	}
	public final void setNo(String value)throws Exception
	{No = value;
	}

	private String Name;
	public final String getName() throws Exception {
		return Name;
	}
	public final void setName(String value)throws Exception
	{Name = value;
	}

	private HashMap<String, String> ParaMS;
	public final HashMap<String, String> getParaMS() throws Exception {
		return ParaMS;
	}
	public final void setParaMS(HashMap<String, String> value)throws Exception
	{ParaMS = value;
	}

	private String Return;
	public final String getReturn() throws Exception {
		return Return;
	}
	public final void setReturn(String value)throws Exception
	{Return = value;
	}
}
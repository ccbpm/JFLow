package bp.wf.httphandler;

import bp.*;
import bp.wf.*;

/** 
 调Handler，返回值data的通用对象类
*/
public class JsonResultInnerData
{
	/** 
	 信息
	*/
	private String Msg;
	public final String getMsg() throws Exception {
		return Msg;
	}
	public final void setMsg(String value)throws Exception
	{Msg = value;
	}
	/** 
	 返回数据对象
	*/
	private Object InnerData;
	public final Object getInnerData() throws Exception {
		return InnerData;
	}
	public final void setInnerData(Object value)throws Exception
	{InnerData = value;
	}
}
package bp.tools;

import bp.en.*;
import bp.da.*;
import bp.*;

/** 
 返回信息格式
 
 <typeparam name="T"></typeparam>
*/
public class MethodReturnMessage<T>
{
	/** 
	 是否运行成功
	*/
	private boolean Success;
	public final boolean getSuccess() throws Exception {
		return Success;
	}
	public final void setSuccess(boolean value)throws Exception
	{Success = value;
	}
	/** 
	 信息    
	*/
	private String Message;
	public final String getMessage() throws Exception {
		return Message;
	}
	public final void setMessage(String value)throws Exception
	{Message = value;
	}
	/** 
	 返回的数据
	*/
	private T Data;
	public final T getData() throws Exception {
		return Data;
	}
	public final void setData(T value)throws Exception
	{Data = value;
	}
}
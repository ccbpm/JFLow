package bp.en;

import bp.da.*;
import bp.*;
import java.io.*;

/** 
 EnDtl 的摘要说明。
*/
public class EnDtl
{
	/** 
	 明细
	*/
	public EnDtl()
	{
	}
	/** 
	 明细
	 
	 @param className 类名称
	*/
	public EnDtl(String className)
	{
		this.setEns(ClassFactory.GetEns(className));
	}
	/** 
	 类名称
	*/
	public final String getEnsName()
	{
		return this.getEns().toString();
	}
	/** 
	 明细
	*/
	private Entities _Ens = null;
	/** 
	 获取或设置 他的集合
	*/
	public final Entities getEns()
	{
		return _Ens;
	}
	public final void setEns(Entities value)
	{
		_Ens = value;
	}
	/** 
	 他关连的key
	*/
	private String _refKey = null;
	/** 
	 他关连的 key
	*/
	public final String getRefKey()
	{
		return _refKey;
	}
	public final void setRefKey(String value) throws Exception
	{
		this._refKey = value;
	}
	/** 
	 描述
	*/
	private String _Desc = null;
	/** 
	 描述
	 * @throws Exception 
	*/
	public final String getDesc() throws Exception
	{
		if (this._Desc == null)
		{
			this._Desc = this.getEns().getGetNewEntity().getEnDesc();
		}
		return _Desc;
	}
	public final void setDesc(String value) throws Exception
	{
		_Desc = value;
	}
	/** 
	 显示到分组
	*/
	private String _groupName = null;
	/** 
	 显示到分组
	*/
	public final String getGroupName()
	{
		return _groupName;
	}
	public final void setGroupName(String value) throws Exception
	{
		this._groupName = value;
	}
}
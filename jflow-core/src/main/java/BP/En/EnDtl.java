package BP.En;


/**
 * EnDtl 的摘要说明。
 */
public class EnDtl
{
	
	/**
	 * 明细
	 */
	public EnDtl()
	{
	}
	
	/**
	 * 明细
	 * 
	 * @param className
	 *            类名称
	 */
	public EnDtl(String className)
	{
		this.setEns(ClassFactory.GetEns(className));
	}
	
	/**
	 * 类名称
	 */
	public final String getEnsName()
	{
		return this.getEns().toString();
	}
	
	/**
	 * 明细
	 */
	public Entities _Ens = null;
	
	/**
	 * 获取或设置 他的集合
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
	 * 他关连的key
	 */
	private String _refKey = null;
	
	/**
	 * 他关连的 key
	 */
	public final String getRefKey()
	{
		return _refKey;
	}
	
	public final void setRefKey(String value)
	{
		this._refKey = value;
	}
	
	/**
	 * 描述
	 */
	private String _Desc = null;
	public String GroupName = null;	
	
	/**
	 * 描述
	 */
	public final String getDesc()
	{
		if (this._Desc == null)
		{
			this._Desc = this.getEns().getGetNewEntity().getEnDesc();
		}
		return _Desc;
	}
	
	public final void setDesc(String value)
	{
		_Desc = value;
	}
}
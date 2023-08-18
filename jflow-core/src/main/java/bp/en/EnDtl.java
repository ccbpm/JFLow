package bp.en;

import bp.da.*;

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
	 编辑器模式 0=默认的DtlBatch.htm, 1=DtlSearch.htm
	 */
	public DtlEditerModel DtlEditerModel = bp.en.DtlEditerModel.DtlBatch;
	/**
	 类名称
	 */
	public final String getEnsName()
	{
		return this.Ens.toString();
	}
	/**
	 明细
	 */
	public Entities Ens = null;

	public Entities getEns() {
		return Ens;
	}

	public void setEns(Entities ens) {
		Ens = ens;
	}

	public String UrlExt = null;
	/**
	 他关连的 key
	 */
	public String RefKey = null;

	public String getRefKey() {
		return RefKey;
	}

	public void setRefKey(String refKey) {
		RefKey = refKey;
	}

	private String _desc = "";
	/**
	 描述
	 */
	public final String getDesc() throws Exception {
		if (DataType.IsNullOrEmpty(_desc))
		{
			return this.Ens.getNewEntity().getEnDesc();
		}
		return this._desc;
	}
	public final void setDesc(String value)
	{
		this._desc = value;
	}
	/**
	 显示到分组
	 */
	public String GroupName = null;
	public String Icon = null;

}

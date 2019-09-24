package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Web.WebUser;

import java.util.*;
import java.math.*;

/** 
 通用从表
*/
public class GEDtl extends EntityOID
{
		///#region 构造函数
	@Override
	public String toString()
	{
		return this.FK_MapDtl;
	}
	@Override
	public String getClassID()
	{
		return this.FK_MapDtl;
	}
	public final String getRDT() throws Exception
	{
		return this.GetValStringByKey(GEDtlAttr.RDT);
	}
	public final void setRDT(String value) throws Exception
	{
		this.SetValByKey(GEDtlAttr.RDT, value);
	}
	public final String getRec() throws Exception
	{
		return this.GetValStringByKey(GEDtlAttr.Rec);
	}
	public final void setRec(String value) throws Exception
	{
		this.SetValByKey(GEDtlAttr.Rec, value);
	}
	/** 
	 关联的PK值
	 * @throws Exception 
	*/
	public final String getRefPK() throws Exception
	{
		return this.GetValStringByKey(GEDtlAttr.RefPK);
	}
	public final void setRefPK(String value) throws Exception
	{
		this.SetValByKey(GEDtlAttr.RefPK, value);
	}
	public final long getRefPKInt64() throws Exception
	{
		return this.GetValInt64ByKey(GEDtlAttr.RefPK);
	}
	public final void setRefPKInt64(long value) throws Exception
	{
		this.SetValByKey(GEDtlAttr.RefPK, value);
	}
	/** 
	 行是否锁定
	 * @throws Exception 
	*/
	public final boolean getIsRowLock() throws Exception
	{
		return this.GetValBooleanByKey(GEDtlAttr.IsRowLock);
	}
	public final void setIsRowLock(boolean value) throws Exception
	{
		this.SetValByKey(GEDtlAttr.IsRowLock, value);
	}
	/** 
	 关联的PKint
	 * @throws Exception 
	*/
	public final int getRefPKInt() throws Exception
	{
		return this.GetValIntByKey(GEDtlAttr.RefPK);
	}
	public final void setRefPKInt(int value) throws Exception
	{
		this.SetValByKey(GEDtlAttr.RefPK, value);
	}
	public final long getFID() throws Exception
	{
		return this.GetValInt64ByKey(GEDtlAttr.FID);
	}
	public final void setFID(long value) throws Exception
	{
		this.SetValByKey(GEDtlAttr.FID, value);
	}
	/** 
	 主键
	*/
	public String FK_MapDtl = null;
	/** 
	 通用从表
	*/
	public GEDtl()
	{
	}
	/** 
	 通用从表
	 
	 @param nodeid 节点ID
	*/
	public GEDtl(String fk_mapdtl)
	{
		this.FK_MapDtl = fk_mapdtl;
	}
	/** 
	 通用从表
	 
	 @param nodeid 节点ID
	 @param _oid OID
	 * @throws Exception 
	*/
	public GEDtl(String fk_mapdtl, int _oid) throws Exception
	{
		this.FK_MapDtl = fk_mapdtl;
		this.setOID(_oid);
	}

	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		if (this.FK_MapDtl == null)
		{
			throw new RuntimeException("没有给" + this.FK_MapDtl + "值，您不能获取它的Map。");
		}

		MapDtl md = new MapDtl(this.FK_MapDtl);
		this.set_enMap(md.GenerMap());
		return this.get_enMap();
	}
	/** 
	 GEDtls
	*/
	@Override
	public Entities getGetNewEntities()
	{
		if (this.FK_MapDtl == null)
		{
			return new GEDtls();
		}

		return new GEDtls(this.FK_MapDtl);
	}
	public final boolean IsChange(GEDtl dtl) throws Exception
	{
		Attrs attrs = dtl.getEnMap().getAttrs();
		for (Attr attr : attrs)
		{
			Object oldVal = this.GetValByKey(attr.getKey());
			Object val = dtl.GetValByKey(attr.getKey());
			if(oldVal == null){
				if(oldVal!=null)
					return true;
			}else{
				if(oldVal==null)
					return true;
				if(oldVal.toString().equals(val.toString())==true)
					continue;
				else
					return true;
			}
		}
		return false;
	}

	/** 
	 记录人
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeInsert() throws Exception
	{
		// 判断是否有变化的项目，决定是否执行储存。
		MapAttrs mattrs = new MapAttrs(this.FK_MapDtl);
		boolean isChange = false;
		for (MapAttr mattr : mattrs.ToJavaList())
		{
			if (isChange)
			{
				break;
			}
			switch (mattr.getKeyOfEn())
			{
				case "Rec":
				case "RDT":
				case "FID":
					break;
				default:
					if (mattr.getIsNum())
					{
						String s = this.GetValStrByKey(mattr.getKeyOfEn());
						if (DataType.IsNullOrEmpty(s))
						{
							this.SetValByKey(mattr.getKeyOfEn(), mattr.getDefVal());
							s = mattr.getDefVal().toString();
						}
						s = s.replace("￥", "");
						s = s.replace(",", "");

						if (mattr.getDefValDecimal().equals(BigDecimal.valueOf(Double.parseDouble((s)))))
						{
							continue;
						}
						isChange = true;
						break;
					}
					else
					{
						if (this.GetValStrByKey(mattr.getKeyOfEn()).equals(mattr.getDefVal()))
						{
							continue;
						}
						isChange = true;
						break;
					}
			}
		}
		if (isChange == false)
		{
			return false;
		}

		this.setRec(WebUser.getNo());
		this.setRDT(DataType.getCurrentDataTime());
		return super.beforeInsert();
	}
	
}
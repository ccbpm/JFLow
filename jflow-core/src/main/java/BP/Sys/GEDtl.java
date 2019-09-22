package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;

import java.util.*;
import java.math.*;

/** 
 通用从表
*/
public class GEDtl extends EntityOID
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
	public final String getRDT()
	{
		return this.GetValStringByKey(GEDtlAttr.RDT);
	}
	public final void setRDT(String value)
	{
		this.SetValByKey(GEDtlAttr.RDT, value);
	}
	public final String getRec()
	{
		return this.GetValStringByKey(GEDtlAttr.Rec);
	}
	public final void setRec(String value)
	{
		this.SetValByKey(GEDtlAttr.Rec, value);
	}
	/** 
	 关联的PK值
	*/
	public final String getRefPK()
	{
		return this.GetValStringByKey(GEDtlAttr.RefPK);
	}
	public final void setRefPK(String value)
	{
		this.SetValByKey(GEDtlAttr.RefPK, value);
	}
	public final long getRefPKInt64()
	{
		return this.GetValInt64ByKey(GEDtlAttr.RefPK);
	}
	public final void setRefPKInt64(long value)
	{
		this.SetValByKey(GEDtlAttr.RefPK, value);
	}
	/** 
	 行是否锁定
	*/
	public final boolean getIsRowLock()
	{
		return this.GetValBooleanByKey(GEDtlAttr.IsRowLock);
	}
	public final void setIsRowLock(boolean value)
	{
		this.SetValByKey(GEDtlAttr.IsRowLock, value);
	}
	/** 
	 关联的PKint
	*/
	public final int getRefPKInt()
	{
		return this.GetValIntByKey(GEDtlAttr.RefPK);
	}
	public final void setRefPKInt(int value)
	{
		this.SetValByKey(GEDtlAttr.RefPK, value);
	}
	public final long getFID()
	{
		return this.GetValInt64ByKey(GEDtlAttr.FID);
	}
	public final void setFID(long value)
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
	*/
	public GEDtl(String fk_mapdtl, int _oid)
	{
		this.FK_MapDtl = fk_mapdtl;
		this.setOID(_oid);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Map
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

		BP.Sys.MapDtl md = new BP.Sys.MapDtl(this.FK_MapDtl);
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
	public final boolean IsChange(GEDtl dtl)
	{
		Attrs attrs = dtl.getEnMap().getAttrs();
		for (Attr attr : attrs)
		{
			if (this.GetValByKey(attr.getKey()) == dtl.GetValByKey(attr.getKey()))
			{
				continue;
			}
			else
			{
				return true;
			}
		}
		return false;
	}
	@Override
	protected boolean beforeUpdate()
	{
		this.AutoFull(); //处理自动计算。
		return super.beforeUpdate();
	}
	/** 
	 记录人
	 
	 @return 
	*/
	@Override
	protected boolean beforeInsert()
	{
		// 判断是否有变化的项目，决定是否执行储存。
		MapAttrs mattrs = new MapAttrs(this.FK_MapDtl);
		boolean isChange = false;
		for (MapAttr mattr : mattrs)
		{
			if (isChange)
			{
				break;
			}
			switch (mattr.getKeyOfEn())
			{
				case "Rec":
				case "RDT":
			   // case "RefPK":
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

						if (mattr.getDefValDecimal().equals(BigDecimal.Parse(s)))
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
					break;
			}
		}
		if (isChange == false)
		{
			return false;
		}

		this.setRec(BP.Web.WebUser.getNo());
		this.setRDT(DataType.getCurrentDataTime());

		this.AutoFull(); //处理自动计算。
		return super.beforeInsert();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
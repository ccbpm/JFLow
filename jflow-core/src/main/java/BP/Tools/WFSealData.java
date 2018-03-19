package BP.Tools;

import BP.DA.DBAccess;
import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.Entities;
import BP.En.EntityMyPK;
import BP.En.Map;

/**
 * 表单签名信息
 */
public class WFSealData extends EntityMyPK
{
	// 用户日志信息键值列表
	
	// 基本属性
	public final String getSealData()
	{
		return this.GetValStringByKey(WFSealDataAttr.SealData);
	}
	
	public final void setSealData(String value)
	{
		this.SetValByKey(WFSealDataAttr.SealData, value);
	}
	
	/**
	 * FK_Emp
	 */
	public final String getFK_Node()
	{
		return this.GetValStringByKey(WFSealDataAttr.FK_Node);
	}
	
	public final void setFK_Node(String value)
	{
		this.SetValByKey(WFSealDataAttr.FK_Node, value);
	}
	
	public final String getRDT()
	{
		return this.GetValStringByKey(WFSealDataAttr.RDT);
	}
	
	public final void setRDT(String value)
	{
		this.SetValByKey(WFSealDataAttr.RDT, value);
	}
	
	public final String getFK_MapData()
	{
		return this.GetValStringByKey(WFSealDataAttr.FK_MapData);
	}
	
	public final void setFK_MapData(String value)
	{
		this.SetValByKey(WFSealDataAttr.FK_MapData, value);
	}
	
	public final String getOID()
	{
		return this.GetValStringByKey(WFSealDataAttr.OID);
	}
	
	public final void setOID(String value)
	{
		this.SetValByKey(WFSealDataAttr.OID, value);
	}
	
	// 构造方法
	/**
	 * 签名信息
	 */
	public WFSealData()
	{
	}
	
	/**
	 * 特殊处理 将SealData 字段变为大文本存储
	 * 
	 * @return
	 */
	public final void UpdateColumn()
	{
		
		String sql = "";
		
		switch (DBAccess.getAppCenterDBType())
		{
			case Oracle:
				sql = String.format("ALTER TABLE %1$s modify(%2$s %3$s)",
						"Sys_WFSealData", WFSealDataAttr.SealData, "CLOB");
				break;
			case Informix:
				// DBAccess.RunSQL(SqlBuilder.GenerCreateTableSQLOfInfoMix(this));
				break;
			case MSSQL:
				sql = String.format("alter table %1$s alter column %2$s  %3$s",
						"Sys_WFSealData", WFSealDataAttr.SealData, "text");
				break;
			case MySQL:
				sql = String.format("ALTER TABLE %1$s MODIFY COLUMN %2$s %3$s",
						"Sys_WFSealData", WFSealDataAttr.SealData, "text");
				break;
			case Access:
				// DBAccess.RunSQL(SqlBuilder.GenerCreateTableSQLOf_OLE(this));
				break;
			default:
				throw new RuntimeException("@未判断的数据库类型。");
		}
		if (!StringHelper.isNullOrEmpty(sql))
		{
			DBAccess.RunSQL(sql);
		}
	}
	
	/**
	 * EnMap
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_WFSealData");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		
		map.setEnDesc("签名信息");
		map.setEnType(EnType.Sys);
		map.AddMyPK();
		map.AddTBString(WFSealDataAttr.OID, null, "OID", false, false, 0, 200,
				20);
		map.AddTBString(WFSealDataAttr.FK_Node, null, "FK_Node", true, false,
				0, 200, 20);
		map.AddTBString(WFSealDataAttr.FK_MapData, null, "FK_MapData", true,
				false, 0, 300, 20);
		map.AddTBString(WFSealDataAttr.SealData, null, "SealData", true, false,
				0, 4000, 20);
		map.AddTBString(WFSealDataAttr.RDT, null, "记录日期", true, false, 0, 20,
				20);
		this.set_enMap(map);
		return this.get_enMap();
	}
	
	// 重写
	@Override
	public Entities getGetNewEntities()
	{
		return new WFSealDatas();
	}
	// 重写
}

package BP.Sys;

import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityOID;
import BP.En.Map;
import BP.En.QueryObject;
import BP.En.UAC;

public class GroupEnsTemplate extends EntityOID
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 基本属性
	/**
	 * 集合类名称
	 */
	public final String getEnsName()
	{
		return this.GetValStringByKey(GroupEnsTemplateAttr.EnsName);
	}
	
	public final void setEnsName(String value)
	{
		this.SetValByKey(GroupEnsTemplateAttr.EnsName, value);
	}
	
	/**
	 * 实体名称
	 */
	public final String getOperateCol()
	{
		return this.GetValStringByKey(GroupEnsTemplateAttr.OperateCol);
	}
	
	public final void setOperateCol(String value)
	{
		this.SetValByKey(GroupEnsTemplateAttr.OperateCol, value);
	}
	
	/**
	 * 数据源
	 */
	public final String getAttrs()
	{
		return this.GetValStringByKey(GroupEnsTemplateAttr.Attrs);
	}
	
	public final void setAttrs(String value)
	{
		this.SetValByKey(GroupEnsTemplateAttr.Attrs, value);
	}
	
	/**
	 * 名称
	 */
	public final String getName()
	{
		return this.GetValStringByKey(GroupEnsTemplateAttr.Name);
	}
	
	public final void setName(String value)
	{
		this.SetValByKey(GroupEnsTemplateAttr.Name, value);
	}
	
	public final String getEnName()
	{
		return this.GetValStringByKey(GroupEnsTemplateAttr.EnName);
	}
	
	public final void setEnName(String value)
	{
		this.SetValByKey(GroupEnsTemplateAttr.EnName, value);
	}
	
	public final String getRec()
	{
		return this.GetValStringByKey(GroupEnsTemplateAttr.Rec);
	}
	
	public final void setRec(String value)
	{
		this.SetValByKey(GroupEnsTemplateAttr.Rec, value);
	}
	
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsUpdate = true;
		uac.IsView = true;
		return super.getHisUAC();
	}
	
	/**
	 * 系统实体
	 */
	public GroupEnsTemplate()
	{
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
		Map map = new Map("Sys_GroupEnsTemplate");
		map.setDepositaryOfEntity(Depositary.None);
		map.setEnDesc("报表模板");
		map.setEnType(EnType.Sys);
		map.AddTBIntPKOID();
		map.AddTBString(GroupEnsTemplateAttr.EnName, null, "表称", false, false,
				0, 500, 20);
		map.AddTBString(GroupEnsTemplateAttr.Name, null, "报表名", true, false, 0,
				500, 20);
		map.AddTBString(GroupEnsTemplateAttr.EnsName, null, "报表类名", false,
				true, 0, 90, 10);
		map.AddTBString(GroupEnsTemplateAttr.OperateCol, null, "操作属性", false,
				true, 0, 90, 10);
		map.AddTBString(GroupEnsTemplateAttr.Attrs, null, "运算属性", false, true,
				0, 90, 10);
		map.AddTBString(GroupEnsTemplateAttr.Rec, null, "记录人", false, true, 0,
				90, 10);
		this.set_enMap(map);
		return this.get_enMap();
	}
	
	/**
	 * @param fk_emp
	 *            fk_emp
	 * @param className
	 *            className
	 * @param attrs
	 *            attrs
	 * @return
	 */
	public final int Search(String fk_emp, String className, String attrs)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(GroupEnsTemplateAttr.Rec, fk_emp);
		qo.addAnd();
		qo.AddWhere(GroupEnsTemplateAttr.Attrs, className);
		qo.addAnd();
		qo.AddWhere(GroupEnsTemplateAttr.EnsName, className);
		return qo.DoQuery();
	}
}
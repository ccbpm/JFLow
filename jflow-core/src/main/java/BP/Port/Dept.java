package BP.Port;

import BP.DA.DBUrl;
import BP.DA.DBUrlType;
import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.En.UAC;
import BP.Sys.PubClass;
import cn.jflow.common.util.ContextHolderUtils;

/**
 * 部门
 */
public class Dept extends EntityNoName
{
	  
	// 属性
	/**
	 * 父节点的ID
	 */
	public final String getParentNo()
	{
		return this.GetValStrByKey(DeptAttr.ParentNo);
	}
	
	public final void setParentNo(String value)
	{
		this.SetValByKey(DeptAttr.ParentNo, value);
	}
	
	public final String getParentName()
	{
		return this.GetValStrByKey(DeptAttr.ParentName);
	}
	
	public final void setParentName(String value)
	{
		this.SetValByKey(DeptAttr.ParentName, value);
	}
	 
	
	private Depts _HisSubDepts = null;
	
	/**
	 * 它的子节点
	 */
	public final Depts getHisSubDepts()
	{
		if (_HisSubDepts == null)
		{
			_HisSubDepts = new Depts(this.getNo());
		}
		return _HisSubDepts;
	}
	
	// 构造函数
	/**
	 * 部门
	 */
	public Dept()
	{
	}
	
	/**
	 * 部门
	 * 
	 * @param no
	 *            编号
	 */
	public Dept(String no)
	{
		super(no);
	}
	
	// 重写方法
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	
	/**
	 * Map
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		
		Map map = new Map();
		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN)); // 连接到的那个数据库上. (默认的是:
															// AppCenterDSN )
		map.setPhysicsTable("Port_Dept");
		map.setEnType(EnType.Admin);
		map.setCodeStruct("2");
		map.IsEnableVer = true;
		map.setEnDesc("部门"); // 实体的描述.
		map.setDepositaryOfEntity(Depositary.Application); // 实体map的存放位置.
		map.setDepositaryOfMap(Depositary.Application); // Map 的存放位置.
		
		map.AddTBStringPK(DeptAttr.No, null, "部门编号", true, true, 1, 50, 20);
		map.AddTBString(DeptAttr.Name, null, "部门名称", true, false, 0, 100, 30);
		map.AddTBString(DeptAttr.ParentNo, null, "父节点编号", true, false, 0,100,30);
		
		/* 保持该类的字段 精简，以方便集成.  */
		 
	 

		// 增加点对多属性
		this.set_enMap(map);
		 
		return this.get_enMap();
	}
	
	 
	
	@Override
	protected boolean beforeDelete()
	{
		Depts depts = new Depts();
		depts.Retrieve(DeptAttr.ParentNo, this.getNo());
		if (null != depts && 0 != depts.size())
		{
			PubClass.Alert("该部门，存在子部门，不能删除！", ContextHolderUtils.getResponse());
			return false;
		}
		return super.beforeDelete();
	}
}

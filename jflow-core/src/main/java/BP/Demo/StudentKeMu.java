package BP.Demo;

import BP.En.EnType;
import BP.En.EntityMM;
import BP.En.Map;
import BP.En.UAC;

/**
 * 学生科目对应
 */
public class StudentKeMu extends EntityMM
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1363636363L;
	
	/**
	 * 学生
	 */
	public final String getFK_Student()
	{
		return this.GetValStringByKey(StudentKeMuAttr.FK_Student);
	}
	
	public final void setFK_Student(String value)
	{
		SetValByKey(StudentKeMuAttr.FK_Student, value);
	}
	
	/**
	 * 科目名称
	 */
	public final String getFK_KeMuT()
	{
		return this.GetValRefTextByKey(StudentKeMuAttr.FK_KeMu);
	}
	
	/**
	 * 科目
	 */
	public final String getFK_KeMu()
	{
		return this.GetValStringByKey(StudentKeMuAttr.FK_KeMu);
	}
	
	public final void setFK_KeMu(String value)
	{
		SetValByKey(StudentKeMuAttr.FK_KeMu, value);
	}
	
	/**
	 * 构造函数
	 */
	public StudentKeMu()
	{
	}
	
	/**
	 * 工作学生科目对应
	 * 
	 * @param _empoid
	 *            学生
	 * @param fk_km
	 *            科目编号
	 */
	public StudentKeMu(String fk_student, String fk_km)
	{
		this.setFK_Student(fk_student);
		this.setFK_KeMu(fk_km);
		this.Retrieve();
	}
	
	/**
	 * 重写基类方法
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		
		Map map = new Map("Demo_StudentKeMu");
		map.setEnDesc("工作学生科目对应");
		map.setEnType(EnType.Dot2Dot);
		
		map.AddTBStringPK(StudentKeMuAttr.FK_Student, null, "学生", false, false,
				1, 20, 1);
		map.AddDDLEntitiesPK(StudentKeMuAttr.FK_KeMu, null, "科目",
				new BP.Demo.KeMus(), true);
		
		this.set_enMap(map);
		return this.get_enMap();
	}
	
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	
	/**
	 * 插入前所做的工作
	 * 
	 * @return true/false
	 */
	@Override
	protected boolean beforeInsert()
	{
		return super.beforeInsert();
	}
	
	/**
	 * 更新前所做的工作
	 * 
	 * @return true/false
	 */
	@Override
	protected boolean beforeUpdate()
	{
		return super.beforeUpdate();
	}
	
	/**
	 * 
	 * 
	 * 删除前所做的工作
	 */
	@Override
	protected boolean beforeDelete()
	{
		return super.beforeDelete();
	}
}
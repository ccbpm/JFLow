package BP.Demo;

import BP.En.EntitiesMM;
import BP.En.Entity;
import BP.En.QueryObject;

/**
 * 学生科目对应s -集合
 */
public class StudentKeMus extends EntitiesMM
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1143131313L;
	
	/**
	 * 构造函数
	 */
	public StudentKeMus()
	{
	}
	
	/**
	 * 构造函数
	 * 
	 * @param FK_Student
	 *            FK_Student
	 */
	public StudentKeMus(String FK_Student)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(StudentKeMuAttr.FK_Student, FK_Student);
		qo.DoQuery();
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new StudentKeMu();
	}
}
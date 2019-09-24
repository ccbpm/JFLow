package BP.GPM.AD;

import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import BP.GPM.*;
import java.util.*;

/** 
 部门
*/
public class Dept extends EntityTree
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 全名
	*/
	public final String getNameOfPath()
	{
		return this.GetValStrByKey(DeptAttr.NameOfPath);
	}
	public final void setNameOfPath(String value)
	{
		this.SetValByKey(DeptAttr.NameOfPath, value);
	}
	/** 
	 父节点的ID
	*/
	public final String getParentNo()
	{
		return this.GetValStrByKey(DeptAttr.ParentNo);
	}
	public final void setParentNo(String value)
	{
		this.SetValByKey(DeptAttr.ParentNo, value);
	}
	private Depts _HisSubDepts = null;
	/** 
	 它的子节点
	*/
	public final Depts getHisSubDepts()
	{
		if (_HisSubDepts == null)
		{
			_HisSubDepts = new Depts(this.No);
		}
		return _HisSubDepts;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 部门
	*/
	public Dept()
	{
	}
	/** 
	 部门
	 
	 @param no 编号
	*/
	public Dept(String no)
	{
		super(no);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重写方法
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	 Map
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map();
		map.EnDBUrl = new DBUrl(DBUrlType.AppCenterDSN); //连接到的那个数据库上. (默认的是: AppCenterDSN )
		map.PhysicsTable = "Port_Dept";
		map.Java_SetEnType(EnType.Admin);

		map.setEnDesc( "部门"; //  实体的描述.
		map.Java_SetDepositaryOfEntity(Depositary.None); //实体map的存放位置.
		map.Java_SetDepositaryOfMap(Depositary.Application); // Map 的存放位置.

		map.AddTBStringPK(DeptAttr.No, null, "编号", true, true, 1, 50, 20);

			//比如xx分公司财务部
		map.AddTBString(DeptAttr.Name, null, "名称", true, false, 0, 100, 30);

			//比如:\\驰骋集团\\南方分公司\\财务部
		map.AddTBString(DeptAttr.NameOfPath, null, "部门路径", true, true, 0, 300, 30, true);

		map.AddTBString(DeptAttr.ParentNo, null, "父节点编号", true, false, 0, 100, 30);

			//顺序号.
		map.AddTBInt(DeptAttr.Idx, 0, "顺序号", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

}
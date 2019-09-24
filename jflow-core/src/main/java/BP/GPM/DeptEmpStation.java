package BP.GPM;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;

import java.util.*;

/** 
 部门岗位人员对应 的摘要说明。
*/
public class DeptEmpStation extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 UI界面上的访问控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	 人员
	 * @throws Exception 
	*/
	public final String getFK_Emp() throws Exception
	{
		return this.GetValStringByKey(DeptEmpStationAttr.FK_Emp);
	}
	public final void setFK_Emp(String value) throws Exception
	{
		SetValByKey(DeptEmpStationAttr.FK_Emp, value);
		this.setMyPK(this.getFK_Dept() + "_" + this.getFK_Emp() + "_" + this.getFK_Station());
	}
	/** 
	 部门
	 * @throws Exception 
	*/
	public final String getFK_Dept() throws Exception
	{
		return this.GetValStringByKey(DeptEmpStationAttr.FK_Dept);
	}
	public final void setFK_Dept(String value) throws Exception
	{
		SetValByKey(DeptEmpStationAttr.FK_Dept, value);
		this.setMyPK(this.getFK_Dept() + "_" + this.getFK_Emp() + "_" + this.getFK_Station());
	}
	public final String getFK_StationT() throws Exception
	{
			//return this.GetValRefTextByKey(DeptEmpStationAttr.FK_Station);

		return this.GetValStringByKey(DeptEmpStationAttr.FK_Station);
	}
	/** 
	岗位
	 * @throws Exception 
	*/
	public final String getFK_Station() throws Exception
	{
		return this.GetValStringByKey(DeptEmpStationAttr.FK_Station);
	}
	public final void setFK_Station(String value) throws Exception
	{
		SetValByKey(DeptEmpStationAttr.FK_Station, value);
		this.setMyPK(this.getFK_Dept() + "_" + this.getFK_Emp() + "_" + this.getFK_Station());
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 工作部门岗位人员对应
	*/
	public DeptEmpStation()
	{
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

		Map map = new Map("Port_DeptEmpStation");
		map.setEnDesc("部门岗位人员对应");

		  //  map.AddMyPK();

		map.AddTBStringPK("MyPK", null, "主键MyPK", false, true, 1, 150, 10);


		map.AddTBString(DeptEmpStationAttr.FK_Dept, null, "部门", false, false, 1, 50, 1);
		map.AddTBString(DeptEmpStationAttr.FK_Station, null, "岗位", false, false, 1, 50, 1);
		map.AddTBString(DeptEmpStationAttr.FK_Emp, null, "操作员", false, false, 1, 50, 1);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 更新删除前做的事情
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		this.setMyPK(this.getFK_Dept() + "_" + this.getFK_Emp() + "_" + this.getFK_Station());
		return super.beforeUpdateInsertAction();
	}
}
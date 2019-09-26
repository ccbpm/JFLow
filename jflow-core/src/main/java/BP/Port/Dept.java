package BP.Port;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Web.*;
import BP.Sys.*;
import java.util.*;

/** 
 部门
*/
public class Dept extends EntityNoName
{

		///#region 属性
	/** 
	 父节点的ID
	 * @throws Exception 
	*/
	public final String getParentNo() throws Exception
	{
		return this.GetValStrByKey(DeptAttr.ParentNo);
	}
	public final void setParentNo(String value) throws Exception
	{
		this.SetValByKey(DeptAttr.ParentNo, value);
	}
	public final int getGrade()
	{
		return 1;
	}
	private Depts _HisSubDepts = null;
	/** 
	 它的子节点
	 * @throws Exception 
	*/
	public final Depts getHisSubDepts() throws Exception
	{
		if (_HisSubDepts == null)
		{
			_HisSubDepts = new Depts(this.getNo());
		}
		return _HisSubDepts;
	}

		///#endregion


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
	 * @throws Exception 
	*/
	public Dept(String no) throws Exception
	{
		super(no);
	}

		///#endregion


		///#region 重写方法
	@Override
	public UAC getHisUAC() throws Exception
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
		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN)); //连接到的那个数据库上. (默认的是: AppCenterDSN )
		map.setPhysicsTable("Port_Dept");
		map.Java_SetEnType(EnType.Admin);
		map.IsEnableVer = true;

		map.setEnDesc("部门"); //  实体的描述.
		map.Java_SetDepositaryOfEntity(Depositary.Application); //实体map的存放位置.
		map.Java_SetDepositaryOfMap(Depositary.Application); // Map 的存放位置.

		map.AddTBStringPK(DeptAttr.No, null, "编号", true, false, 1, 50, 20);
		map.AddTBString(DeptAttr.Name, null, "名称", true, false, 0, 100, 30);
		map.AddTBString(DeptAttr.ParentNo, null, "父节点编号", true, true, 0, 100, 30);



		RefMethod rm = new RefMethod();
		rm.Title = "历史变更";
		rm.ClassMethodName = this.toString() + ".History";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


			///#region 增加点对多属性
			//他的部门权限
		   // map.getAttrsOfOneVSM().Add(new DeptStations(), new Stations(), DeptStationAttr.FK_Dept, DeptStationAttr.FK_Station, StationAttr.Name, StationAttr.No, "岗位权限");

			///#endregion

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	public final String History() throws Exception
	{
		return "EnVerDtl.htm?EnName=" + this.toString() + "&PK=" + this.getNo();
	}


		///#region 重写查询. 2015.09.31 为适应ws的查询.
	/** 
	 查询
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	public int Retrieve() throws Exception
	{
		 
			return super.Retrieve();
		 
	}
	/** 
	 查询.
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	public int RetrieveFromDBSources() throws Exception
	{
		 
			return super.RetrieveFromDBSources();
	}

		///#endregion

}
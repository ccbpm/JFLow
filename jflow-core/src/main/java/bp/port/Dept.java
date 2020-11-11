package bp.port;

import bp.en.*;
import bp.en.Map;

/** 
 部门
*/
public class Dept extends EntityNoName
{
	private static final long serialVersionUID = 1L;
	///属性
	/** 
	 父节点的ID
	*/
	public final String getParentNo()throws Exception
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

		///


		///构造函数
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

		///


		///重写方法
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
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_Dept", "部门");
		map.IsEnableVer = true;

		map.AddTBStringPK(DeptAttr.No, null, "编号", true, false, 1, 50, 20);
		map.AddTBString(DeptAttr.Name, null, "名称", true, false, 0, 100, 30);
		map.AddTBString(DeptAttr.ParentNo, null, "父节点编号", true, true, 0, 100, 30);
		map.AddTBString(DeptAttr.OrgNo, null, "OrgNo", true, true, 0, 50, 30);
		map.AddTBString(DeptAttr.Leader, null, "部门领导", true, true, 0, 50, 30);

		map.AddTBInt(DeptAttr.Idx, 0, "序号", false, true);
		map.AddTBInt(DeptAttr.DeptType,1,"部门状态",false,true);
		map.AddHidden(DeptAttr.DeptType,"=","1");

		RefMethod rm = new RefMethod();
		rm.Title = "历史变更";
		rm.ClassMethodName = this.toString() + ".History";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


			///增加点对多属性
			//他的部门权限
		   // map.getAttrsOfOneVSM().Add(new DeptStations(), new Stations(), DeptStationAttr.FK_Dept, DeptStationAttr.FK_Station, StationAttr.Name, StationAttr.No, "岗位权限");

			///

		this.set_enMap(map);
		return this.get_enMap();
	}

		///

	public final String History()throws Exception
	{
		return "EnVerDtl.htm?EnName=" + this.toString() + "&PK=" + this.getNo();
	}


		///重写查询. 2015.09.31 为适应ws的查询.
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

		///

}
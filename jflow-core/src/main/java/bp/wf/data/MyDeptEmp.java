package bp.wf.data;
import bp.en.EnType;
import bp.en.EntityNoName;
import bp.en.Map;

/** 
 报表
*/
public class MyDeptEmp extends EntityNoName
{

		///attrs - attrs
	public String RptName = null;
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_Emp", "流程数据");
		map.setEnType(EnType.View);

		map.AddTBStringPK(MyDeptEmpAttr.No, null, "编号", false, false, 0, 100, 100);
		map.AddTBString(MyDeptEmpAttr.Name, null, "名称", false, false, 0, 100, 100);
		map.AddTBString(MyDeptEmpAttr.FK_Dept, null, "部门", false, false, 0, 100, 100);

		this.set_enMap(map);
		return this.get_enMap();
	}

		/// attrs

}
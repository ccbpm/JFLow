package bp.wf;

import bp.difference.SystemConfig;
import bp.en.*;
import bp.en.Map;
import bp.port.*;

/** 
 单据
*/
public class Bill extends EntityMyPK
{
	private static final long serialVersionUID = 1L;
		///基本属性
	/** 
	 路径
	*/
	public final String getFullPath()throws Exception
	{
		return this.GetValStringByKey(BillAttr.FullPath);
	}
	public final void setFullPath(String value)throws Exception
	{
		this.SetValByKey(BillAttr.FullPath, value);
	}
	/** 
	 参与人员
	*/
	public final String getEmps()throws Exception
	{
		return this.GetValStringByKey(BillAttr.Emps);
	}
	public final void setEmps(String value)throws Exception
	{
		this.SetValByKey(BillAttr.Emps, value);
	}
	/** 
	 发起日期
	*/
	public final String getStartDT()throws Exception
	{
		return this.GetValStringByKey(BillAttr.StartDT);
	}
	public final void setStartDT(String value)throws Exception
	{
		this.SetValByKey(BillAttr.StartDT, value);
	}
	/** 
	 单据类型
	*/
	public final String getFK_BillType()throws Exception
	{
		return this.GetValStringByKey(BillAttr.FK_BillType);
	}
	public final void setFK_BillType(String value)throws Exception
	{
		this.SetValByKey(BillAttr.FK_BillType, value);
	}
	/** 
	 单据类型名称
	*/
	public final String getFK_BillTypeT()throws Exception
	{
		return this.GetValStrByKey(BillAttr.FK_BillType);
	}
	/** 
	 流程标题
	*/
	public final String getTitle()throws Exception
	{
		return this.GetValStringByKey(BillAttr.Title);
	}
	public final void setTitle(String value)throws Exception
	{
		this.SetValByKey(BillAttr.Title, value);
	}
	/** 
	 流程编号
	*/
	public final String getFK_Flow()throws Exception
	{
		return this.GetValStringByKey(BillAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)throws Exception
	{
		this.SetValByKey(BillAttr.FK_Flow, value);
	}
	/** 
	 流程名称
	*/
	public final String getFK_FlowT()throws Exception
	{
		return this.GetValRefTextByKey(BillAttr.FK_Flow);
	}
	/** 
	 发起人名称
	*/
	public final String getFK_StarterT()throws Exception
	{
		return this.GetValRefTextByKey(BillAttr.FK_Starter);
	}
	/** 
	 发起人
	*/
	public final String getFK_Starter()throws Exception
	{
		return this.GetValStringByKey(BillAttr.FK_Starter);
	}
	public final void setFK_Starter(String value)throws Exception
	{
		this.SetValByKey(BillAttr.FK_Starter, value);
	}
	/** 
	 操作人员
	*/
	public final String getFK_Emp()throws Exception
	{
		return this.GetValStringByKey(BillAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)throws Exception
	{
		this.SetValByKey(BillAttr.FK_Emp, value);
	}
	/** 
	 操作人员名称
	*/
	public final String getFK_EmpT()throws Exception
	{
		return this.GetValRefTextByKey(BillAttr.FK_Emp);
	}
	/** 
	 单据名称
	*/
	public final String getFK_BillText()throws Exception
	{
		return this.GetValRefTextByKey(BillAttr.FK_Bill);
	}
	/** 
	 单据编号
	*/
	public final String getFK_Bill()throws Exception
	{
		return this.GetValStrByKey(BillAttr.FK_Bill);
	}
	public final void setFK_Bill(String value)throws Exception
	{
		this.SetValByKey(BillAttr.FK_Bill, value);
	}
	/** 
	 年月
	*/
	public final String getFK_NY()throws Exception
	{
		return this.GetValStrByKey(BillAttr.FK_NY);
	}
	public final void setFK_NY(String value)throws Exception
	{
		this.SetValByKey(BillAttr.FK_NY, value);
	}
	/** 
	 工作ID
	*/
	public final long getWorkID() throws Exception
	{
		return this.GetValInt64ByKey(BillAttr.WorkID);
	}
	public final void setWorkID(long value)  throws Exception
	{
		this.SetValByKey(BillAttr.WorkID, value);
	}
	/** 
	 流程ID
	*/
	public final long getFID() throws Exception
	{
		return this.GetValInt64ByKey(BillAttr.FID);
	}
	public final void setFID(long value) throws Exception
	{
		this.SetValByKey(BillAttr.FID, value);
	}
	/** 
	 节点ID
	*/
	public final int getFK_Node()throws Exception
	{
		return this.GetValIntByKey(BillAttr.FK_Node);
	}
	public final void setFK_Node(int value)throws Exception
	{
		this.SetValByKey(BillAttr.FK_Node, value);
	}
	/** 
	 节点名称
	*/
	public final String getFK_NodeT()throws Exception
	{
		Node nd = new Node(this.getFK_Node());
		return nd.getName();
			//return this.GetValRefTextByKey(BillAttr.FK_Node);
	}
	/** 
	 单据打印时间
	*/
	public final String getRDT() throws Exception
	{
		return this.GetValStringByKey(BillAttr.RDT);
	}
	public final void setRDT(String value)throws Exception
	{
		this.SetValByKey(BillAttr.RDT, value);
	}
	/** 
	 部门
	*/
	public final String getFK_Dept()throws Exception
	{
		return this.GetValStringByKey(BillAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)throws Exception
	{
		this.SetValByKey(BillAttr.FK_Dept, value);
	}
	/** 
	 部门名称
	*/
	public final String getFK_DeptT()throws Exception
	{
		return this.GetValRefTextByKey(BillAttr.FK_Dept);
	}
	/** 
	 超连接
	*/
	public final String getUrl()throws Exception
	{
		return this.GetValStringByKey(BillAttr.Url);
	}
	public final void setUrl(String value)throws Exception
	{
		this.SetValByKey(BillAttr.Url, value);
	}

		///


		///构造方法
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.IsDelete = false;
		uac.IsInsert = false;
		uac.IsUpdate = false;
		uac.IsView = true;
		return uac;
	}
	/** 
	 单据
	*/
	public Bill()
	{
	}
	/** 
	 
	 
	 @param pk
	*/
	public Bill(String pk)throws Exception
	{
		super(pk);
	}

		///


		///Map
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("WF_Bill", "单据");

		map.AddMyPK(false);

		map.AddTBInt(BillAttr.WorkID, 0, "工作ID", false, true);
		map.AddTBInt(BillAttr.FID, 0, "FID", false, true);
		map.AddTBString(BillAttr.FK_Flow, null, "流程", false, false, 0, 4, 5);
		map.AddTBString(BillAttr.FK_BillType, null, "单据类型", false, false, 0, 300, 5);
		map.AddTBString(BillAttr.Title, null, "标题", false, false, 0, 900, 5);
		map.AddTBString(BillAttr.FK_Starter, null, "发起人", true, true, 0, 50, 5);
		map.AddTBDateTime(BillAttr.StartDT, "发起时间", true, true);

		map.AddTBString(BillAttr.Url, null, "Url", false, false, 0, 2000, 5);
		map.AddTBString(BillAttr.FullPath, null, "FullPath", false, false, 0, 2000, 5);

		map.AddDDLEntities(BillAttr.FK_Emp, null, "打印人", new Emps(), false);
		map.AddTBDateTime(BillAttr.RDT, "打印时间", true, true);

		map.AddDDLEntities(BillAttr.FK_Dept, null, "隶属部门", new bp.port.Depts(), false);

		map.AddTBString(BillAttr.FK_NY, null, "隶属年月", true, true, 0, 50, 5);

		map.AddTBString(BillAttr.Emps, null, "Emps", false, false, 0, 4000, 5);

		map.AddTBString(BillAttr.FK_Node, null, "节点", false, false, 0, 30, 5);
		map.AddTBString(BillAttr.FK_Bill, null, "FK_Bill", false, false, 0, 500, 5);
		map.AddTBIntMyNum();

		map.AddSearchAttr(BillAttr.FK_Dept);
		map.AddSearchAttr(BillAttr.FK_Emp);

		RefMethod rm = new RefMethod();
		rm.Title = "打开";
		rm.ClassMethodName = this.toString() + ".DoOpen";
		rm.Icon = "../../WF/Img/FileType/doc.gif";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "打开";
		rm.ClassMethodName = this.toString() + ".DoOpenPDF";
		rm.Icon = "../../WF/Img/FileType/pdf.gif";
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///

	/** 
	 打开
	 
	 @return 
	*/
	public final String DoOpen()throws Exception
	{
		String path = SystemConfig.getPathOfWebApp() + (this.getUrl());
		return path;
	}
	/** 
	 打开pdf
	 
	 @return 
	*/
	public final String DoOpenPDF()throws Exception
	{
		String path = SystemConfig.getPathOfWebApp() + (this.getUrl());
		return path;
	}
}
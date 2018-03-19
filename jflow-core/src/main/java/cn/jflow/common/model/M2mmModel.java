package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Sys.M2M;
import BP.Sys.M2MAttr;
import BP.Sys.M2Ms;
import BP.Sys.MapM2M;
import BP.Tools.StringHelper;
import cn.jflow.system.ui.core.CheckBox;
/**
 * 多对多多业务数据
 * @author x_zp
 * 20150116
 */
public class M2mmModel extends BaseModel{
	
	public StringBuilder Pub1 = new StringBuilder();
	public StringBuilder Left = new StringBuilder();
	
	private boolean enabled = true;
	private boolean visible = true;
	
	private String OperObj = null;
	
	public M2mmModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	public String getVisible() {
		if(!visible)
			return "style=\"display: none;\"";
		return "";
	}
	
	public String getEnabled() {
		if(!enabled){
			return "disabled=\"disabled\"";
		}
		return "";
	}
	public final long getOID()
	{
		return Long.parseLong(get_request().getParameter("OID"));
	}
	public final String getIsOpen()
	{
		return get_request().getParameter("IsOpen");
	}
	public final String getIsEdit()
	{
		return get_request().getParameter("IsEdit");
	}
	public final String getFK_MapExt()
	{
		return get_request().getParameter("FK_MapExt");
	}
	
	private int GetNum(M2Ms m2ms, String obj)
	{
		for (M2M m2m : m2ms.ToJavaList())
		{
			if (obj.equals(m2m.getDtlObj()))
			{
				return m2m.getNumSelected();
			}
		}
		return 0;
	}
	public String getOperObj(){
		return OperObj;
	}
	/** 
	 操作对象
	 
	*/
	public final boolean BindLeft(MapM2M mapM2M)
	{
		M2Ms m2ms = new M2Ms();
		m2ms.Retrieve(M2MAttr.FK_MapData, this.getFK_MapData(), M2MAttr.M2MNo, this.getNoOfObj(), M2MAttr.EnOID, this.getOID());
		DataTable dtList;
		if (mapM2M.getDBOfLists().substring(0, 1).equals("@"))
		{
			dtList = new DataTable();
			dtList.Columns.Add("No", String.class);
			dtList.Columns.Add("Name", String.class);
			String myNo = mapM2M.getDBOfLists().replace("@", "");
			M2M m2m = new M2M();
			m2m.setMyPK(this.getFK_MapData() + "_" + myNo + "_" + this.getOID() + "_");
			if (m2m.RetrieveFromDBSources() == 0)
			{
				return false;
			}

			String[] vals = m2m.getValsName().split("(@)", -1);
			appendLeft(AddUL());
			for (String val : vals)
			{
				if (StringHelper.isNullOrEmpty(val))
				{
					continue;
				}
				String[] strs = val.split("(,)", -1);
				if (this.OperObj == null)
				{
					this.OperObj = strs[0];
				}

				if (this.OperObj.equals(strs[0]))
				{
					appendLeft(AddLi("<a href='M2MM.jsp?FK_MapData=" + this.getFK_MapData() + "&NoOfObj=" + this.getNoOfObj() + "&OID=" + this.getOID() + "&OperObj=" + strs[0] + "'><b>" + strs[1] + "(" + GetNum(m2ms, strs[0]) + ")</b></a><br>"));
				}
				else
				{
					appendLeft(AddLi("<a href='M2MM.jsp?FK_MapData=" + this.getFK_MapData() + "&NoOfObj=" + this.getNoOfObj() + "&OID=" + this.getOID() + "&OperObj=" + strs[0] + "'>" + strs[1] + "(" + GetNum(m2ms, strs[0]) + ")</a><br>"));
				}
			}
			appendLeft(AddULEnd());
			return true;
		}
		else
		{
			dtList = DBAccess.RunSQLReturnTable(mapM2M.getDBOfListsRun());
		}
		if (dtList.Rows.size() == 0)
		{
			return false;
		}

		appendLeft(AddUL());
		for (DataRow dr : dtList.Rows)
		{
			if (this.OperObj == null)
			{
				this.OperObj = dr.getValue(0).toString();
			}

			if (dr.getValue(0).toString().equals(this.OperObj))
			{
				appendLeft(AddLi("<a href='M2MM.jsp?FK_MapData=" + this.getFK_MapData() + "&NoOfObj=" + this.getNoOfObj() + "&OID=" + this.getOID() + "&OperObj=" + dr.getValue(0).toString() + "'><b>" + dr.getValue(1).toString() + "(" + GetNum(m2ms, dr.getValue(0).toString()) + ")</b></a><br>"));
			}
			else
			{
				appendLeft(AddLi("<a href='M2MM.jsp?FK_MapData=" + this.getFK_MapData() + "&NoOfObj=" + this.getNoOfObj() + "&OID=" + this.getOID() + "&OperObj=" + dr.getValue(0).toString() + "'>" + dr.getValue(1).toString() + "(" + GetNum(m2ms, dr.getValue(0).toString()) + ")</a><br>"));
			}
		}
		appendLeft(AddULEnd());
		return true;
	}
	public final void loadData()
	{
		MapM2M mapM2M = new MapM2M(this.getFK_MapData(), this.getNoOfObj());
		this.OperObj = get_request().getParameter("OperObj");

		if (StringHelper.isNullOrEmpty(mapM2M.getDBOfLists()) == true)
		{
			appendPub(AddFieldSetYellow("提示"));
			appendPub("表单设计错误:没有设置列表数据源.");
			appendPub(AddFieldSetEnd());
			enabled = false;
			return;
		}

		if (this.BindLeft(mapM2M) == false)
		{
			appendPub(AddFieldSetYellow("提示"));
			appendPub("列表数据源为空.");
			appendPub(AddFieldSetEnd());
			return;
		}

		M2M m2m = new M2M();
		m2m.setMyPK(this.getFK_MapData() + "_" + this.getNoOfObj() + "_" + this.getOID() + "_" + this.OperObj);
		m2m.RetrieveFromDBSources();
		DataTable dtGroup = new DataTable();
		if (mapM2M.getDBOfGroups().length() > 5)
		{
			dtGroup = BP.DA.DBAccess.RunSQLReturnTable(mapM2M.getDBOfGroupsRun());
		}
		else
		{
			dtGroup.Columns.Add("No", String.class);
			dtGroup.Columns.Add("Name", String.class);
			DataRow dr = dtGroup.NewRow();
			dr.setValue("No", "01");
			dr.setValue("Name", "全部选择");
			dtGroup.Rows.add(dr);
		}

		DataTable dtObj = BP.DA.DBAccess.RunSQLReturnTable(mapM2M.getDBOfObjsRun());
		if (dtObj.Columns.size() == 2)
		{
			dtObj.Columns.Add("Group", String.class);
			for (DataRow dr : dtObj.Rows)
			{
				dr.setValue("Group","01");
			}
		}
		boolean isInsert = mapM2M.getIsInsert();
		boolean isDelete = mapM2M.getIsDelete();

		if (isDelete == false && isInsert == false)
		{
			enabled = false;
		}

		if ((isDelete || isInsert) && StringHelper.isNullOrEmpty(this.getIsOpen()) == false)
		{
			visible = true;
		}

		appendPub("<Table style='border:none;' >");
		for (DataRow drGroup : dtGroup.Rows)
		{
			String ctlIDs = "";
			String groupNo = drGroup.getValue(0).toString();
			appendPub(AddTR());

			CheckBox cbx = new CheckBox();
			cbx.setId("CBs_" + drGroup.getValue(0).toString());
			cbx.setText(drGroup.getValue(1).toString());
			appendPub(AddTDTitle("align=left", cbx.toString()));
			appendPub(AddTREnd());

			appendPub(AddTR());
			appendPub(AddTDBegin("nowarp=false"));

			appendPub(AddTable());
			int colIdx = -1;
			for (DataRow drObj : dtObj.Rows)
			{
				String no = drObj.getValue(0).toString();
				String name = drObj.getValue(1).toString();
				String group = drObj.getValue(2).toString();
				if (!groupNo.equals(group))
				{
					continue;
				}

				colIdx++;
				if (colIdx == 0)
				{
					appendPub(AddTR());
				}

				CheckBox cb = new CheckBox();
				cb.setId("CB_" + no);
				ctlIDs += cb.getId() + ",";
				cb.attributes.put("onclick", "isChange=true;");
				cb.setText(name);
				cb.setChecked(m2m.getVals().contains("," + no + ","));
				if (cb.getChecked())
				{
					cb.setText("<font color=green>" + cb.getText() + "</font>");
				}

				appendPub(AddTD(cb));

				if (mapM2M.getCols() - 1 == colIdx)
				{
					appendPub(AddTREnd());
					colIdx = -1;
				}
			}
			cbx.attributes.put("onclick", "SetSelected(this,'" + ctlIDs + "')");
			if (colIdx != -1)
			{
				while (colIdx != mapM2M.getCols() - 1)
				{
					colIdx++;
					appendPub(AddTD());
				}
				appendPub(AddTREnd());
			}
			appendPub(AddTableEnd());
			appendPub(AddTDEnd());
			appendPub(AddTREnd());
		}

		// region 处理未分组的情况.
		boolean isHaveUnGroup = false;
		if (dtObj.Columns.size() > 2)
		{
			for (DataRow drObj : dtObj.Rows)
			{
				String group = drObj.getValue(2).toString();
				isHaveUnGroup = true;
				for (DataRow drGroup : dtGroup.Rows)
				{
					String groupNo = drGroup.getValue(0).toString();
					if (group.equals(groupNo))
					{
						isHaveUnGroup = false;
						break;
					}
				}
				if (isHaveUnGroup == false)
				{
					continue;
				}
			}
		}

		if (isHaveUnGroup == true)
		{
			appendPub(AddTR());
			appendPub(AddTDBigDocBegain()); // ("nowarp=true");


			appendPub(AddTable());
			int colIdx = -1;
			String ctlIDs = "";
			for (DataRow drObj : dtObj.Rows)
			{
				String group = drObj.getValue(2).toString();
				isHaveUnGroup = true;
				for (DataRow drGroup : dtGroup.Rows)
				{
					String groupNo = drGroup.getValue(0).toString();
					if (!group.equals(groupNo))
					{
						isHaveUnGroup = true;
						break;
					}
				}

				if (isHaveUnGroup == false)
				{
					continue;
				}

				String no = drObj.getValue(0).toString();
				String name = drObj.getValue(1).toString();

				colIdx++;
				if (colIdx == 0)
				{
					appendPub(AddTR());
				}

				CheckBox cb = new CheckBox();
				cb.setId("CB_" + no);
				ctlIDs += cb.getId() + ",";
				cb.setText(name);
				cb.setChecked(m2m.getVals().contains("," + no + ","));
				if (cb.getChecked())
				{
					cb.setText("<font color=green>" + cb.getText() + "</font>");
				}

				appendPub(AddTD(cb));

				if (mapM2M.getCols() - 1 == colIdx)
				{
					appendPub(AddTREnd());
					colIdx = -1;
				}
			}
			if (colIdx != -1)
			{
				while (colIdx != mapM2M.getCols() - 1)
				{
					colIdx++;
					appendPub(AddTD());
				}
				appendPub(AddTREnd());
			}
			appendPub(AddTableEnd());

			//cbx.Attributes("onclick") = "SetSelected(this,'" + ctlIDs + "')";
			appendPub(AddTDEnd());
			appendPub(AddTREnd());
		}
		// endregion 处理未分组的情况.

		appendPub(AddTableEnd());
	}

	
	private void appendPub(String str){
		Pub1.append(str);
	}
	
	private void appendLeft(String str){
		Left.append(str);
	}

}

package BP.Sys;

import BP.DA.*;
import BP.En.*;

/** 
 点对点
 
*/
public class MapM2M extends EntityMyPK
{

		
	/** 
	 显示方式
	 
	*/
	public final FrmShowWay getShowWay()
	{
		return FrmShowWay.forValue(this.GetValIntByKey(MapM2MAttr.ShowWay));
	}
	public final void setShowWay(FrmShowWay value)
	{
		this.SetValByKey(MapM2MAttr.ShowWay, value.getValue());
	}
	/** 
	 是否显示选择全部？
	 
	*/
	public final boolean getIsCheckAll()
	{
		return this.GetValBooleanByKey(MapM2MAttr.IsCheckAll);
	}
	public final void setIsCheckAll(boolean value)
	{
		this.SetValByKey(MapM2MAttr.IsCheckAll, value);
	}

	public final boolean getIsDelete()
	{
		return this.GetValBooleanByKey(MapM2MAttr.IsDelete);
	}
	public final void setIsDelete(boolean value)
	{
		this.SetValByKey(MapM2MAttr.IsDelete, value);
	}
	public final boolean getIsInsert()
	{
		return this.GetValBooleanByKey(MapM2MAttr.IsInsert);
	}
	public final void setIsInsert(boolean value)
	{
		this.SetValByKey(MapM2MAttr.IsInsert, value);
	}
	public final boolean getIsEdit()
	{
		if (this.getIsInsert() || this.getIsDelete())
		{
			return true;
		}
		return false;
	}
	/** 
	 列表(对一对多对多模式有效）
	 
	*/
	public final String getDBOfLists()
	{
		String sql = this.GetValStrByKey(MapM2MAttr.DBOfLists);
		sql = sql.replace("~", "'");
		return sql;
	}
	public final void setDBOfLists(String value)
	{
		this.SetValByKey(MapM2MAttr.DBOfLists, value);
	}
	/** 
	 列表(对一对多对多模式有效）
	 
	*/
	public final String getDBOfListsRun()
	{
		String sql = this.getDBOfLists();
		sql = sql.replace("~", "'");
		sql = sql.replace("WebUser.No", BP.Web.WebUser.getNo());
		sql = sql.replace("@WebUser.Name", BP.Web.WebUser.getName());
		sql = sql.replace("@WebUser.FK_DeptNameOfFull", BP.Web.WebUser.getFK_DeptNameOfFull());
		sql = sql.replace("@WebUser.FK_DeptName", BP.Web.WebUser.getFK_DeptName());
		sql = sql.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
		return sql;
	}
	public final String getDBOfObjs()
	{
		String sql = this.GetValStrByKey(MapM2MAttr.DBOfObjs);
		sql = sql.replace("~", "'");
		return sql;
	}
	public final void setDBOfObjs(String value)
	{
		this.SetValByKey(MapM2MAttr.DBOfObjs, value);
	}
	public final String getDBOfGroups()
	{
		String sql = this.GetValStrByKey(MapM2MAttr.DBOfGroups);
		sql = sql.replace("~", "'");
		return sql;
	}
	public final void setDBOfGroups(String value)
	{
		this.SetValByKey(MapM2MAttr.DBOfGroups, value);
	}
	public final String getDBOfObjsRun()
	{
		String sql = this.GetValStrByKey(MapM2MAttr.DBOfObjs);
		sql = sql.replace("~", "'");
		sql = sql.replace("WebUser.No", BP.Web.WebUser.getNo());
		sql = sql.replace("@WebUser.Name", BP.Web.WebUser.getName());
		sql = sql.replace("@WebUser.FK_DeptNameOfFull", BP.Web.WebUser.getFK_DeptNameOfFull());
		sql = sql.replace("@WebUser.FK_DeptName", BP.Web.WebUser.getFK_DeptName());
		sql = sql.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
		return sql;
	}
	public final void setDBOfObjsRun(String value)
	{
		this.SetValByKey(MapM2MAttr.DBOfObjs, value);
	}
	public final String getDBOfGroupsRun()
	{
		String sql = this.GetValStrByKey(MapM2MAttr.DBOfGroups);
		sql = sql.replace("~", "'");
		sql = sql.replace("WebUser.No", BP.Web.WebUser.getNo());
		sql = sql.replace("@WebUser.Name", BP.Web.WebUser.getName());
		sql = sql.replace("@WebUser.FK_DeptNameOfFull", BP.Web.WebUser.getFK_DeptNameOfFull());
		sql = sql.replace("@WebUser.FK_DeptName", BP.Web.WebUser.getFK_DeptName());
		sql = sql.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
		return sql;
	}
	public final void setDBOfGroupsRun(String value)
	{
		this.SetValByKey(MapM2MAttr.DBOfGroups, value);
	}
	/** 
	 内部编号
	 
	*/
	public final String getNoOfObj()
	{
		return this.GetValStrByKey(MapM2MAttr.NoOfObj);
	}
	public final void setNoOfObj(String value)
	{
		this.SetValByKey(MapM2MAttr.NoOfObj, value);
	}
	/** 
	 名称
	 
	*/
	public final String getName()
	{
		return this.GetValStrByKey(MapM2MAttr.Name);
	}
	public final void setName(String value)
	{
		this.SetValByKey(MapM2MAttr.Name, value);
	}
	public boolean IsUse = false;
	public final String getFK_MapData()
	{
		return this.GetValStrByKey(MapM2MAttr.FK_MapData);
	}
	public final void setFK_MapData(String value)
	{
		this.SetValByKey(MapM2MAttr.FK_MapData, value);
	}
	public final int getRowIdx()
	{
		return this.GetValIntByKey(MapM2MAttr.RowIdx);
	}
	public final void setRowIdx(int value)
	{
		this.SetValByKey(MapM2MAttr.RowIdx, value);
	}
	public final int getCols()
	{
		return this.GetValIntByKey(MapM2MAttr.Cols);
	}
	public final void setCols(int value)
	{
		this.SetValByKey(MapM2MAttr.Cols, value);
	}
	public final M2MType getHisM2MType()
	{
		return M2MType.forValue(this.GetValIntByKey(MapM2MAttr.M2MType));
	}
	public final void setHisM2MType(M2MType value)
	{
		this.SetValByKey(MapM2MAttr.M2MType, value.getValue());
	}
	public final int getGroupID()
	{
		return this.GetValIntByKey(MapM2MAttr.GroupID);
	}
	public final void setGroupID(int value)
	{
		this.SetValByKey(MapM2MAttr.GroupID, value);
	}
	public final float getH()
	{
		return this.GetValFloatByKey(MapM2MAttr.H);
	}
	public final void setH(float value)
	{
		this.SetValByKey(MapM2MAttr.H, value);
	}
	public final float getW()
	{
		return this.GetValFloatByKey(MapM2MAttr.W);
	}
	public final void setW(float value)
	{
		this.SetValByKey(MapM2MAttr.W, value);
	}
	public final float getX()
	{
		return this.GetValFloatByKey(MapM2MAttr.X);
	}
	public final void setX(float value)
	{
		this.SetValByKey(MapM2MAttr.X, value);
	}
	public final float getY()
	{
		return this.GetValFloatByKey(MapM2MAttr.Y);
	}
	public final void setY(float value)
	{
		this.SetValByKey(MapM2MAttr.Y, value);
	}
	/** 
	 扩展属性
	 
	*/
	public final int getFK_Node()
	{
		return Integer.parseInt(this.getFK_MapData().replace("ND", ""));
	}

		///#endregion


		
	/** 
	 点对点
	 
	*/
	public MapM2M()
	{
	}
	public MapM2M(String myPK)
	{
		this.setMyPK(myPK);
		this.Retrieve();
	}
	/** 
	 点对点
	 
	 @param fk_mapdata
	 @param noOfObj
	*/
	public MapM2M(String fk_mapdata, String noOfObj)
	{
		this.setFK_MapData(fk_mapdata);
		this.setNoOfObj(noOfObj);
		this.setMyPK(this.getFK_MapData() + "_" + this.getNoOfObj());
		this.RetrieveFromDBSources();
	}
	/** 
	 EnMap
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_MapM2M", "多选");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddMyPK();
		map.AddTBString(MapM2MAttr.FK_MapData, null, "主表", true, false, 1, 100, 20);
		map.AddTBString(MapM2MAttr.NoOfObj, null, "编号", true, false, 1, 20, 20);

		map.AddTBString(MapM2MAttr.Name, null, "名称", true, false, 1, 200, 20);

		map.AddTBString(MapM2MAttr.DBOfLists, null, "列表数据源(对一对多对多模式有效）", true, false, 0, 4000, 20);

		map.AddTBString(MapM2MAttr.DBOfObjs, null, "DBOfObjs", true, false, 0, 4000, 20);
		map.AddTBString(MapM2MAttr.DBOfGroups, null, "DBOfGroups", true, false, 0, 4000, 20);

		map.AddTBFloat(MapM2MAttr.H, 100, "H", false, false);
		map.AddTBFloat(MapM2MAttr.W, 160, "W", false, false);
		map.AddTBFloat(FrmImgAttr.X, 5, "X", true, false);
		map.AddTBFloat(FrmImgAttr.Y, 5, "Y", false, false);

		map.AddTBInt(MapM2MAttr.ShowWay, FrmShowWay.FrmAutoSize.getValue(), "显示方式", false, false);

		map.AddTBInt(MapM2MAttr.M2MType, M2MType.M2M.getValue(), "类型", false, false);

		map.AddTBInt(MapM2MAttr.RowIdx, 99, "位置", false, false);
		map.AddTBInt(MapM2MAttr.GroupID, 0, "分组ID", false, false);

		map.AddTBInt(MapM2MAttr.Cols, 4, "记录呈现列数", false, false);

		map.AddBoolean(MapM2MAttr.IsDelete, true, "可删除否", false, false);
		map.AddBoolean(MapM2MAttr.IsInsert, true, "可插入否", false, false);

		map.AddBoolean(MapM2MAttr.IsCheckAll, true, "是否显示选择全部", false, false);


			//map.AddTBFloat(FrmImgAttr.H, 200, "H", true, false);
			//map.AddTBFloat(FrmImgAttr.W, 500, "W", false, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
	@Override
	protected boolean beforeInsert()
	{
		if (this.getDBOfObjs().trim().length() <= 5)
		{
			this.setDBOfGroups("SELECT No,Name FROM Port_Dept");
			this.setDBOfObjs("SELECT No,Name,FK_Dept FROM Port_Emp");
		}

		return super.beforeInsert();
	}
	@Override
	protected boolean beforeUpdateInsertAction()
	{
		this.setMyPK(this.getFK_MapData() + "_" + this.getNoOfObj());
		return super.beforeUpdateInsertAction();
	}

		///#endregion
}
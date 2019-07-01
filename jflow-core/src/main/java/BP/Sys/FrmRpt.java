package BP.Sys;

import BP.DA.*;
import BP.En.*;

/** 
 纬度报表
 
*/
public class FrmRpt extends EntityNoName
{

	///#region 外键属性
	/** 
	 框架
	 * @throws Exception 
	 
	*/
	public final MapFrames getMapFrames() throws Exception
	{
		Object tempVar = this.GetRefObject("MapFrames");
		MapFrames obj = (MapFrames)((tempVar instanceof MapFrames) ? tempVar : null);
		if (obj == null)
		{
			obj = new MapFrames(this.getNo());
			this.SetRefObject("MapFrames", obj);
		}
		return obj;
	}
	 
	/** 
	 逻辑扩展
	 * @throws Exception 
	 
	*/
	public final MapExts getMapExts() throws Exception
	{
		Object tempVar = this.GetRefObject("MapExts");
		MapExts obj = (MapExts)((tempVar instanceof MapExts) ? tempVar : null);
		if (obj == null)
		{
			obj = new MapExts(this.getNo());
			this.SetRefObject("MapExts", obj);
		}
		return obj;
	}
	/** 
	 事件
	 * @throws Exception 
	 
	*/
	public final FrmEvents getFrmEvents() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmEvents");
		FrmEvents obj = (FrmEvents)((tempVar instanceof FrmEvents) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmEvents(this.getNo());
			this.SetRefObject("FrmEvents", obj);
		}
		return obj;
	}
	 
	/** 
	 从表
	 * @throws Exception 
	 
	*/
	public final FrmRpts getFrmRpts() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmRpts");
		FrmRpts obj = (FrmRpts)((tempVar instanceof FrmRpts) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmRpts(this.getNo());
			this.SetRefObject("FrmRpts", obj);
		}
		return obj;
	}
	/** 
	 超连接
	 * @throws Exception 
	 
	*/
	public final FrmLinks getFrmLinks() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmLinks");
		FrmLinks obj = (FrmLinks)((tempVar instanceof FrmLinks) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmLinks(this.getNo());
			this.SetRefObject("FrmLinks", obj);
		}
		return obj;
	}
	/** 
	 按钮
	 * @throws Exception 
	 
	*/
	public final FrmBtns getFrmBtns() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmLinks");
		FrmBtns obj = (FrmBtns)((tempVar instanceof FrmBtns) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmBtns(this.getNo());
			this.SetRefObject("FrmBtns", obj);
		}
		return obj;
	}
	/** 
	 元素
	 * @throws Exception 
	 
	*/
	public final FrmEles getFrmEles() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmEles");
		FrmEles obj = (FrmEles)((tempVar instanceof FrmEles) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmEles(this.getNo());
			this.SetRefObject("FrmEles", obj);
		}
		return obj;
	}
	/** 
	 线
	 * @throws Exception 
	 
	*/
	public final FrmLines getFrmLines() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmLines");
		FrmLines obj = (FrmLines)((tempVar instanceof FrmLines) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmLines(this.getNo());
			this.SetRefObject("FrmLines", obj);
		}
		return obj;
	}
	/** 
	 标签
	 * @throws Exception 
	 
	*/
	public final FrmLabs getFrmLabs() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmLabs");
		FrmLabs obj = (FrmLabs)((tempVar instanceof FrmLabs) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmLabs(this.getNo());
			this.SetRefObject("FrmLabs", obj);
		}
		return obj;
	}
	/** 
	 图片
	 * @throws Exception 
	 
	*/
	public final FrmImgs getFrmImgs() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmLabs");
		FrmImgs obj = (FrmImgs)((tempVar instanceof FrmImgs) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmImgs(this.getNo());
			this.SetRefObject("FrmLabs", obj);
		}
		return obj;
	}
	/** 
	 附件
	 * @throws Exception 
	 
	*/
	public final FrmAttachments getFrmAttachments() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmAttachments");
		FrmAttachments obj = (FrmAttachments)((tempVar instanceof FrmAttachments) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmAttachments(this.getNo());
			this.SetRefObject("FrmAttachments", obj);
		}
		return obj;
	}
	/** 
	 图片附件
	 * @throws Exception 
	 
	*/
	public final FrmImgAths getFrmImgAths() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmImgAths");
		FrmImgAths obj = (FrmImgAths)((tempVar instanceof FrmImgAths) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmImgAths(this.getNo());
			this.SetRefObject("FrmImgAths", obj);
		}
		return obj;
	}
	/** 
	 单选按钮
	 * @throws Exception 
	 
	*/
	public final FrmRBs getFrmRBs() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmRBs");
		FrmRBs obj = (FrmRBs)((tempVar instanceof FrmRBs) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmRBs(this.getNo());
			this.SetRefObject("FrmRBs", obj);
		}
		return obj;
	}
	/** 
	 属性
	 * @throws Exception 
	 
	*/
	public final MapAttrs getMapAttrs() throws Exception
	{
		Object tempVar = this.GetRefObject("MapAttrs");
		MapAttrs obj = (MapAttrs)((tempVar instanceof MapAttrs) ? tempVar : null);
		if (obj == null)
		{
			obj = new MapAttrs(this.getNo());
			this.SetRefObject("MapAttrs", obj);
		}
		return obj;
	}

		///#endregion


		
	public final String getSQLOfRow()
	{
		return this.GetValStringByKey(FrmRptAttr.SQLOfRow);
	}
	public final void setSQLOfRow(String value)
	{
		this.SetValByKey(FrmRptAttr.SQLOfRow, value);
	}
	public final String getSQLOfColumn()
	{
		return this.GetValStringByKey(FrmRptAttr.SQLOfColumn);
	}
	public final void setSQLOfColumn(String value)
	{
		this.SetValByKey(FrmRptAttr.SQLOfColumn, value);
	}
	public GEDtls HisGEDtls_temp = null;
	public final DtlShowModel getHisDtlShowModel()
	{
		return DtlShowModel.forValue(this.GetValIntByKey(FrmRptAttr.DtlShowModel));
	}
	public final void setHisDtlShowModel(DtlShowModel value)
	{
		this.SetValByKey(FrmRptAttr.DtlShowModel, value.getValue());
	}
	/** 
	 
	 
	*/
	public final WhenOverSize getHisWhenOverSize()
	{
		return WhenOverSize.forValue(this.GetValIntByKey(FrmRptAttr.WhenOverSize));
	}
	public final void setHisWhenOverSize(WhenOverSize value)
	{
		this.SetValByKey(FrmRptAttr.WhenOverSize, value.getValue());
	}
	public final boolean getIsExp()
	{
		return this.GetValBooleanByKey(FrmRptAttr.IsExp);
	}
	public final void setIsExp(boolean value)
	{
		this.SetValByKey(FrmRptAttr.IsExp, value);
	}
	public final boolean getIsImp()
	{
		return this.GetValBooleanByKey(FrmRptAttr.IsImp);
	}
	public final void setIsImp(boolean value)
	{
		this.SetValByKey(FrmRptAttr.IsImp, value);
	}
	public final boolean getIsShowSum()
	{
		return this.GetValBooleanByKey(FrmRptAttr.IsShowSum);
	}
	public final void setIsShowSum(boolean value)
	{
		this.SetValByKey(FrmRptAttr.IsShowSum, value);
	}
	public final boolean getIsShowIdx()
	{
		return this.GetValBooleanByKey(FrmRptAttr.IsShowIdx);
	}
	public final void setIsShowIdx(boolean value)
	{
		this.SetValByKey(FrmRptAttr.IsShowIdx, value);
	}
	public final boolean getIsReadonly_del()
	{
		return this.GetValBooleanByKey(FrmRptAttr.IsReadonly);
	}
	public final void setIsReadonly_del(boolean value)
	{
		this.SetValByKey(FrmRptAttr.IsReadonly, value);
	}
	public final boolean getIsShowTitle()
	{
		return this.GetValBooleanByKey(FrmRptAttr.IsShowTitle);
	}
	public final void setIsShowTitle(boolean value)
	{
		this.SetValByKey(FrmRptAttr.IsShowTitle, value);
	}
	/** 
	 是否是合流汇总数据
	 
	*/
	public final boolean getIsHLDtl()
	{
		return this.GetValBooleanByKey(FrmRptAttr.IsHLDtl);
	}
	public final void setIsHLDtl(boolean value)
	{
		this.SetValByKey(FrmRptAttr.IsHLDtl, value);
	}
	public int _IsReadonly = 2;
	public final boolean getIsReadonly()
	{
		if (_IsReadonly != 2)
		{
			if (_IsReadonly == 1)
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		if (this.getIsDelete() || this.getIsInsert() || this.getIsUpdate())
		{
			_IsReadonly = 0;
			return false;
		}
		_IsReadonly = 1;
		return true;
	}
	public final boolean getIsDelete()
	{
		return this.GetValBooleanByKey(FrmRptAttr.IsDelete);
	}
	public final void setIsDelete(boolean value)
	{
		this.SetValByKey(FrmRptAttr.IsDelete, value);
	}
	public final boolean getIsInsert()
	{
		return this.GetValBooleanByKey(FrmRptAttr.IsInsert);
	}
	public final void setIsInsert(boolean value)
	{
		this.SetValByKey(FrmRptAttr.IsInsert, value);
	}
	/** 
	 是否可见
	 
	*/
	public final boolean getIsView()
	{
		return this.GetValBooleanByKey(FrmRptAttr.IsView);
	}
	public final void setIsView(boolean value)
	{
		this.SetValByKey(FrmRptAttr.IsView, value);
	}
	public final boolean getIsUpdate()
	{
		return this.GetValBooleanByKey(FrmRptAttr.IsUpdate);
	}
	public final void setIsUpdate(boolean value)
	{
		this.SetValByKey(FrmRptAttr.IsUpdate, value);
	}
	/** 
	 是否启用多附件
	 
	*/
	public final boolean getIsEnableAthM()
	{
		return this.GetValBooleanByKey(FrmRptAttr.IsEnableAthM);
	}
	public final void setIsEnableAthM(boolean value)
	{
		this.SetValByKey(FrmRptAttr.IsEnableAthM, value);
	}
	/** 
	 是否启用分组字段
	 
	*/
	public final boolean getIsEnableGroupField()
	{
		return this.GetValBooleanByKey(FrmRptAttr.IsEnableGroupField);
	}
	public final void setIsEnableGroupField(boolean value)
	{
		this.SetValByKey(FrmRptAttr.IsEnableGroupField, value);
	}
	/** 
	 是否起用审核连接
	 
	*/
	public final boolean getIsEnablePass()
	{
		return this.GetValBooleanByKey(FrmRptAttr.IsEnablePass);
	}
	public final void setIsEnablePass(boolean value)
	{
		this.SetValByKey(FrmRptAttr.IsEnablePass, value);
	}
	public final boolean getIsCopyNDData()
	{
		return this.GetValBooleanByKey(FrmRptAttr.IsCopyNDData);
	}
	public final void setIsCopyNDData(boolean value)
	{
		this.SetValByKey(FrmRptAttr.IsCopyNDData, value);
	}
	/** 
	 是否启用一对多
	 
	*/
	public final boolean getIsEnableM2M()
	{
		return this.GetValBooleanByKey(FrmRptAttr.IsEnableM2M);
	}
	public final void setIsEnableM2M(boolean value)
	{
		this.SetValByKey(FrmRptAttr.IsEnableM2M, value);
	}
	/** 
	 是否启用一对多多
	 
	*/
	public final boolean getIsEnableM2MM()
	{
		return this.GetValBooleanByKey(FrmRptAttr.IsEnableM2MM);
	}
	public final void setIsEnableM2MM(boolean value)
	{
		this.SetValByKey(FrmRptAttr.IsEnableM2MM, value);
	}

	public boolean IsUse = false;
	/** 
	 是否检查人员的权限
	 
	*/
	public final DtlOpenType getDtlOpenType()
	{
		return DtlOpenType.forValue(this.GetValIntByKey(FrmRptAttr.DtlOpenType));
	}
	public final void setDtlOpenType(DtlOpenType value)
	{
		this.SetValByKey(FrmRptAttr.DtlOpenType, value.getValue());
	}
	/** 
	 分组字段
	 
	*/
	public final String getGroupField()
	{
		return this.GetValStrByKey(FrmRptAttr.GroupField);
	}
	public final void setGroupField(String value)
	{
		this.SetValByKey(FrmRptAttr.GroupField, value);
	}
	public final String getFK_MapData()
	{
		return this.GetValStrByKey(FrmRptAttr.FK_MapData);
	}
	public final void setFK_MapData(String value)
	{
		this.SetValByKey(FrmRptAttr.FK_MapData, value);
	}
	public final int getRowsOfList()
	{
		return this.GetValIntByKey(FrmRptAttr.RowsOfList);
	}
	public final void setRowsOfList(int value)
	{
		this.SetValByKey(FrmRptAttr.RowsOfList, value);
	}
	public final int getRowIdx()
	{
		return this.GetValIntByKey(FrmRptAttr.RowIdx);
	}
	public final void setRowIdx(int value)
	{
		this.SetValByKey(FrmRptAttr.RowIdx, value);
	}
	public final int getGroupID()
	{
		return this.GetValIntByKey(FrmRptAttr.GroupID);
	}
	public final void setGroupID(int value)
	{
		this.SetValByKey(FrmRptAttr.GroupID, value);
	}
	public final String getPTable()
	{
		String s = this.GetValStrByKey(FrmRptAttr.PTable);
		if (s.equals("") || s == null)
		{
			s = this.getNo();
			if (s.substring(0, 1).equals("0"))
			{
				return "T" + this.getNo();
			}
			else
			{
				return s;
			}
		}
		else
		{
			if (s.substring(0, 1).equals("0"))
			{
				return "T" + this.getNo();
			}
			else
			{
				return s;
			}
		}
	}
	public final void setPTable(String value)
	{
		this.SetValByKey(FrmRptAttr.PTable, value);
	}
	/** 
	 多表头
	 
	*/
	public final String getMTR()
	{
		String s= this.GetValStrByKey(FrmRptAttr.MTR);
		s = s.replace("《","<");
		s = s.replace("》",">");
		s = s.replace("‘","'");
		return s;
	}
	public final void setMTR(String value)
	{
		String s = value;
		s = s.replace("<","《");
		s = s.replace(">", "》");
		s = s.replace("'", "‘");
		this.SetValByKey(FrmRptAttr.MTR, value);
	}

		///#endregion


		
	public final Map GenerMap() throws Exception
	{
		boolean isdebug = SystemConfig.getIsDebug();

		if (isdebug == false)
		{
			Map m = BP.DA.Cash.GetMap(this.getNo());
			if (m != null)
			{
				return m;
			}
		}

		MapAttrs mapAttrs = this.getMapAttrs();
		Map map = new Map(this.getPTable(), this.getName());
		map.Java_SetEnType(EnType.App);
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		Attrs attrs = new Attrs();
		for (MapAttr mapAttr : MapAttrs.convertMapAttrs(mapAttrs))
		{
			map.AddAttr(mapAttr.getHisAttr());
		}

		BP.DA.Cash.SetMap(this.getNo(), map);
		return map;
	}
	public final GEDtl getHisGEDtl()
	{
		GEDtl dtl = new GEDtl(this.getNo());
		return dtl;
	}
	public final GEEntity GenerGEMainEntity(String mainPK) throws Exception
	{
		GEEntity en = new GEEntity(this.getFK_MapData(), mainPK);
		return en;
	}
	/** 
	 纬度报表
	 
	*/
	public FrmRpt()
	{
	}
	public FrmRpt(String mypk) throws Exception
	{
		this.setNo(mypk);
		this._IsReadonly = 2;
		this.Retrieve();
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
		Map map = new Map("Sys_FrmRpt", "纬度报表");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddTBStringPK(FrmRptAttr.No, null, "编号", true, false, 1, 20, 20);
		map.AddTBString(FrmRptAttr.Name, null, "描述", true, false, 1, 50, 20);
		map.AddTBString(FrmRptAttr.FK_MapData, null, "主表", true, false, 0, 100, 20);
		map.AddTBString(FrmRptAttr.PTable, null, "物理表", true, false, 0, 30, 20);

		map.AddTBString(FrmRptAttr.SQLOfColumn, null, "列的数据源", true, false, 0, 300, 20);
		map.AddTBString(FrmRptAttr.SQLOfRow, null, "行数据源", true, false, 0, 300, 20);

		map.AddTBInt(FrmRptAttr.RowIdx, 99, "位置", false, false);
		map.AddTBInt(FrmRptAttr.GroupID, 0, "GroupID", false, false);

		map.AddBoolean(FrmRptAttr.IsShowSum, true, "IsShowSum", false, false);
		map.AddBoolean(FrmRptAttr.IsShowIdx, true, "IsShowIdx", false, false);
		map.AddBoolean(FrmRptAttr.IsCopyNDData, true, "IsCopyNDData", false, false);
		map.AddBoolean(FrmRptAttr.IsHLDtl, false, "是否是合流汇总", false, false);

		map.AddBoolean(FrmRptAttr.IsReadonly, false, "IsReadonly", false, false);
		map.AddBoolean(FrmRptAttr.IsShowTitle, true, "IsShowTitle", false, false);
		map.AddBoolean(FrmRptAttr.IsView, true, "是否可见", false, false);

		map.AddBoolean(FrmRptAttr.IsExp, true, "IsExp", false, false);
		map.AddBoolean(FrmRptAttr.IsImp, true, "IsImp", false, false);

		map.AddBoolean(FrmRptAttr.IsInsert, true, "IsInsert", false, false);
		map.AddBoolean(FrmRptAttr.IsDelete, true, "IsDelete", false, false);
		map.AddBoolean(FrmRptAttr.IsUpdate, true, "IsUpdate", false, false);

		map.AddBoolean(FrmRptAttr.IsEnablePass, false, "是否启用通过审核功能?", false, false);
		map.AddBoolean(FrmRptAttr.IsEnableAthM, false, "是否启用多附件", false, false);

		map.AddBoolean(FrmRptAttr.IsEnableM2M, false, "是否启用M2M", false, false);
		map.AddBoolean(FrmRptAttr.IsEnableM2MM, false, "是否启用M2M", false, false);

		map.AddDDLSysEnum(FrmRptAttr.WhenOverSize, 0, "WhenOverSize", true, true, FrmRptAttr.WhenOverSize, "@0=不处理@1=向下顺增行@2=次页显示");

		map.AddDDLSysEnum(FrmRptAttr.DtlOpenType, 1, "数据开放类型", true, true, FrmRptAttr.DtlOpenType, "@0=操作员@1=工作ID@2=流程ID");

		map.AddDDLSysEnum(FrmRptAttr.DtlShowModel, 0, "显示格式", true, true, FrmRptAttr.DtlShowModel, "@0=表格@1=卡片");

		map.AddTBFloat(FrmRptAttr.X, 5, "X", true, false);
		map.AddTBFloat(FrmRptAttr.Y, 5, "Y", false, false);

		map.AddTBFloat(FrmRptAttr.H, 150, "H", true, false);
		map.AddTBFloat(FrmRptAttr.W, 200, "W", false, false);

		map.AddTBFloat(FrmRptAttr.FrmW, 900, "FrmW", true, true);
		map.AddTBFloat(FrmRptAttr.FrmH, 1200, "FrmH", true, true);

			//MTR 多表头列.
		map.AddTBString(FrmRptAttr.MTR, null, "多表头列", true, false, 0, 3000, 20);
		map.AddTBString(FrmBtnAttr.GUID, null, "GUID", true, false, 0, 128, 20);


		this.set_enMap(map);
		return this.get_enMap();
	}
	public final float getX()
	{
		return this.GetValFloatByKey(FrmImgAttr.X);
	}
	public final float getY()
	{
		return this.GetValFloatByKey(FrmImgAttr.Y);
	}
	public final float getW()
	{
		return this.GetValFloatByKey(FrmImgAttr.W);
	}
	public final float getH()
	{
		return this.GetValFloatByKey(FrmImgAttr.H);
	}
	public final float getFrmW()
	{
		return this.GetValFloatByKey(FrmRptAttr.FrmW);
	}
	public final float getFrmH()
	{
		return this.GetValFloatByKey(FrmRptAttr.FrmH);
	}
	/** 
	 获取个数
	 
	 @param fk_val
	 @return 
	*/
	public final int GetCountByFK(int workID)
	{
		return BP.DA.DBAccess.RunSQLReturnValInt("select COUNT(OID) from " + this.getPTable() + " WHERE WorkID=" + workID);
	}

	public final int GetCountByFK(String field, String val)
	{
		return BP.DA.DBAccess.RunSQLReturnValInt("select COUNT(OID) from " + this.getPTable() + " WHERE " + field + "='" + val + "'");
	}
	public final int GetCountByFK(String field, long val)
	{
		return BP.DA.DBAccess.RunSQLReturnValInt("select COUNT(OID) from " + this.getPTable() + " WHERE " + field + "=" + val);
	}
	public final int GetCountByFK(String f1, long val1, String f2, String val2)
	{
		return BP.DA.DBAccess.RunSQLReturnValInt("SELECT COUNT(OID) from " + this.getPTable() + " WHERE " + f1 + "=" + val1 + " AND " + f2 + "='" + val2 + "'");
	}

		///#endregion

	public final void IntMapAttrs() throws Exception
	{
		BP.Sys.MapData md = new BP.Sys.MapData();
		md.setNo(this.getNo());
		if (md.RetrieveFromDBSources() == 0)
		{
			md.setName(this.getName());
			md.Insert();
		}

		MapAttrs attrs = new MapAttrs(this.getNo());
		BP.Sys.MapAttr attr = new BP.Sys.MapAttr();
		if (attrs.Contains(MapAttrAttr.KeyOfEn, "OID") == false)
		{
			attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setHisEditType(EditType.Readonly);

			attr.setKeyOfEn("OID");
			attr.setName("主键");
			attr.setMyDataType(BP.DA.DataType.AppInt);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setDefVal("0");
			attr.Insert();
		}

		if (attrs.Contains(MapAttrAttr.KeyOfEn, "RefPK") == false)
		{
			attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setHisEditType(EditType.Readonly);

			attr.setKeyOfEn("RefPK");
			attr.setName("关联ID");
			attr.setMyDataType(BP.DA.DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setDefVal("0");
			attr.Insert();
		}


		if (attrs.Contains(MapAttrAttr.KeyOfEn, "FID") == false)
		{
			attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setHisEditType(EditType.Readonly);

			attr.setKeyOfEn("FID");
			attr.setName("FID");
			attr.setMyDataType(BP.DA.DataType.AppInt);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setDefVal("0");
			attr.Insert();
		}

		if (attrs.Contains(MapAttrAttr.KeyOfEn, "RDT") == false)
		{
			attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setHisEditType(EditType.UnDel);

			attr.setKeyOfEn("RDT");
			attr.setName("记录时间");
			attr.setMyDataType(BP.DA.DataType.AppDateTime);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setTag("1");
			attr.Insert();
		}

		if (attrs.Contains(MapAttrAttr.KeyOfEn, "Rec") == false)
		{
			attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setHisEditType(EditType.Readonly);

			attr.setKeyOfEn("Rec");
			attr.setName("记录人");
			attr.setMyDataType(BP.DA.DataType.AppString);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setMaxLen(20);
			attr.setMinLen(0);
			attr.setDefVal("WebUser.No");
			attr.setTag("WebUser.No");
			attr.Insert();
		}
	}
	private void InitExtMembers() throws Exception
	{
		// 如果启用了多附件
		if (this.getIsEnableAthM())
		{
			BP.Sys.FrmAttachment athDesc = new BP.Sys.FrmAttachment();
			athDesc.setMyPK(this.getNo() + "_AthM");
			if (athDesc.RetrieveFromDBSources() == 0)
			{
				athDesc.setFK_MapData(this.getNo());
				athDesc.setNoOfObj("AthM");
				athDesc.setName(this.getName());
				athDesc.Insert();
			}
		}

	 
	}
	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.InitExtMembers();
		return super.beforeInsert();
	}
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		/*
		if (this.getIsEnablePass())
		{
			//判断是否有IsPass 字段。
			MapAttrs attrs = new MapAttrs(this.getNo());
			if (attrs.Contains(MapAttrAttr.KeyOfEn, "IsPass") == false)
			{
				throw new RuntimeException("您启用了从表单(" + this.getName() + ")条数据审核选项，但是该从表里没IsPass字段，请参考帮助文档。");
			}
		} */
		return super.beforeUpdateInsertAction();
	}
	@Override
	protected boolean beforeUpdate() throws Exception
	{
		/*MapAttrs attrs = new MapAttrs(this.getNo());
		boolean isHaveEnable = false;
		for (MapAttr attr : MapAttrs.convertMapAttrs(attrs))
		{
			if (attr.getUIIsEnable() && attr.getUIContralType() == UIContralType.TB)
			{
				isHaveEnable = true;
			}
		}*/
		this.InitExtMembers();
		return super.beforeUpdate();
	}
	@Override
	protected boolean beforeDelete() throws Exception
	{
		String sql = "";
		sql += "@DELETE FROM Sys_FrmLine WHERE FK_MapData='" + this.getNo() + "'";
		sql += "@DELETE FROM Sys_FrmLab WHERE FK_MapData='" + this.getNo() + "'";
		sql += "@DELETE FROM Sys_FrmLink WHERE FK_MapData='" + this.getNo() + "'";
		sql += "@DELETE FROM Sys_FrmImg WHERE FK_MapData='" + this.getNo() + "'";
		sql += "@DELETE FROM Sys_FrmImgAth WHERE FK_MapData='" + this.getNo() + "'";
		sql += "@DELETE FROM Sys_FrmRB WHERE FK_MapData='" + this.getNo() + "'";
		sql += "@DELETE FROM Sys_FrmAttachment WHERE FK_MapData='" + this.getNo() + "'";
		sql += "@DELETE FROM Sys_MapFrame WHERE FK_MapData='" + this.getNo() + "'";
		sql += "@DELETE FROM Sys_MapExt WHERE FK_MapData='" + this.getNo() + "'";
		sql += "@DELETE FROM Sys_MapAttr WHERE FK_MapData='" + this.getNo() + "'";
		sql += "@DELETE FROM Sys_MapData WHERE No='" + this.getNo() + "'";
		sql += "@DELETE FROM Sys_GroupField WHERE FrmID='" + this.getNo() + "'";
 
		DBAccess.RunSQLs(sql);
		try
		{
			BP.DA.DBAccess.RunSQL("DROP TABLE " + this.getPTable());
		}
		catch (java.lang.Exception e)
		{
		}
		return super.beforeDelete();
	}
}
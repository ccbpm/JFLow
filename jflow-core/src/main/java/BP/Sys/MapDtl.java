package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.Tools.StringHelper;

/** 
 明细
 
*/
public class MapDtl extends EntityNoName
{

		///#region 导入导出属性.
	/**
	 * 关联主键
	 */
	public String getRefPK() {
		String str = this.GetValStrByKey(MapDtlAttr.RefPK);
		if (DataType.IsNullOrEmpty(str))
			return "RefPK";
		return str;
	}

	public void setRefPK(String value) {
		this.SetValByKey(MapDtlAttr.RefPK, value);
	}
	
	/** 
	 Rowid
	 
	*/
	public final int getRowIdx()
	{
		return this.GetValIntByKey(MapDtlAttr.RowIdx);
	}
	public final void setRowIdx(int value)
	{
		this.SetValByKey(MapDtlAttr.RowIdx, value);
	}
	public final int getGroupID()
	{
		return this.GetValIntByKey(MapDtlAttr.GroupID);
	}
	
	public final void setGroupID(int value)
	{
		this.SetValByKey(MapDtlAttr.GroupID, value);
	}
	/** 
	 是否可以导出
	*/
	public final boolean getIsExp()
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsExp);
	}
	public final void setIsExp(boolean value)
	{
		this.SetValByKey(MapDtlAttr.IsExp, value);
	}
	
	/** 
	 是否可以导入？	 
	*/
	public final int getImpModel()
	{
		return this.GetValIntByKey(MapDtlAttr.ImpModel);
	}
	public final void setIsImp(int value)
	{
		this.SetValByKey(MapDtlAttr.ImpModel, value);
	}
	/** 
	 是否启用选择数据项目导入？
	 
	*/
	 
	public final String getFEBD()
	{
		return this.GetValStringByKey(MapDtlAttr.FEBD);
	}
	/** 
	 查询sql
	 
	*/
	public final String getImpSQLInit()
	{
		return this.GetValStringByKey(MapDtlAttr.ImpSQLInit).replace("~", "'");
	}
	public final void setImpSQLInit(String value)
	{
		this.SetValByKey(MapDtlAttr.ImpSQLInit, value);
	}
	/** 
	 搜索sql
	 
	*/
	public final String getImpSQLSearch()
	{
		return this.GetValStringByKey(MapDtlAttr.ImpSQLSearch).replace("~", "'");
	}
	public final void setImpSQLSearch(String value)
	{
		this.SetValByKey(MapDtlAttr.ImpSQLSearch, value);
	}
	/** 
	 填充数据
	 
	*/
	public final String getImpSQLFull()
	{
		return this.GetValStringByKey(MapDtlAttr.ImpSQLFull).replace("~","'");
	}
	public final void setImpSQLFull(String value)
	{
		this.SetValByKey(MapDtlAttr.ImpSQLFull, value);
	}

	public final String getImpSQLFullOneRow() {
		return this.GetValStringByKey(MapDtlAttr.ImpSQLFullOneRow).replace("~", "'");
	}

	public final void setImpSQLFullOneRow(String value) {
		this.SetValByKey(MapDtlAttr.ImpSQLFullOneRow, value);
	}

	/**
	 * 填充属性sql
	 */
	public final String getImpFixTreeSql()
	{
		return this.GetValStringByKey(MapDtlAttr.ImpFixTreeSql);
	}
	public final void setImpFixTreeSql(String value)
	{
		this.SetValByKey(MapDtlAttr.ImpFixTreeSql, value);
	}
	/** 
	 工作模式
	*/
	public final DtlModel getDtlModel()
	{
		return DtlModel.forValue(this.GetValIntByKey(MapDtlAttr.Model));
	}
	public final void setDtlModel(DtlModel value)
	{
		this.SetValByKey(MapDtlAttr.Model, value.getValue());
	}
	
	/** 
	 是否启用行锁定.
	*/
	public final int getPTableModel()
	{
		return this.GetParaInt(MapDataAttr.PTableModel);
	}
	public final void setPTableModel(int value)
	{
		this.SetPara(MapDataAttr.PTableModel, value);
	}
	
	/** 
	 是否启用行锁定.
	*/
	public final boolean getIsRowLock()
	{
		return this.GetParaBoolen(MapDtlAttr.IsRowLock, false);
	}
	public final void setIsRowLock(boolean value)
	{
		this.SetPara(MapDtlAttr.IsRowLock, value);
	}
	/** 
	 记录增加模式
	*/
	public final DtlAddRecModel getDtlAddRecModel()
	{
		return DtlAddRecModel.forValue(this.GetParaInt(MapDtlAttr.DtlAddRecModel));
	}
	public final void setDtlAddRecModel(DtlAddRecModel value)
	{
		this.SetPara(MapDtlAttr.DtlAddRecModel, value.getValue());
	}
	/** 
	 保存方式
	 
	*/
	public final DtlSaveModel getDtlSaveModel()
	{
		return DtlSaveModel.forValue(this.GetParaInt(MapDtlAttr.DtlSaveModel));
	}
	public final void setDtlSaveModel(DtlSaveModel value)
	{
		this.SetPara(MapDtlAttr.DtlSaveModel, value.getValue());
	}
	/** 
	 是否启用Link,在记录的右边.
	 
	*/
	public final boolean getIsEnableLink()
	{
		return this.GetParaBoolen(MapDtlAttr.IsEnableLink, false);
	}
	public final void setIsEnableLink(boolean value)
	{
		this.SetPara(MapDtlAttr.IsEnableLink, value);
	}
	public final String getLinkLabel()
	{
		String s= this.GetParaString(MapDtlAttr.LinkLabel);
		if (StringHelper.isNullOrEmpty(s))
		{
			return "详细";
		}
		return s;
	}
	public final void setLinkLabel(String value)
	{
		this.SetPara(MapDtlAttr.LinkLabel, value);
	}
	public final String getLinkUrl()
	{
		String s = this.GetParaString(MapDtlAttr.LinkUrl);
		if (StringHelper.isNullOrEmpty(s))
		{
			return "http://ccport.org";
		}

		s = s.replace("*", "@");
		return s;
	}
	public final void setLinkUrl(String value)
	{
		String val = value;
		val = val.replace("@", "*");
		this.SetPara(MapDtlAttr.LinkUrl, val);
	}
	public final String getLinkTarget()
	{
		String s = this.GetParaString(MapDtlAttr.LinkTarget);
		if (StringHelper.isNullOrEmpty(s))
		{
			return "_blank";
		}
		return s;
	}
	public final void setLinkTarget(String value)
	{
		this.SetPara(MapDtlAttr.LinkTarget, value);
	}
	// 基本设置
	
		public final String getImpFixDataSql()
		{
			return this.GetValStringByKey(MapDtlAttr.ImpFixDataSql);
		}
		
		public final void setImpFixDataSql(String value)
		{
			this.SetValByKey(MapDtlAttr.ImpFixDataSql, value);
		}
	/** 
	 子线程处理人字段(用于分流节点的明细表分配子线程任务).
	 
	*/
	public final String getSubThreadWorker()
	{
		String s = this.GetParaString(MapDtlAttr.SubThreadWorker);
		if (StringHelper.isNullOrEmpty(s))
		{
			return "";
		}
		return s;
	}
	public final void setSubThreadWorker(String value)
	{
		this.SetPara(MapDtlAttr.SubThreadWorker, value);
	}
	/** 
	 子线程分组字段(用于分流节点的明细表分配子线程任务)
	 
	*/
	public final String getSubThreadGroupMark()
	{
		String s = this.GetParaString(MapDtlAttr.SubThreadGroupMark);
		if (StringHelper.isNullOrEmpty(s))
		{
			return "";
		}
		return s;
	}
	public final void setSubThreadGroupMark(String value)
	{
		this.SetPara(MapDtlAttr.SubThreadGroupMark, value);
	}
	/** 
	 节点ID
	 
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(MapDtlAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		this.SetValByKey(MapDtlAttr.FK_Node, value);
	}

		///#endregion 参数属性


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
	public final MapDtls getMapDtls() throws Exception
	{
		Object tempVar = this.GetRefObject("MapDtls");
		MapDtls obj = (MapDtls)((tempVar instanceof MapDtls) ? tempVar : null);
		if (obj == null)
		{
			obj = new MapDtls(this.getNo());
			this.SetRefObject("MapDtls", obj);
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


		
	public GEDtls HisGEDtls_temp = null;
	public final DtlShowModel getHisDtlShowModel()
	{
		return DtlShowModel.forValue(this.GetValIntByKey(MapDtlAttr.DtlShowModel));
	}
	public final void setHisDtlShowModel(DtlShowModel value)
	{
		this.SetValByKey(MapDtlAttr.DtlShowModel, value.getValue());
	}
	/** 
	 
	 
	*/
	public final WhenOverSize getHisWhenOverSize()
	{
		return WhenOverSize.forValue(this.GetValIntByKey(MapDtlAttr.WhenOverSize));
	}
	public final void setHisWhenOverSize(WhenOverSize value)
	{
		this.SetValByKey(MapDtlAttr.WhenOverSize, value.getValue());
	}
	/** 
	 是否显示数量
	 
	*/
	public final boolean getIsShowSum()
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsShowSum);
	}
	public final void setIsShowSum(boolean value)
	{
		this.SetValByKey(MapDtlAttr.IsShowSum, value);
	}
	/** 
	 是否显示Idx
	 
	*/
	public final boolean getIsShowIdx()
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsShowIdx);
	}
	public final void setIsShowIdx(boolean value)
	{
		this.SetValByKey(MapDtlAttr.IsShowIdx, value);
	}
	/** 
	 是否显示标题
	 
	*/
	public final boolean getIsShowTitle()
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsShowTitle);
	}
	public final void setIsShowTitle(boolean value)
	{
		this.SetValByKey(MapDtlAttr.IsShowTitle, value);
	}
	/** 
	 是否是合流汇总数据
	 
	*/
	public final boolean getIsHLDtl()
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsHLDtl);
	}
	public final void setIsHLDtl(boolean value)
	{
		this.SetValByKey(MapDtlAttr.IsHLDtl, value);
	}
	/** 
	 是否是分流
	 
	*/
	public final boolean getIsFLDtl()
	{
		return this.GetParaBoolen(MapDtlAttr.IsFLDtl);
	}
	public final void setIsFLDtl(boolean value)
	{
		this.SetPara(MapDtlAttr.IsFLDtl, value);
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
	/** 
	 是否可以删除？
	 
	*/
	public final boolean getIsDelete()
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsDelete);
	}
	public final void setIsDelete(boolean value)
	{
		this.SetValByKey(MapDtlAttr.IsDelete, value);
	}
	/** 
	 是否可以新增？
	 
	*/
	public final boolean getIsInsert()
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsInsert);
	}
	public final void setIsInsert(boolean value)
	{
		this.SetValByKey(MapDtlAttr.IsInsert, value);
	}
	/** 
	 是否可见
	 
	*/
	public final boolean getIsView()
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsView);
	}
	public final void setIsView(boolean value)
	{
		this.SetValByKey(MapDtlAttr.IsView, value);
	}
	/** 
	 是否可以更新？
	 
	*/
	public final boolean getIsUpdate()
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsUpdate);
	}
	public final void setIsUpdate(boolean value)
	{
		this.SetValByKey(MapDtlAttr.IsUpdate, value);
	}
	/** 
	 是否启用多附件
	 
	*/
	public final boolean getIsEnableAthM()
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsEnableAthM);
	}
	public final void setIsEnableAthM(boolean value)
	{
		this.SetValByKey(MapDtlAttr.IsEnableAthM, value);
	}
	/** 
	 是否启用分组字段
	 
	*/
	public final boolean getIsEnableGroupField()
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsEnableGroupField);
	}
	public final void setIsEnableGroupField(boolean value)
	{
		this.SetValByKey(MapDtlAttr.IsEnableGroupField, value);
	}
	/** 
	 是否起用审核连接
	 
	*/
	public final boolean getIsEnablePass()
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsEnablePass);
	}
	public final void setIsEnablePass(boolean value)
	{
		this.SetValByKey(MapDtlAttr.IsEnablePass, value);
	}

	/** 
	 是否copy数据？
	 
	*/
	public final boolean getIsCopyNDData()
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsCopyNDData);
	}
	public final void setIsCopyNDData(boolean value)
	{
		this.SetValByKey(MapDtlAttr.IsCopyNDData, value);
	}
	/** 
	 是否启用一对多
	 
	*/
	public final boolean getIsEnableM2M()
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsEnableM2M);
	}
	public final void setIsEnableM2M(boolean value)
	{
		this.SetValByKey(MapDtlAttr.IsEnableM2M, value);
	}
	/** 
	 是否启用一对多多
	 
	*/
	public final boolean getIsEnableM2MM()
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsEnableM2MM);
	}
	public final void setIsEnableM2MM(boolean value)
	{
		this.SetValByKey(MapDtlAttr.IsEnableM2MM, value);
	}

	public boolean IsUse = false;
	/** 
	 是否检查人员的权限
	 
	*/
	public final DtlOpenType getDtlOpenType()
	{
		return DtlOpenType.forValue(this.GetValIntByKey(MapDtlAttr.DtlOpenType));
	}
	public final void setDtlOpenType(DtlOpenType value)
	{
		this.SetValByKey(MapDtlAttr.DtlOpenType, value.getValue());
	}
	/** 
	 分组字段
	 
	*/
	public final String getGroupField()
	{
		return this.GetValStrByKey(MapDtlAttr.GroupField);
	}
	public final void setGroupField(String value)
	{
		this.SetValByKey(MapDtlAttr.GroupField, value);
	}
	/** 
	 表单ID
	 
	*/
	public final String getFK_MapData()
	{
		return this.GetValStrByKey(MapDtlAttr.FK_MapData);
	}
	public final void setFK_MapData(String value)
	{
		this.SetValByKey(MapDtlAttr.FK_MapData, value);
	}
	/** 
	 数量
	 
	*/
	public final int getRowsOfList()
	{
			//如果不可以插入，就让其返回0.
		if (this.getIsInsert() == false)
		{
			return 0;
		}

		return this.GetValIntByKey(MapDtlAttr.RowsOfList);
	}
	public final void setRowsOfList(int value)
	{
		this.SetValByKey(MapDtlAttr.RowsOfList, value);
	}
	/** 
	 物理表
	 
	*/
	public final String getPTable()
	{
		String s = this.GetValStrByKey(MapDtlAttr.PTable);
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
		this.SetValByKey(MapDtlAttr.PTable, value);
	}
	
	 public final EditModel getHisEditModel()
     {
         return EditModel.forValue(this.GetValIntByKey(MapDtlAttr.EditModel));
     }
	 public final void setHisEditModel(EditModel value){
        this.SetValByKey(MapDtlAttr.EditModel, value.getValue());
        
     }
	 
	 /** 
	 过滤的SQL表达式.
	 
	 */
	public final String getFilterSQLExp()
	{
		String s = this.GetValStrByKey(MapDtlAttr.FilterSQLExp);
		if (StringHelper.isNullOrEmpty(s) == true)
		{
			return "";
		}
		s = s.replace("~", "'");
		return s.trim();
	}
	public final void setFilterSQLExp(String value)
	{
		this.SetValByKey(MapDtlAttr.FilterSQLExp, value);
	}
	/** 
	 多表头
	 
	*/
	public final String getMTR()
	{
		String s= this.GetValStrByKey(MapDtlAttr.MTR);
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
		this.SetValByKey(MapDtlAttr.MTR, value);
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
	 明细
	 
	*/
	public MapDtl()
	{
	}
	public MapDtl(String mypk) throws Exception
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
		 
		  Map map = new Map("Sys_MapDtl", "明细");
          map.Java_SetDepositaryOfEntity(Depositary.None);
          map.Java_SetEnType(EnType.Sys);

        
          map.AddTBStringPK(MapDtlAttr.No, null, "编号", true, false, 1, 100, 20);
          map.AddTBString(MapDtlAttr.Name, null, "描述", true, false, 1, 200, 20);
          map.AddTBString(MapDtlAttr.Alias, null, "别名", true, false, 1, 200, 20);
          map.AddTBString(MapDtlAttr.FK_MapData, null, "主表", true, false, 0, 100, 20);
          map.AddTBString(MapDtlAttr.PTable, null, "物理表", true, false, 0, 200, 20);
          map.AddTBString(MapDtlAttr.GroupField, null, "分组字段", true, false, 0, 300, 20);
          map.AddTBString(MapDtlAttr.RefPK, null, "关联的主键", true, false, 0, 100, 20);

          // 为明细表初始化事件类.
          map.AddTBString(MapDtlAttr.FEBD, null, "映射的事件实体类", true, false, 0, 100, 20);

          //map.AddTBInt(MapDtlAttr.Model, 0, "工作模式", false, false);
          map.AddDDLSysEnum(MapDtlAttr.Model, 0, "工作模式", true, true,
           MapDtlAttr.Model, "@0=普通@1=固定行");

          map.AddTBInt(MapDtlAttr.RowsOfList, 6, "初始化行数", false, false);

          map.AddBoolean(MapDtlAttr.IsEnableGroupField, false, "是否启用分组字段", false, false);

          map.AddBoolean(MapDtlAttr.IsShowSum, true, "是否显示合计？", false, false);
          map.AddBoolean(MapDtlAttr.IsShowIdx, true, "是否显示序号？", false, false);
          map.AddBoolean(MapDtlAttr.IsCopyNDData, true, "是否允许Copy数据", false, false);
          map.AddBoolean(MapDtlAttr.IsHLDtl, false, "是否是合流汇总", false, false);

          map.AddBoolean(MapDtlAttr.IsReadonly, false, "是否只读？", false, false);
          map.AddBoolean(MapDtlAttr.IsShowTitle, true, "是否显示标题？", false, false);
          map.AddBoolean(MapDtlAttr.IsView, true, "是否可见", false, false);

          map.AddBoolean(MapDtlAttr.IsInsert, true, "是否可以插入行？", false, false);
          map.AddBoolean(MapDtlAttr.IsDelete, true, "是否可以删除行", false, false);
          map.AddBoolean(MapDtlAttr.IsUpdate, true, "是否可以更新？", false, false);

          map.AddBoolean(MapDtlAttr.IsEnablePass, false, "是否启用通过审核功能?", false, false);
          map.AddBoolean(MapDtlAttr.IsEnableAthM, false, "是否启用多附件", false, false);

          map.AddBoolean(MapDtlAttr.IsEnableM2M, false, "是否启用M2M", false, false);
          map.AddBoolean(MapDtlAttr.IsEnableM2MM, false, "是否启用M2M", false, false);


          // 以下4 @于庆海.需要对比翻译.
          
          // 超出行数
          map.AddTBInt(MapDtlAttr.WhenOverSize, 0, "列表数据显示格式", false, false);

          //数据开放类型 .
          map.AddTBInt(MapDtlAttr.DtlOpenType, 1, "数据开放类型", false, false);
          
          map.AddTBInt(MapDtlAttr.ListShowModel, 0, "列表数据显示格式", false, false);
          map.AddTBInt(MapDtlAttr.EditModel, 0, "行数据显示格式", false, false);
            

          map.AddTBFloat(MapDtlAttr.X, 5, "距左", true, false);
          map.AddTBFloat(MapDtlAttr.Y, 5, "距上", false, false);

          map.AddTBFloat(MapDtlAttr.H, 150, "高度", true, false);
          map.AddTBFloat(MapDtlAttr.W, 200, "宽度", false, false);

          map.AddTBFloat(MapDtlAttr.FrmW, 900, "表单宽度", true, true);
          map.AddTBFloat(MapDtlAttr.FrmH, 1200, "表单高度", true, true);

          //MTR 多表头列.
          map.AddTBString(MapDtlAttr.MTR, null, "多表头列", true, false, 0, 3000, 20);

          map.AddTBString(MapDtlAttr.FilterSQLExp, null, "过滤SQL表达式", true, false, 0, 200, 20, true);
          map.AddTBString(FrmBtnAttr.GUID, null, "GUID", false, false, 0, 128, 20);

          //add 2014-02-21.
          map.AddTBInt(MapDtlAttr.FK_Node, 0, "节点(用户独立表单权限控制)", false, false);

          //#region 导入导出填充.
          // 2014-07-17 for xinchang bank.
          map.AddBoolean(MapDtlAttr.IsExp, true, "IsExp", false, false);
          map.AddTBInt(MapDtlAttr.ImpModel,0, "导入模式", false, false);
         // map.AddBoolean(MapDtlAttr.IsEnableSelectImp, false, "是否启用选择数据导入?", false, false);
          map.AddTBString(MapDtlAttr.ImpSQLSearch, null, "查询SQL", true, false, 0, 500, 20);
          map.AddTBString(MapDtlAttr.ImpSQLInit, null, "初始化SQL", true, false, 0, 500, 20);
          map.AddTBString(MapDtlAttr.ImpSQLFullOneRow, null, "数据填充SQL", true, false, 0, 500, 20);
          map.AddTBString(MapDtlAttr.ImpSQLFull, null, "填充数据", true, false, 0, 500, 20);
          //#endregion 导入导出填充.


          map.AddTBString(MapDtlAttr.ColAutoExp, null, "列自动计算表达式", true, false, 0, 200, 20, true);



          //要显示的列.
          map.AddTBString(MapDtlAttr.ShowCols, null, "显示的列", true, false, 0, 500, 20, true);
          //map.SetHelperAlert(MapDtlAttr.ShowCols, "默认为空,全部显示,如果配置了就按照配置的计算,格式为:field1,field2");
          
          
          
          map.AddTBInt(MapDtlAttr.PTableModel, 0, "行数据显示格式", false, false);

          
          //参数.
          map.AddTBAtParas(300);

			///#endregion 导入导出填充.

		this.set_enMap(map);
		return this.get_enMap();
	}
	public final float getX()
	{
		return this.GetValFloatByKey(FrmImgAttr.X);
	}
	public final void setX(float value)
	{
		this.SetValByKey(MapDtlAttr.X, value);
	}

	public final float getY()
	{
		return this.GetValFloatByKey(FrmImgAttr.Y);
	}
	public final void setY(float value)
	{
		this.SetValByKey(MapDtlAttr.Y, value);
	}
	public final float getW()
	{
		return this.GetValFloatByKey(FrmImgAttr.W);
	}
	public final void setW(float value)
	{
		this.SetValByKey(MapDtlAttr.W, value);
	}
	public final float getH()
	{
		return this.GetValFloatByKey(FrmImgAttr.H);
	}
	public final void setH(float value)
	{
		this.SetValByKey(MapDtlAttr.H, value);
	}
	public final float getFrmW()
	{
		return this.GetValFloatByKey(MapDtlAttr.FrmW);
	}
	public final float getFrmH()
	{
		return this.GetValFloatByKey(MapDtlAttr.FrmH);
	}

		///#endregion 基本属性.

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
			athDesc.setMyPK(this.getNo() + "_AthMDtl");
			if (athDesc.RetrieveFromDBSources() == 0)
			{
				athDesc.setFK_MapData(this.getNo());
				athDesc.setNoOfObj("AthMDtl");
				athDesc.setName(this.getName());
				athDesc.Insert();
			}
		}

		 
	}
	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.InitExtMembers();

		GroupField gf = new GroupField();
		if (gf.IsExit(GroupFieldAttr.CtrlID, this.getNo()) == false)
		{
			gf.setFrmID(this.getFK_MapData());
			gf.setCtrlID(this.getNo());
			gf.setCtrlType("Dtl");
			gf.setLab(this.getName());
			gf.setIdx(0);
			gf.Insert(); //插入.
		}

		return super.beforeInsert();
	}
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		BP.Sys.MapData md = new BP.Sys.MapData();
		md.setNo(this.getNo());
		if (md.RetrieveFromDBSources() == 0)
		{
			md.setName(this.getName());
			md.Insert();
		}

		if (this.getIsRowLock() == true)
		{
			//检查是否启用了行锁定.
			MapAttrs attrs = new MapAttrs(this.getNo());
			if (attrs.Contains(MapAttrAttr.KeyOfEn, "IsRowLock") == false)
			{
				throw new RuntimeException("您启用了从表单(" + this.getName() + ")行数据锁定功能，但是该从表里没IsRowLock字段，请参考帮助文档。");
			}
		}

		if (this.getIsEnablePass())
		{
			//判断是否有IsPass 字段。
			MapAttrs attrs = new MapAttrs(this.getNo());
			if (attrs.Contains(MapAttrAttr.KeyOfEn, "IsPass") == false)
			{
				throw new RuntimeException("您启用了从表单(" + this.getName() + ")条数据审核选项，但是该从表里没IsPass字段，请参考帮助文档。");
			}
		}
		return super.beforeUpdateInsertAction();
	}
	@Override
	protected boolean beforeUpdate() throws Exception
	{
		MapAttrs attrs = new MapAttrs(this.getNo());
		boolean isHaveEnable = false;
		for (MapAttr attr : MapAttrs.convertMapAttrs(attrs))
		{
			if (attr.getUIIsEnable() && attr.getUIContralType() == UIContralType.TB)
			{
				isHaveEnable = true;
			}
		}

		this.InitExtMembers();

		//更新MapData中的名称
		BP.Sys.MapData md = new BP.Sys.MapData();
		md.setNo(this.getNo());
		if (md.RetrieveFromDBSources() == 1)
		{
			md.setName(this.getName());
			md.setPTable(this.getPTable());
			//避免显示在表单库
			md.setFK_FormTree("");
            md.setFK_FrmSort("");
			md.DirectUpdate();
		}

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
		//sql += "@DELETE FROM Sys_MapM2M WHERE FK_MapData='" + this.getNo() + "'";
		sql += "@DELETE FROM Sys_GroupField WHERE CtrlID='" + this.getNo() + "'";
		DBAccess.RunSQLs(sql);


		if (DBAccess.IsExitsObject(this.getPTable()) &&  this.getPTable().indexOf("ND")==0 )
		{
			//如果其他表单引用了该表，就不能删除它.
			sql = "SELECT COUNT(No) AS NUM  FROM Sys_MapData WHERE PTable='" + this.getPTable() + "' OR ( PTable='' AND No='" + this.getPTable() + "')";
			if (DBAccess.RunSQLReturnValInt(sql, 0) > 1)
			{
				// 说明有多个表单在引用.
			}
			else
			{
				DBAccess.RunSQL("DROP TABLE " + this.getPTable());
			}
		}

		//if (DBAccess.IsExitsObject(this.PTable))
		//    DBAccess.RunSQL("DROP TABLE " + this.PTable);

		return super.beforeDelete();
	}
	public void setAlias(String val) {
		 
		
		this.SetValByKey(MapDtlAttr.Alias, val);
		
	}
}
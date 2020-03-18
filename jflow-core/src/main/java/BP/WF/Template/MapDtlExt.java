package BP.WF.Template;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.En.*;
import BP.En.Map;
import BP.Sys.*;
import BP.Web.WebUser;

/** 
 明细
*/
public class MapDtlExt extends EntityNoName
{

		///#region 导入导出属性.
	/** 
	 用户访问.
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForAppAdmin();
		uac.IsInsert = false;
		return uac;
	}
	/**
	 是否可以导出
	 * @throws Exception
	 */
	public final boolean getIsImp() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsImp);
	}
	public final void setIsImp(boolean value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.IsImp, value);
	}
	/** 
	 是否可以导入
	 * @throws Exception 
	*/
	public final boolean getIsExp() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsExp);
	}
	public final void setIsExp(boolean value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.IsExp, value);
	}
	/** 
	 查询sql
	 * @throws Exception 
	*/
	public final String getImpSQLInit() throws Exception
	{
		return this.GetValStringByKey(MapDtlAttr.ImpSQLInit).replace("~", "'");
	}
	public final void setImpSQLInit(String value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.ImpSQLInit, value);
	}
	/** 
	 搜索sql
	 * @throws Exception 
	*/
	public final String getImpSQLSearch() throws Exception
	{
		return this.GetValStringByKey(MapDtlAttr.ImpSQLSearch).replace("~", "'");
	}
	public final void setImpSQLSearch(String value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.ImpSQLSearch, value);
	}
	/** 
	 填充数据一行数据
	 * @throws Exception 
	*/
	public final String getImpSQLFullOneRow() throws Exception
	{
		return this.GetValStringByKey(MapDtlAttr.ImpSQLFullOneRow).replace("~", "'");
	}
	public final void setImpSQLFullOneRow(String value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.ImpSQLFullOneRow, value);
	}

		///#endregion


		///#region 基本设置
	/** 
	 工作模式
	 * @throws Exception 
	*/
	public final DtlModel getDtlModel() throws Exception
	{
		return DtlModel.forValue(this.GetValIntByKey(MapDtlAttr.Model));
	}
	public final void setDtlModel(DtlModel value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.Model, value.getValue());
	}
	/** 
	 是否启用行锁定.
	 * @throws Exception 
	*/
	public final boolean getIsRowLock() throws Exception
	{
		return this.GetParaBoolen(MapDtlAttr.IsRowLock, false);
	}
	public final void setIsRowLock(boolean value) throws Exception
	{
		this.SetPara(MapDtlAttr.IsRowLock, value);
	}

		///#endregion 基本设置


		///#region 参数属性
	/** 
	 记录增加模式
	 * @throws Exception 
	*/
	public final DtlAddRecModel getDtlAddRecModel() throws Exception
	{
		return DtlAddRecModel.forValue(this.GetParaInt(MapDtlAttr.DtlAddRecModel));
	}
	public final void setDtlAddRecModel(DtlAddRecModel value) throws Exception
	{
		this.SetPara(MapDtlAttr.DtlAddRecModel, value.getValue());
	}
	/** 
	 保存方式
	 * @throws Exception 
	*/
	public final DtlSaveModel getDtlSaveModel() throws Exception
	{
		return DtlSaveModel.forValue(this.GetParaInt(MapDtlAttr.DtlSaveModel));
	}
	public final void setDtlSaveModel(DtlSaveModel value) throws Exception
	{
		this.SetPara(MapDtlAttr.DtlSaveModel, value.getValue());
	}
	/** 
	 是否启用Link,在记录的右边.
	 * @throws Exception 
	*/
	public final boolean getIsEnableLink() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsEnableLink, false);
	}
	public final void setIsEnableLink(boolean value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.IsEnableLink, value);
		this.SetPara(MapDtlAttr.IsEnableLink, value);
	}
	public final String getLinkLabel() throws Exception
	{
		String s = this.GetValStrByKey(MapDtlAttr.LinkLabel);
		if (DataType.IsNullOrEmpty(s))
		{
			return "详细";
		}
		return s;
	}
	public final void setLinkLabel(String value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.LinkLabel, value);
		this.SetPara(MapDtlAttr.LinkLabel, value);
	}
	public final String getLinkUrl() throws Exception
	{
		String s = this.GetValStrByKey(MapDtlAttr.LinkUrl);
		if (DataType.IsNullOrEmpty(s))
		{
			return "http://ccport.org";
		}

		s = s.replace("*", "@");
		return s;
	}
	public final void setLinkUrl(String value) throws Exception
	{
		String val = value;
		val = val.replace("@", "*");
		this.SetValByKey(MapDtlAttr.LinkUrl, val);
		this.SetPara(MapDtlAttr.LinkUrl, val);
	}
	public final String getLinkTarget() throws Exception
	{
		String s = this.GetValStrByKey(MapDtlAttr.LinkTarget);
		if (DataType.IsNullOrEmpty(s))
		{
			return "_blank";
		}
		return s;
	}
	public final void setLinkTarget(String value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.LinkTarget, value);
		this.SetPara(MapDtlAttr.LinkTarget, value);
	}
	/** 
	 子线程处理人字段(用于分流节点的明细表分配子线程任务).
	 * @throws Exception 
	*/
	public final String getSubThreadWorker() throws Exception
	{
		String s = this.GetParaString(MapDtlAttr.SubThreadWorker);
		if (DataType.IsNullOrEmpty(s))
		{
			return "";
		}
		return s;
	}
	public final void setSubThreadWorker(String value) throws Exception
	{
		this.SetPara(MapDtlAttr.SubThreadWorker, value);
	}
	/** 
	 子线程分组字段(用于分流节点的明细表分配子线程任务)
	 * @throws Exception 
	*/
	public final String getSubThreadGroupMark() throws Exception
	{
		String s = this.GetParaString(MapDtlAttr.SubThreadGroupMark);
		if (DataType.IsNullOrEmpty(s))
		{
			return "";
		}
		return s;
	}
	public final void setSubThreadGroupMark(String value) throws Exception
	{
		this.SetPara(MapDtlAttr.SubThreadGroupMark, value);
	}
	/** 
	 节点ID
	 * @throws Exception 
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(MapDtlAttr.FK_Node);
	}
	public final void setFK_Node(int value) throws Exception
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
		MapFrames obj = tempVar instanceof MapFrames ? (MapFrames)tempVar : null;
		if (obj == null)
		{
			obj = new MapFrames(this.getNo());
			this.SetRefObject("MapFrames", obj);
		}
		return obj;
	}
	/** 
	 分组字段
	 * @throws Exception 
	*/
	public final GroupFields getGroupFields_del() throws Exception
	{
		Object tempVar = this.GetRefObject("GroupFields");
		GroupFields obj = tempVar instanceof GroupFields ? (GroupFields)tempVar : null;
		if (obj == null)
		{
			obj = new GroupFields(this.getNo());
			this.SetRefObject("GroupFields", obj);
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
		MapExts obj = tempVar instanceof MapExts ? (MapExts)tempVar : null;
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
		FrmEvents obj = tempVar instanceof FrmEvents ? (FrmEvents)tempVar : null;
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
		MapDtls obj = tempVar instanceof MapDtls ? (MapDtls)tempVar : null;
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
		FrmLinks obj = tempVar instanceof FrmLinks ? (FrmLinks)tempVar : null;
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
		FrmBtns obj = tempVar instanceof FrmBtns ? (FrmBtns)tempVar : null;
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
		FrmEles obj = tempVar instanceof FrmEles ? (FrmEles)tempVar : null;
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
		FrmLines obj = tempVar instanceof FrmLines ? (FrmLines)tempVar : null;
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
		FrmLabs obj = tempVar instanceof FrmLabs ? (FrmLabs)tempVar : null;
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
		FrmImgs obj = tempVar instanceof FrmImgs ? (FrmImgs)tempVar : null;
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
		FrmAttachments obj = tempVar instanceof FrmAttachments ? (FrmAttachments)tempVar : null;
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
		FrmImgAths obj = tempVar instanceof FrmImgAths ? (FrmImgAths)tempVar : null;
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
		FrmRBs obj = tempVar instanceof FrmRBs ? (FrmRBs)tempVar : null;
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
		MapAttrs obj = tempVar instanceof MapAttrs ? (MapAttrs)tempVar : null;
		if (obj == null)
		{
			obj = new MapAttrs(this.getNo());
			this.SetRefObject("MapAttrs", obj);
		}
		return obj;
	}

		///#endregion


		///#region 属性
	public GEDtls HisGEDtls_temp = null;
	/** 
	 是否显示数量
	 * @throws Exception 
	*/
	public final boolean getIsShowSum() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsShowSum);
	}
	public final void setIsShowSum(boolean value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.IsShowSum, value);
	}
	/** 
	 是否显示Idx
	 * @throws Exception 
	*/
	public final boolean getIsShowIdx() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsShowIdx);
	}
	public final void setIsShowIdx(boolean value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.IsShowIdx, value);
	}
	/** 
	 是否显示标题
	 * @throws Exception 
	*/
	public final boolean getIsShowTitle() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsShowTitle);
	}
	public final void setIsShowTitle(boolean value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.IsShowTitle, value);
	}
	/** 
	 是否是合流汇总数据
	 * @throws Exception 
	*/
	public final boolean getIsHLDtl() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsHLDtl);
	}
	public final void setIsHLDtl(boolean value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.IsHLDtl, value);
	}
	/** 
	 是否是分流
	 * @throws Exception 
	*/
	public final boolean getIsFLDtl() throws Exception
	{
		return this.GetParaBoolen(MapDtlAttr.IsFLDtl);
	}
	public final void setIsFLDtl(boolean value) throws Exception
	{
		this.SetPara(MapDtlAttr.IsFLDtl, value);
	}
	public int _IsReadonly = 2;
	public final boolean getIsReadonly() throws Exception
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
	 * @throws Exception 
	*/
	public final boolean getIsDelete() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsDelete);
	}
	public final void setIsDelete(boolean value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.IsDelete, value);
	}
	/** 
	 是否可以新增？
	 * @throws Exception 
	*/
	public final boolean getIsInsert() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsInsert);
	}
	public final void setIsInsert(boolean value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.IsInsert, value);
	}
	/** 
	 是否可见
	 * @throws Exception 
	*/
	public final boolean getIsView() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsView);
	}
	public final void setIsView(boolean value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.IsView, value);
	}
	/** 
	 是否可以更新？
	 * @throws Exception 
	*/
	public final boolean getIsUpdate() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsUpdate);
	}
	public final void setIsUpdate(boolean value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.IsUpdate, value);
	}
	/** 
	 是否启用多附件
	 * @throws Exception 
	*/
	public final boolean getIsEnableAthM() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsEnableAthM);
	}
	public final void setIsEnableAthM(boolean value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.IsEnableAthM, value);
	}
	/** 
	 是否启用分组字段
	 * @throws Exception 
	*/
	public final boolean getIsEnableGroupField() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsEnableGroupField);
	}
	public final void setIsEnableGroupField(boolean value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.IsEnableGroupField, value);
	}
	/** 
	 是否起用审核连接
	 * @throws Exception 
	*/
	public final boolean getIsEnablePass() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsEnablePass);
	}
	public final void setIsEnablePass(boolean value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.IsEnablePass, value);
	}

	/** 
	 是否copy数据？
	 * @throws Exception 
	*/
	public final boolean getIsCopyNDData() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsCopyNDData);
	}
	public final void setIsCopyNDData(boolean value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.IsCopyNDData, value);
	}
	/** 
	 是否启用一对多
	 * @throws Exception 
	*/
	public final boolean getIsEnableM2M() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsEnableM2M);
	}
	public final void setIsEnableM2M(boolean value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.IsEnableM2M, value);
	}
	/** 
	 是否启用一对多多
	 * @throws Exception 
	*/
	public final boolean getIsEnableM2MM() throws Exception
	{
		return this.GetValBooleanByKey(MapDtlAttr.IsEnableM2MM);
	}
	public final void setIsEnableM2MM(boolean value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.IsEnableM2MM, value);
	}

	public boolean IsUse = false;
	/** 
	 是否检查人员的权限
	 * @throws Exception 
	*/
	public final DtlOpenType getDtlOpenType() throws Exception
	{
		return DtlOpenType.forValue(this.GetValIntByKey(MapDtlAttr.DtlOpenType));
	}
	public final void setDtlOpenType(DtlOpenType value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.DtlOpenType, value.getValue());
	}
	/** 
	 分组字段
	 * @throws Exception 
	*/
	public final String getGroupField() throws Exception
	{
		return this.GetValStrByKey(MapDtlAttr.GroupField);
	}
	public final void setGroupField(String value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.GroupField, value);
	}
	/** 
	 表单ID
	*/
	public final String getFK_MapData() throws Exception
	{
		return this.GetValStrByKey(MapDtlAttr.FK_MapData);
	}
	public final void setFK_MapData(String value) throws Exception
	{ 
		this.SetValByKey(MapDtlAttr.FK_MapData, value);
	}
	/** 
	 事件类.
	*/
	public final String getFEBD() throws Exception
	{
		return this.GetValStrByKey(MapDtlAttr.FEBD);
	}
	public final void setFEBD(String value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.FEBD, value);
	}

	/** 
	 数量
	*/
	public final int getRowsOfList() throws Exception
	{
			//如果不可以插入，就让其返回0.
		if (this.getIsInsert() == false)
		{
			return 0;
		}

		return this.GetValIntByKey(MapDtlAttr.RowsOfList);
	}
	public final void setRowsOfList(int value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.RowsOfList, value);
	}
	/** 
	 物理表
	*/
	public final String getPTable() throws Exception
	{
		String s = this.GetValStrByKey(MapDtlAttr.PTable);
		if ( s == null || s.equals(""))
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
	public final void setPTable(String value) throws Exception
	{
		this.SetValByKey(MapDtlAttr.PTable, value);
	}
	/** 
	 多表头
	*/
//	public final String getMTR() throws Exception
//	{
//		String s = this.GetValStrByKey(MapDtlAttr.MTR);
//		s = s.replace("《", "<");
//		s = s.replace("》", ">");
//		s = s.replace("‘", "'");
//		return s;
//	}
//	public final void setMTR(String value) throws Exception
//	{
//		String s = value;
//		s = s.replace("<", "《");
//		s = s.replace(">", "》");
//		s = s.replace("'", "‘");
//		this.SetValByKey(MapDtlAttr.MTR, value);
//	}

		///#endregion


		///#region 构造方法
	/** 
	 明细
	*/
	public MapDtlExt()
	{
	}
	public MapDtlExt(String mypk) throws Exception
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
		map.IndexField = MapDtlAttr.FK_MapData;



			///#region 基础信息.
		map.AddTBStringPK(MapDtlAttr.No, null, "编号", true, false, 1, 100, 20);
		map.AddTBString(MapDtlAttr.Name, null, "名称", true, false, 1, 200, 20);
		map.AddTBString(MapDtlAttr.Alias, null, "别名", true, false, 0, 100, 20, false);
		map.SetHelperAlert(MapDtlAttr.Alias, "用于Excel表单有效.");

		map.AddTBString(MapDtlAttr.FK_MapData, null, "表单ID", true, true, 0, 100, 20);
		map.AddTBString(MapDtlAttr.PTable, null, "存储表", true, false, 0, 200, 20);
		map.SetHelperAlert(MapDtlAttr.PTable, "默认与编号为同一个存储表.");

		map.AddTBString(MapDtlAttr.FEBD, null, "事件类实体类", true, true, 0, 100, 20, false);

		map.AddDDLSysEnum(MapDtlAttr.Model, 0, "工作模式", true, true, MapDtlAttr.Model, "@0=普通@1=固定行");
		map.AddDDLSysEnum(MapDtlAttr.DtlVer, 0, "使用版本", true, true, MapDtlAttr.DtlVer, "@0=2017传统版@1=2019EasyUI版本");


			//map.AddTBString(MapDtlAttr.ImpFixTreeSql, null, "固定列树形SQL", true, false, 0, 500, 20);
			//map.AddTBString(MapDtlAttr.ImpFixDataSql, null, "固定列数据SQL", true, false, 0, 500, 20);

			//map.AddTBInt(MapDtlAttr.RowsOfList, 6, "初始化行数", true, false);
			//map.SetHelperAlert(MapDtlAttr.RowsOfList, "对第1个版本有效.");

		map.AddBoolean(MapDtlAttr.IsEnableGroupField, false, "是否启用分组字段", true, true);

		map.AddBoolean(MapDtlAttr.IsShowSum, true, "是否显示合计？", true, true);
		map.AddBoolean(MapDtlAttr.IsShowIdx, true, "是否显示序号？", true, true);

		map.AddBoolean(MapDtlAttr.IsReadonly, false, "是否只读？", true, true);
		map.AddBoolean(MapDtlAttr.IsShowTitle, true, "是否显示标题？", true, true);
		map.AddBoolean(MapDtlAttr.IsView, true, "是否可见？", true, true);

		map.AddBoolean(MapDtlAttr.IsInsert, true, "是否可以插入行？", true, true);
		map.AddBoolean(MapDtlAttr.IsDelete, true, "是否可以删除行？", true, true);
		map.AddBoolean(MapDtlAttr.IsUpdate, true, "是否可以更新？", true, true);
		map.AddBoolean(MapDtlAttr.IsEnableAthM, false, "是否启用多附件", true, true);
		map.AddDDLSysEnum(MapDtlAttr.WhenOverSize, 0, "超出行数", true, true, MapDtlAttr.WhenOverSize, "@0=不处理@1=向下顺增行@2=次页显示");

			// 为浙商银行设置从表打开.翻译.
		map.AddDDLSysEnum(MapDtlAttr.ListShowModel, 0, "列表数据显示格式", true, true, MapDtlAttr.ListShowModel, "@0=表格@1=卡片");

		map.AddDDLSysEnum(MapDtlAttr.EditModel, 0, "编辑数据方式", true, true, MapDtlAttr.EditModel, "@0=表格模式@1=傻瓜表单@2=自由表单");
		map.SetHelperAlert(MapDtlAttr.EditModel, "格式为:第1种类型就要新建行,其他类型新建的时候弹出卡片.");

			//用于控制傻瓜表单.
		map.AddTBFloat(MapDtlAttr.H, 350, "高度", true, false);
		map.SetHelperAlert(MapDtlAttr.H, "对傻瓜表单有效");

			//移动端数据显示方式
		map.AddDDLSysEnum(MapDtlAttr.MobileShowModel, 0, "移动端数据显示方式", true, true, MapDtlAttr.MobileShowModel, "@0=新页面显示模式@1=列表模式");
		map.AddTBString(MapDtlAttr.MobileShowField, null, "移动端列表显示字段", true, false, 0, 100, 20, false);

			//map.AddTBFloat(MapDtlAttr.X, 5, "距左", false, false);
			//map.AddTBFloat(MapDtlAttr.Y, 5, "距上", false, false);



			//  map.AddTBFloat(MapDtlAttr.W, 200, "宽度", true, false);

			//map.AddTBFloat(MapDtlAttr.FrmW, 900, "表单宽度", true, true);
			//map.AddTBFloat(MapDtlAttr.FrmH, 1200, "表单高度", true, true);

			//对显示的结果要做一定的限制.
		map.AddTBString(MapDtlAttr.FilterSQLExp, null, "过滤数据SQL表达式", true, false, 0, 200, 20, true);
		map.SetHelperAlert(MapDtlAttr.FilterSQLExp, "格式为:WFState=1 过滤WFState=1的数据");

			//列自动计算表达式.
			//map.AddTBString(MapDtlAttr.ColAutoExp, null, "列自动计算", true, false, 0, 200, 20, true);
			//map.SetHelperAlert(MapDtlAttr.ColAutoExp, "格式为:@XiaoJi:Sum@NingLing:Avg 要对小计求合计,对年龄求平均数.不配置不显示.");

			//要显示的列.
		map.AddTBString(MapDtlAttr.ShowCols, null, "显示的列", true, false, 0, 500, 20, true);
		map.SetHelperAlert(MapDtlAttr.ShowCols, "默认为空,全部显示,如果配置了就按照配置的计算,格式为:field1,field2");

		map.AddTBString(MapDtlAttr.GUID, null, "GUID", false, false, 0, 128, 20);

			///#endregion 基础信息.


			///#region 导入导出填充.
			// 2014-07-17 for xinchang bank.
		map.AddBoolean(MapDtlAttr.IsExp, true, "是否可以导出？(导出到Excel,Txt,html类型文件.)", true, true);

			//导入模式.
		map.AddDDLSysEnum(MapDtlAttr.ImpModel, 0, "导入方式", true, true, MapDtlAttr.ImpModel, "@0=不导入@1=按配置模式导入@2=按照xls文件模版导入");

		String strs = "如果是按照xls导入,请做一个从表ID.xls的模版文件放在:/DataUser/DtlTemplate/ 下面. 目前仅仅支持xls文件.";
		map.SetHelperAlert(MapDtlAttr.ImpModel, strs);

		map.AddTBStringDoc(MapDtlAttr.ImpSQLInit, null, "初始化SQL(初始化表格的时候的SQL数据,可以为空)", true, false, true);
		map.AddTBStringDoc(MapDtlAttr.ImpSQLSearch, null, "查询SQL(SQL里必须包含@Key关键字.)", true, false, true);
		map.AddTBStringDoc(MapDtlAttr.ImpSQLFullOneRow, null, "数据填充一行数据的SQL(必须包含@Key关键字,为选择的主键)", true, false, true);
		map.AddTBString(MapDtlAttr.ImpSQLNames, null, "列的中文名称", true, false, 0, 900, 20, true);
		map.AddBoolean(MapDtlAttr.IsImp, false, "是否可以导出？", true, true);
			///#endregion 导入导出填充.


			///#region 多表头.
			//MTR 多表头列.
//		map.AddTBStringDoc(MapDtlAttr.MTR, null, "请书写html标记,以《TR》开头，以《/TR》结尾。", true, false, true);

			///#endregion 多表头.


			///#region 超链接.
		map.AddBoolean(MapDtlAttr.IsEnableLink, false, "是否启用超链接", true, true);
		map.AddTBString(MapDtlAttr.LinkLabel, "", "超连接标签", true, false, 0, 50, 100);
		map.AddTBString(MapDtlAttr.LinkTarget, null, "连接目标", true, false, 0, 10, 100);
		map.AddTBString(MapDtlAttr.LinkUrl, null, "连接URL", true, false, 0, 200, 200, true);

			///#endregion 多表头.


			///#region 工作流相关.
			//add 2014-02-21.
		map.AddBoolean(MapDtlAttr.IsCopyNDData, true, "是否允许copy节点数据", true, true);
		map.AddTBInt(MapDtlAttr.FK_Node, 0, "节点(用户独立表单权限控制)", false, false);
		map.AddBoolean(MapDtlAttr.IsHLDtl, false, "是否是合流汇总", true, true);
		String sql = "SELECT KeyOfEn as No, Name FROM Sys_MapAttr WHERE FK_MapData='@No' AND  ( (MyDataType =1 and UIVisible=1 ) or (UIContralType=1))";
		map.AddDDLSQL(MapDtlAttr.SubThreadWorker, null, "子线程处理人字段", sql, true);
		map.AddBoolean(MapDtlAttr.IsEnablePass, false, "是否启用通过审核功能?", true, true);
		map.AddDDLSysEnum(MapDtlAttr.DtlOpenType, 1, "数据开放类型", true, true, MapDtlAttr.DtlOpenType, "@0=操作员@1=WorkID-流程ID@2=FID-干流程ID@3=PWorkID-父流程WorkID");

			///#endregion 工作流相关.


			///#region 相关方法.
		RefMethod rm = new RefMethod();
		rm = new RefMethod();
		rm.Title = "隐藏字段"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".HidAttr";
		rm.Icon = "../Img/Setting.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "生成英文字段"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".GenerAttrs";
		rm.refMethodType = RefMethodType.Func;
		rm.Warning = "";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "导入其它表/视图字段"; // "设计表单";
		rm.Warning = "导入后系统不会自动刷新，请手工刷新。";
		rm.ClassMethodName = this.toString() + ".ImpFields";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "导入其从表字段"; // "设计表单";
		rm.Warning = "导入后系统不会自动刷新，请手工刷新。";
		rm.ClassMethodName = this.toString() + ".ImpFromDtlID";
		rm.getHisAttrs().AddTBString("ID", null, "请输入要导入的从表ID", true, false, 0, 100, 100);
		rm.refMethodType = RefMethodType.Func;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设计傻瓜表单"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DFoolFrm";
		rm.Icon = "../Img/Setting.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设计自由表单"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DFreeFrm";
		rm.Icon = "../Img/Setting.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "从表附件属性"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".OpenAthAttr";
		rm.Icon = "../Img/AttachmentM.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

			///#endregion 相关方法.


			///#region 实验中的功能.
		rm = new RefMethod();
		rm.GroupName = "实验中的功能";
		rm.Title = "列自动计算"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".ColAutoExp";
		rm.Icon = "../Img/Setting.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "实验中的功能";
		rm.Title = "数据导入"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DtlImp";
		rm.Icon = "../Img/Setting.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "实验中的功能";
		rm.Title = "数据导入v2019"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DtlImpV2019";
		rm.Icon = "../Img/Setting.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.GroupName = "实验中的功能";
		rm.Title = "事件"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoAction";
		rm.Icon = "../Img/Setting.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

			///#endregion 实验中的功能.

		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterInsertUpdateAction();
	}
	/** 
	 导入其他从表字段
	 
	 @return 
	 * @throws Exception 
	*/
	public final String ImpFromDtlID(String dtlId) throws Exception
	{
		MapDtl dtl = new MapDtl();
		dtl.setNo(dtlId);
		if (dtl.RetrieveFromDBSources() == 0)
		{
			return "err@" + dtlId + "输入错误.";
		}


		MapDtl dtlOfThis = new MapDtl(this.getNo());
		dtlOfThis.Copy(dtl);
		dtlOfThis.setFK_MapData(this.getFK_MapData());
		dtlOfThis.Update();


		//删除当前从表Attrs.
		MapAttrs attrs = new MapAttrs();
		attrs.Delete(MapAttrAttr.FK_MapData, this.getNo());

		//查询出来要导入的.
		attrs.Retrieve(MapAttrAttr.FK_MapData, dtlId);

		//执行字段导入.
		for (MapAttr item : attrs.ToJavaList())
		{
			item.setFK_MapData(this.getNo());
			item.Save();
		}



		//删除当前从表 exts .
		MapExts exts = new MapExts();
		exts.Delete(MapAttrAttr.FK_MapData, this.getNo());

		//查询出来要导入的.
		exts.Retrieve(MapAttrAttr.FK_MapData, dtlId);

		//执行字段导入.
		for (MapExt item : exts.ToJavaList())
		{
			item.setFK_MapData(this.getNo());
			item.Save();
		}

		return "导入成功.";
	}
	/** 
	 打开从表附件属性.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String OpenAthAttr() throws Exception
	{
		String url = "../../Comm/RefFunc/En.htm?EnName=BP.Sys.FrmUI.FrmAttachmentExt&PKVal=" + this.getNo()+ "_AthMDtl";
		return url;
	}

	/** 
	 导入
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DtlImp() throws Exception
	{
		String url = "../../Admin/FoolFormDesigner/DtlSetting/DtlImp.htm?FK_MapData=" + this.getNo()+ "&FromDtl=1&IsFirst=1&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID() + "&AppCenterDBType=" + BP.DA.DBAccess.getAppCenterDBType() + "&CustomerNo=" + SystemConfig.getCustomerNo();
		return url;
	}
	/** 
	 导入V2019
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DtlImpV2019() throws Exception
	{
		String url = "../../Admin/FoolFormDesigner/DtlSetting/DtlImp/Default.htm?FK_MapDtl=" + this.getNo()+ "&FromDtl=1";
		return url;
	}
	/** 
	 列自动计算
	 
	 @return 
	 * @throws Exception 
	*/
	public final String ColAutoExp() throws Exception
	{
		String url = "../../Admin/FoolFormDesigner/DtlSetting/ColAutoExp.htm?FK_MapData=" + this.getNo();
		return url;
	}
	/** 
	 导入其他表字段
	 
	 @return 
	 * @throws Exception 
	*/
	public final String ImpFields() throws Exception
	{

		String url = "../../Admin/FoolFormDesigner/ImpTableField.htm?FK_MapData=" + this.getNo()+ "&FromDtl=1&IsFirst=1&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID() + "&AppCenterDBType=" + DBAccess.getAppCenterDBType() + "&CustomerNo=" + SystemConfig.getCustomerNo();
		return url;
	}
	/** 
	 设计傻瓜表单
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DFoolFrm() throws Exception
	{
		String url = "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData=" + this.getNo()+ "&FromDtl=1&IsFirst=1&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID() + "&AppCenterDBType=" + DBAccess.getAppCenterDBType() + "&CustomerNo=" + SystemConfig.getCustomerNo();
		return url;
	}

	/** 
	 设计自由表单
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DFreeFrm() throws Exception
	{
		FrmLabs labs = new FrmLabs(this.getNo());
		if (labs.size() == 0)
		{
			//进行初始化控件位置及标签
			MapData md = new MapData();
			md.setNo(this.getNo());
			md.RetrieveFromDBSources();
			float maxEnd = 200;
			boolean isLeft = true;


				///#region 字段顺序调整
			MapAttrs mapAttrs = new MapAttrs(md.getNo());
			for (MapAttr attr : mapAttrs.ToJavaList())
			{
				if (attr.getUIVisible() == false)
				{
					continue;
				}

				//生成lab.
				FrmLab lab = null;
				if (isLeft == true)
				{
					maxEnd = maxEnd + 40;
					/* 是否是左边 */
					lab = new FrmLab();
					lab.setMyPK(BP.DA.DBAccess.GenerGUID());
					lab.setFK_MapData(attr.getFK_MapData());
					lab.setText(attr.getName());
					lab.setFontName("Arial");
					lab.setX(40);
					lab.setY(maxEnd);
					lab.Insert();

					attr.setX(lab.getX() + 80);
					attr.setY(maxEnd);
					attr.Update();
				}
				else
				{
					lab = new FrmLab();
					lab.setMyPK(BP.DA.DBAccess.GenerGUID());
					lab.setFK_MapData(attr.getFK_MapData());
					lab.setText(attr.getName());
					lab.setFontName("Arial");
					lab.setX(350);
					lab.setY(maxEnd);
					lab.Insert();

					attr.setX(lab.getX() + 80);
					attr.setY(maxEnd);
					attr.Update();
				}
				isLeft = !isLeft;
			}

				///#endregion


				///#region 明细表重置排序
			MapDtls mapDtls = new MapDtls(this.getNo());
			for (MapDtl dtl : mapDtls.ToJavaList())
			{
				maxEnd = maxEnd + 40;
				/* 是否是左边 */
				FrmLab lab = new FrmLab();
				lab.setMyPK(BP.DA.DBAccess.GenerGUID());
				lab.setFK_MapData(dtl.getFK_MapData());
				lab.setText(dtl.getName());
				lab.setFontName("Arial");
				lab.setX(40);
				lab.setY(maxEnd);
				lab.Insert();

				dtl.setX(lab.getX() + 80);
				dtl.setY(maxEnd);
				dtl.Update();
				maxEnd = maxEnd + dtl.getH();
			}

				///#endregion

			md.ResetMaxMinXY();
		}
		String url = "../../Admin/CCFormDesigner/FormDesigner.htm?FK_MapData=" + this.getNo()+ "&FromDtl=1&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID() + "&AppCenterDBType=" + DBAccess.getAppCenterDBType() + "&CustomerNo=" + SystemConfig.getCustomerNo();
		return url;
	}

	public final String GenerAttrs() throws Exception
	{
		String strs = "";
		MapAttrs attrs = new MapAttrs(this.getNo());
		for (MapAttr item : attrs.ToJavaList())
		{
			strs += "\t\n " + item.getKeyOfEn() + ",";
		}
		return strs;
	}

	/** 
	 高级设置
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoAction() throws Exception
	{
		return "../../Admin/FoolFormDesigner/ActionForDtl.htm?DoType=Edit&FK_MapData=" + this.getNo()+ "&t=" + DataType.getCurrentDataTime();
	}
	public final String HidAttr() throws Exception
	{
		return "../../Admin/FoolFormDesigner/HidAttr.htm?DoType=Edit&FK_MapData=" + this.getNo()+ "&t=" + DataType.getCurrentDataTime();
	}


		///#region 基本属性.
	public final float getX() throws Exception
	{
		return this.GetValFloatByKey(FrmImgAttr.X);
	}
	public final float getY() throws Exception
	{
		return this.GetValFloatByKey(FrmImgAttr.Y);
	}
	public final float getW() throws Exception
	{
		return this.GetValFloatByKey(FrmImgAttr.W);
	}
	public final float getH() throws Exception
	{
		return this.GetValFloatByKey(FrmImgAttr.H);
	}
	public final float getFrmW() throws Exception
	{
		return this.GetValFloatByKey(MapDtlAttr.FrmW);
	}
	public final float getFrmH() throws Exception
	{
		return this.GetValFloatByKey(MapDtlAttr.FrmH);
	}

		///#endregion 基本属性.


	/** 
	 初始化自定义字段属性
	 
	 @return 返回执行结果
	 * @throws Exception 
	*/
	public final String InitAttrsOfSelf() throws Exception
	{
		if (this.getFK_Node() == 0)
		{
			return "err@该从表属性不是自定义属性.";
		}

		if (this.getNo().contains("_" + this.getFK_Node()) == false)
		{
			return "err@该从表属性不是自定义属性.";
		}

		//求从表ID.
		String refDtl = this.getNo().replace("_" + this.getFK_Node(), "");

		//处理属性问题.
		MapAttrs attrs = new MapAttrs();
		attrs.Delete(MapAttrAttr.FK_MapData, this.getNo());
		attrs.Retrieve(MapAttrAttr.FK_MapData, refDtl);
		for (MapAttr attr : attrs.ToJavaList())
		{
			attr.setMyPK(this.getNo() + "_" + attr.getKeyOfEn());
			attr.setFK_MapData(this.getNo());
			attr.Insert();
		}

		//处理mapExt 的问题.
		MapExts exts = new MapExts();
		exts.Delete(MapAttrAttr.FK_MapData, this.getNo()); //先删除，后查询.
		exts.Retrieve(MapAttrAttr.FK_MapData, refDtl);
		MapExt mapExt = null;
		for (MapExt ext : exts.ToJavaList())
		{
			mapExt = new MapExt();
			mapExt = ext;
			mapExt.setMyPK(ext.getMyPK() + "_" + this.getFK_Node());
			mapExt.setFK_MapData(this.getNo());
			mapExt.Insert();
		}

		//处理附件问题
		/* 如果启用了多附件*/
		if (this.getIsEnableAthM() == true)
		{
			BP.Sys.FrmAttachment athDesc = new BP.Sys.FrmAttachment();
			//获取原始附件的属性

			athDesc.setMyPK(this.getNo() + "_AthMDtl");
			if (athDesc.RetrieveFromDBSources() == 0)
			{
				//获取原来附件的属性
				BP.Sys.FrmAttachment oldAthDesc = new BP.Sys.FrmAttachment();
				oldAthDesc.setMyPK(refDtl + "_AthMDtl");
				if (oldAthDesc.RetrieveFromDBSources() == 0)
				{
					return "原始从表的附件属性不存在，请联系管理员";
				}
				athDesc = oldAthDesc;
				athDesc.setMyPK(this.getNo() + "_AthMDtl");
				athDesc.setFK_MapData(this.getNo());
				athDesc.setNoOfObj("AthMDtl");
				athDesc.setName(this.getName());
				athDesc.DirectInsert();
				//增加分组
				GroupField group = new GroupField();
				group.setLab(athDesc.getName());
				group.setFrmID(this.getFK_MapData());
				group.setCtrlType("Ath");
				group.setCtrlID(athDesc.getMyPK());
				group.setIdx(10);
				group.Insert();
			}

			//判断是否有隐藏的AthNum 字段
			MapAttr attr = new MapAttr();
			attr.setMyPK(this.getNo() + "_AthNum");
			int count = attr.RetrieveFromDBSources();
			if (count == 0)
			{
				attr.setFK_MapData(this.getNo());
				attr.setKeyOfEn("AthNum");
				attr.setName("附件数量");
				attr.setDefVal("0");
				attr.setUIContralType(UIContralType.TB);
				attr.setMyDataType(DataType.AppInt);
				attr.setUIVisible(false);
				attr.setUIIsEnable(false);
				attr.DirectInsert();
			}


		}

		return "执行成功";
	}

	@Override
	protected boolean beforeUpdate() throws Exception
	{
		MapDtl dtl = new MapDtl(this.getNo());
		//启用审核
		dtl.setIsEnablePass(this.getIsEnablePass());
		//超链接
		dtl.setIsEnableLink(this.getIsEnableLink());
		dtl.setLinkLabel(this.getLinkLabel());
		dtl.setLinkUrl(this.getLinkUrl());
		dtl.setLinkTarget(this.getLinkTarget());
		dtl.Update();

		//判断是否启用多附件
		if (this.getIsEnableAthM() == true)
		{
			BP.Sys.FrmAttachment athDesc = new BP.Sys.FrmAttachment();
			athDesc.setMyPK(this.getNo() + "_AthMDtl");
			if (athDesc.RetrieveFromDBSources() == 0)
			{
				athDesc.setFK_MapData(this.getNo());
				athDesc.setNoOfObj("AthMDtl");
				athDesc.setName(this.getName());
				athDesc.DirectInsert();
				//增加分组
				GroupField group = new GroupField();
				group.setLab(athDesc.getName());
				group.setFrmID(this.getFK_MapData());
				group.setCtrlType("Ath");
				group.setCtrlID(athDesc.getMyPK());
				group.setIdx(10);
				group.Insert();
			}

			//判断是否有隐藏的AthNum 字段
			MapAttr attr = new MapAttr();
			attr.setMyPK(this.getNo() + "_AthNum");
			int count = attr.RetrieveFromDBSources();
			if (count == 0)
			{
				attr.setFK_MapData(this.getNo());
				attr.setKeyOfEn ("AthNum");
				attr.setName("附件数量");
				attr.setDefVal("0");
				attr.setUIContralType(UIContralType.TB);
				attr.setMyDataType(DataType.AppInt);
				attr.setUIVisible(false);
				attr.setUIIsEnable(false);
				attr.DirectInsert();

			}
		}


		//获得事件实体.

		FormEventBaseDtl febd = BP.Sys.Glo.GetFormDtlEventBaseByEnName(this.getNo());
		if (febd == null)
		{
			this.setFEBD("");
		}
		else
		{
			this.setFEBD(febd.toString());
		}



			///#region 检查填充的SQL是否符合要求.

			///#endregion

		//更新分组标签.  @fanleiwei. 代码有变化.
		BP.Sys.GroupField gf = new GroupField();
		int i = gf.Retrieve(GroupFieldAttr.CtrlType, "Dtl", GroupFieldAttr.CtrlID, this.getNo());
		if (i == 0 && this.getFK_Node() ==0)
		{
			gf.setCtrlID(this.getNo());
			gf.setCtrlType("Dtl");
			gf.setFrmID(this.getFK_MapData());
			gf.Insert();
		}

		if (i > 1)
		{
			gf.Delete();
			i = gf.Retrieve(GroupFieldAttr.CtrlType, "Dtl", GroupFieldAttr.CtrlID, this.getNo());
		}

		if (i == 1 && gf.getLab().equals(this.getName()) == false)
		{
			gf.setLab(this.getName());
			gf.Update();
		}

		return super.beforeUpdate();
	}

	@Override
	protected void afterDelete() throws Exception
	{
		MapDtl dtl = new MapDtl();
		dtl.setNo(this.getNo());
		dtl.Delete();

		//删除分组
		GroupFields gfs = new GroupFields();
		gfs.RetrieveByLike(GroupFieldAttr.CtrlID, this.getNo() + "%");
		gfs.Delete();

		//如果启用了附件也需要删除
		if (this.getIsEnableAthM() == true)
		{
			FrmAttachment ath = new FrmAttachment();
			ath.Delete(FrmAttachmentAttr.MyPK, this.getNo() + "_AthMDtl");
		}


		super.afterDelete();
	}
	/** 
	 获取个数
	 
	 @param workID
	 @return 
	 * @throws Exception 
	*/
	public final int GetCountByFK(int workID) throws Exception
	{
		return BP.DA.DBAccess.RunSQLReturnValInt("select COUNT(OID) from " + this.getPTable() + " WHERE WorkID=" + workID);
	}
	public final int GetCountByFK(String field, String val) throws Exception
	{
		return BP.DA.DBAccess.RunSQLReturnValInt("select COUNT(OID) from " + this.getPTable() + " WHERE " + field + "='" + val + "'");
	}
	public final int GetCountByFK(String field, long val) throws Exception
	{
		return BP.DA.DBAccess.RunSQLReturnValInt("select COUNT(OID) from " + this.getPTable() + " WHERE " + field + "=" + val);
	}
	public final int GetCountByFK(String f1, long val1, String f2, String val2) throws Exception
	{
		return BP.DA.DBAccess.RunSQLReturnValInt("SELECT COUNT(OID) from " + this.getPTable() + " WHERE " + f1 + "=" + val1 + " AND " + f2 + "='" + val2 + "'");
	}

		///#endregion
}
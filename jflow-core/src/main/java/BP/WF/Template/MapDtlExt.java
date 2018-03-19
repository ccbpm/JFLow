package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.DotNetToJavaStringHelper;
import BP.WF.Glo;
import BP.Web.WebUser;

/** 
 明细
 
*/
public class MapDtlExt extends EntityNoName
{

		///#region 导入导出属性.
	/** 
	 用户访问.
	 
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForAppAdmin();
		uac.IsInsert = false;
		return uac;
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
	 是否启用选择数据项目导入？
	 
	*/
	public final int getImpModel()
	{
		return this.GetValIntByKey(MapDtlAttr.ImpModel);
	}
	public final void setImpModel(boolean value)
	{
		this.SetValByKey(MapDtlAttr.ImpModel, value);
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

		///#endregion


		///#region 基本设置
	/** 
	 工作模式
	 
	*/
	public final DtlModel getDtlModel()
	{
		return DtlModel.forValue(this.GetValIntByKey(MapDtlAttr.Model));
	}
	public final void setDtlModel(DtlModel value)
	{
		this.SetValByKey(MapDtlAttr.Model, value);
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

		///#endregion 基本设置


		///#region 参数属性
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
		return this.GetValBooleanByKey(MapDtlAttr.IsEnableLink,false);
	}
	public final void setIsEnableLink(boolean value)
	{
		this.SetValByKey(MapDtlAttr.IsEnableLink, value);
		this.SetPara(MapDtlAttr.IsEnableLink, value);
	}
	public final String getLinkLabel()
	{
		String s= this.GetValStrByKey(MapDtlAttr.LinkLabel);
		if (DotNetToJavaStringHelper.isNullOrEmpty(s))
		{
			return "详细";
		}
		return s;
	}
	public final void setLinkLabel(String value)
	{
		this.SetValByKey(MapDtlAttr.LinkLabel, value);
		this.SetPara(MapDtlAttr.LinkLabel, value);
	}
	public final String getLinkUrl()
	{
		String s = this.GetValStrByKey(MapDtlAttr.LinkUrl);
		if (DotNetToJavaStringHelper.isNullOrEmpty(s))
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
		this.SetValByKey(MapDtlAttr.LinkUrl, val);
		this.SetPara(MapDtlAttr.LinkUrl, val);
	}
	public final String getLinkTarget()
	{
		String s = this.GetValStrByKey(MapDtlAttr.LinkTarget);
		if (DotNetToJavaStringHelper.isNullOrEmpty(s))
		{
			return "_blank";
		}
		return s;
	}
	public final void setLinkTarget(String value)
	{
		this.SetValByKey(MapDtlAttr.LinkTarget, value);
		this.SetPara(MapDtlAttr.LinkTarget, value);
	}
	/** 
	 子线程处理人字段(用于分流节点的明细表分配子线程任务).
	 
	*/
	public final String getSubThreadWorker()
	{
		String s = this.GetParaString(MapDtlAttr.SubThreadWorker);
		if (DotNetToJavaStringHelper.isNullOrEmpty(s))
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
		if (DotNetToJavaStringHelper.isNullOrEmpty(s))
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
	 
	*/
	public final MapFrames getMapFrames()
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
	 分组字段
	 
	*/
	public final GroupFields getGroupFields_del()
	{
		Object tempVar = this.GetRefObject("GroupFields");
		GroupFields obj = (GroupFields)((tempVar instanceof GroupFields) ? tempVar : null);
		if (obj == null)
		{
			obj = new GroupFields(this.getNo());
			this.SetRefObject("GroupFields", obj);
		}
		return obj;
	}
	/** 
	 逻辑扩展
	 
	*/
	public final MapExts getMapExts()
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
	 
	*/
	public final FrmEvents getFrmEvents()
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
	 一对多
	 
	*/
	public final MapM2Ms getMapM2Ms()
	{
		Object tempVar = this.GetRefObject("MapM2Ms");
		MapM2Ms obj = (MapM2Ms)((tempVar instanceof MapM2Ms) ? tempVar : null);
		if (obj == null)
		{
			obj = new MapM2Ms(this.getNo());
			this.SetRefObject("MapM2Ms", obj);
		}
		return obj;
	}
	/** 
	 从表
	 
	*/
	public final MapDtls getMapDtls()
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
	 
	*/
	public final FrmLinks getFrmLinks()
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
	 
	*/
	public final FrmBtns getFrmBtns()
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
	 
	*/
	public final FrmEles getFrmEles()
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
	 
	*/
	public final FrmLines getFrmLines()
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
	 
	*/
	public final FrmLabs getFrmLabs()
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
	 
	*/
	public final FrmImgs getFrmImgs()
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
	 
	*/
	public final FrmAttachments getFrmAttachments()
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
	 
	*/
	public final FrmImgAths getFrmImgAths()
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
	 
	*/
	public final FrmRBs getFrmRBs()
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
	 
	*/
	public final MapAttrs getMapAttrs()
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
		this.SetValByKey(MapDtlAttr.DtlShowModel, value);
	}
	/** 
	 
	 
	*/
	public final WhenOverSize getHisWhenOverSize()
	{
		return WhenOverSize.forValue(this.GetValIntByKey(MapDtlAttr.WhenOverSize));
	}
	public final void setHisWhenOverSize(WhenOverSize value)
	{
		this.SetValByKey(MapDtlAttr.WhenOverSize, value);
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
		this.SetValByKey(MapDtlAttr.DtlOpenType, value);
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


		
	/** 
	 明细
	 
	*/
	public MapDtlExt()
	{
	}
	public MapDtlExt(String mypk)
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
		//map.Java_SetEnType(EnType.Sys);
 //System.out.println(EnType.Sys);
		
		//  #region 基础信息.

          map.AddTBStringPK(MapDtlAttr.No, null, "编号", true, false, 1, 100, 20);
          map.AddTBString(MapDtlAttr.Name, null, "名称", true, false, 1, 200, 20);
          map.AddTBString(MapDtlAttr.Alias, null, "别名", true, false, 0, 100, 20, false);
          map.AddTBString(MapDtlAttr.FK_MapData, null, "表单ID", true, true, 0, 100, 20);
          map.AddTBString(MapDtlAttr.PTable, null, "存储表", true, false, 0, 200, 20);
          map.AddTBString(MapDtlAttr.FEBD, null, "事件类实体类", true, true, 0, 100, 20, false);

          map.AddDDLSysEnum(MapDtlAttr.Model, 0, "工作模式", true, true,MapDtlAttr.Model, "@0=普通@1=固定行");

          //map.AddTBString(MapDtlAttr.ImpFixTreeSql, null, "固定列树形SQL", true, false, 0, 500, 20);
          //map.AddTBString(MapDtlAttr.ImpFixDataSql, null, "固定列数据SQL", true, false, 0, 500, 20);

          map.AddTBInt(MapDtlAttr.RowsOfList, 6, "初始化行数", true, false);

          map.AddBoolean(MapDtlAttr.IsEnableGroupField, false, "是否启用分组字段", true, true);

          map.AddBoolean(MapDtlAttr.IsShowSum, true, "是否显示合计？", true, true);
          map.AddBoolean(MapDtlAttr.IsShowIdx, true, "是否显示序号？", true, true);

          map.AddBoolean(MapDtlAttr.IsReadonly, false, "是否只读？", true, true);
          map.AddBoolean(MapDtlAttr.IsShowTitle, true, "是否显示标题？", true, true);
          map.AddBoolean(MapDtlAttr.IsView, true, "是否可见？", true, true);


          map.AddBoolean(MapDtlAttr.IsInsert, true, "是否可以插入行？", true, true);
          map.AddBoolean(MapDtlAttr.IsDelete, true, "是否可以删除行？", true, true);
          map.AddBoolean(MapDtlAttr.IsUpdate, true, "是否可以更新？", true, true);

          map.AddBoolean(MapDtlAttr.IsEnablePass, false, "是否启用通过审核功能?", true, true);
          map.AddBoolean(MapDtlAttr.IsEnableAthM, false, "是否启用多附件", true, true);

          //map.AddBoolean(MapDtlAttr.IsEnableM2M, false, "是否启用M2M", true, true);
          //map.AddBoolean(MapDtlAttr.IsEnableM2MM, false, "是否启用M2M2", true, true);


          map.AddDDLSysEnum(MapDtlAttr.WhenOverSize, 0, "超出行数", true, true,MapDtlAttr.WhenOverSize, "@0=不处理@1=向下顺增行@2=次页显示");

          // 为浙商银行设置从表打开. @于庆海翻译.
          map.AddDDLSysEnum(MapDtlAttr.ListShowModel, 0, "列表数据显示格式", true, true,MapDtlAttr.ListShowModel, "@0=表格@1=卡片");
          map.AddDDLSysEnum(MapDtlAttr.EditModel, 0, "行数据显示格式", true, true,MapDtlAttr.EditModel, "@0=无@1=傻瓜表单@2=自由表单");
          map.AddDDLSysEnum(MapDtlAttr.DtlOpenType, 1, "数据开放类型", true, true,MapDtlAttr.DtlOpenType, "@0=操作员@1=工作ID@2=流程ID");


          //map.AddTBFloat(MapDtlAttr.X, 5, "距左", false, false);
          //map.AddTBFloat(MapDtlAttr.Y, 5, "距上", false, false);

          //map.AddTBFloat(MapDtlAttr.H, 150, "高度", false, false);
          //map.AddTBFloat(MapDtlAttr.W, 200, "宽度", false, false);

          //map.AddTBFloat(MapDtlAttr.FrmW, 900, "表单宽度", true, true);
          //map.AddTBFloat(MapDtlAttr.FrmH, 1200, "表单高度", true, true);

          //对显示的结果要做一定的限制.
          map.AddTBString(MapDtlAttr.FilterSQLExp, null, "过滤数据SQL表达式", true, false, 0, 200, 20,true);
          map.SetHelperAlert(MapDtlAttr.FilterSQLExp, "格式为:WFState=1 过滤WFState=1的数据");

          
          //要显示的列.
          map.AddTBString(MapDtlAttr.ShowCols, null, "显示的列", true, false, 0, 500, 20, true);
          map.SetHelperAlert(MapDtlAttr.ShowCols, "默认为空,全部显示,如果配置了就按照配置的计算,格式为:field1,field2");
        
          
          //列自动计算表达式.
          map.AddTBString(MapDtlAttr.ColAutoExp, null, "列自动计算", true, false, 0, 200, 20, true);
          map.SetHelperAlert(MapDtlAttr.ColAutoExp, "格式为:@XiaoJi:Sum@NingLing:Avg 要对小计求合计,对年龄求平均数.不配置不显示.");

         
          map.AddTBString(FrmBtnAttr.GUID, null, "GUID", false, false, 0, 128, 20);
         // #endregion 基础信息.

          //#region 导入导出填充.
          // 2014-07-17 for xinchang bank.

          map.AddBoolean(MapDtlAttr.IsExp, true, "是否可以导出？(导出到Excel,Txt,html类型文件.)", true, true, true);
          
          //导入模式.
          map.AddDDLSysEnum(MapDtlAttr.ImpModel, 0, "导入方式", true, true, MapDtlAttr.ImpModel,
              "@0=不导入@1=按SQL设置导入@2=按JSON模式导入@3=按照xls文件模版导入");
          map.SetHelperAlert(MapDtlAttr.EditModel, "您需要在相关功能里设置相对应的导入模式设置.");
           
          map.AddTBStringDoc(MapDtlAttr.ImpSQLInit, null, "初始化SQL(初始化表格的时候的SQL数据,可以为空)", true, false, true);
          map.AddTBStringDoc(MapDtlAttr.ImpSQLSearch, null, "查询SQL(SQL里必须包含@Key关键字.)", true, false,true);
          map.AddTBStringDoc(MapDtlAttr.ImpSQLFullOneRow, null, "数据填充一行数据的SQL(必须包含@Key关键字,为选择的主键)", true, false,true);

        //  #endregion 导入导出填充.

          //#region 多表头.
          //MTR 多表头列.
          map.AddTBStringDoc(MapDtlAttr.MTR, null, "请书写html标记,以《TR》开头，以《/TR》结尾。", true, false, true);
          //#endregion 多表头.

          //#region 超链接.
          map.AddBoolean(MapDtlAttr.IsEnableLink, false, "是否启用超链接", true, true);
          map.AddTBString(MapDtlAttr.LinkLabel, "", "超连接标签", true, false, 0, 50, 100);
          map.AddTBString(MapDtlAttr.LinkTarget, null, "连接目标", true, false, 0, 10, 100);
          map.AddTBString(MapDtlAttr.LinkUrl, null, "连接URL", true, false, 0, 200, 200, true);
        //  #region 工作流相关.
          //add 2014-02-21.
          map.AddTBInt(MapDtlAttr.FK_Node, 0, "节点(用户独立表单权限控制)", false, false);
          map.AddBoolean(MapDtlAttr.IsCopyNDData, true, "是否允许copy节点数据", false, false);
          map.AddBoolean(MapDtlAttr.IsHLDtl, false, "是否是合流汇总", false, false);

          String sql = "SELECT KeyOfEn as No, Name FROM Sys_MapAttr WHERE FK_MapData='@No' AND  ( (MyDataType =1 and UIVisible=1 ) or (UIContralType=1))";
          map.AddDDLSQL(MapDtlAttr.SubThreadWorker, null, "子线程处理人字段", sql, true);
          map.AddBoolean(MapDtlAttr.IsEnablePass, false, "是否启用通过审核功能?", true, true);
          map.AddDDLSysEnum(MapDtlAttr.DtlOpenType, 1, "数据开放类型", true, true, MapDtlAttr.DtlOpenType, "@0=操作员@1=工作ID@2=流程ID");
          //#endregion 工作流相关.
          
		

		RefMethod rm = new RefMethod();
//		rm.Title = "高级设置"; // "设计表单";
//		rm.ClassMethodName = this.toString() + ".DoAdvSetting";
//		rm.Icon = "/WF/Img/Setting.png";
//		rm.Visable = true;
//		rm.refMethodType = RefMethodType.RightFrameOpen;
//		rm.Target = "_blank";
//		map.AddRefMethod(rm);

		 

//		rm = new RefMethod();
//		rm.Title = "事件"; // "设计表单";
//		rm.ClassMethodName = this.toString() + ".DoAction";
//		rm.Icon = "/WF/Img/Setting.png";
//		rm.Visable = true;
//		rm.refMethodType = RefMethodType.RightFrameOpen;
//		rm.Target = "_blank";
//		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "隐藏字段"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".HidAttr";
		rm.Icon = "/WF/Img/Setting.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);
		
		rm = new RefMethod();
        rm.Title = "生成英文字段"; // "设计表单";
        rm.ClassMethodName = this.toString() + ".GenerAttrs";
        rm.refMethodType = RefMethodType.Func;
        map.AddRefMethod(rm);


        rm = new RefMethod();
        rm.Title = "导入其他表字段"; // "设计表单";
        rm.Warning = "导入后系统不会自动刷新，请手工刷新。";
        rm.ClassMethodName = this.toString() + ".ImpFields";
        rm.refMethodType = RefMethodType.LinkeWinOpen;
        map.AddRefMethod(rm);

        rm = new RefMethod();
        rm.Title = "设计傻瓜表单"; // "设计表单";
        rm.ClassMethodName = this.toString() + ".DFoolFrm";
        rm.Icon = "/WF/Img/Setting.png";
        rm.Visable = true;
        rm.refMethodType = RefMethodType.LinkeWinOpen;
        rm.Target = "_blank";
        map.AddRefMethod(rm);
        
        rm = new RefMethod();
        rm.Title = "设计自由表单"; // "设计表单";
        rm.ClassMethodName = this.toString() + ".DFreeFrm";
        rm.Icon = "/WF/Img/Setting.png";
        rm.Visable = true;
        rm.refMethodType = RefMethodType.LinkeWinOpen;
        rm.Target = "_blank";
        map.AddRefMethod(rm);
        
        
        rm = new RefMethod();
        rm.GroupName = "实验中的功能";
        rm.Title = "列自动计算"; // "设计表单";
        rm.ClassMethodName = this.toString() + ".ColAutoExp";
        rm.Icon = "/WF/Img/Setting.png";
        rm.Visable = true;
        rm.refMethodType = RefMethodType.RightFrameOpen;
        rm.Target = "_blank";
        map.AddRefMethod(rm);
        

        rm = new RefMethod();
        rm.GroupName = "实验中的功能";
        rm.Title = "数据导入"; // "设计表单";
        rm.ClassMethodName = this.toString() + ".DtlImp";
        rm.Icon = "/WF/Img/Setting.png";
        rm.Visable = true;
        rm.refMethodType = RefMethodType.RightFrameOpen;
        rm.Target = "_blank";
        map.AddRefMethod(rm);



        rm = new RefMethod();
        rm.GroupName = "实验中的功能";
        rm.Title = "事件"; // "设计表单";
        rm.ClassMethodName = this.toString() + ".DoAction";
        rm.Icon = "/WF/Img/Setting.png";
        rm.Visable = true;
        rm.refMethodType = RefMethodType.RightFrameOpen;
        rm.Target = "_blank";
        map.AddRefMethod(rm);

        rm = new RefMethod();
        rm.GroupName = "实验中的功能";
        rm.Title = "高级设置";
        rm.ClassMethodName = this.toString() + ".DoAdvSetting";
        rm.Icon = "/WF/Img/Setting.png";
        rm.Visable = true;
        rm.refMethodType = RefMethodType.RightFrameOpen;
        rm.Target = "_blank";
        map.AddRefMethod(rm);
        

		this.set_enMap(map);
		return this.get_enMap();
	}
	
	/** 列自动计算
	 
	 @return 
	 */
	public final String DtlImp()
	{
		String url = "../../Admin/FoolFormDesigner/DtlSetting/DtlImp.htm?FK_MapData=" + this.getNo() + "&FromDtl=1&IsFirst=1&UserNo=" + BP.Web.WebUser.getNo() + "&SID=" + WebUser.getSID() + "&AppCenterDBType=" + BP.DA.DBAccess.getAppCenterDBType() + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
		return url;
	}
	 
    public String ColAutoExp()
    {
    	String url = "../../Admin/FoolFormDesigner/DtlSetting/ColAutoExp.htm?FK_MapData=" + this.getNo() + "&FromDtl=1&IsFirst=1&UserNo=" + BP.Web.WebUser.getNo() + "&SID=" + BP.Web.WebUser.getSID() + "&AppCenterDBType=" + BP.DA.DBAccess.getAppCenterDBType() + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
        return url;
    }
	
	/** 
	 导入其他表字段
	 
	 @return 
*/
	public final String ImpFields()
	{
		String url = "../../Admin/FoolFormDesigner/ImpTableField.htm?FK_MapData=" + this.getNo() + "&FromDtl=1&IsFirst=1&UserNo=" + BP.Web.WebUser.getNo() + "&SID=" + BP.Web.WebUser.getSID() + "&AppCenterDBType=" + BP.DA.DBAccess.getAppCenterDBType() + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
		return url;
	}
	
	 /** 
	 设计傻瓜表单
	 
	 @return 
*/
	public final String DFoolFrm()
	{
		String url = "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData=" + this.getNo() + "&FromDtl=1&IsFirst=1&UserNo=" + BP.Web.WebUser.getNo() + "&SID=" + BP.Web.WebUser.getSID() + "&AppCenterDBType=" + BP.DA.DBAccess.getAppCenterDBType() + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
		return url;
	}
	
	/// <summary>
    /// 设计自由表单
    /// </summary>
    /// <returns></returns>
    public final String DFreeFrm()
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

            //#region 字段顺序调整
            MapAttrs mapAttrs = new MapAttrs(md.getNo());
            for (MapAttr attr : mapAttrs.ToJavaList())
            {
                if (attr.getUIVisible() == false)
                    continue;

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

                    attr.setX(lab.getX()+80);
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
            //#endregion

            //#region 明细表重置排序
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
            //#endregion

            md.ResetMaxMinXY();
        }
        String url = "../../Admin/CCFormDesigner/FormDesigner.htm?FK_MapData=" + this.getNo() + "&FromDtl=1&UserNo=" + BP.Web.WebUser.getNo() + "&SID=" + WebUser.getSID() + "&AppCenterDBType=" + BP.DA.DBAccess.getAppCenterDBType() + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
        return url;
    }
    
	//生成英文字段
	  public final String GenerAttrs()
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
	*/
	public final String DoAdvSetting()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/MapDtl.htm?DoType=Edit&FK_MapDtl=" + this.getNo() + "&t=" + DataType.getCurrentDataTime();
	}
	/** 
	 高级设置
	 
	 @return 
	*/
	public final String DoAction()
	{
		//return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/Action.jsp?DoType=Edit&FK_MapData=" + this.getNo() + "&t=" + DataType.getCurrentDataTime();
		return "../../Admin/FoolFormDesigner/Action.htm?DoType=Edit&FK_MapData=" + this.getNo() + "&t=" + DataType.getCurrentDataTime();
	}
	public final String HidAttr()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/HidAttr.htm?DoType=Edit&FK_MapData=" + this.getNo() + "&t=" + DataType.getCurrentDataTime();
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
		return this.GetValFloatByKey(MapDtlAttr.FrmW);
	}
	public final float getFrmH()
	{
		return this.GetValFloatByKey(MapDtlAttr.FrmH);
	}

		///#endregion 基本属性.

	@Override
	protected boolean beforeUpdate()
	{
		MapDtl dtl = new MapDtl(this.getNo());
		dtl.setIsEnablePass(this.getIsEnableAthM());
		
		//超链接
        dtl.setIsEnableLink(this.getIsEnableLink() );
        dtl.setLinkLabel( this.getLinkLabel());
        dtl.setLinkUrl( this.getLinkUrl());
        dtl.setLinkTarget( this.getLinkTarget());        
		dtl.Update();
		
		
        //更新分组标签.
        BP.Sys.GroupField gf = new GroupField();
        int i=gf.Retrieve(GroupFieldAttr.CtrlType, "Dtl", GroupFieldAttr.CtrlID, this.getNo());
        if (i == 1 && gf.getLab().equals(this.getName()) == false)
        {
            gf.setLab(this.getName());
            gf.Update();
        }

		
		return super.beforeUpdate();
	}
	
	   @Override
	   protected void afterDelete()
			{
				MapDtl dtl = new MapDtl();
				dtl.setNo(this.getNo());
				dtl.Delete();

				super.afterDelete();
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
}
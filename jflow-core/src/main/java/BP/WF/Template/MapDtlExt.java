package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 明细
*/
public class MapDtlExt extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
	 填充数据一行数据
	*/
	public final String getImpSQLFullOneRow()
	{
		return this.GetValStringByKey(MapDtlAttr.ImpSQLFullOneRow).replace("~", "'");
	}
	public final void setImpSQLFullOneRow(String value)
	{
		this.SetValByKey(MapDtlAttr.ImpSQLFullOneRow, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本设置
	/** 
	 工作模式
	*/
	public final DtlModel getDtlModel()
	{
		return (DtlModel)this.GetValIntByKey(MapDtlAttr.Model);
	}
	public final void setDtlModel(DtlModel value)
	{
		this.SetValByKey(MapDtlAttr.Model, value.getValue());
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 基本设置

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 参数属性
	/** 
	 记录增加模式
	*/
	public final DtlAddRecModel getDtlAddRecModel()
	{
		return (DtlAddRecModel)this.GetParaInt(MapDtlAttr.DtlAddRecModel);
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
		return (DtlSaveModel)this.GetParaInt(MapDtlAttr.DtlSaveModel);
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
		return this.GetValBooleanByKey(MapDtlAttr.IsEnableLink, false);
	}
	public final void setIsEnableLink(boolean value)
	{
		this.SetValByKey(MapDtlAttr.IsEnableLink, value);
		this.SetPara(MapDtlAttr.IsEnableLink, value);
	}
	public final String getLinkLabel()
	{
		String s = this.GetValStrByKey(MapDtlAttr.LinkLabel);
		if (DataType.IsNullOrEmpty(s))
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
		if (DataType.IsNullOrEmpty(s))
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
		if (DataType.IsNullOrEmpty(s))
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
		if (DataType.IsNullOrEmpty(s))
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
		if (DataType.IsNullOrEmpty(s))
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 参数属性

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 外键属性
	/** 
	 框架
	*/
	public final MapFrames getMapFrames()
	{
		Object tempVar = this.GetRefObject("MapFrames");
		MapFrames obj = tempVar instanceof MapFrames ? (MapFrames)tempVar : null;
		if (obj == null)
		{
			obj = new MapFrames(this.No);
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
		GroupFields obj = tempVar instanceof GroupFields ? (GroupFields)tempVar : null;
		if (obj == null)
		{
			obj = new GroupFields(this.No);
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
		MapExts obj = tempVar instanceof MapExts ? (MapExts)tempVar : null;
		if (obj == null)
		{
			obj = new MapExts(this.No);
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
		FrmEvents obj = tempVar instanceof FrmEvents ? (FrmEvents)tempVar : null;
		if (obj == null)
		{
			obj = new FrmEvents(this.No);
			this.SetRefObject("FrmEvents", obj);
		}
		return obj;
	}
	/** 
	 从表
	*/
	public final MapDtls getMapDtls()
	{
		Object tempVar = this.GetRefObject("MapDtls");
		MapDtls obj = tempVar instanceof MapDtls ? (MapDtls)tempVar : null;
		if (obj == null)
		{
			obj = new MapDtls(this.No);
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
		FrmLinks obj = tempVar instanceof FrmLinks ? (FrmLinks)tempVar : null;
		if (obj == null)
		{
			obj = new FrmLinks(this.No);
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
		FrmBtns obj = tempVar instanceof FrmBtns ? (FrmBtns)tempVar : null;
		if (obj == null)
		{
			obj = new FrmBtns(this.No);
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
		FrmEles obj = tempVar instanceof FrmEles ? (FrmEles)tempVar : null;
		if (obj == null)
		{
			obj = new FrmEles(this.No);
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
		FrmLines obj = tempVar instanceof FrmLines ? (FrmLines)tempVar : null;
		if (obj == null)
		{
			obj = new FrmLines(this.No);
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
		FrmLabs obj = tempVar instanceof FrmLabs ? (FrmLabs)tempVar : null;
		if (obj == null)
		{
			obj = new FrmLabs(this.No);
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
		FrmImgs obj = tempVar instanceof FrmImgs ? (FrmImgs)tempVar : null;
		if (obj == null)
		{
			obj = new FrmImgs(this.No);
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
		FrmAttachments obj = tempVar instanceof FrmAttachments ? (FrmAttachments)tempVar : null;
		if (obj == null)
		{
			obj = new FrmAttachments(this.No);
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
		FrmImgAths obj = tempVar instanceof FrmImgAths ? (FrmImgAths)tempVar : null;
		if (obj == null)
		{
			obj = new FrmImgAths(this.No);
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
		FrmRBs obj = tempVar instanceof FrmRBs ? (FrmRBs)tempVar : null;
		if (obj == null)
		{
			obj = new FrmRBs(this.No);
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
		MapAttrs obj = tempVar instanceof MapAttrs ? (MapAttrs)tempVar : null;
		if (obj == null)
		{
			obj = new MapAttrs(this.No);
			this.SetRefObject("MapAttrs", obj);
		}
		return obj;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	public GEDtls HisGEDtls_temp = null;
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
		return (DtlOpenType)this.GetValIntByKey(MapDtlAttr.DtlOpenType);
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
	 事件类.
	*/
	public final String getFEBD()
	{
		return this.GetValStrByKey(MapDtlAttr.FEBD);
	}
	public final void setFEBD(String value)
	{
		this.SetValByKey(MapDtlAttr.FEBD, value);
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
			s = this.No;
			if (s.substring(0, 1).equals("0"))
			{
				return "T" + this.No;
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
				return "T" + this.No;
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
		String s = this.GetValStrByKey(MapDtlAttr.MTR);
		s = s.replace("《", "<");
		s = s.replace("》", ">");
		s = s.replace("‘", "'");
		return s;
	}
	public final void setMTR(String value)
	{
		String s = value;
		s = s.replace("<", "《");
		s = s.replace(">", "》");
		s = s.replace("'", "‘");
		this.SetValByKey(MapDtlAttr.MTR, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 明细
	*/
	public MapDtlExt()
	{
	}
	public MapDtlExt(String mypk)
	{
		this.No = mypk;
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


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基础信息.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 导入导出填充.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 多表头.
			//MTR 多表头列.
		map.AddTBStringDoc(MapDtlAttr.MTR, null, "请书写html标记,以《TR》开头，以《/TR》结尾。", true, false, true);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 多表头.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 超链接.
		map.AddBoolean(MapDtlAttr.IsEnableLink, false, "是否启用超链接", true, true);
		map.AddTBString(MapDtlAttr.LinkLabel, "", "超连接标签", true, false, 0, 50, 100);
		map.AddTBString(MapDtlAttr.LinkTarget, null, "连接目标", true, false, 0, 10, 100);
		map.AddTBString(MapDtlAttr.LinkUrl, null, "连接URL", true, false, 0, 200, 200, true);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 多表头.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 工作流相关.
			//add 2014-02-21.
		map.AddBoolean(MapDtlAttr.IsCopyNDData, true, "是否允许copy节点数据", true, false);
		map.AddTBInt(MapDtlAttr.FK_Node, 0, "节点(用户独立表单权限控制)", false, false);
		map.AddBoolean(MapDtlAttr.IsHLDtl, false, "是否是合流汇总", true, true);
		String sql = "SELECT KeyOfEn as No, Name FROM Sys_MapAttr WHERE FK_MapData='@No' AND  ( (MyDataType =1 and UIVisible=1 ) or (UIContralType=1))";
		map.AddDDLSQL(MapDtlAttr.SubThreadWorker, null, "子线程处理人字段", sql, true);
		map.AddBoolean(MapDtlAttr.IsEnablePass, false, "是否启用通过审核功能?", true, true);
		map.AddDDLSysEnum(MapDtlAttr.DtlOpenType, 1, "数据开放类型", true, true, MapDtlAttr.DtlOpenType, "@0=操作员@1=工作ID@2=流程ID@3=父流程WorkID");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 工作流相关.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 相关方法.
		RefMethod rm = new RefMethod();
		rm = new RefMethod();
		rm.Title = "隐藏字段"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".HidAttr";
		rm.Icon = "../Img/Setting.png";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "生成英文字段"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".GenerAttrs";
		rm.RefMethodType = RefMethodType.Func;
		rm.Warning = "";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "导入其它表/视图字段"; // "设计表单";
		rm.Warning = "导入后系统不会自动刷新，请手工刷新。";
		rm.ClassMethodName = this.toString() + ".ImpFields";
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "导入其从表字段"; // "设计表单";
		rm.Warning = "导入后系统不会自动刷新，请手工刷新。";
		rm.ClassMethodName = this.toString() + ".ImpFromDtlID";
		rm.getHisAttrs().AddTBString("ID", null, "请输入要导入的从表ID", true, false, 0, 100, 100);
		rm.RefMethodType = RefMethodType.Func;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设计傻瓜表单"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DFoolFrm";
		rm.Icon = "../Img/Setting.png";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设计自由表单"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DFreeFrm";
		rm.Icon = "../Img/Setting.png";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "从表附件属性"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".OpenAthAttr";
		rm.Icon = "../Img/AttachmentM.png";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 相关方法.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 实验中的功能.
		rm = new RefMethod();
		rm.GroupName = "实验中的功能";
		rm.Title = "列自动计算"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".ColAutoExp";
		rm.Icon = "../Img/Setting.png";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "实验中的功能";
		rm.Title = "数据导入"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DtlImp";
		rm.Icon = "../Img/Setting.png";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "实验中的功能";
		rm.Title = "数据导入v2019"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DtlImpV2019";
		rm.Icon = "../Img/Setting.png";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.GroupName = "实验中的功能";
		rm.Title = "事件"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoAction";
		rm.Icon = "../Img/Setting.png";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 实验中的功能.

		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected void afterInsertUpdateAction()
	{
		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterInsertUpdateAction();
	}
	/** 
	 导入其他从表字段
	 
	 @return 
	*/
	public final String ImpFromDtlID(String dtlId)
	{
		MapDtl dtl = new MapDtl();
		dtl.No = dtlId;
		if (dtl.RetrieveFromDBSources() == 0)
		{
			return "err@" + dtlId + "输入错误.";
		}


		MapDtl dtlOfThis = new MapDtl(this.No);
		dtlOfThis.Copy(dtl);
		dtlOfThis.FK_MapData = this.getFK_MapData();
		dtlOfThis.Update();


		//删除当前从表Attrs.
		MapAttrs attrs = new MapAttrs();
		attrs.Delete(MapAttrAttr.FK_MapData, this.No);

		//查询出来要导入的.
		attrs.Retrieve(MapAttrAttr.FK_MapData, dtlId);

		//执行字段导入.
		for (MapAttr item : attrs)
		{
			item.FK_MapData = this.No;
			item.Save();
		}



		//删除当前从表 exts .
		MapExts exts = new MapExts();
		exts.Delete(MapAttrAttr.FK_MapData, this.No);

		//查询出来要导入的.
		exts.Retrieve(MapAttrAttr.FK_MapData, dtlId);

		//执行字段导入.
		for (MapExt item : exts)
		{
			item.FK_MapData = this.No;
			item.Save();
		}

		return "导入成功.";
	}
	/** 
	 打开从表附件属性.
	 
	 @return 
	*/
	public final String OpenAthAttr()
	{
		String url = "../../Comm/RefFunc/En.htm?EnName=BP.Sys.FrmUI.FrmAttachmentExt&PKVal=" " + this.getNo()+ " "_AthMDtl";
		// string url = "../../Admin/FoolFormDesigner/DtlSetting/DtlImp.htm?FK_MapData=" " + this.getNo()+ " "&FromDtl=1&IsFirst=1&UserNo=" + WebUser.getNo() + "&SID=" + Web.WebUser.SID + "&AppCenterDBType=" + BP.DA.DBAccess.AppCenterDBType + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
		return url;
	}

	/** 
	 导入
	 
	 @return 
	*/
	public final String DtlImp()
	{
		String url = "../../Admin/FoolFormDesigner/DtlSetting/DtlImp.htm?FK_MapData=" " + this.getNo()+ " "&FromDtl=1&IsFirst=1&UserNo=" + WebUser.getNo() + "&SID=" + Web.WebUser.SID + "&AppCenterDBType=" + BP.DA.DBAccess.AppCenterDBType + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
	   // string url = "../../Admin/FoolFormDesigner/DtlSetting/DtlImp/Default.htm?FK_MapDtl=" " + this.getNo()+ " "&FromDtl=1";
		return url;
	}
	/** 
	 导入V2019
	 
	 @return 
	*/
	public final String DtlImpV2019()
	{
		//string url = "../../Admin/FoolFormDesigner/DtlSetting/DtlImp.htm?FK_MapData=" " + this.getNo()+ " "&FromDtl=1&IsFirst=1&UserNo=" + WebUser.getNo() + "&SID=" + Web.WebUser.SID + "&AppCenterDBType=" + BP.DA.DBAccess.AppCenterDBType + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
		String url = "../../Admin/FoolFormDesigner/DtlSetting/DtlImp/Default.htm?FK_MapDtl=" " + this.getNo()+ " "&FromDtl=1";
		return url;
	}
	/** 
	 列自动计算
	 
	 @return 
	*/
	public final String ColAutoExp()
	{
		String url = "../../Admin/FoolFormDesigner/DtlSetting/ColAutoExp.htm?FK_MapData=" + this.No;
		return url;
	}
	/** 
	 导入其他表字段
	 
	 @return 
	*/
	public final String ImpFields()
	{

		//  http://localhost:18272/WF/Admin/FoolFormDesigner/ImpTableField.htm?FK_MapData=CCFrm_CZBankBXDtl1&reset=true
		String url = "../../Admin/FoolFormDesigner/ImpTableField.htm?FK_MapData=" " + this.getNo()+ " "&FromDtl=1&IsFirst=1&UserNo=" + WebUser.getNo() + "&SID=" + Web.WebUser.SID + "&AppCenterDBType=" + BP.DA.DBAccess.AppCenterDBType + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
		return url;
	}
	/** 
	 设计傻瓜表单
	 
	 @return 
	*/
	public final String DFoolFrm()
	{
		String url = "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData=" " + this.getNo()+ " "&FromDtl=1&IsFirst=1&UserNo=" + WebUser.getNo() + "&SID=" + Web.WebUser.SID + "&AppCenterDBType=" + BP.DA.DBAccess.AppCenterDBType + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
		return url;
	}

	/** 
	 设计自由表单
	 
	 @return 
	*/
	public final String DFreeFrm()
	{
		FrmLabs labs = new Sys.FrmLabs(this.No);
		if (labs.size() == 0)
		{
			//进行初始化控件位置及标签
			MapData md = new MapData();
			md.No = this.No;
			md.RetrieveFromDBSources();
			float maxEnd = 200;
			boolean isLeft = true;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 字段顺序调整
			MapAttrs mapAttrs = new Sys.MapAttrs(md.No);
			for (MapAttr attr : mapAttrs)
			{
				if (attr.UIVisible == false)
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
					lab.setMyPK( BP.DA.DBAccess.GenerGUID();
					lab.FK_MapData = attr.FK_MapData;
					lab.Text = attr.Name;
					lab.FontName = "Arial";
					lab.X = 40;
					lab.Y = maxEnd;
					lab.Insert();

					attr.X = lab.X + 80;
					attr.Y = maxEnd;
					attr.Update();
				}
				else
				{
					lab = new FrmLab();
					lab.setMyPK( BP.DA.DBAccess.GenerGUID();
					lab.FK_MapData = attr.FK_MapData;
					lab.Text = attr.Name;
					lab.FontName = "Arial";
					lab.X = 350;
					lab.Y = maxEnd;
					lab.Insert();

					attr.X = lab.X + 80;
					attr.Y = maxEnd;
					attr.Update();
				}
				isLeft = !isLeft;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 明细表重置排序
			MapDtls mapDtls = new Sys.MapDtls(this.No);
			for (MapDtl dtl : mapDtls)
			{
				maxEnd = maxEnd + 40;
				/* 是否是左边 */
				FrmLab lab = new FrmLab();
				lab.setMyPK( BP.DA.DBAccess.GenerGUID();
				lab.FK_MapData = dtl.FK_MapData;
				lab.Text = dtl.Name;
				lab.FontName = "Arial";
				lab.X = 40;
				lab.Y = maxEnd;
				lab.Insert();

				dtl.X = lab.X + 80;
				dtl.Y = maxEnd;
				dtl.Update();
				maxEnd = maxEnd + dtl.H;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

			md.ResetMaxMinXY();
		}
		String url = "../../Admin/CCFormDesigner/FormDesigner.htm?FK_MapData=" " + this.getNo()+ " "&FromDtl=1&UserNo=" + WebUser.getNo() + "&SID=" + Web.WebUser.SID + "&AppCenterDBType=" + BP.DA.DBAccess.AppCenterDBType + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
		return url;
	}

	public final String GenerAttrs()
	{
		String strs = "";
		MapAttrs attrs = new MapAttrs(this.No);
		for (MapAttr item : attrs)
		{
			strs += "\t\n " + item.KeyOfEn + ",";
		}
		return strs;
	}

	/** 
	 高级设置
	 
	 @return 
	*/
	public final String DoAction()
	{
		return "../../Admin/FoolFormDesigner/ActionForDtl.htm?DoType=Edit&FK_MapData=" " + this.getNo()+ " "&t=" + DataType.getCurrentDataTime();
	}
	public final String HidAttr()
	{
		return "../../Admin/FoolFormDesigner/HidAttr.htm?DoType=Edit&FK_MapData=" " + this.getNo()+ " "&t=" + DataType.getCurrentDataTime();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性.
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 基本属性.


	/** 
	 初始化自定义字段属性
	 
	 @return 返回执行结果
	*/
	public final String InitAttrsOfSelf()
	{
		if (this.getFK_Node() == 0)
		{
			return "err@该从表属性不是自定义属性.";
		}

		if (this.No.Contains("_" + this.getFK_Node()) == false)
		{
			return "err@该从表属性不是自定义属性.";
		}

		//求从表ID.
		String refDtl = this.No.Replace("_" + this.getFK_Node(), "");

		//处理属性问题.
		MapAttrs attrs = new MapAttrs();
		attrs.Delete(MapAttrAttr.FK_MapData, this.No);
		attrs.Retrieve(MapAttrAttr.FK_MapData, refDtl);
		for (MapAttr attr : attrs)
		{
			attr.setMyPK( this.No + "_" + attr.KeyOfEn;
			attr.FK_MapData = this.No;
			attr.Insert();
		}

		//处理mapExt 的问题.
		MapExts exts = new MapExts();
		exts.Delete(MapAttrAttr.FK_MapData, this.No); //先删除，后查询.
		exts.Retrieve(MapAttrAttr.FK_MapData, refDtl);
		MapExt mapExt = null;
		for (MapExt ext : exts)
		{
			mapExt = new MapExt();
			mapExt = ext;
			mapExt.setMyPK( ext.MyPK + "_" + this.getFK_Node();
			mapExt.FK_MapData = this.No;
			mapExt.Insert();
		}

		//处理附件问题
		/* 如果启用了多附件*/
		if (this.getIsEnableAthM() == true)
		{
			BP.Sys.FrmAttachment athDesc = new BP.Sys.FrmAttachment();
			//获取原始附件的属性

			athDesc.setMyPK( this.No + "_AthMDtl";
			if (athDesc.RetrieveFromDBSources() == 0)
			{
				//获取原来附件的属性
				BP.Sys.FrmAttachment oldAthDesc = new BP.Sys.FrmAttachment();
				oldAthDesc.setMyPK( refDtl + "_AthMDtl";
				if (oldAthDesc.RetrieveFromDBSources() == 0)
				{
					return "原始从表的附件属性不存在，请联系管理员";
				}
				athDesc = oldAthDesc;
				athDesc.setMyPK( this.No + "_AthMDtl";
				athDesc.FK_MapData = this.No;
				athDesc.NoOfObj = "AthMDtl";
				athDesc.Name = this.Name;
				athDesc.DirectInsert();
				//增加分组
				GroupField group = new GroupField();
				group.Lab = athDesc.Name;
				group.FrmID = this.getFK_MapData();
				group.CtrlType = "Ath";
				group.CtrlID = athDesc.MyPK;
				group.Idx = 10;
				group.Insert();
			}

			//判断是否有隐藏的AthNum 字段
			MapAttr attr = new MapAttr();
			attr.setMyPK( this.No + "_AthNum";
			int count = attr.RetrieveFromDBSources();
			if (count == 0)
			{
				attr.FK_MapData = this.No;
				attr.KeyOfEn = "AthNum";
				attr.Name = "附件数量";
				attr.DefVal = "0";
				attr.UIContralType = UIContralType.TB;
				attr.MyDataType = DataType.AppInt;
				attr.UIVisible = false;
				attr.UIIsEnable = false;
				attr.DirectInsert();
			}


		}

		return "执行成功";
	}

	@Override
	protected boolean beforeUpdate()
	{
		MapDtl dtl = new MapDtl(this.No);
		//启用审核
		dtl.IsEnablePass = this.getIsEnablePass();
		//超链接
		dtl.IsEnableLink = this.getIsEnableLink();
		dtl.LinkLabel = this.getLinkLabel();
		dtl.LinkUrl = this.getLinkUrl();
		dtl.LinkTarget = this.getLinkTarget();
		dtl.Update();

		//判断是否启用多附件
		if (this.getIsEnableAthM() == true)
		{
			BP.Sys.FrmAttachment athDesc = new BP.Sys.FrmAttachment();
			athDesc.setMyPK( this.No + "_AthMDtl";
			if (athDesc.RetrieveFromDBSources() == 0)
			{
				athDesc.FK_MapData = this.No;
				athDesc.NoOfObj = "AthMDtl";
				athDesc.Name = this.Name;
				athDesc.DirectInsert();
				//增加分组
				GroupField group = new GroupField();
				group.Lab = athDesc.Name;
				group.FrmID = this.getFK_MapData();
				group.CtrlType = "Ath";
				group.CtrlID = athDesc.MyPK;
				group.Idx = 10;
				group.Insert();
			}

			//判断是否有隐藏的AthNum 字段
			MapAttr attr = new MapAttr();
			attr.setMyPK( this.No + "_AthNum";
			int count = attr.RetrieveFromDBSources();
			if (count == 0)
			{
				attr.FK_MapData = this.No;
				attr.KeyOfEn = "AthNum";
				attr.Name = "附件数量";
				attr.DefVal = "0";
				attr.UIContralType = UIContralType.TB;
				attr.MyDataType = DataType.AppInt;
				attr.UIVisible = false;
				attr.UIIsEnable = false;
				attr.DirectInsert();

			}
		}


		//获得事件实体.
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var febd = BP.Sys.Glo.GetFormDtlEventBaseByEnName(this.No);
		if (febd == null)
		{
			this.setFEBD("");
		}
		else
		{
			this.setFEBD(febd.toString());
		}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查填充的SQL是否符合要求.
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		//更新分组标签.  @fanleiwei. 代码有变化.
		BP.Sys.GroupField gf = new GroupField();
		int i = gf.Retrieve(GroupFieldAttr.CtrlType, "Dtl", GroupFieldAttr.CtrlID, this.No);
		if (i == 0)
		{
			gf.CtrlID = this.No;
			gf.CtrlType = "Dtl";
			gf.FrmID = this.getFK_MapData();
			gf.Insert();
		}

		if (i > 1)
		{
			gf.Delete();
			i = gf.Retrieve(GroupFieldAttr.CtrlType, "Dtl", GroupFieldAttr.CtrlID, this.No);
		}

		if (i == 1 && gf.Lab.equals(this.Name) == false)
		{
			gf.Lab = this.Name;
			gf.Update();
		}

		return super.beforeUpdate();
	}

	@Override
	protected void afterDelete()
	{
		MapDtl dtl = new MapDtl();
		dtl.No = this.No;
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}
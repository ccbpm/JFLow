package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;
import java.time.*;

/** 
 表单属性
*/
public class MapDataExt extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 权限控制.
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		if (WebUser.getNo().equals("admin"))
		{
			uac.IsDelete = false;
			uac.IsUpdate = true;
			return uac;
		}
		uac.Readonly();
		return uac;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 权限控制.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region weboffice文档属性(参数属性)
	/** 
	 是否启用锁定行
	*/
	public final boolean getIsRowLock()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsRowLock, false);
	}
	public final void setIsRowLock(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsRowLock, value);
	}

	/** 
	 是否启用打印
	*/
	public final boolean getIsWoEnablePrint()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnablePrint);
	}
	public final void setIsWoEnablePrint(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnablePrint, value);
	}
	/** 
	 是否启用只读
	*/
	public final boolean getIsWoEnableReadonly()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableReadonly);
	}
	public final void setIsWoEnableReadonly(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableReadonly, value);
	}
	/** 
	 是否启用修订
	*/
	public final boolean getIsWoEnableRevise()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableRevise);
	}
	public final void setIsWoEnableRevise(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableRevise, value);
	}
	/** 
	 是否启用保存
	*/
	public final boolean getIsWoEnableSave()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableSave);
	}
	public final void setIsWoEnableSave(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableSave, value);
	}
	/** 
	 是否查看用户留痕
	*/
	public final boolean getIsWoEnableViewKeepMark()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableViewKeepMark);
	}
	public final void setIsWoEnableViewKeepMark(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableViewKeepMark, value);
	}
	/** 
	 是否启用weboffice
	*/
	public final boolean getIsWoEnableWF()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableWF);
	}
	public final void setIsWoEnableWF(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableWF, value);
	}

	/** 
	 是否启用套红
	*/
	public final boolean getIsWoEnableOver()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableOver);
	}
	public final void setIsWoEnableOver(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableOver, value);
	}

	/** 
	 是否启用签章
	*/
	public final boolean getIsWoEnableSeal()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableSeal);
	}
	public final void setIsWoEnableSeal(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableSeal, value);
	}

	/** 
	 是否启用公文模板
	*/
	public final boolean getIsWoEnableTemplete()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableTemplete);
	}
	public final void setIsWoEnableTemplete(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableTemplete, value);
	}

	/** 
	 是否记录节点信息
	*/
	public final boolean getIsWoEnableCheck()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableCheck);
	}
	public final void setIsWoEnableCheck(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableCheck, value);
	}
	/** 
	 是否插入流程图
	*/
	public final boolean getIsWoEnableInsertFlow()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableInsertFlow);
	}
	public final void setIsWoEnableInsertFlow(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableInsertFlow, value);
	}

	/** 
	 是否插入风险点
	*/
	public final boolean getIsWoEnableInsertFengXian()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableInsertFengXian);
	}
	public final void setIsWoEnableInsertFengXian(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableInsertFengXian, value);
	}

	/** 
	 是否启用留痕模式
	*/
	public final boolean getIsWoEnableMarks()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableMarks);
	}
	public final void setIsWoEnableMarks(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableMarks, value);
	}

	/** 
	 是否插入风险点
	*/
	public final boolean getIsWoEnableDown()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableDown);
	}
	public final void setIsWoEnableDown(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableDown, value);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion weboffice文档属性

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 自动计算属性.
	/** 
	 左边界.
	*/
	public final float getMaxLeft()
	{
		return this.GetParaFloat(MapDataAttr.MaxLeft);
	}
	public final void setMaxLeft(float value)
	{
		this.SetPara(MapDataAttr.MaxLeft, value);
	}
	/** 
	 右边界
	*/
	public final float getMaxRight()
	{
		return this.GetParaFloat(MapDataAttr.MaxRight);
	}
	public final void setMaxRight(float value)
	{
		this.SetPara(MapDataAttr.MaxRight, value);
	}
	/** 
	 最高top
	*/
	public final float getMaxTop()
	{
		return this.GetParaFloat(MapDataAttr.MaxTop);
	}
	public final void setMaxTop(float value)
	{
		this.SetPara(MapDataAttr.MaxTop, value);
	}
	/** 
	 最低
	*/
	public final float getMaxEnd()
	{
		return this.GetParaFloat(MapDataAttr.MaxEnd);
	}
	public final void setMaxEnd(float value)
	{
		this.SetPara(MapDataAttr.MaxEnd, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 自动计算属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 报表属性(参数方式存储).
	/** 
	 是否关键字查询
	*/
	public final boolean getRptIsSearchKey()
	{
		return this.GetParaBoolen(MapDataAttr.RptIsSearchKey, true);
	}
	public final void setRptIsSearchKey(boolean value)
	{
		this.SetPara(MapDataAttr.RptIsSearchKey, value);
	}
	/** 
	 时间段查询方式
	*/
	public final DTSearchWay getRptDTSearchWay()
	{
		return (DTSearchWay)this.GetParaInt(MapDataAttr.RptDTSearchWay);
	}
	public final void setRptDTSearchWay(DTSearchWay value)
	{
		this.SetPara(MapDataAttr.RptDTSearchWay, value.getValue());
	}
	/** 
	 时间字段
	*/
	public final String getRptDTSearchKey()
	{
		return this.GetParaString(MapDataAttr.RptDTSearchKey);
	}
	public final void setRptDTSearchKey(String value)
	{
		this.SetPara(MapDataAttr.RptDTSearchKey, value);
	}
	/** 
	 查询外键枚举字段
	*/
	public final String getRptSearchKeys()
	{
		return this.GetParaString(MapDataAttr.RptSearchKeys,"*");
	}
	public final void setRptSearchKeys(String value)
	{
		this.SetPara(MapDataAttr.RptSearchKeys, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 报表属性(参数方式存储).

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 外键属性
	public final String getVer()
	{
		return this.GetValStringByKey(MapDataAttr.Ver);
	}
	public final void setVer(String value)
	{
		this.SetValByKey(MapDataAttr.Ver, value);
	}
	/** 
	 顺序号
	*/
	public final int getIdx()
	{
		return this.GetValIntByKey(MapDataAttr.Idx);
	}
	public final void setIdx(int value)
	{
		this.SetValByKey(MapDataAttr.Idx, value);
	}
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
	public final GroupFields getGroupFields()
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
	 报表
	*/
	public final FrmRpts getFrmRpts()
	{
		Object tempVar = this.GetRefObject("FrmRpts");
		FrmRpts obj = tempVar instanceof FrmRpts ? (FrmRpts)tempVar : null;
		if (obj == null)
		{
			obj = new FrmRpts(this.No);
			this.SetRefObject("FrmRpts", obj);
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
		Object tempVar = this.GetRefObject("FrmBtns");
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
		Object tempVar = this.GetRefObject("FrmImgs");
		FrmImgs obj = tempVar instanceof FrmImgs ? (FrmImgs)tempVar : null;
		if (obj == null)
		{
			obj = new FrmImgs(this.No);
			this.SetRefObject("FrmImgs", obj);
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
	/** 
	 物理表
	*/
	public final String getPTable()
	{
		String s = this.GetValStrByKey(MapDataAttr.PTable);
		if (s.equals("") || s == null)
		{
			return this.No;
		}
		return s;
	}
	public final void setPTable(String value)
	{
		this.SetValByKey(MapDataAttr.PTable, value);
	}
	/** 
	 URL
	*/
	public final String getUrl()
	{
		return this.GetValStrByKey(MapDataAttr.Url);
	}
	public final void setUrl(String value)
	{
		this.SetValByKey(MapDataAttr.Url, value);
	}
	public final DBUrlType getHisDBUrl()
	{
		return DBUrlType.AppCenterDSN;
	}
	public final AppType getHisAppType()
	{
		return (AppType)this.GetValIntByKey(MapDataAttr.AppType);
	}
	public final void setHisAppType(AppType value)
	{
		this.SetValByKey(MapDataAttr.AppType, value.getValue());
	}
	/** 
	 备注
	*/
	public final String getNote()
	{
		return this.GetValStrByKey(MapDataAttr.Note);
	}
	public final void setNote(String value)
	{
		this.SetValByKey(MapDataAttr.Note, value);
	}
	/** 
	 是否有CA.
	*/
	public final boolean getIsHaveCA()
	{
		return this.GetParaBoolen("IsHaveCA", false);

	}
	public final void setIsHaveCA(boolean value)
	{
		this.SetPara("IsHaveCA", value);
	}
	/** 
	 类别，可以为空.
	*/
	public final String getFK_FrmSort()
	{
		return this.GetValStrByKey(MapDataAttr.FK_FrmSort);
	}
	public final void setFK_FrmSort(String value)
	{
		this.SetValByKey(MapDataAttr.FK_FrmSort, value);
	}
	/** 
	 类别，可以为空.
	*/
	public final String getFK_FormTree()
	{
		return this.GetValStrByKey(MapDataAttr.FK_FormTree);
	}
	public final void setFK_FormTree(String value)
	{
		this.SetValByKey(MapDataAttr.FK_FormTree, value);
	}
	/** 
	 从表集合.
	*/
	public final String getDtls()
	{
		return this.GetValStrByKey(MapDataAttr.Dtls);
	}
	public final void setDtls(String value)
	{
		this.SetValByKey(MapDataAttr.Dtls, value);
	}
	/** 
	 主键
	*/
	public final String getEnPK()
	{
		String s = this.GetValStrByKey(MapDataAttr.EnPK);
		if (DataType.IsNullOrEmpty(s))
		{
			return "OID";
		}
		return s;
	}
	public final void setEnPK(String value)
	{
		this.SetValByKey(MapDataAttr.EnPK, value);
	}
	private Entities _HisEns = null;
	public final Entities getHisEns()
	{
		if (_HisEns == null)
		{
			_HisEns = BP.En.ClassFactory.GetEns(this.No);
		}
		return _HisEns;
	}
	public final Entity getHisEn()
	{
		return this.getHisEns().GetNewEntity;
	}
	public final float getFrmW()
	{
		return this.GetValFloatByKey(MapDataAttr.FrmW);
	}
	public final void setFrmW(float value)
	{
		this.SetValByKey(MapDataAttr.FrmW, value);
	}
	///// <summary>
	///// 表单控制方案
	///// </summary>
	//public string Slns
	//{
	//    get
	//    {
	//        return this.GetValStringByKey(MapDataAttr.Slns);
	//    }
	//    set
	//    {
	//        this.SetValByKey(MapDataAttr.Slns, value);
	//    }
	//}
	public final float getFrmH()
	{
		return this.GetValFloatByKey(MapDataAttr.FrmH);
	}
	public final void setFrmH(float value)
	{
		this.SetValByKey(MapDataAttr.FrmH, value);
	}
	/** 
	 表格显示的列
	*/
	public final int getTableCol()
	{
		int i = this.GetValIntByKey(MapDataAttr.TableCol);
		if (i == 0 || i == 1)
		{
			return 4;
		}
		return i;
	}
	public final void setTableCol(int value)
	{
		this.SetValByKey(MapDataAttr.TableCol, value);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 表单属性
	*/
	public MapDataExt()
	{
	}
	/** 
	 表单属性
	 
	 @param no 映射编号
	*/
	public MapDataExt(String no)
	{
		super(no);
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
		Map map = new Map("Sys_MapData", "表单属性");
		map.Java_SetEnType(EnType.Sys);
		map.Java_SetCodeStruct("4");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基本属性.
		map.AddTBStringPK(MapDataAttr.No, null, "表单编号", true, false, 1, 190, 20);
		map.AddTBString(MapDataAttr.Name, null, "表单名称", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.PTable, null, "存储表", true, false, 0, 500, 20);

			//表单的运行类型.
		map.AddDDLSysEnum(MapDataAttr.FrmType, (int)BP.Sys.FrmType.FreeFrm, "表单类型",true, true, MapDataAttr.FrmType);

		map.AddTBString(MapDataAttr.Url, null, "URL连接(对嵌入式表单有效)", true, false, 0, 500, 20, true);
			//数据源.
		map.AddDDLEntities(MapDataAttr.DBSrc, "local", "数据源", new BP.Sys.SFDBSrcs(), true);

		map.AddDDLEntities(MapDataAttr.FK_FormTree, "01", "表单类别", new SysFormTrees(), true);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基本属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 设计者信息.
		map.AddTBString(MapDataAttr.Designer, null, "设计者", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.DesignerContact, null, "联系方式", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.DesignerUnit, null, "单位", true, false, 0, 500, 20,true);
		map.AddTBString(MapDataAttr.GUID, null, "GUID", true, true, 0, 128, 20,false);
		map.AddTBString(MapDataAttr.Ver, null, "版本号", true, true, 0, 30, 20);
		map.AddTBStringDoc(MapDataAttr.Note, null, "备注", true, false,true);

			//增加参数字段.
		map.AddTBAtParas(4000);
		map.AddTBInt(MapDataAttr.Idx, 100, "顺序号", false, false);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 设计者信息.

			//查询条件.
		map.AddSearchAttr(MapDataAttr.DBSrc);

			//RefMethod rm = new RefMethod();
			//rm.Title = "设计自由表单"; // "设计表单";
			//rm.ClassMethodName = this.ToString() + ".DoDFrom";
			//rm.Icon = ../../Img/Form.png";
			//rm.Visable = true;
			//rm.Target = "_blank";
			//map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "设计傻瓜表单"; // "设计表单";
			//rm.ClassMethodName = this.ToString() + ".DoDFromCol4";
			//rm.Icon = ../../Img/Form.png";
			//rm.Visable = true;
			//rm.Target = "_blank";
			//map.AddRefMethod(rm);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 方法 - 基本功能.
		RefMethod rm = new RefMethod();
		rm.Title = "装载填充"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoPageLoadFull";
		rm.Icon = "../../WF/Img/FullData.png";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "表单事件"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoEvent";
		rm.Icon = "../../WF/Img/Event.png";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "批量设置验证规则";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.ClassMethodName = this.toString() + ".DoRegularExpressionBatch";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "批量修改字段"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoBatchEditAttr";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/field.png";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		   // map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "傻瓜表单设计器";
		rm.ClassMethodName = this.toString() + ".DoDesignerFool";
		rm.Icon = "../../WF/Img/FileType/xlsx.gif";
		rm.Visable = true;
		rm.Target = "_blank";
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "手机端表单";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.ClassMethodName = this.toString() + ".DoSortingMapAttrs";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "JS编程"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoInitScript";
		rm.Icon = "../../WF/Img/Script.png";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "表单body属性"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoBodyAttr";
		rm.Icon = "../../WF/Img/Script.png";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "导出XML表单模版"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoExp";
		rm.Icon = "../../WF/Img/Export.png";
		rm.Visable = true;
		rm.RefAttrLinkLabel = "导出到xml";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);


			//带有参数的方法.
		rm = new RefMethod();
		rm.Title = "重命名字段";
		  //  rm.Warning = "您确定要处理吗？";
		rm.getHisAttrs().AddTBString("FieldOld", null, "旧字段英文名", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("FieldNew", null, "新字段英文名", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("FieldNewName", null, "新字段中文名", true, false, 0, 100, 100);
		rm.ClassMethodName = this.toString() + ".DoChangeFieldName";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "重命表单ID";
			//  rm.GroupName = "高级设置";
		rm.getHisAttrs().AddTBString("NewFrmID1", null, "新表单ID名称", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("NewFrmID2", null, "确认表单ID名称", true, false, 0, 100, 100);
		rm.ClassMethodName = this.toString() + ".DoChangeFrmID";
		rm.Icon = "../../WF/Img/ReName.png";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "检查表单";
		rm.ClassMethodName = this.toString() + ".DoCheckFrm";
		rm.Icon = "../../WF/Img/check.png";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 方法 - 基本功能.


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 方法 - 开发接口.
		rm = new RefMethod();
		rm.Title = "调用查询API"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoSearch";
		rm.Icon = "../../WF/Img/Table.gif";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "调用分析API"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoGroup";
		rm.Icon = "../../WF/Img/Table.gif";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		rm.GroupName = "开发接口";
		map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 方法 - 开发接口.


			//rm = new RefMethod();
			//rm.Title = "Word表单属性"; // "设计表单";
			//rm.ClassMethodName = this.ToString() + ".DoWordFrm";
			//rm.Icon = ../../Img/Btn/Word.gif";
			//rm.Visable = true;
			//rm.RefMethodType = RefMethodType.RightFrameOpen;
			//rm.Target = "_blank";
			//rm.GroupName = "开发接口";
			//map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "Excel表单属性"; // "设计表单";
			//rm.ClassMethodName = this.ToString() + ".DoExcelFrm";
			//rm.Icon = ../../Img/Btn/Excel.gif";
			//rm.Visable = true;
			//rm.RefMethodType = RefMethodType.RightFrameOpen;
			//rm.Target = "_blank";
			//rm.GroupName = "开发接口";
			//map.AddRefMethod(rm);


			//rm = new RefMethod();
			//rm.Title = "数据源管理"; // "设计表单";
			//rm.ClassMethodName = this.ToString() + ".DoDBSrc";
			//rm.Icon = "/WF/Img/DB.png";
			//rm.RefMethodType = RefMethodType.RightFrameOpen;
			//rm.Visable = true;
			//rm.RefAttrLinkLabel = "数据源管理";
			//rm.Target = "_blank";
			//map.AddRefMethod(rm);


		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本方法.
	/** 
	 傻瓜表单设计器
	 
	 @return 
	*/
	public final String DoDesignerFool()
	{
		return "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData= " + this.getNo()+ " &IsFirst=1&MyPK= " + this.getNo()+ " &IsEditMapData=True";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法.
	/** 
	 重命名
	 
	 @param frmID1
	 @param frmID2
	 @return 
	*/
	public final String DoChangeFrmID(String frmID1, String frmID2)
	{
		MapData md = new MapData();
		md.No = frmID1;
		if (md.IsExits == true)
		{
			return "表单ID【" + frmID1 + "】已经存在";
		}

		if (!frmID1.equals(frmID2))
		{
			return "两次输入的ID不一致.";
		}


		String frmIDOld = this.No;

		String sqls = "";
		sqls += "@UPDATE Sys_MapData SET No='" + frmID1 + "' WHERE No='" + frmIDOld + "'";
		sqls += "UPDATE Sys_FrmLine SET FK_MapData='" + frmID1 + "' WHERE FK_MapData='" + frmIDOld + "'";
		sqls += "UPDATE Sys_FrmLab SET FK_MapData='" + frmID1 + "' WHERE FK_MapData='" + frmIDOld + "'";
		sqls += "UPDATE Sys_FrmBtn SET FK_MapData='" + frmID1 + "' WHERE FK_MapData='" + frmIDOld + "'";
		sqls += "UPDATE Sys_MapAttr SET FK_MapData='" + frmID1 + "' WHERE FK_MapData='" + frmIDOld + "'";
		sqls += "UPDATE Sys_MapExt SET FK_MapData='" + frmID1 + "' WHERE FK_MapData='" + frmIDOld + "'";
		sqls += "UPDATE Sys_FrmImg SET FK_MapData='" + frmID1 + "' WHERE FK_MapData='" + frmIDOld + "'";
		sqls += "UPDATE Sys_FrmImgAth SET FK_MapData='" + frmID1 + "' WHERE FK_MapData='" + frmIDOld + "'";
		sqls += "UPDATE Sys_FrmRB SET FK_MapData='" + frmID1 + "' WHERE FK_MapData='" + frmIDOld + "'";
		sqls += "UPDATE Sys_MapDtl SET FK_MapData='" + frmID1 + "' WHERE FK_MapData='" + frmIDOld + "'";
		sqls += "UPDATE Sys_MapFrame SET FK_MapData='" + frmID1 + "' WHERE FK_MapData='" + frmIDOld + "'";
		sqls += "UPDATE Sys_FrmEle SET FK_MapData='" + frmID1 + "' WHERE FK_MapData='" + frmIDOld + "'";
		sqls += "UPDATE Sys_FrmEvent SET FK_MapData='" + frmID1 + "' WHERE FK_MapData='" + frmIDOld + "'";
		BP.DA.DBAccess.RunSQLs(sqls);

		return "重命名成功，你需要关闭窗口重新刷新。";
	}
	/** 
	 替换名称
	 
	 @param fieldOldName 旧名称
	 @param newField 新字段
	 @param newFieldName 新字段名称(可以为空)
	 @return 
	*/
	public final String DoChangeFieldName(String fieldOld, String newField, String newFieldName)
	{
		MapAttr attrOld = new MapAttr();
		attrOld.KeyOfEn = fieldOld;
		attrOld.setFK_MapData(this.getNo());
		attrOld.setMyPK( attrOld.FK_MapData + "_" + attrOld.KeyOfEn;
		if (attrOld.RetrieveFromDBSources() == 0)
		{
			return "@旧字段输入错误[" + attrOld.KeyOfEn + "].";
		}

		//检查是否存在该字段？
		MapAttr attrNew = new MapAttr();
		attrNew.KeyOfEn = newField;
		attrNew.setFK_MapData(this.getNo());
		attrNew.setMyPK( attrNew.FK_MapData + "_" + attrNew.KeyOfEn;
		if (attrNew.RetrieveFromDBSources() == 1)
		{
			return "@该字段[" + attrNew.KeyOfEn + "]已经存在.";
		}

		//删除旧数据.
		attrOld.Delete();

		//copy这个数据,增加上它.
		attrNew.Copy(attrOld);
		attrNew.KeyOfEn = newField;
		attrNew.setFK_MapData(this.getNo());

		if (!newFieldName.equals(""))
		{
			attrNew.Name = newFieldName;
		}

		attrNew.Insert();

		//更新处理他的相关业务逻辑.
		MapExts exts = new MapExts(this.No);
		for (MapExt item : exts)
		{
			item.setMyPK( item.MyPK.Replace("_" + fieldOld, "_" + newField);

			if (fieldOld.equals(item.AttrOfOper))
			{
				item.AttrOfOper = newField;
			}

			if (fieldOld.equals(item.AttrsOfActive))
			{
				item.AttrsOfActive = newField;
			}

			item.Tag = item.Tag.Replace(fieldOld, newField);
			item.Tag1 = item.Tag1.Replace(fieldOld, newField);
			item.Tag2 = item.Tag2.Replace(fieldOld, newField);
			item.Tag3 = item.Tag3.Replace(fieldOld, newField);

			item.AtPara = item.AtPara.Replace(fieldOld, newField);
			item.Doc = item.Doc.Replace(fieldOld, newField);
			item.Save();
		}
		return "执行成功";
	}

	/** 
	 检查表单
	 
	 @return 
	*/
	public final String DoCheckFrm()
	{
		return "../../Admin/AttrNode/CheckFrm.htm?FK_MapData= " + this.getNo()+ " &t=" + LocalDateTime.now().toString("yyyyMMddHHmmssffffff");
	}

	/** 
	 批量修改字段
	 
	 @return 
	*/
	public final String DoBatchEditAttr()
	{
		return "../../Admin/FoolFormDesigner/BatchEdit.aspx?FK_MapData= " + this.getNo()+ " &t=" + DataType.getCurrentDataTime();
	}
	/** 
	 批量设置正则表达式规则.
	 
	 @return 
	*/
	public final String DoRegularExpressionBatch()
	{
		return "../../Admin/FoolFormDesigner/MapExt/RegularExpressionBatch.htm?FK_Flow=&FK_MapData= " + this.getNo()+ " &t=" + DataType.getCurrentDataTime();
	}
	/** 
	 排序字段顺序
	 
	 @return 
	*/
	public final String DoSortingMapAttrs()
	{
		return "../../Admin/AttrNode/SortingMapAttrs.htm?FK_Flow=&FK_MapData= " + this.getNo()+ " &t=" + DataType.getCurrentDataTime();
	}
	  /** 
	 设计表单
	 
	 @return 
	  */
	public final String DoDFrom()
	{
		return "../../Admin/FoolFormDesigner/CCForm/Frm.htm?FK_MapData= " + this.getNo()+ " &UserNo=" + WebUser.getNo() + "&SID=" + Web.WebUser.SID + "&AppCenterDBType=" + BP.DA.DBAccess.AppCenterDBType + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
	}
	/** 
	 设计傻瓜表单
	 
	 @return 
	*/
	public final String DoDFromCol4()
	{
		return "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData= " + this.getNo()+ " &IsFirst=1&UserNo=" + WebUser.getNo() + "&SID=" + Web.WebUser.SID + "&AppCenterDBType=" + BP.DA.DBAccess.AppCenterDBType + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
	}
	/** 
	 查询
	 
	 @return 
	*/
	public final String DoSearch()
	{
		return "../../Comm/Search.htm?s=34&FK_MapData= " + this.getNo()+ " &EnsName=" + this.No;
	}
	/** 
	 调用分析API
	 
	 @return 
	*/
	public final String DoGroup()
	{
		return "../../Comm/Group.htm?s=34&FK_MapData= " + this.getNo()+ " &EnsName=" + this.No;
	}
	/** 
	 数据源管理
	 
	 @return 
	*/
	public final String DoDBSrc()
	{
		return "../../Comm/Search.htm?s=34&FK_MapData= " + this.getNo()+ " &EnsName=BP.Sys.SFDBSrcs";
	}
	public final String DoWordFrm()
	{
		return "../../Admin/FoolFormDesigner/MapExt/WordFrm.aspx?s=34&FK_MapData= " + this.getNo()+ " &ExtType=WordFrm&RefNo=";
	}

	public final String DoPageLoadFull()
	{
		return "../../Admin/FoolFormDesigner/MapExt/PageLoadFull.htm?s=34&FK_MapData= " + this.getNo()+ " &ExtType=PageLoadFull&RefNo=";
	}
	public final String DoInitScript()
	{
		return "../../Admin/FoolFormDesigner/MapExt/InitScript.htm?s=34&FK_MapData= " + this.getNo()+ " &ExtType=PageLoadFull&RefNo=";
	}
	/** 
	 表单属性.
	 
	 @return 
	*/
	public final String DoBodyAttr()
	{
		return "../../Admin/FoolFormDesigner/MapExt/BodyAttr.htm?s=34&FK_MapData= " + this.getNo()+ " &ExtType=BodyAttr&RefNo=";
	}
	/** 
	 表单事件
	 
	 @return 
	*/
	public final String DoEvent()
	{
		return "../../Admin/CCFormDesigner/Action.htm?FK_MapData= " + this.getNo()+ " &T=sd&FK_Node=0";
	}

	/** 
	 导出表单
	 
	 @return 
	*/
	public final String DoExp()
	{
		return "../../Admin/FoolFormDesigner/ImpExp/Exp.htm?FK_MapData=" + this.No;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 方法.
}
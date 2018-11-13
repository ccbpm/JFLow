package BP.WF.Template;

import BP.DA.DBUrlType;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.En.UAC;
import BP.Sys.AppType;
import BP.Sys.DTSearchWay;
import BP.Sys.FrmAttachmentAttr;
import BP.Sys.FrmAttachments;
import BP.Sys.FrmBtns;
import BP.Sys.FrmEles;
import BP.Sys.FrmEvents;
import BP.Sys.FrmImgAths;
import BP.Sys.FrmImgs;
import BP.Sys.FrmLabs;
import BP.Sys.FrmLines;
import BP.Sys.FrmLinks;
import BP.Sys.FrmRBs;
import BP.Sys.FrmRpts;
import BP.Sys.GroupFields;
import BP.Sys.MapAttrs;
import BP.Sys.MapDataAttr;
import BP.Sys.MapDtls;
import BP.Sys.MapExts;
import BP.Sys.MapFrames;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Glo;

/** 
 表单属性
 
*/
public class MapDataURL extends EntityNoName
{

		///#region 权限控制.
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		if (BP.Web.WebUser.getNo().equals("admin"))
		{
			uac.IsDelete = false;
			uac.IsUpdate = true;
			return uac;
		}
		uac.Readonly();
		return uac;
	}

		///#endregion 权限控制.


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


		///#endregion weboffice文档属性


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

		///#endregion 自动计算属性.


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
		return DTSearchWay.forValue(this.GetParaInt(MapDataAttr.RptDTSearchWay));
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
		return this.GetParaString(MapDataAttr.RptSearchKeys, "*");
	}
	public final void setRptSearchKeys(String value)
	{
		this.SetPara(MapDataAttr.RptSearchKeys, value);
	}

		///#endregion 报表属性(参数方式存储).


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
	 分组字段
	 * @throws Exception 
	 
	*/
	public final GroupFields getGroupFields() throws Exception
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
	 报表
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
		Object tempVar = this.GetRefObject("FrmBtns");
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
		Object tempVar = this.GetRefObject("FrmImgs");
		FrmImgs obj = (FrmImgs)((tempVar instanceof FrmImgs) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmImgs(this.getNo());
			this.SetRefObject("FrmImgs", obj);
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

	public static boolean getIsEditDtlModel()
	{
		String s = BP.Web.WebUser.GetSessionByKey("IsEditDtlModel", "0");
		if (s.equals("0"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	public static void setIsEditDtlModel(boolean value)
	{
		BP.Web.WebUser.SetSessionByKey("IsEditDtlModel", "1");
	}


		
	/** 
	 物理表
	 
	*/
	public final String getPTable()
	{
		String s = this.GetValStrByKey(MapDataAttr.PTable);
		if (s.equals("") || s == null)
		{
			return this.getNo();
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
		return AppType.forValue(this.GetValIntByKey(MapDataAttr.AppType));
	}
	public final void setHisAppType(AppType value)
	{
		this.SetValByKey(MapDataAttr.AppType, value);
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
		if (StringHelper.isNullOrEmpty(s))
		{
			return "OID";
		}
		return s;
	}
	public final void setEnPK(String value)
	{
		this.SetValByKey(MapDataAttr.EnPK, value);
	}
	public Entities _HisEns = null;
	public final Entities getHisEns()
	{
		if (_HisEns == null)
		{
			_HisEns = BP.En.ClassFactory.GetEns(this.getNo());
		}
		return _HisEns;
	}
	public final Entity getHisEn()
	{
		return this.getHisEns().getGetNewEntity();
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
	public final String getTableWidth()
	{
			//switch (this.TableCol)
			//{
			//    case 2:
			//        return
			//        labCol = 25;
			//        ctrlCol = 75;
			//        break;
			//    case 4:
			//        labCol = 20;
			//        ctrlCol = 30;
			//        break;
			//    case 6:
			//        labCol = 15;
			//        ctrlCol = 30;
			//        break;
			//    case 8:
			//        labCol = 10;
			//        ctrlCol = 15;
			//        break;
			//    default:
			//        break;
			//}


		int i = this.GetValIntByKey(MapDataAttr.TableWidth);
		if (i <= 50)
		{
			return "100%";
		}
		return i + "px";
	}

		///#endregion


		
	/** 
	 表单属性
	 
	*/
	public MapDataURL()
	{
	}
	/** 
	 表单属性
	 
	 @param no 映射编号
	 * @throws Exception 
	*/
	public MapDataURL(String no) throws Exception
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

		Map map = new Map("Sys_MapData", "URL表单属性");


		map.AddTBStringPK(MapDataAttr.No, null, "表单编号", true, false, 1, 200, 20);
		map.AddTBString(MapDataAttr.Name, null, "表单名称", true, false, 0, 500, 20);

		map.AddTBString(MapDataAttr.PTable, null, "存储表", false, false, 0, 500, 20);

		map.AddTBString(MapDataAttr.Url, null, "URL连接", true, false, 0, 500, 20, true);
		

		//表单的运行类型.
	   map.AddDDLSysEnum(MapDataAttr.FrmType, BP.Sys.FrmType.FreeFrm.getValue(), "表单类型",true, false, MapDataAttr.FrmType);

	
			//数据源.
		//map.AddDDLEntities(MapDataAttr.DBSrc, "local", "数据源", new BP.Sys.SFDBSrcs(), false);
		map.AddDDLEntities(MapDataAttr.FK_FormTree, "01", "表单类别", new SysFormTrees(), true);

			///#endregion 基本属性.


			///#region 设计者信息.
		map.AddTBString(MapDataAttr.Designer, null, "设计者", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.DesignerContact, null, "联系方式", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.DesignerUnit, null, "单位", true, false, 0, 500, 20, true);
		map.AddTBString(MapDataAttr.GUID, null, "GUID", true, true, 0, 128, 20, false);
		map.AddTBString(MapDataAttr.Ver, null, "版本号", true, true, 0, 30, 20);
		map.AddTBStringDoc(MapDataAttr.Note, null, "备注", true, false, true);
			//增加参数字段.
		map.AddTBAtParas(4000);
		map.AddTBInt(MapDataAttr.Idx, 100, "顺序号", false, false);

			///#endregion 设计者信息.
 

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
	/** 
	 排序字段顺序
	 
	 @return 
	*/
	public final String DoOpenUrl()
	{
		return this.getUrl();
	}

}
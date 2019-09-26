package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.*;
import BP.Web.WebUser;
import BP.Sys.*;
import BP.Tools.DateUtils;
import BP.WF.*;
import java.util.*;
import java.time.*;

/** 
 表单属性
*/
public class MapDataExt extends EntityNoName
{

		///#region 权限控制.
	@Override
	public UAC getHisUAC() throws Exception
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

		///#endregion 权限控制.


		///#region weboffice文档属性(参数属性)
	/** 
	 是否启用锁定行
	 * @throws Exception 
	*/
	public final boolean getIsRowLock() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsRowLock, false);
	}
	public final void setIsRowLock(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsRowLock, value);
	}

	/** 
	 是否启用打印
	 * @throws Exception 
	*/
	public final boolean getIsWoEnablePrint() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnablePrint);
	}
	public final void setIsWoEnablePrint(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnablePrint, value);
	}
	/** 
	 是否启用只读
	 * @throws Exception 
	*/
	public final boolean getIsWoEnableReadonly() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableReadonly);
	}
	public final void setIsWoEnableReadonly(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableReadonly, value);
	}
	/** 
	 是否启用修订
	*/
	public final boolean getIsWoEnableRevise() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableRevise);
	}
	public final void setIsWoEnableRevise(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableRevise, value);
	}
	/** 
	 是否启用保存
	*/
	public final boolean getIsWoEnableSave() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableSave);
	}
	public final void setIsWoEnableSave(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableSave, value);
	}
	/** 
	 是否查看用户留痕
	*/
	public final boolean getIsWoEnableViewKeepMark() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableViewKeepMark);
	}
	public final void setIsWoEnableViewKeepMark(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableViewKeepMark, value);
	}
	/** 
	 是否启用weboffice
	*/
	public final boolean getIsWoEnableWF() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableWF);
	}
	public final void setIsWoEnableWF(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableWF, value);
	}

	/** 
	 是否启用套红
	*/
	public final boolean getIsWoEnableOver() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableOver);
	}
	public final void setIsWoEnableOver(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableOver, value);
	}

	/** 
	 是否启用签章
	*/
	public final boolean getIsWoEnableSeal() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableSeal);
	}
	public final void setIsWoEnableSeal(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableSeal, value);
	}

	/** 
	 是否启用公文模板
	*/
	public final boolean getIsWoEnableTemplete() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableTemplete);
	}
	public final void setIsWoEnableTemplete(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableTemplete, value);
	}

	/** 
	 是否记录节点信息
	*/
	public final boolean getIsWoEnableCheck() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableCheck);
	}
	public final void setIsWoEnableCheck(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableCheck, value);
	}
	/** 
	 是否插入流程图
	*/
	public final boolean getIsWoEnableInsertFlow() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableInsertFlow);
	}
	public final void setIsWoEnableInsertFlow(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableInsertFlow, value);
	}

	/** 
	 是否插入风险点
	*/
	public final boolean getIsWoEnableInsertFengXian() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableInsertFengXian);
	}
	public final void setIsWoEnableInsertFengXian(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableInsertFengXian, value);
	}

	/** 
	 是否启用留痕模式
	*/
	public final boolean getIsWoEnableMarks() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableMarks);
	}
	public final void setIsWoEnableMarks(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableMarks, value);
	}

	/** 
	 是否插入风险点
	*/
	public final boolean getIsWoEnableDown() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableDown);
	}
	public final void setIsWoEnableDown(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableDown, value);
	}


		///#endregion weboffice文档属性


		///#region 自动计算属性.
	/** 
	 左边界.
	*/
	public final float getMaxLeft() throws Exception
	{
		return this.GetParaFloat(MapDataAttr.MaxLeft);
	}
	public final void setMaxLeft(float value) throws Exception
	{
		this.SetPara(MapDataAttr.MaxLeft, value);
	}
	/** 
	 右边界
	*/
	public final float getMaxRight() throws Exception
	{
		return this.GetParaFloat(MapDataAttr.MaxRight);
	}
	public final void setMaxRight(float value) throws Exception
	{
		this.SetPara(MapDataAttr.MaxRight, value);
	}
	/** 
	 最高top
	*/
	public final float getMaxTop() throws Exception
	{
		return this.GetParaFloat(MapDataAttr.MaxTop);
	}
	public final void setMaxTop(float value) throws Exception
	{
		this.SetPara(MapDataAttr.MaxTop, value);
	}
	/** 
	 最低
	*/
	public final float getMaxEnd() throws Exception
	{
		return this.GetParaFloat(MapDataAttr.MaxEnd);
	}
	public final void setMaxEnd(float value) throws Exception
	{
		this.SetPara(MapDataAttr.MaxEnd, value);
	}

		///#endregion 自动计算属性.


		///#region 报表属性(参数方式存储).
	/** 
	 是否关键字查询
	*/
	public final boolean getRptIsSearchKey() throws Exception
	{
		return this.GetParaBoolen(MapDataAttr.RptIsSearchKey, true);
	}
	public final void setRptIsSearchKey(boolean value) throws Exception
	{
		this.SetPara(MapDataAttr.RptIsSearchKey, value);
	}
	/** 
	 时间段查询方式
	*/
	public final DTSearchWay getRptDTSearchWay() throws Exception
	{
		return DTSearchWay.forValue(this.GetParaInt(MapDataAttr.RptDTSearchWay));
	}
	public final void setRptDTSearchWay(DTSearchWay value) throws Exception
	{
		this.SetPara(MapDataAttr.RptDTSearchWay, value.getValue());
	}
	/** 
	 时间字段
	*/
	public final String getRptDTSearchKey() throws Exception
	{
		return this.GetParaString(MapDataAttr.RptDTSearchKey);
	}
	public final void setRptDTSearchKey(String value) throws Exception
	{
		this.SetPara(MapDataAttr.RptDTSearchKey, value);
	}
	/** 
	 查询外键枚举字段
	*/
	public final String getRptSearchKeys() throws Exception
	{
		return this.GetParaString(MapDataAttr.RptSearchKeys,"*");
	}
	public final void setRptSearchKeys(String value) throws Exception
	{
		this.SetPara(MapDataAttr.RptSearchKeys, value);
	}

		///#endregion 报表属性(参数方式存储).


		///#region 外键属性
	public final String getVer() throws Exception
	{
		return this.GetValStringByKey(MapDataAttr.Ver);
	}
	public final void setVer(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.Ver, value);
	}
	/** 
	 顺序号
	*/
	public final int getIdx() throws Exception
	{
		return this.GetValIntByKey(MapDataAttr.Idx);
	}
	public final void setIdx(int value) throws Exception
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
	public final GroupFields getGroupFields() throws Exception
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
	 报表
	 * @throws Exception 
	*/
	public final FrmRpts getFrmRpts() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmRpts");
		FrmRpts obj = tempVar instanceof FrmRpts ? (FrmRpts)tempVar : null;
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
		Object tempVar = this.GetRefObject("FrmBtns");
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
		Object tempVar = this.GetRefObject("FrmImgs");
		FrmImgs obj = tempVar instanceof FrmImgs ? (FrmImgs)tempVar : null;
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
	/** 
	 物理表
	 * @throws Exception 
	*/
	public final String getPTable() throws Exception
	{
		String s = this.GetValStrByKey(MapDataAttr.PTable);
		if (s.equals("") || s == null)
		{
			return this.getNo();
		}
		return s;
	}
	public final void setPTable(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.PTable, value);
	}
	/** 
	 URL
	 * @throws Exception 
	*/
	public final String getUrl() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.Url);
	}
	public final void setUrl(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.Url, value);
	}
	public final DBUrlType getHisDBUrl()
	{
		return DBUrlType.AppCenterDSN;
	}
	public final AppType getHisAppType() throws Exception
	{
		return AppType.forValue(this.GetValIntByKey(MapDataAttr.AppType));
	}
	public final void setHisAppType(AppType value) throws Exception
	{
		this.SetValByKey(MapDataAttr.AppType, value.getValue());
	}
	/** 
	 备注
	 * @throws Exception 
	*/
	public final String getNote() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.Note);
	}
	public final void setNote(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.Note, value);
	}
	/** 
	 是否有CA.
	 * @throws Exception 
	*/
	public final boolean getIsHaveCA() throws Exception
	{
		return this.GetParaBoolen("IsHaveCA", false);

	}
	public final void setIsHaveCA(boolean value) throws Exception
	{
		this.SetPara("IsHaveCA", value);
	}
	/** 
	 类别，可以为空.
	 * @throws Exception 
	*/
	public final String getFK_FrmSort() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.FK_FrmSort);
	}
	public final void setFK_FrmSort(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.FK_FrmSort, value);
	}
	/** 
	 类别，可以为空.
	 * @throws Exception 
	*/
	public final String getFK_FormTree() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.FK_FormTree);
	}
	public final void setFK_FormTree(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.FK_FormTree, value);
	}
	/** 
	 从表集合.
	 * @throws Exception 
	*/
	public final String getDtls() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.Dtls);
	}
	public final void setDtls(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.Dtls, value);
	}
	/** 
	 主键
	 * @throws Exception 
	*/
	public final String getEnPK() throws Exception
	{
		String s = this.GetValStrByKey(MapDataAttr.EnPK);
		if (DataType.IsNullOrEmpty(s))
		{
			return "OID";
		}
		return s;
	}
	public final void setEnPK(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.EnPK, value);
	}
	private Entities _HisEns = null;
	public final Entities getHisEns() throws Exception
	{
		if (_HisEns == null)
		{
			_HisEns = BP.En.ClassFactory.GetEns(this.getNo());
		}
		return _HisEns;
	}
	public final Entity getHisEn() throws Exception
	{
		return this.getHisEns().getNewEntity();
	}
	public final float getFrmW() throws Exception
	{
		return this.GetValFloatByKey(MapDataAttr.FrmW);
	}
	public final void setFrmW(float value) throws Exception
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
	public final float getFrmH() throws Exception
	{
		return this.GetValFloatByKey(MapDataAttr.FrmH);
	}
	public final void setFrmH(float value) throws Exception
	{
		this.SetValByKey(MapDataAttr.FrmH, value);
	}
	/** 
	 表格显示的列
	 * @throws Exception 
	*/
	public final int getTableCol() throws Exception
	{
		int i = this.GetValIntByKey(MapDataAttr.TableCol);
		if (i == 0 || i == 1)
		{
			return 4;
		}
		return i;
	}
	public final void setTableCol(int value) throws Exception
	{
		this.SetValByKey(MapDataAttr.TableCol, value);
	}


		///#endregion


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
	 * @throws Exception 
	*/
	public MapDataExt(String no) throws Exception
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


			///#region 基本属性.
		map.AddTBStringPK(MapDataAttr.No, null, "表单编号", true, false, 1, 190, 20);
		map.AddTBString(MapDataAttr.Name, null, "表单名称", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.PTable, null, "存储表", true, false, 0, 500, 20);

			//表单的运行类型.
		map.AddDDLSysEnum(MapDataAttr.FrmType, FrmType.FreeFrm.getValue(), "表单类型",true, true, MapDataAttr.FrmType);

		map.AddTBString(MapDataAttr.Url, null, "URL连接(对嵌入式表单有效)", true, false, 0, 500, 20, true);
			//数据源.
		map.AddDDLEntities(MapDataAttr.DBSrc, "local", "数据源", new BP.Sys.SFDBSrcs(), true);

		map.AddDDLEntities(MapDataAttr.FK_FormTree, "01", "表单类别", new SysFormTrees(), true);

			///#endregion 基本属性.


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


			///#region 方法 - 基本功能.
		RefMethod rm = new RefMethod();
		rm.Title = "装载填充"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoPageLoadFull";
		rm.Icon = "../../WF/Img/FullData.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "表单事件"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoEvent";
		rm.Icon = "../../WF/Img/Event.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "批量设置验证规则";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.ClassMethodName = this.toString() + ".DoRegularExpressionBatch";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "批量修改字段"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoBatchEditAttr";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/field.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		   // map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "傻瓜表单设计器";
		rm.ClassMethodName = this.toString() + ".DoDesignerFool";
		rm.Icon = "../../WF/Img/FileType/xlsx.gif";
		rm.Visable = true;
		rm.Target = "_blank";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "手机端表单";
		rm.Icon = "../../WF/Img/Btn/DTS.gif";
		rm.ClassMethodName = this.toString() + ".DoSortingMapAttrs";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "JS编程"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoInitScript";
		rm.Icon = "../../WF/Img/Script.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "表单body属性"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoBodyAttr";
		rm.Icon = "../../WF/Img/Script.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "导出XML表单模版"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoExp";
		rm.Icon = "../../WF/Img/Export.png";
		rm.Visable = true;
		rm.RefAttrLinkLabel = "导出到xml";
		rm.refMethodType = RefMethodType.RightFrameOpen;
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
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		map.AddRefMethod(rm);

			///#endregion 方法 - 基本功能.



			///#region 方法 - 开发接口.
		rm = new RefMethod();
		rm.Title = "调用查询API"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoSearch";
		rm.Icon = "../../WF/Img/Table.gif";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "调用分析API"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoGroup";
		rm.Icon = "../../WF/Img/Table.gif";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

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

		///#endregion


		///#region 基本方法.
	/** 
	 傻瓜表单设计器
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoDesignerFool() throws Exception
	{
		return "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData= " + this.getNo()+ " &IsFirst=1&MyPK= " + this.getNo()+ " &IsEditMapData=True";
	}

		///#endregion


		///#region 方法.
	/** 
	 重命名
	 
	 @param frmID1
	 @param frmID2
	 @return 
	 * @throws Exception 
	*/
	public final String DoChangeFrmID(String frmID1, String frmID2) throws Exception
	{
		MapData md = new MapData();
		md.setNo(frmID1);
		if (md.getIsExits() == true)
		{
			return "表单ID【" + frmID1 + "】已经存在";
		}

		if (!frmID1.equals(frmID2))
		{
			return "两次输入的ID不一致.";
		}


		String frmIDOld = this.getNo();

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
	 * @throws Exception 
	*/
	public final String DoChangeFieldName(String fieldOld, String newField, String newFieldName) throws Exception
	{
		MapAttr attrOld = new MapAttr();
		attrOld.setKeyOfEn(fieldOld);
		attrOld.setFK_MapData(this.getNo());
		attrOld.setMyPK(attrOld.getFK_MapData() + "_" + attrOld.getKeyOfEn());
		if (attrOld.RetrieveFromDBSources() == 0)
		{
			return "@旧字段输入错误[" + attrOld.getKeyOfEn() + "].";
		}

		//检查是否存在该字段？
		MapAttr attrNew = new MapAttr();
		attrNew.setKeyOfEn(newField);
		attrNew.setFK_MapData(this.getNo());
		attrNew.setMyPK(attrNew.getFK_MapData() + "_" + attrNew.getKeyOfEn());
		if (attrNew.RetrieveFromDBSources() == 1)
		{
			return "@该字段[" + attrNew.getKeyOfEn() + "]已经存在.";
		}

		//删除旧数据.
		attrOld.Delete();

		//copy这个数据,增加上它.
		attrNew.Copy(attrOld);
		attrNew.setKeyOfEn(newField);
		attrNew.setFK_MapData(this.getNo());

		if (!newFieldName.equals(""))
		{
			attrNew.setName(newFieldName);
		}

		attrNew.Insert();

		//更新处理他的相关业务逻辑.
		MapExts exts = new MapExts(this.getNo());
		for (MapExt item : exts.ToJavaList())
		{
			item.setMyPK(item.getMyPK().replace("_" + fieldOld, "_" + newField));

			if (fieldOld.equals(item.getAttrOfOper()))
			{
				item.setAttrOfOper(newField);
			}

			if (fieldOld.equals(item.getAttrsOfActive()))
			{
				item.setAttrsOfActive(newField);
			}

			item.setTag(item.getTag().replace(fieldOld, newField));
			item.setTag1(item.getTag1().replace(fieldOld, newField));
			item.setTag2(item.getTag2().replace(fieldOld, newField));
			item.setTag3(item.getTag3().replace(fieldOld, newField));

			item.setAtPara(item.getAtPara().replace(fieldOld, newField));
			item.setDoc(item.getDoc().replace(fieldOld, newField));
			item.Save();
		}
		return "执行成功";
	}

	/** 
	 检查表单
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoCheckFrm() throws Exception
	{
		return "../../Admin/AttrNode/CheckFrm.htm?FK_MapData= " + this.getNo()+ " &t=" + DateUtils.format(new Date(),"yyyyMMddHHmmssffffff");
	}

	/** 
	 批量修改字段
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoBatchEditAttr() throws Exception
	{
		return "../../Admin/FoolFormDesigner/BatchEdit.aspx?FK_MapData= " + this.getNo()+ " &t=" + DataType.getCurrentDataTime();
	}
	/** 
	 批量设置正则表达式规则.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoRegularExpressionBatch() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/RegularExpressionBatch.htm?FK_Flow=&FK_MapData= " + this.getNo()+ " &t=" + DataType.getCurrentDataTime();
	}
	/** 
	 排序字段顺序
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoSortingMapAttrs() throws Exception
	{
		return "../../Admin/AttrNode/SortingMapAttrs.htm?FK_Flow=&FK_MapData= " + this.getNo()+ " &t=" + DataType.getCurrentDataTime();
	}
	  /** 
	 设计表单
	 
	 @return 
	 * @throws Exception 
	  */
	public final String DoDFrom() throws Exception
	{
		return "../../Admin/FoolFormDesigner/CCForm/Frm.htm?FK_MapData= " + this.getNo()+ " &UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID() + "&AppCenterDBType=" + DBAccess.getAppCenterDBType() + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
	}
	/** 
	 设计傻瓜表单
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoDFromCol4() throws Exception
	{
		return "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData= " + this.getNo()+ " &IsFirst=1&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID() + "&AppCenterDBType=" + DBAccess.getAppCenterDBType() + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
	}
	/** 
	 查询
	 
	 @return 
	*/
	public final String DoSearch() throws Exception
	{
		return "../../Comm/Search.htm?s=34&FK_MapData= " + this.getNo()+ " &EnsName=" + this.getNo();
	}
	/** 
	 调用分析API
	 
	 @return 
	*/
	public final String DoGroup() throws Exception
	{
		return "../../Comm/Group.htm?s=34&FK_MapData= " + this.getNo()+ " &EnsName=" + this.getNo();
	}
	/** 
	 数据源管理
	 
	 @return 
	*/
	public final String DoDBSrc() throws Exception
	{
		return "../../Comm/Search.htm?s=34&FK_MapData= " + this.getNo()+ " &EnsName=BP.Sys.SFDBSrcs";
	}
	public final String DoWordFrm() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/WordFrm.aspx?s=34&FK_MapData= " + this.getNo()+ " &ExtType=WordFrm&RefNo=";
	}

	public final String DoPageLoadFull() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/PageLoadFull.htm?s=34&FK_MapData= " + this.getNo()+ " &ExtType=PageLoadFull&RefNo=";
	}
	public final String DoInitScript() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/InitScript.htm?s=34&FK_MapData= " + this.getNo()+ " &ExtType=PageLoadFull&RefNo=";
	}
	/** 
	 表单属性.
	 
	 @return 
	*/
	public final String DoBodyAttr() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/BodyAttr.htm?s=34&FK_MapData= " + this.getNo()+ " &ExtType=BodyAttr&RefNo=";
	}
	/** 
	 表单事件
	 
	 @return 
	*/
	public final String DoEvent() throws Exception
	{
		return "../../Admin/CCFormDesigner/Action.htm?FK_MapData= " + this.getNo()+ " &T=sd&FK_Node=0";
	}

	/** 
	 导出表单
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoExp() throws Exception
	{
		return "../../Admin/FoolFormDesigner/ImpExp/Exp.htm?FK_MapData=" + this.getNo();
	}

		///#endregion 方法.
}
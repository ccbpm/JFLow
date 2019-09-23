package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 Excel表单属性
*/
public class MapFrmExcel extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 文件模版属性.
	/** 
	 模版版本号
	*/
	public final String getTemplaterVer()
	{
		return this.GetValStringByKey(MapFrmExcelAttr.TemplaterVer);
	}
	public final void setTemplaterVer(String value)
	{
		this.SetValByKey(MapFrmExcelAttr.TemplaterVer, value);
	}
	/** 
	 Excel数据存储字段
	 为了处理多个excel文件映射到同一张表上.
	*/
	public final String getDBSave()
	{
		String str = this.GetValStringByKey(MapFrmExcelAttr.DBSave);
		if (DataType.IsNullOrEmpty(str))
		{
			return "DBFile";
		}
		return str;
	}
	public final void setDBSave(String value)
	{
		this.SetValByKey(MapFrmExcelAttr.DBSave, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 文件模版属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 是否是节点表单?
	*/
	public final boolean getIsNodeFrm()
	{
		if (this.No.Contains("ND") == false)
		{
			return false;
		}

		if (this.No.Contains("Rpt") == true)
		{
			return false;
		}

		if (this.No.substring(0, 2).equals("ND"))
		{
			return true;
		}

		return false;
	}
	/** 
	 节点ID.
	*/
	public final int getNodeID()
	{
		return Integer.parseInt(this.No.Replace("ND", ""));
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

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
		///#region 构造方法
	/** 
	 Excel表单属性
	*/
	public MapFrmExcel()
	{
	}
	/** 
	 Excel表单属性
	 
	 @param no 表单ID
	*/
	public MapFrmExcel(String no)
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
		Map map = new Map("Sys_MapData", "Excel表单属性");
		map.Java_SetEnType(EnType.Sys);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基本属性.
		map.AddTBStringPK(MapFrmExcelAttr.No, null, "表单编号", true, true, 1, 190, 20);
		map.AddTBString(MapFrmExcelAttr.PTable, null, "存储表", true, false, 0, 100, 20);
		map.AddTBString(MapFrmExcelAttr.Name, null, "表单名称", true, false, 0, 500, 20, true);

			//数据源.
		map.AddDDLEntities(MapFrmExcelAttr.DBSrc, "local", "数据源", new BP.Sys.SFDBSrcs(), true);
		map.AddDDLEntities(MapFrmExcelAttr.FK_FormTree, "01", "表单类别", new SysFormTrees(), true);

			//表单的运行类型.
		map.AddDDLSysEnum(MapFrmExcelAttr.FrmType, (int)BP.Sys.FrmType.FreeFrm, "表单类型", true, false, MapFrmExcelAttr.FrmType);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基本属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 模版属性。
		map.AddTBString(MapFrmExcelAttr.TemplaterVer, null, "模版编号", true, false, 0, 30, 20);
		map.AddTBString(MapFrmExcelAttr.DBSave, null, "Excel数据文件存储", true, false, 0, 50, 20);
		map.SetHelperAlert(MapFrmExcelAttr.DBSave, "二进制的excel文件存储到表的那个字段里面？默认为DBFile, 如果此表对应多个excel文件就会导致二进制excel文件存储覆盖.");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 模版属性。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 设计者信息.
		map.AddTBString(MapFrmExcelAttr.Designer, null, "设计者", true, false, 0, 500, 20);
		map.AddTBString(MapFrmExcelAttr.DesignerContact, null, "联系方式", true, false, 0, 500, 20);
		map.AddTBString(MapFrmExcelAttr.DesignerUnit, null, "单位", true, false, 0, 500, 20, true);
		map.AddTBString(MapFrmExcelAttr.GUID, null, "GUID", true, true, 0, 128, 20, false);
		map.AddTBString(MapFrmExcelAttr.Ver, null, "版本号", true, true, 0, 30, 20);
		//	map.AddTBString(MapFrmFreeAttr.DesignerTool, null, "表单设计器", true, true, 0, 30, 20);

		map.AddTBStringDoc(MapFrmExcelAttr.Note, null, "备注", true, false, true);

			//增加参数字段.
		map.AddTBAtParas(4000);
		map.AddTBInt(MapFrmExcelAttr.Idx, 100, "顺序号", false, false);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 设计者信息.

		map.AddMyFile("表单模版", null, SystemConfig.PathOfDataUser + "\\FrmOfficeTemplate\\");

			//查询条件.
		map.AddSearchAttr(MapFrmExcelAttr.DBSrc);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 方法 - 基本功能.
		RefMethod rm = new RefMethod();

			/* 2017-04-28 10:52:03
			 * Mayy
			 * 去掉此功能（废弃，因在线编辑必须使用ActiveX控件，适用性、稳定性太差）
			rm = new RefMethod();
			rm.Title = "编辑Excel表单模版";
			rm.ClassMethodName = this.ToString() + ".DoEditExcelTemplate";
			rm.Icon = ../../Img/FileType/xlsx.gif";
			rm.Visable = true;
			rm.Target = "_blank";
			rm.RefMethodType = RefMethodType.RightFrameOpen;
			map.AddRefMethod(rm);
			 */

		rm = new RefMethod();
		rm.Title = "启动傻瓜表单设计器";
		rm.ClassMethodName = this.toString() + ".DoDesignerFool";
		rm.Icon = "../../WF/Img/FileType/xlsx.gif";
		rm.Visable = true;
		rm.Target = "_blank";
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "字段维护";
		rm.ClassMethodName = this.toString() + ".DoEditFiledsList";
		rm.Icon = "../../WF/Img/FileType/xlsx.gif";
			// rm.Icon = ../../Admin/CCBPMDesigner/Img/Field.png";
		rm.Visable = true;
		rm.Target = "_blank";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
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
		rm.Icon = "../../WF/Img/RegularExpression.png";
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
			//map.AddRefMethod(rm);


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
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "../../WF/Img/Export.png";
		rm.Visable = true;
		rm.RefAttrLinkLabel = "导出到xml";
		rm.Target = "_blank";
		map.AddRefMethod(rm);




			//rm = new RefMethod();
			//rm.Title = "节点表单组件"; // "设计表单";
			//rm.ClassMethodName = this.ToString() + ".DoNodeFrmCompent";
			//rm.Visable = true;
			//rm.RefAttrLinkLabel = "节点表单组件";
			//rm.RefMethodType = RefMethodType.RightFrameOpen;
			//rm.Target = "_blank";
			//rm.Icon = ../../Img/Components.png";
			//map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 方法 - 基本功能.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 高级设置.

			//带有参数的方法.
		rm = new RefMethod();
		rm.Title = "重命名字段";
		rm.GroupName = "高级设置";
		rm.getHisAttrs().AddTBString("FieldOld", null, "旧字段英文名", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("FieldNew", null, "新字段英文名", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("FieldNewName", null, "新字段中文名", true, false, 0, 100, 100);
		rm.ClassMethodName = this.toString() + ".DoChangeFieldName";
		rm.Icon = "../../WF/Img/ReName.png";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "手机端表单";
		rm.GroupName = "高级设置";
		rm.Icon = "../../WF/Admin/CCFormDesigner/Img/telephone.png";
		rm.ClassMethodName = this.toString() + ".DoSortingMapAttrs";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "Excel表单属性";
		rm.GroupName = "高级设置";
		rm.ClassMethodName = this.toString() + ".DoMapExcel";
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 高级设置.

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

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 节点表单方法.

	public final String DoMapExcel()
	{
		return SystemConfig.CCFlowWebPath + "WF/Comm/En.htm?EnName=BP.WF.Template.MapFrmExcel&No=" + this.No;
	}
	public final String DoDesignerFool()
	{
		return "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData= " + this.getNo()+ " &MyPK= " + this.getNo()+ " &IsEditMapData=True&IsFirst=1";
	}
	public final String DoEditExcelTemplate()
	{
		return "../../Admin/CCFormDesigner/ExcelFrmDesigner/Designer.htm?FK_MapData=" + this.No;
	}
	/** 
	 表单字段.
	 
	 @return 
	*/
	public final String DoEditFiledsList()
	{
		return "../../Admin/FoolFormDesigner/BatchEdit.htm?FK_MapData=" + this.No;
	}
	/** 
	 节点表单组件
	 
	 @return 
	*/
	public final String DoNodeFrmCompent()
	{
		if (this.No.Contains("ND") == true)
		{
			return "../../Comm/EnOnly.htm?EnName=BP.WF.Template.FrmNodeComponent&PK=" + this.No.Replace("ND", "") + "&t=" + DataType.getCurrentDataTime();
		}
		else
		{
			return "../../Admin/FoolFormDesigner/Do.htm&DoType=FWCShowError";
		}
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 通用方法.
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
	 批量设置正则表达式规则.
	 
	 @return 
	*/
	public final String DoRegularExpressionBatch()
	{
		return "../../Admin/FoolFormDesigner/MapExt/RegularExpressionBatch.htm?FK_Flow=&FK_MapData= " + this.getNo()+ " &t=" + DataType.getCurrentDataTime();
	}
	/** 
	 批量修改字段
	 
	 @return 
	*/
	public final String DoBatchEditAttr()
	{
		return "../../Admin/FoolFormDesigner/BatchEdit.htm?FK_MapData= " + this.getNo()+ " &t=" + DataType.getCurrentDataTime();
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
		String url = "../../Admin/FoolFormDesigner/CCForm/Frm.htm?FK_MapData= " + this.getNo()+ " &UserNo=" + WebUser.getNo() + "&SID=" + Web.WebUser.SID + "&AppCenterDBType=" + BP.DA.DBAccess.AppCenterDBType + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
		return url;
	}
	/** 
	 设计傻瓜表单
	 
	 @return 
	*/
	public final String DoDFromCol4()
	{
		String url = "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData= " + this.getNo()+ " &UserNo=" + WebUser.getNo() + "&SID=" + Web.WebUser.SID + "&AppCenterDBType=" + BP.DA.DBAccess.AppCenterDBType + "&IsFirst=1&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
		return url;
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
	 Excel表单属性.
	 
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
package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 自由表单属性
*/
public class MapFrmNode extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 文件模版属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 文件模版属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 表单事件实体
	*/
	public final String getFromEventEntity()
	{
		return this.GetValStrByKey(MapDataAttr.FormEventEntity);
	}
	public final void setFromEventEntity(String value)
	{
		this.SetValByKey(MapDataAttr.FormEventEntity,value);
	}
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


	/** 
	 表格显示的列
	*/
	public final int getTableCol()
	{
		return 4;
		int i = this.GetValIntByKey(MapFrmNodeAttr.TableCol);
		if (i == 0 || i == 1)
		{
			return 4;
		}
		return i;
	}
	public final void setTableCol(int value)
	{
		this.SetValByKey(MapFrmNodeAttr.TableCol, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 权限控制.
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		if (BP.Web.WebUser.No.equals("admin"))
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
	 自由表单属性
	*/
	public MapFrmNode()
	{
	}
	/** 
	 自由表单属性
	 
	 @param no 表单ID
	*/
	public MapFrmNode(String no)
	{
		super(no);
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("Sys_MapData", "自由表单属性");
		map.Java_SetEnType(EnType.Sys);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基本属性.
		map.AddTBStringPK(MapFrmNodeAttr.No, null, "表单编号", true, true, 1, 190, 20);
		map.AddTBString(MapFrmNodeAttr.PTable, null, "存储表", true, false, 0, 100, 20);
		map.AddTBString(MapFrmNodeAttr.Name, null, "表单名称", true, false, 0, 200, 20,true);
		map.AddTBString(MapDataAttr.FormEventEntity, null, "事件实体", true, true, 0, 100, 20, true);

			//数据源.
		map.AddDDLEntities(MapFrmNodeAttr.DBSrc, "local", "数据源", new BP.Sys.SFDBSrcs(), true);

			//宽度高度.
		map.AddTBInt(MapFrmNodeAttr.FrmW, 900, "宽度", true, false);
		map.AddTBInt(MapFrmNodeAttr.FrmH, 1200, "高度", true, false);

			//表单的运行类型.
		map.AddDDLSysEnum(MapFrmNodeAttr.FrmType, (int)BP.Sys.FrmType.FreeFrm, "表单类型", true, false, MapFrmNodeAttr.FrmType);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基本属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 设计者信息.
		map.AddTBString(MapFrmNodeAttr.Designer, null, "设计者", true, false, 0, 500, 20);
		map.AddTBString(MapFrmNodeAttr.DesignerContact, null, "联系方式", true, false, 0, 500, 20);
		map.AddTBString(MapFrmNodeAttr.DesignerUnit, null, "单位", true, false, 0, 500, 20, false);
		map.AddTBString(MapFrmNodeAttr.GUID, null, "GUID", true, true, 0, 128, 20, false);
		map.AddTBString(MapFrmNodeAttr.Ver, null, "版本号", true, true, 0, 30, 20);
		map.AddTBStringDoc(MapFrmNodeAttr.Note, null, "备注", true, false, true);

			//增加参数字段.
		map.AddTBAtParas(4000);
		map.AddTBInt(MapFrmNodeAttr.Idx, 100, "顺序号", false, false);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 设计者信息.

		map.AddMyFile("表单模版");

			//查询条件.
		map.AddSearchAttr(MapFrmNodeAttr.DBSrc);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 方法 - 基本功能.
		RefMethod rm = new RefMethod();

		rm = new RefMethod();
		rm.Title = "启动傻瓜表单设计器";
		rm.ClassMethodName = this.toString() + ".DoDesignerFool";
		rm.Icon = "../../WF/Img/FileType/xlsx.gif";
		rm.Visable = true;
		rm.Target = "_blank";
		rm.RefMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "字段维护";
			//rm.ClassMethodName = this.ToString() + ".DoEditFiledsList";
			//rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/field.png";
			//rm.Visable = true;
			//rm.Target = "_blank";
			//rm.RefMethodType = RefMethodType.RightFrameOpen;
			//map.AddRefMethod(rm);


			//rm = new RefMethod();
			//rm.Title = "批量修改字段"; // "设计表单";
			//rm.ClassMethodName = this.ToString() + ".DoBatchEditAttr";
			//rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/field.png";
			//rm.Visable = true;
			//rm.RefMethodType = RefMethodType.RightFrameOpen;
			//rm.Target = "_blank";
			//map.AddRefMethod(rm);

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
		   // map.AddRefMethod(rm);

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
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "Tab顺序键"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoTabIdx";
		rm.Visable = true;
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);
		rm = new RefMethod();
		rm.Title = "模板打印";
		rm.ClassMethodName = this.toString() + ".DoBill";
		rm.Icon = "../../WF/Img/FileType/doc.gif";
		rm.RefMethodType = RefMethodType.RightFrameOpen;

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
		 //   rm.GroupName = "高级设置";
		rm.HisAttrs.AddTBString("FieldOld", null, "旧字段英文名", true, false, 0, 100, 100);
		rm.HisAttrs.AddTBString("FieldNew", null, "新字段英文名", true, false, 0, 100, 100);
		rm.HisAttrs.AddTBString("FieldNewName", null, "新字段中文名", true, false, 0, 100, 100);
		rm.ClassMethodName = this.toString() + ".DoChangeFieldName";
		rm.Icon = "../../WF/Img/ReName.png";
		rm.GroupName = "高级设置";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "重命表单ID";
		  //  rm.GroupName = "高级设置";
		rm.HisAttrs.AddTBString("NewFrmID1", null, "新表单ID名称", true, false, 0, 100, 100);
		rm.HisAttrs.AddTBString("NewFrmID2", null, "确认表单ID名称", true, false, 0, 100, 100);
		rm.ClassMethodName = this.toString() + ".DoChangeFrmID";
		rm.Icon = "../../WF/Img/ReName.png";
		rm.GroupName = "高级设置";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "复制表单";
			//  rm.GroupName = "高级设置";
		rm.HisAttrs.AddTBString("FrmID", null, "要复制新表单ID", true, false, 0, 100, 100);
		rm.HisAttrs.AddTBString("FrmName", null, "表单名称", true, false, 0, 100, 100);
		rm.HisAttrs.AddDDLEntities("FrmTree", null, "复制到表单目录", new FrmTrees(),true);

		rm.ClassMethodName = this.toString() + ".DoCopyFrm";
		rm.Icon = "../../WF/Img/Btn/Copy.GIF";
		rm.GroupName = "高级设置";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "手机端表单";
		rm.GroupName = "高级设置";
		rm.Icon = "../../WF/Admin/CCFormDesigner/Img/telephone.png";
		rm.ClassMethodName = this.toString() + ".DoSortingMapAttrs";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
			//map.AddRefMethod(rm);
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
			//map.AddRefMethod(rm);

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

		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


	/** 
	 单据打印
	 
	 @return 
	*/
	public final String DoBill()
	{
		return "../../Admin/AttrNode/Bill.htm?FK_MapData=" + this.No + "&NodeID=" + this.getNodeID() + "&FK_Node=" + this.getNodeID();
	}


	@Override
	protected boolean beforeUpdate()
	{
		//注册事件表单实体.
		BP.Sys.FormEventBase feb = BP.Sys.Glo.GetFormEventBaseByEnName(this.No);
		if (feb == null)
		{
			this.setFromEventEntity("");
		}
		else
		{

			this.setFromEventEntity(feb.toString());
		}

		return super.beforeUpdate();
	}
	public final String DoTabIdx()
	{
		return SystemConfig.CCFlowWebPath + "WF/Admin/FoolFormDesigner/TabIdx.htm?FK_MapData=" + this.No;
	}

	/** 
	 复制表单
	 
	 @return 
	*/
	public final String DoCopyFrm(String frmID, String frmName, String fk_frmTree)
	{
		return BP.Sys.CCFormAPI.CopyFrm(this.No, frmID, frmName, fk_frmTree);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 节点表单方法.

	/** 
	 傻瓜表单设计器
	 
	 @return 
	*/
	public final String DoDesignerFool()
	{
		return "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData=" + this.No + "&MyPK=" + this.No + "&IsEditMapData=True&DoDesignerFool&IsFirst=1";
	}
	/** 
	 编辑excel模版.
	 
	 @return 
	*/
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 通用方法.
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
		attrOld.FK_MapData = this.No;
		attrOld.MyPK = attrOld.FK_MapData + "_" + attrOld.KeyOfEn;
		if (attrOld.RetrieveFromDBSources() == 0)
		{
			return "@旧字段输入错误[" + attrOld.KeyOfEn + "].";
		}

		//检查是否存在该字段？
		MapAttr attrNew = new MapAttr();
		attrNew.KeyOfEn = newField;
		attrNew.FK_MapData = this.No;
		attrNew.MyPK = attrNew.FK_MapData + "_" + attrNew.KeyOfEn;
		if (attrNew.RetrieveFromDBSources() == 1)
		{
			return "@该字段[" + attrNew.KeyOfEn + "]已经存在.";
		}

		//删除旧数据.
		attrOld.Delete();

		//copy这个数据,增加上它.
		attrNew.Copy(attrOld);
		attrNew.KeyOfEn = newField;
		attrNew.FK_MapData = this.No;

		if (!newFieldName.equals(""))
		{
			attrNew.Name = newFieldName;
		}

		attrNew.Insert();

		//更新处理他的相关业务逻辑.
		MapExts exts = new MapExts(this.No);
		for (MapExt item : exts)
		{
			item.MyPK = item.MyPK.Replace("_" + fieldOld, "_" + newField);

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
		return "../../Admin/FoolFormDesigner/MapExt/RegularExpressionBatch.htm?FK_Flow=&FK_MapData=" + this.No + "&t=" + DataType.CurrentDataTime;
	}
	/** 
	 批量修改字段
	 
	 @return 
	*/
	public final String DoBatchEditAttr()
	{
		return "../../Admin/FoolFormDesigner/BatchEdit.htm?FK_MapData=" + this.No + "&t=" + DataType.CurrentDataTime;
	}
	/** 
	 排序字段顺序
	 
	 @return 
	*/
	public final String DoSortingMapAttrs()
	{
		return "../../Admin/AttrNode/SortingMapAttrs.htm?FK_Flow=&FK_MapData=" + this.No + "&t=" + DataType.CurrentDataTime;
	}
	/** 
	 设计表单
	 
	 @return 
	*/
	public final String DoDFrom()
	{
		String url = "../../Admin/FoolFormDesigner/CCForm/Frm.htm?FK_MapData=" + this.No + "&UserNo=" + BP.Web.WebUser.No + "&SID=" + Web.WebUser.SID + "&AppCenterDBType=" + BP.DA.DBAccess.AppCenterDBType + "&CustomerNo=" + BP.Sys.SystemConfig.CustomerNo;
		return url;
	}
	/** 
	 设计傻瓜表单
	 
	 @return 
	*/
	public final String DoDFromCol4()
	{
		String url = "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData=" + this.No + "&UserNo=" + BP.Web.WebUser.No + "&SID=" + Web.WebUser.SID + "&AppCenterDBType=" + BP.DA.DBAccess.AppCenterDBType + "&IsFirst=1&CustomerNo=" + BP.Sys.SystemConfig.CustomerNo;
		return url;
	}
	/** 
	 查询
	 
	 @return 
	*/
	public final String DoSearch()
	{
		return "../../Comm/Search.htm?s=34&FK_MapData=" + this.No + "&EnsName=" + this.No;
	}
	/** 
	 调用分析API
	 
	 @return 
	*/
	public final String DoGroup()
	{
		return "../../Comm/Group.htm?s=34&FK_MapData=" + this.No + "&EnsName=" + this.No;
	}
	/** 
	 数据源管理
	 
	 @return 
	*/
	public final String DoDBSrc()
	{
		return "../../Comm/Search.htm?s=34&FK_MapData=" + this.No + "&EnsName=BP.Sys.SFDBSrcs";
	}
	public final String DoWordFrm()
	{
		return "../../Admin/FoolFormDesigner/MapExt/WordFrm.aspx?s=34&FK_MapData=" + this.No + "&ExtType=WordFrm&RefNo=";
	}

	public final String DoPageLoadFull()
	{
		return "../../Admin/FoolFormDesigner/MapExt/PageLoadFull.htm?s=34&FK_MapData=" + this.No + "&ExtType=PageLoadFull&RefNo=";
	}
	public final String DoInitScript()
	{
		return "../../Admin/FoolFormDesigner/MapExt/InitScript.htm?s=34&FK_MapData=" + this.No + "&ExtType=PageLoadFull&RefNo=";
	}
	/** 
	 自由表单属性.
	 
	 @return 
	*/
	public final String DoBodyAttr()
	{
		return "../../Admin/FoolFormDesigner/MapExt/BodyAttr.htm?s=34&FK_MapData=" + this.No + "&ExtType=BodyAttr&RefNo=";
	}
	/** 
	 表单事件
	 
	 @return 
	*/
	public final String DoEvent()
	{
		return "../../Admin/CCFormDesigner/Action.htm?FK_MapData=" + this.No + "&T=sd&FK_Node=0";
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
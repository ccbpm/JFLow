package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 傻瓜表单属性
*/
public class MapFrmFool extends EntityNoName
{
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

		if (this.No.substring(0, 2).equals("ND") && this.No.Contains("Dtl") == false)
		{
			return true;
		}

		return false;
	}
	/** 
	 物理存储表
	*/
	public final String getPTable()
	{
		return this.GetValStrByKey(MapDataAttr.PTable);
	}
	public final void setPTable(String value)
	{
		this.SetValByKey(MapDataAttr.PTable, value);
	}
	/** 
	 节点ID.
	*/
	public final int getNodeID()
	{
		if (this.No.indexOf("ND") != 0)
		{
			return 0;
		}
		return Integer.parseInt(this.No.Replace("ND", ""));
	}

	/** 
	 表格显示的列
	*/
	public final int getTableCol()
	{
		return 4;
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
	 傻瓜表单属性
	*/
	public MapFrmFool()
	{
	}
	/** 
	 傻瓜表单属性
	 
	 @param no 表单ID
	*/
	public MapFrmFool(String no)
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

		Map map = new Map("Sys_MapData", "傻瓜表单属性");
		map.Java_SetEnType(EnType.Sys);
		map.Java_SetCodeStruct("4");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基本属性.

		map.AddTBStringPK(MapDataAttr.No, null, "表单编号", true, true, 1, 190, 20);

		map.AddTBString(MapDataAttr.PTable, null, "存储表", true, false, 0, 100, 20);
		map.AddTBString(MapDataAttr.Name, null, "表单名称", true, false, 0, 500, 20, true);

		map.AddDDLSysEnum(MapDataAttr.TableCol, 0, "表单显示列数", true, true, "显示方式", "@0=4列@1=6列@2=上下模式3列");

		  //  map.AddTBInt(MapDataAttr.TableWidth, 900, "傻瓜表单宽度", true, false);
		   // map.AddTBInt(MapDataAttr.TableHeight, 900, "傻瓜表单高度", true, false);

		map.AddTBInt(MapDataAttr.FrmW, 900, "表单宽度", true, false);
		map.AddTBInt(MapDataAttr.FrmH, 900, "表单高度", true, false);

			//数据源.
		map.AddDDLEntities(MapDataAttr.DBSrc, "local", "数据源", new BP.Sys.SFDBSrcs(), true);
		map.AddDDLEntities(MapDataAttr.FK_FormTree, "01", "表单类别", new SysFormTrees(), true);
			//表单的运行类型.
		map.AddDDLSysEnum(MapDataAttr.FrmType, (int)BP.Sys.FrmType.FreeFrm, "表单类型", true, false, MapDataAttr.FrmType);
			//表单解析 0 普通 1 页签展示
		map.AddDDLSysEnum(MapDataAttr.FrmShowType, 0, "表单展示方式", true, true, "表单展示方式", "@0=普通方式@1=页签方式");

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基本属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 设计者信息.
		map.AddTBString(MapDataAttr.Designer, null, "设计者", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.DesignerContact, null, "联系方式", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.DesignerUnit, null, "单位", true, false, 0, 500, 20, true);
		map.AddTBString(MapDataAttr.GUID, null, "GUID", true, true, 0, 128, 20, false);
		map.AddTBString(MapDataAttr.Ver, null, "版本号", true, true, 0, 30, 20);
		   // map.AddTBString(MapFrmFreeAttr.DesignerTool, null, "表单设计器", true, true, 0, 30, 20);

		map.AddTBString(MapDataAttr.Note, null, "备注", true, false,0,400,100, true);
			//增加参数字段.
		map.AddTBAtParas(4000);
		map.AddTBInt(MapDataAttr.Idx, 100, "顺序号", false, false);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 设计者信息.

			//查询条件.
		map.AddSearchAttr(MapDataAttr.DBSrc);

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
		  //  map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "手机端表单";
		rm.Icon = "../../WF/Admin/CCFormDesigner/Img/telephone.png";
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
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "../../WF/Img/Export.png";
		rm.Visable = true;
		rm.RefAttrLinkLabel = "导出到xml";
		rm.Target = "_blank";
		map.AddRefMethod(rm);


			//带有参数的方法.
		rm = new RefMethod();
		rm.Title = "重命名字段";
		rm.HisAttrs.AddTBString("FieldOld", null, "旧字段英文名", true, false, 0, 100, 100);
		rm.HisAttrs.AddTBString("FieldNew", null, "新字段英文名", true, false, 0, 100, 100);
		rm.HisAttrs.AddTBString("FieldNewName", null, "新字段中文名", true, false, 0, 100, 100);
		rm.ClassMethodName = this.toString() + ".DoChangeFieldName";
		rm.Icon = "../../WF/Img/ReName.png";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "表单检查"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoCheckFixFrmForUpdateVer";
		rm.Visable = true;
		rm.RefAttrLinkLabel = "表单检查";
		rm.Icon = "../../WF/Img/Check.png";
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "模板打印";
		rm.ClassMethodName = this.toString() + ".DoBill";
		rm.Icon = "../../WF/Img/FileType/doc.gif";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "模板打印2019";
		rm.ClassMethodName = this.toString() + ".DoBill2019";
		rm.Icon = "../../WF/Img/FileType/doc.gif";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 方法 - 基本功能.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 高级功能.
			//@李国文.
		rm = new RefMethod();
		rm.Title = "改变表单类型";
		rm.GroupName = "高级功能";
		rm.ClassMethodName = this.toString() + ".DoChangeFrmType()";
		rm.HisAttrs.AddDDLSysEnum("FrmType", 0, "修改表单类型", true, true);
		map.AddRefMethod(rm);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

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

		this._enMap = map;
		return this._enMap;
	}
	/** 
	 删除后清缓存
	*/
	@Override
	protected void afterDelete()
	{
		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.No);
		super.afterDelete();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 高级设置.
	/** 
	 改变表单类型 @李国文 ，需要搬到jflow.并测试.
	 
	 @param val 要改变的类型
	 @return 
	*/
	public final String DoChangeFrmType(int val)
	{
		MapData md = new MapData(this.No);
		String str = "原来的是:" + md.HisFrmTypeText + "类型，";
		md.HisFrmTypeInt = val;
		str += "现在修改为：" + md.HisFrmTypeText + "类型";
		md.Update();

		return str;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 高级设置.


	@Override
	protected boolean beforeUpdate()
	{
		//注册事件表单实体.
		//BP.Sys.FormEventBase feb = BP.Sys.Glo.GetFormEventBaseByEnName(this.No);
		//if (feb == null)
		//    this.FromEventEntity = "";
		//else

		//    this.FromEventEntity = feb.ToString();

		return super.beforeUpdate();
	}
	@Override
	protected void afterUpdate()
	{
		//修改关联明细表
		MapDtl dtl = new MapDtl();
		dtl.No = this.No;
		if (dtl.RetrieveFromDBSources() == 1)
		{
			dtl.Name = this.Name;
			dtl.PTable = this.getPTable();
			dtl.DirectUpdate();

			MapData map = new MapData(this.No);
			//避免显示在表单库中
			map.FK_FrmSort = "";
			map.FK_FormTree = "";
			map.DirectUpdate();
		}

		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.No);

		super.afterUpdate();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 节点表单方法.
	/** 
	 单据打印
	 
	 @return 
	*/
	public final String DoBill()
	{
		return "../../Admin/AttrNode/Bill.htm?FK_MapData=" + this.No + "&NodeID=" + this.getNodeID() + "&FK_Node=" + this.getNodeID();
	}
	/** 
	 单据打印
	 
	 @return 
	*/
	public final String DoBill2019()
	{
		return "../../Admin/AttrNode/Bill2019.htm?FK_MapData=" + this.No + "&FrmID=" + this.No + "&NodeID=" + this.getNodeID() + "&FK_Node=" + this.getNodeID();
	}

	/** 
	 傻瓜表单设计器
	 
	 @return 
	*/
	public final String DoDesignerFool()
	{
		return "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData=" + this.No + "&MyPK=" + this.No + "&IsFirst=1&IsEditMapData=True";
	}

	/** 
	 节点表单组件
	 
	 @return 
	*/
	public final String DoNodeFrmCompent()
	{
		if (this.No.Contains("ND") == true)
		{
			return "../../Comm/EnOnly.htm?EnName=BP.WF.Template.FrmNodeComponent&PK=" + this.No.Replace("ND", "") + "&t=" + DataType.CurrentDataTime;
		}
		else
		{
			return "../../Admin/FoolFormDesigner/Do.htm&DoType=FWCShowError";
		}
	}
	/** 
	 执行旧版本的兼容性检查.
	*/
	public final String DoCheckFixFrmForUpdateVer()
	{
		// 更新状态.
		DBAccess.RunSQL("UPDATE Sys_GroupField SET CtrlType='' WHERE CtrlType IS NULL");
		DBAccess.RunSQL("UPDATE Sys_GroupField SET CtrlID='' WHERE CtrlID IS NULL");
		DBAccess.RunSQL("UPDATE Sys_GroupField SET CtrlID='' WHERE CtrlID IS NULL");

		//删除重影数据.
		DBAccess.RunSQL("DELETE FROM Sys_GroupField WHERE CtrlType='FWC' and CTRLID is null");

		//一直遇到遇到自动变长的问题, 强制其修复过来.
		DBAccess.RunSQL("UPDATE Sys_Mapattr SET colspan=3 WHERE UIHeight<=38 AND colspan=4");

		String str = "";

		//处理失去分组的字段. 
		String sql = "SELECT MyPK FROM Sys_MapAttr WHERE FK_MapData='" + this.No + "' AND GroupID NOT IN (SELECT OID FROM Sys_GroupField WHERE FrmID='" + this.No + "' AND ( CtrlType='' OR CtrlType IS NULL)  )  OR GroupID IS NULL ";
		MapAttrs attrs = new MapAttrs();
		attrs.RetrieveInSQL(sql);
		if (attrs.size() != 0)
		{
			GroupField gf = null;
			GroupFields gfs = new GroupFields(this.No);
			for (GroupField mygf : gfs)
			{
				if (mygf.CtrlID.equals(""))
				{
					gf = mygf;
				}
			}

			if (gf == null)
			{
				gf = new GroupField();
				gf.Lab = "基本信息";
				gf.FrmID = this.No;
				gf.Insert();
			}

			//设置GID.
			for (MapAttr attr : attrs)
			{
				attr.Update(MapAttrAttr.GroupID, gf.OID);
			}
		}

		//从表.
		MapDtls dtls = new MapDtls(this.No);
		for (MapDtl dtl : dtls)
		{
			GroupField gf = new GroupField();
			int i = gf.Retrieve(GroupFieldAttr.CtrlID, dtl.No, GroupFieldAttr.FrmID, this.No);
			if (i == 1)
			{
				continue;
			}

			//GroupField gf = new GroupField();
			//if (gf.IsExit(GroupFieldAttr.CtrlID, dtl.No) == true && !DataType.IsNullOrEmpty(gf.CtrlType))
			//    continue;

			gf.Lab = dtl.Name;
			gf.CtrlID = dtl.No;
			gf.CtrlType = "Dtl";
			gf.FrmID = dtl.FK_MapData;
			gf.DirectSave();
			str += "@为从表" + dtl.Name + " 增加了分组.";
		}

		// 框架.
		MapFrames frams = new MapFrames(this.No);
		for (MapFrame fram : frams)
		{
			GroupField gf = new GroupField();
			int i = gf.Retrieve(GroupFieldAttr.CtrlID, fram.MyPK, GroupFieldAttr.FrmID, this.No);
			if (i == 1)
			{
				continue;
			}

			gf.Lab = fram.Name;
			gf.CtrlID = fram.MyPK;
			gf.CtrlType = "Frame";
			gf.EnName = fram.FK_MapData;
			gf.Insert();

			str += "@为框架 " + fram.Name + " 增加了分组.";
		}

		// 附件.
		FrmAttachments aths = new FrmAttachments(this.No);
		for (FrmAttachment ath : aths)
		{
			if (ath.IsVisable == false)
			{
				continue;
			}

			GroupField gf = new GroupField();
			int i = gf.Retrieve(GroupFieldAttr.CtrlID, ath.MyPK, GroupFieldAttr.FrmID, this.No);
			if (i == 1)
			{
				continue;
			}

			gf.Lab = ath.Name;
			gf.CtrlID = ath.MyPK;
			gf.CtrlType = "Ath";
			gf.FrmID = ath.FK_MapData;
			gf.Insert();

			str += "@为附件 " + ath.Name + " 增加了分组.";
		}

		if (this.getIsNodeFrm() == true)
		{
			FrmNodeComponent conn = new FrmNodeComponent(this.getNodeID());
			conn.Update();
		}


		//删除重复的数据, 比如一个从表显示了多个分组里. 增加此部分.
		if (SystemConfig.AppCenterDBType == DBType.Oracle)
		{
			sql = "SELECT * FROM (SELECT FrmID,CtrlID,CtrlType, count(*) as Num FROM sys_groupfield WHERE CtrlID!='' GROUP BY FrmID,CtrlID,CtrlType ) WHERE Num > 1";
		}
		else
		{
			sql = "SELECT * FROM (SELECT FrmID,CtrlID,CtrlType, count(*) as Num FROM sys_groupfield WHERE CtrlID!='' GROUP BY FrmID,CtrlID,CtrlType ) AS A WHERE A.Num > 1";
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		for (DataRow dr : dt.Rows)
		{
			String enName = dr.get(0).toString();
			String ctrlID = dr.get(1).toString();
			String ctrlType = dr.get(2).toString();

			GroupFields gfs = new GroupFields();
			gfs.Retrieve(GroupFieldAttr.FrmID, enName, GroupFieldAttr.CtrlID, ctrlID, GroupFieldAttr.CtrlType, ctrlType);

			if (gfs.size() <= 1)
			{
				continue;
			}
			for (GroupField gf : gfs)
			{
				gf.Delete(); //删除其中的一个.
				break;
			}
		}



		if (str.equals(""))
		{
			return "检查成功.";
		}

		return str + ", @@@ 检查成功。";
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
		String url = "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData=" + this.No + "&UserNo=" + BP.Web.WebUser.No + "&SID=" + Web.WebUser.SID + "&IsFirst=1&AppCenterDBType=" + BP.DA.DBAccess.AppCenterDBType + "&CustomerNo=" + BP.Sys.SystemConfig.CustomerNo;
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
	 傻瓜表单属性.
	 
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
package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.*;
import BP.Web.WebUser;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 自由表单属性
*/
public class MapFrmFree extends EntityNoName
{

		///#region 文件模版属性.


		///#endregion 文件模版属性.


		///#region 属性
	/** 
	 物理存储表
	 * @throws Exception 
	*/
	public final String getPTable() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.PTable);
	}
	public final void setPTable(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.PTable, value);
	}
	/** 
	 表单事件实体
	*/
	public final String getFromEventEntity() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.FormEventEntity);
	}
	public final void setFromEventEntity(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.FormEventEntity,value);
	}
	/** 
	 是否是节点表单?
	 * @throws Exception 
	*/
	public final boolean getIsNodeFrm() throws Exception
	{
		if (this.getNo().contains("ND") == false)
		{
			return false;
		}

		if (this.getNo().contains("Rpt") == true)
		{
			return false;
		}

		if (this.getNo().substring(0, 2).equals("ND"))
		{
			return true;
		}

		return false;
	}
	/** 
	 节点ID.
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final int getNodeID() throws NumberFormatException, Exception
	{
		if (this.getNo().indexOf("ND") != 0)
		{
			return 0;
		}
		return Integer.parseInt(this.getNo().replace("ND", ""));
	}

	/** 
	 表格显示的列
	*/
	public final int getTableCol()
	{
		return 4;
//		int i = this.GetValIntByKey(MapFrmFreeAttr.TableCol);
//		if (i == 0 || i == 1)
//		{
//			return 4;
//		}
//		return i;
	}
	public final void setTableCol(int value) throws Exception
	{
		this.SetValByKey(MapFrmFreeAttr.TableCol, value);
	}

		///#endregion


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


		///#region 构造方法
	/** 
	 自由表单属性
	*/
	public MapFrmFree()
	{
	}
	/** 
	 自由表单属性
	 
	 @param no 表单ID
	 * @throws Exception 
	*/
	public MapFrmFree(String no) throws Exception
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

		Map map = new Map("Sys_MapData", "自由表单属性");
		map.Java_SetEnType(EnType.Sys);


			///#region 基本属性.
		map.AddTBStringPK(MapFrmFreeAttr.No, null, "表单编号", true, true, 1, 190, 20);
		map.AddTBString(MapFrmFreeAttr.PTable, null, "存储表", true, false, 0, 100, 20);
		map.AddTBString(MapFrmFreeAttr.Name, null, "表单名称", true, false, 0, 200, 20,true);
		map.AddTBString(MapDataAttr.FormEventEntity, null, "事件实体", true, true, 0, 100, 20, true);

			//数据源.
		map.AddDDLEntities(MapFrmFreeAttr.DBSrc, "local", "数据源", new BP.Sys.SFDBSrcs(), true);
		map.AddDDLEntities(MapFrmFreeAttr.FK_FormTree, "01", "表单类别", new SysFormTrees(), true);

			//宽度高度.
		map.AddTBInt(MapFrmFreeAttr.FrmW, 900, "自由表单-宽度", true, false);
		map.AddTBInt(MapFrmFreeAttr.FrmH, 1200, "自由表单-高度", true, false);

			//表单的运行类型.
		map.AddDDLSysEnum(MapFrmFreeAttr.FrmType, FrmType.FreeFrm.getValue(), "表单类型", true, false, MapFrmFreeAttr.FrmType);

			///#endregion 基本属性.



			///#region 设计者信息.
		map.AddTBString(MapFrmFreeAttr.Designer, null, "设计者", true, false, 0, 500, 20);
		map.AddTBString(MapFrmFreeAttr.DesignerContact, null, "联系方式", true, false, 0, 500, 20);
		map.AddTBString(MapFrmFreeAttr.DesignerUnit, null, "单位", true, false, 0, 500, 20, false);
		map.AddTBString(MapFrmFreeAttr.GUID, null, "GUID", true, true, 0, 128, 20, false);
		map.AddTBString(MapFrmFreeAttr.Ver, null, "版本号", true, true, 0, 30, 20);
		map.AddTBStringDoc(MapFrmFreeAttr.Note, null, "备注", true, false, true);

			//增加参数字段.
		map.AddTBAtParas(4000);
		map.AddTBInt(MapFrmFreeAttr.Idx, 100, "顺序号", false, false);

			///#endregion 设计者信息.

		map.AddMyFile("表单模版");

			//查询条件.
		map.AddSearchAttr(MapFrmFreeAttr.DBSrc);


			///#region 方法 - 基本功能.
		RefMethod rm = new RefMethod();

		rm = new RefMethod();
		rm.Title = "启动傻瓜表单设计器";
		rm.ClassMethodName = this.toString() + ".DoDesignerFool";
		rm.Icon = "../../WF/Img/FileType/xlsx.gif";
		rm.Visable = true;
		rm.Target = "_blank";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "字段维护";
		rm.ClassMethodName = this.toString() + ".DoEditFiledsList";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/field.png";
		rm.Visable = true;
		rm.Target = "_blank";
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
		rm.Icon = "../../WF/Img/RegularExpression.png";
		rm.ClassMethodName = this.toString() + ".DoRegularExpressionBatch";
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
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "../../WF/Img/Export.png";
		rm.Visable = true;
		rm.RefAttrLinkLabel = "导出到xml";
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "Tab顺序键"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoTabIdx";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "模板打印";
		rm.ClassMethodName = this.toString() + ".DoBill";
		rm.Icon = "../../WF/Img/FileType/doc.gif";
		rm.refMethodType = RefMethodType.RightFrameOpen;

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

			///#endregion 方法 - 基本功能.


			///#region 高级设置.
			//带有参数的方法.
		rm = new RefMethod();
		rm.Title = "重命名字段";
		 //   rm.GroupName = "高级设置";
		rm.getHisAttrs().AddTBString("FieldOld", null, "旧字段英文名", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("FieldNew", null, "新字段英文名", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("FieldNewName", null, "新字段中文名", true, false, 0, 100, 100);
		rm.ClassMethodName = this.toString() + ".DoChangeFieldName";
		rm.Icon = "../../WF/Img/ReName.png";
		rm.GroupName = "高级设置";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "重命表单ID";
		  //  rm.GroupName = "高级设置";
		rm.getHisAttrs().AddTBString("NewFrmID1", null, "新表单ID名称", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("NewFrmID2", null, "确认表单ID名称", true, false, 0, 100, 100);
		rm.ClassMethodName = this.toString() + ".DoChangeFrmID";
		rm.Icon = "../../WF/Img/ReName.png";
		rm.GroupName = "高级设置";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "复制表单";
			//  rm.GroupName = "高级设置";
		rm.getHisAttrs().AddTBString("FrmID", null, "要复制新表单ID", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("FrmName", null, "表单名称", true, false, 0, 100, 100);
		rm.getHisAttrs().AddDDLEntities("FrmTree", null, "复制到表单目录", new FrmTrees(),true);

		rm.ClassMethodName = this.toString() + ".DoCopyFrm";
		rm.Icon = "../../WF/Img/Btn/Copy.GIF";
		rm.GroupName = "高级设置";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "手机端表单";
		rm.GroupName = "高级设置";
		rm.Icon = "../../WF/Admin/CCFormDesigner/Img/telephone.png";
		rm.ClassMethodName = this.toString() + ".DoSortingMapAttrs";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

			//@李国文.
		rm = new RefMethod();
		rm.Title = "改变表单类型";
		rm.GroupName = "高级功能";
		rm.ClassMethodName = this.toString() + ".DoChangeFrmType()";
		rm.getHisAttrs().AddDDLSysEnum("FrmType", 0, "修改表单类型", true, true);
		map.AddRefMethod(rm);


			///#endregion 高级设置.


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
		rm.Icon = "../../Img/Table.gif";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

			///#endregion 方法 - 开发接口.

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeUpdate() throws Exception
	{
		//注册事件表单实体.
		BP.Sys.FormEventBase feb = BP.Sys.Glo.GetFormEventBaseByEnName(this.getNo());
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

	@Override
	protected void afterUpdate() throws Exception
	{
		//修改关联明细表
		MapDtl dtl = new MapDtl();
		dtl.setNo(this.getNo());
		if (dtl.RetrieveFromDBSources() == 1)
		{
			dtl.setName(this.getName());
			dtl.setPTable(this.getPTable());
			dtl.DirectUpdate();

			MapData map = new MapData(this.getNo());
			//避免显示在表单库中
			map.setFK_FrmSort("");
			map.setFK_FormTree("");
			map.DirectUpdate();
		}

		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getNo());

		super.afterUpdate();
	}

	/** 
	 删除后清缓存
	 * @throws Exception 
	*/
	@Override
	protected void afterDelete() throws Exception
	{
		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getNo());
		super.afterDelete();
	}


		///#region 高级设置.
	/** 
	 改变表单类型 @李国文 ，需要搬到jflow.
	 
	 @param val 要改变的类型
	 @return 
	 * @throws Exception 
	*/
	public final String DoChangeFrmType(int val) throws Exception
	{
		MapData md = new MapData(this.getNo());
		String str = "原来的是:" + md.getHisFrmTypeText() + "类型，";
		md.setHisFrmTypeInt(val);
		str += "现在修改为：" + md.getHisFrmTypeText() + "类型";
		md.Update();

		return str;
	}
	public final String DoTabIdx() throws Exception
	{
		return SystemConfig.getCCFlowWebPath() + "WF/Admin/FoolFormDesigner/TabIdx.htm?FK_MapData=" + this.getNo();
	}
	/** 
	 复制表单
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoCopyFrm(String frmID, String frmName, String fk_frmTree) throws Exception
	{
		return BP.Sys.CCFormAPI.CopyFrm(this.getNo(), frmID, frmName, fk_frmTree);
	}

		///#endregion 高级设置.



		///#region 节点表单方法.
	/** 
	 单据打印
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoBill() throws Exception
	{
		return "../../Admin/AttrNode/Bill.htm?FK_MapData=" + this.getNo()+ "&NodeID=" + this.getNodeID() + "&FK_Node=" + this.getNodeID();
	}

	/** 
	 启动自由表单设计器(SL)
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoDesignerSL() throws Exception
	{
		return "../../Admin/CCFormDesigner/CCFormDesignerSL.htm?FK_MapData=" + this.getNo()+ "&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID();
	}
	/** 
	 启动自由表单设计器(h5)
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoDesignerH5() throws Exception
	{
		return "../../Admin/CCFormDesigner/FormDesigner.htm?FK_MapData=" + this.getNo()+ "&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID();
	}
	/** 
	 傻瓜表单设计器
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoDesignerFool() throws Exception
	{
		return "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData=" + this.getNo()+ "&MyPK=" + this.getNo()+ "&IsEditMapData=True&DoDesignerFool&IsFirst=1";
	}
	/** 
	 编辑excel模版.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoEditExcelTemplate() throws Exception
	{
		return "../../Admin/CCFormDesigner/ExcelFrmDesigner/Designer.htm?FK_MapData=" + this.getNo();
	}
	/** 
	 表单字段.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoEditFiledsList() throws Exception
	{
		return "../../Admin/FoolFormDesigner/BatchEdit.htm?FK_MapData=" + this.getNo();
	}


		///#endregion


		///#region 通用方法.
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
	 批量设置正则表达式规则.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoRegularExpressionBatch() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/RegularExpressionBatch.htm?FK_Flow=&FK_MapData=" + this.getNo()+ "&t=" + DataType.getCurrentDataTime();
	}
	/** 
	 批量修改字段
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoBatchEditAttr() throws Exception
	{
		return "../../Admin/FoolFormDesigner/BatchEdit.htm?FK_MapData=" + this.getNo()+ "&t=" + DataType.getCurrentDataTime();
	}
	/** 
	 排序字段顺序
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoSortingMapAttrs() throws Exception
	{
		return "../../Admin/AttrNode/SortingMapAttrs.htm?FK_Flow=&FK_MapData=" + this.getNo()+ "&t=" + DataType.getCurrentDataTime();
	}
	/** 
	 设计表单
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoDFrom() throws Exception
	{
		return "../../Admin/FoolFormDesigner/CCForm/Frm.htm?FK_MapData=" + this.getNo()+ "&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID() + "&AppCenterDBType=" + DBAccess.getAppCenterDBType() + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();

	}
	/** 
	 设计傻瓜表单
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoDFromCol4() throws Exception
	{
		return "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData=" + this.getNo()+ "&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID() + "&IsFirst=1&AppCenterDBType=" + DBAccess.getAppCenterDBType() + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
	}
	/** 
	 查询
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoSearch() throws Exception
	{
		return "../../Comm/Search.htm?s=34&FK_MapData=" + this.getNo()+ "&EnsName=" + this.getNo();
	}
	/** 
	 调用分析API
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoGroup() throws Exception
	{
		return "../../Comm/Group.htm?s=34&FK_MapData=" + this.getNo()+ "&EnsName=" + this.getNo();
	}
	/** 
	 数据源管理
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoDBSrc() throws Exception
	{
		return "../../Comm/Search.htm?s=34&FK_MapData=" + this.getNo()+ "&EnsName=BP.Sys.SFDBSrcs";
	}

	public final String DoPageLoadFull() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/PageLoadFull.htm?s=34&FK_MapData=" + this.getNo()+ "&ExtType=PageLoadFull&RefNo=";
	}
	public final String DoInitScript() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/InitScript.htm?s=34&FK_MapData=" + this.getNo()+ "&ExtType=PageLoadFull&RefNo=";
	}
	/** 
	 自由表单属性.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoBodyAttr() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/BodyAttr.htm?s=34&FK_MapData=" + this.getNo()+ "&ExtType=BodyAttr&RefNo=";
	}
	/** 
	 表单事件
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoEvent() throws Exception
	{
		return "../../Admin/CCFormDesigner/Action.htm?FK_MapData=" + this.getNo()+ "&T=sd&FK_Node=0";
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
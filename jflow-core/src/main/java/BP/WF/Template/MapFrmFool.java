package BP.WF.Template;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.En.*;
import BP.En.Map;
import BP.WF.*;
import BP.Web.WebUser;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 傻瓜表单属性
*/
public class MapFrmFool extends EntityNoName
{

		///#region 属性
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

		if (this.getNo().substring(0, 2).equals("ND") && this.getNo().contains("Dtl") == false)
		{
			return true;
		}

		return false;
	}
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

}
	public final void setTableCol(int value) throws Exception
	{
		this.SetValByKey(MapDataAttr.TableCol, value);
	}
	public final String getFK_FormTree() throws Exception {
		return this.GetValStringByKey(MapDataAttr.FK_FormTree);

	}
	public final void setFK_FormTree(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.FK_FormTree,value);
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
	 傻瓜表单属性
	*/
	public MapFrmFool()
	{
	}
	/** 
	 傻瓜表单属性
	 
	 @param no 表单ID
	 * @throws Exception 
	*/
	public MapFrmFool(String no) throws Exception
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

		Map map = new Map("Sys_MapData", "傻瓜表单属性");
		map.Java_SetEnType(EnType.Sys);
		map.Java_SetCodeStruct("4");


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
		map.AddDDLSysEnum(MapDataAttr.FrmType, FrmType.FreeFrm.getValue(), "表单类型", true, false, MapDataAttr.FrmType);
			//表单解析 0 普通 1 页签展示
		map.AddDDLSysEnum(MapDataAttr.FrmShowType, 0, "表单展示方式", true, true, "表单展示方式", "@0=普通方式@1=页签方式");
		map.AddBoolean(MapDataAttr.IsEnableJS,false,"是否启用自定义JS",true,true,false);

			///#endregion 基本属性.


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


			///#endregion 设计者信息.

			//查询条件.
		map.AddSearchAttr(MapDataAttr.DBSrc);


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
		rm.Title = "批量修改字段"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoBatchEditAttr";
		rm.Icon = "../../WF/Admin/CCBPMDesigner/Img/field.png";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Target = "_blank";
		  //  map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "手机端表单";
		rm.Icon = "../../WF/Admin/CCFormDesigner/Img/telephone.png";
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
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "../../WF/Img/Export.png";
		rm.Visable = true;
		rm.RefAttrLinkLabel = "导出到xml";
		rm.Target = "_blank";
		map.AddRefMethod(rm);


			//带有参数的方法.
		rm = new RefMethod();
		rm.Title = "重命名字段";
		rm.getHisAttrs().AddTBString("FieldOld", null, "旧字段英文名", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("FieldNew", null, "新字段英文名", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("FieldNewName", null, "新字段中文名", true, false, 0, 100, 100);
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
		rm.Title = "Tab顺序键"; // Tab顺序键;
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


		rm = new RefMethod();
		rm.Title = "模板打印2019";
		rm.ClassMethodName = this.toString() + ".DoBill2019";
		rm.Icon = "../../WF/Img/FileType/doc.gif";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);



			///#endregion 方法 - 基本功能.


			///#region 高级功能.
			//@李国文.
		rm = new RefMethod();
		rm.Title = "改变表单类型";
		rm.GroupName = "高级功能";
		rm.ClassMethodName = this.toString() + ".DoChangeFrmType()";
		rm.getHisAttrs().AddDDLSysEnum("FrmType", 0, "修改表单类型", true, true);
		map.AddRefMethod(rm);

			///#endregion


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

		this.set_enMap(map);
		return this.get_enMap();
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

		///#endregion


		///#region 高级设置.
	/** 
	 改变表单类型 @李国文 ，需要搬到jflow.并测试.
	 
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

		///#endregion 高级设置.
	public final String DoTabIdx() throws Exception
	{
		return SystemConfig.getCCFlowWebPath() + "WF/Admin/FoolFormDesigner/TabIdx.htm?FK_MapData=" + this.getNo();
	}

	@Override
	protected boolean beforeUpdate() throws NumberFormatException, Exception
	{
		
				
		//注册事件表单实体.
		//BP.Sys.FormEventBase feb = BP.Sys.Glo.GetFormEventBaseByEnName(this.getNo());
		//if (feb == null)
		//    this.FromEventEntity = "";
		//else

		//    this.FromEventEntity = feb.ToString();
		if (this.getNodeID() != 0)
			this.setFK_FormTree("");
		
		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getNo());
		
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
	 单据打印
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoBill2019() throws Exception
	{
		return "../../Admin/AttrNode/Bill2019.htm?FK_MapData=" + this.getNo()+ "&FrmID=" + this.getNo()+ "&NodeID=" + this.getNodeID() + "&FK_Node=" + this.getNodeID();
	}

	/** 
	 傻瓜表单设计器
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoDesignerFool() throws Exception
	{
		return "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData=" + this.getNo()+ "&MyPK=" + this.getNo()+ "&IsFirst=1&IsEditMapData=True";
	}

	/** 
	 节点表单组件
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoNodeFrmCompent() throws Exception
	{
		if (this.getNo().contains("ND") == true)
		{
			return "../../Comm/EnOnly.htm?EnName=BP.WF.Template.FrmNodeComponent&PK=" + this.getNo().replace("ND", "") + "&t=" + DataType.getCurrentDataTime();
		}
		else
		{
			return "../../Admin/FoolFormDesigner/Do.htm&DoType=FWCShowError";
		}
	}
	/** 
	 执行旧版本的兼容性检查.
	 * @throws Exception 
	*/
	public final String DoCheckFixFrmForUpdateVer() throws Exception
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
		String sql = "SELECT MyPK FROM Sys_MapAttr WHERE FK_MapData='" + this.getNo()+ " ' AND GroupID NOT IN (SELECT OID FROM Sys_GroupField WHERE FrmID='" + this.getNo()+ " ' AND ( CtrlType='' OR CtrlType IS NULL)  )  OR GroupID IS NULL ";
		MapAttrs attrs = new MapAttrs();
		attrs.RetrieveInSQL(sql);
		if (attrs.size() != 0)
		{
			GroupField gf = null;
			GroupFields gfs = new GroupFields(this.getNo());
			for (GroupField mygf : gfs.ToJavaList())
			{
				if (mygf.getCtrlID().equals(""))
				{
					gf = mygf;
				}
			}

			if (gf == null)
			{
				gf = new GroupField();
				gf.setLab("基本信息");
				gf.setFrmID(this.getNo());
				gf.Insert();
			}

			//设置GID.
			for (MapAttr attr : attrs.ToJavaList())
			{
				attr.Update(MapAttrAttr.GroupID, gf.getOID());
			}
		}

		//从表.
		MapDtls dtls = new MapDtls(this.getNo());
		for (MapDtl dtl : dtls.ToJavaList())
		{
			GroupField gf = new GroupField();
			int i = gf.Retrieve(GroupFieldAttr.CtrlID, dtl.getNo(), GroupFieldAttr.FrmID, this.getNo());
			if (i == 1)
			{
				continue;
			}

			//GroupField gf = new GroupField();
			//if (gf.IsExit(GroupFieldAttr.CtrlID, dtl.No) == true && !DataType.IsNullOrEmpty(gf.CtrlType))
			//    continue;

			gf.setLab(dtl.getName());
			gf.setCtrlID(dtl.getNo());
			gf.setCtrlType("Dtl");
			gf.setFrmID(dtl.getFK_MapData());
			gf.DirectSave();
			str += "@为从表" + dtl.getName() + " 增加了分组.";
		}

		// 框架.
		MapFrames frams = new MapFrames(this.getNo());
		for (MapFrame fram : frams.ToJavaList())
		{
			GroupField gf = new GroupField();
			int i = gf.Retrieve(GroupFieldAttr.CtrlID, fram.getMyPK(), GroupFieldAttr.FrmID, this.getNo());
			if (i == 1)
			{
				continue;
			}

			gf.setLab(fram.getName());
			gf.setCtrlID(fram.getMyPK());
			gf.setCtrlType("Frame");
			gf.setEnName(fram.getFK_MapData());
			gf.Insert();

			str += "@为框架 " + fram.getName() + " 增加了分组.";
		}

		// 附件.
		FrmAttachments aths = new FrmAttachments(this.getNo());
		for (FrmAttachment ath : aths.ToJavaList())
		{
			if (ath.getIsVisable() == false)
			{
				continue;
			}

			GroupField gf = new GroupField();
			int i = gf.Retrieve(GroupFieldAttr.CtrlID, ath.getMyPK(), GroupFieldAttr.FrmID, this.getNo());
			if (i == 1)
			{
				continue;
			}

			gf.setLab(ath.getName());
			gf.setCtrlID(ath.getMyPK());
			gf.setCtrlType("Ath");
			gf.setFrmID(ath.getFK_MapData());
			gf.Insert();

			str += "@为附件 " + ath.getName() + " 增加了分组.";
		}

		if (this.getIsNodeFrm() == true)
		{
			FrmNodeComponent conn = new FrmNodeComponent(this.getNodeID());
			conn.Update();
		}


		//删除重复的数据, 比如一个从表显示了多个分组里. 增加此部分.
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
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
			String enName = dr.getValue(0).toString();
			String ctrlID = dr.getValue(1).toString();
			String ctrlType = dr.getValue(2).toString();

			GroupFields gfs = new GroupFields();
			gfs.Retrieve(GroupFieldAttr.FrmID, enName, GroupFieldAttr.CtrlID, ctrlID, GroupFieldAttr.CtrlType, ctrlType);

			if (gfs.size() <= 1)
			{
				continue;
			}
			for (GroupField gf : gfs.ToJavaList())
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

		///#endregion


		///#region 通用方法.
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
		String url = "../../Admin/FoolFormDesigner/CCForm/Frm.htm?FK_MapData=" + this.getNo()+ "&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID() + "&AppCenterDBType=" + DBAccess.getAppCenterDBType() + "&CustomerNo=" + SystemConfig.getCustomerNo();
		return url;
	}
	/** 
	 设计傻瓜表单
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoDFromCol4() throws Exception
	{
		String url = "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData=" + this.getNo()+ "&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID() + "&IsFirst=1&AppCenterDBType=" + DBAccess.getAppCenterDBType() + "&CustomerNo=" + SystemConfig.getCustomerNo();
		return url;
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
	public final String DoWordFrm() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/WordFrm.aspx?s=34&FK_MapData=" + this.getNo()+ "&ExtType=WordFrm&RefNo=";
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
	 傻瓜表单属性.
	 
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
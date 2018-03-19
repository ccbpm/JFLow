package BP.WF.Template;

import java.io.IOException;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import cn.jflow.common.util.ContextHolderUtils;
import BP.Sys.*;

/** 
 自由表单属性
 
*/
public class MapFrmNode extends EntityNoName
{
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
	 
	*/
	public final int getNodeID()
	{
		return Integer.parseInt(this.getNo().replace("ND", ""));
	}
	/** 
	 傻瓜表单-宽度
	 
	*/
	public final String getTableWidth()
	{
		int i = this.GetValIntByKey(MapFrmNodeAttr.TableWidth);
		if (i <= 50)
		{
			return "900";
		}
		return (new Integer(i)).toString();
	}
	/** 
	 傻瓜表单-高度
	 
	*/
	public final String getTableHeight()
	{
		int i = this.GetValIntByKey(MapFrmNodeAttr.TableHeight);
		if (i <= 500)
		{
			return "900";
		}
		return (new Integer(i)).toString();
	}
	/** 
	 表格显示的列
	 
	*/
	public final int getTableCol()
	{
		return 4;
		/*int i = this.GetValIntByKey(MapFrmNodeAttr.TableCol);
		if (i == 0 || i == 1)
		{
			return 4;
		}
		return i;*/
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
		if (BP.Web.WebUser.getNo().equals("admin"))
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
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
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
		map.AddDDLSysEnum(MapFrmNodeAttr.FrmType, BP.Sys.FrmType.FreeFrm.getValue(), "表单类型", true, false, MapFrmNodeAttr.FrmType);
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
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "表单检查"; //"设计表单";
		rm.ClassMethodName = this.toString() + ".DoCheckFixFrmForUpdateVer";
		rm.Visable = true;
		rm.RefAttrLinkLabel = "表单检查";
		rm.Icon = "../../WF/Img/Check.png";
		rm.Target = "_blank";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "重置表单"; //"设计表单";
		rm.ClassMethodName = this.toString() + ".DoReset";
		rm.Warning = "重置就是重新让设计器还原原来的设置. \t\n注意:执行重置有可能会导致部分的界面元素不能按照原始的方式还原上来.";
		rm.Visable = true;
		rm.RefAttrLinkLabel = "重置表单";
		rm.Icon = "../../WF/Img/Check.png";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "Tab顺序键"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoTabIdx";
		rm.Visable = true;
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 方法 - 基本功能.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
		rm.Title = "重置表单";
		rm.ClassMethodName = this.toString() + ".DoResetFrm";
		rm.GroupName = "高级设置";
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "手机端表单";
		rm.GroupName = "高级设置";
		rm.Icon = "../../WF/Admin/CCFormDesigner/Img/telephone.png";
		rm.ClassMethodName = this.toString() + ".DoSortingMapAttrs";
		rm.refMethodType = RefMethodType.RightFrameOpen;
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
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.Target = "_blank";
		rm.GroupName = "开发接口";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "调用分析API"; // "设计表单";
		rm.ClassMethodName = this.toString() + ".DoGroup";
		rm.Icon ="../../Img/Table.gif";
		rm.Visable = true;
		rm.refMethodType = RefMethodType.LinkeWinOpen;
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

	@Override
	protected boolean beforeUpdate()
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
	public final String DoTabIdx()
	{
		return BP.WF.Glo.getCCFlowAppPath() +"WF/Admin/FoolFormDesigner/TabIdx.htm?FK_MapData=" + this.getNo();
	}

	public final String DoResetFrm()
	{
		String sql = "UPDATE Sys_MapData SET FormJson= null WHERE No='"+this.getNo()+"'";
		DBAccess.RunSQL(sql);
		return "表单已经重置成功, 请关闭表单设计器重新打开.";
	}

	/** 
	 复制表单
	 
	 @return 
	*/
	public final String DoCopyFrm(String frmID, String frmName, String fk_frmTree)
	{
		return BP.Sys.CCFormAPI.CopyFrm(this.getNo(), frmID, frmName, fk_frmTree);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 节点表单方法.
	public final String DoReset()
	{
		String sql = "UPDATE Sys_MapData SET FormJson = null WHERE No='"+this.getNo()+"'";
		BP.DA.DBAccess.RunSQL(sql);
		return "重置成功,您需要关闭当前H5的表单设计器然后重新打开.";
	}
	/** 
	 启动自由表单设计器(SL)
	 
	 @return 
	*/
	public final String DoDesignerSL()
	{
		return "../../Admin/CCFormDesigner/CCFormDesignerSL.htm?FK_MapData="+this.getNo()+"&UserNo="+BP.Web.WebUser.getNo()+"&SID="+BP.Web.WebUser.getSID();
	}
	/** 
	 启动自由表单设计器(h5)
	 
	 @return 
	*/
	public final String DoDesignerH5()
	{
		// WF/Admin/CCFormDesigner/FormDesigner.htm?FK_MapData=ND102&UserNo=admin&SID=44a42h5gcbxnwjof2hv2pw5e
		return "../../Admin/CCFormDesigner/FormDesigner.htm?FK_MapData=" + this.getNo() + "&UserNo=" + BP.Web.WebUser.getNo() + "&SID=" + BP.Web.WebUser.getSID();
	}
	/** 
	 傻瓜表单设计器
	 
	 @return 
	*/
	public final String DoDesignerFool()
	{
		return "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData=" + this.getNo() + "&MyPK=" + this.getNo() + "&IsEditMapData=True&DoDesignerFool&IsFirst=1";
	}
	/** 
	 编辑excel模版.
	 
	 @return 
	*/
	public final String DoEditExcelTemplate()
	{
		return "../../Admin/CCFormDesigner/ExcelFrmDesigner/Designer.htm?FK_MapData=" + this.getNo();
	}
	/** 
	 表单字段.
	 
	 @return 
	*/
	public final String DoEditFiledsList()
	{
		return "../../Admin/FoolFormDesigner/BatchEdit.htm?FK_MapData=" + this.getNo();
	}
	/** 
	 执行旧版本的兼容性检查.
	 
	*/
	public final String DoCheckFixFrmForUpdateVer()
	{
		// 更新状态.
		DBAccess.RunSQL("UPDATE Sys_GroupField SET CtrlType='' WHERE CtrlType IS NULL");
		DBAccess.RunSQL("UPDATE Sys_GroupField SET CtrlID='' WHERE CtrlID IS NULL");

		String str = "";

		 // 处理失去分组的字段. 
		String sql = "SELECT MyPK FROM Sys_MapAttr WHERE FK_MapData='" + this.getNo() + "' AND GroupID NOT IN (SELECT OID FROM Sys_GroupField WHERE EnName='" + this.getNo() + "' AND CtrlType='' ) ";
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
				gf.setEnName(this.getNo());
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
			if (gf.IsExit(GroupFieldAttr.CtrlID, dtl.getNo()) == true && !DotNetToJavaStringHelper.isNullOrEmpty(gf.getCtrlType()))
			{
				continue;
			}

			gf.setLab(dtl.getName());
			gf.setCtrlID(dtl.getNo());
			gf.setCtrlType("Dtl");
			gf.setEnName(dtl.getFK_MapData());
			try {
				gf.DirectSave();
			} catch (Exception e) {
				e.printStackTrace();
			}

			str += "@为从表" + dtl.getName() + " 增加了分组.";
		}

		// 框架.
		MapFrames frams = new MapFrames(this.getNo());
		for (MapFrame fram : frams.ToJavaList())
		{
			GroupField gf = new GroupField();
			if (gf.IsExit(GroupFieldAttr.CtrlID, fram.getMyPK()) == true && !DotNetToJavaStringHelper.isNullOrEmpty(gf.getCtrlType()))
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
			GroupField gf = new GroupField();
			if (gf.IsExit(GroupFieldAttr.CtrlID, ath.getMyPK()) == true && !DotNetToJavaStringHelper.isNullOrEmpty(gf.getCtrlType()))
			{
				continue;
			}

			gf.setLab(ath.getName());
			gf.setCtrlID(ath.getMyPK());
			gf.setCtrlType("Ath");
			gf.setEnName(ath.getFK_MapData());
			gf.Insert();

			str += "@为附件 " + ath.getName() + " 增加了分组.";
		}

		if (this.getIsNodeFrm() == true)
		{
			FrmNodeComponent conn = new FrmNodeComponent(this.getNodeID());
			conn.Update();
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
	 重命名
	 
	 @param frmID1
	 @param frmID2
	 @return 
	*/
	public final String DoChangeFrmID(String frmID1, String frmID2)
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
	*/
	public final String DoChangeFieldName(String fieldOld, String newField, String newFieldName)
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
	*/
	public final String DoRegularExpressionBatch()
	{
		return "../../Admin/FoolFormDesigner/MapExt/RegularExpressionBatch.htm?FK_Flow=&FK_MapData=" + this.getNo() + "&t=" + DataType.getCurrentDataTime();
	}
	/** 
	 批量修改字段
	 
	 @return 
	*/
	public final String DoBatchEditAttr()
	{
		return "../../Admin/FoolFormDesigner/BatchEdit.htm?FK_MapData=" + this.getNo() + "&t=" + DataType.getCurrentDataTime();
	}
	/** 
	 排序字段顺序
	 
	 @return 
	*/
	public final String DoSortingMapAttrs()
	{
		return "../../Admin/AttrNode/SortingMapAttrs.htm?FK_Flow=&FK_MapData=" + this.getNo() + "&t=" + DataType.getCurrentDataTime();
	}
	/** 
	 设计表单
	 
	 @return 
	*/
	public final String DoDFrom()
	{
		String url = "../../Admin/FoolFormDesigner/CCForm/Frm.htm?FK_MapData=" + this.getNo() + "&UserNo=" + BP.Web.WebUser.getNo() + "&SID=" + BP.Web.WebUser.getSID() + "&AppCenterDBType=" + BP.DA.DBAccess.getAppCenterDBType() + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
		try {
			PubClass.WinOpen(ContextHolderUtils.getResponse(),url, 800, 650);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/** 
	 设计傻瓜表单
	 
	 @return 
	*/
	public final String DoDFromCol4()
	{
		String url = "../../Admin/FoolFormDesigner/Designer.htm?FK_MapData=" + this.getNo() + "&UserNo=" + BP.Web.WebUser.getNo() + "&SID=" + BP.Web.WebUser.getSID() + "&AppCenterDBType=" + BP.DA.DBAccess.getAppCenterDBType() + "&CustomerNo=" + BP.Sys.SystemConfig.getCustomerNo();
		try {
			PubClass.WinOpen(ContextHolderUtils.getResponse(),url, 800, 650);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/** 
	 查询
	 
	 @return 
	*/
	public final String DoSearch()
	{
		return "../../Comm/Search.aspx?s=34&FK_MapData=" + this.getNo() + "&EnsName=" + this.getNo();
	}
	/** 
	 调用分析API
	 
	 @return 
	*/
	public final String DoGroup()
	{
		return "../../Comm/Group.aspx?s=34&FK_MapData=" + this.getNo() + "&EnsName=" + this.getNo();
	}
	/** 
	 数据源管理
	 
	 @return 
	*/
	public final String DoDBSrc()
	{
		return "../../Comm/Search.aspx?s=34&FK_MapData=" + this.getNo() + "&EnsName=BP.Sys.SFDBSrcs";
	}
	public final String DoWordFrm()
	{
		return "../../Admin/FoolFormDesigner/MapExt/WordFrm.aspx?s=34&FK_MapData=" + this.getNo() + "&ExtType=WordFrm&RefNo=";
	}
	public final String DoExcelFrm()
	{
		return "../../Admin/FoolFormDesigner/MapExt/ExcelFrm.aspx?s=34&FK_MapData=" + this.getNo() + "&ExtType=ExcelFrm&RefNo=";
	}
	public final String DoPageLoadFull()
	{
		return "../../Admin/FoolFormDesigner/MapExt/PageLoadFull.htm?s=34&FK_MapData=" + this.getNo() + "&ExtType=PageLoadFull&RefNo=";
	}
	public final String DoInitScript()
	{
		return "../../Admin/FoolFormDesigner/MapExt/InitScript.htm?s=34&FK_MapData=" + this.getNo() + "&ExtType=PageLoadFull&RefNo=";
	}
	/** 
	 自由表单属性.
	 
	 @return 
	*/
	public final String DoBodyAttr()
	{
		return "../../Admin/FoolFormDesigner/MapExt/BodyAttr.aspx?s=34&FK_MapData=" + this.getNo() + "&ExtType=BodyAttr&RefNo=";
	}
	/** 
	 表单事件
	 
	 @return 
	*/
	public final String DoEvent()
	{
		return "../../Admin/AttrNode/Action.htm?FK_MapData=" + this.getNo() + "&T=sd&FK_Node=0";
	}
	/** 
	 导出
	 
	 @return 
	*/
	public final String DoMapExt()
	{
		return "../../Admin/FoolFormDesigner/MapExt/List.aspx?FK_MapData=" + this.getNo() + "&T=sd";
	}
	/** 
	 导出表单
	 
	 @return 
	*/
	public final String DoExp()
	{
		String urlExt = "../../Admin/XAP/DoPort.htm?DoType=DownFormTemplete&FK_MapData=" + this.getNo();
		try {
			PubClass.WinOpen(ContextHolderUtils.getResponse(),urlExt, 900, 1000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 方法.
}

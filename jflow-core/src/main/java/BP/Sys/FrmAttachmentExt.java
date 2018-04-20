package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.Tools.StringHelper;

/** 
 附件
 
*/
public class FrmAttachmentExt extends EntityMyPK
{
	/** 
	 访问权限.
	 * @throws Exception 
	 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.IsView = true;
		uac.IsInsert = false;
		if (BP.Web.WebUser.getNo().equals("admin"))
		{
			uac.IsUpdate = true;
			uac.IsDelete = true;
			return uac;
		}
		return uac;
	}


		
	/** 
	 是否可见？
	 
	*/
	public final boolean getIsVisable()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsVisable, true);
	}
	public final void setIsVisable(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsVisable, value);
	}
	/** 
	 使用上传附件的 - 控件类型
	 0=批量.
	 1=单个。
	 
	*/
	public final int getUploadCtrl()
	{
		return this.GetParaInt(FrmAttachmentAttr.UploadCtrl);
	}
	public final void setUploadCtrl(int value)
	{
		this.SetPara(FrmAttachmentAttr.UploadCtrl, value);
	}
	/** 
	 上传校验
	 0=不校验.
	 1=不能为空.
	 2=每个类别下不能为空.
	 
	*/
	public final UploadFileNumCheck getUploadFileNumCheck()
	{
		return UploadFileNumCheck.forValue(this.GetParaInt(FrmAttachmentAttr.UploadFileNumCheck));
	}
	public final void setUploadFileNumCheck(UploadFileNumCheck value)
	{
		this.SetPara(FrmAttachmentAttr.UploadFileNumCheck, value.getValue());
	}
	/** 
	 保存方式
	 0 =文件方式保存。
	 1 = 保存到数据库.
	 
	*/
	public final int getSaveWay()
	{
		return this.GetParaInt(FrmAttachmentAttr.AthSaveWay);
	}
	public final void setSaveWay(int value)
	{
		this.SetPara(FrmAttachmentAttr.AthSaveWay, value);
	}

		///#endregion 参数属性.


		
	/** 
	 节点编号
	 
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(FrmAttachmentAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		this.SetValByKey(FrmAttachmentAttr.FK_Node, value);
	}
	/** 
	 上传类型（单个的，多个，指定的）
	 
	*/
	public final AttachmentUploadType getUploadType()
	{
		return AttachmentUploadType.forValue(this.GetValIntByKey(FrmAttachmentAttr.UploadType));
	}
	public final void setUploadType(AttachmentUploadType value)
	{
		this.SetValByKey(FrmAttachmentAttr.UploadType, value.getValue());
	}
	/** 
	 类型名称
	 
	*/
	public final String getUploadTypeT()
	{
		if (this.getUploadType() == AttachmentUploadType.Multi)
		{
			return "多附件";
		}
		if (this.getUploadType() == AttachmentUploadType.Single)
		{
			return "单附件";
		}
		if (this.getUploadType() == AttachmentUploadType.Specifically)
		{
			return "指定的";
		}
		return "XXXXX";
	}
	/** 
	 是否可以上传
	 
	*/
	public final boolean getIsUpload()
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsUpload);
	}
	public final void setIsUpload(boolean value)
	{
		this.SetValByKey(FrmAttachmentAttr.IsUpload, value);
	}
	/** 
	 是否可以下载
	 
	*/
	public final boolean getIsDownload()
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsDownload);
	}
	public final void setIsDownload(boolean value)
	{
		this.SetValByKey(FrmAttachmentAttr.IsDownload, value);
	}
	/** 
	 附件删除方式
	 
	*/
	public final AthDeleteWay getHisDeleteWay()
	{
		return AthDeleteWay.forValue(this.GetValIntByKey(FrmAttachmentAttr.DeleteWay));
	}
	public final void setHisDeleteWay(AthDeleteWay value)
	{
		this.SetValByKey(FrmAttachmentAttr.DeleteWay, value.getValue());
	}

	/** 
	 是否可以排序?
	 
	*/
	public final boolean getIsOrder()
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsOrder);
	}
	public final void setIsOrder(boolean value)
	{
		this.SetValByKey(FrmAttachmentAttr.IsOrder, value);
	}
	/** 
	 自动控制大小
	 
	*/
	public final boolean getIsAutoSize()
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsAutoSize);
	}
	public final void setIsAutoSize(boolean value)
	{
		this.SetValByKey(FrmAttachmentAttr.IsAutoSize, value);
	}
	/** 
	 IsShowTitle
	 
	*/
	public final boolean getIsShowTitle()
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsShowTitle);
	}
	public final void setIsShowTitle(boolean value)
	{
		this.SetValByKey(FrmAttachmentAttr.IsShowTitle, value);
	}
	/** 
	 是否是节点表单.
	 
	*/
	public final boolean getIsNodeSheet()
	{
		if (this.getFK_MapData().startsWith("ND") == true)
		{
			return true;
		}
		return false;
	}
	/** 
	 备注列
	 
	*/
	public final boolean getIsNote()
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsNote);
	}
	public final void setIsNote(boolean value)
	{
		this.SetValByKey(FrmAttachmentAttr.IsNote, value);
	}
	/** 
	 附件名称
	 
	*/
	public final String getName()
	{
		String str = this.GetValStringByKey(FrmAttachmentAttr.Name);
		if (StringHelper.isNullOrEmpty(str) == true)
		{
			str = "未命名";
		}
		return str;
	}
	public final void setName(String value)
	{
		this.SetValByKey(FrmAttachmentAttr.Name, value);
	}
	/** 
	 类别
	 
	*/
	public final String getSort()
	{
		return this.GetValStringByKey(FrmAttachmentAttr.Sort);
	}
	public final void setSort(String value)
	{
		this.SetValByKey(FrmAttachmentAttr.Sort, value);
	}
	/** 
	 要求的格式
	 
	*/
	public final String getExts()
	{
		return this.GetValStringByKey(FrmAttachmentAttr.Exts);
	}
	public final void setExts(String value)
	{
		this.SetValByKey(FrmAttachmentAttr.Exts, value);
	}
	/*
	public final String getSaveTo()
	{
		String s = this.GetValStringByKey(FrmAttachmentAttr.SaveTo);
		if (s.equals("") || s == null)
		{
			s = SystemConfig.getPathOfDataUser() + "\\UploadFile\\" + this.getFK_MapData() + "\\";
		}
		return s;
	}
	public final void setSaveTo(String value)
	{
		this.SetValByKey(FrmAttachmentAttr.SaveTo, value);
	}*/
	/** 
	 附件编号
	 
	*/
	public final String getNoOfObj()
	{
		return this.GetValStringByKey(FrmAttachmentAttr.NoOfObj);
	}
	public final void setNoOfObj(String value)
	{
		this.SetValByKey(FrmAttachmentAttr.NoOfObj, value);
	}
	/** 
	 Y
	 
	*/
	public final float getY()
	{
		return this.GetValFloatByKey(FrmAttachmentAttr.Y);
	}
	public final void setY(float value)
	{
		this.SetValByKey(FrmAttachmentAttr.Y, value);
	}
	/** 
	 X
	 
	*/
	public final float getX()
	{
		return this.GetValFloatByKey(FrmAttachmentAttr.X);
	}
	public final void setX(float value)
	{
		this.SetValByKey(FrmAttachmentAttr.X, value);
	}
	/** 
	 W
	 
	*/
	public final float getW()
	{
		return this.GetValFloatByKey(FrmAttachmentAttr.W);
	}
	public final void setW(float value)
	{
		this.SetValByKey(FrmAttachmentAttr.W, value);
	}
	/** 
	 H
	 
	*/
	public final float getH()
	{
		return this.GetValFloatByKey(FrmAttachmentAttr.H);
	}
	public final void setH(float value)
	{
		this.SetValByKey(FrmAttachmentAttr.H, value);
	}

	public final int getGroupID()
	{
		return this.GetValIntByKey(FrmAttachmentAttr.GroupID);
	}
	public final void setGroupID(int value)
	{
		this.SetValByKey(FrmAttachmentAttr.GroupID, value);
	}
	/** 
	 数据控制方式
	 
	*/
	public final AthCtrlWay getHisCtrlWay()
	{
		return AthCtrlWay.forValue(this.GetValIntByKey(FrmAttachmentAttr.CtrlWay));
	}
	public final void setHisCtrlWay(AthCtrlWay value)
	{
		this.SetValByKey(FrmAttachmentAttr.CtrlWay, value.getValue());
	}
	/** 
	 是否是合流汇总多附件？
	 
	*/
	public final boolean getIsHeLiuHuiZong()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsHeLiuHuiZong);
	}
	public final void setIsHeLiuHuiZong(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsHeLiuHuiZong, value);
	}
	/** 
	 该附件是否汇总到合流节点上去？
	 
	*/
	public final boolean getIsToHeLiuHZ()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsToHeLiuHZ);
	}
	public final void setIsToHeLiuHZ(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsToHeLiuHZ, value);
	}
	/** 
	 文件展现方式
	 
	*/
	public final FileShowWay getFileShowWay()
	{
		return FileShowWay.forValue(this.GetParaInt(FrmAttachmentAttr.FileShowWay));
	}
	public final void setFileShowWay(FileShowWay value)
	{
		this.SetPara(FrmAttachmentAttr.FileShowWay, value.getValue());
	}
	/** 
	 上传方式（对于父子流程有效）
	 
	*/
	public final AthUploadWay getAthUploadWay()
	{
		return AthUploadWay.forValue(this.GetValIntByKey(FrmAttachmentAttr.AthUploadWay));
	}
	public final void setAthUploadWay(AthUploadWay value)
	{
		this.SetValByKey(FrmAttachmentAttr.AthUploadWay, value.getValue());
	}
	/** 
	 FK_MapData
	 
	*/
	public final String getFK_MapData()
	{
		return this.GetValStrByKey(FrmAttachmentAttr.FK_MapData);
	}
	public final void setFK_MapData(String value)
	{
		this.SetValByKey(FrmAttachmentAttr.FK_MapData, value);
	}

		///#endregion


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


		///#region 快捷键
	/** 
	 是否启用快捷键
	 
	*/
	public final boolean getFastKeyIsEnable()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.FastKeyIsEnable);
	}
	public final void setFastKeyIsEnable(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.FastKeyIsEnable, value);
	}
	/** 
	 启用规则
	 
	*/
	public final String getFastKeyGenerRole()
	{
		return this.GetParaString(FrmAttachmentAttr.FastKeyGenerRole);
	}
	public final void setFastKeyGenerRole(String value)
	{
		this.SetPara(FrmAttachmentAttr.FastKeyGenerRole, value);
	}

		///#endregion 快捷键


		
	/** 
	 附件
	 
	*/
	public FrmAttachmentExt()
	{
	}
	/** 
	 附件
	 
	 @param mypk 主键
	 * @throws Exception 
	*/
	public FrmAttachmentExt(String mypk) throws Exception
	{
		this.setMyPK(mypk);
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

		Map map = new Map("Sys_FrmAttachment", "附件");

		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);
		map.AddMyPK();
		map.AddTBString(FrmAttachmentAttr.FK_MapData, null, "表单ID", true, true, 1, 100, 20);
		map.AddTBString(FrmAttachmentAttr.NoOfObj, null, "附件编号", true, true, 0, 50, 20);
		map.AddTBInt(FrmAttachmentAttr.FK_Node, 0, "节点控制(对sln有效)", false, false);

		map.AddTBString(FrmAttachmentAttr.Name, null, "附件名称", true, false, 0, 50, 20);
		map.AddTBString(FrmAttachmentAttr.Exts, null, "文件格式(*.*,*.doc)", true, false, 0, 50, 20, true, null);

	//	map.AddTBString(FrmAttachmentAttr.SaveTo, null, "保存到", true, false, 0, 150, 20, true, null);
		
		map.AddTBString(FrmAttachmentAttr.Sort, null, "类别(可为空)", true, false, 0, 500, 20, true, null);
		map.AddBoolean(FrmAttachmentAttr.IsTurn2Hmtl, false, "是否转换成html(方便手机浏览)", true, true, true);

			//位置.
		map.AddTBFloat(FrmAttachmentAttr.X, 5, "X", false, false);
		map.AddTBFloat(FrmAttachmentAttr.Y, 5, "Y", false, false);

		map.AddTBFloat(FrmAttachmentAttr.W, 40, "宽度", false, false);
		map.AddTBFloat(FrmAttachmentAttr.H, 150, "高度", false, false);

			///#endregion 基本属性。


			///#region 权限控制。
			//hzm新增列
			// map.AddTBInt(FrmAttachmentAttr.DeleteWay, 0, "附件删除规则(0=不能删除1=删除所有2=只能删除自己上传的", false, false);

		map.AddDDLSysEnum(FrmAttachmentAttr.DeleteWay, 0, "附件删除规则", true, true, FrmAttachmentAttr.DeleteWay, "@0=不能删除@1=删除所有@2=只能删除自己上传的");

		map.AddBoolean(FrmAttachmentAttr.IsUpload, true, "是否可以上传", false, false);
		map.AddBoolean(FrmAttachmentAttr.IsDownload, true, "是否可以下载", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsOrder, false, "是否可以排序", true, true);

		map.AddBoolean(FrmAttachmentAttr.IsAutoSize, true, "自动控制大小", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsNote, true, "是否增加备注", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsShowTitle, true, "是否显示标题列", true, true);
		map.AddDDLSysEnum(FrmAttachmentAttr.UploadType, 0, "上传类型", true, false, FrmAttachmentAttr.CtrlWay, "@0=单个@1=多个@2=指定");

			//对于父子流程有效.
			 //map.AddTBInt(FrmAttachmentAttr.CtrlWay, 0, "控制呈现控制方式0=PK,1=FID,2=ParentID", true, false);
		map.AddDDLSysEnum(FrmAttachmentAttr.CtrlWay, 0, "控制呈现控制方式", true, true, FrmAttachmentAttr.CtrlWay+"Ath", "@0=PK-主键@1=FID-流程ID@2=ParentID-父流程ID@3=仅能查看自己上传的数据");

		map.AddDDLSysEnum(FrmAttachmentAttr.AthUploadWay, 0, "控制上传控制方式", true, true, FrmAttachmentAttr.CtrlWay, "@0=继承模式@1=协作模式");


			///#endregion 权限控制。


			///#region WebOffice控制方式。
		map.AddBoolean(FrmAttachmentAttr.IsRowLock, true, "是否启用锁定行", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableWF, true, "是否启用weboffice", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableSave, true, "是否启用保存", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableReadonly, true, "是否只读", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableRevise, true, "是否启用修订", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableViewKeepMark, true, "是否查看用户留痕", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnablePrint, true, "是否打印", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableSeal, true, "是否启用签章", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableOver, true, "是否启用套红", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableTemplete, true, "是否启用公文模板", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableCheck, true, "是否自动写入审核信息", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableInsertFlow, true, "是否插入流程", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableInsertFengXian, true, "是否插入风险点", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableMarks, true, "是否启用留痕模式", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableDown, true, "是否启用下载", true, true);

			///#endregion WebOffice控制方式。


			///#region 节点相关
			//map.AddDDLSysEnum(FrmAttachmentAttr.DtlOpenType, 0, "附件删除规则", true, true, FrmAttachmentAttr.DeleteWay, 
			//    "@0=不能删除@1=删除所有@2=只能删除自己上传的");
		map.AddBoolean(FrmAttachmentAttr.IsToHeLiuHZ, true, "该附件是否要汇总到合流节点上去？(对子线程节点有效)", true, true, true);
		map.AddBoolean(FrmAttachmentAttr.IsHeLiuHuiZong, true, "是否是合流节点的汇总附件组件？(对合流节点有效)", true, true, true);

			///#endregion 节点相关

			//map.AddTBInt(FrmAttachmentAttr.AthUploadWay, 0, "控制上传控制方式0=继承模式,1=协作模式.", true, false);


			///#region 其他属性。
			//参数属性.
		map.AddTBAtParas(3000);
		map.AddTBInt(FrmAttachmentAttr.GroupID, 0, "GroupID", false, true);
		map.AddTBString(FrmAttachmentAttr.GUID, null, "GUID", false, true, 0, 128, 20);

			///#endregion 其他属性。


		RefMethod rm = new RefMethod();
		rm.Title = "高级配置";
		  //  rm.Icon = "/WF/Admin/CCFormDesigner/Img/Menu/CC.png";
		rm.ClassMethodName = this.toString() + ".DoAdv";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	/** 
	 执行高级设置.
	 
	 @return 
	*/
	public final String DoAdv()
	{
		return "/WF/Admin/FoolFormDesigner/Attachment.htm?FK_MapData=" + this.getFK_MapData() + "&MyPK=" + this.getMyPK() + "&Ath=" + this.getNoOfObj();
		//   var url = 'Attachment.htm?FK_MapData=' + fk_mapdata + '&Ath=' + ath;
	}

	public boolean IsUse = false;
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		if (this.getFK_Node() == 0)
		{
			//适应设计器新的规则 by dgq 
			if (!StringHelper.isNullOrEmpty(this.getNoOfObj()) && this.getNoOfObj().contains(this.getFK_MapData()))
			{
				this.setMyPK(this.getNoOfObj());
			}
			else
			{
				this.setMyPK(this.getFK_MapData() + "_" + this.getNoOfObj());
			}
		}
		else
		{
			this.setMyPK(this.getFK_MapData() + "_" + this.getNoOfObj() + "_" + this.getFK_Node());
		}

		return super.beforeUpdateInsertAction();
	}
	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.setIsWoEnableWF(true);

		this.setIsWoEnableSave(false);
		this.setIsWoEnableReadonly(false);
		this.setIsWoEnableRevise(false);
		this.setIsWoEnableViewKeepMark(false);
		this.setIsWoEnablePrint(false);
		this.setIsWoEnableOver(false);
		this.setIsWoEnableSeal(false);
		this.setIsWoEnableTemplete(false);

		if (this.getFK_Node() == 0)
		{
			this.setMyPK(this.getFK_MapData() + "_" + this.getNoOfObj());
		}
		else
		{
			this.setMyPK(this.getFK_MapData() + "_" + this.getNoOfObj() + "_" + this.getFK_Node());
		}

		return super.beforeInsert();
	}
	/** 
	 插入之后
	 * @throws Exception 
	 
	*/
	@Override
	protected void afterInsert() throws Exception
	{
		GroupField gf = new GroupField();
		if (gf.IsExit(GroupFieldAttr.CtrlID, this.getMyPK()) == false)
		{
			gf.setEnName(this.getFK_MapData());
			gf.setCtrlID(this.getMyPK());
			gf.setCtrlType("Ath");
			gf.setLab(this.getName());
			gf.setIdx(0);
			gf.Insert(); //插入.
		}
		super.afterInsert();
	}

	/** 
	 删除之后.
	 * @throws Exception 
	 
	*/
	@Override
	protected void afterDelete() throws Exception
	{
		GroupField gf = new GroupField();
		gf.Delete(GroupFieldAttr.CtrlID, this.getMyPK());

		super.afterDelete();
	}
}
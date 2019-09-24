package BP.Sys.FrmUI;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Sys.*;
import BP.Tools.FtpUtil;
import BP.Web.WebUser;
import BP.Sys.*;
import java.util.*;

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
		if (WebUser.getNo().equals("admin") || WebUser.getIsAdmin() == true)
		{
			uac.IsUpdate = true;
			uac.IsDelete = true;
			return uac;
		}
		return uac;
	}

		///#region 参数属性.
	/** 
	 是否可见？
	 * @throws Exception 
	*/
	public final boolean getIsVisable() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsVisable, true);
	}
	public final void setIsVisable(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsVisable, value);
	}
	/** 
	 附件类型
	 * @throws Exception 
	*/
	public final int getFileType() throws Exception
	{
		return this.GetParaInt(FrmAttachmentAttr.FileType);
	}
	public final void setFileType(int value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.FileType, value);
	}
	/** 
	 使用上传附件的 - 控件类型
	 0=批量.
	 1=单个。
	 * @throws Exception 
	*/
	public final int getUploadCtrl() throws Exception
	{
		return this.GetParaInt(FrmAttachmentAttr.UploadCtrl);
	}
	public final void setUploadCtrl(int value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.UploadCtrl, value);
	}
	/** 
	 上传校验
	 0=不校验.
	 1=不能为空.
	 2=每个类别下不能为空.
	 * @throws Exception 
	*/
	public final UploadFileNumCheck getUploadFileNumCheck() throws Exception
	{
		return UploadFileNumCheck.forValue(this.GetValIntByKey(FrmAttachmentAttr.UploadFileNumCheck));
	}
	public final void setUploadFileNumCheck(UploadFileNumCheck value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.UploadFileNumCheck, value.getValue());
	}

		///#endregion 参数属性.

		///#region 属性
	/** 
	 节点编号
	 * @throws Exception 
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(FrmAttachmentAttr.FK_Node);
	}
	public final void setFK_Node(int value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.FK_Node, value);
	}
	/** 
	 上传类型（单个的，多个，指定的）
	 * @throws Exception 
	*/
	public final AttachmentUploadType getUploadType() throws Exception
	{
		return AttachmentUploadType.forValue(this.GetValIntByKey(FrmAttachmentAttr.UploadType));
	}
	public final void setUploadType(AttachmentUploadType value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.UploadType, value.getValue());
	}
	/** 
	 类型名称
	 * @throws Exception 
	*/
	public final String getUploadTypeT() throws Exception
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
	 * @throws Exception 
	*/
	public final boolean getIsUpload() throws Exception
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsUpload);
	}
	public final void setIsUpload(boolean value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.IsUpload, value);
	}
	/** 
	 是否可以下载
	 * @throws Exception 
	*/
	public final boolean getIsDownload() throws Exception
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsDownload);
	}
	public final void setIsDownload(boolean value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.IsDownload, value);
	}
	/** 
	 附件删除方式
	 * @throws Exception 
	*/
	public final AthDeleteWay getHisDeleteWay() throws Exception
	{
		return AthDeleteWay.forValue(this.GetValIntByKey(FrmAttachmentAttr.DeleteWay));
	}
	public final void setHisDeleteWay(AthDeleteWay value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.DeleteWay, value.getValue());
	}

	/** 
	 是否可以排序?
	 * @throws Exception 
	*/
	public final boolean getIsOrder() throws Exception
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsOrder);
	}
	public final void setIsOrder(boolean value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.IsOrder, value);
	}
	/** 
	 自动控制大小
	 * @throws Exception 
	*/
	public final boolean getIsAutoSize() throws Exception
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsAutoSize);
	}
	public final void setIsAutoSize(boolean value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.IsAutoSize, value);
	}
	/** 
	 IsShowTitle
	 * @throws Exception 
	*/
	public final boolean getIsShowTitle() throws Exception
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsShowTitle);
	}
	public final void setIsShowTitle(boolean value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.IsShowTitle, value);
	}
	/** 
	 是否是节点表单.
	 * @throws Exception 
	*/
	public final boolean getIsNodeSheet() throws Exception
	{
		if (this.getFK_MapData().startsWith("ND") == true)
		{
			return true;
		}
		return false;
	}
	/** 
	 备注列
	 * @throws Exception 
	*/
	public final boolean getIsNote() throws Exception
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsNote);
	}
	public final void setIsNote(boolean value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.IsNote, value);
	}
	/** 
	 附件名称
	 * @throws Exception 
	*/
	public final String getName() throws Exception
	{
		String str = this.GetValStringByKey(FrmAttachmentAttr.Name);
		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = "未命名";
		}
		return str;
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.Name, value);
	}
	/** 
	 类别
	 * @throws Exception 
	*/
	public final String getSort() throws Exception
	{
		return this.GetValStringByKey(FrmAttachmentAttr.Sort);
	}
	public final void setSort(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.Sort, value);
	}
	/** 
	 要求的格式
	 * @throws Exception 
	*/
	public final String getExts() throws Exception
	{
		return this.GetValStringByKey(FrmAttachmentAttr.Exts);
	}
	public final void setExts(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.Exts, value);
	}
	public final String getSaveTo() throws Exception
	{
		String s = this.GetValStringByKey(FrmAttachmentAttr.SaveTo);
		if (s.equals("") || s == null)
		{
			s = SystemConfig.getPathOfDataUser() + "\\UploadFile\\" + this.getFK_MapData() + "\\";
		}
		return s;
	}
	public final void setSaveTo(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.SaveTo, value);
	}
	/** 
	 附件标识
	*/
	public final String getNoOfObj() throws Exception
	{
		return this.GetValStringByKey(FrmAttachmentAttr.NoOfObj);
	}
	public final void setNoOfObj(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.NoOfObj, value);
	}
	/** 
	 Y
	*/
	public final float getY() throws Exception
	{
		return this.GetValFloatByKey(FrmAttachmentAttr.Y);
	}
	public final void setY(float value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.Y, value);
	}
	/** 
	 X
	*/
	public final float getX() throws Exception
	{
		return this.GetValFloatByKey(FrmAttachmentAttr.X);
	}
	public final void setX(float value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.X, value);
	}
	/** 
	 W
	*/
	public final float getW() throws Exception
	{
		return this.GetValFloatByKey(FrmAttachmentAttr.W);
	}
	public final void setW(float value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.W, value);
	}
	/** 
	 H
	*/
	public final float getH() throws Exception
	{
		return this.GetValFloatByKey(FrmAttachmentAttr.H);
	}
	public final void setH(float value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.H, value);
	}
	/** 
	 数据控制方式 
	*/
	public final AthCtrlWay getHisCtrlWay() throws Exception
	{
		return AthCtrlWay.forValue(this.GetValIntByKey(FrmAttachmentAttr.CtrlWay));
	}
	public final void setHisCtrlWay(AthCtrlWay value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.CtrlWay, value.getValue());
	}
	/** 
	 是否是合流汇总多附件？
	*/
	public final boolean getIsHeLiuHuiZong() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsHeLiuHuiZong);
	}
	public final void setIsHeLiuHuiZong(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsHeLiuHuiZong, value);
	}
	/** 
	 该附件是否汇总到合流节点上去？
	*/
	public final boolean getIsToHeLiuHZ() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsToHeLiuHZ);
	}
	public final void setIsToHeLiuHZ(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsToHeLiuHZ, value);
	}
	/** 
	 文件展现方式
	*/
	public final FileShowWay getFileShowWay() throws Exception
	{
		return FileShowWay.forValue(this.GetParaInt(FrmAttachmentAttr.FileShowWay));
	}
	public final void setFileShowWay(FileShowWay value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.FileShowWay, value.getValue());
	}
	/** 
	 上传方式（对于父子流程有效）
	*/
	public final AthUploadWay getAthUploadWay() throws Exception
	{
		return AthUploadWay.forValue(this.GetValIntByKey(FrmAttachmentAttr.AthUploadWay));
	}
	public final void setAthUploadWay(AthUploadWay value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.AthUploadWay, value.getValue());
	}
	/** 
	 FK_MapData
	*/
	public final String getFK_MapData() throws Exception
	{
		return this.GetValStrByKey(FrmAttachmentAttr.FK_MapData);
	}
	public final void setFK_MapData(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.FK_MapData, value);
	}

	/** 
	 是否要转换成html
	*/
	public final boolean getIsTurn2Html() throws Exception
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsTurn2Html);
	}
		///#region weboffice文档属性(参数属性)
	/** 
	 是否启用锁定行
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
		///#region 快捷键
	/** 
	 是否启用快捷键
	*/
	public final boolean getFastKeyIsEnable() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.FastKeyIsEnable);
	}
	public final void setFastKeyIsEnable(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.FastKeyIsEnable, value);
	}
	/** 
	 启用规则
	*/
	public final String getFastKeyGenerRole() throws Exception
	{
		return this.GetParaString(FrmAttachmentAttr.FastKeyGenerRole);
	}
	public final void setFastKeyGenerRole(String value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.FastKeyGenerRole, value);
	}
		///#endregion 快捷键

		///#region 构造方法
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
		map.IndexField = MapAttrAttr.FK_MapData;

		map.AddMyPK();

			///#region 基本属性。
		map.AddTBString(FrmAttachmentAttr.FK_MapData, null, "表单ID", true, true, 1, 100, 20);
		map.AddTBString(FrmAttachmentAttr.NoOfObj, null, "附件标识", true, true, 0, 50, 20);
		map.AddTBInt(FrmAttachmentAttr.FK_Node, 0, "节点控制(对sln有效)", false, false);

			//for渔业厅增加.
		map.AddDDLSysEnum(FrmAttachmentAttr.AthRunModel, 0, "运行模式", true, true, FrmAttachmentAttr.AthRunModel, "@0=流水模式@1=固定模式@2=自定义页面");

		map.AddTBString(FrmAttachmentAttr.Name, null, "附件名称", true, false, 0, 50, 20, true);

		map.AddTBString(FrmAttachmentAttr.Exts, null, "文件格式", true, false, 0, 50, 20, true, null);
		map.SetHelperAlert(FrmAttachmentAttr.Exts, "上传要求,设置模式为: *.*, *.doc, *.docx, *.png,多个中间用逗号分开.\t\n表示仅仅允许上传指定的后缀的文件.");

		map.AddTBInt(FrmAttachmentAttr.NumOfUpload, 0, "最小上传数量", true, false);
		map.SetHelperAlert("NumOfUpload", "如果为0则标识必须上传. \t\n用户上传的附件数量低于指定的数量就不让保存.");

		map.AddTBInt(FrmAttachmentAttr.TopNumOfUpload, 99, "最大上传数量", true, false);
		map.AddTBInt(FrmAttachmentAttr.FileMaxSize, 10240, "附件最大限制(KB)", true, false);
		map.AddDDLSysEnum(FrmAttachmentAttr.UploadFileNumCheck, 0, "上传校验方式", true, true, FrmAttachmentAttr.UploadFileNumCheck, "@0=不用校验@1=不能为空@2=每个类别下不能为空");

			//for tianye group 
		map.AddDDLSysEnum(FrmAttachmentAttr.AthSaveWay, 0, "保存方式", true, true, FrmAttachmentAttr.AthSaveWay, "@0=保存到web服务器@1=保存到数据库@2=ftp服务器");

		map.AddTBString(FrmAttachmentAttr.SaveTo, null, "保存到", false, false, 0, 150, 20, true, null);

		map.AddTBString(FrmAttachmentAttr.Sort, null, "类别", true, false, 0, 500, 20, true, null);
		map.SetHelperAlert(FrmAttachmentAttr.Sort, "设置格式:生产类,文件类,其他，也可以设置一个SQL，比如select Name FROM Port_Dept  \t\n目前已经支持了扩展列,可以使用扩展列定义更多的字段，该设置将要被取消.");

		map.AddBoolean(FrmAttachmentAttr.IsTurn2Html, false, "是否转换成html(方便手机浏览)", true, true, true);

			//位置.
		map.AddTBFloat(FrmAttachmentAttr.X, 5, "X", false, false);
		map.AddTBFloat(FrmAttachmentAttr.Y, 5, "Y", false, false);

		map.AddTBFloat(FrmAttachmentAttr.W, 40, "宽度", true, false);
		map.AddTBFloat(FrmAttachmentAttr.H, 150, "高度", true, false);

			//附件是否显示
		map.AddBoolean(FrmAttachmentAttr.IsVisable, true, "是否显示附件分组", true, true, true);

		map.AddDDLSysEnum(FrmAttachmentAttr.FileType, 0, "附件类型", true, true, FrmAttachmentAttr.FileType, "@0=普通附件@1=图片文件");

			///#endregion 基本属性。

			///#region 权限控制。
			//hzm新增列
			// map.AddTBInt(FrmAttachmentAttr.DeleteWay, 0, "附件删除规则(0=不能删除1=删除所有2=只能删除自己上传的", false, false);

		map.AddDDLSysEnum(FrmAttachmentAttr.DeleteWay, 1, "附件删除规则", true, true, FrmAttachmentAttr.DeleteWay, "@0=不能删除@1=删除所有@2=只能删除自己上传的");

		map.AddBoolean(FrmAttachmentAttr.IsUpload, true, "是否可以上传", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsDownload, true, "是否可以下载", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsOrder, false, "是否可以排序", true, true);

		map.AddBoolean(FrmAttachmentAttr.IsAutoSize, true, "自动控制大小", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsNote, true, "是否增加备注", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsExpCol, true, "是否启用扩展列", true, true);

		map.AddBoolean(FrmAttachmentAttr.IsShowTitle, true, "是否显示标题列", true, true);
		map.AddDDLSysEnum(FrmAttachmentAttr.UploadType, 0, "上传类型", true, false, FrmAttachmentAttr.CtrlWay, "@0=单个@1=多个@2=指定");

		map.AddDDLSysEnum(FrmAttachmentAttr.AthUploadWay, 0, "控制上传控制方式", true, true, FrmAttachmentAttr.AthUploadWay, "@0=继承模式@1=协作模式");

		map.AddDDLSysEnum(FrmAttachmentAttr.CtrlWay, 0, "控制呈现控制方式", true, true, "Ath" + FrmAttachmentAttr.CtrlWay, "@0=PK-主键@1=FID-流程ID@2=ParentID-父流程ID@3=仅能查看自己上传的附件@4=按照WorkID计算(对流程节点表单有效)");


			//map.AddDDLSysEnum(FrmAttachmentAttr.DataRef, 0, "数据引用", true, true, FrmAttachmentAttr.DataRef,
			//    "@0=当前组件ID@1=指定的组件ID");
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
		map.AddTBString(FrmAttachmentAttr.DataRefNoOfObj, "AttachM1", "对应附件标识", true, false, 0, 150, 20);
		map.SetHelperAlert("DataRefNoOfObj", "对WorkID权限模式有效,用于查询贯穿整个流程的附件标识,与从表的标识一样.");

		map.AddDDLSysEnum(FrmAttachmentAttr.ReadRole, 0, "阅读规则", true, true, FrmAttachmentAttr.ReadRole, "@0=不控制@1=未阅读阻止发送@2=未阅读做记录");
			///#endregion 节点相关

			///#region 其他属性。
			//参数属性.
		map.AddTBAtParas(3000);
			///#endregion 其他属性。

		RefMethod rm = new RefMethod();
		rm.Title = "高级配置";
			//  rm.Icon = "/WF/Admin/CCFormDesigner/Img/Menu/CC.png";
			//rm.ClassMethodName = this.ToString() + ".DoAdv";
			// rm.RefMethodType = RefMethodType.RightFrameOpen;
			//  map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "类别设置";
		rm.ClassMethodName = this.toString() + ".DoSettingSort";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "测试FTP服务器";
		rm.ClassMethodName = this.toString() + ".DoTestFTPHost";
		rm.refMethodType = RefMethodType.Func;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置扩展列";
		rm.ClassMethodName = this.toString() + ".DtlOfAth";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion

	public final String DtlOfAth() throws Exception
	{
		String url = "../../Admin/FoolFormDesigner/MapDefDtlFreeFrm.htm?FK_MapDtl=" + this.getMyPK() + "&For=" + this.getMyPK();
		return url;
	}

	/** 
	 测试连接
	 
	 @return 
	*/
	public String DoTestFTPHost() {
		FtpUtil ftpUtil;
		try {
			ftpUtil = BP.WF.Glo.getFtpUtil();
		} catch (Exception e) {

			e.printStackTrace();
			return "err@" + e.getMessage();
		}
		return ftpUtil.openConnection();

	}
	/** 
	 固定模式类别设置
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoSettingSort() throws Exception
	{
		return "../../Admin/FoolFormDesigner/AttachmentSortSetting.htm?FK_MapData=" + this.getFK_MapData() + "&MyPK=" + this.getMyPK() + "&Ath=" + this.getNoOfObj();
	}
	/** 
	 执行高级设置.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoAdv() throws Exception
	{
		return "/WF/Admin/FoolFormDesigner/Attachment.aspx?FK_MapData=" + this.getFK_MapData() + "&MyPK=" + this.getMyPK() + "&Ath=" + this.getNoOfObj();
	}

	public boolean IsUse = false;
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		if (this.getFK_Node() == 0)
		{
			//适应设计器新的规则 by dgq 
			if (!DataType.IsNullOrEmpty(this.getNoOfObj()) && this.getNoOfObj().contains(this.getFK_MapData()))
			{
				this.setMyPK(this.getNoOfObj());
			}
			else
			{
				this.setMyPK(this.getFK_MapData() + "_" + this.getNoOfObj());
			}
		}

		if (this.getFK_Node() != 0)
		{
			/*工作流程模式.*/
			if (this.getHisCtrlWay() == AthCtrlWay.PK)
			{
				this.setHisCtrlWay(AthCtrlWay.WorkID);
			}
			this.setMyPK(this.getFK_MapData() + "_" + this.getNoOfObj() + "_" + this.getFK_Node());
		}


			///#region 处理分组.
		//更新相关的分组信息.
		if (this.getIsVisable() == true)
		{
			GroupField gf = new GroupField();
			int i = gf.Retrieve(GroupFieldAttr.FrmID, this.getFK_MapData(), GroupFieldAttr.CtrlID, this.getMyPK());
			if (i == 0)
			{
				gf.setLab(this.getName());
				gf.setFrmID(this.getFK_MapData());
				gf.setCtrlType("Ath");
				//gf.CtrlID = this.MyPK;
				gf.Insert();
			}
			else
			{
				gf.setLab(this.getName());
				gf.setFrmID(this.getFK_MapData());
				gf.setCtrlType("Ath");
				//gf.CtrlID = this.MyPK;
				gf.Update();
			}
		}

		if (this.getIsVisable() == false)
		{
			GroupField gf = new GroupField();
			gf.Delete(GroupFieldAttr.FrmID, this.getFK_MapData(), GroupFieldAttr.CtrlID, this.getMyPK());
		}
			///#endregion 处理分组.


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
			gf.setFrmID(this.getFK_MapData());
			gf.setCtrlID(this.getMyPK());
			gf.setCtrlType("Ath");
			gf.setLab(this.getName());
			gf.setIdx(0);
			gf.Insert(); //插入.
		}
		super.afterInsert();
	}

	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		FrmAttachment ath = new FrmAttachment();
		ath.setMyPK(this.getMyPK());
		ath.RetrieveFromDBSources();
		ath.Update();
		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterInsertUpdateAction();
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

		//把相关的字段也要删除.
		MapAttrString attr = new MapAttrString();
		attr.setMyPK(this.getMyPK());
		attr.Delete();

		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterDelete();
	}
}
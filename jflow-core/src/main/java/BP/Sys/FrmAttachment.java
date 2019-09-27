package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;

/**
 * 附件
 */
public class FrmAttachment extends EntityMyPK {

	/// #region 参数属性.
	/**
	 * 是否可见？
	 * 
	 * @throws Exception
	 */
	public final boolean getIsVisable() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsVisable, true);
	}

	public final void setIsVisable(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsVisable, value);
	}

	/**
	 * 使用上传附件的 - 控件类型 0=批量. 1=单个。
	 * 
	 * @throws Exception
	 */
	public final int getUploadCtrl() throws Exception {
		return this.GetParaInt(FrmAttachmentAttr.UploadCtrl);
	}

	public final void setUploadCtrl(int value) throws Exception {
		this.SetPara(FrmAttachmentAttr.UploadCtrl, value);
	}

	/**
	 * 最低上传数量
	 * 
	 * @throws Exception
	 */
	public final int getNumOfUpload() throws Exception {
		return this.GetValIntByKey(FrmAttachmentAttr.NumOfUpload);
	}

	public final void setNumOfUpload(int value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.NumOfUpload, value);
	}

	/**
	 * 最大上传数量
	 * 
	 * @throws Exception
	 */
	public final int getTopNumOfUpload() throws Exception {
		return this.GetValIntByKey(FrmAttachmentAttr.TopNumOfUpload);
	}

	public final void setTopNumOfUpload(int value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.TopNumOfUpload, value);
	}

	/**
	 * 附件最大限制
	 * 
	 * @throws Exception
	 */
	public final int getFileMaxSize() throws Exception {
		return this.GetValIntByKey(FrmAttachmentAttr.FileMaxSize);
	}

	public final void setFileMaxSize(int value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.FileMaxSize, value);
	}

	/**
	 * 上传校验 0=不校验. 1=不能为空. 2=每个类别下不能为空.
	 * 
	 * @throws Exception
	 */
	public final UploadFileNumCheck getUploadFileNumCheck() throws Exception {
		return UploadFileNumCheck.forValue(this.GetValIntByKey(FrmAttachmentAttr.UploadFileNumCheck));
	}

	public final void setUploadFileNumCheck(UploadFileNumCheck value) throws Exception {
		this.SetPara(FrmAttachmentAttr.UploadFileNumCheck, value.getValue());
	}

	/**
	 * 保存方式 0 =文件方式保存。 1 = 保存到数据库. 2 = ftp服务器.
	 * 
	 * @throws Exception
	 */
	public final AthSaveWay getAthSaveWay() throws Exception {
		return AthSaveWay.forValue(this.GetValIntByKey(FrmAttachmentAttr.AthSaveWay));
	}

	/// #endregion 参数属性.

	/// #region 属性
	/**
	 * 节点编号
	 * 
	 * @throws Exception
	 */
	public final int getFK_Node() throws Exception {
		return this.GetValIntByKey(FrmAttachmentAttr.FK_Node);
	}

	public final void setFK_Node(int value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.FK_Node, value);
	}

	/**
	 * 运行模式？
	 * 
	 * @throws Exception
	 */
	public final AthRunModel getAthRunModel() throws Exception {
		return AthRunModel.forValue(this.GetValIntByKey(FrmAttachmentAttr.AthRunModel));
	}

	public final void setAthRunModel(AthRunModel value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.AthRunModel, value.getValue());
	}

	/**
	 * 上传类型（单个的，多个，指定的）
	 * 
	 * @throws Exception
	 */
	public final AttachmentUploadType getUploadType() throws Exception {
		return AttachmentUploadType.forValue(this.GetValIntByKey(FrmAttachmentAttr.UploadType));
	}

	public final void setUploadType(AttachmentUploadType value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.UploadType, value.getValue());
	}

	/**
	 * 类型名称
	 * 
	 * @throws Exception
	 */
	public final String getUploadTypeT() throws Exception {
		if (this.getUploadType() == AttachmentUploadType.Multi) {
			return "多附件";
		}
		if (this.getUploadType() == AttachmentUploadType.Single) {
			return "单附件";
		}
		if (this.getUploadType() == AttachmentUploadType.Specifically) {
			return "指定的";
		}
		return "XXXXX";
	}

	/**
	 * 是否可以上传
	 * 
	 * @throws Exception
	 */
	public final boolean getIsUpload() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsUpload);
	}

	public final void setIsUpload(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsUpload, value);
	}

	/**
	 * 是否可以下载
	 * 
	 * @throws Exception
	 */
	public final boolean getIsDownload() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsDownload);
	}

	public final void setIsDownload(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsDownload, value);
	}

	/**
	 * 附件删除方式
	 * 
	 * @throws Exception
	 */
	public final AthDeleteWay getHisDeleteWay() throws Exception {
		return AthDeleteWay.forValue(this.GetValIntByKey(FrmAttachmentAttr.DeleteWay));
	}

	public final void setHisDeleteWay(AthDeleteWay value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.DeleteWay, value.getValue());
	}

	/**
	 * 是否可以排序?
	 * 
	 * @throws Exception
	 */
	public final boolean getIsOrder() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsOrder);
	}

	public final void setIsOrder(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsOrder, value);
	}

	/**
	 * 自动控制大小
	 * 
	 * @throws Exception
	 */
	public final boolean getIsAutoSize() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsAutoSize);
	}

	public final void setIsAutoSize(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsAutoSize, value);
	}

	/**
	 * IsShowTitle
	 * 
	 * @throws Exception
	 */
	public final boolean getIsShowTitle() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsShowTitle);
	}

	public final void setIsShowTitle(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsShowTitle, value);
	}

	/**
	 * 是否是节点表单.
	 * 
	 * @throws Exception
	 */
	public final boolean getIsNodeSheet() throws Exception {
		if (this.getFK_MapData().startsWith("ND") == true) {
			return true;
		}
		return false;
	}

	/**
	 * 备注列
	 * 
	 * @throws Exception
	 */
	public final boolean getIsNote() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsNote);
	}

	public final void setIsNote(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsNote, value);
	}

	/**
	 * 是否启用扩张列
	 * 
	 * @throws Exception
	 */
	public final boolean getIsExpCol() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsExpCol);
	}

	public final void setIsExpCol(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsExpCol, value);
	}

	/**
	 * 附件名称
	 * 
	 * @throws Exception
	 */
	public final String getName() throws Exception {
		String str = this.GetValStringByKey(FrmAttachmentAttr.Name);
		if (DataType.IsNullOrEmpty(str) == true) {
			str = "未命名";
		}
		return str;
	}

	public final void setName(String value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.Name, value);
	}

	/**
	 * 类别
	 * 
	 * @throws Exception
	 */
	public final String getSort() throws Exception {
		return this.GetValStringByKey(FrmAttachmentAttr.Sort);
	}

	public final void setSort(String value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.Sort, value);
	}

	/**
	 * 要求的格式
	 * 
	 * @throws Exception
	 */
	public final String getExts() throws Exception {
		return this.GetValStringByKey(FrmAttachmentAttr.Exts);
	}

	public final void setExts(String value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.Exts, value);
	}

	/**
	 * 保存到
	 * 
	 * @throws Exception
	 */
	public final String getSaveTo() throws Exception {
		if (this.getAthSaveWay() == AthSaveWay.WebServer) {
			String s = this.GetValStringByKey(FrmAttachmentAttr.SaveTo);
			if (s.equals("") || s == null) {
				s = SystemConfig.getPathOfDataUser() + "\\UploadFile\\" + this.getFK_MapData() + "\\";
			}
			return s;
		}

		if (this.getAthSaveWay() == AthSaveWay.FTPServer) {
			String s = this.GetValStringByKey(FrmAttachmentAttr.SaveTo);
			if (s.equals("") || s == null) {
				s = "//" + this.getFK_MapData() + "//";
			}
			return s;
		}

		return this.getFK_MapData();
	}

	public final void setSaveTo(String value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.SaveTo, value);
	}

	/**
	 * 数据关联组件ID
	 * 
	 * @throws Exception
	 */
	public final String getDataRefNoOfObj() throws Exception {
		String str = this.GetValStringByKey(FrmAttachmentAttr.DataRefNoOfObj);
		if (str.equals("")) {
			str = this.getNoOfObj();
		}
		return str;
	}

	public final void setDataRefNoOfObj(String value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.DataRefNoOfObj, value);
	}

	/**
	 * 附件编号
	 * 
	 * @throws Exception
	 */
	public final String getNoOfObj() throws Exception {
		return this.GetValStringByKey(FrmAttachmentAttr.NoOfObj);
	}

	public final void setNoOfObj(String value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.NoOfObj, value);
	}

	/**
	 * Y
	 * 
	 * @throws Exception
	 */
	public final float getY() throws Exception {
		return this.GetValFloatByKey(FrmAttachmentAttr.Y);
	}

	public final void setY(float value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.Y, value);
	}

	/**
	 * X
	 * 
	 * @throws Exception
	 */
	public final float getX() throws Exception {
		return this.GetValFloatByKey(FrmAttachmentAttr.X);
	}

	public final void setX(float value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.X, value);
	}

	/**
	 * W
	 * 
	 * @throws Exception
	 */
	public final float getW() throws Exception {
		return this.GetValFloatByKey(FrmAttachmentAttr.W);
	}

	public final void setW(float value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.W, value);
	}

	/**
	 * H
	 * 
	 * @throws Exception
	 */
	public final float getH() throws Exception {
		return this.GetValFloatByKey(FrmAttachmentAttr.H);
	}

	public final void setH(float value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.H, value);
	}

	public final int getGroupID() throws Exception {
		return this.GetValIntByKey(FrmAttachmentAttr.GroupID);
	}

	public final void setGroupID(long value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.GroupID, value);
	}

	/**
	 * 阅读规则:@0=不控制@1=未阅读阻止发送@2=未阅读做记录
	 * 
	 * @throws Exception
	 */
	public final int getReadRole() throws Exception {
		return this.GetValIntByKey(FrmAttachmentAttr.ReadRole);
	}

	public final void setReadRole(int value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.ReadRole, value);
	}

	public final int getRowIdx() throws Exception {
		return this.GetValIntByKey(FrmAttachmentAttr.RowIdx);
	}

	public final void setRowIdx(int value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.RowIdx, value);
	}

	/**
	 * 数据控制方式
	 */
	public final AthCtrlWay getHisCtrlWay() throws Exception {
		return AthCtrlWay.forValue(this.GetValIntByKey(FrmAttachmentAttr.CtrlWay));
	}

	public final void setHisCtrlWay(AthCtrlWay value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.CtrlWay, value.getValue());
	}

	/**
	 * 是否是合流汇总多附件？
	 */
	public final boolean getIsHeLiuHuiZong() throws Exception {
		return this.GetParaBoolen(FrmAttachmentAttr.IsHeLiuHuiZong);
	}

	public final void setIsHeLiuHuiZong(boolean value) throws Exception {
		this.SetPara(FrmAttachmentAttr.IsHeLiuHuiZong, value);
	}

	/**
	 * 该附件是否汇总到合流节点上去？
	 */
	public final boolean getIsToHeLiuHZ() throws Exception {
		return this.GetParaBoolen(FrmAttachmentAttr.IsToHeLiuHZ);
	}

	public final void setIsToHeLiuHZ(boolean value) throws Exception {
		this.SetPara(FrmAttachmentAttr.IsToHeLiuHZ, value);
	}

	/**
	 * 文件展现方式
	 */
	public final FileShowWay getFileShowWay() throws Exception {
		return FileShowWay.forValue(this.GetParaInt(FrmAttachmentAttr.FileShowWay));
	}

	public final void setFileShowWay(FileShowWay value) throws Exception {
		this.SetPara(FrmAttachmentAttr.FileShowWay, value.getValue());
	}

	/**
	 * 上传方式（对于父子流程有效）
	 */
	public final AthUploadWay getAthUploadWay() throws Exception {
		return AthUploadWay.forValue(this.GetValIntByKey(FrmAttachmentAttr.AthUploadWay));
	}

	public final void setAthUploadWay(AthUploadWay value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.AthUploadWay, value.getValue());
	}

	/**
	 * FK_MapData
	 */
	public final String getFK_MapData() throws Exception {
		return this.GetValStrByKey(FrmAttachmentAttr.FK_MapData);
	}

	public final void setFK_MapData(String value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.FK_MapData, value);
	}

	/// #endregion

	/// #region weboffice文档属性
	/**
	 * 是否启用锁定行
	 */
	public final boolean getIsRowLock() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsRowLock, false);
	}

	public final void setIsRowLock(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsRowLock, value);
	}

	/**
	 * 是否启用打印
	 */
	public final boolean getIsWoEnablePrint() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsWoEnablePrint);
	}

	public final void setIsWoEnablePrint(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsWoEnablePrint, value);
	}

	/**
	 * 是否启用只读
	 */
	public final boolean getIsWoEnableReadonly() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsWoEnableReadonly);
	}

	public final void setIsWoEnableReadonly(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsWoEnableReadonly, value);
	}

	/**
	 * 是否启用修订
	 */
	public final boolean getIsWoEnableRevise() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsWoEnableRevise);
	}

	public final void setIsWoEnableRevise(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsWoEnableRevise, value);
	}

	/**
	 * 是否启用保存
	 */
	public final boolean getIsWoEnableSave() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsWoEnableSave);
	}

	public final void setIsWoEnableSave(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsWoEnableSave, value);
	}

	/**
	 * 是否查看用户留痕
	 */
	public final boolean getIsWoEnableViewKeepMark() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsWoEnableViewKeepMark);
	}

	public final void setIsWoEnableViewKeepMark(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsWoEnableViewKeepMark, value);
	}

	/**
	 * 是否启用weboffice
	 */
	public final boolean getIsWoEnableWF() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsWoEnableWF);
	}

	public final void setIsWoEnableWF(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsWoEnableWF, value);
	}

	/**
	 * 是否启用套红
	 */
	public final boolean getIsWoEnableOver() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsWoEnableOver);
	}

	public final void setIsWoEnableOver(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsWoEnableOver, value);
	}

	/**
	 * 是否启用签章
	 */
	public final boolean getIsWoEnableSeal() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsWoEnableSeal);
	}

	public final void setIsWoEnableSeal(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsWoEnableSeal, value);
	}

	/**
	 * 是否启用公文模板
	 */
	public final boolean getIsWoEnableTemplete() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsWoEnableTemplete);
	}

	public final void setIsWoEnableTemplete(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsWoEnableTemplete, value);
	}

	/**
	 * 是否记录节点信息
	 */
	public final boolean getIsWoEnableCheck() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsWoEnableCheck);
	}

	public final void setIsWoEnableCheck(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsWoEnableCheck, value);
	}

	/**
	 * 是否插入流程图
	 */
	public final boolean getIsWoEnableInsertFlow() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsWoEnableInsertFlow);
	}

	public final void setIsWoEnableInsertFlow(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsWoEnableInsertFlow, value);
	}

	/**
	 * 是否插入风险点
	 */
	public final boolean getIsWoEnableInsertFengXian() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsWoEnableInsertFengXian);
	}

	public final void setIsWoEnableInsertFengXian(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsWoEnableInsertFengXian, value);
	}

	/**
	 * 是否启用留痕模式
	 */
	public final boolean getIsWoEnableMarks() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsWoEnableMarks);
	}

	public final void setIsWoEnableMarks(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsWoEnableMarks, value);
	}

	/**
	 * 是否插入风险点
	 */
	public final boolean getIsWoEnableDown() throws Exception {
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsWoEnableDown);
	}

	public final void setIsWoEnableDown(boolean value) throws Exception {
		this.SetValByKey(FrmAttachmentAttr.IsWoEnableDown, value);
	}

	/// #endregion weboffice文档属性

	/// #region 快捷键
	/**
	 * 是否启用快捷键
	 */
	public final boolean getFastKeyIsEnable() throws Exception {
		return this.GetParaBoolen(FrmAttachmentAttr.FastKeyIsEnable);
	}

	public final void setFastKeyIsEnable(boolean value) throws Exception {
		this.SetPara(FrmAttachmentAttr.FastKeyIsEnable, value);
	}

	/**
	 * 启用规则
	 */
	public final String getFastKeyGenerRole() throws Exception {
		return this.GetParaString(FrmAttachmentAttr.FastKeyGenerRole);
	}

	public final void setFastKeyGenerRole(String value) throws Exception {
		this.SetPara(FrmAttachmentAttr.FastKeyGenerRole, value);
	}

	/// #endregion 快捷键

	/// #region 构造方法
	/**
	 * 附件
	 */
	public FrmAttachment() {
	}

	/**
	 * 附件
	 * 
	 * @param mypk
	 */
	public FrmAttachment(String mypk) throws Exception {
		this.setMyPK(mypk);
		this.Retrieve();
	}

	/**
	 * EnMap
	 */
	@Override
	public Map getEnMap() {
		if (this.get_enMap() != null) {
			return this.get_enMap();
		}

		Map map = new Map("Sys_FrmAttachment", "附件");

		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);
		map.IndexField = FrmAttachmentAttr.FK_MapData;

		map.AddMyPK();

		map.AddTBString(FrmAttachmentAttr.FK_MapData, null, "表单ID", true, false, 1, 100, 20);
		map.AddTBString(FrmAttachmentAttr.NoOfObj, null, "附件编号", true, false, 0, 50, 20);
		map.AddTBInt(FrmAttachmentAttr.FK_Node, 0, "节点控制(对sln有效)", false, false);

		// for渔业厅增加.
		map.AddTBInt(FrmAttachmentAttr.AthRunModel, 0, "运行模式", false, false);
		map.AddTBInt(FrmAttachmentAttr.AthSaveWay, 0, "保存方式", false, false);

		map.AddTBString(FrmAttachmentAttr.Name, null, "名称", true, false, 0, 50, 20);
		map.AddTBString(FrmAttachmentAttr.Exts, null, "要求上传的格式", true, false, 0, 200, 20);
		map.AddTBInt(FrmAttachmentAttr.NumOfUpload, 0, "最小上传数量", true, false);
		map.AddTBInt(FrmAttachmentAttr.TopNumOfUpload, 99, "最大上传数量", true, false);
		map.AddTBInt(FrmAttachmentAttr.FileMaxSize, 10240, "附件最大限制(KB)", true, false);
		map.AddTBInt(FrmAttachmentAttr.UploadFileNumCheck, 0, "上传校验方式", true, false);

		// map.AddDDLSysEnum(FrmAttachmentAttr.UploadFileNumCheck, 0, "上传校验方式",
		// true, true, FrmAttachmentAttr.UploadFileNumCheck,
		// "@0=不用校验@1=不能为空@2=每个类别下不能为空");

		map.AddTBString(FrmAttachmentAttr.SaveTo, null, "保存到", true, false, 0, 150, 20);
		map.AddTBString(FrmAttachmentAttr.Sort, null, "类别(可为空)", true, false, 0, 500, 20);

		map.AddTBFloat(FrmAttachmentAttr.X, 5, "X", true, false);
		map.AddTBFloat(FrmAttachmentAttr.Y, 5, "Y", false, false);
		map.AddTBFloat(FrmAttachmentAttr.W, 40, "TBWidth", false, false);
		map.AddTBFloat(FrmAttachmentAttr.H, 150, "H", false, false);

		map.AddBoolean(FrmAttachmentAttr.IsUpload, true, "是否可以上传", false, false);
		map.AddBoolean(FrmAttachmentAttr.IsVisable, true, "是否可见", false, false);
		// map.AddTBInt(FrmAttachmentAttr.IsDelete, 1,
		// "附件删除规则(0=不能删除1=删除所有2=只能删除自己上传的)", false, false);
		map.AddTBInt(FrmAttachmentAttr.FileType, 0, "附件类型", false, false);

		// hzm新增列
		map.AddTBInt(FrmAttachmentAttr.DeleteWay, 0, "附件删除规则(0=不能删除1=删除所有2=只能删除自己上传的", false, false);
		map.AddBoolean(FrmAttachmentAttr.IsDownload, true, "是否可以下载", false, false);
		map.AddBoolean(FrmAttachmentAttr.IsOrder, false, "是否可以排序", false, false);

		map.AddBoolean(FrmAttachmentAttr.IsAutoSize, true, "自动控制大小", false, false);
		map.AddBoolean(FrmAttachmentAttr.IsNote, true, "是否增加备注", false, false);
		map.AddBoolean(FrmAttachmentAttr.IsExpCol, false, "是否启用扩展列", false, false);

		map.AddBoolean(FrmAttachmentAttr.IsShowTitle, true, "是否显示标题列", false, false);
		map.AddTBInt(FrmAttachmentAttr.UploadType, 0, "上传类型0单个1多个2指定", false, false);

		/// #region 流程属性.
		// 对于父子流程有效.
		map.AddTBInt(FrmAttachmentAttr.CtrlWay, 0, "控制呈现控制方式0=PK,1=FID,2=ParentID", false, false);
		map.AddTBInt(FrmAttachmentAttr.AthUploadWay, 0, "控制上传控制方式0=继承模式,1=协作模式.", false, false);
		map.AddTBInt(FrmAttachmentAttr.ReadRole, 0, "阅读规则", true, true);

		// 数据引用，如果为空就引用当前的.
		map.AddTBString(FrmAttachmentAttr.DataRefNoOfObj, null, "数据引用组件ID", true, false, 0, 150, 20, true, null);

		/// #endregion 流程属性.

		/// #region WebOffice控制方式
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableWF, true, "是否启用weboffice", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableSave, true, "是否启用保存", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableReadonly, true, "是否只读", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableRevise, true, "是否启用修订", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableViewKeepMark, true, "是否查看用户留痕", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnablePrint, true, "是否打印", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableOver, true, "是否启用套红", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableSeal, true, "是否启用签章", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableTemplete, false, "是否启用模板文件", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableCheck, true, "是否记录节点信息", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableInsertFlow, true, "是否启用插入流程", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableInsertFengXian, true, "是否启用插入风险点", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableMarks, true, "是否进入留痕模式", true, true);
		map.AddBoolean(FrmAttachmentAttr.IsWoEnableDown, true, "是否启用下载", true, true);

		// 参数属性.
		map.AddTBAtParas(3000);

		map.AddTBInt(FrmAttachmentAttr.GroupID, 0, "GroupID", false, false);
		map.AddTBString(FrmAttachmentAttr.GUID, null, "GUID", true, false, 0, 128, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}

	/// #endregion

	public boolean IsUse = false;

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		if (this.getFK_Node() == 0) {
			// 适应设计器新的规则 by dgq
			if (!DataType.IsNullOrEmpty(this.getNoOfObj()) && this.getNoOfObj().contains(this.getFK_MapData())) {
				this.setMyPK(this.getNoOfObj());
			} else {
				this.setMyPK(this.getFK_MapData() + "_" + this.getNoOfObj());
			}
		} else {
			this.setMyPK(this.getFK_MapData() + "_" + this.getNoOfObj() + "_" + this.getFK_Node());
		}

		return super.beforeUpdateInsertAction();
	}

	@Override
	protected boolean beforeInsert() throws Exception {
		this.setIsWoEnableWF(true);
		this.setIsWoEnableSave(false);
		this.setIsWoEnableReadonly(false);
		this.setIsWoEnableRevise(false);
		this.setIsWoEnableViewKeepMark(false);
		this.setIsWoEnablePrint(false);
		this.setIsWoEnableOver(false);
		this.setIsWoEnableSeal(false);
		this.setIsWoEnableTemplete(false);

		if (this.getFK_Node() == 0) {
			this.setMyPK(this.getFK_MapData() + "_" + this.getNoOfObj());
		} else {
			this.setMyPK(this.getFK_MapData() + "_" + this.getNoOfObj() + "_" + this.getFK_Node());
		}

		// 对于流程类的多附件，默认按照WorkID控制. add 2017.08.03 by zhoupeng.
		if (this.getFK_Node() != 0) {
			this.setHisCtrlWay(AthCtrlWay.WorkID);
		}

		return super.beforeInsert();
	}

	/**
	 * 插入之后
	 * 
	 * @throws Exception
	 */
	@Override
	protected void afterInsert() throws Exception {
		GroupField gf = new GroupField();
		if (gf.IsExit(GroupFieldAttr.CtrlID, this.getMyPK()) == false) {
			gf.setFrmID(this.getFK_MapData());
			gf.setCtrlID(this.getMyPK());
			gf.setCtrlType("Ath");
			gf.setLab(this.getName());
			gf.setIdx(0);
			gf.Insert(); // 插入.
		}
		super.afterInsert();
	}

	/**
	 * 删除之后.
	 * 
	 * @throws Exception
	 */
	@Override
	protected void afterDelete() throws Exception {
		GroupField gf = new GroupField();
		gf.Delete(GroupFieldAttr.CtrlID, this.getMyPK());

		super.afterDelete();
	}
}
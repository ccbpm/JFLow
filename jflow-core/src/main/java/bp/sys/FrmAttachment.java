package bp.sys;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.en.Map;
import bp.en.*;
import bp.*;
import java.util.*;

/** 
 附件
*/
public class FrmAttachment extends EntityMyPK
{

		///参数属性.
	/** 
	 是否可见？
	*/
	public final boolean getIsVisable()
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsVisable, true);
	}
	public final void setIsVisable(boolean value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.IsVisable, value);
	}
	public final int getDeleteWay() throws Exception
	{
		return this.GetValIntByKey(FrmAttachmentAttr.DeleteWay, 0);
	}
	public final void setDeleteWay(int value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.DeleteWay, value);
	}
	/** 
	 使用上传附件的 - 控件类型
	 0=批量.
	 1=单个。
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
	 最低上传数量
	*/
	public final int getNumOfUpload() throws Exception
	{
		return this.GetValIntByKey(FrmAttachmentAttr.NumOfUpload);
	}
	public final void setNumOfUpload(int value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.NumOfUpload, value);
	}
	/** 
	 最大上传数量
	*/
	public final int getTopNumOfUpload() throws Exception
	{
		return this.GetValIntByKey(FrmAttachmentAttr.TopNumOfUpload);
	}
	public final void setTopNumOfUpload(int value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.TopNumOfUpload, value);
	}
	/** 
	 附件最大限制
	*/
	public final int getFileMaxSize() throws Exception
	{
		return this.GetValIntByKey(FrmAttachmentAttr.FileMaxSize);
	}
	public final void setFileMaxSize(int value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.FileMaxSize, value);
	}
	/** 
	 上传校验
	 0=不校验.
	 1=不能为空.
	 2=每个类别下不能为空.
	*/
	public final UploadFileNumCheck getUploadFileNumCheck() throws Exception
	{
		return UploadFileNumCheck.forValue(this.GetValIntByKey(FrmAttachmentAttr.UploadFileNumCheck));
	}
	public final void setUploadFileNumCheck(UploadFileNumCheck value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.UploadFileNumCheck, value.getValue());
	}
	/** 
	 保存方式
	 0 =文件方式保存。
	 1 = 保存到数据库.
	 2 = ftp服务器.
	*/
	public final AthSaveWay getAthSaveWay() throws Exception
	{
		return AthSaveWay.forValue(this.GetValIntByKey(FrmAttachmentAttr.AthSaveWay));
	}

		/// 参数属性.


		///属性
	/** 
	 节点编号
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
	 运行模式？
	*/
	public final AthRunModel getAthRunModel() throws Exception
	{
		return AthRunModel.forValue(this.GetValIntByKey(FrmAttachmentAttr.AthRunModel));
	}
	public final void setAthRunModel(AthRunModel value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.AthRunModel, value.getValue());
	}
	/** 
	 上传类型（单个的，多个，指定的）
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
	 是否启用扩张列
	*/
	public final boolean getIsExpCol() throws Exception
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsExpCol);
	}
	public final void setIsExpCol(boolean value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.IsExpCol, value);
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
	*/
	public final String getExts() throws Exception
	{
		return this.GetValStringByKey(FrmAttachmentAttr.Exts);
	}
	public final void setExts(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.Exts, value);
	}
	/** 
	 保存到
	 * @throws Exception 
	*/
	public final String getSaveTo() throws Exception
	{
		if (this.getAthSaveWay() == AthSaveWay.IISServer || this.getAthSaveWay() == AthSaveWay.DB)
		{
	
			return SystemConfig.getPathOfDataUser() + "/UploadFile/" + this.getFK_MapData() + "/";
				// return s;
		}

		if (this.getAthSaveWay() == AthSaveWay.FTPServer)
		{
				return "//" + this.getFK_MapData() + "//";
		}

		return this.getFK_MapData();
	}
	/** 
	 数据关联组件ID
	*/
	public final String getDataRefNoOfObj() throws Exception
	{
		String str = this.GetValStringByKey(FrmAttachmentAttr.DataRefNoOfObj);
		if (str.equals(""))
		{
			str = this.getNoOfObj();
		}
		return str;
	}
	public final void setDataRefNoOfObj(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.DataRefNoOfObj, value);
	}
	/** 
	 附件编号
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
	public final int getGroupID() throws Exception
	{
		return this.GetValIntByKey(FrmAttachmentAttr.GroupID);
	}
	public final void setGroupID(long value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.GroupID, value);
	}
	/** 
	 阅读规则:@0=不控制@1=未阅读阻止发送@2=未阅读做记录
	*/
	public final int getReadRole() throws Exception
	{
		return this.GetValIntByKey(FrmAttachmentAttr.ReadRole);
	}
	public final void setReadRole(int value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.ReadRole, value);
	}


	public final int getRowIdx() throws Exception
	{
		return this.GetValIntByKey(FrmAttachmentAttr.RowIdx);
	}
	public final void setRowIdx(int value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.RowIdx, value);
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


		///


		///快捷键
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

		/// 快捷键


		///构造方法
	/** 
	 附件
	*/
	public FrmAttachment()
	{
	}
	/** 
	 附件
	 
	 @param mypk
	*/
	public FrmAttachment(String mypk) throws Exception
	{
		this.setMyPK(mypk);
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_FrmAttachment", "附件");
		map.IndexField = FrmAttachmentAttr.FK_MapData;
		map.AddMyPK();


		map.AddTBString(FrmAttachmentAttr.FK_MapData, null, "表单ID", true, false, 1, 100, 20);
		map.AddTBString(FrmAttachmentAttr.NoOfObj, null, "附件编号", true, false, 0, 50, 20);
		map.AddTBInt(FrmAttachmentAttr.FK_Node, 0, "节点控制(对sln有效)", false, false);

			//for渔业厅增加.
		map.AddTBInt(FrmAttachmentAttr.AthRunModel, 0, "运行模式", false, false);
		map.AddTBInt(FrmAttachmentAttr.AthSaveWay, 0, "保存方式", false, false);

		map.AddTBString(FrmAttachmentAttr.Name, null, "名称", true, false, 0, 50, 20);
		map.AddTBString(FrmAttachmentAttr.Exts, null, "要求上传的格式", true, false, 0, 200, 20);
		map.AddTBInt(FrmAttachmentAttr.NumOfUpload, 0, "最小上传数量", true, false);
		map.AddTBInt(FrmAttachmentAttr.TopNumOfUpload, 99, "最大上传数量", true, false);
		map.AddTBInt(FrmAttachmentAttr.FileMaxSize, 10240, "附件最大限制(KB)", true, false);
		map.AddTBInt(FrmAttachmentAttr.UploadFileNumCheck, 0, "上传校验方式", true, false);

			//map.AddDDLSysEnum(FrmAttachmentAttr.UploadFileNumCheck, 0, "上传校验方式", true, true, FrmAttachmentAttr.UploadFileNumCheck,
			//  "@0=不用校验@1=不能为空@2=每个类别下不能为空");

			// map.AddTBString(FrmAttachmentAttr.SaveTo, null, "保存到", true, false, 0, 150, 20);
		map.AddTBString(FrmAttachmentAttr.Sort, null, "类别(可为空)", true, false, 0, 500, 20);

		map.AddTBFloat(FrmAttachmentAttr.X, 5, "X", true, false);
		map.AddTBFloat(FrmAttachmentAttr.Y, 5, "Y", false, false);
		map.AddTBFloat(FrmAttachmentAttr.W, 40, "TBWidth", false, false);
		map.AddTBFloat(FrmAttachmentAttr.H, 150, "H", false, false);

		map.AddBoolean(FrmAttachmentAttr.IsUpload, true, "是否可以上传", false, false);
		map.AddBoolean(FrmAttachmentAttr.IsVisable, true, "是否可见", false, false);
			//  map.AddTBInt(FrmAttachmentAttr.IsDelete, 1, "附件删除规则(0=不能删除1=删除所有2=只能删除自己上传的)", false, false);
		map.AddTBInt(FrmAttachmentAttr.FileType, 0, "附件类型", false, false);
		map.AddTBInt(FrmAttachmentAttr.ReadRole, 0, "阅读规则", true, true);
		map.AddTBInt(FrmAttachmentAttr.PicUploadType, 0, "图片附件上传方式", true, true);

			//hzm新增列
		map.AddTBInt(FrmAttachmentAttr.DeleteWay, 0, "附件删除规则(0=不能删除1=删除所有2=只能删除自己上传的", false, false);
		map.AddBoolean(FrmAttachmentAttr.IsDownload, true, "是否可以下载", false, false);
		map.AddBoolean(FrmAttachmentAttr.IsOrder, false, "是否可以排序", false, false);


		map.AddBoolean(FrmAttachmentAttr.IsAutoSize, true, "自动控制大小", false, false);
		map.AddBoolean(FrmAttachmentAttr.IsNote, true, "是否增加备注", false, false);
		map.AddBoolean(FrmAttachmentAttr.IsExpCol, false, "是否启用扩展列", false, false);

		map.AddBoolean(FrmAttachmentAttr.IsShowTitle, true, "是否显示标题列", false, false);
		map.AddTBInt(FrmAttachmentAttr.UploadType, 0, "上传类型0单个1多个2指定", false, false);

			///流程属性.
			//对于父子流程有效.
		map.AddTBInt(FrmAttachmentAttr.CtrlWay, 0, "控制呈现控制方式0=PK,1=FID,2=ParentID", false, false);
		map.AddTBInt(FrmAttachmentAttr.AthUploadWay, 0, "控制上传控制方式0=继承模式,1=协作模式.", false, false);
		map.AddTBInt(FrmAttachmentAttr.ReadRole, 0, "阅读规则", true, true);

			//数据引用，如果为空就引用当前的.
		map.AddTBString(FrmAttachmentAttr.DataRefNoOfObj, null, "数据引用组件ID", true, false, 0, 150, 20, true, null);

			/// 流程属性.


			//参数属性.
		map.AddTBAtParas(3000);

			//  map.AddTBInt(FrmAttachmentAttr.RowIdx, 0, "RowIdx", false, false);
		map.AddTBInt(FrmAttachmentAttr.GroupID, 0, "GroupID", false, false);
		map.AddTBString(FrmAttachmentAttr.GUID, null, "GUID", true, false, 0, 128, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///

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
		else
		{
			this.setMyPK(this.getFK_MapData() + "_" + this.getNoOfObj() + "_" + this.getFK_Node());
		}

		return super.beforeUpdateInsertAction();
	}
	@Override
	protected boolean beforeInsert() throws Exception
	{
		//在属性实体集合插入前，clear父实体的缓存.
		bp.sys.Glo.ClearMapDataAutoNum(this.getFK_MapData());

		if (this.getFK_Node() == 0)
		{
			this.setMyPK(this.getFK_MapData() + "_" + this.getNoOfObj());
		}
		else
		{
			this.setMyPK(this.getFK_MapData() + "_" + this.getNoOfObj() + "_" + this.getFK_Node());
		}

		//对于流程类的多附件，默认按照WorkID控制. add 2017.08.03  by zhoupeng.
		if (this.getFK_Node() != 0 && this.getHisCtrlWay() == AthCtrlWay.PK)
		{
			this.setHisCtrlWay(AthCtrlWay.WorkID);
		}

		return super.beforeInsert();
	}
	/** 
	 插入之后
	*/
	@Override
	protected void afterInsert() throws Exception
	{

		GroupField gf = new GroupField();
		if (this.getFK_Node() == 0 && gf.IsExit(GroupFieldAttr.CtrlID, this.getMyPK()) == false)
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

	/** 
	 删除之后.
	*/
	@Override
	protected void afterDelete() throws Exception
	{
		GroupField gf = new GroupField();
		gf.Delete(GroupFieldAttr.CtrlID, this.getMyPK());

		super.afterDelete();
	}
}
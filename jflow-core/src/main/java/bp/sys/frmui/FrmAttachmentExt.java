package bp.sys.frmui;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.sys.*;
import bp.tools.FtpUtil;
import bp.web.WebUser;
import bp.*;
import bp.sys.*;
import java.util.*;

/** 
 附件
*/
public class FrmAttachmentExt extends EntityMyPK
{
	/** 
	 访问权限.
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


		///参数属性.
	/** 
	 是否可见？
	*/
	public final boolean getIsVisable()throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsVisable, true);
	}
	public final void setIsVisable(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsVisable, value);
	}
	/** 
	 附件类型
	*/
	public final int getFileType()throws Exception
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
	*/
	public final int getUploadCtrl()throws Exception
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
	*/
	public final UploadFileNumCheck getUploadFileNumCheck()throws Exception
	{
		return UploadFileNumCheck.forValue(this.GetValIntByKey(FrmAttachmentAttr.UploadFileNumCheck));
	}
	public final void setUploadFileNumCheck(UploadFileNumCheck value)throws Exception
	{
		this.SetPara(FrmAttachmentAttr.UploadFileNumCheck, value.getValue());
	}


		/// 参数属性.


		///属性
	/** 
	 节点编号
	*/
	public final int getFK_Node()throws Exception
	{
		return this.GetValIntByKey(FrmAttachmentAttr.FK_Node);
	}
	public final void setFK_Node(int value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.FK_Node, value);
	}
	/** 
	 上传类型（单个的，多个，指定的）
	*/
	public final AttachmentUploadType getUploadType()throws Exception
	{
		return AttachmentUploadType.forValue(this.GetValIntByKey(FrmAttachmentAttr.UploadType));
	}
	public final void setUploadType(AttachmentUploadType value)throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.UploadType, value.getValue());
	}
	/** 
	 类型名称
	*/
	public final String getUploadTypeT()throws Exception
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
	public final boolean getIsUpload()throws Exception
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
	public final boolean getIsDownload()throws Exception
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
	public final AthDeleteWay getHisDeleteWay()throws Exception
	{
		return AthDeleteWay.forValue(this.GetValIntByKey(FrmAttachmentAttr.DeleteWay));
	}
	public final void setHisDeleteWay(AthDeleteWay value)throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.DeleteWay, value.getValue());
	}

	/** 
	 是否可以排序?
	*/
	public final boolean getIsOrder()throws Exception
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
	public final boolean getIsAutoSize()throws Exception
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
	public final boolean getIsShowTitle()throws Exception
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
	public final boolean getIsNote()throws Exception
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsNote);
	}
	public final void setIsNote(boolean value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.IsNote, value);
	}
	/** 
	 附件名称
	*/
	public final String getName()throws Exception
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
	public final String getSort()throws Exception
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
	public final String getExts()throws Exception
	{
		return this.GetValStringByKey(FrmAttachmentAttr.Exts);
	}
	public final void setExts(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.Exts, value);
	}

	/** 
	 附件标识
	*/
	public final String getNoOfObj()throws Exception
	{
		return this.GetValStringByKey(FrmAttachmentAttr.NoOfObj);
	}

	/** 
	 Y
	*/
	public final float getY()throws Exception
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
	public final float getX()throws Exception
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
	public final float getW()throws Exception
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
	public final float getH()throws Exception
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
	public final AthCtrlWay getHisCtrlWay()throws Exception
	{
		return AthCtrlWay.forValue(this.GetValIntByKey(FrmAttachmentAttr.CtrlWay));
	}
	public final void setHisCtrlWay(AthCtrlWay value)throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.CtrlWay, value.getValue());
	}
	/** 
	 是否是合流汇总多附件？
	*/
	public final boolean getIsHeLiuHuiZong()throws Exception
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsHeLiuHuiZong);
	}
	public final void setIsHeLiuHuiZong(boolean value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.IsHeLiuHuiZong, value);
	}
	/** 
	 该附件是否汇总到合流节点上去？
	*/
	public final boolean getIsToHeLiuHZ()throws Exception
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsToHeLiuHZ);
	}
	public final void setIsToHeLiuHZ(boolean value) throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.IsToHeLiuHZ, value);
	}
	/** 
	 文件展现方式
	*/
	public final FileShowWay getFileShowWay()throws Exception
	{
		return FileShowWay.forValue(this.GetParaInt(FrmAttachmentAttr.FileShowWay));
	}
	public final void setFileShowWay(FileShowWay value)throws Exception
	{
		this.SetPara(FrmAttachmentAttr.FileShowWay, value.getValue());
	}
	/** 
	 上传方式（对于父子流程有效）
	*/
	public final AthUploadWay getAthUploadWay()throws Exception
	{
		return AthUploadWay.forValue(this.GetValIntByKey(FrmAttachmentAttr.AthUploadWay));
	}
	public final void setAthUploadWay(AthUploadWay value)throws Exception
	{
		this.SetValByKey(FrmAttachmentAttr.AthUploadWay, value.getValue());
	}
	/** 
	 FK_MapData
	*/
	public final String getFK_MapData()throws Exception
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
	public final boolean getIsTurn2Html()throws Exception
	{
		return this.GetValBooleanByKey(FrmAttachmentAttr.IsTurn2Html);
	}

		///


		///快捷键
	/** 
	 是否启用快捷键
	*/
	public final boolean getFastKeyIsEnable()throws Exception
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
	public final String getFastKeyGenerRole()throws Exception
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
	public FrmAttachmentExt()
	{
	}
	/** 
	 附件
	 
	 @param mypk 主键
	*/
	public FrmAttachmentExt(String mypk)throws Exception
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
		map.IndexField = MapAttrAttr.FK_MapData;

		map.AddMyPK();


			///基本属性。
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

		map.AddDDLSysEnum(FrmAttachmentAttr.PicUploadType, 0, "图片附件上传方式", true, true, FrmAttachmentAttr.PicUploadType, "@0=拍照上传或者相册上传@1=只能拍照上传");
		map.SetHelperAlert(FrmAttachmentAttr.PicUploadType, "该功能只使用于移动端图片文件上传的方式.");


			/// 基本属性。


			///权限控制。
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

		map.AddDDLSysEnum(FrmAttachmentAttr.CtrlWay, 0, "控制呈现控制方式", true, true, "Ath" + FrmAttachmentAttr.CtrlWay, "@0=PK-主键@1=FID-干流程ID@2=PWorkID-父流程ID@3=仅能查看自己上传的附件@4=WorkID-按照WorkID计算(对流程节点表单有效)@5=P2WorkID@6=P3WorkID");


			//map.AddDDLSysEnum(FrmAttachmentAttr.DataRef, 0, "数据引用", true, true, FrmAttachmentAttr.DataRef,
			//    "@0=当前组件ID@1=指定的组件ID");

			/// 权限控制。


			///节点相关
			//map.AddDDLSysEnum(FrmAttachmentAttr.DtlOpenType, 0, "附件删除规则", true, true, FrmAttachmentAttr.DeleteWay, 
			//    "@0=不能删除@1=删除所有@2=只能删除自己上传的");
		map.AddBoolean(FrmAttachmentAttr.IsToHeLiuHZ, true, "该附件是否要汇总到合流节点上去？(对子线程节点有效)", true, true, true);
		map.AddBoolean(FrmAttachmentAttr.IsHeLiuHuiZong, true, "是否是合流节点的汇总附件组件？(对合流节点有效)", true, true, true);
		map.AddTBString(FrmAttachmentAttr.DataRefNoOfObj, "AttachM1", "对应附件标识", true, false, 0, 150, 20);
		map.SetHelperAlert("DataRefNoOfObj", "对WorkID权限模式有效,用于查询贯穿整个流程的附件标识,与从表的标识一样.");

		map.AddDDLSysEnum(FrmAttachmentAttr.ReadRole, 0, "阅读规则", true, true, FrmAttachmentAttr.ReadRole, "@0=不控制@1=未阅读阻止发送@2=未阅读做记录");

			/// 节点相关


			///其他属性。
			//参数属性.
		map.AddTBAtParas(3000);

			/// 其他属性。

		RefMethod rm = new RefMethod();
		rm.Title = "高级配置";
			//  rm.Icon = "/WF/Admin/CCFormDesigner/Img/Menu/CC.png";
			//rm.ClassMethodName = this.ToString() + ".DoAdv";
			// rm.refMethodType = RefMethodType.RightFrameOpen;
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

		///

	public final String DtlOfAth()throws Exception
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
			ftpUtil = bp.wf.Glo.getFtpUtil();
		} catch (Exception e) {

			e.printStackTrace();
			return "err@" + e.getMessage();
		}
		return ftpUtil.openConnection();

	}
	/** 
	 固定模式类别设置
	 
	 @return 
	*/
	public final String DoSettingSort()throws Exception
	{
		return "../../Admin/FoolFormDesigner/AttachmentSortSetting.htm?FK_MapData=" + this.getFK_MapData() + "&MyPK=" + this.getMyPK() + "&Ath=" + this.getNoOfObj();
	}
	/** 
	 执行高级设置.
	 
	 @return 
	*/
	public final String DoAdv()throws Exception
	{
		return "/WF/Admin/FoolFormDesigner/Attachment.aspx?FK_MapData=" + this.getFK_MapData() + "&MyPK=" + this.getMyPK() + "&Ath=" + this.getNoOfObj();
	}

	public boolean IsUse = false;
	@Override
	protected boolean beforeUpdateInsertAction()throws Exception
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

		if (this.getHisCtrlWay() != AthCtrlWay.WorkID)
		{
			this.setAthUploadWay(AthUploadWay.Interwork);
		}



			///处理分组.
			//更新相关的分组信息.
		if (this.getIsVisable() == true && this.getFK_Node() == 0)
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

			/// 处理分组.


		return super.beforeUpdateInsertAction();
	}
	@Override
	protected boolean beforeInsert()throws Exception
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

		return super.beforeInsert();
	}
	/** 
	 插入之后
	*/
	@Override
	protected void afterInsert()throws Exception
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

	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		FrmAttachment ath = new FrmAttachment();
		ath.setMyPK(this.getMyPK());
		ath.RetrieveFromDBSources();
		ath.setIsToHeLiuHZ(this.getIsToHeLiuHZ());
		ath.setIsHeLiuHuiZong(this.getIsHeLiuHuiZong());
		ath.Update();


		//判断是否是字段附件
		MapAttr mapAttr = new MapAttr();
		mapAttr.setMyPK(this.getMyPK());
		if (mapAttr.RetrieveFromDBSources() != 0 && mapAttr.getName().equals(this.getName()) == false)
		{
			mapAttr.setName(this.getName());
			mapAttr.Update();
		}

		//调用frmEditAction, 完成其他的操作.
		bp.sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterInsertUpdateAction();
	}

	/** 
	 删除之后.
	*/
	@Override
	protected void afterDelete()throws Exception
	{
		GroupFields gfs = new GroupFields();
		gfs.RetrieveByLike(GroupFieldAttr.CtrlID, this.getMyPK() + "%");
		gfs.Delete();
		//gf.Delete(GroupFieldAttr.CtrlID, this.MyPK);

		//把相关的字段也要删除.
		MapAttrString attr = new MapAttrString();
		attr.setMyPK(this.getMyPK());
		if (attr.RetrieveFromDBSources() != 0)
		{
			attr.Delete();
		}


		//调用frmEditAction, 完成其他的操作.
		bp.sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterDelete();
	}
}
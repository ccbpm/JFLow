package bp.sys;

import bp.da.*;
import bp.difference.*;
import bp.en.*;
import bp.*;
import bp.tools.FtpUtil;

import java.io.*;

/** 
 附件数据存储
*/
public class FrmAttachmentDB extends EntityMyPK
{

		///#region 属性
	/** 
	 类别
	*/
	public final String getSort()
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.Sort);
	}
	public final void setSort(String value)
	 {
		this.SetValByKey(FrmAttachmentDBAttr.Sort, value);
	}
	/** 
	 记录日期
	*/
	public final String getRDT()  {
		String str = this.GetValStringByKey(FrmAttachmentDBAttr.RDT);
		return str.substring(5, 16);
	}
	public final void setRDT(String value)
	 {
		this.SetValByKey(FrmAttachmentDBAttr.RDT, value);
	}
	/** 
	 文件
	*/
	public final String getFileFullName()
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.FileFullName);
	}
	public final void setFileFullName(String value)
	{String str = value;
		str = str.replace("~", "-");
		str = str.replace("'", "-");
		str = str.replace("*", "-");

			//str = str.Replace("/", "\\");
			//str = str.Replace("/", "\\");
			//str = str.Replace("/", "\\");
			//str = str.Replace("/", "\\");

		this.SetValByKey(FrmAttachmentDBAttr.FileFullName, str);
	}
	/** 
	 上传GUID
	*/
	public final String getUploadGUID()
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.UploadGUID);
	}
	public final void setUploadGUID(String value)
	 {
		this.SetValByKey(FrmAttachmentDBAttr.UploadGUID, value);
	}
	/** 
	 附件路径
	*/
	public final String getFilePathName()  {

		return this.getFileFullName().substring(this.getFileFullName().lastIndexOf('/') + 1);
	}
	/** 
	 附件名称
	*/
	public final String getFileName()
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.FileName);
	}
	public final void setFileName(String value)
	{String str = value;
		str = str.replace("~", "-");
		str = str.replace("'", "-");
		str = str.replace("*", "-");

		this.SetValByKey(FrmAttachmentDBAttr.FileName, str);

		String fileExt = str.substring(str.lastIndexOf('.') + 1);

			//后缀名.
		this.SetValByKey(FrmAttachmentDBAttr.FileExts, fileExt);
	}
	/** 
	 附件扩展名
	*/
	public final String getFileExts()
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.FileExts);
	}
	public final void setFileExts(String value)
	 {
		this.SetValByKey(FrmAttachmentDBAttr.FileExts, value.replace(".", ""));
	}
	/** 
	 相关附件
	*/
	public final String getFK_FrmAttachment()
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.FK_FrmAttachment);
	}
	public final void setFK_FrmAttachment(String value)
	 {
		this.SetValByKey(FrmAttachmentDBAttr.FK_FrmAttachment, value);


		if (DataType.IsNullOrEmpty(this.getFK_MapData()) == true)
		{
			throw new RuntimeException("err@错误:请首先给FK_MapData赋值..");
		}

			//获取最后"_"的位置
		String val = value.replace(this.getFK_MapData() + "_", "");
		this.SetValByKey(FrmAttachmentDBAttr.NoOfObj, val);
	}
	/** 
	 主键值
	*/
	public final String getRefPKVal()
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.RefPKVal);
	}
	public final void setRefPKVal(String value)
	 {
		this.SetValByKey(FrmAttachmentDBAttr.RefPKVal, value);
	}
	/** 
	 工作ID.
	*/
	public final long getFID()
	{
		return this.GetValInt64ByKey(FrmAttachmentDBAttr.FID);
	}
	public final void setFID(long value)
	 {
		this.SetValByKey(FrmAttachmentDBAttr.FID, value);
	}
	/** 
	 MyNote
	*/
	public final String getMyNote()
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.MyNote);
	}
	public final void setMyNote(String value)
	 {
		this.SetValByKey(FrmAttachmentDBAttr.MyNote, value);
	}
	/** 
	 记录人
	*/
	public final String getRec()
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.Rec);
	}
	public final void setRec(String value)
	 {
		this.SetValByKey(FrmAttachmentDBAttr.Rec, value);
	}
	/** 
	 记录人名称
	*/
	public final String getRecName()
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.RecName);
	}
	public final void setRecName(String value)
	 {
		this.SetValByKey(FrmAttachmentDBAttr.RecName, value);
	}



	/** 
	 所在部门
	*/
	public final String getFK_Dept()
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	 {
		this.SetValByKey(FrmAttachmentDBAttr.FK_Dept, value);
	}
	/** 
	 所在部门名称
	*/
	public final String getFK_DeptName()
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.FK_DeptName);
	}
	public final void setFK_DeptName(String value)
	 {
		this.SetValByKey(FrmAttachmentDBAttr.FK_DeptName, value);
	}


	/** 
	 附件编号
	*/
	public final String getFK_MapData()
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.FK_MapData);
	}
	public final void setFK_MapData(String value)
	 {
		this.SetValByKey(FrmAttachmentDBAttr.FK_MapData, value);
	}
	/**
	 文件大小
	*/
	public final float getFileSize()
	{
		return this.GetValFloatByKey(FrmAttachmentDBAttr.FileSize);
	}
	public final void setFileSize(float value)
	 {
		this.SetValByKey(FrmAttachmentDBAttr.FileSize, value / 1024);
	}
	/** 
	 是否锁定行?
	*/
	public final boolean isRowLock()
	{
		return this.GetValBooleanByKey(FrmAttachmentDBAttr.IsRowLock);
	}
	public final void setRowLock(boolean value)
	 {
		this.SetValByKey(FrmAttachmentDBAttr.IsRowLock, value);
	}
	/** 
	 显示顺序
	*/
	public final int getIdx()
	{
		return this.GetValIntByKey(FrmAttachmentDBAttr.Idx);
	}
	public final void setIdx(int value)
	 {
		this.SetValByKey(FrmAttachmentDBAttr.Idx, value);
	}
	/** 
	 附件扩展名
	*/
	public final int getNodeID()
	{
		return this.GetValIntByKey(FrmAttachmentDBAttr.NodeID);
	}
	public final void setNodeID(int value)
	 {
		this.SetValByKey(FrmAttachmentDBAttr.NodeID, value);
	}
	/** 
	 附件类型
	*/
	public final AttachmentUploadType getHisAttachmentUploadType()  {
		if (this.getMyPK().contains("_") && this.getMyPK().length() < 32)
		{
			return AttachmentUploadType.Single;
		}
		else
		{
			return AttachmentUploadType.Multi;
		}
	}

		///#endregion


		///#region 构造方法
	/** 
	 附件数据存储
	*/
	public FrmAttachmentDB()  {
	}
	/** 
	 附件数据存储
	 
	 param mypk
	*/
	public FrmAttachmentDB(String mypk)throws Exception
	{
		this.setMyPK(mypk);
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_FrmAttachmentDB", "附件数据存储");

		map.IndexField = FrmAttachmentDBAttr.RefPKVal;

		map.AddMyPK();

		map.AddTBString(FrmAttachmentDBAttr.FK_MapData, null, "FK_MapData", true, false, 1, 100, 20);
		map.AddTBString(FrmAttachmentDBAttr.FK_FrmAttachment, null, "附件主键", true, false, 1, 500, 20);
		map.AddTBString(FrmAttachmentDBAttr.NoOfObj, null, "附件标识", true, false, 0, 50, 20);

		map.AddTBString(FrmAttachmentDBAttr.RefPKVal, null, "实体主键", true, false, 0, 50, 20);
		map.AddTBInt(FrmAttachmentDBAttr.FID, 0, "FID", true, false);
		map.AddTBInt(FrmAttachmentDBAttr.NodeID, 0, "节点ID", true, false);


		map.AddTBString(FrmAttachmentDBAttr.Sort, null, "类别", true, false, 0, 200, 20);
		map.AddTBString(FrmAttachmentDBAttr.FileFullName, null, "文件路径", true, false, 0, 700, 20);
		map.AddTBString(FrmAttachmentDBAttr.FileName, null, "名称", true, false, 0, 500, 20);
		map.AddTBString(FrmAttachmentDBAttr.FileExts, null, "扩展", true, false, 0, 50, 20);
		map.AddTBFloat(FrmAttachmentDBAttr.FileSize, 0, "文件大小", true, false);

		map.AddTBDateTime(FrmAttachmentDBAttr.RDT, null, "记录日期", true, false);
		map.AddTBString(FrmAttachmentDBAttr.Rec, null, "记录人", true, false, 0, 50, 20);
		map.AddTBString(FrmAttachmentDBAttr.RecName, null, "记录人名字", true, false, 0, 50, 20);
		map.AddTBString(FrmAttachmentDBAttr.FK_Dept, null, "所在部门", true, false, 0, 50, 20);
		map.AddTBString(FrmAttachmentDBAttr.FK_DeptName, null, "所在部门名称", true, false, 0, 50, 20);
		map.AddTBStringDoc(FrmAttachmentDBAttr.MyNote, null, "备注", true, false);

		map.AddTBInt(FrmAttachmentDBAttr.IsRowLock, 0, "是否锁定行", true, false);

			//顺序.
		map.AddTBInt(FrmAttachmentDBAttr.Idx, 0, "排序", true, false);

			//这个值在上传时候产生.
		map.AddTBString(FrmAttachmentDBAttr.UploadGUID, null, "上传GUID", true, false, 0, 500, 20);

		map.AddTBAtParas(3000); //增加参数属性.

		this.set_enMap(map);
		return this.get_enMap();
	}

	/** 
	 生成文件.
	 
	 @return 
	*/
	private String MakeFullFileFromFtp()  {
		String pathOfTemp = SystemConfig.getPathOfTemp();
		if ((new File(pathOfTemp)).isDirectory() == false)
		{
			(new File(pathOfTemp)).mkdirs();
		}

		String tempFile = pathOfTemp + DBAccess.GenerGUID() + "." + this.getFileExts();

		//  string tempFile =  bp.difference.SystemConfig.getPathOfTemp() + + this.FileName;

		if ((new File(tempFile)).isFile() == true)
		{
			(new File(tempFile)).delete();
		}

		/*FtpConnection conn = new FtpConnection(SystemConfig.getFTPServerIP(), SystemConfig.getFTPServerPort(), SystemConfig.getFTPUserNo(), SystemConfig.getFTPUserPassword());

		conn.GetFile(this.getFileFullName(), tempFile, false, FileAttributes.Archive);*/

		return tempFile;
	}
	public final String DoDown() throws Exception {

		return "执行成功.";
	}

	/** 
	 重写
	 
	 @return 
	*/
	@Override
	protected boolean beforeInsert() throws Exception {
		return super.beforeInsert();
	}

	@Override
	protected void afterDelete() throws Exception {
		//判断删除excel数据提取的数据
		if (DataType.IsNullOrEmpty(this.getFK_FrmAttachment()))
		{
			return;
		}

		FrmAttachment ath = new FrmAttachment(this.getFK_FrmAttachment());
		try
		{
			// @于庆海需要翻译.
			if (ath.getAthSaveWay() == bp.sys.AthSaveWay.IISServer)
			{
				(new File(this.getFileFullName())).delete();
			}

			if (ath.getAthSaveWay() == bp.sys.AthSaveWay.FTPServer)
			{
				FtpUtil ftpUtil = bp.wf.Glo.getFtpUtil();
				String fullName = this.getFileFullName();
				ftpUtil.deleteFile(fullName);
			}
		}
		catch (RuntimeException ex)
		{
			Log.DebugWriteError(ex.getMessage());
		}

		super.afterDelete();
	}



		///#endregion

	/** 
	 获得临时文件
	 
	 @return 
	*/

	public final String GenerTempFile() throws Exception {
		return GenerTempFile(bp.sys.AthSaveWay.IISServer);
	}

//ORIGINAL LINE: public string GenerTempFile(AthSaveWay saveWay = BP.Sys.AthSaveWay.IISServer)
	public final String GenerTempFile(AthSaveWay saveWay)
	{
		if (saveWay == bp.sys.AthSaveWay.IISServer)
		{
			return this.getFileFullName();
		}

		if (saveWay == bp.sys.AthSaveWay.FTPServer)
		{
			return this.MakeFullFileFromFtp();
		}

		if (saveWay == bp.sys.AthSaveWay.DB)
		{
			throw new RuntimeException("@尚未处理存储到db里面的文件.");
		}

		throw new RuntimeException("@尚未处理存储到db里面的文件.");
	}

	/** 
	 向下移動
	*/
	public final void DoDownTabIdx() throws Exception {
		this.DoOrderDown(FrmAttachmentDBAttr.RefPKVal, this.getRefPKVal(), FrmAttachmentDBAttr.FK_FrmAttachment, this.getFK_FrmAttachment(), FrmAttachmentDBAttr.Idx);
	}
	/** 
	 向上移動
	*/
	public final void DoUpTabIdx() throws Exception {
		this.DoOrderUp(FrmAttachmentDBAttr.RefPKVal, this.getRefPKVal(), FrmAttachmentDBAttr.FK_FrmAttachment, this.getFK_FrmAttachment(), FrmAttachmentDBAttr.Idx);

		//  this.DoOrderUp(FrmAttachmentDBAttr.FK_MapData, this.FK_MapData, FrmAttachmentDBAttr.Idx);
	}
}
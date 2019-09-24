package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;

import java.util.*;
import java.io.*;

/** 
 附件数据存储
*/
public class FrmAttachmentDB extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 类别
	 * @throws Exception 
	*/
	public final String getSort() throws Exception
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.Sort);
	}
	public final void setSort(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentDBAttr.Sort, value);
	}
	/** 
	 记录日期
	 * @throws Exception 
	*/
	public final String getRDT() throws Exception
	{
		String str = this.GetValStringByKey(FrmAttachmentDBAttr.RDT);
		return str.substring(5, 16);
	}
	public final void setRDT(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentDBAttr.RDT, value);
	}
	/** 
	 文件
	 * @throws Exception 
	*/
	public final String getFileFullName() throws Exception
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.FileFullName);
	}
	public final void setFileFullName(String value) throws Exception
	{
		String str = value;
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
	 * @throws Exception 
	*/
	public final String getUploadGUID() throws Exception
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.UploadGUID);
	}
	public final void setUploadGUID(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentDBAttr.UploadGUID, value);
	}
	/** 
	 附件路径
	 * @throws Exception 
	*/
	public final String getFilePathName() throws Exception
	{

		return this.getFileFullName().substring(this.getFileFullName().lastIndexOf('\\') + 1);
	}
	/** 
	 附件名称
	 * @throws Exception 
	*/
	public final String getFileName() throws Exception
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.FileName);
	}
	public final void setFileName(String value) throws Exception
	{
		String str = value;
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
	 * @throws Exception 
	*/
	public final String getFileExts() throws Exception
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.FileExts);
	}
	public final void setFileExts(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentDBAttr.FileExts, value.replace(".", ""));
	}
	/** 
	 相关附件
	 * @throws Exception 
	*/
	public final String getFK_FrmAttachment() throws Exception
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.FK_FrmAttachment);
	}
	public final void setFK_FrmAttachment(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentDBAttr.FK_FrmAttachment, value);

			//获取最后"_"的位置
		int idx = value.lastIndexOf('_');
		String val = value.substring(idx + 1);
		this.SetValByKey(FrmAttachmentDBAttr.NoOfObj, val);
	}
	/** 
	 主键值
	 * @throws Exception 
	*/
	public final String getRefPKVal() throws Exception
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.RefPKVal);
	}
	public final void setRefPKVal(String v) throws Exception
	{
		this.SetValByKey(FrmAttachmentDBAttr.RefPKVal, v);
	}
	/** 
	 工作ID.
	 * @throws Exception 
	*/
	public final long getFID() throws Exception
	{
		return this.GetValInt64ByKey(FrmAttachmentDBAttr.FID);
	}
	public final void setFID(long value) throws Exception
	{
		this.SetValByKey(FrmAttachmentDBAttr.FID, value);
	}
	/** 
	 MyNote
	 * @throws Exception 
	*/
	public final String getMyNote() throws Exception
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.MyNote);
	}
	public final void setMyNote(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentDBAttr.MyNote, value);
	}
	/** 
	 记录人
	 * @throws Exception 
	*/
	public final String getRec() throws Exception
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.Rec);
	}
	public final void setRec(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentDBAttr.Rec, value);
	}
	/** 
	 记录人名称
	 * @throws Exception 
	*/
	public final String getRecName() throws Exception
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.RecName);
	}
	public final void setRecName(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentDBAttr.RecName, value);
	}
	/** 
	 附件编号
	 * @throws Exception 
	*/
	public final String getFK_MapData() throws Exception
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.FK_MapData);
	}
	public final void setFK_MapData(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentDBAttr.FK_MapData, value);
	}
	/** 
	 文件大小
	 * @throws Exception 
	*/
	public final float getFileSize() throws Exception
	{
		return this.GetValFloatByKey(FrmAttachmentDBAttr.FileSize);
	}
	public final void setFileSize(float value) throws Exception
	{
		this.SetValByKey(FrmAttachmentDBAttr.FileSize, value / 1024);
	}
	/** 
	 是否锁定行?
	 * @throws Exception 
	*/
	public final boolean getIsRowLock() throws Exception
	{
		return this.GetValBooleanByKey(FrmAttachmentDBAttr.IsRowLock);
	}
	public final void setIsRowLock(boolean value) throws Exception
	{
		this.SetValByKey(FrmAttachmentDBAttr.IsRowLock, value);
	}
	/** 
	 显示顺序
	 * @throws Exception 
	*/
	public final int getIdx() throws Exception
	{
		return this.GetValIntByKey(FrmAttachmentDBAttr.Idx);
	}
	public final void setIdx(int value) throws Exception
	{
		this.SetValByKey(FrmAttachmentDBAttr.Idx, value);
	}
	/** 
	 附件扩展名
	 * @throws Exception 
	*/
	public final String getNodeID() throws Exception
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.NodeID);
	}
	public final void setNodeID(String value) throws Exception
	{
		this.SetValByKey(FrmAttachmentDBAttr.NodeID, value);
	}
	/** 
	 附件类型
	 * @throws Exception 
	*/
	public final AttachmentUploadType getHisAttachmentUploadType() throws Exception
	{
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
	public FrmAttachmentDB()
	{
	}
	/** 
	 附件数据存储
	 
	 @param mypk
	 * @throws Exception 
	*/
	public FrmAttachmentDB(String mypk) throws Exception
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
		Map map = new Map("Sys_FrmAttachmentDB", "附件数据存储");

		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.IndexField = FrmAttachmentDBAttr.RefPKVal;

		map.AddMyPK();

		map.AddTBString(FrmAttachmentDBAttr.FK_MapData, null, "FK_MapData", true, false, 1, 100, 20);
		map.AddTBString(FrmAttachmentDBAttr.FK_FrmAttachment, null, "附件主键", true, false, 1, 500, 20);
		map.AddTBString(FrmAttachmentDBAttr.NoOfObj, null, "附件标识", true, false, 0, 50, 20);

		map.AddTBString(FrmAttachmentDBAttr.RefPKVal, null, "实体主键", true, false, 0, 50, 20);
		map.AddTBInt(FrmAttachmentDBAttr.FID, 0, "FID", true, false);
		map.AddTBString(FrmAttachmentDBAttr.NodeID, null, "节点ID", true, false, 0, 50, 20);

		map.AddTBString(FrmAttachmentDBAttr.Sort, null, "类别", true, false, 0, 200, 20);
		map.AddTBString(FrmAttachmentDBAttr.FileFullName, null, "文件路径", true, false, 0, 700, 20);
		map.AddTBString(FrmAttachmentDBAttr.FileName, null, "名称", true, false, 0, 500, 20);
		map.AddTBString(FrmAttachmentDBAttr.FileExts, null, "扩展", true, false, 0, 50, 20);
		map.AddTBFloat(FrmAttachmentDBAttr.FileSize, 0, "文件大小", true, false);

		map.AddTBDateTime(FrmAttachmentDBAttr.RDT, null, "记录日期", true, false);
		map.AddTBString(FrmAttachmentDBAttr.Rec, null, "记录人", true, false, 0, 50, 20);
		map.AddTBString(FrmAttachmentDBAttr.RecName, null, "记录人名字", true, false, 0, 50, 20);
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
	private String MakeFullFileFromFtp()
	{
		String pathOfTemp = SystemConfig.getPathOfTemp();
		if ((new File(pathOfTemp)).isDirectory() == false)
		{
			(new File(pathOfTemp)).mkdirs();
		}

		String tempFile = SystemConfig.getPathOfTemp() + this.getFileName();

	  //  string tempFile = SystemConfig.PathOfTemp + + this.FileName;
		try
		{
			if ((new File(tempFile)).isFile() == true)
			{
				(new File(tempFile)).delete();
			}
		}
		catch (java.lang.Exception e)
		{
			//  tempFile = SystemConfig.PathOfTemp + System.Guid.NewGuid() + this.FileName;
		}

		FtpSupport.FtpConnection conn = new FtpSupport.FtpConnection(SystemConfig.getFTPServerIP(), SystemConfig.getFTPUserNo(), SystemConfig.getFTPUserPassword());

		conn.GetFile(this.getFileFullName(), tempFile, false, System.IO.FileAttributes.Archive);

		return tempFile;
	}
	/** 
	 重写
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeInsert() throws Exception
	{
		return super.beforeInsert();
	}

	@Override
	protected void afterDelete()
	{
		//判断删除excel数据提取的数据
		if (DataType.IsNullOrEmpty(this.getFK_FrmAttachment()))
		{
			return;
		}

		//是一个流程先判断流程是否结束，如果结束了，就不让删除.
	 //   string nodeID = this.FK_MapData.Replace("ND", "");
	  //  if (DataType.IsNumStr(nodeID) = true)
	   // {
		//}



		FrmAttachment ath = new FrmAttachment(this.getFK_FrmAttachment());
		try
		{
			// @于庆海需要翻译.
			if (ath.getAthSaveWay() == Sys.AthSaveWay.IISServer)
			{
				(new File(this.getFileFullName())).delete();
			}

			if (ath.getAthSaveWay() == Sys.AthSaveWay.FTPServer)
			{
				FtpSupport.FtpConnection ftpconn = new FtpSupport.FtpConnection(SystemConfig.getFTPServerIP(), SystemConfig.getFTPUserNo(), SystemConfig.getFTPUserPassword());

				String fullName = this.getFileFullName();
				ftpconn.DeleteFile(fullName);
			}
		}
		catch (RuntimeException ex)
		{
			Log.DebugWriteError(ex.getMessage());
		}



		//没有看明白这是什么意思.
		String fkefs = ath.GetParaString("FK_ExcelFile", null);
		if (DataType.IsNullOrEmpty(fkefs) == false)
		{
			String[] efarr = fkefs.split(",".toCharArray(), StringSplitOptions.RemoveEmptyEntries);
			ExcelFile ef = null;
			ExcelTables ets = null;
			for (String fk_ef : efarr)
			{
				ef = new ExcelFile();
				ef.setNo(fk_ef);

				if (ef.RetrieveFromDBSources() > 0)
				{
					ets = new ExcelTables(fk_ef);
					for (ExcelTable et : ets.ToJavaList())
					{
						if (DBAccess.IsExitsObject(et.getNo()))
						{
							DBAccess.RunSQL(String.format("DELETE FROM %1$s WHERE FK_FrmAttachmentDB = '%2$s'", et.getNo(), this.getMyPK()));
						}
					}
				}
			}
		}


		 super.afterDelete();
	}


	/** 
	 获得临时文件
	 
	 @return 
	 * @throws Exception 
	*/

	public final String GenerTempFile() throws Exception
	{
		return GenerTempFile(AthSaveWay.WebServer);
	}


	public final String GenerTempFile(AthSaveWay saveWay) throws Exception
	{
		if (saveWay == AthSaveWay.WebServer)
		{
			return this.getFileFullName();
		}

		if (saveWay == AthSaveWay.FTPServer)
		{
			return this.MakeFullFileFromFtp();
		}

		if (saveWay == AthSaveWay.DB)
		{
			throw new RuntimeException("@尚未处理存储到db里面的文件.");
		}

		throw new RuntimeException("@尚未处理存储到db里面的文件.");
	}
}
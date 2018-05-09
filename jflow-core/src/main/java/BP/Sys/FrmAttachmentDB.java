package BP.Sys;

import java.io.File;
import org.apache.commons.lang.StringUtils;

import BP.DA.*;
import BP.En.*;
import BP.Tools.StringHelper;
import sun.net.ftp.FtpClient;

/** 
 附件数据存储
 
*/
public class FrmAttachmentDB extends EntityMyPK
{

		
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
	public final String getRDT()
	{
		String str = this.GetValStringByKey(FrmAttachmentDBAttr.RDT);
		return str;
		//return str.substring(5, 16);
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
	{
		String str = value;
		str = str.replace("~", "-");
		str = str.replace("'", "-");
		str = str.replace("*", "-");
//		str = str.replace("/","\\");
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
	public final String getFilePathName()
	{

		return this.getFileFullName().substring(this.getFileFullName().lastIndexOf('\\') + 1);
	}
	/** 
	 附件名称
	 
	*/
	public final String getFileName()
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.FileName);
	}
	public final void setFileName(String value)
	{
		String str = value;
		str = str.replace("~", "-");
		str = str.replace("'", "-");
		str = str.replace("*", "-");

		this.SetValByKey(FrmAttachmentDBAttr.FileName, str);
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
		this.SetValByKey(FrmAttachmentDBAttr.FileExts, value.replace(".",""));
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
		//给标记赋值.
        String[] val = value.split("_");
        this.SetValByKey(FrmAttachmentDBAttr.NoOfObj, val[1]);
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
		this.SetValByKey(FrmAttachmentDBAttr.FileSize, value/1024);
	}
	/** 
	 是否锁定行?
	 
	*/
	public final boolean getIsRowLock()
	{
		return this.GetValBooleanByKey(FrmAttachmentDBAttr.IsRowLock);
	}
	public final void setIsRowLock(boolean value)
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
	public final int getSaveWay()
	{
		return this.GetValIntByKey(FrmAttachmentDBAttr.SaveWay);
	}
	public final void setSaveWay(int value)
	{
		this.SetValByKey(FrmAttachmentDBAttr.SaveWay, value);
	}
	/** 
	 附件扩展名
	 
	*/
	public final String getNodeID()
	{
		return this.GetValStringByKey(FrmAttachmentDBAttr.NodeID);
	}
	public final void setNodeID(String value)
	{
		this.SetValByKey(FrmAttachmentDBAttr.NodeID, value);
	}
	/** 
	 附件类型
	 
	*/
	public final AttachmentUploadType getHisAttachmentUploadType()
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
		map.AddMyPK();
		map.AddTBString(FrmAttachmentDBAttr.FK_MapData, null, "FK_MapData", true, false, 1, 100, 20);
		map.AddTBString(FrmAttachmentDBAttr.FK_FrmAttachment, null, "附件编号", true, false, 1, 500, 20);
		map.AddTBString(FrmAttachmentDBAttr.NoOfObj, null, "附件标识", true, false, 0, 50, 20);
		
		map.AddTBString(FrmAttachmentDBAttr.RefPKVal, null, "实体主键", true, false, 0, 50, 20);
		map.AddTBInt(FrmAttachmentDBAttr.FID, 0, "FID", true, false);


		map.AddTBString(FrmAttachmentDBAttr.Sort, null, "类别", true, false, 0, 200, 20);
		map.AddTBString(FrmAttachmentDBAttr.FileFullName, null, "文件路径", true, false, 0, 700, 20);
		map.AddTBString(FrmAttachmentDBAttr.FileName, null, "名称", true, false, 0, 500, 20);
		map.AddTBString(FrmAttachmentDBAttr.FileExts, null, "扩展", true, false, 0, 50, 20);
		map.AddTBFloat(FrmAttachmentDBAttr.FileSize, 0, "文件大小", true, false);

		map.AddTBDateTime(FrmAttachmentDBAttr.RDT, null, "记录日期", true, false);
		map.AddTBString(FrmAttachmentDBAttr.Rec, null, "记录人", true, false, 0, 50, 20);
		map.AddTBString(FrmAttachmentDBAttr.RecName, null, "记录人名字", true, false, 0, 50, 20);
		map.AddTBStringDoc(FrmAttachmentDBAttr.MyNote, null, "备注", true, false);
		map.AddTBString(FrmAttachmentDBAttr.NodeID, null, "节点ID", true, false, 0, 50, 20);
		map.AddTBInt(FrmAttachmentDBAttr.IsRowLock, 0, "是否锁定行", true, false);
			//顺序.
		map.AddTBInt(FrmAttachmentDBAttr.Idx, 0, "排序", true, false);
			//这个值在上传时候产生.
		map.AddTBString(FrmAttachmentDBAttr.UploadGUID, null, "上传GUID", true, false, 0, 500, 20);
		map.AddTBAtParas(3000); //增加参数属性.
		 
		this.set_enMap(map);
		return this.get_enMap();
	}
	
	 /// <summary>
    /// 生成文件.
    /// </summary>
    /// <returns></returns>
    public String MakeFullFileFromFtp()
    {
        // string tempFile =  SystemConfig.PathOfTemp +System.Guid.NewGuid()+"."+this.FileExts;
        String tempFile = SystemConfig.getPathOfTemp() + this.getFileName();
        try
        {
        	File file = new File(tempFile);
            if (file.exists() == true)
                file.delete();
        }
        catch(Exception e)
        {
            //  tempFile = SystemConfig.PathOfTemp + System.Guid.NewGuid() + this.FileName;
        }
       

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

		///#endregion
	
	@Override
	protected void afterDelete() throws Exception
	{
		//判断删除excel数据提取的数据
		if (StringHelper.isNullOrWhiteSpace(this.getFK_FrmAttachment()))
		{
			return;
		}

		FrmAttachment ath = new FrmAttachment(this.getFK_FrmAttachment());

		try
		{
			// @于庆海需要翻译.
			if (ath.getAthSaveWay()==BP.Sys.AthSaveWay.WebServer)
			{
				new File(this.getFileFullName()).delete();
			}

			if (ath.getAthSaveWay() == BP.Sys.AthSaveWay.FTPServer)
			{
				//FtpSupport.FtpConnection ftpconn = new FtpSupport.FtpConnection(SystemConfig.getFTPServerIP(), SystemConfig.getFTPUserNo(), SystemConfig.getFTPUserPassword());

				String fullName = this.getFileFullName();
				//ny + "//" + athDesc.FK_MapData + "//" + guid + "." + dbUpload.FileExts;

			}
		}
		catch(RuntimeException ex)
		{
			Log.DebugWriteError(ex.getMessage());
		}


		String fkefs = ath.GetParaString("FK_ExcelFile", null);
		if (StringHelper.isNullOrWhiteSpace(fkefs) == false)
		{
			String[] efarr = StringUtils.split(fkefs, ",");
					//split((new String(",")).toCharArray(), StringSplitOptions.RemoveEmptyEntries);
			ExcelFile ef = null;
			ExcelTables ets = null;
			for (String fk_ef : efarr)
			{
				ef = new ExcelFile();
				ef.setNo(fk_ef);

				if (ef.RetrieveFromDBSources() > 0)
				{
					ets = new ExcelTables(fk_ef);
					for (ExcelTable et : ets.Tojavalist())
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
	public String GenerTempFile(AthSaveWay saveWay)
    {
        if (saveWay == BP.Sys.AthSaveWay.WebServer)
            return this.getFileFullName();

        if (saveWay == BP.Sys.AthSaveWay.FTPServer)
            return this.MakeFullFileFromFtp();

        if (saveWay == BP.Sys.AthSaveWay.DB)
			try {
				throw new Exception("@尚未处理存储到db里面的文件.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        return this.getFileFullName();
    }
}
package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.io.*;
import java.time.*;

/** 
 页面功能实体
*/
public class CCMobile_CCForm extends DirectoryPageBase
{
	/** 
	 构造函数
	*/
	public CCMobile_CCForm()
	{
	}
	public final String HandlerMapExt()
	{
		WF_CCForm en = new WF_CCForm();
		return en.HandlerMapExt();
	}

	public final String AttachmentUpload_Down()
	{
		WF_CCForm ccform = new WF_CCForm();
		return ccform.AttachmentUpload_Down();
	}
	/** 
	 表单初始化.
	 
	 @return 
	*/
	public final String Frm_Init()
	{
		WF_CCForm ccform = new WF_CCForm();
		return ccform.Frm_Init();
	}

	public final String Dtl_Init()
	{
		WF_CCForm ccform = new WF_CCForm();
		return ccform.Dtl_Init();
	}

	//保存从表数据
	public final String Dtl_SaveRow()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region  查询出来从表数据.
		GEDtls dtls = new GEDtls(this.getEnsName());
		GEDtl dtl = dtls.getGetNewEntity() instanceof GEDtl ? (GEDtl)dtls.getGetNewEntity() : null;
		dtls.Retrieve("RefPK", this.GetRequestVal("RefPKVal"));
		Map map = dtl.getEnMap();
		for (Entity item : dtls)
		{
			String pkval = item.GetValStringByKey(dtl.getPK());
			for (Attr attr : map.Attrs)
			{
				if (attr.getIsRefAttr() == true)
				{
					continue;
				}

				if (attr.getMyDataType() == DataType.AppDateTime || attr.getMyDataType() == DataType.AppDate)
				{
					if (attr.getUIIsReadonly() == true)
					{
						continue;
					}

					String val = this.GetValFromFrmByKey("TB_" + attr.getKey() + "_" + pkval, null);
					item.SetValByKey(attr.getKey(), val);
					continue;
				}


				if (attr.getUIContralType() == UIContralType.TB && attr.getUIIsReadonly() == false)
				{
					String val = this.GetValFromFrmByKey("TB_" + attr.getKey() + "_" + pkval, null);
					item.SetValByKey(attr.Key, val);
					continue;
				}

				if (attr.UIContralType == UIContralType.DDL && attr.UIIsReadonly == false)
				{
					String val = this.GetValFromFrmByKey("DDL_" + attr.Key + "_" + pkval);
					item.SetValByKey(attr.Key, val);
					continue;
				}

				if (attr.UIContralType == UIContralType.CheckBok && attr.UIIsReadonly == false)
				{
					String val = this.GetValFromFrmByKey("CB_" + attr.Key + "_" + pkval, "-1");
					if (val.equals("0"))
					{
						item.SetValByKey(attr.Key, 0);
					}
					else
					{
						item.SetValByKey(attr.Key, 1);
					}
					continue;
				}
			}
			item.SetValByKey("OID",pkval);
			item.Update(); //执行更新.
		}
		return "保存成功.";
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion  查询出来从表数据.

		///#region 保存新加行.

		//string keyVal = "";
		//foreach (Attr attr in map.Attrs)
		//{

		//    if (attr.MyDataType == DataType.AppDateTime || attr.MyDataType == DataType.AppDate)
		//    {
		//        if (attr.UIIsReadonly == true)
		//            continue;

		//        keyVal = this.GetValFromFrmByKey("TB_" + attr.Key + "_0", null);
		//        dtl.SetValByKey(attr.Key, keyVal);
		//        continue;
		//    }


		//    if (attr.UIContralType == UIContralType.TB && attr.UIIsReadonly == false)
		//    {
		//        keyVal = this.GetValFromFrmByKey("TB_" + attr.Key + "_0");
		//        if (attr.IsNum && keyVal == "")
		//            keyVal = "0";
		//        dtl.SetValByKey(attr.Key, keyVal);
		//        continue;
		//    }

		//    if (attr.UIContralType == UIContralType.DDL && attr.UIIsReadonly == true)
		//    {
		//        keyVal = this.GetValFromFrmByKey("DDL_" + attr.Key + "_0");
		//        dtl.SetValByKey(attr.Key, keyVal);
		//        continue;
		//    }

		//    if (attr.UIContralType == UIContralType.CheckBok && attr.UIIsReadonly == true)
		//    {
		//        keyVal = this.GetValFromFrmByKey("CB_" + attr.Key + "_0", "-1");
		//        if (keyVal == "-1")
		//            dtl.SetValByKey(attr.Key, 0);
		//        else
		//            dtl.SetValByKey(attr.Key, 1);
		//        continue;
		//    }
		//}

		//dtl.SetValByKey("RefPK", this.GetRequestVal("RefPKVal"));
		//dtl.PKVal = "0";
		//dtl.Insert();

		///#endregion 保存新加行.

		//return "保存成功.";
	}

	//多附件上传方法
	public final void MoreAttach()
	{
		String PKVal = this.GetRequestVal("PKVal");
		String attachPk = this.GetRequestVal("AttachPK");
		// 多附件描述.
		BP.Sys.FrmAttachment athDesc = new BP.Sys.FrmAttachment(attachPk);
		MapData mapData = new MapData(athDesc.FK_MapData);
		String msg = null;
		GEEntity en = new GEEntity(athDesc.FK_MapData);
		en.PKVal = PKVal;
		en.Retrieve();

//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var files = HttpContextHelper.RequestFiles();
		for (int i = 0; i < files.size(); i++)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			var file = files[i];

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 文件上传的iis服务器上 or db数据库里.
			if (athDesc.AthSaveWay == AthSaveWay.IISServer)
			{

				String savePath = athDesc.SaveTo;
				if (savePath.contains("@") == true || savePath.contains("*") == true)
				{
					/*如果有变量*/
					savePath = savePath.replace("*", "@");
					savePath = BP.WF.Glo.DealExp(savePath, en, null);

					if (savePath.contains("@") && this.getFK_Node() != 0)
					{
						/*如果包含 @ */
						BP.WF.Flow flow = new BP.WF.Flow(this.getFK_Flow());
						BP.WF.Data.GERpt myen = flow.getHisGERpt();
						myen.setOID(this.getWorkID());
						myen.RetrieveFromDBSources();
						savePath = BP.WF.Glo.DealExp(savePath, myen, null);
					}
					if (savePath.contains("@") == true)
					{
						throw new RuntimeException("@路径配置错误,变量没有被正确的替换下来." + savePath);
					}
				}
				else
				{
					savePath = athDesc.SaveTo + "\\" + PKVal;
				}

				//替换关键的字串.
				savePath = savePath.replace("\\\\", "\\");
				try
				{
					savePath = SystemConfig.PathOfWebApp + savePath;
				}
				catch (RuntimeException e)
				{
				}

				try
				{
					if ((new File(savePath)).isDirectory() == false)
					{
						(new File(savePath)).mkdirs();
					}
				}
				catch (RuntimeException ex)
				{
					throw new RuntimeException("@创建路径出现错误，可能是没有权限或者路径配置有问题:" + SystemConfig.PathOfWebApp + savePath + "===" + savePath + "@技术问题:" + ex.getMessage());
				}

				String exts = System.IO.Path.GetExtension(file.FileName).toLowerCase().replace(".", "");
				String guid = BP.DA.DBAccess.GenerGUID();

				String fileName = file.FileName.substring(0, file.FileName.lastIndexOf('.'));
				if (fileName.lastIndexOf("\\") > 0)
				{
					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
				}
				String ext = System.IO.Path.GetExtension(file.FileName);
				String realSaveTo = savePath + "\\" + guid + "." + fileName + ext;

				realSaveTo = realSaveTo.replace("~", "-");
				realSaveTo = realSaveTo.replace("'", "-");
				realSaveTo = realSaveTo.replace("*", "-");

				HttpContextHelper.UploadFile(file, realSaveTo);

				//执行附件上传前事件，added by liuxc,2017-7-15
				msg = mapData.DoEvent(FrmEventList.AthUploadeBefore, en, "@FK_FrmAttachment=" + athDesc.MyPK + "@FileFullName=" + realSaveTo);
				if (!DataType.IsNullOrEmpty(msg))
				{
					BP.Sys.Glo.WriteLineError("@AthUploadeBefore事件返回信息，文件：" + file.FileName + "，" + msg);

					try
					{
						(new File(realSaveTo)).delete();
					}
					catch (java.lang.Exception e2)
					{
					}
					//note:此处如何向前uploadify传递失败信息，有待研究
					//this.Alert("上传附件错误：" + msg, true);
					return;
				}

				File info = new File(realSaveTo);

				FrmAttachmentDB dbUpload = new FrmAttachmentDB();
				dbUpload.MyPK = guid; // athDesc.FK_MapData + oid.ToString();
				dbUpload.NodeID = String.valueOf(this.getFK_Node());
				dbUpload.FK_FrmAttachment = attachPk;
				dbUpload.FK_MapData = athDesc.FK_MapData;
				dbUpload.FK_FrmAttachment = attachPk;
				dbUpload.FileExts = info.Extension;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 处理文件路径，如果是保存到数据库，就存储pk.
				if (athDesc.AthSaveWay == AthSaveWay.IISServer)
				{
					//文件方式保存
					dbUpload.FileFullName = realSaveTo;
				}

				if (athDesc.AthSaveWay == AthSaveWay.FTPServer)
				{
					//保存到数据库
					dbUpload.FileFullName = dbUpload.MyPK;
				}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 处理文件路径，如果是保存到数据库，就存储pk.

				dbUpload.FileName = fileName + ext;
				dbUpload.FileSize = (float)info.length();
				dbUpload.RDT = DataType.CurrentDataTimess;
				dbUpload.Rec = BP.Web.WebUser.No;
				dbUpload.RecName = BP.Web.WebUser.Name;
				dbUpload.RefPKVal = PKVal;
				dbUpload.FID = this.getFID();

				//if (athDesc.IsNote)
				//    dbUpload.MyNote = this.Pub1.GetTextBoxByID("TB_Note").Text;

				//if (athDesc.Sort.Contains(","))
				//    dbUpload.Sort = this.Pub1.GetDDLByID("ddl").SelectedItemStringVal;

				dbUpload.UploadGUID = guid;
				dbUpload.Insert();

				if (athDesc.AthSaveWay == AthSaveWay.DB)
				{
					//执行文件保存.
					BP.DA.DBAccess.SaveFileToDB(realSaveTo, dbUpload.EnMap.PhysicsTable, "MyPK", dbUpload.MyPK, "FDB");
				}

				//执行附件上传后事件，added by liuxc,2017-7-15
				msg = mapData.DoEvent(FrmEventList.AthUploadeAfter, en, "@FK_FrmAttachment=" + dbUpload.FK_FrmAttachment + "@FK_FrmAttachmentDB=" + dbUpload.MyPK + "@FileFullName=" + dbUpload.FileFullName);
				if (!DataType.IsNullOrEmpty(msg))
				{
					BP.Sys.Glo.WriteLineError("@AthUploadeAfter事件返回信息，文件：" + dbUpload.FileName + "，" + msg);
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 文件上传的iis服务器上 or db数据库里.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 保存到数据库 / FTP服务器上.
			if (athDesc.AthSaveWay == AthSaveWay.DB || athDesc.AthSaveWay == AthSaveWay.FTPServer)
			{
				String guid = DBAccess.GenerGUID();

				//把文件临时保存到一个位置.
				String temp = SystemConfig.PathOfTemp + guid + ".tmp";
				try
				{
					HttpContextHelper.UploadFile(file, temp);
				}
				catch (RuntimeException ex)
				{
					(new File(temp)).delete();
					HttpContextHelper.UploadFile(file, temp);
				}

				//  fu.SaveAs(temp);

				//执行附件上传前事件，added by liuxc,2017-7-15
				msg = mapData.DoEvent(FrmEventList.AthUploadeBefore, en, "@FK_FrmAttachment=" + athDesc.MyPK + "@FileFullName=" + temp);
				if (DataType.IsNullOrEmpty(msg) == false)
				{
					BP.Sys.Glo.WriteLineError("@AthUploadeBefore事件返回信息，文件：" + file.FileName + "，" + msg);

					try
					{
						(new File(temp)).delete();
					}
					catch (java.lang.Exception e3)
					{
					}

					throw new RuntimeException("err@上传附件错误：" + msg);
				}

				File info = new File(temp);
				FrmAttachmentDB dbUpload = new FrmAttachmentDB();
				dbUpload.MyPK = BP.DA.DBAccess.GenerGUID();
				dbUpload.NodeID = String.valueOf(getFK_Node());
				dbUpload.FK_FrmAttachment = athDesc.MyPK;
				dbUpload.FID = this.getFID(); //流程id.
				if (athDesc.AthUploadWay == AthUploadWay.Inherit)
				{
					/*如果是继承，就让他保持本地的PK. */
					dbUpload.RefPKVal = PKVal.toString();
				}

				if (athDesc.AthUploadWay == AthUploadWay.Interwork)
				{
					/*如果是协同，就让他是PWorkID. */
					Paras ps = new Paras();
					ps.SQL = "SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + SystemConfig.AppCenterDBVarStr + "WorkID";
					ps.Add("WorkID", PKVal);
					String pWorkID = BP.DA.DBAccess.RunSQLReturnValInt(ps, 0).toString();
					if (pWorkID == null || pWorkID.equals("0"))
					{
						pWorkID = PKVal;
					}
					dbUpload.RefPKVal = pWorkID;
				}

				dbUpload.FK_MapData = athDesc.FK_MapData;
				dbUpload.FK_FrmAttachment = athDesc.MyPK;
				dbUpload.FileName = file.FileName;
				dbUpload.FileSize = (float)info.length();
				dbUpload.RDT = DataType.CurrentDataTimess;
				dbUpload.Rec = BP.Web.WebUser.No;
				dbUpload.RecName = BP.Web.WebUser.Name;
				//if (athDesc.IsNote)
				//    dbUpload.MyNote = this.Pub1.GetTextBoxByID("TB_Note").Text;

				//if (athDesc.Sort.Contains(","))
				//{
				//    string[] strs = athDesc.Sort.Contains("@") == true ? athDesc.Sort.Substring(athDesc.Sort.LastIndexOf("@") + 1).Split(',') : athDesc.Sort.Split(',');
				//    BP.Web.Controls.DDL ddl = this.Pub1.GetDDLByID("ddl");
				//    dbUpload.Sort = strs[0];
				//    if (ddl != null)
				//    {
				//        int selectedIndex = string.IsNullOrEmpty(ddl.SelectedItemStringVal) ? 0 : int.Parse(ddl.SelectedItemStringVal);
				//        dbUpload.Sort = strs[selectedIndex];
				//    }
				//}

				dbUpload.UploadGUID = guid;

				if (athDesc.AthSaveWay == AthSaveWay.DB)
				{
					dbUpload.Insert();
					//把文件保存到指定的字段里.
					dbUpload.SaveFileToDB("FileDB", temp);
				}

				if (athDesc.AthSaveWay == AthSaveWay.FTPServer)
				{
					/*保存到fpt服务器上.*/
					FtpSupport.FtpConnection ftpconn = new FtpSupport.FtpConnection(SystemConfig.FTPServerIP, SystemConfig.FTPUserNo, SystemConfig.FTPUserPassword);

					String ny = LocalDateTime.now().toString("yyyy_MM");

					//判断目录年月是否存在.
					if (ftpconn.DirectoryExist(ny) == false)
					{
						ftpconn.CreateDirectory(ny);
					}
					ftpconn.SetCurrentDirectory(ny);

					//判断目录是否存在.
					if (ftpconn.DirectoryExist(athDesc.FK_MapData) == false)
					{
						ftpconn.CreateDirectory(athDesc.FK_MapData);
					}

					//设置当前目录，为操作的目录。
					ftpconn.SetCurrentDirectory(athDesc.FK_MapData);

					//把文件放上去.
					ftpconn.PutFile(temp, guid + "." + dbUpload.FileExts);
					ftpconn.Close();

					//设置路径.
					dbUpload.FileFullName = ny + "//" + athDesc.FK_MapData + "//" + guid + "." + dbUpload.FileExts;
					dbUpload.Insert();
				}

				//执行附件上传后事件，added by liuxc,2017-7-15
				msg = mapData.DoEvent(FrmEventList.AthUploadeAfter, en, "@FK_FrmAttachment=" + dbUpload.FK_FrmAttachment + "@FK_FrmAttachmentDB=" + dbUpload.MyPK + "@FileFullName=" + temp);
				if (!DataType.IsNullOrEmpty(msg))
				{
					BP.Sys.Glo.WriteLineError("@AthUploadeAfter事件返回信息，文件：" + dbUpload.FileName + "，" + msg);
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 保存到数据库.
		}
	}
}
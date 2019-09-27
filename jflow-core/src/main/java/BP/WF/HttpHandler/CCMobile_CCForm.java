package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.Handler.WebContralBase;
import BP.Sys.*;
import BP.Tools.DateUtils;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.io.*;
import java.time.*;
import java.util.Date;

/** 
 页面功能实体
*/
public class CCMobile_CCForm extends WebContralBase
{
	/** 
	 构造函数
	*/
	public CCMobile_CCForm()
	{
	}
	public final String HandlerMapExt() throws Exception
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
	 * @throws Exception 
	*/
	public final String Frm_Init() throws Exception
	{
		WF_CCForm ccform = new WF_CCForm();
		return ccform.Frm_Init();
	}

	public final String Dtl_Init() throws Exception
	{
		WF_CCForm ccform = new WF_CCForm();
		return ccform.Dtl_Init();
	}

	//保存从表数据
	public final String Dtl_SaveRow() throws Exception
	{

			///#region  查询出来从表数据.
		GEDtls dtls = new GEDtls(this.getEnsName());
		GEDtl dtl = dtls.getNewEntity() instanceof GEDtl ? (GEDtl)dtls.getNewEntity() : null;
		dtls.Retrieve("RefPK", this.GetRequestVal("RefPKVal"));
		Map map = dtl.getEnMap();
		for (Entity item : dtls.ToJavaList())
		{
			String pkval = item.GetValStringByKey(dtl.getPK());
			for (Attr attr : map.getAttrs())
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
					item.SetValByKey(attr.getKey(), val);
					continue;
				}

				if (attr.getUIContralType() == UIContralType.DDL && attr.getUIIsReadonly() == false)
				{
					String val = this.GetValFromFrmByKey("DDL_" + attr.getKey() + "_" + pkval);
					item.SetValByKey(attr.getKey(), val);
					continue;
				}

				if (attr.getUIContralType() == UIContralType.CheckBok && attr.getUIIsReadonly() == false)
				{
					String val = this.GetValFromFrmByKey("CB_" + attr.getKey() + "_" + pkval, "-1");
					if (val.equals("0"))
					{
						item.SetValByKey(attr.getKey(), 0);
					}
					else
					{
						item.SetValByKey(attr.getKey(), 1);
					}
					continue;
				}
			}
			item.SetValByKey("OID",pkval);
			item.Update(); //执行更新.
		}
		return "保存成功.";

			///#endregion  查询出来从表数据.
	}

	//多附件上传方法
	public final void MoreAttach()
	{
		String PKVal = this.GetRequestVal("PKVal");
		String attachPk = this.GetRequestVal("AttachPK");
		// 多附件描述.
		BP.Sys.FrmAttachment athDesc = new BP.Sys.FrmAttachment(attachPk);
		MapData mapData = new MapData(athDesc.getFK_MapData());
		String msg = null;
		GEEntity en = new GEEntity(athDesc.getFK_MapData());
		en.setPKVal(PKVal);
		en.Retrieve();

//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var files = HttpContextHelper.RequestFiles();
		for (int i = 0; i < files.size(); i++)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			var file = files[i];


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
					savePath = athDesc.getSaveTo() + "\\" + PKVal;
				}

				//替换关键的字串.
				savePath = savePath.replace("\\\\", "\\");
				try
				{
					savePath = SystemConfig.getPathOfWebApp() + savePath;
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
				dbUpload.setMyPK(guid); // athDesc.getFK_MapData() + oid.ToString();
				dbUpload.setNodeID(String.valueOf(this.getFK_Node()));
				dbUpload.setFK_FrmAttachment(attachPk);
				dbUpload.setFK_MapData(athDesc.getFK_MapData());
				dbUpload.setFK_FrmAttachment(attachPk);
				dbUpload.setFileExts(info.Extension);


					///#region 处理文件路径，如果是保存到数据库，就存储pk.
				if (athDesc.getAthSaveWay() == AthSaveWay.IISServer)
				{
					//文件方式保存
					dbUpload.setFileFullName(realSaveTo);
				}

				if (athDesc.getAthSaveWay() == AthSaveWay.FTPServer)
				{
					//保存到数据库
					dbUpload.setFileFullName(dbUpload.getMyPK());
				}

					///#endregion 处理文件路径，如果是保存到数据库，就存储pk.

				dbUpload.setFileName(fileName + ext);
				dbUpload.setFileSize((float)info.length());
				dbUpload.setRDT(DataType.getCurrentDataTime());
				dbUpload.setRec(WebUser.getNo());
				dbUpload.setRecName(WebUser.getName());
				dbUpload.setRefPKVal(PKVal);
				dbUpload.setFID(this.getFID());

				//if (athDesc.IsNote)
				//    dbUpload.MyNote = this.Pub1.GetTextBoxByID("TB_Note").Text;

				//if (athDesc.Sort.Contains(","))
				//    dbUpload.Sort = this.Pub1.GetDDLByID("ddl").SelectedItemStringVal;

				dbUpload.setUploadGUID(guid);
				dbUpload.Insert();

				if (athDesc.getAthSaveWay() == AthSaveWay.DB)
				{
					//执行文件保存.
					BP.DA.DBAccess.SaveFileToDB(realSaveTo, dbUpload.getEnMap().getPhysicsTable(), "MyPK", dbUpload.getMyPK(), "FDB");
				}

				//执行附件上传后事件，added by liuxc,2017-7-15
				msg = mapData.DoEvent(FrmEventList.AthUploadeAfter, en, "@FK_FrmAttachment=" + dbUpload.getFK_FrmAttachment() + "@FK_FrmAttachmentDB=" + dbUpload.getMyPK() + "@FileFullName=" + dbUpload.getFileFullName());
				if (!DataType.IsNullOrEmpty(msg))
				{
					BP.Sys.Glo.WriteLineError("@AthUploadeAfter事件返回信息，文件：" + dbUpload.getFileName() + "，" + msg);
				}
			}

				///#endregion 文件上传的iis服务器上 or db数据库里.


				///#region 保存到数据库 / FTP服务器上.
			if (athDesc.getAthSaveWay() == AthSaveWay.DB || athDesc.getAthSaveWay() == AthSaveWay.FTPServer)
			{
				String guid = DBAccess.GenerGUID();

				//把文件临时保存到一个位置.
				String temp = SystemConfig.getPathOfTemp() + guid + ".tmp";
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
				dbUpload.setMyPK(BP.DA.DBAccess.GenerGUID());
				dbUpload.setNodeID(String.valueOf(getFK_Node()));
				dbUpload.setFK_FrmAttachment(athDesc.getMyPK());
				dbUpload.setFID(this.getFID()); //流程id.
				if (athDesc.getAthUploadWay() == AthUploadWay.Inherit)
				{
					/*如果是继承，就让他保持本地的PK. */
					dbUpload.setRefPKVal(PKVal.toString());
				}

				if (athDesc.getAthUploadWay() == AthUploadWay.Interwork)
				{
					/*如果是协同，就让他是PWorkID. */
					Paras ps = new Paras();
					ps.SQL = "SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID";
					ps.Add("WorkID", PKVal);
					String pWorkID = String.valueOf(DBAccess.RunSQLReturnValInt(ps, 0));
					if (pWorkID == null || pWorkID.equals("0"))
					{
						pWorkID = PKVal;
					}
					dbUpload.setRefPKVal(pWorkID);
				}

				dbUpload.setFK_MapData(athDesc.getFK_MapData());
				dbUpload.setFK_FrmAttachment(athDesc.getMyPK());
				dbUpload.setFileName(file.FileName);
				dbUpload.setFileSize((float)info.length());
				dbUpload.setRDT(DataType.getCurrentDataTime());
				dbUpload.setRec(WebUser.getNo());
				dbUpload.setRecName(WebUser.getName());
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

				dbUpload.setUploadGUID(guid);

				if (athDesc.getAthSaveWay() == AthSaveWay.DB)
				{
					dbUpload.Insert();
					//把文件保存到指定的字段里.
					dbUpload.SaveFileToDB("FileDB", temp);
				}

				if (athDesc.getAthSaveWay() == AthSaveWay.FTPServer)
				{
					/*保存到fpt服务器上.*/
					FtpSupport.FtpConnection ftpconn = new FtpSupport.FtpConnection(SystemConfig.FTPServerIP, SystemConfig.FTPUserNo, SystemConfig.FTPUserPassword);

					String ny = DateUtils.format(new Date(),"yyyy-MM");

					//判断目录年月是否存在.
					if (ftpconn.DirectoryExist(ny) == false)
					{
						ftpconn.CreateDirectory(ny);
					}
					ftpconn.SetCurrentDirectory(ny);

					//判断目录是否存在.
					if (ftpconn.DirectoryExist(athDesc.getFK_MapData()) == false)
					{
						ftpconn.CreateDirectory(athDesc.getFK_MapData());
					}

					//设置当前目录，为操作的目录。
					ftpconn.SetCurrentDirectory(athDesc.getFK_MapData());

					//把文件放上去.
					ftpconn.PutFile(temp, guid + "." + dbUpload.getFileExts());
					ftpconn.Close();

					//设置路径.
					dbUpload.setFileFullName(ny + "//" + athDesc.getFK_MapData() + "//" + guid + "." + dbUpload.getFileExts());
					dbUpload.Insert();
				}

				//执行附件上传后事件，added by liuxc,2017-7-15
				msg = mapData.DoEvent(FrmEventList.AthUploadeAfter, en, "@FK_FrmAttachment=" + dbUpload.getFK_FrmAttachment() + "@FK_FrmAttachmentDB=" + dbUpload.getMyPK() + "@FileFullName=" + temp);
				if (!DataType.IsNullOrEmpty(msg))
				{
					BP.Sys.Glo.WriteLineError("@AthUploadeAfter事件返回信息，文件：" + dbUpload.getFileName() + "，" + msg);
				}
			}

				///#endregion 保存到数据库.
		}
	}
}
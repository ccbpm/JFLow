package cn.jflow.controller.wf.ccform;

import java.io.File;
import java.net.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.Attr;
import BP.En.Entities;
import BP.En.Entity;
import BP.Sys.GEDtl;
import BP.Sys.GEDtls;
import BP.Sys.GENoNames;
import BP.Sys.MapDtl;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Tools.FileAccess;
import BP.Tools.StringHelper;
import BP.Web.WebUser;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/CCForm")
public class DtlOptController extends BaseController{
	

	public String getDDL_ImpWay()
	{
		return "";
	}
	@RequestMapping(value = "/DtlOptImport", method = RequestMethod.POST)
	private void btn_Click(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception
	{
		try
		{
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile fileT = (CommonsMultipartFile)multipartRequest.getFile("fup");
			String fileName = fileT.getOriginalFilename(); // 获取文件名
			
			String tempPath = this.getRequest().getSession().getServletContext().getRealPath("/") + "/Temp/";
			File uploadFile = new File(tempPath + WebUser.getNo()+"_"+fileName);
			if(fileName!=null&&!"".equals(fileName)){
				fileName.replace("\\\\", "\\");
				//System.out.println("上传路径"+tempPath + fileName);
				FileCopyUtils.copy(fileT.getBytes(), uploadFile);
			}

			MapDtl dtl = new MapDtl(this.getFK_MapDtl());

			//求出扩展名.
			String ext = FileAccess.getExtensionName(fileT.getOriginalFilename());

			//保存临时文件.
			//String filep = tempPath + WebUser.getNo() + ext;
			//fuit.SaveAs(file);

			GEDtls dtls = new GEDtls(this.getFK_MapDtl());
			DataTable dt = BP.DA.DBLoad.GetTableByExt(tempPath + WebUser.getNo()+"_"+fileName);

			String file1 = this.getRequest().getSession().getServletContext().getRealPath("/") + "/DataUser/DtlTemplete/" + this.getFK_MapDtl() + ext;
			File f = new File(file1);
			if (!f.exists())
			{
				if (ext.equals(".xlsx"))
				{
					file1 = this.getRequest().getSession().getServletContext().getRealPath("/") + "/DataUser/DtlTemplete/" + this.getFK_MapDtl() + ".xls";
				}
				else
				{
					file1 = this.getRequest().getSession().getServletContext().getRealPath("/") + "/DataUser/DtlTemplete/" + this.getFK_MapDtl() + ".xls";
				}
			}

			DataTable dtTemplete = BP.DA.DBLoad.GetTableByExt(file1);


			///#region 检查两个文件是否一致。
			for (DataColumn dc : dtTemplete.Columns)
			{
				boolean isHave = false;
				for (DataColumn mydc : dt.Columns)
				{
					if (dc.ColumnName.equals(mydc.ColumnName))
					{
						isHave = true;
						break;
					}
				}
				if (isHave == false)
				{
					throw new RuntimeException("@您导入的excel文件不符合系统要求的格式，请下载模版文件重新填入。");
				}
			}

			///#endregion 检查两个文件是否一致。


			///#region 生成要导入的属性.

			BP.En.Attrs attrs = dtls.getGetNewEntity().getEnMap().getAttrs();
			BP.En.Attrs attrsExp = new BP.En.Attrs();
			for (DataColumn dc : dtTemplete.Columns)
			{
				for (Attr attr : attrs)
				{
					if (!attr.getUIVisible())
					{
						continue;
					}

					if (attr.getIsRefAttr())
					{
						continue;
					}

					if (dc.ColumnName.trim().equals(attr.getDesc()))
					{
						attrsExp.Add(attr);
						break;
					}
				}
			}

			///#endregion 生成要导入的属性.


			///#region 执行导入数据.
			if (getDDL_ImpWay().equals("1"))
			{
				BP.DA.DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK='" + this.getWorkID() + "'");
			}

			int i = 0;
			long oid = BP.DA.DBAccess.GenerOID(this.getFK_MapDtl(), dt.Rows.size());
			String rdt = BP.DA.DataType.getCurrentData();

			String errMsg = "";
			for (DataRow dr : dt.Rows)
			{
				GEDtl dtlEn = (GEDtl)((dtls.getGetNewEntity() instanceof GEDtl) ? dtls.getGetNewEntity() : null);
				dtlEn.ResetDefaultVal();

				for (BP.En.Attr attr : attrsExp)
				{
					if (!attr.getUIVisible() || dr.getValue(attr.getDesc()) == null)
					{
						continue;
					}
					String val = dr.getValue(attr.getDesc()).toString();
					if (val == null)
					{
						continue;
					}
					val = val.trim();
					switch (attr.getMyFieldType())
					{
						case Enum:
						case PKEnum:
							SysEnums ses = new SysEnums(attr.getUIBindKey());
							boolean isHavel = false;
							for (SysEnum se : ses.ToJavaList())
							{
								if (val.equals(se.getLab()))
								{
									val = String.valueOf(se.getIntKey());
									isHavel = true;
									break;
								}
							}
							if (isHavel == false)
							{
								errMsg += "@数据格式不规范,第(" + i + ")行，列(" + attr.getDesc() + ")，数据(" + val + ")不符合格式,改值没有在枚举列表里.";
								val = attr.getDefaultVal().toString();
							}
							break;
						case FK:
						case PKFK:
							Entities ens = null;
							if (attr.getUIBindKey().contains("."))
							{
								ens = BP.En.ClassFactory.GetEns(attr.getUIBindKey());
							}
							else
							{
								ens = new GENoNames(attr.getUIBindKey(), "desc");
							}

							ens.RetrieveAll();
							boolean isHavelIt = false;
							for (Entity en : ens.ToJavaListEn())
							{
								if (val.equals(en.GetValStrByKey("Name")))
								{
									val = en.GetValStrByKey("No");
									isHavelIt = true;
									break;
								}
							}
							if (isHavelIt == false)
							{
								errMsg += "@数据格式不规范,第(" + i + ")行，列(" + attr.getDesc() + ")，数据(" + val + ")不符合格式,改值没有在外键数据列表里.";
							}
							break;
						default:
							break;
					}

					if (attr.getMyDataType() == BP.DA.DataType.AppBoolean)
					{
						if (val.trim().equals("是") || val.trim().toLowerCase().equals("yes"))
						{
							val = "1";
						}

						if (val.trim().equals("否") || val.trim().toLowerCase().equals("no"))
						{
							val = "0";
						}
					}

					dtlEn.SetValByKey(attr.getKey(), val);
				}
				dtlEn.setRefPKInt((int)this.getWorkID());
				dtlEn.SetValByKey("RDT", rdt);
				dtlEn.SetValByKey("Rec", WebUser.getNo());
				i++;

				dtlEn.InsertAsOID(oid);
				oid++;
			}

			///#endregion 执行导入数据.

			if (StringHelper.isNullOrEmpty(errMsg))
			{
				this.printAlert(response, "共有(" + i + ")条数据导入成功。");
			}
			else
			{
				this.printAlert(response, "共有(" + i + ")条数据导入成功，但是出现如下错误:" + errMsg);
			}
		}
		catch (RuntimeException ex)
		{
			//this.printAlert(response, msg);
			ex.printStackTrace();
		}
	}
	}
//	@RequestMapping(value = "/DtlDelUnPass", method = RequestMethod.POST)
//	private void btn_DelUnPass_Click(HttpServletRequest request,
//			HttpServletResponse response)
//	{
//		MapDtl dtl = new MapDtl(this.getFK_MapDtl());
//		Node nd = new Node(dtl.getFK_MapData());
//		MapData md = new MapData(dtl.getFK_MapData());
//
//		String starter = "SELECT Rec FROM " + md.getPTable() + " WHERE OID=" + this.getWorkID();
//		starter = BP.DA.DBAccess.RunSQLReturnString(starter);
//		GEDtls geDtls = new GEDtls(this.getFK_MapDtl());
//		geDtls.Retrieve(GEDtlAttr.Rec, starter, "IsPass", "0");
//		for (GEDtl item : geDtls.ToJavaList())
//		{
//			if (this.Pub1.GetCBByID("CB_" + item.OID).Checked == false)
//			{
//				continue;
//			}
//			item.Delete();
//		}
//		this.Response.Redirect(this.Request.RawUrl, true);
//	}
//	
//	@RequestMapping(value = "/DtlImp", method = RequestMethod.POST)
//	private void btn_Imp_Click(HttpServletRequest request,
//			HttpServletResponse response)
//	{
//		MapDtl dtl = new MapDtl(this.getFK_MapDtl());
//		Button btn = (Button)((sender instanceof Button) ? sender : null);
//		if (btn.ID.Contains("ImpClear"))
//		{
//			//如果是清空方式导入。
//			BP.DA.DBAccess.RunSQL("DELETE FROM " + dtl.PTable + " WHERE RefPK='" + this.getWorkID() + "'");
//		}
//
//		Node nd = new Node(dtl.getFK_MapData());
//		MapData md = new MapData(dtl.getFK_MapData());
//
//		String starter = "SELECT Rec FROM " + md.getPTable() + " WHERE OID=" + this.getWorkID();
//		starter = BP.DA.DBAccess.RunSQLReturnString(starter);
//		GEDtls geDtls = new GEDtls(this.getFK_MapDtl());
//		geDtls.Retrieve(GEDtlAttr.Rec, starter, "IsPass", "0");
//
//		String strs = "";
//		for (GEDtl item : geDtls.ToJavaList())
//		{
//			if (!this.Pub1.GetCBByID("CB_" + item.getOID()).Checked)
//			{
//				continue;
//			}
//			strs += ",'" + item.getOID() + "'";
//		}
//		if (strs.equals(""))
//		{
//			this.Alert("请选择要执行的数据。");
//			return;
//		}
//		strs = strs.substring(1);
//		BP.DA.DBAccess.RunSQL("UPDATE  " + dtl.getPTable() + " SET RefPK='" + this.getWorkID() + "',BatchID=0,Check_Note='',Check_RDT='" + BP.DA.DataType.getCurrentDataTime() + "', Checker='',IsPass=1  WHERE OID IN (" + strs + ")");
//		//this.WinClose();
//	}
//
//
//}

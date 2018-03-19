package cn.jflow.common.model;

import java.io.File;
import java.io.IOException;

import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.Sys.AthCtrlWay;
import BP.Sys.AthUploadWay;
import BP.Sys.AttachmentUploadType;
import BP.Sys.FileShowWay;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentAttr;
import BP.Sys.FrmAttachmentDB;
import BP.Sys.FrmAttachmentDBAttr;
import BP.Sys.FrmAttachmentDBs;
import BP.Sys.PubClass;
import BP.Sys.SystemConfig;
import BP.Tools.FileAccess;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Template.CCList;
import BP.WF.Template.CCListAttr;
import BP.Web.WebUser;
import cn.jflow.common.util.ContextHolderUtils;

public class AttachmentUploadModel {

	public StringBuilder Pub1 = new StringBuilder();
	
	/** 
	 ath.
	*/
	public final String getNoOfObj()
	{
		if(ContextHolderUtils.getRequest().getParameter("NoOfObj")==null)
			return "";
		return ContextHolderUtils.getRequest().getParameter("NoOfObj");
	}
	public final String getFK_Flow()
	{
		if(ContextHolderUtils.getRequest().getParameter("FK_Flow")==null)
			return "";
		return ContextHolderUtils.getRequest().getParameter("FK_Flow");
	}

	public final String getMyPK()
	{
		if(ContextHolderUtils.getRequest().getParameter("MyPK")==null)
			return "";
		return ContextHolderUtils.getRequest().getParameter("MyPK");
	}
	public final String getPKVal()
	{
		if(ContextHolderUtils.getRequest().getParameter("PKVal")==null)
			return "";
		return ContextHolderUtils.getRequest().getParameter("PKVal");
	}
	public final String getIsReadonly()
	{
		if(ContextHolderUtils.getRequest().getParameter("IsReadonly")==null)
			return "";
		return ContextHolderUtils.getRequest().getParameter("IsReadonly");
	}
	public final String getDelPKVal()
	{
		if(ContextHolderUtils.getRequest().getParameter("DelPKVal")==null)
			return "";
		return ContextHolderUtils.getRequest().getParameter("DelPKVal");
	}
	public final String getFK_FrmAttachment()
	{
		if(ContextHolderUtils.getRequest().getParameter("FK_FrmAttachment")==null)
			return "";
		return ContextHolderUtils.getRequest().getParameter("FK_FrmAttachment");
	}
	public final String getFK_FrmAttachmentExt()
	{
		return "ND" + this.getFK_Node() + "_DocMultiAth"; // ContextHolderUtils.getRequest().getParameter("FK_FrmAttachment"];
	}

	public int _fk_node = 0;
	public final int getFK_Node()
	{
		String val = ContextHolderUtils.getRequest().getParameter("FK_Node");
		if (_fk_node == 0 && !StringHelper.isNullOrEmpty(val))
		{
			return Integer.parseInt(val);
		}

		return _fk_node;
	}
	public final void setFK_Node(int value)
	{
		_fk_node = value;
	}
	public final String getDoType()
	{
		if(ContextHolderUtils.getRequest().getParameter("DoType")==null)
			return "";
		return ContextHolderUtils.getRequest().getParameter("DoType");
	}
	public final long getWorkID()
	{
		String str = ContextHolderUtils.getRequest().getParameter("WorkID");
		if (StringHelper.isNullOrEmpty(str))
		{
			str = ContextHolderUtils.getRequest().getParameter("OID");
		}

		if (StringHelper.isNullOrEmpty(str))
		{
			str = ContextHolderUtils.getRequest().getParameter("PKVal");
		}
		
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			return 0;
		}

	}
	public final String getFK_MapData()
	{
		String fk_mapdata = ContextHolderUtils.getRequest().getParameter("FK_MapData");
		if (StringHelper.isNullOrEmpty(fk_mapdata))
		{
			fk_mapdata = "ND" + getFK_Node();
		}
		return fk_mapdata;
	}
	public final String getAth()
	{
		if(ContextHolderUtils.getRequest().getParameter("Ath")==null)
			return "";
		return ContextHolderUtils.getRequest().getParameter("Ath");
	}
	public final String getIsCC()
	{
		String paras = ContextHolderUtils.getRequest().getParameter("Paras");
		if (!StringHelper.isNullOrEmpty(paras))
		{
			if (paras.contains("IsCC=1"))
			{
				return "1";
			}
		}
		return "ssss";
	}
	public  void init()
	{
		///#region 功能执行.
	if (this.getDoType().equals("Del"))
	{
		FrmAttachmentDB delDB = new FrmAttachmentDB();
		delDB.setMyPK(this.getDelPKVal()==null? this.getMyPK() :this.getDelPKVal());
		delDB.Retrieve();
		delDB.DirectDelete();
		File file = new File(delDB.getFileFullName());
		if (file.isFile() && file.exists()) {
			file.delete();
			System.out.println("删除单个文件" + delDB.getFileName() + "成功！");
		} else {
			System.out.println("删除单个文件" + delDB.getFileName() + "失败！");
		}
	}
	if (this.getDoType().equals("Down"))
	{
		FrmAttachmentDB downDB = new FrmAttachmentDB();

		if(this.getDelPKVal().equals("")){
			downDB.setMyPK(this.getDelPKVal()==null?this.getDelPKVal():this.getMyPK());
			downDB.Retrieve();
		}else{
			downDB.setMyPK(this.getDelPKVal()==null?this.getMyPK():this.getDelPKVal());
			downDB.Retrieve();
		}

		//String downpath = GetRealPath(downDB.getFileFullName());
		try {
			PubClass.DownloadFile(downDB.getFileFullName(), downDB.getFileName());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		//this.WinClose();
		return;
	}

	if (this.getDoType().equals("WinOpen"))
	{
		FrmAttachmentDB downDB = new FrmAttachmentDB();
		downDB.setMyPK(this.getMyPK());
		downDB.Retrieve();
		ContextHolderUtils.getResponse().setContentType("Application/pdf");
		String downpath = GetRealPath(downDB.getFileFullName());
		try {
			ContextHolderUtils.getResponse().sendRedirect(downpath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}//Writer(downpath);
		try {
			ContextHolderUtils.getResponse().getWriter().close();
			ContextHolderUtils.getResponse().getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

		/// 处理权限控制.
	FrmAttachment athDesc = new FrmAttachment();
	athDesc.setMyPK(this.getFK_FrmAttachment());
	if (this.getFK_Node() ==0 || this.getFK_Flow() == null)
	{
		athDesc.RetrieveFromDBSources();
	}
	else
	{
		//新增加（从ccflow） yqh 2016-7-11
		athDesc.setMyPK(this.getFK_FrmAttachment());
		int result = athDesc.RetrieveFromDBSources();

		///#region 判断是否是明细表的多附件.
		if (result == 0 && isNullOrEmpty(this.getFK_Flow()) == false && this.getFK_FrmAttachment().contains("AthMDtl"))
		{
			athDesc.setFK_MapData(this.getFK_MapData());
			athDesc.setNoOfObj("AthMDtl");
			athDesc.setName("我的从表附件");
			athDesc.setUploadType(AttachmentUploadType.Multi);
			athDesc.Insert();
		}
		
		/// 判断是否可以查询出来，如果查询不出来，就可能是公文流程。
		//athDesc.setMyPK(this.getFK_FrmAttachment());
		if (result == 0 && StringHelper.isNullOrEmpty(this.getFK_Flow()) == false && this.getFK_FrmAttachment().contains("DocMultiAth"))
		{
			//如果没有查询到它,就有可能是公文多附件被删除了.
			athDesc.setMyPK(this.getFK_FrmAttachment());
			athDesc.setNoOfObj("DocMultiAth");
			athDesc.setFK_MapData(this.getFK_MapData());
			athDesc.setExts("*.*");

			//存储路径.
			String path = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("/DataUser/UploadFile/");
			path += "/F" + this.getFK_Flow() + "MultiAth";
			athDesc.setSaveTo(path);
			athDesc.setIsNote(false); //不显示note字段.


			//位置.
			athDesc.setX((float)94.09);
			athDesc.setY ((float)333.18);
			athDesc.setW((float)626.36);
			athDesc.setH ((float)150);

			//多附件.
			athDesc.setUploadType( AttachmentUploadType.Multi);
			athDesc.setName("公文多附件(系统自动增加)");
			athDesc.SetValByKey("AtPara", "@IsWoEnablePageset=1@IsWoEnablePrint=1@IsWoEnableViewModel=1@IsWoEnableReadonly=0@IsWoEnableSave=1@IsWoEnableWF=1@IsWoEnableProperty=1@IsWoEnableRevise=1@IsWoEnableIntoKeepMarkModel=1@FastKeyIsEnable=0@IsWoEnableViewKeepMark=1@FastKeyGenerRole=@IsWoEnableTemplete=1");
			athDesc.Insert();

			//有可能在其其它的节点上没有这个附件，所以也要循环增加上它.
			 Nodes nds = new  Nodes(this.getFK_Flow());
			for (Node nd : nds.ToJavaList())
			{
				athDesc.setFK_MapData("ND" + nd.getNodeID());
				athDesc.setMyPK(athDesc.getFK_MapData() + "_" + athDesc.getNoOfObj());
				if (athDesc.getIsExits())
				{
					continue;
				}

				athDesc.Insert();
			}

			//重新查询一次，把默认值加上.
			athDesc.RetrieveFromDBSources();
		}
		///处理权限方案。
		//首先判断是否具有权限方案
		String at = SystemConfig.getAppCenterDBVarStr();
		Paras ps = new BP.DA.Paras();
		ps.SQL = "SELECT FrmSln FROM WF_FrmNode WHERE FK_Node=" + at + "FK_Node AND FK_Flow=" + at + "FK_Flow AND FK_Frm=" + at + "FK_Frm";
		ps.Add("FK_Node", this.getFK_Node());
		ps.Add("FK_Flow", this.getFK_Flow());
		ps.Add("FK_Frm", this.getFK_MapData());
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		if (dt.Rows.size() == 0)
		{
			athDesc.RetrieveFromDBSources();
		}
		else
		{
			int sln = Integer.parseInt(dt.getValue(0,0).toString());
			if (sln == 0)
			{
				athDesc.RetrieveFromDBSources();
			}
			else
			{
				result = athDesc.Retrieve(FrmAttachmentAttr.FK_MapData, this.getFK_MapData(), FrmAttachmentAttr.FK_Node, this.getFK_Node(), FrmAttachmentAttr.NoOfObj, this.getAth());

				if (result == 0) //如果没有定义，就获取默认的.
				{
					athDesc.RetrieveFromDBSources();
				}
				//  throw new Exception("@该流程表单在该节点("+this.FK_Node+")使用的是自定义的权限控制，但是没有定义该附件的权限。");
			}
		}

	}

	FrmAttachmentDBs dbs = new FrmAttachmentDBs();
	if (athDesc.getHisCtrlWay() == AthCtrlWay.PWorkID)
	{
		String pWorkID = String.valueOf(BP.DA.DBAccess.RunSQLReturnValInt("SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + this.getPKVal(), 0));
		if (pWorkID == null || pWorkID.equals("0"))
		{
			pWorkID = this.getPKVal();
		}

		if (athDesc.getAthUploadWay() == AthUploadWay.Inherit)
		{
			// 继承模式 
			BP.En.QueryObject qo = new BP.En.QueryObject(dbs);
			qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, pWorkID);
			qo.addOr();
			qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, Integer.parseInt(this.getPKVal()));
			qo.addOrderBy("RDT");
			qo.DoQuery();
		}

		if (athDesc.getAthUploadWay() == AthUploadWay.Interwork)
		{
			//共享模式
			dbs.Retrieve(FrmAttachmentDBAttr.RefPKVal, pWorkID);
		}
	}
	else
	{
		//yqh 2016-7-11  修改
		//int num = dbs.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, this.getFK_FrmAttachment(), FrmAttachmentDBAttr.RefPKVal, this.getPKVal(), "RDT");
		int num = 0;
		if (this.getFK_FrmAttachment().contains("AthMDtl"))
		{
			//如果是一个明细表的多附件，就直接按照传递过来的PK来查询.
			BP.En.QueryObject qo = new BP.En.QueryObject(dbs);
			qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, this.getPKVal());
			qo.addAnd();
			qo.AddWhere(FrmAttachmentDBAttr.FK_FrmAttachment, " LIKE ", "%AthMDtl");
			num = qo.DoQuery();
		}
		else
		{
			num = dbs.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, this.getFK_FrmAttachment(), FrmAttachmentDBAttr.RefPKVal, this.getPKVal(), "RDT");
		}
		
		if (num == 0 && this.getIsCC().equals("1"))
		{
			CCList cc = new CCList();
			int nnn = cc.Retrieve(CCListAttr.FK_Node, this.getFK_Node(), CCListAttr.WorkID, this.getWorkID(), CCListAttr.CCTo, WebUser.getNo());
			if (cc.getNDFrom() != 0)
			{
				this._fk_node = cc.getNDFrom();

				dbs.Retrieve(FrmAttachmentDBAttr.FK_FrmAttachment, this.getFK_FrmAttachmentExt(), FrmAttachmentDBAttr.FK_MapData, "ND" + cc.getNDFrom(), FrmAttachmentDBAttr.RefPKVal, (new Long(this.getWorkID())).toString());

				//重新设置文件描述。
				athDesc.Retrieve(FrmAttachmentAttr.FK_MapData, this.getFK_MapData(), FrmAttachmentAttr.NoOfObj, "DocMultiAth");
			}
		}
	}


		/// 生成表头表体.
	//this.setTitle(athDesc.getName();
	if (athDesc.getFileShowWay() == FileShowWay.Pict)
	{

		this.Pub1.append("<div class='slideBox' id='" + athDesc.getMyPK() + "' style='width:" + athDesc.getW() + "px;height:" + athDesc.getH() + "px; position:relative;  overflow:hidden;'>");
		this.Pub1.append("<ul class='items'>");
		for (FrmAttachmentDB db : dbs.ToJavaList())
		{
			if (db.getFileExts().toUpperCase().equals("JPG") || db.getFileExts().toUpperCase().equals("JPEG") || db.getFileExts().toUpperCase().equals("GIF") || db.getFileExts().toUpperCase().equals("PNG") || db.getFileExts().toUpperCase().equals("BMP"))
			{
				this.Pub1.append("<li> <a  title='" + db.getMyNote() + "'><img src = '" + db.getFileFullName() + "' width=" + athDesc.getW() + " height=" + athDesc.getH() + "/></a></li>");
			}
		}

		this.Pub1.append("</ul>");
		this.Pub1.append("</div>");
		this.Pub1.append("<script>$(function(){$('#" + athDesc.getMyPK() + "').slideBox({duration : 0.3,easing : 'linear',delay : 5,hideClickBar : false,clickBarRadius : 10});})</script>");
		// 如果是图片轮播，就在这里根据数据输出轮播的html代码.
		return;
	}

	// 执行装载模版.
	if (dbs.size() == 0 && athDesc.getIsWoEnableTemplete())
	{
		//如果数量为0,就检查一下是否有模版如果有就加载模版文件.
		String templetePath = BP.Sys.SystemConfig.getPathOfDataUser() + "AthTemplete/" + athDesc.getNoOfObj().trim();
		File file = new File(templetePath);
		System.out.println("临时文件路径:"+templetePath);
		if (!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//有模版文件夹
		File mydir = new File(templetePath);
		File[] fls = mydir.listFiles();
		if (fls==null || fls.length == 0)
		{
			throw new RuntimeException("@流程设计错误，该多附件启用了模版组件，模版目录:" + templetePath + "里没有模版文件.");
		}
		File saveToFile = new File(athDesc.getSaveTo());
		
		if (!saveToFile.exists())
		{
			try {
				saveToFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (File fl : fls)
		{
			int oid = BP.DA.DBAccess.GenerOID();
			String saveTo = athDesc.getSaveTo() + "/" + oid + "." + fl.getName().substring(fl.getName().lastIndexOf('.') + 1);
			if (saveTo.contains("@") || saveTo.contains("*"))
			{
				//如果有变量
				saveTo = saveTo.replace("*", "@");
				if (saveTo.contains("@") && this.getFK_Node() != 0)
				{
					//如果包含 @ 
					Flow flow = new Flow(this.getFK_Flow());
					BP.WF.Data.GERpt myen = flow.getHisGERpt();
					myen.setOID(this.getWorkID());
					myen.RetrieveFromDBSources();
					saveTo = BP.WF.Glo.DealExp(saveTo, myen, null);
				}
				if (saveTo.contains("@"))
				{
					throw new RuntimeException("@路径配置错误,变量没有被正确的替换下来." + saveTo);
				}
			}
			FileAccess.Copy(fl, saveTo);

			File info = new File(saveTo);
			FrmAttachmentDB dbUpload = new FrmAttachmentDB();

			dbUpload.CheckPhysicsTable();
			dbUpload.setMyPK(athDesc.getFK_MapData() + (new Integer(oid)).toString());
			dbUpload.setNodeID(String.valueOf(getFK_Node()));
			dbUpload.setFK_FrmAttachment(this.getFK_FrmAttachment());

			if (athDesc.getAthUploadWay() == AthUploadWay.Inherit)
			{
				//如果是继承，就让他保持本地的PK. 
				dbUpload.setRefPKVal(this.getPKVal().toString());
			}

			if (athDesc.getAthUploadWay() == AthUploadWay.Interwork)
			{
				//如果是协同，就让他是PWorkID. 
				String pWorkID = String.valueOf(BP.DA.DBAccess.RunSQLReturnValInt("SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + this.getPKVal(), 0));
				if (pWorkID == null || pWorkID.equals("0"))

				{
					pWorkID = this.getPKVal();
				}
				dbUpload.setRefPKVal(pWorkID);
			}

			dbUpload.setFK_MapData(athDesc.getFK_MapData());
			dbUpload.setFK_FrmAttachment(this.getFK_FrmAttachment());

			dbUpload.setFileExts(FileAccess.getExtensionName(info.getAbsolutePath()));
			dbUpload.setFileFullName(saveTo);
			dbUpload.setFileName(fl.getName());
			dbUpload.setFileSize(((float)info.length()));

			dbUpload.setRDT( DataType.getCurrentDataTime());
			dbUpload.setRec(WebUser.getNo());
			dbUpload.setRecName(WebUser.getName());
			//if (athDesc.getIsNote())
			//    dbUpload.MyNote = this.Pub1.GetTextBoxByID("TB_Note").Text;
			//if (athDesc.Sort.Contains(","))
			//    dbUpload.Sort = this.Pub1.GetDDLByID("ddl").SelectedItemStringVal;

			dbUpload.Insert();

			dbs.AddEntity(dbUpload);
		}
		//BP.Sys.FrmAttachmentDBs dbs = new BP.Sys.FrmAttachmentDBs();
	}

		///#region 处理权限问题.
	// 处理权限问题, 有可能当前节点是可以上传或者删除，但是当前节点上不能让此人执行工作。
	boolean isDel = athDesc.getIsDelete();
	boolean isUpdate = athDesc.getIsUpload();
	if (isDel|| isUpdate)
	{
		if (this.getWorkID() != 0 && !StringHelper.isNullOrEmpty(this.getFK_Flow()) && this.getFK_Node() != 0)
		{
			isDel = BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(this.getFK_Flow(), this.getFK_Node(), this.getWorkID(), WebUser.getNo());
			if (!isDel)
			{
				isUpdate = false;
			}
		}
	}
		///#endregion 处理权限问题.

	if (athDesc.getFileShowWay() == FileShowWay.Free)
	{
		this.Pub1.append(BaseModel.AddTable("border='0' cellspacing='0' cellpadding='0' style=''"));

		for (FrmAttachmentDB db : dbs.ToJavaList())
		{
			this.Pub1.append(BaseModel.AddTR());
			if (CanEditor(db.getFileExts()))
			{
				if (athDesc.getIsWoEnableWF())
				{
					this.Pub1.append(BaseModel.AddTD("<a href=\"javascript:OpenOfiice('" + this.getFK_FrmAttachment() + "','" + this.getWorkID() + "','" + db.getMyPK() + "','" + this.getFK_MapData() + "','" + this.getAth() + "','" + this.getFK_Node() + "')\"><img src='../Img/FileType/" + db.getFileExts() + ".gif' border=0 onerror=\"src='../Img/FileType/Undefined.gif'\" />" + db.getFileName() + "</a>"));
				}
				else
				{
					/*
					 * warning this.Pub1.append(BaseModel.AddTD("<a href=\"javascript:OpenFileView('" + this.getPKVal() + "','" + db.getMyPK() + "')\"><img src='../Img/FileType/" + db.getFileExts() + ".gif' border=0 onerror=\"src='../Img/FileType/Undefined.gif'\" />" + db.getFileName() + "</a>"));*/
					this.Pub1.append(BaseModel.AddTD("<img src='../Img/FileType/" + db.getFileExts() + ".gif' border=0 onerror=\"src='../Img/FileType/Undefined.gif'\" />" + db.getFileName()));
				}
			}
			else if (db.getFileExts().toUpperCase().equals("JPG") || db.getFileExts().toUpperCase().equals("JPEG") || db.getFileExts().toUpperCase().equals("GIF") || db.getFileExts().toUpperCase().equals("PNG") || db.getFileExts().toUpperCase().equals("BMP") || db.getFileExts().toUpperCase().equals("PDF") || db.getFileExts().toUpperCase().equals("CEB"))
			{
				/*
				 * warning this.Pub1.append(BaseModel.AddTD("<a href=\"javascript:OpenFileView('" + this.getPKVal() + "','" + db.getMyPK() + "')\"><img src='../Img/FileType/" + db.getFileExts() + ".gif' border=0 onerror=\"src='../Img/FileType/Undefined.gif'\" />" + db.getFileName() + "</a>"));*/
				this.Pub1.append(BaseModel.AddTD("<img src='../Img/FileType/" + db.getFileExts() + ".gif' border=0 onerror=\"src='../Img/FileType/Undefined.gif'\" />" + db.getFileName()));
			}
			else
			{
				this.Pub1.append(BaseModel.AddTD("<a href='AttachmentUpload.jsp?DoType=Down&MyPK=" + db.getMyPK() + "' target=_blank ><img src='../Img/FileType/" + db.getFileExts() + ".gif' border=0 onerror=\"src='../Img/FileType/Undefined.gif'\" />" + db.getFileName() + "</a>"));
			}

			if (athDesc.getIsDownload())
			{
				this.Pub1.append(BaseModel.AddTD("<a href=\"javascript:Down('" + this.getFK_FrmAttachment() + "','" + this.getPKVal() + "','" + db.getMyPK() + "')\">下载</a>"));
			}
			else
			{
				this.Pub1.append(BaseModel.AddTD(""));
			}

			if (!this.getIsReadonly().equals("1"))
			{
				if (athDesc.getIsDelete())
				{
					this.Pub1.append(BaseModel.AddTD("style='border:0px'", "<a href=\"javascript:Del('" + this.getFK_FrmAttachment() + "','" + this.getPKVal() + "','" + db.getMyPK() + "')\">删除</a>"));
				}
				else
				{
					this.Pub1.append(BaseModel.AddTD(""));
				}
			}
			else
			{
				this.Pub1.append(BaseModel.AddTD(""));
				this.Pub1.append(BaseModel.AddTD(""));
			}

			this.Pub1.append(BaseModel.AddTREnd());
		}
		AddFileUpload(isUpdate, athDesc);
		this.Pub1.append(BaseModel.AddTableEnd());
		return;
	}

	this.Pub1.append(BaseModel.AddTable("border='0' cellspacing='0' cellpadding='0' style='' width='100%' "));
	if (athDesc.getIsShowTitle())
	{
		this.Pub1.append(BaseModel.AddTR("style='border:0px'"));

		this.Pub1.append(BaseModel.AddTDTitleExt("序号"));
		if (athDesc.getSort().contains(","))
		{
			this.Pub1.append(BaseModel.AddTD("style='background:#f4f4f4; font-size:12px; padding:3px;'", "类别"));
		}
		this.Pub1.append(BaseModel.AddTDTitleExt("文件名"));
		this.Pub1.append(BaseModel.AddTDTitleExt("大小KB"));
		this.Pub1.append(BaseModel.AddTDTitleExt("上传时间"));
		this.Pub1.append(BaseModel.AddTDTitleExt("上传人"));
		if (athDesc.getIsDownload())
		{
			this.Pub1.append(BaseModel.AddTDTitleExt("下载"));
		}
		this.Pub1.append(BaseModel.AddTDTitleExt("操作"));
		this.Pub1.append(BaseModel.AddTREnd());
	}

	int i = 0;
	for (FrmAttachmentDB db : dbs.ToJavaList())
	{
		i++;
		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTDIdx(i));
		if (athDesc.getSort().contains(","))
		{
			this.Pub1.append(BaseModel.AddTD(db.getSort()));
		}

		// this.Pub1.AddTDIdx(i++);
		if (athDesc.getIsDownload())
		{
			if (athDesc.getIsWoEnableWF() && CanEditor(db.getFileExts()))
			{
				this.Pub1.append(BaseModel.AddTD("<a href=\"javascript:OpenOfiice('" + this.getFK_FrmAttachment() + "','" + this.getWorkID() + "','" + db.getMyPK() + "','" + this.getFK_MapData() + "','" + this.getAth() + "','" + this.getFK_Node() + "')\"><img src='../Img/FileType/" + db.getFileExts() + ".gif' border=0 onerror=\"src='../Img/FileType/Undefined.gif'\" />" + db.getFileName() + "</a>"));
			}
			else if (db.getFileExts().toUpperCase().equals("JPG") || db.getFileExts().toUpperCase().equals("JPEG") || db.getFileExts().toUpperCase().equals("GIF") || db.getFileExts().toUpperCase().equals("PNG") || db.getFileExts().toUpperCase().equals("BMP") || db.getFileExts().toUpperCase().equals("PDF") || db.getFileExts().toUpperCase().equals("CEB"))
			{
				/*
				 * warning this.Pub1.append(BaseModel.AddTD("<a href=\"javascript:OpenFileView('" + this.getPKVal() + "','" + db.getMyPK() + "')\"><img src='../Img/FileType/" + db.getFileExts() + ".gif' border=0 onerror=\"src='../Img/FileType/Undefined.gif'\" />" + db.getFileName() + "</a>"));*/
				this.Pub1.append(BaseModel.AddTD("<img src='../Img/FileType/" + db.getFileExts() + ".gif' border=0 onerror=\"src='../Img/FileType/Undefined.gif'\" />" + db.getFileName()));
			}
			else
			{
				this.Pub1.append(BaseModel.AddTD("<a href='AttachmentUpload.jsp?DoType=Down&MyPK=" + db.getMyPK() + "' target=_blank ><img src='../Img/FileType/" + db.getFileExts() + ".gif' border=0 onerror=\"src='../Img/FileType/Undefined.gif'\" />" + db.getFileName() + "</a>"));
			}
		}
		else
		{
			this.Pub1.append(BaseModel.AddTD("<img src='../Img/FileType/" + db.getFileExts() + ".gif' border=0 onerror=\"src='../Img/FileType/Undefined.gif'\" />" + db.getFileName()));
		}

		this.Pub1.append(BaseModel.AddTD(db.getFileSize()));
		this.Pub1.append(BaseModel.AddTD(db.getRDT()));
		this.Pub1.append(BaseModel.AddTD(db.getRecName()));
		if (athDesc.getIsDownload())
		{
			this.Pub1.append(BaseModel.AddTD("<a href=\"javascript:Down('" + this.getFK_FrmAttachment() + "','" + this.getPKVal() + "','" + db.getMyPK() + "')\">下载</a>"));
		}

		if (!this.getIsReadonly().equals("1"))
		{
			String op = null;
			if (isDel == true)
			{
				op = "<a href=\"javascript:Del('" + this.getFK_FrmAttachment() + "','" + this.getPKVal() + "','" + db.getMyPK() + "')\">删除</a>";
			}
			this.Pub1.append(BaseModel.AddTD(op));
		}
		else
		{
			this.Pub1.append(BaseModel.AddTD(""));
		}
		this.Pub1.append(BaseModel.AddTREnd());
	}
	if (i == 0)
	{
		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD("0"));
		if (athDesc.getSort().contains(","))
		{
			this.Pub1.append(BaseModel.AddTD("&nbsp&nbsp"));
		}
		this.Pub1.append(BaseModel.AddTD("style='width:100px'", "<span style='color:red;' >上传附件</span>"));
		this.Pub1.append(BaseModel.AddTD("&nbsp&nbsp"));
		this.Pub1.append(BaseModel.AddTD("&nbsp&nbsp"));
		this.Pub1.append(BaseModel.AddTD("&nbsp&nbsp"));
		this.Pub1.append(BaseModel.AddTD("&nbsp&nbsp"));
		this.Pub1.append(BaseModel.AddTD("&nbsp&nbsp"));
		this.Pub1.append(BaseModel.AddTREnd());
	}

	AddFileUpload(isUpdate, athDesc);
	this.Pub1.append(BaseModel.AddTableEnd());
		//#endregion 生成表头表体.
	}
	
	private void AddFileUpload(boolean isUpdate, FrmAttachment athDesc)
	{
		if (isUpdate == true && !this.getIsReadonly().equals("1"))
		{
			this.Pub1.append(BaseModel.AddTR());
			if (athDesc.getIsNote())
			{
				this.Pub1.append(BaseModel.AddTDBegin("colspan=8"));
			}
			else
			{
				this.Pub1.append(BaseModel.AddTDBegin("colspan=7"));
			}
			this.Pub1.append("文件:");

			this.Pub1.append("<input type=\"file\" name=\"file\" id=\"file\" onchange=\"UploadChange('Btn_Upload');\" />");
//			System.Web.UI.WebControls.FileUpload fu = new System.Web.UI.WebControls.FileUpload();
//			fu.ID = "file";
//			fu.BorderStyle = BorderStyle.NotSet;
//			fu.Attributes["onchange"] = "UploadChange('Btn_Upload');";
//			this.Pub1.append(fu);
			if (athDesc.getSort().contains(","))
			{
				String[] strs = athDesc.getSort().split("[,]", -1);
				this.Pub1.append("<select id='ddl' name='ddl'>");
//				BP.Web.Controls.DDL ddl = new BP.Web.Controls.DDL();
//				ddl.ID = "ddl";
				for (String str : strs)
				{
					if (str == null || str.equals(""))
					{
						continue;
					}
					this.Pub1.append("<option value=\""+str+"\">"+str+"</option>");
					//ddl.Items.Add(new ListItem(str, str));
				}
				this.Pub1.append("</select>");
			}

			if (athDesc.getIsNote())
			{
				
//				TextBox tb = new TextBox();
//				tb.ID = "TB_Note";
//				tb.Attributes["Width"] = "90%";
//				tb.Attributes["style"] = "display:none;";
//				// tb.Attributes["class"] = "TBNote";
//				tb.Columns = 30;
				// this.Pub1.append("&nbsp;备注:");
				this.Pub1.append("<input name=\"TB_Note\" type=\"text\" size=\"30\" id=\"TB_Note\" Width=\"90%\" style=\"display:none;\" />");
			}
			//Button btn = new Button();
			//btn.Text = "上传";
			//btn.ID = "Btn_Upload";
			//btn.CssClass = "Btn";
			//btn.Click += new EventHandler(btn_Click);
			//btn.Attributes["style"] = "display:none;";
			//this.Pub1.append(btn);
			this.Pub1.append(BaseModel.AddTDEnd());
			this.Pub1.append(BaseModel.AddTREnd());
		}
	}

	private String GetRealPath(String fileFullName)
	{
		boolean isFile = false;
		String downpath = "";
		try
		{
			//如果相对路径获取不到可能存储的是绝对路径
			String tmpStr = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("/" + fileFullName);
			File downInfo = new File(tmpStr);
			isFile = true;
			downpath = tmpStr;
		}
		catch (RuntimeException e)
		{
			File downInfo = new File(fileFullName);
			isFile = true;
			downpath = fileFullName;
		}
		if (!isFile)
		{
			throw new RuntimeException("没有找到下载的文件路径！");
		}

		return downpath;
	}

	private boolean CanEditor(String fileType)
	{
		try
		{
			String fileTypes = (String) BP.Sys.SystemConfig.getAppSettings().get("OpenTypes");
			if (StringHelper.isNullOrEmpty(fileTypes) == true)
			{
				fileTypes = "doc,docx,pdf,xls,xlsx";
			}

			if (fileTypes.contains(fileType.toLowerCase()))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (RuntimeException e)
		{
			return false;
		}
	}
	public static boolean isNullOrEmpty(String string)
	{
		return string == null || string.equals("");
	}
}

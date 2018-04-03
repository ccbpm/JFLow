package cn.jflow.model.designer;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataType;
import BP.En.Attr;
import BP.En.AttrFile;
import BP.En.AttrFiles;
import BP.En.Attrs;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.Map;
import BP.En.UAC;
import BP.Sys.EnCfg;
import BP.Sys.SysFileManager;
import BP.Sys.SysFileManagerAttr;
import BP.Sys.SysFileManagers;
import BP.Tools.StringHelper;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;
import cn.jflow.system.ui.core.ToolBar;

public class UIEnsModel extends BaseModel{


	public Entities GetEns=null;
	
	private boolean isReadOnly;
	
	public ToolBar ToolBar1=null;
	
	public UiFatory uf;
	
	public UCEnModel UCEn1=new UCEnModel(this.get_request(), this.get_response());
	
	public UiFatory pub=null;

   /// <summary>
   /// 得到一个新的事例数据．
   /// </summary>
   private Entity GetEnDa=new Entity() {
		
		@Override
		public Map getEnMap() {
			return null;
		}
	};
	
	
	private Entity CurrEn=new Entity() {
		
		@Override
		public Map getEnMap() {
			return null;
		}
	};
	
	public Entity getCurrEn() throws Exception {
		if(CurrEn.getIsEmpty()){
			this.CurrEn=this.getGetEnDa();
		}
		return CurrEn;
	}
	public void setCurrEn(Entity currEn) {
		CurrEn = currEn;
	}
	
	

	public Entity getGetEnDa() throws Exception {
		String pk = StringHelper.isEmpty(this.get_request().getParameter("PK"), "");
		Entity en = this.getGetEns().getGetNewEntity();
        Attrs myattrs1 = en.getEnMap().getAttrs();
        for(Attr attr:myattrs1)
        {
            if (StringHelper.isNullOrEmpty(this.get_request().getParameter(attr.getKey())))
                continue;
            en.SetValByKey(attr.getKey(), this.get_request().getParameter(attr.getKey()));
        }
        if (en.getPKCount() == 1)
        {
            if (!pk.equals(""))
            {
                en.setPKVal(pk);
            }
            else
            {
                if (StringHelper.isNullOrEmpty(this.get_request().getParameter(en.getPK())))
                    return en;
                else
                    en.setPKVal(this.get_request().getParameter(en.getPK()));
            }
            if (en.getIsExits() == false)
            {
            	Alert("@记录不存在,或者没有保存.");
            	return null;
//                throw new Exception("@记录不存在,或者没有保存.");
            }
            else
            {
                int i = en.RetrieveFromDBSources();
                if (i == 0)
                    en.RetrieveFromDBSources();
            }
            Attrs myattrs = en.getEnMap().getAttrs();
            for(Attr attr:myattrs)
            {
                if (StringHelper.isNullOrEmpty(this.get_request().getParameter(attr.getKey())))
                    continue;
                en.SetValByKey(attr.getKey(), this.get_request().getParameter(attr.getKey()));
            }
            return en;
        }
        else if (en.getIsMIDEntity())
        {
            String val = this.get_request().getParameter("MID");
            if (StringHelper.isNullOrEmpty(val))
                val = this.get_request().getParameter("PK");
            if (StringHelper.isNullOrEmpty(val))
            {
                return en;
            }
            else
            {
                en.SetValByKey("MID", val);
                en.RetrieveFromDBSources();
                return en;
            }
        }

        Attrs attrs = en.getEnMap().getAttrs();
        for(Attr attr:attrs)
        {
            if (attr.getIsPK())
            {
                String str = getParameter(attr.getKey());
                if (StringHelper.isNullOrEmpty(str))
                {
                    if (en.getIsMIDEntity())
                    {
                        en.SetValByKey("MID", pk);
                        continue;
                    }
                    else
                    {
                    	Alert("@没有把主键值[" + attr.getKey() + "]传输过来.");
                    	return null;
//                        throw new Exception("@没有把主键值[" + attr.getKey() + "]传输过来.");
                    }
                }
            }
            if (!StringHelper.isNullOrEmpty(this.get_request().getParameter(attr.getKey())))
                en.SetValByKey(attr.getKey(), this.get_request().getParameter(attr.getKey()));
        }

        if (en.getIsExits() == false)
        {
//            throw new Exception("@数据没有记录.");
            Alert("@数据没有记录.");
        	return null;
        }
        else
        {
            en.RetrieveFromDBSources();
            Attrs myattrs = en.getEnMap().getAttrs();
            for(Attr attr:myattrs)
            {
                if (StringHelper.isNullOrEmpty(this.get_request().getParameter(attr.getKey())))
                    continue;
                en.SetValByKey(attr.getKey(), this.get_request().getParameter(attr.getKey()));
            }
        }
        return en;
	}
	public void setGetEnDa(Entity getEnDa) {
		GetEnDa = getEnDa;
	}
	
	public boolean isReadOnly() {
		return isReadOnly;
	}
	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}
	public UIEnsModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		uf=new UiFatory();
		ToolBar1=new ToolBar(request,response,uf);
		pub=new UiFatory();
	}
	

	public Entities getGetEns() {
		 if (GetEns == null)
         {
             String enName = this.getEnName();
             if (!StringHelper.isNullOrEmpty(enName))
             {
                 if (enName.toString().contains("."))
                 {
                     Entity en = ClassFactory.GetEn(enName.toString());
                     GetEns = en.getGetNewEntities();
                 }
                 else
                 {
                     GetEns = ClassFactory.GetEns(enName.toString());
                 }
             }
             else
             {
                 GetEns = ClassFactory.GetEns(this.getEnsName());
             }
         }
         return GetEns;
	}
	public void setGetEns(Entities getEns) {
		GetEns = getEns;
	}
	
	public final String getEnsName(){
		String ensName = this.get_request().getParameter("EnsName");
        if (StringHelper.isNullOrEmpty(ensName))
            ensName = null;//this.ViewState["EnsName"] as string;
        else
        	this.get_request().setAttribute("EnsName", ensName);
            //this.ViewState[""] = ensName;

        if (ensName == null)
        {
            String s = this.get_request().getParameter("EnName");
            Entity en = ClassFactory.GetEn(s);
            ensName = en.getGetNewEntities().getClass().getName();
        }
        return ensName;
	}
	/** 
	 类名成
	 
	*/
	public final String getEnName() {
		String enName = this.get_request().getParameter("EnName");
		if (enName == null || enName.equals("")) {
			enName = null;
		}
		else {
			this.get_request().setAttribute("EnName",enName);
		}
		if (enName == null) {
			String s = this.get_request().getParameter("EnsName");
			if(s==null || "".equals(s)) s=null;
			Entities ens = BP.En.ClassFactory.GetEns(s);
            enName = ens.getGetNewEntity().getClass().getName();
		}
		return enName;
	}

	public void init()
	{
		//#region 清除缓存;
//		this.Response.Expires = -1;
//		this.Response.ExpiresAbsolute = DateTime.Now.AddMonths(-1);
//		this.Response.CacheControl = "no-cache";
		get_response().setHeader("Cache-Control","no-cache"); //HTTP 1.1      
		get_response().setHeader("Pragma","no-cache"); //HTTP 1.0      
		get_response().setDateHeader ("Expires", 0); //prevents caching at the proxy
		//#endregion 清除缓存

		try
		{
			//#region 判断权限
			String pk = this.get_request().getParameter("PK");
			if (pk == null){
				pk = getParameter(this.getCurrEn().getPK());
			}
			UAC uac = this.getCurrEn().getHisUAC();
			if (uac.IsView == false){
				ToErrorPage("@对不起，您没有查看的权限！");
				return;
				//throw new Exception("@对不起，您没有查看的权限！");
			}

			this.setReadOnly(!(uac.IsUpdate));  //是否更有修改的权限．
			if ("1".equals(getParameter("IsReadonly")) || "1".equals(getParameter("Readonly"))){
				this.setReadOnly(true);
			}
			
//			uac.IsUpdate=true;
			this.ToolBar1.InitFuncEn(uac, this.getCurrEn());

			this.UCEn1.setReadonly(this.getIsReadonly());
			this.UCEn1.setShowDtl(true);
			this.UCEn1.HisEn = this.getCurrEn();
			
			EnCfg ec = new EnCfg();
			ec.setNo(this.getEnName());
			int i = ec.RetrieveFromDBSources();

			if(i >= 1) {
			    this.UCEn1.BindV2(this.getCurrEn(), this.getCurrEn().toString(),this.getIsReadonly(), true);
			}else{
				this.UCEn1.Bind(this.getCurrEn(), this.getCurrEn().toString(), this.getIsReadonly(), true);
			}
		    /*
			if(getEnsName().contains("BP.WF.Template.Selector") || getEnsName().contains("BP.WF.Template.FlowSorts"))
                this.UCEn1.Bind(this.getCurrEn(), this.getCurrEn().toString(), this.getIsReadonly(), true);
			else{
				if (getEnsName().contains("BP.WF.Template") == true)
	            
	            else
	                this.UCEn1.Bind(this.getCurrEn(), this.getCurrEn().toString(), this.getIsReadonly(), true);
			}*/
		    
//			if(EnsName().equals("BP.WF.Template.Selectors") || EnsName().equals("BP.WF.Template.CCs")){
//				this.UCEn1.Bind(this.CurrEn, this.CurrEn.toString(), this.isReadOnly, true);
//			}else{
//				if (EnsName().contains("BP.WF.Template") == true)
//					this.UCEn1.BindV2(this.CurrEn, this.CurrEn.toString(), this.isReadOnly, true);
//				else
//					this.UCEn1.Bind(this.CurrEn, this.CurrEn.toString(), this.isReadOnly, true);
//			}
		}
		
		catch (Exception ex)
		{
//			response.write(ex.getMessage());
			ex.printStackTrace();
			Entity en = ClassFactory.GetEns(getEnsName()).getGetNewEntity();
			en.CheckPhysicsTable();
			return;
		}

		//this.Page.Title = this.CurrEn.getEnDesc();

		//#region 设置事件
//		if (this.Btn_DelFile != null)
//			this.Btn_DelFile.Click += new ImageClickEventHandler(Btn_DelFile_Click);
		if (this.ToolBar1.GetLinkBtnByID(NamesOfBtn.New)!=null)
			this.ToolBar1.GetLinkBtnByID(NamesOfBtn.New).setHref("onNew();");	
		
		if (this.ToolBar1.GetLinkBtnByID(NamesOfBtn.Save)!=null)
		    this.ToolBar1.GetLinkBtnByID(NamesOfBtn.Save).setHref("onSave();");	
		
		if (this.ToolBar1.GetLinkBtnByID(NamesOfBtn.SaveAndClose)!=null)
			this.ToolBar1.GetLinkBtnByID(NamesOfBtn.SaveAndClose).setHref("onSaveOrClose();");
		
		if (this.ToolBar1.GetLinkBtnByID(NamesOfBtn.SaveAndNew)!=null)
			this.ToolBar1.GetLinkBtnByID(NamesOfBtn.SaveAndNew).setHref("onSaveAndNew();");	
		
		if (this.ToolBar1.GetLinkBtnByID(NamesOfBtn.Delete)!=null)
		    this.ToolBar1.GetLinkBtnByID(NamesOfBtn.Delete).setHref("onDelete();");	
//		AttrFiles fls = this.CurrEn.getEnMap().getHisAttrFiles();
//		for(AttrFile fl:fls)
//		{
//			if (this.UCEn1.IsExit("Btn_DelFile" + fl.FileNo))
//				this.UCEn1.GetImageButtonByID("Btn_DelFile" + fl.FileNo).Click += new ImageClickEventHandler(Btn_DelFile_X_Click);
//		}
	}
//	public String getEnsName() {
//		return ensName;
//	}
//	public void setEnsName(String ensName) {
//		this.ensName = ensName;
//	}
//
//	public ImageButton Btn_DelFile
//	{
//		get
//		{
//			return this.UCEn1.FindControl("Btn_DelFile") as ImageButton;
//		}
//	}
	public void DelFile(String id)
	{
	}
//	private void Btn_DelFile_X_Click(object sender, ImageClickEventArgs e)
//	{
//		ImageButton btn = sender as ImageButton;
//		string id = btn.ID.Replace("Btn_DelFile", "");
//		SysFileManager sf = new SysFileManager();
//		string sql = "DELETE FROM " + sf.getEnMap().getPhysicsTable() + " WHERE " + SysFileManagerAttr.EnName + "='" + this.GetEns.GetNewEntity.ToString() + "' AND RefVal='" + this.PKVal + "' AND " + SysFileManagerAttr.AttrFileNo + "='" + id + "'";
//		BP.DA.DBAccess.RunSQL(sql);
//		this.Response.Redirect("UIEn.aspx?EnsName=" + this.EnsName + "&PK=" + this.PKVal, true);
//	}
//	private void Btn_DelFile_Click(object sender, ImageClickEventArgs e)
//	{
//		Entity en = this.UCEn1.GetEnData(this.GetEns.GetNewEntity);
//		en.RetrieveFromDBSources();
//
//		string file = en.GetValStringByKey("MyFilePath") + "//" + en.PKVal + "." + en.GetValStringByKey("MyFileExt");
//		try
//		{
//			System.IO.File.Delete(file);
//		}
//		catch(Exception e)
//		{
//		}
//		en.SetValByKey("MyFileExt", "");
//		en.SetValByKey("MyFileName", "");
//		en.SetValByKey("MyFilePath", "");
//		en.Update();
//		this.Response.Redirect("UIEn.aspx?EnsName=" + this.EnsName + "&EnName=" + this.EnName + "&PK=" + this.PKVal, true);
//	}
//	private void ToolBar1_ButtonClick(object sender, System.EventArgs e)
//	{
//
//		LinkBtn btn = (LinkBtn)sender;
//		try
//		{
//			switch (btn.ID)
//			{
//			case NamesOfBtn.Copy:
//				Copy();
//				break;
//			case NamesOfBtn.Help:
//				//this.Helper(this.GetEns.GetNewEntity.EnMap.Helper);
//				break;
//			case NamesOfBtn.New:
//				//   New();
//				this.Response.Redirect("UIEn.aspx?EnsName=" + this.EnsName + "&EnName=" + this.EnName, true);
//				break;
//			case NamesOfBtn.SaveAndNew:
//				try
//				{
//					this.Save();
//				}
//				catch (Exception ex)
//				{
//					this.Alert(ex.Message);
//					// this.ResponseWriteBlueMsg(ex.Message);
//					return;
//				}
//				this.Response.Redirect("UIEn.aspx?EnsName=" + this.EnsName + "&EnName=" + this.EnName, true);
//				break;
//			case NamesOfBtn.SaveAndClose:
//				try
//				{
//					this.Save();
//					this.WinClose();
//				}
//				catch (Exception ex)
//				{
//					this.Alert(ex.Message);
//					// this.ResponseWriteBlueMsg(ex.Message);
//					return;
//				}
//				break;
//			case NamesOfBtn.Save:
//				try
//				{
//					this.Save();
//				}
//				catch (Exception ex)
//				{
//					this.Alert(ex.Message);
//					return;
//				}
//				this.Response.Redirect("UIEn.aspx?EnsName=" + this.EnsName + "&PK=" + this.PKVal + "&EnName=" + this.EnName + "&tab=" + Uri.EscapeDataString(GetHiddenTabTitle()), true);
//				break;
//			case NamesOfBtn.Delete:
//				try
//				{
//					Entity en = this.UCEn1.GetEnData(this.GetEns.GetNewEntity);
//					if (this.PKVal != null)
//						en.PKVal = this.PKVal;
//					en.Delete();
//					// this.Alert("删除成功");
//					this.WinCloseWithMsg("删除成功!!!");
//					//this.ToMsgPage("删除成功!!!");
//					return;
//				}
//				catch (Exception ex)
//				{
//					this.Alert("删除成功!!!");
//					//this.ToMsgPage("删除期间出现错误: \t\n" + ex.Message);
//					//this.ToMsgPage("删除成功!!!");
//					return;
//				}
//				return;
//			case NamesOfBtn.Close:
//				this.WinClose();
//				break;
//			case "Btn_EnList":
//				this.EnList();
//				break;
//			case NamesOfBtn.Export:
//				//this.ExportDGToExcel_OpenWin(this.UCEn1,"" );
//				break;
//			case NamesOfBtn.Adjunct:
//				//this.InvokeFileManager(this.GetEnDa);
//				break;
//			default:
//				throw new Exception("@没有找到" + btn.ID);
//			}
//		}
//		catch (Exception ex)
//		{
//			this.ResponseWriteRedMsg(ex.Message);
//		}
//	}

	/// <summary>
	/// 获取隐藏域中保存的当前打开标签值
	/// </summary>
	/// <returns></returns>
	private String GetHiddenTabTitle()
	{
		TextBox hiddenField = (TextBox)uf.GetUIByID("Hid_CurrentTab");

		if (hiddenField != null)
			return hiddenField.getText();

		return "";
	}

	private Object pKVal;


	public Object getpKVal() {
		if (pKVal == null)
			pKVal = this.get_request().getParameter("PK");

		if (pKVal == null)
			pKVal = this.get_request().getParameter("OID");

		if (pKVal == null)
			pKVal = this.get_request().getParameter("No");

		if (pKVal == null)
			pKVal = this.get_request().getParameter("MyPK");
		return pKVal;
	}
	public void setpKVal(Object pKVal) {
		this.pKVal = pKVal;
	}
	
	public void Copy()
	{
		try
		{
			this.pKVal = null;
			Entity en = this.UCEn1.GetEnData(this.GetEns.getGetNewEntity());
			en.Copy();
			this.UCEn1.Bind(en, en.toString(), this.isReadOnly, true);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();;
		}
	}

	

	/// <summary>
	/// delete
	/// </summary>
	public void Delete()
	{
		Entity en = this.GetEnDa;
		en.setPKVal(this.pKVal);
		en.Delete();
		this.winCloseWithMsg(null);
	}
	public void Save() throws Exception
	{
		Entity en = this.GetEns.getGetNewEntity();
		if (this.pKVal != null)
		{
			en.setPKVal(this.pKVal);
			en.RetrieveFromDBSources();
		}

		en = this.UCEn1.GetEnData(en);

		this.CurrEn = en;
		en.Save();
		this.pKVal = en.getPKVal();

		//#region 保存 实体附件
		try
		{
			if (en.getEnMap().getAttrs().Contains("MyFileName"))
			{
				UiFatory uf=new UiFatory();
				TextBox file=(TextBox)uf.GetUIByID("file");
				if (file != null && file.toString().indexOf(".") != -1)
				{
					BP.Sys.EnCfg cfg = new EnCfg(en.toString());
					File f=new File(cfg.getFJSavePath());
					if (f.exists() == false)
						f.mkdirs();

					/* 如果包含这二个字段。*/
					String fileName = f.getName();
					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);

					String filePath = cfg.getFJSavePath();
					en.SetValByKey("MyFilePath", filePath);

					String ext = "";
					if (fileName.indexOf(".") != -1)
						ext = fileName.substring(fileName.lastIndexOf(".") + 1);

					en.SetValByKey("MyFileExt", ext);
					en.SetValByKey("MyFileName", fileName);
					en.SetValByKey("WebPath", cfg.getFJWebPath() + en.getPKVal() + "." + ext);

					String fullFile = filePath + "/" + en.getPKVal() + "." + ext;

//					file.getPostedFile().SaveAs(fullFile);
//					file.PostedFile.InputStream.Close();
//					file.PostedFile.InputStream.Dispose();
//					file.Dispose();

					File info=new File(fullFile);
					en.SetValByKey("MyFileSize", BP.DA.DataType.PraseToMB(info.length()));
//					if (DataType.IsImgExt(ext))
//					{
//						System.Drawing.Image img = System.Drawing.Image.FromFile(fullFile);
//						en.SetValByKey("MyFileH", img.Height);
//						en.SetValByKey("MyFileW", img.Width);
//						img.Dispose();
//					}
					en.Update();
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		//#region 保存 属性 附件
		try
		{
			AttrFiles fils = en.getEnMap().getHisAttrFiles();
			SysFileManagers sfs = new SysFileManagers(en.toString(), en.getPKVal().toString());
			for(AttrFile fl:fils)
			{
				TextBox tb=new TextBox();
				tb.setTextMode(TextBoxMode.Files);
				UiFatory uf=new UiFatory();
				TextBox file =(TextBox)uf.GetUIByID("F" + fl.FileNo);
				if (file.toString().contains(".") == false)
					continue;

				SysFileManager enFile = (SysFileManager)sfs.GetEntityByKey(SysFileManagerAttr.AttrFileNo, fl.FileNo);
				SysFileManager enN = null;
				if (enFile == null)
				{
//					enN = this.FileSave(null, file, en);
				}
				else
				{
					enFile.Delete();
//					enN = this.FileSave(null, file, en);
				}

				enN.setAttrFileNo(fl.FileNo);
				enN.setAttrFileName(fl.FileName);
				enN.setEnName(en.toString());
				enN.Update();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	/// <summary>
	/// 文件保存
	/// </summary>
	/// <param name="fileNameDesc"></param>
	/// <param name="File1"></param>
	/// <returns></returns>
	private SysFileManager FileSave(String fileNameDesc, TextBox tbf, Entity myen)
	{
		SysFileManager en = new SysFileManager();
		en.setEnName(myen.toString());
		// en.FileID = this.RefPK + "_" + count.ToString();
		EnCfg cfg = new EnCfg(en.getEnName());

		String filePath = cfg.getFJSavePath(); // BP.Sys.SystemConfig.PathOfFDB + "\\" + this.EnName + "\\";
		File f=new File(filePath);
		if (f.exists() == false)
			f.mkdirs();

//		String ext = System.IO.Path.GetExtension(File1.PostedFile.FileName);
//		ext = ext.replace(".", "");
//		en.setMyFileExt(ext);
		if (fileNameDesc.equals("") || fileNameDesc == null){
			
//			en.setMyFileName(System.IO.Path.GetFileNameWithoutExtension(File1.PostedFile.FileName));
		}else
			en.setMyFileName(fileNameDesc);
		en.setRDT(DataType.getCurrentData());
		en.setRefVal(myen.getPKVal().toString());
		en.setMyFilePath(filePath);
		en.Insert();

		String fileName = filePath + en.getOID() + "." + en.getMyFileExt();
//		File1.PostedFile.SaveAs(fileName);
//
//		File1.PostedFile.InputStream.Close();
//		File1.PostedFile.InputStream.Dispose();
//		File1.Dispose();

		File fi = new File(fileName);
		en.setMyFileSize(DataType.PraseToMB(fi.length()));

		if (DataType.IsImgExt(en.getMyFileExt()))
		{
//			System.Drawing.Image img = System.Drawing.Image.FromFile(fileName);
//			en.MyFileH = img.Height;
//			en.MyFileW = img.Width;
//			img.Dispose();
		}
		en.setWebPath(cfg.getFJWebPath() + en.getOID() + "." + en.getMyFileExt());
		en.Update();
		return en;
	}

	public void EnList() throws IOException
	{
		sendRedirect(this.get_request().getRealPath("/") + "/Comm/UIEns.htm?EnsName=" + this.getEnsName());
	}
}

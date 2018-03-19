package cn.jflow.controller.des;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.jflow.common.model.BaseModel;
import cn.jflow.common.model.TempObject;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.TextBox;
import BP.En.Attr;
import BP.En.AttrFile;
import BP.En.AttrFiles;
import BP.En.Attrs;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.UIContralType;
import BP.Sys.PubClass;
import BP.Sys.SysFileManagers;
import BP.Tools.StringHelper;
import BP.WF.Glo;

@Controller
@RequestMapping(value = "/DES")
public class UIEnController{

	public Entities _GetEns = null;

//	private Entity CurrEn = null;
	
//	public Entity getCurrEn() throws Exception {
//		if (null == CurrEn || CurrEn.getIsEmpty()) {
//			this.CurrEn = this.getGetEnDa();
//		}
//		return CurrEn;
//	}
	
	private String PK_Val;
	
	public void setPK_Val(String pK_Val) {
		PK_Val = pK_Val;
	}
	
//	public void setCurrEn(Entity currEn) {
//		CurrEn = currEn;
//	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(String EnsName, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		doSave( EnsName, request);
		
		String index=request.getParameter("index");
        String	tab = "&tab=" + index;
		
         if(StringHelper.isNullOrEmpty(EnsName)){
			EnsName=request.getParameter("EnName")+"s";
			response.sendRedirect(Glo.getCCFlowAppPath()+"WF/Comm/RefFunc/UIEn.jsp?EnsName=" + EnsName + "&PK=" + PK_Val + "&EnName=" + this.getEnName(EnsName) + tab);
		}else{
			response.sendRedirect(Glo.getCCFlowAppPath()+"WF/Comm/RefFunc/UIEn.jsp?EnsName=" + EnsName + "&PK=" + PK_Val + "&EnName=" + this.getEnName(EnsName) + tab);
		}
		return null;
	}

	/**
	 * 执行保存
	 * @param object
	 * @throws Exception
	 */
	private void doSave(String EnsName, HttpServletRequest request) throws Exception {
		
		// 小周鹏添加，当获取不到EnsName时, 可通过EnName获取，否则数据无法保存 Start
		if (StringHelper.isNullOrEmpty(EnsName)) {
			EnsName = request.getParameter("EnName")+"s";
		}
		// 小周鹏添加，当获取不到EnsName时, 可通过EnName获取，否则数据无法保存 End

		Entity en = getGetEns(EnsName, request).getGetNewEntity();
		String pk_value = getPKVal(request);
		if (!StringHelper.isNullOrEmpty(pk_value)) {
			en.setPKVal(pk_value);
			en.RetrieveFromDBSources();
		}

		en = GetEnData(en, request);
//		setCurrEn(en);
		// 小周鹏修改，没有No, PK 重新设置 Start
//		if(EnsName.contains("BP.WF.Template") && !EnsName.equals("BP.WF.Template.Selector") && !EnsName.endsWith("BP.WF.Template.FlowSorts")){
		if(en.getPKVal().toString().equals("") && !StringHelper.isNullOrEmpty(pk_value)){
			en.setPKVal(pk_value);
		}	
		
//		}
		// 小周鹏修改，没有No, PK 重新设置 End
		
		en.Save();
		
		setPK_Val(en.getPKVal().toString());

		// #region 保存 实体附件
		try {
			if (en.getEnMap().getAttrs().Contains("MyFileName")) {
				UiFatory uf = new UiFatory();
				TextBox file = (TextBox) uf.GetUIByID("file");

				// HtmlInputFile file = this.UCEn1.FindControl("file") as
				// HtmlInputFile;
				if (file != null && file.getText().indexOf(".") != -1) {
					BP.Sys.EnCfg cfg = new BP.Sys.EnCfg(en.toString());
					File f = new File(cfg.getFJSavePath());
					if (f.exists() == false)
						f.mkdirs();

					/* 如果包含这二个字段。 */
					String fileName = f.getName();
					fileName = fileName
							.substring(fileName.lastIndexOf("/") + 1);

					String filePath = cfg.getFJSavePath();
					en.SetValByKey("MyFilePath", filePath);

					String ext = "";
					if (fileName.indexOf(".") != -1)
						ext = fileName.substring(fileName.lastIndexOf(".") + 1);

					en.SetValByKey("MyFileExt", ext);
					en.SetValByKey("MyFileName", fileName);
					en.SetValByKey("WebPath",
							cfg.getFJWebPath() + en.getPKVal() + "." + ext);

					String fullFile = filePath + "/" + en.getPKVal() + "."
							+ ext;

					// file.PostedFile.SaveAs(fullFile);
					// file.PostedFile.InputStream.Close();
					// file.PostedFile.InputStream.Dispose();
					// file.Dispose();

					File info = new File(fullFile);
					en.SetValByKey("MyFileSize",
							BP.DA.DataType.PraseToMB(info.length()));
					// if (DataType.IsImgExt(ext))
					// {
					// System.Drawing.Image img =
					// System.Drawing.Image.FromFile(fullFile);
					// en.SetValByKey("MyFileH", img.Height);
					// en.SetValByKey("MyFileW", img.Width);
					// img.Dispose();
					// }
					en.Update();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			// System.out.println("保存附件出现错误：" + ex.getMessage());
			PubClass.Alert("保存附件出现错误：" + ex.getMessage(),
					ContextHolderUtils.getResponse());
			return;
		}

		// #region 保存 属性 附件
		try {
			AttrFiles fils = en.getEnMap().getHisAttrFiles();
			SysFileManagers sfs = new SysFileManagers(en.toString(), en
					.getPKVal().toString());
			for (AttrFile fl : fils) {
				// TextBox file = (TextBox) controls.get("F" + fl.FileNo);
				// if (file.getText().contains(".") == false)
				// continue;
				//
				// SysFileManager enFile =
				// (SysFileManager)sfs.GetEntityByKey(SysFileManagerAttr.AttrFileNo,
				// fl.FileNo);
				// SysFileManager enN = null;
				// if (enFile == null)
				// {
				// enN = this.FileSave(null, file, en);
				// }
				// else
				// {
				// enFile.Delete();
				// enN = this.FileSave(null, file, en);
				// }
				//
				// enN.AttrFileNo = fl.FileNo;
				// enN.AttrFileName = fl.FileName;
				// enN.EnName = en.ToString();
				// enN.Update();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			PubClass.Alert("保存附件出现错误：" + ex.getMessage(),
					ContextHolderUtils.getResponse());
			return;
			// System.out.println("保存附件出现错误：" + ex.getMessage());
		}

		return;
	}

	// / <summary>
	// / 获取隐藏域中保存的当前打开标签值
	// / </summary>
	// / <returns></returns>
	private String GetHiddenTabTitle() {
		String hiddenField = ContextHolderUtils.getRequest().getParameter(
				"Hid_CurrentTab");

		return StringHelper.isEmpty(hiddenField, "");
	}

	@RequestMapping(value = "/saveorclose", method = RequestMethod.POST)
	public void saveOrClose(String EnsName, HttpServletRequest request,
			HttpServletResponse response)  {
		try {
			this.doSave(EnsName, request);
			BaseModel.WinClose();//.winClose(response);
		} catch (Exception e) {
		}
	}

	public Entities getGetEns(String EnsName, HttpServletRequest request) {
		return ClassFactory.GetEns(EnsName);
	}

	public String getEnName(String EnsName) {
		String enName = "";
		if (!StringHelper.isNullOrEmpty(EnsName)) {
			Entities ens = BP.En.ClassFactory.GetEns(EnsName);
			enName = ens.getGetNewEntity().toString();
		}
		return enName;
	}

	public Entity GetEnData(Entity en, HttpServletRequest request)
			throws Exception {
		
		String key = "";
		try {
			Attrs attrs = en.getEnMap().getAttrs();
			for (Attr attr : attrs) {
				if (attr.getMyFieldType() == FieldType.RefText)
					continue;

				if ("MyNum".equals(attr.getKey()))
					continue;

				if (attr.getUIVisible() == false)
					continue;

				key = attr.getKey();

				UIContralType ut = attr.getUIContralType();
				if (ut == UIContralType.TB) {
					if (attr.getUIVisible()) {
						UiFatory uf = new UiFatory();
						if (attr.getUIHeight() == 0) {
							if(!attr.getUIIsReadonly()){
								en.SetValByKey(attr.getKey(),request.getParameter("TB_" + attr.getKey()));
							}
							
							continue;
						} else {
							if (uf.GetUIByID("TB_" + attr.getKey()) == null) {
								// TextBox tb=(TextBox)uf.GetUIByID("TB_" +
								// attr.getKey());
								en.SetValByKey(attr.getKey(),request.getParameter("TB_" + attr.getKey()));
								continue;
							}

							if (uf.GetUIByID("TBH_" + attr.getKey()) == null) {
								en.SetValByKey(attr.getKey(),request.getParameter("TBH_"+ attr.getKey()));
								continue;
							}

							if (uf.GetUIByID("TBF_" + attr.getKey()) == null) {
								// FredCK.FCKeditorV2.FCKeditor fck =
								// (FredCK.FCKeditorV2.FCKeditor)this.FindControl("TB_"
								// + attr.Key);
								// en.SetValByKey(attr.Key, fck.Value);
								continue;
							}
						}
					} else {
						en.SetValByKey(attr.getKey(),
								request.getParameter("TB_" + attr.getKey()));
					}
				} else if (ut == UIContralType.DDL) {
					try {
						en.SetValByKey(attr.getKey(),
								request.getParameter("DDL_" + attr.getKey()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (ut == UIContralType.CheckBok) {
					en.SetValByKey(attr.getKey(),
							request.getParameter("CB_" + attr.getKey()));
				} else {

				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("GetEnData error :" + ex.getMessage()
					+ " key = " + key);
		}
		return en;
	}
	
	public String getParamter(String key, HttpServletRequest request){
		String value = request.getParameter(key);
		if(StringHelper.isNullOrEmpty(value))
			return "";
		return value;
	}

	public final String getPKVal(HttpServletRequest request) {
		PK_Val = getParamter("PK", request);

		if (StringHelper.isNullOrEmpty(PK_Val)) {
			PK_Val = getParamter("OID", request);
		}

		if (StringHelper.isNullOrEmpty(PK_Val)) {
			PK_Val = getParamter("No", request);
		}
		
		if (StringHelper.isNullOrEmpty(PK_Val)) {
			PK_Val = getParamter("NodeID", request);
		}

		if (StringHelper.isNullOrEmpty(PK_Val)) {
			PK_Val = getParamter("MyPK", request);
		}

		return PK_Val;
	}

	@RequestMapping(value = "/SaveAndNew", method = RequestMethod.POST)
	public ModelAndView onSaveAndNew(HttpServletRequest request, HttpServletResponse response, String EnsName) {
		try {
			this.doSave(EnsName, request);
			response.sendRedirect(Glo.getCCFlowAppPath()+"WF/Comm/RefFunc/UIEn.jsp?EnsName=" + EnsName + "&EnName="+ this.getEnName(EnsName));
		} catch (Exception ex) {
			ex.printStackTrace();
			PubClass.Alert(ex.getMessage(), ContextHolderUtils.getResponse());
			// this.ResponseWriteBlueMsg(ex.Message);
		}
		return null;
	}

	@RequestMapping(value = "/Delete", method = RequestMethod.POST)
	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
		try {
			Entity en = GetEnData(this.getGetEns(request).getGetNewEntity(),
					request);
			if (this.getPKVal(request) != null)
				en.setPKVal(getPKVal(request));
			int i = en.Delete();
			if (i == 1) {
				BaseModel.WinClose();//.winCloseWithMsg(response, "删除成功!!!");
			} else {
				BaseModel.WinClose();//winCloseWithMsg(response, "删除失败!!!");
			}
			// this.Alert("删除成功");

			// this.ToMsgPage("删除成功!!!");
			return null;
		} catch (Exception ex) {
			// PubClass.Alert("删除成功!!!", ContextHolderUtils.getResponse());
			BaseModel.ToErrorPage("删除期间出现错误: \t\n" + ex.getMessage());
			// this.ToMsgPage("删除成功!!!");
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 当前的实体集合．
	 */
	public final Entities getGetEns(HttpServletRequest request) {
		if (_GetEns == null) {
			String enName = request.getParameter("EnName");//this.getEnName();
			if (StringHelper.isNullOrEmpty(enName)) {
				String ensName = request.getParameter("EnsName");
				if (!StringHelper.isNullOrEmpty(ensName)) {
					Entities ens = BP.En.ClassFactory.GetEns(ensName);
					enName = ens.getGetNewEntity().toString();
				}
			}
			if (enName != null) {
				if (enName.contains(".")) {
					Entity en = BP.En.ClassFactory.GetEn(enName);
					_GetEns = en.getGetNewEntities();
				} else {
					_GetEns = BP.En.ClassFactory.GetEns(enName);
				}
			} else {
				_GetEns = BP.En.ClassFactory.GetEns(request.getParameter("EnsName"));
			}
		}
		return _GetEns;
	}

	public final Entity getGetEnDa(HttpServletRequest request) {
		Entity en = this.getGetEns(request).getGetNewEntity();
		Attrs myattrs1 = en.getEnMap().getAttrs();
		for (Attr attr : myattrs1) {
			if (getParamter(attr.getKey(),request) == null) {
				continue;
			}
			en.SetValByKey(attr.getKey(), getParamter(attr.getKey(),request));
		}
		if (en.getPKCount() == 1) {
			Object pk = getParamter("PK",request);
			if (pk != null) {
				
			} else {
				if (StringHelper.isNullOrEmpty(getParamter(en.getPK(),request))) {
					return en;
				} else {
					en.setPKVal(getParamter(en.getPK(),request));
				}
			}
			if (en.getIsExits() == false) {
				throw new RuntimeException("@记录不存在,或者没有保存.");
			} else {
				int i = en.RetrieveFromDBSources();
				if (i == 0) {
					en.RetrieveFromDBSources();
				}
			}
			Attrs myattrs = en.getEnMap().getAttrs();
			for (Attr attr : myattrs) {
				if (StringHelper.isNullOrEmpty(getParamter(attr.getKey(),request))) {
					continue;
				}
				en.SetValByKey(attr.getKey(), getParamter(attr.getKey(),request));
			}
			return en;
		} else if (en.getIsMIDEntity()) {
			Object val = getParamter("MID",request);
			if (val == null) {
				val = getParamter("PK",request);
			}
			if (val == null) {
				return en;
			} else {
				en.SetValByKey("MID", val);
				en.RetrieveFromDBSources();
				return en;
			}
		}

		Attrs attrs = en.getEnMap().getAttrs();
		for (Attr attr : attrs) {
			if (attr.getIsPK()) {
				String str = getParamter(attr.getKey(),request);
				if (str == null) {
					if (en.getIsMIDEntity()) {
						en.SetValByKey("MID", getParamter("PK",request));
						continue;
					} else {
						throw new RuntimeException("@没有把主键值[" + attr.getKey()
								+ "]传输过来.");
					}
				}
			}
			if (getParamter(attr.getKey(),request) != null) {
				en.SetValByKey(attr.getKey(), getParamter(attr.getKey(),request));
			}
		}

		if (en.getIsExits() == false) {
			throw new RuntimeException("@数据没有记录.");
		} else {
			en.RetrieveFromDBSources();
			Attrs myattrs = en.getEnMap().getAttrs();
			for (Attr attr : myattrs) {
				if (getParamter(attr.getKey(),request) == null) {
					continue;
				}
				en.SetValByKey(attr.getKey(), getParamter(attr.getKey(),request));
			}
		}
		return en;
	}
}

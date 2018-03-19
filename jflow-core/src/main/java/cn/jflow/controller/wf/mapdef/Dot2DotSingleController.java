package cn.jflow.controller.wf.mapdef;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import BP.En.AttrOfOneVSM;
import BP.En.ClassFactory;
import BP.En.EnType;
import BP.En.Entities;
import BP.En.Entity;
import cn.jflow.common.model.TempObject;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.HtmlUtils;
import cn.jflow.system.ui.core.NamesOfBtn;

@Controller
@RequestMapping("/WF/MapDef")
public class Dot2DotSingleController {
	@ResponseBody
	@RequestMapping(value = "/getStr", method = RequestMethod.POST)
	public String BPToolBar1_ButtonClick(TempObject object,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		String btnId = request.getParameter("btnId");
		int DDL_Group = request.getParameter("DDL_Group")==null?0:Integer.parseInt(request.getParameter("DDL_Group"));
		boolean IsTreeShowWay = request.getParameter("IsTreeShowWay") == null ? false
				: true;
		// var btn = (LinkBtn) sender;
		if (btnId != null) {
			if (btnId.equals(NamesOfBtn.SelectNone)) {

			}
			if (btnId.equals(NamesOfBtn.SelectAll)) {

			}
			if (btnId.equals("Btn_Save")) {
				if (IsTreeShowWay) {
					AttrOfOneVSM attr = null;
					Entity en = ClassFactory.GetEn(request
							.getParameter("EnName"));
					for (AttrOfOneVSM attr1 : en.getEnMap().getAttrsOfOneVSM()) {
						if (attr1.getEnsOfMM().toString()
								.equals(request.getParameter("AttrKey"))) {
							attr = attr1;
						}
					}
					// AttrOfOneVSM attr = this.getAttrOfOneVSM();
					Entities ensOfMM = attr.getEnsOfMM();
					ensOfMM.Delete(attr.getAttrOfOneInMM(),
							request.getParameter("PK")); // 删除已经保存的数据。
					AttrOfOneVSM attrOM = null;
					Entity en1 = ClassFactory.GetEn(request
							.getParameter("EnName"));
					for (AttrOfOneVSM attr1 : en1.getEnMap().getAttrsOfOneVSM()) {
						if (attr.getEnsOfMM().toString()
								.equals(request.getParameter("AttrKey"))) {
							attrOM = attr1;
						}
					}
					// AttrOfOneVSM attrOM = this.getAttrOfOneVSM();
					Entities ensOfM = attrOM.getEnsOfM();
					ensOfM.RetrieveAll();

					Entity enP = ClassFactory.GetEn(request
							.getParameter("EnsName"));// this.Request.QueryString["EnsName"]);
					if (enP.getEnMap().getEnType() != EnType.View) {
						enP.SetValByKey(enP.getPK(), request.getParameter("PK"));// =this.PK;
						enP.Retrieve(); // 查询。
						enP.Update(); // 执行更新，处理写在 父实体 的业务逻辑。
					}
				} else {
					// Save();
					AttrOfOneVSM attr = null;// this.getAttrOfOneVSM();
					Entity en = ClassFactory.GetEn(request
							.getParameter("EnName"));
					for (AttrOfOneVSM attr1 : en.getEnMap().getAttrsOfOneVSM()) {
						if (attr1.getEnsOfMM().toString()
								.equals(request.getParameter("AttrKey"))) {
							attr = attr1;
						}
					}
					Entities ensOfMM = attr.getEnsOfMM();
					ensOfMM.Delete(attr.getAttrOfOneInMM(),
							request.getParameter("PK"));

					// 执行保存.
					// edited by liuxc,2015.1.6
					// 增加去除相同项的逻辑，比如同一个人员属于多个部门，则保存的时候则可能会选中有多个相同选择项
					List<String> keys = new ArrayList<String>();
					String key = "";
					HashMap<String, BaseWebControl> controls = HtmlUtils
							.httpParser(object.getFormHtml(), true);
					for (Map.Entry<String, BaseWebControl> entry : controls
							.entrySet())// this.UCSys1.Controls)
					{
						String id = entry.getKey();
						String checkId=request.getParameter(id);
						if (entry == null || id == null)
							continue;

						if (id.contains("CB_") == false)
							continue;

						if(checkId==null)
//						CheckBox cb = (CheckBox) entry;
//						if (cb == null)
//							continue;
//
//						if (cb.getChecked() == false)
							continue;

						//key = id.split("_")[1];// split('_',1);
						key = id.replace("CB_", "");
						if("BP.WF.Template.NodeEmps".equals(request.getParameter("AttrKey"))){
							if(!"None".equals(request.getParameter("ShowWay")))
							{
								key = key.substring(0,key.lastIndexOf("_"));
							}
						}
						
						if ("EN".equals(key) || "SE".equals(key) || keys.contains(key))
							continue;

						Entity en1 = ensOfMM.getGetNewEntity();
						en1.SetValByKey(attr.getAttrOfOneInMM(),
								request.getParameter("PK"));
						en1.SetValByKey(attr.getAttrOfMInMM(), key);
						en1.Insert();

						keys.add(key);
					}

					// 更新entity ,防止有业务逻辑出现.
					String msg = "";
					Entity enP = ClassFactory.GetEn(request
							.getParameter("EnName"));
					if (enP.getEnMap().getEnType() != EnType.View) {
						enP.SetValByKey(enP.getPK(), request.getParameter("PK"));// =this.PK;
						enP.Retrieve(); // 查询。
						try {
							enP.Update(); // 执行更新，处理写在 父实体 的业务逻辑。
						} catch (Exception ex) {
							msg += "执行更新错误：" + enP.getEnDesc() + " "
									+ ex.getMessage();
						}
					}
				}
				String str = request.getRequestURI();
				if (str.contains("ShowWay="))
					mv.addObject("ShowWay", DDL_Group);
				if (str.contains("num="))
					mv.addObject("num", DDL_Group);
				// str = str.replace("&ShowWay=", "&1=");
				// this.Response.Redirect(str + "&ShowWay=" +
				// this.DDL_Group.SelectedItem.Value, true);
				mv.addObject("PK", request.getParameter("PK"));
				mv.addObject("EnName", request.getParameter("EnName"));
				mv.addObject("AttrKey", request.getParameter("AttrKey"));
				mv.addObject("result", "保存成功");
				mv.setViewName("Comm/RefFunc/Dot2DotSingle");
				return mv.toString();
			}
			if (btnId.equals(NamesOfBtn.SaveAndClose)) {
				if (IsTreeShowWay) {
					AttrOfOneVSM attr = null;
					Entity en = ClassFactory.GetEn(request
							.getParameter("EnName"));
					for (AttrOfOneVSM attr1 : en.getEnMap().getAttrsOfOneVSM()) {
						if (attr1.getEnsOfMM().toString()
								.equals(request.getParameter("AttrKey"))) {
							attr = attr1;
						}
					}
					// AttrOfOneVSM attr = this.getAttrOfOneVSM();
					Entities ensOfMM = attr.getEnsOfMM();
					ensOfMM.Delete(attr.getAttrOfOneInMM(),
							request.getParameter("PK")); // 删除已经保存的数据。
					AttrOfOneVSM attrOM = null;
					Entity en1 = ClassFactory.GetEn(request
							.getParameter("EnName"));
					for (AttrOfOneVSM attr1 : en1.getEnMap().getAttrsOfOneVSM()) {
						if (attr.getEnsOfMM().toString()
								.equals(request.getParameter("AttrKey"))) {
							attrOM = attr1;
						}
					}
					// AttrOfOneVSM attrOM = this.getAttrOfOneVSM();
					Entities ensOfM = attrOM.getEnsOfM();
					ensOfM.RetrieveAll();

					Entity enP = ClassFactory.GetEn(request
							.getParameter("EnsName"));// this.Request.QueryString["EnsName"]);
					if (enP.getEnMap().getEnType() != EnType.View) {
						enP.SetValByKey(enP.getPK(), request.getParameter("PK"));// =this.PK;
						enP.Retrieve(); // 查询。
						enP.Update(); // 执行更新，处理写在 父实体 的业务逻辑。
					}
				} else {
					// Save();
					AttrOfOneVSM attr = null;// this.getAttrOfOneVSM();
					Entity en = ClassFactory.GetEn(request
							.getParameter("EnName"));
					for (AttrOfOneVSM attr1 : en.getEnMap().getAttrsOfOneVSM()) {
						if (attr1.getEnsOfMM().toString()
								.equals(request.getParameter("AttrKey"))) {
							attr = attr1;
						}
					}
					Entities ensOfMM = attr.getEnsOfMM();
					ensOfMM.Delete(attr.getAttrOfOneInMM(),
							request.getParameter("PK"));

					// 执行保存.
					// edited by liuxc,2015.1.6
					// 增加去除相同项的逻辑，比如同一个人员属于多个部门，则保存的时候则可能会选中有多个相同选择项
					List<String> keys = new ArrayList<String>();
					String key = "";
					HashMap<String, BaseWebControl> controls = HtmlUtils
							.httpParser(object.getFormHtml(), true);
					for (Map.Entry<String, BaseWebControl> entry : controls
							.entrySet())// this.UCSys1.Controls)
					{
						String id = entry.getKey();
						if (entry == null || id == null)
							continue;

						if (id.contains("CB_") == false)
							continue;

						CheckBox cb = (CheckBox) entry;
						if (cb == null)
							continue;

						if (cb.getChecked() == false)
							continue;

						//key = id.split("_")[1];// split('_',1);
						key = id.replace("CB_", "");
						if("BP.WF.Template.NodeEmps".equals(request.getParameter("AttrKey"))){
							key = key.substring(0,key.lastIndexOf("_"));
						}

						if ("EN".equals(key) || "SE".equals(key) || keys.contains(key))
							continue;

						Entity en1 = ensOfMM.getGetNewEntity();
						en1.SetValByKey(attr.getAttrOfOneInMM(),
								request.getParameter("PK"));
						en1.SetValByKey(attr.getAttrOfMInMM(), key);
						en1.Insert();

						keys.add(key);
					}

					// 更新entity ,防止有业务逻辑出现.
					String msg = "";
					Entity enP = ClassFactory.GetEn(request
							.getParameter("EnName"));
					if (enP.getEnMap().getEnType() != EnType.View) {
						enP.SetValByKey(enP.getPK(), request.getParameter("PK"));// =this.PK;
						enP.Retrieve(); // 查询。
						try {
							enP.Update(); // 执行更新，处理写在 父实体 的业务逻辑。
						} catch (Exception ex) {
							msg += "执行更新错误：" + enP.getEnDesc() + " "
									+ ex.getMessage();
						}
					}
					// this.WinClose();
				}
			}
			if (btnId.equals(NamesOfBtn.Close)) {
				// this.WinClose();
			}

			if (btnId.equals("Btn_EditMEns")) {
				AttrOfOneVSM attr = null;
				Entity en = ClassFactory.GetEn(request.getParameter("EnName"));
				for (AttrOfOneVSM attr1 : en.getEnMap().getAttrsOfOneVSM()) {
					if (attr1.getEnsOfMM().toString()
							.equals(request.getParameter("AttrKey"))) {
						attr = attr1;
					}
				}
				mv.addObject("EnsName", attr.getEnsOfM().toString());
				mv.setViewName("Comm/UIEn");
				// this.WinOpen(this.Request.ApplicationPath +
				// "/Comm/UIEns.aspx?EnsName="
				// + this.AttrOfOneVSM.EnsOfM.ToString());
				// this.EditMEns();
				return mv.toString();
			}
		}
		mv.addObject("PK", request.getParameter("PK"));
		mv.addObject("EnName", request.getParameter("EnName"));
		mv.addObject("AttrKey", request.getParameter("AttrKey"));
		mv.addObject("result", "保存成功");
		mv.setViewName("Comm/RefFunc/Dot2DotSingle");
		return mv.toString();
		// switch (btn.ID)
		// {
		// case NamesOfBtn.SelectNone:
		// //this.CBL1.SelectNone();
		// break;
		// case NamesOfBtn.SelectAll:
		// //this.CBL1.SelectAll();
		// break;
		// case NamesOfBtn.Save:
		// if (this.IsTreeShowWay)
		// SaveTree();
		// else
		// Save();
		//
		// string str = this.Request.RawUrl;
		// if (str.Contains("ShowWay="))
		// str = str.Replace("&ShowWay=", "&1=");
		// this.Response.Redirect(str + "&ShowWay=" +
		// this.DDL_Group.SelectedItem.Value, true);
		// return;
		// case "Btn_SaveAndClose":
		// if (this.IsTreeShowWay)
		// SaveTree();
		// else
		// Save();
		// this.WinClose();
		// break;
		// case "Btn_Close":
		// this.WinClose();
		// break;
		// case "Btn_EditMEns":
		// this.EditMEns();
		// break;
		// default:
		// throw new Exception("@没有找到" + btn.ID);
	}

	// #endregion 方法
	//
	// #region 操作
	// public void EditMEns() {
	// this.WinOpen(this.Request.ApplicationPath + "/Comm/UIEns.aspx?EnsName="
	// + this.AttrOfOneVSM.EnsOfM.ToString());
	// }

	// public void Save() {
	// AttrOfOneVSM attr = this.getAttrOfOneVSM();
	// Entities ensOfMM = attr.getEnsOfMM();
	// ensOfMM.Delete(attr.getAttrOfOneInMM(), this.getPK());
	//
	// // 执行保存.
	// // edited by liuxc,2015.1.6
	// // 增加去除相同项的逻辑，比如同一个人员属于多个部门，则保存的时候则可能会选中有多个相同选择项
	// List<String> keys = new ArrayList<String>();
	// String key = "";
	// for (Control ctl : this.UCSys1.Controls) {
	// if (ctl == null || ctl.ID == null)
	// continue;
	//
	// if (ctl.ID.Contains("CB_") == false)
	// continue;
	//
	// CheckBox cb = (CheckBox) ctl;
	// if (cb == null)
	// continue;
	//
	// if (cb.getChecked() == false)
	// continue;
	//
	// key = ctl.ID.Split('_')[1];
	//
	// if (key == "EN" || key == "SE" || keys.contains(key))
	// continue;
	//
	// Entity en1 = ensOfMM.getGetNewEntity();
	// en1.SetValByKey(attr.getAttrOfOneInMM(), this.getPK());
	// en1.SetValByKey(attr.getAttrOfMInMM(), key);
	// en1.Insert();
	//
	// keys.add(key);
	// }
	//
	// // 更新entity ,防止有业务逻辑出现.
	// String msg = "";
	// Entity enP = ClassFactory.GetEn(this.getEnName());
	// if (enP.getEnMap().getEnType() != EnType.View) {
	// enP.SetValByKey(enP.getPK(), this.getPK());// =this.PK;
	// enP.Retrieve(); // 查询。
	// try {
	// enP.Update(); // 执行更新，处理写在 父实体 的业务逻辑。
	// } catch (Exception ex) {
	// msg += "执行更新错误：" + enP.getEnDesc() + " " + ex.getMessage();
	// }
	// }
	// }
	//
	// public void SaveTree() {
	// AttrOfOneVSM attr = this.getAttrOfOneVSM();
	// Entities ensOfMM = attr.getEnsOfMM();
	// ensOfMM.Delete(attr.getAttrOfOneInMM(), this.getPK()); // 删除已经保存的数据。
	//
	// AttrOfOneVSM attrOM = this.getAttrOfOneVSM();
	// Entities ensOfM = attrOM.getEnsOfM();
	// ensOfM.RetrieveAll();
	//
	// Entity enP = ClassFactory.GetEn(req.getParameter("EnsName"));//
	// this.Request.QueryString["EnsName"]);
	// if (enP.getEnMap().getEnType() != EnType.View) {
	// enP.SetValByKey(enP.getPK(), this.getPK());// =this.PK;
	// enP.Retrieve(); // 查询。
	// enP.Update(); // 执行更新，处理写在 父实体 的业务逻辑。
	// }
	// }
	@RequestMapping(value = "/reload", method = RequestMethod.POST)
	public void BPToolBar1_ButtonClick(HttpServletRequest request, HttpServletResponse response,String url) {
		//String str = request.getRequestURI();//this.Request.RawUrl;
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

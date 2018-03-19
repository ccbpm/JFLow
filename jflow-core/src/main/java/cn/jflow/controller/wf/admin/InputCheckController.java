package cn.jflow.controller.wf.admin;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExts;
import cn.jflow.controller.wf.workopt.BaseController;
import cn.jflow.system.ui.core.ListBox;
import cn.jflow.system.ui.core.ListItem;

@Controller
@RequestMapping("/wf/InputCheck")
public class InputCheckController extends BaseController {

	@RequestMapping(value = "/btn_save", method = RequestMethod.POST)
	@ResponseBody
	public String btn_SaveInputCheck_Click(HttpServletRequest request, HttpServletResponse response) {//Object sender, EventArgs e
		//Object tempVar = null;//// = this.Pub1.append(FindControl("LB1"));
		//ListBox lb = (ListBox) ((tempVar instanceof ListBox) ? tempVar : null);
		
		String[] LB1 = this.getRequest().getParameter("LB1").split(",");

		// 检查路径. 没有就创建它。
		String pathDir = BP.Sys.SystemConfig.getPathOfDataUser() + "JSLibData/";
		File file = new File(pathDir);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdir();
		}

		// 删除已经存在的数据.
		MapExt me = new MapExt();
		me.Retrieve(MapExtAttr.FK_MapData, this.getRequest().getParameter("FK_MapData"), MapExtAttr.ExtType, this.getRequest().getParameter("ExtType"), MapExtAttr.AttrOfOper, this.getRequest().getParameter("OperAttrKey"));

		for (String li : LB1) {
			/*if (li.getSelected() == false) {
				continue;
			}*/

			////me = (MapExt) this.Pub1.append(Copy(me));
			me.setExtType(this.getRequest().getParameter("ExtType")+"");

			// 操作的属性.
			me.setAttrOfOper(this.getRequest().getParameter("OperAttrKey")+"");

			int doWay = 0;
			boolean RB_0 = "RB_0".equals(this.getRequest().getParameter("RB_0C"))||"".equals(this.getRequest().getParameter("RB_0C"))||null==this.getRequest().getParameter("RB_0C");
			if (RB_0 == false) {////this.Pub1.append(GetRadioButtonByID("RB_0").Checked
				doWay = 1;
			}
			/*String path = BP.Sys.SystemConfig.getPathOfData() + "\\JSLib\\";
			//// = this.Pub1.append(GetRadioButtonByID("RB_0"));
			if (RB_0 == false) {
				path = BP.Sys.SystemConfig.getPathOfDataUser() + "\\JSLib\\";
			}*/
			me.setDoWay(doWay);
			me.setDoc(BP.DA.DataType.ReadTextFile(li));
			File info = new File(li);
			//System.out.println("info.getPath() "+info.getParent());
			//System.out.println("info.getName() "+info.getName());
			//System.out.println("info.getName().lastIndexOf(.) + 1:   "+info.getName().lastIndexOf(".") + 1);
			if(info.getParent().lastIndexOf("/")<0)
				me.setTag2(info.getParent().substring(info.getParent().lastIndexOf("\\") + 1)); // #134 表单字段的脚本验证不能使用
			else
				me.setTag2(info.getParent().substring(info.getParent().lastIndexOf("/") + 1)); // #134 表单字段的脚本验证不能使用
			//获取函数的名称.
			try{
				String func = me.getDoc();
				func = me.getDoc().substring(func.indexOf("function") + 8);
				func = func.substring(0, func.indexOf("("));
				me.setTag1(func.trim());
			}catch (java.lang.Exception e3){
				continue;
			}
			

			// 检查路径,没有就创建它.
			File fi = new File(li);
			me.setTag(li);
			me.setFK_MapData(this.getRequest().getParameter("FK_MapData")+"");
			me.setExtType(this.getRequest().getParameter("ExtType")+"");
			me.setMyPK(this.getRequest().getParameter("FK_MapData")+""+ "_" + me.getExtType() + "_" + me.getAttrOfOper() + "_" + me.getTag1());
			try {
				me.Insert();
			} catch (java.lang.Exception e1) {
				me.Update();
			}
		}

		///#region 把所有的js 文件放在一个文件里面。
		MapExts mes = new MapExts();
		mes.Retrieve(MapExtAttr.FK_MapData, this.getRequest().getParameter("FK_MapData")+"", MapExtAttr.ExtType, this.getRequest().getParameter("ExtType")+"");

		String js = "";
		for (MapExt me1 : mes.ToJavaList()) {
			js += "\r\n" + BP.DA.DataType.ReadTextFile(me1.getTag());
		}

		File file2 = new File(pathDir + "/" + this.getRequest().getParameter("FK_MapData")+"" + ".js");
		if (!file2.exists() && !file2.isDirectory()) {
			file2.delete();//(pathDir + "\\" + this.getFK_MapData() + ".js");
		}

		BP.DA.DataType.WriteFile(pathDir + "/" + this.getRequest().getParameter("FK_MapData")+"" + ".js", js);
		///#endregion 把所有的js 文件放在一个文件里面。

		/*try {
			response.sendRedirect("InputCheck.jsp?FK_MapData=" + this.getRequest().getParameter("FK_MapData")+"" + "&ExtType=" + this.getRequest().getParameter("ExtType")+"" + "&RefNo=" + this.getRefNo());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return "success";
	}
}

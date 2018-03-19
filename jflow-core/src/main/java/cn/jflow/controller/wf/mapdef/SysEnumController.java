package cn.jflow.controller.wf.mapdef;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.En.FieldTypeS;
import BP.En.QueryObject;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.SysEnum;
import BP.Sys.SysEnumAttr;
import BP.Sys.SysEnumMain;
import BP.Sys.SysEnums;
import BP.Web.WebUser;
import cn.jflow.common.model.BaseModel;
import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.HtmlUtils;


@Controller
@RequestMapping("/WF/MapDef")
public class SysEnumController extends BaseController{
	@RequestMapping(value = "/btn_Add_Click1", method = RequestMethod.POST)
	public void btn_Add_Click(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			response.sendRedirect("Do.jsp?DoType=AddEnum&MyPK=" + this.getMyPK() + "&IDX=" + object.getIDX() + "&EnumKey=" + this.getRefNo());
			this.winClose(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
		
	}
	
	@RequestMapping(value = "/btn_Save_Click2", method = RequestMethod.POST)
	public void btn_Save_Click(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(object.getFormHtml(), request);
		try
		{
			SysEnumMain main = new SysEnumMain();
			if (this.getRefNo() == null)
			{
				main.setNo(request.getParameter("TB_No"));
				if (main.getIsExits())
				{
					//this.Alert("编号（枚举英文名称）[" + main.No + "]已经存在。");
					try {
						this.printAlert(response, "编号（枚举英文名称）[" + main.getNo() + "]已经存在。");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}

				SysEnum se = new SysEnum();
				if (se.IsExit(SysEnumAttr.EnumKey, main.getNo()) == true)
				{
					try {
						this.printAlert(response, "编号（枚举英文名称）[" + main.getNo() + "]已经存在。");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}

				main = (SysEnumMain)BaseModel.Copy(request, main, null,main.getEnMap(), controls);
				if (main.getNo().length() == 0 || main.getName().length() == 0)
				{
					throw new RuntimeException("编号与名称不能为空");
				}
			}
			else
			{
				main.setNo(this.getRefNo());
				main.Retrieve();
				main = (SysEnumMain)BaseModel.Copy(request, main, null,main.getEnMap(), controls);
				if (main.getNo().length() == 0 || main.getName().length() == 0)
				{
					throw new RuntimeException("编号与名称不能为空");
				}
			}

			String cfgVal = "";
			int idx = -1;
			while (idx < 19)
			{
				idx++;
				String t = request.getParameter("TB_" + idx);
				if (t.length() == 0)
				{
					continue;
				}

				cfgVal += "@" + idx + "=" + t;
			}

			main.setCfgVal(cfgVal);
			if (main.getCfgVal().equals(""))
			{
				throw new RuntimeException("错误：您必须输入枚举值，请参考帮助。"); //错误：您必须输入枚举值，请参考帮助。
			}

			main.Save();

			//重新生成
			SysEnums se1s = new SysEnums();
			se1s.Delete(SysEnumAttr.EnumKey, main.getNo());
			SysEnums ses = new SysEnums();
			ses.RegIt(main.getNo(), cfgVal);

			String keyApp = "EnumOf" + main.getNo() + WebUser.getSysLang();
			BP.DA.Cash.DelObjFormApplication(keyApp);

//			if (this.getMyPK() != null)
//			{
				try {
					response.sendRedirect("SysEnum.jsp?RefNo=" + main.getNo() + "&MyPK=" + this.getMyPK() + "&IDX=" + object.getIDX());
				} catch (IOException e) {
					e.printStackTrace();
				}
//			}
			return;
		}
		catch (RuntimeException ex)
		{
			try {
				this.wirteMsg(response,ex.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
			//this.ToErrorPage(ex.Message);
			//this.Alert(ex.Message);
		}

	}
	@RequestMapping(value = "/btn_Del_Click1", method = RequestMethod.POST)
	public void btn_Del_Click(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		try
		{
			// 检查这个类型是否被使用？
			MapAttrs attrs = new MapAttrs();
			QueryObject qo = new QueryObject(attrs);
			qo.AddWhere(MapAttrAttr.MyDataType, FieldTypeS.Enum.getValue());
			qo.addAnd();
			qo.AddWhere(MapAttrAttr.KeyOfEn, this.getRefNo());
			int i = qo.DoQuery();
			if (i == 0)
			{
				BP.Sys.SysEnums ses = new SysEnums();
				ses.Delete(BP.Sys.SysEnumAttr.EnumKey, this.getRefNo());

				BP.Sys.SysEnumMain m = new SysEnumMain();
				m.setNo(this.getRefNo());
				m.Delete();
				try {
					this.printAlert(response,"删除成功");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}

			String msg = "错误:下列数据已经引用了枚举您不能删除它。"; // "错误:下列数据已经引用了枚举您不能删除它。";
			for (MapAttr attr : attrs.ToJavaList())
			{
				msg += "\t\n" + attr.getField() + "" + attr.getName() + " Table = " + attr.getFK_MapData();
			}
			return;
		}
		catch (RuntimeException ex)
		{
//			this.ToErrorPage(ex.getMessage());
			try {
				this.printAlert(response, ex.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}

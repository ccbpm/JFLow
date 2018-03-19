package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.model.designer.UCEnModel;
import cn.jflow.system.ui.core.Button;
import BP.En.ClassFactory;
import BP.En.Method;
import BP.Tools.StringHelper;


public class MethodModel extends UCEnModel{
	
	

	public MethodModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	public void loadPage()
	{
		String ensName = getParameter("M");
		Method rm = ClassFactory.GetMethod(ensName);
		if (rm == null)
		{
			ToErrorPage("@方法名错误或者该方法已经不存在:"+ensName);
//			throw new RuntimeException("@方法名错误或者该方法已经不存在:"+ensName);
			return;
		}
		this.Bind(rm);
	}
	public final void Bind(Method rm)
	{
		pub.append(AddFieldSet("<b>功能执行:" + rm.Title + "</b>"));
		pub.append(AddBR());
		pub.append(rm.Help);
		if (rm.getHisAttrs().size() > 0)
		{
			 try {
				BindAttrs(rm.getHisAttrs());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Button btn = new Button();
//		btn.CssClass = "Btn";
		btn.setText("功能执行");
		if (StringHelper.isNullOrEmpty(rm.Warning) == false)
		{
			btn.attributes.put("onclick", "if (confirm('" + rm.Warning + "')==false) {return false;}else{ this.disabled=true; }");
		}
		else
		{
			btn.attributes.put("onclick", "this.disabled=true;");
			//  btn.Attributes["onclick"] = "this.disabled=true;return window.confirm('" + rm.Warning + "');";
		}

		pub.append(AddBR());
		pub.append(AddBR());
		btn.setId("Btn_Do");
		btn.addAttr("onclick", "btn_do_click();");

		pub.append(btn.toString());

		pub.append("<input type=button class=Btn onclick='window.close();' value='关闭(Esc)' />");
		pub.append(AddFieldSetEnd());
	}

}

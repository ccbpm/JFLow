package cn.jflow.controller.wf.comm;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.En.Attr;
import BP.En.FieldType;
import BP.En.Method;
import BP.Sys.PubClass;
import cn.jflow.common.model.BaseModel;
import cn.jflow.controller.wf.workopt.BaseController;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.HtmlUtils;
import cn.jflow.system.ui.core.TextBox;
@Controller
@RequestMapping("/WF/Comm")
@Scope("request")
public class MethodController extends BaseController {
	
	@RequestMapping(value = "/doClick", method = RequestMethod.POST)
	public  ModelAndView doClick(HttpServletRequest request,
			HttpServletResponse response)
	{
		
		HashMap<String,BaseWebControl>hashMap = HtmlUtils.httpParser(request.getParameter("BodyHtml"), false);
		
		String ensName = request.getParameter("M");
		Method rm = BP.En.ClassFactory.GetMethod(ensName);
		// rm.Init();
		int mynum = 0;
		for (Attr attr : rm.getHisAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			mynum++;
		}
		int idx = 0;
		for (Attr attr : rm.getHisAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			if (attr.getUIVisible() == false)
			{
				continue;
			}
			try
			{
				TextBox tb = null;
				switch (attr.getUIContralType())
				{
					case TB:
						if(null == hashMap.get("TB_" + attr.getKey())){
							break;
						}
						tb = (TextBox) hashMap.get("TB_" + attr.getKey());
						switch (attr.getMyDataType())
						{
							case BP.DA.DataType.AppString:
							case BP.DA.DataType.AppDate:
							case BP.DA.DataType.AppDateTime:
								
								String str1 = (tb.getText());
								rm.SetValByKey(attr.getKey(), str1);
								break;
							case BP.DA.DataType.AppInt:
								int myInt = Integer.parseInt(tb.getText());
								rm.SetValByKey(String.valueOf(idx), myInt);
								rm.SetValByKey(attr.getKey(), myInt);
								break;
							case BP.DA.DataType.AppFloat:
								float myFloat = Float.parseFloat(tb.getText());
								rm.SetValByKey(attr.getKey(), myFloat);
								break;
							case BP.DA.DataType.AppDouble:
							case BP.DA.DataType.AppMoney:
							case BP.DA.DataType.AppRate:
								java.math.BigDecimal myDoub = java.math.BigDecimal.valueOf(Long.parseLong(tb.getText()));
								rm.SetValByKey(attr.getKey(), myDoub);
								break;
							case BP.DA.DataType.AppBoolean:
								int myBool = Integer.parseInt(tb.getText());
								rm.SetValByKey(attr.getKey(), myBool);
								break;
							default:
								throw new RuntimeException("没有判断的数据类型．");
						}
						break;
					case DDL:
						DDL ddl = (DDL) hashMap.get("DDL_" + attr.getKey());
						try
						{
//							String str = hashMap.get("DDL_" + attr.getKey()).SelectedItemStringVal;
							rm.SetValByKey(attr.getKey(), ddl.getSelectedItemIntVal());
						}
						catch (java.lang.Exception e)
						{
							rm.SetValByKey(attr.getKey(), "");
						}
						break;
					case CheckBok:
						CheckBox checkBox = (CheckBox) hashMap.get("CB_" + attr.getKey());
						if (checkBox.getChecked())
						{
							rm.SetValByKey(attr.getKey(), 1);
						}
						else
						{
							rm.SetValByKey(attr.getKey(), 0);
						}
						break;
					default:
						break;
				}
				idx++;
			}
			catch (RuntimeException ex)
			{
				
				throw new RuntimeException("attr=" + attr.getKey() + " attr = " + attr.getKey() + ex.getMessage());
			}
		}

		try
		{
			Object obj = rm.Do();
			if (obj != null)
			{
				switch (rm.HisMsgShowType)
				{
					case  SelfAlert:
						
					  PubClass.Alert(obj.toString(), response);
						return null;
					case  SelfMsgWindows:
						PubClass.Alert(obj.toString(), response);
						return null;
					case Blank:
				  //  BP.WF.Glo.ToMsg("流程删除成功");
						BaseModel.ToMsgPage(obj.toString());
//						this.ToWFMsgPage(obj.toString());
						return null;
					default:
						return null;
				}
			}
			BaseModel.WinClose();
		}
		catch (RuntimeException ex)
		{
			BaseModel.ToErrorPage("@执行[" + ensName + "]期间出现错误：\n"+ex.getMessage());
//			this.UCEn1.AddMsgOfWarning("@执行[" + ensName + "]期间出现错误：", ex.getMessage());
		}
		return null;
	}
}

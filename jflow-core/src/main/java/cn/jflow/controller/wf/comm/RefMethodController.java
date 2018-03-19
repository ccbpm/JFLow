package cn.jflow.controller.wf.comm;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.En.Attr;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.Tools.StringHelper;
import BP.WF.DotNetToJavaStringHelper;
import cn.jflow.common.model.BaseModel;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/Comm")
@Scope("request")
public class RefMethodController extends BaseController {
	

	@RequestMapping(value = "/doRefClick", method = {RequestMethod.POST} )
	public ModelAndView doRefClick(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String ensName = request.getParameter("EnsName");
		String indexStr = request.getParameter("Index");
		if(StringHelper.isNullOrEmpty(indexStr)){
			indexStr = "0";
		}
		
		int index = Integer.parseInt(indexStr);
		Entities ens = BP.En.ClassFactory.GetEns(ensName);
		Entity en = ens.getGetNewEntity();
		String msg = "";
		String pk = this.getRequest().getParameter("PK");
		if(pk.contains(",") == false){
			en.setPKVal(request.getParameter(en.getPK()));
			en.Retrieve();
			msg = DoOneEntity(en, index);
			if (msg == null)
			{
				winClose(this.getResponse());
			}
			else
			{
				winCloseWithMsg1(this.getResponse(),msg);
			}
		}
		
		//如果是批处理.
		String[] pks = pk.split("[,]", -1);
		for (String mypk : pks)
		{
			if (DotNetToJavaStringHelper.isNullOrEmpty(mypk) == true)
			{
				continue;
			}
			en.setPKVal(mypk);
			en.Retrieve();
			String s = DoOneEntity(en, index);
			if (s != null)
			{
				msg += "@" + s;
			}
		}
		if (msg.equals(""))
		{
			winClose(this.getResponse());
		}
		else
		{
			winCloseWithMsg1(this.getResponse(),msg);
		}
		return null;
		
	}
	
	public final String DoOneEntity(Entity en, int rmIdx)
	{
		BP.En.RefMethod rm = en.getEnMap().getHisRefMethods().get(rmIdx);
		rm.HisEn = en;
		int mynum = 0;
		for (Attr attr : rm.getHisAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			mynum++;
		}

		Object[] objs = new Object[mynum];

		int idx = 0;
		
		for (Attr attr : rm.getHisAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			switch (attr.getUIContralType()) {
			case TB:
				String tb_value = getParamter("TB_" + attr.getKey());
				switch (attr.getMyDataType()) {
				case BP.DA.DataType.AppString:
				case BP.DA.DataType.AppDate:
				case BP.DA.DataType.AppDateTime:
					if(StringHelper.isNullOrEmpty(tb_value)){
						tb_value = "无";
					}
					objs[idx] = tb_value;
					// attr.DefaultVal=str1;
					break;
				case BP.DA.DataType.AppInt:
					if(StringHelper.isNullOrEmpty(tb_value)){
						tb_value = "0";
					}
					int myInt = Integer.parseInt(tb_value);
					objs[idx] = myInt;
					// attr.DefaultVal=myInt;
					break;
				case BP.DA.DataType.AppFloat:
					if(StringHelper.isNullOrEmpty(tb_value)){
						tb_value = "0f";
					}
					float myFloat = Float.parseFloat(tb_value);
					objs[idx] = myFloat;
					// attr.DefaultVal=myFloat;
					break;
				case BP.DA.DataType.AppDouble:
				case BP.DA.DataType.AppMoney:
				case BP.DA.DataType.AppRate:
					if(StringHelper.isNullOrEmpty(tb_value)){
						tb_value = "0.0";
					}
					java.math.BigDecimal myDoub = java.math.BigDecimal.valueOf(Long.parseLong(tb_value));
					objs[idx] = myDoub;
					// attr.DefaultVal=myDoub;
					break;
				case BP.DA.DataType.AppBoolean:
					if(StringHelper.isNullOrEmpty(tb_value)){
						tb_value = "0";
					}
					int myBool = Integer.parseInt(tb_value);
					if (myBool == 0) {
						objs[idx] = false;
						attr.setDefaultVal(false);
					} else {
						objs[idx] = true;
						attr.setDefaultVal(true);
					}
					break;
				default:
					throw new RuntimeException("没有判断的数据类型．");

				}
				break;
			case DDL:
				try {
					String ddl_value = getParamter("DDL_" + attr.getKey());
					objs[idx] = ddl_value;
					attr.setDefaultVal(ddl_value);
				} catch (RuntimeException ex) {
					// this.ToErrorPage("获取：[" + attr.Desc +
					// "] 期间出现错误，可能是该下拉框中没有选择项目，错误技术信息：" + ex.Message);
					objs[idx] = null;
					// attr.DefaultVal = "";
				}
				break;
			case CheckBok:
				String cb_value = getParamter("CB_" + attr.getKey());
				if (null != cb_value) {
					objs[idx] = "1";
				} else {
					objs[idx] = "0";
				}
				attr.setDefaultVal(objs[idx].toString());
				break;
			default:
				break;
			}
			idx++;
		}

		try {
			Object obj = rm.Do(objs);
			if (obj != null) {
				BaseModel.ToMsgPage(obj.toString());
			}
			BaseModel.WinClose();
		} catch (RuntimeException ex) {
			String msg = "";
			for (Object obj : objs) {
				msg += "@" + obj.toString();
			}
			StringBuilder error = new StringBuilder();
			error.append("<font color=red>").append("@执行[").append(this.getEnsName()).append("]期间出现错误：").append(ex.getMessage())
			.append(" InnerException= ").append(ex.getCause()).append("[参数为：]").append(msg).append("</font>");
			BaseModel.ToErrorPage(error.toString());
		} catch (Exception e) {
			BaseModel.ToErrorPage(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}

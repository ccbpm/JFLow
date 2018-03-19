package cn.jflow.controller.wf.admin.AttrFlow;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.WF.Flow;
import BP.WF.StartLimitRole;
import cn.jflow.common.model.AjaxJson;
import cn.jflow.controller.wf.workopt.BaseController;
import cn.jflow.system.ui.core.RadioButton;

@Controller
@RequestMapping("/WF/DTSBTableExt")
@Scope("request")
public class DTSBTableExtController extends BaseController {
	/*
	 * protected final void Btn_Save_Click(Object sender, EventArgs e) { Save();
	 * 
	 * }
	 */
	@ResponseBody
	@RequestMapping(value = "/BtnSaveClick", method = RequestMethod.POST)
	public String Btn_Save_Click(HttpServletRequest request,
			HttpServletResponse response,String FK_Flow,String DDL_OID) {
		String url = request.getQueryString();
		HashMap<String, String> map = this.getParamsMap(url, "utf-8");
		try
		{
			String rpt = "ND" + Integer.parseInt(FK_Flow) + "Rpt";
			Flow fl = new Flow(FK_Flow);
			MapAttrs attrs = new MapAttrs(rpt);

			///#region 求业务表的主键。
			String pk = DDL_OID.split(" ")[0];
			if (isNullOrEmpty(pk) == true)
			{
				//BP.Sys.PubClass.Alert("@必须设置业务表的主键，否则无法同步。", response);
				return "{\"msg\":\"@必须设置业务表的主键，否则无法同步。\"}";
			}
			///#endregion 求业务表的主键。

			String lcStr = ""; //要同步的流程字段
			String ywStr = ""; //第三方字段
			String err = "";
			for (MapAttr attr : attrs.ToJavaList())
			{
				String cb = request.getParameter("CB_" + attr.getKeyOfEn());
				if (cb == null || cb.equals(""))
				{
					continue;
				}

				String ddl = request.getParameter("DDL_" + attr.getKeyOfEn()).split(" ")[0];
				//如果选中的业务字段重复，抛出异常
				if (ywStr.contains("@" + ddl + "@"))
				{
					err += "@配置【" + attr.getKeyOfEn() + " - " + attr.getName() + "】错误, 请确保选中业务字段的唯一性，该业务字段已经被其他字段所使用。";
				}
				lcStr += "@" + attr.getKeyOfEn() + "@,";
				ywStr += "@" + ddl + "@,";
			}

			String rb = request.getParameter("rb_workId");

			String ddl_key = request.getParameter("DDL_OID");
			if (rb!=null&&!"".equals(rb))
			{
				if (ywStr.contains("@" + ddl_key + "@"))
				{
					err += "@请确保选中业务字段的唯一性，该业务字段【" + ddl_key + "】已经被其他字段所使用。";
				}
				lcStr = "@OID@," + lcStr;
				ywStr = "@" + ddl_key + "@," + ywStr;
			}
			else
			{
				if (ywStr.contains("@" + ddl_key + "@"))
				{
					err += "@请确保选中业务字段的唯一性，该业务字段【" + ddl_key + "】已经被其他字段所使用。";
				}
				lcStr = "@GUID@," + lcStr;
				ywStr = "@" + ddl_key + "@," + ywStr;
			}

			if (!err.equals(""))
			{
				return "{\"msg\":\""+err+"\"}";
			}

			lcStr = lcStr.replace("@", "");
			ywStr = ywStr.replace("@", "");


			//去除最后一个字符的操作
			if (isNullOrEmpty(lcStr) || isNullOrEmpty(ywStr))
			{
				//BP.Sys.PubClass.Alert("要配置的内容为空...", response);
				return "{\"msg\":\"要配置的内容为空...\"}";
			}
			lcStr = lcStr.substring(0, lcStr.length() - 1);
			ywStr = ywStr.substring(0, ywStr.length() - 1);


			//数据存储格式   a,b,c@a_1,b_1,c_1
			fl.setDTSFields(lcStr + "@" + ywStr);
			fl.setDTSBTablePK(pk);
			int i= fl.Update();
			if(i>0){
				return "{\"msg\":\"保存成功\"}";
			}else{
				return "{\"msg\":\"保存失败\"}";
			}
			// System.Web.HttpContext.Current.Response.Write("<script language='JavaScript'>if (confirm('操作成功,是否关闭配置页面?'))" +
			//"{window.parent.closeTab('设置字段匹配');}</script> ");

			//System.Web.HttpContext.Current.Response.Write("<script language='JavaScript'>if (confirm('操作成功,是否关闭配置页面?'))" + "{try{window.parent.closeTab('设置字段匹配');}catch{}}</script> ");
		}
		catch (RuntimeException ex)
		{
			throw ex;
		}
	}
		//------------------------------------------------------------------------------------
		//	This method replaces the .NET static string method 'IsNullOrEmpty'.
		//------------------------------------------------------------------------------------
		public static boolean isNullOrEmpty(String string)
		{
			return string == null || string.equals("");
		}

		//------------------------------------------------------------------------------------
		//	This method replaces the .NET static string method 'Join' (2 parameter version).
		//------------------------------------------------------------------------------------
		public static String join(String separator, String[] stringarray)
		{
			if (stringarray == null)
				return null;
			else
				return join(separator, stringarray, 0, stringarray.length);
		}

		//------------------------------------------------------------------------------------
		//	This method replaces the .NET static string method 'Join' (4 parameter version).
		//------------------------------------------------------------------------------------
		public static String join(String separator, String[] stringarray, int startindex, int count)
		{
			String result = "";

			if (stringarray == null)
				return null;

			for (int index = startindex; index < stringarray.length && index - startindex < count; index++)
			{
				if (separator != null && index > startindex)
					result += separator;

				if (stringarray[index] != null)
					result += stringarray[index];
			}

			return result;
		}

		//------------------------------------------------------------------------------------
		//	This method replaces the .NET static string method 'TrimEnd'.
		//------------------------------------------------------------------------------------
		public static String trimEnd(String string, Character... charsToTrim)
		{
			if (string == null || charsToTrim == null)
				return string;

			int lengthToKeep = string.length();
			for (int index = string.length() - 1; index >= 0; index--)
			{
				boolean removeChar = false;
				if (charsToTrim.length == 0)
				{
					if (Character.isWhitespace(string.charAt(index)))
					{
						lengthToKeep = index;
						removeChar = true;
					}
				}
				else
				{
					for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++)
					{
						if (string.charAt(index) == charsToTrim[trimCharIndex])
						{
							lengthToKeep = index;
							removeChar = true;
							break;
						}
					}
				}
				if ( ! removeChar)
					break;
			}
			return string.substring(0, lengthToKeep);
		}

		//------------------------------------------------------------------------------------
		//	This method replaces the .NET static string method 'TrimStart'.
		//------------------------------------------------------------------------------------
		public static String trimStart(String string, Character... charsToTrim)
		{
			if (string == null || charsToTrim == null)
				return string;

			int startingIndex = 0;
			for (int index = 0; index < string.length(); index++)
			{
				boolean removeChar = false;
				if (charsToTrim.length == 0)
				{
					if (Character.isWhitespace(string.charAt(index)))
					{
						startingIndex = index + 1;
						removeChar = true;
					}
				}
				else
				{
					for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++)
					{
						if (string.charAt(index) == charsToTrim[trimCharIndex])
						{
							startingIndex = index + 1;
							removeChar = true;
							break;
						}
					}
				}
				if ( ! removeChar)
					break;
			}
			return string.substring(startingIndex);
		}

		//------------------------------------------------------------------------------------
		//	This method replaces the .NET static string method 'Trim' when arguments are used.
		//------------------------------------------------------------------------------------
		public static String trim(String string, Character... charsToTrim)
		{
			return trimEnd(trimStart(string, charsToTrim), charsToTrim);
		}

		//------------------------------------------------------------------------------------
		//	This method is used for string equality comparisons when the option
		//	'Use helper 'stringsEqual' method to handle null strings' is selected
		//	(The Java String 'equals' method can't be called on a null instance).
		//------------------------------------------------------------------------------------
		public static boolean stringsEqual(String s1, String s2)
		{
			if (s1 == null && s2 == null)
				return true;
			else
				return s1 != null && s1.equals(s2);
		}
		private HashMap<String, String> getParamsMap(String queryString, String enc) {
			HashMap<String, String> paramsMap = new HashMap<String, String>();
			if (queryString != null && queryString.length() > 0) {
				int ampersandIndex, lastAmpersandIndex = 0;
				String subStr, param, value;
				String[] paramPair;
				do {
					ampersandIndex = queryString.indexOf('&', lastAmpersandIndex) + 1;
					if (ampersandIndex > 0) {
						subStr = queryString.substring(lastAmpersandIndex,
								ampersandIndex - 1);
						lastAmpersandIndex = ampersandIndex;
					} else {
						subStr = queryString.substring(lastAmpersandIndex);
					}
					paramPair = subStr.split("=");
					param = paramPair[0];
					value = paramPair.length == 1 ? "" : paramPair[1];
					try {
						value = URLDecoder.decode(value, enc);
					} catch (UnsupportedEncodingException ignored) {
					}
					paramsMap.put(param, value);
				} while (ampersandIndex > 0);
			}
			return paramsMap;
		}
}
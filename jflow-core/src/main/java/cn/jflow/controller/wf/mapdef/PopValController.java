package cn.jflow.controller.wf.mapdef;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping(value="/WF/PopVal")
public class PopValController extends BaseController{
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping(value="/savex",method=RequestMethod.POST)
	public void saveMapDtl(HttpServletRequest request, HttpServletResponse response,
			String Model,String ShowWay,String back,String xxxx,String TB_Group,
			String TB_Entity,String TB_URL,String FK_MapData,String RefNo){
		
		//查询出实体.
		MapExt ext = new MapExt();
		ext.Retrieve(MapExtAttr.FK_MapData, FK_MapData, MapExtAttr.ExtType, "PopVal", MapExtAttr.AttrOfOper, RefNo);

		// 工作模式 0 -url .1-内置
		if ("RB_Model_Inntel".equals(Model))
		{
			ext.setPopValWorkModel(1);
		}
		else
		{
			ext.setPopValWorkModel(0);
		}

		// 数据呈现方式
		if ("RB_Table".equals(ShowWay))
		{
			ext.setPopValShowModel(0);
		}
		else
		{
			ext.setPopValShowModel(1);
		}

		//选择数据方式

		if ("RB_PopValSelectModel_0".equals(xxxx))
		{
			ext.setPopValSelectModel(0);
		}
		else
		{
			ext.setPopValSelectModel(1);
		}

		//返回值格式
		if ("RB_PopValFormat_0".equals(back))
		{
			ext.setPopValFormat(0);
		}
		if ("RB_PopValFormat_1".equals(back))
		{
			ext.setPopValFormat(1);
		}

		if ("RB_PopValFormat_2".equals(back))
		{
			ext.setPopValFormat(2);
		}
		//数据源分组sql
		if (!isNullOrEmpty(TB_Group))
		{
			ext.setTag1(TB_Group);
		}
		else
		{
			ext.setTag1("");
		}
		//数据源sql
		if (!isNullOrEmpty(TB_Entity))
		{
			ext.setTag2(TB_Entity);
		}
		else
		{
			ext.setTag2("");
		}

		//URL
		if (isNullOrEmpty(TB_URL)==false)
		{
			ext.setDoc(TB_URL);
		}
		else
		{
			ext.setDoc("");
		}

		//操作的文本框
		ext.setAttrOfOper(RefNo);
		ext.setExtType(MapExtXmlList.PopVal);
		ext.setFK_MapData(FK_MapData);
		ext.setMyPK(ext.getExtType() + "_" + FK_MapData + "_" + ext.getAttrOfOper());
		ext.Save();
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping(value="/deleteX",method=RequestMethod.POST)
	public void delete(HttpServletRequest request, HttpServletResponse response,
			String Model,String ShowWay,String back,String xxxx,String TB_Group,
			String TB_Entity,String TB_URL,String FK_MapData,String RefNo){
		MapExt ext = new MapExt();
		ext.setMyPK(MapExtXmlList.PopVal + "_" + FK_MapData + "_" +RefNo);
		ext.Delete();
	}
//----------------------------------------------------------------------------------------
//Copyright © 2006 - 2010 Tangible Software Solutions Inc.
//This class can be used by anyone provided that the copyright notice remains intact.
//
//This class is used to simulate some .NET string functions in Java.
//----------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------
//	This method replaces the .NET static string method 'IsNullOrEmpty'.
//------------------------------------------------------------------------------------
public  boolean isNullOrEmpty(String string)
{
	return string == null || string.equals("");
}

//------------------------------------------------------------------------------------
//	This method replaces the .NET static string method 'Join' (2 parameter version).
//------------------------------------------------------------------------------------
public String join(String separator, String[] stringarray)
{
	if (stringarray == null)
		return null;
	else
		return join(separator, stringarray, 0, stringarray.length);
}

//------------------------------------------------------------------------------------
//	This method replaces the .NET static string method 'Join' (4 parameter version).
//------------------------------------------------------------------------------------
public  String join(String separator, String[] stringarray, int startindex, int count)
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
public  String trimEnd(String string, Character... charsToTrim)
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
public  String trimStart(String string, Character... charsToTrim)
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
public  String trim(String string, Character... charsToTrim)
{
	return trimEnd(trimStart(string, charsToTrim), charsToTrim);
}

//------------------------------------------------------------------------------------
//	This method is used for string equality comparisons when the option
//	'Use helper 'stringsEqual' method to handle null strings' is selected
//	(The Java String 'equals' method can't be called on a null instance).
//------------------------------------------------------------------------------------
public  boolean stringsEqual(String s1, String s2)
{
	if (s1 == null && s2 == null)
		return true;
	else
		return s1 != null && s1.equals(s2);
}

}

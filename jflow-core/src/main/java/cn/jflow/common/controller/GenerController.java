package cn.jflow.common.controller;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.DA.Log;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.Sys.SFTable;
import BP.Sys.SysEnums;
import BP.Tools.Json;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/Comm")
public class GenerController extends BaseController{
	
	@RequestMapping(value="/gener" ,method = RequestMethod.POST)
	@ResponseBody
	public void execute()
	{
		String doType = getDoType();
		String msg  = "";
		PrintWriter out = null;
		if("EnumList".equals(doType))
		{
			SysEnums ses = new SysEnums(getEnumKey());
			msg  = Json.ToJson(ses.ToDataTableField());
		}else if("EnsData".equals(doType))    //获得枚举列表的JSON
		{
            Entities ens = ClassFactory.GetEns(this.getEnsName());
            ens.RetrieveAll();
            msg = Json.ToJson(ens.ToDataTableField());
		}else if("SFTable".equals(doType))    //获得枚举列表的JSON
    	{
            SFTable sftable = new SFTable(getSFTable());
            DataTable dt= sftable.GenerData();
            msg= BP.Tools.Json.ToJson(dt);
		}else if("SQLList".equals(doType))    //获得枚举列表的JSON
    	{
			msg = this.SQLList(); 
		}else
		{
			msg = "err@没有判断的标记:" + this.getDoType();
		}
		
		try {
			out = getResponse().getWriter();
			out.write(msg);
		} catch (IOException e) {
			msg = "err@" + e.getMessage();
			Log.DebugWriteError(msg);
		}finally
		{
			if(null != out)
			{
				out.close();
			}
		}
		
	}
	
	 /**
	  *  执行一个SQL，然后返回一个列表.
	  *  @return 
	  */
	public final String SQLList()
	{
		String sqlKey = getRequest().getParameter("SQLKey"); //SQL的key.
		String paras = getRequest().getParameter("Paras"); //参数. 格式为 @para1=paraVal@para2=val2

		BP.Sys.XML.SQLList sqlXml = new BP.Sys.XML.SQLList(sqlKey);

		//获得SQL
		String sql = sqlXml.getSQL();
		String[] strs = paras.split("[@]", -1);
		for (String str : strs)
		{
			if (str == null || str.equals(""))
			{
				continue;
			}

			//参数.
			String[] p = str.split("[=]", -1);
			sql = sql.replace("@" + p[0], p[1]);
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return BP.Tools.Json.ToJson(dt);
	}

	public String getEnumKey()
	{
		String str = getRequest().getParameter("EnumKey");
		 if (str == null || "".equals(str) || "null".equals(str))
		        return null;
		    return str;
	}
	public final String getSFTable()
	{
		String str = getRequest().getParameter("SFTable");
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;
	}

}

package cn.jflow.common.controller;

import java.io.PrintWriter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.Log;
import BP.En.UIContralType;
import BP.Sys.M2MType;
import BP.Sys.MapData;
import BP.Sys.MapM2M;
import BP.Tools.StringHelper;
import BP.WF.CCFormAPI;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF")
public class FormDesignerController extends BaseController {
	
	@RequestMapping(value="/form" ,method = RequestMethod.POST)
	@ResponseBody
	public void execute()
    {
		PrintWriter out = null;
		String action = "";
		//返回值
		String msg = "";
		if (StringHelper.isNullOrEmpty(getRequest().getParameter("action")) == false)
		{
			action = getRequest().getParameter("action");
		}
		if("loadform".equals(action))//获取表单数据
		{
			MapData mapData = new MapData(this.getFK_MapData());
		    msg = mapData.ToJson(); //要返回的值.
		}else if(("ParseStringToPinyin").equals(action))
		{
			String name = getRequest().getParameter("name");
		    String flag = getRequest().getParameter("flag");
		    if ("true".equals(flag))
		    {
		        msg = ParseStringToPinyinField(name, true);
		    }else{	 
		        msg = ParseStringToPinyinField(name, false);
		    }
		}else if("DoType".equals(action))//表单特殊元素保存公共方法
		{
			msg = DoType();
		}else{
			msg = "err@没有判断的标记:" + this.getDoType();
		}
		
		try {
			getResponse().setContentType("text/html");
			out = getResponse().getWriter();
			out.write(msg);
		} catch (Exception ex) {
			msg = "/WF/form err@" + ex.getMessage();
			Log.DebugWriteError(msg);
		}finally{
			if(null !=out)
			{
				out.close();
			}
		}	
    }
	
	
	@RequestMapping(value="/form" ,method = RequestMethod.GET)
	@ResponseBody
	public void executeGet()
    {
		PrintWriter out = null;
		String action = "";
		//返回值
		String msg = "";
		if (StringHelper.isNullOrEmpty(getRequest().getParameter("action")) == false)
		{
			action = getRequest().getParameter("action");
		}
		
		if("GetEnumerationList".equals(action)) //获取所有枚举
		{
			String pageNumberStr = getRequest().getParameter("pageNumber");
            int pageNumber = 1;
            if (!StringHelper.isNullOrEmpty(pageNumberStr))
            {
            	pageNumber = Integer.parseInt(pageNumberStr);
            }
            String pageSizeStr = getRequest().getParameter("pageSize");
            int pageSize = 9999;
            if (StringHelper.isNullOrEmpty(pageSizeStr) == false)
                pageSize = Integer.parseInt(pageSizeStr);

            //调用API获得数据.
            if ("GetSFTableList".equals(action))
            {
            	msg = BP.WF.CCFormAPI.DB_SFTableList(pageNumber, pageSize); 
            } else {
            	msg = BP.WF.CCFormAPI.DB_EnumerationList(pageNumber, pageSize); //调用API获得数据.
            }
		}else{
			msg = "err@没有判断的标记:" + this.getDoType();
		}
		
		try {
			getResponse().setContentType("text/html");
			out = getResponse().getWriter();
			out.write(msg);
		} catch (Exception ex) {
			msg = "/WF/form err@" + ex.getMessage();
			Log.DebugWriteError(msg);
		}finally{
			if(null !=out)
			{
				out.close();
			}
		}	
    }
	/**
	 * 处理表单事件方法
	 * @return
	 */
    public String DoType()
    {
    	String dotype = getRequest().getParameter("DoType");
    	String frmID = getRequest().getParameter("FK_MapData");
    	if (frmID == null){
    		frmID = getRequest().getParameter("FrmID");           
    	}
    	float x = 0;
        float y = 0;
        String no = "";
        String name = "";

        String v1 = getRequestParam("v1");
        String v2 = getRequestParam("v2");
        String v3 = getRequestParam("v3");
        String v4 = getRequestParam("v4");
        String v5 = getRequestParam("v5");
        String sql = "";
        
        try
        {
        	if("NewEnum".equals(dotype.trim()) || "SaveEnum".equals(dotype.trim()))
        	{
        		String enumName = getRequestParam("EnumName");
                String enumKey1 = getRequestParam("EnumKey");
                String cfgVal = getRequestParam("Vals");
                //调用接口执行保存.
                BP.WF.CCFormAPI.SaveEnum(enumKey1, enumName, cfgVal);
                return "true";
        	}else if("PublicNoNameCtrlCreate".equals(dotype.trim())) //创建通用的控件.
        	{
        		String ctrlType = getRequestParam("CtrlType");
                try
                {
                    no = getRequestParam("No");
                    name = getRequestParam("Name");
                    x = Float.parseFloat(getRequestParam("x"));
                    y = Float.parseFloat(getRequestParam("y"));
                    CCFormAPI.CreatePublicNoNameCtrl(frmID, ctrlType, no, name, x, y);
                    return "true";
                }
                catch (Exception ex)
                {
                	Log.DebugWriteError("PublicNoNameCtrlCreate @err:"+ex.getMessage());
                    return ex.getMessage();
                }
        	}else if("NewSFTableField".equals(dotype.trim()))  //创建一个SFTable字段.
        	{
        		try
                {
                    String fk_mapdata = getRequestParam("FK_MapData");
                    String keyOfEn = getRequestParam("KeyOfEn");
                    String fieldDesc = getRequestParam("Name");
                    String sftable = getRequestParam("UIBindKey");
                    x = Float.parseFloat(getRequestParam("x"));
                    y = Float.parseFloat(getRequestParam("y"));

                    //调用接口,执行保存.
                    CCFormAPI.SaveFieldSFTable(fk_mapdata, keyOfEn, fieldDesc, sftable, x, y);
                    return "true";
                }
                catch (Exception ex)
                {
                	Log.DebugWriteError("NewSFTableField @err:"+ex.getMessage());
                    return ex.getMessage();
                }
        	}else if("NewEnumField".equals(dotype.trim()))  //创建一个字段. 对应 FigureCreateCommand.js  里的方法.
        	{
        		try
                {
                    UIContralType ctrl = UIContralType.RadioBtn;
                    String ctrlDoType = getRequestParam("ctrlDoType");
                    if ("DDL".equals(ctrlDoType)){
                    	ctrl = UIContralType.DDL;
                    } else {
                    	ctrl = UIContralType.RadioBtn;
                    }

                    String fk_mapdata = getRequestParam("FK_MapData");
                    String keyOfEn = getRequestParam("KeyOfEn");
                    String fieldDesc = getRequestParam("Name");
                    String enumKeyOfBind = getRequestParam("UIBindKey"); //要绑定的enumKey.
                    x = Float.parseFloat(getRequestParam("x"));
                    y = Float.parseFloat(getRequestParam("y"));

                    CCFormAPI.NewEnumField(frmID, keyOfEn, fieldDesc, enumKeyOfBind, ctrl, x, y);
                    return "true";
                }
                catch (Exception ex)
                {
                	Log.DebugWriteError("NewEnumField @err:"+ex.getMessage());
                    return ex.getMessage();
                }
        	}else if("NewField".equals(dotype.trim()))   //创建一个字段. 对应 FigureCreateCommand.js  里的方法.
        	{
        		try
                {
                    CCFormAPI.NewField(getRequestParam("FrmID"), getRequestParam("KeyOfEn"), getRequestParam("Name"),
                       Integer.parseInt(getRequestParam("FieldType")),
                       Float.parseFloat(getRequestParam("x")),
                       Float.parseFloat(getRequestParam("y")));
                    return "true";
                }
                catch (Exception ex)
                {
                	Log.DebugWriteError("NewField @err:"+ex.getMessage());
                    return ex.getMessage();
                }
        	}else if("CreateCheckGroup".equals(dotype.trim()))   //创建审核分组，暂时未实现.
        	{
        		 CCFormAPI.NewCheckGroup(getFK_MapData(), null, null);
                 return "true";
        	}else if("NewM2M".equals(dotype.trim()))
        	{
        		String fk_mapdataM2M = v1;
                String m2mName = v2;
                MapM2M m2m = new MapM2M();
                m2m.setFK_MapData(v1);
                m2m.setNoOfObj(v2);
                if ("0".equals(v3))
                {
                    m2m.setHisM2MType(M2MType.M2M);
                    m2m.setName("新建一对多");
                }
                else
                {
                    m2m.setHisM2MType(M2MType.M2MM);
                    m2m.setName("新建一对多多");
                }

                m2m.setX(Float.parseFloat(v4));
                m2m.setY(Float.parseFloat(v5));
                m2m.setMyPK(m2m.getFK_MapData() + "_" + m2m.getNoOfObj());
                if (m2m.getIsExits()){
                	return "error:多选名称:" + m2mName + "，已经存在。";
                }
                m2m.Insert();
                return "true";
        	}else if("DelEnum".equals(dotype.trim()))
        	{
        		//删除空数据.
                DBAccess.RunSQL("DELETE FROM Sys_MapAttr WHERE FK_MapData IS NULL OR FK_MapData='' ");
        		
                //获得要删除的枚举值.
                String enumKey = getRequestParam("EnumKey");
                
                // 检查这个物理表是否被使用.
                sql = "SELECT  FK_MapData,KeyOfEn,Name FROM Sys_MapAttr WHERE UIBindKey='" + enumKey + "'";
                DataTable dtEnum = DBAccess.RunSQLReturnTable(sql);
                String msgDelEnum = "";
                for (DataRow dr : dtEnum.Rows)
                {
                    msgDelEnum += "\n 表单编号:" + dr.get("FK_MapData") + " , 字段:" + dr.get("KeyOfEn") + ", 名称:" + dr.get("Name");
                }
                if (msgDelEnum != "")
                {
                	return "error:该枚举已经被如下字段所引用，您不能删除它。" + msgDelEnum;
                }

                sql = "DELETE FROM Sys_EnumMain WHERE No='" + enumKey + "'";
                sql += "@DELETE FROM Sys_Enum WHERE EnumKey='" + enumKey + "' ";
                DBAccess.RunSQLs(sql);
                
                return "true";
        	}else if("DelEnum".equals(dotype.trim()))
        	{
                //删除空数据.
                BP.DA.DBAccess.RunSQL("DELETE FROM Sys_MapAttr WHERE FK_MapData IS NULL OR FK_MapData='' ");

                //获得要删除的枚举值.
                String enumKey = getRequest().getParameter("EnumKey");
                // 检查这个物理表是否被使用.
                sql = "SELECT  FK_MapData,KeyOfEn,Name FROM Sys_MapAttr WHERE UIBindKey='" + enumKey + "'";
                DataTable dtEnum = DBAccess.RunSQLReturnTable(sql);
                String msgDelEnum = "";
                for(DataRow dr : dtEnum.Rows)
                {
                    msgDelEnum += "\n 表单编号:" + dr.getValue("FK_MapData") + " , 字段:" + dr.getValue("KeyOfEn") + ", 名称:" + dr.getValue("Name");
                }

                if (msgDelEnum != "")
                    return "error:该枚举已经被如下字段所引用，您不能删除它。" + msgDelEnum;

                sql = "DELETE FROM Sys_EnumMain WHERE No='" + enumKey + "'";
                sql += "@DELETE FROM Sys_Enum WHERE EnumKey='" + enumKey + "' ";
                DBAccess.RunSQLs(sql);
                return "true";
            }else{
        		 return "error:" + dotype + " , 后台执行错误，未设置此标记.";
        	}
        }catch(Exception e)
        {
        	return "err@DoType 异常信息" + e.getMessage();
        }
      }
	/**
	 * 转拼音方法
	 * <param name="name">字段中文名称</param>
	 * <param name="isQuanPin">是否转换全拼</param>
	 * <returns>转化后的拼音，不成功则抛出异常.</returns>
	 * @param name
	 * @param isQuanPin
	 * @return
	 */
    public static String ParseStringToPinyinField(String name, boolean isQuanPin)
    {
        String s = "" ;
        try
        {
            if (isQuanPin == true)
            {
                s = BP.DA.DataType.ParseStringToPinyin(name);
                if (s.length() > 15)
                    s = BP.DA.DataType.ParseStringToPinyinWordFirst(name);
            }
            else
            {
                s = BP.DA.DataType.ParseStringToPinyinJianXie(name);
            }

            s = s.trim().replace(" ", "");
            s = s.trim().replace(" ", "");
            //常见符号
            s = s.replace(",", "").replace(".", "").replace("，", "").replace("。", "").replace("!", "");
            s = s.replace("*", "").replace("@", "").replace("#", "").replace("~", "").replace("|", "");
            s = s.replace("$", "").replace("%", "").replace("&", "").replace("（", "").replace("）", "").replace("【", "").replace("】", "");
            s = s.replace("(", "").replace(")", "").replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace("/", "");
            if (s.length() > 0)
            {
                //去除开头数字
                int iHead = 0;
                String headStr = s.substring(0, 1);
              //  while (int.TryParse(headStr, out iHead) == true)
              while (StringHelper.isAllWhitespace(headStr)==true)
                {
                    //替换为空
                    s = s.substring(1);
                    if (s.length() > 0) headStr = s.substring(0, 1);
                }
            }
            //去掉空格，去掉点.
            s = s.replace(" ", "");
            s = s.replace(".", "");
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        }
		return s;
      }
	
	/**
	 * 公共方法获取值
	 * @param param
	 * @return
	 */
	public String getRequestParam(String param)
    {
		String str  = getRequest().getParameter(param);
		if(StringHelper.isNullOrEmpty(str))
		{
			return "";
		}
        return str ;
    }
	
	/**
	 * 表单编号
	 */
    private String getFK_MapData()
    {
      return getRequestParam("FK_MapData");
    }

}

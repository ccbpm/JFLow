package cn.jflow.common.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.Log;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Sys.FrmRB;
import BP.Sys.FrmRBAttr;
import BP.Sys.FrmRBs;
import BP.Sys.GroupFields;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import BP.Sys.PopValFormat;
import BP.Sys.PopValSelectModel;
import BP.Sys.PopValWorkModel;
import BP.Tools.StringHelper;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/MapDef/MapExt")
public class MapDefMapExtController extends BaseController{
	
	@RequestMapping(value = "/MapExt", method = RequestMethod.POST)
	@ResponseBody
	protected void execute(HttpServletRequest request,HttpServletResponse response) {
		String doType = request.getParameter("DoType");
        String msg = "";
        PrintWriter out =null;
        response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
        try
        {
        	if("RadioBtns_Init".equals(doType)){
        		msg = this.radioBtns_Init();
        	}else if("RadioBtns_Save".equals(doType)){
        		msg = this.radioBtns_Save();
        	}else if("PopVal_Init".equals(doType))
        	{
        		msg =PopVal_Init();//开窗返回值初始化
        	}else if("PopVal_Save".equals(doType))
        	{
        		msg = PopVal_Save();//开窗返回值保存
        	}
        	else if("RegularExpression_Init".equals(doType))
        	{
        		msg = RegularExpression_Init();//正则表达式初始化
        	}else if("RegularExpression_Save".equals(doType))
        	{
        		msg = RegularExpression_Save();//正则表达式保存
        	}
        	else{
        		  msg = "err@标记错误:" + this.getDoType();
        	}
        	out = response.getWriter();
      		out.write(msg);
        }
        catch (Exception ex)
        {
        	Log.DebugWriteError("/WF/MapDef/MapExt/MapExt.do?DoType= "+doType +"err@"+ex.getMessage());
            msg = "err@" + ex.getMessage();
            ex.printStackTrace();
        }finally{
			if(null !=out)
			{
				out.close();
			}
		}
      
	}
	
	/**
	 * 返回信息          
	 * @param request
	 * @param response
	 * @return
	 */
    public String radioBtns_Init()
    {
        DataSet ds = new DataSet();

        //放入表单字段.
        MapAttrs attrs = new MapAttrs(this.getFK_MapData());
        ds.Tables.add(attrs.ToDataTableField("Sys_MapAttr"));

        //属性.
        MapAttr attr = new MapAttr();
        attr.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn());
        attr.Retrieve();

        //把分组加入里面.
       
        GroupFields gfs = new GroupFields(this.getFK_MapData());
        ds.Tables.add(gfs.ToDataTableField("Sys_GroupFields"));

        //字段值.
        FrmRBs rbs = new FrmRBs();
        rbs.Retrieve(FrmRBAttr.FK_MapData, this.getFK_MapData(), FrmRBAttr.KeyOfEn, this.getKeyOfEn());
       
        if (rbs.size() == 0)
        {
            /*初始枚举值变化.
             */
            SysEnums ses = new SysEnums(attr.getUIBindKey());
            for(SysEnum se : ses.ToJavaList())
            {
                FrmRB rb = new FrmRB();
                rb.setFK_MapData(this.getFK_MapData());
                rb.setKeyOfEn(this.getKeyOfEn());
                rb.setIntKey(se.getIntKey());
                rb.setLab(se.getLab());
                rb.setEnumKey(attr.getUIBindKey());
                rb.Insert(); //插入数据.
            }

            rbs.Retrieve(FrmRBAttr.FK_MapData, this.getFK_MapData(), FrmRBAttr.KeyOfEn, this.getKeyOfEn());
        }
        //加入单选按钮.
        ds.Tables.add(rbs.ToDataTableField("Sys_FrmRB"));
        return BP.Tools.Json.ToJson(ds);
    }
    
    /**
     * 执行保存
     * @return
     */
    public String radioBtns_Save()
    {
        String json =getRequest().getParameter("data");
     	DataTable dt = BP.Tools.Json.ToDataTable(json);

     	for(DataRow dr : dt.Rows)
        {
            FrmRB rb = new FrmRB();
            rb.setMyPK(dr.getValue("MyPK").toString());
            rb.Retrieve();
            rb.setScript(dr.getValue("Script").toString());
            rb.setFieldsCfg(dr.getValue("FieldsCfg").toString()); //格式为 @字段名1=1@字段名2=0
            rb.setTip(dr.getValue("Tip").toString()); //提示信息
            rb.Update();
        }
        return "保存成功.";
    }
    
	 /**
	  * 开窗返回值初始化
	  * @return
	  */
    public String PopVal_Init()
    {
		
        MapExt ext = new MapExt();
        ext.setMyPK(this.getMyPK());
        if (ext.RetrieveFromDBSources() == 0)
        {
            ext.setFK_DBSrc("local");
            ext.setPopValSelectModel(ext.getPopValSelectModel());
            ext.setPopValWorkModel(ext.getPopValWorkModel());
        }
        return ext.PopValToJson();
        
    }
  /// <summary>
    /// 保存设置.
    /// </summary>
    /// <returns></returns>
    public String PopVal_Save()
    {
        try
        {
            MapExt me = new MapExt();
            me.setMyPK(this.getFK_MapExt());
            me.setFK_MapData(this.getFK_MapData());
            me.setExtType("PopVal");
            me.setAttrOfOper(getRequest().getParameter("KeyOfEn"));
            me.RetrieveFromDBSources();

            String valWorkModel = this.GetValFromFrmByKey("Model");
            
            if("SelfUrl".equals(valWorkModel))
            {
            	me.setPopValWorkModelNew(PopValWorkModel.SelfUrl);
                me.setPopValUrl(this.GetValFromFrmByKey("TB_Url"));
            }else if("TableOnly".equals(valWorkModel))//表格模式
            {
            	me.setPopValWorkModelNew(PopValWorkModel.TableOnly);
                me.setPopValEntitySQL(this.GetValFromFrmByKey("TB_Table_SQL"));
            }else if("TablePage".equals(valWorkModel))//分页模式
            {
            	me.setPopValWorkModelNew(PopValWorkModel.TablePage);
                me.setPopValTablePageSQL(this.GetValFromFrmByKey("TB_TablePage_SQL"));
                me.setPopValTablePageSQLCount(this.GetValFromFrmByKey("TB_TablePage_SQLCount"));
            }else if("Group".equals(valWorkModel))//分组模式
            {
            	 me.setPopValWorkModelNew(PopValWorkModel.Group);
                 me.setPopValGroupSQL(this.GetValFromFrmByKey("TB_GroupModel_Group"));
                 me.setPopValEntitySQL(this.GetValFromFrmByKey("TB_GroupModel_Entity"));
            }else if("Tree".equals(valWorkModel))//单实体树
            {
            	me.setPopValWorkModelNew(PopValWorkModel.Tree);
                me.setPopValTreeSQL(this.GetValFromFrmByKey("TB_TreeSQL"));
                me.setPopValTreeParentNo(this.GetValFromFrmByKey("TB_TreeParentNo"));
            }else if("TreeDouble".equals(valWorkModel))//双实体树
            {
            	 me.setPopValWorkModelNew(PopValWorkModel.TreeDouble);
                 me.setPopValTreeSQL(this.GetValFromFrmByKey("TB_DoubleTreeSQL"));// 树SQL
                 me.setPopValTreeParentNo(this.GetValFromFrmByKey("TB_DoubleTreeParentNo"));
                 me.setPopValDoubleTreeEntitySQL(this.GetValFromFrmByKey("TB_DoubleTreeEntitySQL")); //实体SQL
            }
            //高级属性.
            me.setW(Integer.parseInt(this.GetValFromFrmByKey("TB_Width")));
            me.setH(Integer.parseInt(this.GetValFromFrmByKey("TB_Height")));
            me.setPopValColNames(this.GetValFromFrmByKey("TB_ColNames")); //中文列名的对应.
            me.setPopValTitle(this.GetValFromFrmByKey("TB_Title")); //标题.
            me.setPopValSearchTip(this.GetValFromFrmByKey("TB_PopValSearchTip")); //关键字提示.
            me.setPopValSearchCond(this.GetValFromFrmByKey("TB_PopValSearchCond")); //查询条件.
            //数据返回格式.
            String popValFormat = this.GetValFromFrmByKey("PopValFormat");
            if("OnlyNo".equals(popValFormat))
            {
            	me.setPopValFormatNew(PopValFormat.OnlyNo);
            }else if("OnlyName".equals(popValFormat))
            {
                me.setPopValFormatNew(PopValFormat.OnlyName);
            }else if("NoName".equals(popValFormat))
            {
            	me.setPopValFormatNew(PopValFormat.NoName);
            }
            //选择模式.
            String seleModel = this.GetValFromFrmByKey("PopValSelectModel");
            if("One".equals(seleModel))
            {
            	 me.setPopValSelectModelNew(PopValSelectModel.One);
            }else
            {
            	me.setPopValSelectModelNew(PopValSelectModel.More);
            }
            me.Save();
            return "保存成功.";
        }
        catch (Exception ex)
        {
            return "@保存失败:" + ex.getMessage();
        }
    }
	  /**
	   * 初始化正则表达式界面
	   * @return
	   */
    public String RegularExpression_Init()
    {
        DataSet ds = new DataSet();
        String sql = "SELECT * FROM Sys_MapExt WHERE AttrOfOper='" + this.getKeyOfEn() + "' AND FK_MapData='" + this.getFK_MapData() + "'";
        DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
        dt.TableName = "Sys_MapExt";
        ds.Tables.add(dt);

        BP.Sys.XML.RegularExpressions res = new BP.Sys.XML.RegularExpressions();
        res.RetrieveAll();

        DataTable myDT = res.ToDataTable();
        myDT.TableName = "RE";
        ds.Tables.add(myDT);


        BP.Sys.XML.RegularExpressionDtls dtls = new BP.Sys.XML.RegularExpressionDtls();
        dtls.RetrieveAll();
        DataTable myDTDtls = dtls.ToDataTable();
        myDTDtls.TableName = "REDtl";
        ds.Tables.add(myDTDtls);

        return BP.Tools.Json.ToJson(ds);
    }
    
  /**
   * 正则表达式保存
   * @return
   */
    public String RegularExpression_Save()
    {
        //删除该字段的全部扩展设置. 
        MapExt me = new MapExt();
        me.Delete(MapExtAttr.FK_MapData, this.getFK_MapData(),
            MapExtAttr.ExtType, MapExtXmlList.RegularExpression,
            MapExtAttr.AttrOfOper, this.getKeyOfEn());

        //执行存盘.
        RegularExpression_Save_Tag("onblur");
        RegularExpression_Save_Tag("onchange");
        RegularExpression_Save_Tag("onclick");
        RegularExpression_Save_Tag("ondblclick");
        RegularExpression_Save_Tag("onkeypress");
        RegularExpression_Save_Tag("onkeyup");
        RegularExpression_Save_Tag("onsubmit");
        return "保存成功...";
    }
    private void RegularExpression_Save_Tag(String tagID)
    {
        String val = this.GetValFromFrmByKey("TB_Doc_" + tagID);
        if (StringHelper.isNullOrEmpty(val))
            return;
        MapExt me = new MapExt();
        me.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn() + "_RegularExpression_" + tagID);
        me.setFK_MapData(this.getFK_MapData());
        me.setAttrOfOper(this.getKeyOfEn());
        me.setExtType("RegularExpression");
        me.setTag(tagID);
        me.setDoc(val);
        me.setTag1(this.GetValFromFrmByKey("TB_Tag1_" + tagID));
        me.Save();
    }
    
    /**
     * 获得Form数据
     * @param key
     * @return
     */
    public String GetValFromFrmByKey(String key)
    {
        String val = getRequest().getParameter(key);
        val = val.replace("'", "~");
        return val;
    }
    
    public String getKeyOfEn()
    {
            String str = getRequest().getParameter("KeyOfEn");
            return str;
    }
    
    public String getFK_MapData() {
		String fk_mapdata = getRequest().getParameter("FK_MapData");
		if (StringHelper.isNullOrEmpty(fk_mapdata)) {
			fk_mapdata = "ND" + getFK_Node();
		}
		return fk_mapdata;
	}
}

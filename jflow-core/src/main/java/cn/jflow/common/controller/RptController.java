package cn.jflow.common.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.Log;
import BP.Sys.MapData;
import BP.Tools.Json;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/Admin/FoolFormDesigner")
public class RptController extends BaseController  {
	HttpServletRequest request;
	HttpServletResponse response;
	
	@RequestMapping(value = "/Rpt", method = RequestMethod.POST)
	protected void execute(HttpServletRequest request,HttpServletResponse response)
	{
		String doType = getRequest().getParameter("DoType");
		String msg = "";
		PrintWriter out = null;
		
			if("S0_RptList_Init".equals(doType))//初始化
			{
				msg = this.S0_RptList_Init();
			}else if("S0_RptList_Delete".equals(doType))//删除报表
			{
				msg = this.S0_RptList_Delete();
			}else if("S0_RptList_New".equals(doType))//创建一个新报表
			{
				msg = this.S0_RptList_New();
			}else if("S0_RptList_Edit".equals(doType))
			{
				msg = this.S0_RptList_Edit();//报表修改
			}else if("S0_OneRpt_Init".equals(doType))
			{
				 msg = this.S0_OneRpt_Init();//获得单个流程报表的初始化信息
			}else{
				msg = "err@没有判断的执行类型：" + doType;
			}
		try{
			out = getResponse().getWriter();
			out.write(msg);
		}catch (IOException e) {
			Log.DebugWriteError("/WF/Admin/FoolFormDesigner.Rpt.do?DoType="+doType +"err@"+e.getMessage());
		}finally{
			if(null !=out)
			{
				out.close();
			}
		}
		
	}
	
	/**
	 * 获得流程报表列表
	 * @return
	 */
    public String S0_RptList_Init()
    {
        BP.WF.Rpt.MapRpts ens = new BP.WF.Rpt.MapRpts();
        ens.Retrieve(BP.WF.Rpt.MapRptAttr.FK_Flow, this.getFK_Flow());
        if (ens.size() == 0)
        {
            BP.WF.Rpt.MapRpt en = new BP.WF.Rpt.MapRpt();
            en.setNo("ND" + Integer.parseInt(this.getFK_Flow()) + "MyRpt");
            en.setName("流程报表默认");
            en.setFK_Flow(this.getFK_Flow());
            en.Insert();
            ens.Retrieve(BP.WF.Rpt.MapRptAttr.FK_Flow, this.getFK_Flow());
        }
        
        return  Json.ToJson(ens.ToDataTableField()); 
    }
    
   /**
    * 删除报表
    * @return
    */
    public String S0_RptList_Delete()
    {
        String no = getRequest().getParameter("No");
        if (("ND" + Integer.parseInt(this.getFK_Flow()) + "MyRpt").equals(no))
            return "err@默认报表，不能删除。";

        BP.WF.Rpt.MapRpt en = new BP.WF.Rpt.MapRpt();
        en.setNo(no);
        en.Delete();
        return "@删除成功.";
    }
    
  /**
   * 获得单个流程报表的初始化信息
   * @return
   */
    public String S0_OneRpt_Init()
    {
    	String no =getRequest().getParameter("No");
        BP.WF.Rpt.MapRpt ens = new BP.WF.Rpt.MapRpt();
        ens.Retrieve(BP.WF.Rpt.MapRptAttr.No, no);
        return ens.ToJson();
        
    }
    
  /// <summary>
    /// 创建新报表
    /// </summary>
    /// <returns></returns>
    public String S0_RptList_New()
    {
        String no = getRequest().getParameter("No");
        String name = getRequest().getParameter("Name");
        String note = getRequest().getParameter("Note");
        //BP.WF.Rpt.MapRpt en = new BP.WF.Rpt.MapRpt();
        //en.No = no;
        //if (en.RetrieveFromDBSources() == 1)
        //{
        //    return "err@编号{" + en.No + "}已经存在.";
        //}

        //en.Name = name;
        //en.Note = note;
        //en.Insert();

        String mapRpt = no;
        MapData mapData = new MapData();
        mapData.setNo(mapRpt);
        mapData.setName(name);
        mapData.setNote(mapData.getNote());
        mapData.Insert();


        BP.WF.Rpt.MapRpt rpt = new BP.WF.Rpt.MapRpt();
        rpt.setNo(mapRpt);
        rpt.setFK_Flow(getFK_Flow());
        rpt.setNote(note);
        rpt.setName(name);
        Flow flow = new Flow(this.getFK_Flow());
        rpt.setPTable("".equals(flow.getPTable()) ? "ND" + this.getFK_Flow().trim().replace("0"," ") + "RPT" : flow.getPTable());

        rpt.Update();

        String sql = "";
        sql = "insert into Sys_MapAttr  select '" + no + "_'+KeyOfEn,'" + no + "',KeyOfEn,Name,DefVal,UIContralType,MyDataType,LGType,UIWidth,UIHeight,MinLen,MaxLen,UIBindKey,UIRefKey,UIRefKeyText,UIVisible,UIIsEnable,UIIsLine,UIIsInput,Idx,GroupID,IsSigan,x,y,GUID,Tag,EditType,ColSpan,AtPara from Sys_MapAttr WHERE FK_MapData IN ( SELECT 'ND' + cast(NodeID as varchar(20)) FROM WF_Node WHERE FK_Flow='"+flow.getNo()+"')";
        sql = "select * from Sys_MapAttr WHERE FK_MapData IN ( SELECT 'ND' + cast(NodeID as varchar(20)) FROM WF_Node WHERE FK_Flow='" + flow.getNo() + "')";//oracle数据错误
        DataTable dt = DBAccess.RunSQLReturnTable(sql);
        sql = "SELECT KeyOfEn FROM Sys_MapAttr WHERE FK_MapData='" + no + "'";
        DataTable dtExits = DBAccess.RunSQLReturnTable(sql);
        String pks = "@";
        for(DataRow dr : dtExits.Rows)  
            pks += dr.getValue(0) + "@";

        for(DataRow dr : dt.Rows)
        {
            String mypk = dr.getValue("MyPK").toString();
            if (pks.contains("@" + dr.getValue("KeyOfEn").toString() + "@"))
                continue;
            pks += dr.getValue("KeyOfEn").toString() + "@";
            BP.Sys.MapAttr ma = new BP.Sys.MapAttr(mypk);
            ma.setMyPK(no + "_" + ma.getKeyOfEn());
            ma.setFK_MapData(no);
            ma.setUIIsEnable(false);

            if (ma.getDefValReal().contains("@"))
            {
                /*如果是一个有变量的参数.*/
                ma.setDefVal("");
            }
            try
            {
                ma.Insert();
            }
            catch (Exception e) {
    			Log.DebugWriteError(e.getMessage());
    		}
        }

        //DoCheck_CheckRpt(no, this.FK_Flow, name, rpt.PTable);
        return "@创建成功.";
    }
    
  /**
   * 修改
   * @return
   */
    public String S0_RptList_Edit()
    {
        String no = getRequest().getParameter("No");

        BP.WF.Rpt.MapRpt en = new BP.WF.Rpt.MapRpt();
        en.setNo(no);
        en.Retrieve();

        en.setName(getRequest().getParameter("Name"));
        en.setNote(getRequest().getParameter("Note"));
        en.Update();

        return "@保存成功.";
    }
    /**
     * 公共方法取值
     * @param param
     * @return
     */
    public String GetRequestVal(String param)
    {
        try {
        	URLEncoder.encode(getRequest().getParameter(param),"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
             return param ;
     }
    
    /**
     * 获得表单的属性
     * @param key
     * @return
     */
    public String GetValFromFrmByKey(String key)
    {
        String val = getRequest().getParameter(key);
        if (val == null)
            return null;
        val = val.replace("'", "~");
        return val;
    }
    
  /**
   * 流程编号
   */
    public String getFK_Flow()
    {
            String str =getRequest().getParameter("FK_Flow");
            if(StringHelper.isNullOrEmpty(str)){
            	 return null;
            }
            return str;
           
        
    }
	
}

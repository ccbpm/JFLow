package cn.jflow.common.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.Log;
import BP.Sys.MapExt;
import BP.Sys.PopValWorkModel;
import BP.Tools.StringHelper;
import BP.Web.WebUser;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/CCWF")
public class WFCCFormController extends BaseController {
	HttpServletResponse response;
	HttpServletRequest request;
	@RequestMapping(value = "/ccform.do", method = RequestMethod.POST)
	protected void execute(HttpServletRequest request,HttpServletResponse response) {
		String doType = request.getParameter("DoType");
		String msg="";
		PrintWriter out =null;	
		if("InitPopVal".equals(doType))
		{
			msg = InitPopVal();
		}else if("InitPopValTree".equals(doType))
		{
			msg = InitPopValTree();
		}
		try {
			out = getResponse().getWriter();
			out.write(msg);
		} catch (IOException e) {
			Log.DebugWriteError("/WF/CCForm/ccform.do?DoType="+doType +"err@"+e.getMessage());
		}finally{
			if(null !=out)
			{
				out.close();
			}
		}
	}
	/**
	 * 初始化PopVal的值
	 * @return
	 */
	
    public String InitPopVal()
    {
        BP.Sys.MapExt me = new BP.Sys.MapExt();
        me.setMyPK(this.getFK_MapExt());
        me.Retrieve();

        //数据对象，将要返回的.
        DataSet ds = new DataSet();

        //获得配置信息.
        @SuppressWarnings("rawtypes")
		Hashtable ht = me.PopValToHashtable();
        DataTable dtcfg = BP.Sys.PubClass.HashtableToDataTable(ht);

        //增加到数据源.
        ds.Tables.add(dtcfg);

        if (me.getPopValWorkModelNew() == PopValWorkModel.SelfUrl)
            return "@SelfUrl" + me.getPopValUrl();

        if (me.getPopValWorkModelNew() == PopValWorkModel.TableOnly)
        {
            String sqlObjs = me.getPopValEntitySQL();
            sqlObjs = sqlObjs.replace("WebUser.No", BP.Web.WebUser.getNo());
            sqlObjs = sqlObjs.replace("@WebUser.Name", BP.Web.WebUser.getName());
            sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
            sqlObjs = this.DealExpByFromVals(sqlObjs);
            DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
            dt.TableName = "DTObjs";
            ds.Tables.add(dt);
            return BP.Tools.Json.ToJson(ds);
        }

        if (me.getPopValWorkModelNew() == PopValWorkModel.TablePage)
        {
             //分页的 
            //key
            String key = getRequest().getParameter("Key");
            if (StringHelper.isNullOrEmpty(key) == true)
                key = "";

            //取出来查询条件.
            String[] conds = me.getPopValSearchCond().split("$",0);
            
            String countSQL = me.getPopValTablePageSQLCount();

            //固定参数.
            countSQL = countSQL.replace("WebUser.No", BP.Web.WebUser.getNo());
            countSQL = countSQL.replace("@WebUser.Name", BP.Web.WebUser.getName());
            countSQL = countSQL.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
            countSQL = countSQL.replace("@Key", key);
            countSQL = this.DealExpByFromVals(countSQL);

            //替换其他参数.
            for(String cond : conds)
            {
                if (cond == null || "".equals(cond))
                    continue;

                //参数.
                String para = cond.substring(5, cond.indexOf("#")-5);
                String val = getRequest().getParameter(para);
                if (StringHelper.isNullOrEmpty(val))
                {
                    if (cond.contains("ListSQL") == true || cond.contains("EnumKey") == true)
                        val = "all";
                    else
                        val = "";
                }

                if ("all".equals(val))
                {

                    countSQL = countSQL.replace( para+"=@" + para, "1=1");
                    countSQL = countSQL.replace( para + "='@" + para+"'", "1=1");

                    //找到para 前面表的别名   如 t.1=1 把t. 去掉
                    int startIndex = 0;
                    while (startIndex != -1 && startIndex < countSQL.length())
                    {
                        int index = countSQL.indexOf("1=1", startIndex + 1);
                        if (index > 0 && countSQL.substring(startIndex, index - startIndex).trim().endsWith("."))
                        {
                            int lastBlankIndex = countSQL.substring(startIndex, index - startIndex).lastIndexOf(" ");


                            countSQL = countSQL.replace(String.valueOf(lastBlankIndex + startIndex+1), String.valueOf(index - lastBlankIndex-1));

                            startIndex = (startIndex + lastBlankIndex) + 3;
                        }
                        else
                        {
                            startIndex = index;
                        }
                    }
                }
                else
                {
                    //要执行两次替换有可能是，有引号.
                    countSQL = countSQL.replace("@" + para , val);
                }
            }
             

            String count = String.valueOf(BP.DA.DBAccess.RunSQLReturnValInt(countSQL, 0));

            //pageSize
            String pageSize = getRequest().getParameter("pageSize");
            if (StringHelper.isNullOrEmpty(pageSize))
                pageSize = "10";

            //pageIndex
            String pageIndex = getRequest().getParameter("pageIndex");
            if (StringHelper.isNullOrEmpty(pageIndex))
                pageIndex = "1";

            String sqlObjs = me.getPopValTablePageSQL();
            sqlObjs = sqlObjs.replace("WebUser.No", BP.Web.WebUser.getNo());
            sqlObjs = sqlObjs.replace("@WebUser.Name", BP.Web.WebUser.getName());
            sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
            sqlObjs = sqlObjs.replace("@Key", key);

            //三个固定参数.
            sqlObjs = sqlObjs.replace("@PageCount", (String.valueOf((Integer.parseInt(pageIndex) - 1) * Integer.parseInt(pageSize))));
            sqlObjs = sqlObjs.replace("@PageSize", pageSize);
            sqlObjs = sqlObjs.replace("@PageIndex", pageIndex);
            sqlObjs = this.DealExpByFromVals(sqlObjs);

            //替换其他参数.
            for(String cond : conds)
            {
                if (cond == null || "".equals(cond))
                    continue;

                //参数.
                String para = cond.substring(5, cond.indexOf("#")-5);
                String val =getRequest().getParameter(para);
                if (StringHelper.isNullOrEmpty(val))
                {
                    if (cond.contains("ListSQL") == true || cond.contains("EnumKey") == true)
                        val = "all";
                    else
                        val = "";
                }
                if ("all".equals(val))
                {
                    sqlObjs = sqlObjs.replace(para + "=@" + para, "1=1");
                    sqlObjs = sqlObjs.replace(para + "='@" + para + "'", "1=1");

                    int startIndex = 0;
                    while (startIndex != -1 && startIndex < sqlObjs.length())
                    {
                        int index = sqlObjs.indexOf("1=1", startIndex+1);
                        if (index > 0 && sqlObjs.substring(startIndex, index - startIndex).trim().endsWith("."))
                        {
                            int lastBlankIndex = sqlObjs.substring(startIndex, index - startIndex).lastIndexOf(" ");


                            sqlObjs= sqlObjs.replace(String.valueOf(lastBlankIndex  + startIndex+1) , String.valueOf(index - lastBlankIndex-1));
                            
                            startIndex = (startIndex + lastBlankIndex) + 3;
                        }
                        else
                        {
                            startIndex = index;
                        }
                    }
                }
                else
                {
                    //要执行两次替换有可能是，有引号.
                    sqlObjs = sqlObjs.replace("@" + para, val);
                }
            }
            DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
            dt.TableName = "DTObjs";
            ds.Tables.add(dt);
            DataTable dtCount = new DataTable("DTCout");
            dtCount.TableName = "DTCout";
            dtCount.Columns.Add("Count");
            dtCount.Rows.AddRow(count);
            ds.Tables.add(dtCount);


            //处理查询条件.
            //$Para=Dept#Label=所在班级#ListSQL=Select No,Name FROM Port_Dept WHERE No='WebUser.No'
            //$Para=XB#Label=性别#EnumKey=XB
            //$Para=DTFrom#Label=注册日期从#DefVal=@Now-30
            //$Para=DTTo#Label=到#DefVal=@Now
             for(String cond : conds)
             {
                 if (StringHelper.isNullOrEmpty(cond) == true)
                     continue;
                 String sql = null;
                 if (cond.contains("#ListSQL=") == true)
                 {
                     sql = cond.substring(cond.indexOf("ListSQL")+8);
                     sql = sql.replace("WebUser.No",WebUser.getNo());
                     sql = sql.replace("@WebUser.Name", WebUser.getName());
                     sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
                     sql = this.DealExpByFromVals(sql);
                 }
                 if (cond.contains("#EnumKey=") == true)
                 {
                     String enumKey = cond.substring(cond.indexOf("EnumKey") + 8);
                     sql = "SELECT IntKey AS No, Lab as Name FROM Sys_Enum WHERE EnumKey='" + enumKey + "'";
                 }
                 //处理日期的默认值
                 //DefVal=@Now-30
                 //if (cond.Contains("@Now"))
                 //{
                 //    int nowIndex = cond.IndexOf(cond);
                 //    if (cond.Trim().Length - nowIndex > 5)
                 //    {
                 //        char optStr = cond.Trim()[nowIndex + 5];
                 //        int day = 0;
                 //        if (int.TryParse(cond.Trim().substing(nowIndex + 6), out day)) {
                 //            cond = cond.substing(0, nowIndex) + DateTime.Now.AddDays(-1 * day).ToString("yyyy-MM-dd HH:mm");
                 //        }
                 //    }
                 //}

                 if (sql == null)
                     continue;
                 //参数.
                 String para = cond.substring(5, cond.indexOf("#")-5);
					try {
						 if (ds.Tables.contains(para) == true)
						throw new Exception("@配置的查询,参数名有冲突不能命名为:"+para);
					} catch (Exception e) {
						e.printStackTrace();
					}
                 //查询出来数据，就把他放入到dataset里面.
                 DataTable dtPara = BP.DA.DBAccess.RunSQLReturnTable(sql);
                 dtPara.TableName = para;
                 ds.Tables.add(dtPara); //加入到参数集合.
             }
            return BP.Tools.Json.ToJson(ds);
        }
        if (me.getPopValWorkModelNew() == PopValWorkModel.Group)
        {
            String sqlObjs = me.getPopValGroupSQL();
            if (sqlObjs.length() > 10)
            {
                sqlObjs = sqlObjs.replace("WebUser.No", BP.Web.WebUser.getNo());
                sqlObjs = sqlObjs.replace("@WebUser.Name", BP.Web.WebUser.getName());
                sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
                sqlObjs = this.DealExpByFromVals(sqlObjs);
                DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
                dt.TableName = "DTGroup";
                ds.Tables.add(dt);
            }
            sqlObjs = me.getPopValEntitySQL();
            if (sqlObjs.length() > 10)
            {
                sqlObjs = sqlObjs.replace("WebUser.No", BP.Web.WebUser.getNo());
                sqlObjs = sqlObjs.replace("@WebUser.Name", BP.Web.WebUser.getName());
                sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
                sqlObjs = this.DealExpByFromVals(sqlObjs);
                DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
                dt.TableName = "DTEntity";
                ds.Tables.add(dt);
            }
            return BP.Tools.Json.ToJson(ds);
        }
        //返回数据.
        return BP.Tools.Json.ToJson(ds);
    }
    /**
     * 初始化树的接口
     * @return
     */
    public String InitPopValTree()
    {
        String mypk = getRequest().getParameter("FK_MapExt");

        MapExt me = new MapExt();
        me.setMyPK(mypk);
        me.Retrieve();

        //获得配置信息.
        Hashtable ht = me.PopValToHashtable();
        DataTable dtcfg = BP.Sys.PubClass.HashtableToDataTable(ht);

        String parentNo = getRequest().getParameter("ParentNo");
        if (parentNo == null)
            parentNo = me.getPopValTreeParentNo();

        DataSet resultDs = new DataSet();
        String sqlObjs = me.getPopValTreeSQL();
        sqlObjs = sqlObjs.replace("WebUser.No", BP.Web.WebUser.getNo());
        sqlObjs = sqlObjs.replace("@WebUser.Name", BP.Web.WebUser.getName());
        sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
        sqlObjs = sqlObjs.replace("@ParentNo", parentNo);
        sqlObjs = this.DealExpByFromVals(sqlObjs);

        DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
        dt.TableName = "DTObjs";
        resultDs.Tables.add(dt);

        //doubleTree 
        if (me.getPopValWorkModelNew() == PopValWorkModel.TreeDouble && parentNo != me.getPopValTreeParentNo())
        {
            sqlObjs = me.getPopValDoubleTreeEntitySQL();
            sqlObjs = sqlObjs.replace("WebUser.No", BP.Web.WebUser.getNo());
            sqlObjs = sqlObjs.replace("@WebUser.Name", BP.Web.WebUser.getName());
            sqlObjs = sqlObjs.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
            sqlObjs = sqlObjs.replace("@ParentNo", parentNo);
            sqlObjs = this.DealExpByFromVals(sqlObjs);
            DataTable entityDt = BP.DA.DBAccess.RunSQLReturnTable(sqlObjs);
            entityDt.TableName = "DTEntitys";
            resultDs.Tables.add(entityDt);
        }
        return BP.Tools.Json.ToJson(resultDs);
    }
    /**
     * 处理SQL的表达式.
     * @param exp
     * @return
     */
    public String DealExpByFromVals(String exp)
    {
       for(String strKey : getRequest().getParameterValues("FK_MapExt"))
        {
            if (exp.contains("@") == false)
            {
            	 return exp; 
            }
            String str = strKey.replace("TB_", "").replace("CB_", "").replace("DDL_", "").replace("RB_", "");

            exp = exp.replace("@" + str,getRequest().getParameter(strKey));
        }
        return exp;
    }
	
}

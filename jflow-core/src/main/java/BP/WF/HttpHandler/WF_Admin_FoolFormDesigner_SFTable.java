package BP.WF.HttpHandler;

import BP.WF.HttpHandler.Base.WebContralBase;
import java.util.ArrayList;

import org.springframework.util.StringUtils;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.Sys.CodeStruct;
import BP.Sys.SFDBSrc;
import BP.Sys.SFDBSrcs;
import BP.Sys.SFTable;
import BP.Sys.SFTables;
import BP.Sys.SrcType;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
public class WF_Admin_FoolFormDesigner_SFTable extends WebContralBase{
	///#region 执行父类的重写方法.
    /// <summary>
    /// 默认执行的方法
    /// </summary>
    /// <returns></returns>
    protected String DoDefaultMethod()
    {
    	if (this.getDoType().equals("DtlFieldUp")) //字段上移
		{
				return "执行成功.";


		}
		else
		{
		}

        //找不不到标记就抛出异常.
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" + this.getRequest().getRequestURL());
    }
    ///#endregion 执行父类的重写方法.

    ///#region xxx 界面 .
    /// <summary>
    ///  初始化sf0. @于庆海，新方法.
    /// </summary>
    /// <returns></returns>
    public String SF0_Init()
    {
        String cl = "BP.En.Entities";
        ArrayList al = ClassFactory.GetObjects(cl);

        //定义容器.
        DataTable dt = new DataTable();
        dt.Columns.Add("No");
        dt.Columns.Add("Name");

        SFTables sfs = new SFTables();
        sfs.RetrieveAll();

        for(Object obj: al)
        {
            Entities ens = (Entities) obj;
            if (ens == null)
                continue;

            try
            {
                Entity en = ens.getGetNewEntity();
                if (en == null)
                    continue;

                if (en.getEnMap().getAttrs().Contains("No") == false)
                    continue;

                if (sfs.Contains(ens.toString()) == true)
                    continue;

                DataRow dr = dt.NewRow();
                dr.setValue("No", ens.toString());

                if (en.getIsTreeEntity())
                	dr.setValue("Name", en.getEnMap().getEnDesc() + "(树结构) " + ens.toString());
                else
                	dr.setValue("Name", en.getEnMap().getEnDesc() + " " + ens.toString());
                dt.Rows.add(dr);
            }
            catch(Exception e){

            }
        }
        return BP.Tools.Json.ToJson(dt);
    }
    public String SF0_Save()
    {
        return "保存成功.";
    }
    ///#endregion xxx 界面方法.

    ///#region 表或者视图 .
    /// <summary>
    ///  初始化sf2.
    /// </summary>
    /// <returns></returns>
    public String SF2_Init()
    {
        SFDBSrcs srcs = new SFDBSrcs();
        srcs.RetrieveAll();
        return srcs.ToJson();
    }

    public String SF2_GetTVs()
    {
        String src = this.GetRequestVal("src");

        SFDBSrc sr = new SFDBSrc(src);
        DataTable dt = sr.GetTables();

        return BP.Tools.Json.ToJson(dt);
    }

    public String SF2_GetCols()
    {
        String src = this.GetRequestVal("src");
        String table = this.GetRequestVal("table");

        if (StringHelper.isNullOrEmpty(src))
			try {
				throw new Exception("err@参数不正确");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        if (StringHelper.isNullOrEmpty(table))
        {
            return "[]";
        }

        SFDBSrc sr = new SFDBSrc(src);
        DataTable dt = sr.GetColumns(table);

        for(DataRow r : dt.Rows)
        {
        	if(SystemConfig.getAppCenterDBType() == BP.DA.DBType.Oracle){
				r.setValue("NAME", r.getValue("NO") + (r.getValue("NAME") == null || "".equals(r.getValue("NAME")) || StringUtils.isEmpty(r.getValue("NAME").toString()) ? "" : String.format("[%1$s]", r.getValue("NAME"))));
			}else{
				r.setValue("Name", r.getValue("No") + (r.getValue("Name") == null || "".equals(r.getValue("Name")) || StringUtils.isEmpty(r.getValue("Name").toString()) ? "" : String.format("[%1$s]", r.getValue("Name"))));
			}
        	
           
        }

        return BP.Tools.Json.ToJson(dt);
    }

    public String SF2_Save()
    {
        SFTable sf = new SFTable();
        sf.setNo(this.GetValFromFrmByKey("No"));
        if (sf.getIsExits())
            return "err@标记:" + sf.getNo() + "已经存在.";

        sf.setName(this.GetValFromFrmByKey("Name"));
        sf.setFK_SFDBSrc(this.GetValFromFrmByKey("FK_DBSrc"));
        sf.setSrcTable(this.GetValFromFrmByKey("SrcTable"));
        sf.setCodeStruct(CodeStruct.forValue(this.GetValIntFromFrmByKey("CodeStruct")));
        sf.setColumnValue(this.GetValFromFrmByKey("ColumnValue"));
        sf.setColumnText(this.GetValFromFrmByKey("ColumnText"));
        if (sf.getCodeStruct() == CodeStruct.Tree)
        {
            sf.setParentValue(this.GetValFromFrmByKey("ParentValue"));
            sf.setDefVal(this.GetValFromFrmByKey("RootValue"));
        }
        sf.setSelectStatement(this.GetValFromFrmByKey("Selectstatement"));
        sf.setSrcType(SrcType.TableOrView);
        sf.setFK_Val("FK_" + sf.getNo());
        sf.Save();

        return "保存成功！";
    }

    //#endregion xxx 界面方法.
}

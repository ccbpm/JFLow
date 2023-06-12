package bp.cloud.sys;

import bp.da.*;
import bp.difference.ContextHolderUtils;
import bp.en.*;
import bp.sys.*;
import bp.tools.StringHelper;
import bp.tools.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.StringReader;
import java.util.Enumeration;

/**
 * 用户自定义表
 */
public class SFTable extends EntityNoName {

    ///#region 数据源属性.

    /**
     * 获得外部数据表
     */
    public final DataTable GenerHisDataTable() throws Exception {
        return GenerHisDataTable(null);
    }
//ORIGINAL LINE: public DataTable GenerHisDataTable(Hashtable ht = null)
    public final DataTable GenerHisDataTable(java.util.Hashtable ht) throws Exception {

        //创建数据源.
        SFDBSrc src = new SFDBSrc(this.getFK_SFDBSrc());


        ///#region BP类
        if (this.getSrcType() == bp.sys.SrcType.BPClass) {
            Entities ens = ClassFactory.GetEns(this.getNo());
            return ens.RetrieveAllToTable();
        }

        ///#endregion


        ///#region  WebServices
        // this.SrcType == bp.sys.SrcType.WebServices，by liuxc 
        //暂只考虑No,Name结构的数据源，2015.10.04，added by liuxc
        if (this.getSrcType() == bp.sys.SrcType.WebServices) {

            String[] td = this.getTableDesc().split("[,]", -1); //接口名称,返回类型
            String tempVar = this.getSelectStatement();

            String[] ps = ((tempVar != null) ? tempVar : "").split("[&]", -1);
            java.util.ArrayList args = new java.util.ArrayList();
            String[] pa = null;


            for (String p : ps) {
                if (StringUtils.isBlank(p)) {
                    continue;
                }

                pa = p.split("[=]", -1);
                if (pa.length != 2) {
                    continue;
                }

                //此处要SL中显示表单时，会有问题
                try {
                    if (pa[1].contains("@WebUser.No")) {
                        pa[1] = pa[1].replace("@WebUser.No", bp.web.WebUser.getNo());
                    }
                    if (pa[1].contains("@WebUser.Name")) {
                        pa[1] = pa[1].replace("@WebUser.Name", bp.web.WebUser.getName());
                    }
                    if (pa[1].contains("@WebUser.FK_Dept")) {
                        pa[1] = pa[1].replace("@WebUser.FK_Dept", bp.web.WebUser.getFK_Dept());
                    }
                    if (pa[1].contains("@WebUser.FK_DeptName")) {
                        pa[1] = pa[1].replace("@WebUser.FK_DeptName", bp.web.WebUser.getFK_DeptName());
                    }
                } catch (java.lang.Exception e) {
                }

                if (pa[1].contains("@WorkID")) {
                    pa[1] = pa[1].replace("@WorkID", ((bp.sys.Glo.getRequest().getParameter("WorkID")) != null) ? bp.sys.Glo.getRequest().getParameter("WorkID") : "");
                }
                if (pa[1].contains("@NodeID")) {
                    pa[1] = pa[1].replace("@NodeID", ((bp.sys.Glo.getRequest().getParameter("NodeID")) != null) ? bp.sys.Glo.getRequest().getParameter("NodeID") : "");
                }
                if (pa[1].contains("@FK_Node")) {
                    pa[1] = pa[1].replace("@FK_Node", ((bp.sys.Glo.getRequest().getParameter("FK_Node")) != null) ? bp.sys.Glo.getRequest().getParameter("FK_Node") : "");
                }
                if (pa[1].contains("@FK_Flow")) {
                    pa[1] = pa[1].replace("@FK_Flow", ((bp.sys.Glo.getRequest().getParameter("FK_Flow")) != null) ? bp.sys.Glo.getRequest().getParameter("FK_Flow") : "");
                }
                if (pa[1].contains("@FID")) {
                    pa[1] = pa[1].replace("@FID", ((bp.sys.Glo.getRequest().getParameter("FID")) != null) ? bp.sys.Glo.getRequest().getParameter("FID") : "");
                }

                args.add(pa[1]);
            }

            Object result = InvokeWebService(src.getIP(), td[0], args.toArray());


//			switch (td[1])
//ORIGINAL LINE: case "DataSet":
            if (td[1].equals("DataSet")) {
                return result == null ? new DataTable() : ((DataSet) ((result instanceof DataSet) ? result : null)).Tables.get(0);
            }
//ORIGINAL LINE: case "DataTable":
            else if (td[1].equals("DataTable")) {
                return (DataTable) ((result instanceof DataTable) ? result : null);
            }
//ORIGINAL LINE: case "Json":
            else if (td[1].equals("Json")) {

//                var jdata = LitJson.JsonMapper.ToObject((String) ((result instanceof String) ? result : null));
                String jsonResult = String.valueOf(((result instanceof String) ? result : null));
                if (!jsonResult.matches("\\[.*\\]")) {
                    throw new RuntimeException("@返回的JSON格式字符串“" + ((result != null) ? result : "") + "”不正确");
                }
                JSONArray jdata = new JSONArray((String) ((result instanceof String) ? result : null));

                DataTable dt = new DataTable();
                dt.Columns.Add("No", String.class);
                dt.Columns.Add("Name", String.class);

                for (int i = 0; i < jdata.length(); i++) {
                    dt.Rows.AddDatas(jdata.getJSONObject(i).getString("No"), jdata.getJSONObject(i).getString("Name"));
                }

                return dt;
            }
//ORIGINAL LINE: case "Xml":
            else if (td[1].equals("Xml")) {
                if (result == null || StringUtils.isBlank(result.toString())) {
                    throw new RuntimeException("@返回的XML格式字符串不正确。");
                }

                Document document = DocumentHelper.parseText(String.valueOf(result));

                Element root = document.getRootElement();

                DataTable dt = new DataTable();
                dt.Columns.Add("No", String.class);
                dt.Columns.Add("Name", String.class);
                for (Object el : root.elements()){
                    el = el instanceof Element ? (Element)el : null;
                    if(DataType.IsNullOrEmpty(el))
                        throw new RuntimeException("err@类型转换出现错误。");
                    dt.Rows.AddDatas(((Element) el).selectSingleNode("No"), ((Element) el).selectSingleNode("Name"));
                }


//                XmlDocument xml = new XmlDocument();
//                xml.LoadXml((String) ((result instanceof String) ? result : null));
//
//                XmlNode root = null;
//
//                if (xml.ChildNodes.size() < 2) {
//                    root = xml.ChildNodes[0];
//                } else {
//                    root = xml.ChildNodes[1];
//                }
//
//                dt = new DataTable();
//                dt.Columns.Add("No", String.class);
//                dt.Columns.Add("Name", String.class);
//
//                for (XmlNode node : root.ChildNodes) {
//                    dt.Rows.AddRow(node.SelectSingleNode("No").InnerText, node.SelectSingleNode("Name").InnerText);
//                }

                return dt;
            } else {
                throw new RuntimeException("@不支持的返回类型" + td[1]);
            }
        }

        ///#endregion


        ///#region SQL查询.外键表/视图，edited by liuxc,2016-12-29
        if (this.getSrcType() == bp.sys.SrcType.TableOrView) {
            String sql = "SELECT " + this.getColumnValue() + " No, " + this.getColumnText() + " Name";
            if (this.getCodeStruct() == bp.sys.CodeStruct.Tree) {
                sql += ", " + this.getParentValue() + " ParentNo";
            }

            sql += " FROM " + this.getSrcTable();
            return src.RunSQLReturnTable(sql);
        }

        ///#endregion SQL查询.外键表/视图，edited by liuxc,2016-12-29


        ///#region 动态SQL，edited by liuxc,2016-12-29
        if (this.getSrcType() == bp.sys.SrcType.SQL) {
            String runObj = this.getSelectStatement();

            if (DataType.IsNullOrEmpty(runObj)) {
                throw new RuntimeException("@外键类型SQL配置错误," + this.getNo() + " " + this.getName() + " 是一个(SQL)类型(" + this.GetValStrByKey("SrcType") + ")，但是没有配置sql.");
            }

            if (runObj == null) {
                runObj = "";
            }

            runObj = runObj.replace("~", "'");
            runObj = runObj.replace("/#", "+"); //为什么？
            runObj = runObj.replace("/$", "-"); //为什么？
            if (runObj.contains("@WebUser.No")) {
                runObj = runObj.replace("@WebUser.No", bp.web.WebUser.getNo());
            }

            if (runObj.contains("@WebUser.Name")) {
                runObj = runObj.replace("@WebUser.Name", bp.web.WebUser.getName());
            }

            if (runObj.contains("@WebUser.FK_Dept")) {
                runObj = runObj.replace("@WebUser.FK_Dept", bp.web.WebUser.getFK_Dept());
            }

            if (runObj.contains("@") == true && ht == null) {
                throw new RuntimeException("@外键类型SQL错误," + runObj + "部分查询条件没有被替换.");
            }

            if (runObj.contains("@") == true && ht != null) {
                for (Object key : ht.keySet()) {
                    //值为空或者null不替换
                    if (ht.get(key) == null || ht.get(key).equals("") == true) {
                        continue;
                    }

                    if (runObj.contains("@" + key)) {
                        runObj = runObj.replace("@" + key, ht.get(key).toString());
                    }

                    //不包含@则返回SQL语句
                    if (runObj.contains("@") == false) {
                        break;
                    }
                }
            }

            if (runObj.contains("@") && bp.difference.SystemConfig.getIsBSsystem() == true) {
                //如果是bs
//                for (String key : bp.sys.Glo.getRequest().getParameterNames()) {
//                    if (DataType.IsNullOrEmpty(key)) {
//                        continue;
//                    }
//                    runObj = runObj.replace("@" + key, HttpContextHelper.RequestParams(key));
//                }
                Enumeration enu = bp.sys.base.Glo.getRequest().getParameterNames();
                while (enu.hasMoreElements()) {
                    // 判断是否有内容，hasNext()
                    String k = (String) enu.nextElement();
                    if (bp.da.DataType.IsNullOrEmpty(k)) {
                        continue;
                    }
                    runObj = runObj.replace("@" + k, bp.sys.base.Glo.getRequest().getParameter(k));
                }
            }

            if (runObj.contains("@") == true) {
                throw new RuntimeException("@外键类型SQL错误," + runObj + "部分查询条件没有被替换.");
            }

            DataTable dt = src.RunSQLReturnTable(runObj);
            return dt;
        }

        ///#endregion


        ///#region 自定义表.
        if (this.getSrcType() == bp.sys.SrcType.CreateTable) {
            String sql = "SELECT No, Name FROM " + this.getNo();
            return src.RunSQLReturnTable(sql);
        }
        if (this.getSrcType() == bp.sys.SrcType.SysDict) {
            String sql = "SELECT MyPK, BH AS No, Name FROM Sys_SFTableDtl where FK_SFTable='" + this.getNo() + "'";
            return src.RunSQLReturnTable(sql);
        }

        ///#endregion

        return null;

    }

    public final String GenerHisJson() throws Exception {
        return bp.tools.Json.ToJson(this.GenerHisDataTable());
    }

    /**
     * 自动生成编号
     *
     * @return
     */
    public final String GenerSFTableNewNo() {
        String table = this.getSrcTable();
        try {
            String sql = null;
            String field = "No";
            switch (this.getEnMap().getEnDBUrl().getDBType()) {
                case MSSQL:
                    sql = "SELECT CONVERT(INT, MAX(CAST(" + field + " as int)) )+1 AS No FROM " + table;
                    break;
                case PostgreSQL:
                case UX:
                    sql = "SELECT to_number( MAX(" + field + ") ,'99999999')+1   FROM " + table;
                    break;
                case Oracle:
                case KingBaseR3:
                case KingBaseR6:
                    sql = "SELECT MAX(" + field + ") +1 AS No FROM " + table;
                    break;
                case MySQL:
                    sql = "SELECT CONVERT(MAX(CAST(" + field + " AS SIGNED INTEGER)),SIGNED) +1 AS No FROM " + table;
                    break;
                case Informix:
                    sql = "SELECT MAX(" + field + ") +1 AS No FROM " + table;
                    break;
                case Access:
                    sql = "SELECT MAX( [" + field + "]) +1 AS  No FROM " + table;
                    break;
                default:
                    throw new RuntimeException("error");
            }
            String str = String.valueOf(DBAccess.RunSQLReturnValInt(sql, 1));
            if (DataType.IsNullOrEmpty(str) || str.equals("0")) {
                str = "1";
            }
            return StringHelper.padLeft(str, 3, '0');
        } catch (RuntimeException e) {
            return "";
        }
    }

    /**
     * 实例化 WebServices
     *
     * @param url        WebServices地址
     * @param methodname 调用的方法
     * @param args       把webservices里需要的参数按顺序放到这个object[]里
     */
    public final Object InvokeWebService(String url, String methodname, Object[] args) {
        return null;
//             TODO 2019-07-25 为了合并core，注释掉
//                        //这里的namespace是需引用的webservices的命名空间，在这里是写死的，大家可以加一个参数从外面传进来。
//                        string @namespace = "BP.RefServices";
//                        try
//                        {
//                            if (url.EndsWith(".asmx"))
//                                url += "?wsdl";
//                            else if (url.EndsWith(".svc"))
//                                url += "?singleWsdl";
//
//                            //获取WSDL
//                            WebClient wc = new WebClient();
//                            Stream stream = wc.OpenRead(url);
//                            ServiceDescription sd = ServiceDescription.Read(stream);
//                            string classname = sd.Services[0].Name;
//                            ServiceDescriptionImporter sdi = new ServiceDescriptionImporter();
//                            sdi.AddServiceDescription(sd, "", "");
//                            CodeNamespace cn = new CodeNamespace(@namespace);
//
//                            //生成客户端代理类代码
//                            CodeCompileUnit ccu = new CodeCompileUnit();
//                            ccu.Namespaces.Add(cn);
//                            sdi.Import(cn, ccu);
//                            CSharpCodeProvider csc = new CSharpCodeProvider();
//                            ICodeCompiler icc = csc.CreateCompiler();
//
//                            //设定编译参数
//                            CompilerParameters cplist = new CompilerParameters();
//                            cplist.GenerateExecutable = false;
//                            cplist.GenerateInMemory = true;
//                            cplist.ReferencedAssemblies.Add("System.dll");
//                            cplist.ReferencedAssemblies.Add("System.XML.dll");
//                            cplist.ReferencedAssemblies.Add("System.Web.Services.dll");
//                            cplist.ReferencedAssemblies.Add("System.Data.dll");
//
//                            //编译代理类
//                            CompilerResults cr = icc.CompileAssemblyFromDom(cplist, ccu);
//                            if (true == cr.Errors.HasErrors)
//                            {
//                                System.Text.StringBuilder sb = new System.Text.StringBuilder();
//                                foreach (System.CodeDom.Compiler.CompilerError ce in cr.Errors)
//                                {
//                                    sb.Append(ce.ToString());
//                                    sb.Append(System.Environment.NewLine);
//                                }
//                                throw new Exception(sb.ToString());
//                            }
//
//                            //生成代理实例，并调用方法
//                            System.Reflection.Assembly assembly = cr.CompiledAssembly;
//                            Type t = assembly.GetType(@namespace + "." + classname, true, true);
//                            object obj = Activator.CreateInstance(t);
//                            System.Reflection.MethodInfo mi = t.GetMethod(methodname);
//
//                            return mi.Invoke(obj, args);
//                        }
//                        catch
//                        {
//                            return null;
//                        }
//            
    }

    ///#endregion


    ///#region 链接到其他系统获取数据的属性

    /**
     * 数据源
     */
    public final String getFK_SFDBSrc() {
        return this.GetValStringByKey(SFTableAttr.FK_SFDBSrc);
    }

    public final void setFK_SFDBSrc(String value) {
        this.SetValByKey(SFTableAttr.FK_SFDBSrc, value);
    }

    public final String getOrgNo() {
        return this.GetValStringByKey(SysEnumAttr.OrgNo);
    }

    public final void setOrgNo(String value) {
        this.SetValByKey(SysEnumAttr.OrgNo, value);
    }

    public final String getFK_SFDBSrcT() {
        return this.GetValRefTextByKey(SFTableAttr.FK_SFDBSrc);
    }

    /**
     * 数据缓存时间
     */
    public final String getRootVal() {
        return this.GetValStringByKey(SFTableAttr.RootVal);
    }

    public final void setRootVal(String value) {
        this.SetValByKey(SFTableAttr.RootVal, value);
    }

    /**
     * 同步间隔
     */
    public final int getCashMinute() {
        return this.GetValIntByKey(SFTableAttr.CashMinute);
    }

    public final void setCashMinute(int value) {
        this.SetValByKey(SFTableAttr.CashMinute, value);
    }

    /**
     * 物理表名称
     */
    public final String getSrcTable() {
        String str = this.GetValStringByKey(SFTableAttr.SrcTable);
        if (DataType.IsNullOrEmpty(str)) {
            return this.getNo();
        }
        return str;
    }

    public final void setSrcTable(String value) {
        this.SetValByKey(SFTableAttr.SrcTable, value);
    }

    /**
     * 值/主键字段名
     */
    public final String getColumnValue() {
        return this.GetValStringByKey(SFTableAttr.ColumnValue);
    }

    public final void setColumnValue(String value) {
        this.SetValByKey(SFTableAttr.ColumnValue, value);
    }

    /**
     * 显示字段/显示字段名
     */
    public final String getColumnText() {
        return this.GetValStringByKey(SFTableAttr.ColumnText);
    }

    public final void setColumnText(String value) {
        this.SetValByKey(SFTableAttr.ColumnText, value);
    }

    /**
     * 父结点字段名
     */
    public final String getParentValue() {
        return this.GetValStringByKey(SFTableAttr.ParentValue);
    }

    public final void setParentValue(String value) {
        this.SetValByKey(SFTableAttr.ParentValue, value);
    }

    /**
     * 查询语句
     */
    public final String getSelectStatement() {
        return this.GetValStringByKey(SFTableAttr.SelectStatement);
    }

    public final void setSelectStatement(String value) {
        this.SetValByKey(SFTableAttr.SelectStatement, value);
    }

    /**
     * 加入日期
     */
    public final String getRDT() {
        return this.GetValStringByKey(SFTableAttr.RDT);
    }

    public final void setRDT(String value) {
        this.SetValByKey(SFTableAttr.RDT, value);
    }

    ///#endregion


    ///#region 属性

    /**
     * 是否是类
     */
    public final boolean getIsClass() {
        if (this.getNo().contains(".")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否是树形实体?
     */
    public final boolean getIsTree() {
        if (this.getCodeStruct() == bp.sys.CodeStruct.NoName) {
            return false;
        }
        return true;
    }

    /**
     * 数据源类型
     */
    public final SrcType getSrcType() {
        if (this.getNo().contains("BP.") == true) {
            return getSrcType().BPClass;
        } else {
            SrcType src = SrcType.forValue(this.GetValIntByKey(SFTableAttr.SrcType));
            if (src == bp.sys.SrcType.BPClass) {
                return bp.sys.SrcType.CreateTable;
            }
            return src;
        }
    }

    public final void setSrcType(SrcType value) {
        this.SetValByKey(SFTableAttr.SrcType, value.getValue());
    }

    /**
     * 数据源类型名称
     */
    public final String getSrcTypeText() {
        switch (this.getSrcType()) {
            case TableOrView:
                if (this.getIsClass()) {
                    return "<img src='/WF/Img/Class.png' width='16px' broder='0' />实体类";
                } else {
                    return "<img src='/WF/Img/Table.gif' width='16px' broder='0' />表/视图";
                }
            case SQL:
                return "<img src='/WF/Img/SQL.png' width='16px' broder='0' />SQL表达式";
            case WebServices:
                return "<img src='/WF/Img/WebServices.gif' width='16px' broder='0' />WebServices";
            default:
                return "";
        }
    }

    /**
     * 字典表类型
     * <p>0：NoName类型</p>
     * <p>1：NoNameTree类型</p>
     * <p>2：NoName行政区划类型</p>
     */
    public final CodeStruct getCodeStruct() {
        return CodeStruct.forValue(this.GetValIntByKey(SFTableAttr.CodeStruct));
    }

    public final void setCodeStruct(CodeStruct value) {
        this.SetValByKey(SFTableAttr.CodeStruct, value.getValue());
    }

    /**
     * 编码类型
     */
    public final String getCodeStructT() {
        return this.GetValRefTextByKey(SFTableAttr.CodeStruct);
    }

    /**
     * 值
     */
    public final String getFK_Val() {
        return this.GetValStringByKey(SFTableAttr.FK_Val);
    }

    public final void setFK_Val(String value) {
        this.SetValByKey(SFTableAttr.FK_Val, value);
    }

    /**
     * 描述
     */
    public final String getTableDesc() {
        return this.GetValStringByKey(SFTableAttr.TableDesc);
    }

    public final void setTableDesc(String value) {
        this.SetValByKey(SFTableAttr.TableDesc, value);
    }

    /**
     * 默认值
     */
    public final String getDefVal() {
        return this.GetValStringByKey(SFTableAttr.DefVal);
    }

    public final void setDefVal(String value) {
        this.SetValByKey(SFTableAttr.DefVal, value);
    }

    public final EntitiesNoName getHisEns() throws Exception {
        if (this.getIsClass()) {
            EntitiesNoName ens = (EntitiesNoName) bp.en.ClassFactory.GetEns(this.getNo());
            ens.RetrieveAll();
            return ens;
        }

        bp.en.GENoNames ges = new GENoNames(this.getNo(), this.getName());
        ges.RetrieveAll();
        return ges;
    }

    ///#endregion


    ///#region 构造方法
    @Override
    public UAC getHisUAC() {
        UAC uac = new UAC();
        uac.OpenForSysAdmin();
        uac.IsInsert = false;
        return uac;
    }

    /**
     * 用户自定义表
     */
    public SFTable() {
    }

    public SFTable(String mypk) throws Exception {
        this.setNo(mypk);
        try {
            this.Retrieve();
        } catch (Exception ex) {

//			switch (this.getNo())
//ORIGINAL LINE: case "BP.Pub.NYs":
            if (this.getNo().equals("BP.Pub.NYs")) {
                this.setName("年月");
                this.setFK_Val("FK_NY");
                this.Insert();
            }
//ORIGINAL LINE: case "BP.Pub.YFs":
            else if (this.getNo().equals("BP.Pub.YFs")) {
                this.setName("月");
                this.setFK_Val("FK_YF");
                this.Insert();
            }
//ORIGINAL LINE: case "BP.Pub.Days":
            else if (this.getNo().equals("BP.Pub.Days")) {
                this.setName("天");
                this.setFK_Val("FK_Day");
                this.Insert();
            }
//ORIGINAL LINE: case "BP.Pub.NDs":
            else if (this.getNo().equals("BP.Pub.NDs")) {
                this.setName("年");
                this.setFK_Val("FK_ND");
                this.Insert();
            } else {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    /**
     * EnMap
     */
    @Override
    public Map getEnMap() {
        if (this.get_enMap() != null) {
            return this.get_enMap();
        }
        Map map = new Map("Sys_SFTable", "字典表");

        map.AddTBStringPK(SFTableAttr.No, null, "表英文名称", true, false, 1, 200, 20);
        map.AddTBString(SFTableAttr.Name, null, "表中文名称", true, false, 0, 200, 20);

        map.AddDDLSysEnum(SFTableAttr.SrcType, 0, "数据表类型", true, false, SFTableAttr.SrcType, "@0=本地的类@1=创建表@2=表或视图@3=SQL查询表@4=WebServices@5=微服务Handler外部数据源@6=JavaScript外部数据源@7=动态Json");

        map.AddDDLSysEnum(SFTableAttr.CodeStruct, 0, "字典表类型", true, false, SFTableAttr.CodeStruct);
        map.AddTBString(SFTableAttr.RootVal, null, "根节点值", false, false, 0, 200, 20);


        map.AddTBString(SFTableAttr.FK_Val, null, "默认创建的字段名", true, false, 0, 200, 20);
        map.AddTBString(SFTableAttr.TableDesc, null, "表描述", true, false, 0, 200, 20);
        map.AddTBString(SFTableAttr.DefVal, null, "默认值", true, false, 0, 200, 20);


        //数据源.
        map.AddDDLEntities(SFTableAttr.FK_SFDBSrc, "local", "数据源", new bp.sys.SFDBSrcs(), true);

        map.AddTBString(SFTableAttr.SrcTable, null, "数据源表", false, false, 0, 200, 20);
        map.AddTBString(SFTableAttr.ColumnValue, null, "显示的值(编号列)", false, false, 0, 200, 20);
        map.AddTBString(SFTableAttr.ColumnText, null, "显示的文字(名称列)", false, false, 0, 200, 20);
        map.AddTBString(SFTableAttr.ParentValue, null, "父级值(父级列)", false, false, 0, 200, 20);
        map.AddTBString(SFTableAttr.SelectStatement, null, "查询语句", true, false, 0, 1000, 600, true);
        map.AddTBDateTime(SFTableAttr.RDT, null, "加入日期", false, false);

        map.AddTBString(SysEnumAttr.OrgNo, null, "OrgNo", true, false, 0, 100, 8);

        //查找.
        map.AddSearchAttr(SFTableAttr.FK_SFDBSrc);

        RefMethod rm = new RefMethod();
        rm.Title = "查看数据";
        rm.ClassMethodName = this.toString() + ".DoEdit";
        rm.refMethodType = RefMethodType.RightFrameOpen;
        rm.IsForEns = false;
        map.AddRefMethod(rm);

        //rm = new RefMethod();
        //rm.Title = "创建Table向导";
        //rm.ClassMethodName = this.ToString() + ".DoGuide";
        //rm.refMethodType = RefMethodType.RightFrameOpen;
        //rm.IsForEns = false;
        //map.AddRefMethod(rm);

        //rm = new RefMethod();
        //rm.Title = "数据源管理";
        //rm.ClassMethodName = this.ToString() + ".DoMangDBSrc";
        //rm.refMethodType = RefMethodType.RightFrameOpen;
        //rm.IsForEns = false;
        //map.AddRefMethod(rm);

        this.set_enMap(map);
        return this.get_enMap();
    }

    ///#endregion

    /**
     * 数据源管理
     *
     * @return
     */
    public final String DoMangDBSrc() {
        return "../../Comm/Sys/SFDBSrcNewGuide.htm";
    }

    /**
     * 创建表向导
     *
     * @return
     */
    public final String DoGuide() {
        return "../../../WF/Admin/FoolFormDesigner/CreateSFGuide.htm";
    }

    /**
     * 编辑数据
     *
     * @return
     */
    public final String DoEdit() {
        if (this.getIsClass()) {
            return "../../Comm/Ens.htm?EnsName=" + this.getNo();
        } else {
            return "../../Admin/CCFormDesigner/SFTable/SFTableEditData.htm?FK_SFTable=" + this.getNo();
        }
    }

    public final String IsCanDelete() throws Exception {
        MapAttrs attrs = new MapAttrs();
        attrs.Retrieve(MapAttrAttr.UIBindKey, this.getNo());
        if (attrs.size() != 0) {
            String err = "";
            for (MapAttr item : attrs.ToJavaList()) {
                err += " @ " + item.getMyPK() + " " + item.getName();
            }
            return "err@如下实体字段在引用:" + err + "。您不能删除该表。";
        }
        return null;
    }

    @Override
    protected boolean beforeDelete() throws Exception {
        String delMsg = this.IsCanDelete();
        if (delMsg != null) {
            throw new RuntimeException(delMsg);
        }

        return super.beforeDelete();
    }

    @Override
    protected boolean beforeInsert() throws Exception {
        //利用这个时间串进行排序.
        this.setRDT(DataType.getCurrentDateTime());

        //SaaS模式下前缀增加OrgNo
        this.setNo(bp.web.WebUser.getOrgNo() + "_" + this.getNo());
        this.setOrgNo(bp.web.WebUser.getOrgNo());

        ///#region 如果是本地类. @于庆海.
        if (this.getSrcType() == bp.sys.SrcType.BPClass) {
            Entities ens = ClassFactory.GetEns(this.getNo());
            Entity en = ens.getGetNewEntity();
            this.setName(en.getEnDesc());

            //检查是否是树结构.
            if (en.getIsTreeEntity() == true) {
                this.setCodeStruct(bp.sys.CodeStruct.Tree);
            } else {
                this.setCodeStruct(bp.sys.CodeStruct.NoName);
            }
        }

        ///#endregion 如果是本地类.


        ///#region 本地类，物理表..
        if (this.getSrcType() == bp.sys.SrcType.CreateTable) {
            if (DBAccess.IsExitsObject(this.getNo()) == true) {
                return super.beforeInsert();
                //throw new Exception("err@表名[" + this.getNo() + "]已经存在，请使用其他的表名.");
            }

            String sql = "";
            if (this.getCodeStruct() == bp.sys.CodeStruct.NoName || this.getCodeStruct() == bp.sys.CodeStruct.GradeNoName) {
                sql = "CREATE TABLE " + this.getNo() + " (";
                sql += "No varchar(30) NOT NULL,";
                sql += "Name varchar(3900) NULL";
                sql += ")";
            }

            if (this.getCodeStruct() == bp.sys.CodeStruct.Tree) {
                sql = "CREATE TABLE " + this.getNo() + " (";
                sql += "No varchar(30) NOT NULL,";
                sql += "Name varchar(3900)  NULL,";
                sql += "ParentNo varchar(3900)  NULL";
                sql += ")";
            }
            this.RunSQL(sql);

            //初始化数据.
            this.InitDataTable();
        }

        ///#endregion 如果是本地类.

        return super.beforeInsert();
    }

    @Override
    protected void afterInsert() throws Exception {
        try {
            if (this.getSrcType() == bp.sys.SrcType.TableOrView) {
                //暂时这样处理
                String sql = "CREATE VIEW " + this.getNo() + " (";
                sql += "[No],";
                sql += "[Name]";
                sql += (this.getCodeStruct() == bp.sys.CodeStruct.Tree ? ",[ParentNo])" : ")");
                sql += " AS ";
                sql += "SELECT " + this.getColumnValue() + " No," + this.getColumnText() + " Name" + (this.getCodeStruct() == bp.sys.CodeStruct.Tree ? ("," + this.getParentValue() + " ParentNo") : "") + " FROM " + this.getSrcTable() + (StringHelper.isNullOrWhiteSpace(this.getSelectStatement()) ? "" : (" WHERE " + this.getSelectStatement()));

                if (bp.difference.SystemConfig.getAppCenterDBType() == DBType.MySQL) {
                    sql = sql.replace("[", "`").replace("]", "`");
                } else {
                    sql = sql.replace("[", "").replace("]", "");
                }
                this.RunSQL(sql);
            }

            //if (this.SrcType == bp.sys.SrcType.SQL)
            //{
            //    //暂时这样处理
            //    string sql = "CREATE VIEW " + this.getNo() + " (";
            //    sql += "[No],";
            //    sql += "[Name]";
            //    sql += (this.CodeStruct == bp.sys.CodeStruct.Tree ? ",[ParentNo])" : ")");
            //    sql += " AS ";
            //    sql += this.SelectStatement;

            //    if (bp.difference.SystemConfig.getAppCenterDBType() == DBType.MySQL)
            //    {
            //        sql = sql.Replace("[", "`").Replace("]", "`");
            //    }
            //    else
            //    {
            //        sql = sql.Replace("[", "").Replace("]", "");
            //    }
            //    this.RunSQL(sql);
            //}
        } catch (RuntimeException ex) {
            //创建视图失败时，删除此记录，并提示错误
            this.DirectDelete();
            throw ex;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        super.afterInsert();
    }

    /**
     * 获得该数据源的数据
     *
     * @return
     */
    public final DataTable GenerData_bak() {
        String sql = "";
        DataTable dt = null;
        if (this.getSrcType() == bp.sys.SrcType.CreateTable) {
            sql = "SELECT No,Name FROM " + this.getSrcTable();
            dt = this.RunSQLReturnTable(sql);
        }

        if (this.getSrcType() == bp.sys.SrcType.TableOrView) {
            sql = "SELECT No,Name FROM " + this.getSrcTable();
            dt = this.RunSQLReturnTable(sql);
        }

        if (dt == null) {
            throw new RuntimeException("@没有判断的数据.");
        }

        dt.Columns.get(0).ColumnName = "No";
        dt.Columns.get(1).ColumnName = "Name";

        return dt;
    }

    /**
     * 返回json.
     *
     * @return
     */
    public final String GenerDataOfJson() throws Exception {
        return bp.tools.Json.ToJson(this.GenerHisDataTable());
    }


    /**
     * 初始化数据.
     */
    public final void InitDataTable() throws Exception {
        DataTable dt = this.GenerHisDataTable();

        String sql = "";
        if (dt.Rows.size() == 0) {
            //初始化数据.
            if (this.getCodeStruct() == bp.sys.CodeStruct.Tree) {
                sql = "INSERT INTO " + this.getSrcTable() + " (No,Name,ParentNo) VALUES('1','" + this.getName() + "','0') ";
                this.RunSQL(sql);

                for (int i = 1; i < 4; i++) {
                    String no = (new Integer(i)).toString();
                    no = StringHelper.padLeft(no,3,'0');

                    sql = "INSERT INTO " + this.getSrcTable() + " (No,Name,ParentNo) VALUES('" + no + "','Item" + no + "','1') ";
                    this.RunSQL(sql);
                }
            }

            if (this.getCodeStruct() == bp.sys.CodeStruct.NoName) {
                for (int i = 1; i < 4; i++) {
                    String no = (new Integer(i)).toString();
                    no = StringHelper.padLeft(no,3,'0');
                    sql = "INSERT INTO " + this.getSrcTable() + " (No,Name) VALUES('" + no + "','Item" + no + "') ";
                    this.RunSQL(sql);
                }
            }
        }
    }

}
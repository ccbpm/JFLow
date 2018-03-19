package cn.jflow.controller.wf.admin.xap.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.jws.WebService;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.ClassFactory;
import BP.En.EditType;
import BP.En.Entity;
import BP.En.FieldTypeS;
import BP.En.UIContralType;
import BP.Sys.AthUploadWay;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentDB;
import BP.Sys.FrmEle;
import BP.Sys.FrmLabAttr;
import BP.Sys.FrmLines;
import BP.Sys.GroupField;
import BP.Sys.M2MType;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.MapM2M;
import BP.Sys.PubClass;
import BP.Sys.SFTable;
import BP.Sys.SysEnum;
import BP.Sys.SysEnumMain;
import BP.Tools.FormatToJson;
import BP.Tools.StringHelper;
import BP.WF.Node;
import BP.WF.Entity.FrmWorkCheck;
import BP.WF.Template.Frm;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmNodeAttr;
import BP.Web.WebUser;
import cn.jflow.common.util.ContextHolderUtils;

@Deprecated
@WebService(endpointInterface = "cn.jflow.controller.wf.admin.xap.service.CCForm")
public class CCFormImpl implements CCForm{
	

    /// <summary>
    /// 获取值
    /// </summary>
    /// <param name="key"></param>
    /// <returns></returns>
    public String CfgKey(String key)
    {
        return BP.Sys.SystemConfig.getAppSettings().get(key).toString();
    }
    
    /// <summary>
    /// 上传多附件
    /// </summary>
    /// <param name="WorkID">业务编号</param>
    /// <param name="FK_Node">节点编号</param>
    /// <param name="FK_FrmAttachment">控件ID</param>
    /// <param name="UserNo">上传人账号</param>
    /// <param name="UserName">上传人中文名</param>
    /// <param name="FileByte">文件大小</param>
    /// <param name="fileName">文件名，不能为空</param>
    /// <param name="MyNote">备注，可以为空</param>
    /// <param name="sort">排序，可以为空</param>
    /// <returns>处理消息</returns>
    public String AttachmentFiles(String WorkID, String FK_Node, String FK_FrmAttachment, String UserNo, String UserName, byte[] FileByte, String fileName, String MyNote, String sort)
    {

        try
        {
            String guid = BP.DA.DBAccess.GenerGUID();
            String exts = fileName.substring(fileName.indexOf(".") + 1);
            FrmAttachment athDesc = new FrmAttachment(FK_FrmAttachment);
            //如果有上传类型限制，进行判断格式
            if ("*.*".equals(athDesc.getExts()) || "".equals(athDesc.getExts()))
            {
                /*任何格式都可以上传*/
            }
            else
            {
                if (athDesc.getExts().toLowerCase().contains(exts) == false)
                {
                    return "error:您上传的文件，不符合系统的格式要求，要求的文件格式:" + athDesc.getExts() + "，您现在上传的文件格式为:" + exts;
                }
            }

            //保存文件
            String realSaveTo = CCFormUtil.UploadFile(FileByte, fileName, athDesc.getSaveTo());
            File info = new File(realSaveTo);

            FrmAttachmentDB dbUpload = new FrmAttachmentDB();

            dbUpload.setMyPK(guid);
            dbUpload.setNodeID(FK_Node);

            if (athDesc.getAthUploadWay() == AthUploadWay.Inherit)
            {
                /*如果是继承，就让他保持本地的PK. */
                dbUpload.setRefPKVal(WorkID);
            }

            if (athDesc.getAthUploadWay() == AthUploadWay.Interwork)
            {
                /*如果是协同，就让他是PWorkID. */
                String pWorkID = ""+BP.DA.DBAccess.RunSQLReturnValInt("SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + WorkID, 0);
                if (pWorkID == null || "0".equals(pWorkID))
                    pWorkID = WorkID;

                dbUpload.setRefPKVal(pWorkID);
            }

            dbUpload.setFK_MapData(athDesc.getFK_MapData());
            dbUpload.setFK_FrmAttachment(FK_FrmAttachment);

            dbUpload.setFileExts(info.getName().substring(info.getName().lastIndexOf(".")));
            dbUpload.setFileFullName(realSaveTo);
            dbUpload.setFileName(info.getName());
            dbUpload.setFileSize((float)info.length());

            dbUpload.setRDT(DataType.getCurrentDataTimess());
            dbUpload.setRec(UserNo);
            dbUpload.setRecName(UserName);
            if (athDesc.getIsNote())
                dbUpload.setMyNote(MyNote);

            if (!StringHelper.isNullOrEmpty(sort))
                dbUpload.setSort(sort);

            dbUpload.setUploadGUID(guid);
            dbUpload.Insert();
        }
        catch (Exception ex)
        {
            return "error:" + ex.getMessage();
        }
        return "success";
    }
    
    /// <summary>
    /// 上传文件.
    /// </summary>
    /// <param name="FileByte"></param>
    /// <param name="fileName"></param>
    /// <returns></returns>
    public String UploadFile(byte[] FileByte, String fileName) throws IOException
    {
    	 //创建临时目录.
        String pathTemp = BP.Sys.SystemConfig.getPathOfTemp() + "/Temp";
        File file1=new File(pathTemp);
        if (!file1.exists())
            file1.mkdirs();

        String path = BP.Sys.SystemConfig.getPathOfTemp();
        String filePath = path + "/" + fileName;
        File file2=new File(filePath);
        if (file2.exists())
            file2.delete();

        //这里使用绝对路径来索引
        FileOutputStream stream = new FileOutputStream(filePath);
        stream.write(FileByte, 0, FileByte.length);
        stream.close();

        DataSet ds = new DataSet();
        ds.readXml(filePath);

        String strs = null;
        strs = FormatToJson.ToJson(ds);
        return strs;
    }
    /// <summary>
    /// 执行sql
    /// </summary>
    /// <param name="sql"></param>
    /// <returns></returns>
    public int RunSQL(String sql)
    {
        return BP.DA.DBAccess.RunSQL(sql);
    }

    public int RunSQLs(String sqls)
    {
        if (StringHelper.isNullOrEmpty(sqls))
            return 0;

        int i = 0;
        String[] strs = sqls.split("@");
        for(String str:strs)
        {
            if (StringHelper.isNullOrEmpty(str))
                continue;
            i += BP.DA.DBAccess.RunSQL(str);
        }
        return i;
    }
    /// <summary>
    /// 运行sql返回table.
    /// </summary>
    /// <param name="sql"></param>
    /// <returns></returns>
    public String RunSQLReturnTable(String sql)
    {
        DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
        DataSet ds = new DataSet();
        ds.Tables.add(BP.DA.DBAccess.RunSQLReturnTable(sql));
        return DataSet.ConvertDataSetToXml(ds);
    }
    
    /// <summary>
    /// 运行sql返回table.
    /// </summary>
    /// <param name="sql"></param>
    /// <returns></returns>
    public String RunSQLReturnJson(String sql)
    {
        DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
        DataSet ds = new DataSet();
        ds.Tables.add(dt);
        return BP.DA.DataType.ToJson(ds.getTables().get(0),null);
    }
    /// <summary>
    /// 运行sql返回table.
    /// </summary>
    /// <param name="sql"></param>
    /// <returns></returns>
    public String RunSQLReturnTable2DataTable(String sql)
    {
    	DataSet ds=new DataSet();
        DataTable dt=BP.DA.DBAccess.RunSQLReturnTable(sql);
        ds.Tables.add(dt);
        return DataSet.ConvertDataSetToXml(ds);
    }
    /// <summary>
    /// 运行sql返回String.
    /// </summary>
    /// <param name="sql"></param>
    /// <returns></returns>
    public String RunSQLReturnString(String sql)
    {
        return BP.DA.DBAccess.RunSQLReturnString(sql);
    }
    /// <summary>
    /// 运行sql返回String.
    /// </summary>
    /// <param name="sql"></param>
    /// <returns></returns>
    public int RunSQLReturnValInt(String sql)
    {
        return BP.DA.DBAccess.RunSQLReturnValInt(sql);
    }
    /// <summary>
    /// 运行sql返回float.
    /// </summary>
    /// <param name="sql"></param>
    /// <returns></returns>
    public float RunSQLReturnValFloat(String sql) throws Exception
    {
        return BP.DA.DBAccess.RunSQLReturnValFloat(sql);
    }
    /// <summary>
    /// 运行sql返回table.
    /// </summary>
    /// <param name="sql"></param>
    /// <returns></returns>
    public String RunSQLReturnTableS(String sqls)
    {
        String[] strs = sqls.split("@");
        DataSet ds = new DataSet();
        int i = 0;
        for(String sql:strs)
        {
            if (StringHelper.isNullOrEmpty(sql))
                continue;
            DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
            dt.TableName = "DT" + i;
            ds.Tables.add(dt);
            i++;
        }
        return DataSet.ConvertDataSetToXml(ds);
    }
    /// <summary>
    /// 将中文转化成拼音.
    /// </summary>
    /// <param name="name"></param>
    /// <param name="flag">true: 全拼，false : 简拼</param>
    /// <returns></returns>
    public String ParseStringToPinyin(String name, boolean flag)
    {
        String s =null ;
        try
        {
            if (flag)
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
            s = s.replace(",", "");
            s = s.replace(".", "");
            return s;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public String DealPK(String pk, String fromMapdata, String toMapdata)
    {
        if (pk.contains("*" + fromMapdata))
            return pk.replace("*" + toMapdata, "*" + toMapdata);
        else
            return pk + "*" + toMapdata;
    }
    
    public void LetAdminLogin()
    {
        BP.Port.Emp emp = new BP.Port.Emp("admin");
        WebUser.SignInOfGener(emp);
    }
    /// <summary>
    /// 让他登录
    /// </summary>
    /// <param name="user"></param>
    /// <param name="sid"></param>
    public void LetUserLogin(String user, String sid)
    {
        BP.Port.Emp emp = new BP.Port.Emp(user);
        WebUser.SignInOfGener(emp);
    }

    /// <summary>
    /// 保存Enum.
    /// </summary>
    /// <param name="enumKey"></param>
    /// <param name="enumLab"></param>
    /// <param name="cfg"></param>
    /// <returns></returns>
    public String SaveEnum(String enumKey, String enumLab, String cfg)
    {
        SysEnumMain sem = new SysEnumMain();
        sem.setNo(enumKey);
        if (sem.RetrieveFromDBSources() == 0)
        {
            sem.setName(enumLab);
            sem.setCfgVal(cfg);
            sem.setLang(WebUser.getSysLang());
            sem.Insert();
        }
        else
        {
            sem.setName(enumLab);
            sem.setCfgVal(cfg);
            sem.setLang(WebUser.getSysLang());
            sem.Update();
        }

        String[] Strs = cfg.split("@");
        for(String str:Strs)
        {
            if (StringHelper.isNullOrEmpty(str))
                continue;
            String[] kvs = str.split("=");
            SysEnum se = new SysEnum();
            se.setEnumKey(enumKey);
            se.setLang(WebUser.getSysLang());
            se.setIntKey(Integer.parseInt(kvs[0]));
            se.setLab(kvs[1]);
            se.Insert();
        }
        return "save ok.";
    }

    /// <summary>
    /// 保存枚举字段
    /// </summary>
    /// <param name="fk_mapData">表单ID</param>
    /// <param name="fieldKey">字段值</param>
    /// <param name="fieldName">名</param>
    /// <param name="enumKey">枚举值</param>
    /// <returns>是否保存成功</returns>
    public String SaveEnumField(String fk_mapData, String fieldKey, String fieldName, String enumKey, double x, double y)
    {
        try
        {
            MapAttr attr = new MapAttr();
            attr.setMyPK(fk_mapData + "_" + fieldKey);
            if (attr.getIsExits() == true)
                return "字段{" + fieldKey + "}已经存在.";

            attr.setFK_MapData(fk_mapData);
            attr.setKeyOfEn(fieldKey);
            attr.setName(fieldName);
            attr.setMyDataType(DataType.AppInt);

            attr.setX(Float.parseFloat(""+x));
            attr.setY(Float.parseFloat(""+y));

            attr.setUIBindKey(enumKey);
            attr.setUIContralType(UIContralType.DDL);
            attr.setLGType(FieldTypeS.Enum);
            attr.Insert();
            return "OK";
        }
        catch (Exception ex)
        {
            return "@错误信息:" + ex.getMessage();
            // +" StackTrace:" + ex.getStackTrace();
            //                return "@错误信息:" + ex.Message + " StackTrace:" + ex.getStackTrace();
        }
    }
    /// <summary>
    /// 保存外键字段
    /// </summary>
    /// <param name="fk_mapData">表单ID</param>
    /// <param name="fieldKey">字段值</param>
    /// <param name="fieldName">名</param>
    /// <param name="enName">枚举值</param>
    /// <returns>是否保存成功</returns>
    public String SaveFKField(String fk_mapData, String fieldKey, String fieldName, String enName, double x, double y)
    {
        try
        {
            MapAttr attr = new MapAttr();
            attr.setMyPK(fk_mapData + "_" + fieldKey);
            if (attr.getIsExits() == true)
                return "字段{" + fieldKey + "}已经存在.";

            attr.setFK_MapData(fk_mapData);
            attr.setKeyOfEn(fieldKey);
            attr.setName(fieldName);
            attr.setMyDataType(DataType.AppString);

            attr.setX(Float.parseFloat(""+x));
            attr.setY(Float.parseFloat(""+y));

            attr.setUIBindKey(enName);
            attr.setUIContralType(UIContralType.DDL);
            attr.setLGType(FieldTypeS.FK);
            attr.Insert();
            return "OK";
        }
        catch (Exception ex)
        {
            return "@错误信息:" + ex.getMessage();
        }
    }
    
    
    
    public String SaveImageAsFile(byte[] img, String pkval, String fk_Frm_Ele)
    {
        FrmEle fe = new FrmEle(fk_Frm_Ele);
//        System.Drawing.Image newImage;
//        using (MemoryStream ms = new MemoryStream(img, 0, img.Length))
//        {
//            ms.Write(img, 0, img.Length);
//            newImage = Image.FromStream(ms, true);
//            Bitmap bitmap = new Bitmap(newImage, new Size(fe.WOfInt, fe.HOfInt));
//
//            if (System.IO.Directory.Exists(fe.HandSigantureSavePath + "\\" + fe.FK_MapData + "\\") == false)
//                System.IO.Directory.CreateDirectory(fe.HandSigantureSavePath + "\\" + fe.FK_MapData + "\\");
//
//            string saveTo = fe.HandSigantureSavePath + "\\" + fe.FK_MapData + "\\" + pkval + ".jpg";
//            bitmap.Save(saveTo, ImageFormat.Jpeg);
//
//            string pathFile = System.Web.HttpContext.Current.Request.ApplicationPath + fe.HandSiganture_UrlPath + fe.FK_MapData + "/" + pkval + ".jpg";
//            FrmEleDB ele = new FrmEleDB();
//            ele.FK_MapData = fe.FK_MapData;
//            ele.EleID = fe.EleID;
//            ele.RefPKVal = pkval;
//            ele.Tag1 = pathFile.Replace("\\\\", "\\");
//            ele.Tag1 = pathFile.Replace("////", "//");
//
//            ele.Tag2 = saveTo.Replace("\\\\", "\\");
//            ele.Tag2 = saveTo.Replace("////", "//");
//
//            ele.GenerPKVal();
//            ele.Save();
//
//            return pathFile;
//            // return "../DataUser/" + realpath + strFileName + ".png";
//        }
        //FrmEleDB db = new FrmEleDB();
        //db.MyPK= 
        //return "error";
        return null;
    }

    /// <summary>
    /// 装载表单模板
    /// </summary>
    /// <param name="fileByte">字节数</param>
    /// <param name="fk_mapData"></param>
    /// <param name="isClear"></param>
    /// <returns></returns>
    public String LoadFrmTemplete(byte[] fileByte, String fk_mapData, boolean isClear)
    {
        try
        {

            String file = "/Temp/" + fk_mapData + ".xml";
            UploadFile(fileByte, file);
            String path = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("/") + file;
            this.LoadFrmTempleteFile(path, fk_mapData, isClear, true);
            return null;
        }
        catch (Exception ex)
        {
            return ex.getMessage();
        }
    }

    /// <summary>
    /// 导入表单
    /// </summary>
    /// <param name="file">文件</param>
    /// <param name="fk_mapData">表单ID</param>
    /// <param name="isClear">是否清除现有的元素</param>
    /// <param name="isSetReadonly">是否设置的只读？</param>
    /// <returns>导入结果</returns>
    public String LoadFrmTempleteFile(String file, String fk_mapData, boolean isClear, boolean isSetReadonly)
    {
        try
        {
            DataSet ds = new DataSet();
            ds.readXml(file);
            MapData.ImpMapData(fk_mapData, ds, isSetReadonly);
            if (fk_mapData.contains("ND"))
            {
                /* 判断是否是节点表单 */
                int nodeID = 0;
                try
                {
                    nodeID = Integer.parseInt(fk_mapData.replace("ND", ""));
                }
                catch(Exception e)
                {
                    return null;
                }
                Node nd = new Node(nodeID);
                nd.RepareMap();
            }
            return null;
        }
        catch (Exception ex)
        {
            return ex.getMessage();
        }
    }

    /// <summary>
    /// 获取xml数据
    /// </summary>
    /// <param name="xmlFileName"></param>
    /// <returns></returns>
    public String GetXmlData(String xmlFileName)
    {
        DataTable dt = new DataTable("o");
        dt.Columns.Add(new DataColumn("DFor"));
        dt.Columns.Add(new DataColumn("Tag1"));
        dt.Columns.Add(new DataColumn("Tag2"));
        dt.Columns.Add(new DataColumn("Tag3"));
        dt.Columns.Add(new DataColumn("Tag4"));

        DataRow dr = dt.NewRow();
        dr.setValue("DFor", "HandSiganture");
        dr.setValue("Tag1","@Label=存储路径@FType=String@DefVal=D:\\ccflow\\VisualFlow\\DataUser\\BPPaint\\");
        dr.setValue("Tag2","@Label=窗口打开高度@FType=Int@DefVal=500");
        dr.setValue("Tag3","@Label=窗口打开宽度@FType=Int@DefVal=200");
        dr.setValue("Tag4","@Label=UrlPath@FType=String@DefVal=/DataUser/BPPaint/");
        dt.Rows.add(dr);

        dr = dt.NewRow();
        dr.setValue("DFor","EleSiganture");
        dr.setValue("Tag1","@Label=位置@FType=String");
        dr.setValue("Tag2","@Label=高度@FType=Int");
        dr.setValue("Tag3","@Label=宽度@FType=Int");
        dr.setValue("Tag4","");
        dt.Rows.add(dr);

        DataSet myds = new DataSet();
        myds.Tables.add(dt);
        return DataSet.ConvertDataSetToXml(myds);
    }
    
    
    
    public String DoType(String dotype, String v1, String v2, String v3, String v4, String v5)
    {
        String sql = "";
        try
        {
        	if(dotype.equals("CreateCheckGroup")){
        		String gKey = v1;
                String gName = v2;
                String enName1 = v3;

                MapAttr attrN = new MapAttr();
                int i = attrN.Retrieve(MapAttrAttr.FK_MapData, enName1, MapAttrAttr.KeyOfEn, gKey + "_Note");
                i += attrN.Retrieve(MapAttrAttr.FK_MapData, enName1, MapAttrAttr.KeyOfEn, gKey + "_Checker");
                i += attrN.Retrieve(MapAttrAttr.FK_MapData, enName1, MapAttrAttr.KeyOfEn, gKey + "_RDT");
                if (i > 0)
                    return "前缀已经使用：" + gKey + " ， 请确认您是否增加了这个审核分组或者，请您更换其他的前缀。";

                GroupField gf = new GroupField();
                gf.setLab(gName);
                gf.setEnName(enName1);
                gf.Insert();

                attrN = new MapAttr();
                attrN.setFK_MapData(enName1);
                attrN.setKeyOfEn(gKey + "_Note");
                attrN.setName("审核意见");
                attrN.setMyDataType(DataType.AppString);
                attrN.setUIContralType(UIContralType.TB);
                attrN.setUIIsEnable(true);
                attrN.setUIIsLine(true);
                attrN.setMaxLen(4000);
                attrN.setGroupID(Integer.parseInt(""+gf.getOID()));
                attrN.setUIHeight(23 * 3);
                attrN.setIdx(1);
                attrN.Insert();

                attrN = new MapAttr();
                attrN.setFK_MapData(enName1);
                attrN.setKeyOfEn(gKey + "_Checker");
                attrN.setName("审核人");// "审核人";
                attrN.setMyDataType(DataType.AppString);
                attrN.setUIContralType(UIContralType.TB);
                attrN.setMaxLen(50);
                attrN.setMinLen( 0);
                attrN.setUIIsEnable(true);
                attrN.setUIIsLine(false);
                attrN.setDefVal("@WebUser.Name");
                attrN.setUIIsEnable(false);
                attrN.setGroupID(Integer.parseInt(""+gf.getOID()));
                attrN.setIsSigan(true);
                attrN.setIdx(2);
                attrN.Insert();

                attrN = new MapAttr();
                attrN.setFK_MapData(enName1);
                attrN.setKeyOfEn( gKey + "_RDT");
                attrN.setName("审核日期"); // "审核日期";
                attrN.setMyDataType(DataType.AppDateTime);
                attrN.setUIContralType( UIContralType.TB);
                attrN.setUIIsEnable(true);
                attrN.setUIIsLine(false);
                attrN.setDefVal("@RDT");
                attrN.setUIIsEnable(false);
                attrN.setGroupID(Integer.parseInt(""+gf.getOID()));
                attrN.setIdx(3);
                attrN.Insert();

                /*
                 * 判断是否是节点设置的审核分组，如果是就为节点设置焦点字段。
                 */

                String frmID = attrN.getFK_MapData();
                frmID = frmID.replace("ND", "");
                int nodeid = 0;
                try
                {
                    nodeid = Integer.parseInt(frmID);
                }
                catch(Exception e)
                {
                    //转化不成功就是不是节点表单字段.
                    return null;
                }

                Node nd = new Node();
                nd.setNodeID(nodeid);
                if (nd.RetrieveFromDBSources()!=0)
                {
                    if (StringHelper.isNullOrEmpty(nd.getFocusField()) == false)
                        return null;

                    nd.setFocusField("@" + gKey + "_Note");
                    nd.Update();
                }
                return null;
        	}
        	else if(dotype.equals("NewDtl")){
        		MapDtl dtlN = new MapDtl();
                dtlN.setNo(v1);
                if (dtlN.RetrieveFromDBSources() != 0)
                    return "从表已存在";
                dtlN.setName(v1);
                dtlN.setFK_MapData(v2);
                dtlN.setPTable(v1);
                dtlN.Insert();
                dtlN.IntMapAttrs();
                return null;
        	}
        	else if(dotype.equals("DelM2M")){
        		 MapM2M m2mDel = new MapM2M();
                 m2mDel.setMyPK(v1);
                 m2mDel.Delete();
                 //M2M m2mData = new M2M();
                 //m2mData.Delete(M2MAttr.FK_MapData, v1);
                 return null;
        	}
        	else if(dotype.equals("NewAthM")){
        		String fk_mapdataAth = v1;
                String athName = v2;

                FrmAttachment athM = new FrmAttachment();
                athM.setMyPK(athName);
                if (athM.getIsExits())
                    return "多选名称:" + athName + "，已经存在。";

                athM.setX(Float.parseFloat(v3));
                athM.setY( Float.parseFloat(v4));
                athM.setName("多文件上传");
                athM.setFK_MapData( fk_mapdataAth);
                athM.Insert();
                return null;
        	}
        	else if(dotype.equals("NewM2M")){
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
                     m2m.setName( "新建一对多多");
                 }

                 m2m.setX(Float.parseFloat(v4));
                 m2m.setY(Float.parseFloat(v5));
                 m2m.setMyPK(m2m.getFK_MapData() + "_" + m2m.getNoOfObj());
                 if (m2m.getIsExits())
                     return "多选名称:" + m2mName + "，已经存在。";
                 m2m.Insert();
                 return null;
        	}
        	else if(dotype.equals("DelEnum")){
        		 //删除空数据.
                BP.DA.DBAccess.RunSQL("DELETE FROM Sys_MapAttr WHERE FK_MapData Is null or FK_MapData='' ");

                // 检查这个物理表是否被使用。
                sql = "SELECT  * FROM Sys_MapAttr WHERE UIBindKey='" + v1 + "'";
                DataTable dtEnum = DBAccess.RunSQLReturnTable(sql);
                String msgDelEnum = "";
                for(DataRow dr:dtEnum.Rows)
                {
                    msgDelEnum += "\n 表单编号:" + dr.get("FK_MapData") + " , 字段:" + dr.get("KeyOfEn") + ", 名称:" + dr.get("Name");
                }

                if (msgDelEnum != "")
                    return "该枚举已经被如下字段所引用，您不能删除它。" + msgDelEnum;

                sql = "DELETE FROM Sys_EnumMain WHERE No='" + v1 + "'";
                sql += "@DELETE FROM Sys_Enum WHERE EnumKey='" + v1 + "' ";
                DBAccess.RunSQLs(sql);
                return null;
        	}
        	else if(dotype.equals("DelSFTable")){
        		 // 检查这个物理表是否被使用。
                sql = "SELECT  * FROM Sys_MapAttr WHERE UIBindKey='" + v1 + "'";
                DataTable dt = DBAccess.RunSQLReturnTable(sql);
                String msgDel = "";
                for(DataRow dr:dt.Rows)
                {
                    msgDel += "\n 表单编号:" + dr.get("FK_MapData") + " , 字段:" + dr.get("KeyOfEn") + ", 名称:" + dr.get("Name");
                }

                if (msgDel != "")
                    return "该数据表已经被如下字段所引用，您不能删除它。" + msgDel;

                SFTable sfDel = new SFTable();
                sfDel.setNo(v1);
                sfDel.DirectDelete();
                return null;
        	}
        	else if(dotype.equals("SaveSFTable")){
        		String enName = v2;
                String chName = v1;
                if (StringHelper.isNullOrEmpty(v1) || StringHelper.isNullOrEmpty(v2))
                    return "视图中的中英文名称不能为空。";

                SFTable sf = new SFTable();
                sf.setNo( enName);
                sf.setName(chName);

                sf.setNo(enName);
                sf.setName(chName);

                sf.setFK_Val(enName);
                sf.Save();
                if (DBAccess.IsExitsObject(enName) == true)
                {
                    /*已经存在此对象，检查一下是否有No,Name列。*/
                    sql = "SELECT No,Name FROM " + enName;
                    try
                    {
                        DBAccess.RunSQLReturnTable(sql);
                    }
                    catch (Exception ex)
                    {
                        return "您指定的表或视图(" + enName + ")，不包含No,Name两列，不符合ccflow约定的规则。技术信息:" + ex.getMessage();
                    }
                    return null;
                }
                else
                {
                    /*创建这个表，并且插入基础数据。*/
                    try
                    {
                        // 如果没有该表或者视图，就要创建它。
                        sql = "CREATE TABLE " + enName + "(No varchar(30) NOT NULL,Name varchar(50) NULL)";
                        DBAccess.RunSQL(sql);
                        DBAccess.RunSQL("INSERT INTO " + enName + " (No,Name) VALUES('001','Item1')");
                        DBAccess.RunSQL("INSERT INTO " + enName + " (No,Name) VALUES('002','Item2')");
                        DBAccess.RunSQL("INSERT INTO " + enName + " (No,Name) VALUES('003','Item3')");
                    }
                    catch (Exception ex)
                    {
                        sf.DirectDelete();
                        return "创建物理表期间出现错误,可能是非法的物理表名.技术信息:" + ex.getMessage();
                    }
                }
                return null; /*创建成功后返回空值*/
        	}
        	else if(dotype.equals("FrmTempleteExp")){
        		MapData mdfrmtem = new MapData();
                mdfrmtem.setNo(v1);
                if (mdfrmtem.RetrieveFromDBSources() == 0)
                {
                    if (v1.contains("ND"))
                    {
                        int nodeId = Integer.parseInt(v1.replace("ND", ""));
                        Node nd123 = new Node(nodeId);
                        mdfrmtem.setName(nd123.getName());
                        mdfrmtem.setPTable(v1);
                        mdfrmtem.setEnPK("OID");
                        mdfrmtem.Insert();
                    }
                }

                DataSet ds = mdfrmtem.GenerHisDataSet();
                String file = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("/") + "/Temp/" + v1 + ".xml";
//                if (System.IO.File.Exists(file))
//                    System.IO.File.Delete(file);
//                ds.WriteXml(file);

                // BP.Sys.PubClass.DownloadFile(file, mdfrmtem.Name + ".xml");
                //this.DownLoadFile(System.Web.HttpContext.Current, file, mdfrmtem.Name);
                return null;
        	}
        	else if(dotype.equals("FrmTempleteImp")){
        		 DataSet dsImp = new DataSet();
                 String fileImp = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("/") + "/Temp/" + v1 + ".xml";
                 dsImp.readXml(fileImp); //读取文件.
                 MapData.ImpMapData(v1, dsImp, true);
                 return null;
        	}
        	else if(dotype.equals("NewHidF")){
        		 String fk_mapdataHid = v1;
                 String key = v2;
                 String name = v3;
                 int dataType = Integer.parseInt(v4);
                 MapAttr mdHid = new MapAttr();
                 mdHid.setMyPK(fk_mapdataHid + "_" + key);
                 mdHid.setFK_MapData(fk_mapdataHid);
                 mdHid.setKeyOfEn(key);
                 mdHid.setName(name);
                 mdHid.setMyDataType( dataType);
                 mdHid.setHisEditType(EditType.Edit);
                 mdHid.setMaxLen(100);
                 mdHid.setMinLen(0);
                 mdHid.setLGType(FieldTypeS.Normal);
                 mdHid.setUIVisible(false);
                 mdHid.setUIIsEnable(false);
                 mdHid.Insert();
                 return null;
        	}
        	else if(dotype.equals("DelDtl")){
        		 MapDtl dtl = new MapDtl(v1);
                 dtl.Delete();
                 return null;
        	}
        	else if(dotype.equals("DelWorkCheck")){
        		 FrmWorkCheck check = new FrmWorkCheck();
                 check.setNo(v1);
                 check.Delete();
                 return null;
        	}
        	else if(dotype.equals("DeleteFrm")){
        		 String delFK_Frm = v1;
                 MapData mdDel = new MapData(delFK_Frm);
                 mdDel.Delete();
                 sql = "@DELETE FROM Sys_MapData WHERE No='" + delFK_Frm + "'";
                 sql = "@DELETE FROM WF_FrmNode WHERE FK_Frm='" + delFK_Frm + "'";
                 DBAccess.RunSQLs(sql);
                 return null;
        	}
        	else if(dotype.equals("FrmUp")){
        		
        	}
        	else if(dotype.equals("FrmDown")){
        		 FrmNode myfn = new FrmNode();
                 myfn.Retrieve(FrmNodeAttr.FK_Node, v1, FrmNodeAttr.FK_Frm, v2);
                 if ("FrmUp".equals(dotype))
                     myfn.DoUp();
                 else
                     myfn.DoDown();
                 return null;
        	}
        	else if(dotype.equals("SaveFlowFrm")){
        		// 转化参数意义.
                String vals = v1;
                String fk_Node = v2;
                String fk_flow = v3;
                boolean isPrint = false;
                if ("1".equals(v5))
                    isPrint = true;

                boolean isReadonly = false;
                if ("1".equals(v4))
                    isReadonly = true;

                String msg = this.SaveEn(vals);
                if (msg.contains("Error"))
                    return msg;

                String fk_frm = msg;
                Frm fm = new Frm();
                fm.setNo(fk_frm);
                fm.Retrieve();

                FrmNode fn = new FrmNode();
                if (fn.Retrieve(FrmNodeAttr.FK_Frm, fk_frm,
                    FrmNodeAttr.FK_Node, fk_Node) == 1)
                {
                    fn.setIsEdit(!isReadonly);
                    fn.setIsPrint(isPrint);
                    fn.setFK_Flow(fk_flow);
                    fn.Update();
                    BP.DA.DBAccess.RunSQL("UPDATE Sys_MapData SET FK_FrmSort='01',AppType=1  WHERE No='" + fk_frm + "'");
                    return fk_frm;
                }

                fn.setFK_Frm(fk_frm);
                fn.setFK_Flow(fk_flow);
                fn.setFK_Node(Integer.parseInt(fk_Node));
                fn.setIsEdit(!isReadonly);
                fn.setIsPrint(isPrint);
                fn.setIdx(100);
                fn.setFK_Flow(fk_flow);
                fn.Insert();

                MapData md = new MapData();
                md.setNo(fm.getNo());
                if (md.RetrieveFromDBSources() == 0)
                {
                    md.setName(fm.getName());
                    md.setEnPK("OID");
                    md.Insert();

                }

                MapAttr attr = new MapAttr();
                attr.setFK_MapData(md.getNo());
                attr.setKeyOfEn("OID");
                attr.setName("WorkID");
                attr.setMyDataType(BP.DA.DataType.AppInt);
                attr.setUIContralType(UIContralType.TB);
                attr.setLGType(FieldTypeS.Normal);
                attr.setUIVisible(false);
                attr.setUIIsEnable(false);
                attr.setDefVal("0");
                attr.setHisEditType(BP.En.EditType.Readonly);
                attr.Insert();

                attr = new MapAttr();
                attr.setFK_MapData( md.getNo());
                attr.setKeyOfEn("FID");
                attr.setName("FID");
                attr.setMyDataType(BP.DA.DataType.AppInt);
                attr.setUIContralType(UIContralType.TB);
                attr.setLGType(FieldTypeS.Normal);
                attr.setUIVisible(false);
                attr.setUIIsEnable(false);
                attr.setDefVal("0");
                attr.setHisEditType(BP.En.EditType.Readonly);
                attr.Insert();

                attr = new MapAttr();
                attr.setFK_MapData(md.getNo());
                attr.setKeyOfEn("RDT");
                attr.setName("记录日期");
                attr.setMyDataType(BP.DA.DataType.AppDateTime);
                attr.setUIContralType( UIContralType.TB);
                attr.setLGType(FieldTypeS.Normal);
                attr.setUIVisible(false);
                attr.setUIIsEnable(false);
                attr.setDefVal("@RDT");
                attr.setHisEditType(BP.En.EditType.Readonly);
                attr.Insert();
                return fk_frm;
        	}
        	else{
        		return "Error:" + dotype + " , 未设置此标记.";
        	}
        	return null;
        }
        catch (Exception ex)
        {
            return "Error:" + ex.getMessage();
        }
    }
    /// <summary>
    /// 保存entity.
    /// 事例: @EnName=BP.Sys.FrmLabel@PKVal=Lin13b@X=100@Y=299@Text=我的标签
    /// </summary>
    /// <param name="vals"></param>
    /// <returns></returns>
    public String SaveEn(String vals)
    {
        Entity en = null;
        try
        {
            AtPara ap = new AtPara(vals);
            String enName = ap.GetValStrByKey("EnName");
            String pk = ap.GetValStrByKey("PKVal");
            en = ClassFactory.GetEn(enName);
            en.ResetDefaultVal();

            if (en == null)
                throw new Exception("无效的类名:" + enName);

            if (StringHelper.isNullOrEmpty(pk) == false)
            {
                en.setPKVal(pk);
                en.RetrieveFromDBSources();
            }

            for(String key:ap.getHisHT().keySet())
            {
                if ("PKVal".equals(key))
                    continue;
                en.SetValByKey(key, ap.getHisHT().get(key).toString().replace('^', '@'));
            }
            en.Save();
            return null;
//            return en.PKVal as string;
        }
        catch (Exception ex)
        {
            if (en != null)
                en.CheckPhysicsTable();
            return "Error:" + ex.getMessage();
        }
    }

    /// <summary>
    /// 获取路径
    /// </summary>
    /// <param name="path"></param>
    /// <returns></returns>
//    public String FtpMethod(String doType, String v1, String v2, String v3)
//    {
//        try
//        {
//            FtpSupport.FtpConnection conn = new FtpSupport.FtpConnection("192.168.1.138", "administrator", "jiaozi");
//            switch (doType)
//            {
//                case "ShareFrm": /*共享模板*/
//                    MapData md = new MapData();
//                    DataSet ds = md.GenerHisDataSet();
//                    string file = BP.Sys.SystemConfig.getPathOfTemp() + v1 + "_" + v2 + "_" + DateTime.Now.ToString("MM-dd hh-mm") + ".xml";
//                    ds.WriteXml(file);
//                    conn.SetCurrentDirectory("/");
//                    conn.SetCurrentDirectory("/Upload.Form/");
//                    conn.SetCurrentDirectory(v3);
//                    conn.PutFile(file, md.Name + ".xml");
//                    conn.Close();
//                    return null;
//                case "GetDirs":
//                    //   return "@01.日常办公@02.人力资源@03.其它类";
//                    conn.SetCurrentDirectory(v1);
//                    FtpSupport.Win32FindData[] dirs = conn.FindFiles();
//                    conn.Close();
//                    string dirsStr = "";
//                    foreach (FtpSupport.Win32FindData dir in dirs)
//                    {
//                        dirsStr += "@" + dir.FileName;
//                    }
//                    return dirsStr;
//                case "GetFls":
//                    conn.SetCurrentDirectory(v1);
//                    FtpSupport.Win32FindData[] fls = conn.FindFiles();
//                    conn.Close();
//                    string myfls = "";
//                    foreach (FtpSupport.Win32FindData fl in fls)
//                    {
//                        myfls += "@" + fl.FileName;
//                    }
//                    return myfls;
//                case "LoadTempleteFile":
//                    string fileFtpPath = v1;
//                    conn.SetCurrentDirectory("/Form.表单模版/");
//                    conn.SetCurrentDirectory(v3);
//
//                    /*下载文件到指定的目录: */
//                    string tempFile = BP.Sys.SystemConfig.getPathOfTemp() + "\\" + v2 + ".xml";
//                    conn.GetFile(v1, tempFile, false, FileAttributes.Archive, FtpSupport.FtpTransferType.Ascii);
//                    return this.LoadFrmTempleteFile(tempFile, v2, true, true);
//                default:
//                    return null;
//            }
//        }
//        catch (Exception ex)
//        {
//            return "Error:" + ex.Message;
//        }
//    }
    /// <summary>
    /// 获取外键数据
    /// </summary>
    /// <param name="uiBindKey">外键</param>
    /// <returns>外键数据</returns>
    public String RequestSFTableV1(String uiBindKey) throws Exception
    {
        if (StringHelper.isNullOrEmpty(uiBindKey))
            throw new Exception("@uiBindKey不能为空值.");

        DataSet ds = new DataSet();
        ds.Tables.add(PubClass.GetDataTableByUIBineKeyForCCFormDesigner(uiBindKey));
        return DataSet.ConvertDataSetToXml(ds);
    }

    public void InitFrm(String fk_mapdata) throws Exception
    {
        try
        {
            BP.Sys.PubClass.InitFrm(fk_mapdata);
        }
        catch (Exception ex)
        {
            FrmLines lines = new FrmLines();
            lines.Delete(FrmLabAttr.FK_MapData, fk_mapdata);
            throw ex;
        }
    }
    private DataSet ds = null;
    /// <summary>
    /// 获取一个Frm
    /// </summary>
    /// <param name="fk_mapdata">map</param>
    /// <param name="workID">可以为0</param>
    /// <returns>form描述</returns>
    public String GenerFrm(String fk_mapdata, int workID)
    {
        try
        {
//            this.ds = MapData.GenerHisDataSet(fk_mapdata);
            if (this.ds == null || this.ds.Tables.size() <= 0)
            {
                MapData md = new MapData();
                md.setNo( fk_mapdata);
                if (md.RetrieveFromDBSources() == 0)
                {
                    MapDtl dtl = new MapDtl();
                    dtl.setNo(fk_mapdata);
                    if (dtl.RetrieveFromDBSources() == 0)
                        throw new Exception("装载错误，该表单ID=" + fk_mapdata + "丢失，请修复一次流程重新加载一次.");
                    else
                    {
                        md.Copy(dtl);
                        md.DirectInsert();
                    }
                }
            }
          
            String json = FormatToJson.ToJson(ds);
            //string xml =  Silverlight.DataSetConnector.Connector.ToXml(ds);
            return json;
        }
        catch (Exception ex)
        {
            return ex.getMessage();
        }
    }

    /// <summary>
    /// 
    /// </summary>
    /// <param name="fromMapData"></param>
    /// <param name="fk_mapdata"></param>
    /// <param name="isClear">是否清除</param>
    /// <param name="isSetReadonly">是否设置为只读?</param>
    /// <returns></returns>
    public String CopyFrm(String fromMapData, String fk_mapdata, boolean isClear, boolean isSetReadonly)
    {
        isSetReadonly = true;
        this.LetAdminLogin();

        MapData md = new MapData(fromMapData);
        MapData.ImpMapData(fk_mapdata, md.GenerHisDataSet(), isSetReadonly);

        // 如果是节点表单，就要执行一次修复，以免漏掉应该有的系统字段。
        if (fk_mapdata.contains("ND") == true)
        {
            try
            {
                String fk_node = fk_mapdata.replace("ND", "");
                Node nd = new Node(Integer.parseInt(fk_node));
                nd.RepareMap();
            }
            catch(Exception e)
            {
                // 不处理异常。
            }
        }
        return null;
    }

 
    public String SaveFrm(String fk_mapdata, String xml, String sqls, String mapAttrKeyName)
    {
        DataSet ds = new DataSet();
        try
        {
            //ds.ReadXml(sr);
            ds = FormatToJson.JsonToDataSet(xml);
        }
        catch (Exception ex)
        {
            return ex.getMessage();
        }
   
        String str = "";
        for(DataTable dt:ds.Tables)
        {
            try
            {
                str += CCFormUtil.SaveDT(dt);
                if (dt.TableName.toLowerCase().equals("wf_node") && dt.Columns.size() >0 && dt.Rows.size() ==1)
                {
                    /* 更新审核组件状态. */
                    String nodeid = fk_mapdata.replace("ND", "");
                    
                    
                    // dt.Rows[0]["NodeID"] = Glo.FK_MapData.Replace("ND", "");
                   String x=  (String) dt.Rows.get(0).get("FWC_X");  
                   String y=  (String) dt.Rows.get(0).get("FWC_Y");
                   String w=  (String) dt.Rows.get(0).get("FWC_W");
                   String h=  (String) dt.Rows.get(0).get("FWC_H");
                  
                  BP.DA.DBAccess.RunSQL("UPDATE WF_Node SET FWCType=1,FWC_X="+x+",FWC_Y="+y+", FWC_W="+w+", FWC_H="+h+"  WHERE NodeID=" + nodeid);
                }
            }
            catch (Exception ex)
            {
                str += "@保存" + dt.TableName + "失败:" + ex.getMessage();
            }
        }

        
        if (BP.Sys.SystemConfig.getAppCenterDBType() == DBType.Oracle)
        {
            sqls = sqls.replace("LEN(", "LENGTH(");
        }

        sqls += "@UPDATE Sys_MapAttr SET Name='' WHERE FK_MapData='" + fk_mapdata + "'  AND Name IS NULL ";
        sqls += "@UPDATE Sys_MapAttr SET UIVisible=1 WHERE FK_MapData='" + fk_mapdata + "' AND UIVisible is null";
        sqls += "@UPDATE Sys_MapAttr SET UIIsEnable=1 WHERE FK_MapData='" + fk_mapdata + "' AND UIIsEnable is null";
        sqls += "@UPDATE Sys_MapAttr SET UIIsLine=0 WHERE FK_MapData='" + fk_mapdata + "' AND UIIsLine is null";

       

        // 更新版本号,执行的sql,所以不需要Retrieve
        String sql = "@UPDATE Sys_MapData SET VER='" + BP.DA.DataType.getCurrentDataTimess() + "' WHERE No='" + fk_mapdata + "'";
        sqls += sql;

        RunSQLs(sqls);

        //string  sql = "SELECT KeyOfEn FROM Sys_MapAttr WHERE Name='' AND FK_MapData='' ";
        // DataTable mydt = DBAccess.RunSQLReturnTable(sql);
        // if (mydt.Rows.Count != 0)
        // {
        //     string[] strs = mapAttrKeyName.Split('@');
        //     sqls = "";
        //     foreach (DataRow dr in mydt.Rows)
        //     {
        //         string key = dr.getValue(0).ToString();
        //         foreach (string mystr in strs)
        //         {
        //             if (mystr.Contains(key + "=") == false)
        //                 continue;

        //             string[] kv = mystr.Split('=');

        //             sqls += "@ UPDATE Sys_MapAttr SET Name='"+kv[2]+"' WHERE FK_MapData='' AND KeyOfEn=''";
        //         }
        //     }
        //     // 执行sql.
        //     DBAccess.RunSQLs(sqls);
        // }


        ////移除缓存.
        //MapData md = new MapData(fk_mapdata);
        //md.DeleteFromCash();

        // 备份文件
//        CCFlow.WF.Admin.XAP.DoPort.WriteToXmlMapData(fk_mapdata, false);

        if (StringHelper.isNullOrEmpty(str))
            return null;
        return str;
    }

  
}


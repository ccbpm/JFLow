package cn.jflow.controller.wf.admin.xap.service;


import java.io.IOException;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

@Deprecated
@WebService(targetNamespace="http://www.observer.com/service",serviceName="CCForm")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public interface CCForm {
	

    /// <summary>
    /// 获取值
    /// </summary>
    /// <param name="key"></param>
    /// <returns></returns>
	@WebMethod(operationName="CfgKey")
	@WebResult(name="result")
	public String CfgKey(String key);
    
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
	@WebMethod(operationName="AttachmentFiles")
	@WebResult(name="result")
    public String AttachmentFiles(String WorkID, String FK_Node, String FK_FrmAttachment, String UserNo, String UserName, byte[] FileByte, String fileName, String MyNote, String sort);
    
    
    /// <summary>
    /// 上传文件.
    /// </summary>
    /// <param name="FileByte"></param>
    /// <param name="fileName"></param>
    /// <returns></returns>
	@WebMethod(operationName="UploadFile")
	@WebResult(name="result")
    public String UploadFile(byte[] FileByte, String fileName) throws IOException;
    
    /// <summary>
    /// 执行sql
    /// </summary>
    /// <param name="sql"></param>
    /// <returns></returns>
	@WebMethod(operationName="RunSQL")
	@WebResult(name="result")
    public int RunSQL(String sql);

	@WebMethod(operationName="RunSQLs")
	@WebResult(name="result")
    public int RunSQLs(String sqls);
    
    /// <summary>
    /// 运行sql返回table.
    /// </summary>
    /// <param name="sql"></param>
    /// <returns></returns>
	@WebMethod(operationName="RunSQLReturnTable")
	@WebResult(name="result")
    public String RunSQLReturnTable(String sql);
    
    
    /// <summary>
    /// 运行sql返回table.
    /// </summary>
    /// <param name="sql"></param>
    /// <returns></returns>
	@WebMethod(operationName="RunSQLReturnJson")
	@WebResult(name="result")
    public String RunSQLReturnJson(String sql);
    
    /// <summary>
    /// 运行sql返回table.
    /// </summary>
    /// <param name="sql"></param>
    /// <returns></returns>
	@WebMethod(operationName="RunSQLReturnTable2DataTable")
	@WebResult(name="result")
    public String RunSQLReturnTable2DataTable(String sql);
    
    /// <summary>
    /// 运行sql返回String.
    /// </summary>
    /// <param name="sql"></param>
    /// <returns></returns>
	@WebMethod(operationName="RunSQLReturnString")
	@WebResult(name="result")
    public String RunSQLReturnString(String sql);
    
    /// <summary>
    /// 运行sql返回String.
    /// </summary>
    /// <param name="sql"></param>
    /// <returns></returns>
	@WebMethod(operationName="RunSQLReturnValInt")
	@WebResult(name="result")
    public int RunSQLReturnValInt(String sql);
    
    /// <summary>
    /// 运行sql返回float.
    /// </summary>
    /// <param name="sql"></param>
    /// <returns></returns>
	@WebMethod(operationName="RunSQLReturnValFloat")
	@WebResult(name="result")
    public float RunSQLReturnValFloat(String sql) throws Exception;
    
    /// <summary>
    /// 运行sql返回table.
    /// </summary>
    /// <param name="sql"></param>
    /// <returns></returns>
	@WebMethod(operationName="RunSQLReturnTableS")
	@WebResult(name="result")
    public String RunSQLReturnTableS(String sqls);
    
    /// <summary>
    /// 将中文转化成拼音.
    /// </summary>
    /// <param name="name"></param>
    /// <param name="flag">true: 全拼，false : 简拼</param>
    /// <returns></returns>
	@WebMethod(operationName="ParseStringToPinyin")
	@WebResult(name="result")
    public String ParseStringToPinyin(String name, boolean flag);
    

	@WebMethod(operationName="DealPK")
	@WebResult(name="result")
    public String DealPK(String pk, String fromMapdata, String toMapdata);

	@WebMethod(operationName="LetAdminLogin")
	@WebResult(name="result")
    public void LetAdminLogin();
   
    /// <summary>
    /// 让他登录
    /// </summary>
    /// <param name="user"></param>
    /// <param name="sid"></param>
	@WebMethod(operationName="LetUserLogin")
	@WebResult(name="result")
    public void LetUserLogin(String user, String sid);
    

    /// <summary>
    /// 保存Enum.
    /// </summary>
    /// <param name="enumKey"></param>
    /// <param name="enumLab"></param>
    /// <param name="cfg"></param>
    /// <returns></returns>
	@WebMethod(operationName="SaveEnum")
	@WebResult(name="result")
    public String SaveEnum(String enumKey, String enumLab, String cfg);
    

    /// <summary>
    /// 保存枚举字段
    /// </summary>
    /// <param name="fk_mapData">表单ID</param>
    /// <param name="fieldKey">字段值</param>
    /// <param name="fieldName">名</param>
    /// <param name="enumKey">枚举值</param>
    /// <returns>是否保存成功</returns>
	@WebMethod(operationName="SaveEnumField")
	@WebResult(name="result")
    public String SaveEnumField(String fk_mapData, String fieldKey, String fieldName, String enumKey, double x, double y);
    
    /// <summary>
    /// 保存外键字段
    /// </summary>
    /// <param name="fk_mapData">表单ID</param>
    /// <param name="fieldKey">字段值</param>
    /// <param name="fieldName">名</param>
    /// <param name="enName">枚举值</param>
    /// <returns>是否保存成功</returns>
	@WebMethod(operationName="SaveFKField")
	@WebResult(name="result")
    public String SaveFKField(String fk_mapData, String fieldKey, String fieldName, String enName, double x, double y);
    
    

	@WebMethod(operationName="SaveImageAsFile")
	@WebResult(name="result")
    public String SaveImageAsFile(byte[] img, String pkval, String fk_Frm_Ele);
    

    /// <summary>
    /// 装载表单模板
    /// </summary>
    /// <param name="fileByte">字节数</param>
    /// <param name="fk_mapData"></param>
    /// <param name="isClear"></param>
    /// <returns></returns>
	@WebMethod(operationName="LoadFrmTemplete")
	@WebResult(name="result")
    public String LoadFrmTemplete(byte[] fileByte, String fk_mapData, boolean isClear);
    

    /// <summary>
    /// 导入表单
    /// </summary>
    /// <param name="file">文件</param>
    /// <param name="fk_mapData">表单ID</param>
    /// <param name="isClear">是否清除现有的元素</param>
    /// <param name="isSetReadonly">是否设置的只读？</param>
    /// <returns>导入结果</returns>
	@WebMethod(operationName="LoadFrmTempleteFile")
	@WebResult(name="result")
    public String LoadFrmTempleteFile(String file, String fk_mapData, boolean isClear, boolean isSetReadonly);
    

    /// <summary>
    /// 获取xml数据
    /// </summary>
    /// <param name="xmlFileName"></param>
    /// <returns></returns>
	@WebMethod(operationName="GetXmlData")
	@WebResult(name="result")
    public String GetXmlData(String xmlFileName);
   
    
    

	@WebMethod(operationName="DoType")
	@WebResult(name="result")
    public String DoType(String dotype, String v1, String v2, String v3, String v4, String v5);
    
    /// <summary>
    /// 保存entity.
    /// 事例: @EnName=BP.Sys.FrmLabel@PKVal=Lin13b@X=100@Y=299@Text=我的标签
    /// </summary>
    /// <param name="vals"></param>
    /// <returns></returns>
	@WebMethod(operationName="SaveEn")
	@WebResult(name="result")
    public String SaveEn(String vals);
    

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
	@WebMethod(operationName="RequestSFTableV1")
	@WebResult(name="result")
    public String RequestSFTableV1(String uiBindKey) throws Exception;


	@WebMethod(operationName="InitFrm")
	@WebResult(name="result")
    public void InitFrm(String fk_mapdata) throws Exception;

    /// <summary>
    /// 获取一个Frm
    /// </summary>
    /// <param name="fk_mapdata">map</param>
    /// <param name="workID">可以为0</param>
    /// <returns>form描述</returns>
	@WebMethod(operationName="GenerFrm")
	@WebResult(name="result")
    public String GenerFrm(String fk_mapdata, int workID);
  

    /// <summary>
    /// 
    /// </summary>
    /// <param name="fromMapData"></param>
    /// <param name="fk_mapdata"></param>
    /// <param name="isClear">是否清除</param>
    /// <param name="isSetReadonly">是否设置为只读?</param>
    /// <returns></returns>
	@WebMethod(operationName="CopyFrm")
	@WebResult(name="result")
    public String CopyFrm(String fromMapData, String fk_mapdata, boolean isClear, boolean isSetReadonly);
    


	@WebMethod(operationName="SaveFrm")
	@WebResult(name="result")
    public String SaveFrm(String fk_mapdata, String xml, String sqls, String mapAttrKeyName);
    


    
//    @WebMethod(operationName="getTableSql")
//	@WebResult(name="result")
//    public TableSQL getTableSql(String tableName, DataColumnCollection columns);
    
    
    
}


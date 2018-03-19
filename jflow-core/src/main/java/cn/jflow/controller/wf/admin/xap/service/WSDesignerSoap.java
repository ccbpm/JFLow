package cn.jflow.controller.wf.admin.xap.service;

import java.io.IOException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

@WebService(targetNamespace="http://www.observer.com/service", serviceName="Service")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public interface WSDesignerSoap 
{

	/** 
	   人员修改
	 @param empNo 人员编号
	 @param deptNo 部门编号
	 @param attrs 人员属性格式为@字段1=值1@字段2=值2
	 @param stations 该人员在本部门下的岗位集合
	*/
	@WebMethod(operationName="Emp_Edit")
    @WebResult(name = "result")
	public String Emp_Edit(String empNo, String deptNo, String attrs, String stations);
	
	
	/**
	 * 获取表单树
	 * @return
	 */
	@WebMethod(operationName="GetFormTree")
    @WebResult(name = "result")
	public String GetFormTree();
	
	/**
	 * 获取流程树
	 * @return
	 */
	@WebMethod(operationName="GetFlowTree")
    @WebResult(name = "result")
	public String GetFlowTree();
	
	/**
	 * 获取轨迹图json数据
	 * @param fk_flow 流程编号
	 * @param workid 工作编号
	 * @return
	 */
	@WebMethod(operationName="GetFlowTrackJsonData")
    @WebResult(name = "result")
	public String GetFlowTrackJsonData(@WebParam(name = "fk_flow")String fk_flow, @WebParam(name = "workid")String workid);
	
	/**
	 * 流程设计器树控件数据源
	 * @param paras
	 * @return
	 */
	@WebMethod(operationName="GetFlowDesignerTree")
    @WebResult(name = "result")
	public String GetFlowDesignerTree(Boolean[] paras);
	
	/** 
	   人员新增
	 @param empNo 人员编号
	 @param deptNo 部门编号
	 @param attrs 人员属性格式为@字段1=值1@字段2=值2
	 @param stations 该人员在本部门下的岗位集合
	*/
	@WebMethod(operationName="Emp_New")
    @WebResult(name = "result")
	public String Emp_New(String empNo, String deptNo, String attrs, String stations);
	
	/** 
	   删除人员
	 @param empNo 人员编号
	 @param deptNo 部门编号
	 @return 
	*/
	@WebMethod(operationName="Emp_Delete")
    @WebResult(name = "result")
	public String Emp_Delete(String empNo, String deptNo);
	
	/** 
	 人员与部门进行关联
	 @param empNos 人员编号集合
	 @param deptNo 部门编号
	 @return 
	*/
	@WebMethod(operationName="Dept_Emp_Related")
    @WebResult(name = "result")
	public String Dept_Emp_Related(String empNos, String deptNo);

	/** 
	 编辑部门属性.
	 @param deptNo 部门编号
	 @param attrs 属性是@字段名=值
	 @param stations 多个用逗号分开.
	 @param dutys 多个职务用逗号分开.
	*/
	@WebMethod(operationName="Dept_Edit")
    @WebResult(name = "result")
	public void Dept_Edit(String deptNo, String attrs, String stations, String dutys);
	
	/** 
	 增加同级部门
	 
	 @param currDeptNo 当前部门编号
	 @param attrs 新部门属性
	 @param stations 新部门关联的岗位集合，用逗号分开.
	 @param dutys 新部门关联的职务集合，用逗号分开.
	 @return 新建部门的编号
	*/
	@WebMethod(operationName="Dept_CreateSameLevel")
    @WebResult(name = "result")
	public String Dept_CreateSameLevel(String currDeptNo, String attrs, String stations, String dutys);
	
	/** 
	 增加增加下级部门
	 
	 @param currDeptNo 当前部门编号
	 @param attrs 新部门属性
	 @param stations 新部门关联的岗位集合，用逗号分开.
	 @param dutys 新部门关联的职务集合，用逗号分开.
	 @return 新建部门的编号
	*/
	@WebMethod(operationName="Dept_CreateSubLevel")
    @WebResult(name = "result")
	public String Dept_CreateSubLevel(String currDeptNo, String attrs, String stations, String dutys);

	/** 
	 检查根节点
	 @return 
	*/
	@WebMethod(operationName="Dept_CheckRootNode")
    @WebResult(name = "result")
	public String Dept_CheckRootNode();
	
	/** 
	 拖动部门改变节点父编号
	 
	 @param currDeptNo 拖动节点
	 @param pDeptNo 拖动节点的父节点
	 @return 
	*/
	@WebMethod(operationName="Dept_DragTarget")
    @WebResult(name = "result")
	public String Dept_DragTarget(String currDeptNo, String pDeptNo);
	/** 
	 拖动部门进行排序
	 
	 @param currDeptNo 拖动节点
	 @param nextDeptNo 关系节点
	 @param nextNodeNos 下面节点的编号集合
	 @param isUpNode 是否拖动节点后的上面节点
	 @return 
	*/
	@WebMethod(operationName="Dept_DragSort")
    @WebResult(name = "result")
	public String Dept_DragSort(String currDeptNo, String nextDeptNo, String nextNodeNos, boolean isUpNode);
	
	/** 
	 检查部门是否存在
	 
	 @param attrs 部门名称
	 @return 
	*/

	@WebMethod(operationName="DeptName_Check")
    @WebResult(name = "result")
	public boolean DeptName_Check(String attrs);
	
	/** 
	 删除部门
	 
	 @param deptNo 部门编号
	 @param forceDel 强制删除
	 @return 返回删除信息
	*/

	@WebMethod(operationName="Dept_Delete")
    @WebResult(name = "result")
	public String Dept_Delete(String deptNo, boolean forceDel);


	/** 
	 获得共享模版的目录名称
	 
	 @return 用@符合分开的文件名.
	*/
	@WebMethod(operationName="GetDirs")
    @WebResult(name = "result")
	public String GetDirs(String dir, boolean FileOrDirecotry);

	public class FtpFile
	{
		public enum FileType
		{
			File,
			Directory;

			public int getValue()
			{
				return this.ordinal();
			}

			public static FileType forValue(int value)
			{
				return values()[value];
			}
		}
		public FileType Type = FileType.Directory;
		public String Name;
		public String Ext;
		public String Path;
		public FtpFile Super = null;
		public java.util.ArrayList<FtpFile> Subs;
		/** 
		 true标识该级目录一下为资源文件，可预览下载，在下级文件中配置值
		 
		*/
		public boolean CanViewAndDown;


		public final void SyncChildren()
		{
			//foreach (var item in this.Subs)
			//{
			//    item.Super = this;
			//}
		}
	}
	/** 
	 获得共享模版的目录名称
	 
	 @return 用@符合分开的文件名.
	*/
	public FtpFile GetDirectory();

//	private void getSubFile(FtpSupport.FtpConnection conn, FtpFile Superfile)
//	{
//		Superfile.Subs = new java.util.ArrayList<FtpFile>();
//		String path = Superfile.Path;
//
//		java.util.ArrayList<FtpSupport.Win32FindData> sts = getFiles(conn, path);
//		for (FtpSupport.Win32FindData item : sts)
//		{
//
//			FtpFile tempVar = new FtpFile();
//			tempVar.Name = item.FileName;
//			tempVar.Path = path + "\\" + item.FileName;
//			FtpFile file = tempVar;
//			FtpFile tempVar2 = new FtpFile();
//			tempVar2.Name = Superfile.getName();
//			tempVar2.Path = Superfile.Path;
//			file.Super = tempVar2;
//
//
//			if (item.FileAttributes == FileAttributes.Directory)
//			{
//				file.Type = FtpFile.FileType.Directory;
//				Superfile.Subs.add(file);
//			}
//			else
//			{
//				file.Type = FtpFile.FileType.File;
//				String tmp = item.FileName;
//
//				file.setName(tmp.substring(0, tmp.lastIndexOf('.')));
//				file.Ext = tmp.substring(tmp.lastIndexOf('.') + 1);
//
//
//				if (file.getName().contains("Flow"))
//				{
//					Superfile.CanViewAndDown = true;
//					Superfile.Type = FtpFile.FileType.File;
//				}
//				boolean flag = false;
//
//				for (var f : Superfile.Subs)
//				{
//					if (f.getName().equals(file.getName()))
//					{
//						flag = true;
//						break;
//					}
//				}
//
//				if (!flag)
//				{
//					Superfile.Subs.add(file);
//				}
//					;
//			}
//		}
//
//		Superfile.SyncChildren();
//
//		for (FtpFile item : Superfile.Subs)
//		{
//			if (item.Type == FtpFile.FileType.Directory)
//			{
//				getSubFile(conn, item);
//			}
//		}
//	}

//	private java.util.ArrayList<FtpSupport.Win32FindData> getFiles(FtpSupport.FtpConnection conn, String path)
//	{
//		java.util.ArrayList<FtpSupport.Win32FindData> sts = new java.util.ArrayList<Win32FindData>();
//		try
//		{
//			String tmp = conn.GetCurrentDirectory();
//			conn.SetCurrentDirectory("/");
//			conn.SetCurrentDirectory(path); //设置当前目录.
//			FtpSupport.Win32FindData[] f = conn.FindFiles();
//
//			for (FtpSupport.Win32FindData item : f)
//			{
//				if ((new String(".")).equals(item.FileName) || (new String("..")).equals(item.FileName) || (new String("")).equals(item.FileName))
//				{
//					continue;
//				}
//
//				sts.add(item);
//			}
//		}
//		catch (RuntimeException e)
//		{
//			throw new RuntimeException("FTP服务器读取目录出错：" + e.getMessage(), e);
//		}
//		return sts;
//	}


	public byte[] FlowTemplateDown(String[] FlowFileName);
//	{
//		String path = FlowFileName[0], fileName = FlowFileName[1], fileType = FlowFileName[2], cmd = FlowFileName[3];
//
//		if (StringHelper.isNullOrEmpty(path) || StringHelper.isNullOrEmpty(fileName) || StringHelper.isNullOrEmpty(fileType) || StringHelper.isNullOrEmpty(cmd))
//		{
//			throw new RuntimeException("FTP服务器文件读取参数出错!");
//		}
//
//		String ip = "online.ccflow.org", userNo = "ccflowlover", pass = "ccflowlover";
//		FtpConnection conn = new FtpConnection(ip, userNo, pass);
//
//		byte[] bytes = null;
//		try
//		{
//			bytes = new byte[] { };
//
//			conn.SetCurrentDirectory(path); //设置当前目录.
//			FtpStream fs = conn.OpenFile(fileName, GenericRights.Read);
//			if (null != fs)
//			{
//				System.IO.MemoryStream ms = new MemoryStream();
//				fs.CopyTo(ms);
//				bytes = new byte[ms.getLength()];
//				ms.Seek(0, SeekOrigin.Begin);
//				ms.Read(bytes, 0, bytes.length);
//			}
//		}
//		catch(RuntimeException e)
//		{
//			throw new RuntimeException("FTP服务器文件读取出错：" + e.getMessage(), e);
//		}
//		conn.Close();
//
//		if (null != bytes && 0 < bytes.length && fileType.equals("XML"))
//		{
//			if (cmd.equals("INSTALL"))
//			{ //在线安装
//				if (fileName.equals("Flow.xml"))
//				{
//					// 流程安装
//					path = this.FlowTemplateUpload(bytes, fileName);
//					bytes = System.Text.Encoding.UTF8.GetBytes(path);
//				}
//				else
//				{
//					//表单安装
//					//this.UploadfileCCForm(bytes, fileName, "");
//				}
//			}
//			else if (cmd.equals("DOWN"))
//			{ // 保存到本机
//
//				//HttpContext.Current.Response.BinaryWrite(bytes);
//				//string xml = System.Text.Encoding.UTF8.GetString(bytes, 0, bytes.Length);
//				//HttpContext.Current.Response.Write(xml);
//
//				//fileName = HttpUtility.UrlEncode(fileName);
//				//HttpContext.Current.Response.Charset = "GB2312";
//				//HttpContext.Current.Response.AppendHeader("Content-Disposition", "attachment;filename=" + fileName);
//				//HttpContext.Current.Response.ContentEncoding = System.Text.Encoding.GetEncoding("GB2312");
//
//				//HttpContext.Current.Response.Flush();
//				//HttpContext.Current.Response.End();
//				//HttpContext.Current.Response.Close();
//			}
//		}
//		return bytes;
//	}

//	@WebMethod(operationName="Dept_Edit")
//    @WebResult(name = "result")
//	public String TurnXmlDataSet2SLDataSet(String ds);
	/** 
	 获取值
	 @param kev
	 @return 
	*/
	@WebMethod(operationName="CfgKey")
	@WebResult(name = "result")
	public String CfgKey(String kev, String UserNo, String SID);
	
	/** 
	 保存ens
	
	 @param vals
	 @return 
	*/
	@WebMethod(operationName="SaveEn")
	@WebResult(name = "result")
	public String SaveEn(String vals);


	/** 
	 上传文件.
	 
	 @param FileByte
	 @param fileName
	 @return 
	*/

//	public final String UploadFile(byte[] FileByte, String fileName)
//	{
//		String path = System.Web.HttpContext.Current.Request.PhysicalApplicationPath;
//
//		String filePath = path + "\\" + fileName;
//		if (System.IO.File.Exists(filePath))
//		{
//			System.IO.File.Delete(filePath);
//		}
//
//		//这里使用绝对路径来索引
//		FileStream stream = new FileStream(filePath, FileMode.CreateNew);
//		stream.Write(FileByte, 0, FileByte.length);
//		stream.Close();
//
//		DataSet ds = new DataSet();
//		ds.ReadXml(filePath);
//
//		return Silverlight.DataSetConnector.Connector.ToXml(ds);
//	}

	@WebMethod(operationName="RunSQL")
    @WebResult(name = "result")
	public int RunSQL(String sql, String UserNo, String SID);
	/** 
	 运行sqls
	 
	 @param sqls
	 @return 
	*/
	@WebMethod(operationName="RunSQLs")
    @WebResult(name = "result")
	public int RunSQLs(String sqls, String UserNo, String SID);
	
	/** 
	 运行sql返回table.
	 
	 @param sql
	 @return 
	*/
	@WebMethod(operationName="RunSQLReturnTable")
    @WebResult(name = "result")
	public String RunSQLReturnTable(String sql, String UserNo, String SID);
	
	/** 
	 运行sql返回String.
	 
	 @param sql
	 @return 
	*/
	@WebMethod(operationName="RunSQLReturnString")
    @WebResult(name = "result")
	public String RunSQLReturnString(String sql, String UserNo, String SID);
	
	/** 
	 运行sql返回String.
	 @param sql
	 @return 
	*/
	@WebMethod(operationName="RunSQLReturnValInt")
    @WebResult(name = "result")
	public int RunSQLReturnValInt(String sql,String UserNo, String SID);
	
	/** 
	 运行sql返回float.
	 
	 @param sql
	 @return 
	*/
	@WebMethod(operationName="RunSQLReturnValFloat")
    @WebResult(name = "result")
	public float RunSQLReturnValFloat(String sql,String UserNo, String SID);
	
	/** 
	 运行sql返回table.
	 
	 @param sql
	 @return 
	*/
	@WebMethod(operationName="RunSQLReturnTableS")
    @WebResult(name = "result")
	public String RunSQLReturnTableS(String sqls, String UserNo,String SID);
	

	
	/** 
	   将中文转化成拼音.
	 @param name
	 @return 
	*/

	@WebMethod(operationName="ParseStringToPinyin")
    @WebResult(name = "result")
	public String ParseStringToPinyin(String name);
	
	/** 
	   获取自定义表
	 @param ensName
	 @return 
	*/
	@WebMethod(operationName="RequestSFTable")
    @WebResult(name = "result")
	public String RequestSFTable(String ensName);
	
//	public void LetAdminLogin();

//	public String SaveEnum(String enumKey, String enumLab, String cfg);

	/**
	 * 获取工具箱数据
	 * @return
	 */
	public String[] GetNodeIconFile();
	

	/** 
	 执行功能返回信息
	 
	 @param doType
	 @param v1
	 @param v2
	 @param v3
	 @param v4
	 @param v5
	 @return 
	*/
	@WebMethod(operationName="DoType")
    @WebResult(name = "result")
	public String DoType(String doType, String v1, String v2, String v3, String v4, String v5);
	

	/** 
	 根据workID获取工作列表
	 FK_Node 节点ID
	 rdt 记录日期，也是工作接受日期。
	 sdt 应完成日期.
	 FK_emp 操作员编号。
	 EmpName 操作员名称.
	 @param workid workid
	 @return 
	*/
	@WebMethod(operationName="GetDTOfWorkList")
    @WebResult(name = "result")
	public String GetDTOfWorkList(String fk_flow, String workid);
	
	@WebMethod(operationName="GetWorkList")
    @WebResult(name = "result")
	public String GetWorkList(String fk_flow, String workid);
	
	/** 
	 让admin 登录
	 @param lang 当前的语言
	 @return 成功则为空，有异常时返回异常信息
	*/
	@WebMethod(operationName="LetAdminLogin")
    @WebResult(name = "result")
	public String LetAdminLogin(String lang, boolean islogin);


	@WebMethod(operationName="GetFlowBySort")
    @WebResult(name = "result")
	public String GetFlowBySort(String sort);
	

	@WebMethod(operationName="Do")
    @WebResult(name = "result")
	public String Do(String doWhat, String para1, boolean isLogin) throws Exception;
	

	/** 
	 @param isLogin
	 @param param fk_flow,nodeName,icon,x,y,HisRunModel
	 @return 
	 @return 返回节点编号
	*/
	@WebMethod(operationName="DoNewNode1")
    @WebResult(name = "result")
	public int DoNewNode1(boolean isLogin, String... param);
	

	/** 
	 创建一个节点
	 @param fk_flow 流程编号
	 @param x
	 @param y
	 @return 返回节点编号
	*/
	@WebMethod(operationName="DoNewNode")
    @WebResult(name = "result")
	public int DoNewNode(boolean isLogin, String[] param);

	/** 
	 删除一个连接线
	 @param from 从节点
	 @param to 到节点
	 @return 
	 * @throws Exception 
	*/
	@WebMethod(operationName="DoDropLine")
    @WebResult(name = "result")
	public boolean DoDropLine(int from, int to) throws Exception;
	

	/** 
	 创建一个标签
	 @param fk_flow 流程编号
	 @param x
	 @param y
	 @return 返回标签编号
	*/
	@WebMethod(operationName="DoNewLabel")
    @WebResult(name = "result")
	public String DoNewLabel(String fk_flow, int x, int y, String name, String lableId);

	@WebMethod(operationName="FlowTemplateUpload")
    @WebResult(name = "result")
	public String FlowTemplateUpload(byte[] FileByte, String fileName) throws IOException;

	/** 
	 @param FK_flowSort 流程类别编号
	 @param Path 模板文件路径
	 @param ImportModel
	 @param Islogin 0,1,2,3
	 @return 
	*/
	@WebMethod(operationName="FlowTemplateLoad")
    @WebResult(name = "result")
	public String FlowTemplateLoad(String FK_flowSort, String Path, int ImportModel, int SpecialFlowNo);
	

	/** 
	 保存流程
	 
	 @param fk_flow
	 @param nodes
	 @param dirs`
	 @param labes
	*/
	@WebMethod(operationName="FlowSave")
    @WebResult(name = "result")
	public String FlowSave(String fk_flow, String nodes, String dirs, String labes);


	@WebMethod(operationName="UploadfileCCForm")
    @WebResult(name = "result")
	public String UploadfileCCForm(byte[] FileByte, String fileName, String fk_frmSort) throws IOException;


	@WebMethod(operationName="GetConfig")
    @WebResult(name = "result")
	public String GetConfig(String key);
	
	/**
	 * 发送消息接口. 需要与web.config中 ShortMessageWriteTo 配置才能起作用。
	 * 发送短信接口(二次开发需要重写这个接口) 
	 * @param msgPK 消息主键，是对应的Sys_SMS的MyPK。
	 * @param sender 发送人(内部帐号，可以为空.)
	 * @param sendToEmpNo 发送给(内部帐号，可以为空.)
	 * @param tel 手机号码
	 * @param msgInfo 短消息
	 * @return 是否发送成功
	 */
	@WebMethod(operationName="SendToWebServices")
    @WebResult(name = "result")
	public boolean SendToWebServices(String msgPK, String sender, String sendToEmpNo, String tel, String msgInfo);
	
	/**
	 * 发送邮件
	 * @param mypk 消息主键，是对应的Sys_SMS的MyPK。
	 * @param sender 发送人
	 * @param sendToEmpNo 发送给 
	 * @param email 邮件地址
	 * @param title 标题
	 * @param maildoc 内容
	 * @return
	 */
	@WebMethod(operationName="SendToEmail")
    @WebResult(name = "result")
	public boolean SendToEmail(String mypk, String sender, String sendToEmpNo, String email, String title, String maildoc);
	
	/**
	 * 发送叮叮
	 * @param mypk 消息主键，是对应的Sys_SMS的MyPK。
	 * @param sender 发送人
	 * @param sendToEmpNo 发送给 
	 * @param tel 手机号码
	 * @param msgInfo 短消息
	 * @return
	 */
	@WebMethod(operationName="SendToDingDing")
    @WebResult(name = "result")
	public boolean SendToDingDing(String mypk, String sender, String sendToEmpNo, String tel, String msgInfo);

	/**
	 * 发送微信消息
	 * @param mypk 消息主键，是对应的Sys_SMS的MyPK。
	 * @param sender 发送人
	 * @param sendToEmpNo 发送给 
	 * @param tel 手机号码
	 * @param msgInfo 短消息
	 * @return
	 */
	@WebMethod(operationName="SendToWeiXin")
    @WebResult(name = "result")
	public boolean SendToWeiXin(String mypk, String sender, String sendToEmpNo, String tel, String msgInfo);
}
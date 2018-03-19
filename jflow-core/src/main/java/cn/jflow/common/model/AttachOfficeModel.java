package cn.jflow.common.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.Button;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentAttr;
import BP.Sys.FrmAttachmentDB;
import BP.Tools.FileAccess;
import BP.Tools.StringHelper;
import BP.WF.WorkFlow;
import BP.WF.Data.GERpt;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.WFState;
import BP.Web.WebUser;

public class AttachOfficeModel {
	public AttachOfficeModel() {

	}

	private String PKVal;
	private String DelPKVal;
	private String FK_FrmAttachment;
	private String DoType;
	private String FK_Node;
	private String NoOfObj;
	private String FK_MapData;
	private String UserName;
	private boolean ReadOnly;

	public boolean isReadOnly() {
		return ReadOnly;
	}

	public void setReadOnly(boolean readOnly) {
		ReadOnly = readOnly;
	}

	private String FileSavePath;

	public String getFileSavePath() {
		return FileSavePath;
	}

	public void setFileSavePath(String fileSavePath) {
		FileSavePath = fileSavePath;
	}

	private String RealFileName;

	public String getRealFileName() {
		return RealFileName;
	}

	public void setRealFileName(String realFileName) {
		RealFileName = realFileName;
	}

	private String FileFullName;

	public String getFileFullName() {
		return FileFullName;
	}

	public void setFileFullName(String fileFullName) {
		FileFullName = fileFullName;
	}

	private String NodeInfo;

	public String getNodeInfo() {
		Node nodeInfo = new Node();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		return nodeInfo.getName() + ": " + WebUser.getName() + "    时间:"
				+ sdf.format(new Date());
	}

	public void setNodeInfo(String nodeInfo) {
		NodeInfo = nodeInfo;
	}

	private boolean IsCheck;

	public boolean isIsCheck() {
		return IsCheck;
	}

	public void setIsCheck(boolean isCheck) {
		IsCheck = isCheck;
	}

	private boolean IsSavePDF;

	public boolean isIsSavePDF() {
		return IsSavePDF;
	}

	public void setIsSavePDF(boolean isSavePDF) {
		IsSavePDF = isSavePDF;
	}

	private boolean IsMarks;

	public boolean isIsMarks() {
		return IsMarks;
	}

	public void setIsMarks(boolean isMarks) {
		IsMarks = isMarks;
	}

	private boolean IsPostBack;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private String fileType;

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public AttachOfficeModel(String PKVal, String DelPKVal,
			String FK_FrmAttachment, String DoType, String FK_Node,
			String NoOfObj, String FK_MapData, String UserName,
			boolean ReadOnly, String FileSavePath, String RealFileName,
			String FileFullName, String NodeInfo, boolean IsCheck,
			boolean IsSavePDF, boolean IsMarks, boolean IsPostBack,
			HttpServletRequest req, HttpServletResponse res) {
		this.PKVal = PKVal;
		this.DelPKVal = DelPKVal;
		this.FK_FrmAttachment = FK_FrmAttachment;
		this.DoType = DoType;
		this.FK_Node = FK_Node;
		this.NoOfObj = NoOfObj;
		this.FK_MapData = FK_MapData;
		this.UserName = UserName;
		this.ReadOnly = ReadOnly;
		this.FileSavePath = FileSavePath;
		this.RealFileName = RealFileName;
		this.FileFullName = FileFullName;
		this.NodeInfo = NodeInfo;
		this.IsCheck = IsCheck;
		this.IsSavePDF = IsSavePDF;
		this.IsMarks = IsMarks;
		this.IsPostBack = IsPostBack;
		this.request = req;
		this.response = res;
	}

	public StringBuffer divMenu = null;

	public void init(Object sender) {
		this.ui = new UiFatory();
		this.divMenu = new StringBuffer();
		if ((StringHelper.isNullOrEmpty(String.valueOf(FK_MapData)))
				|| (StringHelper.isNullOrEmpty(String.valueOf(FK_FrmAttachment)))) {
			divMenu.append("<h1 style='color:red'>传入参数错误!<h1>");
			return;
		}

		if (!IsPostBack) {
			String type = this.request.getParameter("action")==null?"":this.request.getParameter("action");
			if (type == null && type.equals("")) {
				InitOffice(true);
			} else {
				InitOffice(false);
				if (type.equals("LoadFile"))
					LoadFile();
				else if (type.equals("SaveFile"))
					SaveFile();
				else
					try {
						throw new Exception("传入的参数不正确!");
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
	}

	public void InitOffice(boolean isMenu) {
		boolean isCompleate = false;
		Node node = new Node(FK_Node);
		try {

			WorkFlow workFlow = new WorkFlow(node.getFK_Flow(),
					Integer.parseInt(PKVal));
			isCompleate = workFlow.getIsComplete();
		} catch (Exception e) {
			try {
				Flow fl = new Flow(node.getFK_Flow());
				GERpt rpt = fl.getHisGERpt();
				rpt.setOID(Integer.parseInt(PKVal));
				rpt.Retrieve();

				if (rpt != null) {
					if (rpt.getWFState() == WFState.Complete)
						isCompleate = true;
				}
			} catch (Exception ex) {

			}

		}
		if (!isCompleate) {
			try {
				isCompleate = !BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(
						node.getFK_Flow(), node.getNodeID(),
						Integer.parseInt(PKVal), WebUser.getNo());
				// WorkFlow workFlow = new WorkFlow(node.FK_Flow,
				// Int64.Parse(PKVal));
				// isCompleate = !workFlow.IsCanDoCurrentWork(WebUser.getNo());
			} catch (Exception ex) {

			}

		}

		FrmAttachment attachment = new FrmAttachment();
		int result = 0;
		// 表单编号与节点不为空
		if (this.FK_MapData != null && this.FK_Node != null) {
			BP.En.QueryObject objInfo = new BP.En.QueryObject(attachment);
			objInfo.AddWhere(FrmAttachmentAttr.FK_MapData, this.FK_MapData);
			objInfo.addAnd();
			objInfo.AddWhere(FrmAttachmentAttr.FK_Node, this.FK_Node);
			objInfo.addAnd();
			objInfo.AddWhere(FrmAttachmentAttr.NoOfObj, this.NoOfObj);
			result = objInfo.DoQuery();
			// result = attachment.Retrieve(FrmAttachmentAttr.FK_MapData,
			// this.FK_MapData,
			// FrmAttachmentAttr.FK_Node, this.FK_Node,
			// FrmAttachmentAttr.NoOfObj, this.DelPKVal);
		}
		if (result == 0) /* 如果没有定义，就获取默认的. */
		{
			attachment.setMyPK(this.FK_FrmAttachment);
			attachment.Retrieve();
		}
		if (!isCompleate) {
			if (attachment.getIsWoEnableReadonly()) {
				ReadOnly = true;
			}
			if (attachment.getIsWoEnableCheck()) {
				IsCheck = true;
			}
			IsMarks = attachment.getIsWoEnableMarks();

		} else {
			ReadOnly = true;
		}
		if (isMenu && !isCompleate) {
			// #region 初始化按钮
			if (attachment.getIsWoEnableViewKeepMark()) {
				divMenu.append("<select id='marks' onchange='ShowUserName()'  style='width: 100px'><option value='1'>全部</option><select>");
			}

			if (attachment.getIsWoEnableTemplete()) {
				AddBtn("openTempLate", "打开模板", "OpenTempLate");
			}
			if (attachment.getIsWoEnableSave()) {
				AddBtn("saveFile", "保存", "saveOffice");
			}
			if (attachment.getIsWoEnableRevise()) {
				AddBtn("accept", "接受修订", "acceptOffice");
				AddBtn("refuse", "拒绝修订", "refuseOffice");
			}

			if (attachment.getIsWoEnableOver()) {
				AddBtn("over", "套红文件", "overOffice");
			}

			if (attachment.getIsWoEnablePrint()) {

				AddBtn("print", "打印", "printOffice");

			}
			if (attachment.getIsWoEnableSeal()) {
				AddBtn("seal", "签章", "sealOffice");
			}
			if (attachment.getIsWoEnableInsertFlow()) {
				AddBtn("flow", "插入流程图", "InsertFlow");
			}
			if (attachment.getIsWoEnableInsertFlow()) {
				AddBtn("fegnxian", "插入风险点", "InsertFengXian");
			}
			if (attachment.getIsWoEnableDown()) {
				AddBtn("download", "下载", "DownLoad");
			}
		}
		// #region 初始化文件

		FrmAttachmentDB downDB = new FrmAttachmentDB();

		downDB.setMyPK(this.DelPKVal);
		downDB.Retrieve();
		setFileName(downDB.getFileName());
		setFileType(downDB.getFileExts());
		RealFileName = downDB.getFileName();
		FileFullName = downDB.getFileFullName();
		FileSavePath = attachment.getSaveTo();
	}

	public void LoadFile() {
		try {
			String loadType = this.request.getParameter("LoadType")==null?"":this.request.getParameter("LoadType");
			String type = fileType;
			String name = this.request.getParameter("fileName")==null?"":this.request.getParameter("fileName");
			String path = null;
			if (loadType.equals("1")) {
				try {
					path = request.getSession().getServletContext()
							.getRealPath(FileFullName);

				} catch (Exception ex) {
					path = FileFullName;
				}
			} else {
				path = request.getSession().getServletContext()
						.getRealPath("/DataUser/OfficeTemplate/" + name);
			}

			String result = FileAccess.readFileByBytes(path);
             
             this.response.getOutputStream().write(result.getBytes());
//			File file = new File(path);
//			FileReader fileread = new FileReader(file);
//			BufferedReader br = new BufferedReader(fileread);
//			String read = "";
//			String readStr = "";
//			while ((read = br.readLine()) != null) {
//				readStr = readStr + read;
//			}
//
//			response.flushBuffer();
//
//			response.getWriter().write(readStr);
			// response.End();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void SaveFile() {
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) this.request;
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				MultipartFile file = entity.getValue();// 获取上传文件对象
				// File[] files = request.Files;

				// string file = Request["Path"];
				// file = HttpUtility.UrlDecode(file, Encoding.UTF8);

				// if (files.length > 0) {
				// /'检查文件扩展名字
				MultipartFile postedFile = file;
				String path = "";
				try {
					path = request.getSession().getServletContext()
							.getRealPath(FileFullName);
				} catch (Exception ex) {
					path = FileFullName;

				}

				String fileName = file.getOriginalFilename();
				String fileExtension = FileAccess.getExtensionName(fileName);
				// postedFile.SaveAs(path);
				FileOutputStream fos = null;

				try {
					// if (!postedFile.exists()) {// 文件不存在则创建
					// postedFile.createNewFile();
					// }
					// fos = new FileOutputStream(postedFile);
					fos.write(path.getBytes());// 写入文件内容
					fos.flush();
				} catch (IOException e) {
					System.err.println("文件创建失败");
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							System.err.println("文件流关闭失败");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public UiFatory ui = null;

	public void AddBtn(String id, String label, String clickEvent) {
		Button btn = this.ui.creatButton(id);
		btn.setText(label);
		btn.addAttr("onclick", "return " + clickEvent + "()");
		btn.addAttr("class", "btn");
		divMenu.append(btn);
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param file
	 * @return
	 */
	private static String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		} else {
			return "";
		}
	}
}

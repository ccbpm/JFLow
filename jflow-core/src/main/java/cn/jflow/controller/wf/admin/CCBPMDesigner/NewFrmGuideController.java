package cn.jflow.controller.wf.admin.CCBPMDesigner;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import BP.Sys.FrmType;
import BP.Sys.MapData;
import BP.Tools.FileAccess;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/NewFrmGuide")
public class NewFrmGuideController extends BaseController {

	public StringBuffer Pub1;

	public final int getFrmType() {
		try {
			return Integer.parseInt(this.getRequest().getParameter("FrmType"));
		} catch (java.lang.Exception e) {
			return 0;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/BindStep1_Click", method = RequestMethod.POST)
	public String BindStep1_Click(HttpServletRequest request, HttpServletResponse response,String TB_Name) throws IOException {
		MapData md = new MapData();
		md.setName(request.getParameter("TB_Name"));
		md.setNo(request.getParameter("TB_No"));
		md.setPTable(request.getParameter("TB_PTable"));
		md.setFK_FrmSort(request.getParameter("DDL_FrmTree"));
		md.setFK_FormTree(request.getParameter("DDL_FrmTree"));
		md.setAppType("0"); //独立表单
		md.setDBSrc(request.getParameter("DBSrc"));

		if (md.getName().length() == 0 || md.getNo().length() == 0 || md.getPTable().length() == 0) {
			//BP.Sys.PubClass.Alert("必填项不能为空.", this.getResponse());
			return "必填项不能为空.";
		}

		if (md.getIsExits() == true) {
			//BP.Sys.PubClass.Alert("表单ID:" + md.getNo() + "已经存在.", this.getResponse());
			return "表单ID:" + md.getNo() + "已经存在.";
		}

		md.setHisFrmTypeInt(this.getFrmType()); //表单类型.

		switch (FrmType.forValue(this.getFrmType())) {
		//自由，傻瓜，SL表单不做判断
		case FreeFrm:
		case FoolForm:
		case Silverlight:
			break;
		case Url:
			String url = request.getParameter("TB_Url");
			if (isNullOrEmpty(url)) {
				//BP.Sys.PubClass.Alert("必填项不可以为空", this.getResponse());
				return "必填项不可以为空";
			}
			md.setUrl(url);
			break;
		//如果是以下情况，导入模式
		case WordFrm:
		case ExcelFrm:
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest
					.getFile("fUpFrm");

			String name = multipartRequest.getParameter("name");
			// 获得文件名：
			String realFileName = file.getOriginalFilename();
			String savePath = null;
			String ext = FileAccess
					.getExtensionName(file.getOriginalFilename());

			ext = ext.toLowerCase();

			if (this.getFrmType() == FrmType.ExcelFrm.getValue() && !ext.equals("xls") && !ext.equals("xlsx")) {

				//BP.Sys.PubClass.Alert("上传的Excel文件格式错误.");
				return "上传的Excel文件格式错误.";
			}

			if (this.getFrmType() == FrmType.WordFrm.getValue() && !ext.equals("doc") && !ext.equals("docx")) {
				//BP.Sys.PubClass.Alert("上传的Word文件格式错误.");
				return "上传的Word文件格式错误.";
			}
			savePath = BP.Sys.SystemConfig.getPathOfDataUser() + "FrmOfficeTemplate/";
			// 创建文件
			File dirPath = new File(savePath);
			if (!dirPath.exists()) {
				dirPath.mkdir();
			}
			realFileName.replace("\\\\", "\\");
			File uploadFile = new File(savePath + request.getParameter("TB_No") + "." +ext);
			//System.out.println("文件保存路径：\n"+savePath + request.getParameter("TB_No") + ext);
			FileCopyUtils.copy(file.getBytes(), uploadFile);
			break;
		default:
			throw new RuntimeException("未知表单类型.");
		}

		md.Insert();

		if (md.getHisFrmType().getValue() == FrmType.WordFrm.getValue() || md.getHisFrmType().getValue() == FrmType.ExcelFrm.getValue()) {

			//把表单模版存储到数据库里 

			////this.Pub1.append(Clear());
			Pub1 = new StringBuffer();
			this.Pub1.append("已经成功创建excel表单，请按照如下步骤处理。");
			this.Pub1.append("\n");
			this.Pub1.append("请打开excel表单模版");
			//this.getResponse().getWriter().write(Pub1.toString());
			return Pub1.toString();
		}

		if (md.getHisFrmType().getValue() == FrmType.FreeFrm.getValue() && "RB_FrmGenerMode".equals(request.getParameter("RB_FrmGenerMode_2"))) {////
			this.getResponse().sendRedirect("../MapDef/ImpTableField.jsp?DoType=New&FK_MapData=" + md.getNo());
		}

//		if (md.getHisFrmType().getValue() == FrmType.FreeFrm.getValue()) {//此页面为H5表单，未翻译。暂时注释
//			this.getResponse().sendRedirect("FormDesigner.jsp?FK_MapData=" + md.getNo());
//		}


		if (md.getHisFrmType().getValue() == FrmType.FoolForm.getValue()) {
			this.getResponse().	sendRedirect("/WF/Admin/FoolFormDesigner/Designer.jsp?IsFirst=1&FK_MapData=" + md.getNo());
		}
		return "操作成功";
	}

	public static boolean isNullOrEmpty(String string) {
		return string == null || string.equals("");
	}

	@RequestMapping(value = "/tb_TextChanged", method = RequestMethod.POST)
	@ResponseBody
	private String tb_TextChanged(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String name = this.getRequest().getParameter("TB_Name");
		String val = "";

		if (this.getRequest().getParameter("ss").equals("RB0")) {
			val = BP.DA.DataType.ParseStringToPinyin(name);
		} else {
			val = BP.DA.DataType.ParseStringToPinyinJianXie(name);
		}

		////this.Pub1.append(this.getRequest().getParameter("TB_No").setText(val));
		////this.Pub1.append(this.getRequest().getParameter("TB_PTable").setText("CCFrom_" + val));
		return "{\"TB_No\":\""+val+"\",\"TB_PTable\":\"CCFrom_"+ val+"\"}";
	}
}

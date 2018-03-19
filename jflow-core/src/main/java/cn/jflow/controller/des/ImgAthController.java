package cn.jflow.controller.des;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.jflow.controller.wf.workopt.BaseController;
import BP.Sys.FrmImgAthDB;
import BP.Tools.FileAccess;
import BP.Web.WebUser;

@Controller
@RequestMapping(value="/DES")
public class ImgAthController extends BaseController{
	
	//确定按钮
	@RequestMapping(value="/btnSubmit", method = RequestMethod.POST)
    public void btnSubmit(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="fileToUpload") CommonsMultipartFile fileToUpload,String FK_MapData,String ImgAths,String MyPK,String img) throws Exception
    {
    	
        String sourceFile = img;
        String myName = ImgAths + "_" + MyPK;
        String type = sourceFile.substring(sourceFile.lastIndexOf(".") + 1); //得到文件后缀名 
        File fileInfo = new File(request.getSession().getServletContext().getRealPath("/")+"/"+sourceFile);
        float fileSize = 0;
        if (fileInfo.exists())
            fileSize = Float.parseFloat(""+fileInfo.length());

        FrmImgAthDB imgDB = new FrmImgAthDB();
        imgDB.setFK_MapData(FK_MapData);
        imgDB.setFK_FrmImgAth(ImgAths);
        imgDB.setRefPKVal(MyPK);
        imgDB.setFileFullName(sourceFile);
        imgDB.setFileName(myName);
        imgDB.setFileExts(type);
        imgDB.setFileSize(fileSize);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd mm:HH");
        imgDB.setRDT(sdf.format(new Date()));
        imgDB.setRec(WebUser.getNo());
        imgDB.setRecName(WebUser.getName());

        try
        {
            imgDB.Save();
        }
        catch(Exception e)
        {
            imgDB.CheckPhysicsTable();
            imgDB.Save();
        }
        String path = request.getSession().getServletContext().getRealPath("/DataUser/ImgAth/Data/");
        File file=new File(path);
        if(!file.exists()){
        	file.mkdirs();
        }
        path = path + File.separator + myName + ".png";
        FileUtils.copyInputStreamToFile(fileToUpload.getInputStream(), new File(path));
        //fileToUpload.transferTo(file);
        //FileAccess.Copy(fileToUpload.getInputStream(),BP.WF.Glo.getCCFlowAppPath() + "DataUser/ImgAth/Data/" + myName + ".png");
        //fileToUpload.getFileItem().write(file); // 将上传的文件写入新建的文件中
        //fileToUpload.getFileItem().write(file); 
        response.sendRedirect("/jflow-web/WF/CCForm/ImgAth.jsp?FK_MapData=" + FK_MapData + "&MyPK=" + MyPK + "&ImgAth=" + ImgAths);
        
    }
    
	 //复制文件
    public void CopyFile(HttpServletRequest request,String SourceFile, String ObjectFile)
    {
        String sourceFilePath = request.getSession().getServletContext().getRealPath("/");
        String objectFile = request.getSession().getServletContext().getRealPath("/")+ObjectFile;
        File f=new File(sourceFilePath);
        if (f.exists())
        {
            FileAccess.Copy(sourceFilePath, objectFile);
        }
    }
    
   
	
}

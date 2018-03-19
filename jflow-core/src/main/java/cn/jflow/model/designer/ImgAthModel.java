package cn.jflow.model.designer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.FrmImgAthDB;
import BP.Tools.StringHelper;
import cn.jflow.common.model.BaseModel;

public class ImgAthModel extends BaseModel{

	public ImgAthModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	 private String sourceFile = "";
	 
	 
     public String getSourceFile() {
		return sourceFile;
	}
	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}
	public int getH()
     {
             try
             {
                 return Integer.parseInt(this.get_request().getParameter("H"));
             }
             catch(Exception e)
             {
                 return 120;
             }
     }
     public int getW()
     {
             try
             {
                 return Integer.parseInt(this.get_request().getParameter("W"));
             }
             catch(Exception e)
             {
                 return 100;
             }
     }
 
     public String getImgAths()
     {
             return this.get_request().getParameter("ImgAth");
     }
     
 
	public String getMSG()
     {
          return this.get_request().getParameter("MSG"); 
     }
     public String MSGWidth()
     {
         return this.get_request().getParameter("MSGWidth"); 
     }
	public void init(){
		
             if (StringHelper.isNullOrEmpty(getMSG()))
             {

                 this.setSourceFile("DataUser/ImgAth/Def.jpg");
                 String myPK = this.getImgAths() + "_" + this.getMyPK();
                 FrmImgAthDB imgDB = new FrmImgAthDB();
                 imgDB.setMyPK(myPK);
                 imgDB.RetrieveFromDBSources();

                 if (imgDB != null && !StringHelper.isNullOrEmpty(imgDB.getFileFullName()))
                 {
                	 File file=new File(this.get_request().getSession().getServletContext().getRealPath("/")+imgDB.getFileFullName());
                     if (file.exists())
                         sourceFile = imgDB.getFileFullName();
                 }
                 //sourceImg.Value = sourceFile;
                 this.get_request().setAttribute("sourceFile", getSourceFile());
                 //newImgUrl.Value = sourceFile;
//                 Page.ClientScript.RegisterStartupScript(this.GetType(),
//                     "js", "<script>ImageCut('" + sourceFile + "','" + getW() + "','" + getH() + "' )</script>");
             }
             else
             {
            	 this.get_request().setAttribute("sourceFile", getMSG());
//                 sourceImg.Value = getMSG();
//                 newImgUrl.Value = getMSG();
//                 Page.ClientScript.RegisterStartupScript(this.GetType(),
//                     "js", "<script>ImageCut('" + getMSG() + "','" + getW() + "','" + getH() + "' )</script>");
             }
	}
	

    
    public int GetImage(String url, int widthSize) throws IOException
    {
    	File file=new File(this.get_request().getSession().getServletContext().getRealPath("/")+url);
        if (file.exists())
        {
            BufferedImage imgOutput =ImageIO.read(file);// System.Drawing.Bitmap.FromFile(System.Web.HttpContext.Current.Server.MapPath());
            if (imgOutput.getWidth() > widthSize)
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
        else
        {
            return 1;
        }
    }
	
}

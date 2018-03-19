package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.En.ClassFactory;
import BP.En.Entity;
import BP.Sys.ActionTypeS;
import BP.Sys.SysFileManager;
import cn.jflow.common.model.BaseModel;

public class DoModel extends BaseModel{
	
	public DoModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	public ActionTypeS getGetActionType()
	{
		 return ActionTypeS.forValue(Integer.parseInt(this.get_request().getParameter("ActionType").toString()));
	}
	
    public String getDoType()
    {
    	String str= this.get_request().getParameter("DoType");
    	if ("".equals(str))
    		str = null;
    	return str;
    }
    
    public String getRefPK()
    {
            String s = this.get_request().getParameter("RefPK");
            if (s == null || "".equals(s) )
                s = this.get_request().getParameter("PK");

            return s;
    }
	
	public void Page_Load() throws Exception
	{
		
		if(this.getDoType().equals("DelGradeEns")){
			 Entity enGrade = BP.En.ClassFactory.GetEns(this.getEnsName()).getGetNewEntity();
             enGrade.setPKVal(this.getRefPK());
             enGrade.Delete();
             this.WinClose();
             return;
		}
		else if(this.getDoType().equals("DownFile")){
			Entity enF = BP.En.ClassFactory.GetEn(this.getEnName());
            enF.setPKVal(this.get_request().getParameter("PK"));
            enF.Retrieve();
            String pPath = enF.GetValStringByKey("MyFilePath") + "/" + enF.getPKVal() + "." + enF.GetValStringByKey("MyFileExt");
            BP.Sys.PubClass.DownloadFile(pPath,
                enF.GetValStringByKey("MyFileName"));
            this.WinClose();
            return;
		}
		else{
			
		}
		
       
		switch(this.getGetActionType())
		{
			case DeleteFile:
				SysFileManager sysfile = new SysFileManager( Integer.parseInt(this.get_request().getParameter("OID")) );
				sysfile.Delete();
				break;
			case PrintEnBill:
				String className=this.get_request().getParameter("MainEnsName");
				Entity en =ClassFactory.GetEns(className).getGetNewEntity();
				try
				{
					en.setPKVal(this.get_request().getParameter("PK"));
					en.Retrieve();
				}
				catch(Exception e)
				{
					en.setPKVal(this.get_request().getParameter(en.getPK()));
					en.Retrieve();
				}
				//this.GenerRptByPeng(en);
				break;
			default:
				throw new Exception("do error"+this.getGetActionType());
		}
		this.WinClose();
		
	}


	
}

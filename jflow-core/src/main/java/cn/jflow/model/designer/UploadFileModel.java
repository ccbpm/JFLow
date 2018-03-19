package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.FileUpload;

public class UploadFileModel extends BaseModel{

	StringBuffer Pub1=null;
	
	public UploadFileModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		Pub1=new StringBuffer();
	}
	
	public void init(){
		 this.Pub1.append(AddFieldSet("文件上传"));
         FileUpload fu = new FileUpload();
         fu.setId("s");
         // fu.Width = 300;
         this.Pub1.append(fu);
         this.Pub1.append(AddFieldSetEnd());
	}
	
	
}

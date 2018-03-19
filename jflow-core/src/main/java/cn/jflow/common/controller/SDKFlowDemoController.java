package cn.jflow.common.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.Demo.Student;
import BP.Tools.Json;
import BP.Tools.StringHelper;
import cn.jflow.common.model.TempObject;
import cn.jflow.controller.wf.workopt.BaseController;
@Controller
@RequestMapping("/SDKFlowDemo/BPFramework")
public class SDKFlowDemoController  extends BaseController{
	HttpServletResponse response;
	HttpServletRequest request;
	@RequestMapping(value = "/DataInputJQ/Student.do", method = RequestMethod.POST)
	protected void execute(TempObject object, HttpServletRequest request,HttpServletResponse response) {
	String doType = request.getParameter("DoType");
	String msg = "";
	PrintWriter out =null;
	response.setCharacterEncoding("UTF-8");
	response.setContentType("text/html");
	try
    {
		if("Student_Init".equals(doType))
		{
			  msg = this.Student_Init();//初始化实体demo.
		}else if("Student_Save".equals(doType))
		{
			 msg = this.Student_Save(); //保存实体demo.
		}else if("Student_Delete".equals(doType))//删除.
		{
			 msg = this.Student_Delete();
		}else if("StudentList_Init".equals(doType))//获取学生列表。
		{
			 msg = this.StudentList_Init();
		}else if("StudentList_Delete".equals(doType))//删除单个学生.
		{
			  msg = this.StudentList_Delete();
		}else{
			  msg = "err@没有判断的标记:" + this.getDoType();
		}
		out = response.getWriter();
		out.write(msg);
    }catch (Exception ex)
    {
        msg = "err@" + ex.getMessage();
    }
}
	/**
	 * 初始化学生信息.
	 * @return
	 */
    public String Student_Init()	
    {
        BP.Demo.Student en = new BP.Demo.Student();
       
        if (StringHelper.isNullOrEmpty(getNo()))
        {
            en.setNo(en.getGenerNewNo());
            en.setAge(23);
            en.setAddr("山东.济南.");

        }
        else
        {
            en.setNo(this.getNo());
            en.Retrieve();
        }
        return en.ToJson();
    }
    /**
     * 实体保存
     * @return
     */
    public String Student_Save()
    {
        BP.Demo.Student stu = new BP.Demo.Student();
        if (!StringHelper.isNullOrEmpty(getNo()))
        {
            stu.setNo(this.getNo());
            stu.Retrieve();
        }

        stu = (Student) BP.Sys.PubClass.CopyFromRequestByPost(stu, getRequest());
        stu.Save();  //执行保存.
        
        return "编号["+stu.getNo()+"]名称["+stu.getName()+"]保存成功...";
    }
    
	 /**
	  * 删除
	  * @return
	  */
    public String Student_Delete()
    {
        BP.Demo.Student stu = new BP.Demo.Student();
        if(StringHelper.isNullOrEmpty(getNo()))
        {
            stu.setNo(this.getNo());
            stu.Delete();
            return "删除成功...";
        }
        else
        {
            return "err@删除失败...";
        }

    }
    
   /**
    * 初始化参数
    * @return
    */
    public String StudentList_Init()
    {
        BP.Demo.Students ens = new BP.Demo.Students();
        ens.RetrieveAll();
        return Json.ToJson(ens.ToDataTableField());
    }
    
    public String StudentList_Delete()
    {
        try
        {
            BP.Demo.Student stu = new BP.Demo.Student();
            stu.setNo(this.getNo());
            stu.Delete();
            return "删除成功";

        }catch(Exception ex)
        {
            return "err@" + ex.getMessage();
        }
    }
    /**
     * 编号
     */
    public String getNo()
    {
            String str = getRequest().getParameter("No");
            if(StringHelper.isNullOrEmpty(str))
            {
            	 return null;
            }
            return str;
        }
    }


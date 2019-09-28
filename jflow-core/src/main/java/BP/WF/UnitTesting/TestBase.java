package BP.WF.UnitTesting;

import BP.DA.DataTable;

public abstract class TestBase {
	public EditState EditState = BP.WF.UnitTesting.EditState.Editing;
	/** 
	 执行步骤信息
	 
	*/
	public int TestStep = 0;
	public String Note = "";
	/** 
	 增加测试内容.
	 
	 @param note 测试内容的详细描述.
	*/
	public final void AddNote(String note)
	{
		TestStep++;
		if (Note.equals(""))
		{
			Note += "\t\n 进行:" + TestStep + "项测试";
			Note += "\t\n" + note;
		}
		else
		{
			Note += "\t\n测试通过.";
			Note += "\t\n 进行:" + TestStep + "项测试";
			Note += "\t\n" + note;
		}
	}
	public String sql = "";
	public DataTable dt = null;
	/** 
	 让子类重写
	 
	*/
	public void Do()
	{
	}

	/** 
	 标题
	 
	*/
	public String Title = "未命名的单元测试";
	public String DescIt = "描述";
	/** 
	 错误信息
	 
	*/
	public String ErrInfo = "";
	/** 
	 测试基类
	 
	*/
	public TestBase()
	{
	}

}

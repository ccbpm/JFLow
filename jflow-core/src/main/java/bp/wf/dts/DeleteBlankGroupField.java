package bp.wf.dts;

import bp.sys.*;
import bp.da.*;
import bp.en.*;


/** 
 删除空白的字段分组
*/
public class DeleteBlankGroupField extends Method
{
	/** 
	 删除空白的字段分组
	*/
	public DeleteBlankGroupField()throws Exception
	{
		this.Title = "删除空白的字段分组";
		this.Help = "";
		this.Icon = "<img src='/WF/Img/Btn/Delete.gif'  border=0 />";
		this.GroupName = "系统维护";
	}
	@Override
	public void Init()
	{

	}
	@Override
	public boolean getIsCanDo()
	{
		return true;
	}
	@Override
	public Object Do()throws Exception
	{
		GroupFields gfs = new GroupFields();
		gfs.RetrieveAll();

		int delNum = 0;
		for (GroupField item : gfs.ToJavaList())
		{
			int num = 0;
			num += DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM Sys_MapAttr WHERE GroupID='" + item.getOID() + "' and FK_MapData='" + item.getFrmID() + "'");
			if (num == 0)
			{
				delNum++;
				item.Delete();
			}
		}
		return "执行成功,删除数量:" + delNum;
	}
}
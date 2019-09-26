package BP.WF.DTS;

import BP.DA.*;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.*;
import java.time.*;

/** 
 同步待办时间戳 的摘要说明
*/
public class DTS_GenerWorkFlowTimeSpan extends Method
{
	/** 
	 同步待办时间戳
	*/
	public DTS_GenerWorkFlowTimeSpan()
	{
		this.Title = "同步待办时间戳,状态,流程注册表的时间段(本周，上周，2周以前，3其他。).";
		this.Help = "该方法每周一自动执行，如果不能自动执行就手动执行";
		this.GroupName = "流程自动执行定时任务";

	}
	/** 
	 设置执行变量
	 
	 @return 
	*/
	@Override
	public void Init()
	{
		//this.Warning = "您确定要执行吗？";
		//HisAttrs.AddTBString("P1", null, "原密码", true, false, 0, 10, 10);
		//HisAttrs.AddTBString("P2", null, "新密码", true, false, 0, 10, 10);
		//HisAttrs.AddTBString("P3", null, "确认", true, false, 0, 10, 10);
	}
	/** 
	 当前的操纵员是否可以执行这个方法
	*/
	@Override
	public boolean getIsCanDo()
	{
		return true;
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	*/
	@Override
	public Object Do()
	{

		//只能在周1执行.
		LocalDateTime dtNow = LocalDateTime.now();

		//设置为开始的日期为周1.
		LocalDateTime dtBegin = LocalDateTime.now();

		dtBegin = dtBegin.plusDays(-7);
		for (int i = 0; i < 8; i++)
		{
			if (dtBegin.getDayOfWeek() == DayOfWeek.MONDAY)
			{
				break;
			}
			dtBegin = dtBegin.plusDays(-1);
		}

		//结束日期为当前.
		LocalDateTime dtEnd = dtBegin.plusDays(7);

		//默认都设置为本周
		String sql = "UPDATE WF_GenerWorkFlow SET TSpan=" + TSpan.ThisWeek.getValue();
		BP.DA.DBAccess.RunSQL(sql);

		//设置为上周.
		sql = "UPDATE WF_GenerWorkFlow SET TSpan=" + TSpan.NextWeek.getValue() + " WHERE RDT >= '" + dtBegin.toString(DataType.getSysDataFormat()) + " 00:00' AND RDT <= '" + dtEnd.toString(DataType.getSysDataFormat()) + " 00:00'";
		BP.DA.DBAccess.RunSQL(sql);

		dtBegin = dtBegin.plusDays(-7);
		dtEnd = dtEnd.plusDays(-7);

		//把上周的，设置为两个周以前.
		sql = "UPDATE WF_GenerWorkFlow SET TSpan=" + TSpan.TowWeekAgo.getValue() + " WHERE RDT >= '" + dtBegin.toString(DataType.getSysDataFormat()) + " 00:00' AND RDT <= '" + dtEnd.toString(DataType.getSysDataFormat()) + " 00:00' ";
		BP.DA.DBAccess.RunSQL(sql);

		//把上周的，设置为更早.
		sql = "UPDATE WF_GenerWorkFlow SET TSpan=" + TSpan.More.getValue() + " WHERE RDT <= '" + dtBegin.toString(DataType.getSysDataFormat()) + " 00:00' ";
		BP.DA.DBAccess.RunSQL(sql);

		return "执行成功...";
	}
}
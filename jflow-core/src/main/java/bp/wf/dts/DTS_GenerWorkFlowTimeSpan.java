package bp.wf.dts;

import bp.da.*;
import bp.en.*;
import bp.tools.DateUtils;
import bp.wf.*;
import java.time.*;
import java.util.Date;

/**
 同步待办时间戳 的摘要说明
 */
public class DTS_GenerWorkFlowTimeSpan extends Method
{
	/**
	 同步待办时间戳
	 */
	public DTS_GenerWorkFlowTimeSpan()throws Exception
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
	public void Init()  {
		//this.Warning = "您确定要执行吗？";
		//HisAttrs.AddTBString("P1", null, "原密码", true, false, 0, 10, 10);
		//HisAttrs.AddTBString("P2", null, "新密码", true, false, 0, 10, 10);
		//HisAttrs.AddTBString("P3", null, "确认", true, false, 0, 10, 10);
	}
	/**
	 当前的操纵员是否可以执行这个方法
	 */
	@Override
	public boolean getIsCanDo()  {
		return true;
	}
	/**
	 执行

	 @return 返回执行结果
	 */
	@Override
	public Object Do() throws Exception
	{

		//只能在周1执行.
		Date dtNow = new Date();

		//设置为开始的日期为周1.
		Date dtBegin = new Date();

		dtBegin = DateUtils.addDay(dtBegin,-7);
		for (int i = 0; i < 8; i++)
		{
			if (Integer.parseInt(DateUtils.dayForWeek(dtBegin)) == DayOfWeek.MONDAY.getValue())
			{
				break;
			}
			dtBegin = DateUtils.addDay(dtBegin,-1);
		}

		//结束日期为当前.
		Date dtEnd = DateUtils.addDay(dtBegin,7);

		//默认都设置为本周
		String sql = "UPDATE WF_GenerWorkFlow SET TSpan=" + TSpan.ThisWeek.getValue();
		DBAccess.RunSQL(sql);

		//设置为上周.
		sql = "UPDATE WF_GenerWorkFlow SET TSpan=" + TSpan.NextWeek.getValue() + " WHERE RDT >= '" + DateUtils.format(dtBegin,DataType.getSysDataFormat()) + " 00:00' AND RDT <= '" + DateUtils.format(dtEnd,DataType.getSysDataFormat()) + " 00:00'";
		DBAccess.RunSQL(sql);

		dtBegin = DateUtils.addDay(dtBegin,-7);
		dtEnd = DateUtils.addDay(dtEnd,-7);

		//把上周的，设置为两个周以前.
		sql = "UPDATE WF_GenerWorkFlow SET TSpan=" + TSpan.TowWeekAgo.getValue() + " WHERE RDT >= '" + DateUtils.format(dtBegin,DataType.getSysDataFormat()) + " 00:00' AND RDT <= '" + DateUtils.format(dtEnd,DataType.getSysDataFormat()) + " 00:00' ";
		DBAccess.RunSQL(sql);

		//把上周的，设置为更早.
		sql = "UPDATE WF_GenerWorkFlow SET TSpan=" + TSpan.More.getValue() + " WHERE RDT <= '" + DateUtils.format(dtBegin,DataType.getSysDataFormat()) + " 00:00' ";
		DBAccess.RunSQL(sql);

		return "执行成功...";
	}
}
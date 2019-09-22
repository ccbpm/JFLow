package BP.DA;

import BP.Sys.*;
import java.time.*;

public class DTTemp
{
	/** 
	 使用C#把发表的时间改为几个月,几天前,几小时前,几分钟前,或几秒前
	  2008年03月15日 星期六 02:35
	 
	 @param dt
	 @return 
	*/
	public final String DateStringFromNow(String dt)
	{
		return DateStringFromNow(DataType.ParseSysDateTime2DateTime(dt));
	}
	/** 
	 使用C#把发表的时间改为几个月,几天前,几小时前,几分钟前,或几秒前
	  2008年03月15日 星期六 02:35
	 
	 @param dt
	 @return 
	*/
	public final String DateStringFromNow(LocalDateTime dt)
	{
		TimeSpan span = LocalDateTime.now() - dt;
		if (span.TotalDays > 60)
		{
			return dt.ToShortDateString();
		}
		else
		{
			if (span.TotalDays > 30)
			{
				return "1个月前";
			}
			else
			{
				if (span.TotalDays > 14)
				{
					return "2周前";
				}
				else
				{
					if (span.TotalDays > 7)
					{
						return "1周前";
					}
					else
					{
						if (span.TotalDays > 1)
						{
							return String.format("%1$s天前", (int)Math.floor(span.TotalDays));
						}
						else
						{
							if (span.TotalHours > 1)
							{
								return String.format("%1$s小时前", (int)Math.floor(span.TotalHours));
							}
							else
							{
								if (span.TotalMinutes > 1)
								{
									return String.format("%1$s分钟前", (int)Math.floor(span.TotalMinutes));
								}
								else
								{
									if (span.TotalSeconds >= 1)
									{
										return String.format("%1$s秒前", (int)Math.floor(span.TotalSeconds));
									}
									else
									{
										return "1秒前";
									}
								}
							}
						}
					}
				}
			}
		}
	}

	//C#中使用TimeSpan计算两个时间的差值
	//可以反加两个日期之间任何一个时间单位。
	private String DateDiff(LocalDateTime DateTime1, LocalDateTime DateTime2)
	{
		String dateDiff = null;
		TimeSpan ts1 = new TimeSpan(DateTime1.getTime());
		TimeSpan ts2 = new TimeSpan(DateTime2.getTime());
		TimeSpan ts = ts1.Subtract(ts2).Duration();
		dateDiff = String.valueOf(ts.Days) + "天" + String.valueOf(ts.Hours) + "小时" + String.valueOf(ts.Minutes) + "分钟" + String.valueOf(ts.Seconds) + "秒";
		return dateDiff;
	}

	//说明：
	/*1.DateTime值类型代表了一个从公元0001年1月1日0点0分0秒到公元9999年12月31日23点59分59秒之间的具体日期时刻。因此，你可以用DateTime值类型来描述任何在想象范围之内的时间。一个DateTime值代表了一个具体的时刻
	2.TimeSpan值包含了许多属性与方法，用于访问或处理一个TimeSpan值
	下面的列表涵盖了其中的一部分：
	Add：与另一个TimeSpan值相加。 
	Days:返回用天数计算的TimeSpan值。 
	Duration:获取TimeSpan的绝对值。 
	Hours:返回用小时计算的TimeSpan值 
	Milliseconds:返回用毫秒计算的TimeSpan值。 
	Minutes:返回用分钟计算的TimeSpan值。 
	Negate:返回当前实例的相反数。 
	Seconds:返回用秒计算的TimeSpan值。 
	Subtract:从中减去另一个TimeSpan值。 
	Ticks:返回TimeSpan值的tick数。 
	TotalDays:返回TimeSpan值表示的天数。 
	TotalHours:返回TimeSpan值表示的小时数。 
	TotalMilliseconds:返回TimeSpan值表示的毫秒数。 
	TotalMinutes:返回TimeSpan值表示的分钟数。 
	TotalSeconds:返回TimeSpan值表示的秒数。
	*/
	/** 
	 日期比较
	 
	 @param today 当前日期
	 @param writeDate 输入日期
	 @param n 比较天数
	 @return 大于天数返回true，小于返回false
	*/
	private boolean CompareDate(String today, String writeDate, int n)
	{
		LocalDateTime Today = LocalDateTime.parse(today);
		LocalDateTime WriteDate = LocalDateTime.parse(writeDate);
		WriteDate = WriteDate.plusDays(n);
		if (Today.compareTo(WriteDate) >= 0)
		{
			return false;
		}

		return true;
	}
}
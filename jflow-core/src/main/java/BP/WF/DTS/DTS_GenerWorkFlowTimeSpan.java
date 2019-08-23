package BP.WF.DTS;
import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.LogType;
import BP.En.Method;
import BP.Port.Emp;
import BP.Tools.DateUtils;
import BP.WF.*;
import BP.WF.Template.NodeAttr;
import com.hp.hpl.sparta.xpath.ThisNodeTest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/// <summary>
    /// 同步待办时间戳 的摘要说明
    /// </summary>



    public class DTS_GenerWorkFlowTimeSpan extends Method
    {


        /// <summary>
        /// 同步待办时间戳
        /// </summary>
        public DTS_GenerWorkFlowTimeSpan()
        {
            this.Title = "同步待办时间戳,状态,流程注册表的时间段(本周，上周，2周以前，3其他。).";
            this.Help = "该方法每周一自动执行，如果不能自动执行就手动执行";
        }
        /// <summary>
        /// 设置执行变量
        /// </summary>
        /// <returns></returns>
        public void Init()
        {
            //this.Warning = "您确定要执行吗？";
            //HisAttrs.AddTBString("P1", null, "原密码", true, false, 0, 10, 10);
            //HisAttrs.AddTBString("P2", null, "新密码", true, false, 0, 10, 10);
            //HisAttrs.AddTBString("P3", null, "确认", true, false, 0, 10, 10);
        }
        /// <summary>
        /// 当前的操纵员是否可以执行这个方法
        /// </summary>
        public  boolean getIsCanDo()
        {
            return true;
        }

        /// <summary>
        /// 执行
        /// </summary>
        /// <returns>返回执行结果</returns>
        public  String Do()
        {
            //只能在周1执行.
            Date dtNow = new Date();//获取当前时间
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");//可以方便地修改日期格式
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dtNow);

            //设置为开始的日期为周1.
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            Date dtBegin = calendar.getTime();
            for (int i = 0; i < 8; i++)
            {
                if (calendar.get(Calendar.DAY_OF_MONTH) == 1)
                    break;
                calendar.add(Calendar.DAY_OF_MONTH, -1);
            }
            dtBegin = calendar.getTime();
            //结束日期为当前.
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            Date dtEnd =calendar.getTime();

            //默认都设置为本周
            String sql = "UPDATE WF_GenerWorkFlow SET TSpan=" + TSpan.ThisWeek;
            BP.DA.DBAccess.RunSQL(sql);

            //设置为上周.
            sql = "UPDATE WF_GenerWorkFlow SET TSpan=" + TSpan.NextWeek + " WHERE RDT >= '" + dateFormat.format(dtBegin) + " 00:00' AND RDT <= '" + dateFormat.format(dtEnd) + " 00:00'";
            BP.DA.DBAccess.RunSQL(sql);

            calendar.setTime(dtBegin);
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            dtBegin = calendar.getTime();

            calendar.setTime(dtEnd);
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            dtEnd = calendar.getTime();
            //把上周的，设置为两个周以前.
            sql = "UPDATE WF_GenerWorkFlow SET TSpan=" + TSpan.TowWeekAgo + " WHERE RDT >= '" + dateFormat.format(dtBegin) + " 00:00' AND RDT <= '" + dateFormat.format(dtEnd) + " 00:00' ";
            BP.DA.DBAccess.RunSQL(sql);

            //把上周的，设置为更早.
            sql = "UPDATE WF_GenerWorkFlow SET TSpan=" + TSpan.More + " WHERE RDT <= '" + dateFormat.format(dtBegin) + " 00:00' ";
            BP.DA.DBAccess.RunSQL(sql);

            return "执行成功...";
        }
    }


package bp.dts;

import java.time.LocalDateTime;

import bp.da.DBAccess;
import bp.da.DataRow;
import bp.da.DataTable;
import bp.da.DataType;
import bp.difference.SystemConfig;
import bp.en.Method;
import bp.wf.Dev2Interface;
import bp.wf.Glo;
import bp.wf.data.AutoRpt;
import bp.wf.data.AutoRptDtl;
import bp.wf.data.AutoRptDtlAttr;
import bp.wf.data.AutoRptDtls;
import bp.wf.data.AutoRpts;

public class Auto_Rpt_Dtl_DTS extends Method {
	/** 
	 不带有参数的方法
*/
//C# TO JAVA CONVERTER WARNING: The following constructor is declared outside of its associated class:
//ORIGINAL LINE: public Auto_Rpt_Dtl_DTS()
	public Auto_Rpt_Dtl_DTS()
	{
		this.Title = "自动报表的发送";
		this.Help = "自动报表配置到WF_AutoRpt, 与WF_AutoRptDtl中，读取之后进行发送消息或者数据.";
		this.GroupName = "流程自动执行定时任务";

	}
	/** 
	 设置执行变量
	 
	 @return 
	*/
	@Override
	public void Init()
	{
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
		return GroupName;}

}

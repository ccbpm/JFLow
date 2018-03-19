package cn.jflow.model.ch;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.Web.WebUser;
import cn.jflow.common.model.BaseModel;

public class FlowCHModel extends BaseModel{
	
	private Calendar cal;

	 public FlowCHModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	 
	 public String getDaysTitle() {
			cal = Calendar.getInstance();

			cal.add(Calendar.MONTH, -1);
			int daysCount = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.DAY_OF_MONTH,1);//

			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
			String dtFirst = sdf.format(cal.getTime());

			StringBuilder sb = new StringBuilder();
			sb.append("<categories >");
			sb.append("<category label='" + dtFirst + "' />");
					for (int i = 2; i <= daysCount; i++) {
					cal.set(Calendar.DAY_OF_MONTH, i);
					String dtNextTime = sdf.format(cal.getTime());
					sb.append("<category label='" + dtNextTime + "' />");
			}
			sb.append("</categories>");
			return sb.toString();
		}
		/** 
		 加载我的工作图形
		 
		 @return 
		*/
		public String empChart()
		{
		    Calendar cal=Calendar.getInstance();
		    
			cal.add(Calendar.MONTH,-1);
			int daysCount=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.DAY_OF_MONTH,1);//
			
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
			String dtFirst=sdf.format(cal.getTime());        
			
			
			StringBuilder sb = new StringBuilder();

		    sb.append(getDaysTitle());


			sb.append("<dataset seriesName='按期完成数' color='F6BD0F' anchorBorderColor='F6BD0F' >");
			
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			int maxValue = 0;
			String sql = "";
			for (int i = 1; i < daysCount; i++)
			{
				if (i == 1)
				{
					sql = "select * from wf_ch where fk_emp ='" + WebUser.getNo() + "' and DTFrom like '%" + dtFirst + "%' and chsta='1'";
					sb.append("<set value='" + DBAccess.RunSQLReturnCOUNT(sql) + "' />");
				}

				cal.set(Calendar.DAY_OF_MONTH,i+1);
				String dtNextTime = sdf.format(cal.getTime());  
				sql = "select * from wf_ch where fk_emp ='" + WebUser.getNo()+ "' and DTFrom like '%" + dtNextTime + "%' and chsta='1'";

				int rowsCount = DBAccess.RunSQLReturnCOUNT(sql);
				if (rowsCount > maxValue)
				{
					maxValue = rowsCount;
				}
				sb.append("<set value='" + rowsCount + "' />");
			}
			sb.append("</dataset>");

			sb.append("<dataset seriesName='逾期完成数' color='FF0000' anchorBorderColor='FF0000' >");
			for (int i = 1; i < daysCount; i++)
			{
				if (i == 1)
				{
					sql = "select * from wf_ch where fk_emp ='" + WebUser.getNo() + "' and DTFrom like '%" + dtFirst + "%' and chsta='2'";
					sb.append("<set value='" + DBAccess.RunSQLReturnCOUNT(sql) + "' />");
				}

				cal.set(Calendar.DAY_OF_MONTH,i+1);
				String dtNextTime = sdf.format(cal.getTime()); 
				sql = "select * from wf_ch where fk_emp ='" + WebUser.getNo() + "' and DTFrom like '%" + dtNextTime + "%' and chsta='2'";

				int rowsCount = DBAccess.RunSQLReturnCOUNT(sql);
				if (rowsCount > maxValue)
				{
					maxValue = rowsCount;
				}
				sb.append("<set value='" + rowsCount + "' />");
			}
			sb.append("</dataset>");

			sb.append(" </chart>\"]}");

			if (maxValue == 0)
			{
				maxValue = 100;
			}
			sb.insert(0, "{set_XML:[\"<chart numdivlines='9' lineThickness='2' yAxisMaxValue='" + maxValue + "' showValues='0' numVDivLines='22' formatNumberScale='1'" + "labelDisplay='ROTATE' slantLabels='1' anchorRadius='2' anchorBgAlpha='50' showAlternateVGridColor='1' anchorAlpha='100' animation='1' " + "limitsDecimalPrecision='0' divLineDecimalPrecision='1'>");
		
			return sb.toString();
		}
		/** 
		 加载我部门图形
		 
		 @return 
		*/
		public String deptChart()
		{
			 Calendar cal=Calendar.getInstance();
			    
		     cal.add(Calendar.MONTH,-1);
			 int daysCount=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			 
			 cal.set(Calendar.DAY_OF_MONTH,1);
				
			 SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
			 String dtFirst=sdf.format(cal.getTime());  
			

			StringBuilder sb = new StringBuilder();
			sb.append(getDaysTitle());

			
			sb.append("<dataset seriesName='按期完成数' color='F6BD0F' anchorBorderColor='F6BD0F' >");
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			int maxValue = 0;
			String sql = "";
			for (int i = 1; i < daysCount; i++)
			{
				if (i == 1)
				{
					sql = "select * from wf_ch where fk_emp ='" + WebUser.getNo() + "' and DTFrom like '%" + dtFirst + "%' and chsta='1' and fk_dept='" + WebUser.getFK_Dept() + "'";
					sb.append("<set value='" + DBAccess.RunSQLReturnCOUNT(sql) + "' />");
				}
				cal.set(Calendar.DAY_OF_MONTH,i+1);
				String dtNextTime = sdf.format(cal.getTime());  
				
				sql = "select * from wf_ch where fk_emp ='" + WebUser.getNo() + "' and DTFrom like '%" + dtNextTime + "%' and chsta='1' and fk_dept='" + WebUser.getFK_Dept() + "'";
				int rowsCount = DBAccess.RunSQLReturnCOUNT(sql);
				if (rowsCount > maxValue)
				{
					maxValue = rowsCount;
				}
				sb.append("<set value='" + rowsCount + "' />");
			}
			sb.append("</dataset>");

			sb.append("<dataset seriesName='逾期完成数' color='FF0000' anchorBorderColor='FF0000' >");
			for (int i = 1; i < daysCount; i++)
			{
				if (i == 1)
				{
					sql = "select * from wf_ch where fk_emp ='" + WebUser.getNo() + "' and DTFrom like '%" + dtFirst + "%' and chsta='2'";
					sb.append("<set value='" + DBAccess.RunSQLReturnCOUNT(sql) + "' />");
				}

				cal.set(Calendar.DAY_OF_MONTH,i+1);
				String dtNextTime = sdf.format(cal.getTime());  
				
				sql = "select * from wf_ch where fk_emp ='" + WebUser.getNo() + "' and DTFrom like '%" + dtNextTime + "%' and chsta='2'";

				int rowsCount = DBAccess.RunSQLReturnCOUNT(sql);
				if (rowsCount > maxValue)
				{
					maxValue = rowsCount;
				}
				sb.append("<set value='" + rowsCount + "' />");
			}
			sb.append("</dataset>");

			sb.append(" </chart>\"]}");

			if (maxValue == 0)
			{
				maxValue = 100;
			}
			sb.insert(0, "{set_XML:[\"<chart numdivlines='9' lineThickness='2' yAxisMaxValue='" + maxValue + "' showValues='0' numVDivLines='22' formatNumberScale='1'" + "labelDisplay='ROTATE' slantLabels='1' anchorRadius='2' anchorBgAlpha='50' showAlternateVGridColor='1' anchorAlpha='100' animation='1' " + "limitsDecimalPrecision='0' divLineDecimalPrecision='1'>");
			return sb.toString();
		}
		/** 
		 加载全单位图形
		 
		 @return 
		*/
		public String allDeptChart()
		{
			 Calendar cal=Calendar.getInstance();
			    
				cal.add(Calendar.MONTH,-1);
				int daysCount=cal.getActualMaximum(Calendar.DAY_OF_MONTH);

				cal.set(Calendar.DAY_OF_MONTH,1);
				
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
				String dtFirst=sdf.format(cal.getTime());  

			StringBuilder sb = new StringBuilder();
			sb.append(getDaysTitle());


			sb.append("<dataset seriesName='按期完成数' color='F6BD0F' anchorBorderColor='F6BD0F' >");
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			int maxValue = 0;
			String sql = "";
			for (int i = 1; i < daysCount; i++)
			{
				if (i == 1)
				{
					sql = "select * from wf_ch where DTFrom like '%" + dtFirst + "%' and chsta='1'";
					sb.append("<set value='" + DBAccess.RunSQLReturnCOUNT(sql) + "' />");
				}

				cal.set(Calendar.DAY_OF_MONTH,i+1);
				String dtNextTime = sdf.format(cal.getTime());
				
				sql = "select * from wf_ch where  DTFrom like '%" + dtNextTime+ "%' and chsta='1'";

				int rowsCount = DBAccess.RunSQLReturnCOUNT(sql);
				if (rowsCount > maxValue)
				{
					maxValue = rowsCount;
				}
				sb.append("<set value='" + rowsCount + "' />");
			}
			sb.append("</dataset>");

			sb.append("<dataset seriesName='逾期完成数' color='FF0000' anchorBorderColor='FF0000' >");
			for (int i = 1; i < daysCount; i++)
			{
				if (i == 1)
				{
					sql = "select * from wf_ch where fk_emp ='" + WebUser.getNo() + "' and DTFrom like '%" + dtFirst + "%' and chsta='2'";
					sb.append("<set value='" + DBAccess.RunSQLReturnCOUNT(sql) + "' />");
				}

				cal.set(Calendar.DAY_OF_MONTH,i+1);
				String dtNextTime = sdf.format(cal.getTime());
				sql = "select * from wf_ch where fk_emp ='" + WebUser.getNo() + "' and DTFrom like '%" + dtNextTime + "%' and chsta='2'";

				int rowsCount = DBAccess.RunSQLReturnCOUNT(sql);
				if (rowsCount > maxValue)
				{
					maxValue = rowsCount;
				}
				sb.append("<set value='" + rowsCount + "' />");
			}
			sb.append("</dataset>");

			sb.append(" </chart>\"]}");

			if (maxValue == 0)
			{
				maxValue = 100;
			}
			sb.insert(0, "{set_XML:[\"<chart numdivlines='9' lineThickness='2' yAxisMaxValue='" + maxValue + "' showValues='0' numVDivLines='22' formatNumberScale='1'" + "labelDisplay='ROTATE' slantLabels='1' anchorRadius='2' anchorBgAlpha='50' showAlternateVGridColor='1' anchorAlpha='100' animation='1' " + "limitsDecimalPrecision='0' divLineDecimalPrecision='1'>");
			return sb.toString();
		}
}


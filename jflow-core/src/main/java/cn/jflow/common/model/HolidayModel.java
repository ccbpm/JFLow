package cn.jflow.common.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.system.ui.core.CheckBox;
import BP.WF.Template.DayOfWeek;

/**
 *
 */
public class HolidayModel extends BaseModel {

	public HolidayModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public StringBuffer Pub1 = null;

	public void loadPage() {
		Pub1 = new StringBuffer();
		BP.Sys.GloVar var = new BP.Sys.GloVar();
		var.setNo("Holiday");
		var.RetrieveFromDBSources();

		Calendar dt = Calendar.getInstance();
		int yearN = dt.get(Calendar.YEAR);// 得到年
		while (dt.get(Calendar.DAY_OF_WEEK) != (DayOfWeek.Sunday.getValue()+1)){
			dt.add(Calendar.DATE, -1);
	    }
		// System.DateTime dtEnd = System.DateTime.Parse(dt.Year + "-12-31");
		int idx = 0;
		while (true) {
			int year = dt.get(Calendar.YEAR);// 得到年
		    int month = dt.get(Calendar.MONTH) + 1;// 得到月（+1获得习惯的月）
		    int day = dt.get(Calendar.DATE);// 得到日
			if (dt.get(Calendar.YEAR) != yearN) {
				break;
			}
			CheckBox cb = new CheckBox();
			cb.setText(month+"月"+day+"日");
			cb.setId(month+"-"+day);
			cb.checked = var.getVal().contains(month+"-"+day);
			if (dt.get(Calendar.DAY_OF_WEEK)==(DayOfWeek.Sunday.getValue()+1)) {
				idx++;
				this.Pub1.append(AddTR());
				this.Pub1.append(AddTDIdx(idx));
				this.Pub1.append(AddTD(month + "月"));
				this.Pub1.append(AddTD(cb));
			}else if(dt.get(Calendar.DAY_OF_WEEK)==(DayOfWeek.Saturday.getValue()+1)){
					this.Pub1.append(AddTD(cb));
					this.Pub1.append(AddTREnd());
			}else{
				this.Pub1.append(AddTD("class=TRSum", cb));
			}
			dt.add(Calendar.DATE, 1);
		}
		//System.out.println("-----------------------------------------------:"+Pub1.toString());
	}
}

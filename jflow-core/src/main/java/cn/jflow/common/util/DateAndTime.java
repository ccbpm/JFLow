package cn.jflow.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAndTime {	
	public static String getDateNow(){
		Date day=new Date();    
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		return df.format(day);
	}
}

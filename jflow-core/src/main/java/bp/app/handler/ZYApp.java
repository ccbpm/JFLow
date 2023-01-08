package bp.app.handler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;


import bp.da.DataTable;
import bp.da.DataType;
import bp.difference.ContextHolderUtils;
import bp.difference.SystemConfig;
import bp.difference.handler.WebContralBase;
import bp.port.Emp;
import bp.tools.HttpClientUtil;
import bp.web.WebUser;
import bp.wf.GenerWorkFlow;
import bp.wf.WFSta;
import net.sf.json.JSONArray;
import org.apache.axis.encoding.Base64;
import org.apache.http.protocol.HttpContext;

import net.sf.json.JSONObject;

public class ZYApp  extends WebContralBase {
	/** 
	 初始化数据
	 
	 @param mycontext
	*/
	public ZYApp(HttpContext mycontext)
	{
		this.context = mycontext;
	}
	public ZYApp()
	{
	}
	/**
	 * 根据约定自动跳转
	 * @return
	 * @throws Exception
	 */
	public String Auto_Comm() throws Exception{
		String FK_Flow=this.GetRequestVal("OpenFlow");
		String MapData=this.GetRequestVal("OpenMapData");
		String billNo=this.GetRequestVal("ajbh");
		String FID=this.GetRequestVal("FID");

		if(billNo.equals("@BillNo")){
			if(!DataType.IsNullOrEmpty(FID)) {
				GenerWorkFlow generWorkFlow = new GenerWorkFlow(Long.parseLong(FID));
				billNo = generWorkFlow.getBillNo();
			}
			else{
				GenerWorkFlow generWorkFlow = new GenerWorkFlow(this.getWorkID());
				billNo = generWorkFlow.getBillNo();
			}
		}

		if(!DataType.IsNullOrEmpty(MapData)){
			DataTable dt=bp.da.DBAccess.RunSQLReturnTable("select * from "+MapData+" where BillNo='"+billNo+"'");
			if(dt.Rows.size()>0){
				if(!DataType.IsNullOrEmpty(FK_Flow)){
					GenerWorkFlow gwf=new GenerWorkFlow(Long.parseLong(dt.Rows.get(0).get("OID").toString()));
					if(gwf.getWFSta()== WFSta.Complete){
						return "/WF/MyView.htm?FK_Flow="+FK_Flow+"&WorkID="+dt.Rows.get(0).get("OID")+"&BillNo="+billNo;
					}
					return "/WF/MyFlow.htm?FK_Flow="+FK_Flow+"&WorkID="+dt.Rows.get(0).get("OID")+"&BillNo="+billNo;
				}
				else{
					return "err@缺少参数";
				}
			}
			else{
				if(!DataType.IsNullOrEmpty(FK_Flow)){
					return "/WF/MyFlow.htm?FK_Flow="+FK_Flow+"&BillNo="+billNo;
				}
				else{
					return "err@缺少参数";
				}
			}
		}
		else{
			return "err@缺少参数";
		}
	}
}

package WebService;

import javax.jws.WebMethod;
import javax.jws.WebService;
import BP.DA.DataSet;
@WebService
public interface CCFormAPII {
	/** 
	 获得单据模版信息
	 
	 @param userNo 用户编号
	 @param sid SID
	 @param workID 工作ID
	 @param billTemplateNo 单据模版编号
	 @param ds 返回的数据源
	 @param bytes 返回的字节
	 * @throws Exception 
	*/
	@WebMethod
	public  void GenerBillTemplate(String userNo, String sid, long workID, String billTemplateNo, DataSet ds, byte[] bytes) throws Exception;
	
	/** 
	 获得Word文件 - 未开发完成.
	 @param userNo 用户编号
	 @param sid SID
	 @param frmID 表单ID
	 @param oid 表单主键
	 @return 
	 * @throws Exception 
	*/
	@WebMethod
	public  void WordFileGener(String userNo, String sid, long workID, byte[] bytes) throws Exception;
	

}

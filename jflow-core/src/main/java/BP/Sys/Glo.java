package BP.Sys;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;

import cn.jflow.common.util.ContextHolderUtils;
import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.En.Attr;
import BP.Tools.AesEncodeUtil;
import BP.Tools.Cryptos;
import BP.Tools.En3Des;
import BP.WF.StartWorkAttr;
import BP.WF.Work;
import BP.WF.WorkAttr;

/**
 * 公用的静态方法.
 */
public class Glo
{

	private static Hashtable Htable_BuessUnit = null;

	// / <summary>
	// / 获得节点事件实体
	// / </summary>
	// / <param name="enName">实例名称</param>
	// / <returns>获得节点事件实体,如果没有就返回为空.</returns>
	public static BuessUnitBase GetBuessUnitEntityByEnName(String enName) {
		if (Htable_BuessUnit == null || Htable_BuessUnit.size() == 0) {
			Htable_BuessUnit = new Hashtable();
			ArrayList<BuessUnitBase> al = BP.En.ClassFactory.GetObjects("BP.Sys.BuessUnitBase");
			for (BuessUnitBase en : al) {
				Htable_BuessUnit.put(en.toString(), en);
			}
		}
		BuessUnitBase myen = (BuessUnitBase) Htable_BuessUnit.get(enName);
		if (myen == null) {
			// throw new Exception("@根据类名称获取业务单元实例出现错误:" + enName + ",没有找到该类的实体.");
			BP.DA.Log.DefaultLogWriteLineError("@根据类名称获取业务单元实例出现错误:" + enName + ",没有找到该类的实体.");
			return null;
		}
		return myen;
	}

	  ///#region 与 表单 事件实体相关.
			private static java.util.Hashtable Htable_FormFEE = null;
	 /** 
	 获得节点事件实体
	 
	 @param enName 实例名称
	 @return 获得节点事件实体,如果没有就返回为空.
*/
	public static FormEventBase GetFormEventBaseByEnName(String enName)
	{
		if (Htable_FormFEE == null)
		{
			Htable_FormFEE = new java.util.Hashtable();

			java.util.ArrayList al = BP.En.ClassFactory.GetObjects("BP.Sys.FormEventBase");
			Htable_FormFEE.clear();

			for (Object en : al)
			{
				FormEventBase myen=(FormEventBase)en;	
				Htable_FormFEE.put(myen.getFormMark(), en);
			}
		}

		for (Object key : Htable_FormFEE.keySet())
		{
			FormEventBase fee = (FormEventBase)((Htable_FormFEE.get(key) instanceof FormEventBase) ? Htable_FormFEE.get(key) : null);
			if (fee.getFormMark().indexOf(enName + ",") >= 0 || enName.equals(fee.getFormMark()))
			{
				return fee;
			}
		}
		return null;
	
	}
	private static java.util.Hashtable Htable_FormFEEDtl = null;
	/** 
	 获得节点事件实体
	 
	 @param enName 实例名称
	 @return 获得节点事件实体,如果没有就返回为空.
	*/
	public static FormEventBaseDtl GetFormDtlEventBaseByEnName(String dtlEnName)
	{
		if (Htable_FormFEEDtl == null || Htable_FormFEEDtl.isEmpty())
		{
			Htable_FormFEEDtl = new java.util.Hashtable();
			java.util.ArrayList al = BP.En.ClassFactory.GetObjects("BP.Sys.FormEventBaseDtl");
			Htable_FormFEEDtl.clear();
			for (Object en : al)
			{
				FormEventBaseDtl myen=(FormEventBaseDtl)en;	
				Htable_FormFEEDtl.put(myen.getFormDtlMark(), en);
			}
		}		
		

		for (Object key : Htable_FormFEEDtl.keySet())
		{
			FormEventBaseDtl fee = (FormEventBaseDtl)((Htable_FormFEEDtl.get(key) instanceof FormEventBaseDtl) ? Htable_FormFEEDtl.get(key) : null);
			if (fee.getFormDtlMark().indexOf(dtlEnName + ",") >= 0 || dtlEnName.equals(fee.getFormDtlMark()))
			{
				return fee;
			}
		}
		return null;
	}

	
	// 写入系统日志(写入的文件:\DataUser\Log\*.*)
	/**
	 * 写入一条消息
	 * 
	 * @param msg
	 *            消息
	 */
	public static void WriteLineInfo(String msg)
	{
		BP.DA.Log.DefaultLogWriteLineInfo(msg);
	}
	
	/**
	 * 写入一条警告
	 * 
	 * @param msg
	 *            消息
	 */
	public static void WriteLineWarning(String msg)
	{
		BP.DA.Log.DefaultLogWriteLineWarning(msg);
	}
	
	/**
	 * 写入一条错误
	 * 
	 * @param msg
	 *            消息
	 */
	public static void WriteLineError(String msg)
	{
		BP.DA.Log.DefaultLogWriteLineError(msg);
	}
	
	// 写入系统日志
	
	// 写入用户日志(写入的用户表:Sys_UserLog).
	/**
	 * 写入用户日志
	 * 
	 * @param logType
	 *            类型
	 * @param empNo
	 *            操作员编号
	 * @param msg
	 *            信息
	 * @param ip
	 *            IP
	 * @throws Exception 
	 */
	public static void WriteUserLog(String logType, String empNo, String msg,
			String ip) throws Exception
	{
		UserLog ul = new UserLog();
		ul.setMyPK(DBAccess.GenerGUID());
		ul.setFK_Emp(empNo);
		ul.setLogFlag(logType);
		ul.setDocs(msg);
		ul.setIP(ip);
		ul.setRDT(DataType.getCurrentDataTime());
		ul.Insert();
	}
	
	/**
	 * 写入用户日志
	 * 
	 * @param logType
	 *            日志类型
	 * @param empNo
	 *            操作员编号
	 * @param msg
	 *            消息
	 * @throws Exception 
	 */
	public static void WriteUserLog(String logType, String empNo, String msg) throws Exception
	{
		UserLog ul = new UserLog();
		ul.setMyPK(DBAccess.GenerGUID());
		ul.setFK_Emp(empNo);
		ul.setLogFlag(logType);
		ul.setDocs(msg);
		ul.setRDT(DataType.getCurrentDataTime());
		try
		{
			if (BP.Sys.SystemConfig.getIsBSsystem())
			{
				ul.setIP(BP.Sys.Glo.getRequest().getRemoteHost());
			}
		} catch (java.lang.Exception e)
		{
		}
		ul.Insert();
	}
	
	// 写入用户日志.
	
	public static ArrayList<String> getQueryStringKeys()
	{
		// 处理传递过来的参数。
		ArrayList<String> requestKeys = new ArrayList<String>();
		if(getRequest()!=null){
			Enumeration enu = getRequest().getParameterNames();
			while (enu.hasMoreElements())
			{
				// 判断是否有内容，hasNext()
				String key = (String) enu.nextElement();
				requestKeys.add(key);
			}
		}
		
		return requestKeys;
	}
	
	/**
	 * 获得对象.
	 */
	public static HttpServletRequest getRequest()
	{
		return ContextHolderUtils.getRequest();
	}
	
	
	/** 
	 产生消息,senderEmpNo是为了保证写入消息的唯一性，receiveid才是真正的接收者.
	 如果插入失败.
	 
	 @param fromEmpNo 发送人
	 @param now 发送时间
	 @param msg 消息内容
	 @param sendToEmpNo 接受人
*/
	public static void SendMessageToCCIM(String fromEmpNo, String sendToEmpNo, String msg, String now)
	{
		// 周朋@于庆海 

		//暂停对ccim消息提醒的支持.
		if(1==1)
		return;

		if (fromEmpNo == null)
		{
			fromEmpNo = "";
		}

		if (sendToEmpNo == null || sendToEmpNo.equals(""))
		{
			return;
		}

		// throw new Exception("@接受人不能为空");

		String dbStr = SystemConfig.getAppCenterDBVarStr();
		//保存系统通知消息
		StringBuilder strHql1 = new StringBuilder();
		//加密处理
		msg = BP.Tools.SecurityDES.encrypt(msg);

		Paras ps = new Paras();
		String sql = "INSERT INTO CCIM_RecordMsg (OID,SendID,MsgDateTime,MsgContent,ImageInfo,FontName,FontSize,FontBold,FontColor,InfoClass,GroupID,SendUserID) VALUES (";
		sql += dbStr + "OID,";
		sql += "'SYSTEM',";
		sql += dbStr + "MsgDateTime,";
		sql += dbStr + "MsgContent,";
		sql += dbStr + "ImageInfo,";
		sql += dbStr + "FontName,";
		sql += dbStr + "FontSize,";
		sql += dbStr + "FontBold,";
		sql += dbStr + "FontColor,";
		sql += dbStr + "InfoClass,";
		sql += dbStr + "GroupID,";
		sql += dbStr + "SendUserID)";
		ps.SQL = sql;

		long messgeID = BP.DA.DBAccess.GenerOID("RecordMsgUser");

		ps.Add("OID", messgeID);
		ps.Add("MsgDateTime", now);
		ps.Add("MsgContent", msg);
		ps.Add("ImageInfo", "");
		ps.Add("FontName", "宋体");
		ps.Add("FontSize", 10);
		ps.Add("FontBold", 0);
		ps.Add("FontColor", -16777216);
		ps.Add("InfoClass", 15);
		ps.Add("GroupID", -1);
		ps.Add("SendUserID", fromEmpNo);
		BP.DA.DBAccess.RunSQL(ps);

		//保存消息发送对象,这个是消息的接收人表.
		ps = new Paras();
		ps.SQL = "INSERT INTO CCIM_RecordMsgUser (OID,MsgId,ReceiveID) VALUES ( ";
		ps.SQL += dbStr + "OID,";
		ps.SQL += dbStr + "MsgId,";
		ps.SQL += dbStr + "ReceiveID)";

		ps.Add("OID", messgeID);
		ps.Add("MsgId", messgeID);
		ps.Add("ReceiveID", sendToEmpNo);
		BP.DA.DBAccess.RunSQL(ps);
	}
//}
	
	
	
	/**
	 * 加密MD5
	 * 
	 * @param s
	 * @return
	 */
	public static String GenerMD5(Work wk)
	{
		String s = null;
		for (Attr attr : wk.getEnMap().getAttrs())
		{
			if (attr.getKey() == WorkAttr.MD5)
			{
				
			} else if (attr.getKey() == BP.WF.WorkAttr.RDT)
			{
				
			} else if (attr.getKey() == WorkAttr.CDT)
			{
				
			} else if (attr.getKey() == WorkAttr.Rec)
			{
				
			} else if (attr.getKey() == StartWorkAttr.Title)
			{
				
			} else if (attr.getKey() == StartWorkAttr.Emps)
			{
				
			} else if (attr.getKey() == StartWorkAttr.FK_Dept)
			{
				
			} else if (attr.getKey() == StartWorkAttr.PRI)
			{
				
			} else if (attr.getKey() == StartWorkAttr.FID)
			{
				continue;
			}
			
			String obj = (String) ((attr.getDefaultVal() instanceof String) ? attr
					.getDefaultVal() : null);
			
			if (obj != null && obj.contains("@"))
			{
				continue;
			}
			
			s += wk.GetValStrByKey(attr.getKey());
		}
		s += "ccflow";
		
		return DigestUtils.md5Hex(s).toLowerCase();
	}
	
	/**
	 * 文件上传加密
	 * @param fileFullPath上传的文件
	 * @param toFileFullPath 加密的文件
	 * @throws Exception 
	 */
	 ///#region 加密解密文件.
     public static void File_JiaMi(String fileFullPath,String toFileFullPath) throws Exception{
         //南京宝旺达.
         if (SystemConfig.getCustomerNo().equals("BWDA")) {
        	 AesEncodeUtil.encryptFile(fileFullPath,toFileFullPath);
         }
     }
     
     /**
      * 文件下载解密
      * @param fileFullPath 下载的文件
      * @param toFileFullPath解密的文件
      */
     public static void File_JieMi(String fileFullPath,String toFileFullPath){
         //南京宝旺达.
         if (SystemConfig.getCustomerNo().equals( "BWDA")){
        	 AesEncodeUtil.decryptFile(fileFullPath,toFileFullPath);
         }
     }
     /// <summary>
     /// 字符串的解密
     /// </summary>
     /// <param name="str">加密的字符串</param>
     /// <returns>返回解密后的字符串</returns>
     public static String String_JieMi(String str) throws Exception
     {
         //南京宝旺达.
         if (SystemConfig.getCustomerNo().equals("BWDA"))         
             return Cryptos.aesDecrypt(str);
          
         return str;
     }
     public static String String_JieMi_FTP(String str) throws Exception
     {
 
        //南京宝旺达.
       if (SystemConfig.getCustomerNo().equals( "BWDA"))
        	 return Cryptos.aesDecrypt(str);  

         return str;
     }
     ///#endregion 加密解密文件.
}
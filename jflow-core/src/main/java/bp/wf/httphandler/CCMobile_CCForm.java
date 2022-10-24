package bp.wf.httphandler;

import bp.da.*;
import bp.difference.handler.CommonFileUtils;
import bp.difference.handler.WebContralBase;
import bp.en.Map;
import bp.sys.*;
import bp.web.*;
import bp.en.*;
import bp.wf.Glo;
import bp.wf.template.*;
import bp.tools.*;
import bp.difference.*;
import bp.*;
import bp.wf.*;
import net.sf.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

/** 
 表单
*/
public class CCMobile_CCForm extends WebContralBase
{
	/** 
	 构造函数
	*/
	public CCMobile_CCForm() throws Exception {
		WebUser.setSheBei( "Mobile");
	}
	public final String HandlerMapExt() throws Exception {
		WF_CCForm en = new WF_CCForm();
		return en.HandlerMapExt();
	}

	public final String AttachmentUpload_Down() throws Exception {
		WF_CCForm ccform = new WF_CCForm();
		return ccform.AttachmentUpload_Down();
	}
	/** 
	 表单初始化.
	 
	 @return 
	*/
	public final String Frm_Init() throws Exception {
		WF_CCForm ccform = new WF_CCForm();
		return ccform.Frm_Init();
	}

	public final String FrmGener_Init() throws Exception {
		WF_CCForm ccform = new WF_CCForm();
		return ccform.FrmGener_Init();
	}

	public final String Dtl_Init() throws Exception {
		WF_CCForm ccform = new WF_CCForm();
		return ccform.Dtl_Init();
	}

	//保存从表数据
	public final String Dtl_SaveRow() throws Exception {

			///#region  查询出来从表数据.
		GEDtls dtls = new GEDtls(this.getEnsName());
		bp.en.Entity tempVar = dtls.getGetNewEntity();
		GEDtl dtl = tempVar instanceof GEDtl ? (GEDtl)tempVar : null;
		dtls.Retrieve("RefPK", this.GetRequestVal("RefPKVal"), null);
		MapDtl mdtl = new MapDtl(this.getEnsName());
		Map map = dtl.getEnMap();
		for (GEDtl item : dtls.ToJavaList())
		{
			String pkval = item.GetValStringByKey(dtl.getPK());
			for (Attr attr : map.getAttrs())
			{
				if (attr.getIsRefAttr() == true)
				{
					continue;
				}

				if (attr.getMyDataType() == DataType.AppDateTime || attr.getMyDataType() == DataType.AppDate)
				{
					if (attr.getUIIsReadonly() == true)
					{
						continue;
					}

					String val = this.GetValFromFrmByKey("TB_" + attr.getKey() + "_" + pkval, null);
					item.SetValByKey(attr.getKey(),  URLDecoder.decode(val, "UTF-8"));
					continue;
				}


				if (attr.getUIContralType() == UIContralType.TB)
				{

					String val = this.GetValFromFrmByKey("TB_" + attr.getKey() + "_" + pkval, null);
					item.SetValByKey(attr.getKey(), URLDecoder.decode(val, "UTF-8"));
					continue;
				}

				if (attr.getUIContralType() == UIContralType.DDL)
				{
					String val = this.GetValFromFrmByKey("DDL_" + attr.getKey() + "_" + pkval);
					item.SetValByKey(attr.getKey(), URLDecoder.decode(val, "UTF-8"));
					continue;
				}

				if (attr.getUIContralType() == UIContralType.CheckBok)
				{
					String val = this.GetValFromFrmByKey("CB_" + attr.getKey() + "_" + pkval, "-1");
					if (val.equals("0"))
					{
						item.SetValByKey(attr.getKey(), 0);
					}
					else
					{
						item.SetValByKey(attr.getKey(), 1);
					}
					continue;
				}
			}
			item.SetValByKey("OID",pkval);
			//关联主赋值.
			item.setRefPK(this.getRefPKVal());
			switch (mdtl.getDtlOpenType())
			{
				case ForEmp: // 按人员来控制.
					item.setRefPK(this.getRefPKVal());
					break;
				case ForWorkID: // 按工作ID来控制
					item.setRefPK(this.getRefPKVal());
					item.setFID(this.getFID());
					break;
				case ForFID: // 按流程ID来控制.
					item.setRefPK(this.getRefPKVal());
					item.setFID(this.getFID());
					break;
			}
			item.setRec(WebUser.getNo());
			item.Update(); //执行更新.
		}
		return "保存成功.";

			///#endregion  查询出来从表数据.

	}

	//多附件上传方法
	public final String UploadIOSAttach() throws Exception {

		String uploadFileM = ""; //上传附件数据的MyPK,用逗号分开
		String pkVal = this.GetRequestVal("PKVal");
		String attachPk = this.GetRequestVal("FK_FrmAttachment");
		String sort = this.GetRequestVal("Sort");
		String fileSoruce = this.GetRequestVal("fileSource");
		String fileName = this.GetRequestVal("fileName");
		String ext = this.GetRequestVal("Ext");

		// 多附件描述.
		FrmAttachment athDesc = new FrmAttachment(attachPk);
		MapData mapData = new MapData(athDesc.getFK_MapData());
		String msg = "";
		//求出来实体记录，方便执行事件.
		GEEntity en = new GEEntity(athDesc.getFK_MapData());
		en.setPKVal(pkVal);
		if (en.RetrieveFromDBSources() == 0)
		{
			en.setPKVal(this.getFID());
			if (en.RetrieveFromDBSources() == 0)
			{
				en.setPKVal(this.getPWorkID());
				en.RetrieveFromDBSources();
			}
		}

		//求主键. 如果该表单挂接到流程上.
		if (this.getFK_Node() != 0)
		{
			//判断表单方案。
			FrmNode fn = new FrmNode(this.getFK_Node(), this.getFK_MapData());
			if (fn.getFrmSln() == FrmSln.Self)
			{
				FrmAttachment myathDesc = new FrmAttachment();
				myathDesc.setMyPK(attachPk + "_" + this.getFK_Node());
				if (myathDesc.RetrieveFromDBSources() != 0)
				{
					athDesc.setHisCtrlWay(myathDesc.getHisCtrlWay());
				}
			}
			pkVal = Dev2Interface.GetAthRefPKVal(this.getWorkID(), this.getPWorkID(), this.getFID(), this.getFK_Node(), this.getFK_MapData(), athDesc);
		}

		//获取上传文件是否需要加密
		boolean fileEncrypt = SystemConfig.getIsEnableAthEncrypt();


			///#region 文件上传的iis服务器上 or db数据库里.
		if (athDesc.getAthSaveWay() == AthSaveWay.IISServer || athDesc.getAthSaveWay() == AthSaveWay.DB)
		{
			String savePath = athDesc.getSaveTo();
			if (savePath.contains("@") == true || savePath.contains("*") == true)
			{
				/*如果有变量*/
				savePath = savePath.replace("*", "@");

				if (savePath.contains("@") && this.getFK_Node() != 0)
				{
					/*如果包含 @ */
					Flow flow = new Flow(this.getFK_Flow());
					bp.wf.data.GERpt myen = flow.getHisGERpt();
					myen.setOID(this.getWorkID());
					myen.RetrieveFromDBSources();
					savePath = Glo.DealExp(savePath, myen, null);
				}
				if (savePath.contains("@") == true)
				{
					throw new RuntimeException("@路径配置错误,变量没有被正确的替换下来." + savePath);
				}
			}
			else
			{
				savePath = athDesc.getSaveTo() + "/" + pkVal;
			}

			//替换关键的字串.
			savePath = savePath.replace("\\\\", "/");
			try
			{
				if (savePath.contains(SystemConfig.getPathOfWebApp()) == false)
				{
					savePath = SystemConfig.getPathOfWebApp() + savePath;
				}
			}
			catch (RuntimeException ex)
			{
				savePath = SystemConfig.getPathOfDataUser() + "UploadFile/" + mapData.getNo() + "/";
				//return "err@获取路径错误" + ex.Message + ",配置的路径是:" + savePath + ",您需要在附件属性上修改该附件的存储路径.";
			}

			try
			{
				if ((new File(savePath)).isDirectory() == false)
				{
					(new File(savePath)).mkdirs();
				}
			}
			catch (RuntimeException ex)
			{
				throw new RuntimeException("err@创建路径出现错误，可能是没有权限或者路径配置有问题:" + savePath + "@异常信息:" + ex.getMessage());
			}


			String guid = DBAccess.GenerGUID(0, null, null);



			String realSaveTo = savePath + "/" + guid + "." + fileName;

			realSaveTo = realSaveTo.replace("~", "-");
			realSaveTo = realSaveTo.replace("'", "-");
			realSaveTo = realSaveTo.replace("*", "-");

			if (fileEncrypt == true)
			{
				String strtmp = realSaveTo + ".tmp";
				Base64StrToImage(fileSoruce, strtmp);
				AesEncodeUtil.encryptFile(strtmp, strtmp.replace(".tmp", "")); //加密
				(new File(strtmp)).delete(); //删除临时文件
			}
			else
			{
				//文件保存的路径
				Base64StrToImage(fileSoruce, realSaveTo);
			}

			//执行附件上传前事件，added by liuxc,2017-7-15
			msg = ExecEvent.DoFrm(mapData, EventListFrm.AthUploadeBefore, en, "@FK_FrmAttachment=" + athDesc.getMyPK() + "@FileFullName=" + realSaveTo);
			if (!DataType.IsNullOrEmpty(msg))
			{
				bp.sys.Glo.WriteLineError("@AthUploadeBefore事件返回信息，文件：" + fileName + "，" + msg);
				new File(realSaveTo).delete();

			}

			File info = new File(realSaveTo);
			FrmAttachmentDB dbUpload = new FrmAttachmentDB();
			dbUpload.setMyPK(guid);
			dbUpload.setNodeID(this.getFK_Node());
			dbUpload.setSort(sort);
			dbUpload.setFK_MapData(athDesc.getFK_MapData());
			dbUpload.setFK_FrmAttachment(attachPk);
			dbUpload.setFileExts(getFileExtension(info));
			dbUpload.setFID(this.getFID());
			if (fileEncrypt == true)
				dbUpload.SetPara("IsEncrypt", 1);
			dbUpload.setFileFullName(realSaveTo);
			dbUpload.setFileName(fileName);
			dbUpload.setFileSize((float)info.length());
			dbUpload.setRDT(DataType.getCurrentDateTimess());
			dbUpload.setRec(bp.web.WebUser.getNo());
			dbUpload.setRecName(bp.web.WebUser.getName());
			dbUpload.setFK_Dept(WebUser.getFK_Dept());
			dbUpload.setFK_DeptName(WebUser.getFK_DeptName());
			dbUpload.setRefPKVal(pkVal);
			dbUpload.setFID(this.getFID());

			dbUpload.setUploadGUID(guid);
			dbUpload.Insert();
			//执行附件上传后事件，added by liuxc,2017-7-15
			msg = ExecEvent.DoFrm(mapData, EventListFrm.AthUploadeAfter, en, "@FK_FrmAttachment=" + dbUpload.getFK_FrmAttachment() + "@FK_FrmAttachmentDB=" + dbUpload.getMyPK() + "@FileFullName=" + dbUpload.getFileFullName());
			if (!DataType.IsNullOrEmpty(msg))
				bp.sys.Glo.WriteLineError("@AthUploadeAfter事件返回信息，文件：" + dbUpload.getFileName() + "，" + msg);
		}

			///#endregion 文件上传的iis服务器上 or db数据库里.


			///#region 保存到数据库 / FTP服务器上.
		if (athDesc.getAthSaveWay() == AthSaveWay.FTPServer)
		{
			String guid = DBAccess.GenerGUID(0, null, null);

			//把文件临时保存到一个位置.
			String temp = SystemConfig.getPathOfTemp() + "" + guid + ".tmp";

			if (fileEncrypt == true)
			{
				String strtmp = SystemConfig.getPathOfTemp() + "" + guid + "_Desc" + ".tmp";
				Base64StrToImage(fileSoruce, strtmp);
				AesEncodeUtil.encryptFile(strtmp, temp); //加密
				(new File(strtmp)).delete(); //删除临时文件
			}
			else
			{
				//文件保存的路径
				Base64StrToImage(fileSoruce, temp);
			}

			//执行附件上传前事件，added by liuxc,2017-7-15
			msg = ExecEvent.DoFrm(mapData, EventListFrm.AthUploadeBefore, en, "@FK_FrmAttachment=" + athDesc.getMyPK() + "@FileFullName=" + temp);
			if (DataType.IsNullOrEmpty(msg) == false)
			{
				bp.sys.Glo.WriteLineError("@AthUploadeBefore事件返回信息，文件：" +fileName + "，" + msg);
				new File(temp).delete();

				throw new Exception("err@上传附件错误：" + msg);
			}

			File info = new File(temp);
			FrmAttachmentDB dbUpload = new FrmAttachmentDB();
			dbUpload.setMyPK(guid);
			dbUpload.setNodeID(this.getFK_Node());
			dbUpload.setSort(sort);
			dbUpload.setFK_MapData(athDesc.getFK_MapData());
			dbUpload.setFK_FrmAttachment(attachPk);
			dbUpload.setFileExts(getFileExtension(info));
			dbUpload.setFID(this.getFID());
			if (fileEncrypt == true)
				dbUpload.SetPara("IsEncrypt", 1);
			dbUpload.setFileName(fileName);
			dbUpload.setFileSize((float)info.length());
			dbUpload.setRDT(DataType.getCurrentDateTimess());
			dbUpload.setRec(bp.web.WebUser.getNo());
			dbUpload.setRecName(bp.web.WebUser.getName());
			dbUpload.setFK_Dept(WebUser.getFK_Dept());
			dbUpload.setFK_DeptName(WebUser.getFK_DeptName());
			dbUpload.setRefPKVal(pkVal);
			dbUpload.setFID(this.getFID());

			dbUpload.setUploadGUID(guid);


			if (athDesc.getAthSaveWay() == AthSaveWay.FTPServer)
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM");
				String ny = sdf.format(new Date());

				String workDir = ny + "/" + athDesc.getFK_MapData() + "/";

				//特殊处理文件路径.
				if (SystemConfig.getCustomerNo().equals( "BWDA") ) {

					sdf = new SimpleDateFormat("yyyy_MM_dd");
					ny = sdf.format(new Date());

					ny = ny.replace("_", "/");
					ny = ny.replace("_", "/");

					workDir =  ny+ "/" + WebUser.getNo()+"/";
				}

				boolean  isOK=false;

				if (SystemConfig.getFTPServerType().equals("FTP") ) {

					FtpUtil ftpUtil = bp.wf.Glo.getFtpUtil();

					ftpUtil.changeWorkingDirectory(workDir,true);

					// 把文件放在FTP服务器上去.
					isOK=ftpUtil.uploadFile( guid + "." + dbUpload.getFileExts(),temp);

					ftpUtil.releaseConnection();
				}

				if (SystemConfig.getFTPServerType().equals("SFTP") ) {

					SftpUtil ftpUtil = bp.wf.Glo.getSftpUtil();

					ftpUtil.changeWorkingDirectory(workDir,true);
					// 把文件放在FTP服务器上去.
					isOK=ftpUtil.uploadFile(guid + "." + dbUpload.getFileExts(),temp);
					ftpUtil.releaseConnection();
				}


				// 删除临时文件
				new File(temp).delete();
				new File(SystemConfig.getPathOfTemp() + "" + guid + "_Desc" + ".tmp").delete();

				// 设置路径.
				dbUpload.setFileFullName( workDir  + guid + "." + dbUpload.getFileExts());

				if (isOK==false)
					throw new RuntimeException("err文件上传失败，请检查ftp服务器配置信息");

				dbUpload.Insert();
			}


			//执行附件上传后事件，added by liuxc,2017-7-15
			msg = ExecEvent.DoFrm(mapData,EventListFrm.AthUploadeAfter, en, "@FK_FrmAttachment=" + dbUpload.getFK_FrmAttachment() + "@FK_FrmAttachmentDB=" + dbUpload.getMyPK()
					+ "@FileFullName=" + temp);

			if (DataType.IsNullOrEmpty(msg)==false)
				bp.sys.Glo.WriteLineError("@AthUploadeAfter事件返回信息，文件：" + dbUpload.getFileName() + "，" + msg);

		}
		//保存到数据库.
		return "上传成功";
	}


	public final void Base64StrToImage(String base64Str, String savePath)
	{
		try{
		base64Str = base64Str.replace(" ", "+");
		String[] str = base64Str.split(",");  //base64Str为base64完整的字符串，先处理一下得到我们所需要的字符串
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] imageBytes = decoder.decode(str[1]);
		InputStream in = new ByteArrayInputStream(imageBytes);
		byte[] b = new byte[1024];
		int nRead = 0;

		OutputStream o = new FileOutputStream(savePath);

		while ((nRead = in.read(b)) != -1) {
			o.write(b, 0, nRead);
		}

		o.flush();
		o.close();

		in.close();
	}catch(Exception e){
		throw new RuntimeException("err@IOS上传附件失败");
	}

	}

		/** 
		 获取百度云token
		 
		 @return 
		*/
		public final String getAccessToken()  {

			String ak = SystemConfig.getAPIKey();
			String sk = SystemConfig.getSecretKey();

			// 获取token地址
			String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
			String getAccessTokenUrl = authHost
					// 1. grant_type为固定参数
					+ "grant_type=client_credentials"
					// 2. 官网获取的 API Key
					+ "&client_id=" + ak
					// 3. 官网获取的 Secret Key
					+ "&client_secret=" + sk;
			try {
				URL realUrl = new URL(getAccessTokenUrl);
				// 打开和URL之间的连接
				HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();

				// 定义 BufferedReader输入流来读取URL的响应
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String result = "";
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
			} catch (Exception e) {
				System.err.printf("获取token失败！");
				e.printStackTrace(System.err);
			}
			return null;
		}

//	public final String GetAccessTokenImgs() throws Exception {
//		//获取 AccessToken
//		return bp.gpm.weixin.WeiXinEntity.getAccessToken();
//	}

	/** 
	 下载微信服务器图片，上传到应用服务器
	 
	 param mideaid
	*/
//	public final String MyFlowGener_SaveUploadeImg() throws Exception {
//		try
//		{
//			String media_id = this.GetRequestVal("IDs");
//			String athMyPK = this.GetRequestVal("AthMyPK"); //图片组件.
//
//			FrmAttachment athDesc = new FrmAttachment(athMyPK);
//
//			if (DataType.IsNullOrEmpty(media_id))
//			{
//				return "media_id为空";
//			}
//			String accessToken = GetAccessTokenImgs();
//			//bp.da.Log.DebugWriteError("accessToken:" + accessToken);
//			Bitmap img = null;
//			HttpWebRequest req;
//			HttpWebResponse res = null;
//
//			String url = String.format("https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=%1$s&media_id=%2$s", accessToken, media_id);
//
//			System.Uri httpUrl = new System.Uri(url);
//			req = (HttpWebRequest)(WebRequest.Create(httpUrl));
//			req.Timeout = 180000; //设置超时值10秒
//			req.UserAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)";
//			req.Method = "GET";
//			res = (HttpWebResponse)(req.GetResponse());
//			img = new Bitmap(res.GetResponseStream()); //获取图片流
//			//bp.da.Log.DebugWriteError(res.GetResponseHeader("Content-Type"));
//			String fileName = res.GetResponseHeader("Content-Disposition").replace("attachment; filename=", "").replace("\"", "");
//
//			img.Save(SystemConfig.getPathOfTemp() + fileName);
//			Log.DebugWriteError(this.getFK_Node() + ":" + this.getFK_Flow() + ":" + this.getWorkID() + ":" + athDesc.NoOfObj + ":" + athDesc.getFK_MapData() + ":" + SystemConfig.getPathOfTemp() + fileName + ":" + fileName);
//			CCFormAPI.CCForm_AddAth(this.getFK_Node(),this.getFK_Flow(), this.getWorkID(), athMyPK, athDesc.getFK_MapData(), SystemConfig.getPathOfTemp() + fileName, fileName);
//
//			return "执行成功";
//		}
//		catch (RuntimeException ex)
//		{
//			String msg = "err@GetMedia:" + ex.getMessage() + " -- " + ex.StackTrace;
//			Log.DebugWriteError(msg);
//			return msg;
//
//		}
//	}

//ORIGINAL LINE: public static byte[] BitmapToBytes(Bitmap Bitmap)
//	public static byte[] BitmapToBytes(Bitmap Bitmap)
//	{
////C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.MemoryStream is input or output:
//		MemoryStream ms = null;
//		try
//		{
////C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.MemoryStream is input or output:
//			ms = new MemoryStream();
//			Bitmap.Save(ms, Bitmap.RawFormat);
//
////ORIGINAL LINE: byte[] byteImage = new Byte[ms.Length];
//			byte[] byteImage = new byte[ms.Length];
//			byteImage = ms.ToArray();
//			return byteImage;
//		}
//		catch (NullPointerException ex)
//		{
//			throw ex;
//		}
//		finally
//		{
//			ms.Close();
//		}
//	}
	/** 
	 调用企业号获取地理位置
	 
	 @return 
	*/
//	public final String GetWXConfigSetting() throws Exception {
//		String htmlPage = this.GetRequestVal("htmlPage");
//		Hashtable ht = new Hashtable();
//
//		//生成签名的时间戳
//		String timestamp = Date.now().toString("yyyyMMDDHHddss");
//		//生成签名的随机串
//		String nonceStr = DBAccess.GenerGUID(0, null, null);
//		//企业号jsapi_ticket
//		String jsapi_ticket = "";
//		String url1 = htmlPage;
//		//获取 AccessToken
//		String accessToken = bp.gpm.weixin.WeiXinEntity.getAccessToken();
//
//		String url = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=" + accessToken;
//
//
//		HttpWebResponse response = (new HttpWebResponseUtility()).CreateGetHttpResponse(url, 10000, null, null);
//		InputStreamReader reader = new InputStreamReader(response.GetResponseStream(), java.nio.charset.StandardCharsets.UTF_8);
//		String str = reader.ReadToEnd();
//
//		//权限签名算法
//		bp.gpm.weixin.Ticket ticket = new bp.gpm.weixin.Ticket();
//		ticket = FormatToJson.<bp.gpm.weixin.Ticket>ParseFromJson(str);
//
//		if (ticket.getErrcode().equals("0"))
//		{
//			jsapi_ticket = ticket.getTicket();
//		}
//		else
//		{
//			return "err:@获取jsapi_ticket失败+accessToken=" + accessToken;
//		}
//
//		ht.put("timestamp", timestamp);
//		ht.put("nonceStr", nonceStr);
//		//企业微信的corpID
//		ht.put("AppID", SystemConfig.getWX_CorpID());
//
//		//生成签名算法
//		String str1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url1 + "";
//		String Signature = Sha1Signature(str1);
//		ht.put("signature", Signature);
//
//		return Json.ToJson(ht);
//	}
//	public static String Sha1Signature(String str)
//	{
//		String s = System.Web.Security.FormsAuthentication.HashPasswordForStoringInConfigFile(str, "SHA1").toString();
//		return s.toLowerCase();
//	}



public final String GetIDCardInfo() throws Exception {
		String token = getAccessToken();
	    JSONObject jd = JSONObject.fromObject(token);
	    String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";
	    long filesSize = CommonFileUtils.getFilesSize(getRequest(), "file");
		if (filesSize == 0)
		{
			return "err@请选择要上传的身份证件。";
		}
		InputStream stream = CommonFileUtils.getInputStream(getRequest(), "file"); //new MemoryStream();

//ORIGINAL LINE: byte[] bytes = new byte[stream.Length];
	    byte[] bytes = new byte[stream.available()];
		stream.read(bytes);
	    Base64.Encoder encoder = Base64.getEncoder();
	    String imgParam = encoder.encodeToString(bytes);
		stream.close();

	    String param = "id_card_side=" + "front" + "&image=" + imgParam;
	// 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
	Hashtable<String,String> map = new Hashtable<String,String>();
	map.put("access_token",jd.getString("access_token"));
	map.put("id_card_side","front");
	map.put("image",imgParam);

	String result =HttpClientUtil.doPost(url, map, "Content-Type","application/x-www-form-urlencoded");
	System.out.println(result);
	return result;
}
	/**
	 * 获取文件后缀的方法
	 *
	 * @param file 要获取文件后缀的文件
	 * @return 文件后缀
	 * @author https://www.4spaces.org/
	 */
	public String getFileExtension(File file) {
		String extension = "";
		try {
			if (file != null && file.exists()) {
				String name = file.getName();
				extension = name.substring(name.lastIndexOf("."));
			}
		} catch (Exception e) {
			extension = "";
		}
		return extension;
	}
}
package bp.wf.httphandler;
import bp.da.*;
import bp.difference.SystemConfig;
import bp.difference.handler.CommonFileUtils;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.tools.AesEncodeUtil;
import bp.tools.FtpUtil;
import bp.tools.HttpClientUtil;
import bp.tools.SftpUtil;
import bp.web.*;
import bp.port.*;
import bp.en.*;
import bp.en.Map;
import bp.wf.*;
import bp.wf.template.*;
//import bp.wf.weixin.*;
import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Base64.Encoder;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.time.*;

/** 
 表单
*/
public class CCMobile_CCForm extends WebContralBase
{
	/** 
	 构造函数
	 * @throws Exception 
	*/
	public CCMobile_CCForm() throws Exception
	{
		WebUser.setSheBei("Mobile");
	}
	public final String HandlerMapExt() throws Exception
	{
		WF_CCForm en = new WF_CCForm();
		return en.HandlerMapExt();
	}

	public final String AttachmentUpload_Down() throws Exception
	{
		WF_CCForm ccform = new WF_CCForm();
		return ccform.AttachmentUpload_Down();
	}
	/** 
	 表单初始化.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Frm_Init() throws Exception
	{
		WF_CCForm ccform = new WF_CCForm();
		return ccform.Frm_Init();
	}

	public final String Dtl_Init() throws Exception
	{
		WF_CCForm ccform = new WF_CCForm();
		return ccform.Dtl_Init();
	}

	//保存从表数据
	public final String Dtl_SaveRow() throws Exception
	{

		GEDtls dtls = new GEDtls(this.getEnsName());
		GEDtl dtl = dtls.getGetNewEntity() instanceof GEDtl ? (GEDtl) dtls.getGetNewEntity() : null;
		dtls.Retrieve("RefPK", this.GetRequestVal("RefPKVal"));
		MapDtl mdtl = new MapDtl(this.getEnsName());
		Map map = dtl.getEnMap();
		for (Entity item : dtls.ToJavaList()) {
			String pkval = item.GetValStringByKey(dtl.getPK());
			for (Attr attr : map.getAttrs()) {
				if (attr.getIsRefAttr() == true) {
					continue;
				}

				if (attr.getMyDataType() == DataType.AppDateTime || attr.getMyDataType() == DataType.AppDate) {
					if (attr.getUIIsReadonly() == true) {
						continue;
					}

					String val = this.GetValFromFrmByKey("TB_" + attr.getKey() + "_" + pkval, null);
					item.SetValByKey(attr.getKey(), val);
					continue;
				}

				if (attr.getUIContralType() == UIContralType.TB ) {
					String val = this.GetValFromFrmByKey("TB_" + attr.getKey() + "_" + pkval, null);
					item.SetValByKey(attr.getKey(), URLDecoder.decode(val, "UTF-8"));
					continue;
				}

				if (attr.getUIContralType() == UIContralType.DDL ) {
					String val = this.GetValFromFrmByKey("DDL_" + attr.getKey() + "_" + pkval);
					item.SetValByKey(attr.getKey(), URLDecoder.decode(val, "UTF-8"));
					continue;
				}

				if (attr.getUIContralType() == UIContralType.CheckBok) {
					String val = this.GetValFromFrmByKey("CB_" + attr.getKey() + "_" + pkval, "-1");
					if (val.equals("0")) {
						item.SetValByKey(attr.getKey(), 0);
					} else {
						item.SetValByKey(attr.getKey(), 1);
					}
					continue;
				}
			}
			item.SetValByKey("OID", pkval);
			item.Update(); // 执行更新.
		}
		return "保存成功.";

	}


	/** 
	 获取百度云token
	 
	 @return 
	*/
	public String getAccessToken()
	{
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


	public String GetIDCardInfo() throws Exception
	{
		String token = getAccessToken();
		JSONObject jd = JSONObject.fromObject(token);

		String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";
		// 本地文件路径
		// 图片的base64编码
		long filesSize = CommonFileUtils.getFilesSize(getRequest(), "file");
		if (filesSize == 0) {
			return "err@请选择要上传的身份证件。";
		}

		InputStream stream = CommonFileUtils.getInputStream(getRequest(), "file");//new MemoryStream();
		byte[] bytes = new byte[stream.available()];
		stream.read(bytes);
		Encoder encoder = Base64.getEncoder();
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
	 * IOS附件上传
	 * @return
	 */
	public String UploadIOSAttach() throws Exception
	{

		String uploadFileM = ""; //上传附件数据的MyPK,用逗号分开
		String pkVal = this.GetRequestVal("PKVal");
		String attachPk = this.GetRequestVal("FK_FrmAttachment");
		String sort = this.GetRequestVal("Sort");
		String fileSoruce = this.GetRequestVal("fileSource");
		String fileName = this.GetRequestVal("fileName");
		String ext = this.GetRequestVal("Ext");

		// 多附件描述.
		bp.sys.FrmAttachment athDesc = new bp.sys.FrmAttachment(attachPk);
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
				bp.sys.FrmAttachment myathDesc = new FrmAttachment();
				myathDesc.setMyPK(attachPk + "_" + this.getFK_Node());
				if (myathDesc.RetrieveFromDBSources() != 0)
					athDesc.setHisCtrlWay( myathDesc.getHisCtrlWay());
			}
			pkVal = bp.wf.Dev2Interface.GetAthRefPKVal(this.getWorkID(), this.getPWorkID(), this.getFID(), this.getFK_Node(), this.getFK_MapData(), athDesc);
		}

		//获取上传文件是否需要加密
		boolean fileEncrypt = SystemConfig.getIsEnableAthEncrypt();

        //文件上传的iis服务器上 or db数据库里.
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
					bp.wf.Flow flow = new bp.wf.Flow(this.getFK_Flow());
					bp.wf.data.GERpt myen = flow.getHisGERpt();
					myen.setOID(this.getWorkID());
					myen.RetrieveFromDBSources();
					savePath = bp.wf.Glo.DealExp(savePath, myen, null);
				}
				if (savePath.contains("@") == true)
					throw new Exception("@路径配置错误,变量没有被正确的替换下来." + savePath);
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
					savePath = SystemConfig.getPathOfWebApp()+ savePath;
			}
			catch (Exception ex)
			{
				savePath = SystemConfig.getPathOfDataUser() + "UploadFile/" + mapData.getNo() + "/";
				//return "err@获取路径错误" + ex.Message + ",配置的路径是:" + savePath + ",您需要在附件属性上修改该附件的存储路径.";
			}
			File file = new File(savePath);
			try
			{
				if (file.exists() == false)
					file.mkdir();
			}
			catch (Exception ex)
			{
				throw new Exception("err@创建路径出现错误，可能是没有权限或者路径配置有问题:" + savePath + "@异常信息:" + ex.getMessage());
			}


			String guid = DBAccess.GenerGUID();



			String realSaveTo = savePath + "/" + guid + "." + fileName;

			realSaveTo = realSaveTo.replace("~", "-");
			realSaveTo = realSaveTo.replace("'", "-");
			realSaveTo = realSaveTo.replace("*", "-");

			if (fileEncrypt == true)
			{
				String strtmp = realSaveTo + ".tmp";
				Base64StrToImage(fileSoruce, strtmp);
				AesEncodeUtil.encryptFile(strtmp, strtmp.replace(".tmp", ""));//加密
				new File(strtmp).delete();//删除临时文件
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
			dbUpload.setRDT(DataType.getCurrentDataTimess());
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
         //文件上传的iis服务器上 or db数据库里.

        //保存到数据库 / FTP服务器上.
		if (athDesc.getAthSaveWay() == AthSaveWay.FTPServer)
		{
			String guid = DBAccess.GenerGUID();

			//把文件临时保存到一个位置.
			String temp = SystemConfig.getPathOfTemp() + "" + guid + ".tmp";

			if (fileEncrypt == true)
			{
				String strtmp = SystemConfig.getPathOfTemp() + "" + guid + "_Desc" + ".tmp";
				Base64StrToImage(fileSoruce, strtmp);
				AesEncodeUtil.encryptFile(strtmp, temp);//加密
				new File(strtmp).delete();//删除临时文件
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
			dbUpload.setRDT(DataType.getCurrentDataTimess());
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
					throw new com.sun.star.uno.Exception("err文件上传失败，请检查ftp服务器配置信息");

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

	public  void Base64StrToImage(String base64Str, String savePath)
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
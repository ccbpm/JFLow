package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.Handler.CommonFileUtils;
import BP.Difference.Handler.WebContralBase;
import BP.En.Attr;
import BP.En.Entity;
import BP.En.Map;
import BP.En.UIContralType;
import BP.Sys.*;
import net.sf.json.JSONObject;
import org.aspectj.util.FileUtil;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.stream.Stream;


/**
 * 页面功能实体
 */
public class CCMobile_CCForm extends WebContralBase {
	/**
	 * 构造函数
	 */
	public CCMobile_CCForm() {
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
	 * 表单初始化.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String Frm_Init() throws Exception {
		WF_CCForm ccform = new WF_CCForm();
		return ccform.Frm_Init();
	}

	public final String Dtl_Init() throws Exception {
		WF_CCForm ccform = new WF_CCForm();
		return ccform.Dtl_Init();
	}

	// 保存从表数据
	public final String Dtl_SaveRow() throws Exception {

		/// #region 查询出来从表数据.
		GEDtls dtls = new GEDtls(this.getEnsName());
		GEDtl dtl = dtls.getNewEntity() instanceof GEDtl ? (GEDtl) dtls.getNewEntity() : null;
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

		/// #endregion 查询出来从表数据.
	}

	/// <summary>
	/// 获取百度云token
	/// </summary>
	/// <returns></returns>
	public String getAccessToken()
	{
		String ak = BP.Difference.SystemConfig.getAPIKey();
		String sk = BP.Difference.SystemConfig.getSecretKey();

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
		String imgParam = new BASE64Encoder().encode(bytes);
		stream.close();

		String param = "id_card_side=" + "front" + "&image=" + imgParam;

		// 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
		Hashtable<String,String> map = new Hashtable<String,String>();
		map.put("access_token",jd.getString("access_token"));
		map.put("id_card_side","front");
		map.put("image",imgParam);

		String result =BP.Tools.HttpClientUtil.doPost(url, map, "Content-Type","application/x-www-form-urlencoded");
		System.out.println(result);
		return result;



	}
}
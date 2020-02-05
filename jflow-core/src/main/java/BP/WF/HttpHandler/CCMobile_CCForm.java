package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.Handler.WebContralBase;
import BP.En.Attr;
import BP.En.Entity;
import BP.En.Map;
import BP.En.UIContralType;
import BP.Sys.*;

import java.net.URLDecoder;

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
}
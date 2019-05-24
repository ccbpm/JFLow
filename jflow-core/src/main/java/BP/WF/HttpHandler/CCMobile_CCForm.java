package BP.WF.HttpHandler;



import BP.DA.DataType;
import BP.En.Attr;
import BP.En.Entity;
import BP.En.Map;
import BP.En.UIContralType;
import BP.Sys.GEDtl;
import BP.Sys.GEDtls;
import BP.WF.HttpHandler.Base.WebContralBase;

/** 
 页面功能实体
 
*/
public class CCMobile_CCForm extends WebContralBase
{

	/**
	 * 构造函数
	 */
	public CCMobile_CCForm()
	{
	
	}
	
	public final String HandlerMapExt() throws Exception
	{
		WF_CCForm en = new WF_CCForm();
		return en.HandlerMapExt();
	}

	public String AttachmentUpload_Down() throws Exception
    {
        WF_CCForm ccform = new WF_CCForm(this.context);
        return ccform.AttachmentUpload_Down();
    }
	
	/// <summary>
    /// 表单初始化.
    /// </summary>
    /// <returns></returns>
    public String Frm_Init() throws Exception
    {
        WF_CCForm ccform = new WF_CCForm(this.context);
        return ccform.Frm_Init();
    }

    public String Dtl_Init() throws Exception
    {
        WF_CCForm ccform = new WF_CCForm(this.context);
        return ccform.Dtl_Init();
    }
	
	
	/// <summary>
    /// 保存从表数据
    /// </summary>
    /// <returns></returns>
    
    public String Dtl_SaveRow() throws Exception
    {
        //#region  查询出来从表数据.
        GEDtls dtls = new GEDtls(this.getEnsName());
        GEDtl dtl = (GEDtl) dtls.getGetNewEntity();
        dtls.Retrieve("RefPK", this.GetRequestVal("RefPKVal"));
        BP.En.Map map = dtl.getEnMap();
        for (GEDtl item : dtls.ToJavaList())
        {
            String pkval = item.GetValStringByKey(dtl.getPK());
            for (Attr attr : ((BP.En.Map) map).getAttrs())
            {
                if (attr.getUIVisible() == false )
                    continue;

                if (attr.getMyDataType() == DataType.AppDateTime || attr.getMyDataType() == DataType.AppDate)
                {
                    String val = this.GetValFromFrmByKey("TB_" + attr.getKey() + "_" + pkval, null);
                    item.SetValByKey(attr.getKey(), val);
                    continue;
                }


                if (attr.getUIContralType().equals(UIContralType.TB)  && attr.getUIIsReadonly() == false)
                {
                    String val = this.GetValFromFrmByKey("TB_" + attr.getKey() + "_" + pkval, null);
                    item.SetValByKey(attr.getKey(), val);
                    continue;
                }

                if (attr.getUIContralType().equals(UIContralType.DDL) && attr.getUIIsReadonly() == true)
                {
                    String val = this.GetValFromFrmByKey("DDL_" + attr.getKey() + "_" + pkval);
                    item.SetValByKey(attr.getKey(), val);
                    continue;
                }

                if (attr.getUIContralType().equals(UIContralType.CheckBok) && attr.getUIIsReadonly() == true)
                {
                    String val = this.GetValFromFrmByKey("CB_" + attr.getKey() + "_" + pkval, "-1");
                    if (val == "-1")
                        item.SetValByKey(attr.getKey(), 0);
                    else
                        item.SetValByKey(attr.getKey(), 1);
                    continue;
                }
            }
            item.SetValByKey("OID",pkval);
            item.Update(); //执行更新.
        }
        //#endregion  查询出来从表数据.
        return "保存成功.";
       /* //#region 保存新加行.
      
        String keyVal = "";
        for (Attr attr : ((BP.En.Map) map).getAttrs())
        {

            if (attr.getMyDataType() == DataType.AppDateTime || attr.getMyDataType() == DataType.AppDate)
            {
                if (attr.getUIIsReadonly() == true)
                    continue;

                keyVal = this.GetValFromFrmByKey("TB_" + attr.getKey() + "_0", null);
                dtl.SetValByKey(attr.getKey(), keyVal);
                continue;
            }


            if (attr.getUIContralType().equals(UIContralType.TB) && attr.getUIIsReadonly() == false)
            {
                keyVal = this.GetValFromFrmByKey("TB_" + attr.getKey() + "_0");
                if (attr.getIsNum() && keyVal == "")
                    keyVal = "0";
                dtl.SetValByKey(attr.getKey(), keyVal);
                continue;
            }

            if (attr.getUIContralType().equals(UIContralType.DDL) && attr.getUIIsReadonly() == true)
            {
                keyVal = this.GetValFromFrmByKey("DDL_" + attr.getKey() + "_0");
                dtl.SetValByKey(attr.getKey(), keyVal);
                continue;
            }

            if (attr.getUIContralType().equals(UIContralType.CheckBok) && attr.getUIIsReadonly() == true)
            {
                keyVal = this.GetValFromFrmByKey("CB_" + attr.getKey() + "_0", "-1");
                if (keyVal == "-1")
                    dtl.SetValByKey(attr.getKey(), 0);
                else
                    dtl.SetValByKey(attr.getKey(), 1);
                continue;
            }
        }

        dtl.SetValByKey("RefPK", this.GetRequestVal("RefPKVal"));
        dtl.setPKVal("0");
       
		dtl.Insert();
		
        
        //#endregion 保存新加行.

        return "保存成功.";*/
    }


}

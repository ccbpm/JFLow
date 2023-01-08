package bp.app.handler.Entity;

import bp.en.EntityMyPK;
import bp.en.Map;
import bp.en.UAC;

public class XZCJ extends EntityMyPK {
    /**
     UI界面上的访问控制
     */
    @Override
    public UAC getHisUAC()  {
        UAC uac = new UAC();
        //对所有人开放权限
        uac.OpenAll();
        return uac;
    }

    /**
     * 获取案件编号
     * @return
     */
    public final String getAJBH()
    {
        return this.GetValStringByKey(XZCJAttr.AJBH);
    }

    /**
     * 设置案件编号
     * @param value
     */
    public final void setAJBH(String value)
    {
        SetValByKey(XZCJAttr.AJBH, value);
    }

    /**
     * 获取案件状态
     * @return
     */
    public final String getState()
    {
        return this.GetValStringByKey(XZCJAttr.State);
    }

    /**
     * 设置案件状态
     * @param value
     */
    public final void setState(String value)
    {
        SetValByKey(XZCJAttr.State, value);
    }

    /**
     * 获取案件标题
     * @return
     */
    public final String getTitle()
    {
        return this.GetValStringByKey(XZCJAttr.Title);
    }

    /**
     * 设置案件标题
     * @param value
     */
    public final void setTitle(String value)
    {
        SetValByKey(XZCJAttr.Title, value);
    }

    /**
     * 获取请求人
     * @return
     */
    public final String getQQR()
    {
        return this.GetValStringByKey(XZCJAttr.QQR);
    }

    /**
     * 设置请求人
     * @param value
     */
    public final void setQQR(String value)
    {
        SetValByKey(XZCJAttr.QQR, value);
    }

    /**
     * 获取请求日期
     * @return
     */
    public final String getQQRQ()
    {
        return this.GetValStringByKey(XZCJAttr.QQRQ);
    }

    /**
     * 设置请求日期
     * @param value
     */
    public final void setQQRQ(String value)
    {
        SetValByKey(XZCJAttr.QQRQ, value);
    }

    /**
     * 获取被请求人
     * @return
     */
    public final String getBQQR()
    {
        return this.GetValStringByKey(XZCJAttr.BQQR);
    }

    /**
     * 设置被请求人
     * @param value
     */
    public final void setBQQR(String value)
    {
        SetValByKey(XZCJAttr.BQQR, value);
    }

    /**
     * 获取专利号
     * @return
     */
    public final String getZLH()
    {
        return this.GetValStringByKey(XZCJAttr.ZLH);
    }

    /**
     * 设置专利号
     * @param value
     */
    public final void setZLH(String value)
    {
        SetValByKey(XZCJAttr.ZLH, value);
    }

    /**
     * 获取专利名称
     * @return
     */
    public final String getZLMC()
    {
        return this.GetValStringByKey(XZCJAttr.ZLMC);
    }

    /**
     * 设置专利名称
     * @param value
     */
    public final void setZLMC(String value)
    {
        SetValByKey(XZCJAttr.ZLMC, value);
    }

    /**
     * 获取专利权人
     * @return
     */
    public final String getZLQR()
    {
        return this.GetValStringByKey(XZCJAttr.ZLQR);
    }

    /**
     * 设置专利权人
     * @param value
     */
    public final void setZLQR(String value)
    {
        SetValByKey(XZCJAttr.ZLQR, value);
    }

    /**
     * 获取专利申请日期
     * @return
     */
    public final String getZLSQRQ()
    {
        return this.GetValStringByKey(XZCJAttr.ZLSQRQ);
    }

    /**
     * 设置专利申请日期
     * @param value
     */
    public final void setZLSQRQ(String value)
    {
        SetValByKey(XZCJAttr.ZLSQRQ, value);
    }

    /**
     * 获取授权公告日
     * @return
     */
    public final String getSQGGR()
    {
        return this.GetValStringByKey(XZCJAttr.SQGGR);
    }

    /**
     * 设置授权公告日
     * @param value
     */
    public final void setSQGGR(String value)
    {
        SetValByKey(XZCJAttr.SQGGR, value);
    }

    /**
     * 获取公告号
     * @return
     */
    public final String getSQGGH()
    {
        return this.GetValStringByKey(XZCJAttr.SQGGH);
    }

    /**
     * 设置公告号
     * @param value
     */
    public final void setSQGGH(String value)
    {
        SetValByKey(XZCJAttr.SQGGH, value);
    }

    /**
     * 获取证书号
     * @return
     */
    public final String getZSH()
    {
        return this.GetValStringByKey(XZCJAttr.ZSH);
    }

    /**
     * 设置证书号
     * @param value
     */
    public final void setZSH(String value)
    {
        SetValByKey(XZCJAttr.ZSH, value);
    }

    /**
     * 立案时间
     * @return
     */
    public final String getLASJ()
    {
        return this.GetValStringByKey(XZCJAttr.LASJ);
    }

    /**
     * 设置立案时间
     * @param value
     */
    public final void setLASJ(String value)
    {
        SetValByKey(XZCJAttr.LASJ, value);
    }

    public XZCJ(){}

    /**
     * 查询对象
     * @param AJBH
     * @throws Exception
     */
    public XZCJ(String AJBH) throws Exception{
        if (AJBH == null || AJBH.length() == 0)
        {
            throw new RuntimeException("@要查询的案件编号为空。");
        }
        this.setAJBH(AJBH);
        this.Retrieve();
    }

    /**
     * 重写map
     * @return
     */
    @Override
    public bp.en.Map getEnMap()  {
        if (this.get_enMap() != null)
        {
            return this.get_enMap();
        }

        Map map = new Map("App_XZCJ", "行政裁决案件列表");
        map.AddTBStringPK("MyPK", null, "主键MyPK", false, true, 1, 150, 10);
        map.AddTBString(XZCJAttr.AJBH, null, "案件编号", true, true, 0, 100, 200);
        map.AddDDLSysEnum(XZCJAttr.State,0,"案件状态",true,true,XZCJAttr.State,"@0=未立案@1=已立案@2=不予立案@3=调查取证@4=待开庭@5=已结案@6=已公示@7=监督中");
        map.AddTBString(XZCJAttr.Title, null, "案件标题", true, true, 0, 500, 200);
        map.AddTBString(XZCJAttr.QQR, null, "请求人", true, true, 0, 100, 200);
        map.AddTBString(XZCJAttr.QQRQ, null, "请求日期", true, true, 0, 100, 200);
        map.AddTBString(XZCJAttr.BQQR, null, "被请求人", true, true, 0, 100, 200);
        map.AddTBString(XZCJAttr.ZLH, null, "专利号", true, true, 0, 100, 200);
        map.AddTBString(XZCJAttr.ZLMC, null, "专利名称", true, true, 0, 200, 200);
        map.AddTBString(XZCJAttr.ZLQR, null, "专利权人", true, true, 0, 100, 200);
        map.AddTBString(XZCJAttr.ZLSQRQ, null, "专利申请日期", true, true, 0, 100, 200);
        map.AddTBString(XZCJAttr.SQGGR, null, "授权公告日", true, true, 0, 100, 200);
        map.AddTBString(XZCJAttr.SQGGH, null, "授权公告号", true, true, 0, 100, 200);
        map.AddTBString(XZCJAttr.ZSH, null, "证书号", true, true, 0, 100, 200);
        map.AddTBString(XZCJAttr.LASJ, null, "立案时间", true, false, 0, 100, 200);

        this.set_enMap(map);
        return this.get_enMap();
    }
}

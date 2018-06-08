package BP.WF.Template;

import org.apache.commons.lang.StringUtils;

import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.En.UAC;
import BP.Sys.MapDatas;

public class FrmNodeExt extends EntityMyPK{
	
	 public String getFK_Frm()
     {
             return this.GetValStrByKey(FrmNodeAttr.FK_Frm);
     }
     public int getFK_Node()
     {
             return this.GetValIntByKey(FrmNodeAttr.FK_Node);
     }
     public int getFK_Flow(){
    	 return this.GetValIntByKey(FrmNodeAttr.FK_Flow);
     }
	/**
	 * UI界面上的访问控制
	 */
    
    @Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenAll();
		uac.IsInsert=false;
		return uac;
	}
    /**
     * 节点表单
     */
    public FrmNodeExt() { }
    /**
     * 节点表单
     * @param mypk
     * @throws Exception 
     */
    public FrmNodeExt(String mypk) throws Exception
    {
    	this.setMyPK(mypk);
		this.Retrieve();
    }
     
    /// <summary>
    /// 重写基类方法
    /// </summary>
    @Override
    public BP.En.Map getEnMap() {
    {
            if (this.get_enMap() != null)
                return this.get_enMap();

            Map map = new Map("WF_FrmNode", "节点表单");

            map.AddMyPK();

            map.AddDDLEntities(FrmNodeAttr.FK_Frm, null, "表单", new MapDatas(), false);
            map.AddTBInt(FrmNodeAttr.FK_Node, 0, "节点ID", true, true);
            map.AddTBInt(FrmNodeAttr.FK_Flow, 0, "流程ID", true, true);

            map.AddBoolean(FrmNodeAttr.IsPrint, false, "是否可以打印", true, true);
            map.AddBoolean(FrmNodeAttr.IsEnableLoadData, false, "是否启用装载填充事件", true, true);
            map.AddDDLSysEnum(FrmNodeAttr.FrmSln, 0, "表单控制方案", true, true, FrmNodeAttr.FrmSln,
                "@0=默认方案@1=只读方案@2=自定义方案");

            map.AddDDLSysEnum(FrmNodeAttr.WhoIsPK, 0, "谁是主键?", true, true);

            //显示的
            map.AddTBInt(FrmNodeAttr.Idx, 0, "顺序号(显示的顺序)", true, false);

            //add 2016.3.25.
            map.AddBoolean(FrmNodeAttr.Is1ToN, false, "是否1变N？(分流节点有效)", true, true, true);
            map.AddTBString(FrmNodeAttr.HuiZong, null, "子线程要汇总的数据表(子线程节点)", true, false, 0, 300, 20);

            //模版文件，对于office表单有效.
            map.AddTBString(FrmNodeAttr.TempleteFile, null, "模版文件", true, false, 0, 500, 20);

            //是否显示
            map.AddTBString(FrmNodeAttr.GuanJianZiDuan, null, "关键字段", true, false, 0, 20, 20);

           // #region 表单启用规则.
            map.AddDDLSysEnum(FrmNodeAttr.FrmEnableRole, 0, "表单启用规则?", true, true);
            map.AddTBStringDoc(FrmNodeAttr.FrmEnableExp, null, "启用的表达式", true, false, true);
            //#endregion 表单启用规则.
            

            RefMethod rm = new RefMethod();

            //rm.Title = "启用规则";
            //rm.ClassMethodName = this.ToString() + ".DoEnableRole()";
            //rm.RefMethodType = RefMethodType.RightFrameOpen;
            //map.AddRefMethod(rm);

            rm = new RefMethod();
            rm.Title = "自定义方案(权限设置)";
            rm.ClassMethodName = this.toString() + ".DoSelfSln()";
            rm.refMethodType=RefMethodType.LinkeWinOpen;
            map.AddRefMethod(rm);
            
            rm = new RefMethod();
            rm.Title = "字段权限";
            rm.ClassMethodName = this.toString() + ".DoFields()";
            rm.refMethodType = RefMethodType.LinkeWinOpen;
            map.AddRefMethod(rm);
            
            rm = new RefMethod();
            rm.Title = "从表权限";
            rm.ClassMethodName = this.toString() + ".DoDtls()";
            rm.refMethodType = RefMethodType.LinkeWinOpen;
            map.AddRefMethod(rm);

            rm = new RefMethod();
            rm.Title = "附件权限";
            rm.ClassMethodName = this.toString() + ".DoAths()";
            rm.refMethodType = RefMethodType.LinkeWinOpen;
            map.AddRefMethod(rm);

            rm = new RefMethod();
            rm.Title = "从其他节点Copy权限设置";
            rm.ClassMethodName = this.toString() + ".DoCopyFromNode()";
            rm.refMethodType = RefMethodType.LinkeWinOpen;
            map.AddRefMethod(rm);

            this.set_enMap(map);
            return this.get_enMap();
        }
    }
    public final String DoDtls() throws Exception
	{
    	if(!StringUtils.isEmpty(this.getMyPK())){
    		this.setMyPK(this.getMyPK());
    		this.Retrieve();
    	}
		return "../../Admin/Sln/Dtls.htm?FK_MapData=" + this.getFK_Frm() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&DoType=Field";
	}
	public final String DoFields() throws Exception
	{
		if(!StringUtils.isEmpty(this.getMyPK())){
    		this.setMyPK(this.getMyPK());
    		this.Retrieve();
    	}
		return "../../Admin/Sln/Fields.htm?FK_MapData=" + this.getFK_Frm() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&DoType=Field";
	}
	public final String DoAths() throws Exception
	{
		if(!StringUtils.isEmpty(this.getMyPK())){
    		this.setMyPK(this.getMyPK());
    		this.Retrieve();
    	}
		return "../../Admin/Sln/Aths.htm?FK_MapData=" + this.getFK_Frm() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&DoType=Field";
	}

	public final String DoCopyFromNode() throws Exception
	{
		if(!StringUtils.isEmpty(this.getMyPK())){
    		this.setMyPK(this.getMyPK());
    		this.Retrieve();
    	}
		return "../../Admin/Sln/Aths.htm?FK_MapData=" + this.getFK_Frm() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&DoType=Field";
	}
	public final String DoSelfSln() throws Exception
	{
		if(!StringUtils.isEmpty(this.getMyPK())){
    		this.setMyPK(this.getMyPK());
    		this.Retrieve();
    	}
		return "../../Admin/Sln/Sln.htm?FK_MapData=" + this.getFK_Frm() + "&FK_Node=" + this.getFK_Node() + "&FK_Flow=" + this.getFK_Flow() + "&DoType=Field";
	}
	public final String DoEnableRole() throws Exception
	{
		if(!StringUtils.isEmpty(this.getMyPK())){
    		this.setMyPK(this.getMyPK());
    		this.Retrieve();
    	}
		return "../../Admin/AttrNode/BindFrmsFrmEnableRole.htm?MyPK=" + this.getMyPK();
	}
}

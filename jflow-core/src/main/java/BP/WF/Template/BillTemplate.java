package BP.WF.Template;

import java.io.File;

import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.UAC;
import BP.WF.Node;

/** 
 单据模板
*/
public class BillTemplate extends EntityNoName
{
	/** 
	 单据类型
	*/
	public final String getFK_BillType()
	{
		return this.GetValStringByKey(BillTemplateAttr.FK_BillType);
	}
	public final void setFK_BillType(String value)
	{
		this.SetValByKey(BillTemplateAttr.FK_BillType, value);
	}
	/** 
	 要替换的值
	*/
	public final String getReplaceVal()
	{
		return this.GetValStringByKey(BillTemplateAttr.ReplaceVal);
	}
	public final void setReplaceVal(String value)
	{
		this.SetValByKey(BillTemplateAttr.ReplaceVal, value);
	}
	/** 
	 UI界面上的访问控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	 编号
	*/
	public final String getNo()
	{
		String no = this.GetValStrByKey("No");
		no = no.replace("\n", "");
		no = no.replace(" ", "");
		return no;
	}
	public final void setNo(String value)
	{
		this.SetValByKey("No", value);
		this.SetValByKey(BillTemplateAttr.TempFilePath, value);
	}
	/** 
	 生成的单据类型
	*/
	public final BillFileType getHisBillFileType()
	{
		return BillFileType.forValue(this.GetValIntByKey(BillTemplateAttr.BillFileType));
	}
	public final void setHisBillFileType(int value)
	{
		this.SetValByKey(BillTemplateAttr.BillFileType, (int)value);
	}
	
	 public TemplateFileModel getTemplateFileModel()
     {
             return TemplateFileModel.forValue(this.GetValIntByKey(BillTemplateAttr.TemplateFileModel));
     }
     public void setTemplateFileModel(TemplateFileModel value){
         this.SetValByKey(BillTemplateAttr.TemplateFileModel, value.getValue());
     }
	 
    /// <summary>
    /// 生成的单据打开方式
    /// </summary>
    public final BillOpenModel getBillOpenModel()
    {
    		return BillOpenModel.forValue(this.GetValIntByKey(BillTemplateAttr.BillOpenModel));
    }
    public final void setBillOpenModel(int value)
    {
    	 this.SetValByKey(BillTemplateAttr.BillOpenModel, (int)value);
    }
    //是否生成二维码
    public final QRModel getQRModel(){
    	
    	return QRModel.forValue(this.GetValIntByKey(BillTemplateAttr.QRModel));
    }
    public final void setQRModel(int value){
    	
    	 this.SetValByKey(BillTemplateAttr.QRModel,(int)value);
    }
	/** 
	 打开的连接
	*/
	public final String getTempFilePath()
	{
		String s= this.GetValStrByKey(BillTemplateAttr.TempFilePath);
		if (s.equals("") || s == null)
		{
			return this.getNo();
		}
		return s;
	}
	public final void setTempFilePath(String value)
	{
		this.SetValByKey(BillTemplateAttr.TempFilePath, value);
	}
	/** 
	 节点名称
	 * @throws Exception 
	*/
	public final String getNodeName() throws Exception
	{
		Node nd = new Node(this.getNodeID());
		return nd.getName();
	}
	/** 
	 节点ID
	*/
	public final int getNodeID()
	{
		return this.GetValIntByKey(BillTemplateAttr.NodeID);
	}
	public final void setNodeID(int value)
	{
		this.SetValByKey(BillTemplateAttr.NodeID, value);
	}
	/** 
	 是否需要送达
	*/
	public final boolean getIsNeedSend_del()
	{
		return this.GetValBooleanByKey(BillTemplateAttr.IsNeedSend);
	}
	/** 
	 单据模板
	*/
	public BillTemplate()
	{
	}
	public BillTemplate(String no) throws Exception
	{
		super(no.replace("\n","").trim());
	}
	
	/// <summary>
    /// 获得Excel文件流
    /// </summary>
    /// <param name="oid"></param>
    /// <returns></returns>
    public byte[]  GenerTemplateFile()
    {
    	 byte[] bytes = BP.DA.DBAccess.GetByteFromDB(this.getEnMap().getPhysicsTable(), "No", this.getNo(), "DBFile");
         if (bytes != null)
             return bytes;

         //如果没有找到，就看看默认的文件是否有.
         String tempExcel = BP.Sys.SystemConfig.getPathOfDataUser() + "CyclostyleFile\\" + this.getNo() + ".rtf";
         if (new File(tempExcel).exists() == false)
             tempExcel = BP.Sys.SystemConfig.getPathOfDataUser() + "CyclostyleFile\\Word单据模版定义演示.docx";

         bytes = BP.DA.DataType.ConvertFileToByte(tempExcel);
         return bytes;
    }
    
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("WF_BillTemplate", "单据模板");

		map.Java_SetCodeStruct("6");

		map.AddTBStringPK(BillTemplateAttr.No, null, "No", true, false, 1, 190, 6);
		map.AddTBString(BillTemplateAttr.Name, null, "Name", true, false, 0, 200, 20);
		map.AddTBString(BillTemplateAttr.TempFilePath, null, "模板路径", true, false, 0, 200, 20);
		map.AddTBInt(BillTemplateAttr.NodeID, 0, "NodeID", true, false);

		map.AddDDLSysEnum(BillTemplateAttr.BillFileType, 0, "生成的文件类型", true, false, "BillFileType","@0=Word@1=PDF@2=Excel(未完成)@3=Html(未完成)@5=锐浪报表");

        map.AddDDLSysEnum(BillTemplateAttr.BillOpenModel, 0, "生成的文件打开方式", true, false,
                "BillOpenModel", "@0=下载本地@1=在线WebOffice打开");

        map.AddDDLSysEnum(BillTemplateAttr.QRModel, 0, "二维码生成方式", true, false,
               BillTemplateAttr.QRModel, "@0=不生成@1=生成二维码");
        
        map.AddDDLSysEnum(BillTemplateAttr.TemplateFileModel, 0, "模版模式", true, false,
                BillTemplateAttr.TemplateFileModel, "@0=rtf模版@1=vsto模式的word模版@2=vsto模式的excel模版");
        
		map.AddTBString(BillTemplateAttr.FK_BillType, null, "单据类型", true, false, 0, 4, 4);

		map.AddTBString("IDX", null, "IDX", false, false, 0, 200, 20);
		map.AddTBString(BillTemplateAttr.ExpField, null, "要排除的字段", false, false, 0, 800, 20);
		map.AddTBString(BillTemplateAttr.ReplaceVal, null, "要替换的值", false, false, 0, 3000, 20);
		this.set_enMap(map);
		return this.get_enMap();
	}
}
package bp.wf.template;
import bp.da.*;
import bp.en.*;
import bp.en.Map;

/** 
 子线程组件
*/
public class FrmThread extends Entity
{

		///属性
	public final String getNo() throws Exception
	{
		return "ND" + this.getNodeID();
	}
	public final void setNo(String value) throws Exception
	{
		String nodeID = value.replace("ND", "");
		this.setNodeID(Integer.parseInt(nodeID));
	}
	/** 
	 节点ID
	 * @throws Exception 
	*/
	public final int getNodeID()  throws Exception
	{
		return this.GetValIntByKey(NodeAttr.NodeID);
	}
	public final void setNodeID(int value) throws Exception
	{
		this.SetValByKey(NodeAttr.NodeID, value);
	}
	/** 
	 控件状态
	 * @throws Exception 
	*/
	public final FrmThreadSta getFrmThreadSta() throws Exception
	{
		return FrmThreadSta.forValue(this.GetValIntByKey(FrmThreadAttr.FrmThreadSta));
	}
	public final void setFrmThreadSta(FrmThreadSta value) throws Exception
	{
		this.SetValByKey(FrmThreadAttr.FrmThreadSta, value.getValue());
	}
	/** 
	 Y
	*/
	public final float getFrmThreadY() throws Exception
	{
		return this.GetValFloatByKey(FrmThreadAttr.FrmThread_Y);
	}
	public final void setFrmThreadY(float value) throws Exception
	{
		this.SetValByKey(FrmThreadAttr.FrmThread_Y, value);
	}
	/** 
	 X
	*/
	public final float getFrmThreadX() throws Exception
	{
		return this.GetValFloatByKey(FrmThreadAttr.FrmThread_X);
	}
	public final void setFrmThreadX(float value) throws Exception
	{
		this.SetValByKey(FrmThreadAttr.FrmThread_X, value);
	}
	/** 
	 W
	*/
	public final float getFrmThreadW() throws Exception
	{
		return this.GetValFloatByKey(FrmThreadAttr.FrmThread_W);
	}
	public final void setFrmThreadW(float value) throws Exception
	{
		this.SetValByKey(FrmThreadAttr.FrmThread_W, value);
	}
	public final String getFrmThreadWstr() throws Exception
	{
		if (this.getFrmThreadW() == 0)
		{
			return "100%";
		}
		return this.getFrmThreadW() + "px";
	}
	/** 
	 H
	*/
	public final float getFrmThreadH() throws Exception
	{
		return this.GetValFloatByKey(FrmThreadAttr.FrmThread_H);
	}
	public final void setFrmThreadH(float value) throws Exception
	{
		this.SetValByKey(FrmThreadAttr.FrmThread_H, value);
	}
	public final String getFrmThreadHstr() throws Exception
	{
		if (this.getFrmThreadH() == 0)
		{
			return "100%";
		}
		return this.getFrmThreadH() + "px";
	}
	/** 
	 节点名称.
	*/
	public final String getName() throws Exception
	{
		return this.GetValStringByKey("Name");
	}
	/** 
	 标签
	*/
	public final String getFrmThreadLab() throws Exception
	{
		return this.GetValStringByKey(FrmThreadAttr.FrmThreadLab);
	}


		///


		///构造方法
	/** 
	 控制
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsDelete = false;
		uac.IsInsert = false;
		return uac;
	}
	/** 
	 重写主键
	*/
	@Override
	public String getPK()
	{
		return "NodeID";
	}
	/** 
	 子线程组件
	*/
	public FrmThread()
	{
	}
	/** 
	 子线程组件
	 
	 @param no
	*/
	public FrmThread(String mapData) throws Exception
	{
		if (mapData.contains("ND") == false)
		{
			this.setFrmThreadSta(FrmThreadSta.Disable);
			return;
		}

		String mapdata = mapData.replace("ND", "");
		if (DataType.IsNumStr(mapdata) == false)
		{
			this.setFrmThreadSta(FrmThreadSta.Disable);
			return;
		}

		try
		{
			this.setNodeID(Integer.parseInt(mapdata));
		}
		catch (java.lang.Exception e)
		{
			return;
		}
		this.Retrieve();
	}
	/** 
	 子线程组件
	 
	 @param no
	*/
	public FrmThread(int nodeID) throws Exception
	{
		this.setNodeID(nodeID);
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Node", "子线程组件");

		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
		map.AddTBString(NodeAttr.Name, null, "节点名称", true, true, 0, 100, 10);

		map.AddTBString(FrmThreadAttr.FrmThreadLab, "子线程", "显示标签", true, false, 0, 200, 10, true);



			///此处变更了 NodeSheet类中的，map 描述该部分也要变更.

		map.AddDDLSysEnum(FrmThreadAttr.FrmThreadSta, getFrmThreadSta().Disable.getValue(), "组件状态", true, true, FrmThreadAttr.FrmThreadSta, "@0=禁用@1=启用");

		map.AddTBFloat(FrmThreadAttr.FrmThread_X, 5, "位置X", true, false);
		map.AddTBFloat(FrmThreadAttr.FrmThread_Y, 5, "位置Y", true, false);

		map.AddTBFloat(FrmThreadAttr.FrmThread_H, 300, "高度", true, false);
		map.AddTBFloat(FrmThreadAttr.FrmThread_W, 400, "宽度", true, false);


			/// 此处变更了 NodeSheet类中的，map 描述该部分也要变更.

		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		return super.beforeUpdateInsertAction();
	}

		///
}
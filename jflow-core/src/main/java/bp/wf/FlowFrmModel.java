package bp.wf;

import bp.*;

/** 
 流程表单类型
*/
public enum FlowFrmModel
{
	/** 
	 完整版-2019年更早版本
	*/
	Ver2019Earlier(0),
	/** 
	 传统(经典-推荐)模式(绑定表单库的表单)
	*/
	TraditionModel(1),
	/** 
	 表单树模式
	*/
	FrmTreeModel(2),
	/** 
	 嵌入模式
	*/
	EmbeddedModel(3),
	/** 
	 SDK自定义表单模式
	*/
	SDKFrm(4);

	public static final int SIZE = java.lang.Integer.SIZE;

	private int intValue;
	private static java.util.HashMap<Integer, FlowFrmModel> mappings;
	private static java.util.HashMap<Integer, FlowFrmModel> getMappings()  {
		if (mappings == null)
		{
			synchronized (FlowFrmModel.class)
			{
				if (mappings == null)
				{
					mappings = new java.util.HashMap<Integer, FlowFrmModel>();
				}
			}
		}
		return mappings;
	}

	private FlowFrmModel(int value)
	{intValue = value;
		getMappings().put(value, this);
	}

	public int getValue()  {
		return intValue;
	}

	public static FlowFrmModel forValue(int value)
	{return getMappings().get(value);
	}
}
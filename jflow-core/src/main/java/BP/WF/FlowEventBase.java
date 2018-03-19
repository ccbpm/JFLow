package BP.WF;

import java.util.ArrayList;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;

/** 
 流程事件基类
 0,集成该基类的子类,可以重写事件的方法与基类交互.
 1,一个子类必须与一个流程模版绑定.
 2,基类里有很多流程运行过程中的变量，这些变量可以辅助开发者在编写复杂的业务逻辑的时候使用.
 3,该基类有一个子类模版，位于:\CCFlow\WF\Admin\AttrFlow\F001Templepte.cs .
 
*/
public abstract class FlowEventBase
{

		///#region 要求子类强制重写的属性.
	/** 
	 流程编号/流程标记.
	 该参数用于说明要把此事件注册到那一个流程模版上.
	 
	*/
	public abstract String getFlowMark();

		///#endregion 要求子类重写的属性.


		//内部变量(流程在运行的时候触发各类事件，子类可以访问这些属性来获取引擎内部的信息).
	/** 
	 发送对象
	 
	*/
	public SendReturnObjs SendReturnObjs = null;
	/** 
	 实体，一般是工作实体
	 
	*/
	public Entity HisEn = null;
	/** 
	 当前节点
	 
	*/
	public Node HisNode = null;
	/** 
	 参数对象.
	 
	*/
	private Row _SysPara = null;
	/** 
	 参数
	 
	*/
	public final Row getSysPara()
	{
		if (_SysPara == null)
		{
			_SysPara = new Row();
		}
		return _SysPara;
	}
	public final void setSysPara(Row value)
	{
		_SysPara = value;
	}
	/** 
	 成功信息
	 
	*/
	public String SucessInfo = null;

		///#endregion 属性/内部变量(流程在运行的时候触发各类事件，子类可以访问这些属性来获取引擎内部的信息).


		///#region 在发送前的事件里可以改变参数.
	/** 
	 要跳转的节点.(开发人员可以设置该参数,改变发送到的节点转向.)
	 
	*/
	public Node JumpToNode = null;
	/** 
	 接受人, (开发人员可以设置该参数,改变接受人的范围.)
	 
	*/
	public String JumpToEmps = null;

		///#endregion 在发送前的事件里可以改变参数


		///#region 系统参数
	/** 
	 表单ID
	 
	*/
	public final String getFK_Mapdata()
	{
		return this.GetValStr("FK_MapData");
	}

		///#endregion


		///#region 常用属性.
	/** 
	 工作ID
	 
	*/
	public final int getOID()
	{
		return this.GetValInt("OID");
	}
	/** 
	 工作ID
	 
	*/
	public final long getWorkID()
	{
		if (this.getOID() == 0)
		{
			return this.GetValInt64("WorkID"); //有可能开始节点的WorkID=0
		}
		return this.getOID();
	}
	/** 
	 流程ID
	 
	*/
	public final long getFID()
	{
		return this.GetValInt64("FID");
	}
	/** 
	 传过来的WorkIDs集合，子流程.
	 
	*/
	public final String getWorkIDs()
	{
		return this.GetValStr("WorkIDs");
	}
	/** 
	 编号集合s
	 
	*/
	public final String getNos()
	{
		return this.GetValStr("Nos");
	}

		///#endregion 常用属性.


		///#region 获取参数方法
	/** 
	 事件参数
	 
	 @param key 时间字段
	 @return 根据字段返回一个时间,如果为Null,或者不存在就抛出异常.
	*/
	public final java.util.Date GetValDateTime(String key)
	{
		try
		{
			String str = this.getSysPara().GetValByKey(key).toString();
			return DataType.ParseSysDateTime2DateTime(str);
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@流程事件实体在获取参数期间出现错误，请确认字段(" + key + ")是否拼写正确,技术信息:" + ex.getMessage());
		}
	}
	/** 
	 获取字符串参数
	 
	 @param key key
	 @return 如果为Null,或者不存在就抛出异常
	*/
	public final String GetValStr(String key)
	{
		try
		{
			return this.getSysPara().GetValByKey(key).toString();
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@流程事件实体在获取参数期间出现错误，请确认字段(" + key + ")是否拼写正确,技术信息:" + ex.getMessage());
		}
	}
	/** 
	 获取Int64的数值
	 
	 @param key 键值
	 @return 如果为Null,或者不存在就抛出异常
	*/
	public final long GetValInt64(String key)
	{
		return Long.parseLong(this.GetValStr(key));
	}
	/** 
	 获取int的数值
	 
	 @param key 键值
	 @return 如果为Null,或者不存在就抛出异常
	*/
	public final int GetValInt(String key)
	{
		return Integer.parseInt(this.GetValStr(key));
	}
	/** 
	 获取Boolen值
	 
	 @param key 字段
	 @return 如果为Null,或者不存在就抛出异常
	*/
	public final boolean GetValBoolen(String key)
	{
		if (Integer.parseInt(this.GetValStr(key)) == 0)
		{
			return false;
		}
		return true;
	}
	/** 
	 获取decimal的数值
	 
	 @param key 字段
	 @return 如果为Null,或者不存在就抛出异常
	*/
	public final java.math.BigDecimal GetValDecimal(String key)
	{
		return new java.math.BigDecimal(this.GetValStr(key));
	}

		///#endregion 获取参数方法


		
	/** 
	 流程事件基类
	 
	*/
	public FlowEventBase()
	{
	}

		///#endregion 构造方法


		///#region 节点表单事件
	public String FrmLoadAfter()
	{
		return null;
	}
	public String FrmLoadBefore()
	{
		return null;
	}

		///#endregion


		///#region 要求子类重写的方法(流程事件).
	/** 
	 当创建WorkID的时候
	 
	 @return 创建WorkID所执行的操作
	*/
	public String FlowOnCreateWorkID()
	{
		return null;
	}
	/** 
	 流程完成前
	 
	 @return 返回null，不提示信息，返回string提示结束信息,抛出异常就阻止流程删除.
	*/
	public String FlowOverBefore()
	{
		return null;
	}
	/** 
	 流程结束后
	 
	 @return 返回null，不提示信息，返回string提示结束信息,抛出异常不处理。
	*/
	public String FlowOverAfter()
	{
		return null;
	}
	/** 
	 流程删除前
	 
	 @return 返回null,不提示信息,返回信息，提示删除警告/提示信息, 抛出异常阻止删除操作.
	*/
	public String BeforeFlowDel()
	{
		return null;
	}
	/** 
	 流程删除后
	 
	 @return 返回null,不提示信息,返回信息，提示删除警告/提示信息, 抛出异常不处理.
	*/
	public String AfterFlowDel()
	{
		return null;
	}

		///#endregion 要求子类重写的方法(流程事件).


		///#region 要求子类重写的方法(节点事件).
	/** 
	 保存后
	 
	*/
	public String SaveAfter()
	{
		return null;
	}
	/** 
	 保存前
	 
	*/
	public String SaveBefore()
	{
		return null;
	}
	/** 
	 创建工作ID后的事件
	 
	 @return 
	*/
	public String CreateWorkID()
	{
		return null;
	}
	/** 
	发送前
	 
	*/
	public String SendWhen()
	{
		return null;
	}
	/** 
	 发送成功时
	 
	*/
	public String SendSuccess()
	{
		return null;
	}
	/** 
	 发送失败
	 
	 @return 
	*/
	public String SendError()
	{
		return null;
	}
	/** 
	 退回前
	 
	 @return 
	*/
	public String ReturnBefore()
	{
		return null;
	}
	/** 
	 退后后
	 
	 @return 
	*/
	public String ReturnAfter()
	{
		return null;
	}
	/** 
	 撤销之前
	 
	 @return 
	*/
	public String UndoneBefore()
	{
		return null;
	}
	/** 
	 撤销之后
	 
	 @return 
	*/
	public String UndoneAfter()
	{
		return null;
	}
	/** 
	 移交后
	 
	 @return 
	*/
	public String ShiftAfter()
	{
		return null;
	}
	/** 
	 加签后
	 
	 @return 
	*/
	public String AskerAfter()
	{
		return null;
	}
	/** 
	 加签答复后
	 
	 @return 
	*/
	public String AskerReAfter()
	{
		return null;
	}
	/** 
	 队列节点发送后
	 
	 @return 
	*/
	public String QueueSendAfter()
	{
		return null;
	}

		///#endregion 要求子类重写的方法(节点事件).


		///#region 基类方法.
	/** 
	 执行事件
	 
	 @param eventType 事件类型
	 @param en 实体参数
	*/
	public final String DoIt(String eventType, Node currNode, Entity en, String atPara, Node jumpToNode, String jumpToEmps)
	{
		this.HisEn = en;
		this.HisNode = currNode;

		//用于代码改变跳转规则,方向条件规则.
		this.JumpToEmps = jumpToEmps;
		this.JumpToNode = jumpToNode;	


			///#region 处理参数.
		Row r = en.getRow();
		try
		{
			//系统参数.
			r.put("FK_MapData", en.getClassID());
		}
		catch (java.lang.Exception e)
		{
			r.put("FK_MapData", en.getClassID());
		}

		if (atPara != null)
		{
			AtPara ap = new AtPara(atPara);
			for (String s : ap.getHisHT().keySet())
			{
				try
				{
					r.put(s, ap.GetValStrByKey(s));
				} catch (java.lang.Exception e2)
				{
					// r[s] = ap.GetValStrByKey(s);
					r.put(s, ap.GetValStrByKey(s));
				}
			}
		}

		if (SystemConfig.getIsBSsystem() == true)
		{
			/*如果是bs系统, 就加入外部url的变量.*/
			ArrayList<String> keys = BP.Sys.Glo.getQueryStringKeys();
			for (String key : keys)
			{
				r.put(key, BP.Sys.Glo.getRequest().getParameter(key));
			}
			// String queryStr = Glo.getRequest().getQueryString()
			// .replace("?", "");
			// String[] params = queryStr.split("&");
			// for (int i = 0; i < params.length; i++) {
			// String[] key_value = params[i].split("=");
			// if(key_value.length == 1){
			// r.put(key_value[0], "");
			// }else{
			// r.put(key_value[0], key_value[1]);
			// }
			// }
		}
		this.setSysPara(r);
		if (eventType.equals(EventListOfNode.CreateWorkID)) // 节点表单事件。
		{
				return this.CreateWorkID();
		}
		else if (eventType.equals(EventListOfNode.FrmLoadAfter)) // 节点表单事件。
		{
				return this.FrmLoadAfter();
		}
		else if (eventType.equals(EventListOfNode.FrmLoadBefore)) // 节点表单事件。
		{
				return this.FrmLoadBefore();
		}
		else if (eventType.equals(EventListOfNode.SaveAfter)) // 节点事件 保存后。
		{
				return this.SaveAfter();
		}
		else if (eventType.equals(EventListOfNode.SaveBefore)) // 节点事件 - 保存前.。
		{
				return this.SaveBefore();
		}
		else if (eventType.equals(EventListOfNode.SendWhen)) // 节点事件 - 发送前。
		{
				return this.SendWhen();
		}
		else if (eventType.equals(EventListOfNode.SendSuccess)) // 节点事件 - 发送成功时。
		{
				return this.SendSuccess();
		}
		else if (eventType.equals(EventListOfNode.SendError)) // 节点事件 - 发送失败。
		{
				return this.SendError();
		}
		else if (eventType.equals(EventListOfNode.ReturnBefore)) // 节点事件 - 退回前。
		{
				return this.ReturnBefore();
		}
		else if (eventType.equals(EventListOfNode.ReturnAfter)) // 节点事件 - 退回后。
		{
				return this.ReturnAfter();
		}
		else if (eventType.equals(EventListOfNode.UndoneBefore)) // 节点事件 - 撤销前。
		{
				return this.UndoneBefore();
		}
		else if (eventType.equals(EventListOfNode.UndoneAfter)) // 节点事件 - 撤销后。
		{
				return this.UndoneAfter();
		}
		else if (eventType.equals(EventListOfNode.ShitAfter)) // 节点事件-移交后
		{
				return this.ShiftAfter();
		}
		else if (eventType.equals(EventListOfNode.AskerAfter)) //节点事件 加签后
		{
				return this.AskerAfter();
		}
		else if (eventType.equals(EventListOfNode.AskerReAfter)) //节点事件加签回复后
		{
				return this.FlowOverBefore();
		}
		else if (eventType.equals(EventListOfNode.QueueSendAfter)) //队列节点发送后
		{
				return this.AskerReAfter();

		}
		else if (eventType.equals(EventListOfNode.FlowOnCreateWorkID)) // 流程事件 -------------------------------------------。
		{
				return this.FlowOnCreateWorkID();
		}
		else if (eventType.equals(EventListOfNode.FlowOverBefore)) // 流程结束前.。
		{
				return this.FlowOverBefore();
		}
		else if (eventType.equals(EventListOfNode.FlowOverAfter)) // 流程结束后。
		{
				return this.FlowOverAfter();
		}
		else if (eventType.equals(EventListOfNode.BeforeFlowDel)) // 流程删除前。
		{
				return this.BeforeFlowDel();
		}
		else if (eventType.equals(EventListOfNode.AfterFlowDel)) // 删除后.
		{
				return this.AfterFlowDel();
		}
		else
		{
				throw new RuntimeException("@没有判断的事件类型:" + eventType);
		}
	}
}
package bp.sys;

import bp.da.*;
import bp.difference.ContextHolderUtils;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.web.*;
import bp.sys.*;
import bp.*;
import java.time.*;
import java.util.Date;
import java.util.Enumeration;
import java.math.*;

/** 
 表单事件基类
*/
public abstract class FrmEventBase
{

		///要求子类强制重写的属性.
	/** 
	 表单编号
	 该参数用于说明要把此事件注册到那一个表单模版上.
	*/
	public abstract String getFrmNo();

		/// 要求子类重写的属性.


		///常用属性.
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
	public final long getWorkID() throws Exception
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
	public final long getFID() throws Exception
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
	/** 
	  行数据
	*/
	private Row Row;
	public final Row getRow()
	{
		return Row;
	}
	public final void setRow(Row value)
	{
		Row = value;
	}

		/// 常用属性.


		///数据字段的方法
	/** 
	 时间参数
	 
	 @param key 时间字段
	 @return 根据字段返回一个时间,如果为Null,或者不存在就抛出异常.
	*/
	public final Date GetValDateTime(String key)
	{
		try
		{
			String str = this.getRow().GetValByKey(key).toString();
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
			return this.getRow().GetValByKey(key).toString();
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
	public final BigDecimal GetValDecimal(String key)
	{
		return new BigDecimal(this.GetValStr(key));
	}

		/// 获取参数方法


		///构造方法
	/** 
	 表单事件基类
	*/
	public FrmEventBase()
	{
	}

		/// 构造方法


		///节点表单事件
	public String FrmLoadAfter()
	{
		return null;
	}
	public String FrmLoadBefore()
	{
		return null;
	}

		///


		///要求子类重写的方法(节点事件).
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
	 创建OID后的事件
	 
	 @return 
	*/
	public String CreateOID()
	{
		return null;
	}

		/// 要求子类重写的方法(节点事件).


		///基类方法.
	/** 
	 执行事件
	 
	 @param eventType 事件类型
	 @param en 实体参数
	 * @throws Exception 
	*/
	public final String DoIt(String eventType, Entity en, Row row, String atPara) throws Exception
	{
		this.setRow(row);


			///处理参数.
		Row r = en.getRow();
		try
		{
			//系统参数.
			this.getRow().put("FK_MapData", en.getClassID());
		}
		catch (java.lang.Exception e)
		{
			this.getRow().put("FK_MapData", en.getClassID());
		}

		if (atPara != null)
		{
			AtPara ap = new AtPara(atPara);
			for (String s : ap.getHisHT().keySet())
			{
				try
				{
					this.getRow().put(s, ap.GetValStrByKey(s));
				}
				catch (java.lang.Exception e2)
				{
					this.getRow().put(s, ap.GetValStrByKey(s));
				}
			}
		}

		if (SystemConfig.getIsBSsystem() == true)
		{
			/*如果是bs系统, 就加入外部url的变量.*/
			Enumeration enu = ContextHolderUtils.getRequest().getParameterNames();
			while (enu.hasMoreElements()) {
				String key = (String) enu.nextElement();
				String val = ContextHolderUtils.getRequest().getParameter(key);
				try
				{
					this.getRow().put(key, val);
				}
				catch (java.lang.Exception e3)
				{
					this.getRow().put(key, val);
				}
			}
			
			
		}

			/// 处理参数.


			///执行事件.
		switch (eventType)
		{
			case EventListFrm.CreateOID: // 节点表单事件。
				return this.CreateOID();
			case EventListFrm.FrmLoadAfter: // 节点表单事件。
				return this.FrmLoadAfter();
			case EventListFrm.FrmLoadBefore: // 节点表单事件。
				return this.FrmLoadBefore();
			case EventListFrm.SaveAfter: // 节点事件 保存后。
				return this.SaveAfter();
			case EventListFrm.SaveBefore: // 节点事件 - 保存前.。
				return this.SaveBefore();
			default:
				throw new RuntimeException("@没有判断的事件类型:" + eventType);
		}

	}

		/// 基类方法.
}
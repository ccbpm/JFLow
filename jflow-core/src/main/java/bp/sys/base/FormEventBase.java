package bp.sys.base;

import bp.da.*;
import bp.en.*; import bp.en.Map;
import bp.web.*;
import bp.sys.*;
import bp.difference.*;
import bp.*;
import bp.sys.*;
import java.time.*;
import java.math.*;
import java.util.Date;

/** 
 表单事件基类
 0,集成该基类的子类,可以重写事件的方法与基类交互.
 1,一个子类必须与一个表单模版绑定.
 2,基类里有很多表单运行过程中的变量，这些变量可以辅助开发者在编写复杂的业务逻辑的时候使用.
 3,该基类有一个子类模版，位于:\CCForm\WF\Admin\AttrForm\F001Templepte.cs .
*/
public abstract class FormEventBase
{

		///#region 要求子类强制重写的属性.
	/** 
	 表单编号/表单标记.
	 该参数用于说明要把此事件注册到那一个表单模版上.
	*/
	public abstract String getFormMark();

		///#endregion 要求子类重写的属性.


		///#region 属性/内部变量(表单在运行的时候触发各类事件，子类可以访问这些属性来获取引擎内部的信息).
	/** 
	 实体，一般是工作实体
	*/
	public Entity HisEn = null;
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

		///#endregion 属性/内部变量(表单在运行的时候触发各类事件，子类可以访问这些属性来获取引擎内部的信息).


		///#region 系统参数
	/** 
	 表单ID
	*/
	public final String getFKMapdata()  {
		return this.GetValStr("FK_MapData");
	}

		///#endregion


		///#region 常用属性.
	/** 
	 工作ID
	*/
	public final int getOID()  {
		return this.GetValInt("OID");
	}

		///#endregion 常用属性.


		///#region 获取参数方法
	/** 
	 事件参数
	 
	 @param key 时间字段
	 @return 根据字段返回一个时间,如果为Null,或者不存在就抛出异常.
	*/
	public final Date GetValDateTime(String key)
	{
		try
		{
			String str = this.getSysPara().GetValByKey(key).toString();
			return DataType.ParseSysDateTime2DateTime(str);
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@表单事件实体在获取参数期间出现错误，请确认字段(" + key + ")是否拼写正确,技术信息:" + ex.getMessage());
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
			throw new RuntimeException("@表单事件实体在获取参数期间出现错误，请确认字段(" + key + ")是否拼写正确,技术信息:" + ex.getMessage());
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

		///#endregion 获取参数方法


		///#region 构造方法
	/** 
	 表单事件基类
	*/
	public FormEventBase()
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


		///#region 要求子类重写的方法(表单事件).
	/** 
	 表单删除前
	 
	 @return 返回null,不提示信息,返回信息，提示删除警告/提示信息, 抛出异常阻止删除操作.
	*/
	public String BeforeFormDel()
	{
		return null;
	}
	/** 
	 表单删除后
	 
	 @return 返回null,不提示信息,返回信息，提示删除警告/提示信息, 抛出异常不处理.
	*/
	public String AfterFormDel()
	{
		return null;
	}

		///#endregion 要求子类重写的方法(表单事件).


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
	 附件上传前
	*/
	public String AthUploadeBefore()
	{
		return null;
	}
	/** 
	 上传后
	*/
	public String AthUploadeAfter()
	{
		return null;
	}
	/** 
	 从表保存前
	*/
	public String DtlRowSaveBefore()
	{
		return null;
	}
	/** 
	 从表保存后
	*/
	public String DtlRowSaveAfter()
	{
		return null;
	}
	/** 
	 创建工作ID后的事件
	 
	 @return 
	*/
	public String CreateOID()
	{
		return null;
	}

		///#endregion 要求子类重写的方法(节点事件).


		///#region 基类方法.

	/** 
	 执行事件
	 
	 @param EventSource
	 @param en
	 @param atPara
	 @return 
	*/
	public final String DoIt(String EventSource, Entity en, String atPara) throws Exception {
		this.HisEn = en;


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
				}
				catch (java.lang.Exception e2)
				{
					r.put(s, ap.GetValStrByKey(s));
				}
			}
		}

		if (SystemConfig.isBSsystem() == true)
		{
			/*如果是bs系统, 就加入外部url的变量.*/
			for (String key : ContextHolderUtils.getRequest().getParameterMap().keySet())
			{
				String val = ContextHolderUtils.getRequest().getParameter(key);
				try
				{
					r.put(key, val);
				}
				catch (java.lang.Exception e3)
				{
					r.put(key, val);
				}
			}
		}
		this.setSysPara(r);

			///#endregion 处理参数.


			///#region 执行事件.
		switch (EventSource)
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

			case EventListFrm.AthUploadeBefore: // 附件上传前.。
				return this.AthUploadeBefore();
			case EventListFrm.AthUploadeAfter: // 附件上传后.。
				return this.AthUploadeAfter();

			case EventListFrm.DtlRowSaveBefore: // 从表-保存前.。
				return this.DtlRowSaveBefore();
			case EventListFrm.DtlRowSaveAfter: // 从表-保存后.。
				return this.DtlRowSaveAfter();
			default:
				throw new RuntimeException("@没有判断的表单事件类型:" + EventSource);
		}

			///#endregion 执行事件.
	}

		///#endregion 基类方法.
}

package BP.En;

import BP.DA.*;
import BP.Web.Controls.*;
import java.io.*;

/** 
 RefMethod 的摘要说明。
*/
public class RefMethod
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 与窗口有关的方法.
	/** 
	 高度
	*/
	public int Height = 600;
	/** 
	 宽度
	*/
	public int Width = 800;
	public String Target = "_B123";
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 功能
	*/
	public RefMethodType RefMethodType = RefMethodType.Func;
	/** 
	 相关字段
	*/
	public String RefAttrKey = null;
	/** 
	 连接标签
	*/
	public String RefAttrLinkLabel = null;
	/** 
	 分组名称
	*/
	public String GroupName = null;
	/** 
	 是否显示在Ens中?
	*/
	public boolean IsForEns = false;
	/** 
	 相关功能
	*/
	public RefMethod()
	{
	}
   /** 
	 相关功能
	
	@param groupName
   */
	public RefMethod(String groupName)
	{
		this.GroupName = groupName;
	}
	/** 
	 参数
	*/
	private Attrs _HisAttrs = null;
	/** 
	 参数
	*/
	public final Attrs getHisAttrs()
	{
		if (_HisAttrs == null)
		{
			_HisAttrs = new Attrs();
		}
		return _HisAttrs;
	}
	public final void setHisAttrs(Attrs value)
	{
		_HisAttrs = value;
	}
	/** 
	 索引位置，用它区分实体.
	*/
	public int Index = 0;
	/** 
	 是否显示
	*/
	public boolean Visable = true;
	/** 
	 是否可以批处理
	*/
	public boolean IsCanBatch = false;
	/** 
	 标题
	*/
	public String Title = null;
	/** 
	 操作前提示信息
	*/
	public String Warning = null;
	/** 
	 连接
	*/
	public String ClassMethodName = null;
	/** 
	 图标
	*/
	public String Icon = null;
	public final String GetIcon(String path)
	{
		if (this.Icon == null)
		{
			return null;
			return "<img src='/WF/Img/Btn/Do.gif'  border=0 />";
		}
		else
		{
			String url = path + Icon;
			url = url.replace("//", "/");
			return "<img src='" + url + "'  border=0 />";
		}
	}
	/** 
	 提示信息
	*/
	public String ToolTip = null;

	/** 
	 PKVal
	*/
	public Object PKVal = "PKVal";
	/** 
	 
	*/
	public Entity HisEn = null;
	/** 
	 实体PK
	*/
	public String[] PKs = "".split("[.]", -1);
	/** 
	 执行
	 
	 @param paras
	 @return 
	*/
	public final Object Do(Object[] paras)
	{
		String str = tangible.StringHelper.trim(this.ClassMethodName, ' ', ';', '.');
		int pos = str.lastIndexOf(".");
		String clas = str.substring(0, pos);
		String meth = tangible.StringHelper.trim(str.substring(pos, str.length()), '.', ' ', '(', ')');
		if (this.HisEn == null)
		{
			this.HisEn = BP.En.ClassFactory.GetEn(clas);
			Attrs attrs = this.HisEn.getEnMap().getAttrs();
		}

		java.lang.Class tp = this.HisEn.getClass();
		java.lang.reflect.Method mp = tp.getMethod(meth);
		if (mp == null)
		{
			throw new RuntimeException("@对象实例[" + tp.FullName + "]中没有找到方法[" + meth + "]！");
		}

		try
		{
			return mp.Invoke(this.HisEn, paras); //调用由此 MethodInfo 实例反射的方法或构造函数。
		}
		catch (System.Reflection.TargetException ex)
		{
			String strs = "";
			if (paras == null)
			{
				throw new RuntimeException(ex.getMessage());
			}
			else
			{
				for (Object obj : paras)
				{
					strs += "para= " + obj.toString() + " type=" + obj.getClass().toString() + "\n<br>";
				}
			}
			throw new RuntimeException(ex.getMessage() + "  more info:" + strs);
		}
	}
}
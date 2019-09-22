package BP.En;

import java.util.ArrayList;
import java.util.List;

/** 
 
 
*/
public class EnDtls extends ArrayList<EnDtl>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 是不是包含className
	 * 
	 * @param className
	 * @return
	 */
	public final boolean IsContainKey(String className)
	{
		for (EnDtl ed : this)
		{
			if (ed.getEnsName().equals(className))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 加入
	 * 
	 * @param attr
	 *            attr
	 */
	public final void Add(EnDtl en)
	{
		if (this.IsExits(en))
		{
			return;
		}
		this.add(en);
		/*
		 * warning this.add(en);
		 */
	}
	
	/**
	 * 是不是存在集合里面
	 * 
	 * @param en
	 *            要检查的EnDtl
	 * @return true/false
	 */
	public final boolean IsExits(EnDtl en)
	{
		for (EnDtl dtl : this)
		{
			if (dtl.getEns() == en.getEns())
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 通过一个key 得到它的属性值。
	 * 
	 * @param key
	 *            key
	 * @return EnDtl
	 */
	public final EnDtl GetEnDtlByKey(String key)
	{
		for (EnDtl dtl : this)
		{
			if (dtl.getRefKey().equals(key))
			{
				return dtl;
			}
		}
		throw new RuntimeException("@没有找到 key=[" + key + "]的属性，请检查map文件。");
	}
	
	/**
	 * 根据索引访问集合内的元素Attr。
	 */
	public final EnDtl getItem(int index)
	{
		return (EnDtl) this.get(index);
		/*
		 * warning return (EnDtl)this.get(index);
		 */
	}
	
	/**
	 * className
	 * 
	 * @param className
	 *            类名称
	 * @return
	 */
	public final EnDtl GetEnDtlByEnsName(String className)
	{
		for (EnDtl en : this)
		{
			if (en.getEnsName().equals(className))
			{
				return en;
			}
		}
		throw new RuntimeException("@没有找到他的明细:" + className);
	}
	
	public List<EnDtl> ToJavaList()
	{
		return (List<EnDtl>)(Object)this;
	}
}
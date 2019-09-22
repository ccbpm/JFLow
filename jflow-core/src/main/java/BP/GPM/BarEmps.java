package BP.GPM;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 人员信息块s
*/
public class BarEmps extends EntitiesMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 人员信息块s
	*/
	public BarEmps()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new BarEmp();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	public final String InitMyBars()
	{
		Bars bars = new Bars();
		bars.RetrieveAll();
		for (Bar b : bars)
		{
			BarEmp be = new BarEmp();
			be.setMyPK( b.No + "_" + WebUser.getNo();
			if (be.RetrieveFromDBSources() == 1)
			{
				continue;
			}

			be.setFK_Bar(b.No);
			be.setFK_Emp(WebUser.getNo());
			be.setIsShow(true);
			be.setTitle(b.Name);
			be.Insert();
		}

		return "执行成功";
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<BarEmp> ToJavaList()
	{
		return (List<BarEmp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<BarEmp> Tolist()
	{
		ArrayList<BarEmp> list = new ArrayList<BarEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((BarEmp)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
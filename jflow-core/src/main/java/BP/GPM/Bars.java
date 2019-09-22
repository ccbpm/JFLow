package BP.GPM;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 信息块s
*/
public class Bars extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
	/** 
	 信息块s
	*/
	public Bars()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new Bar();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Bar> ToJavaList()
	{
		return (List<Bar>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Bar> Tolist()
	{
		ArrayList<Bar> list = new ArrayList<Bar>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Bar)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

	@Override
	public int RetrieveAll()
	{
		//初始化数据到，表里面去.
		ArrayList als = ClassFactory.GetObjects("BP.GPM.BarBase");
		for (Object item : als)
		{
			if (item == null)
			{
				continue;
			}

			BP.GPM.BarBase en = (BP.GPM.BarBase)item;
			if (en == null)
			{
				continue;
			}

			BP.GPM.Bar bar = new Bar();
			bar.No = en.getNo();
			bar.Name = en.getName();
			bar.setTitle(en.getTitle());
			bar.setMoreUrl(en.getMore());
			bar.setHeight(en.getHeight());
			bar.setWidth(en.getWidth());
			bar.Save();

			if (en.getIsCanView() == false)
			{
				continue;
			}


			BP.GPM.BarEmp barEmp = new BarEmp();
			barEmp.setMyPK( en.getNo() + "_" + Web.WebUser.getNo();
		   int i = barEmp.RetrieveFromDBSources();

			barEmp.setFK_Bar(en.getNo());
			barEmp.setFK_Emp(Web.WebUser.getNo());
			barEmp.setIsShow(true);
			barEmp.setTitle(en.getName());
			if (i == 0)
			{
				barEmp.Insert();
			}

		}
		return super.RetrieveAll();
	}
}
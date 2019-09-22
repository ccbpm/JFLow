package BP.Port;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import java.util.*;

/** 
 岗位s
*/
public class Stations extends EntitiesNoName
{
	/** 
	 岗位
	*/
	public Stations()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Station();
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重写查询,add by stone 2015.09.30 为了适应能够从webservice数据源查询数据.
	/** 
	 重写查询全部适应从WS取数据需要
	 
	 @return 
	*/
	@Override
	public int RetrieveAll()
	{
		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.WebServices)
		{
			this.Clear(); //清除缓存数据.
			//获得数据.
			BP.En30.ccportal.PortalInterfaceSoapClient v = DataType.GetPortalInterfaceSoapClientInstance();
			DataTable dt = v.GetStations();
			if (dt.Rows.size() == 0)
			{
				return 0;
			}

			//设置查询.
			QueryObject.InitEntitiesByDataTable(this, dt, null);
			return dt.Rows.size();
		}
		else
		{
			return super.RetrieveAll();
		}
	}
	/** 
	 重写重数据源查询全部适应从WS取数据需要
	 
	 @return 
	*/
	@Override
	public int RetrieveAllFromDBSource()
	{
		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.WebServices)
		{
			this.Clear(); //清除缓存数据.
			//获得数据.
			BP.En30.ccportal.PortalInterfaceSoapClient v = DataType.GetPortalInterfaceSoapClientInstance();
			DataTable dt = v.GetStations();
			if (dt.Rows.size() == 0)
			{
				return 0;
			}

			//设置查询.
			QueryObject.InitEntitiesByDataTable(this, dt, null);
			return dt.Rows.size();
		}
		else
		{
			return super.RetrieveAllFromDBSource();
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 重写查询.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Station> ToJavaList()
	{
		return (List<Station>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Station> Tolist()
	{
		ArrayList<Station> list = new ArrayList<Station>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Station)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
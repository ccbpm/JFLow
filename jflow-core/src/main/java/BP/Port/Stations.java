package BP.Port;

import java.util.ArrayList;
import java.util.List;

import BP.En.EntitiesNoName;
import BP.En.Entity;

/**
 * 岗位s
 */
public class Stations extends EntitiesNoName
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static ArrayList<Station> convertStations(Object obj)
	{
		return (ArrayList<Station>) obj;
	}
	public List<Station> ToJavaList()
	{
		return (List<Station>)(Object)this;
	}
	/**
	 * 岗位
	 */
	public Stations()
	{
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new Station();
	}
}
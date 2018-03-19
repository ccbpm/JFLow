package BP.WF.Entity;

import java.util.ArrayList;
import java.util.List;

import BP.En.Entity;
import BP.Sys.MapExt;

/**
 * 轨迹集合
 */
public class Tracks extends BP.En.Entities
{
	public static ArrayList<Track> convertTracks(Object obj)
	{
		return (ArrayList<Track>) obj;
	}
	public List<Track> ToJavaList()
	{
		return (List<Track>)(Object)this;
	}
	/**
	 * 轨迹集合
	 */
	public Tracks()
	{
	}
	
	@Override
	public Entity getGetNewEntity()
	{
		return new Track();
	}
}
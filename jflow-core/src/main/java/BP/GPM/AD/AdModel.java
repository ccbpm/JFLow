package BP.GPM.AD;

import BP.*;
import BP.Port.*;
import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.GPM.*;
import java.util.*;

/** 
 Ad域信息实体
*/
public class AdModel
{
	public AdModel(String id, String name, int typeId, String parentId)
	{
		setId(id);
		setName(name);
		setTypeId(typeId);
		setParentId(parentId);
	}

	private String Id;
	public final String getId()
	{
		return Id;
	}
	public final void setId(String value)
	{
		Id = value;
	}

	private String Name;
	public final String getName()
	{
		return Name;
	}
	public final void setName(String value)
	{
		Name = value;
	}

	private int TypeId;
	public final int getTypeId()
	{
		return TypeId;
	}
	public final void setTypeId(int value)
	{
		TypeId = value;
	}

	private String ParentId;
	public final String getParentId()
	{
		return ParentId;
	}
	public final void setParentId(String value)
	{
		ParentId = value;
	}
}
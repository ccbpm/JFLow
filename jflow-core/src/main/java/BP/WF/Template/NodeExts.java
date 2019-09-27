package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;
import java.io.*;

/** 
 节点集合
*/
public class NodeExts extends Entities
{

		///#region 构造方法
	/** 
	 节点集合
	*/
	public NodeExts()
	{
	}

	public NodeExts(String fk_flow) throws Exception
	{
		this.Retrieve(NodeAttr.FK_Flow, fk_flow, NodeAttr.Step);
		return;
	}

	public final List<NodeExt> ToJavaList()
	{
		return (List<NodeExt>)(Object)this;
	}

	@Override
	public Entity getNewEntity()
	{
		return new NodeExt();
	}
}
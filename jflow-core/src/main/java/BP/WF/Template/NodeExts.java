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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	@Override
	public Entity getNewEntity()
	{
		return new NodeExt();
	}
}
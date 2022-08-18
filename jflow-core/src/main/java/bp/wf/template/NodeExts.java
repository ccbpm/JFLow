package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.sys.*;
import bp.port.*;
import bp.wf.template.sflow.*;
import bp.wf.template.ccen.*;
import bp.*;
import bp.wf.*;
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
	public NodeExts() throws Exception {
	}

	public NodeExts(String fk_flow) throws Exception {
		this.Retrieve(NodeAttr.FK_Flow, fk_flow, NodeAttr.Step);
		return;
	}

		///#endregion

	@Override
	public Entity getGetNewEntity() {
		return new NodeExt();
	}
}
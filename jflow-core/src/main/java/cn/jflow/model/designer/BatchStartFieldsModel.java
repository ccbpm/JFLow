package cn.jflow.model.designer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.WF.Node;
import BP.WF.Template.NodeAttr;
import cn.jflow.system.ui.UiFatory;

public class BatchStartFieldsModel {
	public HttpServletRequest request;
	public HttpServletResponse response;
	public UiFatory ui = null;
	public String basePath = "";

	
	public BatchStartFieldsModel (HttpServletRequest request,HttpServletResponse response){
		this.request=request;
		this.response=response;
	}
	
	/**
	 * 
	 */
	public final int getSetup(){
		try {
			return Integer.parseInt(request.getParameter("Step"));
		} catch (Exception e) {
			return 0;
		}
	}
	
	public final String getFK_Flow(){
		return request.getParameter("FK_Flow");
	}
	
	/**
	 * 节点ID
	 * @return
	 */
	public final int getFK_Node() {
		try {
			return Integer.parseInt(request.getParameter("FK_Node"));
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 显示的行数：
	 */
	public int tbNum;
	/**
	 * 页面隐藏字段
	 */
	public String srole;
	/**
	 * 规则设置列表
	 */
	public List<Map<String,Object>> bRoleList=new ArrayList<Map<String,Object>>();
	/**
	 * 字段列表
	 */
	public List<Map<String,Object>> filedList=new ArrayList<Map<String,Object>>();
	
	/**
	 * 
	 * @Description: 批量发起规则设置页面初始化
	 * @Title: Page_Load   
	 * @author peixiaofeng
	 * @date 2016年5月12日
	 */
	public void Page_Load() {
		Node nd = new Node();
		nd.setNodeID(this.getFK_Node());
		MapAttrs attrs = new MapAttrs("ND" + this.getFK_Node());
		nd.RetrieveFromDBSources();
		this.setTbNum(nd.getBatchListCount());
		//动态为 批处理赋值 和默认参数
		if("None".equals(nd.getHisBatchRole())){
			this.setSrole("0");
		}else if("Ordinary".equals(nd.getHisBatchRole())){
			this.setSrole("1");
		}else {
			this.setSrole("2");
		}
		List<Map<String,Object>> alist=new ArrayList<Map<String,Object>>(); 
		SysEnums ses = new SysEnums(NodeAttr.BatchRole);
		List<Map<String,Object>> blist=new ArrayList<Map<String,Object>>(); 
		for(int i=0;i<ses.size();i++){
			SysEnum item=(SysEnum) ses.get(i);
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("optionVal", item.getIntKey());
			map.put("optionLab", item.getLab());
			if(Integer.parseInt(this.getSrole())==(item.getIntKey())){
				map.put("selected", "selected='selected'");
			}else{
				map.put("selected", "");
			}
			blist.add(map);
		}
		this.setbRoleList(blist);
		
		for(int j=0;j<attrs.size();j++){
			MapAttr item=(MapAttr) attrs.get(j);
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("KeyOfEn",item.getKeyOfEn());
			map.put("name",item.getName());
			map.put("LGTypeT",item.getLGTypeT());
			if(nd.getBatchParas().contains(item.getKeyOfEn())){
				map.put("checked","checked='checked'");
			}else{
				map.put("checked","");
			}
			
			alist.add(map);
		}
		this.setFiledList(alist);
	}

	
/*****************get()/set()**************************************************************/	
	
	public int getTbNum() {
		return tbNum;
	}

	public void setTbNum(int tbNum) {
		this.tbNum = tbNum;
	}

	public String getSrole() {
		return srole;
	}

	public void setSrole(String srole) {
		this.srole = srole;
	}

	public List<Map<String, Object>> getbRoleList() {
		return bRoleList;
	}

	public void setbRoleList(List<Map<String, Object>> bRoleList) {
		this.bRoleList = bRoleList;
	}

	public List<Map<String, Object>> getFiledList() {
		return filedList;
	}

	public void setFiledList(List<Map<String, Object>> filedList) {
		this.filedList = filedList;
	}
	
	
	
}

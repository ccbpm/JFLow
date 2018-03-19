package cn.jflow.model.wf.admin.findworker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.WF.Node;
import BP.WF.Template.BtnLab;
import cn.jflow.common.model.BaseModel;

public class NodeCCRoleModel extends BaseModel{
	
	

	public NodeCCRoleModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	/**
	 * 抄送规则
	 */
	public List<Map<String,Object>> csgzList=new ArrayList<Map<String,Object>>();
	/**
	 * 抄送写入规则
	 */
	public List<Map<String,Object>> csxrgzList=new ArrayList<Map<String,Object>>();
	/**
	 * 模板标题 、内容
	 */
	public String title;
	public String content;
	/**
	 * 岗位、部门、人员 、SQL选择框
	 */
	public String gwChecked;
	public String bmChecked;
	public String ryChecked;
	public String sqlChecked;
	/**
	 * SQL设置范围
	 */
	public String sqlText;

	
	
	
	public void pageLoad() {
		BtnLab btn = new BtnLab(this.getFK_Node());
		Node nd = new Node(this.getFK_Node());
		BP.WF.Template.CC cc = new BP.WF.Template.CC();
		cc.setNodeID(this.getFK_Node());
		cc.Retrieve();
		// 设置抄送规则
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		SysEnums ens = new SysEnums("CCRole");
		for (int i = 0; i < ens.size(); i++) {
			SysEnum en = (SysEnum) ens.get(i);
			Map<String, Object> aMap = new HashMap<String, Object>();
			aMap.put("value", en.getIntKey());
			aMap.put("lab", en.getLab());
			if (en.getIntKey() == btn.getCCRole().getValue()) {
				aMap.put("selected", "selected='selected'");
			} else {
				aMap.put("selected", "");
			}
			list.add(aMap);
		}
		this.setCsgzList(list);

		// 设置抄送写入规则
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		ens = new SysEnums("CCWriteTo");
		for (int i = 0; i < ens.size(); i++) {
			SysEnum en = (SysEnum) ens.get(i);
			Map<String, Object> aMap = new HashMap<String, Object>();
			aMap.put("value", en.getIntKey());
			aMap.put("lab", en.getLab());
			if (en.getIntKey() == nd.getCCWriteTo().getValue()) {
				aMap.put("selected", "selected='selected'");
			} else {
				aMap.put("selected", "");
			}
			list1.add(aMap);
		}
		this.setCsxrgzList(list1);
		// 设置模板标题 、内容
		this.setTitle(cc.getCCTitle());
		this.setContent(cc.getCCDoc());

		if (cc.getCCIsStations() == true) {
			this.setGwChecked("checked='checked'");
		} else {
			this.setGwChecked("");
		}
		if (cc.getCCIsDepts() == true) {
			this.setBmChecked("checked='checked'");
		} else {
			this.setBmChecked("");
		}
		if (cc.getCCIsEmps() == true) {
			this.setRyChecked("checked='checked'");
		} else {
			this.setRyChecked("");
		}
		if (cc.getCCIsSQLs() == true) {
			this.setSqlChecked("checked='checked'");
			this.setSqlText(cc.getCCSQL());
		} else {
			this.setSqlChecked("");
			//this.setSqlText(cc.getCCSQL());
			this.setSqlText("");
		}
	}


/***********************get()/set()******************************************************************************************/

	public List<Map<String, Object>> getCsgzList() {
		return csgzList;
	}




	public void setCsgzList(List<Map<String, Object>> csgzList) {
		this.csgzList = csgzList;
	}




	public List<Map<String, Object>> getCsxrgzList() {
		return csxrgzList;
	}




	public void setCsxrgzList(List<Map<String, Object>> csxrgzList) {
		this.csxrgzList = csxrgzList;
	}




	public String getTitle() {
		return title;
	}




	public void setTitle(String title) {
		this.title = title;
	}




	public String getContent() {
		return content;
	}




	public void setContent(String content) {
		this.content = content;
	}




	public String getGwChecked() {
		return gwChecked;
	}




	public void setGwChecked(String gwChecked) {
		this.gwChecked = gwChecked;
	}




	public String getBmChecked() {
		return bmChecked;
	}




	public void setBmChecked(String bmChecked) {
		this.bmChecked = bmChecked;
	}




	public String getRyChecked() {
		return ryChecked;
	}




	public void setRyChecked(String ryChecked) {
		this.ryChecked = ryChecked;
	}




	public String getSqlChecked() {
		return sqlChecked;
	}




	public void setSqlChecked(String sqlChecked) {
		this.sqlChecked = sqlChecked;
	}




	public String getSqlText() {
		return sqlText;
	}




	public void setSqlText(String sqlText) {
		this.sqlText = sqlText;
	}

	

}

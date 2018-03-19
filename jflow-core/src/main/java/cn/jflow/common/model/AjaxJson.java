package cn.jflow.common.model;


import java.io.Serializable;
import java.util.Map;

/**
 * $.ajax后需要接受的JSON
 * 
 * @author
 * 
 */
public class AjaxJson implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean success = true;// 是否成功
	private String msg = "操作成功";// 提示信息
	private Map<String, Object> attributes;// 其他参数
	private Object data;// 其他参数
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	


	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
//	public String getJsonStr(){
//		JSONObject obj = new JSONObject();
//		obj.put("success", this.isSuccess());
//		obj.put("msg", this.getMsg());
//		obj.put("attributes", this.attributes);
//		return obj.toString();
//	}
}

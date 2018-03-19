package cn.jflow.common.model;

import java.io.Serializable;

public class SelectDept implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String method;
	private String kw;
	private boolean havesub;
	private boolean havesame;
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getKw() {
		return kw;
	}
	public void setKw(String kw) {
		this.kw = kw;
	}
	public boolean isHavesub() {
		return havesub;
	}
	public void setHavesub(boolean havesub) {
		this.havesub = havesub;
	}
	public boolean isHavesame() {
		return havesame;
	}
	public void setHavesame(boolean havesame) {
		this.havesame = havesame;
	}
	
}

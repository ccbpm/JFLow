package cn.jflow.common.model;

import java.io.Serializable;

public class SelectUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String DeptId;
	private boolean SearchChild;
	private String KeyWord;
	private String StationId;
	private String SelUsers;
	public String getDeptId() {
		return DeptId;
	}
	public void setDeptId(String deptId) {
		DeptId = deptId;
	}
	public boolean isSearchChild() {
		return SearchChild;
	}
	public void setSearchChild(boolean searchChild) {
		SearchChild = searchChild;
	}
	public String getKeyWord() {
		return KeyWord;
	}
	public void setKeyWord(String keyWord) {
		KeyWord = keyWord;
	}
	public String getStationId() {
		return StationId;
	}
	public void setStationId(String stationId) {
		StationId = stationId;
	}
	public String getSelUsers() {
		return SelUsers;
	}
	public void setSelUsers(String selUsers) {
		SelUsers = selUsers;
	}
}

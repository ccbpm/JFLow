package bp.port;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.en.Map;
import bp.sys.*;
import bp.tools.Cryptos;
import bp.tools.Encodes;
import bp.web.WebUser;
import bp.*;
import java.util.*;
import java.io.*;

/**
 * Emp 的摘要说明。
 */
public class Emp extends EntityNoName {

	/// 扩展属性
	/**
	 * 主要的部门。
	 * 
	 * @throws Exception
	 */
	public final Dept getHisDept() throws Exception {
		try {
			return new Dept(this.getFK_Dept());
		} catch (RuntimeException ex) {
			throw new RuntimeException(
					"@获取操作员" + this.getNo() + "部门[" + this.getFK_Dept() + "]出现错误,可能是系统管理员没有给他维护部门.@" + ex.getMessage());
		}
	}

	/**
	 * 部门编号
	 */
	public final String getFK_Dept() throws Exception{
		return this.GetValStrByKey(EmpAttr.FK_Dept);
	}

	public final void setFK_Dept(String value) throws Exception {
		this.SetValByKey(EmpAttr.FK_Dept, value);
	}

	/**
	 * 部门编号
	 */
	public final String getFK_DeptText()throws Exception {
		return this.GetValRefTextByKey(EmpAttr.FK_Dept);
	}

	/**
	 * 密码
	 */
	public final String getPass()throws Exception {
		return this.GetValStrByKey(EmpAttr.Pass);
	}

	public final void setPass(String value) throws Exception{
		this.SetValByKey(EmpAttr.Pass, value);
	}

	public final String getSID()throws Exception {
		return this.GetValStrByKey(EmpAttr.SID);
	}

	public final void setSID(String value) throws Exception {
		this.SetValByKey(EmpAttr.SID, value);
	}

	public final String getTel()throws Exception {
		return this.GetValStrByKey(EmpAttr.Tel);
	}

	public final void setTel(String value) throws Exception {
		this.SetValByKey(EmpAttr.Tel, value);
	}

	public final String getEmail()throws Exception {
		return this.GetValStrByKey(EmpAttr.Email);
	}

	public final void setEmail(String value) throws Exception{
		this.SetValByKey(EmpAttr.Email, value);
	}

	public final String getOrgNo()throws Exception {
		return this.GetValStrByKey(EmpAttr.OrgNo);
	}

	public final void setOrgNo(String value) throws Exception{
		this.SetValByKey(EmpAttr.OrgNo, value);
	}
	public final String getUserID()throws Exception {
		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
            return this.GetValStringByKey(EmpAttr.UserID);

        return this.GetValStringByKey(EmpAttr.No);
	}

	public final void setUserID(String value) throws Exception{
		this.SetValByKey(EmpAttr.UserID, value);

        if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
            this.SetValByKey(EmpAttr.No, bp.web.WebUser.getOrgNo() + "_" + value);
        else
            this.SetValByKey(EmpAttr.No, value);
	}
	///

	/// 公共方法
	/**
	 * 权限管理.
	 * 
	 * @throws Exception
	 */
	@Override
	public UAC getHisUAC() throws Exception {
		UAC uac = new UAC();
		uac.OpenForAppAdmin();
		return uac;
	}

	/**
	 * 检查密码(可以重写此方法)
	 * 
	 * @param pass
	 *            密码
	 * @return 是否匹配成功
	 * @throws Exception 
	 */
	public final boolean CheckPass(String pass) throws Exception {

		// 启用加密
		if (SystemConfig.getIsEnablePasswordEncryption() == true) {
			if(SystemConfig.getPasswordEncryptionType().equals("0"))
				pass=Encodes.encodeBase64(pass);
			if(SystemConfig.getPasswordEncryptionType().equals("1"))
				pass = Cryptos.aesEncrypt(pass);
		}

		/* 使用数据库校验. */
		if (this.getPass().equals(pass)) {
			return true;
		}

		return false;
	}

	private static byte[] Keys = { 0x12, (byte) 0xCD, 0x3F, 0x34, 0x78, (byte) 0x90, 0x56, 0x7B };

	/// 构造函数
	/**
	 * 操作员
	 */
	public Emp() {
	}

	/**
	 * 操作员
	 * 
	 * @param no
	 *            编号
	 * @throws Exception 
	 */
	public Emp(String no) throws Exception {
		if (no == null || no.length() == 0) {
			throw new RuntimeException("@要查询的操作员编号为空。");
		}

		this.setNo(no.trim());

		this.Retrieve();

	}

	/**
	 * 重写基类方法
	 */
	@Override
	public Map getEnMap() throws Exception {
		if (this.get_enMap() != null) {
			return this.get_enMap();
		}

		Map map = new Map("Port_Emp", "用户");

		/// 基本属性
		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN)); // 要连接的数据源（表示要连接到的那个系统数据库）。
		map.IndexField = EmpAttr.FK_Dept;

		///

		/// 字段
		/* 关于字段属性的增加 .. */
		// map.IsEnableVer = true;

		map.AddTBStringPK(EmpAttr.No, null, "编号", true, false, 1, 20, 30);
		map.AddTBString(EmpAttr.Name, null, "名称", true, false, 0, 200, 30);
		//如果是集团模式或者是SAAS模式.
		if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			map.AddTBString(EmpAttr.UserID, null, "用户ID", true, false, 0, 50, 30);
			map.AddTBString(EmpAttr.OrgNo, null, "OrgNo", true, false, 0, 50, 30);
		}
		map.AddTBString(EmpAttr.Pass, "123", "密码", false, false, 0, 20, 10);
		map.AddDDLEntities(EmpAttr.FK_Dept, null, "部门", new bp.port.Depts(), true);
		map.AddTBString(EmpAttr.SID, null, "安全校验码", false, false, 0, 36, 36);
		map.AddTBString(EmpAttr.Tel, null, "手机号", false, false, 0, 36, 36);
		map.AddTBString(EmpAttr.Email, null, "邮箱", false, false, 0, 36, 36);
		map.AddTBInt(EmpAttr.UserType,1,"用户状态",false,true);

		/// 字段

		map.AddSearchAttr(EmpAttr.FK_Dept);
		map.AddHidden(EmpAttr.UserType,"=","1");

		this.set_enMap(map);
		return this.get_enMap();
	}

	/**
	 * 获取集合
	 */
	@Override
	public Entities getGetNewEntities() {
		return new Emps();
	}

	/// 构造函数

	/// 重写方法
	@Override
	protected boolean beforeDelete() throws Exception {

		return super.beforeDelete();
	}

	/// 重写方法

	/// 重写查询.
	/**
	 * 查询
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public int Retrieve() throws Exception {

		return super.Retrieve();
	}

	/**
	 * 查询.
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public int RetrieveFromDBSources() throws Exception {

		return super.RetrieveFromDBSources();

	}

	///

	/// 方法测试代码.
	/**
	 * 测试
	 * 
	 * @return
	 */
	public final String ResetPass() {
		return "执行成功.";
	}

	/**
	 * ChangePass
	 * 
	 * @param oldpass
	 * @param pass1
	 * @param pass2
	 * @return
	 * @throws Exception
	 */
	public final String ChangePass(String oldpass, String pass1, String pass2) throws Exception {
		if (!this.getNo().equals(WebUser.getNo())) {
			return "err@sss";
		}
		return "执行成功.";
	}

	/// 方法测试代码.

}
package BP.Port;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.Difference.Handler.PortalInterface;
import BP.En.*;
import BP.En.Map;
import BP.Sys.*;
import BP.Tools.Cryptos;
import BP.Tools.Encodes;
import BP.Web.WebUser;

/**
 * Emp 的摘要说明。
 */
public class Emp extends EntityNoName {
	/// #region 扩展属性
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
	 * 
	 * @throws Exception
	 */
	public final String getFK_Dept() throws Exception {
		return this.GetValStrByKey(EmpAttr.FK_Dept);
	}

	public final void setFK_Dept(String value) throws Exception {
		this.SetValByKey(EmpAttr.FK_Dept, value);
	}

	/**
	 * 部门编号
	 * 
	 * @throws Exception
	 */
	public final String getFK_DeptText() throws Exception {
		return this.GetValRefTextByKey(EmpAttr.FK_Dept);
	}

	/**
	 * 密码
	 * 
	 * @throws Exception
	 */
	public final String getPass() throws Exception {
		return this.GetValStrByKey(EmpAttr.Pass);
	}

	public final void setPass(String value) throws Exception {
		this.SetValByKey(EmpAttr.Pass, value);
	}

	public final String getSID() throws Exception {
		return this.GetValStrByKey(EmpAttr.SID);
	}

	public final void setSID(String value) throws Exception {
		this.SetValByKey(EmpAttr.SID, value);
	}
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
		// 检查是否与通用密码相符.
//		if (SystemConfig.getOSDBSrc() == OSDBSrc.WebServices) {
//			// 如果是使用webservices校验.
//			PortalInterface ws = new PortalInterface();
//			boolean IsCheck = ws.CheckUserNoPassWord(this.getNo(), pass);
//			if (IsCheck == true) {
//				return true;
//			}
//			return false;
//		} else {
			// 启用加密
			if (SystemConfig.getIsEnablePasswordEncryption() == true)
			{
				if(SystemConfig.getPasswordEncryptionType().equals("0"))
					pass=Encodes.encodeBase64(pass);
				if(SystemConfig.getPasswordEncryptionType().equals("1"))
					pass = Cryptos.aesEncrypt(pass);
			}

			/* 使用数据库校验. */
			if (this.getPass().equals(pass)) {
				return true;
			}

//		}
		return false;
	}

	private static byte[] Keys = { 0x12, (byte) 0xCD, 0x3F, 0x34, 0x78, (byte) 0x90, 0x56, 0x7B };

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
	public Map getEnMap() {
		if (this.get_enMap() != null) {
			return this.get_enMap();
		}

		Map map = new Map();

		/// #region 基本属性
		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN)); // 要连接的数据源（表示要连接到的那个系统数据库）。
		map.setPhysicsTable("Port_Emp"); // 要物理表。
		map.Java_SetDepositaryOfMap(Depositary.Application); // 实体map的存放位置.
		map.Java_SetDepositaryOfEntity(Depositary.Application); // 实体存放位置
		map.setEnDesc("用户"); // "用户";
		map.Java_SetEnType(EnType.App); // 实体类型。
		map.IndexField = EmpAttr.FK_Dept;

		map.AddTBStringPK(EmpAttr.No, null, "编号", true, false, 1, 20, 30);
		map.AddTBString(EmpAttr.Name, null, "名称", true, false, 0, 200, 30);
		map.AddTBString(EmpAttr.Pass, "123", "密码", false, false, 0, 20, 10);
		map.AddDDLEntities(EmpAttr.FK_Dept, null, "部门", new BP.Port.Depts(), true);
		map.AddTBString(EmpAttr.SID, null, "安全校验码", false, false, 0, 36, 36);

		map.AddSearchAttr(EmpAttr.FK_Dept);

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

	/**
	 * 测试
	 * 
	 * @return
	 */
	public final String ResetPass() {
		return "执行成功.";
	}

	public final String ChangePass(String oldpass, String pass1, String pass2) throws Exception {
		if (!this.getNo().equals(WebUser.getNo())) {
			return "err@sss";
		}
		return "执行成功.";
	}

}
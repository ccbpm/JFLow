package BP.Port;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Sys.*;
import BP.Web.WebUser;

import java.util.*;
import java.io.*;

/** 
 Emp 的摘要说明。
*/
public class Emp extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 扩展属性
	/** 
	 主要的部门。
	 * @throws Exception 
	*/
	public final Dept getHisDept() throws Exception
	{
		try
		{
			return new Dept(this.getFK_Dept());
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@获取操作员" + this.getNo() + "部门[" + this.getFK_Dept() + "]出现错误,可能是系统管理员没有给他维护部门.@" + ex.getMessage());
		}
	}

	/** 
	 部门编号
	 * @throws Exception 
	*/
	public final String getFK_Dept() throws Exception
	{
		return this.GetValStrByKey(EmpAttr.FK_Dept);
	}
	public final void setFK_Dept(String value) throws Exception
	{
		this.SetValByKey(EmpAttr.FK_Dept, value);
	}
	/** 
	 部门编号
	 * @throws Exception 
	*/
	public final String getFK_DeptText() throws Exception
	{
		return this.GetValRefTextByKey(EmpAttr.FK_Dept);
	}
	/** 
	 密码
	 * @throws Exception 
	*/
	public final String getPass() throws Exception
	{
		return this.GetValStrByKey(EmpAttr.Pass);
	}
	public final void setPass(String value) throws Exception
	{
		this.SetValByKey(EmpAttr.Pass, value);
	}
	public final String getSID() throws Exception
	{
		return this.GetValStrByKey(EmpAttr.SID);
	}
	public final void setSID(String value) throws Exception
	{
		this.SetValByKey(EmpAttr.SID, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 公共方法
	/** 
	 权限管理.
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForAppAdmin();
		return uac;
	}
	/** 
	 检查密码(可以重写此方法)
	 
	 @param pass 密码
	 @return 是否匹配成功
	*/
	public final boolean CheckPass(String pass)
	{
		//检查是否与通用密码相符.
		//string gePass = SystemConfig.AppSettings["GenerPass"];
		//if (gePass == pass && DataType.IsNullOrEmpty(gePass) == false)
		//    return true;

		if (SystemConfig.getOSDBSrc() == OSDBSrc.WebServices)
		{
			//如果是使用webservices校验.
			BP.En30.ccportal.PortalInterfaceSoapClient v = DataType.GetPortalInterfaceSoapClientInstance();
			int i = v.CheckUserNoPassWord(this.getNo(), pass);
			if (i == 1)
			{
				return true;
			}
			return false;
		}
		else
		{
			//启用加密
			if (SystemConfig.getIsEnablePasswordEncryption() == true)
			{
				pass = BP.Tools.Cryptography.EncryptString(pass);
			}

			/*使用数据库校验.*/
			if (this.getPass().equals(pass))
			{
				return true;
			}

		}
		return false;
	}

//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: private static byte[] Keys = { 0x12, 0xCD, 0x3F, 0x34, 0x78, 0x90, 0x56, 0x7B };
	private static byte[] Keys = {0x12, (byte)0xCD, 0x3F, 0x34, 0x78, (byte)0x90, 0x56, 0x7B};

	/** 
	 加密字符串
	 
	 @param pass
	 @return 
	*/
	public static String EncryptPass(String pass)
	{
		DESCryptoServiceProvider descsp = new DESCryptoServiceProvider(); //实例化加/解密类对象

//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] data = Encoding.Unicode.GetBytes(pass);
		byte[] data = pass.getBytes(java.nio.charset.StandardCharsets.UTF_16LE); //定义字节数组，用来存储要加密的字符串
		ByteArrayOutputStream MStream = new ByteArrayOutputStream(); //实例化内存流对象
		//使用内存流实例化加密流对象   
		CryptoStream CStream = new CryptoStream(MStream, descsp.CreateEncryptor(Keys, Keys), CryptoStreamMode.Write);
		CStream.Write(data, 0, data.length); //向加密流中写入数据
		CStream.FlushFinalBlock(); //释放加密流
		return Convert.ToBase64String(MStream.ToArray()); //返回加密后的字符串
	}

	/** 
	 解密字符串
	 
	 @param pass
	 @return 
	*/
	public static String DecryptPass(String pass)
	{
		DESCryptoServiceProvider descsp = new DESCryptoServiceProvider(); //实例化加/解密类对象
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] data = Convert.FromBase64String(pass);
		byte[] data = Convert.FromBase64String(pass); //定义字节数组，用来存储要解密的字符串
		ByteArrayOutputStream MStream = new ByteArrayOutputStream(); //实例化内存流对象
		//使用内存流实例化解密流对象       
		CryptoStream CStream = new CryptoStream(MStream, descsp.CreateDecryptor(Keys, Keys), CryptoStreamMode.Write);
		CStream.Write(data, 0, data.length); //向解密流中写入数据
		CStream.FlushFinalBlock(); //释放解密流
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: return Encoding.Unicode.GetString(MStream.ToArray());
		return Encoding.Unicode.GetString((Byte)MStream.ToArray()); //返回解密后的字符串
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 公共方法

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 操作员
	*/
	public Emp()
	{
	}
	/** 
	 操作员
	 
	 @param no 编号
	 * @throws Exception 
	*/
	public Emp(String no) throws Exception
	{
		if (no == null || no.length() == 0)
		{
			throw new RuntimeException("@要查询的操作员编号为空。");
		}

		this.setNo(no.trim());
		try
		{
			this.Retrieve();
		}
		catch (RuntimeException ex)
		{
			if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.Database)
			{
				//登陆帐号查询不到用户，使用职工编号查询。
				QueryObject obj = new QueryObject(this);
				obj.AddWhere(EmpAttr.No, no);
				int i = obj.DoQuery();
				if (i == 0)
				{
					i = this.RetrieveFromDBSources();
				}
				if (i == 0)
				{
					throw new RuntimeException("@用户或者密码错误：[" + no + "]，或者帐号被停用。@技术信息(从内存中查询出现错误)：ex1=" + ex.getMessage());
				}
			}
			else
			{
				throw ex;
			}
		}
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基本属性
		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN)); //要连接的数据源（表示要连接到的那个系统数据库）。
		map.setPhysicsTable("Port_Emp"); // 要物理表。
		map.Java_SetDepositaryOfMap(Depositary.Application); //实体map的存放位置.
		map.Java_SetDepositaryOfEntity(Depositary.Application); //实体存放位置
		map.setEnDesc("用户"); // "用户";
		map.Java_SetEnType(EnType.App); //实体类型。
		map.IndexField = EmpAttr.FK_Dept;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 字段
			/* 关于字段属性的增加 .. */
			//map.IsEnableVer = true;

		map.AddTBStringPK(EmpAttr.No, null, "编号", true, false, 1, 20, 30);
		map.AddTBString(EmpAttr.Name, null, "名称", true, false, 0, 200, 30);
		map.AddTBString(EmpAttr.Pass, "123", "密码", false, false, 0, 20, 10);
		map.AddDDLEntities(EmpAttr.FK_Dept, null, "部门", new BP.Port.Depts(), true);
		map.AddTBString(EmpAttr.SID, null, "安全校验码", false, false, 0, 36, 36);

			// map.AddTBString("docs", null, "安全校33验码", false, false, 0, 4000, 36);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 字段

		map.AddSearchAttr(EmpAttr.FK_Dept);



		this.set_enMap(map);
		return this.get_enMap();
	}

	/** 
	 获取集合
	*/
	@Override
	public Entities getGetNewEntities()
	{
		return new Emps();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 构造函数

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重写方法
	@Override
	protected boolean beforeDelete() throws Exception
	{

		return super.beforeDelete();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 重写方法

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重写查询.
	/** 
	 查询
	 
	 @return 
	*/
	@Override
	public int Retrieve()
	{
		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.WebServices)
		{
			BP.En30.ccportal.PortalInterfaceSoapClient v = DataType.GetPortalInterfaceSoapClientInstance();
			DataTable dt = v.GetEmp(this.getNo());
			if (dt.Rows.size() == 0)
			{
				throw new RuntimeException("@编号为(" + this.getNo() + ")的人员不存在。");
			}
			this.getRow().LoadDataTable(dt, dt.Rows.get(0));
			return 1;
		}
		else
		{
			return super.Retrieve();
		}
	}
	/** 
	 查询.
	 
	 @return 
	*/
	@Override
	public int RetrieveFromDBSources()
	{
		if (BP.Sys.SystemConfig.getOSDBSrc() == OSDBSrc.WebServices)
		{
			BP.En30.ccportal.PortalInterfaceSoapClient v = DataType.GetPortalInterfaceSoapClientInstance();
			DataTable dt = v.GetEmp(this.getNo());
			if (dt.Rows.size() == 0)
			{
				return 0;
			}
			this.getRow().LoadDataTable(dt, dt.Rows.get(0));
			return 1;
		}
		else
		{
			return super.RetrieveFromDBSources();
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法测试代码.
	/** 
	 测试
	 
	 @return 
	*/
	public final String ResetPass()
	{
		return "执行成功.";
	}

	public final String ChangePass(String oldpass, String pass1, String pass2) throws Exception
	{
		if (!this.getNo().equals(WebUser.getNo()))
		{
			return "err@sss";
		}
		return "执行成功.";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 方法测试代码.

}
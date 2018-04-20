package BP.Demo;

import BP.DA.DataType;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.UAC;

/**
 * 学生
 */
public class Student extends EntityNoName
{
	private static final long serialVersionUID = 1002323232L;
	
	/** 
	 登录系统密码
	*/
	public final String getPWD()
	{
		return this.GetValStringByKey(StudentAttr.PWD);
	}
	public final void setPWD(String value)
	{
		this.SetValByKey(StudentAttr.PWD, value);
	}

	/**
	 * 年龄
	 */
	public final int getAge()
	{
		return this.GetValIntByKey(StudentAttr.Age);
	}
	
	public final void setAge(int value)
	{
		this.SetValByKey(StudentAttr.Age, value);
	}
	
	/**
	 * 地址
	 */
	public final String getAddr()
	{
		return this.GetValStringByKey(StudentAttr.Addr);
	}
	
	public final void setAddr(String value)
	{
		this.SetValByKey(StudentAttr.Addr, value);
	}
	
	/**
	 * 性别
	 */
	public final int getXB()
	{
		return this.GetValIntByKey(StudentAttr.XB);
	}
	
	public final void setXB(int value)
	{
		this.SetValByKey(StudentAttr.XB, value);
	}
	
	/**
	 * 性别名称
	 */
	public final String getXBText()
	{
		return this.GetValRefTextByKey(StudentAttr.XB);
	}
	
	/**
	 * 班级编号
	 */
	public final String getFJ_BanJi()
	{
		return this.GetValStringByKey(StudentAttr.FK_BanJi);
	}
	
	public final void setFJ_BanJi(String value)
	{
		this.SetValByKey(StudentAttr.FK_BanJi, value);
	}
	
	/**
	 * 班级名称
	 */
	public final String getFJ_BanJiText()
	{
		return this.GetValRefTextByKey(StudentAttr.FK_BanJi);
	}
	
	/**
	 * 邮件
	 */
	public final String getEmail()
	{
		return this.GetValStringByKey(StudentAttr.Email);
	}
	
	public final void setEmail(String value)
	{
		this.SetValByKey(StudentAttr.Email, value);
	}
	
	/**
	 * 是否特困生？
	 */
	public final String getIsTeKunSheng()
	{
		return this.GetValStringByKey(StudentAttr.IsTeKunSheng);
	}
	
	public final void setIsTeKunSheng(String value)
	{
		this.SetValByKey(StudentAttr.IsTeKunSheng, value);
	}
	
	/**
	 * 是否有重大疾病史？
	 */
	public final String getIsJiBing()
	{
		return this.GetValStringByKey(StudentAttr.IsJiBing);
	}
	
	public final void setIsJiBing(String value)
	{
		this.SetValByKey(StudentAttr.IsJiBing, value);
	}
	/**
	 * 是否偏远山区？
	 */
	public final String getIsPianYuanShanQu()
	{
		return this.GetValStringByKey(StudentAttr.IsPianYuanShanQu);
	}
	
	public final void setIsPianYuanShanQu(String value)
	{
		this.SetValByKey(StudentAttr.IsPianYuanShanQu, value);
	}
	/**
	 * 是否独生子
	 */
	
	public final String getIsDuShengZi()
	{
		return this.GetValStringByKey(StudentAttr.IsDuShengZi);
	}
	
	public final void setIsDuShengZi(String value)
	{
		this.SetValByKey(StudentAttr.IsDuShengZi, value);
	}
	/**
	 * 政治面貌
	 */
	public final String getZZMM()
	{
		return this.GetValStringByKey(StudentAttr.ZZMM);
	}
	
	public final void setZZMM(String value)
	{
		this.SetValByKey(StudentAttr.ZZMM, value);
	}
	
	
	/**
	 * 电话
	 */
	public final String getTel()
	{
		return this.GetValStringByKey(StudentAttr.Tel);
	}
	
	public final void setTel(String value)
	{
		this.SetValByKey(StudentAttr.Tel, value);
	}
	
	/**
	 * 注册日期
	 */
	public final String getRegDate()
	{
		return this.GetValStringByKey(StudentAttr.RegDate);
	}
	
	public final void setRegDate(String value)
	{
		this.SetValByKey(StudentAttr.RegDate, value);
	}
	/** 
	 实体的权限控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsDelete = true;
		uac.IsUpdate = true;
		uac.IsInsert = true;
		return uac;
	}

	/**
	 * 构造函数
	 */
	public Student()
	{
	}
	
	/**
	 * 构造函数
	 * @throws Exception 
	 */
	public Student(String no) throws Exception
	{
		super(no);
	}
	
	/**
	 * 重写基类方法
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map();
		
		// 基础信息.
		map.setEnDesc("学生");
		map.setPhysicsTable("Demo_Student");
		map.setIsAllowRepeatName(true); // 是否允许名称重复.
		map.setIsAutoGenerNo(true); // 是否自动生成编号.
		map.setCodeStruct("4"); // 4位数的编号，从0001 开始，到 9999.
		
		// 普通字段
		map.AddTBStringPK(StudentAttr.No, null, "学号", true, true, 4, 4, 4); // 如果设置自动编号字段必须是只读的.
		map.AddTBString(StudentAttr.Name, null, "名称", true, false, 0, 200, 70);
		map.AddTBString(StudentAttr.PWD, null, "登录密码", true, false, 0, 200, 70);
		map.AddTBString(StudentAttr.Addr, null, "地址", true, false, 0, 200, 100,
				true);
		map.AddTBInt(StudentAttr.Age, 0, "年龄", true, false);
		map.AddTBString(StudentAttr.Tel, null, "电话", true, false, 0, 200, 60);
		map.AddTBString(StudentAttr.Email, null, "邮件", true, false, 0, 200, 50);
		map.AddTBDateTime(StudentAttr.RegDate, null, "注册日期", true, true);
		
		//增加checkbox属性.
        map.AddBoolean(StudentAttr.IsDuShengZi, false, "是否是独生子？", true, true);
        map.AddBoolean(StudentAttr.IsJiBing, false, "是否有重大疾病？", true, true);
        map.AddBoolean(StudentAttr.IsPianYuanShanQu, false, "是否偏远山区？", true, true);
        map.AddBoolean(StudentAttr.IsTeKunSheng, false, "是否是特困生？", true, true);

        // 枚举字段 - 整治面貌.
        map.AddDDLSysEnum(StudentAttr.ZZMM, 0, "整治面貌", true, true, StudentAttr.ZZMM,
            "@0=少先队员@1=团员@2=党员");
		
		// 枚举字段
		map.AddDDLSysEnum(StudentAttr.XB, 0, "性别", true, false, StudentAttr.XB,
				"@0=女@1=男");
		
		// 外键字段.
		map.AddDDLEntities(StudentAttr.FK_BanJi, null, "班级", new BanJis(),
				false);
		
		map.AddTBStringDoc(StudentAttr.Note, null, "备注", true, false, true); //
		
		map.AddMyFile("简历");

		// 设置查询条件
		map.AddSearchAttr(StudentAttr.XB);
		map.AddSearchAttr(StudentAttr.FK_BanJi);
		
		// 多对多的映射.
		map.getAttrsOfOneVSM().Add(new StudentKeMus(), new KeMus(),
				StudentKeMuAttr.FK_Student, StudentKeMuAttr.FK_KeMu,
				KeMuAttr.Name, KeMuAttr.No, "学习的科目");
		
		// 明细表映射.
		// map.AddDtl(new Resumes(), ResumeAttr.FK_Emp);
		//
		// //带有参数的方法.
		// RefMethod rm = new RefMethod();
		// rm.Title = "缴纳班费";
		// rm.getHisAttrs().AddTBDecimal("JinE", new BigDecimal(100), "缴纳金额",
		// true, false);
		// rm.getHisAttrs().AddTBString("Note", null, "备注", true, false, 0, 100,
		// 100);
		// rm.ClassMethodName = this.toString() + ".DoJiaoNaBanFei";
		// map.AddRefMethod(rm);
		//
		// //不带有参数的方法.
		// rm = new RefMethod();
		// rm.Title = "注销学籍";
		// rm.ClassMethodName = this.toString() + ".DoZhuXiao";
		// map.AddRefMethod(rm);
		
		this.set_enMap(map);
		return this.get_enMap();
	}
	
 
	/**
	 * 带有参数的方法:缴纳班费 说明：都要返回string类型.
	 * 
	 * @return
	 */
	public final String DoJiaoNaBanFei(java.math.BigDecimal jine, String note)
	{
		return "学号:" + this.getNo() + ",姓名:" + this.getName() + ",缴纳了:" + jine
				+ "元,说明:" + note;
	}
	
	/**
	 * 无参数的方法:注销学籍 说明：都要返回string类型.
	 * 
	 * @return
	 */
	public final String DoZhuXiao()
	{
		return "学号:" + this.getNo() + ",姓名:" + this.getName() + ",已经注销.";
	}
}
package bp.cloud.sys;

import bp.en.*;

/**
 * 用户自定义表
 */
public class SFTableAttr extends EntityNoNameAttr {
    /**
     * 组织机构编码
     */
    public static final String OrgNo = "OrgNo";
    /**
     * 是否可以删除
     */
    public static final String IsDel = "IsDel";
    /**
     * 字段
     */
    public static final String FK_Val = "FK_Val";
    /**
     * 数据表描述
     */
    public static final String TableDesc = "TableDesc";
    /**
     * 默认值
     */
    public static final String DefVal = "DefVal";
    /**
     * 数据源
     */
    public static final String DBSrc = "DBSrc";
    /**
     * 是否是树
     */
    public static final String IsTree = "IsTree";
    /**
     * 表类型
     */
    public static final String SrcType = "SrcType";
    /**
     * 字典表类型
     */
    public static final String CodeStruct = "CodeStruct";
    /**
     * 表的ID ForSAAS
     */
    public static final String TableID = "TableID";


    ///#region 链接到其他系统获取数据的属性。
    /**
     * 数据源
     */
    public static final String FK_SFDBSrc = "FK_SFDBSrc";
    /**
     * 数据源表
     */
    public static final String SrcTable = "SrcTable";
    /**
     * 显示的值
     */
    public static final String ColumnValue = "ColumnValue";
    /**
     * 显示的文字
     */
    public static final String ColumnText = "ColumnText";
    /**
     * 父结点值
     */
    public static final String ParentValue = "ParentValue";
    /**
     * 查询语句
     */
    public static final String SelectStatement = "SelectStatement";
    /**
     * 缓存分钟数
     */
    public static final String CashMinute = "CashMinute";
    /**
     * 最近缓存的时间
     */
    public static final String RootVal = "RootVal";
    /**
     * 加入日期
     */
    public static final String RDT = "RDT";


    ///#endregion 链接到其他系统获取数据的属性。
}
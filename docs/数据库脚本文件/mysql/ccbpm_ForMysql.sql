/*
Navicat MySQL Data Transfer

Source Server         : 3307
Source Server Version : 50622
Source Host           : localhost:3307
Source Database       : ccbpm

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2019-04-28 10:41:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `gpm_app`
-- ----------------------------
DROP TABLE IF EXISTS `gpm_app`;
CREATE TABLE `gpm_app` (
  `No` varchar(30) NOT NULL COMMENT '编号',
  `AppModel` int(11) DEFAULT '0' COMMENT '应用类型',
  `Name` text COMMENT '名称',
  `FK_AppSort` varchar(100) DEFAULT NULL COMMENT '类别',
  `IsEnable` int(11) DEFAULT '1' COMMENT '是否启用',
  `Url` text COMMENT '默认连接',
  `SubUrl` text COMMENT '第二连接',
  `UidControl` varchar(100) DEFAULT NULL COMMENT '用户名控件',
  `PwdControl` varchar(100) DEFAULT NULL COMMENT '密码控件',
  `ActionType` int(11) DEFAULT '0' COMMENT '提交类型',
  `SSOType` int(11) DEFAULT '0' COMMENT '登录方式',
  `OpenWay` int(11) DEFAULT '0' COMMENT '打开方式',
  `RefMenuNo` text COMMENT '关联菜单编号',
  `AppRemark` varchar(500) DEFAULT NULL COMMENT '备注',
  `Idx` int(11) DEFAULT '0' COMMENT '显示顺序',
  `MyFileName` varchar(300) DEFAULT NULL COMMENT 'ICON',
  `MyFilePath` varchar(300) DEFAULT NULL COMMENT 'MyFilePath',
  `MyFileExt` varchar(20) DEFAULT NULL COMMENT 'MyFileExt',
  `WebPath` varchar(300) DEFAULT NULL COMMENT 'WebPath',
  `MyFileH` int(11) DEFAULT '0' COMMENT 'MyFileH',
  `MyFileW` int(11) DEFAULT '0' COMMENT 'MyFileW',
  `MyFileSize` float DEFAULT NULL COMMENT 'MyFileSize',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统';

-- ----------------------------
-- Records of gpm_app
-- ----------------------------

-- ----------------------------
-- Table structure for `gpm_appsort`
-- ----------------------------
DROP TABLE IF EXISTS `gpm_appsort`;
CREATE TABLE `gpm_appsort` (
  `No` varchar(2) NOT NULL COMMENT '编号',
  `Name` varchar(300) DEFAULT NULL COMMENT '名称',
  `Idx` int(11) DEFAULT '0' COMMENT '显示顺序',
  `RefMenuNo` varchar(300) DEFAULT NULL COMMENT '关联的菜单编号',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统类别';

-- ----------------------------
-- Records of gpm_appsort
-- ----------------------------

-- ----------------------------
-- Table structure for `gpm_bar`
-- ----------------------------
DROP TABLE IF EXISTS `gpm_bar`;
CREATE TABLE `gpm_bar` (
  `No` varchar(200) NOT NULL COMMENT '编号',
  `Name` text COMMENT '名称',
  `Title` text COMMENT '标题',
  `OpenWay` int(11) DEFAULT '0' COMMENT '打开方式',
  `IsLine` int(11) DEFAULT '0' COMMENT '是否独占一行',
  `MoreUrl` text COMMENT '更多标签Url',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='信息块';

-- ----------------------------
-- Records of gpm_bar
-- ----------------------------

-- ----------------------------
-- Table structure for `gpm_baremp`
-- ----------------------------
DROP TABLE IF EXISTS `gpm_baremp`;
CREATE TABLE `gpm_baremp` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Bar` varchar(90) DEFAULT NULL COMMENT '信息块编号',
  `FK_Emp` varchar(90) DEFAULT NULL COMMENT '人员编号',
  `Title` text COMMENT '标题',
  `IsShow` int(11) DEFAULT '1' COMMENT '是否显示',
  `Idx` int(11) DEFAULT '0' COMMENT '显示顺序',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='人员信息块';

-- ----------------------------
-- Records of gpm_baremp
-- ----------------------------

-- ----------------------------
-- Table structure for `gpm_empapp`
-- ----------------------------
DROP TABLE IF EXISTS `gpm_empapp`;
CREATE TABLE `gpm_empapp` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Emp` varchar(50) DEFAULT NULL COMMENT '操作员',
  `FK_App` varchar(50) DEFAULT NULL COMMENT '系统',
  `Name` text COMMENT '系统-名称',
  `Url` text COMMENT '连接',
  `MyFileName` varchar(300) DEFAULT NULL COMMENT '图标',
  `MyFilePath` varchar(300) DEFAULT NULL COMMENT 'MyFilePath',
  `MyFileExt` varchar(20) DEFAULT NULL COMMENT 'MyFileExt',
  `WebPath` varchar(300) DEFAULT NULL COMMENT 'WebPath',
  `MyFileH` int(11) DEFAULT '0' COMMENT 'MyFileH',
  `MyFileW` int(11) DEFAULT '0' COMMENT 'MyFileW',
  `MyFileSize` float DEFAULT NULL COMMENT 'MyFileSize',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员与系统权限';

-- ----------------------------
-- Records of gpm_empapp
-- ----------------------------

-- ----------------------------
-- Table structure for `gpm_empmenu`
-- ----------------------------
DROP TABLE IF EXISTS `gpm_empmenu`;
CREATE TABLE `gpm_empmenu` (
  `FK_Menu` varchar(50) NOT NULL COMMENT '菜单',
  `FK_Emp` varchar(100) NOT NULL COMMENT '菜单功能',
  `IsChecked` int(11) DEFAULT '1' COMMENT '是否选中',
  PRIMARY KEY (`FK_Menu`,`FK_Emp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='人员菜单对应';

-- ----------------------------
-- Records of gpm_empmenu
-- ----------------------------

-- ----------------------------
-- Table structure for `gpm_group`
-- ----------------------------
DROP TABLE IF EXISTS `gpm_group`;
CREATE TABLE `gpm_group` (
  `No` varchar(3) NOT NULL COMMENT '编号',
  `Name` varchar(300) DEFAULT NULL COMMENT '名称',
  `Idx` int(11) DEFAULT '0' COMMENT '显示顺序',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限组';

-- ----------------------------
-- Records of gpm_group
-- ----------------------------

-- ----------------------------
-- Table structure for `gpm_groupemp`
-- ----------------------------
DROP TABLE IF EXISTS `gpm_groupemp`;
CREATE TABLE `gpm_groupemp` (
  `FK_Group` varchar(50) NOT NULL COMMENT '权限组',
  `FK_Emp` varchar(100) NOT NULL COMMENT '人员',
  PRIMARY KEY (`FK_Group`,`FK_Emp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限组人员';

-- ----------------------------
-- Records of gpm_groupemp
-- ----------------------------

-- ----------------------------
-- Table structure for `gpm_groupmenu`
-- ----------------------------
DROP TABLE IF EXISTS `gpm_groupmenu`;
CREATE TABLE `gpm_groupmenu` (
  `FK_Group` varchar(50) NOT NULL COMMENT '权限组',
  `FK_Menu` varchar(50) NOT NULL COMMENT '菜单',
  `IsChecked` int(11) DEFAULT '1' COMMENT '是否选中',
  PRIMARY KEY (`FK_Group`,`FK_Menu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限组菜单';

-- ----------------------------
-- Records of gpm_groupmenu
-- ----------------------------

-- ----------------------------
-- Table structure for `gpm_groupstation`
-- ----------------------------
DROP TABLE IF EXISTS `gpm_groupstation`;
CREATE TABLE `gpm_groupstation` (
  `FK_Group` varchar(50) NOT NULL COMMENT '权限组',
  `FK_Station` varchar(100) NOT NULL COMMENT '岗位',
  PRIMARY KEY (`FK_Group`,`FK_Station`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限组岗位';

-- ----------------------------
-- Records of gpm_groupstation
-- ----------------------------

-- ----------------------------
-- Table structure for `gpm_menu`
-- ----------------------------
DROP TABLE IF EXISTS `gpm_menu`;
CREATE TABLE `gpm_menu` (
  `No` varchar(90) NOT NULL COMMENT '功能编号',
  `ParentNo` varchar(100) DEFAULT NULL COMMENT '父节点',
  `Name` varchar(300) DEFAULT NULL COMMENT '名称',
  `Idx` int(11) DEFAULT '0' COMMENT '顺序号',
  `MenuType` int(11) DEFAULT '0' COMMENT '菜单类型',
  `FK_App` varchar(100) DEFAULT NULL COMMENT '系统',
  `OpenWay` int(11) DEFAULT '1' COMMENT '打开方式',
  `Url` text COMMENT '连接',
  `IsEnable` int(11) DEFAULT '1' COMMENT '是否启用?',
  `Icon` varchar(500) DEFAULT NULL COMMENT 'Icon',
  `MenuCtrlWay` int(11) DEFAULT '0' COMMENT '控制方式',
  `Flag` varchar(500) DEFAULT NULL COMMENT '标记',
  `Tag1` varchar(500) DEFAULT NULL COMMENT 'Tag1',
  `Tag2` varchar(500) DEFAULT NULL COMMENT 'Tag2',
  `Tag3` varchar(500) DEFAULT NULL COMMENT 'Tag3',
  `MyFileName` varchar(300) DEFAULT NULL COMMENT '图标',
  `MyFilePath` varchar(300) DEFAULT NULL COMMENT 'MyFilePath',
  `MyFileExt` varchar(20) DEFAULT NULL COMMENT 'MyFileExt',
  `WebPath` varchar(300) DEFAULT NULL COMMENT 'WebPath',
  `MyFileH` int(11) DEFAULT '0' COMMENT 'MyFileH',
  `MyFileW` int(11) DEFAULT '0' COMMENT 'MyFileW',
  `MyFileSize` float DEFAULT NULL COMMENT 'MyFileSize',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统菜单';

-- ----------------------------
-- Records of gpm_menu
-- ----------------------------

-- ----------------------------
-- Table structure for `gpm_persetting`
-- ----------------------------
DROP TABLE IF EXISTS `gpm_persetting`;
CREATE TABLE `gpm_persetting` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Emp` varchar(200) DEFAULT NULL COMMENT '人员',
  `FK_App` varchar(200) DEFAULT NULL COMMENT '系统',
  `UserNo` varchar(200) DEFAULT NULL COMMENT 'UserNo',
  `UserPass` varchar(200) DEFAULT NULL COMMENT 'UserPass',
  `Idx` int(11) DEFAULT '0' COMMENT '显示顺序',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人设置';

-- ----------------------------
-- Records of gpm_persetting
-- ----------------------------

-- ----------------------------
-- Table structure for `gpm_stationmenu`
-- ----------------------------
DROP TABLE IF EXISTS `gpm_stationmenu`;
CREATE TABLE `gpm_stationmenu` (
  `FK_Station` varchar(100) NOT NULL COMMENT '岗位',
  `FK_Menu` varchar(50) NOT NULL COMMENT '菜单',
  `IsChecked` int(11) DEFAULT '1' COMMENT '是否选中',
  PRIMARY KEY (`FK_Station`,`FK_Menu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='岗位菜单';

-- ----------------------------
-- Records of gpm_stationmenu
-- ----------------------------

-- ----------------------------
-- Table structure for `port_dept`
-- ----------------------------
DROP TABLE IF EXISTS `port_dept`;
CREATE TABLE `port_dept` (
  `No` varchar(50) NOT NULL COMMENT '编号',
  `Name` varchar(100) DEFAULT NULL COMMENT '名称',
  `NameOfPath` varchar(300) DEFAULT NULL COMMENT '部门路径',
  `ParentNo` varchar(100) DEFAULT NULL COMMENT '父节点编号',
  `Idx` int(11) DEFAULT '0' COMMENT '顺序号',
  `OrgNo` varchar(50) DEFAULT '' COMMENT '隶属组织',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门';

-- ----------------------------
-- Records of port_dept
-- ----------------------------
INSERT INTO `port_dept` VALUES ('100', '集团总部', null, '0', '0', '');
INSERT INTO `port_dept` VALUES ('1001', '集团市场部', null, '100', '0', '');
INSERT INTO `port_dept` VALUES ('1002', '集团研发部', null, '100', '0', '');
INSERT INTO `port_dept` VALUES ('1003', '集团服务部', null, '100', '0', '');
INSERT INTO `port_dept` VALUES ('1004', '集团财务部', null, '100', '0', '');
INSERT INTO `port_dept` VALUES ('1005', '集团人力资源部', null, '100', '0', '');
INSERT INTO `port_dept` VALUES ('1060', '南方分公司', null, '100', '0', '');
INSERT INTO `port_dept` VALUES ('1061', '市场部', null, '1060', '0', '');
INSERT INTO `port_dept` VALUES ('1062', '财务部', null, '1060', '0', '');
INSERT INTO `port_dept` VALUES ('1063', '销售部', null, '1060', '0', '');
INSERT INTO `port_dept` VALUES ('1070', '北方分公司', null, '100', '0', '');
INSERT INTO `port_dept` VALUES ('1071', '市场部', null, '1070', '0', '');
INSERT INTO `port_dept` VALUES ('1072', '财务部', null, '1070', '0', '');
INSERT INTO `port_dept` VALUES ('1073', '销售部', null, '1070', '0', '');
INSERT INTO `port_dept` VALUES ('1099', '外来单位', null, '100', '0', '');

-- ----------------------------
-- Table structure for `port_deptemp`
-- ----------------------------
DROP TABLE IF EXISTS `port_deptemp`;
CREATE TABLE `port_deptemp` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Dept` varchar(50) DEFAULT NULL COMMENT '部门',
  `FK_Emp` varchar(100) DEFAULT NULL COMMENT '操作员',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门人员信息';

-- ----------------------------
-- Records of port_deptemp
-- ----------------------------
INSERT INTO `port_deptemp` VALUES ('1001_zhanghaicheng', '1001', 'zhanghaicheng');
INSERT INTO `port_deptemp` VALUES ('1001_zhangyifan', '1001', 'zhangyifan');
INSERT INTO `port_deptemp` VALUES ('1001_zhoushengyu', '1001', 'zhoushengyu');
INSERT INTO `port_deptemp` VALUES ('1002_qifenglin', '1002', 'qifenglin');
INSERT INTO `port_deptemp` VALUES ('1002_zhoutianjiao', '1002', 'zhoutianjiao');
INSERT INTO `port_deptemp` VALUES ('1003_fuhui', '1003', 'fuhui');
INSERT INTO `port_deptemp` VALUES ('1003_guoxiangbin', '1003', 'guoxiangbin');
INSERT INTO `port_deptemp` VALUES ('1004_guobaogeng', '1004', 'guobaogeng');
INSERT INTO `port_deptemp` VALUES ('1004_yangyilei', '1004', 'yangyilei');
INSERT INTO `port_deptemp` VALUES ('1005_liping', '1005', 'liping');
INSERT INTO `port_deptemp` VALUES ('1005_liyan', '1005', 'liyan');
INSERT INTO `port_deptemp` VALUES ('100_zhoupeng', '100', 'zhoupeng');

-- ----------------------------
-- Table structure for `port_deptempstation`
-- ----------------------------
DROP TABLE IF EXISTS `port_deptempstation`;
CREATE TABLE `port_deptempstation` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Dept` varchar(50) DEFAULT NULL COMMENT '部门',
  `FK_Station` varchar(50) DEFAULT NULL COMMENT '岗位',
  `FK_Emp` varchar(50) DEFAULT NULL COMMENT '操作员',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门岗位人员对应';

-- ----------------------------
-- Records of port_deptempstation
-- ----------------------------
INSERT INTO `port_deptempstation` VALUES ('1001_zhanghaicheng_02', '1001', '02', 'zhanghaicheng');
INSERT INTO `port_deptempstation` VALUES ('1001_zhangyifan_07', '1001', '07', 'zhangyifan');
INSERT INTO `port_deptempstation` VALUES ('1001_zhoushengyu_07', '1001', '07', 'zhoushengyu');
INSERT INTO `port_deptempstation` VALUES ('1002_qifenglin_03', '1002', '03', 'qifenglin');
INSERT INTO `port_deptempstation` VALUES ('1002_zhoutianjiao_08', '1002', '08', 'zhoutianjiao');
INSERT INTO `port_deptempstation` VALUES ('1003_fuhui_09', '1003', '09', 'fuhui');
INSERT INTO `port_deptempstation` VALUES ('1003_guoxiangbin_04', '1003', '04', 'guoxiangbin');
INSERT INTO `port_deptempstation` VALUES ('1004_guobaogeng_10', '1004', '10', 'guobaogeng');
INSERT INTO `port_deptempstation` VALUES ('1004_yangyilei_05', '1004', '05', 'yangyilei');
INSERT INTO `port_deptempstation` VALUES ('1005_liping_06', '1005', '06', 'liping');
INSERT INTO `port_deptempstation` VALUES ('1005_liyan_11', '1005', '11', 'liyan');
INSERT INTO `port_deptempstation` VALUES ('100_zhoupeng_01', '100', '01', 'zhoupeng');
INSERT INTO `port_deptempstation` VALUES ('1099_Guest_12', '1005', '12', 'Guest');

-- ----------------------------
-- Table structure for `port_deptstation`
-- ----------------------------
DROP TABLE IF EXISTS `port_deptstation`;
CREATE TABLE `port_deptstation` (
  `FK_Dept` varchar(15) NOT NULL COMMENT '部门',
  `FK_Station` varchar(100) NOT NULL COMMENT '岗位',
  PRIMARY KEY (`FK_Dept`,`FK_Station`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门岗位对应';

-- ----------------------------
-- Records of port_deptstation
-- ----------------------------
INSERT INTO `port_deptstation` VALUES ('100', '01');
INSERT INTO `port_deptstation` VALUES ('1001', '07');
INSERT INTO `port_deptstation` VALUES ('1002', '08');
INSERT INTO `port_deptstation` VALUES ('1003', '09');
INSERT INTO `port_deptstation` VALUES ('1004', '10');
INSERT INTO `port_deptstation` VALUES ('1005', '11');

-- ----------------------------
-- Table structure for `port_emp`
-- ----------------------------
DROP TABLE IF EXISTS `port_emp`;
CREATE TABLE `port_emp` (
  `No` varchar(150) NOT NULL COMMENT '登陆账号',
  `Name` varchar(200) DEFAULT NULL COMMENT '名称',
  `Pass` varchar(100) DEFAULT NULL COMMENT '密码',
  `FK_Dept` varchar(100) DEFAULT NULL COMMENT '主要部门',
  `SID` varchar(36) DEFAULT NULL COMMENT '安全校验码',
  `Tel` varchar(20) DEFAULT NULL COMMENT '电话',
  `Email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `PinYin` varchar(500) DEFAULT NULL COMMENT '拼音',
  `SignType` int(11) DEFAULT '0' COMMENT '签字类型',
  `Idx` int(11) DEFAULT '0' COMMENT '序号',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';

-- ----------------------------
-- Records of port_emp
-- ----------------------------
INSERT INTO `port_emp` VALUES ('admin', 'admin', '123', '100', '', '0531-82374939', 'zhoupeng@ccflow.org', ',admin,admin,', '0', '0');
INSERT INTO `port_emp` VALUES ('fuhui', '福惠', '123', '1003', '', '0531-82374939', 'fuhui@ccflow.org', ',fuhui,fh,fh/jtfwb,', '0', '0');
INSERT INTO `port_emp` VALUES ('guobaogeng', '郭宝庚', '123', '1004', '', '0531-82374939', 'guobaogeng@ccflow.org', ',guobaogeng,gbg,gbg/jtcwb,', '0', '0');
INSERT INTO `port_emp` VALUES ('guoxiangbin', '郭祥斌', '123', '1003', '', '0531-82374939', 'guoxiangbin@ccflow.org', ',guoxiangbin,gxb,gxb/jtfwb,', '0', '0');
INSERT INTO `port_emp` VALUES ('liping', '李萍', '123', '1005', '', '0531-82374939', 'liping@ccflow.org', ',liping,lp,lp/jtrlzyb,', '0', '0');
INSERT INTO `port_emp` VALUES ('liyan', '李言', '123', '1005', '', '0531-82374939', 'liyan@ccflow.org', ',liyan,ly,ly/jtrlzyb,', '0', '0');
INSERT INTO `port_emp` VALUES ('qifenglin', '祁凤林', '123', '1002', '', '0531-82374939', 'qifenglin@ccflow.org', ',qifenglin,qfl,qfl/jtyfb,', '0', '0');
INSERT INTO `port_emp` VALUES ('yangyilei', '杨依雷', '123', '1004', '', '0531-82374939', 'yangyilei@ccflow.org', ',yangyilei,yyl,yyl/jtcwb,', '0', '0');
INSERT INTO `port_emp` VALUES ('zhanghaicheng', '张海成', '123', '1001', '', '0531-82374939', 'zhanghaicheng@ccflow.org', ',zhanghaicheng,zhc,zhc/jtscb,', '0', '0');
INSERT INTO `port_emp` VALUES ('zhangyifan', '张一帆', '123', '1001', '', '0531-82374939', 'zhangyifan@ccflow.org', ',zhangyifan,zyf,zyf/jtscb,', '0', '0');
INSERT INTO `port_emp` VALUES ('zhoupeng', '周朋', '123', '100', '', '0531-82374939', 'zhoupeng@ccflow.org', ',zhoupeng,zp,zp/jtzb,', '0', '0');
INSERT INTO `port_emp` VALUES ('zhoushengyu', '周升雨', '123', '1001', '', '0531-82374939', 'zhoushengyu@ccflow.org', ',zhoushengyu,zsy,zsy/jtscb,', '0', '0');
INSERT INTO `port_emp` VALUES ('zhoutianjiao', '周天娇', '123', '1002', '', '0531-82374939', 'zhoutianjiao@ccflow.org', ',zhoutianjiao,ztj,ztj/jtyfb,', '0', '0');

-- ----------------------------
-- Table structure for `port_station`
-- ----------------------------
DROP TABLE IF EXISTS `port_station`;
CREATE TABLE `port_station` (
  `No` varchar(8) NOT NULL COMMENT '编号',
  `Name` varchar(100) DEFAULT NULL COMMENT '名称',
  `FK_StationType` varchar(100) DEFAULT NULL COMMENT '类型',
  `OrgNo` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='岗位';

-- ----------------------------
-- Records of port_station
-- ----------------------------
INSERT INTO `port_station` VALUES ('01', '总经理', '1', '0');
INSERT INTO `port_station` VALUES ('02', '市场部经理', '2', '0');
INSERT INTO `port_station` VALUES ('03', '研发部经理', '2', '0');
INSERT INTO `port_station` VALUES ('04', '客服部经理', '2', '0');
INSERT INTO `port_station` VALUES ('05', '财务部经理', '2', '0');
INSERT INTO `port_station` VALUES ('06', '人力资源部经理', '2', '0');
INSERT INTO `port_station` VALUES ('07', '销售人员岗', '3', '0');
INSERT INTO `port_station` VALUES ('08', '程序员岗', '3', '0');
INSERT INTO `port_station` VALUES ('09', '技术服务岗', '3', '0');
INSERT INTO `port_station` VALUES ('10', '出纳岗', '3', '0');
INSERT INTO `port_station` VALUES ('11', '人力资源助理岗', '3', '0');
INSERT INTO `port_station` VALUES ('12', '外来人员岗', '3', '0');

-- ----------------------------
-- Table structure for `port_stationtype`
-- ----------------------------
DROP TABLE IF EXISTS `port_stationtype`;
CREATE TABLE `port_stationtype` (
  `No` varchar(2) NOT NULL COMMENT '编号',
  `Name` varchar(50) DEFAULT NULL COMMENT '名称',
  `Idx` int(11) DEFAULT '0' COMMENT '顺序',
  `OrgNo` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='岗位类型';

-- ----------------------------
-- Records of port_stationtype
-- ----------------------------
INSERT INTO `port_stationtype` VALUES ('1', '高层', '0', null);
INSERT INTO `port_stationtype` VALUES ('2', '中层', '0', null);
INSERT INTO `port_stationtype` VALUES ('3', '基层', '0', null);

-- ----------------------------
-- Table structure for `pub_day`
-- ----------------------------
DROP TABLE IF EXISTS `pub_day`;
CREATE TABLE `pub_day` (
  `No` varchar(30) NOT NULL COMMENT '编号',
  `Name` varchar(60) DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日期';

-- ----------------------------
-- Records of pub_day
-- ----------------------------

-- ----------------------------
-- Table structure for `pub_nd`
-- ----------------------------
DROP TABLE IF EXISTS `pub_nd`;
CREATE TABLE `pub_nd` (
  `No` varchar(30) NOT NULL COMMENT '编号',
  `Name` varchar(60) DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='年度';

-- ----------------------------
-- Records of pub_nd
-- ----------------------------

-- ----------------------------
-- Table structure for `pub_ny`
-- ----------------------------
DROP TABLE IF EXISTS `pub_ny`;
CREATE TABLE `pub_ny` (
  `No` varchar(30) NOT NULL COMMENT '编号',
  `Name` varchar(60) DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='年月';

-- ----------------------------
-- Records of pub_ny
-- ----------------------------

-- ----------------------------
-- Table structure for `pub_yf`
-- ----------------------------
DROP TABLE IF EXISTS `pub_yf`;
CREATE TABLE `pub_yf` (
  `No` varchar(30) NOT NULL COMMENT '编号',
  `Name` varchar(60) DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='月份';

-- ----------------------------
-- Records of pub_yf
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_docfile`
-- ----------------------------
DROP TABLE IF EXISTS `sys_docfile`;
CREATE TABLE `sys_docfile` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FileName` varchar(200) DEFAULT NULL COMMENT '名称',
  `FileSize` int(11) DEFAULT '0' COMMENT '大小',
  `FileType` varchar(50) DEFAULT NULL COMMENT '文件类型',
  `D1` text COMMENT 'D1',
  `D2` text COMMENT 'D2',
  `D3` text COMMENT 'D3',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='备注字段文件管理者';

-- ----------------------------
-- Records of sys_docfile
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_encfg`
-- ----------------------------
DROP TABLE IF EXISTS `sys_encfg`;
CREATE TABLE `sys_encfg` (
  `No` varchar(100) NOT NULL COMMENT '实体名称',
  `GroupTitle` varchar(2000) DEFAULT NULL COMMENT '分组标签',
  `Url` varchar(500) DEFAULT NULL COMMENT '要打开的Url',
  `FJSavePath` varchar(100) DEFAULT NULL COMMENT '保存路径',
  `FJWebPath` varchar(100) DEFAULT NULL COMMENT '附件Web路径',
  `Datan` varchar(200) DEFAULT NULL COMMENT '字段数据分析方式',
  `UI` varchar(2000) DEFAULT NULL COMMENT 'UI设置',
  `AtPara` varchar(3000) DEFAULT NULL COMMENT 'AtPara',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='实体配置';

-- ----------------------------
-- Records of sys_encfg
-- ----------------------------
INSERT INTO `sys_encfg` VALUES ('BP.Sys.FrmUI.FrmAttachmentExt', '@MyPK=基础信息,附件的基本配置.\r\n@DeleteWay=权限控制,控制附件的下载与上传权限.@IsRowLock=WebOffice属性,设置与公文有关系的属性配置.\r\n@IsToHeLiuHZ=流程相关,控制节点附件的分合流.', null, null, null, null, null, null);
INSERT INTO `sys_encfg` VALUES ('BP.WF.CCBill.FrmBill', '@No=基础信息,单据基础配置信息.@BtnNewLable=单据按钮权限,用于控制每个功能按钮启用规则.@BtnImpExcel=列表按钮,列表按钮控制@Designer=设计者,流程开发设计者信息', null, null, null, null, null, null);
INSERT INTO `sys_encfg` VALUES ('BP.WF.Template.FlowExt', '@No=基础信息,基础信息权限信息.@IsBatchStart=数据&表单,数据导入导出.@DesignerNo=设计者,流程开发设计者信息', null, null, null, null, null, null);
INSERT INTO `sys_encfg` VALUES ('BP.WF.Template.FlowSheet', '@No=基本配置@FlowRunWay=启动方式,配置工作流程如何自动发起，该选项要与流程服务一起工作才有效.@StartLimitRole=启动限制规则@StartGuideWay=发起前置导航@CFlowWay=延续流程@DTSWay=流程数据与业务数据同步@PStarter=轨迹查看权限', null, null, null, null, null, null);
INSERT INTO `sys_encfg` VALUES ('BP.WF.Template.FrmNodeComponent', '@NodeID=审核组件,适用于sdk表单审核组件与ccform上的审核组件属性设置.@SFLab=父子流程组件,在该节点上配置与显示父子流程.@FrmThreadLab=子线程组件,对合流节点有效，用于配置与现实子线程运行的情况。@FrmTrackLab=轨迹组件,用于显示流程运行的轨迹图.@FTCLab=流转自定义,在每个节点上自己控制节点的处理人.', null, null, null, null, null, null);
INSERT INTO `sys_encfg` VALUES ('BP.WF.Template.MapDataExt', '@No=基本属性@Designer=设计者信息', null, null, null, null, null, null);
INSERT INTO `sys_encfg` VALUES ('BP.WF.Template.MapDtlExt', '@No=基础信息,基础信息权限信息.@IsExp=数据导入导出,数据导入导出.@MTR=多表头,实现多表头.@IsEnableLink=超链接,显示在从表的右边.@IsCopyNDData=流程相关,与流程相关的配置非流程可以忽略.', null, null, null, null, null, null);
INSERT INTO `sys_encfg` VALUES ('BP.WF.Template.MapFrmFool', '@No=基础属性,基础属性.@Designer=设计者信息,设计者的单位信息，人员信息，可以上传到表单云.', null, null, null, null, null, null);
INSERT INTO `sys_encfg` VALUES ('BP.WF.Template.NodeExt', '@NodeID=基本配置@SendLab=按钮权限,控制工作节点可操作按钮.@RunModel=运行模式,分合流,父子流程@AutoJumpRole0=跳转,自动跳转规则当遇到该节点时如何让其自动的执行下一步.', null, null, null, null, null, null);
INSERT INTO `sys_encfg` VALUES ('BP.WF.Template.NodeSheet', '@NodeID=基本配置@FormType=表单@FWCSta=审核组件,适用于sdk表单审核组件与ccform上的审核组件属性设置.@SFSta=父子流程,对启动，查看父子流程的控件设置.@SendLab=按钮权限,控制工作节点可操作按钮.@RunModel=运行模式,分合流,父子流程@AutoJumpRole0=跳转,自动跳转规则当遇到该节点时如何让其自动的执行下一步.@MPhone_WorkModel=移动,与手机平板电脑相关的应用设置.@OfficeOpen=公文按钮,只有当该节点是公文流程时候有效', null, null, null, null, null, null);

-- ----------------------------
-- Table structure for `sys_enum`
-- ----------------------------
DROP TABLE IF EXISTS `sys_enum`;
CREATE TABLE `sys_enum` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `Lab` varchar(300) DEFAULT NULL COMMENT 'Lab',
  `EnumKey` varchar(100) DEFAULT NULL COMMENT 'EnumKey',
  `IntKey` int(11) DEFAULT '0' COMMENT 'Val',
  `Lang` varchar(10) DEFAULT NULL COMMENT '语言',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='枚举数据';

-- ----------------------------
-- Records of sys_enum
-- ----------------------------
INSERT INTO `sys_enum` VALUES ('ActionType_CH_0', 'GET', 'ActionType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ActionType_CH_1', 'POST', 'ActionType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertType_CH_0', '短信', 'AlertType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertType_CH_1', '邮件', 'AlertType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertType_CH_2', '邮件与短信', 'AlertType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertType_CH_3', '系统(内部)消息', 'AlertType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertWay_CH_0', '不接收', 'AlertWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertWay_CH_1', '短信', 'AlertWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertWay_CH_2', '邮件', 'AlertWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertWay_CH_3', '内部消息', 'AlertWay', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertWay_CH_4', 'QQ消息', 'AlertWay', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertWay_CH_5', 'RTX消息', 'AlertWay', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('AlertWay_CH_6', 'MSN消息', 'AlertWay', '6', 'CH');
INSERT INTO `sys_enum` VALUES ('AppModel_CH_0', 'BS系统', 'AppModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('AppModel_CH_1', 'CS系统', 'AppModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('AppType_CH_0', '外部Url连接', 'AppType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('AppType_CH_1', '本地可执行文件', 'AppType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('AthCtrlWay_CH_0', 'PK-主键', 'AthCtrlWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('AthCtrlWay_CH_1', 'FID-流程ID', 'AthCtrlWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('AthCtrlWay_CH_2', 'ParentID-父流程ID', 'AthCtrlWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('AthCtrlWay_CH_3', '仅能查看自己上传的附件', 'AthCtrlWay', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('AthCtrlWay_CH_4', '按照WorkID计算(对流程节点表单有效)', 'AthCtrlWay', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('AthSaveWay_CH_0', '保存到web服务器', 'AthSaveWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('AthSaveWay_CH_1', '保存到数据库', 'AthSaveWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('AthSaveWay_CH_2', 'ftp服务器', 'AthSaveWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('AthUploadWay_CH_0', '继承模式', 'AthUploadWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('AthUploadWay_CH_1', '协作模式', 'AthUploadWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('AuthorWay_CH_0', '不授权', 'AuthorWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('AuthorWay_CH_1', '全部流程授权', 'AuthorWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('AuthorWay_CH_2', '指定流程授权', 'AuthorWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('BillFrmType_CH_0', '傻瓜表单', 'BillFrmType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('BillFrmType_CH_1', '自由表单', 'BillFrmType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('BillOpenModel_CH_0', '下载本地', 'BillOpenModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('BillOpenModel_CH_1', '在线WebOffice打开', 'BillOpenModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('BillState_CH_0', '空白', 'BillState', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('BillState_CH_1', '草稿', 'BillState', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('BillState_CH_100', '归档', 'BillState', '100', 'CH');
INSERT INTO `sys_enum` VALUES ('BillState_CH_2', '编辑中', 'BillState', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('BillSta_CH_0', '运行中', 'BillSta', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('BillSta_CH_1', '已完成', 'BillSta', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('BillSta_CH_2', '其他', 'BillSta', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('CancelRole_CH_0', '上一步可以撤销', 'CancelRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('CancelRole_CH_1', '不能撤销', 'CancelRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('CancelRole_CH_2', '上一步与开始节点可以撤销', 'CancelRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('CancelRole_CH_3', '指定的节点可以撤销', 'CancelRole', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('CCRole_CH_0', '不能抄送', 'CCRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('CCRole_CH_1', '手工抄送', 'CCRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('CCRole_CH_2', '自动抄送', 'CCRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('CCRole_CH_3', '手工与自动', 'CCRole', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('CCRole_CH_4', '按表单SysCCEmps字段计算', 'CCRole', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('CCRole_CH_5', '在发送前打开抄送窗口', 'CCRole', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('CCWriteTo_CH_0', '写入抄送列表', 'CCWriteTo', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('CCWriteTo_CH_1', '写入待办', 'CCWriteTo', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('CCWriteTo_CH_2', '写入待办与抄送列表', 'CCWriteTo', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('CHAlertRole_CH_0', '不提示', 'CHAlertRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('CHAlertRole_CH_1', '每天1次', 'CHAlertRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('CHAlertRole_CH_2', '每天2次', 'CHAlertRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('CHAlertWay_CH_0', '邮件', 'CHAlertWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('CHAlertWay_CH_1', '短信', 'CHAlertWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('CHAlertWay_CH_2', 'CCIM即时通讯', 'CHAlertWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('ChartType_CH_0', '几何图形', 'ChartType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ChartType_CH_1', '肖像图片', 'ChartType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('CHSta_CH_0', '及时完成', 'CHSta', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('CHSta_CH_1', '按期完成', 'CHSta', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('CHSta_CH_2', '逾期完成', 'CHSta', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('CHSta_CH_3', '超期完成', 'CHSta', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('ColSpanAttrDT_CH_1', '跨1个单元格', 'ColSpanAttrDT', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ColSpanAttrDT_CH_3', '跨3个单元格', 'ColSpanAttrDT', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('ColSpanAttrString_CH_1', '跨1个单元格', 'ColSpanAttrString', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ColSpanAttrString_CH_2', '跨2个单元格', 'ColSpanAttrString', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('ColSpanAttrString_CH_3', '跨3个单元格', 'ColSpanAttrString', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('ColSpanAttrString_CH_4', '跨4个单元格', 'ColSpanAttrString', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('ConfirmKind_CH_0', '当前单元格', 'ConfirmKind', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ConfirmKind_CH_1', '左方单元格', 'ConfirmKind', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ConfirmKind_CH_2', '上方单元格', 'ConfirmKind', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('ConfirmKind_CH_3', '右方单元格', 'ConfirmKind', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('ConfirmKind_CH_4', '下方单元格', 'ConfirmKind', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('ConnJudgeWay_CH_0', 'or', 'ConnJudgeWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ConnJudgeWay_CH_1', 'and', 'ConnJudgeWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('CtrlWay_CH_0', '单个', 'CtrlWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('CtrlWay_CH_1', '多个', 'CtrlWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('CtrlWay_CH_2', '指定', 'CtrlWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('DataStoreModel_CH_0', '数据轨迹模式', 'DataStoreModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DataStoreModel_CH_1', '数据合并模式', 'DataStoreModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DataType_CH_0', '字符串', 'DataType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DataType_CH_1', '整数', 'DataType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DataType_CH_2', '浮点数', 'DataType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('DataType_CH_3', '日期', 'DataType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('DataType_CH_4', '日期时间', 'DataType', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('DataType_CH_5', '外键', 'DataType', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('DataType_CH_6', '枚举', 'DataType', '6', 'CH');
INSERT INTO `sys_enum` VALUES ('DBSrcType_CH_0', '应用系统主数据库(默认)', 'DBSrcType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DBSrcType_CH_1', 'SQLServer数据库', 'DBSrcType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DBSrcType_CH_100', 'WebService数据源', 'DBSrcType', '100', 'CH');
INSERT INTO `sys_enum` VALUES ('DBSrcType_CH_2', 'Oracle数据库', 'DBSrcType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('DBSrcType_CH_3', 'MySQL数据库', 'DBSrcType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('DBSrcType_CH_4', 'Informix数据库', 'DBSrcType', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('DBSrcType_CH_50', 'Dubbo服务', 'DBSrcType', '50', 'CH');
INSERT INTO `sys_enum` VALUES ('DelEnable_CH_0', '不能删除', 'DelEnable', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DelEnable_CH_1', '逻辑删除', 'DelEnable', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DelEnable_CH_2', '记录日志方式删除', 'DelEnable', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('DelEnable_CH_3', '彻底删除', 'DelEnable', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('DelEnable_CH_4', '让用户决定删除方式', 'DelEnable', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('DeleteWay_CH_0', '不能删除', 'DeleteWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DeleteWay_CH_1', '删除所有', 'DeleteWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DeleteWay_CH_2', '只能删除自己上传的', 'DeleteWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('DocType_CH_0', '正式公文', 'DocType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DocType_CH_1', '便函', 'DocType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('Draft_CH_0', '无(不设草稿)', 'Draft', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('Draft_CH_1', '保存到待办', 'Draft', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('Draft_CH_2', '保存到草稿箱', 'Draft', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('DtlAddRecModel_CH_0', '按设置的数量初始化空白行', 'DtlAddRecModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DtlAddRecModel_CH_1', '用按钮增加空白行', 'DtlAddRecModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DtlOpenType_CH_0', '操作员', 'DtlOpenType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DtlOpenType_CH_1', '工作ID', 'DtlOpenType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DtlOpenType_CH_2', '流程ID', 'DtlOpenType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('DtlSaveModel_CH_0', '自动存盘(失去焦点自动存盘)', 'DtlSaveModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DtlSaveModel_CH_1', '手动存盘(保存按钮触发存盘)', 'DtlSaveModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DtlVer_CH_0', '2017传统版', 'DtlVer', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DtlVer_CH_1', '2019EasyUI版本', 'DtlVer', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DTSearchWay_CH_0', '不启用', 'DTSearchWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DTSearchWay_CH_1', '按日期', 'DTSearchWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DTSearchWay_CH_2', '按日期时间', 'DTSearchWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('DTSWay_CH_0', '不考核', 'DTSWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('DTSWay_CH_1', '按照时效考核', 'DTSWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('DTSWay_CH_2', '按照工作量考核', 'DTSWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('EditerType_CH_0', '无编辑器', 'EditerType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('EditerType_CH_1', 'Sina编辑器0', 'EditerType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('EditerType_CH_2', 'FKEditer', 'EditerType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('EditerType_CH_3', 'KindEditor', 'EditerType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('EditerType_CH_4', '百度的UEditor', 'EditerType', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('EnumUIContralType_CH_1', '下拉框', 'EnumUIContralType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('EnumUIContralType_CH_3', '单选按钮', 'EnumUIContralType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('EventType_CH_0', '禁用', 'EventType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('EventType_CH_1', '执行URL', 'EventType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('EventType_CH_2', '执行CCFromRef.js', 'EventType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('ExcelType_CH_0', '普通文件数据提取', 'ExcelType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ExcelType_CH_1', '流程附件数据提取', 'ExcelType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ExpType_CH_3', '按照SQL计算', 'ExpType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('ExpType_CH_4', '按照参数计算', 'ExpType', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('FJOpen_CH_0', '关闭附件', 'FJOpen', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FJOpen_CH_1', '操作员', 'FJOpen', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FJOpen_CH_2', '工作ID', 'FJOpen', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FJOpen_CH_3', '流程ID', 'FJOpen', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('FlowDeleteRole_CH_0', '超级管理员可以删除', 'FlowDeleteRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FlowDeleteRole_CH_1', '分级管理员可以删除', 'FlowDeleteRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FlowDeleteRole_CH_2', '发起人可以删除', 'FlowDeleteRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FlowDeleteRole_CH_3', '节点启动删除按钮的操作员', 'FlowDeleteRole', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('FlowRunWay_CH_0', '手工启动', 'FlowRunWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FlowRunWay_CH_1', '指定人员按时启动', 'FlowRunWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FlowRunWay_CH_2', '数据集按时启动', 'FlowRunWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FlowRunWay_CH_3', '触发式启动', 'FlowRunWay', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('FLRole_CH_0', '按接受人', 'FLRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FLRole_CH_1', '按部门', 'FLRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FLRole_CH_2', '按岗位', 'FLRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmBillWorkModel_CH_0', '独立表单', 'FrmBillWorkModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmBillWorkModel_CH_1', '单据工作模式', 'FrmBillWorkModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmBillWorkModel_CH_2', '流程工作模式', 'FrmBillWorkModel', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmSln_CH_0', '默认方案', 'FrmSln', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmSln_CH_1', '只读方案', 'FrmSln', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmSln_CH_2', '自定义方案', 'FrmSln', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmThreadSta_CH_0', '禁用', 'FrmThreadSta', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmThreadSta_CH_1', '启用', 'FrmThreadSta', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmType_CH_0', '傻瓜表单', 'FrmType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmType_CH_1', '自由表单', 'FrmType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmType_CH_11', '累加表单', 'FrmType', '11', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmType_CH_3', '嵌入式表单', 'FrmType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmType_CH_4', 'Word表单', 'FrmType', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmType_CH_5', '在线编辑模式Excel表单', 'FrmType', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmType_CH_6', 'VSTO模式Excel表单', 'FrmType', '6', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmType_CH_7', '实体类组件', 'FrmType', '7', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmUrlShowWay_CH_0', '不显示', 'FrmUrlShowWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmUrlShowWay_CH_1', '自动大小', 'FrmUrlShowWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmUrlShowWay_CH_2', '指定大小', 'FrmUrlShowWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FrmUrlShowWay_CH_3', '新窗口', 'FrmUrlShowWay', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('FTCWorkModel_CH_0', '简洁模式', 'FTCWorkModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FTCWorkModel_CH_1', '高级模式', 'FTCWorkModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCAth_CH_0', '不启用', 'FWCAth', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCAth_CH_1', '多附件', 'FWCAth', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCAth_CH_2', '单附件(暂不支持)', 'FWCAth', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCAth_CH_3', '图片附件(暂不支持)', 'FWCAth', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCMsgShow_CH_0', '都显示', 'FWCMsgShow', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCMsgShow_CH_1', '仅显示自己的意见', 'FWCMsgShow', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCOrderModel_CH_0', '按审批时间先后排序', 'FWCOrderModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCOrderModel_CH_1', '按照接受人员列表先后顺序(官职大小)', 'FWCOrderModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCShowModel_CH_0', '表格方式', 'FWCShowModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCShowModel_CH_1', '自由模式', 'FWCShowModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCSta_CH_0', '禁用', 'FWCSta', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCSta_CH_1', '启用', 'FWCSta', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('FWCSta_CH_2', '只读', 'FWCSta', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('HuiQianRole_CH_0', '不启用', 'HuiQianRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('HuiQianRole_CH_1', '协作模式', 'HuiQianRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('HuiQianRole_CH_4', '组长模式', 'HuiQianRole', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('HungUpWay_CH_0', '无限挂起', 'HungUpWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('HungUpWay_CH_1', '按指定的时间解除挂起并通知我自己', 'HungUpWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('HungUpWay_CH_2', '按指定的时间解除挂起并通知所有人', 'HungUpWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('ImgSrcType_CH_0', '本地', 'ImgSrcType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ImgSrcType_CH_1', 'URL', 'ImgSrcType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ImpModel_CH_0', '不导入', 'ImpModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ImpModel_CH_1', '按配置模式导入', 'ImpModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ImpModel_CH_2', '按照xls文件模版导入', 'ImpModel', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('IsAutoSendSubFlowOver_CH_0', '不处理', 'IsAutoSendSubFlowOver', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('IsAutoSendSubFlowOver_CH_1', '让父流程自动运行下一步', 'IsAutoSendSubFlowOver', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('IsAutoSendSubFlowOver_CH_2', '结束父流程', 'IsAutoSendSubFlowOver', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('IsSigan_CH_0', '无', 'IsSigan', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('IsSigan_CH_1', '图片签名', 'IsSigan', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('IsSigan_CH_2', '山东CA', 'IsSigan', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('IsSigan_CH_3', '广东CA', 'IsSigan', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('IsSigan_CH_4', '图片盖章', 'IsSigan', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('JMCD_CH_0', '一般', 'JMCD', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('JMCD_CH_1', '保密', 'JMCD', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('JMCD_CH_2', '秘密', 'JMCD', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('JMCD_CH_3', '机密', 'JMCD', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('JumpWay_CH_0', '不能跳转', 'JumpWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('JumpWay_CH_1', '只能向后跳转', 'JumpWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('JumpWay_CH_2', '只能向前跳转', 'JumpWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('JumpWay_CH_3', '任意节点跳转', 'JumpWay', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('JumpWay_CH_4', '按指定规则跳转', 'JumpWay', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('LGType_CH_0', '普通', 'LGType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('LGType_CH_1', '枚举', 'LGType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('LGType_CH_2', '外键', 'LGType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('LGType_CH_3', '打开系统页面', 'LGType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('ListShowModel_CH_0', '表格', 'ListShowModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ListShowModel_CH_1', '卡片', 'ListShowModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('MenuCtrlWay_CH_0', '按照设置的控制', 'MenuCtrlWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('MenuCtrlWay_CH_1', '任何人都可以使用', 'MenuCtrlWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('MenuCtrlWay_CH_2', 'Admin用户可以使用', 'MenuCtrlWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('MenuType_CH_0', '系统根目录', 'MenuType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('MenuType_CH_1', '系统类别', 'MenuType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('MenuType_CH_2', '系统', 'MenuType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('MenuType_CH_3', '目录', 'MenuType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('MenuType_CH_4', '功能/界面', 'MenuType', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('MenuType_CH_5', '功能控制点', 'MenuType', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('MethodDocTypeOfFunc_CH_0', 'SQL', 'MethodDocTypeOfFunc', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('MethodDocTypeOfFunc_CH_1', 'URL', 'MethodDocTypeOfFunc', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('MethodDocTypeOfFunc_CH_2', 'JavaScript', 'MethodDocTypeOfFunc', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('MethodDocTypeOfFunc_CH_3', '业务单元', 'MethodDocTypeOfFunc', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('Model_CH_0', '普通', 'Model', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('Model_CH_1', '固定行', 'Model', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('MoveToShowWay_CH_0', '不显示', 'MoveToShowWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('MoveToShowWay_CH_1', '下拉列表0', 'MoveToShowWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('MoveToShowWay_CH_2', '平铺', 'MoveToShowWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('MsgCtrl_CH_0', '不发送', 'MsgCtrl', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('MsgCtrl_CH_1', '按设置的下一步接受人自动发送（默认）', 'MsgCtrl', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('MsgCtrl_CH_2', '由本节点表单系统字段(IsSendEmail,IsSendSMS)来决定', 'MsgCtrl', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('MsgCtrl_CH_3', '由SDK开发者参数(IsSendEmail,IsSendSMS)来决定', 'MsgCtrl', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDataType_CH_1', '字符串String', 'MyDataType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDataType_CH_2', '整数类型Int', 'MyDataType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDataType_CH_3', '浮点类型AppFloat', 'MyDataType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDataType_CH_4', '判断类型Boolean', 'MyDataType', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDataType_CH_5', '双精度类型Double', 'MyDataType', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDataType_CH_6', '日期型Date', 'MyDataType', '6', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDataType_CH_7', '时间类型Datetime', 'MyDataType', '7', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDataType_CH_8', '金额类型AppMoney', 'MyDataType', '8', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDeptRole_CH_0', '仅部门领导可以查看', 'MyDeptRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDeptRole_CH_1', '部门下所有的人都可以查看', 'MyDeptRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('MyDeptRole_CH_2', '本部门里指定岗位的人可以查看', 'MyDeptRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('OpenWay_CH_0', '新窗口', 'OpenWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('OpenWay_CH_1', '本窗口', 'OpenWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('OpenWay_CH_2', '覆盖新窗口', 'OpenWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('PopValFormat_CH_0', 'No(仅编号)', 'PopValFormat', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('PopValFormat_CH_1', 'Name(仅名称)', 'PopValFormat', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('PopValFormat_CH_2', 'No,Name(编号与名称,比如zhangsan,张三;lisi,李四;)', 'PopValFormat', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('PrintDocEnable_CH_0', '不打印', 'PrintDocEnable', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('PrintDocEnable_CH_1', '打印网页', 'PrintDocEnable', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('PrintDocEnable_CH_2', '打印RTF模板', 'PrintDocEnable', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('PrintDocEnable_CH_3', '打印Word模版', 'PrintDocEnable', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('PrintPDFModle_CH_0', '全部打印', 'PrintPDFModle', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('PrintPDFModle_CH_1', '单个表单打印(针对树形表单)', 'PrintPDFModle', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('PRI_CH_0', '低', 'PRI', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('PRI_CH_1', '中', 'PRI', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('PRI_CH_2', '高', 'PRI', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('PushWay_CH_0', '按照指定节点的工作人员', 'PushWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('PushWay_CH_1', '按照指定的工作人员', 'PushWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('PushWay_CH_2', '按照指定的工作岗位', 'PushWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('PushWay_CH_3', '按照指定的部门', 'PushWay', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('PushWay_CH_4', '按照指定的SQL', 'PushWay', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('PushWay_CH_5', '按照系统指定的字段', 'PushWay', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('QRModel_CH_0', '不生成', 'QRModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('QRModel_CH_1', '生成二维码', 'QRModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('RBShowModel_CH_0', '竖向', 'RBShowModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('RBShowModel_CH_3', '横向', 'RBShowModel', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('ReadReceipts_CH_0', '不回执', 'ReadReceipts', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ReadReceipts_CH_1', '自动回执', 'ReadReceipts', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ReadReceipts_CH_2', '由上一节点表单字段决定', 'ReadReceipts', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('ReadReceipts_CH_3', '由SDK开发者参数决定', 'ReadReceipts', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('ReadRole_CH_0', '不控制', 'ReadRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ReadRole_CH_1', '未阅读阻止发送', 'ReadRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ReadRole_CH_2', '未阅读做记录', 'ReadRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('RefMethodTypeLink_CH_1', '模态窗口打开', 'RefMethodTypeLink', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('RefMethodTypeLink_CH_2', '新窗口打开', 'RefMethodTypeLink', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('RefMethodTypeLink_CH_3', '右侧窗口打开', 'RefMethodTypeLink', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('RefMethodType_CH_0', '功能', 'RefMethodType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('RefMethodType_CH_1', '模态窗口打开', 'RefMethodType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('RefMethodType_CH_2', '新窗口打开', 'RefMethodType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('RefMethodType_CH_3', '右侧窗口打开', 'RefMethodType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('RefMethodType_CH_4', '实体集合的功能', 'RefMethodType', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('ReturnOneNodeRole_CH_0', '不启用', 'ReturnOneNodeRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ReturnOneNodeRole_CH_1', '按照[退回信息填写字段]作为退回意见直接退回', 'ReturnOneNodeRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ReturnOneNodeRole_CH_2', '按照[审核组件]填写的信息作为退回意见直接退回', 'ReturnOneNodeRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('ReturnRole_CH_0', '不能退回', 'ReturnRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ReturnRole_CH_1', '只能退回上一个节点', 'ReturnRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ReturnRole_CH_2', '可退回以前任意节点', 'ReturnRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('ReturnRole_CH_3', '可退回指定的节点', 'ReturnRole', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('ReturnRole_CH_4', '由流程图设计的退回路线决定', 'ReturnRole', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('ReturnSendModel_CH_0', '从退回节点正常执行', 'ReturnSendModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ReturnSendModel_CH_1', '直接发送到当前节点', 'ReturnSendModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ReturnSendModel_CH_2', '直接发送到当前节点的下一个节点', 'ReturnSendModel', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('RowSpanAttrString_CH_1', '跨1行', 'RowSpanAttrString', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('RowSpanAttrString_CH_2', '跨2行', 'RowSpanAttrString', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('RowSpanAttrString_CH_3', '跨3行', 'RowSpanAttrString', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('SaveModel_CH_0', '仅节点表', 'SaveModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SaveModel_CH_1', '节点表与Rpt表', 'SaveModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SearchUrlOpenType_CH_0', 'En.htm', 'SearchUrlOpenType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SearchUrlOpenType_CH_1', 'EnOnly.htm', 'SearchUrlOpenType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SearchUrlOpenType_CH_2', '自定义url', 'SearchUrlOpenType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('SFOpenType_CH_0', '工作查看器', 'SFOpenType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SFOpenType_CH_1', '傻瓜表单轨迹查看器', 'SFOpenType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SFShowCtrl_CH_0', '可以看所有的子流程', 'SFShowCtrl', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SFShowCtrl_CH_1', '仅仅可以看自己发起的子流程', 'SFShowCtrl', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SFShowModel_CH_0', '表格方式', 'SFShowModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SFShowModel_CH_1', '自由模式', 'SFShowModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SFSta_CH_0', '禁用', 'SFSta', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SFSta_CH_1', '启用', 'SFSta', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SFSta_CH_2', '只读', 'SFSta', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('SharingType_CH_0', '共享', 'SharingType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SharingType_CH_1', '私有', 'SharingType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ShowModel_CH_0', '按钮', 'ShowModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ShowModel_CH_1', '超链接', 'ShowModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ShowWhere_CH_0', '树形表单', 'ShowWhere', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ShowWhere_CH_1', '工具栏', 'ShowWhere', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SignType_CH_0', '不签名', 'SignType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SignType_CH_1', '图片签名', 'SignType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SignType_CH_2', '电子签名', 'SignType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('SQLType_CH_0', '方向条件', 'SQLType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SQLType_CH_1', '接受人规则', 'SQLType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SQLType_CH_2', '下拉框数据过滤', 'SQLType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('SQLType_CH_3', '级联下拉框', 'SQLType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('SQLType_CH_4', 'PopVal开窗返回值', 'SQLType', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('SQLType_CH_5', '人员选择器人员选择范围', 'SQLType', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('SSOType_CH_0', 'SID验证', 'SSOType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SSOType_CH_1', '连接', 'SSOType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SSOType_CH_2', '表单提交', 'SSOType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('SSOType_CH_3', '不传值', 'SSOType', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('SubFlowStartWay_CH_0', '不启动', 'SubFlowStartWay', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SubFlowStartWay_CH_1', '指定的字段启动', 'SubFlowStartWay', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('SubFlowStartWay_CH_2', '按明细表启动', 'SubFlowStartWay', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('SubThreadType_CH_0', '同表单', 'SubThreadType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('SubThreadType_CH_1', '异表单', 'SubThreadType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('TabType_CH_0', '本地表或视图', 'TabType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('TabType_CH_1', '通过一个SQL确定的一个外部数据源', 'TabType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('TabType_CH_2', '通过WebServices获得的一个数据源', 'TabType', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('Target_CH_0', '新窗口', 'Target', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('Target_CH_1', '本窗口', 'Target', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('Target_CH_2', '父窗口', 'Target', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('TaskSta_CH_0', '未开始', 'TaskSta', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('TaskSta_CH_1', '进行中', 'TaskSta', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('TaskSta_CH_2', '完成', 'TaskSta', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('TaskSta_CH_3', '推迟', 'TaskSta', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('TemplateFileModel_CH_0', 'rtf模版', 'TemplateFileModel', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('TemplateFileModel_CH_1', 'vsto模式的word模版', 'TemplateFileModel', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('TemplateFileModel_CH_2', 'vsto模式的excel模版', 'TemplateFileModel', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('ThreadKillRole_CH_0', '不能删除', 'ThreadKillRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ThreadKillRole_CH_1', '手工删除', 'ThreadKillRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ThreadKillRole_CH_2', '自动删除', 'ThreadKillRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('TimelineRole_CH_0', '按节点(由节点属性来定义)', 'TimelineRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('TimelineRole_CH_1', '按发起人(开始节点SysSDTOfFlow字段计算)', 'TimelineRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('ToobarExcType_CH_0', '超链接', 'ToobarExcType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('ToobarExcType_CH_1', '函数', 'ToobarExcType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('TSpan_CH_0', '本周', 'TSpan', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('TSpan_CH_1', '上周', 'TSpan', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('TSpan_CH_2', '上上周', 'TSpan', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('TSpan_CH_3', '更早', 'TSpan', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('UIRowStyleGlo_CH_0', '无风格', 'UIRowStyleGlo', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('UIRowStyleGlo_CH_1', '交替风格', 'UIRowStyleGlo', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('UIRowStyleGlo_CH_2', '鼠标移动', 'UIRowStyleGlo', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('UIRowStyleGlo_CH_3', '交替并鼠标移动', 'UIRowStyleGlo', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('UploadFileCheck_CH_0', '不控制', 'UploadFileCheck', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('UploadFileCheck_CH_1', '上传附件个数不能为0', 'UploadFileCheck', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('UploadFileCheck_CH_2', '每个类别下面的个数不能为0', 'UploadFileCheck', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('UploadFileNumCheck_CH_0', '不用校验', 'UploadFileNumCheck', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('UploadFileNumCheck_CH_1', '不能为空', 'UploadFileNumCheck', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('UploadFileNumCheck_CH_2', '每个类别下不能为空', 'UploadFileNumCheck', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('UrlSrcType_CH_0', '自定义', 'UrlSrcType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('UrlSrcType_CH_1', '表单库', 'UrlSrcType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('UserType_CH_0', '普通用户', 'UserType', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('UserType_CH_1', '管理员用户', 'UserType', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('UseSta_CH_0', '禁用', 'UseSta', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('UseSta_CH_1', '启用', 'UseSta', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('WFStateApp_CH_10', '批处理', 'WFStateApp', '10', 'CH');
INSERT INTO `sys_enum` VALUES ('WFStateApp_CH_2', '运行中', 'WFStateApp', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('WFStateApp_CH_3', '已完成', 'WFStateApp', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('WFStateApp_CH_4', '挂起', 'WFStateApp', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('WFStateApp_CH_5', '退回', 'WFStateApp', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('WFStateApp_CH_6', '转发', 'WFStateApp', '6', 'CH');
INSERT INTO `sys_enum` VALUES ('WFStateApp_CH_7', '删除', 'WFStateApp', '7', 'CH');
INSERT INTO `sys_enum` VALUES ('WFStateApp_CH_8', '加签', 'WFStateApp', '8', 'CH');
INSERT INTO `sys_enum` VALUES ('WFStateApp_CH_9', '冻结', 'WFStateApp', '9', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_0', '空白', 'WFState', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_1', '草稿', 'WFState', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_10', '批处理', 'WFState', '10', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_11', '加签回复状态', 'WFState', '11', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_2', '运行中', 'WFState', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_3', '已完成', 'WFState', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_4', '挂起', 'WFState', '4', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_5', '退回', 'WFState', '5', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_6', '转发', 'WFState', '6', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_7', '删除', 'WFState', '7', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_8', '加签', 'WFState', '8', 'CH');
INSERT INTO `sys_enum` VALUES ('WFState_CH_9', '冻结', 'WFState', '9', 'CH');
INSERT INTO `sys_enum` VALUES ('WFSta_CH_0', '运行中', 'WFSta', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('WFSta_CH_1', '已完成', 'WFSta', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('WFSta_CH_2', '其他', 'WFSta', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('WhatAreYouTodo_CH_0', '关闭提示窗口', 'WhatAreYouTodo', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('WhatAreYouTodo_CH_1', '关闭提示窗口并刷新', 'WhatAreYouTodo', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('WhatAreYouTodo_CH_2', '转入到Search.htm页面上去', 'WhatAreYouTodo', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('WhenOverSize_CH_0', '不处理', 'WhenOverSize', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('WhenOverSize_CH_1', '向下顺增行', 'WhenOverSize', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('WhenOverSize_CH_2', '次页显示', 'WhenOverSize', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('WhoExeIt_CH_0', '操作员执行', 'WhoExeIt', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('WhoExeIt_CH_1', '机器执行', 'WhoExeIt', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('WhoExeIt_CH_2', '混合执行', 'WhoExeIt', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('WhoIsPK_CH_0', 'WorkID是主键', 'WhoIsPK', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('WhoIsPK_CH_1', 'FID是主键(干流程的WorkID)', 'WhoIsPK', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('WhoIsPK_CH_2', '父流程ID是主键', 'WhoIsPK', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('WhoIsPK_CH_3', '延续流程ID是主键', 'WhoIsPK', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('YBFlowReturnRole_CH_0', '不能退回', 'YBFlowReturnRole', '0', 'CH');
INSERT INTO `sys_enum` VALUES ('YBFlowReturnRole_CH_1', '退回到父流程的开始节点', 'YBFlowReturnRole', '1', 'CH');
INSERT INTO `sys_enum` VALUES ('YBFlowReturnRole_CH_2', '退回到父流程的任何节点', 'YBFlowReturnRole', '2', 'CH');
INSERT INTO `sys_enum` VALUES ('YBFlowReturnRole_CH_3', '退回父流程的启动节点', 'YBFlowReturnRole', '3', 'CH');
INSERT INTO `sys_enum` VALUES ('YBFlowReturnRole_CH_4', '可退回到指定的节点', 'YBFlowReturnRole', '4', 'CH');

-- ----------------------------
-- Table structure for `sys_enummain`
-- ----------------------------
DROP TABLE IF EXISTS `sys_enummain`;
CREATE TABLE `sys_enummain` (
  `No` varchar(40) NOT NULL COMMENT '编号',
  `Name` varchar(40) DEFAULT NULL COMMENT '名称',
  `CfgVal` varchar(1500) DEFAULT NULL COMMENT '配置信息',
  `Lang` varchar(10) DEFAULT NULL COMMENT '语言',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='枚举';

-- ----------------------------
-- Records of sys_enummain
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_enver`
-- ----------------------------
DROP TABLE IF EXISTS `sys_enver`;
CREATE TABLE `sys_enver` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `No` varchar(50) DEFAULT NULL COMMENT '实体类',
  `Name` varchar(100) DEFAULT NULL COMMENT '实体名',
  `PKValue` varchar(300) DEFAULT NULL COMMENT '主键值',
  `EVer` int(11) DEFAULT '1' COMMENT '版本号',
  `Rec` varchar(100) DEFAULT NULL COMMENT '修改人',
  `RDT` varchar(50) DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='实体版本号';

-- ----------------------------
-- Records of sys_enver
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_enverdtl`
-- ----------------------------
DROP TABLE IF EXISTS `sys_enverdtl`;
CREATE TABLE `sys_enverdtl` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `EnName` varchar(200) DEFAULT NULL COMMENT '实体名',
  `EnVerPK` varchar(100) DEFAULT NULL COMMENT '版本主表PK',
  `AttrKey` varchar(100) DEFAULT NULL COMMENT '字段',
  `AttrName` varchar(200) DEFAULT NULL COMMENT '字段名',
  `OldVal` varchar(100) DEFAULT NULL COMMENT '旧值',
  `NewVal` varchar(100) DEFAULT NULL COMMENT '新值',
  `EnVer` int(11) DEFAULT '1' COMMENT '版本号(日期)',
  `RDT` varchar(50) DEFAULT NULL COMMENT '日期',
  `Rec` varchar(100) DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='实体修改明细';

-- ----------------------------
-- Records of sys_enverdtl
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_excelfield`
-- ----------------------------
DROP TABLE IF EXISTS `sys_excelfield`;
CREATE TABLE `sys_excelfield` (
  `No` varchar(36) NOT NULL COMMENT '编号',
  `Name` varchar(50) DEFAULT NULL COMMENT '名称',
  `CellName` varchar(50) DEFAULT NULL COMMENT '单元格名称',
  `CellRow` int(11) DEFAULT '0' COMMENT '行号',
  `CellColumn` int(11) DEFAULT '0' COMMENT '列号',
  `FK_ExcelSheet` varchar(100) DEFAULT NULL COMMENT '所属ExcelSheet表',
  `Field` varchar(50) DEFAULT NULL COMMENT '存储字段名',
  `FK_ExcelTable` varchar(100) DEFAULT NULL COMMENT '存储数据表',
  `DataType` int(11) DEFAULT '0' COMMENT '值类型',
  `UIBindKey` varchar(100) DEFAULT NULL COMMENT '外键表/枚举',
  `UIRefKey` varchar(30) DEFAULT NULL COMMENT '外键表No',
  `UIRefKeyText` varchar(30) DEFAULT NULL COMMENT '外键表Name',
  `Validators` text COMMENT '校验器',
  `FK_ExcelFile` varchar(100) DEFAULT NULL COMMENT '所属Excel模板',
  `AtPara` text COMMENT '参数',
  `ConfirmKind` int(11) DEFAULT '0' COMMENT '单元格确认方式',
  `ConfirmCellCount` int(11) DEFAULT '1' COMMENT '单元格确认方向移动量',
  `ConfirmCellValue` varchar(200) DEFAULT NULL COMMENT '对应单元格值',
  `ConfirmRepeatIndex` int(11) DEFAULT '0' COMMENT '对应单元格值重复选定次序',
  `SkipIsNull` int(11) DEFAULT '0' COMMENT '不计非空',
  `SyncToField` varchar(100) DEFAULT NULL COMMENT '同步到字段',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Excel字段';

-- ----------------------------
-- Records of sys_excelfield
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_excelfile`
-- ----------------------------
DROP TABLE IF EXISTS `sys_excelfile`;
CREATE TABLE `sys_excelfile` (
  `No` varchar(36) NOT NULL COMMENT '编号',
  `Name` varchar(50) DEFAULT NULL COMMENT '名称',
  `Mark` varchar(50) DEFAULT NULL COMMENT '标识',
  `ExcelType` int(11) DEFAULT '0' COMMENT '类型',
  `Note` text COMMENT '上传说明',
  `MyFileName` varchar(300) DEFAULT NULL COMMENT '模板文件',
  `MyFilePath` varchar(300) DEFAULT NULL COMMENT 'MyFilePath',
  `MyFileExt` varchar(20) DEFAULT NULL COMMENT 'MyFileExt',
  `WebPath` varchar(300) DEFAULT NULL COMMENT 'WebPath',
  `MyFileH` int(11) DEFAULT '0' COMMENT 'MyFileH',
  `MyFileW` int(11) DEFAULT '0' COMMENT 'MyFileW',
  `MyFileSize` float DEFAULT NULL COMMENT 'MyFileSize',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Excel模板';

-- ----------------------------
-- Records of sys_excelfile
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_excelsheet`
-- ----------------------------
DROP TABLE IF EXISTS `sys_excelsheet`;
CREATE TABLE `sys_excelsheet` (
  `No` varchar(36) NOT NULL COMMENT 'No',
  `Name` varchar(50) DEFAULT NULL COMMENT 'Sheet名称',
  `FK_ExcelFile` varchar(100) DEFAULT NULL COMMENT 'Excel模板',
  `SheetIndex` int(11) DEFAULT '0' COMMENT 'Sheet索引',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='ExcelSheet';

-- ----------------------------
-- Records of sys_excelsheet
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_exceltable`
-- ----------------------------
DROP TABLE IF EXISTS `sys_exceltable`;
CREATE TABLE `sys_exceltable` (
  `No` varchar(36) NOT NULL COMMENT '编号',
  `Name` varchar(50) DEFAULT NULL COMMENT '数据表名',
  `FK_ExcelFile` varchar(100) DEFAULT NULL COMMENT 'Excel模板',
  `IsDtl` int(11) DEFAULT '0' COMMENT '是否明细表',
  `Note` text COMMENT '数据表说明',
  `SyncToTable` varchar(100) DEFAULT NULL COMMENT '同步到表',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Excel数据表';

-- ----------------------------
-- Records of sys_exceltable
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_filemanager`
-- ----------------------------
DROP TABLE IF EXISTS `sys_filemanager`;
CREATE TABLE `sys_filemanager` (
  `OID` int(11) NOT NULL COMMENT 'OID',
  `AttrFileName` varchar(50) DEFAULT NULL COMMENT '指定名称',
  `AttrFileNo` varchar(50) DEFAULT NULL COMMENT '指定编号',
  `EnName` varchar(50) DEFAULT NULL COMMENT '关联的表',
  `RefVal` varchar(50) DEFAULT NULL COMMENT '主键值',
  `WebPath` varchar(100) DEFAULT NULL COMMENT 'Web路径',
  `MyFileName` varchar(300) DEFAULT NULL COMMENT '文件名称',
  `MyFilePath` varchar(300) DEFAULT NULL COMMENT 'MyFilePath',
  `MyFileExt` varchar(20) DEFAULT NULL COMMENT 'MyFileExt',
  `MyFileH` int(11) DEFAULT '0' COMMENT 'MyFileH',
  `MyFileW` int(11) DEFAULT '0' COMMENT 'MyFileW',
  `MyFileSize` float DEFAULT NULL COMMENT 'MyFileSize',
  `RDT` varchar(50) DEFAULT NULL COMMENT '上传时间',
  `Rec` varchar(50) DEFAULT NULL COMMENT '上传人',
  `Doc` text COMMENT '内容',
  PRIMARY KEY (`OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件管理者';

-- ----------------------------
-- Records of sys_filemanager
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_formtree`
-- ----------------------------
DROP TABLE IF EXISTS `sys_formtree`;
CREATE TABLE `sys_formtree` (
  `No` varchar(10) NOT NULL COMMENT '编号',
  `Name` varchar(100) DEFAULT NULL COMMENT '名称',
  `ParentNo` varchar(100) DEFAULT NULL COMMENT '父节点No',
  `OrgNo` varchar(100) DEFAULT NULL COMMENT '组织编号',
  `Idx` int(11) DEFAULT '0' COMMENT 'Idx',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表单树';

-- ----------------------------
-- Records of sys_formtree
-- ----------------------------
INSERT INTO `sys_formtree` VALUES ('1', '表单库', '0', null, '0');

-- ----------------------------
-- Table structure for `sys_frmattachment`
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmattachment`;
CREATE TABLE `sys_frmattachment` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT '表单ID',
  `NoOfObj` varchar(50) DEFAULT NULL COMMENT '附件标识',
  `FK_Node` int(11) DEFAULT '0' COMMENT '节点控制(对sln有效)',
  `AthRunModel` int(11) DEFAULT '0' COMMENT '运行模式',
  `Name` varchar(50) DEFAULT NULL COMMENT '附件名称',
  `Exts` varchar(50) DEFAULT NULL COMMENT '文件格式',
  `NumOfUpload` int(11) DEFAULT '0' COMMENT '最低上传数量',
  `UploadFileNumCheck` int(11) DEFAULT '0' COMMENT '上传校验方式',
  `AthSaveWay` int(11) DEFAULT '0' COMMENT '保存方式',
  `SaveTo` varchar(150) DEFAULT NULL COMMENT '保存到',
  `Sort` varchar(500) DEFAULT NULL COMMENT '类别',
  `IsTurn2Html` int(11) DEFAULT '0' COMMENT '是否转换成html(方便手机浏览)',
  `X` float DEFAULT NULL COMMENT 'X',
  `Y` float DEFAULT NULL COMMENT 'Y',
  `W` float DEFAULT NULL COMMENT '宽度',
  `H` float DEFAULT NULL COMMENT '高度',
  `IsVisable` int(11) DEFAULT '1' COMMENT '是否显示附件分组',
  `DeleteWay` int(11) DEFAULT '0' COMMENT '附件删除规则',
  `IsUpload` int(11) DEFAULT '1' COMMENT '是否可以上传',
  `IsDownload` int(11) DEFAULT '1' COMMENT '是否可以下载',
  `IsOrder` int(11) DEFAULT '0' COMMENT '是否可以排序',
  `IsAutoSize` int(11) DEFAULT '1' COMMENT '自动控制大小',
  `IsNote` int(11) DEFAULT '1' COMMENT '是否增加备注',
  `IsExpCol` int(11) DEFAULT '1' COMMENT '是否启用扩展列',
  `IsShowTitle` int(11) DEFAULT '1' COMMENT '是否显示标题列',
  `UploadType` int(11) DEFAULT '0' COMMENT '上传类型',
  `AthUploadWay` int(11) DEFAULT '0' COMMENT '控制上传控制方式',
  `CtrlWay` int(11) DEFAULT '0' COMMENT '控制呈现控制方式',
  `IsRowLock` int(11) DEFAULT '1' COMMENT '是否启用锁定行',
  `IsWoEnableWF` int(11) DEFAULT '1' COMMENT '是否启用weboffice',
  `IsWoEnableSave` int(11) DEFAULT '1' COMMENT '是否启用保存',
  `IsWoEnableReadonly` int(11) DEFAULT '1' COMMENT '是否只读',
  `IsWoEnableRevise` int(11) DEFAULT '1' COMMENT '是否启用修订',
  `IsWoEnableViewKeepMark` int(11) DEFAULT '1' COMMENT '是否查看用户留痕',
  `IsWoEnablePrint` int(11) DEFAULT '1' COMMENT '是否打印',
  `IsWoEnableSeal` int(11) DEFAULT '1' COMMENT '是否启用签章',
  `IsWoEnableOver` int(11) DEFAULT '1' COMMENT '是否启用套红',
  `IsWoEnableTemplete` int(11) DEFAULT '1' COMMENT '是否启用公文模板',
  `IsWoEnableCheck` int(11) DEFAULT '1' COMMENT '是否自动写入审核信息',
  `IsWoEnableInsertFlow` int(11) DEFAULT '1' COMMENT '是否插入流程',
  `IsWoEnableInsertFengXian` int(11) DEFAULT '1' COMMENT '是否插入风险点',
  `IsWoEnableMarks` int(11) DEFAULT '1' COMMENT '是否启用留痕模式',
  `IsWoEnableDown` int(11) DEFAULT '1' COMMENT '是否启用下载',
  `IsToHeLiuHZ` int(11) DEFAULT '1' COMMENT '该附件是否要汇总到合流节点上去？(对子线程节点有效)',
  `IsHeLiuHuiZong` int(11) DEFAULT '1' COMMENT '是否是合流节点的汇总附件组件？(对合流节点有效)',
  `DataRefNoOfObj` varchar(150) DEFAULT NULL COMMENT '对应附件标识',
  `ReadRole` int(11) DEFAULT '0' COMMENT '阅读规则',
  `AtPara` varchar(3000) DEFAULT NULL COMMENT 'AtPara',
  `GroupID` int(11) DEFAULT '0' COMMENT 'GroupID',
  `GUID` varchar(128) DEFAULT '' COMMENT 'GUID',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='附件';

-- ----------------------------
-- Records of sys_frmattachment
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_frmattachmentdb`
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmattachmentdb`;
CREATE TABLE `sys_frmattachmentdb` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT 'FK_MapData',
  `FK_FrmAttachment` varchar(500) DEFAULT NULL COMMENT '附件主键',
  `NoOfObj` varchar(50) DEFAULT NULL COMMENT '附件标识',
  `RefPKVal` varchar(50) DEFAULT NULL COMMENT '实体主键',
  `FID` int(11) DEFAULT '0' COMMENT 'FID',
  `NodeID` varchar(50) DEFAULT NULL COMMENT '节点ID',
  `Sort` varchar(200) DEFAULT NULL COMMENT '类别',
  `FileFullName` varchar(700) DEFAULT NULL COMMENT '文件路径',
  `FileName` varchar(500) DEFAULT NULL COMMENT '名称',
  `FileExts` varchar(50) DEFAULT NULL COMMENT '扩展',
  `FileSize` float DEFAULT NULL COMMENT '文件大小',
  `RDT` varchar(50) DEFAULT NULL COMMENT '记录日期',
  `Rec` varchar(50) DEFAULT NULL COMMENT '记录人',
  `RecName` varchar(50) DEFAULT NULL COMMENT '记录人名字',
  `MyNote` text COMMENT '备注',
  `IsRowLock` int(11) DEFAULT '0' COMMENT '是否锁定行',
  `Idx` int(11) DEFAULT '0' COMMENT '排序',
  `UploadGUID` varchar(500) DEFAULT NULL COMMENT '上传GUID',
  `AtPara` varchar(3000) DEFAULT NULL COMMENT 'AtPara',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='附件数据存储';

-- ----------------------------
-- Records of sys_frmattachmentdb
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_frmbtn`
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmbtn`;
CREATE TABLE `sys_frmbtn` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT '表单ID',
  `Text` text COMMENT '标签',
  `IsView` int(11) DEFAULT '0' COMMENT '是否可见',
  `IsEnable` int(11) DEFAULT '0' COMMENT '是否起用',
  `UAC` int(11) DEFAULT '0' COMMENT '控制类型',
  `UACContext` text COMMENT '控制内容',
  `EventType` int(11) DEFAULT '0' COMMENT '事件类型',
  `EventContext` text COMMENT '事件内容',
  `MsgOK` varchar(500) DEFAULT NULL COMMENT '运行成功提示',
  `MsgErr` varchar(500) DEFAULT NULL COMMENT '运行失败提示',
  `BtnID` varchar(128) DEFAULT NULL COMMENT '按钮ID',
  `GroupID` varchar(50) DEFAULT NULL COMMENT '所在分组',
  `GroupIDText` varchar(200) DEFAULT NULL COMMENT '所在分组',
  `X` float(11,2) DEFAULT '5.00' COMMENT 'X',
  `Y` float(11,2) DEFAULT '5.00' COMMENT 'Y',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='按钮';

-- ----------------------------
-- Records of sys_frmbtn
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_frmele`
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmele`;
CREATE TABLE `sys_frmele` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT '表单ID',
  `EleType` varchar(50) DEFAULT NULL COMMENT '类型',
  `EleID` varchar(50) DEFAULT NULL COMMENT '控件ID值(对部分控件有效)',
  `EleName` varchar(200) DEFAULT NULL COMMENT '控件名称(对部分控件有效)',
  `X` float DEFAULT NULL COMMENT 'X',
  `Y` float DEFAULT NULL COMMENT 'Y',
  `H` float DEFAULT NULL COMMENT 'H',
  `W` float DEFAULT NULL COMMENT 'W',
  `Tag1` varchar(50) DEFAULT NULL COMMENT '链接URL',
  `Tag2` varchar(50) DEFAULT NULL COMMENT 'Tag2',
  `Tag3` varchar(50) DEFAULT NULL COMMENT 'Tag3',
  `Tag4` varchar(50) DEFAULT NULL COMMENT 'Tag4',
  `GUID` varchar(128) DEFAULT NULL COMMENT 'GUID',
  `IsEnable` int(11) DEFAULT '1' COMMENT '是否启用',
  `AtPara` text COMMENT 'AtPara',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表单元素扩展';

-- ----------------------------
-- Records of sys_frmele
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_frmeledb`
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmeledb`;
CREATE TABLE `sys_frmeledb` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT 'FK_MapData',
  `EleID` varchar(50) DEFAULT NULL COMMENT 'EleID',
  `RefPKVal` varchar(50) DEFAULT NULL COMMENT 'RefPKVal',
  `FID` int(11) DEFAULT '0' COMMENT 'FID',
  `Tag1` varchar(1000) DEFAULT NULL COMMENT 'Tag1',
  `Tag2` varchar(1000) DEFAULT NULL COMMENT 'Tag2',
  `Tag3` varchar(1000) DEFAULT NULL COMMENT 'Tag3',
  `Tag4` varchar(1000) DEFAULT NULL COMMENT 'Tag4',
  `Tag5` varchar(1000) DEFAULT NULL COMMENT 'Tag5',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表单元素扩展DB';

-- ----------------------------
-- Records of sys_frmeledb
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_frmevent`
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmevent`;
CREATE TABLE `sys_frmevent` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Event` varchar(400) DEFAULT NULL COMMENT '事件名称',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT '表单ID',
  `FK_Flow` varchar(100) DEFAULT NULL COMMENT '流程编号',
  `FK_Node` int(11) DEFAULT '0' COMMENT '节点ID',
  `EventDoType` int(11) DEFAULT '0' COMMENT '事件类型',
  `DoDoc` varchar(400) DEFAULT NULL COMMENT '执行内容',
  `MsgOK` varchar(400) DEFAULT NULL COMMENT '成功执行提示',
  `MsgError` varchar(400) DEFAULT NULL COMMENT '异常信息提示',
  `MsgCtrl` int(11) DEFAULT '0' COMMENT '消息发送控制',
  `MailEnable` int(11) DEFAULT '1' COMMENT '是否启用邮件发送？(如果启用就要设置邮件模版，支持ccflow表达式。)',
  `MailTitle` varchar(200) DEFAULT NULL COMMENT '邮件标题模版',
  `MailDoc` text COMMENT '邮件内容模版',
  `SMSEnable` int(11) DEFAULT '0' COMMENT '是否启用短信发送？(如果启用就要设置短信模版，支持ccflow表达式。)',
  `SMSDoc` text COMMENT '短信内容模版',
  `MobilePushEnable` int(11) DEFAULT '1' COMMENT '是否推送到手机、pad端。',
  `AtPara` text COMMENT 'AtPara',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='事件';

-- ----------------------------
-- Records of sys_frmevent
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_frmimg`
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmimg`;
CREATE TABLE `sys_frmimg` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `ImgSrcType` int(11) DEFAULT '0' COMMENT '装饰图片来源',
  `ImgURL` varchar(200) DEFAULT NULL COMMENT '装饰图片URL',
  `ImgPath` varchar(200) DEFAULT NULL COMMENT '装饰图片路径',
  `LinkURL` varchar(200) DEFAULT NULL COMMENT '连接到URL',
  `LinkTarget` varchar(200) DEFAULT NULL COMMENT '连接目标',
  `Tag0` varchar(500) DEFAULT NULL COMMENT '参数',
  `Name` varchar(500) DEFAULT NULL COMMENT '中文名称',
  `EnPK` varchar(500) DEFAULT NULL COMMENT '英文名称',
  `GUID` varchar(128) DEFAULT NULL COMMENT 'GUID',
  `GroupID` varchar(50) DEFAULT NULL COMMENT '所在分组',
  `GroupIDText` varchar(200) DEFAULT NULL COMMENT '所在分组',
  `FK_MapData` varchar(100) DEFAULT '' COMMENT 'FK_MapData',
  `ImgAppType` int(11) DEFAULT '0' COMMENT '应用类型',
  `X` float(11,2) DEFAULT '5.00' COMMENT 'X',
  `Y` float(11,2) DEFAULT '5.00' COMMENT 'Y',
  `H` float(11,2) DEFAULT '200.00' COMMENT 'H',
  `W` float(11,2) DEFAULT '160.00' COMMENT 'W',
  `IsEdit` int(11) DEFAULT '0' COMMENT '是否可以编辑',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='图片';

-- ----------------------------
-- Records of sys_frmimg
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_frmimgath`
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmimgath`;
CREATE TABLE `sys_frmimgath` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT '表单ID',
  `CtrlID` varchar(200) DEFAULT NULL COMMENT '控件ID',
  `Name` varchar(200) DEFAULT NULL COMMENT '中文名称',
  `X` float DEFAULT NULL COMMENT 'X',
  `Y` float DEFAULT NULL COMMENT 'Y',
  `H` float DEFAULT NULL COMMENT 'H',
  `W` float DEFAULT NULL COMMENT 'W',
  `IsEdit` int(11) DEFAULT '1' COMMENT '是否可编辑',
  `IsRequired` int(11) DEFAULT '0' COMMENT '是否必填项',
  `GUID` varchar(128) DEFAULT '' COMMENT 'GUID',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='图片附件';

-- ----------------------------
-- Records of sys_frmimgath
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_frmimgathdb`
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmimgathdb`;
CREATE TABLE `sys_frmimgathdb` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT '表单ID',
  `FK_FrmImgAth` varchar(50) DEFAULT NULL COMMENT '图片附件编号',
  `RefPKVal` varchar(50) DEFAULT NULL COMMENT '实体主键',
  `FileFullName` varchar(700) DEFAULT NULL COMMENT '文件全路径',
  `FileName` varchar(500) DEFAULT NULL COMMENT '名称',
  `FileExts` varchar(50) DEFAULT NULL COMMENT '扩展名',
  `FileSize` float DEFAULT NULL COMMENT '文件大小',
  `RDT` varchar(50) DEFAULT NULL COMMENT '记录日期',
  `Rec` varchar(50) DEFAULT NULL COMMENT '记录人',
  `RecName` varchar(50) DEFAULT NULL COMMENT '记录人名字',
  `MyNote` text COMMENT '备注',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='剪切图片附件数据存储';

-- ----------------------------
-- Records of sys_frmimgathdb
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_frmlab`
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmlab`;
CREATE TABLE `sys_frmlab` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT 'FK_MapData',
  `Text` text COMMENT 'Label',
  `X` float DEFAULT NULL COMMENT 'X',
  `Y` float DEFAULT NULL COMMENT 'Y',
  `FontSize` int(11) DEFAULT '12' COMMENT '字体大小',
  `FontColor` varchar(50) DEFAULT NULL COMMENT '颜色',
  `FontName` varchar(50) DEFAULT NULL COMMENT '字体名称',
  `FontStyle` varchar(200) DEFAULT NULL COMMENT '字体风格',
  `FontWeight` varchar(50) DEFAULT NULL COMMENT '字体宽度',
  `IsBold` int(11) DEFAULT '0' COMMENT '是否粗体',
  `IsItalic` int(11) DEFAULT '0' COMMENT '是否斜体',
  `GUID` varchar(128) DEFAULT NULL COMMENT 'GUID',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='标签';

-- ----------------------------
-- Records of sys_frmlab
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_frmline`
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmline`;
CREATE TABLE `sys_frmline` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT '主表',
  `X1` float DEFAULT NULL COMMENT 'X1',
  `Y1` float DEFAULT NULL COMMENT 'Y1',
  `X2` float DEFAULT NULL COMMENT 'X2',
  `Y2` float DEFAULT NULL COMMENT 'Y2',
  `X` float DEFAULT NULL COMMENT 'X',
  `Y` float DEFAULT NULL COMMENT 'Y',
  `BorderWidth` float DEFAULT NULL COMMENT '宽度',
  `BorderColor` varchar(30) DEFAULT NULL COMMENT '颜色',
  `GUID` varchar(128) DEFAULT NULL COMMENT '初始的GUID',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='线';

-- ----------------------------
-- Records of sys_frmline
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_frmlink`
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmlink`;
CREATE TABLE `sys_frmlink` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT 'FK_MapData',
  `Text` varchar(500) DEFAULT NULL COMMENT 'Label',
  `URL` varchar(500) DEFAULT NULL COMMENT 'URL',
  `Target` varchar(20) DEFAULT NULL COMMENT 'Target',
  `X` float DEFAULT NULL COMMENT 'X',
  `Y` float DEFAULT NULL COMMENT 'Y',
  `FontSize` int(11) DEFAULT '12' COMMENT 'FontSize',
  `FontColor` varchar(50) DEFAULT NULL COMMENT 'FontColor',
  `FontName` varchar(50) DEFAULT NULL COMMENT 'FontName',
  `FontStyle` varchar(50) DEFAULT NULL COMMENT 'FontStyle',
  `IsBold` int(11) DEFAULT '0' COMMENT 'IsBold',
  `IsItalic` int(11) DEFAULT '0' COMMENT 'IsItalic',
  `GUID` varchar(128) DEFAULT NULL COMMENT 'GUID',
  `GroupID` varchar(50) DEFAULT '0' COMMENT '显示的分组',
  `GroupIDText` varchar(200) DEFAULT '0' COMMENT '显示的分组',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='超连接';

-- ----------------------------
-- Records of sys_frmlink
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_frmrb`
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmrb`;
CREATE TABLE `sys_frmrb` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_MapData` varchar(300) DEFAULT NULL COMMENT '表单ID',
  `KeyOfEn` varchar(300) DEFAULT NULL COMMENT '字段',
  `EnumKey` varchar(30) DEFAULT NULL COMMENT '枚举值',
  `Lab` varchar(500) DEFAULT NULL COMMENT '标签',
  `IntKey` int(11) DEFAULT '0' COMMENT 'IntKey',
  `UIIsEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `X` float DEFAULT NULL COMMENT 'X',
  `Y` float DEFAULT NULL COMMENT 'Y',
  `Script` text COMMENT '要执行的脚本',
  `FieldsCfg` text COMMENT '配置信息@FieldName=Sta',
  `SetVal` varchar(200) DEFAULT NULL COMMENT '设置的值',
  `Tip` varchar(1000) DEFAULT NULL COMMENT '选择后提示的信息',
  `GUID` varchar(128) DEFAULT NULL COMMENT 'GUID',
  `AtPara` text,
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='单选框';

-- ----------------------------
-- Records of sys_frmrb
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_frmreportfield`
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmreportfield`;
CREATE TABLE `sys_frmreportfield` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT '表单编号',
  `KeyOfEn` varchar(100) DEFAULT NULL COMMENT '字段名',
  `Name` varchar(200) DEFAULT NULL COMMENT '显示中文名',
  `UIWidth` varchar(100) DEFAULT NULL COMMENT '宽度',
  `UIVisible` int(11) DEFAULT '1' COMMENT '是否显示',
  `Idx` int(11) DEFAULT '0' COMMENT '显示顺序',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表单报表';

-- ----------------------------
-- Records of sys_frmreportfield
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_frmrpt`
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmrpt`;
CREATE TABLE `sys_frmrpt` (
  `No` varchar(20) NOT NULL COMMENT '编号',
  `Name` varchar(50) DEFAULT NULL COMMENT '描述',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT '主表',
  `PTable` varchar(30) DEFAULT NULL COMMENT '物理表',
  `SQLOfColumn` varchar(300) DEFAULT NULL COMMENT '列的数据源',
  `SQLOfRow` varchar(300) DEFAULT NULL COMMENT '行数据源',
  `RowIdx` int(11) DEFAULT '99' COMMENT '位置',
  `GroupID` int(11) DEFAULT '0' COMMENT 'GroupID',
  `IsShowSum` int(11) DEFAULT '1' COMMENT 'IsShowSum',
  `IsShowIdx` int(11) DEFAULT '1' COMMENT 'IsShowIdx',
  `IsCopyNDData` int(11) DEFAULT '1' COMMENT 'IsCopyNDData',
  `IsHLDtl` int(11) DEFAULT '0' COMMENT '是否是合流汇总',
  `IsReadonly` int(11) DEFAULT '0' COMMENT 'IsReadonly',
  `IsShowTitle` int(11) DEFAULT '1' COMMENT 'IsShowTitle',
  `IsView` int(11) DEFAULT '1' COMMENT '是否可见',
  `IsExp` int(11) DEFAULT '1' COMMENT 'IsExp',
  `IsImp` int(11) DEFAULT '1' COMMENT 'IsImp',
  `IsInsert` int(11) DEFAULT '1' COMMENT 'IsInsert',
  `IsDelete` int(11) DEFAULT '1' COMMENT 'IsDelete',
  `IsUpdate` int(11) DEFAULT '1' COMMENT 'IsUpdate',
  `IsEnablePass` int(11) DEFAULT '0' COMMENT '是否启用通过审核功能?',
  `IsEnableAthM` int(11) DEFAULT '0' COMMENT '是否启用多附件',
  `IsEnableM2M` int(11) DEFAULT '0' COMMENT '是否启用M2M',
  `IsEnableM2MM` int(11) DEFAULT '0' COMMENT '是否启用M2M',
  `WhenOverSize` int(11) DEFAULT '0' COMMENT 'WhenOverSize',
  `DtlOpenType` int(11) DEFAULT '1' COMMENT '数据开放类型',
  `EditModel` int(11) DEFAULT '0' COMMENT '显示格式',
  `X` float DEFAULT NULL COMMENT 'X',
  `Y` float DEFAULT NULL COMMENT 'Y',
  `H` float DEFAULT NULL COMMENT 'H',
  `W` float DEFAULT NULL COMMENT 'W',
  `FrmW` float DEFAULT NULL COMMENT 'FrmW',
  `FrmH` float DEFAULT NULL COMMENT 'FrmH',
  `MTR` varchar(3000) DEFAULT NULL COMMENT '多表头列',
  `GUID` varchar(128) DEFAULT NULL COMMENT 'GUID',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='纬度报表';

-- ----------------------------
-- Records of sys_frmrpt
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_frmsln`
-- ----------------------------
DROP TABLE IF EXISTS `sys_frmsln`;
CREATE TABLE `sys_frmsln` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Flow` varchar(4) DEFAULT NULL COMMENT '流程编号',
  `FK_Node` int(11) DEFAULT '0' COMMENT '节点',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT '表单ID',
  `KeyOfEn` varchar(200) DEFAULT NULL COMMENT '字段',
  `Name` varchar(500) DEFAULT NULL COMMENT '字段名',
  `EleType` varchar(20) DEFAULT NULL COMMENT '类型',
  `UIIsEnable` int(11) DEFAULT '1' COMMENT '是否可用',
  `UIVisible` int(11) DEFAULT '1' COMMENT '是否可见',
  `IsSigan` int(11) DEFAULT '0' COMMENT '是否签名',
  `IsNotNull` int(11) DEFAULT '0' COMMENT '是否为空',
  `RegularExp` varchar(500) DEFAULT NULL COMMENT '正则表达式',
  `IsWriteToFlowTable` int(11) DEFAULT '0' COMMENT '是否写入流程表',
  `IsWriteToGenerWorkFlow` int(11) DEFAULT '0' COMMENT '是否写入流程注册表',
  `DefVal` varchar(200) DEFAULT NULL COMMENT '默认值',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表单字段方案';

-- ----------------------------
-- Records of sys_frmsln
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_glovar`
-- ----------------------------
DROP TABLE IF EXISTS `sys_glovar`;
CREATE TABLE `sys_glovar` (
  `No` varchar(50) NOT NULL COMMENT '键',
  `Name` varchar(120) DEFAULT NULL COMMENT '名称',
  `Val` text,
  `GroupKey` varchar(120) DEFAULT NULL COMMENT '分组值',
  `Note` text COMMENT '备注',
  `Idx` int(11) DEFAULT '0' COMMENT '顺序号',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='全局变量';

-- ----------------------------
-- Records of sys_glovar
-- ----------------------------
INSERT INTO `sys_glovar` VALUES ('0', '选择系统约定默认值', null, 'DefVal', null, '0');
INSERT INTO `sys_glovar` VALUES ('@CurrWorker', '当前工作可处理人员', null, 'DefVal', null, '0');
INSERT INTO `sys_glovar` VALUES ('@FK_ND', '当前年度', null, 'DefVal', null, '0');
INSERT INTO `sys_glovar` VALUES ('@FK_YF', '当前月份', null, 'DefVal', null, '0');
INSERT INTO `sys_glovar` VALUES ('@WebUser.FK_Dept', '登陆人员部门编号', null, 'DefVal', null, '0');
INSERT INTO `sys_glovar` VALUES ('@WebUser.FK_DeptFullName', '登陆人员部门全称', null, 'DefVal', null, '0');
INSERT INTO `sys_glovar` VALUES ('@WebUser.FK_DeptName', '登陆人员部门名称', null, 'DefVal', null, '0');
INSERT INTO `sys_glovar` VALUES ('@WebUser.Name', '登陆人员名称', null, 'DefVal', null, '0');
INSERT INTO `sys_glovar` VALUES ('@WebUser.No', '登陆人员账号', null, 'DefVal', null, '0');
INSERT INTO `sys_glovar` VALUES ('@yyyy年mm月dd日', '当前日期(yyyy年MM月dd日)', null, 'DefVal', null, '0');
INSERT INTO `sys_glovar` VALUES ('@yyyy年mm月dd日HH时mm分', '当前日期(yyyy年MM月dd日HH时mm分)', null, 'DefVal', null, '0');
INSERT INTO `sys_glovar` VALUES ('@yy年mm月dd日', '当前日期(yy年MM月dd日)', null, 'DefVal', null, '0');
INSERT INTO `sys_glovar` VALUES ('@yy年mm月dd日HH时mm分', '当前日期(yy年MM月dd日HH时mm分)', null, 'DefVal', null, '0');

-- ----------------------------
-- Table structure for `sys_groupenstemplate`
-- ----------------------------
DROP TABLE IF EXISTS `sys_groupenstemplate`;
CREATE TABLE `sys_groupenstemplate` (
  `OID` int(11) NOT NULL COMMENT 'OID',
  `EnName` varchar(500) DEFAULT NULL COMMENT '表称',
  `Name` varchar(500) DEFAULT NULL COMMENT '报表名',
  `EnsName` varchar(90) DEFAULT NULL COMMENT '报表类名',
  `OperateCol` varchar(90) DEFAULT NULL COMMENT '操作属性',
  `Attrs` varchar(90) DEFAULT NULL COMMENT '运算属性',
  `Rec` varchar(90) DEFAULT NULL COMMENT '记录人',
  PRIMARY KEY (`OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报表模板';

-- ----------------------------
-- Records of sys_groupenstemplate
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_groupfield`
-- ----------------------------
DROP TABLE IF EXISTS `sys_groupfield`;
CREATE TABLE `sys_groupfield` (
  `OID` int(11) NOT NULL COMMENT 'OID',
  `Lab` varchar(500) DEFAULT NULL COMMENT '标签',
  `FrmID` varchar(200) DEFAULT NULL COMMENT '表单ID',
  `CtrlType` varchar(50) DEFAULT NULL COMMENT '控件类型',
  `CtrlID` varchar(500) DEFAULT NULL COMMENT '控件ID',
  `Idx` int(11) DEFAULT '99' COMMENT '顺序号',
  `GUID` varchar(128) DEFAULT NULL COMMENT 'GUID',
  `AtPara` varchar(3000) DEFAULT NULL COMMENT 'AtPara',
  PRIMARY KEY (`OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='傻瓜表单分组';

-- ----------------------------
-- Records of sys_groupfield
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_langue`
-- ----------------------------
DROP TABLE IF EXISTS `sys_langue`;
CREATE TABLE `sys_langue` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `Langue` varchar(20) DEFAULT NULL COMMENT '语言ID',
  `Model` varchar(20) DEFAULT NULL COMMENT '模块',
  `ModelKey` varchar(200) DEFAULT NULL COMMENT '模块实例',
  `Sort` varchar(20) DEFAULT NULL COMMENT '类别',
  `SortKey` varchar(100) DEFAULT NULL COMMENT '类别PK',
  `Val` text COMMENT '语言值',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='语言定义';

-- ----------------------------
-- Records of sys_langue
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_mapattr`
-- ----------------------------
DROP TABLE IF EXISTS `sys_mapattr`;
CREATE TABLE `sys_mapattr` (
  `MyPK` varchar(200) NOT NULL DEFAULT '',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT '实体标识',
  `KeyOfEn` varchar(200) DEFAULT NULL COMMENT '属性',
  `Name` varchar(200) DEFAULT NULL COMMENT '描述',
  `DefVal` text,
  `UIContralType` int(11) DEFAULT '0' COMMENT '控件',
  `MyDataType` int(11) DEFAULT '1' COMMENT '数据类型',
  `LGType` int(11) DEFAULT '0' COMMENT '逻辑类型',
  `UIWidth` float DEFAULT NULL COMMENT '宽度',
  `UIHeight` float DEFAULT NULL COMMENT '高度',
  `MinLen` int(11) DEFAULT '0' COMMENT '最小长度',
  `MaxLen` int(11) DEFAULT '300' COMMENT '最大长度',
  `UIBindKey` varchar(100) DEFAULT NULL COMMENT '绑定的信息',
  `UIRefKey` varchar(30) DEFAULT NULL COMMENT '绑定的Key',
  `UIRefKeyText` varchar(30) DEFAULT NULL COMMENT '绑定的Text',
  `ExtIsSum` int(11) DEFAULT '0' COMMENT '是否显示合计(对从表有效)',
  `UIVisible` int(11) DEFAULT '1' COMMENT '是否可见',
  `UIIsEnable` int(11) DEFAULT '1' COMMENT '是否启用',
  `UIIsLine` int(11) DEFAULT '0' COMMENT '是否单独栏显示',
  `UIIsInput` int(11) DEFAULT '0' COMMENT '是否必填字段',
  `IsRichText` int(11) DEFAULT '0' COMMENT '富文本',
  `IsSupperText` int(11) DEFAULT '0' COMMENT '富文本',
  `FontSize` int(11) DEFAULT '0' COMMENT '富文本',
  `IsSigan` int(11) DEFAULT '0' COMMENT '签字？',
  `X` float DEFAULT NULL COMMENT 'X',
  `Y` float DEFAULT NULL COMMENT 'Y',
  `GUID` varchar(128) DEFAULT NULL COMMENT 'GUID',
  `Tag` varchar(100) DEFAULT NULL COMMENT '标识（存放临时数据）',
  `EditType` int(11) DEFAULT '0' COMMENT '编辑类型',
  `Tip` text,
  `ColSpan` int(11) DEFAULT '1' COMMENT '单元格数量',
  `TextColSpan` int(11) DEFAULT '1' COMMENT '文本单元格数量',
  `RowSpan` int(11) DEFAULT '1' COMMENT '行数',
  `GroupID` int(11) DEFAULT '1' COMMENT '显示的分组',
  `IsEnableInAPP` int(11) DEFAULT '1' COMMENT '是否在移动端中显示',
  `Idx` int(11) DEFAULT '0' COMMENT '序号',
  `AtPara` text COMMENT 'AtPara',
  `GroupIDText` varchar(200) DEFAULT '0' COMMENT '显示的分组',
  `ExtDefVal` varchar(50) DEFAULT '0' COMMENT '系统默认值',
  `ExtDefValText` varchar(200) DEFAULT '0' COMMENT '系统默认值',
  `DefValText` varchar(200) DEFAULT '0' COMMENT '默认值（选中）',
  `RBShowModel` int(11) DEFAULT '0' COMMENT '单选按钮的展现方式',
  `IsEnableJS` int(11) DEFAULT '0' COMMENT '是否启用JS高级设置？',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='实体属性';

-- ----------------------------
-- Records of sys_mapattr
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_mapdata`
-- ----------------------------
DROP TABLE IF EXISTS `sys_mapdata`;
CREATE TABLE `sys_mapdata` (
  `No` varchar(200) NOT NULL COMMENT '编号',
  `Name` varchar(500) DEFAULT NULL COMMENT '描述',
  `FormEventEntity` varchar(100) DEFAULT NULL COMMENT '事件实体',
  `EnPK` varchar(200) DEFAULT NULL COMMENT '实体主键',
  `PTable` varchar(500) DEFAULT NULL COMMENT '物理表',
  `PTableModel` int(11) DEFAULT '0' COMMENT '表存储模式',
  `Url` varchar(500) DEFAULT NULL COMMENT '连接(对嵌入式表单有效)',
  `Dtls` varchar(500) DEFAULT NULL COMMENT '从表',
  `FrmW` int(11) DEFAULT '900' COMMENT 'FrmW',
  `FrmH` int(11) DEFAULT '1200' COMMENT 'FrmH',
  `TableCol` int(11) DEFAULT '4' COMMENT '傻瓜表单显示的列',
  `Tag` varchar(500) DEFAULT NULL COMMENT 'Tag',
  `FK_FrmSort` varchar(500) DEFAULT NULL COMMENT '表单类别',
  `FK_FormTree` varchar(500) DEFAULT NULL COMMENT '表单树类别',
  `FrmType` int(11) DEFAULT '1' COMMENT '表单类型',
  `AppType` int(11) DEFAULT '0' COMMENT '应用类型',
  `DBSrc` varchar(100) DEFAULT NULL COMMENT '数据源',
  `BodyAttr` varchar(100) DEFAULT NULL COMMENT '表单Body属性',
  `Note` text,
  `Designer` varchar(500) DEFAULT NULL COMMENT '设计者',
  `DesignerUnit` varchar(500) DEFAULT NULL COMMENT '单位',
  `DesignerContact` varchar(500) DEFAULT NULL COMMENT '联系方式',
  `Idx` int(11) DEFAULT '100' COMMENT '顺序号',
  `GUID` varchar(128) DEFAULT NULL COMMENT 'GUID',
  `Ver` varchar(30) DEFAULT NULL COMMENT '版本号',
  `FlowCtrls` varchar(200) DEFAULT NULL COMMENT '流程控件',
  `AtPara` text COMMENT 'AtPara',
  `IsTemplate` int(11) DEFAULT '0' COMMENT '是否是表单模版',
  `OfficeOpenLab` varchar(50) DEFAULT '打开本地' COMMENT '打开本地标签',
  `OfficeOpenEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeOpenTemplateLab` varchar(50) DEFAULT '打开模板' COMMENT '打开模板标签',
  `OfficeOpenTemplateEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeSaveLab` varchar(50) DEFAULT '保存' COMMENT '保存标签',
  `OfficeSaveEnable` int(11) DEFAULT '1' COMMENT '是否启用',
  `OfficeAcceptLab` varchar(50) DEFAULT '接受修订' COMMENT '接受修订标签',
  `OfficeAcceptEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeRefuseLab` varchar(50) DEFAULT '拒绝修订' COMMENT '拒绝修订标签',
  `OfficeRefuseEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeOverLab` varchar(50) DEFAULT '套红按钮' COMMENT '套红按钮标签',
  `OfficeOverEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeMarksEnable` int(11) DEFAULT '1' COMMENT '是否查看用户留痕',
  `OfficePrintLab` varchar(50) DEFAULT '打印按钮' COMMENT '打印按钮标签',
  `OfficePrintEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeSealLab` varchar(50) DEFAULT '签章按钮' COMMENT '签章按钮标签',
  `OfficeSealEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeInsertFlowLab` varchar(50) DEFAULT '插入流程' COMMENT '插入流程标签',
  `OfficeInsertFlowEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeNodeInfo` int(11) DEFAULT '0' COMMENT '是否记录节点信息',
  `OfficeReSavePDF` int(11) DEFAULT '0' COMMENT '是否该自动保存为PDF',
  `OfficeDownLab` varchar(50) DEFAULT '下载' COMMENT '下载按钮标签',
  `OfficeDownEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeIsMarks` int(11) DEFAULT '1' COMMENT '是否进入留痕模式',
  `OfficeTemplate` varchar(100) DEFAULT '' COMMENT '指定文档模板',
  `OfficeIsParent` int(11) DEFAULT '1' COMMENT '是否使用父流程的文档',
  `OfficeTHEnable` int(11) DEFAULT '0' COMMENT '是否自动套红',
  `OfficeTHTemplate` varchar(200) DEFAULT '' COMMENT '自动套红模板',
  `MyFileName` varchar(300) DEFAULT '' COMMENT '表单模版',
  `MyFilePath` varchar(300) DEFAULT '' COMMENT 'MyFilePath',
  `MyFileExt` varchar(20) DEFAULT '' COMMENT 'MyFileExt',
  `WebPath` varchar(300) DEFAULT '' COMMENT 'WebPath',
  `MyFileH` int(11) DEFAULT '0' COMMENT 'MyFileH',
  `MyFileW` int(11) DEFAULT '0' COMMENT 'MyFileW',
  `MyFileSize` float(11,2) DEFAULT '0.00' COMMENT 'MyFileSize',
  `FrmBillWorkModel` int(11) DEFAULT '0' COMMENT '工作模式',
  `BillNoFormat` varchar(100) DEFAULT '' COMMENT '单号规则',
  `RefFlowNo` varchar(100) DEFAULT '' COMMENT '关联流程号',
  `TitleRole` varchar(100) DEFAULT '' COMMENT '标题生成规则',
  `BtnNewLable` varchar(50) DEFAULT '新建' COMMENT '新建',
  `BtnNewEnable` int(11) DEFAULT '1' COMMENT '是否可用？',
  `BtnSaveLable` varchar(50) DEFAULT '保存' COMMENT '保存',
  `BtnSaveEnable` int(11) DEFAULT '1' COMMENT '是否可用？',
  `BtnStartFlowLable` varchar(50) DEFAULT '启动流程' COMMENT '启动流程',
  `BtnStartFlowEnable` int(11) DEFAULT '0' COMMENT '是否可用？',
  `BtnDelLable` varchar(50) DEFAULT '删除' COMMENT '删除',
  `BtnDelEnable` int(11) DEFAULT '1' COMMENT '是否可用？',
  `BtnSearchLabel` varchar(50) DEFAULT '查询' COMMENT '查询',
  `BtnSearchEnable` int(11) DEFAULT '1' COMMENT '是否可用？',
  `BtnGroupLabel` varchar(50) DEFAULT '分析' COMMENT '分析',
  `BtnGroupEnable` int(11) DEFAULT '0' COMMENT '是否可用？',
  `BtnPrintHtml` varchar(50) DEFAULT '打印Html' COMMENT '打印Html',
  `BtnPrintHtmlEnable` int(11) DEFAULT '0' COMMENT '是否可用？',
  `BtnPrintPDF` varchar(50) DEFAULT '打印PDF' COMMENT '打印PDF',
  `BtnPrintPDFEnable` int(11) DEFAULT '0' COMMENT '是否可用？',
  `BtnPrintRTF` varchar(50) DEFAULT '打印RTF' COMMENT '打印RTF',
  `BtnPrintRTFEnable` int(11) DEFAULT '0' COMMENT '是否可用？',
  `BtnPrintCCWord` varchar(50) DEFAULT '打印CCWord' COMMENT '打印CCWord',
  `BtnPrintCCWordEnable` int(11) DEFAULT '0' COMMENT '是否可用？',
  `BtnExpZip` varchar(50) DEFAULT '导出zip文件' COMMENT '导出zip文件',
  `BtnExpZipEnable` int(11) DEFAULT '0' COMMENT '是否可用？',
  `BtnImpExcel` varchar(50) DEFAULT '导入Excel文件' COMMENT '导入Excel文件',
  `BtnImpExcelEnable` int(11) DEFAULT '1' COMMENT '是否可用？',
  `BtnExpExcel` varchar(50) DEFAULT '导出Excel文件' COMMENT '导出Excel文件',
  `BtnExpExcelEnable` int(11) DEFAULT '1' COMMENT '是否可用？',
  `TemplaterVer` varchar(30) DEFAULT '' COMMENT '模版编号',
  `DBSave` varchar(50) DEFAULT '' COMMENT 'Word数据文件存储',
  `FK_Flow` varchar(50) DEFAULT NULL,
  `RightViewWay` int(11) DEFAULT '0' COMMENT '报表查看权限控制方式',
  `RightViewTag` text COMMENT '报表查看权限控制Tag',
  `RightDeptWay` int(11) DEFAULT '0' COMMENT '部门数据查看控制方式',
  `RightDeptTag` text COMMENT '部门数据查看控制Tag',
  `DBURL` int(11) DEFAULT '0' COMMENT 'DBURL',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表单属性';

-- ----------------------------
-- Records of sys_mapdata
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_mapdtl`
-- ----------------------------
DROP TABLE IF EXISTS `sys_mapdtl`;
CREATE TABLE `sys_mapdtl` (
  `No` varchar(100) NOT NULL COMMENT '编号',
  `Name` varchar(200) DEFAULT NULL COMMENT '描述',
  `Alias` varchar(200) DEFAULT NULL COMMENT '别名',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT '主表',
  `PTable` varchar(200) DEFAULT NULL COMMENT '物理表',
  `GroupField` varchar(300) DEFAULT NULL COMMENT '分组字段',
  `RefPK` varchar(100) DEFAULT NULL COMMENT '关联的主键',
  `FEBD` varchar(100) DEFAULT NULL COMMENT '映射的事件实体类',
  `Model` int(11) DEFAULT '0' COMMENT '工作模式',
  `DtlVer` int(11) DEFAULT '0' COMMENT '使用版本',
  `RowsOfList` int(11) DEFAULT '6' COMMENT '初始化行数',
  `IsEnableGroupField` int(11) DEFAULT '0' COMMENT '是否启用分组字段',
  `IsShowSum` int(11) DEFAULT '1' COMMENT '是否显示合计？',
  `IsShowIdx` int(11) DEFAULT '1' COMMENT '是否显示序号？',
  `IsCopyNDData` int(11) DEFAULT '1' COMMENT '是否允许Copy数据',
  `IsHLDtl` int(11) DEFAULT '0' COMMENT '是否是合流汇总',
  `IsReadonly` int(11) DEFAULT '0' COMMENT '是否只读？',
  `IsShowTitle` int(11) DEFAULT '1' COMMENT '是否显示标题？',
  `IsView` int(11) DEFAULT '1' COMMENT '是否可见',
  `IsInsert` int(11) DEFAULT '1' COMMENT '是否可以插入行？',
  `IsDelete` int(11) DEFAULT '1' COMMENT '是否可以删除行',
  `IsUpdate` int(11) DEFAULT '1' COMMENT '是否可以更新？',
  `IsEnablePass` int(11) DEFAULT '0' COMMENT '是否启用通过审核功能?',
  `IsEnableAthM` int(11) DEFAULT '0' COMMENT '是否启用多附件',
  `IsEnableM2M` int(11) DEFAULT '0' COMMENT '是否启用M2M',
  `IsEnableM2MM` int(11) DEFAULT '0' COMMENT '是否启用M2M',
  `WhenOverSize` int(11) DEFAULT '0' COMMENT '列表数据显示格式',
  `DtlOpenType` int(11) DEFAULT '1' COMMENT '数据开放类型',
  `ListShowModel` int(11) DEFAULT '0' COMMENT '列表数据显示格式',
  `EditModel` int(11) DEFAULT '0' COMMENT '行数据显示格式',
  `X` float DEFAULT NULL COMMENT '距左',
  `Y` float DEFAULT NULL COMMENT '距上',
  `H` float DEFAULT NULL COMMENT '高度',
  `W` float DEFAULT NULL COMMENT '宽度',
  `FrmW` float DEFAULT NULL COMMENT '表单宽度',
  `FrmH` float DEFAULT NULL COMMENT '表单高度',
  `MTR` text,
  `FilterSQLExp` varchar(200) DEFAULT NULL COMMENT '过滤SQL表达式',
  `FK_Node` int(11) DEFAULT '0' COMMENT '节点(用户独立表单权限控制)',
  `ShowCols` varchar(500) DEFAULT NULL COMMENT '显示的列',
  `IsExp` int(11) DEFAULT '1' COMMENT 'IsExp',
  `ImpModel` int(11) DEFAULT '0' COMMENT '导入规则',
  `ImpSQLSearch` text,
  `ImpSQLInit` text,
  `ImpSQLFullOneRow` text,
  `ImpSQLNames` varchar(900) DEFAULT NULL COMMENT '字段中文名',
  `ColAutoExp` varchar(200) DEFAULT NULL COMMENT '列自动计算表达式',
  `GUID` varchar(128) DEFAULT NULL COMMENT 'GUID',
  `AtPara` varchar(300) DEFAULT NULL COMMENT 'AtPara',
  `IsEnableLink` int(11) DEFAULT '0' COMMENT '是否启用超链接',
  `LinkLabel` varchar(50) DEFAULT '' COMMENT '超连接标签',
  `LinkTarget` varchar(10) DEFAULT '' COMMENT '连接目标',
  `LinkUrl` varchar(200) DEFAULT '' COMMENT '连接URL',
  `SubThreadWorker` varchar(50) DEFAULT '' COMMENT '子线程处理人字段',
  `SubThreadWorkerText` varchar(200) DEFAULT '' COMMENT '子线程处理人字段',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='明细';

-- ----------------------------
-- Records of sys_mapdtl
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_mapext`
-- ----------------------------
DROP TABLE IF EXISTS `sys_mapext`;
CREATE TABLE `sys_mapext` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT '主表',
  `ExtType` varchar(30) DEFAULT NULL COMMENT '类型',
  `DoWay` int(11) DEFAULT '0' COMMENT '执行方式',
  `AttrOfOper` varchar(30) DEFAULT NULL COMMENT '操作的Attr',
  `AttrsOfActive` varchar(900) DEFAULT NULL COMMENT '激活的字段',
  `Doc` text COMMENT '内容',
  `Tag` varchar(2000) DEFAULT NULL COMMENT 'Tag',
  `Tag1` varchar(2000) DEFAULT NULL COMMENT 'Tag1',
  `Tag2` varchar(2000) DEFAULT NULL COMMENT 'Tag2',
  `Tag3` varchar(2000) DEFAULT NULL COMMENT 'Tag3',
  `Tag4` varchar(2000) DEFAULT NULL COMMENT 'Tag4',
  `Tag5` varchar(2000) DEFAULT NULL COMMENT 'Tag5',
  `H` int(11) DEFAULT '500' COMMENT '高度',
  `W` int(11) DEFAULT '400' COMMENT '宽度',
  `DBType` int(11) DEFAULT '0' COMMENT '数据类型',
  `FK_DBSrc` varchar(100) DEFAULT NULL COMMENT '数据源',
  `PRI` int(11) DEFAULT '0' COMMENT 'PRI/顺序号',
  `AtPara` text COMMENT '参数',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务逻辑';

-- ----------------------------
-- Records of sys_mapext
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_mapframe`
-- ----------------------------
DROP TABLE IF EXISTS `sys_mapframe`;
CREATE TABLE `sys_mapframe` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT '表单ID',
  `Name` varchar(200) DEFAULT NULL COMMENT '名称',
  `URL` varchar(3000) DEFAULT NULL COMMENT 'URL',
  `UrlSrcType` int(11) DEFAULT '0' COMMENT 'URL来源',
  `FrmID` varchar(50) DEFAULT NULL COMMENT '表单表单',
  `FrmIDText` varchar(200) DEFAULT NULL COMMENT '表单表单',
  `Y` varchar(20) DEFAULT NULL COMMENT 'Y',
  `X` varchar(20) DEFAULT NULL COMMENT 'x',
  `W` varchar(20) DEFAULT NULL COMMENT '宽度',
  `H` varchar(20) DEFAULT NULL COMMENT '高度',
  `IsAutoSize` int(11) DEFAULT '1' COMMENT '是否自动设置大小',
  `EleType` varchar(50) DEFAULT NULL COMMENT '类型',
  `GUID` varchar(128) DEFAULT NULL COMMENT 'GUID',
  `Idx` int(11) DEFAULT '0' COMMENT '顺序号',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='框架';

-- ----------------------------
-- Records of sys_mapframe
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_rptdept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_rptdept`;
CREATE TABLE `sys_rptdept` (
  `FK_Rpt` varchar(15) NOT NULL COMMENT '报表',
  `FK_Dept` varchar(100) NOT NULL COMMENT '部门',
  PRIMARY KEY (`FK_Rpt`,`FK_Dept`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报表部门对应信息';

-- ----------------------------
-- Records of sys_rptdept
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_rptemp`
-- ----------------------------
DROP TABLE IF EXISTS `sys_rptemp`;
CREATE TABLE `sys_rptemp` (
  `FK_Rpt` varchar(15) NOT NULL COMMENT '报表',
  `FK_Emp` varchar(100) NOT NULL COMMENT '人员',
  PRIMARY KEY (`FK_Rpt`,`FK_Emp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报表人员对应信息';

-- ----------------------------
-- Records of sys_rptemp
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_rptstation`
-- ----------------------------
DROP TABLE IF EXISTS `sys_rptstation`;
CREATE TABLE `sys_rptstation` (
  `FK_Rpt` varchar(15) NOT NULL COMMENT '报表',
  `FK_Station` varchar(100) NOT NULL COMMENT '岗位',
  PRIMARY KEY (`FK_Rpt`,`FK_Station`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报表岗位对应信息';

-- ----------------------------
-- Records of sys_rptstation
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_rpttemplate`
-- ----------------------------
DROP TABLE IF EXISTS `sys_rpttemplate`;
CREATE TABLE `sys_rpttemplate` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `EnsName` varchar(500) DEFAULT NULL COMMENT '类名',
  `FK_Emp` varchar(20) DEFAULT NULL COMMENT '操作员',
  `D1` varchar(90) DEFAULT NULL COMMENT 'D1',
  `D2` varchar(90) DEFAULT NULL COMMENT 'D2',
  `D3` varchar(90) DEFAULT NULL COMMENT 'D3',
  `AlObjs` varchar(90) DEFAULT NULL COMMENT '要分析的对象',
  `Height` int(11) DEFAULT '600' COMMENT 'Height',
  `Width` int(11) DEFAULT '800' COMMENT 'Width',
  `IsSumBig` int(11) DEFAULT '0' COMMENT '是否显示大合计',
  `IsSumLittle` int(11) DEFAULT '0' COMMENT '是否显示小合计',
  `IsSumRight` int(11) DEFAULT '0' COMMENT '是否显示右合计',
  `PercentModel` int(11) DEFAULT '0' COMMENT '比率显示方式',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报表模板';

-- ----------------------------
-- Records of sys_rpttemplate
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_serial`
-- ----------------------------
DROP TABLE IF EXISTS `sys_serial`;
CREATE TABLE `sys_serial` (
  `CfgKey` varchar(100) NOT NULL COMMENT 'CfgKey',
  `IntVal` int(11) DEFAULT '0' COMMENT '属性',
  PRIMARY KEY (`CfgKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='序列号';

-- ----------------------------
-- Records of sys_serial
-- ----------------------------
INSERT INTO `sys_serial` VALUES ('BP.WF.Template.FlowSort', '102');
INSERT INTO `sys_serial` VALUES ('UpdataCCFlowVer', '310154128');
INSERT INTO `sys_serial` VALUES ('Ver', '20190225');

-- ----------------------------
-- Table structure for `sys_sfdbsrc`
-- ----------------------------
DROP TABLE IF EXISTS `sys_sfdbsrc`;
CREATE TABLE `sys_sfdbsrc` (
  `No` varchar(20) NOT NULL COMMENT '数据源编号(必须是英文)',
  `Name` varchar(30) DEFAULT NULL COMMENT '数据源名称',
  `DBSrcType` int(11) DEFAULT '0' COMMENT '数据源类型',
  `UserID` varchar(30) DEFAULT NULL COMMENT '数据库登录用户ID',
  `Password` varchar(30) DEFAULT NULL COMMENT '数据库登录用户密码',
  `IP` varchar(500) DEFAULT NULL COMMENT 'IP地址/数据库实例名',
  `DBName` varchar(30) DEFAULT NULL COMMENT '数据库名称/Oracle保持为空',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据源';

-- ----------------------------
-- Records of sys_sfdbsrc
-- ----------------------------
INSERT INTO `sys_sfdbsrc` VALUES ('local', '本机数据源(默认)', '0', '', '', '', '');

-- ----------------------------
-- Table structure for `sys_sftable`
-- ----------------------------
DROP TABLE IF EXISTS `sys_sftable`;
CREATE TABLE `sys_sftable` (
  `No` varchar(200) NOT NULL COMMENT '表英文名称',
  `Name` varchar(200) DEFAULT NULL COMMENT '表中文名称',
  `SrcType` int(11) DEFAULT '0' COMMENT '数据表类型',
  `CodeStruct` int(11) DEFAULT '0' COMMENT '字典表类型',
  `FK_Val` varchar(200) DEFAULT NULL COMMENT '默认创建的字段名',
  `TableDesc` varchar(200) DEFAULT NULL COMMENT '表描述',
  `DefVal` varchar(200) DEFAULT NULL COMMENT '默认值',
  `FK_SFDBSrc` varchar(100) DEFAULT NULL COMMENT '数据源',
  `SrcTable` varchar(200) DEFAULT NULL COMMENT '数据源表',
  `ColumnValue` varchar(200) DEFAULT NULL COMMENT '显示的值(编号列)',
  `ColumnText` varchar(200) DEFAULT NULL COMMENT '显示的文字(名称列)',
  `ParentValue` varchar(200) DEFAULT NULL COMMENT '父级值(父级列)',
  `SelectStatement` varchar(1000) DEFAULT NULL COMMENT '查询语句',
  `RDT` varchar(50) DEFAULT NULL COMMENT '加入日期',
  `RootVal` varchar(200) DEFAULT '' COMMENT '根节点值',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表';

-- ----------------------------
-- Records of sys_sftable
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_sms`
-- ----------------------------
DROP TABLE IF EXISTS `sys_sms`;
CREATE TABLE `sys_sms` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `Sender` varchar(200) DEFAULT NULL COMMENT '发送人(可以为空)',
  `SendTo` varchar(200) DEFAULT NULL COMMENT '发送给(可以为空)',
  `RDT` varchar(50) DEFAULT NULL COMMENT '写入时间',
  `Mobile` varchar(30) DEFAULT NULL COMMENT '手机号(可以为空)',
  `MobileSta` int(11) DEFAULT '0' COMMENT '消息状态',
  `MobileInfo` varchar(1000) DEFAULT NULL COMMENT '短信信息',
  `Email` varchar(200) DEFAULT NULL COMMENT 'Email(可以为空)',
  `EmailSta` int(11) DEFAULT '0' COMMENT 'EmaiSta消息状态',
  `EmailTitle` varchar(3000) DEFAULT NULL COMMENT '标题',
  `EmailDoc` text COMMENT '内容',
  `SendDT` varchar(50) DEFAULT NULL COMMENT '发送时间',
  `IsRead` int(11) DEFAULT '0' COMMENT '是否读取?',
  `IsAlert` int(11) DEFAULT '0' COMMENT '是否提示?',
  `MsgFlag` varchar(200) DEFAULT NULL COMMENT '消息标记(用于防止发送重复)',
  `MsgType` varchar(200) DEFAULT NULL COMMENT '消息类型(CC抄送,Todolist待办,Return退回,Etc其他消息...)',
  `AtPara` varchar(500) DEFAULT NULL COMMENT 'AtPara',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息';

-- ----------------------------
-- Records of sys_sms
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_userlogt`
-- ----------------------------
DROP TABLE IF EXISTS `sys_userlogt`;
CREATE TABLE `sys_userlogt` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Emp` varchar(30) DEFAULT NULL COMMENT '用户',
  `IP` varchar(200) DEFAULT NULL COMMENT 'IP',
  `LogFlag` varchar(300) DEFAULT NULL COMMENT '标识',
  `Docs` varchar(300) DEFAULT NULL COMMENT '说明',
  `RDT` varchar(20) DEFAULT NULL COMMENT '记录日期',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户日志';

-- ----------------------------
-- Records of sys_userlogt
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_userregedit`
-- ----------------------------
DROP TABLE IF EXISTS `sys_userregedit`;
CREATE TABLE `sys_userregedit` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `ContrastKey` varchar(20) DEFAULT NULL COMMENT '对比项目',
  `KeyVal1` varchar(20) DEFAULT NULL COMMENT 'KeyVal1',
  `KeyVal2` varchar(20) DEFAULT NULL COMMENT 'KeyVal2',
  `SortBy` varchar(20) DEFAULT NULL COMMENT 'SortBy',
  `KeyOfNum` varchar(20) DEFAULT NULL COMMENT 'KeyOfNum',
  `GroupWay` int(11) DEFAULT '1' COMMENT '求什么?SumAvg',
  `OrderWay` varchar(300) DEFAULT NULL,
  `EnsName` varchar(100) DEFAULT '' COMMENT '实体类名称',
  `FK_Emp` varchar(100) DEFAULT '' COMMENT '工作人员',
  `Attrs` text COMMENT '属性s',
  `FK_MapData` varchar(100) DEFAULT '' COMMENT '实体',
  `AttrKey` varchar(50) DEFAULT '' COMMENT '节点对应字段',
  `LB` int(11) DEFAULT '0' COMMENT '类别',
  `CurValue` text COMMENT '文本',
  `CfgKey` varchar(200) DEFAULT '' COMMENT '键',
  `Vals` varchar(2000) DEFAULT '' COMMENT '值',
  `GenerSQL` varchar(2000) DEFAULT '' COMMENT 'GenerSQL',
  `Paras` varchar(2000) DEFAULT '' COMMENT 'Paras',
  `NumKey` varchar(300) DEFAULT '' COMMENT '分析的Key',
  `OrderBy` varchar(300) DEFAULT '' COMMENT 'OrderBy',
  `SearchKey` varchar(300) DEFAULT '' COMMENT 'SearchKey',
  `MVals` varchar(2000) DEFAULT '' COMMENT 'MVals',
  `IsPic` int(11) DEFAULT '0' COMMENT '是否图片',
  `DTFrom` varchar(20) DEFAULT '' COMMENT '查询时间从',
  `DTTo` varchar(20) DEFAULT '' COMMENT '到',
  `AtPara` text COMMENT 'AtPara',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户注册表';

-- ----------------------------
-- Records of sys_userregedit
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_wfsealdata`
-- ----------------------------
DROP TABLE IF EXISTS `sys_wfsealdata`;
CREATE TABLE `sys_wfsealdata` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `OID` varchar(200) DEFAULT NULL COMMENT 'OID',
  `FK_Node` varchar(200) DEFAULT NULL COMMENT 'FK_Node',
  `FK_MapData` varchar(100) DEFAULT NULL COMMENT 'FK_MapData',
  `SealData` text COMMENT 'SealData',
  `RDT` varchar(20) DEFAULT NULL COMMENT '记录日期',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='签名信息';

-- ----------------------------
-- Records of sys_wfsealdata
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_accepterrole`
-- ----------------------------
DROP TABLE IF EXISTS `wf_accepterrole`;
CREATE TABLE `wf_accepterrole` (
  `OID` int(11) NOT NULL COMMENT 'OID',
  `Name` varchar(200) DEFAULT NULL,
  `FK_Node` varchar(100) DEFAULT NULL COMMENT '节点',
  `FK_Mode` int(11) DEFAULT '0' COMMENT '模式类型',
  `Tag0` varchar(999) DEFAULT NULL COMMENT 'Tag0',
  `Tag1` varchar(999) DEFAULT NULL COMMENT 'Tag1',
  `Tag2` varchar(999) DEFAULT NULL COMMENT 'Tag2',
  `Tag3` varchar(999) DEFAULT NULL COMMENT 'Tag3',
  `Tag4` varchar(999) DEFAULT NULL COMMENT 'Tag4',
  `Tag5` varchar(999) DEFAULT NULL COMMENT 'Tag5',
  PRIMARY KEY (`OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接受人规则';

-- ----------------------------
-- Records of wf_accepterrole
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_athunreadlog`
-- ----------------------------
DROP TABLE IF EXISTS `wf_athunreadlog`;
CREATE TABLE `wf_athunreadlog` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Dept` varchar(100) DEFAULT NULL COMMENT '部门',
  `Title` varchar(100) DEFAULT NULL COMMENT '标题',
  `WorkID` int(11) DEFAULT '0' COMMENT 'WorkID',
  `FlowStarter` varchar(100) DEFAULT NULL COMMENT '发起人',
  `FlowStartRDT` varchar(50) DEFAULT NULL COMMENT '发起时间',
  `FK_NY` varchar(100) DEFAULT NULL COMMENT '年月',
  `FK_Flow` varchar(100) DEFAULT NULL COMMENT '流程',
  `FK_Node` int(11) DEFAULT '0' COMMENT '节点ID',
  `NodeName` varchar(20) DEFAULT NULL COMMENT '节点名称',
  `FK_Emp` varchar(20) DEFAULT NULL COMMENT '人员',
  `FK_EmpDept` varchar(20) DEFAULT NULL COMMENT '人员部门',
  `FK_EmpDeptName` varchar(200) DEFAULT NULL COMMENT '人员名称',
  `BeiZhu` text COMMENT '内容',
  `SendDT` varchar(50) DEFAULT NULL COMMENT '日期',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='附件未读日志';

-- ----------------------------
-- Records of wf_athunreadlog
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_bill`
-- ----------------------------
DROP TABLE IF EXISTS `wf_bill`;
CREATE TABLE `wf_bill` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `WorkID` int(11) DEFAULT '0' COMMENT '工作ID',
  `FID` int(11) DEFAULT '0' COMMENT 'FID',
  `FK_Flow` varchar(4) DEFAULT NULL COMMENT '流程',
  `FK_BillType` varchar(300) DEFAULT NULL COMMENT '单据类型',
  `Title` varchar(900) DEFAULT NULL COMMENT '标题',
  `FK_Starter` varchar(50) DEFAULT NULL COMMENT '发起人',
  `StartDT` varchar(50) DEFAULT NULL COMMENT '发起时间',
  `Url` varchar(2000) DEFAULT NULL COMMENT 'Url',
  `FullPath` varchar(2000) DEFAULT NULL COMMENT 'FullPath',
  `FK_Emp` varchar(100) DEFAULT NULL COMMENT '打印人',
  `RDT` varchar(50) DEFAULT NULL COMMENT '打印时间',
  `FK_Dept` varchar(100) DEFAULT NULL COMMENT '隶属部门',
  `FK_NY` varchar(100) DEFAULT NULL COMMENT '隶属年月',
  `Emps` text COMMENT 'Emps',
  `FK_Node` varchar(30) DEFAULT NULL COMMENT '节点',
  `FK_Bill` varchar(500) DEFAULT NULL COMMENT 'FK_Bill',
  `MyNum` int(11) DEFAULT '1' COMMENT '个数',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='单据';

-- ----------------------------
-- Records of wf_bill
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_billtemplate`
-- ----------------------------
DROP TABLE IF EXISTS `wf_billtemplate`;
CREATE TABLE `wf_billtemplate` (
  `No` varchar(190) NOT NULL COMMENT 'No',
  `Name` varchar(200) DEFAULT NULL COMMENT 'Name',
  `TempFilePath` varchar(200) DEFAULT NULL COMMENT '模板路径',
  `NodeID` int(11) DEFAULT '0' COMMENT 'NodeID',
  `BillFileType` int(11) DEFAULT '0' COMMENT '生成的文件类型',
  `BillOpenModel` int(11) DEFAULT '0' COMMENT '生成的文件打开方式',
  `QRModel` int(11) DEFAULT '0' COMMENT '二维码生成方式',
  `TemplateFileModel` int(11) DEFAULT '0' COMMENT '模版模式',
  `Idx` varchar(200) DEFAULT NULL COMMENT 'Idx',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='单据模板';

-- ----------------------------
-- Records of wf_billtemplate
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_ccbill`
-- ----------------------------
DROP TABLE IF EXISTS `wf_ccbill`;
CREATE TABLE `wf_ccbill` (
  `WorkID` int(11) NOT NULL DEFAULT '0' COMMENT 'WorkID',
  `FK_FrmTree` varchar(10) DEFAULT NULL COMMENT '单据类别',
  `FrmID` varchar(100) DEFAULT NULL COMMENT '单据ID',
  `FrmName` varchar(200) DEFAULT NULL COMMENT '单据名称',
  `Title` varchar(1000) DEFAULT NULL COMMENT '标题',
  `BillSta` int(11) DEFAULT '0' COMMENT '状态(简)',
  `BillState` int(11) DEFAULT '0' COMMENT '单据状态',
  `Starter` varchar(200) DEFAULT NULL COMMENT '发起人',
  `StarterName` varchar(200) DEFAULT NULL COMMENT '发起人名称',
  `Sender` varchar(200) DEFAULT NULL COMMENT '发送人',
  `RDT` varchar(50) DEFAULT NULL COMMENT '记录日期',
  `SendDT` varchar(50) DEFAULT NULL COMMENT '单据活动时间',
  `NDStep` int(11) DEFAULT '0' COMMENT '步骤',
  `NDStepName` varchar(100) DEFAULT NULL COMMENT '步骤名称',
  `FK_Dept` varchar(100) DEFAULT NULL COMMENT '部门',
  `DeptName` varchar(100) DEFAULT NULL COMMENT '部门名称',
  `PRI` int(11) DEFAULT '1' COMMENT '优先级',
  `SDTOfNode` varchar(50) DEFAULT NULL COMMENT '节点应完成时间',
  `SDTOfFlow` varchar(50) DEFAULT NULL COMMENT '单据应完成时间',
  `PFlowNo` varchar(3) DEFAULT NULL COMMENT '父单据编号',
  `PWorkID` int(11) DEFAULT '0' COMMENT '父单据ID',
  `PNodeID` int(11) DEFAULT '0' COMMENT '父单据调用节点',
  `PEmp` varchar(32) DEFAULT NULL COMMENT '子单据的调用人',
  `BillNo` varchar(100) DEFAULT NULL COMMENT '单据编号',
  `FlowNote` text COMMENT '备注',
  `TodoEmps` text COMMENT '待办人员',
  `TodoEmpsNum` int(11) DEFAULT '0' COMMENT '待办人员数量',
  `TaskSta` int(11) DEFAULT '0' COMMENT '共享状态',
  `AtPara` varchar(2000) DEFAULT NULL COMMENT '参数(单据运行设置临时存储的参数)',
  `Emps` text COMMENT '参与人',
  `GUID` varchar(36) DEFAULT NULL COMMENT 'GUID',
  `FK_NY` varchar(7) DEFAULT NULL COMMENT '年月',
  `TSpan` int(11) DEFAULT '0' COMMENT 'TSpan',
  `TodoSta` int(11) DEFAULT '0' COMMENT '待办状态',
  PRIMARY KEY (`WorkID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='单据控制表';

-- ----------------------------
-- Records of wf_ccbill
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_ccdept`
-- ----------------------------
DROP TABLE IF EXISTS `wf_ccdept`;
CREATE TABLE `wf_ccdept` (
  `FK_Node` int(11) NOT NULL DEFAULT '0' COMMENT '节点',
  `FK_Dept` varchar(100) NOT NULL COMMENT '部门',
  PRIMARY KEY (`FK_Node`,`FK_Dept`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='抄送部门';

-- ----------------------------
-- Records of wf_ccdept
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_ccemp`
-- ----------------------------
DROP TABLE IF EXISTS `wf_ccemp`;
CREATE TABLE `wf_ccemp` (
  `FK_Node` int(11) NOT NULL DEFAULT '0' COMMENT '节点',
  `FK_Emp` varchar(100) NOT NULL COMMENT '人员',
  PRIMARY KEY (`FK_Node`,`FK_Emp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='抄送人员';

-- ----------------------------
-- Records of wf_ccemp
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_cclist`
-- ----------------------------
DROP TABLE IF EXISTS `wf_cclist`;
CREATE TABLE `wf_cclist` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `Title` varchar(500) DEFAULT NULL COMMENT '标题',
  `Sta` int(11) DEFAULT '0' COMMENT '状态',
  `FK_Flow` varchar(3) DEFAULT NULL COMMENT '流程编号',
  `FlowName` varchar(200) DEFAULT NULL COMMENT '流程名称',
  `FK_Node` int(11) DEFAULT '0' COMMENT '节点',
  `NodeName` varchar(500) DEFAULT NULL COMMENT '节点名称',
  `WorkID` int(11) DEFAULT '0' COMMENT '工作ID',
  `FID` int(11) DEFAULT '0' COMMENT 'FID',
  `Doc` text COMMENT '内容',
  `Rec` varchar(50) DEFAULT NULL COMMENT '抄送人员',
  `RDT` varchar(50) DEFAULT NULL COMMENT '记录日期',
  `CCTo` varchar(50) DEFAULT NULL COMMENT '抄送给',
  `CCToName` varchar(50) DEFAULT NULL COMMENT '抄送给(人员名称)',
  `CCToDept` varchar(50) DEFAULT NULL COMMENT '抄送到部门',
  `CCToDeptName` varchar(600) DEFAULT NULL COMMENT '抄送给部门名称',
  `CDT` varchar(50) DEFAULT NULL COMMENT '打开时间',
  `PFlowNo` varchar(100) DEFAULT NULL COMMENT '父流程编号',
  `PWorkID` int(11) DEFAULT '0' COMMENT '父流程WorkID',
  `InEmpWorks` int(11) DEFAULT '0' COMMENT '是否加入待办列表',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='抄送列表';

-- ----------------------------
-- Records of wf_cclist
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_ccstation`
-- ----------------------------
DROP TABLE IF EXISTS `wf_ccstation`;
CREATE TABLE `wf_ccstation` (
  `FK_Node` int(11) NOT NULL DEFAULT '0' COMMENT '节点',
  `FK_Station` varchar(100) NOT NULL COMMENT '工作岗位',
  PRIMARY KEY (`FK_Node`,`FK_Station`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='抄送岗位';

-- ----------------------------
-- Records of wf_ccstation
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_cctrack`
-- ----------------------------
DROP TABLE IF EXISTS `wf_cctrack`;
CREATE TABLE `wf_cctrack` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `ActionType` int(11) DEFAULT '0' COMMENT '类型',
  `ActionTypeText` varchar(30) DEFAULT NULL COMMENT '类型(名称)',
  `FID` int(11) DEFAULT '0' COMMENT '流程ID',
  `WorkID` int(11) DEFAULT '0' COMMENT '工作ID',
  `NDFrom` int(11) DEFAULT '0' COMMENT '从节点',
  `NDFromT` varchar(300) DEFAULT NULL COMMENT '从节点(名称)',
  `NDTo` int(11) DEFAULT '0' COMMENT '到节点',
  `NDToT` varchar(999) DEFAULT NULL COMMENT '到节点(名称)',
  `EmpFrom` varchar(20) DEFAULT NULL COMMENT '从人员',
  `EmpFromT` varchar(30) DEFAULT NULL COMMENT '从人员(名称)',
  `EmpTo` varchar(2000) DEFAULT NULL COMMENT '到人员',
  `EmpToT` varchar(2000) DEFAULT NULL COMMENT '到人员(名称)',
  `RDT` varchar(20) DEFAULT NULL COMMENT '日期',
  `WorkTimeSpan` float DEFAULT NULL COMMENT '时间跨度(天)',
  `Msg` text COMMENT '消息',
  `NodeData` text COMMENT '节点数据(日志信息)',
  `Tag` varchar(300) DEFAULT NULL COMMENT '参数',
  `Exer` varchar(200) DEFAULT NULL COMMENT '执行人',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='轨迹表';

-- ----------------------------
-- Records of wf_cctrack
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_ch`
-- ----------------------------
DROP TABLE IF EXISTS `wf_ch`;
CREATE TABLE `wf_ch` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `WorkID` int(11) DEFAULT '0' COMMENT '工作ID',
  `FID` int(11) DEFAULT '0' COMMENT 'FID',
  `Title` varchar(900) DEFAULT NULL COMMENT '标题',
  `FK_Flow` varchar(100) DEFAULT NULL,
  `FK_FlowT` varchar(200) DEFAULT NULL COMMENT '流程名称',
  `FK_Node` int(11) DEFAULT '0' COMMENT '节点',
  `FK_NodeT` varchar(200) DEFAULT NULL COMMENT '节点名称',
  `Sender` varchar(200) DEFAULT NULL COMMENT '发送人',
  `SenderT` varchar(200) DEFAULT NULL COMMENT '发送人名称',
  `FK_Emp` varchar(100) DEFAULT NULL,
  `FK_EmpT` varchar(200) DEFAULT NULL COMMENT '当事人名称',
  `GroupEmps` varchar(400) DEFAULT NULL COMMENT '相关当事人',
  `GroupEmpsNames` varchar(900) DEFAULT NULL COMMENT '相关当事人名称',
  `GroupEmpsNum` int(11) DEFAULT '1' COMMENT '相关当事人数量',
  `DTFrom` varchar(50) DEFAULT NULL COMMENT '任务下达时间',
  `DTTo` varchar(50) DEFAULT NULL COMMENT '任务处理时间',
  `SDT` varchar(50) DEFAULT NULL COMMENT '应完成日期',
  `FK_Dept` varchar(100) DEFAULT NULL,
  `FK_DeptT` varchar(500) DEFAULT NULL COMMENT '部门名称',
  `FK_NY` varchar(100) DEFAULT NULL,
  `DTSWay` int(11) DEFAULT '0' COMMENT '考核方式',
  `TimeLimit` varchar(50) DEFAULT NULL COMMENT '规定限期',
  `OverMinutes` float DEFAULT NULL COMMENT '逾期分钟',
  `UseDays` float DEFAULT NULL COMMENT '实际使用天',
  `OverDays` float DEFAULT NULL COMMENT '逾期天',
  `CHSta` int(11) DEFAULT '0' COMMENT '状态',
  `WeekNum` int(11) DEFAULT '0' COMMENT '第几周',
  `Points` float DEFAULT NULL COMMENT '总扣分',
  `MyNum` int(11) DEFAULT '1' COMMENT '个数',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='时效考核';

-- ----------------------------
-- Records of wf_ch
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_cheval`
-- ----------------------------
DROP TABLE IF EXISTS `wf_cheval`;
CREATE TABLE `wf_cheval` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `Title` varchar(500) DEFAULT NULL COMMENT '标题',
  `FK_Flow` varchar(7) DEFAULT NULL COMMENT '流程编号',
  `FlowName` varchar(100) DEFAULT NULL COMMENT '流程名称',
  `WorkID` int(11) DEFAULT '0' COMMENT '工作ID',
  `FK_Node` int(11) DEFAULT '0' COMMENT '评价节点',
  `NodeName` varchar(100) DEFAULT NULL COMMENT '停留节点',
  `Rec` varchar(50) DEFAULT NULL COMMENT '评价人',
  `RecName` varchar(50) DEFAULT NULL COMMENT '评价人名称',
  `RDT` varchar(50) DEFAULT NULL COMMENT '评价日期',
  `EvalEmpNo` varchar(50) DEFAULT NULL COMMENT '被考核的人员编号',
  `EvalEmpName` varchar(50) DEFAULT NULL COMMENT '被考核的人员名称',
  `EvalCent` varchar(20) DEFAULT NULL COMMENT '评价分值',
  `EvalNote` varchar(20) DEFAULT NULL COMMENT '评价内容',
  `FK_Dept` varchar(50) DEFAULT NULL COMMENT '部门',
  `DeptName` varchar(100) DEFAULT NULL COMMENT '部门名称',
  `FK_NY` varchar(7) DEFAULT NULL COMMENT '年月',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作质量评价';

-- ----------------------------
-- Records of wf_cheval
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_cond`
-- ----------------------------
DROP TABLE IF EXISTS `wf_cond`;
CREATE TABLE `wf_cond` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `CondType` int(11) DEFAULT '0' COMMENT '条件类型',
  `DataFrom` int(11) DEFAULT '0' COMMENT '条件数据来源0表单,1岗位(对方向条件有效)',
  `FK_Flow` varchar(60) DEFAULT NULL COMMENT '流程',
  `NodeID` int(11) DEFAULT '0' COMMENT '发生的事件MainNode',
  `FK_Node` int(11) DEFAULT '0' COMMENT '节点ID',
  `FK_Attr` varchar(80) DEFAULT NULL COMMENT '属性',
  `AttrKey` varchar(60) DEFAULT NULL COMMENT '属性键',
  `AttrName` varchar(500) DEFAULT NULL COMMENT '中文名称',
  `FK_Operator` varchar(60) DEFAULT NULL COMMENT '运算符号',
  `OperatorValue` text COMMENT '要运算的值',
  `OperatorValueT` text COMMENT '要运算的值T',
  `ToNodeID` int(11) DEFAULT '0' COMMENT 'ToNodeID（对方向条件有效）',
  `ConnJudgeWay` int(11) DEFAULT '0' COMMENT '条件关系',
  `MyPOID` int(11) DEFAULT '0' COMMENT 'MyPOID',
  `PRI` int(11) DEFAULT '0' COMMENT '计算优先级',
  `CondOrAnd` int(11) DEFAULT '0' COMMENT '方向条件类型',
  `Note` varchar(500) DEFAULT NULL COMMENT '备注',
  `AtPara` varchar(2000) DEFAULT NULL COMMENT 'AtPara',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程条件';

-- ----------------------------
-- Records of wf_cond
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_deptflowsearch`
-- ----------------------------
DROP TABLE IF EXISTS `wf_deptflowsearch`;
CREATE TABLE `wf_deptflowsearch` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Emp` varchar(50) DEFAULT NULL COMMENT '操作员',
  `FK_Flow` varchar(50) DEFAULT NULL COMMENT '流程编号',
  `FK_Dept` varchar(100) DEFAULT NULL COMMENT '部门编号',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程部门数据查询权限';

-- ----------------------------
-- Records of wf_deptflowsearch
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_direction`
-- ----------------------------
DROP TABLE IF EXISTS `wf_direction`;
CREATE TABLE `wf_direction` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Flow` varchar(10) DEFAULT NULL COMMENT '流程',
  `Node` int(11) DEFAULT '0' COMMENT '从节点',
  `ToNode` int(11) DEFAULT '0' COMMENT '到节点',
  `IsCanBack` int(11) DEFAULT '0' COMMENT '是否可以原路返回(对后退线有效)',
  `Dots` varchar(300) DEFAULT NULL COMMENT '轨迹信息',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节点方向信息';

-- ----------------------------
-- Records of wf_direction
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_directionstation`
-- ----------------------------
DROP TABLE IF EXISTS `wf_directionstation`;
CREATE TABLE `wf_directionstation` (
  `FK_Direction` int(11) NOT NULL DEFAULT '0' COMMENT '节点',
  `FK_Station` varchar(100) NOT NULL COMMENT '工作岗位',
  PRIMARY KEY (`FK_Direction`,`FK_Station`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节点岗位';

-- ----------------------------
-- Records of wf_directionstation
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_emp`
-- ----------------------------
DROP TABLE IF EXISTS `wf_emp`;
CREATE TABLE `wf_emp` (
  `No` varchar(50) NOT NULL COMMENT '帐号',
  `Name` varchar(50) DEFAULT NULL COMMENT '名称',
  `FK_Dept` varchar(100) DEFAULT NULL COMMENT '主部门',
  `OrgNo` varchar(100) DEFAULT NULL COMMENT '组织',
  `UseSta` int(11) DEFAULT '3' COMMENT '用户状态',
  `UserType` int(11) DEFAULT '3' COMMENT '用户状态',
  `RootOfFlow` varchar(100) DEFAULT NULL COMMENT '流程权限节点',
  `RootOfForm` varchar(100) DEFAULT NULL COMMENT '表单权限节点',
  `RootOfDept` varchar(100) DEFAULT NULL COMMENT '组织结构权限节点',
  `MyNum` int(11) DEFAULT '1' COMMENT '个数',
  `Tel` varchar(50) DEFAULT '' COMMENT 'Tel',
  `Email` varchar(50) DEFAULT '' COMMENT 'Email',
  `TM` varchar(50) DEFAULT '' COMMENT '即时通讯号',
  `AlertWay` int(11) DEFAULT '3' COMMENT '收听方式',
  `Author` varchar(50) DEFAULT '' COMMENT '授权人',
  `AuthorDate` varchar(50) DEFAULT '' COMMENT '授权日期',
  `AuthorWay` int(11) DEFAULT '0' COMMENT '授权方式',
  `AuthorToDate` varchar(50) DEFAULT NULL,
  `AuthorFlows` text COMMENT '可以执行的授权流程',
  `Stas` varchar(3000) DEFAULT '' COMMENT '岗位s',
  `Depts` varchar(100) DEFAULT '' COMMENT 'Deptss',
  `FtpUrl` varchar(50) DEFAULT '' COMMENT 'FtpUrl',
  `Msg` text COMMENT 'Msg',
  `Style` text COMMENT 'Style',
  `StartFlows` text COMMENT '可以发起的流程',
  `SPass` varchar(200) DEFAULT '' COMMENT '图片签名密码',
  `Idx` int(11) DEFAULT '0' COMMENT 'Idx',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作员';

-- ----------------------------
-- Records of wf_emp
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_findworkerrole`
-- ----------------------------
DROP TABLE IF EXISTS `wf_findworkerrole`;
CREATE TABLE `wf_findworkerrole` (
  `OID` int(11) NOT NULL COMMENT 'OID',
  `Name` varchar(200) DEFAULT NULL COMMENT 'Name',
  `FK_Node` int(11) DEFAULT '0' COMMENT '节点ID',
  `SortVal0` varchar(200) DEFAULT NULL COMMENT 'SortVal0',
  `SortText0` varchar(200) DEFAULT NULL COMMENT 'SortText0',
  `SortVal1` varchar(200) DEFAULT NULL COMMENT 'SortVal1',
  `SortText1` varchar(200) DEFAULT NULL COMMENT 'SortText1',
  `SortVal2` varchar(200) DEFAULT NULL COMMENT 'SortText2',
  `SortText2` varchar(200) DEFAULT NULL COMMENT 'SortText2',
  `SortVal3` varchar(200) DEFAULT NULL COMMENT 'SortVal3',
  `SortText3` varchar(200) DEFAULT NULL COMMENT 'SortText3',
  `TagVal0` varchar(1000) DEFAULT NULL COMMENT 'TagVal0',
  `TagVal1` varchar(1000) DEFAULT NULL COMMENT 'TagVal1',
  `TagVal2` varchar(1000) DEFAULT NULL COMMENT 'TagVal2',
  `TagVal3` varchar(1000) DEFAULT NULL COMMENT 'TagVal3',
  `TagText0` varchar(1000) DEFAULT NULL COMMENT 'TagText0',
  `TagText1` varchar(1000) DEFAULT NULL COMMENT 'TagText1',
  `TagText2` varchar(1000) DEFAULT NULL COMMENT 'TagText2',
  `TagText3` varchar(1000) DEFAULT NULL COMMENT 'TagText3',
  `IsEnable` int(11) DEFAULT '1' COMMENT '是否可用',
  `Idx` int(11) DEFAULT '0' COMMENT 'IDX',
  PRIMARY KEY (`OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='找人规则';

-- ----------------------------
-- Records of wf_findworkerrole
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_flow`
-- ----------------------------
DROP TABLE IF EXISTS `wf_flow`;
CREATE TABLE `wf_flow` (
  `No` varchar(200) NOT NULL DEFAULT '',
  `FK_FlowSort` varchar(100) DEFAULT NULL COMMENT '流程类别',
  `Name` varchar(500) DEFAULT NULL,
  `FlowMark` varchar(150) DEFAULT NULL COMMENT '流程标记',
  `FlowEventEntity` varchar(150) DEFAULT NULL COMMENT '流程事件实体',
  `TitleRole` varchar(150) DEFAULT NULL COMMENT '标题生成规则',
  `IsCanStart` int(11) DEFAULT '1' COMMENT '可以独立启动否？(独立启动的流程可以显示在发起流程列表里)',
  `IsFullSA` int(11) DEFAULT '0' COMMENT '是否自动计算未来的处理人？',
  `IsAutoSendSubFlowOver` int(11) DEFAULT '0' COMMENT '为子流程时结束规则',
  `IsGuestFlow` int(11) DEFAULT '0' COMMENT '是否外部用户参与流程(非组织结构人员参与的流程)',
  `FlowAppType` int(11) DEFAULT '0' COMMENT '流程应用类型',
  `TimelineRole` int(11) DEFAULT '0' COMMENT '时效性规则',
  `Draft` int(11) DEFAULT '0' COMMENT '草稿规则',
  `FlowDeleteRole` int(11) DEFAULT '0' COMMENT '流程实例删除规则',
  `HelpUrl` varchar(300) DEFAULT NULL COMMENT '帮助文档',
  `SysType` varchar(100) DEFAULT NULL COMMENT '系统类型',
  `Tester` varchar(300) DEFAULT NULL COMMENT '发起测试人',
  `NodeAppType` varchar(50) DEFAULT NULL COMMENT '业务类型枚举(可为Null)',
  `NodeAppTypeText` varchar(200) DEFAULT NULL COMMENT '业务类型枚举(可为Null)',
  `ChartType` int(11) DEFAULT '1' COMMENT '节点图形类型',
  `HostRun` varchar(40) DEFAULT NULL COMMENT '运行主机(IP+端口)',
  `IsBatchStart` int(11) DEFAULT '0' COMMENT '是否可以批量发起流程？(如果是就要设置发起的需要填写的字段,多个用逗号分开)',
  `BatchStartFields` varchar(500) DEFAULT NULL COMMENT '发起字段s',
  `HistoryFields` varchar(500) DEFAULT NULL COMMENT '历史查看字段',
  `IsResetData` int(11) DEFAULT '0' COMMENT '是否启用开始节点数据重置按钮？',
  `IsLoadPriData` int(11) DEFAULT '0' COMMENT '是否自动装载上一笔数据？',
  `IsDBTemplate` int(11) DEFAULT '1' COMMENT '是否启用数据模版？',
  `IsStartInMobile` int(11) DEFAULT '1' COMMENT '是否可以在手机里启用？(如果发起表单特别复杂就不要在手机里启用了)',
  `IsMD5` int(11) DEFAULT '0' COMMENT '是否是数据加密流程(MD5数据加密防篡改)',
  `DataStoreModel` int(11) DEFAULT '0' COMMENT '数据存储',
  `PTable` varchar(30) DEFAULT NULL COMMENT '流程数据存储表',
  `FlowNoteExp` varchar(500) DEFAULT NULL COMMENT '备注的表达式',
  `BillNoFormat` varchar(50) DEFAULT NULL COMMENT '单据编号格式',
  `DesignerNo` varchar(50) DEFAULT NULL COMMENT '设计者编号',
  `DesignerName` varchar(50) DEFAULT NULL COMMENT '设计者名称',
  `Note` text COMMENT '流程描述',
  `MyDeptRole` int(11) DEFAULT '0' COMMENT '本部门发起的流程',
  `FlowRunWay` int(11) DEFAULT '0' COMMENT '运行方式',
  `RunObj` varchar(2000) DEFAULT '' COMMENT '运行内容',
  `RunSQL` varchar(2000) DEFAULT '' COMMENT '流程结束执行后执行的SQL',
  `NumOfBill` int(11) DEFAULT '0' COMMENT '是否有单据',
  `NumOfDtl` int(11) DEFAULT '0' COMMENT 'NumOfDtl',
  `AvgDay` float(11,2) DEFAULT '0.00' COMMENT '平均运行用天',
  `Idx` int(11) DEFAULT '0' COMMENT '显示顺序号(在发起列表中)',
  `Paras` varchar(2000) DEFAULT '' COMMENT '参数',
  `DRCtrlType` int(11) DEFAULT '0' COMMENT '部门查询权限控制方式',
  `StartLimitRole` int(11) DEFAULT '0' COMMENT '启动限制规则',
  `StartLimitPara` varchar(500) DEFAULT '' COMMENT '规则内容',
  `StartLimitAlert` varchar(500) DEFAULT '' COMMENT '限制提示',
  `StartLimitWhen` int(11) DEFAULT '0' COMMENT '提示时间',
  `StartGuideWay` int(11) DEFAULT '0' COMMENT '前置导航方式',
  `StartGuideLink` varchar(200) DEFAULT '' COMMENT '右侧的连接',
  `StartGuideLab` varchar(200) DEFAULT '' COMMENT '连接标签',
  `StartGuidePara1` varchar(500) DEFAULT '' COMMENT '参数1',
  `StartGuidePara2` varchar(500) DEFAULT '' COMMENT '参数2',
  `StartGuidePara3` varchar(500) DEFAULT '' COMMENT '参数3',
  `Ver` varchar(20) DEFAULT '' COMMENT '版本号',
  `AtPara` varchar(1000) DEFAULT '' COMMENT 'AtPara',
  `DTSWay` int(11) DEFAULT '0' COMMENT '同步方式',
  `DTSDBSrc` varchar(200) DEFAULT '' COMMENT '数据源',
  `DTSBTable` varchar(200) DEFAULT '' COMMENT '业务表名',
  `DTSBTablePK` varchar(32) DEFAULT '' COMMENT '业务表主键',
  `DTSTime` int(11) DEFAULT '0' COMMENT '执行同步时间点',
  `DTSSpecNodes` varchar(200) DEFAULT '' COMMENT '指定的节点ID',
  `DTSField` int(11) DEFAULT '0' COMMENT '要同步的字段计算方式',
  `DTSFields` varchar(2000) DEFAULT '' COMMENT '要同步的字段s,中间用逗号分开.',
  `PStarter` int(11) DEFAULT '1' COMMENT '发起人可看(必选)',
  `PWorker` int(11) DEFAULT '1' COMMENT '参与人可看(必选)',
  `PCCer` int(11) DEFAULT '1' COMMENT '被抄送人可看(必选)',
  `PMyDept` int(11) DEFAULT '1' COMMENT '本部门人可看',
  `PPMyDept` int(11) DEFAULT '1' COMMENT '直属上级部门可看(比如:我是)',
  `PPDept` int(11) DEFAULT '1' COMMENT '上级部门可看',
  `PSameDept` int(11) DEFAULT '1' COMMENT '平级部门可看',
  `PSpecDept` int(11) DEFAULT '1' COMMENT '指定部门可看',
  `PSpecDeptExt` varchar(200) DEFAULT '' COMMENT '部门编号',
  `PSpecSta` int(11) DEFAULT '1' COMMENT '指定的岗位可看',
  `PSpecStaExt` varchar(200) DEFAULT '' COMMENT '岗位编号',
  `PSpecGroup` int(11) DEFAULT '1' COMMENT '指定的权限组可看',
  `PSpecGroupExt` varchar(200) DEFAULT '' COMMENT '权限组',
  `PSpecEmp` int(11) DEFAULT '1' COMMENT '指定的人员可看',
  `PSpecEmpExt` varchar(200) DEFAULT '' COMMENT '指定的人员编号',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程模版主表';

-- ----------------------------
-- Records of wf_flow
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_flowformtree`
-- ----------------------------
DROP TABLE IF EXISTS `wf_flowformtree`;
CREATE TABLE `wf_flowformtree` (
  `No` varchar(10) NOT NULL COMMENT '编号',
  `Name` varchar(100) DEFAULT NULL COMMENT '名称',
  `ParentNo` varchar(100) DEFAULT NULL COMMENT '父节点No',
  `Idx` int(11) DEFAULT '0' COMMENT 'Idx',
  `FK_Flow` varchar(20) DEFAULT NULL COMMENT '流程编号',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='独立表单树';

-- ----------------------------
-- Records of wf_flowformtree
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_flowsort`
-- ----------------------------
DROP TABLE IF EXISTS `wf_flowsort`;
CREATE TABLE `wf_flowsort` (
  `No` varchar(100) NOT NULL COMMENT '编号',
  `ParentNo` varchar(100) DEFAULT NULL COMMENT '父节点No',
  `Name` varchar(200) DEFAULT NULL COMMENT '名称',
  `OrgNo` varchar(150) DEFAULT NULL COMMENT '组织编号(0为系统组织)',
  `Domain` varchar(100) DEFAULT NULL COMMENT '域/系统编号',
  `Idx` int(11) DEFAULT '0' COMMENT 'Idx',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程类别';

-- ----------------------------
-- Records of wf_flowsort
-- ----------------------------
INSERT INTO `wf_flowsort` VALUES ('1', '0', '流程树', '0', '', '0');
INSERT INTO `wf_flowsort` VALUES ('100', '1', '日常办公类', '0', '', '0');
INSERT INTO `wf_flowsort` VALUES ('101', '1', '财务类', '0', '', '0');
INSERT INTO `wf_flowsort` VALUES ('102', '1', '人力资源类', '0', '', '0');

-- ----------------------------
-- Table structure for `wf_frmmethod`
-- ----------------------------
DROP TABLE IF EXISTS `wf_frmmethod`;
CREATE TABLE `wf_frmmethod` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FrmID` varchar(300) DEFAULT NULL COMMENT '表单ID',
  `MethodName` varchar(300) DEFAULT NULL COMMENT '方法名',
  `MethodID` varchar(300) DEFAULT NULL COMMENT '方法ID',
  `WhatAreYouTodo` int(11) DEFAULT '0' COMMENT '执行完毕后干啥？',
  `WarningMsg` varchar(300) DEFAULT NULL COMMENT '功能执行警告信息',
  `ShowModel` int(11) DEFAULT '0' COMMENT '显示方式',
  `MethodDocTypeOfFunc` int(11) DEFAULT '0' COMMENT '内容类型',
  `MethodDoc_Url` text,
  `MsgSuccess` varchar(300) DEFAULT NULL COMMENT '成功提示信息',
  `MsgErr` varchar(300) DEFAULT NULL COMMENT '失败提示信息',
  `IsMyBillToolBar` int(11) DEFAULT '1' COMMENT '是否显示在MyBill.htm工具栏上',
  `IsMyBillToolExt` int(11) DEFAULT '0' COMMENT '是否显示在MyBill.htm工具栏右边的更多按钮里',
  `IsSearchBar` int(11) DEFAULT '0' COMMENT '是否显示在Search.htm工具栏上(用于批处理)',
  `RefMethodType` int(11) DEFAULT '0' COMMENT '方法类型',
  `Idx` int(11) DEFAULT '0' COMMENT 'Idx',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='连接';

-- ----------------------------
-- Records of wf_frmmethod
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_frmnode`
-- ----------------------------
DROP TABLE IF EXISTS `wf_frmnode`;
CREATE TABLE `wf_frmnode` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Frm` varchar(200) DEFAULT NULL,
  `FK_Node` int(11) DEFAULT '0' COMMENT '节点编号',
  `FK_Flow` varchar(20) DEFAULT NULL COMMENT '流程编号',
  `OfficeOpenLab` varchar(50) DEFAULT NULL COMMENT '打开本地标签',
  `OfficeOpenEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeOpenTemplateLab` varchar(50) DEFAULT NULL COMMENT '打开模板标签',
  `OfficeOpenTemplateEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeSaveLab` varchar(50) DEFAULT NULL COMMENT '保存标签',
  `OfficeSaveEnable` int(11) DEFAULT '1' COMMENT '是否启用',
  `OfficeAcceptLab` varchar(50) DEFAULT NULL COMMENT '接受修订标签',
  `OfficeAcceptEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeRefuseLab` varchar(50) DEFAULT NULL COMMENT '拒绝修订标签',
  `OfficeRefuseEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeOverLab` varchar(50) DEFAULT NULL COMMENT '套红按钮标签',
  `OfficeOverEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeMarksEnable` int(11) DEFAULT '1' COMMENT '是否查看用户留痕',
  `OfficePrintLab` varchar(50) DEFAULT NULL COMMENT '打印按钮标签',
  `OfficePrintEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeSealLab` varchar(50) DEFAULT NULL COMMENT '签章按钮标签',
  `OfficeSealEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeInsertFlowLab` varchar(50) DEFAULT NULL COMMENT '插入流程标签',
  `OfficeInsertFlowEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeNodeInfo` int(11) DEFAULT '0' COMMENT '是否记录节点信息',
  `OfficeReSavePDF` int(11) DEFAULT '0' COMMENT '是否该自动保存为PDF',
  `OfficeDownLab` varchar(50) DEFAULT NULL COMMENT '下载按钮标签',
  `OfficeDownEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeIsMarks` int(11) DEFAULT '1' COMMENT '是否进入留痕模式',
  `OfficeTemplate` varchar(100) DEFAULT NULL COMMENT '指定文档模板',
  `OfficeIsParent` int(11) DEFAULT '1' COMMENT '是否使用父流程的文档',
  `OfficeTHEnable` int(11) DEFAULT '0' COMMENT '是否自动套红',
  `OfficeTHTemplate` varchar(200) DEFAULT NULL COMMENT '自动套红模板',
  `FrmType` varchar(20) DEFAULT '0' COMMENT '表单类型',
  `IsPrint` int(11) DEFAULT '0' COMMENT '是否可以打印',
  `IsEnableLoadData` int(11) DEFAULT '0' COMMENT '是否启用装载填充事件',
  `IsDefaultOpen` int(11) DEFAULT '0' COMMENT '是否默认打开',
  `IsCloseEtcFrm` int(11) DEFAULT '0' COMMENT '打开时是否关闭其它的页面？',
  `IsEnableFWC` int(11) DEFAULT '0' COMMENT '是否启用审核组件？',
  `Idx` int(11) DEFAULT '0' COMMENT '顺序号',
  `FrmSln` int(11) DEFAULT '0' COMMENT '表单控制方案',
  `WhoIsPK` int(11) DEFAULT '0' COMMENT '谁是主键？',
  `Is1ToN` int(11) DEFAULT '0' COMMENT '是否1变N？',
  `HuiZong` varchar(300) DEFAULT '' COMMENT '子线程要汇总的数据表',
  `FrmEnableRole` int(11) DEFAULT '0' COMMENT '表单启用规则',
  `FrmEnableExp` text,
  `TempleteFile` varchar(500) DEFAULT '' COMMENT '模版文件',
  `IsEnable` int(11) DEFAULT '1' COMMENT '是否显示',
  `GuanJianZiDuan` varchar(20) DEFAULT '' COMMENT '关键字段',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节点表单';

-- ----------------------------
-- Records of wf_frmnode
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_generworkerlist`
-- ----------------------------
DROP TABLE IF EXISTS `wf_generworkerlist`;
CREATE TABLE `wf_generworkerlist` (
  `WorkID` int(11) NOT NULL DEFAULT '0' COMMENT '工作ID',
  `FK_Emp` varchar(20) NOT NULL COMMENT '人员',
  `FK_Node` int(11) NOT NULL DEFAULT '0' COMMENT '节点ID',
  `FID` int(11) DEFAULT '0' COMMENT '流程ID',
  `FK_EmpText` varchar(30) DEFAULT NULL COMMENT '人员名称',
  `FK_NodeText` varchar(100) DEFAULT NULL COMMENT '节点名称',
  `FK_Flow` varchar(3) DEFAULT NULL COMMENT '流程',
  `FK_Dept` varchar(100) DEFAULT NULL COMMENT '使用部门',
  `SDT` varchar(50) DEFAULT NULL COMMENT '应完成日期',
  `DTOfWarning` varchar(50) DEFAULT NULL COMMENT '警告日期',
  `RDT` varchar(50) DEFAULT NULL COMMENT '记录时间',
  `CDT` varchar(50) DEFAULT NULL COMMENT '完成时间',
  `IsEnable` int(11) DEFAULT '1' COMMENT '是否可用',
  `IsRead` int(11) DEFAULT '0' COMMENT '是否读取',
  `IsPass` int(11) DEFAULT '0' COMMENT '是否通过(对合流节点有效)',
  `WhoExeIt` int(11) DEFAULT '0' COMMENT '谁执行它',
  `Sender` varchar(200) DEFAULT NULL COMMENT '发送人',
  `PRI` int(11) DEFAULT '1' COMMENT '优先级',
  `PressTimes` int(11) DEFAULT '0' COMMENT '催办次数',
  `DTOfHungUp` varchar(50) DEFAULT NULL COMMENT '挂起时间',
  `DTOfUnHungUp` varchar(50) DEFAULT NULL COMMENT '预计解除挂起时间',
  `HungUpTimes` int(11) DEFAULT '0' COMMENT '挂起次数',
  `GuestNo` varchar(30) DEFAULT NULL COMMENT '外部用户编号',
  `GuestName` varchar(100) DEFAULT NULL COMMENT '外部用户名称',
  `AtPara` text COMMENT 'AtPara',
  PRIMARY KEY (`WorkID`,`FK_Emp`,`FK_Node`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作者';

-- ----------------------------
-- Records of wf_generworkerlist
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_generworkflow`
-- ----------------------------
DROP TABLE IF EXISTS `wf_generworkflow`;
CREATE TABLE `wf_generworkflow` (
  `WorkID` int(11) NOT NULL DEFAULT '0' COMMENT 'WorkID',
  `FID` int(11) DEFAULT '0' COMMENT '流程ID',
  `FK_FlowSort` varchar(100) DEFAULT NULL,
  `SysType` varchar(10) DEFAULT NULL COMMENT '系统类别',
  `FK_Flow` varchar(100) DEFAULT NULL,
  `FlowName` varchar(100) DEFAULT NULL COMMENT '流程名称',
  `Title` varchar(1000) DEFAULT NULL COMMENT '标题',
  `WFSta` int(11) DEFAULT '0' COMMENT '状态',
  `WFState` int(11) DEFAULT '0' COMMENT '流程状态',
  `Starter` varchar(200) DEFAULT NULL COMMENT '发起人',
  `StarterName` varchar(200) DEFAULT NULL COMMENT '发起人名称',
  `Sender` varchar(200) DEFAULT NULL COMMENT '发送人',
  `RDT` varchar(50) DEFAULT NULL COMMENT '记录日期',
  `SendDT` varchar(50) DEFAULT NULL COMMENT '流程活动时间',
  `FK_Node` int(11) DEFAULT '0' COMMENT '节点',
  `NodeName` varchar(100) DEFAULT NULL COMMENT '节点名称',
  `FK_Dept` varchar(100) DEFAULT NULL COMMENT '部门',
  `DeptName` varchar(100) DEFAULT NULL COMMENT '部门名称',
  `PRI` int(11) DEFAULT '1' COMMENT '优先级',
  `SDTOfNode` varchar(50) DEFAULT NULL COMMENT '节点应完成时间',
  `SDTOfFlow` varchar(50) DEFAULT NULL COMMENT '流程应完成时间',
  `PFlowNo` varchar(3) DEFAULT NULL COMMENT '父流程编号',
  `PWorkID` int(11) DEFAULT '0' COMMENT '父流程ID',
  `PNodeID` int(11) DEFAULT '0' COMMENT '父流程调用节点',
  `PFID` int(11) DEFAULT '0' COMMENT '父流程调用的PFID',
  `PEmp` varchar(32) DEFAULT NULL COMMENT '子流程的调用人',
  `GuestNo` varchar(100) DEFAULT NULL COMMENT '客户编号',
  `GuestName` varchar(100) DEFAULT NULL COMMENT '客户名称',
  `BillNo` varchar(100) DEFAULT NULL COMMENT '单据编号',
  `FlowNote` text COMMENT '备注',
  `TodoEmps` text COMMENT '待办人员',
  `TodoEmpsNum` int(11) DEFAULT '0' COMMENT '待办人员数量',
  `TaskSta` int(11) DEFAULT '0' COMMENT '共享状态',
  `AtPara` varchar(2000) DEFAULT NULL COMMENT '参数(流程运行设置临时存储的参数)',
  `Emps` text COMMENT '参与人',
  `GUID` varchar(36) DEFAULT NULL COMMENT 'GUID',
  `FK_NY` varchar(100) DEFAULT NULL,
  `WeekNum` int(11) DEFAULT '0' COMMENT '周次',
  `TSpan` int(11) DEFAULT '0' COMMENT '时间间隔',
  `TodoSta` int(11) DEFAULT '0' COMMENT '待办状态',
  `Domain` varchar(100) DEFAULT NULL COMMENT '域/系统编号',
  `MyNum` int(11) DEFAULT '1' COMMENT '个数',
  PRIMARY KEY (`WorkID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程实例';

-- ----------------------------
-- Records of wf_generworkflow
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_hungup`
-- ----------------------------
DROP TABLE IF EXISTS `wf_hungup`;
CREATE TABLE `wf_hungup` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Node` int(11) DEFAULT '0' COMMENT '节点ID',
  `WorkID` int(11) DEFAULT '0' COMMENT 'WorkID',
  `HungUpWay` int(11) DEFAULT '0' COMMENT '挂起方式',
  `Note` text COMMENT '挂起原因(标题与内容支持变量)',
  `Rec` varchar(50) DEFAULT NULL COMMENT '挂起人',
  `DTOfHungUp` varchar(50) DEFAULT NULL COMMENT '挂起时间',
  `DTOfUnHungUp` varchar(50) DEFAULT NULL COMMENT '实际解除挂起时间',
  `DTOfUnHungUpPlan` varchar(50) DEFAULT NULL COMMENT '预计解除挂起时间',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='挂起';

-- ----------------------------
-- Records of wf_hungup
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_labnote`
-- ----------------------------
DROP TABLE IF EXISTS `wf_labnote`;
CREATE TABLE `wf_labnote` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `Name` varchar(3000) DEFAULT NULL,
  `FK_Flow` varchar(100) DEFAULT NULL COMMENT '流程',
  `X` int(11) DEFAULT '0' COMMENT 'X坐标',
  `Y` int(11) DEFAULT '0' COMMENT 'Y坐标',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='标签';

-- ----------------------------
-- Records of wf_labnote
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_node`
-- ----------------------------
DROP TABLE IF EXISTS `wf_node`;
CREATE TABLE `wf_node` (
  `NodeID` int(11) NOT NULL DEFAULT '0' COMMENT '节点ID',
  `Step` int(11) DEFAULT '0' COMMENT '步骤(无计算意义)',
  `FK_Flow` varchar(150) DEFAULT NULL,
  `FlowName` varchar(200) DEFAULT NULL COMMENT '流程名',
  `Name` varchar(200) DEFAULT NULL,
  `Tip` varchar(100) DEFAULT NULL COMMENT '操作提示',
  `WhoExeIt` int(11) DEFAULT '0' COMMENT '谁执行它',
  `ReadReceipts` int(11) DEFAULT '0' COMMENT '已读回执',
  `CondModel` int(11) DEFAULT '0' COMMENT '方向条件控制规则',
  `CancelRole` int(11) DEFAULT '0' COMMENT '撤销规则',
  `CancelDisWhenRead` int(11) DEFAULT '0' COMMENT '对方已经打开就不能撤销',
  `IsTask` int(11) DEFAULT '1' COMMENT '允许分配工作否?',
  `IsExpSender` int(11) DEFAULT '1' COMMENT '本节点接收人不允许包含上一步发送人',
  `IsRM` int(11) DEFAULT '1' COMMENT '是否启用投递路径自动记忆功能?',
  `DTFrom` varchar(50) DEFAULT NULL COMMENT '生命周期从',
  `DTTo` varchar(50) DEFAULT NULL COMMENT '生命周期到',
  `IsBUnit` int(11) DEFAULT '0' COMMENT '是否是节点模版（业务单元）?',
  `FocusField` varchar(50) DEFAULT NULL COMMENT '焦点字段',
  `IsGuestNode` int(11) DEFAULT '0' COMMENT '是否是外部用户执行的节点(非组织结构人员参与处理工作的节点)?',
  `NodeAppType` int(11) DEFAULT '0' COMMENT '节点业务类型',
  `FWCSta` int(11) DEFAULT '0' COMMENT '节点状态',
  `SelfParas` varchar(1000) DEFAULT NULL,
  `RunModel` int(11) DEFAULT '0' COMMENT '节点类型',
  `SubThreadType` int(11) DEFAULT '0' COMMENT '子线程类型',
  `PassRate` float DEFAULT NULL COMMENT '完成通过率',
  `SubFlowStartWay` int(11) DEFAULT '0' COMMENT '子线程启动方式',
  `SubFlowStartParas` varchar(100) DEFAULT NULL COMMENT '启动参数',
  `ThreadIsCanDel` int(11) DEFAULT '0' COMMENT '是否可以删除子线程(当前节点已经发送出去的线程，并且当前节点是分流，或者分合流有效，在子线程退回后的操作)？',
  `ThreadIsCanShift` int(11) DEFAULT '0' COMMENT '是否可以移交子线程(当前节点已经发送出去的线程，并且当前节点是分流，或者分合流有效，在子线程退回后的操作)？',
  `IsAllowRepeatEmps` int(11) DEFAULT '0' COMMENT '是否允许子线程接受人员重复(仅当分流点向子线程发送时有效)?',
  `AutoRunEnable` int(11) DEFAULT '0' COMMENT '是否启用自动运行？(仅当分流点向子线程发送时有效)',
  `AutoRunParas` varchar(100) DEFAULT NULL COMMENT '自动运行SQL',
  `AutoJumpRole0` int(11) DEFAULT '0' COMMENT '处理人就是发起人',
  `AutoJumpRole1` int(11) DEFAULT '0' COMMENT '处理人已经出现过',
  `AutoJumpRole2` int(11) DEFAULT '0' COMMENT '处理人与上一步相同',
  `WhenNoWorker` int(11) DEFAULT '0' COMMENT '(是)找不到人就跳转,(否)提示错误.',
  `SendLab` varchar(50) DEFAULT NULL COMMENT '发送按钮标签',
  `SendJS` varchar(999) DEFAULT NULL COMMENT '按钮JS函数',
  `SaveLab` varchar(50) DEFAULT NULL COMMENT '保存按钮标签',
  `SaveEnable` int(11) DEFAULT '1' COMMENT '是否启用',
  `ThreadLab` varchar(50) DEFAULT NULL COMMENT '子线程按钮标签',
  `ThreadEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `ThreadKillRole` int(11) DEFAULT '0' COMMENT '子线程删除方式',
  `JumpWayLab` varchar(50) DEFAULT NULL COMMENT '跳转按钮标签',
  `JumpWay` int(11) DEFAULT '0' COMMENT '跳转规则',
  `JumpToNodes` varchar(200) DEFAULT NULL COMMENT '可跳转的节点',
  `ReturnLab` varchar(50) DEFAULT NULL COMMENT '退回按钮标签',
  `ReturnRole` int(11) DEFAULT '0' COMMENT '退回规则',
  `ReturnAlert` varchar(999) DEFAULT NULL COMMENT '被退回后信息提示',
  `IsBackTracking` int(11) DEFAULT '0' COMMENT '是否可以原路返回(启用退回功能才有效)',
  `ReturnField` varchar(50) DEFAULT NULL COMMENT '退回信息填写字段',
  `ReturnReasonsItems` varchar(999) DEFAULT NULL COMMENT '退回原因',
  `ReturnOneNodeRole` int(11) DEFAULT '0' COMMENT '单节点退回规则',
  `CCLab` varchar(50) DEFAULT NULL COMMENT '抄送按钮标签',
  `CCRole` int(11) DEFAULT '0' COMMENT '抄送规则',
  `CCWriteTo` int(11) DEFAULT '0' COMMENT '抄送写入规则',
  `DoOutTime` varchar(300) DEFAULT NULL COMMENT '超时处理内容',
  `DoOutTimeCond` varchar(200) DEFAULT NULL COMMENT '执行超时的条件',
  `ShiftLab` varchar(50) DEFAULT NULL COMMENT '移交按钮标签',
  `ShiftEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `DelLab` varchar(50) DEFAULT NULL COMMENT '删除按钮标签',
  `DelEnable` int(11) DEFAULT '0' COMMENT '删除规则',
  `EndFlowLab` varchar(50) DEFAULT NULL COMMENT '结束流程按钮标签',
  `EndFlowEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeBtnLab` varchar(50) DEFAULT NULL COMMENT '公文按钮标签',
  `OfficeBtnEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `PrintHtmlLab` varchar(50) DEFAULT NULL COMMENT '打印Html标签',
  `PrintHtmlEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `PrintPDFLab` varchar(50) DEFAULT NULL COMMENT '打印pdf标签',
  `PrintPDFEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `PrintPDFModle` int(11) DEFAULT '0' COMMENT 'PDF打印规则',
  `PrintZipLab` varchar(50) DEFAULT NULL COMMENT '打包下载zip按钮标签',
  `PrintZipEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `PrintDocLab` varchar(50) DEFAULT NULL COMMENT '打印单据按钮标签',
  `PrintDocEnable` int(11) DEFAULT '0' COMMENT '打印方式',
  `TrackLab` varchar(50) DEFAULT NULL COMMENT '轨迹按钮标签',
  `TrackEnable` int(11) DEFAULT '1' COMMENT '是否启用',
  `HungLab` varchar(50) DEFAULT NULL COMMENT '挂起按钮标签',
  `HungEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `SearchLab` varchar(50) DEFAULT NULL COMMENT '查询按钮标签',
  `SearchEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `WorkCheckLab` varchar(50) DEFAULT NULL COMMENT '审核按钮标签',
  `WorkCheckEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `AskforLab` varchar(50) DEFAULT NULL COMMENT '加签按钮标签',
  `AskforEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `HuiQianLab` varchar(50) DEFAULT NULL COMMENT '会签标签',
  `HuiQianRole` int(11) DEFAULT '0' COMMENT '会签模式',
  `TCLab` varchar(50) DEFAULT NULL COMMENT '流转自定义',
  `TCEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `WebOffice` varchar(50) DEFAULT NULL COMMENT '文档按钮标签',
  `WebOfficeEnable` int(11) DEFAULT '0' COMMENT '文档启用方式',
  `PRILab` varchar(50) DEFAULT NULL COMMENT '重要性',
  `PRIEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `AllotLab` varchar(50) DEFAULT NULL COMMENT '分配按钮标签',
  `AllotEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `FocusLab` varchar(50) DEFAULT NULL COMMENT '关注',
  `FocusEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `ConfirmLab` varchar(50) DEFAULT NULL COMMENT '确认按钮标签',
  `ConfirmEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `ListLab` varchar(50) DEFAULT NULL COMMENT '列表按钮标签',
  `ListEnable` int(11) DEFAULT '1' COMMENT '是否启用',
  `BatchLab` varchar(50) DEFAULT NULL COMMENT '批量审核标签',
  `BatchEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `TimeLimit` float DEFAULT NULL COMMENT '限期(天)',
  `TWay` int(11) DEFAULT '0' COMMENT '时间计算方式',
  `TAlertRole` int(11) DEFAULT '0' COMMENT '逾期提醒规则',
  `TAlertWay` int(11) DEFAULT '0' COMMENT '逾期提醒方式',
  `WarningDay` float DEFAULT NULL COMMENT '工作预警(天)',
  `WAlertRole` int(11) DEFAULT '0' COMMENT '预警提醒规则',
  `WAlertWay` int(11) DEFAULT '0' COMMENT '预警提醒方式',
  `TCent` float DEFAULT NULL COMMENT '扣分(每延期1小时)',
  `CHWay` int(11) DEFAULT '0' COMMENT '考核方式',
  `IsEval` int(11) DEFAULT '0' COMMENT '是否工作质量考核',
  `OutTimeDeal` int(11) DEFAULT '0' COMMENT '超时处理方式',
  `CCIsAttr` int(11) DEFAULT '0' COMMENT '按表单字段抄送',
  `CCFormAttr` varchar(100) DEFAULT '' COMMENT '抄送人员字段',
  `CCIsStations` int(11) DEFAULT '0' COMMENT '按照岗位抄送',
  `CCStaWay` int(11) DEFAULT '0' COMMENT '抄送岗位计算方式',
  `CCIsDepts` int(11) DEFAULT '0' COMMENT '按照部门抄送',
  `CCIsEmps` int(11) DEFAULT '0' COMMENT '按照人员抄送',
  `CCIsSQLs` int(11) DEFAULT '0' COMMENT '按照SQL抄送',
  `CCSQL` varchar(200) DEFAULT '' COMMENT 'SQL表达式',
  `CCTitle` varchar(100) DEFAULT '' COMMENT '抄送标题',
  `CCDoc` text COMMENT '抄送内容(标题与内容支持变量)',
  `FWCLab` varchar(100) DEFAULT '审核信息' COMMENT '显示标签',
  `FWCShowModel` int(11) DEFAULT '1' COMMENT '显示方式',
  `FWCType` int(11) DEFAULT '0' COMMENT '审核组件',
  `FWCNodeName` varchar(100) DEFAULT '' COMMENT '节点意见名称',
  `FWCAth` int(11) DEFAULT '0' COMMENT '附件上传',
  `FWCTrackEnable` int(11) DEFAULT '1' COMMENT '轨迹图是否显示？',
  `FWCListEnable` int(11) DEFAULT '1' COMMENT '历史审核信息是否显示？(否,仅出现意见框)',
  `FWCIsShowAllStep` int(11) DEFAULT '0' COMMENT '在轨迹表里是否显示所有的步骤？',
  `FWCOpLabel` varchar(50) DEFAULT '审核' COMMENT '操作名词(审核/审阅/批示)',
  `FWCDefInfo` varchar(50) DEFAULT '同意' COMMENT '默认审核信息',
  `SigantureEnabel` int(11) DEFAULT '0' COMMENT '操作人是否显示为图片签名？',
  `FWCIsFullInfo` int(11) DEFAULT '1' COMMENT '如果用户未审核是否按照默认意见填充？',
  `FWC_X` float(11,2) DEFAULT '300.00' COMMENT '位置X',
  `FWC_Y` float(11,2) DEFAULT '500.00' COMMENT '位置Y',
  `FWC_H` float(11,2) DEFAULT '300.00' COMMENT '高度(0=100%)',
  `FWC_W` float(11,2) DEFAULT '400.00' COMMENT '宽度(0=100%)',
  `FWCFields` varchar(50) DEFAULT '' COMMENT '审批格式字段',
  `FWCIsShowTruck` int(11) DEFAULT '0' COMMENT '是否显示未审核的轨迹？',
  `FWCIsShowReturnMsg` int(11) DEFAULT '0' COMMENT '是否显示退回信息？',
  `FWCOrderModel` int(11) DEFAULT '0' COMMENT '协作模式下操作员显示顺序',
  `FWCMsgShow` int(11) DEFAULT '0' COMMENT '审核意见显示方式',
  `FTCLab` varchar(50) DEFAULT '流转自定义' COMMENT '显示标签',
  `FTCSta` int(11) DEFAULT '0' COMMENT '组件状态',
  `FTCWorkModel` int(11) DEFAULT '0' COMMENT '工作模式',
  `FTC_X` float(11,2) DEFAULT '5.00' COMMENT '位置X',
  `FTC_Y` float(11,2) DEFAULT '5.00' COMMENT '位置Y',
  `FTC_H` float(11,2) DEFAULT '300.00' COMMENT '高度',
  `FTC_W` float(11,2) DEFAULT '400.00' COMMENT '宽度',
  `SelectAccepterLab` varchar(50) DEFAULT '接受人' COMMENT '接受人按钮标签',
  `SelectAccepterEnable` int(11) DEFAULT '0' COMMENT '方式',
  `CHLab` varchar(50) DEFAULT '节点时限' COMMENT '节点时限',
  `CHEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeOpenLab` varchar(50) DEFAULT '打开本地' COMMENT '打开本地标签',
  `OfficeOpenEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeOpenTemplateLab` varchar(50) DEFAULT '打开模板' COMMENT '打开模板标签',
  `OfficeOpenTemplateEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeSaveLab` varchar(50) DEFAULT '保存' COMMENT '保存标签',
  `OfficeSaveEnable` int(11) DEFAULT '1' COMMENT '是否启用',
  `OfficeAcceptLab` varchar(50) DEFAULT '接受修订' COMMENT '接受修订标签',
  `OfficeAcceptEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeRefuseLab` varchar(50) DEFAULT '拒绝修订' COMMENT '拒绝修订标签',
  `OfficeRefuseEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeOverLab` varchar(50) DEFAULT '套红' COMMENT '套红标签',
  `OfficeOverEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeMarksEnable` int(11) DEFAULT '1' COMMENT '是否查看用户留痕',
  `OfficePrintLab` varchar(50) DEFAULT '打印' COMMENT '打印标签',
  `OfficePrintEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeSealLab` varchar(50) DEFAULT '签章' COMMENT '签章标签',
  `OfficeSealEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeInsertFlowLab` varchar(50) DEFAULT '插入流程' COMMENT '插入流程标签',
  `OfficeInsertFlowEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeNodeInfo` int(11) DEFAULT '0' COMMENT '是否记录节点信息',
  `OfficeReSavePDF` int(11) DEFAULT '0' COMMENT '是否该自动保存为PDF',
  `OfficeDownLab` varchar(50) DEFAULT '下载' COMMENT '下载按钮标签',
  `OfficeDownEnable` int(11) DEFAULT '0' COMMENT '是否启用',
  `OfficeIsMarks` int(11) DEFAULT '1' COMMENT '是否进入留痕模式',
  `OfficeTemplate` varchar(100) DEFAULT '' COMMENT '指定文档模板',
  `OfficeIsParent` int(11) DEFAULT '1' COMMENT '是否使用父流程的文档',
  `OfficeTHEnable` int(11) DEFAULT '0' COMMENT '是否自动套红',
  `OfficeTHTemplate` varchar(200) DEFAULT '' COMMENT '自动套红模板',
  `SFLab` varchar(200) DEFAULT '子流程' COMMENT '显示标签',
  `SFSta` int(11) DEFAULT '0' COMMENT '父子流程状态',
  `SFShowModel` int(11) DEFAULT '1' COMMENT '显示方式',
  `SFCaption` varchar(100) DEFAULT '启动子流程' COMMENT '连接标题',
  `SFDefInfo` varchar(50) DEFAULT '' COMMENT '可启动的子流程编号(多个用逗号分开)',
  `SFActiveFlows` varchar(100) DEFAULT NULL,
  `SF_X` float(11,2) DEFAULT '5.00' COMMENT '位置X',
  `SF_Y` float(11,2) DEFAULT '5.00' COMMENT '位置Y',
  `SF_H` float(11,2) DEFAULT '300.00' COMMENT '高度',
  `SF_W` float(11,2) DEFAULT '400.00' COMMENT '宽度',
  `SFFields` varchar(50) DEFAULT '' COMMENT '审批格式字段',
  `SFShowCtrl` int(11) DEFAULT '0' COMMENT '显示控制方式',
  `SFOpenType` int(11) DEFAULT '0' COMMENT '打开子流程显示',
  `SelectorModel` int(11) DEFAULT '5' COMMENT '显示方式',
  `FK_SQLTemplate` varchar(50) DEFAULT '' COMMENT 'SQL模版',
  `FK_SQLTemplateText` varchar(200) DEFAULT '' COMMENT 'SQL模版',
  `IsAutoLoadEmps` int(11) DEFAULT '1' COMMENT '是否自动加载上一次选择的人员？',
  `IsSimpleSelector` int(11) DEFAULT '0' COMMENT '是否单项选择(只能选择一个人)？',
  `IsEnableDeptRange` int(11) DEFAULT '0' COMMENT '是否启用部门搜索范围限定(对使用通用人员选择器有效)？',
  `IsEnableStaRange` int(11) DEFAULT '0' COMMENT '是否启用岗位搜索范围限定(对使用通用人员选择器有效)？',
  `SelectorP1` text COMMENT '分组参数:可以为空,比如:SELECT No,Name,ParentNo FROM  Port_Dept',
  `SelectorP2` text COMMENT '操作员数据源:比如:SELECT No,Name,FK_Dept FROM  Port_Emp',
  `SelectorP3` text COMMENT '默认选择的数据源:比如:SELECT FK_Emp FROM  WF_GenerWorkerList WHERE FK_Node=102 AND WorkID=@WorkID',
  `SelectorP4` text COMMENT '强制选择的数据源:比如:SELECT FK_Emp FROM  WF_GenerWorkerList WHERE FK_Node=102 AND WorkID=@WorkID',
  `OfficeOpen` varchar(50) DEFAULT '打开本地' COMMENT '打开本地标签',
  `OfficeOpenTemplate` varchar(50) DEFAULT '打开模板' COMMENT '打开模板标签',
  `OfficeSave` varchar(50) DEFAULT '保存' COMMENT '保存标签',
  `OfficeAccept` varchar(50) DEFAULT '接受修订' COMMENT '接受修订标签',
  `OfficeRefuse` varchar(50) DEFAULT '拒绝修订' COMMENT '拒绝修订标签',
  `OfficeOver` varchar(50) DEFAULT '套红按钮' COMMENT '套红按钮标签',
  `OfficeMarks` int(11) DEFAULT '1' COMMENT '是否查看用户留痕',
  `OfficeReadOnly` int(11) DEFAULT '0' COMMENT '是否只读',
  `OfficePrint` varchar(50) DEFAULT '打印按钮' COMMENT '打印按钮标签',
  `OfficeSeal` varchar(50) DEFAULT '签章按钮' COMMENT '签章按钮标签',
  `OfficeInsertFlow` varchar(50) DEFAULT '插入流程' COMMENT '插入流程标签',
  `OfficeIsTrueTH` int(11) DEFAULT '0' COMMENT '是否自动套红',
  `WebOfficeFrmModel` int(11) DEFAULT '0' COMMENT '表单工作方式',
  `FrmThreadLab` varchar(200) DEFAULT '子线程' COMMENT '显示标签',
  `FrmThreadSta` int(11) DEFAULT '0' COMMENT '组件状态',
  `FrmThread_X` float(11,2) DEFAULT '5.00' COMMENT '位置X',
  `FrmThread_Y` float(11,2) DEFAULT '5.00' COMMENT '位置Y',
  `FrmThread_H` float(11,2) DEFAULT '300.00' COMMENT '高度',
  `FrmThread_W` float(11,2) DEFAULT '400.00' COMMENT '宽度',
  `CheckNodes` varchar(50) DEFAULT '' COMMENT '工作节点s',
  `DeliveryWay` int(11) DEFAULT '0' COMMENT '访问规则',
  `X` int(11) DEFAULT '0' COMMENT 'X坐标',
  `Y` int(11) DEFAULT '0' COMMENT 'Y坐标',
  `FrmTrackLab` varchar(200) DEFAULT '轨迹' COMMENT '显示标签',
  `FrmTrackSta` int(11) DEFAULT '0' COMMENT '组件状态',
  `FrmTrack_X` float(11,2) DEFAULT '5.00' COMMENT '位置X',
  `FrmTrack_Y` float(11,2) DEFAULT '5.00' COMMENT '位置Y',
  `FrmTrack_H` float(11,2) DEFAULT '300.00' COMMENT '高度',
  `FrmTrack_W` float(11,2) DEFAULT '400.00' COMMENT '宽度',
  `ICON` varchar(70) DEFAULT '' COMMENT '节点ICON图片路径',
  `NodeWorkType` int(11) DEFAULT '0' COMMENT '节点类型',
  `FrmAttr` varchar(300) DEFAULT '' COMMENT 'FrmAttr',
  `Doc` varchar(100) DEFAULT '' COMMENT '描述',
  `DeliveryParas` varchar(300) DEFAULT '' COMMENT '访问规则设置',
  `NodeFrmID` varchar(50) DEFAULT '' COMMENT '节点表单ID',
  `SaveModel` int(11) DEFAULT '0' COMMENT '保存模式',
  `IsCanDelFlow` int(11) DEFAULT '0' COMMENT '是否可以删除流程',
  `TodolistModel` int(11) DEFAULT '0' COMMENT '多人处理规则',
  `TeamLeaderConfirmRole` int(11) DEFAULT '0' COMMENT '组长确认规则',
  `TeamLeaderConfirmDoc` varchar(100) DEFAULT '' COMMENT '组长确认设置内容',
  `IsHandOver` int(11) DEFAULT '0' COMMENT '是否可以移交',
  `BlockModel` int(11) DEFAULT '0' COMMENT '阻塞模式',
  `BlockExp` varchar(200) DEFAULT '' COMMENT '阻塞表达式',
  `BlockAlert` varchar(100) DEFAULT '' COMMENT '被阻塞提示信息',
  `BatchRole` int(11) DEFAULT '0' COMMENT '批处理',
  `BatchListCount` int(11) DEFAULT '12' COMMENT '批处理数量',
  `BatchParas` varchar(500) DEFAULT '' COMMENT '参数',
  `FormType` int(11) DEFAULT '1' COMMENT '表单类型',
  `FormUrl` varchar(300) DEFAULT 'http://' COMMENT '表单URL',
  `TurnToDeal` int(11) DEFAULT '0' COMMENT '转向处理',
  `TurnToDealDoc` varchar(200) DEFAULT '' COMMENT '发送后提示信息',
  `NodePosType` int(11) DEFAULT '0' COMMENT '位置',
  `IsCCFlow` int(11) DEFAULT '0' COMMENT '是否有流程完成条件',
  `HisStas` varchar(300) DEFAULT '' COMMENT '岗位',
  `HisDeptStrs` varchar(300) DEFAULT '' COMMENT '部门',
  `HisToNDs` varchar(50) DEFAULT '' COMMENT '转到的节点',
  `HisBillIDs` varchar(50) DEFAULT '' COMMENT '单据IDs',
  `HisSubFlows` varchar(30) DEFAULT '' COMMENT 'HisSubFlows',
  `PTable` varchar(100) DEFAULT '' COMMENT '物理表',
  `GroupStaNDs` varchar(200) DEFAULT '' COMMENT '岗位分组节点',
  `RefOneFrmTreeType` varchar(100) DEFAULT '' COMMENT '独立表单类型',
  `AtPara` varchar(500) DEFAULT '' COMMENT 'AtPara',
  PRIMARY KEY (`NodeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节点';

-- ----------------------------
-- Records of wf_node
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_nodecancel`
-- ----------------------------
DROP TABLE IF EXISTS `wf_nodecancel`;
CREATE TABLE `wf_nodecancel` (
  `FK_Node` int(11) NOT NULL DEFAULT '0' COMMENT '节点',
  `CancelTo` int(11) NOT NULL DEFAULT '0' COMMENT '撤销到',
  PRIMARY KEY (`FK_Node`,`CancelTo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='可撤销的节点';

-- ----------------------------
-- Records of wf_nodecancel
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_nodedept`
-- ----------------------------
DROP TABLE IF EXISTS `wf_nodedept`;
CREATE TABLE `wf_nodedept` (
  `FK_Node` int(11) NOT NULL DEFAULT '0' COMMENT '节点',
  `FK_Dept` varchar(100) NOT NULL COMMENT '部门',
  PRIMARY KEY (`FK_Node`,`FK_Dept`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节点部门';

-- ----------------------------
-- Records of wf_nodedept
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_nodeemp`
-- ----------------------------
DROP TABLE IF EXISTS `wf_nodeemp`;
CREATE TABLE `wf_nodeemp` (
  `FK_Node` int(11) NOT NULL DEFAULT '0' COMMENT 'Node',
  `FK_Emp` varchar(100) NOT NULL COMMENT '到人员',
  PRIMARY KEY (`FK_Node`,`FK_Emp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节点人员';

-- ----------------------------
-- Records of wf_nodeemp
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_nodereturn`
-- ----------------------------
DROP TABLE IF EXISTS `wf_nodereturn`;
CREATE TABLE `wf_nodereturn` (
  `FK_Node` int(11) NOT NULL DEFAULT '0' COMMENT '节点',
  `ReturnTo` int(11) NOT NULL DEFAULT '0' COMMENT '退回到',
  `Dots` varchar(300) DEFAULT NULL COMMENT '轨迹信息',
  PRIMARY KEY (`FK_Node`,`ReturnTo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='可退回的节点';

-- ----------------------------
-- Records of wf_nodereturn
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_nodestation`
-- ----------------------------
DROP TABLE IF EXISTS `wf_nodestation`;
CREATE TABLE `wf_nodestation` (
  `FK_Node` int(11) NOT NULL DEFAULT '0' COMMENT '节点',
  `FK_Station` varchar(100) NOT NULL COMMENT '工作岗位',
  PRIMARY KEY (`FK_Node`,`FK_Station`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节点岗位';

-- ----------------------------
-- Records of wf_nodestation
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_nodesubflow`
-- ----------------------------
DROP TABLE IF EXISTS `wf_nodesubflow`;
CREATE TABLE `wf_nodesubflow` (
  `OID` int(11) NOT NULL COMMENT 'OID',
  `FK_Node` int(11) DEFAULT '0' COMMENT '节点',
  `FK_Flow` varchar(100) DEFAULT NULL COMMENT '延续子流程',
  `Idx` int(11) DEFAULT '0' COMMENT '显示顺序',
  `ExpType` int(11) DEFAULT '3' COMMENT '表达式类型',
  `CondExp` varchar(500) DEFAULT NULL COMMENT '条件表达式',
  `YBFlowReturnRole` int(11) DEFAULT '0' COMMENT '退回方式',
  `ReturnToNode` varchar(50) DEFAULT NULL COMMENT '要退回的节点',
  `ReturnToNodeText` varchar(200) DEFAULT NULL COMMENT '要退回的节点',
  PRIMARY KEY (`OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='延续子流程';

-- ----------------------------
-- Records of wf_nodesubflow
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_nodetoolbar`
-- ----------------------------
DROP TABLE IF EXISTS `wf_nodetoolbar`;
CREATE TABLE `wf_nodetoolbar` (
  `OID` int(11) NOT NULL COMMENT 'OID',
  `Title` varchar(100) DEFAULT NULL COMMENT '标题',
  `ExcType` int(11) DEFAULT '0' COMMENT '执行类型',
  `Url` varchar(500) DEFAULT NULL COMMENT '连接/函数',
  `Target` varchar(100) DEFAULT NULL COMMENT '目标',
  `ShowWhere` int(11) DEFAULT '1' COMMENT '显示位置',
  `Idx` int(11) DEFAULT '0' COMMENT '显示顺序',
  `FK_Node` int(11) DEFAULT '0' COMMENT '节点',
  `MyFileName` varchar(300) DEFAULT NULL COMMENT '图标',
  `MyFilePath` varchar(300) DEFAULT NULL COMMENT 'MyFilePath',
  `MyFileExt` varchar(20) DEFAULT NULL COMMENT 'MyFileExt',
  `WebPath` varchar(300) DEFAULT NULL COMMENT 'WebPath',
  `MyFileH` int(11) DEFAULT '0' COMMENT 'MyFileH',
  `MyFileW` int(11) DEFAULT '0' COMMENT 'MyFileW',
  `MyFileSize` float DEFAULT NULL COMMENT 'MyFileSize',
  PRIMARY KEY (`OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自定义工具栏';

-- ----------------------------
-- Records of wf_nodetoolbar
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_part`
-- ----------------------------
DROP TABLE IF EXISTS `wf_part`;
CREATE TABLE `wf_part` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Flow` varchar(100) DEFAULT NULL COMMENT '流程编号',
  `FK_Node` int(11) DEFAULT '0' COMMENT '节点ID',
  `PartType` varchar(100) DEFAULT NULL COMMENT '类型',
  `Tag0` varchar(2000) DEFAULT NULL COMMENT 'Tag0',
  `Tag1` varchar(2000) DEFAULT NULL COMMENT 'Tag1',
  `Tag2` varchar(2000) DEFAULT NULL COMMENT 'Tag2',
  `Tag3` varchar(2000) DEFAULT NULL COMMENT 'Tag3',
  `Tag4` varchar(2000) DEFAULT NULL COMMENT 'Tag4',
  `Tag5` varchar(2000) DEFAULT NULL COMMENT 'Tag5',
  `Tag6` varchar(2000) DEFAULT NULL COMMENT 'Tag6',
  `Tag7` varchar(2000) DEFAULT NULL COMMENT 'Tag7',
  `Tag8` varchar(2000) DEFAULT NULL COMMENT 'Tag8',
  `Tag9` varchar(2000) DEFAULT NULL COMMENT 'Tag9',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='前置导航-父子流程';

-- ----------------------------
-- Records of wf_part
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_pushmsg`
-- ----------------------------
DROP TABLE IF EXISTS `wf_pushmsg`;
CREATE TABLE `wf_pushmsg` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Flow` varchar(3) DEFAULT NULL COMMENT '流程',
  `FK_Node` int(11) DEFAULT '0' COMMENT '节点',
  `FK_Event` varchar(15) DEFAULT NULL COMMENT '事件类型',
  `PushWay` int(11) DEFAULT '0' COMMENT '推送方式',
  `PushDoc` text COMMENT '推送保存内容',
  `Tag` varchar(500) DEFAULT NULL COMMENT 'Tag',
  `SMSPushWay` int(11) DEFAULT '0' COMMENT '短信发送方式',
  `SMSField` varchar(100) DEFAULT NULL COMMENT '短信字段',
  `SMSDoc` text COMMENT '短信内容模版',
  `SMSNodes` varchar(100) DEFAULT NULL COMMENT 'SMS节点s',
  `MailPushWay` int(11) DEFAULT '0' COMMENT '邮件发送方式',
  `MailAddress` varchar(100) DEFAULT NULL COMMENT '邮件字段',
  `MailTitle` varchar(200) DEFAULT NULL COMMENT '邮件标题模版',
  `MailDoc` text COMMENT '邮件内容模版',
  `MailNodes` varchar(100) DEFAULT NULL COMMENT 'Mail节点s',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息推送';

-- ----------------------------
-- Records of wf_pushmsg
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_rememberme`
-- ----------------------------
DROP TABLE IF EXISTS `wf_rememberme`;
CREATE TABLE `wf_rememberme` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Node` int(11) DEFAULT '0' COMMENT '节点',
  `FK_Emp` varchar(30) DEFAULT NULL COMMENT '当前操作人员',
  `Objs` text COMMENT '分配人员',
  `ObjsExt` text COMMENT '分配人员Ext',
  `Emps` text COMMENT '所有的工作人员',
  `EmpsExt` text COMMENT '工作人员Ext',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记忆我';

-- ----------------------------
-- Records of wf_rememberme
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_returnwork`
-- ----------------------------
DROP TABLE IF EXISTS `wf_returnwork`;
CREATE TABLE `wf_returnwork` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `WorkID` int(11) DEFAULT '0' COMMENT 'WorkID',
  `ReturnNode` int(11) DEFAULT '0' COMMENT '退回节点',
  `ReturnNodeName` varchar(100) DEFAULT NULL COMMENT '退回节点名称',
  `Returner` varchar(20) DEFAULT NULL COMMENT '退回人',
  `ReturnerName` varchar(100) DEFAULT NULL COMMENT '退回人名称',
  `ReturnToNode` int(11) DEFAULT '0' COMMENT 'ReturnToNode',
  `ReturnToEmp` text COMMENT '退回给',
  `BeiZhu` text COMMENT '退回原因',
  `RDT` varchar(50) DEFAULT NULL COMMENT '退回日期',
  `IsBackTracking` int(11) DEFAULT '0' COMMENT '是否要原路返回?',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退回轨迹';

-- ----------------------------
-- Records of wf_returnwork
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_selectaccper`
-- ----------------------------
DROP TABLE IF EXISTS `wf_selectaccper`;
CREATE TABLE `wf_selectaccper` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Node` int(11) DEFAULT '0' COMMENT '接受人节点',
  `WorkID` int(11) DEFAULT '0' COMMENT 'WorkID',
  `FK_Emp` varchar(20) DEFAULT NULL COMMENT 'FK_Emp',
  `EmpName` varchar(20) DEFAULT NULL COMMENT 'EmpName',
  `DeptName` varchar(400) DEFAULT NULL COMMENT '部门名称',
  `AccType` int(11) DEFAULT '0' COMMENT '类型(@0=接受人@1=抄送人)',
  `Rec` varchar(20) DEFAULT NULL COMMENT '记录人',
  `Info` varchar(200) DEFAULT NULL COMMENT '办理意见信息',
  `IsRemember` int(11) DEFAULT '0' COMMENT '以后发送是否按本次计算',
  `Idx` int(11) DEFAULT '0' COMMENT '顺序号(可以用于流程队列审核模式)',
  `Tag` varchar(200) DEFAULT NULL COMMENT '维度信息Tag',
  `TimeLimit` int(11) DEFAULT '0' COMMENT '时限-天',
  `TSpanHour` float DEFAULT NULL COMMENT '时限-小时',
  `ADT` varchar(50) DEFAULT NULL COMMENT '到达日期(计划)',
  `SDT` varchar(50) DEFAULT NULL COMMENT '应完成日期(计划)',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='选择接受/抄送人信息';

-- ----------------------------
-- Records of wf_selectaccper
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_shiftwork`
-- ----------------------------
DROP TABLE IF EXISTS `wf_shiftwork`;
CREATE TABLE `wf_shiftwork` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `WorkID` int(11) DEFAULT '0' COMMENT '工作ID',
  `FK_Node` int(11) DEFAULT '0' COMMENT 'FK_Node',
  `FK_Emp` varchar(40) DEFAULT NULL COMMENT '移交人',
  `FK_EmpName` varchar(40) DEFAULT NULL COMMENT '移交人名称',
  `ToEmp` varchar(40) DEFAULT NULL COMMENT '移交给',
  `ToEmpName` varchar(40) DEFAULT NULL COMMENT '移交给名称',
  `RDT` varchar(50) DEFAULT NULL COMMENT '移交时间',
  `Note` varchar(2000) DEFAULT NULL COMMENT '移交原因',
  `IsRead` int(11) DEFAULT '0' COMMENT '是否读取？',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='移交记录';

-- ----------------------------
-- Records of wf_shiftwork
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_sqltemplate`
-- ----------------------------
DROP TABLE IF EXISTS `wf_sqltemplate`;
CREATE TABLE `wf_sqltemplate` (
  `No` varchar(3) NOT NULL COMMENT '编号',
  `SQLType` int(11) DEFAULT '0' COMMENT '模版SQL类型',
  `Name` varchar(200) DEFAULT NULL COMMENT 'SQL说明',
  `Docs` text COMMENT 'SQL模版',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='SQL模板';

-- ----------------------------
-- Records of wf_sqltemplate
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_task`
-- ----------------------------
DROP TABLE IF EXISTS `wf_task`;
CREATE TABLE `wf_task` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Flow` varchar(200) DEFAULT NULL COMMENT '流程编号',
  `Starter` varchar(200) DEFAULT NULL COMMENT '发起人',
  `ToNode` int(11) DEFAULT '0' COMMENT '到达的节点',
  `ToEmps` varchar(200) DEFAULT NULL COMMENT '到达人员',
  `Paras` text COMMENT '参数',
  `TaskSta` int(11) DEFAULT '0' COMMENT '任务状态',
  `Msg` text COMMENT '消息',
  `StartDT` varchar(20) DEFAULT NULL COMMENT '发起时间',
  `RDT` varchar(20) DEFAULT NULL COMMENT '插入数据时间',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务';

-- ----------------------------
-- Records of wf_task
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_testapi`
-- ----------------------------
DROP TABLE IF EXISTS `wf_testapi`;
CREATE TABLE `wf_testapi` (
  `No` varchar(92) NOT NULL COMMENT '编号',
  `Name` varchar(50) DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试过程';

-- ----------------------------
-- Records of wf_testapi
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_testcase`
-- ----------------------------
DROP TABLE IF EXISTS `wf_testcase`;
CREATE TABLE `wf_testcase` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `FK_Flow` varchar(100) DEFAULT NULL COMMENT '流程编号',
  `ParaType` varchar(100) DEFAULT NULL COMMENT '参数类型',
  `Vals` varchar(500) DEFAULT NULL COMMENT '值s',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自定义流程测试';

-- ----------------------------
-- Records of wf_testcase
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_testsample`
-- ----------------------------
DROP TABLE IF EXISTS `wf_testsample`;
CREATE TABLE `wf_testsample` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `Name` varchar(50) DEFAULT NULL COMMENT '测试名称',
  `FK_API` varchar(100) DEFAULT NULL COMMENT '测试的API',
  `FK_Ver` varchar(100) DEFAULT NULL COMMENT '测试的版本',
  `DTFrom` varchar(50) DEFAULT NULL COMMENT '从',
  `DTTo` varchar(50) DEFAULT NULL COMMENT '到',
  `TimeUse` float DEFAULT NULL COMMENT '用时(毫秒)',
  `TimesPerSecond` float DEFAULT NULL COMMENT '每秒跑多少个?',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试明细';

-- ----------------------------
-- Records of wf_testsample
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_testver`
-- ----------------------------
DROP TABLE IF EXISTS `wf_testver`;
CREATE TABLE `wf_testver` (
  `No` varchar(92) NOT NULL COMMENT '编号',
  `Name` varchar(50) DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试版本';

-- ----------------------------
-- Records of wf_testver
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_track`
-- ----------------------------
DROP TABLE IF EXISTS `wf_track`;
CREATE TABLE `wf_track` (
  `MyPK` int(11) NOT NULL DEFAULT '0' COMMENT 'MyPK',
  `ActionType` int(11) DEFAULT '0' COMMENT '类型',
  `ActionTypeText` varchar(30) DEFAULT NULL COMMENT '类型(名称)',
  `FID` int(11) DEFAULT '0' COMMENT '流程ID',
  `WorkID` int(11) DEFAULT '0' COMMENT '工作ID',
  `NDFrom` int(11) DEFAULT '0' COMMENT '从节点',
  `NDFromT` varchar(300) DEFAULT NULL COMMENT '从节点(名称)',
  `NDTo` int(11) DEFAULT '0' COMMENT '到节点',
  `NDToT` varchar(999) DEFAULT NULL COMMENT '到节点(名称)',
  `EmpFrom` varchar(20) DEFAULT NULL COMMENT '从人员',
  `EmpFromT` varchar(30) DEFAULT NULL COMMENT '从人员(名称)',
  `EmpTo` varchar(2000) DEFAULT NULL COMMENT '到人员',
  `EmpToT` varchar(2000) DEFAULT NULL COMMENT '到人员(名称)',
  `RDT` varchar(20) DEFAULT NULL COMMENT '日期',
  `WorkTimeSpan` float DEFAULT NULL COMMENT '时间跨度(天)',
  `Msg` text COMMENT '消息',
  `NodeData` text COMMENT '节点数据(日志信息)',
  `Tag` varchar(300) DEFAULT NULL COMMENT '参数',
  `Exer` varchar(200) DEFAULT NULL COMMENT '执行人',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='轨迹表';

-- ----------------------------
-- Records of wf_track
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_transfercustom`
-- ----------------------------
DROP TABLE IF EXISTS `wf_transfercustom`;
CREATE TABLE `wf_transfercustom` (
  `MyPK` varchar(100) NOT NULL COMMENT '主键MyPK',
  `WorkID` int(11) DEFAULT '0' COMMENT 'WorkID',
  `FK_Node` int(11) DEFAULT '0' COMMENT '节点ID',
  `Worker` varchar(200) DEFAULT NULL COMMENT '处理人(多个人用逗号分开)',
  `WorkerName` varchar(200) DEFAULT NULL COMMENT '处理人(多个人用逗号分开)',
  `SubFlowNo` varchar(3) DEFAULT NULL COMMENT '要经过的子流程编号',
  `PlanDT` varchar(50) DEFAULT NULL COMMENT '计划完成日期',
  `TodolistModel` int(11) DEFAULT '0' COMMENT '多人工作处理模式',
  `Idx` int(11) DEFAULT '0' COMMENT '顺序号',
  PRIMARY KEY (`MyPK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自定义运行路径';

-- ----------------------------
-- Records of wf_transfercustom
-- ----------------------------

-- ----------------------------
-- Table structure for `wf_workflowdeletelog`
-- ----------------------------
DROP TABLE IF EXISTS `wf_workflowdeletelog`;
CREATE TABLE `wf_workflowdeletelog` (
  `OID` int(11) NOT NULL COMMENT 'OID',
  `FID` int(11) DEFAULT '0' COMMENT 'FID',
  `FK_Dept` varchar(100) DEFAULT NULL COMMENT '部门',
  `Title` varchar(100) DEFAULT NULL COMMENT '标题',
  `FlowStarter` varchar(100) DEFAULT NULL COMMENT '发起人',
  `FlowStartRDT` varchar(50) DEFAULT NULL COMMENT '发起时间',
  `FK_NY` varchar(100) DEFAULT NULL COMMENT '年月',
  `FK_Flow` varchar(100) DEFAULT NULL COMMENT '流程',
  `FlowEnderRDT` varchar(50) DEFAULT NULL COMMENT '最后处理时间',
  `FlowEndNode` int(11) DEFAULT '0' COMMENT '停留节点',
  `FlowDaySpan` float DEFAULT NULL COMMENT '跨度(天)',
  `FlowEmps` varchar(100) DEFAULT NULL COMMENT '参与人',
  `Oper` varchar(20) DEFAULT NULL COMMENT '删除人员',
  `OperDept` varchar(20) DEFAULT NULL COMMENT '删除人员部门',
  `OperDeptName` varchar(200) DEFAULT NULL COMMENT '删除人员名称',
  `DeleteNote` text COMMENT '删除原因',
  `DeleteDT` varchar(50) DEFAULT NULL COMMENT '删除日期',
  PRIMARY KEY (`OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程删除日志';

-- ----------------------------
-- Records of wf_workflowdeletelog
-- ----------------------------

-- ----------------------------
-- View structure for `port_inc`
-- ----------------------------
DROP VIEW IF EXISTS `port_inc`;
CREATE ALGORITHM=UNDEFINED DEFINER=`admin`@`%` SQL SECURITY DEFINER VIEW `port_inc` AS select `port_dept`.`No` AS `No`,`port_dept`.`Name` AS `Name`,`port_dept`.`NameOfPath` AS `NameOfPath`,`port_dept`.`ParentNo` AS `ParentNo`,`port_dept`.`Idx` AS `Idx`,`port_dept`.`OrgNo` AS `OrgNo` from `port_dept` where ((`port_dept`.`No` = '100') or (`port_dept`.`No` = '1060') or (`port_dept`.`No` = '1070')) ;

-- ----------------------------
-- View structure for `v_flowstarter`
-- ----------------------------
DROP VIEW IF EXISTS `v_flowstarter`;
CREATE ALGORITHM=UNDEFINED DEFINER=`admin`@`%` SQL SECURITY DEFINER VIEW `v_flowstarter` AS select `a`.`FK_Flow` AS `FK_Flow`,`a`.`FlowName` AS `FlowName`,`c`.`FK_Emp` AS `FK_Emp` from ((`wf_node` `a` join `wf_nodestation` `b`) join `port_deptempstation` `c`) where ((`a`.`NodePosType` = 0) and ((`a`.`WhoExeIt` = 0) or (`a`.`WhoExeIt` = 2)) and (`a`.`NodeID` = `b`.`FK_Node`) and (`b`.`FK_Station` = `c`.`FK_Station`) and ((`a`.`DeliveryWay` = 0) or (`a`.`DeliveryWay` = 14))) union select `a`.`FK_Flow` AS `FK_Flow`,`a`.`FlowName` AS `FlowName`,`c`.`No` AS `No` from ((`wf_node` `a` join `wf_nodedept` `b`) join `port_emp` `c`) where ((`a`.`NodePosType` = 0) and ((`a`.`WhoExeIt` = 0) or (`a`.`WhoExeIt` = 2)) and (`a`.`NodeID` = `b`.`FK_Node`) and (`b`.`FK_Dept` = `c`.`FK_Dept`) and (`a`.`DeliveryWay` = 1)) union select `a`.`FK_Flow` AS `FK_Flow`,`a`.`FlowName` AS `FlowName`,`b`.`FK_Emp` AS `FK_Emp` from (`wf_node` `a` join `wf_nodeemp` `b`) where ((`a`.`NodePosType` = 0) and ((`a`.`WhoExeIt` = 0) or (`a`.`WhoExeIt` = 2)) and (`a`.`NodeID` = `b`.`FK_Node`) and (`a`.`DeliveryWay` = 3)) union select `a`.`FK_Flow` AS `FK_Flow`,`a`.`FlowName` AS `FlowName`,`b`.`No` AS `FK_Emp` from (`wf_node` `a` join `port_emp` `b`) where ((`a`.`NodePosType` = 0) and ((`a`.`WhoExeIt` = 0) or (`a`.`WhoExeIt` = 2)) and (`a`.`DeliveryWay` = 4)) union select `a`.`FK_Flow` AS `FK_Flow`,`a`.`FlowName` AS `FlowName`,`e`.`FK_Emp` AS `FK_Emp` from ((((`wf_node` `a` join `wf_nodedept` `b`) join `wf_nodestation` `c`) join `port_emp` `d`) join `port_deptempstation` `e`) where ((`a`.`NodePosType` = 0) and ((`a`.`WhoExeIt` = 0) or (`a`.`WhoExeIt` = 2)) and (`a`.`NodeID` = `b`.`FK_Node`) and (`a`.`NodeID` = `c`.`FK_Node`) and (`b`.`FK_Dept` = `d`.`FK_Dept`) and (`c`.`FK_Station` = `e`.`FK_Station`) and (`a`.`DeliveryWay` = 9)) ;

-- ----------------------------
-- View structure for `v_flowstarterbpm`
-- ----------------------------
DROP VIEW IF EXISTS `v_flowstarterbpm`;
CREATE ALGORITHM=UNDEFINED DEFINER=`admin`@`%` SQL SECURITY DEFINER VIEW `v_flowstarterbpm` AS select `a`.`FK_Flow` AS `FK_Flow`,`a`.`FlowName` AS `FlowName`,`c`.`FK_Emp` AS `FK_Emp` from ((`wf_node` `a` join `wf_nodestation` `b`) join `port_deptempstation` `c`) where ((`a`.`NodePosType` = 0) and ((`a`.`WhoExeIt` = 0) or (`a`.`WhoExeIt` = 2)) and (`a`.`NodeID` = `b`.`FK_Node`) and (`b`.`FK_Station` = `c`.`FK_Station`) and ((`a`.`DeliveryWay` = 0) or (`a`.`DeliveryWay` = 14))) union select `a`.`FK_Flow` AS `FK_Flow`,`a`.`FlowName` AS `FlowName`,`c`.`FK_Emp` AS `FK_Emp` from ((`wf_node` `a` join `wf_nodedept` `b`) join `port_deptemp` `c`) where ((`a`.`NodePosType` = 0) and ((`a`.`WhoExeIt` = 0) or (`a`.`WhoExeIt` = 2)) and (`a`.`NodeID` = `b`.`FK_Node`) and (`b`.`FK_Dept` = `c`.`FK_Dept`) and (`a`.`DeliveryWay` = 1)) union select `a`.`FK_Flow` AS `FK_Flow`,`a`.`FlowName` AS `FlowName`,`b`.`FK_Emp` AS `FK_Emp` from (`wf_node` `a` join `wf_nodeemp` `b`) where ((`a`.`NodePosType` = 0) and ((`a`.`WhoExeIt` = 0) or (`a`.`WhoExeIt` = 2)) and (`a`.`NodeID` = `b`.`FK_Node`) and (`a`.`DeliveryWay` = 3)) union select `a`.`FK_Flow` AS `FK_Flow`,`a`.`FlowName` AS `FlowName`,`b`.`No` AS `FK_Emp` from (`wf_node` `a` join `port_emp` `b`) where ((`a`.`NodePosType` = 0) and ((`a`.`WhoExeIt` = 0) or (`a`.`WhoExeIt` = 2)) and (`a`.`DeliveryWay` = 4)) union select `a`.`FK_Flow` AS `FK_Flow`,`a`.`FlowName` AS `FlowName`,`e`.`FK_Emp` AS `FK_Emp` from (((`wf_node` `a` join `wf_nodedept` `b`) join `wf_nodestation` `c`) join `port_deptempstation` `e`) where ((`a`.`NodePosType` = 0) and ((`a`.`WhoExeIt` = 0) or (`a`.`WhoExeIt` = 2)) and (`a`.`NodeID` = `b`.`FK_Node`) and (`a`.`NodeID` = `c`.`FK_Node`) and (`b`.`FK_Dept` = `e`.`FK_Dept`) and (`c`.`FK_Station` = `e`.`FK_Station`) and (`a`.`DeliveryWay` = 9)) ;

-- ----------------------------
-- View structure for `v_totalch`
-- ----------------------------
DROP VIEW IF EXISTS `v_totalch`;
CREATE ALGORITHM=UNDEFINED DEFINER=`admin`@`%` SQL SECURITY DEFINER VIEW `v_totalch` AS select `wf_ch`.`FK_Emp` AS `FK_Emp`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`)) AS `AllNum`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` <= 1) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`))) AS `ASNum`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` >= 2) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`))) AS `CSNum`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` = 0) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`))) AS `JiShi`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` = 1) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`))) AS `ANQI`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` = 2) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`))) AS `YuQi`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` = 3) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`))) AS `ChaoQi`,round((((select cast(count(`a`.`MyPK`) as decimal(10,0)) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` <= 1) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`))) / (select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`))) * 100),2) AS `WCRate` from `wf_ch` group by `wf_ch`.`FK_Emp` ;

-- ----------------------------
-- View structure for `v_totalchweek`
-- ----------------------------
DROP VIEW IF EXISTS `v_totalchweek`;
CREATE ALGORITHM=UNDEFINED DEFINER=`admin`@`%` SQL SECURITY DEFINER VIEW `v_totalchweek` AS select `wf_ch`.`FK_Emp` AS `FK_Emp`,`wf_ch`.`WeekNum` AS `WeekNum`,`wf_ch`.`FK_NY` AS `FK_NY`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`FK_Emp` = `wf_ch`.`FK_Emp`) and (`a`.`WeekNum` = `wf_ch`.`WeekNum`))) AS `AllNum`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` <= 1) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`) and (`a`.`WeekNum` = `wf_ch`.`WeekNum`))) AS `ASNum`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` >= 2) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`) and (`a`.`WeekNum` = `wf_ch`.`WeekNum`))) AS `CSNum`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` = 0) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`) and (`a`.`WeekNum` = `wf_ch`.`WeekNum`))) AS `JiShi`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` = 1) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`) and (`a`.`WeekNum` = `wf_ch`.`WeekNum`))) AS `AnQi`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` = 2) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`) and (`a`.`WeekNum` = `wf_ch`.`WeekNum`))) AS `YuQi`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` = 3) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`) and (`a`.`WeekNum` = `wf_ch`.`WeekNum`))) AS `ChaoQi`,round((((select cast(count(`a`.`MyPK`) as decimal(10,0)) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` <= 1) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`) and (`a`.`WeekNum` = `wf_ch`.`WeekNum`))) / (select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`FK_Emp` = `wf_ch`.`FK_Emp`) and (`a`.`WeekNum` = `wf_ch`.`WeekNum`)))) * 100),2) AS `WCRate` from `wf_ch` group by `wf_ch`.`FK_Emp`,`wf_ch`.`WeekNum`,`wf_ch`.`FK_NY` ;

-- ----------------------------
-- View structure for `v_totalchyf`
-- ----------------------------
DROP VIEW IF EXISTS `v_totalchyf`;
CREATE ALGORITHM=UNDEFINED DEFINER=`admin`@`%` SQL SECURITY DEFINER VIEW `v_totalchyf` AS select `wf_ch`.`FK_Emp` AS `FK_Emp`,`wf_ch`.`FK_NY` AS `FK_NY`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`FK_Emp` = `wf_ch`.`FK_Emp`) and (`a`.`FK_NY` = `wf_ch`.`FK_NY`))) AS `AllNum`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` <= 1) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`) and (`a`.`FK_NY` = `wf_ch`.`FK_NY`))) AS `ASNum`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` >= 2) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`) and (`a`.`FK_NY` = `wf_ch`.`FK_NY`))) AS `CSNum`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` = 0) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`) and (`a`.`FK_NY` = `wf_ch`.`FK_NY`))) AS `JiShi`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` = 1) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`) and (`a`.`FK_NY` = `wf_ch`.`FK_NY`))) AS `AnQi`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` = 2) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`) and (`a`.`FK_NY` = `wf_ch`.`FK_NY`))) AS `YuQi`,(select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` = 3) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`) and (`a`.`FK_NY` = `wf_ch`.`FK_NY`))) AS `ChaoQi`,round((((select cast(count(`a`.`MyPK`) as decimal(10,0)) AS `Num` from `wf_ch` `a` where ((`a`.`CHSta` <= 1) and (`a`.`FK_Emp` = `wf_ch`.`FK_Emp`) and (`a`.`FK_NY` = `wf_ch`.`FK_NY`))) / (select count(`a`.`MyPK`) AS `Num` from `wf_ch` `a` where ((`a`.`FK_Emp` = `wf_ch`.`FK_Emp`) and (`a`.`FK_NY` = `wf_ch`.`FK_NY`)))) * 100),2) AS `WCRate` from `wf_ch` group by `wf_ch`.`FK_Emp`,`wf_ch`.`FK_NY` ;

-- ----------------------------
-- View structure for `v_wf_delay`
-- ----------------------------
DROP VIEW IF EXISTS `v_wf_delay`;
CREATE ALGORITHM=UNDEFINED DEFINER=`admin`@`%` SQL SECURITY DEFINER VIEW `v_wf_delay` AS select concat(`wf_empworks`.`WorkID`,'_',`wf_empworks`.`FK_Emp`,'_',`wf_empworks`.`FK_Node`) AS `MyPK`,`wf_empworks`.`PRI` AS `PRI`,`wf_empworks`.`WorkID` AS `WorkID`,`wf_empworks`.`IsRead` AS `IsRead`,`wf_empworks`.`Starter` AS `Starter`,`wf_empworks`.`StarterName` AS `StarterName`,`wf_empworks`.`WFState` AS `WFState`,`wf_empworks`.`FK_Dept` AS `FK_Dept`,`wf_empworks`.`DeptName` AS `DeptName`,`wf_empworks`.`FK_Flow` AS `FK_Flow`,`wf_empworks`.`FlowName` AS `FlowName`,`wf_empworks`.`PWorkID` AS `PWorkID`,`wf_empworks`.`PFlowNo` AS `PFlowNo`,`wf_empworks`.`FK_Node` AS `FK_Node`,`wf_empworks`.`NodeName` AS `NodeName`,`wf_empworks`.`WorkerDept` AS `WorkerDept`,`wf_empworks`.`Title` AS `Title`,`wf_empworks`.`RDT` AS `RDT`,`wf_empworks`.`ADT` AS `ADT`,`wf_empworks`.`SDT` AS `SDT`,`wf_empworks`.`FK_Emp` AS `FK_Emp`,`wf_empworks`.`FID` AS `FID`,`wf_empworks`.`FK_FlowSort` AS `FK_FlowSort`,`wf_empworks`.`SysType` AS `SysType`,`wf_empworks`.`SDTOfNode` AS `SDTOfNode`,`wf_empworks`.`PressTimes` AS `PressTimes`,`wf_empworks`.`GuestNo` AS `GuestNo`,`wf_empworks`.`GuestName` AS `GuestName`,`wf_empworks`.`BillNo` AS `BillNo`,`wf_empworks`.`FlowNote` AS `FlowNote`,`wf_empworks`.`TodoEmps` AS `TodoEmps`,`wf_empworks`.`TodoEmpsNum` AS `TodoEmpsNum`,`wf_empworks`.`TodoSta` AS `TodoSta`,`wf_empworks`.`TaskSta` AS `TaskSta`,`wf_empworks`.`ListType` AS `ListType`,`wf_empworks`.`Sender` AS `Sender`,`wf_empworks`.`AtPara` AS `AtPara`,`wf_empworks`.`MyNum` AS `MyNum` from `wf_empworks` where (`wf_empworks`.`SDT` > now()) ;

-- ----------------------------
-- View structure for `wf_empworks`
-- ----------------------------
DROP VIEW IF EXISTS `wf_empworks`;
CREATE ALGORITHM=UNDEFINED DEFINER=`admin`@`%` SQL SECURITY DEFINER VIEW `wf_empworks` AS select `a`.`PRI` AS `PRI`,`a`.`WorkID` AS `WorkID`,`b`.`IsRead` AS `IsRead`,`a`.`Starter` AS `Starter`,`a`.`StarterName` AS `StarterName`,`a`.`WFState` AS `WFState`,`a`.`FK_Dept` AS `FK_Dept`,`a`.`DeptName` AS `DeptName`,`a`.`FK_Flow` AS `FK_Flow`,`a`.`FlowName` AS `FlowName`,`a`.`PWorkID` AS `PWorkID`,`a`.`PFlowNo` AS `PFlowNo`,`b`.`FK_Node` AS `FK_Node`,`b`.`FK_NodeText` AS `NodeName`,`b`.`FK_Dept` AS `WorkerDept`,`a`.`Title` AS `Title`,`a`.`RDT` AS `RDT`,`b`.`RDT` AS `ADT`,`b`.`SDT` AS `SDT`,`b`.`FK_Emp` AS `FK_Emp`,`b`.`FID` AS `FID`,`a`.`FK_FlowSort` AS `FK_FlowSort`,`a`.`SysType` AS `SysType`,`a`.`SDTOfNode` AS `SDTOfNode`,`b`.`PressTimes` AS `PressTimes`,`a`.`GuestNo` AS `GuestNo`,`a`.`GuestName` AS `GuestName`,`a`.`BillNo` AS `BillNo`,`a`.`FlowNote` AS `FlowNote`,`a`.`TodoEmps` AS `TodoEmps`,`a`.`TodoEmpsNum` AS `TodoEmpsNum`,`a`.`TodoSta` AS `TodoSta`,`a`.`TaskSta` AS `TaskSta`,0 AS `ListType`,`a`.`Sender` AS `Sender`,`a`.`AtPara` AS `AtPara`,1 AS `MyNum` from (`wf_generworkflow` `a` join `wf_generworkerlist` `b`) where ((`b`.`IsEnable` = 1) and (`b`.`IsPass` = 0) and (`a`.`WorkID` = `b`.`WorkID`) and (`a`.`FK_Node` = `b`.`FK_Node`) and (`a`.`WFState` <> 0) and (`b`.`WhoExeIt` <> 1)) union select `a`.`PRI` AS `PRI`,`a`.`WorkID` AS `WorkID`,`b`.`Sta` AS `IsRead`,`a`.`Starter` AS `Starter`,`a`.`StarterName` AS `StarterName`,2 AS `WFState`,`a`.`FK_Dept` AS `FK_Dept`,`a`.`DeptName` AS `DeptName`,`a`.`FK_Flow` AS `FK_Flow`,`a`.`FlowName` AS `FlowName`,`a`.`PWorkID` AS `PWorkID`,`a`.`PFlowNo` AS `PFlowNo`,`b`.`FK_Node` AS `FK_Node`,`b`.`NodeName` AS `NodeName`,`b`.`CCToDept` AS `WorkerDept`,`a`.`Title` AS `Title`,`a`.`RDT` AS `RDT`,`b`.`RDT` AS `ADT`,`b`.`RDT` AS `SDT`,`b`.`CCTo` AS `FK_Emp`,`b`.`FID` AS `FID`,`a`.`FK_FlowSort` AS `FK_FlowSort`,`a`.`SysType` AS `SysType`,`a`.`SDTOfNode` AS `SDTOfNode`,0 AS `PressTimes`,`a`.`GuestNo` AS `GuestNo`,`a`.`GuestName` AS `GuestName`,`a`.`BillNo` AS `BillNo`,`a`.`FlowNote` AS `FlowNote`,`a`.`TodoEmps` AS `TodoEmps`,`a`.`TodoEmpsNum` AS `TodoEmpsNum`,0 AS `TodoSta`,0 AS `TaskSta`,1 AS `ListType`,`b`.`Rec` AS `Sender`,('@IsCC=1' or `a`.`AtPara`) AS `AtPara`,1 AS `MyNum` from (`wf_generworkflow` `a` join `wf_cclist` `b`) where ((`a`.`WorkID` = `b`.`WorkID`) and (`b`.`Sta` <= 1) and (`b`.`InEmpWorks` = 1) and (`a`.`WFState` <> 0)) ;

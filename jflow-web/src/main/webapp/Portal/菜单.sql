-- 系统;
DELETE FROM GPM_App WHERE No='GJTLJ' ;
INSERT INTO GPM_App(No,Name,AppModel,UrlExt,MyFileName,MyFilePath,MyFileExt,WebPath,FK_AppSort,RefMenuNo) VALUES ('GJTLJ','国家铁路局公文管理',0,'http://www.chichengoa.org/App/login/login.ashx','ccoa.png','Path','GIF','/DataUser/BP.GPM.STem/CCOA.png','01','2002');

-- 删除所有的菜单.
DELETE FROM GPM_Menu WHERE FK_App='GJTLJ';

-- root;
INSERT INTO GPM_Menu(FK_App,No,ParentNo,Name,MenuType,UrlExt,IsEnable) VALUES ('GJTLJ','2000','0','国家铁路局公文管理',1,'',1);
 
-- GJTLJ 流程管理;
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD01', '资产业务流程管理', '2000',1, 3, 'GJTLJ', '', 0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0101', '发起', 'HD01', 1, 4, 'GJTLJ', '/WF/Start.htm',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0102', '待办', 'HD01', 2, 4, 'GJTLJ', '/WF/Todolist.htm',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0103', '已办', 'HD01', 3, 4, 'GJTLJ', '/WF/Runing.htm',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0104', '查询', 'HD01', 3, 4, 'GJTLJ', '/WF/Search.htm',  0,'',1);
  
 
-- GJTLJ 系统管理;
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD99', '组织结构管理', '2000',1, 3, 'GJTLJ', '', 0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9900', '部门树', 'HD99', 2, 4, 'GJTLJ', '/WF/Comm/Tree.htm?EnsName=BP.GPM.Depts',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9901', '部门台账', 'HD99', 2, 4, 'GJTLJ', '/WF/Comm/Search.htm?EnsName=BP.GPM.Depts',  0,'',1);

INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9902', '人员台账', 'HD99', 2, 4, 'GJTLJ', '/WF/Comm/Search.htm?EnsName=BP.GPM.Emps',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9905', '组织结构', 'HD99', 2, 4, 'GJTLJ', '/GPM/Organization.htm',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9906', '岗位类型', 'HD99', 1, 4, 'GJTLJ', '/WF/Comm/Search.htm?EnsName=BP.Port.StationTypes',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9907', '岗位', 'HD99', 1, 4, 'GJTLJ', '/WF/Comm/Search.htm?EnsName=BP.GPM.Stations',  0,'',1);

-- GJTLJ 系统/菜单/权限;
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD98', '系统/菜单/权限', '2000',1, 3, 'GJTLJ', '', 0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9803', '权限组菜单', 'HD98', 1, 4, 'GJTLJ', '/WF/Comm/Search.htm?EnsName=BP.GPM.Groups',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9806', '菜单台账', 'HD98', 1, 4, 'GJTLJ', '/WF/Comm/Search.htm?EnsName=BP.GPM.Menus',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9807', '菜单树', 'HD98', 1, 4, 'GJTLJ', '/WF/Comm/Tree.htm?EnsName=BP.GPM.Menus',  0,'',1);

UPDATE GPM_Menu SET IsEnable=1;
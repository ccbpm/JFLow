-- 系统;
DELETE FROM GPM_App WHERE No='ZhengZhou' ;
INSERT INTO GPM_App(No,Name,AppModel,UrlExt,MyFileName,MyFilePath,MyFileExt,WebPath,FK_AppSort,RefMenuNo) VALUES ('ZhengZhou','河南财险',0,'http://www.chichengoa.org/App/login/login.ashx','ccoa.png','Path','GIF','/DataUser/BP.GPM.STem/CCOA.png','01','2002');

-- 删除所有的菜单.
DELETE FROM GPM_Menu WHERE FK_App='ZhengZhou';

-- root;
INSERT INTO GPM_Menu(FK_App,No,ParentNo,Name,MenuType,UrlExt,IsEnable) VALUES ('ZhengZhou','2000','0','河南财险',1,'',1);
 
-- ZhengZhou 流程管理;
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD01', '业务流程', '2000',1, 3, 'ZhengZhou', '', 0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0101', '发起', 'HD01', 1, 4, 'ZhengZhou', '/WF/Start.htm',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0102', '待办', 'HD01', 2, 4, 'ZhengZhou', '/WF/Todolist.htm',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0103', '已办', 'HD01', 3, 4, 'ZhengZhou', '/WF/Runing.htm',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0104', '查询', 'HD01', 3, 4, 'ZhengZhou', '/WF/Search.htm',  0,'',1);
  
  -- ZhengZhou 流程管理;
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD02', '报案流程', '2000',1, 3, 'ZhengZhou', '', 0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0201', '报案库', 'HD02', 1, 4, 'ZhengZhou', '/WF/Comm/Search.htm?EnsName=BP.App.PICC.BaoAns',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0202', '办理中', 'HD02', 2, 4, 'ZhengZhou', '/WF/Comm/Search.htm?EnsName=BP.App.ND1Rpts',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0203', '已办结', 'HD02', 3, 4, 'ZhengZhou', '/WF/Comm/Search.htm',  0,'',1);
  
 
-- ZhengZhou 系统管理;
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD99', '组织结构管理', '2000',1, 3, 'ZhengZhou', '', 0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9900', '部门树', 'HD99', 2, 4, 'ZhengZhou', '/WF/Comm/Tree.htm?EnsName=BP.GPM.Depts',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9901', '部门台账', 'HD99', 2, 4, 'ZhengZhou', '/WF/Comm/Search.htm?EnsName=BP.GPM.Depts',  0,'',1);

INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9902', '人员台账', 'HD99', 2, 4, 'ZhengZhou', '/WF/Comm/Search.htm?EnsName=BP.GPM.Emps',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9905', '组织结构', 'HD99', 2, 4, 'ZhengZhou', '/GPM/Organization.htm',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9906', '岗位类型', 'HD99', 1, 4, 'ZhengZhou', '/WF/Comm/Search.htm?EnsName=BP.Port.StationTypes',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9907', '岗位', 'HD99', 1, 4, 'ZhengZhou', '/WF/Comm/Search.htm?EnsName=BP.GPM.Stations',  0,'',1);

-- ZhengZhou 系统/菜单/权限;
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD98', '系统/菜单/权限', '2000',1, 3, 'ZhengZhou', '', 0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9803', '权限组菜单', 'HD98', 1, 4, 'ZhengZhou', '/WF/Comm/Search.htm?EnsName=BP.GPM.Groups',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9806', '菜单台账', 'HD98', 1, 4, 'ZhengZhou', '/WF/Comm/Search.htm?EnsName=BP.GPM.Menus',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9807', '菜单树', 'HD98', 1, 4, 'ZhengZhou', '/WF/Comm/Tree.htm?EnsName=BP.GPM.Menus',  0,'',1);

UPDATE GPM_Menu SET IsEnable=1;
-- ϵͳ;
DELETE FROM GPM_App WHERE No='ZhengZhou' ;
INSERT INTO GPM_App(No,Name,AppModel,UrlExt,MyFileName,MyFilePath,MyFileExt,WebPath,FK_AppSort,RefMenuNo) VALUES ('ZhengZhou','���ϲ���',0,'http://www.chichengoa.org/App/login/login.ashx','ccoa.png','Path','GIF','/DataUser/BP.GPM.STem/CCOA.png','01','2002');

-- ɾ�����еĲ˵�.
DELETE FROM GPM_Menu WHERE FK_App='ZhengZhou';

-- root;
INSERT INTO GPM_Menu(FK_App,No,ParentNo,Name,MenuType,UrlExt,IsEnable) VALUES ('ZhengZhou','2000','0','���ϲ���',1,'',1);
 
-- ZhengZhou ���̹���;
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD01', 'ҵ������', '2000',1, 3, 'ZhengZhou', '', 0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0101', '����', 'HD01', 1, 4, 'ZhengZhou', '/WF/Start.htm',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0102', '����', 'HD01', 2, 4, 'ZhengZhou', '/WF/Todolist.htm',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0103', '�Ѱ�', 'HD01', 3, 4, 'ZhengZhou', '/WF/Runing.htm',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0104', '��ѯ', 'HD01', 3, 4, 'ZhengZhou', '/WF/Search.htm',  0,'',1);
  
  -- ZhengZhou ���̹���;
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD02', '��������', '2000',1, 3, 'ZhengZhou', '', 0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0201', '������', 'HD02', 1, 4, 'ZhengZhou', '/WF/Comm/Search.htm?EnsName=BP.App.PICC.BaoAns',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0202', '������', 'HD02', 2, 4, 'ZhengZhou', '/WF/Comm/Search.htm?EnsName=BP.App.ND1Rpts',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0203', '�Ѱ��', 'HD02', 3, 4, 'ZhengZhou', '/WF/Comm/Search.htm',  0,'',1);
  
 
-- ZhengZhou ϵͳ����;
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD99', '��֯�ṹ����', '2000',1, 3, 'ZhengZhou', '', 0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9900', '������', 'HD99', 2, 4, 'ZhengZhou', '/WF/Comm/Tree.htm?EnsName=BP.GPM.Depts',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9901', '����̨��', 'HD99', 2, 4, 'ZhengZhou', '/WF/Comm/Search.htm?EnsName=BP.GPM.Depts',  0,'',1);

INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9902', '��Ա̨��', 'HD99', 2, 4, 'ZhengZhou', '/WF/Comm/Search.htm?EnsName=BP.GPM.Emps',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9905', '��֯�ṹ', 'HD99', 2, 4, 'ZhengZhou', '/GPM/Organization.htm',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9906', '��λ����', 'HD99', 1, 4, 'ZhengZhou', '/WF/Comm/Search.htm?EnsName=BP.Port.StationTypes',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9907', '��λ', 'HD99', 1, 4, 'ZhengZhou', '/WF/Comm/Search.htm?EnsName=BP.GPM.Stations',  0,'',1);

-- ZhengZhou ϵͳ/�˵�/Ȩ��;
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD98', 'ϵͳ/�˵�/Ȩ��', '2000',1, 3, 'ZhengZhou', '', 0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9803', 'Ȩ����˵�', 'HD98', 1, 4, 'ZhengZhou', '/WF/Comm/Search.htm?EnsName=BP.GPM.Groups',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9806', '�˵�̨��', 'HD98', 1, 4, 'ZhengZhou', '/WF/Comm/Search.htm?EnsName=BP.GPM.Menus',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9807', '�˵���', 'HD98', 1, 4, 'ZhengZhou', '/WF/Comm/Tree.htm?EnsName=BP.GPM.Menus',  0,'',1);

UPDATE GPM_Menu SET IsEnable=1;
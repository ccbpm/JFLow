-- ϵͳ;
DELETE FROM GPM_App WHERE No='GJTLJ' ;
INSERT INTO GPM_App(No,Name,AppModel,UrlExt,MyFileName,MyFilePath,MyFileExt,WebPath,FK_AppSort,RefMenuNo) VALUES ('GJTLJ','������·�ֹ��Ĺ���',0,'http://www.chichengoa.org/App/login/login.ashx','ccoa.png','Path','GIF','/DataUser/BP.GPM.STem/CCOA.png','01','2002');

-- ɾ�����еĲ˵�.
DELETE FROM GPM_Menu WHERE FK_App='GJTLJ';

-- root;
INSERT INTO GPM_Menu(FK_App,No,ParentNo,Name,MenuType,UrlExt,IsEnable) VALUES ('GJTLJ','2000','0','������·�ֹ��Ĺ���',1,'',1);
 
-- GJTLJ ���̹���;
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD01', '�ʲ�ҵ�����̹���', '2000',1, 3, 'GJTLJ', '', 0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0101', '����', 'HD01', 1, 4, 'GJTLJ', '/WF/Start.htm',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0102', '����', 'HD01', 2, 4, 'GJTLJ', '/WF/Todolist.htm',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0103', '�Ѱ�', 'HD01', 3, 4, 'GJTLJ', '/WF/Runing.htm',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD0104', '��ѯ', 'HD01', 3, 4, 'GJTLJ', '/WF/Search.htm',  0,'',1);
  
 
-- GJTLJ ϵͳ����;
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD99', '��֯�ṹ����', '2000',1, 3, 'GJTLJ', '', 0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9900', '������', 'HD99', 2, 4, 'GJTLJ', '/WF/Comm/Tree.htm?EnsName=BP.GPM.Depts',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9901', '����̨��', 'HD99', 2, 4, 'GJTLJ', '/WF/Comm/Search.htm?EnsName=BP.GPM.Depts',  0,'',1);

INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9902', '��Ա̨��', 'HD99', 2, 4, 'GJTLJ', '/WF/Comm/Search.htm?EnsName=BP.GPM.Emps',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9905', '��֯�ṹ', 'HD99', 2, 4, 'GJTLJ', '/GPM/Organization.htm',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9906', '��λ����', 'HD99', 1, 4, 'GJTLJ', '/WF/Comm/Search.htm?EnsName=BP.Port.StationTypes',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9907', '��λ', 'HD99', 1, 4, 'GJTLJ', '/WF/Comm/Search.htm?EnsName=BP.GPM.Stations',  0,'',1);

-- GJTLJ ϵͳ/�˵�/Ȩ��;
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD98', 'ϵͳ/�˵�/Ȩ��', '2000',1, 3, 'GJTLJ', '', 0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9803', 'Ȩ����˵�', 'HD98', 1, 4, 'GJTLJ', '/WF/Comm/Search.htm?EnsName=BP.GPM.Groups',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9806', '�˵�̨��', 'HD98', 1, 4, 'GJTLJ', '/WF/Comm/Search.htm?EnsName=BP.GPM.Menus',  0,'',1);
INSERT INTO GPM_Menu (No, Name, ParentNo, Idx, MenuType, FK_App, UrlExt, OpenWay,Icon,MenuCtrlWay) VALUES ('HD9807', '�˵���', 'HD98', 1, 4, 'GJTLJ', '/WF/Comm/Tree.htm?EnsName=BP.GPM.Menus',  0,'',1);

UPDATE GPM_Menu SET IsEnable=1;
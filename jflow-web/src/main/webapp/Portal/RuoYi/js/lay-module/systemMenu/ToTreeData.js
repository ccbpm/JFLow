layui.define(['jquery'], function (exports) {
    "use strict";
    var systemNodes,
        moduleNode,
        menuNode,
        $ = layui.jquery;
    var nonSystemItems = ['Forms', 'Frms', 'Flows', 'flows', 'frms', 'forms']
    class MenuConvertTools {
        constructor(webUser, resdata) {
            this.webUser = webUser;
            this.resdata = resdata;
            this.convertToTreeData()
            
        }
        getSystemMenus(){
            systemNodes = this.resdata['System'];
            moduleNode = this.resdata['Module'];
            menuNode = this.resdata['Menu'];
            var adminMenuNodes = [];
            //循环系统.
            for (var idx = 0; idx < systemNodes.length; idx++) {
                var systemNode = systemNodes[idx];
                if (nonSystemItems.indexOf(systemNode.No) > -1) continue;
                systemNode.children = [];
                systemNode.open = false;
                if (systemNode.No === "System") {
                    systemNode.Icon = 'icon-settings';
                    systemNode.Name = "系统管理";
                }

                if (systemNode.Icon === '')
                    systemNode.Icon = 'icon-settings';

                //循环模块.
                for (var idxModule = 0; idxModule < moduleNode.length; idxModule++) {

                    var moduleEn = moduleNode[idxModule];
                    if (moduleEn.SystemNo !== systemNode.No)
                        continue; //如果不是本系统的.
                    moduleEn.children = [];
                    if (moduleEn.Icon === "" || moduleEn.Icon == null || moduleEn.Icon === "")
                        moduleEn.Icon = 'icon-list';
                    moduleEn.open = false;


                    //增加菜单.
                    for (var idxMenu = 0; idxMenu < menuNode.length; idxMenu++) {

                        var menu = menuNode[idxMenu];
                        if (moduleEn.No !== menu.ModuleNo)
                            continue; // 不是本模块的。
                        if (menu.MenuModel == "FlowEntityBatchStart")
                            continue;

                        menu = this.DealMenuUrl(menu);

                        if (menu.Url.indexOf('@WebUser.FK_Dept') > 0)
                            menu.Url = menu.Url.replace('@WebUser.FK_Dept', this.webUser.FK_Dept);

                        if (menu.Url.indexOf('@WebUser.No') > 0)
                            menu.Url = menu.Url.replace('@WebUser.No', this.webUser.No);

                        if (menu.Url.indexOf('@WebUser.OrgNo') > 0)
                            menu.Url = menu.Url.replace('@WebUser.OrgNo', this.webUser.OrgNo);

                        if (menu.Icon === '')
                            menu.Icon = 'icon-user';

                        moduleEn.children.push(menu);
                    }
                    systemNode.children.push(moduleEn);

                }
                adminMenuNodes.push(systemNode)
            }
            return adminMenuNodes
        }
        convertToTreeData(){
            var topNodes = [];
            //if (this.webUser.No === "admin" || parseInt(this.webUser.IsAdmin) === 1) {
                // if (this.getFlowMenu(this.data).length !== 0)
                //     topNodes.push(this.getFlowMenu(this.data));
                //  if (this.getFormMenu(this.data).length !== 0)
                //     topNodes.push(this.getFormMenu(this.data));
           // }
            topNodes = topNodes.concat(this.getSystemMenus(this.resdata))
            // console.log(topNodes)
            return topNodes
        }
        DealMenuUrl(menu) {

        if (menu.UrlExt == undefined) menu.UrlExt = menu.Url;

        var basePath = "";
        if (menu.MenuModel === "" || menu.MenuModel === null) {
            //alert("没有保存菜单标记 MenuModel  " + menu.MenuModel);
            return menu;
        }

        if (menu.MenuModel === "SelfUrl") {
            menu.Url = basePath + menu.UrlExt;
            return menu;
        }

        //如果是修改基础数据..
        if (menu.MenuModel === "FlowBaseData" || menu.MenuModel === "FlowEtc") {

            if (menu.Mark == "Todolist")
                menu.Url = basePath + "/WF/Todolist.htm?FK_Flow=" + menu.Tag1;

            if (menu.Mark == "Runing")
                menu.Url = basePath + "/WF/Runing.htm?FK_Flow=" + menu.Tag1;

            if (menu.Mark == "Start") {
                menu.Url = basePath + "/WF/MyFlow.htm?FK_Flow=" + menu.Tag1;
                menu.Icon = "icon-planct";
            }

            if (menu.Mark == "FlowSearch")
                menu.Url = basePath + "/WF/Search.htm?FK_Flow=" + menu.Tag1;

            if (menu.Mark == "FlowGroup")
                menu.Url = basePath + "/WF/Group.htm?FK_Flow=" + menu.Tag1;

            if (menu.Icon == null || menu.Icon == "") menu.Icon = "icon-paper-plane";

            return menu;
        }

        //新建实体.
        if (menu.MenuModel == "FlowNewEntity") {
            if (menu.Mark == "StartFlow")
                menu.Url = basePath + "/WF/CCBill/Opt/StartFlowByNewEntity.htm?FK_Flow=" + menu.Tag1 + "&MenuNo=" + menu.No;

            // alert(menu.Icon);
            if (menu.Icon === "" || menu.Icon == null) menu.Icon = "icon-paper-plane";

            return menu;
        }

        if (menu.MenuModel == "FlowSearch") {
            menu.Url = basePath + "/WF/Search.htm?FK_Flow=" + menu.Tag;
            if (menu.Icon === "" || menu.Icon == null) menu.Icon = "icon-paper-plane";
            return menu;
        }

        if (menu.MenuModel === "Dict") {
            if (menu.ListModel === 0) //如果是批量编辑模式.
                menu.Url = basePath + "/WF/CCBill/SearchEditer.htm?FrmID=" + menu.UrlExt;
            else
                menu.Url = basePath + "/WF/CCBill/SearchDict.htm?FrmID=" + menu.UrlExt;
            return menu;
        }

        if (menu.MenuModel === "DBList") {
            menu.Url = basePath + "/WF/CCBill/SearchDBList.htm?FrmID=" + menu.UrlExt;
            return menu;
        }

        //流程菜单.
        if (menu.MenuModel == "FlowUrl") {
            // menu.Url = basePath + "/WF/" + menu.Url;
            menu.Url = basePath + "/WF/" + menu.UrlExt;
            return menu;
        }

        if (menu.MenuModel == "DictTable") {

            url = basePath + "/WF/Admin/FoolFormDesigner/SFTableEditData.htm?FK_SFTable=" + menu.UrlExt + "&QueryType=Dict";
            menu.Url = url;
            return menu;
        }

        if (menu.MenuModel === "Bill") {
            menu.Url = basePath + "/WF/CCBill/SearchBill.htm?FrmID=" + menu.UrlExt;
            return menu;
        }

        //独立功能.
        if (menu.MenuModel === "Func" || menu.MenuModel === "StandAloneFunc") {
            menu.Url = basePath + "/WF/CCBill/Sys/Func.htm?FuncNo=" + menu.UrlExt;
            return menu;
        }

        if (menu.MenuModel === "Windows") {
            menu.Url = basePath + "/WF/Portal/Home.htm?PageID=" + menu.No;
            return menu;
        }

        if (menu.MenuModel === "Tabs") {
            menu.Url = basePath + "/WF/Portal/Tabs.htm?PageID=" + menu.No;
            return menu;
        }

        if (menu.MenuModel === "Rpt3D") {
            menu.Url = basePath + "/CCFast/Rpt/Rpt3D.htm?RptNo=" + menu.No;
            return menu;
        }

        if (menu.Url != "") {
            if (menu.Url.indexOf("?") != -1)
                menu.Url = basePath + menu.Url + "&PageID=" + menu.No;
            else
                menu.Url = basePath + menu.Url + "?PageID=" + menu.No;
            return menu;
        }

        alert('没有判断的模式:' + menu.MenuModel + "  urlExt:" + menu.UrlExt);
        return menu;
    }
    };

    exports('MenuConvertTools', MenuConvertTools)

})
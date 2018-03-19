
    var d = new dTree("d");
    d.add(0, -1 , "信息");
	
			d.add(100, 0,"处理");
			
				d.add(101, 100, "代办()","");
				
				d.add(102, 100, "在途()","");
			
			
			
			/*d.add(300, 0, "Daily Performance Summary","#");
			
			d.add(400, 0, "CRM Monthly Summary","#");
			
			d.add(500, 0, "Ad-hoc rate Analysis report","#");
						
			d.add(600,0,"Weekly Performance Review","#");
			
			d.add(700,0,"Freighter Performance Review","#");	
			
			d.add(800,0,"Year to Year Cargo Performance Review ","#");*/
			
								
			
					
					
			
			
					
					
			
								
			
			
					

    document.write(d);
    d.openAll();

// 重写单击事件的触发函数
// 该函数仅仅用于demo, 实际开发时应该删除, 因为ajax调用不需要在页面上显示出来
dTree.TreeNode.onclick = function(sUrl)
{
	// showWaitDialog();

	if(window.top != window && window.top["mainFrame"] != null)
	{
		window.top["mainFrame"].location.href = sUrl;
	}

	return false;
}
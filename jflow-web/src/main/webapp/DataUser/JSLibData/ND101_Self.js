// 处理一些控件是否可用.
function IsCheck(cb) {
	var temp = document.getElementsByName("RB_xitongxuanze");
	var myFavorite;
	for (var i = 0; i < temp.length; i++) {
		myFavorite = temp[i].value;
		if(temp[i].checked){
				
			if (myFavorite == '0') {
				document.getElementById("DDL_sf_functionsystem").disabled = 'true';
				document.getElementById("DDL_sf_fsgroup").disabled = 'true';
				document.getElementById("DDL_sf_part").disabled = 'true';
				document.getElementById("DDL_sf_fault").disabled = 'true';
				
			
				document.getElementById("TB_spns").readOnly=false;
				document.getElementById("TB_fmis").readOnly=false;
				document.getElementById("DDL_sf_dkfult").disabled = false;
				
				document.getElementById("DDL_sf_functionsystem").value = "";
				document.getElementById("DDL_sf_fsgroup").value = "";
				document.getElementById("DDL_sf_part").value = "";
				document.getElementById("DDL_sf_fault").value = "";
				document.getElementById("TB_guzhangdaima").value = "";
				document.getElementById("TB_guzhangshuyu").value = "";
				
				
			} else if (myFavorite == '1') {
				document.getElementById("TB_spns").readOnly='true';
				document.getElementById("TB_fmis").readOnly='true';
				document.getElementById("DDL_sf_dkfult").disabled = 'true';
				
				document.getElementById("DDL_sf_functionsystem").disabled = false;
				document.getElementById("DDL_sf_fsgroup").disabled = false;
				document.getElementById("DDL_sf_part").disabled = false;
				document.getElementById("DDL_sf_fault").disabled = false;
				
				document.getElementById("TB_spns").value="";
				document.getElementById("TB_fmis").value="";
				document.getElementById("DDL_sf_dkfult").value = "";
				
				document.getElementById("TB_guzhangdaima").value = "";
				document.getElementById("TB_guzhangshuyu").value = "";
				
				
			} else {
				document.getElementById("DDL_sf_functionsystem").disabled = true;
				document.getElementById("DDL_sf_fsgroup").disabled = true;
				document.getElementById("DDL_sf_part").disabled = true;
				document.getElementById("DDL_sf_fault").disabled = true;
				document.getElementById("TB_spns").readOnly=true;
				document.getElementById("TB_fmis").readOnly=true;
				document.getElementById("DDL_sf_dkfult").disabled = true;
				
				document.getElementById("DDL_sf_functionsystem").value = "";
				document.getElementById("DDL_sf_fsgroup").value = "";
				document.getElementById("DDL_sf_part").value = "";
				document.getElementById("DDL_sf_fault").value = "";
				
				document.getElementById("TB_guzhangdaima").value = "";
				document.getElementById("TB_guzhangshuyu").value = "";
				
				document.getElementById("TB_spns").value='';
				document.getElementById("TB_fmis").value='';
				document.getElementById("DDL_sf_dkfult").value ='';
			}
		}
	}
}

function IsSubmit() {
	var gzdm = document.getElementById("TB_guzhangdaima").value;
	var gzsy = document.getElementById("TB_guzhangshuyu").value;
	var temp = document.getElementsByName("RB_xitongxuanze");
	for (var i = 0; i < temp.length; i++) {
		myFavorite = temp[i].value;
		if(temp[i].checked){
			if(myFavorite == '2'){
					return true; 
			}else {
				if(null== gzdm || null == gzsy ||""==gzdm || ""==gzsy){
					alert("故障代码不能为空;\n故障术语不能为空;");
					return false;
				}else{
					return true;
				}
			}
		}
	
	}
	
}

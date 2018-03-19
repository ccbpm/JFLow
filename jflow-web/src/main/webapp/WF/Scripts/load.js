var t_id = setInterval(animate,20); 
var pos=0; 
var dir=2; 
var len=0; 
function animate() { 
    var elem = document.getElementById('progress'); 
    if(elem != null) { 
     if (pos==0) len += dir; 
     if (len>32 || pos>79) pos += dir; 
     if (pos>79) len -= dir; 
     if (pos>79 && len==0) pos=0; 
     elem.style.left = pos; 
     elem.style.width = len; 
    } 
} 
function remove_loading() { 
    this.clearInterval(t_id); 
    var targelem = document.getElementById('loader_container'); 
    targelem.style.display='none'; 
    targelem.style.visibility='hidden'; 
} 
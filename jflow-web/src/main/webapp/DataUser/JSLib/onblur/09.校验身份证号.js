function isCodeNo(s) {
    var patrn = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    if (!patrn.exec(s.value)) 
    {
       alert('身份证格式不正确.');
       s.value=null;
       return false;
    }
    return true
}

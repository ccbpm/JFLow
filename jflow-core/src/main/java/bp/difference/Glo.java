package bp.difference;

public class Glo {
    public static  String getGetIP() throws Exception {
        return bp.sys.base.Glo.getRequest().getLocalAddr();
    }
}

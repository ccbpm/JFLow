package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.difference.*;

/**
 * 类型
 */
public class UserLogType extends EntityNoName {
    ///#region 构造方法

    /**
     * 类型
     */
    public UserLogType() {
    }

    /**
     * 类型
     *
     * @param _No
     */
    public UserLogType(String _No) {
        super(_No);
    }

    @Override
    public UAC getHisUAC() {
        UAC uac = new UAC();
        uac.Readonly(); 
        return uac;
    }
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#endregion

    /**
     * 类型
     */
    @Override
    public Map getEnMap() {
        if (this.get_enMap() != null) {
            return this.get_enMap();
        }

        Map map = new Map("Sys_UserLogType", "类型");

        map.AddTBStringPK("No", null, "编号", true, true, 1, 100, 20);
        map.AddTBString("Name", null, "名称", true, false, 0, 100, 300);

        this.set_enMap(map);
        return this.get_enMap();
    }
}
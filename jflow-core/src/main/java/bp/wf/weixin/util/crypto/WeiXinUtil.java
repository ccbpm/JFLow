package bp.wf.weixin.util.crypto;

import bp.difference.SystemConfig;

public class WeiXinUtil {

	public static String  ResponseMsg(String touser, String toparty, String totag, String msgtype, String msg)
    {
        StringBuilder sbStr = new StringBuilder();
        String msgTypeStr = "";
        switch (msgtype)
        {
            case "text":
                msgTypeStr = " \"text\": { \"content\":\"" + msg + "\"  },";
                break;
            case "image":
                msgTypeStr = " \"image\": { \"media_id\":\"" + msg + "\"  },";
                break;
            case "voice":
                msgTypeStr = " \"voice\": { \"media_id\":\"" + msg + "\"  },";
                break;
            case "video":
                msgTypeStr = " \"video\": { \"media_id\":\"" + msg + "\",\"\":'标题',\"description\":'描述'  },";
                break;
            default:
                msgTypeStr = " \"text\": { \"content\":'数据类型错误！'  },";
                break;
        }
        sbStr.append("{");
        sbStr.append("\"touser\":\"" + touser + "\",");
        sbStr.append("\"toparty\":\"" + toparty + "\",");
        sbStr.append("\"totag\":\"" + totag + "\",");
        sbStr.append("\"msgtype\":\"" + msgtype + "\",");
        sbStr.append("\"agentid\":\"" + SystemConfig.getWX_AgentID() + "\",");
        sbStr.append(msgTypeStr);
        sbStr.append("\"safe\":\"0\"");
        sbStr.append("}");
        return sbStr.toString();
    }
}

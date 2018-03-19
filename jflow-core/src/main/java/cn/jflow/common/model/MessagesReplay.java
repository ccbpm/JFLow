package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Port.Emp;
import BP.Tools.StringHelper;
import BP.WF.SMS;
import cn.jflow.model.designer.UCEnModel;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.TB;
import cn.jflow.system.ui.core.TextBoxMode;

public class MessagesReplay extends UCEnModel{

	public StringBuffer pub1=new StringBuffer();
	
	public MessagesReplay(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}
	
	public void pageLoad(HttpServletRequest request,
			HttpServletResponse response)
    {
		String receiver = request.getParameter("RE");
	    String mypk = request.getParameter("MyPK");

        BP.WF.SMS sms = null;
       if (!StringHelper.isNullOrEmpty(mypk))
       {
           sms = new SMS();
           sms.setMyPK(mypk);
           sms.RetrieveFromDBSources();
       }

        this.pub1.append(AddTable()); // (" id='recTable' class='Table' cellpadding='0' cellspacing='0' border='0' style='width:100%;margin-left:auto;margin-right:auto;' ");
        this.pub1.append(AddCaptionMsg("消息回复")); //("<caption><div class='CaptionMsg'>消息</div></caption>");
        this.pub1.append(AddTR());
        this.pub1.append(AddTD("接受人："));

        TB tb = new TB();
        tb.setId("rec");
        tb.setWidth(430);
        if (!StringHelper.isNullOrEmpty(receiver) && !StringHelper.isNullOrEmpty(mypk))
        {
            Emp emp = new Emp(receiver);
            tb.setText(emp.getName());
            tb.setReadOnly(true);
            this.pub1.append(AddTD(tb));
        }
        else
        {
            this.pub1.append(AddTDBegin());
            this.pub1.append(Add(tb));

            tb = new TB();
            tb.setId("Hid_FQR");
            //HiddenField hid = new HiddenField();
            //hid.ID = "Hid_FQR";
            //pub1.Add(hid);

            this.pub1.append(Add("<a onclick=\"openSelectEmp('rec','TB_FQR')\" href='javascript:;'>添加人员</a>"));
            this.pub1.append(AddTDEnd());
        }
        this.pub1.append(AddTREnd());


        this.pub1.append(AddTR());
        this.pub1.append(AddTD("标题"));
        tb = new TB();
        tb.setId("title");
        if (sms!= null)
        {
          tb.setText("RE:" + sms.getTitle());
        }
        tb.setWidth(430);
        this.pub1.append(AddTD(tb));
        this.pub1.append(AddTREnd());


        this.pub1.append(AddTR());
        this.pub1.append(AddTD("正文"));
        tb = new TB();
        tb.setId("con");
        tb.setTextMode(TextBoxMode.MultiLine);
        tb.setWidth(430);
        tb.setHeight(120);

        if (sms != null)
          tb.setText("\t\n ------------------ \t\n " + sms.getDocOfEmail());

        this.pub1.append(AddTD(tb));
        this.pub1.append(AddTREnd());


        this.pub1.append(AddTR());
        this.pub1.append(AddTDBegin(" colspan=2 "));
        Button btn = new Button();
        btn.setId("ContentPlaceHolder1_pub1_Btn_Save");
        btn.setText("发送");
        btn.setType("submit");
        
        //btn.Click += new EventHandler(btn_Save_Click);

        Button btnClose = new Button();
        btnClose.setId("ContentPlaceHolder1_pub1_Btn_Close");
        btnClose.setText("取消");
        btnClose.attributes.put("onclick", "onCancel()");
        
        //btnClose.Click += new EventHandler(btnClose_Click);

        this.pub1.append(Add(btn));
        this.pub1.append(Add(btnClose));
        this.pub1.append(AddTDEnd());
        this.pub1.append(AddTREnd());

        this.pub1.append(AddTableEnd());

    }
}


package BP.DA;

import java.io.FileWriter;
import java.math.BigDecimal;
import java.nio.ShortBuffer;

public class SB  
{
	public  String toStr()
	{
	  return _sb.toString();
	}
	private StringBuffer _sb;
	public SB()
	{
		_sb = new StringBuffer();
	}

	  public void AddTable()
      {
		  _sb.append("<Table class='Table' cellpadding='2' cellspacing='2'>");
      }
	  
	  
	  
	  public void AddCaption(String str)
      {
		  _sb.append("<caption >"+str+"</caption>");
      }
	  
	  
	  
      public void AddTableEnd()
      {
           _sb.append("</Table>");
      }
      public void AddTableEndWithHR()
      {
           _sb.append("</Table><HR>");
      }
      public void AddTableEndWithBR()
      {
           _sb.append("</Table><Br>");
      }
      public void AddTable(String attr)
      {
          // _sb.append("<Table id='table_01' "+attr+" >");
           _sb.append("<Table " + attr + " >");
      }
      /// <summary>
      /// 增加一般的table，width=100%
      /// </summary>
      public void AddTableNormal()
      {
           _sb.append("<table class='Table' cellpadding='0' cellspacing='0' border='0' style='width:100%' >");
      }
      public void AddTable(String id, String styleClass)
      {
           _sb.append("<Table class='" + id + "'  cellpadding='0' cellspacing='0' class='" + styleClass + "'>");
      }
     /* public void AddTDNum(TB tb)
      {
           _sb.append("\n<TD class='TDNum' nowrap >");
           _sb.append(tb);
           _sb.append("</TD>");
      }
      public void AddTDNum(TextBox tb)
      {
           _sb.append("\n<TD class='TDNum' nowrap >");
           _sb.append(tb);
           _sb.append("</TD>");
      }*/
      
      public void Add(String str)
      {
           _sb.append(str);
      }
      public void AddTDNum(String str)
      {
           _sb.append("\n<TD class='TDNum' nowrap >" + str + "</TD>");
      }
      public void AddTDNum(BigDecimal str)
      {
           _sb.append("\n<TD class='TDNum' nowrap >" + str + "</TD>");
      }
      public void AddTDJE(BigDecimal str)
      {
           _sb.append("\n<TD class='TDNum' nowrap >" + str + "</TD>");
      }
      public void AddTDDate(String str)
      {
          if (str == null || "".equals(str))
               _sb.append("\n<TD  nowrap class='TBDate' >&nbsp;</TD>");
          else
               _sb.append("\n<TD  nowrap class='TBDate' >" + str + "</TD>");
      }
      public void AddTD(String str)
      {
          if (str == null || "".equals(str))
               _sb.append("\n<TD  nowrap >&nbsp;</TD>");
          else
               _sb.append("\n<TD  nowrap >" + str + "</TD>");
      }
      public void AddTD(String style, String str)
      {
          if (str == null || "".equals(str))
               _sb.append("\n<TD  "+style+" >&nbsp;</TD>");
          else
               _sb.append("\n<TD  "+style+" >" + str + "</TD>");
      }
      public void AddTDA(String href, String str)
      {
           _sb.append("\n<TD  nowrap ><a href=\"" + href + "\">" + str + "</a></TD>");
      }
      public void AddTDA(String href, String str, String target)
      {
           _sb.append("\n<TD  nowrap ><a href=\"" + href + "\" target=" + target + ">" + str + "</a></TD>");
      }

      public void Href(String href, String str, String target)
      {
           _sb.append("<a href=\"" + href + "\" target=" + target + ">" + str + "</A>");
      }
      public void Href(String href, String str, String target, int blank)
      {
           _sb.append("<a href=\"" + href + "\" target=" + target + ">" + str + "</A>" + BP.DA.DataType.GenerSpace(blank));
      }
      public void Href(String href, String str)
      {
           _sb.append("<a href=\"" + href + "\" >" + str + "</A>");
      }

      public void Href(String href, String str, int blank)
      {
           _sb.append("<a href=\"" + href + "\">" + str + "</A>" + BP.DA.DataType.GenerSpace(blank));
      }

      public void AddTDM(String str)
      {
           _sb.append("\n<TD class='TDM' nowrap >" + str + "</TD>");
      }
      public void AddTDMS(String str)
      {
           _sb.append("\n<TD class='TDMS' nowrap >" + str + "</TD>");
      }
      
      public void AddTDTitle(String str)
      {
           _sb.append("\n<th  >" + str + "</th>");
      }
      

      public void AddTDA(String href, int str)
      {
           _sb.append("\n<TD class='TDNum' nowrap ><a href=\"" + href + "\">" + str + "</TD>");
      }
      public void AddTD(Boolean val)
      {
          if (val)
               _sb.append("\n<TD >是</TD>");
          else
               _sb.append("\n<TD >否</TD>");
      }
      public void AddTDBegin(String attr)
      {
           _sb.append("\n<TD " + attr + " nowrap >");
      }
      public void AddTDBegin()
      {
           _sb.append("\n<TD valign=top nowrap >");
      }
      public void AddTDEnd()
      {
           _sb.append("\n</TD>");
      }

      public void AddTDInfoBegin()
      {
           _sb.append("\n<TD  nowrap bgcolor=InfoBackground >");
      }
      public void AddTDInfo(String str)
      {
           _sb.append("\n<TD  nowrap bgcolor=InfoBackground >" + str + "</TD>");
      }
      public void AddTDInfo()
      {
           _sb.append("\n<TD  nowrap bgcolor=InfoBackground >&nbsp;</TD>");
      }
     
      
      public void AddTDCenter(String str)
      {
           _sb.append("\n<TD align=center nowrap >" + str + "</TD>");
      }
      public void AddTD()
      {
           _sb.append("\n<TD >&nbsp;</TD>");
      }
      public void AddTDToolbar(String str)
      {
           _sb.append("\n<TD class='Toolbar' nowrap >" + str + "</TD>");
      }
      public void AddTR()
      {
           _sb.append("\n<TR>");
      }
      public void AddTRHand()
      {
           _sb.append("\n<TR class='TRHand' >");
      }
      public void AddTRHand(String attr)
      {
           _sb.append("\n<TR class='TRHand' " + attr + " >");
      }
      public void AddTRTXHand()
      {
           _sb.append("\n<TR class='TRHand' onmouseover='TROver(this)' onmouseout='TROut(this)' >");
      }
      public void AddTRTXHand(String attr)
      {
           _sb.append("\n<TR class='TRHand' onmouseover='TROver(this)' onmouseout='TROut(this)' " + attr + " >");
      }
      public void AddTR(String attr)
      {
           _sb.append("\n<TR " + attr + " >");
      }
      public void AddTRSum()
      {
           _sb.append("\n<TR class='TRSum' >");
      }
      public void AddTRRed()
      {
           _sb.append("\n<TR class='TRRed' >");
      }
      public void AddTR1()
      {
           _sb.append("\n<TR class=TR1 >");
      }

      public Boolean AddTR(Boolean item, String attr)
      {
          if (item)
               _sb.append("\n<TR bgcolor=AliceBlue " + attr + " >");
          else
               _sb.append("\n<TR bgcolor=white " + attr + " class=TR>");

          item = !item;
          return item;
      }
      
      public Boolean AddTR(Boolean item)
      {
          if (item)
               _sb.append("\n<TR bgcolor=AliceBlue >");
          else
               _sb.append("\n<TR bgcolor=white >");

          item = !item;
          return item;
      }
      public void AddTRTXRed()
      {
           _sb.append("\n<TR  bgcolor=red >");
      }
      /// <summary>
      /// 加上特殊效果
      /// </summary>
      public void AddTRTX()
      {
           _sb.append("\n<TR onmouseover='TROver(this)' onmouseout='TROut(this)' >");
      }
      public void AddTRTX(String attr)
      {
           _sb.append("\n<TR onmouseover='TROver(this)' onmouseout='TROut(this)' " + attr + ">");
      }
      public void AddTREnd()
      {
           _sb.append("\n</TR>");
      }
      public void AddTDIdx(int idx)
      {
           _sb.append("\n<TD class='Idx' nowrap>" + idx + "</TD>");
      }
      
      
      public void AddTDIdx(String idx)
      {
           _sb.append("\n<TH class='Idx' nowrap >" + idx + "</TH>");
      }
      public void AddTDH(String url, String lab)
      {
           _sb.append("\n<TD  nowrap ><a href='" + url + "'>" + lab + "</a></TD>");
      }
      public void AddTDH(String url, String lab, String target)
      {
           _sb.append("\n<TD  nowrap ><a href='" + url + "' target=" + target + ">" + lab + "</a></TD>");
      }
      public void AddTDH(String url, String lab, String target, String img)
      {
           _sb.append("\n<TD  nowrap ><a href='" + url + "' target=" + target + "><img src='" + img + "' border=0/>" + lab + "</a></TD>");
      }

	
 
 
}
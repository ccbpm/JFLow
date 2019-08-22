package BP.Sys.FrmUI;

import BP.DA.DataType;
import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.UAC;
import BP.Sys.FrmImgAttr;
import BP.Sys.MapAttrAttr;

public class ExtImg extends EntityMyPK
{
		/** 
		 控制权限
		 
		*/
		@Override
		public UAC getHisUAC()
		{
			UAC uac = new UAC();
			uac.IsInsert = false;
			uac.IsUpdate = true;
			uac.IsDelete = true;
			return uac;
		}
		/** 
		 装饰图片
		 
		*/
		public ExtImg()
		{
		}
		/** 
		 装饰图片
		 
		 @param mypk
		 * @throws Exception 
		*/
		public ExtImg(String mypk) throws Exception
		{
			this.setMyPK(mypk);
			this.Retrieve();
		}
		
		/**
		 * 图片对应的字段
		 * @return
		 */
		public String getKeyOfEn(){
			return this.GetValStringByKey(MapAttrAttr.KeyOfEn);
		}
		public void setKeyOfEN(String val){
			this.SetValByKey(MapAttrAttr.KeyOfEn, val);
		}
		/** 
		 EnMap
		 
		*/
		@Override
		public Map getEnMap()
		{
			if (this.get_enMap() != null)
			{
				return this.get_enMap();
			}

			Map map = new Map("Sys_FrmImg", "装饰图片");
			map.Java_SetDepositaryOfEntity(Depositary.None);
			map.Java_SetDepositaryOfMap(Depositary.Application);
			map.Java_SetEnType(EnType.Sys);

			map.AddMyPK();
			map.AddTBString(MapAttrAttr.FK_MapData, null, "表单ID", true, true, 0, 200, 20);
			map.AddTBString(MapAttrAttr.KeyOfEn, null, "对应字段", true, true, 0, 200, 20);
			map.AddTBString(FrmImgAttr.Name, null, "中文名称", true, true, 0, 500, 20);
			map.AddDDLSysEnum(FrmImgAttr.ImgSrcType, 0, "图片来源", true, true, FrmImgAttr.ImgSrcType, "@0=本地@1=URL");

			map.AddTBString(FrmImgAttr.ImgPath, null, "图片路径", true, false, 0, 200, 20, true);

			String strs = "本机的图片路径:";
			strs += " \n 1.您可以使用 ＠baseBase 或者＠+字段英文名作为变量来标识文件路径.";
			strs += " \n 2.注意＠需要半角字符.";
			strs += " \n 3.例如:＠basePath/DataUser/UserIcon/＠QingJiaRenID.png";

			map.SetHelperAlert(FrmImgAttr.ImgPath, strs);

			map.AddTBString(FrmImgAttr.ImgURL, null, "图片URL", true, false, 0, 200, 20, true);

			map.AddTBString(FrmImgAttr.LinkURL, null, "连接到URL", true, false, 0, 200, 20, true);
			map.AddTBString(FrmImgAttr.LinkTarget, "_blank", "连接目标", true, false, 0, 200, 20);

			map.AddTBInt(FrmImgAttr.ImgAppType, 0, "应用类型", false, false);

				//显示的分组.
			map.AddDDLSQL(MapAttrAttr.GroupID, 0, "显示的分组", MapAttrString.SQLOfGroupAttr(), true);
			map.AddTBInt(MapAttrAttr.ColSpan, 0, "单元格数量", false, true);

				//跨单元格
			map.AddDDLSysEnum(MapAttrAttr.TextColSpan, 1, "文本单元格数量", true, true, "ColSpanAttrString", "@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格");
				//跨行
			map.AddDDLSysEnum(MapAttrAttr.RowSpan, 1, "行数", true, true, "RowSpanAttrString", "@1=跨1个行@2=跨2行@3=跨3行");

			map.AddTBInt(MapAttrAttr.UIWidth, 0, "宽度", true, false);
			map.AddTBInt(MapAttrAttr.UIHeight, 0, "高度", true, false);

			this.set_enMap(map);
			return this.get_enMap();
		}

		@Override
		protected void afterInsertUpdateAction() throws Exception
		{
			BP.Sys.FrmImg imgAth = new BP.Sys.FrmImg();
			imgAth.setMyPK(this.getMyPK());
			imgAth.RetrieveFromDBSources();
			imgAth.Update();
			
			//同步更新MapAttr 
			if(DataType.IsNullOrEmpty(this.getKeyOfEn())==false){
				MapAttrString attr = new MapAttrString(this.getMyPK());
				attr.SetValByKey(MapAttrAttr.ColSpan, this.GetValStrByKey(MapAttrAttr.ColSpan));
				attr.SetValByKey(MapAttrAttr.TextColSpan, this.GetValStrByKey(MapAttrAttr.TextColSpan));
				attr.SetValByKey(MapAttrAttr.RowSpan, this.GetValStrByKey(MapAttrAttr.RowSpan));
	
				attr.SetValByKey(MapAttrAttr.Name, this.GetValStrByKey(FrmImgAttr.Name)); //名称.
	
				attr.SetValByKey(MapAttrAttr.X, this.GetValStrByKey(FrmImgAttr.X)); //名称.
				attr.SetValByKey(MapAttrAttr.Y, this.GetValStrByKey(FrmImgAttr.Y)); //名称.
				attr.Update();
			}

			super.afterInsertUpdateAction();
		}
		@Override
		protected void afterDelete() throws Exception
		{
			//把相关的字段也要删除.
			MapAttrString attr = new MapAttrString();
			attr.setMyPK( this.getMyPK());
			attr.Delete();

			super.afterDelete();
		}

	}

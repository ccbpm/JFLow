package BP.En;

import BP.DA.*;

/** 
 EntityMyPKMyFile
*/
public abstract class EntityMyPKMyFile extends EntityMyPK
{

		///#region 属性
	@Override
	public String getPK()
	{
		return "MyPK";
	}
	public final String getMyFilePath() throws Exception
	{
		return this.GetValStringByKey("MyFilePath");
	}
	public final void setMyFilePath(String value) throws Exception
	{
		this.SetValByKey("MyFilePath", value);
	}
	public final String getMyFileExt() throws Exception
	{
		return this.GetValStringByKey("MyFileExt");
	}
	public final void setMyFileExt(String value) throws Exception
	{
		this.SetValByKey("MyFileExt", value.replace(".",""));
	}
	public final String getMyFileName() throws Exception
	{
		return this.GetValStringByKey("MyFileName");
	}
	public final void setMyFileName(String value) throws Exception
	{
		this.SetValByKey("MyFileName", value);
	}
	public final long getMyFileSize() throws Exception
	{
		return this.GetValInt64ByKey("MyFileSize");
	}
	public final void setMyFileSize(long value) throws Exception
	{
		this.SetValByKey("MyFileSize", value);
	}
	public final int getMyFileH() throws Exception
	{
		return this.GetValIntByKey("MyFileH");
	}
	public final void setMyFileH(int value) throws Exception
	{
		this.SetValByKey("MyFileH", value);
	}
	public final int getMyFileW() throws Exception
	{
		return this.GetValIntByKey("MyFileW");
	}
	public final void setMyFileW(int value) throws Exception
	{
		this.SetValByKey("MyFileW", value);
	}
	public final boolean getIsImg() throws Exception
	{
		return DataType.IsImgExt(this.getMyFileExt());
	}

		///#endregion


		///#region 构造
	public EntityMyPKMyFile()
	{
	}
	/** 
	 class Name 
	 
	 @param _MyPK _MyPK
	 * @throws Exception 
	*/
	protected EntityMyPKMyFile(String _MyPK) throws Exception
	{
		this.setMyPK(_MyPK);
		this.Retrieve();
	}

		///#endregion
}
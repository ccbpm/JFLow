package bp.wf.rpt;

/** 
 装载水印图片的相关信息
*/
public class WaterImage
{
	/** 
	 水文
	*/
	public WaterImage()throws Exception
	{
	}

	private String m_sourcePicture;
	/** 
	 源图片地址名字(带后缀)
	*/

	public final String getSourcePicture()throws Exception
	{
		return m_sourcePicture;
	}
	public final void setSourcePicture(String value) throws Exception
	{
		m_sourcePicture = value;
	}

	private String m_waterImager;
	/** 
	 水印图片名字(带后缀)
	*/
	public final String getWaterPicture()throws Exception
	{
		return m_waterImager;
	}
	public final void setWaterPicture(String value) throws Exception
	{
		m_waterImager = value;
	}

	private float m_alpha;
	/** 
	 水印图片文字的透明度
	*/
	public final float getAlpha()throws Exception
	{
		return m_alpha;
	}
	public final void setAlpha(float value)throws Exception
	{m_alpha = value;
	}

	private ImagePosition m_postition = ImagePosition.values()[0];
	/** 
	 水印图片或文字在图片中的位置
	*/
	public final ImagePosition getPosition()throws Exception
	{
		return m_postition;
	}
	public final void setPosition(ImagePosition value)throws Exception
	{m_postition = value;
	}

	private String m_words;
	/** 
	 水印文字的内容
	*/
	public final String getWords()throws Exception
	{
		return m_words;
	}
	public final void setWords(String value) throws Exception
	{
		m_words = value;
	}

}
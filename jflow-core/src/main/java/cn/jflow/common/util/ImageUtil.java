package cn.jflow.common.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * <p>Title: ImageUtil </p>
 * <p>Description: </p>
 * <p>Email: icerainsoft@hotmail.com </p> 
 * @author Ares
 * @date 2014年10月28日 上午10:24:26
 */
public class ImageUtil {

    private static String DEFAULT_THUMB_PREVFIX = "";
    private static String DEFAULT_CUT_PREVFIX = "";
    private static Boolean DEFAULT_FORCE = false;
    
    
    /**
     * <p>Title: cutImage</p>
     * <p>Description:  根据原图与裁切size截取局部图片</p>
     * @param srcImg    源图片
     * @param output    图片输出流
     * @param rect        需要截取部分的坐标和大小
     */
    public void cutImage(File srcImg, OutputStream output, java.awt.Rectangle rect){
        if(srcImg.exists()){
            java.io.FileInputStream fis = null;
            ImageInputStream iis = null;
            try {
                fis = new FileInputStream(srcImg);
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames()).replace("]", ",");
                String suffix = null;
                // 获取图片后缀
                if(srcImg.getName().indexOf(".") > -1) {
                    suffix = srcImg.getName().substring(srcImg.getName().lastIndexOf(".") + 1);
                }// 类型和图片后缀全部小写，然后判断后缀是否合法
                if(suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()+",") < 0){
                    System.out.println("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
                    return ;
                }
                // 将FileInputStream 转换为ImageInputStream
                iis = ImageIO.createImageInputStream(fis);
                // 根据图片类型获取该种类型的ImageReader
                ImageReader reader = ImageIO.getImageReadersBySuffix(suffix).next();
                reader.setInput(iis,true);
                ImageReadParam param = reader.getDefaultReadParam();
                param.setSourceRegion(rect);
                BufferedImage bi = reader.read(0, param);
                ImageIO.write(bi, suffix, output);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(fis != null) fis.close();
                    if(iis != null) iis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
        	System.out.println("the src image is not exist.");
        }
    }
    
    public void cutImage(File srcImg, OutputStream output, int x, int y, int width, int height){
        cutImage(srcImg, output, new java.awt.Rectangle(x, y, width, height));
    }
    
    public void cutImage(File srcImg, String destImgPath,String userName, java.awt.Rectangle rect){
        File destImg = new File(destImgPath);
        if(destImg.exists()){
            String p = destImg.getPath();
            try {
                if(!destImg.isDirectory()) p = destImg.getParent();
                if(!p.endsWith(File.separator)) p = p + File.separator;
                cutImage(srcImg, new java.io.FileOutputStream(p + userName+"SmallerCon.png"), rect);
            } catch (FileNotFoundException e) {
            	System.out.println("the dest image is not exist.");
            }
        }else System.out.println("the dest image folder is not exist.");
    }
    
    public void cutImage(File srcImg, String destImg,String userName, int x, int y, int width, int height){
        cutImage(srcImg, destImg,userName, new java.awt.Rectangle(x, y, width, height));
    }
    
    public void cutImage(String srcImg, String destImg,String userName, int x, int y, int width, int height) throws IOException{
    	//讲PNG图片转换为jpg格式
    	RenderedImage img = ImageIO.read(new File(srcImg));   
    	ImageIO.write(img, "jpg", new File(destImg+userName+"SmallerCon.jpg"));
        cutImage(new File(destImg+userName+"SmallerCon.jpg"), destImg,userName, new java.awt.Rectangle(x, y, width, height));
    }
    /**
     * <p>Title: thumbnailImage</p>
     * <p>Description: 根据图片路径生成缩略图 </p>
     * @param imagePath    原图片路径
     * @param w            缩略图宽
     * @param h            缩略图高
     * @param prevfix    生成缩略图的前缀
     * @param force        是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
     */
    public void thumbnailImage(File srcImg, OutputStream output, int w, int h){
        if(srcImg.exists()){
            try {
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames()).replace("]", ",");
                String suffix = null;
                // 获取图片后缀
                if(srcImg.getName().indexOf(".") > -1) {
                    suffix = srcImg.getName().substring(srcImg.getName().lastIndexOf(".") + 1);
                }// 类型和图片后缀全部小写，然后判断后缀是否合法
                if(suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()+",") < 0){
                	System.out.println("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
                    return ;
                }
                System.out.println("target image's size, width:{}, height:{}."+w+","+h);
                Image img = ImageIO.read(srcImg);
                // 根据原图与要求的缩略图比例，找到最合适的缩略图比例
                if(DEFAULT_FORCE){
                    int width = img.getWidth(null);
                    int height = img.getHeight(null);
                    if((width*1.0)/w < (height*1.0)/h){
                        if(width > w){
                            h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w/(width*1.0)));
                            System.out.println("change image's height, width:{}, height:{}."+w+","+h);
                        }
                    } else {
                        if(height > h){
                            w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h/(height*1.0)));
                            System.out.println("change image's width, width:{}, height:{}."+w+","+h);
                        }
                    }
                }
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.getGraphics();
                g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
                g.dispose();
                // 将图片保存在原目录并加上前缀
                ImageIO.write(bi, suffix, output);
                output.close();
            } catch (IOException e) {
            	System.out.println("generate thumbnail image failed."+e);
            }
        }else{
        	System.out.println("the src image is not exist.");
        }
    }
    public void thumbnailImage(File srcImg, int w, int h, String userName){
        String p = srcImg.getAbsolutePath();
        try {
            if(!srcImg.isDirectory()) p = srcImg.getParent();
            if(!p.endsWith(File.separator)) p = p + File.separator;
            String fro=".png";
            if(w==40) 
            	fro = "Smaller"+fro;
            if(w==100)
            	fro = "Biger"+fro;
           if(w>300||h>300)
            	fro = "SmallerCon"+fro;
            thumbnailImage(srcImg, new java.io.FileOutputStream(p + userName + fro), w, h);
        } catch (FileNotFoundException e) {
        	System.out.println("the dest image is not exist."+e);
        }
    }
    
    public void thumbnailImage(String imagePath, int w, int h, String userName){
        File srcImg = new File(imagePath);
        thumbnailImage(srcImg, w, h, userName);
    }
    
    public static void main(String[] args) throws IOException {
        //new ImageUtil().thumbnailImage("imgs/Tulips.jpg", 150, 100);
    	String userName = "zhangsan";
        new ImageUtil().cutImage("C:\\Users\\Administrator\\Desktop\\1.jpg","C:\\Users\\Administrator\\Desktop\\", userName,80, 80, 200, 200);
        new ImageUtil().thumbnailImage("C:\\Users\\Administrator\\Desktop\\"+userName+"SmallerCon.png", 40, 40,userName);
        new ImageUtil().thumbnailImage("C:\\Users\\Administrator\\Desktop\\"+userName+"SmallerCon.png", 60, 60,userName);
        new ImageUtil().thumbnailImage("C:\\Users\\Administrator\\Desktop\\"+userName+"SmallerCon.png", 100, 100,userName);
    }

}
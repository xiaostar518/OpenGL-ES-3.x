package com.bn.Sample10_1b;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import android.content.res.Resources;
import android.opengl.GLES30;

public class BnETC2Util 
{
	public static final int PKM_HEADER_SIZE=16;
	public static final int PKM_HEADER_WIDTH_OFFSET=12;
	public static final int PKM_HEADER_HEIGHT_OFFSET=14;
	
	public static byte[] loadDataFromAssets(String fname, Resources r)
	{
		byte[] data=null;
		InputStream in=null;
		try
		{
			in = r.getAssets().open(fname);
	    	int ch=0;
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    while((ch=in.read())!=-1)
		    {
		      	baos.write(ch);
		    }      
		    data=baos.toByteArray();
		    baos.close();
		    in.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		return data;
	}
	
    public static int initTextureETC2(String pkmName,Resources r) //textureId
	{
		//生成纹理ID
		int[] textures = new int[1];
		GLES30.glGenTextures
		(
				1,          //产生的纹理id的数量
				textures,   //纹理id的数组
				0           //偏移量
		);    
		int textureId=textures[0];    
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE);
        
        byte[] data=loadDataFromAssets(pkmName,r);
        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length).order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(data).position(PKM_HEADER_SIZE);//将起始位置放在文件头之后

        ByteBuffer header = ByteBuffer.allocateDirect(PKM_HEADER_SIZE).order(ByteOrder.BIG_ENDIAN);//创建头Buffer
        header.put(data, 0, PKM_HEADER_SIZE).position(0);//获取头数据

        int width = header.getShort(PKM_HEADER_WIDTH_OFFSET);
        int height = header.getShort(PKM_HEADER_HEIGHT_OFFSET);
        
        GLES30.glCompressedTexImage2D
        (
    		GLES30.GL_TEXTURE_2D, 
    		0, 
    		GLES30.GL_COMPRESSED_RGBA8_ETC2_EAC, 
    		width, 
    		height, 
    		0, 
    		data.length - PKM_HEADER_SIZE, 
    		buffer
        );  
		
		return textureId;
	}
}

package com.bn.Sample3_12.tex3d;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.Resources;

public class Load3DTexUtil 
{
	//将字节数组转四字节整数
	public static int fromBytesToInt(byte[] buff)
	{
		return (buff[3] << 24) 
			+ ((buff[2] << 24) >>> 8) 
			+ ((buff[1] << 24) >>> 16) 
			+ ((buff[0] << 24) >>> 24);
	}
	
	public static Tex3D load(Resources res,String fileName)
	{
		Tex3D result=new Tex3D();
		
		try 
		{
			InputStream fin=res.getAssets().open(fileName);
			byte[] buf=new byte[4];
			fin.read(buf);
			result.width=fromBytesToInt(buf);
			fin.read(buf);
			result.height=fromBytesToInt(buf);
			fin.read(buf);
			result.depth=fromBytesToInt(buf);
			System.out.println(result.width+","+result.height+","+result.depth);
			buf=new byte[result.width*result.height*result.depth*4];
			fin.read(buf);
			fin.close();
			result.data=buf;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		
		return result;
	}
}

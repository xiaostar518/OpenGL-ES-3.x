function Button()
{
	this.drawSelf = function(uriIndex,ms2D)
	{
		ms2D.pushMatrix();
		
        //开启混合
        gl.enable(gl.BLEND);  
        //设置混合因子
        gl.blendFunc(gl.SRC_ALPHA, gl.ONE_MINUS_SRC_ALPHA);
		
		//绘制暂停、前进按钮图标
		if(uriIndex=="0")
		{
			ms2D.pushMatrix(); 
			ms2D.translate(goOrStopX,goOrStopY,0);
			ms2D.scale(0.005,0.005,1);
			if(isGo)
			{
				rectangle.drawSelf(ms2D,texMap["stopTex"]);//绘制暂停按钮图标
			}else
			{
				rectangle.drawSelf(ms2D,texMap["goTex"]);//绘制前进按钮图标
			}
			ms2D.popMatrix();
		}
		//绘制摇杆图标
		if(uriIndex=="1")
		{
			ms2D.pushMatrix(); 
			ms2D.translate(directionX,directionY,0);
			ms2D.scale(0.005,0.005,1);//0.25
			rectangle.drawSelf(ms2D,texMap["directionTex"]);//绘制摇杆图标
			ms2D.popMatrix();
		}
		//绘制返回按钮
		ms2D.pushMatrix();
		ms2D.translate(returnX,returnY,0);
		ms2D.scale(0.005,0.005,1);//0.25,0.25
		rectangle.drawSelf(ms2D,texMap["returnTex"]);//绘制返回按钮
        ms2D.popMatrix();
		
		//绘制指南针图标
		ms2D.pushMatrix();
		ms2D.translate(returnX,-returnY,0);
		ms2D.rotate(currentAngle/0.017453,0,0,1);
		ms2D.scale(0.002,0.004,1);//0.25,0.25
		rectangle.drawSelf(ms2D,texMap["pointerTex"]);//绘制指南针图标
        ms2D.popMatrix();
		
			
        //关闭混合
        gl.disable(gl.BLEND);
		
		ms2D.popMatrix();
	}
}
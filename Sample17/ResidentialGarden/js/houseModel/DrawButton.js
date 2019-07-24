function DrawButton()
{//绘制按钮构造方法
	this.drawSelf = function(ms2D)
	{//绘制按钮方法
		ms2D.pushMatrix();//保护现场
		
        //开启混合
        gl.enable(gl.BLEND);  
        //设置混合因子
        gl.blendFunc(gl.SRC_ALPHA, gl.ONE_MINUS_SRC_ALPHA);
		
		//绘制按钮图
		ms2D.pushMatrix();//保护现场
		ms2D.translate(ONEROOM_X,ONEROOM_Y,0);//平移到指定位置
		ms2D.scale(0.012,0.005,1);//缩放比例
		rectangle.drawSelf(ms2D,texMap[PIC_NAME[ONE]]);//绘制一室一厅按钮
		ms2D.popMatrix();//恢复现场
		
		ms2D.pushMatrix(); 
		ms2D.translate(TOWROOM_X,TOWROOM_Y,0);
		ms2D.scale(0.012,0.005,1);
		rectangle.drawSelf(ms2D,texMap[PIC_NAME[TWO]]);//绘制二室一厅按钮
		ms2D.popMatrix();
		
		ms2D.pushMatrix(); 
		ms2D.translate(THREEROOM_X,THREEROOM_Y,0);
		ms2D.scale(0.012,0.005,1);
		rectangle.drawSelf(ms2D,texMap[PIC_NAME[THREE]]);//绘制三室一厅按钮
		ms2D.popMatrix();
		
		ms2D.pushMatrix(); 
		ms2D.translate(HUXING_X,HUXING_Y,0);
		ms2D.scale(0.024,0.026,1);
		rectangle.drawSelf(ms2D,texMap["huxing"]);//绘制户型展示图标
		ms2D.popMatrix();
        
		//绘制返回按钮
		ms2D.pushMatrix();
		ms2D.translate(returnX,returnY,0);
		ms2D.scale(0.005,0.005,1);//0.25,0.25
		rectangle.drawSelf(ms2D,texMap["returnTex"]);//绘制返回按钮
        ms2D.popMatrix();
		
		//关闭混合
        gl.disable(gl.BLEND);
		ms2D.popMatrix();//恢复现场
	}
}
//===========将标准屏幕上的坐标转换成2D世界坐标---start======//
function fromScreenTo2DWordX(x)
{//转换X坐标	
	var x2D=(x/(SCREEN_WIDTH_STANDARD/2)-1)*RATIO;
	return x2D;
}
function fromScreenTo2DWordY(y)
{//转换Y坐标
	var y2D=1-y/(SCREEN_HEIGHT_STANDARD/2);
	return y2D;
}
function fromPixHEIGHTToNearHEIGHT(height)
{//转换高度尺寸大小
	return height*2/SCREEN_HEIGHT_STANDARD;
}
function fromPixWIDTHToNearWIDTH(width)
{//转换宽度尺寸大小
	return width*2/SCREEN_WIDTH_STANDARD;
}
//===========将标准屏幕上的坐标转换成2D世界坐标---end======//
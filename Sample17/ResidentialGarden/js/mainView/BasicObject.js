function BasicObject
(
	object,//obj对象
	width,//2D物体的宽度
	height,//2D物体的高度
	x,//绘制的位置
	y,
	texId//纹理ID
)
{
	this.texId=texId;//获得纹理坐标Id
	this.width=fromPixWIDTHToNearWIDTH(width);
	this.height=fromPixHEIGHTToNearHEIGHT(height);
	this.x=fromScreenTo2DWordX(x);
	this.y=fromScreenTo2DWordY(y);
	
	this.drawSelf=function(lms)
	{
		lms.pushMatrix();
		lms.translate(this.x,this.y, 0);//平移
		lms.scale(this.width,this.height,0);
		
		object.drawSelf(lms,this.texId);//绘制3D物体
		
		lms.popMatrix();
	}      
}
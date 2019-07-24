function MatrixState()
{
	this.mProjMatrix = new Array(16);
	this.mVMatrix = new Array(16);
	this.currMatrix=new Array(16);
	this.mStack=new Array(100);
	this.cx=0;
	this.cy=0;
	this.cz=0;
  
	//获取不变换初始矩阵
	this.setInitStack=function()
	{
		this.currMatrix=new Array(16);
		setIdentityM(this.currMatrix,0);
	}
	
	//保护变换矩阵
	this.pushMatrix=function()
	{	    	
		this.mStack.push(this.currMatrix.slice(0));
	}
	
	//恢复变换矩阵
	this.popMatrix=function()
	{
		this.currMatrix=this.mStack.pop();
	}
	
	//执行平移变换
	this.translate=function(x,y,z)//设置沿xyz轴移动
	{
		translateM(this.currMatrix, 0, x, y, z);
	}
	
	//执行旋转变换
	this.rotate=function(angle,x,y,z)//设置绕xyz轴移动
	{
		rotateM(this.currMatrix,0,angle,x,y,z);
	}
	
	//执行缩放变换
	this.scale=function(x,y,z)//设置绕xyz轴移动
	{
		scaleM(this.currMatrix,0,x,y,z) 
	}
	
	//设置摄像机
	this.setCamera=function
	(
		cx,	//摄像机位置x
		cy,   //摄像机位置y
		cz,   //摄像机位置z
		tx,   //摄像机目标点x
		ty,   //摄像机目标点y
		tz,   //摄像机目标点z
		upx,  //摄像机UP向量X分量
		upy,  //摄像机UP向量Y分量
		upz   //摄像机UP向量Z分量		
	)
	{
		setLookAtM
		(
			this.mVMatrix,0, 
			cx,cy,cz,
			tx,ty,tz,
			upx,upy,upz
		);
		this.cx=cx;
		this.cy=cy;
		this.cz=cz;
	}
	
	//设置透视投影参数
	this.setProjectFrustum=function
	(
		left,		//near面的left
		right,    //near面的right
		bottom,   //near面的bottom
		top,      //near面的top
		near,		//near面距离
		far       //far面距离
	)
	{
		frustumM(this.mProjMatrix, 0, left, right, bottom, top, near, far);    	
	}
	
	//设置正交投影参数
	this.setProjectOrtho=function
	(
		left,		//near面的left
		right,    //near面的right
		bottom,   //near面的bottom
		top,      //near面的top
		near,		//near面距离
		far       //far面距离
	)
	{    	
		orthoM(this.mProjMatrix, 0, left, right, bottom, top, near, far);
	}  
	
	//获取具体物体的总变换矩阵
	this.getFinalMatrix=function()
	{
		var mMVPMatrix=new Array(16);
		multiplyMM(mMVPMatrix, 0, this.mVMatrix, 0, this.currMatrix, 0);
		multiplyMM(mMVPMatrix, 0, this.mProjMatrix, 0, mMVPMatrix, 0);        
		return mMVPMatrix;
	} 
	
	//获取具体物体的变换矩阵
	this.getMMatrix=function()
	{       
		return this.currMatrix;
	}
	//获取摄像机矩阵的逆矩阵的方法
	this.getInvertMvMatrix=function(){
		var invM = new Array(16);
		invertM(invM, 0, this.mVMatrix, 0);//求逆矩阵
		return invM;
	}
		
	//通过摄像机变换后的点求变换前的点的方法：乘以摄像机矩阵的逆矩阵
	this.fromPtoPreP=function(p){
		//通过逆变换，得到变换之前的点
		var inverM = this.getInvertMvMatrix();//获取逆变换矩阵
		var preP = new Array(4);
		multiplyMV(preP, 0, inverM, 0,new Float32Array([p[0],p[1],p[2],1]), 0);
		//new Array(p[0],p[1],p[2],1), 0);//求变换前的点
		//alert("Matrix"+inverM);
		return new Float32Array([preP[0],preP[1],preP[2]]);
		//new Array(preP[0],preP[1],preP[2]);//变换前的点就是变换之前的法向量
	}
}

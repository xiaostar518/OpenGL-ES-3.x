//加载的用于绘制的3D物体
function ObjObject
(
	gl,				//GL上下文
	vertexDataIn,   //顶点坐标数组
	vertexNormalIn, //顶点法向量数组
	vertexTexCoorIn,//顶点纹理坐标数组
	programIn		//着色器程序对象
)
{
	//接收顶点数据
	this.vertexData=vertexDataIn;   	  
	//得到顶点数量
	this.vcount=this.vertexData.length/3;
	//创建顶点数据缓冲
	this.vertexBuffer=gl.createBuffer();
	//将顶点数据送入缓冲
	gl.bindBuffer(gl.ARRAY_BUFFER,this.vertexBuffer);
	gl.bufferData(gl.ARRAY_BUFFER,new Float32Array(this.vertexData),gl.STATIC_DRAW); 

	//接收顶点法向量数据
	this.vertexNormal=vertexNormalIn;  
	//创建顶点法向量数据缓冲
	this.vertexNormalBuffer=gl.createBuffer();
	//将顶点法向量数据送入缓冲
	gl.bindBuffer(gl.ARRAY_BUFFER,this.vertexNormalBuffer);
	gl.bufferData(gl.ARRAY_BUFFER,new Float32Array(this.vertexNormal),gl.STATIC_DRAW); 

	//接收顶点纹理坐标数据
	this.vertexTexCoor=vertexTexCoorIn;
	//创建顶点纹理坐标缓冲
	this.vertexTexCoorBuffer=gl.createBuffer();
	//将顶点纹理坐标数据送入缓冲
	gl.bindBuffer(gl.ARRAY_BUFFER,this.vertexTexCoorBuffer);
	gl.bufferData(gl.ARRAY_BUFFER,new Float32Array(this.vertexTexCoor),gl.STATIC_DRAW);	    	  

	//加载着色器程序
	this.program=programIn; 

	this.drawSelf=function(Oms,texture)
	{		
		Oms.pushMatrix(); 
		gl.useProgram(this.program);			
		//送入总矩阵
		var uMVPMatrixHandle=gl.getUniformLocation(this.program, "uMVPMatrix");   
		gl.uniformMatrix4fv(uMVPMatrixHandle,false,new Float32Array(Oms.getFinalMatrix())); 

		//送入变换矩阵
		var uMMatrixHandle=gl.getUniformLocation(this.program, "uMMatrix");
		gl.uniformMatrix4fv(uMMatrixHandle,false,new Float32Array(Oms.currMatrix)); 
	  
		//送入光源位置
		var uLightLocationHandle=gl.getUniformLocation(this.program, "uLightLocation");
		gl.uniform3fv(uLightLocationHandle,new Float32Array([lightManager.lx,lightManager.ly,lightManager.lz]));
		//[0,500000,0]));
	  
		//送入摄像机位置
		var uCameraHandle=gl.getUniformLocation(this.program, "uCamera");
		gl.uniform3fv(uCameraHandle,new Float32Array([0,0,15]));
	  
		//启用顶点数据
		gl.enableVertexAttribArray(gl.getAttribLocation(this.program, "aPosition"));        
		//将顶点数据送入渲染管线
		gl.bindBuffer(gl.ARRAY_BUFFER, this.vertexBuffer);
		gl.vertexAttribPointer(gl.getAttribLocation(this.program, "aPosition"), 3, gl.FLOAT, false, 0, 0);   

		//启用法向量数据
		gl.enableVertexAttribArray(gl.getAttribLocation(this.program, "aNormal")); 
		//将顶点法向量数据送入渲染管线
		gl.bindBuffer(gl.ARRAY_BUFFER, this.vertexNormalBuffer);
		gl.vertexAttribPointer(gl.getAttribLocation(this.program, "aNormal"), 3, gl.FLOAT, false, 0, 0);    

		//启用纹理坐标数据
		gl.enableVertexAttribArray(gl.getAttribLocation(this.program, "aTexCoor")); 
		//将顶点纹理坐标数据送入渲染管线
		gl.bindBuffer(gl.ARRAY_BUFFER, this.vertexTexCoorBuffer);
		gl.vertexAttribPointer(gl.getAttribLocation(this.program, "aTexCoor"), 2, gl.FLOAT, false, 0, 0);        
	  
		//设置纹理
		gl.uniform1i(gl.getUniformLocation(this.program, "sTexture"), 0);

		//绑定纹理
		gl.activeTexture(gl.TEXTURE0);
		gl.bindTexture(gl.TEXTURE_2D, texture);              

		//用顶点法绘制物体
		gl.drawArrays(gl.TRIANGLES, 0, this.vcount); 
	  
		Oms.popMatrix();
	}      
}
//加载的用于绘制的3D物体
function ObjObject
(
 	gl,						  //GL上下文
	vertexDataIn,    //顶点坐标数据
	vertexNormalIn,   //顶点法向量数据
	vertexTexCoorIn,	//顶点纹理坐标数据
	programIn		//着色器程序id
)
{
	//初始化顶点坐标数据
	this.vertexData=vertexDataIn;   	  
	//得到顶点数量
	this.vcount=this.vertexData.length/3;
	//创建顶点坐标数据缓冲
	this.vertexBuffer=gl.createBuffer();
	//绑定顶点坐标数据缓冲
	gl.bindBuffer(gl.ARRAY_BUFFER,this.vertexBuffer);
	//将顶点坐标数据送入缓冲
	gl.bufferData(gl.ARRAY_BUFFER,new Float32Array(this.vertexData),gl.STATIC_DRAW);  

	//初始化顶点法向量数据
	this.vertexNormal=vertexNormalIn;  
	//创建顶点法向量数据缓冲
	this.vertexNormalBuffer=gl.createBuffer();
	//绑定顶点法向量数据缓冲
	gl.bindBuffer(gl.ARRAY_BUFFER,this.vertexNormalBuffer);
	//将顶点法向量数据送入缓冲
	gl.bufferData(gl.ARRAY_BUFFER,new Float32Array(this.vertexNormal),gl.STATIC_DRAW); 

  
	//初始化顶点纹理坐标数据
	this.vertexTexCoor=vertexTexCoorIn;
	//创建顶点纹理坐标数据缓冲
	this.vertexTexCoorBuffer=gl.createBuffer();
	//绑定顶点纹理坐标数据缓冲
	gl.bindBuffer(gl.ARRAY_BUFFER,this.vertexTexCoorBuffer);
	//将顶点纹理坐标数据送入缓冲
	gl.bufferData(gl.ARRAY_BUFFER,new Float32Array(this.vertexTexCoor),gl.STATIC_DRAW);  
	  
	//初始化着色器程序id
	this.program=programIn;          
	
	this.drawSelfEarth=function(ms,texture0,texture1)
	{//绘制地球物体的方法			
		gl.useProgram(this.program);	//指定使用某套着色器程序	
		
		//获取总变换矩阵引用id
		var uMVPMatrixHandle=gl.getUniformLocation(this.program, "uMVPMatrix");
		//将总变换矩阵送入渲染管线		
		gl.uniformMatrix4fv(uMVPMatrixHandle,false,new Float32Array(ms.getFinalMatrix())); 

		//获取变换矩阵引用id
		var uMMatrixHandle=gl.getUniformLocation(this.program, "uMMatrix");
		//将变换矩阵送入渲染管线	
		gl.uniformMatrix4fv(uMMatrixHandle,false,new Float32Array(ms.currMatrix)); 

		//获取光源位置引用id
		var uLightLocationHandle=gl.getUniformLocation(this.program, "uLightLocation");
		//将光源位置送入渲染管线
		gl.uniform3fv(uLightLocationHandle,new Float32Array([sunx,5,sunz]));
		
		//获取摄像机位置引用id
		var uCameraHandle=gl.getUniformLocation(this.program, "uCamera");
		//将摄像机位置送入渲染管线
		gl.uniform3fv(uCameraHandle,new Float32Array([ms.cx,ms.cy,ms.cz]));

		//启用顶点坐标数据数组
		gl.enableVertexAttribArray(gl.getAttribLocation(this.program, "aPosition"));        
		//绑定顶点坐标数据缓冲
		gl.bindBuffer(gl.ARRAY_BUFFER, this.vertexBuffer);
		//给管线指定顶点坐标数据
		gl.vertexAttribPointer(gl.getAttribLocation(this.program, "aPosition"), 3, gl.FLOAT, false, 0, 0);   

		//启用顶点法向量数据数组
		gl.enableVertexAttribArray(gl.getAttribLocation(this.program, "aNormal")); 
		//绑定顶点法向量数据缓冲
		gl.bindBuffer(gl.ARRAY_BUFFER, this.vertexNormalBuffer);
		//给管线指定顶点法向量数据
		gl.vertexAttribPointer(gl.getAttribLocation(this.program, "aNormal"), 3, gl.FLOAT, false, 0, 0);    

		//启用顶点纹理坐标数据数组
		gl.enableVertexAttribArray(gl.getAttribLocation(this.program, "aTexCoor")); 
		//绑定顶点纹理坐标数据缓冲
		gl.bindBuffer(gl.ARRAY_BUFFER, this.vertexTexCoorBuffer);
		//给管线指定顶点纹理坐标数据
		gl.vertexAttribPointer(gl.getAttribLocation(this.program, "aTexCoor"), 2, gl.FLOAT, false, 0, 0); 
		
		gl.activeTexture(gl.TEXTURE0);//设置使用的纹理编号-0
		gl.bindTexture(gl.TEXTURE_2D, texture0);//绑定白天纹理
		gl.activeTexture(gl.TEXTURE1);//设置使用的纹理编号-1
		gl.bindTexture(gl.TEXTURE_2D, texture1);//绑定黑夜纹理
		//设置纹理
		gl.uniform1i(gl.getUniformLocation(this.program, "sTexture"), 0);//将白天纹理送入渲染管线 
		gl.uniform1i(gl.getUniformLocation(this.program, "sTextureNight"), 1);	 //将黑夜纹理送入渲染管线		  

		//用顶点法绘制物体
		gl.drawArrays(gl.TRIANGLES, 0, this.vcount); 
	}      

	this.drawSelfMoon=function(ms,texture)
	{//绘制月球物体的方法	
	
		gl.useProgram(this.program);//指定使用某套着色器程序			
		
		//获取总变换矩阵引用id
		var uMVPMatrixHandle=gl.getUniformLocation(this.program, "uMVPMatrix");
		//将总变换矩阵送入渲染管线		
		gl.uniformMatrix4fv(uMVPMatrixHandle,false,new Float32Array(ms.getFinalMatrix())); 

		//获取变换矩阵引用id
		var uMMatrixHandle=gl.getUniformLocation(this.program, "uMMatrix");
		//将变换矩阵送入渲染管线	
		gl.uniformMatrix4fv(uMMatrixHandle,false,new Float32Array(ms.currMatrix)); 

		//获取光源位置引用id
		var uLightLocationHandle=gl.getUniformLocation(this.program, "uLightLocation");
		//将光源位置送入渲染管线
		gl.uniform3fv(uLightLocationHandle,new Float32Array([sunx,5,sunz]));
		
		//获取摄像机位置引用id
		var uCameraHandle=gl.getUniformLocation(this.program, "uCamera");
		//将摄像机位置送入渲染管线
		gl.uniform3fv(uCameraHandle,new Float32Array([ms.cx,ms.cy,ms.cz]));

		//启用顶点坐标数据数组
		gl.enableVertexAttribArray(gl.getAttribLocation(this.program, "aPosition"));        
		//绑定顶点坐标数据缓冲
		gl.bindBuffer(gl.ARRAY_BUFFER, this.vertexBuffer);
		//给管线指定顶点坐标数据
		gl.vertexAttribPointer(gl.getAttribLocation(this.program, "aPosition"), 3, gl.FLOAT, false, 0, 0);   

		//启用顶点法向量数据数组
		gl.enableVertexAttribArray(gl.getAttribLocation(this.program, "aNormal")); 
		//绑定顶点法向量数据缓冲
		gl.bindBuffer(gl.ARRAY_BUFFER, this.vertexNormalBuffer);
		//给管线指定顶点法向量数据
		gl.vertexAttribPointer(gl.getAttribLocation(this.program, "aNormal"), 3, gl.FLOAT, false, 0, 0);    

		//启用顶点纹理坐标数据数组
		gl.enableVertexAttribArray(gl.getAttribLocation(this.program, "aTexCoor")); 
		//绑定顶点纹理坐标数据缓冲
		gl.bindBuffer(gl.ARRAY_BUFFER, this.vertexTexCoorBuffer);
		//给管线指定顶点纹理坐标数据
		gl.vertexAttribPointer(gl.getAttribLocation(this.program, "aTexCoor"), 2, gl.FLOAT, false, 0, 0);

		
		gl.activeTexture(gl.TEXTURE0);//设置使用的纹理编号-0
		gl.bindTexture(gl.TEXTURE_2D, texture);//绑定纹理
		//设置纹理
		gl.uniform1i(gl.getUniformLocation(this.program, "sTexture"), 0);//将纹理送入渲染管线  			  

		//用顶点法绘制物体
		gl.drawArrays(gl.TRIANGLES, 0, this.vcount); 
	}      
}
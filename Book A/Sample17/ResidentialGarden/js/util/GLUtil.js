//初始化WebGL Canvas
function initWebGLCanvas(canvasName)
{
	var canvas = document.getElementById(canvasName);
	var names = ["webgl", "experimental-webgl", "webkit-3d", "moz-webgl"];
	var context = null;
	for (var ii = 0; ii < names.length; ++ii) 
	{
		try 
		{
			context = canvas.getContext(names[ii], null);				      
		} 
		catch(e) {}
		if (context) 
		{
			break;
		}
	}			    
	return context;
}

//加载单个着色器的方法			
function loadSingleShader(ctx, shaderScript)
{
	if (shaderScript.type == "vertex")
		var shaderType = ctx.VERTEX_SHADER;
	else if (shaderScript.type == "fragment")
		var shaderType = ctx.FRAGMENT_SHADER;
	else {
		log("*** Error: shader script of undefined type '"+shaderScript.type+"'");
		return null;
	}

	// Create the shader object
	var shader = ctx.createShader(shaderType);

	// Load the shader source
	ctx.shaderSource(shader, shaderScript.text);

	// Compile the shader
	ctx.compileShader(shader);

	// Check the compile status
	var compiled = ctx.getShaderParameter(shader, ctx.COMPILE_STATUS);
	if (!compiled && !ctx.isContextLost()) {
		// Something went wrong during compilation; get the error
		var error = ctx.getShaderInfoLog(shader);
		log("*** Error compiling shader '"+shaderId+"':"+error);
		ctx.deleteShader(shader);
		return null;
	}			
	return shader;
}	

//加载顶点着色器与片元着色器的套件
function loadShaderSerial(gl, vshader, fshader)
{
	//加载顶点着色器
	var vertexShader = loadSingleShader(gl, vshader);
	//加载片元着色器
	var fragmentShader = loadSingleShader(gl, fshader);

	//创建着色器程序
	var program = gl.createProgram();

	//将顶点着色器和片元着色器挂接到着色器程序
	gl.attachShader (program, vertexShader);
	gl.attachShader (program, fragmentShader);


	//链接着色器程序
	gl.linkProgram(program);

	//检查链接是否成功
	var linked = gl.getProgramParameter(program, gl.LINK_STATUS);
	if (!linked && !gl.isContextLost()) 
	{
		//获取并在控制台打印错误信息
		var error = gl.getProgramInfoLog (program);
		log("Error in program linking:"+error);

		gl.deleteProgram(program);
		gl.deleteProgram(fragmentShader);
		gl.deleteProgram(vertexShader);

		return null;
	}

	return program;
}

//加载纹理图的方法
function loadImageTexture(gl,url,texName)
{
	var texture = gl.createTexture();
	var image = new Image();
	image.onload = function() { doLoadImageTexture(gl, image, texture) }
	image.src = url;
	texMap[texName]=texture;
}

//加载纹理图的方法
function loadImageTexture1(gl, url){
	var texture = gl.createTexture();
	var image = new Image();
	image.onload = function(){ 
		doLoadImageTexture(gl, image, texture) 
	}
	image.src = url;
	return texture;
}

function doLoadImageTexture(gl, image, texture)
{
	gl.bindTexture(gl.TEXTURE_2D, texture);
	gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, image);
	gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
	gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
	gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.REPEAT);
	gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.REPEAT);
	gl.bindTexture(gl.TEXTURE_2D, null);
}
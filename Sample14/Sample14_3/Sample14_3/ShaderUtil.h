#ifndef OpenGL3_0Demo_ShaderUtil_hpp
#define OpenGL3_0Demo_ShaderUtil_hpp
#import <OpenGLES/ES3/gl.h>
#import <OpenGLES/ES3/glext.h>
#include <iostream>
#include "LoadResourceUtil.h"

class ShaderUtil
{
public:
    static GLuint createProgram(const string& vname,const string& fname)
    {//创建着色器程序的方法 
        string vertexShaderSource=LoadResourceUtil::loadShaderScript(vname);//加载脚本
        string fragmentShaderSource=LoadResourceUtil::loadShaderScript(fname);//加载脚本
        
        GLuint vertexShader = loadShader(vertexShaderSource.c_str(), GL_VERTEX_SHADER);//加载顶点着色器
        GLuint fragmentShader = loadShader(fragmentShaderSource.c_str(), GL_FRAGMENT_SHADER);//加载片元着色器
        GLuint programHandle = glCreateProgram();//创建程序
        glAttachShader(programHandle, vertexShader);//向程序中加入顶点着色器
        glAttachShader(programHandle, fragmentShader);//向程序中加入片元着色器
        glLinkProgram(programHandle);//链接程序
        GLint linkSuccess;//存放链接成功program数量的变量
        glGetProgramiv(programHandle, GL_LINK_STATUS, &linkSuccess);//获取的链接情况
        //若链接失败则报错
        if (linkSuccess == GL_FALSE) {
            GLchar messages[256];
            glGetProgramInfoLog(programHandle, sizeof(messages), 0, &messages[0]);//错误信息存储数组
            std::cout<<"MSG>>" << messages;//输出错误信息
            //exit(1);
        }
        return programHandle;
    }
    
    static GLuint loadShader(const char* source, GLenum shaderType)
    {//加载指定着色器的方法 
        //创建一个新着色器
        GLuint shaderHandle = glCreateShader(shaderType);
        //加载着色器的源代码
        glShaderSource(shaderHandle, 1, &source, 0);
        //编译着色器
        glCompileShader(shaderHandle);
        //编译是否成功标志位
        GLint compileSuccess;
        glGetShaderiv(shaderHandle, GL_COMPILE_STATUS, &compileSuccess);
        if (compileSuccess == GL_FALSE) {	//若编译失败
            GLchar messages[256];	//错误信息存储数组
            glGetShaderInfoLog(shaderHandle, sizeof(messages), 0, &messages[0]);
            std::cout << messages;//输出错误信息
            exit(1);//退出
        }
        return shaderHandle;
    }
    
    
};

#endif

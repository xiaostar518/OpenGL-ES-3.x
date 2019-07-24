#include "Triangle.hpp"
#include "ShaderUtil.h"
#include "MatrixState.hpp"

Triangle::Triangle()
{//构造函数
    initVertexData();
    initShader();
}

void Triangle::initVertexData()
{//初始化顶点数据
    vCount = 3;
    pCoords = &vertices[0];
    pColors = &colors[0];
}

void Triangle::initShader() 
{//初始化着色器
    mProgram = ShaderUtil::createProgram("Simplevert", "Simplefrag");//创建着色器程序
    
    maPositionHandle = glGetAttribLocation(mProgram, "Position");//获取顶点位置引用
    maColorHandle = glGetAttribLocation(mProgram, "SourceColor");//获取顶点纹理引用
    muMVPMatrixHandle = glGetUniformLocation(mProgram, "Modelview");//获取总变换矩阵引用
}

void Triangle::drawSelf()
{//绘制方法
    glUseProgram(mProgram);
    //初始化变换矩阵
    MatrixState::setInitStack();
   //设置沿Z轴负向移动
    MatrixState::translate(0, 0, -10);
    //设置绕x轴旋转
    MatrixState::rotate(aa++, 1, 0, 0);
    //将总变换矩阵传入渲染管线
    glUniformMatrix4fv(muMVPMatrixHandle, 1, 0, MatrixState::getFinalMatrix());
    //将顶点坐标数据传入渲染管线
    glVertexAttribPointer(maPositionHandle, 3, GL_FLOAT, GL_FALSE, 3*4, pCoords);
    //将顶点颜色数据传入渲染管线
    glVertexAttribPointer(maColorHandle, 4, GL_FLOAT, GL_FALSE, 4*4, pColors);
    //启动顶点位置数据数组
    glEnableVertexAttribArray(maPositionHandle);
    //启用顶点颜色数据数组
    glEnableVertexAttribArray(maColorHandle);
    //执行绘制
    glDrawArrays(GL_TRIANGLES, 0, vCount);
    //关闭顶点位置数组
    glDisableVertexAttribArray(maPositionHandle);
    //关闭顶点颜色数组
    glDisableVertexAttribArray(maColorHandle);
}
#ifndef RenderingEngine__h
#define RenderingEngine__h

#import <OpenGLES/ES3/gl.h>
#import <OpenGLES/ES3/glext.h>
#include "Triangle.hpp"
#include "IRenderingEngine.hpp"

class RenderingEngine:public IRenderingEngine{//实现IRenderingEngine接口
public:
    RenderingEngine();//构造函数
    void Initialize(int width, int height);//初始化函数
    void Render() const;//渲染函数
private:
    GLuint m_framebuffer;//帧缓冲ID
    GLuint m_renderbuffer;//渲染缓冲ID
    Triangle *t;//指向三角形绘制对象的指针
};


struct IRenderingEngine* CreateRenderer()//创建渲染引擎对象函数
{
    return new RenderingEngine();
}

#endif



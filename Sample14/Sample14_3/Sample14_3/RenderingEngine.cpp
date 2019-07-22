#include "RenderingEngine.hpp"
#include "MatrixState.hpp"


RenderingEngine::RenderingEngine()
{//构造函数
    glGenRenderbuffers(1, &m_renderbuffer);//创建渲染缓冲对象
    glBindRenderbuffer(GL_RENDERBUFFER, m_renderbuffer);//绑定至渲染管线
}

void RenderingEngine::Initialize(int width, int height)
{
    glGenFramebuffers(1, &m_framebuffer);//创建帧缓冲
    glBindFramebuffer(GL_FRAMEBUFFER, m_framebuffer);//绑定至渲染管线
    //将渲染缓冲作为颜色附件绑定到帧缓冲
    glFramebufferRenderbuffer(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,
                                 GL_RENDERBUFFER,m_renderbuffer);
    glViewport(0, 0, width, height);//设置视口
    t=new Triangle();//创建三角形绘制对象
    float ratio = (float) width/height;//计算屏幕宽与长比
    MatrixState::setProjectFrustum(-ratio, ratio, -1, 1, 5, 50);//设置投影矩阵
    MatrixState::setCamera(0, 0, 3, 0, 0, 0, 0, 1, 0);//设置摄像机矩阵 
}



void RenderingEngine::Render() const
{//渲染函数
    glClearColor(0.5f, 0.5f, 0.5f, 1);//设置背景颜色
    glClear(GL_COLOR_BUFFER_BIT);//清除颜色缓冲
    t->drawSelf();//执行绘制
}

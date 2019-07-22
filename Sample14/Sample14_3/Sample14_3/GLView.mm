#import "GLView.h"
#import "IRenderingEngine.hpp"

@implementation GLView

IRenderingEngine* mRenderingEngine;//指向渲染器接口实现对象的指针

- (id)initWithFrame:(CGRect)frame
{//传入CGect对象
    if (self = [super initWithFrame:frame]) {
        CAEAGLLayer* eaglLayer = (CAEAGLLayer*) super.layer;//创建CAEAGLLayer对象
        eaglLayer.opaque = YES;
        
        EAGLRenderingAPI api = kEAGLRenderingAPIOpenGLES3;//设置使用OPENGL ES3.0
        //使用OPENGL ES3.0版初始化EAGLContext
        mContext = [[EAGLContext alloc] initWithAPI:api];
         //使用3.0版初始化EAGLContext失败
        if (!mContext || ![EAGLContext setCurrentContext:mContext])
        {
            return nil;//返回空值
        }
        else
        {
            mRenderingEngine = CreateRenderer();//创建mRenderingEngine对象
        }
        
        [mContext renderbufferStorage:GL_RENDERBUFFER fromDrawable: eaglLayer];
        
        //调用渲染器对象的初始化函数对其进行初始化
        mRenderingEngine->Initialize(CGRectGetWidth(frame), CGRectGetHeight(frame));
        
        [self drawView: nil];//调用drawView函数
        
        CADisplayLink* displayLink;//声明指向CADisplayLink对象的指针
        displayLink = [CADisplayLink displayLinkWithTarget:self
                                                  selector:@selector(drawView:)];//创建CADisplayLink对象
        
        [displayLink addToRunLoop:[NSRunLoop currentRunLoop]//设置循环方式
                          forMode:NSDefaultRunLoopMode];
        
    }
    return self;
}

- (void) drawView: (CADisplayLink*) displayLink
{//drawView函数
    mRenderingEngine->Render();//进行一帧的渲染
    [mContext presentRenderbuffer:GL_RENDERBUFFER];
}

+ (Class) layerClass
{
    return [CAEAGLLayer class];//返回CAEAGLLayer
}

@end

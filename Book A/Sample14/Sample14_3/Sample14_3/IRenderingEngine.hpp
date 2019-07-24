#ifndef OpenGL3_0Demo_IRenderingEngine_h
#define OpenGL3_0Demo_IRenderingEngine_h

// Physical orientation of a handheld device, equivalent to UIDeviceOrientation.
// Creates an instance of the renderer and sets up various OpenGL state.

struct IRenderingEngine* CreateRenderer();//创建渲染器对象的函数

// Interface to the OpenGL ES renderer; consumed by GLView.
struct IRenderingEngine{
    virtual void Initialize(int width, int height) = 0;//初始化方法
    virtual void Render() const = 0;//渲染方法
    virtual ~IRenderingEngine() {}//析构函数
};

#endif

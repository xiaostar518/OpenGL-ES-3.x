#import <UIKit/UIKit.h>
#import <OpenGLES/EAGL.h>
#import <QuartzCore/QuartzCore.h>
#import <OpenGLES/ES3/gl.h>
#import <OpenGLES/ES3/glext.h>


@interface GLView : UIView
{
    EAGLContext* mContext;//指向EAGLContext对象的指针

}
- (void) drawView: (CADisplayLink*) displayLink;    //drawView方法
@end

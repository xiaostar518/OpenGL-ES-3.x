#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>
#import <string>
#import <iostream>
#import <OpenGLES/ES3/gl.h>
#import <OpenGLES/ES3/glext.h>

#import "LoadResourceUtil.h"

using namespace std;

string LoadResourceUtil::loadShaderScript(const string& name)
{
    NSString* basePath = [[NSString alloc] initWithUTF8String:name.c_str()];
    NSBundle* mainBundle = [NSBundle mainBundle];
    //    获取txt文件路径
    NSString *txtPath = [mainBundle pathForResource:basePath ofType:@"sh"];
    //    将txt到string对象中，编码类型为NSUTF8StringEncoding
    NSString *nr = [[NSString  alloc] initWithContentsOfFile:txtPath encoding:NSUTF8StringEncoding error:nil];
    
    const char* scriptNr=[nr UTF8String];
    
    return string(scriptNr);
}

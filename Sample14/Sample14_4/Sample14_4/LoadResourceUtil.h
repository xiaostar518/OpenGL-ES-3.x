#ifndef LoadResourceUtil_h
#define LoadResourceUtil_h

using namespace std;

class LoadResourceUtil
{
public:
    static GLuint initTexture(const string& name);
    static string loadShaderScript(const string& name);
};

#endif

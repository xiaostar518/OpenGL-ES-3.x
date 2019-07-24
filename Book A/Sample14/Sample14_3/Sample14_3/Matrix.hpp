#ifndef __OpenGL3_0Demo__Matrix__
#define __OpenGL3_0Demo__Matrix__

#include <iostream>

class Matrix {
public:
    static void multiplyMM(float* result, int resultOffset, float* mlIn, int lhsOffset, float* mrIn, int rhsOffset)
    {
        double ml[16];
		double mr[16];
		for(int i=0;i<16;i++)
		{
			ml[i]=mlIn[i];
			mr[i]=mrIn[i];
		}
		
		result[0+resultOffset]=(float) (ml[0+lhsOffset]*mr[0+rhsOffset]+ml[4+lhsOffset]*mr[1+rhsOffset]+ml[8+lhsOffset]*mr[2+rhsOffset]+ml[12+lhsOffset]*mr[3+rhsOffset]);
		result[1+resultOffset]=(float) (ml[1+lhsOffset]*mr[0+rhsOffset]+ml[5+lhsOffset]*mr[1+rhsOffset]+ml[9+lhsOffset]*mr[2+rhsOffset]+ml[13+lhsOffset]*mr[3+rhsOffset]);
		result[2+resultOffset]=(float) (ml[2+lhsOffset]*mr[0+rhsOffset]+ml[6+lhsOffset]*mr[1+rhsOffset]+ml[10+lhsOffset]*mr[2+rhsOffset]+ml[14+lhsOffset]*mr[3+rhsOffset]);
		result[3+resultOffset]=(float) (ml[3+lhsOffset]*mr[0+rhsOffset]+ml[7+lhsOffset]*mr[1+rhsOffset]+ml[11+lhsOffset]*mr[2+rhsOffset]+ml[15+lhsOffset]*mr[3+rhsOffset]);
		
		result[4+resultOffset]=(float) (ml[0+lhsOffset]*mr[4+rhsOffset]+ml[4+lhsOffset]*mr[5+rhsOffset]+ml[8+lhsOffset]*mr[6+rhsOffset]+ml[12+lhsOffset]*mr[7+rhsOffset]);
		result[5+resultOffset]=(float) (ml[1+lhsOffset]*mr[4+rhsOffset]+ml[5+lhsOffset]*mr[5+rhsOffset]+ml[9+lhsOffset]*mr[6+rhsOffset]+ml[13+lhsOffset]*mr[7+rhsOffset]);
		result[6+resultOffset]=(float) (ml[2+lhsOffset]*mr[4+rhsOffset]+ml[6+lhsOffset]*mr[5+rhsOffset]+ml[10+lhsOffset]*mr[6+rhsOffset]+ml[14+lhsOffset]*mr[7+rhsOffset]);
		result[7+resultOffset]=(float) (ml[3+lhsOffset]*mr[4+rhsOffset]+ml[7+lhsOffset]*mr[5+rhsOffset]+ml[11+lhsOffset]*mr[6+rhsOffset]+ml[15+lhsOffset]*mr[7+rhsOffset]);
		
		result[8+resultOffset]=(float) (ml[0+lhsOffset]*mr[8+rhsOffset]+ml[4+lhsOffset]*mr[9+rhsOffset]+ml[8+lhsOffset]*mr[10+rhsOffset]+ml[12+lhsOffset]*mr[11+rhsOffset]);
		result[9+resultOffset]=(float) (ml[1+lhsOffset]*mr[8+rhsOffset]+ml[5+lhsOffset]*mr[9+rhsOffset]+ml[9+lhsOffset]*mr[10+rhsOffset]+ml[13+lhsOffset]*mr[11+rhsOffset]);
		result[10+resultOffset]=(float) (ml[2+lhsOffset]*mr[8+rhsOffset]+ml[6+lhsOffset]*mr[9+rhsOffset]+ml[10+lhsOffset]*mr[10+rhsOffset]+ml[14+lhsOffset]*mr[11+rhsOffset]);
		result[11+resultOffset]=(float) (ml[3+lhsOffset]*mr[8+rhsOffset]+ml[7+lhsOffset]*mr[9+rhsOffset]+ml[11+lhsOffset]*mr[10+rhsOffset]+ml[15+lhsOffset]*mr[11+rhsOffset]);
		
		result[12+resultOffset]=(float) (ml[0+lhsOffset]*mr[12+rhsOffset]+ml[4+lhsOffset]*mr[13+rhsOffset]+ml[8+lhsOffset]*mr[14+rhsOffset]+ml[12+lhsOffset]*mr[15+rhsOffset]);
		result[13+resultOffset]=(float) (ml[1+lhsOffset]*mr[12+rhsOffset]+ml[5+lhsOffset]*mr[13+rhsOffset]+ml[9+lhsOffset]*mr[14+rhsOffset]+ml[13+lhsOffset]*mr[15+rhsOffset]);
		result[14+resultOffset]=(float) (ml[2+lhsOffset]*mr[12+rhsOffset]+ml[6+lhsOffset]*mr[13+rhsOffset]+ml[10+lhsOffset]*mr[14+rhsOffset]+ml[14+lhsOffset]*mr[15+rhsOffset]);
		result[15+resultOffset]=(float) (ml[3+lhsOffset]*mr[12+rhsOffset]+ml[7+lhsOffset]*mr[13+rhsOffset]+ml[11+lhsOffset]*mr[14+rhsOffset]+ml[15+lhsOffset]*mr[15+rhsOffset]);
    }
    static void multiplyMV (float* resultVec, int resultVecOffset, float* mlIn, int lhsMatOffset,
                            float* vrIn, int rhsVecOffset)
	{
		double ml[16];
		double vr[16];
		for(int i=0;i<16;i++)
		{
			ml[i]=mlIn[i];
		}
		vr[0]=vrIn[0];
		vr[1]=vrIn[1];
		vr[2]=vrIn[2];
		vr[3]=vrIn[3];
		
		resultVec[0+resultVecOffset]=(float) (ml[0+lhsMatOffset]*vr[0+rhsVecOffset]+ml[4+lhsMatOffset]*vr[1+rhsVecOffset]+ml[8+lhsMatOffset]*vr[2+rhsVecOffset]+ml[12+lhsMatOffset]*vr[3+rhsVecOffset]);
		resultVec[1+resultVecOffset]=(float) (ml[1+lhsMatOffset]*vr[0+rhsVecOffset]+ml[5+lhsMatOffset]*vr[1+rhsVecOffset]+ml[9+lhsMatOffset]*vr[2+rhsVecOffset]+ml[13+lhsMatOffset]*vr[3+rhsVecOffset]);
		resultVec[2+resultVecOffset]=(float) (ml[2+lhsMatOffset]*vr[0+rhsVecOffset]+ml[6+lhsMatOffset]*vr[1+rhsVecOffset]+ml[10+lhsMatOffset]*vr[2+rhsVecOffset]+ml[14+lhsMatOffset]*vr[3+rhsVecOffset]);
		resultVec[3+resultVecOffset]=(float) (ml[3+lhsMatOffset]*vr[0+rhsVecOffset]+ml[7+lhsMatOffset]*vr[1+rhsVecOffset]+ml[11+lhsMatOffset]*vr[2+rhsVecOffset]+ml[15+lhsMatOffset]*vr[3+rhsVecOffset]);
	}
    
    static void setIdentityM (float* sm, int smOffset)
	{
		for(int i=0;i<16;i++)
		{
			sm[i]=0;
		}
		
		sm[0]=1;
		sm[5]=1;
		sm[10]=1;
		sm[15]=1;
	}
    
    static void setRotateM(float* m, int mOffset,float a, float x, float y, float z)
    {
        float radians = a * 3.14159f / 180.0f;
        float s = std::sin(radians);
        float c = std::cos(radians);
        float sm[16];
        setIdentityM(sm, 0);
        sm[0] = c + (1 - c) * x * x;
        sm[1] = (1 - c) * x * y - z * s;
        sm[2] = (1 - c) * x * z + y * s;
        sm[4] = (1 - c) * x * y + z * s;
        sm[5] = c + (1 - c) * y * y;
        sm[6] = (1 - c) * y * z - x * s;
        sm[8] = (1 - c) * x * z - y * s;
        sm[9] = (1 - c) * y * z + x * s;
        sm[10] = c + (1 - c) * z * z;
        for(int i=0;i<16;i++)
		{
			m[i]=sm[i];
		}
    }
	
	static void translateM(float* m, int mOffset,float x, float y, float z)
	{
		for (int i=0 ; i<4 ; i++)
		{
			int mi = mOffset + i;
			m[12 + mi] += m[mi] * x + m[4 + mi] * y + m[8 + mi] * z;
		}
	}
	
    static void rotateM(float* m, int mOffset,float a, float x, float y, float z)
	{
		float rm[16];
		setRotateM(rm, 0, a, x, y, z);
		float rem[16];
		multiplyMM(rem, 0, m, 0, rm, 0);
		for(int i=0;i<16;i++)
		{
			m[i]=rem[i];
		}
	}
    
    static void scaleM(float* m, int mOffset, float x, float y, float z)
    {
        float sm[16];
        setIdentityM(sm, 0);
        sm[0] = x;
        sm[5] = y;
        sm[10] = z;
        sm[15] = 1;
        float tm[16];
        multiplyMM(tm,0,m,0,sm,0);
        for(int i=0;i<16;i++)
		{
			m[i]=tm[i];
		}
    }
    
    static void transposeM(float* mTrans, int mTransOffset, float* m, int mOffset)
    {
        mTrans[0] = m[0]; mTrans[1] = m[4]; mTrans[2] = m[8]; mTrans[3] = m[12];
        mTrans[4] = m[1]; mTrans[5] = m[5]; mTrans[6] = m[9]; mTrans[7] = m[13];
        mTrans[8] = m[2]; mTrans[9] = m[6]; mTrans[10] = m[10]; mTrans[11] = m[14];
        mTrans[12] = m[3]; mTrans[13] = m[7]; mTrans[14] = m[11]; mTrans[15] = m[15];
    }
    
    static void orthoM(float* m, int mOffset, float left, float right, float bottom, float top, float near, float far)
    {
        float a = 2.0f / (right - left);
        float b = 2.0f / (top - bottom);
        float c = -2.0f / (far - near);
        float tx = (right + left) / (right - left);
        float ty = (top + bottom) / (top - bottom);
        float tz = (far + near) / (far - near);
        m[0] = a; m[1] = 0; m[2] = 0; m[3] = tx;
        m[4] = 0; m[5] = b; m[6] = 0; m[7] = ty;
        m[8] = 0; m[9] = 0; m[10] = c; m[11] = tz;
        m[12] = 0; m[13] = 0; m[14] = 0; m[15] = 1;
    }
    
    static void frustumM(float* m, int offset, float left, float right, float bottom, float top, float near, float far)
    {
        float a = 2.0f * near / (right - left);
        float b = 2.0f * near / (top - bottom);
        float c = (right + left) / (right - left);
        float d = (top + bottom) / (top - bottom);
        float e = - (far + near) / (far - near);
        float f = -2.0f * far * near / (far - near);
        m[0] = a; m[1] = 0; m[2] = 0; m[3] = 0;
        m[4] = 0; m[5] = b; m[6] = 0; m[7] = 0;
        m[8] = c; m[9] = d; m[10] = e; m[11] = -1;
        m[12] = 0; m[13] = 0; m[14] = f; m[15] = 1;
    }
    
    static void setLookAtM(float* m, int rmOffset, float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ)
    {
        float zx = eyeX - centerX;
        float zy = eyeY - centerY;
        float zz = eyeZ - centerZ;
        float t = 1.0f/std::sqrt(zx*zx + zy*zy +zz*zz);
        zx *= t;
        zy *= t;
        zz *= t;
        
        float xx = upY*zz - upZ*zy;
        float xy = upZ*zx - upX*zz;
        float xz = upX*zy - upY*zx;
        t = 1.0f/std::sqrt(xx*xx + xy*xy +xz*xz);
        xx *= t;
        xy *= t;
        xz *= t;
        
        float yx = zy*xz - zz*xy;
        float yy = zz*xx - zx*xz;
        float yz = zx*xy - zy*xx;
        t = 1.0f/std::sqrt(yx*yx + yy*yy +yz*yz);
        yx *= t;
        yy *= t;
        yz *= t;
        
        m[0] = xx; m[1] = xy; m[2] = xz; m[3] = 0;
        m[4] = yx; m[5] = yy; m[6] = yz; m[7] = 0;
        m[8] = zx; m[9] = zy; m[10] = zz; m[11] = 0;
        m[12] = 0; m[13] = 0; m[14] = 0; m[15] = 1;
        
        float eyePrime[4];
        eyePrime[0] = -eyeX;
        eyePrime[1] = -eyeY;
        eyePrime[2] = -eyeZ;
        eyePrime[3] = 1;
        
        float tm[16];
        multiplyMV(tm,0,m,0,eyePrime,0);
        m = tm;
        transposeM(tm,0,m,0);
        m = tm;
        m[12] = eyePrime[0];
        m[13] = eyePrime[1];
        m[14] = eyePrime[2];
        m[15] = eyePrime[3];
    }
};

#endif /* defined(__OpenGL2_0Demo__Matrix__) */

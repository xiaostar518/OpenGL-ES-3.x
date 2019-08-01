//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.bn;

public class VectorUtil {
    public VectorUtil() {
    }

    public static float[] getCrossProduct(float var0, float var1, float var2, float var3, float var4, float var5) {
        float var6 = var1 * var5 - var4 * var2;
        float var7 = var2 * var3 - var5 * var0;
        float var8 = var0 * var4 - var3 * var1;
        return new float[]{var6, var7, var8};
    }

    public static float[] vectorNormal(float[] var0) {
        float var1 = (float)Math.sqrt((double)(var0[0] * var0[0] + var0[1] * var0[1] + var0[2] * var0[2]));
        return new float[]{var0[0] / var1, var0[1] / var1, var0[2] / var1};
    }

    public static float mould(float[] var0) {
        return (float)Math.sqrt((double)(var0[0] * var0[0] + var0[1] * var0[1] + var0[2] * var0[2]));
    }
}

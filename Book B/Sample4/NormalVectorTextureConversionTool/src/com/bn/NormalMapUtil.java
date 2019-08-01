//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.bn;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class NormalMapUtil extends JFrame {
    JLabel jls = new JLabel();
    JScrollPane jspz;
    JLabel jlt;
    JScrollPane jspy;
    JSplitPane jsp;
    JFileChooser jfc;
    ImageIcon ii;

    public NormalMapUtil() {
        this.jspz = new JScrollPane(this.jls);
        this.jlt = new JLabel();
        this.jspy = new JScrollPane(this.jlt);
        this.jsp = new JSplitPane(1, this.jspz, this.jspy);
        String var1 = (new File("a")).getAbsolutePath();
        var1 = var1.substring(0, var1.length() - 2);
        this.jfc = new JFileChooser(var1);
        this.ii = this.chooserFile();
        this.jls.setIcon(this.ii);
        this.jls.setVerticalAlignment(0);
        this.jls.setHorizontalAlignment(0);
        this.jlt.setVerticalAlignment(0);
        this.jlt.setHorizontalAlignment(0);
        this.jsp.setDividerLocation(500);
        this.jsp.setDividerSize(4);
        this.add(this.jsp);
        this.setTitle("高度域灰度图转换成法向量纹理图工具");
        this.setBounds(0, 0, 1000, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(3);
        Image var2 = this.process();
        this.jlt.setIcon(new ImageIcon(var2));

        try {
            FileOutputStream var3 = new FileOutputStream("./result/resultnt.jpg");
            System.out.println(((RenderedImage)var2).getColorModel());
            ImageIO.write((RenderedImage)var2, "JPEG", var3);
            var3.flush();
            var3.close();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public ImageIcon chooserFile() {
        int var1 = this.jfc.showOpenDialog(this);
        String var2 = this.jfc.getSelectedFile() != null ? this.jfc.getSelectedFile().getPath() : null;
        return var2 != null && !var2.equals("") ? new ImageIcon(var2) : null;
    }

    public Image process() {
        int var1 = this.ii.getImage().getWidth((ImageObserver)null);
        int var2 = this.ii.getImage().getHeight((ImageObserver)null);
        BufferedImage var3 = new BufferedImage(var1, var2, 2);
        BufferedImage var4 = new BufferedImage(var1, var2, 1);
        Graphics var5 = var3.getGraphics();
        var5.drawImage(this.ii.getImage(), 0, 0, Color.white, (ImageObserver)null);

        for(int var6 = 0; var6 < var2; ++var6) {
            for(int var7 = 0; var7 < var1; ++var7) {
                int var8 = var3.getRGB(var7, var6);
                int var9 = var8 >> 16 & 255;
                int var10 = var8 >> 8 & 255;
                int var11 = var8 & 255;
                float var12 = (float)(var9 + var10 + var11) / 3.0F / 255.0F;
                if (var6 != 0 && var7 != var1 - 1) {
                    int var13 = var3.getRGB(var7, var6 - 1);
                    int var14 = var13 >> 16 & 255;
                    int var15 = var13 >> 8 & 255;
                    int var16 = var13 & 255;
                    float var17 = (float)(var14 + var15 + var16) / 3.0F / 255.0F;
                    int var18 = var3.getRGB(var7 + 1, var6);
                    int var19 = var18 >> 16 & 255;
                    int var20 = var18 >> 8 & 255;
                    int var21 = var18 & 255;
                    float var22 = (float)(var19 + var20 + var21) / 3.0F / 255.0F;
                    float[] var23 = new float[]{1.0F, 0.0F, var17 - var12};
                    float[] var24 = new float[]{0.0F, 1.0F, var22 - var12};
                    float[] var25 = VectorUtil.getCrossProduct(var23[0], var23[1], var23[2] * 4.0F, var24[0], var24[1], var24[2] * 4.0F);
                    var25 = VectorUtil.vectorNormal(var25);
                    int var26 = (int)(var25[0] * 128.0F) + 128;
                    int var27 = (int)(var25[1] * 128.0F) + 128;
                    int var28 = (int)(var25[2] * 128.0F) + 128;
                    var26 = var26 > 255 ? 255 : var26;
                    var27 = var27 > 255 ? 255 : var27;
                    var28 = var28 > 255 ? 255 : var28;
                    int var29 = -16777216;
                    var29 += var26 << 16;
                    var29 += var27 << 8;
                    var29 += var28;
                    var4.setRGB(var7, var6, var29);
                } else {
                    var4.setRGB(var7, var6, -8355585);
                }
            }
        }

        return var4;
    }

    public static void main(String[] var0) {
        new NormalMapUtil();
    }
}

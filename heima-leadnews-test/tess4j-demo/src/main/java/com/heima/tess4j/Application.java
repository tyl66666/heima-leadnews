package com.heima.tess4j;


import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class Application {

    public static void main(String[] args) throws TesseractException {
        //创建实例
        ITesseract tesseract = new Tesseract();

        //设置字体库路径 不能用中文为路径名
        tesseract.setDatapath("D:\\workspace\\tessdata");

        //设置语言 -->简体中文
        tesseract.setLanguage("chi_sim");

        // 图片可以使用中文 最好还是全是中文
        File file = new File("D:\\黑马点评资料\\day04-自媒体文章审核\\资料\\143.png");

        //识别图片
        String result = tesseract.doOCR(file);

        System.out.println("识别的结果为："+result.replaceAll("\\r|\\n","-"));
    }
}

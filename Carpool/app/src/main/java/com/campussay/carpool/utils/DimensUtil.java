package com.campussay.carpool.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * create by zuyuan on 2019/2/20
 *
 * 源文件: values/dimens
 * 目标文件: values-w360dp、values-w390dp、values-w410dp(须保证文件存在)
 * 适配范围: 屏幕最大宽度在360dp~420dp(720px~1440px)的手机
 *
 * dimens标准: 源文件与values-w360dp/dimens保持一致性
 *            values-w390dp/dimens中数值与原数值成 1080/2.75/360 的关系
 *            values-w410dp/dimens中数值与原数值成 1440/3.5/360 的关系
 *
 * sp 和 dp 遵守同一个标准
 */
public final class DimensUtil {

    public static void main(String[] args) {
        startGenerate();
    }

    private static void startGenerate() {
        System.out.println("generate failed ---> start");
        File rootFile =  new File("./app/src/main/res/values/dimens.xml");

        StringBuilder w360dpBuilder = new StringBuilder();
        StringBuilder w390dpBuilder = new StringBuilder();
        StringBuilder w410dpBuilder = new StringBuilder();

        BufferedReader reader = null;
        try {

            File file360 = new File("./app/src/main/res/values-w360dp");
            file360.mkdir();
            File file390 = new File("./app/src/main/res/values-w390dp");
            file390.mkdir();
            File file410 = new File("./app/src/main/res/values-w410dp");
            file410.mkdir();
            reader = new BufferedReader(new FileReader(rootFile));
            String lineStr;

            while ((lineStr = reader.readLine()) != null) {
                if (!lineStr.contains("</dimen>")) {
                    w360dpBuilder.append(lineStr).append("\r\n");
                    w390dpBuilder.append(lineStr).append("\r\n");
                    w410dpBuilder.append(lineStr).append("\r\n");
                } else {
                    String startStr = lineStr.substring(0, lineStr.indexOf(">") + 1);
                    String endStr = lineStr.substring(lineStr.lastIndexOf("<") - 2);

                    int startIndex = lineStr.indexOf(">") + 1;
                    int endIndex = lineStr.indexOf("</") - 2;
                    Float rootValue = Float.parseFloat(lineStr.substring(startIndex, endIndex));

                    w360dpBuilder.append(lineStr).append("\r\n");
                    w390dpBuilder.append(startStr)
                            .append(String.valueOf(
                                    //保留两位小数
                                    Math.round(rootValue * 1080f / 2.75f / 360 * 10f) / 10f))
                            .append(endStr)
                            .append("\r\n");
                    w410dpBuilder.append(startStr)
                            .append(String.valueOf(
                                    //保留两位小数
                                    Math.round(rootValue * 1440f / 3.5f / 360f * 10f) / 10f))
                            .append(endStr)
                            .append("\r\n");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("generate failed ---> file not found!");
        } catch (IOException e) {
            System.out.println("generate failed ---> reader read file error!");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //此时读取数据、生成字符串完成

        String w360dpFilePath = "./app/src/main/res/values-w360dp/dimens.xml";
        String w390dpFilePath = "./app/src/main/res/values-w390dp/dimens.xml";
        String w410dpFilePath = "./app/src/main/res/values-w410dp/dimens.xml";
        writeDimensFile(w360dpFilePath, w360dpBuilder);
        writeDimensFile(w390dpFilePath, w390dpBuilder);
        writeDimensFile(w410dpFilePath, w410dpBuilder);

        System.out.println("generate failed ---> finish!");
    }

    private static void writeDimensFile(String path, StringBuilder stringBuilder) {
        PrintWriter writer = null;
        File file = new File(path);
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(path)));
            writer.println(stringBuilder);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("generate failed ---> print writer error!");
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}

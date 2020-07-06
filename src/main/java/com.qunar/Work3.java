package com.qunar;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @auther lx
 * @create 2020-07-06 20:29
 */
public class Work3 {
    private static File sdxlFile = new File("E:/idea_project/qunarWork/src/main/resources/sdxl.txt");
    private static File sdxlPropFile = new File("C:/Users/lx/Desktop/后端&测试自学作业/attachments/Question 3/sdxl_prop.txt");
    private static File sdxlTemplateFile = new File("C:/Users/lx/Desktop/后端&测试自学作业/attachments/Question 3/sdxl_template.txt");

    public static void main(String[] args) throws IOException {
        //按行读取sdxl_prop.txt
        ImmutableList<String> sdxlProLines = Files.asCharSource(sdxlPropFile, Charsets.UTF_8).readLines();
        //读取sdxl_template.txt文件到字符串sdxlTemplateString中
        String sdxlTemplateString = Files.asCharSource(sdxlTemplateFile, Charsets.UTF_8).read();

        //indexOrderMap保存索引排序的键值对
        Map<Integer, String> indexOrderMap = new HashMap<>();
        //natureOrderList保存文本中的排列顺序
        ArrayList<String> natureOrderList = Lists.newArrayList();
        //charOrderList保存按java的字符排序结果
        ArrayList<String> charOrderList = Lists.newArrayList();

        StringBuilder res = new StringBuilder();

        for (String sdxlProLine : sdxlProLines) {
            String[] s = sdxlProLine.split("\t");

            indexOrderMap.put(Integer.parseInt(s[0]), s[1]);
            natureOrderList.add(s[1]);
            charOrderList.add(s[1]);
        }

        //使用sort方法对集合排序
        Collections.sort(charOrderList);

        for (int i = 0; i < sdxlTemplateString.length(); i++) {
            if (sdxlTemplateString.charAt(i) == '$') {
                //第一种替换规则：$natureOrder(0)
                if (sdxlTemplateString.substring(i + 1, i + 12).equals("natureOrder")) {
                    i = i + 13;//i+12是'('，直接跳过获取括号中的数字
                    String index = String.valueOf(sdxlTemplateString.charAt(i++));
                    while (sdxlTemplateString.charAt(i) != ')') {
                        index += sdxlTemplateString.charAt(i++);
                    }
                    //通过索引查到详细内容替换该形式文字
                    res.append(natureOrderList.get(Integer.parseInt(index)));
                }
                //第二种替换规则：$indexOrder(5722)
                else if (sdxlTemplateString.substring(i + 1, i + 11).equals("indexOrder")) {
                    i = i + 12;//i+11是'('，直接跳过获取括号中的数字
                    String index = String.valueOf(sdxlTemplateString.charAt(i++));
                    while (sdxlTemplateString.charAt(i) != ')') {
                        index += sdxlTemplateString.charAt(i++);
                    }
                    //通过索引查到详细内容替换该形式文字
                    res.append(indexOrderMap.get(Integer.parseInt(index)));
                }
                //第四种替换规则：$charOrderDESC(4177)
                else if (sdxlTemplateString.substring(i + 1, i + 14).equals("charOrderDESC")) {
                    i = i + 15;//i+14是'('，直接跳过获取括号中的数字
                    String index = String.valueOf(sdxlTemplateString.charAt(i++));
                    while (sdxlTemplateString.charAt(i) != ')') {
                        index += sdxlTemplateString.charAt(i++);
                    }
                    //通过索引查到详细内容替换该形式文字
                    res.append(charOrderList.get(
                            charOrderList.size() - 1 - Integer.parseInt(index)));
                }
                //第三种替换规则：$charOrder(4641)
                else if (sdxlTemplateString.substring(i + 1, i + 10).equals("charOrder")) {
                    i = i + 11;//i+10是'('，直接跳过获取括号中的数字
                    String index = String.valueOf(sdxlTemplateString.charAt(i++));
                    while (sdxlTemplateString.charAt(i) != ')') {
                        index += sdxlTemplateString.charAt(i++);
                    }
                    //通过索引查到详细内容替换该形式文字
                    res.append(charOrderList.get(Integer.parseInt(index)));
                }
                //此时i是字符')'的索引位置,for循环结束会进行+1操作
            } else {
                res.append(sdxlTemplateString.charAt(i));
            }

        }

        Files.asCharSink(sdxlFile, Charsets.UTF_8).write(res.toString());
    }
}
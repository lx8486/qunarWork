package com.qunar;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

/**
 * @Description
 * @auther lx
 * @create 2020-07-06 20:21
 */
public class Work2 {
    private static File StringUtilsFile = new File("E:/idea_project/qunarWork/src/main/resources/StringUtils.java");
    private static File validLineCountFile = new File("E:/idea_project/qunarWork/src/main/resources/validLineCount.txt");

    public static void main(String[] args) throws IOException {
        int validLineCount = 0;
        ImmutableList<String> lines = Files.asCharSource(StringUtilsFile, Charsets.UTF_8).readLines();
        for (String line : lines) {
            //判断不是为有效行，主要有以下几种依据：
            //1.该行字符串的非空字符长度为0
            //2.该行第一个非空字符为“*”
            //3.该行前两个非空字符为“/*”（前三个非空字符为“/**”包含在内）
            //4.该行前两个非空字符为“*/”
            //5.该行前两个非空字符为“//”
            //3,4,5点的前提条件为该行非空字符的长度大于1
            if (line.trim().length() == 0 || line.trim().substring(0, 1).equals("*")
                    || (line.trim().length() > 1) && line.trim().substring(0, 2).equals("/*")
                    || (line.trim().length() > 1) && line.trim().substring(0, 2).equals("*/")
                    || (line.trim().length() > 1) && line.trim().substring(0, 2).equals("//"))
                continue;
            validLineCount++;
        }
        System.out.println(validLineCount);
        Files.asCharSink(validLineCountFile, Charsets.UTF_8).write(String.valueOf(validLineCount));
    }
}
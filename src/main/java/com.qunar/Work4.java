package com.qunar;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @Description
 * @auther lx
 * @create 2020-07-06 20:38
 */
public class Work4 {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            try {
                if (command.trim().length() == 0)
                    continue;
                if (!command.contains("|")) {//没有管道操作
                    String[] splitCommands = command.trim().split("\\s+");
                    if (splitCommands[0].equals("cat")) {
                        splitCommands = command.trim().split("\\s+", 2);
                        String read = Files.asCharSource(new File(splitCommands[1]), Charsets.UTF_8).read();
                        System.out.println(read);
                    } else if (splitCommands[0].equals("grep")) {
                        splitCommands = command.trim().split("\\s+", 3);
                        ImmutableList<String> readLines = Files.asCharSource(new File(splitCommands[2]), Charsets.UTF_8).readLines();
                        for (String readLine : readLines) {
                            if (readLine.contains(splitCommands[1]))
                                System.out.println(readLine);
                        }
                    } else if (splitCommands[0].equals("wc")) {
                        splitCommands = command.trim().split("\\s+", 3);
                        if (splitCommands[1].equals("-l")) {
                            ImmutableList<String> readLines = Files.asCharSource(new File(splitCommands[2]), Charsets.UTF_8).readLines();
                            System.out.println(readLines.size());
                        }
                    } else {
                        System.out.println("无效的命令，请重新输入！");
                    }
                } else {//有管道操作
                    String[] pipingCommands = command.split("\\|");
                    String[] splitCommands0 = pipingCommands[0].trim().split("\\s+");
                    if (splitCommands0[0].equals("cat")) {
                        splitCommands0 = pipingCommands[0].trim().split("\\s+", 2);
                        ImmutableList<String> readLines = Files.asCharSource(new File(splitCommands0[1]), Charsets.UTF_8).readLines();
                        if (pipingCommands.length == 2) {//只有一个管道的情况下
                            String[] splitCommands1 = pipingCommands[1].trim().split("\\s+");
                            if (splitCommands1.length == 2 && splitCommands1[0].equals("wc")
                                    && splitCommands1[1].equals("-l")) {
                                System.out.println(readLines.size());
                            } else if (splitCommands1[0].equals("grep")) {
                                splitCommands1 = pipingCommands[1].trim().split("\\s+", 2);
                                for (String readLine : readLines) {
                                    if (readLine.contains(splitCommands1[1]))
                                        System.out.println(readLine);
                                }
                            } else {
                                System.out.println("无效的命令，请重新输入！");
                            }
                        } else if (pipingCommands.length == 3) {//有两个管道的情况
                            String[] splitCommands1 = pipingCommands[1].trim().split("\\s+");
                            String[] splitCommands2 = pipingCommands[2].trim().split("\\s+");
                            if (splitCommands1[0].equals("grep")) {
                                splitCommands1 = pipingCommands[1].trim().split("\\s+" , 2);
                                ArrayList<String> grepList = Lists.newArrayList();
                                for (String readLine : readLines) {
                                    if (readLine.contains(splitCommands1[1]))
                                        grepList.add(readLine);
                                }
    //                            System.out.println(grepList);
                                if (splitCommands2.length == 2 && splitCommands2[0].equals("wc")
                                        && splitCommands2[1].equals("-l")) {
                                    System.out.println(grepList.size());
                                } else {
                                    System.out.println("无效的命令，请重新输入！");
                                }
                            } else {
                                System.out.println("无效的命令，请重新输入！");
                            }
                        }
                    } else {
                        System.out.println("无效的命令，请重新输入！");
                    }
                }
            } catch (IOException e) {
//                e.printStackTrace();
                System.out.println(e);
            }
        }
    }
}
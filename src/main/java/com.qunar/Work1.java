package com.qunar;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.*;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @auther lx
 * @create 2020-07-06 20:08
 */
public class Work1 {
     private static File accessFile = new File("C:/Users/lx/Desktop/后端&测试自学作业/attachments/Question 1/access.log");

    public static void main(String[] args) throws IOException {
        ImmutableList<String> lines = Files.asCharSource(accessFile, Charsets.UTF_8).readLines();
        Multiset<String> uris = HashMultiset.create(lines);

        Map<String, Integer> hashMap = new HashMap<>();

        Multiset<String> wordOccurrences = HashMultiset.create(
                Splitter.on(CharMatcher.WHITESPACE)
                        .trimResults()
                        .omitEmptyStrings()
                        .split(Files.asCharSource(accessFile, Charsets.UTF_8).read()));

        System.out.println("1.请求总量：" + uris.size());

        ImmutableMultiset<String> highestCountFirst = Multisets.copyHighestCountFirst(uris);
        for (Multiset.Entry<String> entry : highestCountFirst.entrySet()) {
            if (hashMap.size() == 10)
                break;
            hashMap.put(entry.getElement(), entry.getCount());
        }

        System.out.println("2.请求最频繁的10个 HTTP 接口，及其相应的请求数量： ");
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            System.out.println("    接口：" + entry.getKey() + "，数量：" + entry.getValue());
        }

        System.out.println("3.POST请求量：" + wordOccurrences.count("POST"));
        System.out.println("  GET请求量：" + wordOccurrences.count("GET"));
    }
}
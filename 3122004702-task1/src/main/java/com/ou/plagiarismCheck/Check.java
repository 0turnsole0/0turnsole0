package com.ou.plagiarismCheck;

import java.io.*;
import java.util.*;

public class Check {
    public static void main(String[] args) {

        // 参数检验
        if (args.length != 3) {
            System.out.println("你输入的参数数量不对噢");
            return;
        }

        // 获取命令行参数
        String origFilePath = args[0];
        String plagiarizedFilePath = args[1];
        String resultFilePath = args[2];

        // 生成每个文件的单词频率映射
        HashMap<String, Integer> origWordFreq = generateWordsFrequency(origFilePath);
        HashMap<String, Integer> plagiarizedWordFreq = generateWordsFrequency(plagiarizedFilePath);

        // 计算两文件的相似度并将结果写入到文件中
        double similarity = calculateSimilarity(origWordFreq, plagiarizedWordFreq);
        writeToFile(resultFilePath, similarity);
    }

    // 生成单词频率映射
    static HashMap<String, Integer> generateWordsFrequency(String filePath) {
        HashMap<String, Integer> wordFreq = new HashMap<String, Integer>();

        try {
            File file = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            // 对文件中的每行进行迭代
            while ((line = reader.readLine()) != null) {
                // 根据空格拆分单词，并将其添加到映射中
                for (String word : line.split("\\s+")) {
                    wordFreq.put(word, wordFreq.getOrDefault(word, 0) + 1);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordFreq;
    }

    // 计算相似度
    static double calculateSimilarity(HashMap<String, Integer> orig, HashMap<String, Integer> plagiarized) {
        double dotProduct = 0.0, origMagnitude = 0.0, plagiarizedMagnitude = 0.0;

        // 计算点积
        for (String word : orig.keySet()) {
            if (plagiarized.containsKey(word)) {
                dotProduct += orig.get(word) * plagiarized.get(word);
            }
        }

        // 计算原始文件单词频率向量的大小（即向量的长度）
        for (int freq : orig.values()) {
            origMagnitude += Math.pow(freq, 2);
        }
        origMagnitude = Math.sqrt(origMagnitude);

        // 计算抄袭文件单词频率向量的大小
        for (int freq : plagiarized.values()) {
            plagiarizedMagnitude += Math.pow(freq, 2);
        }
        plagiarizedMagnitude = Math.sqrt(plagiarizedMagnitude);

        //返回余弦相似度
        if (origMagnitude == 0.0 || plagiarizedMagnitude == 0.0) {
            return 0.0;
        } else {
            return dotProduct / (origMagnitude * plagiarizedMagnitude);
        }
    }

    // 将结果写入文件
    static void writeToFile(String filePath, double similarity) {
        try {
            FileWriter writer = new FileWriter(filePath);
            // 将相似度写入到文件中
            writer.write(String.format("%.2f", similarity));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

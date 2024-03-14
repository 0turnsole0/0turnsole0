package com.ou.plagiarismCheck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;


public class CheckTest {

    @Test
    public void testGenerateWordsFrequency() {
        Check check = new Check();
        HashMap<String, Integer> wordFreq = check.generateWordsFrequency("src/main/resources/txt/1.txt");

        Assertions.assertNotNull(wordFreq);
        Assertions.assertTrue(wordFreq.containsKey(""));
    }

    @Test
    public void testCalculateSimilarity() {
        Check check = new Check();
        HashMap<String, Integer> origWordFreq = check.generateWordsFrequency("src/main/resources/txt/1.txt");
        HashMap<String, Integer> plagiarizedWordFreq = check.generateWordsFrequency("src/main/resources/txt/2.txt");

        double similarity = check.calculateSimilarity(origWordFreq, plagiarizedWordFreq);

        Assertions.assertTrue(similarity >= 0 && similarity <= 1);
    }

    @Test
    public void testWriteToFile() throws Exception {
        Check check = new Check();
        String resultFilePath = "src/main/resources/txt/5.txt";
        check.writeToFile(resultFilePath, 0.95);

        // Verify file exists and is readable, contains expected result
        File file = new File(resultFilePath);
        Assertions.assertTrue(file.exists() && file.canRead());
        BufferedReader reader = new BufferedReader(new FileReader(file));
        double similarity = Double.parseDouble(reader.readLine());
        Assertions.assertEquals(0.95, similarity, 0.01);
    }

}

package com.ou.mathproblemgenerator;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MathProblemGenerator {

    // 定义全局变量以存储不重复的题目和答案
    static Set<String> expressions = new HashSet<>();
    private static List<String> answers = new ArrayList<>();

    public static void mathProblemGenerator(String[] args) {
        // 解析命令行参数
        Map<String, String> params = parseArguments(args);

        if (params.containsKey("-n") && params.containsKey("-r")) {
            // 生成题目和答案
            generateProblemsAndAnswers(Integer.parseInt(params.get("-n")), Integer.parseInt(params.get("-r")));
            saveToFile("Exercises.txt", expressions);
            saveToFile("Answers.txt", answers);
        } else if (params.containsKey("-e") && params.containsKey("-a")) {
            // 校验答案并统计
            checkAndGrade(params.get("-e"), params.get("-a"));
        } else {
            // 显示帮助信息
            System.out.println("Error: Invalid argument(s).");
            showHelp();
        }
    }

    private static void generateProblemsAndAnswers(int problemCount, int range) {
        Random rand = new Random();
        while (expressions.size() < problemCount) {
            String expr = generateSingleProblem(rand, range);
            if (!expressions.contains(expr + " = ")) {  // 确保表达式唯一
                String answer = solveProblem(expr);
                if (answer != null && !isDuplicate(expr, answer)) {
                    expressions.add(expr + " = ");
                    answers.add(answer);
                }
            }
        }
    }

    static boolean isDuplicate(String expr, String answer) {
        // 生成一个可以用来比较的规范化表达式字符串
        String normalizedExpr = normalizeExpression(expr);

        // 遍历已有的表达式列表，检查是否有相同的规范化字符串
        for (String existingExpr : expressions) {
            String existingNormalized = normalizeExpression(existingExpr);
            if (normalizedExpr.equals(existingNormalized)) {
                return true;  // 发现重复
            }
        }

        return false;  // 未发现重复
    }

    static String normalizeExpression(String expr) {
        // 分割表达式成操作数和操作符
        String[] tokens = expr.split(" ");

        // 由于只使用了自然数和基本运算符，规范化只需排序
        Arrays.sort(tokens);

        // 再将排序后的操作数和操作符连接成字符串
        return String.join(" ", tokens);
    }

    static String generateSingleProblem(Random rand, int range) {
        String[] operators = {"+", "-", "*", "/"};
        // 操作数的数量在2到4之间
        int numOfOperands = 2 + rand.nextInt(3);
        // 创建一个包含操作数和操作符的List
        List<String> components = new ArrayList<>();
        for (int i = 0; i < numOfOperands; ++i) {
            components.add(Integer.toString(rand.nextInt(range) + 1));  // 生成操作数
            if (i != numOfOperands - 1) {
                components.add(operators[rand.nextInt(operators.length)]);  // 生成操作符
            }
        }
        return String.join(" ", components);
    }

    static String solveProblem(String expr) {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        try {
            Object result = engine.eval(expr);
            return result.toString();
        } catch (ScriptException e) {
            e.printStackTrace();
            return null;  // 或者您可以返回一些错误信息
        }
    }

    static void saveToFile(String filename, Collection<String> lines) {
        File file = new File(filename);
        try (PrintWriter writer = new PrintWriter(file)) {
            for (String line : lines) {
                writer.println(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: Unable to create or write to file " + filename);
            e.printStackTrace();
        }
    }

    static void checkAndGrade(String exerciseFile, String answerFile) {
        // 存储正确和错误答案的列表
        List<Integer> correctAnswers = new ArrayList<>();
        List<Integer> wrongAnswers = new ArrayList<>();

        try {
            List<String> givenAnswers = Files.readAllLines(Paths.get(answerFile));
            List<String> exercises = Files.readAllLines(Paths.get(exerciseFile));

            for (int i = 0; i < givenAnswers.size(); i++) {
                // 移除题目字符串尾部的等号和可能的空格
                String exercise = exercises.get(i).split(" =")[0];
                String correctAnswer = solveProblem(exercise);
                if (givenAnswers.get(i).equals(correctAnswer)) {
                    correctAnswers.add(i + 1);
                } else {
                    wrongAnswers.add(i + 1);
                }
            }

            // 写入Grade.txt文件
            saveToFile("Grade.txt", Arrays.asList(
                    "Correct: " + correctAnswers.size() +  correctAnswers ,
                    "Wrong: " + wrongAnswers.size() + wrongAnswers
            ));
        } catch (IOException e) {
            System.err.println("Error reading from files.");
            e.printStackTrace();
        }
    }

    static Map<String, String> parseArguments(String[] args) {
        Map<String, String> arguments = new HashMap<>();
        // 遍历args数组
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            // 检查arg是否是一个参数键，即它是否以'-'开头
            if (arg.charAt(0) == '-') {
                // 如果它是一个参数键，我们检查是否还有更多的参数并且下一个参数不是另一个键
                if (i + 1 < args.length && args[i + 1].charAt(0) != '-') {
                    // 将下一个参数作为这个键的值保存
                    arguments.put(arg, args[i + 1]);
                    i++; // 增加i以跳过下一个参数（因为它已经作为当前键的值被处理）
                } else {
                    // 如果下一个参数是另一个参数键或者没有更多的参数，那么我们可以认为这个参数键没有值
                    arguments.put(arg, "");
                }
            }
        }
        return arguments;
    }

    private static void showHelp() {
        // 显示帮助信息
        System.out.println("Usage: java MyApp -n <number> -r <range>");
        System.out.println("       java MyApp -e <exercise_file> -a <answer_file>");
    }
}
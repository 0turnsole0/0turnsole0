package com.ou.mathproblemgenerator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MathProblemGeneratorApplicationTests {

    @Test
    public void mathProblemGenerator() {
        String[] args0 = {"-n", "10", "-r", "1000"};
        MathProblemGenerator.mathProblemGenerator(args0);

        String[] args1 = {"-e", "MyAnswer.txt", "-a", "Answers.txt"};
        MathProblemGenerator.mathProblemGenerator(args1);
    }

    @Test
    public void testCheckAndGrade() {
        MathProblemGenerator.checkAndGrade("MyAnswer.txt", "Answers.txt");
    }

    @Test
    public void testParseArguments() {
        String[] args = new String[]{"-n", "10", "-r", "20"};
        Map<String, String> expected = new HashMap<>();
        expected.put("-n", "10");
        expected.put("-r", "20");

        Map<String, String> actual = MathProblemGenerator.parseArguments(args);

        assertEquals(expected, actual);
    }

    // 假设你的方法是没有副作用的，并且你可以预测输出
    @Test
    public void testGenerateSingleProblem() {
        Random rand = new Random(0); // 使用一个确定的种子以便结果可预测
        String problem = MathProblemGenerator.generateSingleProblem(rand, 10);

        // 由于随机性，这里不能确切知道输出会是什么
        // 但你可以测试表达式的格式是否正确，例如是否包含运算符和操作数
        System.out.println("problem: " + problem);
        assertNotNull(problem);
        assertTrue(problem.contains("+") || problem.contains("-")
                || problem.contains("*") || problem.contains("/"));
    }


    @Test
    public void testIsDuplicate() {
        // 依赖于normalizeExpression的实现
        String expr = "1 + 2 = ";
        String answer = "3";
        MathProblemGenerator.expressions.add("2 + 1 = ");  // 假设有一个已存在的表达式
        assertTrue(MathProblemGenerator.isDuplicate(expr, answer), "Expression should be detected as a duplicate");
    }

    @Test
    public void testNormalizeExpression() {
        String expr = "2 + 3 * 4";
        String normalized = MathProblemGenerator.normalizeExpression(expr);
        assertEquals("* + 2 3 4", normalized, "Normalized expression should have sorted tokens");
    }

    @Test
    public void testSolveProblem() {
        String expression = "4 + 5";
        String expectedAnswer = "9";

        String actualAnswer = MathProblemGenerator.solveProblem(expression);

        System.out.println("expectedAnswer:" + expectedAnswer + " actualAnswer:" + actualAnswer);
        assertEquals(expectedAnswer, actualAnswer);
    }

    // 写入和读取文件通常不是纯函数，可能会被认为是集成测试的一部分
    // 但是为了示例，这里是一个测试saveToFile方法的简单方法
    @Test
    public void testSaveToFile() throws IOException {
        String filename = "test.txt";
        List<String> lines = List.of("line1", "line2", "line3");

        MathProblemGenerator.saveToFile(filename, lines);

        // 读取文件，验证写入是否成功
        List<String> readLines = Files.readAllLines(Paths.get(filename));
        assertArrayEquals(lines.toArray(), readLines.toArray());

        // 清理测试文件
        Files.delete(Paths.get(filename));
    }

}

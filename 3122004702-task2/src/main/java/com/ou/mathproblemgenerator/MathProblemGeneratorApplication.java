package com.ou.mathproblemgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MathProblemGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MathProblemGeneratorApplication.class, args);
        System.setProperty("nashorn.args","--no-deprecation-warning");

        MathProblemGenerator.mathProblemGenerator(args);
    }

}

package com.kob.botrunningsystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        
        try {
            String dockerCommand = String.format("docker run my-java-app");
//            System.out.println(dockerCommand);
            Runtime.getRuntime().exec(dockerCommand);
            Process process = new ProcessBuilder("docker", "run", "my-java-app").start();

            // 读取容器输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待容器执行完毕
            int exitCode = process.waitFor();
            System.out.println("Container exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println("test:"+args[0]);
    }
}


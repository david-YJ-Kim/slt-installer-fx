package com.tsh.slt.installer.bizService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExecuteService {


    public static void main(String[] args) {

        String filePath = "C:\\Users\\tspsc\\AppData\\Local\\sellter-service\\v1.0.0\\bin\\sample_run.bat";

        CommandExecuteService service = CommandExecuteService.getInstance();
        service.executeWinScript(filePath, true);

        System.out.println("Finish execute file.");
    }

    private static CommandExecuteService instance;

    public static CommandExecuteService getInstance(){
        if( instance == null){
            instance = new CommandExecuteService();
        }
        return instance;
    }




    /**
     * 스크립트 파일(BAT 또는 VBS)을 실행하는 메소드
     * @param scriptPath 스크립트 파일 경로
     * @param isSync true이면 프로세스 완료까지 대기, false이면 비동기 실행
     * @return 실행 성공 여부
     */
    private boolean executeWinScript(String scriptPath, boolean isSync) {
        try {
            ProcessBuilder processBuilder;

            // 파일 확장자에 따라 적절한 실행기 선택
            if (scriptPath.toLowerCase().endsWith(".vbs")) {
                processBuilder = new ProcessBuilder("wscript.exe", scriptPath);
            } else if (scriptPath.toLowerCase().endsWith(".bat") ||
                    scriptPath.toLowerCase().endsWith(".cmd")) {
                processBuilder = new ProcessBuilder("cmd.exe", "/c", scriptPath);
            } else {
                return false;
            }

            // 작업 디렉토리 설정 (선택사항)
            // File scriptFile = new File(scriptPath);
            // processBuilder.directory(scriptFile.getParentFile());

            Process process = processBuilder.start();

            // 비동기 실행인 경우 별도 스레드에서 출력 처리
            if (!isSync) {
                new Thread(() -> {
                    try {
                        processOutputAsync(process);
                    } catch (IOException e) {
                        System.err.println("비동기 출력 처리 중 오류: " + e.getMessage());
                    }
                }).start();

                // 비동기 실행 시 즉시 성공 반환
                return true;
            }

            // 동기식 실행인 경우 출력 처리 및 완료 대기
            processOutput(process);

            int exitCode = process.waitFor();

            return exitCode == 0;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 프로세스 출력을 동기식으로 처리하는 메소드
     * @param process 실행된 프로세스
     * @throws IOException 입출력 예외
     */
    private void processOutput(Process process) throws IOException {
        // 표준 출력 처리
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("스크립트 출력: " + line);
            }
        }

        // 오류 출력 처리
        try (BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = errorReader.readLine()) != null) {
                System.err.println("스크립트 오류: " + line);
            }
        }
    }

    /**
     * 프로세스 출력을 비동기식으로 처리하는 메소드
     * @param process 실행된 프로세스
     * @throws IOException 입출력 예외
     */
    private void processOutputAsync(Process process) throws IOException {
        // 표준 출력을 별도 스레드에서 처리
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    final String output = line;
                    // UI 스레드에서 안전하게 처리
                    javafx.application.Platform.runLater(() -> {
                        System.out.println("스크립트 출력: " + output);
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // 오류 출력을 별도 스레드에서 처리
        new Thread(() -> {
            try (BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    final String error = line;
                    // UI 스레드에서 안전하게 처리
                    javafx.application.Platform.runLater(() -> {
                        System.err.println("스크립트 오류: " + error);
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // 프로세스 종료 시 이벤트 처리 (선택사항)
        new Thread(() -> {
            try {
                int exitCode = process.waitFor();
                javafx.application.Platform.runLater(() -> {
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}

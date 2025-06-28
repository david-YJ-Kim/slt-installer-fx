package com.tsh.slt.installer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * JDK8에서 BAT 파일과 VBS 파일을 실행하는 유틸리티 클래스
 */
public class WinScriptExecutor {

    static final Logger log = LoggerFactory.getLogger(WinScriptExecutor.class);

    private static final int DEFAULT_TIMEOUT_SECONDS = 60;

    /**
     * BAT 파일 실행
     * @param batFilePath BAT 파일 경로
     * @return 실행 결과
     */
    public static ExecutionResult executeBatFile(String batFilePath) {
        return executeBatFile(batFilePath, DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * BAT 파일 실행 (타임아웃 설정)
     * @param batFilePath BAT 파일 경로
     * @param timeoutSeconds 타임아웃 (초)
     * @return 실행 결과
     */
    public static ExecutionResult executeBatFile(String batFilePath, int timeoutSeconds) {
        if (!isValidFile(batFilePath, ".bat")) {
            return new ExecutionResult(false, -1, "", "유효하지 않은 BAT 파일입니다: " + batFilePath);
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", batFilePath);
            return executeProcess(processBuilder, timeoutSeconds);
        } catch (Exception e) {
            return new ExecutionResult(false, -1, "", "BAT 파일 실행 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * VBS 파일 실행
     * @param vbsFilePath VBS 파일 경로
     * @return 실행 결과
     */
    public static ExecutionResult executeVbsFile(String vbsFilePath) {
        return executeVbsFile(vbsFilePath, DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * VBS 파일 실행 (타임아웃 설정)
     * @param vbsFilePath VBS 파일 경로
     * @param timeoutSeconds 타임아웃 (초)
     * @return 실행 결과
     */
    public static ExecutionResult executeVbsFile(String vbsFilePath, int timeoutSeconds) {
        if (!isValidFile(vbsFilePath, ".vbs")) {
            return new ExecutionResult(false, -1, "", "유효하지 않은 VBS 파일입니다: " + vbsFilePath);
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cscript.exe", "//NoLogo", vbsFilePath);
            return executeProcess(processBuilder, timeoutSeconds);
        } catch (Exception e) {
            return new ExecutionResult(false, -1, "", "VBS 파일 실행 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 작업 디렉토리를 지정하여 BAT 파일 실행
     * @param batFilePath BAT 파일 경로
     * @param workingDirectory 작업 디렉토리
     * @return 실행 결과
     */
    public static ExecutionResult executeBatFileWithWorkingDir(String batFilePath, String workingDirectory) {
        return executeBatFileWithWorkingDir(batFilePath, workingDirectory, DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * 작업 디렉토리를 지정하여 BAT 파일 실행 (타임아웃 설정)
     * @param batFilePath BAT 파일 경로
     * @param workingDirectory 작업 디렉토리
     * @param timeoutSeconds 타임아웃 (초)
     * @return 실행 결과
     */
    public static ExecutionResult executeBatFileWithWorkingDir(String batFilePath, String workingDirectory, int timeoutSeconds) {
        if (!isValidFile(batFilePath, ".bat")) {
            return new ExecutionResult(false, -1, "", "유효하지 않은 BAT 파일입니다: " + batFilePath);
        }

        if (!isValidDirectory(workingDirectory)) {
            return new ExecutionResult(false, -1, "", "유효하지 않은 작업 디렉토리입니다: " + workingDirectory);
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", batFilePath);
            processBuilder.directory(new File(workingDirectory));
            return executeProcess(processBuilder, timeoutSeconds);
        } catch (Exception e) {
            return new ExecutionResult(false, -1, "", "BAT 파일 실행 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 프로세스 실행 및 결과 반환
     * @param processBuilder ProcessBuilder 객체
     * @param timeoutSeconds 타임아웃 (초)
     * @return 실행 결과
     */
    private static ExecutionResult executeProcess(ProcessBuilder processBuilder, int timeoutSeconds) {
        Process process = null;
        try {
            // 표준 출력과 에러 출력을 분리하여 처리
            processBuilder.redirectErrorStream(false);

            process = processBuilder.start();

            // 출력 스트림 읽기
            String output = readInputStream(process.getInputStream());
            String errorOutput = readInputStream(process.getErrorStream());

            // 프로세스 완료 대기 (타임아웃 설정)
            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);

            if (!finished) {
                process.destroyForcibly();
                return new ExecutionResult(false, -1, output, "프로세스 실행 시간이 초과되었습니다 (" + timeoutSeconds + "초)");
            }

            int exitCode = process.exitValue();
            boolean success = (exitCode == 0);

            return new ExecutionResult(success, exitCode, output, errorOutput);

        } catch (Exception e) {
            if (process != null) {
                process.destroyForcibly();
            }
            return new ExecutionResult(false, -1, "", "프로세스 실행 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * InputStream을 문자열로 읽기
     * @param inputStream 입력 스트림
     * @return 읽은 문자열
     */
    private static String readInputStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "MS949"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            return "스트림 읽기 오류: " + e.getMessage();
        }
        return output.toString();
    }

    /**
     * 파일 유효성 검증
     * @param filePath 파일 경로
     * @param expectedExtension 예상 확장자
     * @return 유효 여부
     */
    private static boolean isValidFile(String filePath, String expectedExtension) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }

        try {
            Path path = Paths.get(filePath);
            return Files.exists(path) &&
                    Files.isRegularFile(path) &&
                    filePath.toLowerCase().endsWith(expectedExtension.toLowerCase());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 디렉토리 유효성 검증
     * @param directoryPath 디렉토리 경로
     * @return 유효 여부
     */
    private static boolean isValidDirectory(String directoryPath) {
        if (directoryPath == null || directoryPath.trim().isEmpty()) {
            return false;
        }

        try {
            Path path = Paths.get(directoryPath);
            return Files.exists(path) && Files.isDirectory(path);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 실행 결과를 담는 클래스
     */
    public static class ExecutionResult {
        private final boolean success;
        private final int exitCode;
        private final String output;
        private final String errorOutput;

        public ExecutionResult(boolean success, int exitCode, String output, String errorOutput) {
            this.success = success;
            this.exitCode = exitCode;
            this.output = output != null ? output : "";
            this.errorOutput = errorOutput != null ? errorOutput : "";
        }

        public boolean isSuccess() {
            return success;
        }

        public int getExitCode() {
            return exitCode;
        }

        public String getOutput() {
            return output;
        }

        public String getErrorOutput() {
            return errorOutput;
        }

        @Override
        public String toString() {
            return "ExecutionResult{" +
                    "success=" + success +
                    ", exitCode=" + exitCode +
                    ", output='" + output + '\'' +
                    ", errorOutput='" + errorOutput + '\'' +
                    '}';
        }
    }

    // 사용 예시를 위한 메인 메서드
    public static void main(String[] args) {
        // BAT 파일 실행 예시
        ExecutionResult batResult = WinScriptExecutor.executeBatFile("C:\\test\\example.bat");
        System.out.println("BAT 실행 결과:");
        System.out.println("성공 여부: " + batResult.isSuccess());
        System.out.println("종료 코드: " + batResult.getExitCode());
        System.out.println("출력: " + batResult.getOutput());
        System.out.println("에러: " + batResult.getErrorOutput());

        System.out.println("=====================================");

        // VBS 파일 실행 예시
        ExecutionResult vbsResult = WinScriptExecutor.executeVbsFile("C:\\test\\example.vbs");
        System.out.println("VBS 실행 결과:");
        System.out.println("성공 여부: " + vbsResult.isSuccess());
        System.out.println("종료 코드: " + vbsResult.getExitCode());
        System.out.println("출력: " + vbsResult.getOutput());
        System.out.println("에러: " + vbsResult.getErrorOutput());

        System.out.println("=====================================");

        // 작업 디렉토리 지정하여 BAT 파일 실행 예시
        ExecutionResult batWithDirResult = WinScriptExecutor.executeBatFileWithWorkingDir(
                "example.bat", "C:\\test\\", 30);
        System.out.println("작업 디렉토리를 지정한 BAT 실행 결과:");
        System.out.println("성공 여부: " + batWithDirResult.isSuccess());
        System.out.println("출력: " + batWithDirResult.getOutput());
    }
}
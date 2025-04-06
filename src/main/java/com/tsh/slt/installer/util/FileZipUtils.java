package com.tsh.slt.installer.util;

import java.io.*;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * ZIP 파일 압축 해제를 위한 유틸리티 클래스
 */
public class FileZipUtils {

    /**
     * ZIP 파일을 지정된 디렉토리에 압축 해제합니다.
     *
     * @param zipFilePath ZIP 파일의 전체 경로 (디렉토리 경로 + 파일명 포함, 예: "C:/downloads/myfile.zip")
     * @param destDirectory 압축 해제될 대상 디렉토리 경로
     * @throws IOException 파일 처리 중 오류 발생 시
     */
    public static void unzip(String zipFilePath, String destDirectory) throws IOException {
        System.out.println("압축 해제 시작: " + zipFilePath + " -> " + destDirectory);

        // 1. 대상 디렉토리 확인 및 생성
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            boolean created = destDir.mkdirs();
            System.out.println("대상 디렉토리 생성 결과: " + created + " (경로: " + destDir.getAbsolutePath() + ")");
        } else {
            System.out.println("대상 디렉토리가 이미 존재함: " + destDir.getAbsolutePath());
        }

        // 2. ZIP 파일 존재 확인
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists() || !zipFile.isFile()) {
            throw new FileNotFoundException("ZIP 파일을 찾을 수 없습니다: " + zipFilePath);
        }
        System.out.println("ZIP 파일 크기: " + zipFile.length() + " 바이트");

        // 3. ZIP 파일 열기
        try (ZipFile zip = new ZipFile(zipFilePath)) {
            System.out.println("ZIP 파일 항목 수: " + zip.size());
            Enumeration<? extends ZipEntry> entries = zip.entries();

            int count = 0;
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File entryDestination = new File(destDir, entry.getName());

                System.out.println("항목 " + (++count) + ": " + entry.getName() +
                        " (디렉토리: " + entry.isDirectory() +
                        ", 크기: " + entry.getSize() + " 바이트)");

                // 디렉토리인 경우 생성
                if (entry.isDirectory()) {
                    boolean created = entryDestination.mkdirs();
                    System.out.println("  - 디렉토리 생성 결과: " + created + " (경로: " + entryDestination.getAbsolutePath() + ")");
                } else {
                    // 상위 디렉토리가 없는 경우 생성
                    if (!entryDestination.getParentFile().exists()) {
                        boolean created = entryDestination.getParentFile().mkdirs();
                        System.out.println("  - 상위 디렉토리 생성 결과: " + created +
                                " (경로: " + entryDestination.getParentFile().getAbsolutePath() + ")");
                    }

                    // 파일 복사
                    try (InputStream in = zip.getInputStream(entry);
                         OutputStream out = new FileOutputStream(entryDestination)) {

                        byte[] buffer = new byte[1024];
                        int len;
                        long totalBytes = 0;

                        while ((len = in.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                            totalBytes += len;
                        }

                        System.out.println("  - 파일 복사 완료: " + totalBytes + " 바이트");

                        // 파일 존재 확인
                        if (entryDestination.exists() && entryDestination.length() > 0) {
                            System.out.println("  - 파일 생성 확인: " + entryDestination.getAbsolutePath() +
                                    " (" + entryDestination.length() + " 바이트)");
                        } else {
                            System.out.println("  - 경고: 파일이 생성되지 않았거나 크기가 0입니다.");
                        }
                    }
                }
            }

            System.out.println("압축 해제 완료: " + count + "개 항목 처리됨");
        }
    }

    /**
     * 테스트 메인 메소드
     */
    public static void main(String[] args) {

        String zipFilePath = "C:\\Users\\tspsc\\AppData\\Local\\sellter-service\\v1.0.0\\bin\\java-1.8.0-openjdk-1.8.0.332-1.b09.ojdkbuild.windows.x86_64.zip";
        String destDirectory = "C:\\Users\\tspsc\\AppData\\Local\\sellter-service\\v1.0.0\\jdk";

        try {
            System.out.println("===== ZIP 압축 해제 테스트 시작 =====");
            System.out.println("파일 경로: " + zipFilePath);
            System.out.println("대상 디렉토리: " + destDirectory);

            // ZIP 파일 확인
            File zipFile = new File(zipFilePath);
            if (!zipFile.exists()) {
                System.err.println("오류: ZIP 파일이 존재하지 않습니다!");
                return;
            }

            if (!zipFile.isFile()) {
                System.err.println("오류: 지정된 경로는 파일이 아닙니다!");
                return;
            }

            if (!zipFile.canRead()) {
                System.err.println("오류: ZIP 파일을 읽을 수 없습니다!");
                return;
            }

            System.out.println("ZIP 파일 정보: " + zipFile.getName() +
                    " (" + zipFile.length() + " 바이트)");

            // 압축 해제 실행
            long startTime = System.currentTimeMillis();
            unzip(zipFilePath, destDirectory);
            long endTime = System.currentTimeMillis();

            System.out.println("압축 해제 완료!");
            System.out.println("소요 시간: " + (endTime - startTime) + "ms");

            // 압축 해제된 파일 확인
            File destDir = new File(destDirectory);
            File[] extractedFiles = destDir.listFiles();
            if (extractedFiles != null && extractedFiles.length > 0) {
                System.out.println("압축 해제된 파일/디렉토리 목록:");
                printFileStructure(destDir, "");
            } else {
                System.out.println("압축 해제된 파일이 없거나 목록을 가져올 수 없습니다.");
            }

            System.out.println("===== ZIP 압축 해제 테스트 종료 =====");

        } catch (Exception e) {
            System.err.println("압축 해제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 디렉토리 구조를 재귀적으로 출력하는 도우미 메소드
     *
     * @param dir 출력할 디렉토리
     * @param indent 들여쓰기 문자열
     */
    private static void printFileStructure(File dir, String indent) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            System.out.println(indent + " - " + file.getName() +
                    (file.isDirectory() ? " (디렉토리)" : " (파일, " + file.length() + " 바이트)"));

            if (file.isDirectory()) {
                printFileStructure(file, indent + "    ");
            }
        }
    }
}
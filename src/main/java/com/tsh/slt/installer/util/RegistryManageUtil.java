package com.tsh.slt.installer.util;


import java.io.IOException;
import java.io.File;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

/**
 * Windows 레지스트리를 통해 VBS 파일을 시작 프로그램으로 등록하는 유틸리티 클래스
 */
public class RegistryManageUtil {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\tspsc\\AppData\\Local\\sellter-service\\v1.0.0\\bin\\sample_run.bat";

        RegistryManageUtil.registerVbsToRegistry(filePath, "slt");

    }

    // 시작 프로그램 레지스트리 경로
    private static final String RUN_KEY_PATH = "Software\\Microsoft\\Windows\\CurrentVersion\\Run";

    /**
     * 레지스트리를 통해 VBS 파일을 시작 프로그램으로 등록합니다 (JNA 라이브러리 사용).
     *
     * @param vbsFilePath 등록할 VBS 파일의 전체 경로
     * @param appName 애플리케이션 이름 (레지스트리 키로 사용됨)
     * @return 등록 성공 여부
     */
    public static boolean registerVbsToRegistry(String vbsFilePath, String appName) {
        try {
            // 파일 존재 여부 확인
            File vbsFile = new File(vbsFilePath);
            if (!vbsFile.exists() || !vbsFile.isFile()) {
                System.err.println("VBS 파일을 찾을 수 없습니다: " + vbsFilePath);
                return false;
            }

            // 레지스트리 키 값으로 등록할 명령어 생성 (wscript.exe를 사용하여 VBS 실행)
            String command = "wscript.exe \"" + vbsFile.getAbsolutePath() + "\"";

            // JNA를 사용하여 레지스트리에 등록
            Advapi32Util.registrySetStringValue(
                    WinReg.HKEY_CURRENT_USER,
                    RUN_KEY_PATH,
                    appName,
                    command
            );

            System.out.println("시작 프로그램 등록 완료: " + appName);
            return true;
        } catch (Exception e) {
            System.err.println("레지스트리 등록 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 레지스트리에서 시작 프로그램 등록을 제거합니다.
     *
     * @param appName 제거할 애플리케이션 이름 (레지스트리 키)
     * @return 제거 성공 여부
     */
    public static boolean unregisterFromRegistry(String appName) {
        try {
            // 해당 키가 존재하는지 확인
            if (Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, RUN_KEY_PATH, appName)) {
                // 키 삭제
                Advapi32Util.registryDeleteValue(
                        WinReg.HKEY_CURRENT_USER,
                        RUN_KEY_PATH,
                        appName
                );

                System.out.println("시작 프로그램 등록 제거 완료: " + appName);
                return true;
            } else {
                System.out.println("등록된 시작 프로그램을 찾을 수 없습니다: " + appName);
                return false;
            }
        } catch (Exception e) {
            System.err.println("레지스트리 제거 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * reg.exe 명령을 사용하여 VBS 파일을 시작 프로그램으로 등록합니다 (JNA 없이 실행).
     *
     * @param vbsFilePath 등록할 VBS 파일의 전체 경로
     * @param appName 애플리케이션 이름 (레지스트리 키로 사용됨)
     * @return 등록 성공 여부
     */
    public static boolean registerVbsUsingRegExe(String vbsFilePath, String appName) {
        try {
            // 파일 존재 여부 확인
            File vbsFile = new File(vbsFilePath);
            if (!vbsFile.exists() || !vbsFile.isFile()) {
                System.err.println("VBS 파일을 찾을 수 없습니다: " + vbsFilePath);
                return false;
            }

            // 레지스트리 키 값으로 등록할 명령어 생성 (wscript.exe를 사용하여 VBS 실행)
            String command = "wscript.exe \"" + vbsFile.getAbsolutePath() + "\"";

            // reg.exe 명령 생성
            String regCommand = "reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run\" /v \""
                    + appName + "\" /t REG_SZ /d \"" + command + "\" /f";

            // 명령 실행
            Process process = Runtime.getRuntime().exec(regCommand);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("시작 프로그램 등록 완료: " + appName);
                return true;
            } else {
                System.err.println("reg.exe 실행 중 오류 발생. 종료 코드: " + exitCode);
                return false;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("레지스트리 등록 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * reg.exe 명령을 사용하여 시작 프로그램 등록을 제거합니다 (JNA 없이 실행).
     *
     * @param appName 제거할 애플리케이션 이름 (레지스트리 키)
     * @return 제거 성공 여부
     */
    public static boolean unregisterUsingRegExe(String appName) {
        try {
            // reg.exe 명령 생성
            String regCommand = "reg delete \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run\" /v \""
                    + appName + "\" /f";

            // 명령 실행
            Process process = Runtime.getRuntime().exec(regCommand);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("시작 프로그램 등록 제거 완료: " + appName);
                return true;
            } else {
                System.err.println("reg.exe 실행 중 오류 발생. 종료 코드: " + exitCode);
                return false;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("레지스트리 제거 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 현재 등록된 시작 프로그램인지 확인합니다.
     *
     * @param appName 확인할 애플리케이션 이름 (레지스트리 키)
     * @return 등록되어 있는지 여부
     */
    public static boolean isRegisteredInStartup(String appName) {
        try {
            return Advapi32Util.registryValueExists(
                    WinReg.HKEY_CURRENT_USER,
                    RUN_KEY_PATH,
                    appName
            );
        } catch (Exception e) {
            System.err.println("레지스트리 확인 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
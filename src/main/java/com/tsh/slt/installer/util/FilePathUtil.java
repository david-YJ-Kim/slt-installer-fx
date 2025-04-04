package com.tsh.slt.installer.util;


import com.tsh.slt.installer.enums.DownloadFileTypes;
import com.tsh.slt.installer.enums.SrvDeployFileName;

import java.io.File;

/**
 * 파일 경로와 관련된 공통 유틸리티 함수 모음
 */
public class FilePathUtil {


    static final String serviceDirName = "sellter-service";
    static final String userHomeProperty = "user.home";



    /**
     * 서비스 루트 폴더 생성 및 경로 리턴
     * @param isWindow
     * @return
     * @throws Exception
     */
    public static String getServiceRootPath(boolean isWindow, String version) throws Exception {

        String serviceBasePath = "";

        if(isWindow){

            String localAppData = FilePathUtil.getLocalAppDataPath();
            serviceBasePath = localAppData + File.separator + serviceDirName + File.separator + version;

        }

        // File created if not exist
        if(!FilePathUtil.createDirectoryIfNotExists(serviceBasePath)){
            throw new Exception("File not created.");
        }

        return serviceBasePath;
    }


    /**
     * 서비스 배포를 위한 기초 폴더 생성
     * @param isWindow
     * @throws Exception
     */
    public static void createServiceDeployDir(boolean isWindow, String version) throws Exception {

        String srvRootPath = FilePathUtil.getServiceRootPath(isWindow, version);

        // values() 메서드를 사용하여 모든 enum 상수 배열 가져오기
        SrvDeployFileName[] values = SrvDeployFileName.values();


        for (SrvDeployFileName folder : SrvDeployFileName.values()) {
            String filePath = srvRootPath + File.separator + folder.name();
            FilePathUtil.createDirectoryIfNotExists(filePath);
        }

    }

    /**
     * 윈도우 PC 전용, AppData\Local 경로 획득
     * @return C:\Users\tspsc\AppData\Local
     */
    public static String getLocalAppDataPath(){

        String userHome = System.getProperty(userHomeProperty);
        return userHome + "\\AppData\\Local";


    }



    /**
     * 다운로드 파일 타입별 firebase 경로 획득
     * @param type
     * @return
     */
    public static String getFirebaseFilePath(DownloadFileTypes type, String version){

        String jarFileName = "stl.agent-0.0.1-SNAPSHOT.jar";
        String ymlFileName = "application.yml";
        String jdkZipFileName = "java-1.8.0-openjdk-1.8.0.332-1.b09.ojdkbuild.windows.x86_64.zip";
        String vbsFileName = "run_vbs.vbs";
        String runBatFileName = "sample_run.txt";
        String addBatFileName = "addStartProgream.txt";

        String basePath = "installer/agent/deploy/" + version + "/";

        switch (type){
            case jar:
                return basePath + "jar/" + jarFileName;
            case yml:
                return basePath + "jar/" + ymlFileName;
            case jdk:
                return basePath + "jdk/" + jdkZipFileName;
            case vbs:
                return basePath + "scripts/win/" + vbsFileName;
            case runBat:
                return basePath + "scripts/win/" + runBatFileName;
            case addBat:
                return basePath + "scripts/win/" + addBatFileName;
            default:
                throw new IllegalArgumentException();
        }

    }


    /**
     * 다운로드 파일 타입별 로컬 폴더에 저장할 이름
     * @param type
     * @return
     */
    public static String getLocalDownloadFilePath(DownloadFileTypes type, boolean isWindow, String version) throws Exception {

        String jarFileName = "stl.agent-0.0.1-SNAPSHOT.jar";
        String ymlFileName = "application.yml";
        String jdkZipFileName = "java-1.8.0-openjdk-1.8.0.332-1.b09.ojdkbuild.windows.x86_64.zip";
        String vbsFileName = "run_vbs.vbs";
        String runBatFileName = "sample_run.bat";
        String addBatFileName = "addStartProgream.bat";

        String basePath = FilePathUtil.getServiceRootPath(isWindow, version);
        String binPath = basePath + File.separator + "bin";

        switch (type){
            case jar:
                String targetPath = basePath + File.separator + "target";
                if(!FilePathUtil.createDirectoryIfNotExists(targetPath)) throw new Exception("Target File not created.");

                return targetPath + File.separator + jarFileName;

            case yml:
                String confPath = basePath + File.separator + "conf";
                if(!FilePathUtil.createDirectoryIfNotExists(confPath)) throw new Exception("Conf File not created.");

                return confPath  + File.separator + ymlFileName;

            case jdk:
                if(!FilePathUtil.createDirectoryIfNotExists(binPath)) throw new Exception("Conf File not created.");

                return binPath  + File.separator + jdkZipFileName;

            case vbs:
                if(!FilePathUtil.createDirectoryIfNotExists(binPath)) throw new Exception("Conf File not created.");
                return binPath  + File.separator + vbsFileName;

            case runBat:
                if(!FilePathUtil.createDirectoryIfNotExists(binPath)) throw new Exception("Conf File not created.");
                return binPath  + File.separator + runBatFileName;

            case addBat:
                if(!FilePathUtil.createDirectoryIfNotExists(binPath)) throw new Exception("Conf File not created.");
                return binPath  + File.separator + addBatFileName;

            default:
                throw new IllegalArgumentException();
        }

    }

    /**
     * 지정된 경로에 폴더가 없는 경우 폴더를 생성합니다.
     *
     * @param directoryPath 생성할 폴더 경로
     * @return 폴더 생성 성공 여부 (이미 존재하는 경우에도 true 반환)
     */
    public static boolean createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);

        // 이미 폴더가 존재하는 경우
        if (directory.exists()) {
            // 파일이 아니라 폴더인지 확인
            if (directory.isDirectory()) {
                return true;
            } else {
                System.err.println("경로가 이미 파일로 존재합니다: " + directoryPath);
                return false;
            }
        }

        // 폴더가 존재하지 않는 경우 생성 (상위 폴더 포함)
        boolean created = directory.mkdirs();

        if (created) {
            System.out.println("폴더 생성 완료: " + directoryPath);
        } else {
            System.err.println("폴더 생성 실패: " + directoryPath);
        }

        return created;
    }


    public static void createDeployDirectory(){

    }
    
    
}

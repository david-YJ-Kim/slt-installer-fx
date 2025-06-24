package com.tsh.slt.installer.util;


import com.tsh.slt.installer.enums.DownloadFileTypes;
import com.tsh.slt.installer.enums.CompanyCommonFileName;
import com.tsh.slt.installer.enums.CompanyCommonUtilFileName;
import com.tsh.slt.installer.enums.SrvDeployFileName;
import com.tsh.slt.installer.vo.ServiceDeployInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 파일 경로와 관련된 공통 유틸리티 함수 모음
 */
public class FilePathUtil {

    private final static Logger log = LoggerFactory.getLogger(FilePathUtil.class);


    static final String serviceDirName = "sellter-service";
    static final String userHomeProperty = "user.home";


    /**
     * 서비스에 필요한 폴더를 생성 
     * SrvCommonFileName 에 정의된 폴더
     * @param isWindow
     * @return
     * @throws Exception
     */
    @Deprecated
    public static boolean createCommonDir(boolean isWindow, String serviceName) throws Exception {

        String rootPath = FilePathUtil.getServiceRootPath(isWindow, serviceName);
        return FilePathUtil.createDirectories(rootPath, CompanyCommonFileName.class);
    }

    /**
     * Util에 필요한 폴더를 생성
     * SrvCommonFileName 에 정의된 폴더
     * @param isWindow
     * @return AppData/Local/serviceNm/util
     * @throws Exception
     */
    @Deprecated
    public static String createUtilCommonDir(boolean isWindow, String serviceName) throws Exception {

        String rootPath = FilePathUtil.getServiceRootPath(isWindow, serviceName) + File.separator + CompanyCommonFileName.util;
        File directory = new File(rootPath);

        if(!directory.exists()) {
            FilePathUtil.createCommonDir(true, serviceName);
        }

        boolean result = FilePathUtil.createDirectories(rootPath, CompanyCommonUtilFileName.class);
        if(!result) throw new Exception("Fail to create file");

        return rootPath;

    }


    /**
     * Logs에 필요한 폴더를 생성
     * @param isWindow
     * @return 생성된 Path 리턴 (AppData/Local/serviceNm/logs/v.1.0.0)
     * @throws Exception
     */
    @Deprecated
    public static String createLogsDir(boolean isWindow, ServiceDeployInfoDto dto) throws Exception {

        String rootPath = FilePathUtil.getServiceRootPath(isWindow, "serviceName") + File.separator + CompanyCommonFileName.logs;
        File directory = new File(rootPath);

        if(!directory.exists()) {
            FilePathUtil.createCommonDir(true, "serviceName");
        }

        String createdPath = rootPath + File.separator + dto.getProdId() + File.separator + dto.getVersion();

        boolean result = FilePathUtil.createDirectoryIfNotExists(createdPath);
        if(!result) throw new Exception("Fail to create file");

        return createdPath;
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
     * @param productBaseDir     AppData/Local/serviceNm/product/agent/v1.0.0
     * @param utilBaseDir        AppData/Local/serviceNm/util
     * @param type
     * @return
     */
    public static String getLocalDownloadFilePath(String productBaseDir, String utilBaseDir, DownloadFileTypes type, boolean isWindow, String version) throws Exception {

        String jarFileName = "stl.agent-0.0.1-SNAPSHOT.jar";
        String ymlFileName = "application.yml";
        String jdkZipFileName = "java-1.8.0-openjdk-1.8.0.332-1.b09.ojdkbuild.windows.x86_64.zip";
        String vbsFileName = "run_vbs.vbs";
        String runBatFileName = "sample_run.bat";
        String addBatFileName = "addStartProgream.bat";

        String binPath = productBaseDir + File.separator + SrvDeployFileName.bin;

        switch (type){
            case jar:
                String targetPath = productBaseDir + File.separator + SrvDeployFileName.target;
                if(!FilePathUtil.createDirectoryIfNotExists(targetPath)) throw new Exception("Target File not created.");

                return targetPath + File.separator + jarFileName;

            case yml:
                String confPath = productBaseDir + File.separator + SrvDeployFileName.conf;
                if(!FilePathUtil.createDirectoryIfNotExists(confPath)) throw new Exception("Conf File not created.");

                return confPath  + File.separator + ymlFileName;

            case jdk:
                if(!FilePathUtil.createDirectoryIfNotExists(utilBaseDir)) throw new Exception("Conf File not created.");
                
                return utilBaseDir  + File.separator + jdkZipFileName;

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
     * 서비스 루트 폴더 생성 및 경로 리턴
     * @param isWindow
     * @return          ~/AppData/Local/serviceNm
     * @throws Exception
     */
    private static String getServiceRootPath(boolean isWindow, String serviceName) throws Exception {

        String serviceBasePath = "";

        if(isWindow){

            String localAppData = FilePathUtil.getLocalAppDataPath();
            serviceBasePath = localAppData + File.separator + serviceName;

        }

        // File created if not exist
        if(!FilePathUtil.createDirectoryIfNotExists(serviceBasePath)){
            throw new Exception("File not created.");
        }

        return serviceBasePath;
    }

    /**
     * 지정된 경로에 enum에 정의된 값들을 기반으로 폴더 구조 생성
     * @param rootPath 생성할 폴더의 루트 경로
     * @param enumType 폴더명이 정의된 enum 클래스
     * @throws Exception 폴더 생성 실패 시 발생
     */
    public static <T extends Enum<T>> boolean createDirectories(String rootPath, Class<T> enumType) {
        // enum의 모든 상수 가져오기
        T[] enumConstants = enumType.getEnumConstants();


        try{
            // 각 enum 상수에 대해 디렉토리 생성
            for (T enumValue : enumConstants) {
                String directoryPath = rootPath + File.separator + enumValue.name();
                FilePathUtil.createDirectoryIfNotExists(directoryPath);
            }
        }catch (Exception e){
            return false;
        }
        return true;
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
                log.error("path:{}. files are already existed.", directoryPath);
                return false;
            }
        }

        // 폴더가 존재하지 않는 경우 생성 (상위 폴더 포함)
        boolean created = directory.mkdirs();

        if (created) {
            log.info("path:{}. file are created.", directoryPath);
        } else {
            log.error("path:{}. fail to create file.", directoryPath);
        }

        return created;
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
     * 해당 경로에 해당 파일명이 존재하는지 확인
     */
    public static boolean checkFileExisted(String filePath, String fileName) {
        try {
            // 입력 파라미터 유효성 검증
            if (!areAllStringsValid(filePath, fileName)) {
                return false;
            }
            
            // 파일 경로 생성 및 존재 여부 확인
            Path fullPath = Paths.get(filePath, fileName);
            return Files.exists(fullPath) && Files.isRegularFile(fullPath);
            
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 해당 경로에 요청받은 파일 타입과 동일한 파일이 있는지 확인 (jar, bat 등)
     */
    public static boolean checkFileTypeExisted(String filePath, String fileType) {
        try {
            // 입력 파라미터 유효성 검증
            if (!areAllStringsValid(filePath, fileType)) {
                return false;
            }
            
            // 디렉토리 유효성 검증
            if (!isValidDirectory(filePath)) {
                return false;
            }
            
            // 파일타입 정리
            String cleanedFileType = cleanFileType(fileType);
            
            // 디렉토리 내 파일 검색
            File directory = new File(filePath);
            File[] files = directory.listFiles();
            
            if (files == null) {
                return false;
            }
            
            // 각 파일의 확장자 확인
            for (File file : files) {
                if (file.isFile()) {
                    String extension = getFileExtension(file.getName());
                    if (!isNullOrEmpty(extension) && extension.equalsIgnoreCase(cleanedFileType)) {
                        return true;
                    }
                }
            }
            
            return false;
            
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 문자열이 null이거나 빈 문자열인지 확인하는 공통 메소드
     */
    private static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 여러 문자열이 모두 유효한지 확인하는 공통 메소드
     */
    private static boolean areAllStringsValid(String... strings) {
        for (String str : strings) {
            if (isNullOrEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 디렉토리가 존재하고 유효한지 확인하는 공통 메소드
     */
    private static boolean isValidDirectory(String directoryPath) {
        try {
            File directory = new File(directoryPath);
            return directory.exists() && directory.isDirectory();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 파일 확장자를 추출하는 공통 메소드
     */
    private static String getFileExtension(String fileName) {
        if (isNullOrEmpty(fileName)) {
            return "";
        }
        
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }

    /**
     * 파일 타입에서 점(.) 제거하는 공통 메소드
     */
    private static String cleanFileType(String fileType) {
        if (isNullOrEmpty(fileType)) {
            return "";
        }
        return fileType.startsWith(".") ? fileType.substring(1) : fileType;
    }

    /**
     * 해당 경로에 해당 파일명이 존재하는지 확인
     */
    public static boolean checkFileExisted(String filePath, String fileName) {
        try {
            // 입력 파라미터 유효성 검증
            if (!areAllStringsValid(filePath, fileName)) {
                return false;
            }
            
            // 파일 경로 생성 및 존재 여부 확인
            Path fullPath = Paths.get(filePath, fileName);
            return Files.exists(fullPath) && Files.isRegularFile(fullPath);
            
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 디렉토리를 재귀적으로 삭제하는 공통 메소드 (파일과 하위 디렉토리 모두 삭제)
     */
    private static boolean deleteRecursively(File file) {
        if (file == null || !file.exists()) {
            return true;
        }
        
        // 디렉토리인 경우 하위 파일/디렉토리를 먼저 삭제
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (!deleteRecursively(child)) {
                        return false;
                    }
                }
            }
        }
        
        // 파일 또는 빈 디렉토리 삭제
        return file.delete();
    }


    /**
     * 요청받은 경로 하위에 있는 파일들 전부 삭제하는 메소드
     * @param directoryPath 삭제할 파일들이 있는 디렉토리 경로
     * @param deleteDirectory 디렉토리 자체도 삭제할지 여부 (true: 디렉토리도 삭제, false: 내용만 삭제)
     * @return 삭제 성공 여부
     */
    public static boolean deleteAllFilesInDirectory(String directoryPath, boolean deleteDirectory) {
        try {
            // 입력 파라미터 유효성 검증
            if (!areAllStringsValid(directoryPath)) {
                return false;
            }
            
            // 디렉토리 유효성 검증
            if (!isValidDirectory(directoryPath)) {
                return false;
            }
            
            File directory = new File(directoryPath);
            File[] files = directory.listFiles();
            
            // 파일 목록이 null인 경우 (권한 문제 등)
            if (files == null) {
                return false;
            }
            
            // 하위 파일/디렉토리 삭제
            boolean allDeleted = true;
            for (File file : files) {
                if (!deleteRecursively(file)) {
                    allDeleted = false;
                }
            }
            
            // 디렉토리 자체도 삭제하는 경우
            if (deleteDirectory && allDeleted) {
                return directory.delete();
            }
            
            return allDeleted;
            
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 요청받은 경로 하위에 있는 파일들만 삭제하는 메소드 (디렉토리는 유지)
     * @param directoryPath 삭제할 파일들이 있는 디렉토리 경로
     * @return 삭제 성공 여부
     */
    public static boolean deleteAllFilesOnly(String directoryPath) {
        return deleteAllFilesInDirectory(directoryPath, false);
    }


    /**
     * 요청받은 경로와 하위 모든 내용을 삭제하는 메소드 (디렉토리 포함)
     * @param directoryPath 삭제할 디렉토리 경로
     * @return 삭제 성공 여부
     */
    public static boolean deleteDirectoryCompletely(String directoryPath) {
        return deleteAllFilesInDirectory(directoryPath, true);
    }
    
    
}

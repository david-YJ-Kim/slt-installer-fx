package com.tsh.slt.installer.bizService;

import com.tsh.slt.installer.enums.*;
import com.tsh.slt.installer.util.FilePathUtil;
import com.tsh.slt.installer.vo.FileDownloadInfoVo;
import com.tsh.slt.installer.vo.MainFilePathVo;
import com.tsh.slt.installer.vo.OverallFileDownloadInfoVo;
import com.tsh.slt.installer.vo.ServiceDeployInfoDto;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InstallFilePathGetter {

    static final Logger log = LoggerFactory.getLogger(InstallFilePathGetter.class);

    private String companyName;
    private String serviceName;
    private String version;

    private String companyCommonUtilFolderPath;
    private String companyCommonUtilScriptFolderPath;
    private String companyCommonUtilJdkFolderPath;
    private String companyCommonUtilStorageFolderPath;

    private String serviceFolderPath;
    private String serviceBinFolderPath;
    private String serviceConfFolderPath;
    private String serviceTargetFolderPath;

    private ServiceDeployInfoDto serviceDeployInfo;

    private ConcurrentHashMap<LocalDownloadedType, ArrayList<FileDownloadInfoVo>> downloadInfoMapByType;

    static final String serverSeparator = "/";


    public InstallFilePathGetter(String serviceName, String version, MainFilePathVo mainFilePathVo,ServiceDeployInfoDto serviceDeployInfo) {

        log.info("initialize class.");
        this.companyName = COMPANY_NAME.COMPANY_NAME;
        this.serviceName = serviceName;
        this.version = version;
        this.companyCommonUtilFolderPath = mainFilePathVo.getCompanyCommonUtilPath();
        this.serviceFolderPath = mainFilePathVo.getServicePath();
        this.serviceDeployInfo = serviceDeployInfo;

        this.initializeFolderPath();

        this.initializeDownloadMapInfo();
        log.info("complete initialize class.");
    }

    private void initializeFolderPath(){

        this.companyCommonUtilScriptFolderPath = this.companyCommonUtilFolderPath + File.separator
                                                    + CompanyCommonUtilFileName.scripts.name();
        this.companyCommonUtilJdkFolderPath = this.companyCommonUtilFolderPath + File.separator
                                                    + CompanyCommonUtilFileName.jdk.name();
        this.companyCommonUtilStorageFolderPath = this.companyCommonUtilFolderPath + File.separator
                                                    + CompanyCommonUtilFileName.storage.name();

        this.serviceBinFolderPath = this.serviceFolderPath + File.separator + SrvDeployFileName.bin.name();
        this.serviceConfFolderPath = this.serviceFolderPath + File.separator + SrvDeployFileName.conf.name();
        this.serviceTargetFolderPath = this.serviceFolderPath + File.separator + SrvDeployFileName.target.name();

        log.info("complete initialize all paths.");
    }

    private void initializeDownloadMapInfo(){

        if(this.downloadInfoMapByType == null){
            this.downloadInfoMapByType = new ConcurrentHashMap<>();
        }

    }

    public OverallFileDownloadInfoVo generateFileDownloadInfoVo(){

        log.info("request to generate files download info.");

        ArrayList<FileDownloadInfoVo> localFileAndStorageInfoForCommonUtil = this.generateCommonUtilDownloadInfoVo();
        ArrayList<FileDownloadInfoVo> localFileAndStorageInfoForService = this.generateServiceDownloadInfoVo();


        this.downloadInfoMapByType.put(LocalDownloadedType.CompanyUtil, localFileAndStorageInfoForCommonUtil);
        this.downloadInfoMapByType.put(LocalDownloadedType.Service, localFileAndStorageInfoForService);
        log.info("complete generate files download info.");

        return this.generateOverallFileDownInfo();

    }


    private ArrayList<FileDownloadInfoVo> generateCommonUtilDownloadInfoVo(){

        ArrayList<FileDownloadInfoVo> commonUtilArray = new ArrayList<>();

        log.info("request to generate common-util files download info.");

        log.info("generate commonUtil info. runbat script among (script(runBat/addBat/vbs) & jdk & storage)");
        LocalDownloadedType downloadedType = LocalDownloadedType.CompanyUtil;
        commonUtilArray.add(this.generateInfoVo(downloadedType, DownloadFileTypeAndName.runBat, serviceDeployInfo.getWinRunBatFileName()));
        commonUtilArray.add(this.generateInfoVo(downloadedType, DownloadFileTypeAndName.addBat, serviceDeployInfo.getWinRunAddBatFileName()));
        commonUtilArray.add(this.generateInfoVo(downloadedType, DownloadFileTypeAndName.vbs, serviceDeployInfo.getWinVbsFileName()));


        commonUtilArray.add(this.generateInfoVo(downloadedType, DownloadFileTypeAndName.jdk, serviceDeployInfo.getJdkFileName()));
        commonUtilArray.add(this.generateInfoVo(downloadedType, DownloadFileTypeAndName.data, serviceDeployInfo.getStorageFileName()));


        return commonUtilArray;
    }


    private ArrayList<FileDownloadInfoVo> generateServiceDownloadInfoVo(){

        log.info("request to generate service files download info. (jar/conf)");


        ArrayList<FileDownloadInfoVo> serviceArray = new ArrayList<>();


        LocalDownloadedType downloadedType = LocalDownloadedType.Service;
        serviceArray.add(this.generateInfoVo(downloadedType, DownloadFileTypeAndName.jar, serviceDeployInfo.getJarFileName()));
        serviceArray.add(this.generateInfoVo(downloadedType, DownloadFileTypeAndName.yml, serviceDeployInfo.getConfFileName()));


        return serviceArray;


    }


    private FileDownloadInfoVo generateInfoVo(LocalDownloadedType downloadedType, DownloadFileTypeAndName localFileNameAndType,
                                                              String fullFileNameOnServer){


        String[] pathAndFileNameAndExtensionOnServer = FilePathUtil.separatePathAndFileNameAndExtension(fullFileNameOnServer);

        String fileName = localFileNameAndType.fileName.isEmpty() ? pathAndFileNameAndExtensionOnServer[1] : localFileNameAndType.fileName;
        String fileType = localFileNameAndType.fileType.isEmpty() ? pathAndFileNameAndExtensionOnServer[2] : localFileNameAndType.fileType;
        String fileNameAndType = FilePathUtil.attachedFileNameAndExtension(fileName, fileType);

        return FileDownloadInfoVo.builder()
                .localDownloadType(downloadedType).fileName(fileName).fileType(fileType).fileNameAndType(fileNameAndType)
                .filePathIncludingTypeInLocalPc(this.companyCommonUtilScriptFolderPath + File.separator + fileNameAndType)
                .storageSavedFileName(serviceDeployInfo.getWinRunBatFileName())
                .storageDownloadPathIncludingName(serviceDeployInfo.getStoragePath() + serverSeparator + fullFileNameOnServer)
                .build();


    }









    private OverallFileDownloadInfoVo generateOverallFileDownInfo(){

        return OverallFileDownloadInfoVo.builder()
                                            .companyName(this.companyName).serviceName(this.serviceName).version(this.version)
                                            .companyCommonUtilFolderPath(this.companyCommonUtilFolderPath)
                                            .companyCommonUtilScriptFolderPath(this.companyCommonUtilScriptFolderPath)
                                            .companyCommonUtilJdkFolderPath(this.companyCommonUtilJdkFolderPath)
                                            .serviceFolderPath(this.serviceFolderPath)
                                            .serviceBinFolderPath(this.serviceBinFolderPath).serviceConfFolderPath(this.serviceConfFolderPath)
                                            .serviceTargetFolderPath(this.serviceTargetFolderPath)
                                            .downloadInfoMapByType(this.downloadInfoMapByType)
                                            .build();


    }
}
package com.tsh.slt.installer.bizService;

import com.tsh.slt.installer.enums.CompanyCommonUtilFileName;
import com.tsh.slt.installer.enums.SrvDeployFileName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tsh.slt.installer.vo.*;
import com.tsh.slt.installer.enums.*;

import java.io.File;

public class FileDownloadBizService {

    public static final Logger log = LoggerFactory.getLogger(FileDownloadBizService.class);

    FirebaseStorageService storageService;

    private ConcurrentHashMap<LocalDownloadedType, ArrayList<FileDownloadInfoVo>> downloadInfoMapByType;

    private String companyCommonUtilFolderPath;
    private String companyCommonUtilScriptFolderPath;
    private String companyCommonUtilJdkFolderPath;
    private String companyCommonUtilStorageFolderPath;

    private String serviceFolderPath;
    private String serviceBinFolderPath;
    private String serviceConfFolderPath;
    private String serviceTargetFolderPath;

    // TODO make property and make is security.
    private final String storageBaseUri = "installer/agent/deploy/";

    public FileDownloadBizService(
        FirebaseStorageService storageService, String companyCommonUtilFolderPath, String serviceFolderPath){

        this.storageService = storageService;
        this.companyCommonUtilFolderPath = companyCommonUtilFolderPath;
        this.serviceFolderPath = serviceFolderPath;

        this.initializeFolderPath();
        log.info("complete initialize all paths.");

    }

    private void initializeDowloadMapInfo(){

        if(this.downloadInfoMapByType == null){
            this.downloadInfoMapByType = new ConcurrentHashMap<>();
        }

        this.downloadInfoMapByType.put(LocalDownloadedType.jdk, new ArrayList)
    }

    private void initializeFolderPath(){

        this.companyCommonUtilScriptFolderPath = this.companyCommonUtilFolderPath + File.separator
                                                    + CompanyCommonUtilFileName.script.name();
        this.companyCommonUtilJdkFolderPath = this.companyCommonUtilFolderPath + File.separator
                                                    + CompanyCommonUtilFileName.jdk.name();
        this.companyCommonUtilStorageFolderPath = this.companyCommonUtilFolderPath + File.separator
                                                    + CompanyCommonUtilFileName.storage.name();

        this.serviceBinFolderPath = this.serviceFolderPath + File.separator + SrvDeployFileName.bin.name();
        this.serviceConfFolderPath = this.serviceFolderPath + File.separator + SrvDeployFileName.conf.name();
        this.serviceTargetFolderPath = this.serviceFolderPath + File.separator + SrvDeployFileName.target.name();

        log.info("complete initialize all paths.");
    }


    public boolean handleCommonUtilDownload(boolean isForce){

        log.info("start download common-util files.(jdk / storage)")

        this.checkCommonUtilAlreadyInstalled();

        // check already installed files

        return true;
    }


    public boolean handleServiceDownload(){

        return true;
    }

    private boolean downloadFilesFromStorage(){
        return true;
    }


    private boolean checkCommonUtilAlreadyInstalled(){

        log.info("start check common-util files already installed ")

        return true;
    }

    private boolean checkServiceAlreadyInstalled(){

        return true;
    }


}

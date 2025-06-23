package com.tsh.slt.installer.bizService;

import com.tsh.slt.installer.enums.CompanyCommonUtilFileName;
import com.tsh.slt.installer.enums.SrvDeployFileName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileDownloadBizService {

    public static final Logger log = LoggerFactory.getLogger(FileDownloadBizService.class);

    FirebaseStorageService storageService;

    private String companyCommonUtilFolderPath;
    private String companyCommonUtilScriptFolderPath;
    private String companyCommonUtilJdkFolderPath;

    private String serviceFolderPath;
    private String serviceBinFolderPath;
    private String serviceConfFolderPath;
    private String serviceTargetFolderPath;

    public FileDownloadBizService(FirebaseStorageService storageService, String companyCommonUtilFolderPath, String serviceFolderPath){

        this.storageService = storageService;
        this.companyCommonUtilFolderPath = companyCommonUtilFolderPath;
        this.serviceFolderPath = serviceFolderPath;
    }

    private void initializeFolderPath(){

        this.companyCommonUtilScriptFolderPath = this.companyCommonUtilFolderPath + File.separator
                                                    + CompanyCommonUtilFileName.script.name();
        this.companyCommonUtilJdkFolderPath = this.companyCommonUtilFolderPath + File.separator
                                                    + CompanyCommonUtilFileName.jdk.name();

        this.serviceBinFolderPath = this.serviceFolderPath + File.separator + SrvDeployFileName.bin.name();
        this.serviceConfFolderPath = this.serviceFolderPath + File.separator + SrvDeployFileName.conf.name();
        this.serviceTargetFolderPath = this.serviceFolderPath + File.separator + SrvDeployFileName.target.name();

        log.info("complete initialize all paths.");
    }
}

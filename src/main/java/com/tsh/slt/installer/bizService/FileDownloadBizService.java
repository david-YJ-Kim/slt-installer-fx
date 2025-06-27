package com.tsh.slt.installer.bizService;

import com.tsh.slt.installer.enums.CompanyCommonUtilFileName;
import com.tsh.slt.installer.enums.SrvDeployFileName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tsh.slt.installer.vo.*;
import com.tsh.slt.installer.enums.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileDownloadBizService {

    public static final Logger log = LoggerFactory.getLogger(FileDownloadBizService.class);

    FirebaseStorageService storageService;
    OverallFileDownloadInfoVo overallFileDownloadInfoVo;

    private String companyCommonUtilFolderPath;
    private String companyCommonUtilScriptFolderPath;
    private String companyCommonUtilJdkFolderPath;
    private String companyCommonUtilStorageFolderPath;

    private String serviceFolderPath;
    private String serviceBinFolderPath;
    private String serviceConfFolderPath;
    private String serviceTargetFolderPath;


    public FileDownloadBizService(FirebaseStorageService storageService, OverallFileDownloadInfoVo overallFileDownloadInfoVo){

        this.storageService = storageService;
        this.overallFileDownloadInfoVo = overallFileDownloadInfoVo;

        log.info("complete initialize all paths.");

    }



    public boolean handleCommonUtilDownload(boolean isForce){

        log.info("start download common-util files.(jdk / storage)");

        boolean isAlreadyInstalled = this.checkCommonUtilAlreadyInstalled();
        if(isAlreadyInstalled){
            log.info("common-util files already installed. skip download");
            return true;

        }

        return true;
    }


    public boolean handleServiceDownload(){

        log.info("start download service files(bin / conf / target).");


        log.info("detect same version folder istalled.");
        boolean isSameVersionInstalled = false; // TODO generate code here.
        if(isSameVersionInstalled){
            log.info("same version existed. delete this and install.");
            
            // TODO delete same version of folder.

            log.info("complete delete same version folder.");
        }

        log.info("start install service files.");
        

        return true;
    }

    private boolean downloadFilesFromStorage(){
        return true;
    }


    private boolean checkCommonUtilAlreadyInstalled(){

        log.info("start check common-util files already installed ");

        return true;
    }




}

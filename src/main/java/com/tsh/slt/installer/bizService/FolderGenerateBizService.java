package com.tsh.slt.installer.bizService;


import com.tsh.slt.installer.enums.CompanyCommonFileName;
import com.tsh.slt.installer.enums.CompanyCommonUtilFileName;
import com.tsh.slt.installer.enums.PcEnvTypes;
import com.tsh.slt.installer.util.FilePathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 	서비스 폴더 구조
 * 	폴더 구분
 * 	서비스 공통 폴더
 * 	서비스 전용 폴더
 *
 * 	기본 구조
 * → AppData/Local/${companyName}/${serviceName}
 * 	버전(version): 서비스 전용 폴더
 * → ${serviceName}/${version}
 * 	실행파일(bin)
 * 	설정파일(conf)
 * 	Jar파일(target)
 * 	공통(util) : 하위는 서비스 공통 폴더
 * → ${serviceName}/util
 * 	실행파일(bin)
 * 	자바파일(jdk)
 * 	로그(logs)
 * → ${serviceName}/logs
 * 
 * 	데이터(storage)
 * → ${serviceName}/storage
 */
public class FolderGenerateBizService {

    private final static Logger log = LoggerFactory.getLogger(FolderGenerateBizService.class);

    public String COMPANY_NAME;
    public String SERVICE_NAME;
    public String SERVICE_VERSION;
    public PcEnvTypes pcEnvTypes;

    private String companyRootFolderPath;
    private String companyCommonUtilFolderPath;
    private String serviceExclusiveFolderPath;


    public FolderGenerateBizService(String companyName, String serviceName, String serviceVersion, PcEnvTypes pcEnvTypes) throws Exception {

        this.COMPANY_NAME = companyName;
        this.SERVICE_NAME = serviceName;
        this.SERVICE_VERSION = serviceVersion;
        this.pcEnvTypes = pcEnvTypes;

        this.setCompanyRootPath();
        log.info("set rootPath:{}.", this.companyRootFolderPath);

        this.generateFolder(this.companyRootFolderPath);
        log.info("generate root files. finish initialize.");
    }


    public String getCompanyUtilPath(){

        if(this.companyCommonUtilFolderPath.isEmpty()){
            this.setCompanyUtilPath();
        }

        return this.companyCommonUtilFolderPath;
    }

    public String getServicePath(){

        if(this.serviceExclusiveFolderPath.isEmpty()){
            this.setServicePath();
        }

        return this.serviceExclusiveFolderPath;
    }


    public boolean generateCommonFolders() throws Exception {

        if(companyRootFolderPath.isEmpty()){
            log.error("root path is empty.");
            this.setCompanyRootPath();
        }

        boolean fileCreateResult = FilePathUtil.createDirectories(this.companyRootFolderPath, CompanyCommonFileName.class);


        if(!fileCreateResult){
            log.error("fail to create common folder (uitl, logs, storage) under companyRootFolder:{}.", this.companyRootFolderPath);
            throw new Exception("fail to create common folder (uitl, logs, storage) under companyRootFolder:" + this.companyRootFolderPath + ".");
        }

        log.info("created common folders (util, logs, storage) under({}).", this.companyRootFolderPath);
        return true;
    }


    public boolean generateCommonUtilFolders() throws Exception {

        if(companyCommonUtilFolderPath.isEmpty()){
            log.error("util path is empty.");
            this.setCompanyUtilPath();
        }

        boolean fileCreateResult = FilePathUtil.createDirectories(this.companyCommonUtilFolderPath, CompanyCommonUtilFileName.class);


        if(!fileCreateResult){
            log.error("fail to create common folder (script, jdk) under companyCommonFolder:{}.", this.companyCommonUtilFolderPath);
            throw new Exception("fail to create common folder (script, jdk) under companyCommonFolder:" + this.companyCommonUtilFolderPath + ".");
        }

        log.info("created common folders (script, jdk) under({}).", this.companyCommonUtilFolderPath);
        return true;

    }

    public boolean generateServiceFolders() throws Exception {

        if(serviceExclusiveFolderPath.isEmpty()){
            log.error("service path is empty.");
            this.setServicePath();
        }

        boolean fileCreateResult = FilePathUtil.createDirectories(this.serviceExclusiveFolderPath, CompanyCommonUtilFileName.class);


        if(!fileCreateResult){
            log.error("fail to create service folder (bin, conf, target, deploy) under serviceFolder:{}."
                    , this.serviceExclusiveFolderPath);
            throw new Exception("fail to service folder (bin, conf, target, deploy) under serviceFolder:"
                    + this.serviceExclusiveFolderPath + ".");
        }

        log.info("created service folders (bin, conf, target, deploy) under({}).", this.serviceExclusiveFolderPath);
        return true;
    }



    private void setCompanyUtilPath(){
        this.companyCommonUtilFolderPath = this.companyRootFolderPath + File.separator + CompanyCommonFileName.util.name();
        log.info("company common util path: {}.", this.companyCommonUtilFolderPath);

    }

    private void setServicePath(){

        this.serviceExclusiveFolderPath = this.companyRootFolderPath + File.separator + SERVICE_NAME + File.separator + SERVICE_VERSION;
        log.info("service util path: {}.", this.serviceExclusiveFolderPath);

    }

    private void generateFolder(String path) throws Exception {

        log.info("request to generate path:{}.", path);
        boolean fileCreateResult = FilePathUtil.createDirectoryIfNotExists(path);

        if(!fileCreateResult){
            log.error("fail to create folder w/ pat:{}.", path);
            throw new Exception("Fail to created file" + path + ".");
        }

        log.info("created file:{}.", path);

    }

    private void setCompanyRootPath(){
        this.companyRootFolderPath = FilePathUtil.getLocalAppDataPath() + File.separator + COMPANY_NAME;
        log.info("get serviceRootPath:{}.", this.companyRootFolderPath);

    }

}

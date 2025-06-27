package com.tsh.slt.installer.vo;

import com.tsh.slt.installer.enums.LocalDownloadedType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class OverallFileDownloadInfoVo {

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

    ConcurrentHashMap<LocalDownloadedType, ArrayList<FileDownloadInfoVo>> downloadInfoMapByType;

    @Builder
    public OverallFileDownloadInfoVo(String companyName, String serviceName, String version, String companyCommonUtilFolderPath, String companyCommonUtilScriptFolderPath, String companyCommonUtilJdkFolderPath, String companyCommonUtilStorageFolderPath, String serviceFolderPath, String serviceBinFolderPath, String serviceConfFolderPath, String serviceTargetFolderPath, ConcurrentHashMap<LocalDownloadedType, ArrayList<FileDownloadInfoVo>> downloadInfoMapByType) {
        this.companyName = companyName;
        this.serviceName = serviceName;
        this.version = version;
        this.companyCommonUtilFolderPath = companyCommonUtilFolderPath;
        this.companyCommonUtilScriptFolderPath = companyCommonUtilScriptFolderPath;
        this.companyCommonUtilJdkFolderPath = companyCommonUtilJdkFolderPath;
        this.companyCommonUtilStorageFolderPath = companyCommonUtilStorageFolderPath;
        this.serviceFolderPath = serviceFolderPath;
        this.serviceBinFolderPath = serviceBinFolderPath;
        this.serviceConfFolderPath = serviceConfFolderPath;
        this.serviceTargetFolderPath = serviceTargetFolderPath;
        this.downloadInfoMapByType = downloadInfoMapByType;
    }
}
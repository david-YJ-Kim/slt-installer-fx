package com.tsh.slt.installer.vo;

import com.tsh.slt.installer.enums.LocalDownloadedType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
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
}
package com.tsh.slt.installer.vo;

import com.tsh.slt.installer.enums.LocalDownloadedType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDownloadInfoVo {

    LocalDownloadedType localDownloadType; // CompanyUtil - Service
    String fileName;
    String fileType;
    String fileNameIncludingType;

    String targetLocalFolderPath;
    String storageDownloadPath;
}
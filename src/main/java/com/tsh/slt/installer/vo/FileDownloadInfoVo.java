package com.tsh.slt.installer.vo;

import com.tsh.slt.installer.enums.LocalDownloadedType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDownloadInfoVo {

    LocalDownloadedType localDownloadType; // CompanyUtil or Service
    String fileNameInLocalPc;
    String fileTypeInLocalPc;
    String fileNameIncludingTypeInLocalPc;
    String fileInstalledFolderPathInLocalPc;

    String targetLocalFolderPath;
    String storageSavedFileName;
    String storageDownloadPath;
}
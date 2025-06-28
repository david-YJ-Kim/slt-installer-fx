package com.tsh.slt.installer.vo;

import com.tsh.slt.installer.enums.LocalDownloadedType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileDownloadInfoVo {

    LocalDownloadedType localDownloadType; // CompanyUtil or Service
    String fileName;
    String fileType;
    String fileNameAndType;
    String filePathIncludingTypeInLocalPc;

    String storageSavedFileName;
    String storageDownloadPathIncludingName;
}
package com.tsh.slt.installer.bizService;

import com.tsh.slt.installer.enums.LocalDownloadedType;
import com.tsh.slt.installer.util.FilePathUtil;
import com.tsh.slt.installer.vo.FileDownloadInfoVo;
import com.tsh.slt.installer.vo.OverallFileDownloadInfoVo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


    public FileDownloadBizService(OverallFileDownloadInfoVo overallFileDownloadInfoVo) {

        this.storageService = FirebaseStorageService.getInstance();
        this.overallFileDownloadInfoVo = overallFileDownloadInfoVo;

        log.info("complete initialize all paths.");

    }

    public void executeDownload() {

        this.handleServiceDownload();
        this.handleCommonUtilDownload(false);
    }


    public boolean handleCommonUtilDownload(boolean isForce) {

        log.info("start download common-util files.(jdk / storage)");

        String commonUtilPath = overallFileDownloadInfoVo.getCompanyCommonUtilFolderPath();
        boolean isAlreadyInstalled = FilePathUtil.checkFolderExisted(commonUtilPath);
        if (isAlreadyInstalled) {
            log.info("common-util files already installed. skip download");
            return true;

        }

        this.downloadFilesFromStorage(LocalDownloadedType.CompanyUtil);

        return true;
    }


    public void handleServiceDownload() {

        log.info("start download service files(bin / conf / target).");
        log.info("detect same version folder istalled.");

        String servicePath = overallFileDownloadInfoVo.getServiceFolderPath();
        boolean isAlreadyInstalled = FilePathUtil.checkFolderExisted(servicePath);
        if (isAlreadyInstalled) {
            log.info("same version existed. delete this and install.");

            FilePathUtil.deleteAllFilesInDirectory(overallFileDownloadInfoVo.getServiceFolderPath(), true);

            log.info("complete delete same version folder.");
        }

        log.info("start install service files.");
        this.downloadFilesFromStorage(LocalDownloadedType.Service);

    }

    private void downloadFilesFromStorage(LocalDownloadedType downloadedType) {

        ArrayList<FileDownloadInfoVo> infoVoArray = overallFileDownloadInfoVo.getDownloadInfoMapByType()
                .get(downloadedType);

        if (infoVoArray == null || infoVoArray.isEmpty()) {
            log.warn("{} Nothing to download.", downloadedType.name());
            return;
        }

        for (FileDownloadInfoVo infoVo : infoVoArray) {
            log.info("start download. fileName:{}, path:{}, from server:{}.",
                    infoVo.getFileNameAndType(), infoVo.getFilePathIncludingTypeInLocalPc(),
                    infoVo.getStorageDownloadPathIncludingName());

            try {

                String serverPath = infoVo.getStorageDownloadPathIncludingName();
                String localPath = infoVo.getFilePathIncludingTypeInLocalPc();
                File installedFile = this.storageService.downloadFile(serverPath, localPath);

                if (installedFile.exists()) {
                    log.error("fail to download.");
                    return;
                }
                log.info("complete download.");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


}

package com.tsh.slt.installer.bizService;

import com.tsh.slt.installer.controller.InstallerController;
import com.tsh.slt.installer.enums.DownloadFileTypes;
import com.tsh.slt.installer.util.FilePathUtil;
import com.tsh.slt.installer.util.ServicePortFindUtil;
import com.tsh.slt.installer.vo.InstallPreActionResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class InstallProcessBizService {

    static int portRangeStart = 15000;          // 가용 포트 범위 시작
    static int portRangeEnd = 15100;            // 가용 포트 범위 끝
    static int portNum = 2;                     // 필요한 포트 개수

    public static Logger logger = LoggerFactory.getLogger(InstallProcessBizService.class);

    private static InstallProcessBizService instance;

    // 싱글톤 패턴
    public static InstallProcessBizService getInstance() {
        if (instance == null) {
            instance = new InstallProcessBizService();
        }
        return instance;
    }

    private InstallProcessBizService() {}


    /**
     * Install 수행을 위한 사전 검증 로직
     * @param controller
     * @return
     */
    public InstallPreActionResultVo preAction(InstallerController controller){

        // 서비스 폴더 존재 여부
        // 서비스 + 설치할 버전 존재 여부
        // 프로세스 실행 여부 (서비스 명)

        // 포드 등록 여부
        // 프로세스 실행 여부 (서비스 포트 기준)

        return null;
        
    }


    /**
     * Agent 설치 수행 메소드
     * @param controller
     * @return
     */
    public boolean executeInstallLogic(InstallerController controller, InstallPreActionResultVo vo){

        FirebaseStorageService storage = FirebaseStorageService.getInstance();
        FirebaseStoreService store = FirebaseStoreService.getInstance();

        String installVersion = vo.getDeployInfo().getVersion();


        controller.updateProgress(0.0);
        controller.updateStatus("Start Installing.");

        String logBaseDir; String productBaseDir; String utilBaseDir;

        // 1. 서비스 폴더 생성
        try {
            FilePathUtil.createCommonDir(true);     // AppData/Local/serviceNm/ product & util & logs & storage
            utilBaseDir = FilePathUtil.createUtilCommonDir(true); // AppData/Local/serviceNm/util/ script & jdk
            logBaseDir = FilePathUtil.createLogsDir(true, vo.getDeployInfo());   // AppData/Local/serviceNm/logs/agent/v1.0.0
            productBaseDir = FilePathUtil.createProductDir(true, vo.getDeployInfo()); // AppData/Local/serviceNm/product/agent/v1.0.0

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        controller.updateProgress(1.0);
        controller.updateStatus("Compete create folder.");


        // 2. Storage 에서 필요한 파일 다운로드
        for(DownloadFileTypes file : DownloadFileTypes.values()){

            try {
                storage.downloadFile(FilePathUtil.getFirebaseFilePath(file, installVersion)
                        , FilePathUtil.getLocalDownloadFilePath(productBaseDir, utilBaseDir, file, true, installVersion));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        controller.updateProgress(2.0);
        controller.updateStatus("Compete download files.");


        // 3. 가용 포트 조회
        List<Integer> availableServicePorts = ServicePortFindUtil.findAvailablePorts(portRangeStart, portRangeEnd, portNum);
        controller.updateProgress(3.0);
        controller.updateStatus("Compete getting service port.");

        // 4. 포트 정보 등록
        try {
            store.updateTicketPortInfo(vo.getUserInfo(), availableServicePorts);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        controller.updateProgress(4.0);
        controller.updateStatus("Compete update service port.");

        // 5. 스크립트 생성
        // 6. Registry 등록
        // 7. Agent 실행
        // 8. Satellite 실행
        // 결과 보고

        return true;

    
        
    }
}

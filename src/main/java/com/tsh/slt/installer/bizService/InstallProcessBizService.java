package com.tsh.slt.installer.bizService;

import com.tsh.slt.installer.controller.InstallerController;
import com.tsh.slt.installer.enums.COMPANY_NAME;
import com.tsh.slt.installer.enums.DownloadFileTypes;
import com.tsh.slt.installer.enums.PcEnvTypes;
import com.tsh.slt.installer.util.FilePathUtil;
import com.tsh.slt.installer.util.ServicePortFindUtil;
import com.tsh.slt.installer.vo.InstallPreActionResultVo;
import com.tsh.slt.installer.vo.ServiceDeployInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class InstallProcessBizService {

    static int portRangeStart = 15000;          // 가용 포트 범위 시작
    static int portRangeEnd = 15100;            // 가용 포트 범위 끝
    static int portNum = 2;                     // 필요한 포트 개수

    public static Logger log = LoggerFactory.getLogger(InstallProcessBizService.class);

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
    public boolean executeInstallLogic(InstallerController controller, InstallPreActionResultVo vo) throws Exception {


        this.updateInstallProgress(controller, 0.0, "start installing.")
        
        // TODO PC 환경 정보 가져오기
        PcEnvTypes pcEnvTypes = PcEnvTypes.WINDOW;
        
        FirebaseStorageService storage = FirebaseStorageService.getInstance();
        FirebaseStoreService store = FirebaseStoreService.getInstance();
        
        ServiceDeployInfoDto serviceInstallInfo = vo.getDeployInfo();
        String serviceName = serviceInstallInfo.getProdId();
        String serviceVersion = serviceInstallInfo.getVersion();
        
        
        
        String logBaseDir; String productBaseDir = ""; String utilBaseDir = "";
        
        this.updateInstallProgress(controller, 1.0, "generate some folders.")
        FolderGenerateBizService folderGenerateBizService =
        new FolderGenerateBizService(COMPANY_NAME.COMPANY_NAME, serviceName, serviceVersion, pcEnvTypes);
        
        try{
            folderGenerateBizService.generateCommonFolders();
            log.info("complete generate folders under company.");
            folderGenerateBizService.generateCommonUtilFolders();
            log.info("complete generate util folders under common.");
            
            folderGenerateBizService.generateServiceFolders();
            log.info("complete generate service folder.");
            
        }catch (Exception e){
            log.error("error while generate folders.");
        }
        log.info("complete process to generate folders. update status.");
        
        
        // Folder Generate 하면서 확인된 주요 경로 획득
        String companyCommonUtilPath = folderGenerateBizService.getCompanyUtilPath();
        String servicePath = folderGenerateBizService.getServicePath();
        
        
        this.updateInstallProgress(controller, 2.0, "generate install info.")
        InstallFilePathGetter filePathGetter = new InstallFilePathGetter(
                                    COMPANY_NAME.COMPANY_NAME, serviceName, serviceVersion, serviceInstallInfo);
        OverallFileDownloadInfoVo downloadInfoMapByType = filePathGetter.generateFileDownloadInfoVo();
        

        this.updateInstallProgress(controller, 3.0, "install some files from storage.")
        FileDownloadBizService fileDownloadBizService = new FileDownloadBizService(storage, OverallFileDownloadInfoVo);
        
        for(DownloadFileTypes file : DownloadFileTypes.values()){
            
            try {
                storage.downloadFile(FilePathUtil.getFirebaseFilePath(file, serviceVersion)
                , FilePathUtil.getLocalDownloadFilePath(productBaseDir, utilBaseDir, file, true, serviceVersion));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        
        this.updateInstallProgress(controller, 4.0, "searching for available service port.")
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
        
        this.updateInstallProgress(controller, 5.0, "generate runnign scripts.")
        this.updateInstallProgress(controller, 6.0, "set-up running enviroment.")
        this.updateInstallProgress(controller, 7.0, "start running your agent.")
        this.updateInstallProgress(controller, 9.0, "start running satelite.")
        this.updateInstallProgress(controller, 10.0, "complete install enjoy.")

        return true;

    
        
    }


    private void updateInstallProgress(InstallerController controller, double progress, String status) {
    
        controller.updateProgress(progress);
        controller.updateStatus(status);
    }



}

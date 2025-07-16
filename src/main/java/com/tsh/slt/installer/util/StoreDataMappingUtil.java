package com.tsh.slt.installer.util;


import com.google.cloud.firestore.DocumentSnapshot;
import com.tsh.slt.installer.enums.DeployInfoColumns;
import com.tsh.slt.installer.enums.TicketInfoColumns;
import com.tsh.slt.installer.vo.ServiceDeployInfoDto;
import com.tsh.slt.installer.vo.UserTicketInfoDto;

/**
 * Firebase Store 에서 조회한 데이터를 DTO 객체에 수동 매핑하는 클래스
 */
public class StoreDataMappingUtil {


    /**
     * Deploy 버전 dto mapping 메소드
     *
     * @param collectionNm
     * @param document
     * @return
     */
    public static ServiceDeployInfoDto deployInfoMapping(String collectionNm, DocumentSnapshot document) {

        ServiceDeployInfoDto dto = ServiceDeployInfoDto.builder().build();

        // ID 설정
        dto.setCollectionName(collectionNm);
        dto.setId(document.getId());

        // 필드 매핑
        if (document.contains(DeployInfoColumns.ID.name())) {
            dto.setId(document.getString(DeployInfoColumns.ID.name()));
        }
        if (document.contains(DeployInfoColumns.SITE_ID.name())) {
            dto.setSiteId(document.getString(DeployInfoColumns.SITE_ID.name()));
        }
        if (document.contains(DeployInfoColumns.VERSION.name())) {
            dto.setVersion(document.getString(DeployInfoColumns.VERSION.name()));
        }
        if (document.contains(DeployInfoColumns.PROD_DESC.name())) {
            dto.setProdDesc(document.getString(DeployInfoColumns.PROD_DESC.name()));
        }
        if (document.contains(DeployInfoColumns.PROD_ID.name())) {
            dto.setProdId(document.getString(DeployInfoColumns.PROD_ID.name()));
        }
        if (document.contains(DeployInfoColumns.CRT_DT.name())) {
            dto.setCrtDt(document.getTimestamp(DeployInfoColumns.CRT_DT.name()));
        }
        if (document.contains(DeployInfoColumns.UPDATE_DT.name())) {
            dto.setUpdateDt(document.getTimestamp(DeployInfoColumns.UPDATE_DT.name()));
        }
        if (document.contains(DeployInfoColumns.COMPANY_NAME.name())) {
            dto.setCompanyName(document.getString(DeployInfoColumns.COMPANY_NAME.name()));
        }
        if (document.contains(DeployInfoColumns.CONF_FILE_NAME.name())) {
            dto.setConfFileName(document.getString(DeployInfoColumns.CONF_FILE_NAME.name()));
        }
        if (document.contains(DeployInfoColumns.JAR_FILE_NAME.name())) {
            dto.setJarFileName(document.getString(DeployInfoColumns.JAR_FILE_NAME.name()));
        }
        if (document.contains(DeployInfoColumns.JDK_FILE_NAME.name())) {
            dto.setJdkFileName(document.getString(DeployInfoColumns.JDK_FILE_NAME.name()));
        }
        if (document.contains(DeployInfoColumns.PORT_NUM.name())) {
            dto.setPortNum(document.getLong(DeployInfoColumns.PORT_NUM.name()));
        }
        if (document.contains(DeployInfoColumns.PORT_RNG_END.name())) {
            dto.setPortRngEnd(document.getLong(DeployInfoColumns.PORT_RNG_END.name()));
        }
        if (document.contains(DeployInfoColumns.PORT_RNG_STRT.name())) {
            dto.setPortRngStrt(document.getLong(DeployInfoColumns.PORT_RNG_STRT.name()));
        }
        if (document.contains(DeployInfoColumns.STORAGE_FILE_NAME.name())) {
            dto.setStorageFileName(document.getString(DeployInfoColumns.STORAGE_FILE_NAME.name()));
        }
        if (document.contains(DeployInfoColumns.STORAGE_PATH.name())) {
            dto.setStoragePath(document.getString(DeployInfoColumns.STORAGE_PATH.name()));
        }
        if (document.contains(DeployInfoColumns.WIN_RUN_ADD_BAT_FILE_NAME.name())) {
            dto.setWinRunAddBatFileName(document.getString(DeployInfoColumns.WIN_RUN_ADD_BAT_FILE_NAME.name()));
        }
        if (document.contains(DeployInfoColumns.WIN_RUN_BAT_FILE_NAME.name())) {
            dto.setWinRunBatFileName(document.getString(DeployInfoColumns.WIN_RUN_BAT_FILE_NAME.name()));
        }
        if (document.contains(DeployInfoColumns.WIN_VBS_FILE_NAME.name())) {
            dto.setWinVbsFileName(document.getString(DeployInfoColumns.WIN_VBS_FILE_NAME.name()));
        }

        return dto;
    }


    /**
     * User tickets dto mapping 메소드
     *
     * @param collectionNm
     * @param document
     * @return
     */
    public static UserTicketInfoDto ticketMapping(String collectionNm, DocumentSnapshot document) {

        UserTicketInfoDto dto = UserTicketInfoDto.builder().build();

        // ID 설정
        dto.setCollectionName(collectionNm);
        dto.setId(document.getId());

        // 필드 매핑
        if (document.contains(TicketInfoColumns.userId.name())) {
            dto.setUserId(document.getString(TicketInfoColumns.userId.name()));
        }
        if (document.contains(TicketInfoColumns.username.name())) {
            dto.setUsername(document.getString(TicketInfoColumns.username.name()));
        }
        if (document.contains(TicketInfoColumns.email.name())) {
            dto.setEmail(document.getString(TicketInfoColumns.email.name()));
        }
        if (document.contains(TicketInfoColumns.grade.name())) {
            dto.setGrade(document.getString(TicketInfoColumns.grade.name()));
        }
        if (document.contains(TicketInfoColumns.createdAt.name())) {
            dto.setCreatedAt(document.getTimestamp(TicketInfoColumns.createdAt.name()));
        }
        if (document.contains(TicketInfoColumns.expiredAt.name())) {
            dto.setExpiredAt(document.getTimestamp(TicketInfoColumns.expiredAt.name()));
        }
        if (document.contains(TicketInfoColumns.updatedAt.name())) {
            dto.setUpdatedAt(document.getTimestamp(TicketInfoColumns.updatedAt.name()));
        }

        if (document.contains(TicketInfoColumns.agentPort.name())) {
            dto.setAgentPort(document.getString(TicketInfoColumns.agentPort.name()));
        }
        if (document.contains(TicketInfoColumns.satellitePort.name())) {
            dto.setSatellitePort(document.getString(TicketInfoColumns.satellitePort.name()));
        }

        return dto;
    }

}

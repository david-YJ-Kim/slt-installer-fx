package com.tsh.slt.installer.util;


import com.google.cloud.firestore.DocumentSnapshot;
import com.tsh.slt.installer.enums.DeployInfoColumns;
import com.tsh.slt.installer.vo.ServiceDeployInfoDto;

/**
 * Firebase Store 에서 조회한 데이터를 DTO 객체에 수동 매핑하는 클래스
 */
public class StoreDataMappingUtil {

    public static ServiceDeployInfoDto deployInfoMapping(DocumentSnapshot document){

        ServiceDeployInfoDto dto = new ServiceDeployInfoDto();

        // ID 설정
        dto.setId(document.getId());

        // 필드 매핑
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

        return dto;
    }

}

package com.tsh.slt.installer.vo;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ServiceDeployInfoDto {

    String collectionName;                  // DB Collection 이름
    String id;
    String siteId;
    private String version;
    private String prodDesc;
    private String prodId;
    private String companyName;
    
    private String confFileName;
    private String jarFileName;
    private String jdkFileName;
    private String storageFileName;
    private String storagePath;
    private String winRunAddBatFileName;
    private String winRunBatFileName;
    private String winVbsFileName;

    private Long portRngStrt;
    private Long portRngEnd;
    private Long portNum;

    private Timestamp crtDt;
    private Timestamp updateDt;
}

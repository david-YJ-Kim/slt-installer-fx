package com.tsh.slt.installer.vo;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServiceDeployInfoDto {

    String id;
    String siteId;
    private String version;
    private String prodDesc;
    private String prodId;
    private Timestamp crtDt;
    private Timestamp updateDt;
}

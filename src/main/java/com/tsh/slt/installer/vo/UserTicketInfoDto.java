package com.tsh.slt.installer.vo;

import com.google.cloud.Timestamp;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class UserTicketInfoDto {

    String collectionName;                  // DB Collection 이름
    String id;
    String userId;
    private String username;
    private String email;
    private String grade;
    private String agentPort;
    private String satellitePort;
    private Timestamp createdAt;
    private Timestamp expiredAt;
    private Timestamp updatedAt;
}

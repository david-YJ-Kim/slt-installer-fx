package com.tsh.slt.installer.vo;

import com.tsh.slt.installer.enums.BizWorkTypes;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InstallPreActionResultVo {

    public BizWorkTypes workTypes;                     // 수행할 Work 타입
    public ServiceDeployInfoDto deployInfo;            // 설치 버전 정보
    public UserTicketInfoDto userInfo;                 // 사용자 정보
}

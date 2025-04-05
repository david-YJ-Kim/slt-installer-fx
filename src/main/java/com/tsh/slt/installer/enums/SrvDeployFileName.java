package com.tsh.slt.installer.enums;

/**
 * 서비스 배포를 위한 기초 폴더
 */
public enum SrvDeployFileName {
    bin,            // 서비스 실행을 위한 폴더
    conf,           // 서비스 설정 정보를 위한 폴더
    target,         // 서비스 실행 빌드 파일을 위한 폴더
    logs,           // 서비스 로그 폴더
    data            // 서비스에서 로컬에 저장하는 DB 폴더
    ;
}

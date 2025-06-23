package com.tsh.slt.installer.enums;

public enum DownloadFileTypeAndName {

    jar("", "jar"),
    yml("application", "yml"),
    jdk("openjdk-1.8", "zip"),
    vbs("", "vbs"),
    bat("", "bat"),
    addBat("addStartProgram", "bat"),
    data("agent-init-data", "sql3") // TODO check data type
    ;

    private final String fileName;
    private final String fileType;

    DownloadFileTypeAndName(String fileName, String fileType){
        this.fileName = fileName;
        this.fileType = fileType;
    }
}

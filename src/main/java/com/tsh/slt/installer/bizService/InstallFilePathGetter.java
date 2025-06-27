package com.tsh.slt.installer.bizService;

public class InstallFilePathGetter {

    static final Logger log = LoggerFactory.getLogger(InstallFilePathGetter.class);

    private String companyName;
    private String serviceName;
    private String version;

    private String companyCommonUtilFolderPath;
    private String companyCommonUtilScriptFolderPath;
    private String companyCommonUtilJdkFolderPath;
    private String companyCommonUtilStorageFolderPath;

    private String serviceFolderPath;
    private String serviceBinFolderPath;
    private String serviceConfFolderPath;
    private String serviceTargetFolderPath;

    private ServiceDeployInfoDto serviceDeployInfo;

    private ConcurrentHashMap<LocalDownloadedType, ArrayList<FileDownloadInfoVo>> downloadInfoMapByType;


    public InstallFilePathGetter(String companyName, String serviceName, String version, String companyCommonUtilFolderPath, String serviceFolderPath,
                                    ServiceDeployInfoDto serviceDeployInfo) {

        log.info("initialize class.")
        this.companyName = companyName;
        this.serviceName = serviceName;
        this.version = version;
        this.companyCommonUtilFolderPath = companyCommonUtilFolderPath;
        this.serviceFolderPath = serviceFolderPath;
        this.serviceDeployInfo = serviceDeployInfo;

        this.initializeFolderPath();

        this.initializeDowloadMapInfo();
        log.info("complete initialize class.")
    }

    private void initializeFolderPath(){

        this.companyCommonUtilScriptFolderPath = this.companyCommonUtilFolderPath + File.separator
                                                    + CompanyCommonUtilFileName.script.name();
        this.companyCommonUtilJdkFolderPath = this.companyCommonUtilFolderPath + File.separator
                                                    + CompanyCommonUtilFileName.jdk.name();
        this.companyCommonUtilStorageFolderPath = this.companyCommonUtilFolderPath + File.separator
                                                    + CompanyCommonUtilFileName.storage.name();

        this.serviceBinFolderPath = this.serviceFolderPath + File.separator + SrvDeployFileName.bin.name();
        this.serviceConfFolderPath = this.serviceFolderPath + File.separator + SrvDeployFileName.conf.name();
        this.serviceTargetFolderPath = this.serviceFolderPath + File.separator + SrvDeployFileName.target.name();

        log.info("complete initialize all paths.");
    }

    private void initializeDowloadMapInfo(){

        if(this.downloadInfoMapByType == null){
            this.downloadInfoMapByType = new ConcurrentHashMap<>();
        }

    }

    public OverallFileDownloadInfoVo generateFileDownloadInfoVo(){

        log.info("request to generate files download info.")

        this.downloadInfoMapByType.put(LocalDownloadedType.CompanyUtil, this.generateCommonUtilDownloadInfoVo());
        this.downloadInfoMapByType.put(LocalDownloadedType.Service, this.generateServiceDownloadInfoVo());

        log.info("complete generate files download info.")

        OverallFileDownloadInfoVo result = OverallFileDownloadInfoVo().builder().build();

        return result;
    }

    private ArrayList<FileDownloadInfoVo> generateCommonUtilDownloadInfoVo(){

        log.info("request to generate commont-util files download info.")
    }

    private ArrayList<FileDownloadInfoVo> generateServiceDownloadInfoVo(){

        log.info("request to generate service files download info. (jar/conf/vbs/bats)")

        ServiceDeployInfoDto serviceDeployInfo = this.dataAccess.getLatestVersion();


    }
}
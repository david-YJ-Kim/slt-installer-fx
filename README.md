
## 요구사항
- 크로스 플랫폼으로 사용자 PC에서 해당 프로젝트의 빌드 파일인 실행파일을 실행하여 특정 행위를 사용자 PC에서 수행
- Firebase에 저장된 데이터를 조회, 다운로드, 생성 및 삭제 기능을 수행
- 설치 화면이 있고, 유저의 데이터를 받을 수 있어야한다.
- 특정 폴더에 접근하고 다운로드한 파일을 unzip 하고 생성 및 삭제한다.
- registry 를 업데이트 하여 설치한 프로그램을 PC 기동 시 자동으로 시작되게 한다.


## 소프트웨어 요구사항
- JDK8로 개발
- Maven 사용

installer-app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── tsh.slt/
│   │   │           └── installer/
│   │   │               ├── Main.java
│   │   │               ├── Launcher.java
│   │   │               ├── controller/
│   │   │               │   └── InstallerController.java
│   │   │               ├── model/
│   │   │               │   ├── UserConfig.java
│   │   │               │   └── DownloadTask.java
│   │   │               ├── service/
│   │   │               │   ├── FirebaseService.java
│   │   │               │   ├── RegistryService.java
│   │   │               │   └── FileService.java
│   │   │               └── util/
│   │   │                   └── SystemUtils.java
│   │   └── resources/
│   │       ├── fxml/
│   │       │   ├── welcome.fxml
│   │       │   ├── userInfo.fxml
│   │       │   ├── installation.fxml
│   │       │   └── complete.fxml
│   │       ├── css/
│   │       │   └── styles.css
│   │       └── images/
│   │           └── logo.png
│   └── test/
│       └── java/
├── pom.xml
└── README.md
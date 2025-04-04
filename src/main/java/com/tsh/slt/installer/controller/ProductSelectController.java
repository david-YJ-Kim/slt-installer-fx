package com.tsh.slt.installer.controller;

import com.tsh.slt.installer.bizService.FirebaseStorageService;
import com.tsh.slt.installer.code.desings.ScreenSize;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class ProductSelectController extends BaseController {

    @FXML private ComboBox<String> environmentComboBox;
    @FXML private Button installButton;
    @FXML private Button cancelButton;
    @FXML private Pane rootPane;
    @FXML private Label titleLabel;
    @FXML private ProgressBar downloadProgressBar; // 새로 추가한 진행 표시줄
    @FXML private Label downloadStatusLabel; // 새로 추가한 상태 레이블
    @FXML private VBox mainContainer; // 메인 컨테이너 참조 추가

    private String userEmail;
    private FirebaseStorageService firebaseStorageService;

    @FXML
    public void initialize() {
        // 스타일 적용
        applyStyles();

        // ComboBox 초기화
        environmentComboBox.setItems(FXCollections.observableArrayList(
                "Window", "Mac", "Linux"
        ));
        environmentComboBox.setValue("Window"); // 기본값 설정

        // 버튼 이벤트 처리
        installButton.setOnAction(this::handleInstall);

        if (cancelButton != null) {
            cancelButton.setOnAction(this::handleCancel);
            // 공통 헤더를 사용하므로 취소 버튼 숨김
            cancelButton.setVisible(false);
            cancelButton.setManaged(false);
        }

        // 진행 표시줄 초기화 (처음에는 숨김)
        if (downloadProgressBar != null) {
            downloadProgressBar.setVisible(false);
            downloadProgressBar.setProgress(0);
        }

        // 상태 레이블 초기화
        if (downloadStatusLabel != null) {
            downloadStatusLabel.setVisible(false);
        }

        // BaseController의 공통 기능 초기화
        initializeCommon(rootPane);
    }


    @Override
    protected void applyStyles() {
        // 버튼 스타일 적용
        styleButtons(new Button[]{installButton}, new Button[]{cancelButton});

        // 콤보박스 스타일 적용
        styleComboBoxes(new ComboBox<?>[]{environmentComboBox});

        // 레이블 스타일 적용
        styleLabels(
                new Label[]{titleLabel},     // 헤더 레이블
                null,                        // 서브헤더 레이블
                new Label[]{downloadStatusLabel}  // 일반 레이블
        );

        // 진행 표시줄 스타일 적용
        if (downloadProgressBar != null) {
            styleProgressBars(new ProgressBar[]{downloadProgressBar});
        }

        // 컨테이너 스타일 적용
        styleContainers(rootPane, null);
    }

    @Override
    protected void navigateBack() {
        // 이전 화면이 로그인인 경우 뒤로가기는 작동하지 않습니다.
        // 이 조건은 BaseController와 HeaderComponent에서 처리
        // 하지만 만약의 경우를 위해 여기서도 체크
        if (previousIsLoginScreen) {
            System.out.println("이전 화면이 로그인 화면이므로 뒤로가기를 수행하지 않습니다.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Login.fxml"));

            if (loader.getLocation() == null) {
                loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));

                if (loader.getLocation() == null) {
                    throw new IOException("Login.fxml 파일을 찾을 수 없습니다.");
                }
            }

            Parent root = loader.load();
            Scene scene = new Scene(root, ScreenSize.WIDTH, ScreenSize.HEIGHT);

            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.centerOnScreen();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "오류", "로그인 화면을 로드할 수 없습니다: " + e.getMessage());
        }
    }

    /**
     * 사용자 이메일 설정
     * @param email 사용자 이메일
     */
    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    @FXML
    private void handleInstall(ActionEvent event) {
        String selectedEnvironment = environmentComboBox.getValue();
        if (selectedEnvironment == null || selectedEnvironment.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "오류", "환경을 선택해주세요.");
            return;
        }

        // 버튼 비활성화
        installButton.setDisable(true);
        installButton.setText("다운로드 중...");

        // 진행 표시줄 표시
        if (downloadProgressBar != null) {
            downloadProgressBar.setVisible(true);
            downloadProgressBar.setProgress(0);
        }

        // 상태 레이블 표시
        if (downloadStatusLabel != null) {
            downloadStatusLabel.setVisible(true);
            downloadStatusLabel.setText("다운로드 준비 중...");
        }

        // 선택된 환경에 따라 다운로드 URL 결정
        String downloadUrl = getDownloadUrlForEnvironment(selectedEnvironment);
        String fileName = selectedEnvironment.toLowerCase() + "_installer.exe";

        // 다운로드 경로 설정 (임시 디렉토리 사용)
        String tempDir = System.getProperty("java.io.tmpdir");
        String localFilePath = tempDir + File.separator + fileName;

        // 비동기적으로 파일 다운로드 실행
        CompletableFuture.runAsync(() -> {
            try {
                File downloadedFile;

                // 진행률 추적 가능한 다운로드 시도
                updateDownloadStatus("다운로드 시작...", 0.05);

                if (firebaseStorageService != null) {
                    try {
                        // Firebase Storage 서비스 초기화 성공한 경우 - 진행률 추적 다운로드
                        downloadedFile = firebaseStorageService.downloadFileWithProgress(
                                downloadUrl,
                                localFilePath,
                                progress -> updateDownloadStatus("다운로드 중... " + String.format("%.1f%%", progress * 100), progress)
                        );
                    } catch (Exception e) {
                        // Firebase SDK를 사용한 다운로드 실패 시 URL 방식으로 대체
                        updateDownloadStatus("Firebase 다운로드 실패, URL 방식으로 재시도 중...", 0.1);
                        downloadedFile = downloadFileFromUrl(downloadUrl, localFilePath);
                    }
                } else {
                    // Firebase 초기화 실패한 경우 - URL 방식으로 진행
                    downloadedFile = downloadFileFromUrl(downloadUrl, localFilePath);
                }

                // 다운로드 완료 상태 업데이트
                updateDownloadStatus("다운로드 완료. 설치 준비 중...", 1.0);

                // 약간의 지연 추가 (UI 효과)
                Thread.sleep(1000);

                // UI 업데이트
                File finalDownloadedFile = downloadedFile;
                Platform.runLater(() -> {
                    try {
                        // 다운로드 상태 표시 숨김
                        if (downloadProgressBar != null) {
                            downloadProgressBar.setVisible(false);
                        }
                        if (downloadStatusLabel != null) {
                            downloadStatusLabel.setVisible(false);
                        }

                        // 버튼 상태 복원
                        installButton.setDisable(false);
                        installButton.setText("Install");

                        // 설치 화면으로 이동
                        loadInstallerScreen(finalDownloadedFile);
                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, "UI 업데이트 오류", "화면 전환 중 오류가 발생했습니다: " + e.getMessage());
                    }
                });

            } catch (Exception e) {
                // 오류 처리
                Platform.runLater(() -> {
                    // 진행 표시줄 숨김
                    if (downloadProgressBar != null) {
                        downloadProgressBar.setVisible(false);
                    }
                    if (downloadStatusLabel != null) {
                        downloadStatusLabel.setVisible(false);
                    }

                    // 버튼 상태 복원
                    installButton.setDisable(false);
                    installButton.setText("Install");

                    showAlert(Alert.AlertType.ERROR, "다운로드 오류",
                            "파일 다운로드 중 오류가 발생했습니다: " + e.getMessage());
                });
            }
        });
    }

    /**
     * 다운로드 상태 업데이트 (UI 스레드에서 실행)
     * @param status 상태 메시지
     * @param progress 진행률 (0.0 ~ 1.0)
     */
    private void updateDownloadStatus(String status, double progress) {
        Platform.runLater(() -> {
            if (downloadStatusLabel != null) {
                downloadStatusLabel.setText(status);
            }
            if (downloadProgressBar != null) {
                downloadProgressBar.setProgress(progress);
            }
        });
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow(installButton);
    }

    /**
     * 환경에 따른 다운로드 URL 반환
     * @param environment 선택된 환경
     * @return 다운로드 URL
     */
    private String getDownloadUrlForEnvironment(String environment) {
        // Firebase Storage URL
        // 실제 URL로 변경 필요
        switch (environment) {
            case "Window":
                return "https://firebasestorage.googleapis.com/v0/b/your-project-id.appspot.com/o/installers%2Fwindows_installer.exe?alt=media";
            case "Mac":
                return "https://firebasestorage.googleapis.com/v0/b/your-project-id.appspot.com/o/installers%2Fmac_installer.dmg?alt=media";
            case "Linux":
                return "https://firebasestorage.googleapis.com/v0/b/your-project-id.appspot.com/o/installers%2Flinux_installer.sh?alt=media";
            default:
                return "https://firebasestorage.googleapis.com/v0/b/your-project-id.appspot.com/o/installers%2Fdefault_installer.exe?alt=media";
        }
    }

    /**
     * URL을 사용하여 파일 다운로드 (Firebase 초기화 실패 시 사용)
     */
    private File downloadFileFromUrl(String url, String localFilePath) throws IOException {
        try {
            // 다운로드 시작 상태 업데이트
            updateDownloadStatus("URL에서 다운로드 시작...", 0.1);

            // URL 연결 설정
            java.net.URL downloadUrl = new java.net.URL(url);
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) downloadUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // 파일 크기 확인 (HTTP 헤더에서)
            int fileSize = connection.getContentLength();

            // 입력 스트림 열기
            java.io.InputStream inputStream = connection.getInputStream();

            // 로컬 파일 생성
            File targetFile = new File(localFilePath);

            // 필요한 경우 상위 디렉토리 생성
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }

            // 파일 출력 스트림 생성
            java.io.FileOutputStream outputStream = new java.io.FileOutputStream(targetFile);

            // 버퍼 생성
            byte[] buffer = new byte[4096];
            int bytesRead;
            int totalBytesRead = 0;

            // 상태 업데이트 빈도 제한 (UI 스레드 부하 방지)
            long lastUpdateTime = System.currentTimeMillis();

            // 데이터 읽기 및 진행 상황 추적
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                // 진행률 계산 및 업데이트 (100ms마다)
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastUpdateTime > 100) {
                    if (fileSize > 0) {
                        double progress = (double) totalBytesRead / fileSize;
                        updateDownloadStatus("다운로드 중... " + String.format("%.1f%%", progress * 100), progress);
                    } else {
                        // 파일 크기를 알 수 없는 경우 진행 표시줄 펄스 모드 사용
                        updateDownloadStatus("다운로드 중... (" + formatFileSize(totalBytesRead) + " 다운로드됨)", -1);
                    }
                    lastUpdateTime = currentTime;
                }
            }

            // 리소스 정리
            outputStream.close();
            inputStream.close();

            return targetFile;
        } catch (Exception e) {
            System.err.println("URL에서 파일 다운로드 중 오류 발생: " + e.getMessage());
            throw new IOException("파일 다운로드 실패: " + e.getMessage(), e);
        }
    }

    /**
     * 파일 크기를 사람이 읽기 쉬운 형식으로 포맷팅
     */
    private String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", bytes / (1024.0 * 1024));
        } else {
            return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
        }
    }

    /**
     * 설치 화면으로 이동
     * @param installerFile 다운로드된 인스톨러 파일
     */
    private void loadInstallerScreen(File installerFile) {
        try {
            String fxmlPath = "fxml/InstallerView.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));

            if (loader.getLocation() == null) {
                loader = new FXMLLoader(getClass().getResource("/fxml/InstallerView.fxml"));

                if (loader.getLocation() == null) {
                    throw new IOException("InstallerView.fxml 파일을 찾을 수 없습니다.");
                }
            }

            Parent root = loader.load();
            Scene scene = new Scene(root, ScreenSize.WIDTH, ScreenSize.HEIGHT);

            Stage currentStage = (Stage) installButton.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.centerOnScreen();

            // 컨트롤러에 데이터 전달
            InstallerController controller = loader.getController();
            controller.setUserEmail(userEmail);
            controller.setInstallerFile(installerFile);

            // 이전 화면이 로그인 화면이 아님을 설정 (제품 선택 화면은 로그인 화면이 아니므로)
            controller.setPreviousIsLoginScreen(false);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "오류", "설치 화면을 로드할 수 없습니다: " + e.getMessage());
        }
    }

    /**
     * 알림 대화상자를 표시하는 헬퍼 메서드
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
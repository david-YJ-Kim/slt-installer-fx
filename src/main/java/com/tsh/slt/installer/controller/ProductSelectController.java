package com.tsh.slt.installer.controller;

import com.tsh.slt.installer.code.desings.ScreenSize;
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
import javafx.scene.layout.Pane;
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

    private String userEmail;

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
                null                         // 일반 레이블
        );

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

        // 비동기적으로 파일 다운로드 실행
        CompletableFuture.runAsync(() -> {
            try {
                // 선택된 환경에 따라 다운로드 URL 결정
                String downloadUrl = getDownloadUrlForEnvironment(selectedEnvironment);

                // 파일 다운로드
                File downloadedFile = downloadFile(downloadUrl, selectedEnvironment.toLowerCase() + "_installer.exe");

                // UI 업데이트
                javafx.application.Platform.runLater(() -> {
                    installButton.setDisable(false);
                    installButton.setText("Install");

                    // 설치 화면으로 이동
                    loadInstallerScreen(downloadedFile);
                });

            } catch (Exception e) {
                // 오류 처리
                javafx.application.Platform.runLater(() -> {
                    installButton.setDisable(false);
                    installButton.setText("Install");
                    showAlert(Alert.AlertType.ERROR, "다운로드 오류", "파일 다운로드 중 오류가 발생했습니다: " + e.getMessage());
                });
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
        // Firebase Storage URL 예시
        // 실제 구현 시 Firebase SDK를 사용하여 동적으로 URL을 가져오는 것이 좋습니다.
        switch (environment) {
            case "Window":
                return "https://firebasestorage.googleapis.com/your-storage/windows_installer.exe";
            case "Mac":
                return "https://firebasestorage.googleapis.com/your-storage/mac_installer.dmg";
            case "Linux":
                return "https://firebasestorage.googleapis.com/your-storage/linux_installer.sh";
            default:
                return "https://firebasestorage.googleapis.com/your-storage/default_installer.exe";
        }
    }

    /**
     * 파일 다운로드 메서드
     * @param url 다운로드 URL
     * @param fileName 저장할 파일 이름
     * @return 다운로드된 파일
     * @throws IOException 다운로드 중 오류 발생 시
     */
    private File downloadFile(String url, String fileName) throws IOException {
        // 테스트 목적으로 임시 파일 생성 (실제 구현 시 실제 다운로드 로직으로 대체)
        File tempFile = File.createTempFile("download-", fileName);

        // 실제 다운로드 코드 (예시)
        /*
        URL website = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        */

        // 테스트를 위해 약간의 지연 추가
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return tempFile;
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
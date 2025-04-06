package com.tsh.slt.installer.controller;

import com.tsh.slt.installer.code.desings.ScreenSize;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class InstallerController extends BaseController {

    @FXML private TextField installPathField;
    @FXML private CheckBox createShortcutCheckbox;
    @FXML private ProgressBar progressBar;
    @FXML private Button installButton;
    @FXML private Button browseButton;
    @FXML private Button closeButton;
    @FXML private Label statusLabel;
    @FXML private Label pathLabel;
    @FXML private Pane rootPane; // 최상위 컨테이너

    private String userEmail; // 사용자 이메일 저장
    private File installerFile; // 다운로드된 인스톨러 파일

    @FXML
    public void initialize() {
        // 스타일 적용
        applyStyles();

        // 컴포넌트 초기화
        installPathField.setText(getDefaultInstallPath());
        createShortcutCheckbox.setSelected(true);
        progressBar.setVisible(false);

        // 이벤트 핸들러 설정
        browseButton.setOnAction(e -> openDirectoryChooser());
        installButton.setOnAction(e -> startInstallation());

        // 기존 닫기 버튼은 표시하지 않음 (헤더의 닫기 아이콘으로 대체)
        if (closeButton != null) {
            closeButton.setVisible(false);
            closeButton.setManaged(false);
        }

        // BaseController의 공통 기능 초기화
        initializeCommon(rootPane);
    }

    @Override
    protected void applyStyles() {
        // 버튼 스타일 적용
        styleButtons(new Button[]{installButton, browseButton}, null);

        // 텍스트 필드 스타일 적용
        styleTextFields(new TextField[]{installPathField});

        // 레이블 스타일 적용
        styleLabels(
                null,                       // 헤더 레이블
                null,                       // 서브헤더 레이블
                new Label[]{statusLabel, pathLabel}  // 일반 레이블
        );

        // 체크박스 스타일 적용
        styleCheckBoxes(new CheckBox[]{createShortcutCheckbox});

        // 진행바 스타일 적용
        styleProgressBars(new ProgressBar[]{progressBar});

        // 컨테이너 스타일 적용
        styleContainers(rootPane, null);
    }

    @Override
    protected void navigateBack() {
        // 제품 선택 화면으로 돌아가기
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProductSelect.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, ScreenSize.WIDTH, ScreenSize.HEIGHT);

            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.centerOnScreen();

            // 제품 선택 컨트롤러에 사용자 이메일 전달
            ProductSelectController controller = loader.getController();
            controller.setUserEmail(userEmail);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "오류", "제품 선택 화면을 로드할 수 없습니다: " + e.getMessage());
        }
    }

    /**
     * 사용자 이메일 설정
     * @param email 사용자 이메일
     */
    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    /**
     * 설치 파일 설정
     * @param file 다운로드된 설치 파일
     */
    public void setInstallerFile(File file) {
        this.installerFile = file;

        // 설치 파일 정보를 상태 레이블에 표시
        if (file != null) {
            statusLabel.setText("설치 파일 준비됨: " + file.getName());
        }
    }

    private void openDirectoryChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("설치 경로 선택");
        Stage stage = (Stage) installPathField.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            installPathField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private String getDefaultInstallPath() {
        return System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Local" + File.separator + "DemoApp";
    }


    /**
     * 설치 프로세스 시작
     */
    private void startInstallation() {
        // 설치 파일이 없는 경우 오류 표시
        if (installerFile == null) {
            showAlert(Alert.AlertType.ERROR, "오류", "설치 파일이 준비되지 않았습니다. 제품 선택 화면으로 돌아가 다시 시도해주세요.");
            return;
        }

        // 설치 버튼 비활성화
        installButton.setDisable(true);
        progressBar.setVisible(true);

        // 설치 프로세스 시뮬레이션
        Thread installThread = new Thread(() -> {
            try {
                String installPath = installPathField.getText();
                updateStatus("설치 경로 확인 중...");
                updateProgress(0.1);
                Thread.sleep(500);

                // 디렉토리 생성
                updateStatus("설치 디렉토리 생성 중...");
                updateProgress(0.2);
                Path installDir = Paths.get(installPath);
                if (!Files.exists(installDir)) {
                    Files.createDirectories(installDir);
                }
                Thread.sleep(1000);

                // 설치 파일 복사
                updateStatus("설치 파일 복사 중...");
                updateProgress(0.3);
                Path targetPath = installDir.resolve(installerFile.getName());
                Files.copy(installerFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                Thread.sleep(1000);

                // 추가 파일 설치 시뮬레이션
                updateStatus("추가 파일 설치 중...");
                for (int i = 4; i <= 8; i++) {
                    updateProgress(i / 10.0);
                    Thread.sleep(500);
                }


                // 완료
                updateProgress(1.0);
                updateStatus("설치가 완료되었습니다!");

                // UI 상태 업데이트
                javafx.application.Platform.runLater(() -> {
                    installButton.setText("완료");
                    installButton.setDisable(false);
                });

            } catch (Exception e) {
                updateStatus("설치 중 오류 발생: " + e.getMessage());
                javafx.application.Platform.runLater(() -> {
                    installButton.setDisable(false);
                });
            }
        });

        installThread.start();
    }

    private void updateProgress(double progress) {
        javafx.application.Platform.runLater(() -> progressBar.setProgress(progress));
    }

    private void updateStatus(String message) {
        javafx.application.Platform.runLater(() -> statusLabel.setText(message));
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
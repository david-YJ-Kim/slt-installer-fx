package com.tsh.slt.installer.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InstallerController extends BaseController {

    @FXML private TextField installPathField;
    @FXML private CheckBox createShortcutCheckbox;
    @FXML private ProgressBar progressBar;
    @FXML private Button installButton;
    @FXML private Button browseButton;
    @FXML private Button closeButton;
    @FXML private Label statusLabel;
    @FXML private Pane rootPane; // 최상위 컨테이너

    private String userEmail; // 사용자 이메일 저장

    @FXML
    public void initialize() {
        // 컴포넌트 초기화
        installPathField.setText(getDefaultInstallPath());
        createShortcutCheckbox.setSelected(true);
        progressBar.setVisible(false);

        // 이벤트 핸들러 설정
        browseButton.setOnAction(e -> openDirectoryChooser());
        installButton.setOnAction(e -> startInstallation());
        closeButton.setOnAction(e -> closeWindow(closeButton));

        // BaseController의 공통 기능 초기화
        initializeCommon(rootPane);
    }

    @Override
    protected void initializeCommon(Parent root) {
        // 창 드래그 기능 활성화
        enableWindowDrag(root);
    }

    /**
     * 사용자 이메일 설정
     * @param email 사용자 이메일
     */
    public void setUserEmail(String email) {
        this.userEmail = email;
        // 필요한 경우 이메일을 사용하여 화면 초기화
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

    private void startInstallation() {
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

                // 파일 복사 시뮬레이션
                updateStatus("파일 복사 중...");
                for (int i = 3; i <= 8; i++) {
                    updateProgress(i / 10.0);
                    Thread.sleep(500);
                }

                // 바로가기 생성
                if (createShortcutCheckbox.isSelected()) {
                    updateStatus("바탕 화면 바로가기 생성 중...");
                    updateProgress(0.9);
                    Thread.sleep(500);
                    // 실제로는 여기서 바로가기 생성 코드가 들어갑니다
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
}
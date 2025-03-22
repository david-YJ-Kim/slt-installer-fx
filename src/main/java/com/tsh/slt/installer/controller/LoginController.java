package com.tsh.slt.installer.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.tsh.slt.installer.codes.desings.ScreenSize;

public class LoginController extends BaseController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink signInLink;

    @FXML
    private Button loginButton;

    @FXML
    private Button closeButton; // 닫기 버튼 추가

    @FXML
    private Pane rootPane; // 최상위 컨테이너

    private static final String SIGN_UP_URL = "https://www.tsh.com/signup"; // 회원가입 페이지 URL 설정

    @FXML
    private void initialize() {
        // 기존 초기화 코드

        // 닫기 버튼 이벤트 핸들러 등록
        if (closeButton != null) {
            closeButton.setOnAction(this::handleClose);
        }

        // BaseController의 공통 기능 초기화
        initializeCommon(rootPane);
    }

    @Override
    protected void initializeCommon(Parent root) {
        // 창 드래그 기능 활성화
        enableWindowDrag(root);
    }

    @FXML
    private void handleClose(ActionEvent event) {
        closeWindow(loginButton);
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // 이메일과 비밀번호 유효성 검사
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "로그인 오류", "이메일과 비밀번호를 모두 입력해주세요.");
            return;
        }

        // 로그인 로직 수행
        boolean loginSuccess = performLogin(email, password);

        if (loginSuccess) {
            // 로그인 성공 시 다음 화면으로 이동하거나 필요한 작업 수행
            showAlert(Alert.AlertType.INFORMATION, "로그인 성공", "환영합니다!");

            // 로그인 성공 후 메인 화면으로 이동
            loadMainScreen();
        } else {
            showAlert(Alert.AlertType.ERROR, "로그인 실패", "이메일 또는 비밀번호가 올바르지 않습니다.");
        }
    }

    @FXML
    private void handleSignInLink(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URI(SIGN_UP_URL));
        } catch (IOException | URISyntaxException e) {
            showAlert(Alert.AlertType.ERROR, "오류", "회원가입 페이지를 열 수 없습니다: " + e.getMessage());
        }
    }

    /**
     * 실제 로그인 처리를 수행하는 메서드
     * @param email 사용자 이메일
     * @param password 사용자 비밀번호
     * @return 로그인 성공 여부
     */
    private boolean performLogin(String email, String password) {
        // 테스트용 로직 (실제 애플리케이션에서는 제거할 것)
        return email.equals("david@tsh.com") && password.length() > 0;
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

    /**
     * 메인 화면으로 이동하는 메서드
     */
    private void loadMainScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InstallerView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, ScreenSize.WIDTH, ScreenSize.HEIGHT);

            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.centerOnScreen();

            // 필요한 경우 컨트롤러에 데이터 전달
            InstallerController controller = loader.getController();
            controller.setUserEmail(emailField.getText());
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "오류", "메인 화면을 로드할 수 없습니다: " + e.getMessage());
        }
    }
}
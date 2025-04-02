package com.tsh.slt.installer.controller;

import com.tsh.slt.installer.code.FxmlFileConstants;
import com.tsh.slt.installer.code.styles.LoginStyles;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.tsh.slt.installer.code.desings.ScreenSize;

public class LoginController extends BaseController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Hyperlink signInLink;
    @FXML private Button loginButton;
    @FXML private Button closeButton;
    @FXML private Pane rootPane;
    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;
    @FXML private Label signupInfoLabel;

    private static final String SIGN_UP_URL = "https://www.tsh.com/signup";

    @FXML
    private void initialize() {
        // 로그인 화면임을 표시
        super.isLoginScreen = true;

        // 스타일 적용
        applyStyles();

        // 닫기 버튼 이벤트 핸들러 등록
        if (closeButton != null) {
            closeButton.setOnAction(this::handleClose);
        }

        // BaseController의 공통 기능 초기화
        initializeCommon(rootPane);
    }

    @Override
    protected void applyStyles() {
        // 전용 스타일 클래스를 사용하여 스타일 적용
        LoginStyles.applyRootContainerStyle(rootPane);

        // 메인 VBox 컨테이너 찾기 (rootPane의 첫 번째 자식이라고 가정)
        if (rootPane.getChildren().size() > 0 && rootPane.getChildren().get(0) instanceof VBox) {
            VBox mainContainer = (VBox) rootPane.getChildren().get(0);
            LoginStyles.applyMainContainerStyle(mainContainer);
        }

        // 제목과 부제목 스타일 적용
        LoginStyles.applyTitleStyle(titleLabel);
        LoginStyles.applySubtitleStyle(subtitleLabel);

        // 입력 필드 스타일 적용
        LoginStyles.applyInputFieldsStyle(emailField, passwordField);

        // 회원가입 관련 컨테이너 찾기
        for (javafx.scene.Node node : rootPane.lookupAll(".hbox")) {
            if (node instanceof HBox && ((HBox) node).getChildren().contains(signInLink)) {
                LoginStyles.applySignupContainerStyle((HBox) node, signupInfoLabel, signInLink);
                break;
            }
        }

        // 버튼 스타일 적용
        LoginStyles.applyLoginButtonStyle(loginButton);
        LoginStyles.applyCloseButtonStyle(closeButton);

        // 버튼 컨테이너 찾기
        for (javafx.scene.Node node : rootPane.lookupAll(".hbox")) {
            if (node instanceof HBox && ((HBox) node).getChildren().contains(loginButton)) {
                LoginStyles.applyButtonContainerStyle((HBox) node);
                break;
            }
        }
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
        // TODO 테스트를 위한 임시주석
//        if (email.isEmpty() || password.isEmpty()) {
//            showAlert(Alert.AlertType.ERROR, "로그인 오류", "이메일과 비밀번호를 모두 입력해주세요.");
//            return;
//        }

        // 로그인 로직 수행
        boolean loginSuccess = performLogin(email, password);

        if (loginSuccess) {
            // 로그인 성공 시 제품 선택 화면으로 이동
            loadProductSelectScreen();

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
//        return email.equals("david@tsh.com") && password.length() > 0;
        return true;
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
     * 제품 선택 화면으로 이동하는 메서드
     */
    private void loadProductSelectScreen() {
        try {
            // 클래스 로더를 사용하여 확실한 리소스 경로 지정
            String fxmlPath = FxmlFileConstants.PRODUCT_SELECT_FXML;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));

            if (loader.getLocation() == null) {
                // 경로를 찾을 수 없는 경우 다른 방법으로 시도
                loader = new FXMLLoader(getClass().getResource("/".concat(fxmlPath)));

                if (loader.getLocation() == null) {
                    throw new IOException("ProductSelect.fxml 파일을 찾을 수 없습니다.");
                }
            }

            Parent root = loader.load();
            Scene scene = new Scene(root, ScreenSize.WIDTH, ScreenSize.HEIGHT);

            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.centerOnScreen();

            // 제품 선택 컨트롤러에 사용자 이메일 전달 및 이전 화면이 로그인 화면임을 설정
            ProductSelectController controller = loader.getController();
            controller.setUserEmail(emailField.getText());
            controller.setPreviousIsLoginScreen(true); // 현재 화면은 로그인 화면이므로 true 전달
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "오류", "제품 선택 화면을 로드할 수 없습니다: " + e.getMessage());
        }
    }
}
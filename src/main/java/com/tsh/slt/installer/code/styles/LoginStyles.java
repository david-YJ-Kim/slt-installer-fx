package com.tsh.slt.installer.code.styles;

import com.tsh.slt.installer.code.desings.AppStyles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * 로그인 화면 전용 스타일 및 레이아웃 클래스
 */
public class LoginStyles {

    // 로그인 화면 특화 상수
    private static final double LOGIN_VBOX_SPACING = 20.0;
    private static final Insets LOGIN_PADDING = new Insets(50, 30, 50, 30);
    private static final double LOGIN_TITLE_BOTTOM_MARGIN = 30.0;

    /**
     * 로그인 화면의 루트 컨테이너에 스타일을 적용합니다.
     */
    public static void applyRootContainerStyle(Pane rootPane) {
        // 공통 스타일 적용
        AppStyles.applyRootPaneStyle(rootPane);

        // 로그인 화면 특화 스타일 (배경 이미지 등)
        rootPane.setStyle(rootPane.getStyle() +
                "-fx-background-image: url('/images/login_background.png');" +
                "-fx-background-size: cover;");
    }

    /**
     * 로그인 화면의 메인 컨테이너(VBox)에 스타일을 적용합니다.
     */
    public static void applyMainContainerStyle(VBox container) {
        // 간격 및 정렬 설정
        container.setSpacing(LOGIN_VBOX_SPACING);
        container.setPadding(LOGIN_PADDING);
        container.setAlignment(Pos.CENTER);

        // 최대 너비 설정
        container.setMaxWidth(450);

        // 반투명 배경 적용 (선택적)
        container.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9);" +
                "-fx-background-radius: 10px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);");
    }

    /**
     * 로그인 화면의 제목 레이블에 스타일을 적용합니다.
     */
    public static void applyTitleStyle(Label titleLabel) {
        AppStyles.applyHeaderLabelStyle(titleLabel);

        // 추가 스타일: 로그인 화면 제목 특화
        titleLabel.setStyle(titleLabel.getStyle() +
                "-fx-font-size: 32px;");

        // 여백 추가
        VBox.setMargin(titleLabel, new Insets(0, 0, LOGIN_TITLE_BOTTOM_MARGIN, 0));
    }

    /**
     * 로그인 화면의 부제목 레이블에 스타일을 적용합니다.
     */
    public static void applySubtitleStyle(Label subtitleLabel) {
        AppStyles.applySubHeaderLabelStyle(subtitleLabel);

        // 부제목 특화 스타일
        subtitleLabel.setStyle(subtitleLabel.getStyle() +
                "-fx-font-style: italic;");

        // 여백 추가
        VBox.setMargin(subtitleLabel, new Insets(0, 0, 20, 0));
    }

    /**
     * 로그인 화면의 입력 필드에 스타일을 적용합니다.
     */
    public static void applyInputFieldsStyle(TextField emailField, PasswordField passwordField) {
        // 기본 스타일 적용
        AppStyles.applyTextFieldStyle(emailField);
        AppStyles.applyPasswordFieldStyle(passwordField);

        // 너비 조정
        emailField.setPrefWidth(350);
        passwordField.setPrefWidth(350);

        // 아이콘 추가 (필요시)
        setTextFieldIcon(emailField, "email_icon.png");
        setTextFieldIcon(passwordField, "password_icon.png");

        // 여백 설정
        VBox.setMargin(emailField, new Insets(5, 0, 5, 0));
        VBox.setMargin(passwordField, new Insets(5, 0, 15, 0));
    }

    /**
     * 로그인 버튼에 스타일을 적용합니다.
     */
    public static void applyLoginButtonStyle(Button loginButton) {
        AppStyles.applyPrimaryButtonStyle(loginButton);

        // 로그인 버튼 특화 스타일
        loginButton.setPrefWidth(150);
        loginButton.setStyle(loginButton.getStyle() +
                "-fx-font-weight: bold;");

        // 여백 설정
        VBox.setMargin(loginButton, new Insets(10, 0, 0, 0));
    }

    /**
     * 닫기 버튼에 스타일을 적용합니다.
     */
    public static void applyCloseButtonStyle(Button closeButton) {
        AppStyles.applySecondaryButtonStyle(closeButton);

        // 닫기 버튼 크기 조정
        closeButton.setPrefWidth(100);
    }

    /**
     * 회원가입 관련 컨테이너에 스타일을 적용합니다.
     */
    public static void applySignupContainerStyle(HBox container, Label signupInfoLabel, Hyperlink signInLink) {
        // 컨테이너 정렬 및 간격
        container.setAlignment(Pos.CENTER);
        container.setSpacing(5);

        // 레이블 스타일
        AppStyles.applyNormalLabelStyle(signupInfoLabel);

        // 하이퍼링크 스타일
        AppStyles.applyHyperlinkStyle(signInLink);

        // 여백 설정
        VBox.setMargin(container, new Insets(20, 0, 10, 0));
    }

    /**
     * 버튼 컨테이너에 스타일을 적용합니다.
     */
    public static void applyButtonContainerStyle(HBox container) {
        container.setAlignment(Pos.CENTER);
        container.setSpacing(15);
    }

    /**
     * 텍스트 필드에 아이콘을 추가합니다 (옵션).
     */
    private static void setTextFieldIcon(TextField textField, String iconPath) {
        try {
            // 여기에 아이콘 추가 로직 구현
            // 예: ImageView 생성 후 HBox로 감싸기
        } catch (Exception e) {
            System.err.println("아이콘 로드 실패: " + e.getMessage());
        }
    }
}
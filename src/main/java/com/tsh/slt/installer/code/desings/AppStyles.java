package com.tsh.slt.installer.code.desings;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * 애플리케이션 전체에서 사용되는 공통 스타일 정의
 */
public class AppStyles {
    // 색상 상수
    public static final String PRIMARY_COLOR = "#2c3e50";
    public static final String SECONDARY_COLOR = "#3498db";
    public static final String BACKGROUND_COLOR = "#f0f0f4";
    public static final String TEXT_COLOR = "#333333";
    public static final String TEXT_LIGHT_COLOR = "#ffffff";
    public static final String ACCENT_COLOR = "#e74c3c";

    // 헤더 관련 색상 및 속성
    public static final String HEADER_BACKGROUND_COLOR = "rgba(44, 62, 80, 0.9)"; // 약간 불투명하게
    public static final String HEADER_HEIGHT = "30px";
    public static final String HEADER_BORDER_RADIUS = "0 0 5px 5px";
    public static final String HEADER_BOX_SHADOW = "0 2px 5px rgba(0, 0, 0, 0.2)";

    // 아이콘 크기
    public static final int ICON_SIZE = 20;

    // 폰트 크기
    public static final double HEADER_FONT_SIZE = 24.0;
    public static final double SUBHEADER_FONT_SIZE = 18.0;
    public static final double NORMAL_FONT_SIZE = 14.0;
    public static final double SMALL_FONT_SIZE = 12.0;

    // 여백 및 간격
    public static final double DEFAULT_SPACING = 10.0;
    public static final double LARGE_SPACING = 20.0;
    public static final double COMPONENT_WIDTH = 400.0;
    public static final double BUTTON_WIDTH = 100.0;

    // 헤더 스타일 적용
    public static void applyHeaderStyle(HBox header) {
        header.setStyle(
                "-fx-background-color: " + HEADER_BACKGROUND_COLOR + ";" +
                        "-fx-padding: 5px 10px;" +
                        "-fx-background-radius: " + HEADER_BORDER_RADIUS + ";" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);" +
                        "-fx-alignment: center-left;" +
                        "-fx-spacing: 10px;" +
                        "-fx-pref-height: " + HEADER_HEIGHT + ";" +
                        "-fx-max-height: " + HEADER_HEIGHT + ";" +
                        "-fx-min-width: 100%;" +
                        "-fx-pref-width: 100%;" +
                        "-fx-max-width: 100%;"
        );

        // HBox는 기본적으로 내용물에 맞춰서 크기가 조정됨
        // 명시적으로 최대 너비를 무한대로 설정하여 부모 컨테이너의 전체 너비를 사용하도록 함
        header.setMaxWidth(Double.MAX_VALUE);
    }

    // 아이콘 스타일 적용
    public static void applyIconStyle(ImageView icon) {
        icon.setFitWidth(ICON_SIZE);
        icon.setFitHeight(ICON_SIZE);
        icon.setPickOnBounds(true);
        icon.setPreserveRatio(true);
        icon.setStyle(
                "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 2, 0, 0, 1);"
        );
    }

    // 버튼 스타일
    public static void applyPrimaryButtonStyle(Button button) {
        button.setStyle(
                "-fx-background-color: " + PRIMARY_COLOR + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-min-width: " + BUTTON_WIDTH + "px;" +
                        "-fx-font-size: " + NORMAL_FONT_SIZE + "px;" +
                        "-fx-padding: 8px 15px;" +
                        "-fx-background-radius: 4px;"
        );
    }

    public static void applySecondaryButtonStyle(Button button) {
        button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: " + PRIMARY_COLOR + ";" +
                        "-fx-border-width: 1px;" +
                        "-fx-text-fill: " + PRIMARY_COLOR + ";" +
                        "-fx-min-width: " + BUTTON_WIDTH + "px;" +
                        "-fx-font-size: " + NORMAL_FONT_SIZE + "px;" +
                        "-fx-padding: 8px 15px;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-border-radius: 4px;"
        );
    }

    // 입력 필드 스타일
    public static void applyTextFieldStyle(TextField textField) {
        textField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #dddddd;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 4px;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-padding: 8px;" +
                        "-fx-font-size: " + NORMAL_FONT_SIZE + "px;" +
                        "-fx-max-width: " + COMPONENT_WIDTH + "px;"
        );
    }

    public static void applyPasswordFieldStyle(PasswordField passwordField) {
        passwordField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #dddddd;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 4px;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-padding: 8px;" +
                        "-fx-font-size: " + NORMAL_FONT_SIZE + "px;" +
                        "-fx-max-width: " + COMPONENT_WIDTH + "px;"
        );
    }

    // 레이블 스타일
    public static void applyHeaderLabelStyle(Label label) {
        label.setStyle(
                "-fx-font-size: " + HEADER_FONT_SIZE + "px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + PRIMARY_COLOR + ";"
        );
    }

    public static void applySubHeaderLabelStyle(Label label) {
        label.setStyle(
                "-fx-font-size: " + SUBHEADER_FONT_SIZE + "px;" +
                        "-fx-text-fill: " + TEXT_COLOR + ";"
        );
    }

    public static void applyNormalLabelStyle(Label label) {
        label.setStyle(
                "-fx-font-size: " + NORMAL_FONT_SIZE + "px;" +
                        "-fx-text-fill: " + TEXT_COLOR + ";"
        );
    }

    // 하이퍼링크 스타일
    public static void applyHyperlinkStyle(Hyperlink hyperlink) {
        hyperlink.setStyle(
                "-fx-font-size: " + NORMAL_FONT_SIZE + "px;" +
                        "-fx-text-fill: " + SECONDARY_COLOR + ";" +
                        "-fx-border-width: 0px;" +
                        "-fx-underline: true;"
        );
    }

    // 콤보박스 스타일
    public static void applyComboBoxStyle(ComboBox<?> comboBox) {
        comboBox.setStyle(
                "-fx-font-size: " + NORMAL_FONT_SIZE + "px;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: #dddddd;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 4px;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-min-width: 300px;"
        );
    }

    // 체크박스 스타일
    public static void applyCheckBoxStyle(CheckBox checkBox) {
        checkBox.setStyle(
                "-fx-font-size: " + NORMAL_FONT_SIZE + "px;" +
                        "-fx-text-fill: " + TEXT_COLOR + ";"
        );
    }

    // 진행 바 스타일
    public static void applyProgressBarStyle(ProgressBar progressBar) {
        progressBar.setStyle(
                "-fx-accent: " + SECONDARY_COLOR + ";" +
                        "-fx-background-color: #e0e0e0;" +
                        "-fx-background-radius: 2px;" +
                        "-fx-min-width: " + COMPONENT_WIDTH + "px;"
        );
    }

    // 컨테이너 스타일
    public static void applyRootPaneStyle(Pane pane) {
        pane.setStyle(
                "-fx-background-color: " + BACKGROUND_COLOR + ";"
        );
    }

    public static void applyContainerStyle(Pane pane) {
        pane.setStyle(
                "-fx-background-color: white;" +
                        "-fx-padding: " + LARGE_SPACING + "px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
    }
}
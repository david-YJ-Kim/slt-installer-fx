package com.tsh.slt.installer.code.styles;

import com.tsh.slt.installer.code.desings.AppStyles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * 설치 화면 전용 스타일 및 레이아웃 클래스
 */
public class InstallerStyles {

    // 설치 화면 특화 상수
    private static final double INSTALLER_VBOX_SPACING = 15.0;
    private static final Insets INSTALLER_PADDING = new Insets(30, 40, 40, 40);
    private static final double PATH_FIELD_WIDTH = 400.0;
    private static final double PROGRESS_BAR_HEIGHT = 25.0;

    /**
     * 설치 화면의 루트 컨테이너에 스타일을 적용합니다.
     */
    public static void applyRootContainerStyle(Pane rootPane) {
        // 공통 스타일 적용
        AppStyles.applyRootPaneStyle(rootPane);

        // 설치 화면 특화 배경
        rootPane.setStyle(rootPane.getStyle() +
                "-fx-background-color: linear-gradient(to bottom right, #f5f7fa, #e6e9f0);");
    }

    /**
     * 설치 화면의 메인 컨테이너에 스타일을 적용합니다.
     */
    public static void applyMainContainerStyle(VBox container) {
        // 간격 및 정렬 설정
        container.setSpacing(INSTALLER_VBOX_SPACING);
        container.setPadding(INSTALLER_PADDING);
        container.setAlignment(Pos.CENTER);

        // 배경 및 테두리 스타일
        container.setStyle("-fx-background-color: white;" +
                "-fx-background-radius: 12px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 3);");

        // 컨테이너 크기 조정
        container.setMaxWidth(650);
        container.setMaxHeight(550);
    }

    /**
     * 설치 경로 레이블에 스타일을 적용합니다.
     */
    public static void applyPathLabelStyle(Label pathLabel) {
        AppStyles.applyNormalLabelStyle(pathLabel);

        // 추가 스타일 (굵은 글씨)
        pathLabel.setStyle(pathLabel.getStyle() +
                "-fx-font-weight: bold;");

        // 여백 설정
        VBox.setMargin(pathLabel, new Insets(10, 0, 5, 0));
    }

    /**
     * 설치 경로 입력 필드와 찾아보기 버튼 컨테이너에 스타일을 적용합니다.
     */
    public static void applyPathSelectorStyle(HBox container, TextField pathField, Button browseButton) {
        // 컨테이너 설정
        container.setSpacing(10);
        container.setAlignment(Pos.CENTER);

        // 텍스트 필드 스타일 적용
        AppStyles.applyTextFieldStyle(pathField);
        pathField.setPrefWidth(PATH_FIELD_WIDTH);

        // 찾아보기 버튼 스타일 적용
        AppStyles.applySecondaryButtonStyle(browseButton);
        browseButton.setPrefWidth(100);

        // 여백 설정
        VBox.setMargin(container, new Insets(0, 0, 10, 0));
    }

    /**
     * 체크박스에 스타일을 적용합니다.
     */
    public static void applyCheckBoxStyle(CheckBox checkBox) {
        AppStyles.applyCheckBoxStyle(checkBox);

        // 여백 설정
        VBox.setMargin(checkBox, new Insets(10, 0, 15, 0));
    }

    /**
     * 구분선에 스타일을 적용합니다.
     */
    public static void applySeparatorStyle(Separator separator) {
        separator.setStyle("-fx-opacity: 0.7;");

        // 여백 설정
        VBox.setMargin(separator, new Insets(5, 0, 15, 0));
    }

    /**
     * 진행 바에 스타일을 적용합니다.
     */
    public static void applyProgressBarStyle(ProgressBar progressBar) {
        // 기본 스타일 적용
        AppStyles.applyProgressBarStyle(progressBar);

        // 크기 및 추가 스타일
        progressBar.setPrefWidth(PATH_FIELD_WIDTH + 110); // 경로 필드 + 버튼 너비 맞춤
        progressBar.setPrefHeight(PROGRESS_BAR_HEIGHT);
        progressBar.setStyle(progressBar.getStyle() +
                "-fx-background-radius: 4px;");

        // 여백 설정
        VBox.setMargin(progressBar, new Insets(5, 0, 5, 0));
    }

    /**
     * 상태 레이블에 스타일을 적용합니다.
     */
    public static void applyStatusLabelStyle(Label statusLabel) {
        AppStyles.applyNormalLabelStyle(statusLabel);

        // 추가 스타일
        statusLabel.setStyle(statusLabel.getStyle() +
                "-fx-text-fill: " + AppStyles.SECONDARY_COLOR + ";");

        // 여백 설정
        VBox.setMargin(statusLabel, new Insets(0, 0, 15, 0));
    }

    /**
     * 버튼 컨테이너에 스타일을 적용합니다.
     */
    public static void applyButtonContainerStyle(HBox container) {
        container.setSpacing(15);
        container.setAlignment(Pos.CENTER);

        // 여백 설정
        VBox.setMargin(container, new Insets(10, 0, 0, 0));
    }

    /**
     * 설치 버튼에 스타일을 적용합니다.
     */
    public static void applyInstallButtonStyle(Button installButton) {
        AppStyles.applyPrimaryButtonStyle(installButton);

        // 버튼 크기 조정
        installButton.setPrefWidth(180);
        installButton.setPrefHeight(45);

        // 추가 스타일
        installButton.setStyle(installButton.getStyle() +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 15px;");
    }

    /**
     * 닫기 버튼에 스타일을 적용합니다.
     */
    public static void applyCloseButtonStyle(Button closeButton) {
        AppStyles.applySecondaryButtonStyle(closeButton);

        // 버튼 크기 조정
        closeButton.setPrefWidth(120);
        closeButton.setPrefHeight(45);
    }

    /**
     * 설치 진행 중 상태에 따른 스타일을 적용합니다.
     */
    public static void applyInstallationProgressStyle(Label statusLabel, double progress) {
        // 진행률에 따라 다른 색상 적용
        if (progress < 0.3) {
            statusLabel.setStyle("-fx-text-fill: " + AppStyles.SECONDARY_COLOR + ";" +
                    "-fx-font-size: " + AppStyles.NORMAL_FONT_SIZE + "px;");
        } else if (progress < 0.7) {
            statusLabel.setStyle("-fx-text-fill: #f39c12;" + // 주황색
                    "-fx-font-size: " + AppStyles.NORMAL_FONT_SIZE + "px;");
        } else if (progress < 1.0) {
            statusLabel.setStyle("-fx-text-fill: #27ae60;" + // 녹색
                    "-fx-font-size: " + AppStyles.NORMAL_FONT_SIZE + "px;");
        } else {
            // 완료 상태
            statusLabel.setStyle("-fx-text-fill: #27ae60;" + // 녹색
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: " + (AppStyles.NORMAL_FONT_SIZE + 2) + "px;");
        }
    }

    /**
     * 설치 완료 상태의 버튼 스타일을 적용합니다.
     */
    public static void applyCompletedInstallButtonStyle(Button installButton) {
        installButton.setStyle("-fx-background-color: #27ae60;" + // 녹색
                "-fx-text-fill: white;" +
                "-fx-min-width: 180px;" +
                "-fx-font-size: 15px;" +
                "-fx-padding: 8px 15px;" +
                "-fx-background-radius: 4px;" +
                "-fx-font-weight: bold;");
    }
}
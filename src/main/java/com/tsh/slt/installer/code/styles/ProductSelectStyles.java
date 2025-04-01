package com.tsh.slt.installer.code.styles;

import com.tsh.slt.installer.code.desings.AppStyles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * 제품 선택 화면 전용 스타일 및 레이아웃 클래스
 */
public class ProductSelectStyles {

    // 제품 선택 화면 특화 상수
    private static final double PRODUCT_SELECT_VBOX_SPACING = 40.0;
    private static final Insets PRODUCT_SELECT_PADDING = new Insets(40, 30, 30, 30);
    private static final double TITLE_BOTTOM_MARGIN = 25.0;

    /**
     * 제품 선택 화면의 루트 컨테이너에 스타일을 적용합니다.
     */
    public static void applyRootContainerStyle(Pane rootPane) {
        // 공통 스타일 적용
        AppStyles.applyRootPaneStyle(rootPane);

        // 제품 선택 화면 특화 스타일
        rootPane.setStyle(rootPane.getStyle() +
                "-fx-background-color: linear-gradient(to bottom, #f0f0f4, #e4e6f0);");
    }

    /**
     * 제품 선택 화면의 메인 컨테이너에 스타일을 적용합니다.
     */
    public static void applyMainContainerStyle(VBox container) {
        // 간격 및 정렬 설정
        container.setSpacing(PRODUCT_SELECT_VBOX_SPACING);
        container.setPadding(PRODUCT_SELECT_PADDING);
        container.setAlignment(Pos.CENTER);

        // 배경 및 테두리 스타일
        container.setStyle("-fx-background-color: white;" +
                "-fx-background-radius: 15px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 5);");

        // 컨테이너 크기 조정
        container.setMaxWidth(700);
        container.setMaxHeight(500);
        container.setPrefHeight(Region.USE_COMPUTED_SIZE);
    }

    /**
     * 제품 선택 제목에 스타일을 적용합니다.
     */
    public static void applyTitleStyle(Label titleLabel) {
        AppStyles.applyHeaderLabelStyle(titleLabel);

        // 추가 스타일
        titleLabel.setStyle(titleLabel.getStyle() +
                "-fx-font-size: 28px;");

        // 여백 설정
        VBox.setMargin(titleLabel, new Insets(10, 0, TITLE_BOTTOM_MARGIN, 0));
    }

    /**
     * 환경 선택 콤보박스에 스타일을 적용합니다.
     */
    public static void applyEnvironmentComboBoxStyle(ComboBox<String> comboBox) {
        // 기본 스타일 적용
        AppStyles.applyComboBoxStyle(comboBox);

        // 콤보박스 크기 조정
        comboBox.setPrefWidth(400);

        // 추가 스타일
        comboBox.setStyle(comboBox.getStyle() +
                "-fx-background-radius: 6px;" +
                "-fx-border-radius: 6px;");

        // 여백 설정
        VBox.setMargin(comboBox, new Insets(20, 0, 20, 0));

        // 드롭다운 아이템 스타일 설정
        comboBox.setButtonCell(getStyledListCell());
        comboBox.setCellFactory(param -> getStyledListCell());
    }

    /**
     * 스타일이 적용된 ListCell을 반환합니다.
     */
    private static ListCell<String> getStyledListCell() {
        return new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-font-size: " + AppStyles.NORMAL_FONT_SIZE + "px;" +
                            "-fx-padding: 8 10;");
                }
            }
        };
    }

    /**
     * 버튼 컨테이너에 스타일을 적용합니다.
     */
    public static void applyButtonContainerStyle(HBox container) {
        container.setAlignment(Pos.CENTER_RIGHT);
        container.setSpacing(20);

        // 여백 설정
        VBox.setMargin(container, new Insets(30, 0, 0, 0));
        HBox.setMargin(container, new Insets(0, 40, 0, 0));
    }

    /**
     * 설치 버튼에 스타일을 적용합니다.
     */
    public static void applyInstallButtonStyle(Button installButton) {
        AppStyles.applyPrimaryButtonStyle(installButton);

        // 버튼 크기 조정
        installButton.setPrefWidth(150);
        installButton.setPrefHeight(40);

        // 추가 스타일
        installButton.setStyle(installButton.getStyle() +
                "-fx-font-weight: bold;");
    }

    /**
     * 취소 버튼에 스타일을 적용합니다.
     */
    public static void applyCancelButtonStyle(Button cancelButton) {
        AppStyles.applySecondaryButtonStyle(cancelButton);

        // 버튼 크기 조정
        cancelButton.setPrefWidth(120);
        cancelButton.setPrefHeight(40);
    }

    /**
     * 로딩 상태 표시에 스타일을 적용합니다 (다운로드 중).
     */
    public static void applyLoadingStateStyle(Button button, ProgressIndicator indicator) {
        // 버튼 비활성화 스타일
        button.setDisable(true);
        button.setStyle(button.getStyle() +
                "-fx-opacity: 0.8;");

        // 진행 표시기 스타일
        indicator.setStyle("-fx-progress-color: " + AppStyles.SECONDARY_COLOR + ";");
        indicator.setPrefSize(20, 20);

        // 버튼 내용 업데이트 (텍스트 + 인디케이터)
        HBox content = new HBox(10);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(new Label("다운로드 중..."), indicator);
        button.setGraphic(content);
        button.setText("");
    }

    /**
     * 버튼을 기본 상태로 복원합니다.
     */
    public static void restoreButtonStyle(Button button, String text) {
        button.setDisable(false);
        button.setGraphic(null);
        button.setText(text);

        // 원래 스타일 다시 적용
        applyInstallButtonStyle(button);
    }
}
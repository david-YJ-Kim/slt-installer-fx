package com.tsh.slt.installer.component;

import com.tsh.slt.installer.code.desings.AppStyles;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;

/**
 * 공통 헤더 컴포넌트 클래스
 * 모든 화면(로그인 화면 제외)에서 사용할 수 있는 헤더를 제공합니다.
 */
public class HeaderComponent {

    @FXML private HBox headerContainer;
    @FXML private ImageView backButton;
    @FXML private ImageView closeButton;

    private Pane parentPane;
    private Runnable onBackAction;
    private boolean canGoBack = true;

    /**
     * 헤더 컴포넌트를 초기화하고 부모 컨테이너에 추가합니다.
     *
     * @param parentPane 헤더가 추가될 부모 컨테이너
     * @param onBackAction 뒤로 가기 버튼 클릭 시 실행할 동작
     * @param canGoBack 뒤로 가기 가능 여부
     * @return 생성된 헤더 컴포넌트 인스턴스
     */
    public static HeaderComponent addToPane(Pane parentPane, Runnable onBackAction, boolean canGoBack) {
        try {
            // 클래스 로더를 사용하여 리소스 경로 명확하게 지정
            FXMLLoader loader = new FXMLLoader(HeaderComponent.class.getClassLoader().getResource("fxml/component/HeaderComponent.fxml"));
            if (loader.getLocation() == null) {
                System.err.println("HeaderComponent.fxml 파일을 찾을 수 없습니다.");
                // 대체 경로 시도
                loader = new FXMLLoader(HeaderComponent.class.getResource("/fxml/component/HeaderComponent.fxml"));
                if (loader.getLocation() == null) {
                    System.err.println("모든 경로에서 HeaderComponent.fxml을 찾을 수 없습니다.");
                    return createFallbackHeader(parentPane, onBackAction, canGoBack);
                }
            }

            Parent header = loader.load();

            HeaderComponent controller = loader.getController();
            controller.init(parentPane, onBackAction, canGoBack);

            // 헤더가 화면 전체 너비를 차지하도록 AnchorPane 설정
            if (parentPane instanceof AnchorPane) {
                AnchorPane anchorPane = (AnchorPane) parentPane;
                AnchorPane.setTopAnchor(header, 0.0);
                AnchorPane.setLeftAnchor(header, 0.0);
                AnchorPane.setRightAnchor(header, 0.0);

                // 헤더를 부모 컨테이너의 맨 위에 추가 (z-순서 최상위로)
                parentPane.getChildren().add(0, header);
            } else {
                // AnchorPane이 아닌 경우 전체 너비를 적용하기 위한 처리
                header.prefWidth(parentPane.getWidth());
                header.maxWidth(Double.MAX_VALUE);

                parentPane.getChildren().add(0, header);

                // 부모 크기가 변경될 때 헤더 크기도 조정되도록 리스너 추가
                parentPane.widthProperty().addListener((obs, oldVal, newVal) -> {
                    header.prefWidth(newVal.doubleValue());
                });
            }

            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("HeaderComponent 로드 중 오류: " + e.getMessage());
            return createFallbackHeader(parentPane, onBackAction, canGoBack);
        }
    }

    /**
     * FXML 로딩 실패 시 코드로 생성하는 대체 헤더
     */
    private static HeaderComponent createFallbackHeader(Pane parentPane, Runnable onBackAction, boolean canGoBack) {
        System.out.println("FXML 로딩 실패로 대체 헤더를 생성합니다.");

        // 헤더 컨테이너 생성
        HBox header = new HBox();
        header.setPrefHeight(30);
        header.setMaxHeight(30);
        header.setSpacing(10);
        header.setPadding(new Insets(5, 10, 5, 10));
        header.setStyle(
                "-fx-background-color: rgba(44, 62, 80, 0.9);" +
                        "-fx-padding: 5px 10px;" +
                        "-fx-background-radius: 0 0 5px 5px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);" +
                        "-fx-alignment: center-left;" +
                        "-fx-spacing: 10px;" +
                        "-fx-pref-height: 30px;" +
                        "-fx-max-height: 30px;"
        );

        // 뒤로 가기 아이콘
        Label backIcon = new Label("←");
        backIcon.setTextFill(Color.WHITE);
        backIcon.setFont(Font.font("System", FontWeight.BOLD, 16));
        backIcon.setStyle("-fx-cursor: hand;");

        // 여백
        Region spacer = new Region();
        spacer.setMinWidth(10);
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // 닫기 아이콘
        Label closeIcon = new Label("✕");
        closeIcon.setTextFill(Color.WHITE);
        closeIcon.setFont(Font.font("System", FontWeight.BOLD, 16));
        closeIcon.setStyle("-fx-cursor: hand;");

        // 헤더에 요소 추가
        header.getChildren().addAll(backIcon, spacer, closeIcon);

        // AnchorPane에 추가
        if (parentPane instanceof AnchorPane) {
            AnchorPane.setTopAnchor(header, 0.0);
            AnchorPane.setLeftAnchor(header, 0.0);
            AnchorPane.setRightAnchor(header, 0.0);
        }

        // 뒤로가기 버튼 비활성화 설정
        if (!canGoBack) {
            backIcon.setOpacity(0.3);
            backIcon.setDisable(true);
        }

        // 이벤트 설정
        closeIcon.setOnMouseClicked(e -> {
            Stage stage = (Stage) header.getScene().getWindow();
            stage.close();
        });

        backIcon.setOnMouseClicked(e -> {
            if (canGoBack && onBackAction != null) {
                onBackAction.run();
            }
        });

        // 마우스 이벤트 설정 (페이드 인/아웃)
        header.setOpacity(1.0); // 처음에 보이도록 변경

        header.setOnMouseEntered(e -> {
            FadeTransition fadeIn = new FadeTransition(Duration.millis(150), header);
            fadeIn.setFromValue(header.getOpacity());
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        header.setOnMouseExited(e -> {
            if (e.getY() > 30) {
                FadeTransition fadeOut = new FadeTransition(Duration.millis(150), header);
                fadeOut.setFromValue(header.getOpacity());
                fadeOut.setToValue(0.0);
                fadeOut.play();
            }
        });

        parentPane.setOnMouseMoved(e -> {
            if (e.getY() < 40) {
                FadeTransition fadeIn = new FadeTransition(Duration.millis(150), header);
                fadeIn.setFromValue(header.getOpacity());
                fadeIn.setToValue(1.0);
                fadeIn.play();
            } else if (header.getOpacity() > 0 && !header.isHover()) {
                FadeTransition fadeOut = new FadeTransition(Duration.millis(150), header);
                fadeOut.setFromValue(header.getOpacity());
                fadeOut.setToValue(0.0);
                fadeOut.play();
            }
        });

        // 헤더 추가
        parentPane.getChildren().add(0, header);

        // HeaderComponent 인스턴스 생성 및 반환
        HeaderComponent component = new HeaderComponent();
        component.headerContainer = header;
        component.canGoBack = canGoBack;
        component.onBackAction = onBackAction;
        component.parentPane = parentPane;

        return component;
    }

    /**
     * 헤더 컴포넌트 초기화 및 이벤트 설정
     */
    private void init(Pane parentPane, Runnable onBackAction, boolean canGoBack) {
        this.parentPane = parentPane;
        this.onBackAction = onBackAction;
        this.canGoBack = canGoBack;

        // 스타일 적용
        applyStyles();

        // 초기 상태는 보이도록 설정 (문제 해결을 위해 변경)
        headerContainer.setOpacity(1.0);

        // 마우스 이벤트 설정
        setupMouseEvents();

        // 뒤로가기/닫기 버튼 이벤트 설정
        try {
            closeButton.setOnMouseClicked(e -> closeWindow());

            if (canGoBack) {
                backButton.setOnMouseClicked(e -> {
                    if (onBackAction != null) {
                        onBackAction.run();
                    }
                });
                // 뒤로가기 가능한 경우 정상 표시
                backButton.setOpacity(1.0);
            } else {
                // 뒤로가기 불가능한 경우 비활성화 스타일 적용
                backButton.setOpacity(0.3);
                backButton.setDisable(true);
            }
        } catch (Exception e) {
            System.err.println("버튼 이벤트 설정 중 오류: " + e.getMessage());
        }

        // 아이콘 로드 - 실패 시 직접 생성
        boolean iconsLoaded = loadIcons();
        if (!iconsLoaded) {
            createTextBasedIcons();
        }

        // 헤더 컨테이너가 전체 너비를 사용하도록 설정
        headerContainer.prefWidthProperty().bind(parentPane.widthProperty());
        headerContainer.maxWidthProperty().bind(parentPane.widthProperty());
    }

    /**
     * 텍스트 기반 아이콘 생성 (아이콘 로드 실패 시)
     */
    private void createTextBasedIcons() {
        try {
            // 기존 ImageView 제거
            if (headerContainer.getChildren().contains(backButton)) {
                headerContainer.getChildren().remove(backButton);
            }
            if (headerContainer.getChildren().contains(closeButton)) {
                headerContainer.getChildren().remove(closeButton);
            }

            // 텍스트 기반 아이콘 생성
            Label backIcon = new Label("←");
            backIcon.setTextFill(Color.WHITE);
            backIcon.setFont(Font.font("System", FontWeight.BOLD, 16));
            backIcon.setStyle("-fx-cursor: hand;");

            Label closeIcon = new Label("✕");
            closeIcon.setTextFill(Color.WHITE);
            closeIcon.setFont(Font.font("System", FontWeight.BOLD, 16));
            closeIcon.setStyle("-fx-cursor: hand;");

            // 이벤트 핸들러 설정
            closeIcon.setOnMouseClicked(e -> closeWindow());

            if (canGoBack) {
                backIcon.setOnMouseClicked(e -> {
                    if (onBackAction != null) {
                        onBackAction.run();
                    }
                });
            } else {
                backIcon.setOpacity(0.3);
                backIcon.setDisable(true);
            }

            // 여백
            Region spacer = new Region();
            spacer.setMinWidth(10);
            HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

            // 헤더 컨테이너의 모든 요소 제거하고 새로 추가
            headerContainer.getChildren().clear();
            headerContainer.getChildren().addAll(backIcon, spacer, closeIcon);

            System.out.println("텍스트 기반 아이콘 생성 완료");
        } catch (Exception e) {
            System.err.println("텍스트 아이콘 생성 중 오류: " + e.getMessage());
        }
    }

    /**
     * 스타일 적용
     */
    private void applyStyles() {
        // AppStyles의 스타일 적용
        try {
            AppStyles.applyHeaderStyle(headerContainer);

            // 아이콘 스타일 직접 적용 (가시성 문제 해결)
            if (backButton != null) {
                backButton.setFitWidth(20);
                backButton.setFitHeight(20);
                backButton.setPickOnBounds(true);
                backButton.setPreserveRatio(true);
                backButton.setStyle("-fx-cursor: hand;");
                // 가시성을 위해 배경 추가
                // 백그라운드 대신 효과(Effect)를 사용
                backButton.setEffect(new javafx.scene.effect.DropShadow(2, Color.WHITE));
            }

            if (closeButton != null) {
                closeButton.setFitWidth(20);
                closeButton.setFitHeight(20);
                closeButton.setPickOnBounds(true);
                closeButton.setPreserveRatio(true);
                closeButton.setStyle("-fx-cursor: hand;");
                // 백그라운드 대신 효과(Effect)를 사용
                closeButton.setEffect(new javafx.scene.effect.DropShadow(2, Color.WHITE));
            }
        } catch (Exception e) {
            System.err.println("스타일 적용 중 오류: " + e.getMessage());
            // 기본 스타일 직접 적용
            headerContainer.setStyle(
                    "-fx-background-color: rgba(44, 62, 80, 0.9);" +
                            "-fx-padding: 5px 10px;" +
                            "-fx-background-radius: 0 0 5px 5px;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);" +
                            "-fx-alignment: center-left;" +
                            "-fx-spacing: 10px;" +
                            "-fx-pref-height: 30px;" +
                            "-fx-max-height: 30px;"
            );
        }
    }

    /**
     * 마우스 이벤트 설정
     */
    private void setupMouseEvents() {
        try {
            // 헤더 자체에 마우스 이벤트 설정
            headerContainer.setOnMouseEntered(e -> showHeader());
            headerContainer.setOnMouseExited(e -> {
                if (e.getY() > 30) {
                    hideHeader();
                }
            });

            // 부모 컨테이너에 마우스 이벤트 설정
            parentPane.setOnMouseMoved(e -> {
                // 마우스가 화면 상단 근처에 있을 때 헤더 표시
                if (e.getY() < 40) {
                    showHeader();
                } else if (headerContainer.getOpacity() > 0 &&
                        !headerContainer.isHover()) {
                    hideHeader();
                }
            });

            // 부모에 진입/이탈 이벤트 추가
            parentPane.setOnMouseEntered(e -> {
                if (e.getY() < 40) {
                    showHeader();
                }
            });

            parentPane.setOnMouseExited(e -> hideHeader());
        } catch (Exception e) {
            System.err.println("마우스 이벤트 설정 중 오류: " + e.getMessage());
        }
    }

    /**
     * SVG 아이콘 로드 메서드
     * @return 로드 성공 여부
     */
    private boolean loadIcons() {
        try {
            // 리소스 경로를 수정 - 클래스로더를 통해 명확한 경로 지정
            String backIconPath = "/icons/back-icon.svg";
            String closeIconPath = "/icons/close-icon.svg";

            // 콘솔에 경로 확인 로그 추가
            System.out.println("아이콘 로드 시도: " + backIconPath + ", " + closeIconPath);

            InputStream backStream = getClass().getResourceAsStream(backIconPath);
            InputStream closeStream = getClass().getResourceAsStream(closeIconPath);

            // 스트림이 null인 경우 대체 경로 시도
            if (backStream == null) {
                backStream = getClass().getClassLoader().getResourceAsStream("icons/back-icon.svg");
                System.out.println("대체 경로 시도 (back): icons/back-icon.svg");
            }
            if (closeStream == null) {
                closeStream = getClass().getClassLoader().getResourceAsStream("icons/close-icon.svg");
                System.out.println("대체 경로 시도 (close): icons/close-icon.svg");
            }

            boolean success = false;

            if (backStream != null && closeStream != null) {
                backButton.setImage(new Image(backStream));
                closeButton.setImage(new Image(closeStream));

                // 이미지 로드 후 제대로 표시되도록 설정
                backButton.setFitWidth(20);
                backButton.setFitHeight(20);
                backButton.setPreserveRatio(true);
                backButton.setVisible(true);

                closeButton.setFitWidth(20);
                closeButton.setFitHeight(20);
                closeButton.setPreserveRatio(true);
                closeButton.setVisible(true);

                backStream.close();
                closeStream.close();

                success = true;
                System.out.println("아이콘 로드 성공!");
            } else {
                System.err.println("아이콘 스트림 로드 실패: back=" + (backStream != null) + ", close=" + (closeStream != null));
            }

            if (!success) {
                // 아이콘 로드 실패 시 명시적으로 대체 텍스트 아이콘 생성
                createTextBasedIcons();
            }

            return success;
        } catch (Exception e) {
            System.err.println("아이콘을 로드할 수 없습니다: " + e.getMessage());
            e.printStackTrace();
            // 예외 발생 시 대체 텍스트 아이콘 생성
            createTextBasedIcons();
            return false;
        }
    }

    /**
     * 리소스에서 아이콘 설정
     * @return 로드 성공 여부
     */
    private boolean setIconFromResource(ImageView imageView, String resourcePath) {
        try {
            InputStream is = getClass().getResourceAsStream(resourcePath);
            if (is == null) {
                // 대체 경로 시도
                is = getClass().getClassLoader().getResourceAsStream(resourcePath.substring(1));
            }

            if (is != null) {
                Image image = new Image(is);
                imageView.setImage(image);
                is.close();

                // 이미지 로드 확인 (디버깅용)
                System.out.println("아이콘 로드 성공: " + resourcePath);
                System.out.println("이미지 크기: " + image.getWidth() + "x" + image.getHeight());
                System.out.println("이미지가 로드되었나요? " + !image.isError());

                // 이미지뷰 속성 설정 (가시성 문제 해결)
                imageView.setFitWidth(20);
                imageView.setFitHeight(20);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                imageView.setVisible(true);

                return true;
            } else {
                System.err.println("리소스를 찾을 수 없습니다: " + resourcePath);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 애니메이션과 함께 헤더 표시
     */
    private void showHeader() {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(150), headerContainer);
        fadeIn.setFromValue(headerContainer.getOpacity());
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    /**
     * 애니메이션과 함께 헤더 숨김
     */
    private void hideHeader() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), headerContainer);
        fadeOut.setFromValue(headerContainer.getOpacity());
        fadeOut.setToValue(0.0);
        fadeOut.play();
    }

    /**
     * 현재 창 닫기
     */
    private void closeWindow() {
        Stage stage = (Stage) headerContainer.getScene().getWindow();
        stage.close();
    }

    /**
     * 뒤로 가기 가능 여부 설정
     */
    public void setCanGoBack(boolean canGoBack) {
        this.canGoBack = canGoBack;

        try {
            if (backButton != null) {
                if (canGoBack) {
                    backButton.setOpacity(1.0);
                    backButton.setDisable(false);
                } else {
                    backButton.setOpacity(0.3);
                    backButton.setDisable(true);
                }
            } else {
                // FXML로 로드하지 않은 경우 첫 번째 자식이 뒤로가기 버튼일 것
                Node firstChild = headerContainer.getChildren().get(0);
                if (firstChild != null) {
                    if (canGoBack) {
                        firstChild.setOpacity(1.0);
                        firstChild.setDisable(false);
                    } else {
                        firstChild.setOpacity(0.3);
                        firstChild.setDisable(true);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("뒤로가기 설정 중 오류: " + e.getMessage());
        }
    }
}
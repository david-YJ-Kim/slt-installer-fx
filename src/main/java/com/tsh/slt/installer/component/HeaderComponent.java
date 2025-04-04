package com.tsh.slt.installer.component;

import com.tsh.slt.installer.code.FxmlFileConstants;
import com.tsh.slt.installer.code.desings.AppStyles;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 애플리케이션의 모든 화면(로그인 화면 제외)에서 사용되는 공통 헤더 컴포넌트.
 * 뒤로가기 버튼, 닫기 버튼을 제공하고, 마우스가 화면 상단에 위치할 때만 보이는 기능을 가짐.
 */
public class HeaderComponent {
    // 로거 설정
    private static final Logger LOGGER = Logger.getLogger(HeaderComponent.class.getName());

    // FXML로 주입되는 UI 컴포넌트
    @FXML private HBox headerContainer;    // 헤더의 메인 컨테이너
    @FXML private ImageView backButton;    // 뒤로가기 버튼
    @FXML private ImageView closeButton;   // 닫기 버튼

    // 상태 및 설정 필드
    private Pane parentPane;               // 헤더가 추가될 부모 컨테이너
    private Runnable onBackAction;         // 뒤로가기 버튼 클릭 시 실행할 동작
    private boolean canGoBack = true;      // 뒤로가기 가능 여부

    /**
     * 헤더 컴포넌트를 초기화하고 부모 컨테이너에 추가합니다.
     *
     * @param parentPane 헤더가 추가될 부모 컨테이너
     * @param onBackAction 뒤로가기 버튼 클릭 시 실행할 동작
     * @param canGoBack 뒤로가기 가능 여부
     * @return 생성된 헤더 컴포넌트 인스턴스, 실패 시 null 반환
     */
    public static HeaderComponent addToPane(Pane parentPane, Runnable onBackAction, boolean canGoBack) {
        if (parentPane == null) {
            LOGGER.warning("부모 컨테이너가 null입니다. 헤더를 추가할 수 없습니다.");
            return null;
        }

        try {
            // FXML 파일 로드
            String fxmlPath = FxmlFileConstants.HEADER_COMPONENT_FXML;
            FXMLLoader loader = new FXMLLoader(HeaderComponent.class.getResource(fxmlPath));

            if (loader.getLocation() == null) {
                LOGGER.warning("첫 번째 경로에서 FXML을 찾을 수 없습니다: " + fxmlPath);
                // 대체 경로 시도
                loader = new FXMLLoader(HeaderComponent.class.getClassLoader().getResource("/".concat(FxmlFileConstants.HEADER_COMPONENT_FXML)));

                if (loader.getLocation() == null) {
                    LOGGER.severe("모든 경로에서 HeaderComponent.fxml을 찾을 수 없습니다.");
                    return null;
                }
            }

            // FXML 로드 및 컨트롤러 초기화
            Parent header = loader.load();
            HeaderComponent controller = loader.getController();
            controller.init(parentPane, onBackAction, canGoBack);

            // 헤더가 화면 전체 너비를 차지하도록 AnchorPane 설정
            if (parentPane instanceof AnchorPane) {
                AnchorPane anchorPane = (AnchorPane) parentPane;
                AnchorPane.setTopAnchor(header, 0.0);
                AnchorPane.setLeftAnchor(header, 0.0);
                AnchorPane.setRightAnchor(header, 0.0);
            }

            // 헤더를 부모 컨테이너의 맨 위에 추가 (z-index 최상위로)
            parentPane.getChildren().add(0, header);
            LOGGER.info("헤더 컴포넌트가 성공적으로 추가되었습니다.");

            return controller;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "HeaderComponent 로드 중 오류가 발생했습니다.", e);
            return null;
        }
    }

    /**
     * 헤더 컴포넌트 초기화 및 이벤트 설정.
     *
     * @param parentPane 부모 컨테이너
     * @param onBackAction 뒤로가기 동작
     * @param canGoBack 뒤로가기 가능 여부
     */
    private void init(Pane parentPane, Runnable onBackAction, boolean canGoBack) {
        this.parentPane = parentPane;
        this.onBackAction = onBackAction;
        this.canGoBack = canGoBack;

        // 스타일 적용
        applyStyles();

        // 항상 보이도록 설정 (fade 효과는 마우스 이벤트로 제어)
        headerContainer.setOpacity(1.0);

        // 뒤로가기/닫기 버튼 이벤트 설정
        setupButtonEvents();

        // 아이콘 이미지 로드
        loadIcons();

        // 헤더 너비를 부모 컨테이너 너비에 바인딩
        headerContainer.prefWidthProperty().bind(parentPane.widthProperty());
        headerContainer.maxWidthProperty().bind(parentPane.widthProperty());

        // 마우스 이벤트 설정 (fade in/out)
        setupMouseEvents();

        LOGGER.info("헤더 컴포넌트 초기화 완료");
    }

    /**
     * 헤더와 버튼에 스타일을 적용합니다.
     * ColorAdjust 효과와 회전 애니메이션을 설정합니다.
     */
    private void applyStyles() {
        try {
            // 헤더 컨테이너 스타일 적용
            headerContainer.setStyle(
                    "-fx-background-color: " + AppStyles.HEADER_BACKGROUND_COLOR + ";" +
                            "-fx-padding: 5px 10px;" +
                            "-fx-alignment: center-left;" +
                            "-fx-spacing: 10px;" +
                            "-fx-pref-height: 50px;" +
                            "-fx-max-height: 50px;"
            );

            // 이미지뷰 스타일 설정
            if (backButton != null) {
                setupImageViewStyle(backButton, true);
            }

            if (closeButton != null) {
                setupImageViewStyle(closeButton, false);
            }

            LOGGER.fine("헤더 스타일 적용 완료");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "스타일 적용 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 이미지뷰에 스타일 및 효과를 적용합니다.
     * 흰색 색상 효과와 마우스 오버 시 회전 애니메이션을 설정합니다.
     *
     * @param imageView 스타일을 적용할 이미지뷰
     * @param isBackButton 뒤로가기 버튼 여부 (회전 방향 결정)
     */
    private void setupImageViewStyle(ImageView imageView, boolean isBackButton) {
        // 기본 속성 설정
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-cursor: hand;");

        // 이미지 색상을 흰색으로 조정
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(1.0);  // 밝기 최대로 설정 (흰색에 가깝게)
        imageView.setEffect(colorAdjust);

        // 회전 효과 설정
        RotateTransition rotateIn = new RotateTransition(Duration.millis(200), imageView);
        rotateIn.setFromAngle(0);
        rotateIn.setToAngle(isBackButton ? -90 : 90);  // 뒤로가기는 반시계, 닫기는 시계방향

        RotateTransition rotateOut = new RotateTransition(Duration.millis(200), imageView);
        rotateOut.setFromAngle(isBackButton ? -90 : 90);
        rotateOut.setToAngle(0);

        // 마우스 이벤트 핸들러
        imageView.setOnMouseEntered(e -> rotateIn.play());
        imageView.setOnMouseExited(e -> rotateOut.play());
    }

    /**
     * 버튼 클릭 이벤트를 설정합니다.
     * 닫기 버튼은 창을 닫고, 뒤로가기 버튼은 제공된 액션을 실행합니다.
     */
    private void setupButtonEvents() {
        if (closeButton != null) {
            closeButton.setOnMouseClicked(e -> {
                LOGGER.info("닫기 버튼 클릭됨");
                Stage stage = (Stage) headerContainer.getScene().getWindow();
                stage.close();
            });
        }

        if (backButton != null) {
            if (canGoBack) {
                backButton.setOnMouseClicked(e -> {
                    LOGGER.info("뒤로가기 버튼 클릭됨");
                    if (onBackAction != null) {
                        onBackAction.run();
                    } else {
                        LOGGER.warning("뒤로가기 액션이 설정되지 않았습니다.");
                    }
                });
                backButton.setOpacity(1.0);
            } else {
                // 뒤로가기 불가능한 경우 비활성화 처리
                backButton.setOpacity(0.3);
                backButton.setDisable(true);
                LOGGER.fine("뒤로가기 불가능 - 버튼 비활성화됨");
            }
        }
    }

    /**
     * 마우스 이벤트를 설정하여 헤더의 표시/숨김을 제어합니다.
     * 마우스가 화면 상단에 있을 때는 헤더가 표시되고,
     * 그렇지 않을 때는 헤더가 점차 사라집니다.
     */
    private void setupMouseEvents() {
        try {
            // 헤더에 마우스 이벤트 설정
            headerContainer.setOnMouseEntered(e -> showHeader());
            headerContainer.setOnMouseExited(e -> {
                if (e.getY() > 30) {
                    hideHeader();
                }
            });

            // 부모 컨테이너에 마우스 이벤트 설정
            parentPane.setOnMouseMoved(e -> {
                if (e.getY() < 40) {
                    showHeader();
                } else if (headerContainer.getOpacity() > 0 && !headerContainer.isHover()) {
                    hideHeader();
                }
            });

            parentPane.setOnMouseEntered(e -> {
                if (e.getY() < 40) {
                    showHeader();
                }
            });

            parentPane.setOnMouseExited(e -> hideHeader());

            LOGGER.fine("마우스 이벤트 설정 완료");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "마우스 이벤트 설정 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 아이콘 이미지를 로드합니다.
     * 여러 경로를 시도하여 이미지를 찾습니다.
     *
     * @return 로드 성공 여부
     */
    private boolean loadIcons() {
        try {
            // 리소스 경로 설정 (PNG 파일 사용)
            String backIconPath = "/icons/back-icon.png";
            String closeIconPath = "/icons/close-icon.png";

            LOGGER.info("아이콘 로드 시도: " + backIconPath + ", " + closeIconPath);

            // 이미지 로드 시도
            boolean backSuccess = loadImageForView(backButton, backIconPath);
            boolean closeSuccess = loadImageForView(closeButton, closeIconPath);

            LOGGER.info("아이콘 로드 결과: back=" + backSuccess + ", close=" + closeSuccess);

            return backSuccess && closeSuccess;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "아이콘 로드 중 오류가 발생했습니다.", e);
            return false;
        }
    }

    /**
     * 이미지뷰에 이미지를 로드합니다.
     * 여러 경로를 시도하여 리소스를 찾고, 실패 시 색상 사각형으로 대체합니다.
     *
     * @param imageView 이미지를 설정할 ImageView
     * @param path 리소스 경로
     * @return 로드 성공 여부
     */
    private boolean loadImageForView(ImageView imageView, String path) {
        if (imageView == null) return false;

        try {
            // 여러 경로를 시도
            InputStream is = null;

            // 1. 클래스에서 직접 리소스 로드 시도
            is = getClass().getResourceAsStream(path);
            LOGGER.fine("경로 1 시도: " + path + ", 결과: " + (is != null));

            // 2. 클래스로더로 시도 (경로 수정)
            if (is == null) {
                String path2 = path.startsWith("/") ? path.substring(1) : path;
                is = getClass().getClassLoader().getResourceAsStream(path2);
                LOGGER.fine("경로 2 시도: " + path2 + ", 결과: " + (is != null));
            }

            // 3. 파일명만 사용하여 시도
            if (is == null) {
                String filename = path.substring(path.lastIndexOf('/') + 1);
                is = getClass().getClassLoader().getResourceAsStream("icons/" + filename);
                LOGGER.fine("경로 3 시도: icons/" + filename + ", 결과: " + (is != null));
            }

            // 4. 다른 상대 경로 시도
            if (is == null) {
                String filename = path.substring(path.lastIndexOf('/') + 1);
                is = getClass().getClassLoader().getResourceAsStream("../icons/" + filename);
                LOGGER.fine("경로 4 시도: ../icons/" + filename + ", 결과: " + (is != null));
            }

            // 이미지 로드 성공 시
            if (is != null) {
                Image image = new Image(is);
                imageView.setImage(image);
                is.close();

                // 이미지 사이즈와 속성 확인
                LOGGER.fine("이미지 로드 성공: " + path);
                LOGGER.fine("이미지 정보: " + image.getWidth() + "x" + image.getHeight() +
                        ", 로드됨=" + !image.isError());

                return true;
            } else {
                // 이미지 로드 실패 시 색상 사각형으로 대체
                LOGGER.warning("리소스를 찾을 수 없습니다: " + path);

                if (path.contains("back")) {
                    setColorRectangle(imageView, Color.LIGHTBLUE);
                } else if (path.contains("close")) {
                    setColorRectangle(imageView, Color.LIGHTCORAL);
                }

                return false;
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "이미지 로드 중 오류가 발생했습니다: " + path, e);

            // 예외 발생 시 색상 사각형으로 대체
            if (path.contains("back")) {
                setColorRectangle(imageView, Color.LIGHTBLUE);
            } else if (path.contains("close")) {
                setColorRectangle(imageView, Color.LIGHTCORAL);
            }

            return false;
        }
    }

    /**
     * 이미지 로드 실패 시 색상 사각형으로 대체합니다.
     *
     * @param imageView 이미지뷰
     * @param color 사각형 색상
     */
    private void setColorRectangle(ImageView imageView, Color color) {
        // JavaFX의 사각형을 생성하여 이미지 대신 사용
        javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(20, 20, color);

        // 이미지뷰에 사각형을 스냅샷으로 변환하여 설정
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        WritableImage image = rect.snapshot(params, null);
        imageView.setImage(image);
        imageView.setVisible(true);

        LOGGER.fine("색상 사각형으로 대체됨: " + color);
    }

    /**
     * 애니메이션과 함께 헤더를 표시합니다.
     */
    private void showHeader() {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(150), headerContainer);
        fadeIn.setFromValue(headerContainer.getOpacity());
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    /**
     * 애니메이션과 함께 헤더를 숨깁니다.
     */
    private void hideHeader() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), headerContainer);
        fadeOut.setFromValue(headerContainer.getOpacity());
        fadeOut.setToValue(0.0);
        fadeOut.play();
    }

    /**
     * 뒤로가기 가능 여부를 설정합니다.
     * 설정에 따라 뒤로가기 버튼의 외관과 동작이 변경됩니다.
     *
     * @param canGoBack 뒤로가기 가능 여부
     */
    public void setCanGoBack(boolean canGoBack) {
        this.canGoBack = canGoBack;
        LOGGER.info("뒤로가기 가능 여부 설정: " + canGoBack);

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
                LOGGER.warning("backButton이 null입니다. 설정을 적용할 수 없습니다.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "뒤로가기 설정 중 오류가 발생했습니다.", e);
        }
    }
}
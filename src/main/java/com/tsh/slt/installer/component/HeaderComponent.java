package com.tsh.slt.installer.component;

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
            // 명확한 경로 지정
            String fxmlPath = "/fxml/component/HeaderComponent.fxml";
            FXMLLoader loader = new FXMLLoader(HeaderComponent.class.getResource(fxmlPath));

            if (loader.getLocation() == null) {
                System.err.println("HeaderComponent.fxml 파일을 찾을 수 없습니다: " + fxmlPath);
                // 대체 경로 시도
                loader = new FXMLLoader(HeaderComponent.class.getClassLoader().getResource("fxml/component/HeaderComponent.fxml"));

                if (loader.getLocation() == null) {
                    System.err.println("모든 경로에서 HeaderComponent.fxml을 찾을 수 없습니다.");
                    return null;
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
            }

            // 헤더를 부모 컨테이너의 맨 위에 추가
            parentPane.getChildren().add(0, header);

            return controller;
        } catch (IOException e) {
            System.err.println("HeaderComponent 로드 중 오류: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
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

        // 항상 보이도록 설정
        headerContainer.setOpacity(1.0);

        // 뒤로가기/닫기 버튼 이벤트 설정
        setupButtonEvents();

        // 아이콘 로드
        loadIcons();

        // 헤더 컨테이너가 전체 너비를 사용하도록 설정
        headerContainer.prefWidthProperty().bind(parentPane.widthProperty());
        headerContainer.maxWidthProperty().bind(parentPane.widthProperty());

        // 마우스 이벤트 설정 (fade in/out)
        setupMouseEvents();
    }

    /**
     * 버튼 이벤트 설정
     */
    private void setupButtonEvents() {
        if (closeButton != null) {
            closeButton.setOnMouseClicked(e -> {
                Stage stage = (Stage) headerContainer.getScene().getWindow();
                stage.close();
            });
        }

        if (backButton != null) {
            if (canGoBack) {
                backButton.setOnMouseClicked(e -> {
                    if (onBackAction != null) {
                        onBackAction.run();
                    }
                });
                backButton.setOpacity(1.0);
            } else {
                backButton.setOpacity(0.3);
                backButton.setDisable(true);
            }
        }
    }

    /**
     * 스타일 적용
     */
    private void applyStyles() {
        try {
            // 헤더 컨테이너 스타일 적용
            headerContainer.setStyle(
                    "-fx-background-color: " + AppStyles.HEADER_BACKGROUND_COLOR + ";" +
                            "-fx-padding: 5px 10px;" +
                            "-fx-alignment: center-left;" +
                            "-fx-spacing: 10px;" +
                            "-fx-pref-height: 30px;" +
                            "-fx-max-height: 30px;"
            );

            // 이미지뷰 스타일 설정
            if (backButton != null) {
                setupImageViewStyle(backButton, true);
            }

            if (closeButton != null) {
                setupImageViewStyle(closeButton, false);
            }
        } catch (Exception e) {
            System.err.println("스타일 적용 중 오류: " + e.getMessage());
        }
    }

    /**
     * 이미지뷰에 스타일 및 효과 적용
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
     * 마우스 이벤트 설정
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
        } catch (Exception e) {
            System.err.println("마우스 이벤트 설정 중 오류: " + e.getMessage());
        }
    }

    /**
     * 아이콘 로드 메서드
     */
    private boolean loadIcons() {
        try {
            // 리소스 경로 설정 (SVG 대신 PNG 사용)
            String backIconPath = "/icons/back-icon.png";
            String closeIconPath = "/icons/close-icon.png";

            System.out.println("아이콘 로드 시도: " + backIconPath + ", " + closeIconPath);

            // 여러 경로로 시도
            boolean backSuccess = loadImageForView(backButton, backIconPath);
            boolean closeSuccess = loadImageForView(closeButton, closeIconPath);

            System.out.println("아이콘 로드 결과: back=" + backSuccess + ", close=" + closeSuccess);

            return backSuccess && closeSuccess;
        } catch (Exception e) {
            System.err.println("아이콘 로드 중 오류: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 이미지뷰에 이미지 로드
     */
    private boolean loadImageForView(ImageView imageView, String path) {
        if (imageView == null) return false;

        try {
            // 여러 경로를 시도
            InputStream is = null;

            // 1. 직접 경로로 시도
            is = getClass().getResourceAsStream(path);
            System.out.println("경로 1 시도: " + path + ", 결과: " + (is != null));

            // 2. 클래스로더로 시도 (경로 수정)
            if (is == null) {
                String path2 = path.startsWith("/") ? path.substring(1) : path;
                is = getClass().getClassLoader().getResourceAsStream(path2);
                System.out.println("경로 2 시도: " + path2 + ", 결과: " + (is != null));
            }

            // 3. 파일명만 사용하여 시도
            if (is == null) {
                String filename = path.substring(path.lastIndexOf('/') + 1);
                is = getClass().getClassLoader().getResourceAsStream("icons/" + filename);
                System.out.println("경로 3 시도: icons/" + filename + ", 결과: " + (is != null));
            }

            // 4. 다른 상대 경로 시도
            if (is == null) {
                String filename = path.substring(path.lastIndexOf('/') + 1);
                is = getClass().getClassLoader().getResourceAsStream("../icons/" + filename);
                System.out.println("경로 4 시도: ../icons/" + filename + ", 결과: " + (is != null));
            }

            if (is != null) {
                Image image = new Image(is);
                imageView.setImage(image);
                is.close();

                // 이미지 사이즈와 속성 설정 확인
                imageView.setFitWidth(20);
                imageView.setFitHeight(20);
                imageView.setPreserveRatio(true);
                imageView.setVisible(true);

                System.out.println("이미지 로드 성공: " + path);
                System.out.println("이미지 정보: " + image.getWidth() + "x" + image.getHeight() + ", 로드됨=" + !image.isError());

                return true;
            } else {
                System.err.println("리소스를 찾을 수 없습니다: " + path);

                // 이미지 로드 실패 시 색상으로 구분된 사각형으로 대체
                if (path.contains("back")) {
                    setColorRectangle(imageView, Color.LIGHTBLUE);
                } else if (path.contains("close")) {
                    setColorRectangle(imageView, Color.LIGHTCORAL);
                }

                return false;
            }
        } catch (IOException e) {
            System.err.println("이미지 로드 중 오류: " + e.getMessage());
            e.printStackTrace();

            // 예외 발생 시 색상으로 구분된 사각형으로 대체
            if (path.contains("back")) {
                setColorRectangle(imageView, Color.LIGHTBLUE);
            } else if (path.contains("close")) {
                setColorRectangle(imageView, Color.LIGHTCORAL);
            }

            return false;
        }
    }

    /**
     * 이미지 로드 실패 시 색상 사각형으로 대체
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
     * 뒤로 가기 가능 여부 설정
     */
    public void setCanGoBack(boolean canGoBack) {
        this.canGoBack = canGoBack;

        if (backButton != null) {
            if (canGoBack) {
                backButton.setOpacity(1.0);
                backButton.setDisable(false);
            } else {
                backButton.setOpacity(0.3);
                backButton.setDisable(true);
            }
        } else {
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
    }
}
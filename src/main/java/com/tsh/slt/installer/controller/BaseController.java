package com.tsh.slt.installer.controller;

import com.tsh.slt.installer.code.desings.AppStyles;
import com.tsh.slt.installer.component.HeaderComponent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * 모든 컨트롤러의 기본 클래스
 * 공통 기능과 스타일 적용 메서드를 제공합니다.
 */
public abstract class BaseController {

    private double xOffset = 0;
    private double yOffset = 0;
    protected HeaderComponent headerComponent;

    // 로그인 화면인지 확인하는 플래그
    protected boolean isLoginScreen = false;
    // 이전 화면이 로그인 화면인지 여부
    protected boolean previousIsLoginScreen = false;

    /**
     * 창 드래그 기능을 추가합니다.
     *
     * @param root 드래그 이벤트를 적용할 루트 컨테이너
     */
    protected void enableWindowDrag(Parent root) {
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    /**
     * 현재 창을 닫습니다.
     *
     * @param node 창에 포함된 아무 노드
     */
    protected void closeWindow(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    /**
     * 컨트롤러 초기화 시 공통 스타일을 적용합니다.
     * 자식 클래스에서 오버라이드하여 컨트롤에 스타일을 적용합니다.
     */
    protected abstract void applyStyles();

    /**
     * 초기화 후 호출되어야 하는 메서드
     * 자식 클래스에서 이 메서드를 구현하여 공통 기능 초기화 코드를 실행합니다.
     *
     * @param root 컨트롤러가 관리하는 루트 노드
     */
    protected void initializeCommon(Parent root) {
        // 창 드래그 기능 활성화
        enableWindowDrag(root);

        // 공통 헤더 추가 (로그인 화면이 아닌 경우에만)
        if (!isLoginScreen && root instanceof Pane) {
            addHeaderComponent((Pane) root);
        }
    }

    /**
     * 이전 화면이 로그인 화면인지 설정
     * @param isPreviousLogin 이전 화면이 로그인 화면인지 여부
     */
    public void setPreviousIsLoginScreen(boolean isPreviousLogin) {
        this.previousIsLoginScreen = isPreviousLogin;

        // 이미 헤더가 추가되었고, 이전 화면이 로그인 화면이면 뒤로가기 비활성화
        if (headerComponent != null) {
            headerComponent.setCanGoBack(!isPreviousLogin);
        }
    }

    /**
     * 헤더 컴포넌트를 추가합니다.
     *
     * @param rootPane 헤더를 추가할 루트 컨테이너
     */
    protected void addHeaderComponent(Pane rootPane) {
        try {
            // 뒤로가기 가능 여부 - 이전 화면이 로그인 화면이면 뒤로가기 불가능
            boolean canGoBack = !previousIsLoginScreen;

            // VBox인 경우
            if (rootPane instanceof VBox) {
                VBox vbox = (VBox) rootPane;

                // 첫 번째 요소로 빈 공간(여백) 추가
                Region headerSpace = new Region();
                headerSpace.setPrefHeight(80); // 헤더 높이 + 여유 공간
                headerSpace.setMinHeight(40);
                vbox.getChildren().add(0, headerSpace);
            }
            // AnchorPane인 경우
            else if (rootPane instanceof AnchorPane) {
                // AnchorPane의 경우, 다른 컨텐츠의 top anchor 값을 조정
                // 이미 헤더가 추가되기 전에 처리
                for (Node child : rootPane.getChildren()) {
                    // 모든 노드의 topAnchor 값 조정 (특정 노드 타입 체크로 변경)
                    Object topAnchor = AnchorPane.getTopAnchor(child);
                    if (topAnchor != null) {
                        AnchorPane.setTopAnchor(child, (Double)topAnchor + 40);
                    }
                }
            }

            // 헤더 컴포넌트 추가 및 뒤로가기 동작 설정
            headerComponent = HeaderComponent.addToPane(rootPane, this::navigateBack, canGoBack);

            if (headerComponent == null) {
                System.err.println("HeaderComponent를 로드할 수 없습니다.");
            }
        } catch (Exception e) {
            System.err.println("헤더 컴포넌트 추가 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 뒤로가기 동작 구현 메서드
     * 필요에 따라 자식 클래스에서 오버라이드
     */
    protected void navigateBack() {
        // 기본 구현에서는 아무 동작도 하지 않음
        // 필요한 경우 자식 클래스에서 오버라이드
        System.out.println("기본 navigateBack 메서드 호출됨 - 자식 클래스에서 오버라이드 필요");
    }

    /**
     * 버튼에 기본 스타일을 적용합니다.
     *
     * @param primaryButtons 주요 액션 버튼(기본 스타일 적용)
     * @param secondaryButtons 보조 액션 버튼(보조 스타일 적용)
     */
    protected void styleButtons(Button[] primaryButtons, Button[] secondaryButtons) {
        if (primaryButtons != null) {
            for (Button button : primaryButtons) {
                if (button != null) {
                    AppStyles.applyPrimaryButtonStyle(button);
                }
            }
        }

        if (secondaryButtons != null) {
            for (Button button : secondaryButtons) {
                if (button != null) {
                    AppStyles.applySecondaryButtonStyle(button);
                }
            }
        }
    }

    /**
     * 텍스트 필드에 스타일을 적용합니다.
     *
     * @param textFields 스타일을 적용할 텍스트 필드 배열
     */
    protected void styleTextFields(TextField[] textFields) {
        if (textFields != null) {
            for (TextField textField : textFields) {
                if (textField != null) {
                    AppStyles.applyTextFieldStyle(textField);
                }
            }
        }
    }

    /**
     * 패스워드 필드에 스타일을 적용합니다.
     *
     * @param passwordFields 스타일을 적용할 패스워드 필드 배열
     */
    protected void stylePasswordFields(PasswordField[] passwordFields) {
        if (passwordFields != null) {
            for (PasswordField passwordField : passwordFields) {
                if (passwordField != null) {
                    AppStyles.applyPasswordFieldStyle(passwordField);
                }
            }
        }
    }

    /**
     * 레이블에 스타일을 적용합니다.
     *
     * @param headerLabels 헤더 스타일을 적용할 레이블 배열
     * @param subHeaderLabels 서브헤더 스타일을 적용할 레이블 배열
     * @param normalLabels 일반 스타일을 적용할 레이블 배열
     */
    protected void styleLabels(Label[] headerLabels, Label[] subHeaderLabels, Label[] normalLabels) {
        if (headerLabels != null) {
            for (Label label : headerLabels) {
                if (label != null) {
                    AppStyles.applyHeaderLabelStyle(label);
                }
            }
        }

        if (subHeaderLabels != null) {
            for (Label label : subHeaderLabels) {
                if (label != null) {
                    AppStyles.applySubHeaderLabelStyle(label);
                }
            }
        }

        if (normalLabels != null) {
            for (Label label : normalLabels) {
                if (label != null) {
                    AppStyles.applyNormalLabelStyle(label);
                }
            }
        }
    }

    /**
     * 하이퍼링크에 스타일을 적용합니다.
     *
     * @param hyperlinks 스타일을 적용할 하이퍼링크 배열
     */
    protected void styleHyperlinks(Hyperlink[] hyperlinks) {
        if (hyperlinks != null) {
            for (Hyperlink hyperlink : hyperlinks) {
                if (hyperlink != null) {
                    AppStyles.applyHyperlinkStyle(hyperlink);
                }
            }
        }
    }

    /**
     * 콤보박스에 스타일을 적용합니다.
     *
     * @param comboBoxes 스타일을 적용할 콤보박스 배열
     */
    protected void styleComboBoxes(ComboBox<?>[] comboBoxes) {
        if (comboBoxes != null) {
            for (ComboBox<?> comboBox : comboBoxes) {
                if (comboBox != null) {
                    AppStyles.applyComboBoxStyle(comboBox);
                }
            }
        }
    }

    /**
     * 체크박스에 스타일을 적용합니다.
     *
     * @param checkBoxes 스타일을 적용할 체크박스 배열
     */
    protected void styleCheckBoxes(CheckBox[] checkBoxes) {
        if (checkBoxes != null) {
            for (CheckBox checkBox : checkBoxes) {
                if (checkBox != null) {
                    AppStyles.applyCheckBoxStyle(checkBox);
                }
            }
        }
    }

    /**
     * 진행 바에 스타일을 적용합니다.
     *
     * @param progressBars 스타일을 적용할 진행 바 배열
     */
    protected void styleProgressBars(ProgressBar[] progressBars) {
        if (progressBars != null) {
            for (ProgressBar progressBar : progressBars) {
                if (progressBar != null) {
                    AppStyles.applyProgressBarStyle(progressBar);
                }
            }
        }
    }

    /**
     * 컨테이너(Pane)에 스타일을 적용합니다.
     *
     * @param rootPane 루트 컨테이너
     * @param contentPanes 내부 컨텐츠 컨테이너 배열
     */
    protected void styleContainers(Pane rootPane, Pane[] contentPanes) {
        if (rootPane != null) {
            AppStyles.applyRootPaneStyle(rootPane);
        }

        if (contentPanes != null) {
            for (Pane pane : contentPanes) {
                if (pane != null) {
                    AppStyles.applyContainerStyle(pane);
                }
            }
        }
    }
}
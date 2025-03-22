package com.tsh.slt.installer.controller;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * 모든 컨트롤러의 기본 클래스
 * 공통 기능을 제공합니다.
 */
public abstract class BaseController {

    private double xOffset = 0;
    private double yOffset = 0;

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
     * 초기화 후 호출되어야 하는 메서드
     * 자식 클래스에서 이 메서드를 구현하여 공통 기능 초기화 코드를 실행합니다.
     *
     * @param root 컨트롤러가 관리하는 루트 노드
     */
    protected abstract void initializeCommon(Parent root);
}
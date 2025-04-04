package com.tsh.slt.installer;

import com.tsh.slt.installer.bizService.FirebaseStorageService;
import com.tsh.slt.installer.code.desings.ScreenSize;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class Main extends Application {

    static final String firebaseAccountPath = "firebase/service-account.json";
    static final String storageBucket = "sellter-e5dc9.appspot.com";

    @Override
    public void start(Stage primaryStage) throws Exception {
        // FXML 로드
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));

        // 씬 생성 및 스테이지 설정
        Scene scene = new Scene(root, ScreenSize.WIDTH, ScreenSize.HEIGHT);

        // 창 프레임 제거
        primaryStage.initStyle(StageStyle.UNDECORATED);

        primaryStage.setTitle("SLT Installer");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * 애플리케이션 진입점
     */
    public static void main(String[] args) throws IOException {
        // Firebase 초기화 코드가 필요하면 여기에 추가
         initializeFirebase();

        // JavaFX 애플리케이션 시작
        launch(args);
    }

    /**
     * Firebase 초기화 메서드 (필요한 경우 구현)
     */
    private static void initializeFirebase() throws IOException {
        // Firebase SDK 초기화 코드 (필요 시 구현)

        FirebaseStorageService service = FirebaseStorageService.getInstance();
        service.initialize(firebaseAccountPath, storageBucket);

        // 메인 메소드나 초기화 코드에 추가
        System.setProperty("https.protocols", "TLSv1.2");
        System.setProperty("jdk.tls.client.protocols", "TLSv1.2");


    }
}
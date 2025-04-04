package com.tsh.slt.installer.bizService;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.tsh.slt.installer.util.FilePathUtil;
import enums.DownloadFileTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;

/**
 * Firebase Storage 서비스 클래스
 * 파일 다운로드 및 관련 기능을 제공합니다.
 */
public class FirebaseStorageService {

    static final String firebaseAccountPath = "firebase/service-account.json";
    static final String storageBucket = "sellter-e5dc9.appspot.com";


    public static void main(String[] args) throws Exception {
        FirebaseStorageService service = FirebaseStorageService.getInstance();
        service.initialize(firebaseAccountPath, storageBucket);

        // 메인 메소드나 초기화 코드에 추가
        System.setProperty("https.protocols", "TLSv1.2");
        System.setProperty("jdk.tls.client.protocols", "TLSv1.2");


        service.downloadFile(FilePathUtil.getFirebaseFilePath(DownloadFileTypes.jar)
                , FilePathUtil.getLocalDownloadFilePath(DownloadFileTypes.jar, true));

        service.downloadFile(FilePathUtil.getFirebaseFilePath(DownloadFileTypes.yml)
                , FilePathUtil.getLocalDownloadFilePath(DownloadFileTypes.yml, true));

        service.downloadFile(FilePathUtil.getFirebaseFilePath(DownloadFileTypes.jdk)
                , FilePathUtil.getLocalDownloadFilePath(DownloadFileTypes.jdk, true));

        service.downloadFile(FilePathUtil.getFirebaseFilePath(DownloadFileTypes.vbs)
                , FilePathUtil.getLocalDownloadFilePath(DownloadFileTypes.vbs, true));

    }


    static final Logger logger = LoggerFactory.getLogger(FirebaseStorageService.class);

    private static FirebaseStorageService instance;
    private boolean initialized = false;

    // 싱글톤 패턴
    public static FirebaseStorageService getInstance() {
        if (instance == null) {
            instance = new FirebaseStorageService();
        }
        return instance;
    }

    private FirebaseStorageService() {
        // 생성자는 private으로 외부에서 직접 생성 불가
    }



    /**
     * Firebase 초기화
     * @param serviceAccountPath 서비스 계정 JSON 파일 경로
     * @throws IOException 초기화 실패 시
     */
    public void initialize(String serviceAccountPath, String storageBucket) throws IOException {
        if (initialized) return;

        try {
            // 클래스로더를 통해 리소스 파일 접근
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream(serviceAccountPath);

            if (serviceAccount == null) {
                throw new IOException("서비스 계정 파일을 찾을 수 없습니다: " + serviceAccountPath);
            }
            // Firebase 옵션 설정
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(storageBucket) // 실제 버킷 이름으로 변경 필요
                    .build();

            // Firebase 초기화 (이미 초기화되었는지 확인)
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            initialized = true;
        } catch (IOException e) {
            System.err.println("Firebase 초기화 중 오류 발생: " + e.getMessage());
            throw e;
        }
    }


    /**
     * Firebase Storage에서 파일 다운로드 (방법 1: StorageClient 사용)
     * @param remoteFilePath Firebase Storage 내 파일 경로
     * @param localFilePath 다운로드할 로컬 파일 경로
     * @return 다운로드된 파일 객체
     * @throws IOException 다운로드 실패 시
     */
    public File downloadFile(String remoteFilePath, String localFilePath) throws IOException {
        if (!initialized) {
            throw new IllegalStateException("Firebase가 초기화되지 않았습니다. initialize() 메서드를 먼저 호출하세요.");
        }

        try {
            // StorageClient 획득
            StorageClient storageClient = StorageClient.getInstance();

            // 원격 파일 참조 획득
            Blob blob = storageClient.bucket().get(remoteFilePath);
            if (blob == null) {
                throw new IOException("파일을 찾을 수 없습니다: " + remoteFilePath);
            }

            // 로컬 파일 생성
            File targetFile = new File(localFilePath);

            // 필요한 경우 상위 디렉토리 생성
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }

            // 파일 다운로드
            blob.downloadTo(Paths.get(localFilePath));

            return targetFile;
        } catch (Exception e) {
            System.err.println("파일 다운로드 중 오류 발생: " + e.getMessage());
            throw new IOException("파일 다운로드 실패: " + e.getMessage(), e);
        }
    }

    /**
     * Firebase Storage에서 파일 다운로드 (방법 2: Google Cloud Storage API 직접 사용)
     * @param bucketName 버킷 이름
     * @param remoteFilePath Firebase Storage 내 파일 경로
     * @param localFilePath 다운로드할 로컬 파일 경로
     * @return 다운로드된 파일 객체
     * @throws IOException 다운로드 실패 시
     */
    public File downloadFileDirectly(String bucketName, String remoteFilePath, String localFilePath) throws IOException {
        if (!initialized) {
            throw new IllegalStateException("Firebase가 초기화되지 않았습니다. initialize() 메서드를 먼저 호출하세요.");
        }

        try {
            // Google Cloud Storage 클라이언트 획득
            Storage storage = StorageOptions.getDefaultInstance().getService();

            // BlobId 생성
            BlobId blobId = BlobId.of(bucketName, remoteFilePath);

            // Blob 획득
            Blob blob = storage.get(blobId);
            if (blob == null) {
                throw new IOException("파일을 찾을 수 없습니다: " + remoteFilePath);
            }

            // 로컬 파일 생성
            File targetFile = new File(localFilePath);

            // 필요한 경우 상위 디렉토리 생성
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }

            // 파일 다운로드
            blob.downloadTo(Paths.get(localFilePath));

            return targetFile;
        } catch (Exception e) {
            System.err.println("파일 다운로드 중 오류 발생: " + e.getMessage());
            throw new IOException("파일 다운로드 실패: " + e.getMessage(), e);
        }
    }

    /**
     * Firebase Storage URL에서 파일 다운로드 (HTTP 요청 사용)
     * 이 방법은 공개 URL이 있는 경우에만 작동합니다.
     * @param downloadUrl 파일 다운로드 URL
     * @param localFilePath 다운로드할 로컬 파일 경로
     * @return 다운로드된 파일 객체
     * @throws IOException 다운로드 실패 시
     */
    public File downloadFileFromUrl(String downloadUrl, String localFilePath) throws IOException {
        try {
            // 다운로드 URL에서 입력 스트림 열기
            java.net.URL url = new java.net.URL(downloadUrl);
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());

            // 로컬 파일 생성
            File targetFile = new File(localFilePath);

            // 필요한 경우 상위 디렉토리 생성
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }

            // 파일 출력 스트림 생성 및 다운로드
            FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            // 리소스 정리
            fileChannel.close();
            fileOutputStream.close();
            readableByteChannel.close();

            return targetFile;
        } catch (Exception e) {
            System.err.println("URL에서 파일 다운로드 중 오류 발생: " + e.getMessage());
            throw new IOException("파일 다운로드 실패: " + e.getMessage(), e);
        }
    }

    /**
     * 비동기 파일 다운로드 (백그라운드 스레드에서 실행)
     * @param downloadUrl 파일 다운로드 URL
     * @param localFilePath 다운로드할 로컬 파일 경로
     * @return CompletableFuture 객체
     */
    public CompletableFuture<File> downloadFileAsync(String downloadUrl, String localFilePath) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return downloadFileFromUrl(downloadUrl, localFilePath);
            } catch (IOException e) {
                throw new RuntimeException("비동기 파일 다운로드 실패: " + e.getMessage(), e);
            }
        });
    }

    /**
     * 진행 상황을 추적할 수 있는 다운로드 메서드
     * (참고: Firebase Admin SDK는 진행률 추적을 직접 지원하지 않으므로 HTTP 다운로드 방식만 가능)
     * @param downloadUrl 파일 다운로드 URL
     * @param localFilePath 다운로드할 로컬 파일 경로
     * @param progressListener 진행 상황 리스너
     * @return 다운로드된 파일 객체
     * @throws IOException 다운로드 실패 시
     */
    public File downloadFileWithProgress(String downloadUrl, String localFilePath,
                                         ProgressListener progressListener) throws IOException {
        try {
            // URL 연결 설정
            java.net.URL url = new java.net.URL(downloadUrl);
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // 파일 크기 확인 (HTTP 헤더에서)
            int fileSize = connection.getContentLength();

            // 입력 스트림 열기
            InputStream inputStream = connection.getInputStream();

            // 로컬 파일 생성
            File targetFile = new File(localFilePath);

            // 필요한 경우 상위 디렉토리 생성
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }

            // 파일 출력 스트림 생성
            FileOutputStream outputStream = new FileOutputStream(targetFile);

            // 버퍼 생성
            byte[] buffer = new byte[4096];
            int bytesRead;
            int totalBytesRead = 0;

            // 데이터 읽기 및 진행 상황 추적
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                // 진행률 계산 및 리스너에 알림
                if (fileSize > 0) { // 파일 크기를 알 수 있는 경우에만
                    double progress = (double) totalBytesRead / fileSize;
                    progressListener.onProgressUpdate(progress);
                }
            }

            // 리소스 정리
            outputStream.close();
            inputStream.close();

            return targetFile;
        } catch (Exception e) {
            System.err.println("진행 상황 추적 다운로드 중 오류 발생: " + e.getMessage());
            throw new IOException("파일 다운로드 실패: " + e.getMessage(), e);
        }
    }

    /**
     * 진행 상황 리스너 인터페이스
     */
    public interface ProgressListener {
        /**
         * 진행 상황 업데이트 콜백
         * @param progress 진행률 (0.0 ~ 1.0)
         */
        void onProgressUpdate(double progress);
    }
}
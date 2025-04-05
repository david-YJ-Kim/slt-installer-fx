package com.tsh.slt.installer.bizService;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.tsh.slt.installer.util.StoreDataMappingUtil;
import com.tsh.slt.installer.vo.ServiceDeployInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FirebaseStoreService {

    static final String deployCollectionName = "deploy";

    static final Logger logger = LoggerFactory.getLogger(FirebaseStoreService.class);

    private static FirebaseStoreService instance;

    // 싱글톤 패턴
    public static FirebaseStoreService getInstance() {
        if (instance == null) {
            instance = new FirebaseStoreService();
        }
        return instance;
    }

    private FirebaseStoreService() {
        // 생성자는 private으로 외부에서 직접 생성 불가
    }

    /**
     * 서비스 최신의 배포 버전 가져오기
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public ServiceDeployInfoDto getLatestVersion() throws ExecutionException, InterruptedException {


        // Firestore 인스턴스 가져오기
        Firestore db = FirestoreClient.getFirestore();

        // 타임스탬프 필드를 기준으로 내림차순 정렬 후 첫 번째 문서만 가져오기
        Query query = db.collection(deployCollectionName)
                .orderBy("UPDATE_DT", Query.Direction.DESCENDING)
                .limit(1);

        // 결과 가져오기
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            ServiceDeployInfoDto dto = StoreDataMappingUtil.deployInfoMapping(document);
            System.out.println(dto.toString());
            return dto;

        } else {
            System.out.println("컬렉션에 문서가 없습니다.");
            throw new RuntimeException();
        }
    }

}

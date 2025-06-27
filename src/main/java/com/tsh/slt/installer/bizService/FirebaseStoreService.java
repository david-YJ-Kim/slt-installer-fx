package com.tsh.slt.installer.bizService;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.tsh.slt.installer.enums.DeployInfoColumns;
import com.tsh.slt.installer.enums.ServiceProductId;
import com.tsh.slt.installer.enums.TicketInfoColumns;
import com.tsh.slt.installer.util.StoreDataMappingUtil;
import com.tsh.slt.installer.vo.ServiceDeployInfoDto;
import com.tsh.slt.installer.vo.UserTicketInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FirebaseStoreService {

    static final String deployCollectionName = "deploy";
    static final String userCollectionName = "tickets";

    static final String deployProdIdColName = "PROD_ID";

    static final Logger log = LoggerFactory.getLogger(FirebaseStoreService.class);

    private static FirebaseStoreService instance;

    private Firestore db;

    // 싱글톤 패턴
    public static FirebaseStoreService getInstance() {
        if (instance == null) {
            instance = new FirebaseStoreService();
        }

        if(instance.db == null){
            instance.db = FirestoreClient.getFirestore();
        }
        return instance;
    }

    private FirebaseStoreService() {
        // 생성자는 private으로 외부에서 직접 생성 불가
    }

    // TODO method name change getLatestVersion → getLatestDeployInfo
    /**
     * 서비스 최신의 배포 버전 가져오기
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public ServiceDeployInfoDto getLatestVersion() throws ExecutionException, InterruptedException {

        // 타임스탬프 필드를 기준으로 내림차순 정렬 후 첫 번째 문서만 가져오기
        Query query = db.collection(deployCollectionName)
                .whereEqualTo(deployProdIdColName, ServiceProductId.agent.name())
                .orderBy(DeployInfoColumns.UPDATE_DT.name(), Query.Direction.DESCENDING)
                .limit(1);

        // 결과 가져오기
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            ServiceDeployInfoDto dto = StoreDataMappingUtil.deployInfoMapping(deployCollectionName, document);
            
            log.info(dto.toString());
            return dto;

        } else {
            log.error("Not found deploy info.");
            throw new RuntimeException();
        }
    }

    /**
     * fetch user info by email
     * @param userName
     * @param email
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public UserTicketInfoDto getUserTickerInfo(String userName, String email) throws ExecutionException, InterruptedException {

        // 타임스탬프 필드를 기준으로 내림차순 정렬 후 첫 번째 문서만 가져오기
        Query query = db.collection(userCollectionName)
                .whereEqualTo(TicketInfoColumns.username.name(), userName)
                .whereEqualTo(TicketInfoColumns.email.name(), email)
                .orderBy(TicketInfoColumns.createdAt.name(), Query.Direction.DESCENDING)
                .limit(1);

        // 결과 가져오기
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            UserTicketInfoDto dto = StoreDataMappingUtil.ticketMapping(userCollectionName, document);
            System.out.println(dto.toString());
            return dto;

        } else {
            System.out.println("컬렉션에 문서가 없습니다.");
            throw new RuntimeException();
        }
    }


    /**
     * User 정보에 가용할 포트 정보 업데이트
     * @param dto
     * @param portData
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public boolean updateTicketPortInfo(UserTicketInfoDto dto, List<Integer> portData) {

        if(portData == null || portData.size() != 2){
            System.err.println("Port data is not defined.");
            throw new InvalidParameterException("Port data is missing");
        }

        // 트랜잭션 실행
        ApiFuture<String> future = db.runTransaction(transaction -> {
            // 문서 가져오기
            DocumentReference docRef = db.collection(dto.getCollectionName()).document(dto.getId());
            DocumentSnapshot snapshot = transaction.get(docRef).get();

            if (snapshot.exists()) {

                // 트랜잭션 내 업데이트
                transaction.update(docRef,
                        TicketInfoColumns.agentPort.name(), portData.get(0),
                        TicketInfoColumns.satellitePort.name(), portData.get(1),
                        TicketInfoColumns.updatedAt.name(), Timestamp.now()
                );

                return "버전 업데이트 완료: ";
            } else {
                throw new Exception("문서가 존재하지 않습니다.");
            }
        });

        // 트랜잭션 결과 확인
        String result = null;
        try {
            result = future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}

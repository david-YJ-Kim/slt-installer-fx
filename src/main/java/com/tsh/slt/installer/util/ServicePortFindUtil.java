package com.tsh.slt.installer.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * 시스템에서 사용 가능한 포트를 찾는 유틸리티 클래스
 */
public class ServicePortFindUtil {

    public static void main(String[] args) {
        List<Integer> availableServicePorts = ServicePortFindUtil.findAvailablePorts(15001, 15100, 2);

        for (Integer number : availableServicePorts) {
            System.out.println(number);
        }
    }

    /**
     * 지정된 범위 내에서 요청한 개수만큼 사용 가능한 포트를 찾습니다.
     *
     * @param startPort 시작 포트 번호
     * @param endPort 종료 포트 번호
     * @param count 찾을 포트 개수
     * @return 사용 가능한 포트 목록
     */
    public static List<Integer> findAvailablePorts(int startPort, int endPort, int count) {
        if (startPort <= 0 || endPort > 65535 || startPort > endPort) {
            throw new IllegalArgumentException("유효한 포트 범위는 1-65535이며, 시작 포트는 종료 포트보다 작아야 합니다.");
        }

        List<Integer> availablePorts = new ArrayList<>();
        int currentPort = startPort;

        while (availablePorts.size() < count && currentPort <= endPort) {
            if (isPortAvailable(currentPort)) {
                availablePorts.add(currentPort);
            }
            currentPort++;
        }

        return availablePorts;
    }

    /**
     * 지정된 포트가 사용 가능한지 확인합니다.
     *
     * @param port 확인할 포트 번호
     * @return 포트 사용 가능 여부
     */
    public static boolean isPortAvailable(int port) {
        if (port < 1 || port > 65535) {
            return false;
        }

        ServerSocket socket = null;
        try {
            // ServerSocket을 생성하여 포트가 사용 가능한지 테스트
            socket = new ServerSocket(port);
            socket.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            // 포트가 이미 사용 중인 경우
            return false;
        } finally {
            // 테스트를 위해 생성한 소켓 닫기
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // 무시
                }
            }
        }
    }

    /**
     * 지정된 범위 내의 모든 사용 가능한 포트를 찾습니다.
     *
     * @param startPort 시작 포트 번호
     * @param endPort 종료 포트 번호
     * @return 사용 가능한 포트 목록
     */
    public static List<Integer> findAllAvailablePorts(int startPort, int endPort) {
        if (startPort <= 0 || endPort > 65535 || startPort > endPort) {
            throw new IllegalArgumentException("유효한 포트 범위는 1-65535이며, 시작 포트는 종료 포트보다 작아야 합니다.");
        }

        List<Integer> availablePorts = new ArrayList<>();
        for (int port = startPort; port <= endPort; port++) {
            if (isPortAvailable(port)) {
                availablePorts.add(port);
            }
        }

        return availablePorts;
    }
}
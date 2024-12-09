package com.trip.plan.model.service;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ShortestDistanceService {

    private static final Logger logger = LoggerFactory.getLogger(ShortestDistanceService.class);

    /**
     * 주어진 아이템들을 day 기준으로 분류하고, 각 day별로 위치의 최단 경로를 찾아 정렬된 리스트를 반환합니다.
     *
     * @param items 입력 데이터 리스트
     * @return 정렬된 결과 리스트
     */
    public List<Map<String, Object>> getShortestDistance(List<Map<String, Object>> items) {
        // 결과를 저장할 리스트
        List<Map<String, Object>> result = new ArrayList<>();

        // day 기준으로 아이템들을 그룹화
        Map<Integer, List<Map<String, Object>>> groupedByDay = items.stream()
                .collect(Collectors.groupingBy(item -> getDayAsInteger(item.get("day"))));

        // 각 day별로 처리
        for (Map.Entry<Integer, List<Map<String, Object>>> entry : groupedByDay.entrySet()) {
            Integer day = entry.getKey();
            List<Map<String, Object>> dayItems = entry.getValue();

            // 최단 경로를 찾기 위해 아이템들을 순서대로 정렬
            List<Map<String, Object>> orderedItems = findShortestPath(dayItems);

            // 정렬된 아이템들을 결과 리스트에 추가
            for (int i = 0; i < orderedItems.size(); i++) {
                Map<String, Object> originalItem = orderedItems.get(i);
                Map<String, Object> map = new HashMap<>();
                map.put("day", day);
                map.put("index", i); // 정렬된 순서
                map.put("contentId", originalItem.get("contentId"));
                map.put("latitude", getLatitudeAsDouble(originalItem.get("latitude")));
                map.put("longitude", getLongitudeAsDouble(originalItem.get("longitude")));
                result.add(map);
            }
        }

        return result;
    }

    /**
     * 주어진 리스트에서 최단 경로를 찾기 위해 Nearest Neighbor 알고리즘을 사용하여 아이템들을 정렬합니다.
     *
     * @param items 해당 day의 아이템 리스트
     * @return 정렬된 아이템 리스트
     */
    private List<Map<String, Object>> findShortestPath(List<Map<String, Object>> items) {
        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> remaining = new ArrayList<>(items);
        List<Map<String, Object>> ordered = new ArrayList<>();

        // 시작점으로 첫 번째 아이템 선택
        Map<String, Object> current = remaining.remove(0);
        ordered.add(current);

        while (!remaining.isEmpty()) {
            Map<String, Object> nearest = null;
            double minDistance = Double.MAX_VALUE;

            double currentLat = getLatitudeAsDouble(current.get("latitude"));
            double currentLon = getLongitudeAsDouble(current.get("longitude"));

            // 남은 아이템 중 가장 가까운 아이템 찾기
            for (Map<String, Object> item : remaining) {
                double lat = getLatitudeAsDouble(item.get("latitude"));
                double lon = getLongitudeAsDouble(item.get("longitude"));
                double distance = calculateDistance(currentLat, currentLon, lat, lon);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = item;
                }
            }

            // 가장 가까운 아이템을 순서에 추가하고 remaining에서 제거
            if (nearest != null) {
                ordered.add(nearest);
                remaining.remove(nearest);
                current = nearest;
            } else {
                break;
            }
        }

        return ordered;
    }

    /**
     * 두 지점 간의 거리를 계산합니다. (Haversine 공식을 사용)
     *
     * @param lat1 첫 번째 지점의 위도
     * @param lon1 첫 번째 지점의 경도
     * @param lat2 두 번째 지점의 위도
     * @param lon2 두 번째 지점의 경도
     * @return 두 지점 간의 거리 (킬로미터 단위)
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // 지구 반경 (킬로미터)

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    /**
     * day 값을 Integer로 안전하게 변환합니다.
     *
     * @param dayObj day 값 객체
     * @return Integer로 변환된 day 값
     */
    private Integer getDayAsInteger(Object dayObj) {
        if (dayObj instanceof Integer) {
            return (Integer) dayObj;
        } else if (dayObj instanceof String) {
            try {
                return Integer.parseInt((String) dayObj);
            } catch (NumberFormatException e) {
                logger.error("Invalid day value: {}", dayObj);
                throw new IllegalArgumentException("Invalid day value: " + dayObj, e);
            }
        } else {
            logger.error("Unsupported day type: {}", dayObj.getClass().getName());
            throw new IllegalArgumentException("Unsupported day type: " + dayObj.getClass().getName());
        }
    }

    /**
     * latitude 값을 Double로 안전하게 변환합니다.
     *
     * @param latObj latitude 값 객체
     * @return Double으로 변환된 latitude 값
     */
    private Double getLatitudeAsDouble(Object latObj) {
        if (latObj instanceof Double) {
            return (Double) latObj;
        } else if (latObj instanceof String) {
            try {
                return Double.parseDouble((String) latObj);
            } catch (NumberFormatException e) {
                logger.error("Invalid latitude value: {}", latObj);
                throw new IllegalArgumentException("Invalid latitude value: " + latObj, e);
            }
        } else {
            logger.error("Unsupported latitude type: {}", latObj.getClass().getName());
            throw new IllegalArgumentException("Unsupported latitude type: " + latObj.getClass().getName());
        }
    }

    /**
     * longitude 값을 Double로 안전하게 변환합니다.
     *
     * @param lonObj longitude 값 객체
     * @return Double으로 변환된 longitude 값
     */
    private Double getLongitudeAsDouble(Object lonObj) {
        if (lonObj instanceof Double) {
            return (Double) lonObj;
        } else if (lonObj instanceof String) {
            try {
                return Double.parseDouble((String) lonObj);
            } catch (NumberFormatException e) {
                logger.error("Invalid longitude value: {}", lonObj);
                throw new IllegalArgumentException("Invalid longitude value: " + lonObj, e);
            }
        } else {
            logger.error("Unsupported longitude type: {}", lonObj.getClass().getName());
            throw new IllegalArgumentException("Unsupported longitude type: " + lonObj.getClass().getName());
        }
    }
}

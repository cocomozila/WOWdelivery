package com.wow.delivery.service;

import com.google.common.geometry.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class S2Service {

    private static final int S2_REGION_COVERER_MAX_CELLS = 16;
    private static final List<String> POPULATED_STREET_NAMES = List.of("세종대로", "테헤란로", "강남대로", "올림픽대로", "도산대로", "서초대로", "봉은사로", "학동로", "언주로", "송파대로", "성수이로", "여의대로", "반포대로", "동작대로", "양재대로");

    public List<String> getNearbyCellIdTokens(double latitude, double longitude, double radiusMeters, int cellLevel) {
        S2LatLng center = S2LatLng.fromDegrees(latitude, longitude);

        // 반경을 각도로 변환
        double earthCircumferenceMeters = 2 * Math.PI * 6371000; // 지구의 둘레 (미터 단위)
        double angleDegrees = (360.0 * radiusMeters) / earthCircumferenceMeters;
        S1Angle angle = S1Angle.degrees(angleDegrees);

        S2Cap cap = S2Cap.fromAxisAngle(center.toPoint(), angle);

        // 원하는 크기의 셀을 사용하여 covering 수행
        S2RegionCoverer coverer = new S2RegionCoverer();
        coverer.setMinLevel(cellLevel);
        coverer.setMaxLevel(cellLevel);
        coverer.setMaxCells(S2_REGION_COVERER_MAX_CELLS); // 사용할 셀의 최대 개수를 설정

        ArrayList<S2CellId> coveringCells = new ArrayList<>();
        coverer.getCovering(cap, coveringCells);

        return coveringCells.stream()
            .map(S2CellId::toToken)
            .toList();
    }

    public boolean isPopulatedArea(String state) {
        return POPULATED_STREET_NAMES.contains(state);
    }
}

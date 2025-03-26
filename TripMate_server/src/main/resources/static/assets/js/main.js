// 서비스 키와 URL 설정
//const serviceKey = "bUYZOmaJ1Js%2FEHmEpOykIb6%2BIE%2F8zx6nsYWah9lY4GvbJGvNUqVQoAcHIrY4Y%2BBk7POcU4CH%2B%2FezL8LBPAm2Mw%3D%3D";
const areaUrl = "https://apis.data.go.kr/B551011/KorService1/areaCode1?serviceKey=" + serviceKey + "&numOfRows=20&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json";

// 전역 변수 설정
let markers = []; // 마커 저장 배열
let recentList = []; // 최근 방문 지역 배열
let positions = []; // 마커 위치 배열

// 검색 버튼 이벤트
document.getElementById('btn-search').addEventListener('click', function() {
    const form = document.getElementById('search-form');
    if (form.checkValidity()) {
        form.submit();
    }
});
//document.getElementById('btn-search').click();
// 카카오 지도 설정
const mapContainer = document.getElementById("map");
let mapOption = {
    center: new kakao.maps.LatLng(37.500613, 127.036431), // 초기 지도 중심 좌표
    level: 5, // 지도 확대 레벨
};
let map = new kakao.maps.Map(mapContainer, mapOption); // 지도 생성

// 마커 표시 함수
function displayMarkers(attractions) {
    removeMarkers();  // 기존 마커 삭제

    // attraction 리스트를 순회하며 마커 생성
    attractions.forEach(function(attraction) {
        const position = new kakao.maps.LatLng(attraction.latitude, attraction.longitude);

        // 마커 생성 및 지도에 표시
        const marker = new kakao.maps.Marker({
            position: position,
            map: map,
            image: new kakao.maps.MarkerImage(
                "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png",
                new kakao.maps.Size(24, 35)
            )
        });

        // 마커 배열에 추가
        markers.push(marker);
    });

    // 첫 번째 마커 위치로 지도 중심 이동
    if (attractions.length > 0) {
        map.setCenter(new kakao.maps.LatLng(attractions[0].latitude, attractions[0].longitude));
    }
}

// 기존 마커 삭제 함수
function removeMarkers() {
    markers.forEach(marker => marker.setMap(null)); // 마커 삭제
    markers = [];
}

// 지도 중심 이동 함수
function moveCenter(lat, lng) {
    map.setCenter(new kakao.maps.LatLng(lat, lng));
}


// 각 관광지에 마커 표시
document.querySelectorAll(".attraction").forEach(function(attraction) {
    const position = new kakao.maps.LatLng(attraction.latitude, attraction.longitude);

    // 마커 생성 및 지도에 표시
    const marker = new kakao.maps.Marker({
        position: position,
        map: map
    });

    markers.push(marker);
});

// 로드뷰 함수 (카카오맵 연동)
function roadMap(lat, lng) {
    window.open("https://map.kakao.com/link/roadview/" + lat + "," + lng);
}

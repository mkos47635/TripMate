// 전역 변수 설정
let markers = []; // 마커 저장 배열
let positions = []; // 마커 위치 배열
let polyline;
let selectedSet = new Set(); // 선택된 경로를 저장하는 Set

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
        const position = new kakao.maps.LatLng(attraction.mapy, attraction.mapx);

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
        map.setCenter(new kakao.maps.LatLng(attractions[0].mapy, attractions[0].mapx));
    }
}

// 기존 마커 삭제 함수
function removeMarkers() {
    markers.forEach(marker => marker.setMap(null)); // 마커 삭제
    markers = [];
}

// 선택 목록에 경로 추가 함수 (중복 방지)
function addLinkPrint(img, title, area, mapy, mapx) {
    const item = { mapy, mapx, img, title, area };

    // 중복되지 않도록 Set에 추가
    selectedSet.add(JSON.stringify(item));

    renderSelectedList();
}

// 이미 있는 item을 바로 추가하는 함수
function addLinkPrintByItem(item) {
    // 중복되지 않도록 Set에 추가
    selectedSet.add(JSON.stringify(item));

    renderSelectedList();
}

// 선택된 경로를 출력하는 함수
function renderSelectedList() {
    const selectListElem = document.getElementById("selectList");
    selectListElem.innerHTML = ''; // 기존 리스트 초기화

    // Set을 리스트로 변환하여 순회
    Array.from(selectedSet).forEach((itemStr) => {
        const item = JSON.parse(itemStr);

        let newArea = `
            <li class="list-group-item d-flex justify-content-between align-items-start">
                <div class="ms-2 me-auto">
                    <div class="fw-bold">${item.title}</div>
                    ${item.area}
                </div>
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-car-front-fill" viewBox="0 0 16 16">
                    <path d="M2.52 3.515A2.5 2.5 0 0 1 4.82 2h6.362c1 0 1.904.596 2.298 1.515l.792 1.848c.075.175.21.319.38.404.5.25.855.715.965 1.262l.335 1.679q.05.242.049.49v.413c0 .814-.39 1.543-1 1.997V13.5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5v-1.338c-1.292.048-2.745.088-4 .088s-2.708-.04-4-.088V13.5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5v-1.892c-.61-.454-1-1.183-1-1.997v-.413a2.5 2.5 0 0 1 .049-.49l.335-1.68c.11-.546.465-1.012.964-1.261a.8.8 0 0 0 .381-.404l.792-1.848ZM3 10a1 1 0 1 0 0-2 1 1 0 0 0 0 2m10 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2M6 8a1 1 0 0 0 0 2h4a1 1 0 1 0 0-2zM2.906 5.189a.51.51 0 0 0 .497.731c.91-.073 3.35-.17 4.597-.17s3.688.097 4.597.17a.51.51 0 0 0 .497-.731l-.956-1.913A.5.5 0 0 0 11.691 3H4.309a.5.5 0 0 0-.447.276L2.906 5.19Z"/>
                </svg>
            </li>
        `;
        selectListElem.innerHTML += newArea;
    });
}

// 경로 그리기 함수
document.getElementById("btn-selectedList").addEventListener("click", linkPaint);
// 경로 그리기 함수

function linkPaint() {
    // 기존 선 삭제
    if (polyline) {
        polyline.setMap(null);
    }

    // Set을 리스트로 변환하여 순회
    const selectedList = Array.from(selectedSet).map(itemStr => JSON.parse(itemStr));
    displayMarkers(selectedList);

    const linePath = [];
    const n = selectedList.length;

    // Haversine 공식을 이용한 거리 계산
    function haversineDistance(itemA, itemB) {
        const toRad = (value) => (value * Math.PI) / 180;
        const lat1 = toRad(itemA.mapy);
        const lon1 = toRad(itemA.mapx);
        const lat2 = toRad(itemB.mapy);
        const lon2 = toRad(itemB.mapx);

        const R = 6371; // 지구의 반지름 (킬로미터)
        const dLat = lat2 - lat1;
        const dLon = lon2 - lon1;

        const a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(lat1) * Math.cos(lat2) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2);
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // 거리 (킬로미터)
    }

    // 거리 행렬 계산
    const edges = [];
    for (let i = 0; i < n; i++) {
        for (let j = i + 1; j < n; j++) {
            if (i !== j) {
                const distance = haversineDistance(selectedList[i], selectedList[j]);
                edges.push({ distance, from: i, to: j });
            }
        }
    }

    // 크루스칼 알고리즘으로 MST 생성
    edges.sort((a, b) => a.distance - b.distance); // 거리 기준으로 정렬

    const parent = Array(n).fill(0).map((_, idx) => idx); // Union-Find 초기화

    function find(node) {
        if (parent[node] !== node) {
            parent[node] = find(parent[node]); // 경로 압축
        }
        return parent[node];
    }

    function union(nodeA, nodeB) {
        const rootA = find(nodeA);
        const rootB = find(nodeB);
        if (rootA !== rootB) {
            parent[rootB] = rootA; // 두 집합 합치기
            return true;
        }
        return false;
    }

    // MST 경로 생성
    const usedEdges = []; // 추가된 간선 기록
    for (const edge of edges) {
        if (union(edge.from, edge.to)) {
            usedEdges.push(edge); // 추가된 간선을 기록
            linePath.push(new kakao.maps.LatLng(
                selectedList[edge.from].mapy,
                selectedList[edge.from].mapx
            ));
            linePath.push(new kakao.maps.LatLng(
                selectedList[edge.to].mapy,
                selectedList[edge.to].mapx
            ));
        }
    }

    // 중복된 경로가 있는지 확인
    const uniqueLinePath = [];
    const visitedNodes = new Set();
    linePath.forEach((node) => {
        const nodeKey = `${node.getLat()},${node.getLng()}`; // 좌표를 문자열로 변환하여 고유한 값으로 사용
        if (!visitedNodes.has(nodeKey)) {
            uniqueLinePath.push(node); // 중복되지 않는 노드만 추가
            visitedNodes.add(nodeKey);
        }
    });

    // Polyline 생성 및 지도에 표시
    polyline = new kakao.maps.Polyline({
        path: uniqueLinePath, // 중복이 제거된 경로
        strokeWeight: 5,
        strokeColor: "#FFAE00",
        strokeOpacity: 0.8,
        strokeStyle: "solid"
    });
    polyline.setMap(map);
}

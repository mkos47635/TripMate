<script setup>
import { computed } from "vue"
const props = defineProps({
  attraction: Object,
})

const image = computed(() => {
  return props.attraction.firstimage ? props.attraction.firstimage : "/src/assets/no-image.png"
})

const attractionType = computed(() => {
  let typeStr = ""
  switch (parseInt(props.attraction.contenttypeid)) {
    case 12:
      typeStr = "관광지"
      break
    case 14:
      typeStr = "문화시설"
      break
    case 15:
      typeStr = "축제,공연,행사"
      break
    case 25:
      typeStr = "여행코스"
      break
    case 28:
      typeStr = "레포츠"
      break
    case 32:
      typeStr = "숙박"
      break
    case 38:
      typeStr = "쇼핑"
      break
    case 39:
      typeStr = "음식점"
      break
  }
  return typeStr
})
</script>

<template>
  <div class="card m-2 pt-2" style="width: 18rem">
    <img :src="image" class="card-img-top" alt="..." />
    <div class="card-body">
      <h5 class="card-title">{{ attraction.title }}</h5>
      <p class="card-text">주소 : {{ `${attraction.addr1} ${attraction.addr2}` }}</p>
      <p>유형 : {{ attractionType }}</p>
      <p>위도 : {{ attraction.mapy }}</p>
      <p>경도 : {{ attraction.mapx }}</p>
      <a href="#" class="btn btn-primary">Go Homepage</a>
    </div>
  </div>
</template>

<style scoped></style>

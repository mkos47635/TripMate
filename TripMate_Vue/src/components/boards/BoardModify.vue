<script setup>
import BoardFormItem from "./item/BoardFormItem.vue";
</script>

<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-10">
        <h2 class="my-3 py-3 shadow-sm bg-light text-center">
          <mark class="sky">글수정</mark>
        </h2>
      </div>
      <div class="col-lg-10 text-start">
        <BoardFormItem type="modify" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  margin-top: 60px;
  padding: 30px 40px 30px;
  border: 1px solid #ccc; /* 테두리 추가 */
  border-radius: 20px; /* 모서리 둥글게 */
  background-color: #f9f9f9;
}

h2 {
  color: #75aae2;
  font-size: 1.6em;
  margin-bottom: 20px;
  text-align: center;
  border-bottom: 2px solid #75aae2; /* 제목 아래에 테두리 추가 */
  padding-bottom: 10px; /* 제목 아래에 여백 추가 */
}

.sky {
  color: #75aae2;
  background-color: transparent; /* mark 요소의 배경 색상 설정 */
}
</style>

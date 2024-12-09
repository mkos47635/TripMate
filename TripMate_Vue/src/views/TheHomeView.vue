<template>
  <main>
    <h2>떠나고 싶은 곳을 찾아보세요!</h2>
    <h3>TripMate와 함께 당신의 다음 여행지를 찾아보세요.</h3>
    <div class="slider">
      <div
        class="slide-track"
        :style="{ transform: `translateX(-${currentImage * 100}%)` }"
      >
        <img
          v-for="(image, index) in images"
          :key="index"
          :src="image"
          alt="배경여행사진"
        />
      </div>
    </div>
    <div class="content">
      <p>
        TripMate는 인기 있는 여행지 정보를 제공하며, 맞춤형 여행 계획을
        도와드립니다.
      </p>
      <button @click="goToSearch">여행지 추천 받기</button>
    </div>
  </main>
</template>

<script>
export default {
  data() {
    return {
      images: ["/background1.png", "/background4.jpg", "/background3.jpg", "/background6.jpg"],
      currentImage: 0,
    };
  },
  mounted() {
    // 슬라이드 애니메이션 설정
    setInterval(() => {
      this.currentImage = (this.currentImage + 1) % this.images.length;
    }, 3000); // 3초마다 슬라이드 전환
  },
};
</script>

<style scoped>
main {
  padding: 130px 10px 30px;
  background-color: #e8f5f5;
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
}

main h2 {
  color: #2ec4ff;
  font-size: 1.8em;
  margin-top: 5px;
  margin-bottom: 10px;
}

main h3 {
  color: #7dcbff;
  font-size: 1.2em;
  margin-bottom: 15px;
}

.slider {
  position: relative;
  width: 100%;
  height: auto;
  margin-top: 30px;
  overflow: hidden;
  border-radius: 20px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
}

.slide-track {
  display: flex;
  transition: transform 1s ease-in-out; /* 슬라이드 전환 효과 */
}

.slider img {
  width: 100%;
  height: 600px;
  flex-shrink: 0; /* 모든 이미지의 크기를 동일하게 유지 */
}

.content {
  margin-top: 40px;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 15px;
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
}

.content p {
  color: #555;
  font-size: 1.1em;
  margin-bottom: 20px;
}

button {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 1em;
}

button:hover {
  background-color: #0056b3;
}
</style>

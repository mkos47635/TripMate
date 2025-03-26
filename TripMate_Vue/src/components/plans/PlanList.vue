<script setup>
import PlanListItem from '@/components/plans/item/PlanListItem.vue';
import { useRouter } from 'vue-router';
import { getPlanList, deletePlan, generateShareLink } from "@/api/plan.js";
import { ref, onMounted } from "vue";
import { useAuthStore } from "@/stores/authStore";
import { usePlanStore } from "@/stores/planStore";
const authStore = useAuthStore();
const planStore = usePlanStore();
const router = useRouter();
const plans = ref([]); // 계획 리스트 저장
const userId = authStore.loginUserId;

// 데이터 로드
const loadPlans = () => {
  getPlanList(
    { userId },
    ({ data }) => {
      plans.value = data;
    },
    (error) => {
      console.error("API 요청 실패:", error);
    }
  );
};

// Plan 삭제
const handleDelete = async (planId) => {
  if (confirm("이 계획을 삭제하시겠습니까?")) {
    await deletePlan(
      Number(planId),
      () => {
        plans.value = plans.value.filter(plan => plan.planId !== planId); // 삭제된 계획 제거
        alert("계획이 삭제되었습니다.");
      },
      (error) => {
        console.error("삭제 실패:", error);
        alert("삭제에 실패했습니다.");
      }
    );
  }
};
const handleShare = (planId) => {
  // 공유 링크 요청
  generateShareLink(
    planId,
    (response) => {
      const shareLink = response.data; // 서버에서 반환된 공유 링크
      console.log("Generated Share Link:", shareLink);

      // 클립보드에 복사
      navigator.clipboard.writeText(shareLink)
        .then(() => {
          alert("공유 링크가 클립보드에 복사되었습니다:\n" + shareLink);
        })
        .catch((err) => {
          console.error("클립보드 복사 실패:", err);
          alert("공유 링크 복사에 실패했습니다.");
        });
    },
    (error) => {
      console.error("공유 링크 생성 실패:", error);
      alert("공유 링크 생성에 실패했습니다.");
    }
  );
};

// 초기 데이터 로드
onMounted(() => {
  loadPlans();
});

// Plan 생성 페이지로 이동
const navigateToPlanMake = (id, title, day) => {
  planStore.setPlanInfo(id, title, day);
  router.push({ name: 'plan-make', params: { id } });
};
</script>

<template>
  <div class="plan-list">
    <div class="plan-list-grid">
      <PlanListItem v-for="plan in plans" :key="plan.planId" :plan-id="plan.planId" :title="plan.title"
        :description="plan.description" :image="plan.image || '/src/assets/img/noImage.png'" :tourDay="plan.tourDay"
        @click="navigateToPlanMake(plan.planId, plan.title, plan.tourDay)" @delete="handleDelete"
        @share="handleShare" />
    </div>
  </div>
</template>

<style scoped>
.plan-list {
  margin-top: 70px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  padding: 20px 50px 20px;
  background-color: #f7fdff;
  width: 1200px;
  border-radius: 20px;
  box-shadow: 0 -1px 8px rgba(0, 0, 0, 0.1);
}

.plan-list-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  width: 100%;
  max-width: 1200px;
}
</style>

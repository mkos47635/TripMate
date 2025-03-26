import { defineStore } from "pinia";
import { ref, computed } from "vue";

export const usePlanStore = defineStore(
  "plan",
  () => {
    const selectDay = ref("1");
    const planInfo = ref({
      planId: "",
      title: "",
      tourDay: "",
    });

    const setPlanInfo = (planId, title, tourDay) => {
      planInfo.value = {
        planId,
        title,
        tourDay,
      };
    };

    // 추가된 아이템 리스트
    const items = ref([]);

    const addItem = (
      planId,
      contentId,
      title,
      addr1,
      addr2,
      day,
      latitude,
      longitude
    ) => {
      if (!contentId) {
        console.warn("Invalid contentId. Cannot add item:", {
          planId,
          contentId,
          title,
          addr1,
          addr2,
          day,
          latitude,
          longitude,
        });
        return;
      }

      const exists = items.value.some(
        (item) => item.contentId === contentId && item.day === day
      );
      if (!exists) {
        items.value.push({
          planId,
          contentId,
          title,
          addr1,
          addr2,
          day,
          latitude,
          longitude,
          planIndex: items.value.filter((item) => item.day === day).length, // day별 index 계산
        });
      } else {
        console.warn(
          `Item with contentId ${contentId} for day ${day} already exists in the list.`
        );
      }
    };
    
    // selectDay과 일치하는 day의 아이템들만 반환하는 computed 속성
    const filteredItems = computed(() => {
      return items.value.filter(item => item.day === selectDay.value);
    });

    return {
      selectDay,
      planInfo,
      setPlanInfo,
      items,
      addItem,
      filteredItems, // 추가된 속성
    };

    
  },
  {
    persist: {
      storage: sessionStorage,
    },
  }
);

import { localAxios } from "../util/http-commons";

export const generateItinerary = async (inputs) => {
  try {
    const response = await localAxios().post("/ai", {
      model: "gpt-3.5-turbo",
      prompt: `
        여행 목적에 따라 추천 여행 일정을 만들어주세요:
        여행지: ${inputs.destination}
        여행 기간: ${inputs.days}일
        여행 목적: ${inputs.purpose}
        예산: ${inputs.budget}
        여행 시간대: ${inputs.timeFrame}
      `,
      maxTokens: 1500,
      temperature: 0.7,
    });
      console.log(inputs.destination);
      console.log(inputs.destination);
      console.log(inputs.destination);
      console.log(inputs.destination);
      console.log(inputs.destination);
      console.log(inputs.destination);
    return response.data;
  } catch (error) {
    console.error("일정 생성 실패:", error);
    throw error;
  }
};

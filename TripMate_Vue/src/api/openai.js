import OpenAI from "openai";

// OpenAI API 설정
const openai = new OpenAI({
  apiKey: VITE_OPEN_API_SERVICE_KEY, // OpenAI API 키
});

export const generateItinerary = async (inputs) => {
  const { destination, days, purpose, budget, timeFrame } = inputs;

  const prompt = `
    여행 목적에 따라 추천 여행 일정을 만들어주세요:
    - 여행지: ${destination}
    - 여행 기간: ${days}일
    - 여행 목적: ${purpose}
    - 예산: ${budget}
    - 여행 시간대: ${timeFrame}
    
    형식:
    Day 1:
    - 오전:
    - 점심:
    - 오후:
    - 저녁:
    
    Day 2:
    - 오전:
    - 점심:
    - 오후:
    - 저녁:
  `;

  try {
    const response = await openai.chat.completions.create({
      model: "gpt-3.5-turbo",
      messages: [
        { role: "system", content: "당신은 여행 일정 생성 전문가입니다." },
        { role: "user", content: prompt },
      ],
      max_tokens: 1500,
      temperature: 0.7,
    });

    return response.choices[0].message.content.trim();
  } catch (error) {
    console.error("OpenAI API 요청 실패:", error);
    throw error;
  }
};

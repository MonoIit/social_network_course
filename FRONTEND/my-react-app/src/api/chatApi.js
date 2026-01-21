const BASE = "http://localhost:8080/api/v1";

const headers = () => ({
  "auth-token": localStorage.getItem("auth-token")
});

export async function getChatMessages(chatId) {
  const res = await fetch(`${BASE}/messenger/${chatId}`, {
    headers: headers()
  });

  if (!res.ok) {
    throw new Error("CHAT_LOAD_ERROR");
  }

  return res.json(); // ChatResponse[]
}

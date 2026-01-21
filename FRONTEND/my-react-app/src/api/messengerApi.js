const BASE = "http://localhost:8080/api/v1";

const headers = () => ({
  "auth-token": localStorage.getItem("auth-token")
});

export async function getChats() {
  const res = await fetch(`${BASE}/messenger`, {
    headers: headers()
  });

  if (!res.ok) {
    throw new Error("CHAT_LOAD_ERROR");
  }

  return res.json(); // ChatResponse[]
}

export async function createGroupChat(name, photo) {
  const form = new FormData();
  form.append("data", new Blob(
    [JSON.stringify({ name })],
    { type: "application/json" }
  ));
  if (photo) form.append("photo", photo);

  await fetch(`${BASE}/messenger`, {
    method: "POST",
    headers: headers(),
    body: form
  });
}

export async function quitChat(chatId) {
  await fetch(`${BASE}/messenger/${chatId}/quit`, {
    method: "POST",
    headers: headers()
  });
}

export async function editGroup(chatId, name, photo) {
  const form = new FormData();
  form.append("data", new Blob(
    [JSON.stringify({ name })],
    { type: "application/json" }
  ));
  if (photo) form.append("photo", photo);

  await fetch(`${BASE}/messenger/${chatId}/edit`, {
    method: "PATCH",
    headers: headers(),
    body: form
  });
}

export async function invite(chatId, userId) {
  await fetch(`${BASE}/messenger/${chatId}/invite`, {
    method: "POST",
    headers: {
      "auth-token": localStorage.getItem("auth-token"),
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ userId })
  });
}

export async function remove(chatId, userId) {
  await fetch(`${BASE}/messenger/${chatId}/remove`, {
    method: "POST",
    headers: {
      "auth-token": localStorage.getItem("auth-token"),
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ userId })
  });
}

export async function getParticipants(chatId) {
  const res = await fetch(`${BASE}/messenger/${chatId}/participants`, { headers: headers() });
  if (!res.ok) throw new Error("PARTICIPANTS_ERROR");
  return res.json();
}

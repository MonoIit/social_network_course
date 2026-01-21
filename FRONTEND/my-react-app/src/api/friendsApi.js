const BASE = "http://localhost:8080/api/v1";

const headers = () => ({
  "Content-Type": "application/json",
  "auth-token": localStorage.getItem("auth-token")
});

export async function getFriendRequests() {
  const res = await fetch(`${BASE}/friends/requests`, { headers: headers() });
  return res.json();
}

export async function sendRequest(friendId) {
  return await fetch(`${BASE}/friends/${friendId}/requests`, {
    method: "POST",
    headers: headers()
  });
}

export async function getFriends() {
  const res = await fetch(`${BASE}/friends`, {
    method: "GET",
    headers: headers()
  });
  if (!res.ok) throw new Error("friends error");
  return res.json();
}

export async function acceptFriend(friendId) {
  return fetch(`${BASE}/friends/${friendId}/requests`, {
    method: "PATCH",
    headers: headers(),
    body: JSON.stringify({ status: "APPROVED" })
  });
}

export async function deleteFriend(friendId) {
  return fetch(`${BASE}/friends/${friendId}`, {
    method: "DELETE",
    headers: headers()
  });
}

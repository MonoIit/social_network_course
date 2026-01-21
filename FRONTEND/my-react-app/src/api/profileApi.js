const BASE = "http://localhost:8080/api/v1";

const headers = () => ({
  "auth-token": localStorage.getItem("auth-token")
});

export async function getProfile(userId) {
  const res = await fetch(`${BASE}/profile/${userId}`, {
    headers: headers()
  });
  if (!res.ok) throw new Error("profile error");
  return res.json();
}

export async function getProfilePhoto(userId) {
  const res = await fetch(`${BASE}/profile/${userId}/photo`, {
    headers: headers()
  });
  if (!res.ok) return null;
  return URL.createObjectURL(await res.blob());
}

export async function findProfileByName(name) {
  const res = await fetch(
    `${BASE}/profile/by-name/${encodeURIComponent(name)}`,
    { headers: headers() }
  );
  if (!res.ok) return null;
  return res.json();
}

export async function updateProfile(id, data, photo) {
  const formData = new FormData();
  formData.append(
    "data",
    new Blob([JSON.stringify(data)], { type: "application/json" })
  );
  if (photo) formData.append("photo", photo);

  const res = await fetch(`${BASE}/profile/${id}`, {
    method: "PUT",
    headers: headers(),
    body: formData
  });

  if (!res.ok) throw new Error("PROFILE_UPDATE_ERROR");
}
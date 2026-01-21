const BASE = "http://localhost:8080/api/v1";

const headers = () => ({
  "auth-token": localStorage.getItem("auth-token")
});

export async function getPosts() {
  const res = await fetch(`${BASE}/posts`, { headers: headers() });
  if (!res.ok) throw new Error("POSTS_ERROR");
  return res.json();
}

export async function getPostPhoto(postId) {
  const res = await fetch(`${BASE}/posts/${postId}/photo`, {
    headers: headers()
  });
  if (!res.ok) return null;
  return URL.createObjectURL(await res.blob());
}

export async function createPost({ userId, text, photo, token }) {
    const formData = new FormData();
    formData.append(
        "data",
        new Blob([JSON.stringify({ userId, text })], { type: "application/json" })
    );

    if (photo) {
        formData.append("photo", photo);
    }

    const response = await fetch(`${BASE}/posts`, {
        method: "POST",
        headers: { "auth-token": token },
        body: formData
    });

    if (!response.ok) {
        throw new Error("Не удалось создать пост");
    }
}

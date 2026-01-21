import { useEffect, useState } from "react";
import * as authApi from "../api/authApi";
import * as postsApi from "../api/postsApi";
import * as profileApi from "../api/profileApi";

export function useFeedViewModel() {
  const [posts, setPosts] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadFeed();
  }, []);

  async function loadFeed() {
    setLoading(true);
    setError("");

    const token = localStorage.getItem("auth-token");

    try {
      /* 1. validate token */
      const auth = await authApi.validateToken(token);
      localStorage.setItem("user-id", auth.userId);

      /* 2. load posts */
      const rawPosts = await postsApi.getPosts();

      /* 3. cache profiles */
      const profileCache = new Map();

      const enriched = await Promise.all(
        rawPosts.map(async post => {
          /* username */
          if (!profileCache.has(post.userId)) {
            const profile = await profileApi.getProfile(post.userId);
            profileCache.set(
              post.userId,
              profile?.name || profile?.login || `User ${post.userId}`
            );
          }

          /* post photo */
          let photoUrl = null;
          if (post.hasPhoto) {
            photoUrl = await postsApi.getPostPhoto(post.id);
          }

          return {
            ...post,
            userName: profileCache.get(post.userId),
            photoUrl
          };
        })
      );

      setPosts(enriched);
    } catch (err) {
      if (err.message === "UNAUTHORIZED") {
        setError("Не авторизован");
      } else {
        setError("Ошибка загрузки ленты");
      }
    } finally {
      setLoading(false);
    }
  }

  return {
    posts,
    error,
    loading
  };
}

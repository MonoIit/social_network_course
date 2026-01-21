import { useEffect, useMemo, useState } from "react";
import * as friendsApi from "../api/friendsApi";
import * as profileApi from "../api/profileApi";

const DEFAULT_AVATAR = "https://i.pravatar.cc/150?img=3";

const statusOrder = {
  RECEIVED: 1,
  APPROVED: 2,
  SENT: 3
};

export function useFriendsViewModel() {
  const [friends, setFriends] = useState([]);
  const [query, setQuery] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    loadFriends();
  }, []);

  async function loadFriends() {
    setError("");

    try {
      const raw = await friendsApi.getFriends();

      const enriched = await Promise.all(
        raw.map(async f => {
          try {
            const profile = await profileApi.getProfile(f.id);

            let avatar = DEFAULT_AVATAR;
            if (profile.hasPhoto) {
              const photo = await profileApi.getProfilePhoto(f.id);
              if (photo) avatar = photo;
            }

            return {
              ...f,
              login: profile.login,
              avatar
            };
          } catch {
            return { ...f, login: "Неизвестный", avatar: DEFAULT_AVATAR };
          }
        })
      );

      setFriends(enriched);
    } catch {
      setError("Ошибка соединения с сервером");
    }
  }

  const sortedFriends = useMemo(
    () => [...friends].sort((a, b) => statusOrder[a.status] - statusOrder[b.status]),
    [friends]
  );

  async function searchProfile(navigate) {
    setError("");
    const profile = await profileApi.findProfileByName(query);
    if (!profile) {
      setError("Пользователь не найден");
      return;
    }
    navigate(`/profile/${profile.id}`);
  }

  async function accept(friendId) {
    await friendsApi.acceptFriend(friendId);
    setFriends(prev =>
      prev.map(f => (f.id === friendId ? { ...f, status: "APPROVED" } : f))
    );
  }

  async function remove(friendId) {
    await friendsApi.deleteFriend(friendId);
    setFriends(prev => prev.filter(f => f.id !== friendId));
  }

  return {
    query,
    setQuery,
    friends: sortedFriends,
    error,
    searchProfile,
    accept,
    remove
  };
}

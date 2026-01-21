import { useEffect, useState } from "react";
import * as profileApi from "../api/profileApi";
import * as friendsApi from "../api/friendsApi";

export function useProfileViewModel(profileId) {
  const myUserId = Number(localStorage.getItem("user-id"));
  const isMyProfile = myUserId === Number(profileId);

  const [profile, setProfile] = useState(null);
  const [status, setStatus] = useState("NONE");
  const [error, setError] = useState("");
  const [isEditing, setIsEditing] = useState(false);
  const [editData, setEditData] = useState({ login: "", email: "", photo: null });

  /* ---------- загрузка профиля ---------- */
  useEffect(() => {
    loadProfile();
    if (!isMyProfile) loadFriendStatus();
  }, [profileId]);

  async function loadProfile() {
    try {
      const data = await profileApi.getProfile(profileId);
      let photo = null;

      if (data.hasPhoto) {
        photo = await profileApi.getProfilePhoto(profileId);
      }

      setProfile({ ...data, photo });
      setEditData({ login: data.login, email: data.email, photo: null });
    } catch {
      setError("Не удалось загрузить профиль");
    }
  }

  async function loadFriendStatus() {
    try {
      const requests = await friendsApi.getFriendRequests();
      const relation = requests.find(
        r => r.friendId === Number(profileId) || r.userId === Number(profileId)
      );
      setStatus(relation?.status ?? "NONE");
    } catch {
      setError("Ошибка загрузки статуса дружбы");
    }
  }

  /* ---------- действия ---------- */
  async function sendFriendRequest() {
    await friendsApi.sendRequest(profileId);
    loadFriendStatus();
  }

  async function acceptFriendRequest() {
    await friendsApi.acceptRequest(profileId);
    loadFriendStatus();
  }

  async function deleteFriend() {
    await friendsApi.deleteFriend(profileId);
    loadFriendStatus();
  }

  async function saveProfile() {
    try {
      await profileApi.updateProfile(
        profileId,
        { login: editData.login, email: editData.email },
        editData.photo
      );

      setProfile(prev => ({
        ...prev,
        login: editData.login,
        email: editData.email,
        photo: editData.photo
          ? URL.createObjectURL(editData.photo)
          : prev.photo
      }));

      setIsEditing(false);
    } catch {
      setError("Ошибка обновления профиля");
    }
  }

  return {
    profile,
    isMyProfile,
    status,
    isEditing,
    editData,
    setEditData,
    setIsEditing,
    error,

    sendFriendRequest,
    acceptFriendRequest,
    deleteFriend,
    saveProfile
  };
}

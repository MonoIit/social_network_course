import { useEffect, useState } from "react";
import * as messengerApi from "../api/messengerApi";
import * as profileApi from "../api/profileApi";

const DEFAULT_AVATAR = "/default-avatar.png";

export function useMessagesViewModel() {
  const [chats, setChats] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    loadChats();
  }, []);

  async function loadChats() {
    setLoading(true);
    setError("");

    try {
      const chatResponses = await messengerApi.getChats();

      const enriched = await Promise.all(
        chatResponses.map(async (chat) => {
          // =========================
          // ГРУППОВОЙ ЧАТ
          // =========================
          if (chat.friendId === -1) {
            let avatar = DEFAULT_AVATAR;

            if (chat.photo) {
              avatar = `data:image/jpeg;base64,${chat.photo}`
            }

            return {
              chatId: chat.chatId,
              type: "GROUP",
              title: chat.name ?? "Групповой чат",
              avatar
            };
          }

          // =========================
          // ЛИЧНЫЙ ЧАТ
          // =========================
          const profile = await profileApi.getProfile(chat.friendId);

          let avatar = DEFAULT_AVATAR;
          if (profile?.hasPhoto) {
            const photo = await profileApi.getProfilePhoto(chat.friendId);
            if (photo) avatar = photo;
          }

          return {
            chatId: chat.chatId,
            type: "PRIVATE",
            friendId: chat.friendId,
            title: profile?.login ?? "Неизвестный",
            avatar
          };
        })
      );

      setChats(enriched);
    } catch (e) {
      console.error(e);
      setError("Ошибка загрузки чатов");
    } finally {
      setLoading(false);
    }
  }

  return {
    chats,
    loading,
    error
  };
}

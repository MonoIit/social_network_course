import { useEffect, useState } from "react";
import * as messengerApi from "../api/messengerApi";
import * as friendsApi from "../api/friendsApi";
import * as profileApi from "../api/profileApi";


export default function EditGroupModal({ chatId, onClose }) {
  const [name, setName] = useState("");
  const [photo, setPhoto] = useState(null);
  const [friends, setFriends] = useState([]);

  const DEFAULT_AVATAR = "https://i.pravatar.cc/150?img=3";

  useEffect(() => {
    loadFriends();
  }, []);

  async function loadFriends() {
    const raw = await friendsApi.getFriends();
    const participants = await messengerApi.getParticipants(chatId);

    const enriched = await Promise.all(
      raw.map(async f => {
        try {
          const profile = await profileApi.getProfile(f.id);

          let isParticipant = false;
          if (participants.some(p => p.userId === f.id)) {
            isParticipant = true;
          }

          let avatar = DEFAULT_AVATAR;
          if (profile.hasPhoto) {
            const photo = await profileApi.getProfilePhoto(f.id);
            if (photo) avatar = photo;
          }

          return {
            ...f,
            login: profile.login,
            avatar,
            isParticipant
          };
        } catch {
          return { ...f, login: "Неизвестный", avatar: DEFAULT_AVATAR, isParticipant: false };
        }
      })
    );

    setFriends(enriched);
  }

  async function save() {
    await messengerApi.editGroup(chatId, name, photo);
    onClose();
  }

  async function add(userId) {
    await messengerApi.invite(chatId, userId);
    loadFriends();
  }

  async function remove(userId) {
    await messengerApi.remove(chatId, userId);
    loadFriends();
  }

  return (
    <div className="modal-overlay">
      <div className="modal-card">
        <header className="modal-header">
          <h2>Редактирование чата</h2>
        </header>

        <section className="modal-section">
          <label className="field">
            <span className="field-label">Название чата</span>
            <input
              className="field-input"
              value={name}
              onChange={e => setName(e.target.value)}
              placeholder="Введите название"
            />
          </label>

          <label className="field">
            <span className="field-label">Аватар</span>
            <input
              className="field-file"
              type="file"
              accept="image/*"
              onChange={e => setPhoto(e.target.files[0])}
            />
          </label>
        </section>

        <section className="modal-section">
        <h3 className="section-title">Участники</h3>

        <div className="friends-list">
          {friends.map(f => (
            <div key={f.id} className="friend-item">
              <img
                className="friend-avatar"
                src={f.avatar}
                alt={f.login}
              />

              <span className="friend-name">{f.login}</span>

              {f.isParticipant ? (
                <button
                  className="btn btn-danger"
                  onClick={() => remove(f.id)}
                >
                  Удалить
                </button>
              ) : (
                <button
                  className="btn btn-primary"
                  onClick={() => add(f.id)}
                >
                  Добавить
                </button>
              )}
            </div>
          ))}
        </div>
      </section>

        <footer className="modal-actions">
          <button className="btn btn-primary" onClick={save}>
            Сохранить
          </button>
          <button className="btn btn-secondary" onClick={onClose}>
            Отмена
          </button>
        </footer>
      </div>
    </div>
  );
}

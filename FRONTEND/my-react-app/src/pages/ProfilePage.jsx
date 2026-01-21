import { useParams, useNavigate } from "react-router-dom";
import { useProfileViewModel } from "../viewmodels/useProfileViewModel";

export default function ProfilePage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const {
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
  } = useProfileViewModel(id);

  if (!profile) return <div>Загрузка...</div>;

  return (
    <div className="profile-page">
      <div className="profile-header">
        <img
          src={profile.photo || "https://i.pravatar.cc/200"}
          alt="avatar"
          className="profile-avatar"
        />

        <div className="profile-info">
          {isEditing ? (
            <div className="edit-form">
              <input
                className="input"
                value={editData.login}
                placeholder="Логин"
                onChange={e => setEditData({ ...editData, login: e.target.value })}
              />
              <input
                className="input"
                value={editData.email}
                placeholder="Email"
                onChange={e => setEditData({ ...editData, email: e.target.value })}
              />
              <label className="btn secondary file-label">
                Загрузить фото
                <input
                  type="file"
                  accept="image/*"
                  hidden
                  onChange={e => setEditData({ ...editData, photo: e.target.files[0] })}
                />
              </label>

              <div className="edit-buttons">
                <button className="btn" onClick={saveProfile}>Сохранить</button>
                <button className="btn danger" onClick={() => setIsEditing(false)}>Отмена</button>
              </div>
            </div>
          ) : (
            <>
              <h2 className="profile-login">{profile.login}</h2>
              <div className="profile-email">{profile.email}</div>

              {isMyProfile && (
                <button className="btn secondary" onClick={() => setIsEditing(true)}>
                  Редактировать профиль
                </button>
              )}

              {!isMyProfile && (
                <div className="profile-actions">
                  {status === "NONE" && <button className="btn" onClick={sendFriendRequest}>Добавить</button>}
                  {status === "SENT" && <button className="btn danger" onClick={deleteFriend}>Отменить</button>}
                  {status === "RECEIVED" && (
                    <>
                      <button className="btn" onClick={acceptFriendRequest}>Принять</button>
                      <button className="btn danger" onClick={deleteFriend}>Отклонить</button>
                    </>
                  )}
                  {status === "APPROVED" && (
                    <>
                      <button className="btn" onClick={() => navigate(`/chats/1`)}>Сообщение</button>
                      <button className="btn danger" onClick={deleteFriend}>Удалить</button>
                    </>
                  )}
                </div>
              )}
            </>
          )}
        </div>
      </div>

      {error && <div className="error-msg">{error}</div>}
    </div>
  );
}

import { useNavigate } from "react-router-dom";
import { useFriendsViewModel } from "../viewmodels/useFriendsViewModel";
import FriendCard from "../components/FriendCard";

export default function FriendsPage() {
  const navigate = useNavigate();

  const {
    query,
    setQuery,
    friends,
    error,
    searchProfile,
    accept,
    remove
  } = useFriendsViewModel();

  return (
    <div className="friends-page">
      <h2 className="page-title">Друзья</h2>

      <div className="friends-search">
        <input
          className="input"
          value={query}
          placeholder="Введите имя..."
          onChange={e => setQuery(e.target.value)}
        />
        <button className="btn" onClick={() => searchProfile(navigate)}>
          Найти
        </button>
      </div>

      {error && <div className="error-msg">{error}</div>}

      <h3 className="section-title">Ваши друзья</h3>

      <div className="friends-list">
        {friends.map(f => (
          <FriendCard
            key={f.id}
            friend={f}
            onAccept={() => accept(f.id)}
            onDelete={() => remove(f.id)}
            onMessage={() => navigate(`/chats/${f.chatId}`)}
          />
        ))}
      </div>
    </div>
  );
}


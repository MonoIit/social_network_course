export default function FriendCard({ friend, onAccept, onDelete, onMessage }) {
  return (
    <div className="friend-card">
      <img src={friend.avatar} className="friend-avatar" />

      <div className="friend-info">
        <div className="friend-name">
          {friend.login}
          {friend.status === "RECEIVED" && " • входящий запрос"}
          {friend.status === "SENT" && " • исходящий запрос"}
        </div>

        <div className="friend-actions">
          {friend.status === "APPROVED" && (
            <button className="btn gray small" onClick={onMessage}>
              Сообщение
            </button>
          )}

          {friend.status === "RECEIVED" && (
            <button className="btn small" onClick={onAccept}>
              Принять
            </button>
          )}

          <button className="btn danger small" onClick={onDelete}>
            Удалить
          </button>
        </div>
      </div>
    </div>
  );
}

import { useNavigate } from "react-router-dom";
import { useMessagesViewModel } from "../viewmodels/useMessagesViewModel";

export default function MessagePage() {
  const { chats, loading, error } = useMessagesViewModel();
  const navigate = useNavigate();

  return (
    <div className="messages-page">
      {/* header */}
      <div className="messages-header">
        <h2>Сообщения</h2>

        <button
          className="btn"
          onClick={() => navigate("/message/create")}
        >
          Создать чат
        </button>
      </div>

      {/* body */}
      <div className="messages-body">
        {loading && <div className="loading">Загрузка...</div>}
        {error && <div className="error-msg">{error}</div>}

        <div className="chat-list">
          {chats.map(chat => (
            <div key={chat.chatId} className="chat-item">
              <img
                src={chat.avatar}
                className="chat-avatar"
                alt="avatar"
              />

              <div className="chat-info">
                <div className="chat-name">{chat.title}</div>

                <button
                  className="btn gray"
                  onClick={() =>
                    navigate(`/chats/${chat.chatId}`, {
                      state: { type: chat.type }
                    })
                  }
                >
                  Сообщить
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}


import { useRef, useEffect, useState } from "react";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import { useChatViewModel } from "../viewmodels/useChatViewModel";
import EditGroupModal from "../components/EditGroupModal";
import * as messengerApi from "../api/messengerApi";

export default function GroupChatPage() {
    const location = useLocation();
    const { type } = location.state || {};
    const { chatId } = useParams();
    const navigate = useNavigate();

    const [editOpen, setEditOpen] = useState(false);

    const isGroup = type === "GROUP";
    const hasChatMeta = type !== undefined;

    const {
        messages, text, setText, connected, sendMessage, myUserId
    } = useChatViewModel(chatId);

    const messagesEndRef = useRef(null);

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages]);

    async function quit() {
        await messengerApi.quitChat(chatId);
        navigate("/messages");
    }

    return (
        <div className="chat-page">
            {hasChatMeta && isGroup && (
                <header className="chat-header">
                    <div className="chat-header-actions">
                        <button
                            className="btn btn-primary"
                            onClick={() => setEditOpen(true)}
                        >
                            Редактировать
                        </button>

                        <button
                            className="btn btn-danger"
                            onClick={quit}
                        >
                            Выйти
                        </button>
                    </div>
                </header>
            )}

            <main className="chat-body">
                <div className="chat-messages">
                    {messages.map(m => {
                        const isMine = m.senderId === myUserId;

                        return (
                            <div
                                key={m.id ?? m.tempId}
                                className={`message-row ${isMine ? "my" : "other"}`}
                            >
                                {!isMine && (
                                    <img
                                        src={m.avatar}
                                        className="message-avatar"
                                        alt="avatar"
                                    />
                                )}

                                <div className="message-bubble">
                                    {m.text}
                                </div>
                            </div>
                        );
                    })}

                    <div ref={messagesEndRef} />
                </div>
            </main>

            <footer className="chat-footer">
                <div className="chat-input-wrapper">
                    <input
                        className="chat-input"
                        value={text}
                        onChange={e => setText(e.target.value)}
                        disabled={!connected}
                        placeholder={
                            connected ? "Введите сообщение…" : "Соединение отсутствует"
                        }
                    />

                    <button
                        className="btn btn-primary"
                        onClick={sendMessage}
                        disabled={!connected || !text.trim()}
                    >
                        Отправить
                    </button>
                </div>
            </footer>

            {editOpen && (
                <EditGroupModal
                    chatId={chatId}
                    onClose={() => setEditOpen(false)}
                />
            )}
        </div>
    );

}

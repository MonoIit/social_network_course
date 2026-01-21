import { useRef, useEffect } from "react";
import { useParams } from "react-router-dom";
import { useChatViewModel } from "../viewmodels/useChatViewModel";

export default function ChatPage() {
    const { chatId } = useParams();
    const {
        messages, text, setText, connected, sendMessage, myUserId
    } = useChatViewModel(chatId);

    const messagesEndRef = useRef(null);

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages]);

    return (
        <div className="chat-page">
            <div className="chat-messages">
                {messages.map((m) => (
                    <div
                        key={m.id}
                        className={`message ${m.senderId === myUserId ? "my" : "other"}`}
                    >
                        <div className="message-bubble">{m.text}</div>
                    </div>
                ))}
                <div ref={messagesEndRef} />
            </div>

            <div className="chat-input">
                <input
                    value={text}
                    onChange={e => setText(e.target.value)}
                    placeholder="Введите сообщение..."
                    disabled={!connected}
                />
                <button onClick={sendMessage} disabled={!connected}>
                    {connected ? "Отправить" : "Подключение..."}
                </button>
            </div>
        </div>
    );
}

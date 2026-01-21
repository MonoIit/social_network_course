import { useEffect, useRef, useState } from "react";
import * as chatService from "../services/chatService";
import * as chatApi from "../api/chatApi";
import * as profileApi from "../api/profileApi";
import { v4 as uuidv4 } from "uuid";

const DEFAULT_AVATAR = "/default-avatar.png";

export function useChatViewModel(chatId) {
    const myUserId = Number(localStorage.getItem("user-id"));

    const [messages, setMessages] = useState([]);
    const [text, setText] = useState("");
    const [connected, setConnected] = useState(false);
    const [loading, setLoading] = useState(true);

    // кеш аватаров: { userId: avatarUrl }
    const [avatars, setAvatars] = useState({});

    const stompRef = useRef(null);

    /**
     * Загружает и кеширует аватар пользователя
     */
    const resolveAvatar = async (userId) => {
        if (avatars[userId]) {
            return avatars[userId];
        }

        try {
            const photo = await profileApi.getProfilePhoto(userId);
            const avatar = photo ?? DEFAULT_AVATAR;

            setAvatars(prev => ({
                ...prev,
                [userId]: avatar
            }));

            return avatar;
        } catch {
            return DEFAULT_AVATAR;
        }
    };

    useEffect(() => {
        let mounted = true;

        async function loadHistory() {
            try {
                const history = await chatApi.getChatMessages(chatId);
                if (!mounted) return;

                const enriched = await Promise.all(
                    history.map(async m => ({
                        ...m,
                        avatar: await resolveAvatar(m.senderId)
                    }))
                );

                enriched.sort(
                    (a, b) => new Date(a.sentAt) - new Date(b.sentAt)
                );

                setMessages(enriched);
            } catch (err) {
                console.error("Ошибка загрузки истории чата", err);
            } finally {
                setLoading(false);
            }
        }

        loadHistory();

        const client = chatService.createChatClient({
            chatId,
            onMessage: async (msg) => {
                // добавляем аватар отправителя
                msg.avatar = await resolveAvatar(msg.senderId);

                setMessages(prev => {
                    // если сервер прислал подтверждение нашего локального сообщения
                    if (
                        prev.some(
                            m =>
                                m.id === msg.id ||
                                (m.tempId &&
                                    m.text === msg.text &&
                                    m.senderId === msg.senderId)
                        )
                    ) {
                        return prev.map(m => {
                            if (
                                m.tempId &&
                                m.text === msg.text &&
                                m.senderId === msg.senderId
                            ) {
                                return msg;
                            }
                            return m;
                        });
                    }

                    return [...prev, msg].sort(
                        (a, b) => new Date(a.sentAt) - new Date(b.sentAt)
                    );
                });
            },
            onConnect: () => setConnected(true)
        });

        client.connect();
        stompRef.current = client;

        return () => {
            mounted = false;
            client.disconnect();
        };
    }, [chatId]);

    const sendMessage = () => {
        if (!connected || !text.trim()) return;

        const tempId = uuidv4();

        const newMsg = {
            tempId, // только локально
            chatId: Number(chatId),
            senderId: myUserId,
            text,
            sentAt: new Date().toISOString(),
            avatar: avatars[myUserId] ?? DEFAULT_AVATAR
        };

        // показываем локально сразу
        setMessages(prev => [...prev, newMsg]);

        // отправляем на сервер
        stompRef.current.sendMessage({
            destination: `/app/chats/${chatId}/sendMessage`,
            body: {
                chatId: newMsg.chatId,
                senderId: newMsg.senderId,
                text: newMsg.text,
                sentAt: newMsg.sentAt
            }
        });

        setText("");
    };

    return {
        messages,   // каждое сообщение содержит avatar
        text,
        setText,
        connected,
        sendMessage,
        loading,
        myUserId
    };
}

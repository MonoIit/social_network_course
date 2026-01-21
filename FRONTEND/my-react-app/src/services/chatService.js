import Stomp from "stompjs";

export function createChatClient({ chatId, onMessage, onConnect }) {
    const socket = new WebSocket("ws://localhost:8084/ws");
    console.log("ws://localhost:8084/ws");
    const stomp = Stomp.over(socket);
    stomp.debug = null;

    const connect = () => {
        stomp.connect({}, () => {
            onConnect?.();
            stomp.subscribe(`/topic/chats/${chatId}`, msg => {
                const newMsg = JSON.parse(msg.body);
                onMessage?.(newMsg);
            });
        });
    };

    const disconnect = () => {
        if (stomp.connected) {
            stomp.disconnect(() => console.log("Chat disconnected"));
        }
    };

    const sendMessage = ({ destination, body }) => {
        if (stomp.connected) {
            stomp.send(destination, {}, JSON.stringify(body));
        }
    };

    return { connect, disconnect, sendMessage };
}

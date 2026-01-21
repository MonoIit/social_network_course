import { useNavigate } from "react-router-dom";

export default function Sidebar() {
    const navigate = useNavigate();

    return (
        <aside className="sidebar">
            <button className="btn gray" onClick={() => navigate("/feed")}>
                Новости
            </button>
            <button className="btn gray" onClick={() => navigate("/message")}>
                Сообщения
            </button>
            <button className="btn gray" onClick={() => navigate("/friends")}>
                Друзья
            </button>
        </aside>
    );
}

import { useNavigate } from "react-router-dom";

export default function Header() {
    const navigate = useNavigate();
    const userId = localStorage.getItem("user-id");

    return (
        <header className="topbar">
            <button className="btn" onClick={() => navigate("/feed/create")}>
                Создать пост
            </button>
            <div className="right">
                {userId ? (
                    <button
                        className="btn"
                        onClick={() => navigate(`/profile/${userId}`)}
                    >
                        Профиль
                    </button>
                ) : (
                    <button
                        className="btn"
                        onClick={() => navigate("/login")}
                    >
                        Войти
                    </button>
                )}
            </div>
        </header>
    );
}

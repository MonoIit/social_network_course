import { useNavigate } from "react-router-dom";
import { useAuthViewModel } from "../viewmodels/useAuthViewModel";

export default function LoginPage() {
    const {
        login,
        password,
        error,
        setLogin,
        setPassword,
        submit
    } = useAuthViewModel();

    const navigate = useNavigate();

    return (
        <div className="auth-page">
            <form className="auth-form" onSubmit={e => { e.preventDefault(); submit(); }}>
                <h2>Вход</h2>

                <label>Логин</label>
                <input
                    type="text"
                    value={login}
                    onChange={e => setLogin(e.target.value)}
                    className="input"
                    placeholder="Введите логин"
                />

                <label>Пароль</label>
                <input
                    type="password"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    className="input"
                    placeholder="Введите пароль"
                />

                {error && <div className="error-msg">{error}</div>}

                <div className="auth-buttons">
                    <button className="btn-align" type="submit">Войти</button>
                    <button
                        type="button"
                        className="btn-align secondary"
                        onClick={() => navigate("/register")}
                    >
                        Регистрация
                    </button>
                </div>
            </form>
        </div>
    );
}


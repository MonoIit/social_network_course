import { useRegisterViewModel } from "../viewmodels/useRegisterViewModel";

export default function RegisterPage() {
    const vm = useRegisterViewModel();

    return (
        <div className="auth-page">
            <form className="auth-form" onSubmit={e => { e.preventDefault(); vm.submit(); }}>
                <h2>Регистрация</h2>

                <label>Логин</label>
                <input
                    type="text"
                    value={vm.login}
                    onChange={e => vm.setLogin(e.target.value)}
                    className="input"
                    placeholder="Введите логин"
                />

                <label>Пароль</label>
                <input
                    type="password"
                    value={vm.password}
                    onChange={e => vm.setPassword(e.target.value)}
                    className="input"
                    placeholder="Введите пароль"
                />

                <label>Email</label>
                <input
                    type="email"
                    value={vm.email}
                    onChange={e => vm.setEmail(e.target.value)}
                    className="input"
                    placeholder="Введите email"
                />

                {vm.error && <div className="error-msg">{vm.error}</div>}

                <div className="auth-buttons">
                    <button className="btn-align" type="submit">Зарегистрироваться</button>
                    <button
                        type="button"
                        className="btn-align secondary"
                        onClick={vm.goToLogin}
                    >
                        Войти
                    </button>
                </div>
            </form>
        </div>
    );
}


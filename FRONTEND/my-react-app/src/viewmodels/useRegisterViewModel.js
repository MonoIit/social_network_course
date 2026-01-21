import { useState } from "react";
import { useNavigate } from "react-router-dom";
import * as authApi from "../api/authApi";

export function useRegisterViewModel() {
    const [login, setLogin] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const [error, setError] = useState("");

    const navigate = useNavigate();

    const submit = async () => {
        setError("");

        try {
            await authApi.register({ login, password, email });
            navigate("/login");
        } catch (e) {
            switch (e.message) {
                case "USER_EXISTS":
                    setError("Пользователь с такими данными уже существует");
                    break;
                case "INVALID_DATA":
                    setError("Неверный формат данных");
                    break;
                default:
                    setError("Ошибка соединения с сервером");
            }
        }
    };

    return {
        login,
        password,
        email,
        error,
        setLogin,
        setPassword,
        setEmail,
        submit,
        goToLogin: () => navigate("/login")
    };
}

import { useState } from "react";
import { useNavigate } from "react-router-dom";
import * as authApi from "../api/authApi";

export function useAuthViewModel() {
    const [login, setLogin] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const navigate = useNavigate();

    const submit = async () => {
        setError("");
        console.log("hello");

        try {
            const data = await authApi.login(login, password);

            const token = data["auth-token"];
            localStorage.setItem("auth-token", token);

            const user = await authApi.validateToken(token);
            localStorage.setItem("user-id", user.userId);

            navigate("/feed");
        } catch (e) {
            if (e.message === "INVALID_CREDENTIALS") {
                setError("Неверный логин или пароль");
            } else {
                console.log(e);
                setError("Ошибка соединения с сервером");
            }
        }
    };

    return {
        login,
        password,
        error,
        setLogin,
        setPassword,
        submit
    };
}

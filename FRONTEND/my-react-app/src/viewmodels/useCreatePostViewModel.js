import { useState } from "react";
import { useNavigate } from "react-router-dom";
import * as postsApi from "../api/postsApi";

export function useCreatePostViewModel() {
    const [text, setText] = useState("");
    const [photo, setPhoto] = useState(null);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();
    const token = localStorage.getItem("auth-token");
    const userId = localStorage.getItem("user-id");

    const submit = async () => {
        setError("");

        if (!text.trim() && !photo) {
            setError("Введите текст или добавьте фото");
            return;
        }

        try {
            setLoading(true);
            await postsApi.createPost({ userId, text, photo, token });
            setText("");
            setPhoto(null);
            navigate("/feed");
        } catch (err) {
            console.error(err);
            setError(err.message || "Ошибка создания поста");
        } finally {
            setLoading(false);
        }
    };

    return {
        text,
        setText,
        photo,
        setPhoto,
        error,
        loading,
        submit
    };
}

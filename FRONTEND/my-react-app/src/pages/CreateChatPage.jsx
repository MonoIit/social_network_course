import { useState } from "react";
import { useNavigate } from "react-router-dom";
import * as messengerApi from "../api/messengerApi";

export default function CreateChatPage() {
  const [name, setName] = useState("");
  const [photo, setPhoto] = useState(null);
  const navigate = useNavigate();

  async function createChat() {
    await messengerApi.createGroupChat(name, photo);
    navigate("/messages");
  }

  return (
    <div className="create-chat-page">
      <h2>Создание чата</h2>

      <input
        placeholder="Название чата"
        value={name}
        onChange={e => setName(e.target.value)}
      />

      <input
        type="file"
        accept="image/*"
        onChange={e => setPhoto(e.target.files[0])}
      />

      <div className="actions">
        <button onClick={createChat}>Создать</button>
        <button onClick={() => navigate("/message")}>Отмена</button>
      </div>
    </div>
  );
}

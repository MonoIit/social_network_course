import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Header from "./components/Header";
import Sidebar from "./components/Sidebar";
import PrivateRoute from "./components/PrivateRoute";

import LoginPage from "./pages/LoginPage";
import FriendsPage from "./pages/FriendsPage";
import RegisterPage from "./pages/RegisterPage";
import FeedPage from "./pages/FeedPage";
import ProfilePage from "./pages/ProfilePage";
import CreatePostPage from "./pages/CreatePostPage";
import ChatPage from "./pages/ChatPage";
import MessagePage from "./pages/MessagePage";
import CreateChatPage from "./pages/CreateChatPage";

import "./index.css"
import GroupChatPage from "./pages/GroupChatPage";

export default function App() {
  return (
    <Router>
      <Header />
      <div className="app-layout">
        <Sidebar />

        <main className="app-content">
          <Routes>
            {/* публичные */}
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />

            {/* защищённые */}
            <Route
              path="/feed"
              element={
                <PrivateRoute>
                  <FeedPage />
                </PrivateRoute>
              }
            />

            <Route
              path="/friends"
              element={
                <PrivateRoute>
                  <FriendsPage />
                </PrivateRoute>
              }
            />

            <Route
              path="/profile/:id"
              element={
                <PrivateRoute>
                  <ProfilePage />
                </PrivateRoute>
              }
            />

            <Route
              path="/feed/create"
              element={
                <PrivateRoute>
                  <CreatePostPage />
                </PrivateRoute>
              }
            />

            <Route
              path="/message"
              element={
              <PrivateRoute>
                <MessagePage />
                </PrivateRoute>
              }
            />

            <Route
              path="/message/create"
              element={
              <PrivateRoute>
                <CreateChatPage />
                </PrivateRoute>
              }
            />

            <Route
              path="/chats/:chatId"
              element={
                <PrivateRoute>
                  <GroupChatPage />
                </PrivateRoute>
              }
            />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

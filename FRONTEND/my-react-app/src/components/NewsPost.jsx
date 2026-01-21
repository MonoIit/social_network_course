import { Link } from "react-router-dom";

export default function NewsPost({ post }) {
  return (
    <div className="post-card">
      <div className="post-header">
        <Link to={`/profile/${post.userId}`} className="post-user">
          {post.userName}
        </Link>
      </div>

      {post.photoUrl && (
        <img
          src={post.photoUrl}
          alt="post"
          className="post-photo"
        />
      )}

      <div className="post-text">{post.text}</div>
    </div>
  );
}


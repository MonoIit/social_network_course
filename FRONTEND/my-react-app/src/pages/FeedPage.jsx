import { useFeedViewModel } from "../viewmodels/useFeedViewModel";
import NewsPost from "../components/NewsPost";

export default function FeedPage() {
  const { posts, error, loading } = useFeedViewModel();

  return (
    <div className="feed-page">
      <h2 className="page-title">Новости</h2>

      {error && <div className="error-msg">{error}</div>}
      {loading && !error && <div className="loading">Загрузка...</div>}

      <div className="feed-list">
        {posts.map(post => (
          <NewsPost key={post.id} post={post} />
        ))}
      </div>
    </div>
  );
}


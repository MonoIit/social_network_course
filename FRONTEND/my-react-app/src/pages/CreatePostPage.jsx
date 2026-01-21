import { useCreatePostViewModel } from "../viewmodels/useCreatePostViewModel";

export default function CreatePostPage() {
  const vm = useCreatePostViewModel();

  return (
    <form
      className="create-post-form"
      onSubmit={e => {
        e.preventDefault();
        vm.submit();
      }}
    >
      <h2 className="page-title">Создать пост</h2>

      <textarea
        className="input textarea"
        placeholder="Что у вас нового?"
        value={vm.text}
        onChange={e => vm.setText(e.target.value)}
      />

      {vm.photo && (
        <div className="image-preview">
          <img src={URL.createObjectURL(vm.photo)} alt="preview" />
        </div>
      )}

      <div className="form-actions">
        <label className="btn gray">
          Добавить фото
          <input
            type="file"
            accept="image/*"
            hidden
            onChange={e => vm.setPhoto(e.target.files[0])}
          />
        </label>

        <button className="btn" disabled={vm.loading}>
          {vm.loading ? "Публикация..." : "Опубликовать"}
        </button>
      </div>

      {vm.error && <div className="error-msg">{vm.error}</div>}
    </form>
  );
}


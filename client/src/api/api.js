import axios from "axios";

axios.defaults.withCredentials = true;

export const instance = axios.create({
  baseURL: 'http://52.78.145.37:8080', //기본 URL
  timeout: 5000,
});

export const getAllPosts = () => {
  return instance.get(`/post/all`);
};

export const getAlltypePosts = (type_name) => {
  return instance.get(`/post/type/${type_name}`);
};

export const getPost = (postId) => {
  return instance.get(`/post/${postId}`);
};
export const getAuthPosts = () => {
  return instance.get(`/post/type/auth`);
}

export const getEnvPosts = () => {
  return instance.get(`/post/type/env`);
}

export const deletePost = (userId, postId) => {
  return instance.delete(`/post/${userId}/${postId}`);
};

export const getUser = (userId) => {
  return instance.get(`/user/${userId}`);
};

export const postVote = (postId) => {
  return instance.post(`/vote/${postId}`);
}

export const getVote = (postId, voteId) => {
  return instance.get(`/vote/${postId}/${voteId}`);
};

export const patchVote = (postId, userId, voteId, data) => {
  return instance.patch(`/vote/${postId}/${userId}/${voteId}`, data);
}

export const getComment = (postId) => {
  return instance.get(`/comment/${postId}`);
};

export const postComment = (postId, userId, commentText) => {
  return instance.post(`/comment/${postId}/${userId}`, { body: commentText });
};


export const postPosts = (type, title, body, open, img) => {
  const formData = new FormData();

  const jsonData = {
    type: type,
    title: title,
    body: body,
    open: open,
  };

  formData.append(
    "json",
    new Blob([JSON.stringify(jsonData)], { type: "application/json" })
  );
  formData.append("image", img);

  return instance.post(`/post/10`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
};

export const postSignUp = (
  username,
  userId,
  password,
  password_question,
  password_answer
) => {
  const formData = new FormData();

  formData.append("image", null);

  const jsonData = {
    userUseId: userId,
    userName: username,
    password: password,
    passwordQuestion: password_question,
    passwordAnswer: password_answer,
  };

  formData.append(
    "json",
    new Blob([JSON.stringify(jsonData)], { type: "application/json" })
  );

  return instance.post("/user", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
};



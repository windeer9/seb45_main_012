import axios from "axios";

axios.defaults.withCredentials = true;

export const instance = axios.create({
  baseURL: 'http://ec2-52-78-145-37.ap-northeast-2.compute.amazonaws.com:8080',
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
export const deletePost = (userId, postId) => {
  return instance.delete(`/post/${userId}/${postId}`);
};

export const getAuthPosts = () => {
  return instance.get(`/post/type/auth`);
}

export const getEnvPosts = () => {
  return instance.get(`/post/type/env`);
}

export const getUser = (userId) => {
  return instance.get(`/user/${userId}`);
};

export const deleteUser = (userId) => {
  return instance.delete(`/user/${userId}`);
}

export const postVote = (postId) => {
  return instance.post(`/vote/${postId}`);
}

export const getVote = (postId, voteId) => {
  return instance.get(`/vote/${postId}/${voteId}`);
};

export const patchVote = (postId, userId, voteId) => {
  return instance.patch(`/vote/${postId}/${userId}/${voteId}`);
}

export const getComment = (postId) => {
  return instance.get(`/comment/${postId}`);
};

export const postComment = (postId, userId, commentText) => {
  return instance.post(`/comment/${postId}/${userId}`, { body: commentText });
};


export const postPosts = (type, title, body, open, img, userId) => {
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

  return instance.post(`/post/${userId}`, formData, {
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

export const getCustomerPost = (userId) => {
  return instance.get(`/post/customer/${userId}`);
}

export const patchPost = (userId, postId, title, body, img) => {
  const formData = new FormData();

  const jsonData = {
    title: title,
    body: body,
  };

  formData.append(
    "json",
    new Blob([JSON.stringify(jsonData)], { type: "application/json" })
  );

  formData.append("image", img);

  return instance.patch(`/post/${userId}/${postId}`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
}


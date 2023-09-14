import axios from 'axios';

const instance = axios.create({
  baseURL: 'http://52.78.145.37:8080', //기본 URL
  timeout: 5000,
});

export const getPosts = (page) => {
  return instance.get(`/post/free?page=${page}`);
};

export const postSignUp = (username, userId, password, password_question, password_answer) => {
  const formData = new FormData();

  formData.append('image', null);

  const jsonData = {
    userUseId: userId,
    userName: username,
    password: password,
    passwordQuestion: password_question,
    passwordAnswer: password_answer
  };

  formData.append('json', new Blob([JSON.stringify(jsonData)], { type: 'application/json' }));

  return instance.post('/user', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}






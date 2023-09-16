import axios from 'axios';
import Cookie from 'js-cookie';
import jwtDecode from 'jwt-decode';

axios.defaults.withCredentials = true;

const Instance = axios.create({
  baseURL: 'http://52.78.145.37:8080',
});

export const refresh = async (config) => {
  const refreshToken = Cookie.get('refreshToken');
  const accessToken = localStorage.getItem('accessToken');
  const decodedToken = jwtDecode(accessToken);
  const expiresAt = decodedToken.exp;
  const currentTime = Math.floor(Date.now() / 1000);

  if (expiresAt < currentTime) {
    try {
      const res = await axios.post("http://52.78.145.37:8080/refresh", null, {
        headers: { 
          Refresh: refreshToken
        }
      });
      const auth = res.headers['authorization'];
      const newAccessToken = auth.substring(6);
      localStorage.setItem('accessToken', newAccessToken);
      config.headers['authorization'] = `Bearer${newAccessToken}`;
    }
    catch (err) {
      console.error('토큰 재발급 실패', err);
    }
    return config;
  }
}
Instance.interceptors.request.use(refresh)

export default Instance;
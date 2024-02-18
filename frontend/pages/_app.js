import '@/styles/globals.css'
import axios from 'axios';

export default function App({ Component, pageProps }) {
  axios.defaults.baseURL = "http://localhost:8080";
  return <Component {...pageProps} />
}

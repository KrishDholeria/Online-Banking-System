import Navbar from '@/components/navbar/navbar';
import '@/styles/globals.css'
import axios from 'axios';
import { useEffect, useState } from 'react';
import { Toaster } from '@/components/ui/sonner';

export default function App({ Component, pageProps }) {
  axios.defaults.baseURL = "http://localhost:8080";
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  useEffect(() => {
    const token = localStorage.getItem('customer-token');
    if (token) {
      setIsLoggedIn(true);
    }
  }
    , [isLoggedIn])
  return (
    <div>
      <Navbar login={isLoggedIn} />
      <Component {...pageProps} />
      <Toaster />
    </div>
  )
}

import Head from 'next/head';
import { useState } from 'react';
import axios from 'axios';
import { useRouter } from 'next/router'; // Import the useRouter hook

export default function AdminLogin() {
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const router = useRouter(); // Initialize the useRouter hook

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post('/admin/login', {
        userName,
        password,
      });

      const { data } = response;

      // Store the token in localStorage
      localStorage.setItem('token', data.token);
      localStorage.setItem('username', userName);

      console.log(data.token);

      // Redirect to the admin home page upon successful login
      router.push('/admin/home');
    } catch (error) {
      setErrorMessage('Invalid username or password.');
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 flex justify-center items-center">
      <Head>
        <title>Admin Login</title>
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <main className="bg-white p-8 rounded-lg shadow-md max-w-md w-full">
        <h1 className="text-3xl mb-6 text-center text-gray-900">Admin Login</h1>

        <form className="space-y-4" onSubmit={handleLogin}>
          <div>
            <label htmlFor="userName" className="block text-sm font-medium text-gray-900">Username:</label>
            <input 
              type="text" 
              id="userName" 
              name="userName" 
              value={userName}
              onChange={(e) => setUserName(e.target.value)}
              minLength={8}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          <div>
            <label htmlFor="password" className="block text-sm font-medium text-gray-900">Password:</label>
            <input 
              type="password" 
              id="password" 
              name="password" 
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          {errorMessage && <p className="text-red-500">{errorMessage}</p>}

          <button type="submit" className="w-full bg-black text-white p-2 rounded-md hover:bg-gray-800 transition duration-300">Login</button>
        </form>
      </main>
    </div>
  )
}

import Head from 'next/head';
import { useState } from 'react';
import axios from 'axios';

export default function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post('/manager/login', {
        username,
        password,
      });

      const { data } = response;

      // Store the token in localStorage
      localStorage.setItem('token', data.token);

      // Optionally, redirect to another page upon successful login
      // router.push('/dashboard');
    } catch (error) {
      setErrorMessage('Invalid username or password.');
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 flex justify-center items-center">
      <Head>
        <title>Manager Login</title>
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <main className="bg-white p-8 rounded-lg shadow-md max-w-md w-full">
        <h1 className="text-3xl mb-6 text-center text-gray-900">Manager Login</h1>

        <form className="space-y-4" onSubmit={handleLogin}>
          <div>
            <label htmlFor="username" className="block text-sm font-medium text-gray-900">Username:</label>
            <input 
              type="text" 
              id="username" 
              name="username" 
              value={username}
              onChange={(e) => setUsername(e.target.value)}
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

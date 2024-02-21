import Head from 'next/head';
import { useRouter } from 'next/router';
import { useState, useEffect } from 'react';
import { FaArrowLeft } from 'react-icons/fa';
import axios from 'axios';

export default function ChangePassword() {
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmNewPassword, setConfirmNewPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [isLoggedIn, setIsLoggedIn] = useState(false); // Add state for login status
  const router = useRouter();

  useEffect(() => {
    // Check if the user is logged in by verifying the presence of a token in localStorage
    const token = localStorage.getItem('token');
    if (token) {
      setIsLoggedIn(true);
    } else {
      // Redirect to the login page if the user is not logged in
      router.push('/admin');
    }
  }, []);

  const handleChangePassword = async (e) => {
    e.preventDefault();
    if (newPassword !== confirmNewPassword) {
      setErrorMessage('New passwords do not match.');
      return;
    }
    try {
      const username = localStorage.getItem('username');
      // Make API request to change the password
      const response = await axios.post('/admin/change-password', {
        username: username,
        oldPassword: oldPassword,
        newPassword: newPassword
      });
      console.log('Password changed successfully:', response.data);
      setErrorMessage('');
      // Redirect to admin home page after successful password change
      router.push('/admin/home');
    } catch (error) {
      console.error('Error changing password:', error);
      setErrorMessage('Failed to change password. Please try again.');
    }
  };

  const handleGoBack = () => {
    router.back(); // Navigate back to the previous page
  };

  return (
    <div className="min-h-screen bg-gray-900 flex flex-col justify-center items-center">
      <Head>
        <title>Change Password</title>
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <main className="bg-white p-8 rounded-lg shadow-md max-w-md w-full flex flex-col justify-center items-center">
        <div className="flex justify-start mb-4 w-full">
          <button onClick={handleGoBack} className="flex items-center text-gray-900">
            <FaArrowLeft size={20} className="mr-2" />
            Back
          </button>
        </div>

        <h1 className="text-3xl mb-6 text-center text-gray-900">Change Password</h1>

        <form className="space-y-4" onSubmit={handleChangePassword}>
          <div>
            <label htmlFor="oldPassword" className="block text-sm font-medium text-gray-900">Old Password:</label>
            <input 
              type="password" 
              id="oldPassword" 
              value={oldPassword}
              onChange={(e) => setOldPassword(e.target.value)}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          <div>
            <label htmlFor="newPassword" className="block text-sm font-medium text-gray-900">New Password:</label>
            <input 
              type="password" 
              id="newPassword" 
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          <div>
            <label htmlFor="confirmNewPassword" className="block text-sm font-medium text-gray-900">Confirm New Password:</label>
            <input 
              type="password" 
              id="confirmNewPassword" 
              value={confirmNewPassword}
              onChange={(e) => setConfirmNewPassword(e.target.value)}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          <button type="submit" className="w-full bg-black text-white p-2 rounded-md hover:bg-gray-800 transition duration-300">Change Password</button>
          {errorMessage && <p className="text-red-500">{errorMessage}</p>}
        </form>
      </main>
    </div>
  );
}

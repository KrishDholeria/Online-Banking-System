import Head from 'next/head';
import Link from 'next/link';
import { useRouter } from 'next/router';
import { useState, useEffect } from 'react';
import { FaSignOutAlt, FaUserPlus, FaUserEdit, FaUserCog } from 'react-icons/fa';

export default function ManagerHome() {

  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState('');
  const router = useRouter();

  const handleLogout = () => {
    // Perform logout logic here (e.g., clear localStorage, reset state, etc.)
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    setIsLoggedIn(false);
    // Redirect to the login page after logout
    router.push('/manager');
  };

  useEffect(() => {
    // Check if the user is logged in by verifying the presence of a token in localStorage
    const token = localStorage.getItem('token');
    if (token) {
      // You can also validate the token with your backend to ensure it's still valid
      // For simplicity, we're assuming the presence of a token means the user is logged in
      setIsLoggedIn(true);
      setUsername(localStorage.getItem('username')); // Get username from localStorage
    } else {
      // Redirect to the login page if the user is not logged in
      router.push('/manager');
    }
  }, []);

  return (
    <div className="min-h-screen bg-gray-900 flex flex-col justify-center items-center">

      <header className="bg-gray-800 text-white w-full fixed top-0">
        <div className="flex justify-end items-center p-4">
          {isLoggedIn && (
            <>
              <button onClick={handleLogout} className="mr-4 hover:underline flex items-center">
                Logout
                <FaSignOutAlt size={18} className="ml-1" />
              </button>
            </>
          )}
        </div>
      </header>
      
      <Head>
        <title>Manager Home</title>
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <main className="bg-white p-8 rounded-lg shadow-md max-w-md w-full flex flex-col justify-center items-center">
        <h1 className="text-3xl mb-6 text-center text-gray-900">Welcome, {username}</h1> {/* Display username here */}

        <div className="grid grid-cols-1 gap-4">
          <div className="flex flex-col items-center"> {/* Wrap buttons in a div with flex direction column and center alignment */}
            <Link href="/manager/add-customer"> 
              <button className="p-4 bg-gray-800 text-white rounded-md text-center hover:bg-gray-700 transition duration-300 flex items-center">
                <FaUserPlus size={18} className="mr-2" />
                Add Customer
              </button>
            </Link>

            <Link href="/manager/update-profile">
              <button className="p-4 bg-gray-800 text-white rounded-md text-center hover:bg-gray-700 transition duration-300 mt-4 flex items-center"> {/* Add margin top */}
                <FaUserEdit size={18} className="mr-2" />
                Update Profile
              </button>
            </Link>

            <Link href="/manager/view-customer-details">
              <button className="p-4 bg-gray-800 text-white rounded-md text-center hover:bg-gray-700 transition duration-300 mt-4 flex items-center"> {/* Add margin top */}
                <FaUserCog size={18} className="mr-2" />
                View Customer Details
              </button>
            </Link>

            <Link href="/manager/change-password">
              <button className="p-4 bg-gray-800 text-white rounded-md text-center hover:bg-gray-700 transition duration-300 mt-4 flex items-center">
                <FaUserCog size={18} className="mr-2" />
                Change Password
              </button>
            </Link>
          </div>
        </div>
      </main>
    </div>
  )
}

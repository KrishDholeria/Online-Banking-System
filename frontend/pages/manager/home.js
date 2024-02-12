import Head from 'next/head';
import Link from 'next/link';
import { useRouter } from 'next/router';
import { useState, useEffect } from 'react';

export default function ManagerHome() {

  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const router = useRouter();


  useEffect(() => {
    // Check if the user is logged in by verifying the presence of a token in localStorage
    const token = localStorage.getItem('token');
    if (token) {
      // You can also validate the token with your backend to ensure it's still valid
      // For simplicity, we're assuming the presence of a token means the user is logged in
      setIsLoggedIn(true);
    } else {
      // Redirect to the login page if the user is not logged in
      router.push('/index.js');
    }
  }, []);

  return (
    <div className="min-h-screen bg-gray-900 flex flex-col justify-center items-center">
      
      <Head>
        <title>Manager Home</title>
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <main className="bg-white p-8 rounded-lg shadow-md max-w-md w-full">
        <h1 className="text-3xl mb-6 text-center text-gray-900">Manager Home</h1>

        <div className="grid grid-cols-1 gap-4">
          <div className="flex justify-between">
            <Link href="/manager/add-customer"> {/* Update the href to point to the Add Customer form route */}
              <button className="p-4 bg-gray-800 text-white rounded-md text-center hover:bg-gray-700 transition duration-300">
                Add Customer
              </button>
            </Link>

            <Link href="/manager/update-profile">
              <button className="p-4 bg-gray-800 text-white rounded-md text-center hover:bg-gray-700 transition duration-300 ml-4">
                Update Profile
              </button>
            </Link>

            <Link href="/manager/view-customer-details">
              <button className="p-4 bg-gray-800 text-white rounded-md text-center hover:bg-gray-700 transition duration-300 ml-4">
                View Customer Details
              </button>
            </Link>
          </div>
        </div>
      </main>
    </div>
  )
}

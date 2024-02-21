import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useRouter } from 'next/router';
import { FaSignOutAlt, FaArrowLeft } from 'react-icons/fa';

const AddManagerForm = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [lastName, setLastName] = useState('');
  const [firstName, setFirstName] = useState('');
  const [dob, setDob] = useState('');
  const [email, setEmail] = useState('');
  const [phoneNo, setPhoneNo] = useState('');
  const [address, setAddress] = useState('');
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const [branchId, setBranchId] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const router = useRouter();

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      setIsLoggedIn(true);
    } else {
      router.push('/manager');
    }
  }, []);

  const handleBack = () => {
    router.push('/admin/home');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('/add/manager', {
        userName,
        password,
        branch:{
            branchCode:branchId
        },
        person: {
          lastName,
          firstName,
          dob,
          email,
          phoneNo,
          address,
        },
      });
      console.log(response.data);
      router.push('/admin/home');
    } catch (error) {
      console.error('Error adding manager:', error);
      setErrorMessage('Failed to add manager. Please try again.');
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 flex justify-center items-center">
      <div className="bg-white p-8 rounded-lg shadow-md max-w-md w-full">
        <div className="flex items-center justify-between mb-4">
          <button onClick={handleBack} className="mr-4 text-gray-700 hover:text-gray-900">
            <FaArrowLeft size={20} />
          </button>
          <h1 className="text-3xl text-gray-900">Add Manager</h1>
          <div></div>
        </div>

        <form className="space-y-4" onSubmit={handleSubmit}>
          <div>
            <label htmlFor="userName" className="block text-sm font-medium text-gray-900">Username:</label>
            <input 
              type="text" 
              id="userName" 
              value={userName}
              onChange={(e) => setUserName(e.target.value)}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          <div>
            <label htmlFor="password" className="block text-sm font-medium text-gray-900">Password:</label>
            <input 
              type="password" 
              id="password" 
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          <div>
            <label htmlFor="lastName" className="block text-sm font-medium text-gray-900">Last Name:</label>
            <input 
              type="text" 
              id="lastName" 
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          <div>
            <label htmlFor="firstName" className="block text-sm font-medium text-gray-900">First Name:</label>
            <input 
              type="text" 
              id="firstName" 
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          <div>
            <label htmlFor="dob" className="block text-sm font-medium text-gray-900">Date of Birth:</label>
            <input 
              type="date" 
              id="dob" 
              value={dob}
              onChange={(e) => setDob(e.target.value)}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-900">Email:</label>
            <input 
              type="email" 
              id="email" 
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          <div>
            <label htmlFor="phoneNo" className="block text-sm font-medium text-gray-900">Phone Number:</label>
            <input 
              type="tel" 
              id="phoneNo" 
              value={phoneNo}
              onChange={(e) => setPhoneNo(e.target.value)}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          <div>
            <label htmlFor="address" className="block text-sm font-medium text-gray-900">Address:</label>
            <textarea 
              id="address" 
              value={address}
              onChange={(e) => setAddress(e.target.value)}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm resize-none" 
              required 
            />
          </div>

          <div>
            <label htmlFor="branchId" className="block text-sm font-medium text-gray-900">Branch ID:</label>
            <input 
              type="text" 
              id="branchId" 
              value={branchId}
              onChange={(e) => setBranchId(e.target.value)}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          <button type="submit" className="w-full bg-black text-white p-2 rounded-md hover:bg-gray-800 transition duration-300">Add Manager</button>
          {errorMessage && <p className="text-red-500">{errorMessage}</p>}
        </form>
      </div>
    </div>
  )
}

export default AddManagerForm;

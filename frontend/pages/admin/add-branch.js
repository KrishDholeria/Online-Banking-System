import React from "react"
import { useState, useEffect } from 'react';
import axios from 'axios';
import { useRouter } from 'next/router';
import { FaArrowLeft } from 'react-icons/fa';

export default function AddBranchForm() {
  const router = useRouter();

  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [branchName, setBranchName] = useState('');
  const [address, setAddress] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    const token = localStorage.getItem('admin-token');
    if (token) {
      setIsLoggedIn(true);
    } else {
      router.push('/admin');
    }
  }, []);

  const handleBack = () => {
    // Redirect to the admin home page
    router.push('/admin/home');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('/add/branch', {
        branchName,
        address,
        phoneNumber
      });
      // Handle successful response
      console.log(response.data);
      // Redirect to the admin home page after adding the branch
      router.push('/admin/home');
    } catch (error) {
      // Handle error
      console.error('Error adding branch:', error);
      setErrorMessage('Failed to add branch. Please try again.');
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 flex justify-center items-center">
      <div className="bg-white p-8 rounded-lg shadow-md max-w-md w-full">
        <div className="flex items-center justify-between mb-4">
          <button onClick={handleBack} className="mr-4 text-gray-700 hover:text-gray-900">
            <FaArrowLeft size={20} />
          </button>
          <h1 className="text-3xl text-gray-900">Add Branch</h1>
          <div></div>
        </div>

        <form className="space-y-4" onSubmit={handleSubmit}>
          <div>
            <label htmlFor="branchName" className="block text-sm font-medium text-gray-900">Branch Name:</label>
            <input 
              type="text" 
              id="branchName" 
              value={branchName}
              onChange={(e) => setBranchName(e.target.value)}
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
            <label htmlFor="phoneNumber" className="block text-sm font-medium text-gray-900">Phone Number:</label>
            <input 
              type="tel" 
              id="phoneNumber" 
              value={phoneNumber}
              onChange={(e) => setPhoneNumber(e.target.value)}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required
              pattern="[0-9]{10}"  // Add pattern attribute to enforce 10 digits
              title="Phone number must be 10 digits"  // Add title attribute for validation message
            />
          </div>

          <button type="submit" className="w-full bg-black text-white p-2 rounded-md hover:bg-gray-800 transition duration-300">Add Branch</button>
          {errorMessage && <p className="text-red-500">{errorMessage}</p>}
        </form>
      </div>
    </div>
  );
}

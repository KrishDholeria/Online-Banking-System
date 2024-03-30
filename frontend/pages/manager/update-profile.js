import Head from 'next/head';
import Link from 'next/link';
import { useRouter } from 'next/router';
import { useState, useEffect } from 'react';
import axios from 'axios';
import { FaSignOutAlt, FaArrowLeft } from 'react-icons/fa';

export default function UpdateManagerDetailForm() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [person, setPerson] = useState({
    lastName: '',
    firstName: '',
    dob: '',
    email: '',
    phoneNo: '',
    address: ''
  });
  const [errorMessage, setErrorMessage] = useState('');
  const router = useRouter();

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      setIsLoggedIn(true);
      fetchManagerDetails();
    } else {
      router.push('/manager');
    }
  }, []);

  const fetchManagerDetails = async () => {
    try {
      const username = localStorage.getItem('username');
      const response = await axios.post(`/manager/details`,username);
      const managerDetails = response.data;
      setPerson({
        lastName: managerDetails.person.lastName,
        firstName: managerDetails.person.firstName,
        dob: managerDetails.person.dob,
        email: managerDetails.person.email,
        phoneNo: managerDetails.person.phoneNo,
        address: managerDetails.person.address
      });
    } catch (error) {
      console.error('Error fetching manager details:', error);
      setErrorMessage('Failed to fetch manager details. Please try again.');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const username = localStorage.getItem('username');
      await axios.put(`/manager/details?username=${username}`, person);
      // Optionally, you can handle success response here
    } catch (error) {
      console.error('Error updating manager details:', error);
      setErrorMessage('Failed to update manager details. Please try again.');
    }
  };

  const handleBackArrowClick = () => {
    router.push('/manager/home');
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setPerson({ ...person, [name]: value });
  };

  return (
    <div className="min-h-screen bg-gray-900 flex justify-center items-center">
      <div className="bg-white p-8 rounded-lg shadow-md max-w-md w-full">
        <div className="flex items-center mb-4">
          <FaArrowLeft className="text-gray-700 hover:text-gray-900 cursor-pointer" size={20} onClick={handleBackArrowClick} />
          <h1 className="text-3xl ml-2 text-center text-gray-900">Update Manager Details</h1>
        </div>

        <form className="space-y-4" onSubmit={handleSubmit}>
          <div>
            <label htmlFor="lastName" className="block text-sm font-medium text-gray-900">Last Name:</label>
            <input 
              type="text" 
              id="lastName" 
              name="lastName"
              value={person.lastName}
              onChange={handleInputChange}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          <div>
            <label htmlFor="firstName" className="block text-sm font-medium text-gray-900">First Name:</label>
            <input 
              type="text" 
              id="firstName" 
              name="firstName"
              value={person.firstName}
              onChange={handleInputChange}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          <div>
            <label htmlFor="dob" className="block text-sm font-medium text-gray-900">Date of Birth:</label>
            <input 
              type="date" 
              id="dob" 
              name="dob"
              value={person.dob}
              onChange={handleInputChange}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-900">Email:</label>
            <input 
              type="email" 
              id="email" 
              name="email"
              value={person.email}
              onChange={handleInputChange}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          <div>
            <label htmlFor="phoneNo" className="block text-sm font-medium text-gray-900">Phone Number:</label>
            <input 
              type="tel" 
              id="phoneNo" 
              name="phoneNo"
              value={person.phoneNo}
              onChange={handleInputChange}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          <div>
            <label htmlFor="address" className="block text-sm font-medium text-gray-900">Address:</label>
            <textarea 
              id="address" 
              name="address"
              value={person.address}
              onChange={handleInputChange}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm resize-none" 
              required 
            />
          </div>

          <button type="submit" className="w-full bg-black text-white p-2 rounded-md hover:bg-gray-800 transition duration-300">Update Details</button>
          {errorMessage && <p className="text-red-500">{errorMessage}</p>}
        </form>
      </div>
    </div>
  );
}

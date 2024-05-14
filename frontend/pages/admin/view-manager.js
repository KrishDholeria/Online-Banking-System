import React from "react"
import Head from 'next/head';
import { useRouter } from 'next/router';
import { useState, useEffect, useRef } from 'react';
import { FaArrowLeft, FaSearch, FaTrash } from 'react-icons/fa';
import axios from 'axios';

export default function ViewManagerDetails() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [managers, setManagers] = useState([]);
  const [searchInput, setSearchInput] = useState('');
  const router = useRouter();
  const searchInputRef = useRef(null);

  useEffect(() => {
    const token = localStorage.getItem('admin-token');
    if (token) {
      setIsLoggedIn(true);
      fetchManagers();
      searchInputRef.current.focus();
    } else {
      router.push('/admin');
    }
  }, []);

  const fetchManagers = async () => {
    try {
      const response = await axios.get('all/managers'); // Adjust the endpoint to fetch managers
      setManagers(response.data);
    } catch (error) {
      console.error('Error fetching managers:', error);
    }
  };

  const handleSearch = async () => {
    if (searchInput.trim() !== '') {
      try {
        const response = await axios.get(`manager/username?userName=${searchInput.trim()}`); 
        setManagers([response.data]);
      } catch (error) {
        console.error('Error fetching managers:', error);
      }
    } else {
      fetchManagers();
    }
  };

  const handleSearchInputChange = (e) => {
    setSearchInput(e.target.value);
  };

  const handleDeleteClick = async (username) => {
    try {
      console.log(username)
      const res = await axios.delete(`/managers/${username}`);
      console.log(res.data);
      setManagers(managers.filter(manager => manager.userName !== username));
      console.log('Manager deleted successfully');
    } catch (error) {
      console.error('Error deleting manager:', error);
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 flex flex-col">
      <header className="bg-gray-800 text-white w-full flex justify-between items-center px-4 py-2">
        <div>
          <FaArrowLeft className="cursor-pointer" size={24} onClick={() => router.push('/admin/home')} />
        </div>
        <div>
          <h1 className="text-xl font-bold">View Manager Details</h1>
        </div>
        <div className="flex items-center">
          <input
            type="text"
            placeholder="Enter Username"
            value={searchInput}
            onChange={handleSearchInputChange}
            className="p-2 mr-2 rounded-md border border-gray-400 focus:outline-none z-10 text-gray-900"
            readOnly={false}
            ref={searchInputRef}
            onClick={(e) => e.target.focus()}
          />
          <FaSearch size={20} className="cursor-pointer" onClick={handleSearch} />
        </div>
      </header>
      
      <Head>
        <title>View Manager Details</title>
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <main className="bg-white p-12 rounded-lg shadow-md w-full flex-grow flex flex-col justify-center items-center">
        <table className="w-full">
          <thead>
            <tr className="bg-gray-200">
              <th className="py-2 px-4">Manager Name</th>
              <th className="py-2 px-4">Username</th>
              <th className="py-2 px-4">Branch Code</th>
              <th className="py-2 px-4">Email</th>
              <th className="py-2 px-4">Phone</th>
              <th className="py-2 px-4">Address</th>
              <th className="py-2 px-4">Actions</th>
            </tr>
          </thead>
          <tbody>
            {managers.map((manager, index) => (
              <tr key={index} className="text-gray-800">
                <td className="py-2 px-4">{manager.person.firstName} {manager.person.lastName}</td>
                <td className="py-2 px-4">{manager.userName}</td>
                <td className="py-2 px-4">{manager.branch.branchCode}</td>
                <td className="py-2 px-4">{manager.person.email}</td>
                <td className="py-2 px-4">{manager.person.phoneNo}</td>
                <td className="py-2 px-4">{manager.person.address}</td>
                <td className="py-2 px-4">
                  <FaTrash
                    className="cursor-pointer"
                    size={20}
                    onClick={() => handleDeleteClick(manager.userName)}
                  />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </main>
    </div>
  );
}

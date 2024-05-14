import React from "react"
import Head from 'next/head';
import { useRouter } from 'next/router';
import { useState, useEffect, useRef } from 'react';
import { FaArrowLeft, FaSearch, FaEdit, FaSave } from 'react-icons/fa';
import axios from 'axios';
import Link from 'next/link';

export default function ViewBranchDetails() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [branches, setBranches] = useState([]);
  const [searchInput, setSearchInput] = useState('');
  const [editingBranch, setEditingBranch] = useState(null); // State to track the branch being edited
  const [updatedBranchName, setUpdatedBranchName] = useState(''); // State to store updated branch name
  const [updatedAddress, setUpdatedAddress] = useState(''); // State to store updated address
  const [updatedPhoneNumber, setUpdatedPhoneNumber] = useState(''); // State to store updated phone number
  const router = useRouter();
  const searchInputRef = useRef(null);

  useEffect(() => {
    const token = localStorage.getItem('admin-token');
    if (token) {
      setIsLoggedIn(true);
      fetchBranches();
      searchInputRef.current.focus();
    } else {
      router.push('/admin');
    }
  }, []);

  const fetchBranches = async () => {
    try {
      const response = await axios.get('/branches');
      setBranches(response.data);
    } catch (error) {
      console.error('Error fetching branches:', error);
    }
  };

  const handleSearch = async () => {
    if (searchInput.trim() !== '') {
      try {
        const response = await axios.get(`/branches/${searchInput.trim()}`);
        setBranches([response.data]);
      } catch (error) {
        console.error('Error fetching branches:', error);
      }
    } else {
      fetchBranches();
    }
  };

  const handleSearchInputChange = (e) => {
    setSearchInput(e.target.value);
  };

  const handleEditClick = (branch) => {
    setEditingBranch(branch); // Set the branch to be edited
    setUpdatedBranchName(branch.branchName); // Set initial values for updated branch details
    setUpdatedAddress(branch.address);
    setUpdatedPhoneNumber(branch.phoneNumber);
  };

  const handleSaveClick = async () => {
    try {
      const response = await axios.put(`/branches/${editingBranch.branchCode}`, { // Send a PUT request to update the branch
        branchName: updatedBranchName,
        address: updatedAddress,
        phoneNumber: updatedPhoneNumber
      });
      console.log('Branch updated successfully:', response.data);
      setEditingBranch(null); // Clear the editing state
      fetchBranches(); // Fetch branches again to update the UI
    } catch (error) {
      console.error('Error updating branch:', error);
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 flex flex-col">
      <header className="bg-gray-800 text-white w-full flex justify-between items-center px-4 py-2">
        <div>
          <FaArrowLeft className="cursor-pointer" size={24} onClick={() => router.push('/admin/home')} />
        </div>
        <div>
          <h1 className="text-xl font-bold">View Branch Details</h1>
        </div>
        <div className="flex items-center">
          <input
            type="text"
            placeholder="Enter Branch Code"
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
        <title>View Branch Details</title>
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <main className="bg-white p-12 rounded-lg shadow-md w-full flex-grow flex flex-col justify-center items-center">
        <table className="w-full">
          <thead>
            <tr className="bg-gray-200">
              <th className="py-2 px-4">Branch Name</th>
              <th className="py-2 px-4">Address</th>
              <th className="py-2 px-4">Phone Number</th>
              <th className="py-2 px-4">Branch Code</th>
              <th className="py-2 px-4">Actions</th>
            </tr>
          </thead>
          <tbody>
            {branches.map((branch, index) => (
              <tr key={index} className="text-gray-800">
                <td className="py-2 px-4">{editingBranch === branch ? <input type="text" value={updatedBranchName} onChange={(e) => setUpdatedBranchName(e.target.value)} /> : branch.branchName}</td>
                <td className="py-2 px-4">{editingBranch === branch ? <input type="text" value={updatedAddress} onChange={(e) => setUpdatedAddress(e.target.value)} /> : branch.address}</td>
                <td className="py-2 px-4">{editingBranch === branch ? <input type="text" value={updatedPhoneNumber} onChange={(e) => setUpdatedPhoneNumber(e.target.value)} /> : branch.phoneNumber}</td>
                <td className="py-2 px-4">{branch.branchCode}</td>
                <td className="py-2 px-4">
                  {editingBranch === branch ? (
                    <FaSave className="cursor-pointer" size={20} onClick={handleSaveClick} />
                  ) : (
                    <FaEdit className="cursor-pointer" size={20} onClick={() => handleEditClick(branch)} />
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </main>
    </div>
  );
}

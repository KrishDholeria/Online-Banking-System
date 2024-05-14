import React from "react"
import Head from 'next/head';
import { useRouter } from 'next/router';
import { useState, useEffect, useRef } from 'react'; // Import useRef hook
import { FaArrowLeft, FaSearch } from 'react-icons/fa'; // Import FaSearch icon
import axios from 'axios';

export default function ViewCustomerDetails() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [customers, setCustomers] = useState([]);
  const [searchInput, setSearchInput] = useState('');
  const router = useRouter();
  const searchInputRef = useRef(null); // Create a ref for the input field

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      setIsLoggedIn(true);
      fetchCustomers();
      // Set focus on the input field when the component mounts
      searchInputRef.current.focus();
    } else {
      router.push('/manager');
    }
  }, []);

  const fetchCustomers = async () => {
    try {
      const response = await axios.get('all/customers');
      setCustomers(response.data);
    } catch (error) {
      console.error('Error fetching customers:', error);
    }
  };

  const handleSearch = async () => {
    if (searchInput.trim() !== '') {
      try {
        const response = await axios.get(`customer?accountNo=${searchInput.trim()}`);
        setCustomers(response.data);
      } catch (error) {
        console.error('Error fetching customers:', error);
      }
    } else {
      fetchCustomers();
    }
  };

  const handleSearchInputChange = (e) => {
    setSearchInput(e.target.value);
  };
  

  return (
    <div className="min-h-screen bg-gray-900 flex flex-col">
      <header className="bg-gray-800 text-white w-full flex justify-between items-center px-4 py-2">
        <div>
          <FaArrowLeft className="cursor-pointer" size={24} onClick={() => router.push('/manager/home')} />
        </div>
        <div>
          <h1 className="text-xl font-bold">View Customer Details</h1>
        </div>
        <div className="flex items-center">
          <input
            type="text"
            placeholder="Enter Account Number"
            value={searchInput}
            onChange={handleSearchInputChange}
            className="p-2 mr-2 rounded-md border border-gray-400 focus:outline-none z-10 text-gray-900"
            readOnly={false} // Ensure the input field is not read-only
            ref={searchInputRef}
            onClick={(e) => e.target.focus()} // Attach the ref to the input field
          />
          <FaSearch size={20} className="cursor-pointer" onClick={handleSearch} />
        </div>
      </header>
      
      <Head>
        <title>View Customer Details</title>
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <main className="bg-white p-12 rounded-lg shadow-md w-full flex-grow flex flex-col justify-center items-center">
        <table className="w-full">
          <thead>
            <tr className="bg-gray-200">
              <th className="py-2 px-4">Customer Name</th>
              <th className="py-2 px-4">Date of Birth</th>
              <th className="py-2 px-4">Email</th>
              <th className="py-2 px-4">Phone</th>
              <th className="py-2 px-4">Address</th>
              <th className="py-2 px-4">PAN No</th>
              <th className="py-2 px-4">Account Balance</th>
              <th className="py-2 px-4">Customer ID</th>
              <th className="py-2 px-4">Branch ID</th>
            </tr>
          </thead>
          <tbody>
            {customers.map((customer, index) => (
              <tr key={index} className="text-gray-800">
                <td className="py-2 px-4">{customer.person.firstName} {customer.person.lastName}</td>
                <td className="py-2 px-4">{customer.person.dob}</td>
                <td className="py-2 px-4">{customer.person.email}</td>
                <td className="py-2 px-4">{customer.person.phoneNo}</td>
                <td className="py-2 px-4">{customer.person.address}</td>
                <td className="py-2 px-4">{customer.panNo}</td>
                <td className="py-2 px-4">{customer.account.accountBalance}</td>
                <td className="py-2 px-4">{customer.account.cutomerId}</td>
                <td className="py-2 px-4">{customer.account.branchId}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </main>
    </div>
  );
}

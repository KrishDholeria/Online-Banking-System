import Head from 'next/head';
import { useRouter } from 'next/router';
import { useState, useEffect, useRef } from 'react';
import { FaArrowLeft, FaSearch } from 'react-icons/fa';
import axios from 'axios';

export default function ViewBranchDetails() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [branches, setBranches] = useState([]);
  const [searchInput, setSearchInput] = useState('');
  const router = useRouter();
  const searchInputRef = useRef(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      setIsLoggedIn(true);
      fetchBranches();
      searchInputRef.current.focus();
    } else {
      router.push('/admin'); // Redirect to admin page if not logged in
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
            </tr>
          </thead>
          <tbody>
            {branches.map((branch, index) => (
              <tr key={index} className="text-gray-800">
                <td className="py-2 px-4">{branch.branchName}</td>
                <td className="py-2 px-4">{branch.address}</td>
                <td className="py-2 px-4">{branch.phoneNumber}</td>
                <td className="py-2 px-4">{branch.branchCode}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </main>
    </div>
  );
}

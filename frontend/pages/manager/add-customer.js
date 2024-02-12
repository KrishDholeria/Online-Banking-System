import { useState } from 'react';
import axios from 'axios';

export default function AddCustomerForm() {
  const [lastName, setLastName] = useState('');
  const [firstName, setFirstName] = useState('');
  const [dob, setDob] = useState('');
  const [email, setEmail] = useState('');
  const [phoneNo, setPhoneNo] = useState('');
  const [address, setAddress] = useState('');
  const [panNo, setPanNo] = useState('');
  const [accountBalance, setAccountBalance] = useState('');
  const [branchId, setBranchId] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('/add/customer', {
        person: {
          lastName,
        firstName,
        dob,
        email,
        phoneNo,
        address
        },
        panNo,
        account: {
        accountBalance,
        branchId
        }
      });
      // Handle successful response
      console.log(response.data);
    } catch (error) {
      // Handle error
      console.error('Error adding customer:', error);
      setErrorMessage('Failed to add customer. Please try again.');
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 flex justify-center items-center">
      <div className="bg-white p-8 rounded-lg shadow-md max-w-md w-full">
        <h1 className="text-3xl mb-6 text-center text-gray-900">Add Customer</h1>

        <form className="space-y-4" onSubmit={handleSubmit}>
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
            <label htmlFor="panNo" className="block text-sm font-medium text-gray-900">PAN No:</label>
            <input 
              type="text" 
              id="panNo" 
              value={panNo}
              onChange={(e) => setPanNo(e.target.value)}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
              required 
            />
          </div>

          <div>
            <label htmlFor="accountBalance" className="block text-sm font-medium text-gray-900">Account Balance:</label>
            <input 
              type="text" 
              id="accountBalance" 
              value={accountBalance}
              onChange={(e) => setAccountBalance(e.target.value)}
              className="mt-1 p-2 block w-full border border-gray-300 rounded-md shadow-sm" 
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

          <button type="submit" className="w-full bg-black text-white p-2 rounded-md hover:bg-gray-800 transition duration-300">Add Customer</button>
          {errorMessage && <p className="text-red-500">{errorMessage}</p>}
        </form>
      </div>
    </div>
  )
}

// pages/customer/profile.js

import Head from 'next/head';
import { useState, useEffect } from 'react';
import axios from 'axios';
import { useRouter } from 'next/router';
import Navbar from '@/components/navbar/navbar';

export default function CustomerProfile() {
    const [customer, setCustomer] = useState(null);
    const [showFullAccNo, setShowFullAccNo] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const router = useRouter();

    const toggleAccNoVisibility = () => {
        setShowFullAccNo(!showFullAccNo);
    };

    useEffect(() => {
        const token = localStorage.getItem('customer-token');
        const username = localStorage.getItem('customer-username');
        if(!token){
            router.push('/customer/login');
            setIsLoggedIn(false);
            return;
        }
        else {
            setIsLoggedIn(true);
        }
        const config = {
            headers: {
                Authorization: `Bearer ${token}`
            },
            params: {
                username: username
            }
        };

        const fetchCustomerData = async () => {
            try {
                const response = await axios.get('/customer/profile', config);
                setCustomer(response.data);
            } catch (error) {
                console.error('Error fetching customer data:', error);
            }
        };

        fetchCustomerData();
    }, []);

    return (
        <div>
        <Navbar login={isLoggedIn} />
            <Head>
                <title>Customer Profile - Bank4Ever</title>
                <meta name="description" content="Customer Profile - Bank4Ever" />
                <link rel="icon" href="/favicon.ico" />
            </Head>

            <main>
                <section className="max-w-3xl mx-auto bg-white shadow-md rounded-md p-8 mt-10 grid grid-cols-2 gap-8">
                    <div className="customer-info">
                        <h1 className="text-3xl font-bold text-center mb-8">Bank4Ever</h1>
                        <h2 className="text-xl font-semibold mb-4 text-center">Customer Details</h2>
                        {customer && (
                            <div>
                                <p className="text-lg"><span className="font-semibold">Customer Name:</span> {customer.person.firstName} {customer.person.lastName}</p>
                                <p className="text-lg"><span className="font-semibold">Username:</span> {customer.userName}</p>
                                <p className="text-lg">
                                    <span className="font-semibold">Account No:</span>
                                    {showFullAccNo ? customer.accNo : "**** **** **** " + customer.accNo.slice(-4)}
                                    <button onClick={toggleAccNoVisibility} className="ml-2 focus:outline-none">
                                        {showFullAccNo ? '👁️' : '🔒'} {/* Eye icon or lock icon */}
                                    </button>
                                </p>

                                <p className="text-lg"><span className="font-semibold">Address:</span> {customer.person.address}</p>
                                <p className="text-lg"><span className="font-semibold">DOB:</span> {customer.person.dob}</p>
                                <p className="text-lg"><span className="font-semibold">Pan No:</span> {customer.panNo}</p>
                                <p className="text-lg"><span className="font-semibold">Email:</span> {customer.person.email}</p>
                                <p className="text-lg"><span className="font-semibold">Phone:</span> {customer.person.phoneNo}</p>
                                <p className="text-lg"><span className="font-semibold">Account Balance:</span> ₹{customer.balance}</p>
                            </div>
                        )}
                    </div>

                    <div className="branch-details flex flex-col justify-center">
                        <h2 className="text-xl font-semibold mb-4 text-center">Branch Details</h2>
                        {customer && (
                            <div>
                                <p className="text-lg"><span className="font-semibold">Branch Name:</span> {customer.branchName}</p>
                                <p className="text-lg"><span className="font-semibold">IFSC Code:</span> {customer.ifscCode}</p>
                                <p className="text-lg"><span className="font-semibold">Branch Address:</span> {customer.branchAddress}</p>
                                <p className="text-lg"><span className="font-semibold">Branch Phone:</span> {customer.phoneNo}</p>
                            </div>
                        )}
                    </div>
                </section>
            </main>


            <footer className="text-center mt-8">
                <p className="text-gray-500">&copy; 2024 Bank4Ever. All rights reserved.</p>
            </footer>
        </div>
    );
}

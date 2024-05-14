// pages/index.js

import React from "react"
import Navbar from '@/components/navbar/navbar';
import axios from 'axios';
import Head from 'next/head';
import { useRouter } from 'next/router';
import { useState, useEffect } from 'react';

export default function Home() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [user, setUser] = useState(null);
    const [firstName, setFirstName] = useState('');
    const router = useRouter();

    const fetchCustomer = async () => {
        const username = localStorage.getItem('customer-username');
        await axios.get(`/customer/getCustomer/${username}`).then((res) => {
            console.log(res.data);
            setUser(res.data);
            setFirstName(res.data.person.firstName);
        }
        ).catch((err) => {
            console.log(err);
        });
    }

    useEffect(() => {
        const token = localStorage.getItem('customer-token');
        if (!token) {
            setIsLoggedIn(false);
            return;
        }
        else {
            setIsLoggedIn(true);
            fetchCustomer();
        }
    }, [])

    const handleSignup = () => {
        window.location.href = '/customer/signup';
    }
    return (
        <div>
            <Navbar login={isLoggedIn} />
            <Head>
                <title>Welcome to Bank4Ever</title>
                <meta name="description" content="Bank4Ever - Your Trusted Banking Partner" />
                <link rel="icon" href="/favicon.ico" />
            </Head>

            <main>
                {/* Hero section */}
                <section className="bg-slate-800 text-white py-20">
                    <div className="container mx-auto">
                        <h1 className="text-4xl md:text-6xl font-bold mb-4">Hi, {firstName}</h1>
                        <p className="text-lg md:text-xl">Welcome to Bank4Ever</p>
                    </div>
                </section>

                {/* Features section */}
                <section className="py-20">
                    <div className="container mx-auto">
                        <h2 className="text-3xl font-bold mb-8">Why Choose Bank4Ever?</h2>
                        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                            {/* Feature 1 */}
                            <div className="p-6 bg-white shadow-lg rounded-md">
                                <h3 className="text-xl font-semibold mb-4">Secure Transactions</h3>
                                <p>Bank4Ever ensures the security of your transactions with the latest encryption technologies.</p>
                            </div>
                            {/* Feature 2 */}
                            <div className="p-6 bg-white shadow-lg rounded-md">
                                <h3 className="text-xl font-semibold mb-4">Easy Account Management</h3>
                                <p>Manage your accounts effortlessly with Bank4Ever&apos;s intuitive online banking platform.</p>
                            </div>
                            {/* Feature 3 */}
                            <div className="p-6 bg-white shadow-lg rounded-md">
                                <h3 className="text-xl font-semibold mb-4">24/7 Customer Support</h3>
                                <p>Our dedicated support team is available round-the-clock to assist you with any queries or concerns.</p>
                            </div>
                        </div>
                    </div>
                </section>

                {/* Call to action */}
                {/* <section className="bg-slate-800 text-white py-16">
                    <div className="container mx-auto text-center">
                        <h2 className="text-3xl font-bold mb-4">Open an Account Today!</h2>
                        <p>Experience hassle-free banking with Bank4Ever. Sign up now to get started.</p>
                        <button className="mt-6 bg-white text-slate-800 px-8 py-3 rounded-md font-semibold hover:bg-slate-900 hover:text-white" onClick={handleSignup}>Sign Up</button>
                    </div>
                </section> */}
            </main>

            <footer className="bg-gray-200 py-12 text-center">
                <div className="container mx-auto">
                    <p>&copy; 2024 Bank4Ever. All rights reserved.</p>
                </div>
            </footer>
        </div>
    );
}

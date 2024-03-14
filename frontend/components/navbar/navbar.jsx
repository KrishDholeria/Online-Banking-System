// components/Navbar.js

import Link from 'next/link';
import { useEffect } from 'react';

const Navbar = ({login}) => {
    const handleLogout = () => {
        localStorage.removeItem('customer-token');
        localStorage.removeItem('customer-username');
        setIsLoggedIn(false);
    }
    return (
        <nav className="bg-slate-800 p-4">
            <div className="container mx-auto flex justify-between items-center">
                <Link href="/customer" className="text-white font-bold text-lg">
                    Bank4Ever
                </Link>
                {login && (<ul className="flex space-x-4 text-white">
                    <li>
                        <Link href="/customer/transaction">
                            Transactions
                        </Link>
                    </li>
                    <li>
                        <Link href="/customer/beneficiary">
                            Beneficiaries
                        </Link>
                    </li>
                    <li>
                        <Link href="/customer/profile">
                            Profile
                        </Link>
                    </li>
                    <li>
                        <Link href="" onClick={handleLogout}>
                            Logout
                        </Link>
                    </li>
                </ul>)}
                {!login && (<ul className="flex space-x-4 text-white">
                    <li>
                        <Link href="/customer/login">
                            Login
                        </Link>
                    </li>
                </ul>
                )}
            </div>
        </nav>
    );
}

export default Navbar;

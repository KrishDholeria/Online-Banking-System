// components/Navbar.js

import Link from 'next/link';
import { FiChevronDown } from 'react-icons/fi';
import { DropdownMenu, DropdownMenuContent, DropdownMenuGroup, DropdownMenuItem, DropdownMenuTrigger } from '../ui/dropdown-menu';

const Navbar = ({ login }) => {
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
                {login && (<ul className="flex space-x-12 text-white">
                    <li>
                        <Link href="/customer/statement">
                            Statement
                        </Link>
                    </li>
                    <li>
                        <Link href="/customer/transaction">
                            Transfer
                        </Link>
                    </li>
                    <li>
                        <DropdownMenu>
                            <DropdownMenuTrigger className='flex'>
                                Beneficiery <FiChevronDown className='mt-1 ml-1' />
                            </DropdownMenuTrigger>
                            <DropdownMenuContent>
                                <DropdownMenuGroup>
                                    <DropdownMenuItem>
                                        <Link href="/customer/beneficiary/add">
                                            Add Beneficiery
                                        </Link>
                                    </DropdownMenuItem>
                                    <DropdownMenuItem>
                                        <Link href="/customer/beneficiary">
                                            Manage Beneficiery
                                        </Link>
                                    </DropdownMenuItem>
                                </DropdownMenuGroup>
                            </DropdownMenuContent>
                        </DropdownMenu>
                    </li>
                    <li>
                        <DropdownMenu>
                            <DropdownMenuTrigger className='flex'>
                                Profile <FiChevronDown className='mt-1 ml-1' />
                            </DropdownMenuTrigger>
                            <DropdownMenuContent>
                                <DropdownMenuGroup>
                                    <DropdownMenuItem>
                                        <Link href="/customer/profile">
                                            Details
                                        </Link>
                                    </DropdownMenuItem>
                                    <DropdownMenuItem>
                                        <Link href="/customer/changePassword">
                                            Change Password
                                        </Link>
                                    </DropdownMenuItem>
                                </DropdownMenuGroup>
                            </DropdownMenuContent>
                        </DropdownMenu>
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

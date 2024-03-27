// components/Navbar.js

import Link from 'next/link';
import { FiChevronDown } from 'react-icons/fi';
import { DropdownMenu, DropdownMenuContent, DropdownMenuGroup, DropdownMenuItem, DropdownMenuTrigger } from '../ui/dropdown-menu';

const LoginNavbar = ({login}) => {
    return (
        <nav className="bg-slate-800 p-4">
            <div className="container mx-auto flex justify-between items-center">
                <Link href="/customer" className="text-white font-bold text-lg">
                    Bank4Ever
                </Link>
                {!login && (<ul className="flex space-x-4 text-white">
                    <li>
                        <Link href="/customer/login">
                            Login
                        </Link>
                    </li>
                </ul>
                )}
                {login && (<ul className="flex space-x-4 text-white">
                    <li>
                        <Link href="/customer/signup">
                            Signup
                        </Link>
                    </li>
                </ul>
                )}
            </div>
        </nav>
    );
}

export default LoginNavbar;

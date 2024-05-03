import React, { useEffect, useState } from 'react'
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"
import { Label } from "@/components/ui/label"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import axios from 'axios'
import { useRouter } from 'next/router'
import LoginNavbar from '@/components/navbar/loginNavbar'
import { toast } from 'sonner'
import Navbar from '@/components/navbar/navbar'

const login = () => {
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmNewPassword, setConfirmNewPassword] = useState('');
    const [error, setError] = useState(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const router = useRouter();

    useEffect(() => {
        const token = localStorage.getItem('customer-token');
        if (!token) {
            router.push('/customer/login');
            setIsLoggedIn(false);
            return;
        }
        else {
            setIsLoggedIn(true);
        }
    }, []);

    const handleOldpasswordChange = (e) => {
        setOldPassword(e.target.value);
    }

    const handleNewPasswordChange = (e) => {
        setNewPassword(e.target.value);
    }

    const handleConfirmPasswordChange = (e) => {
        setConfirmNewPassword(e.target.value);
    }

    const handleLogin = async (e) => {
        e.preventDefault();
        if (oldPassword === '' || newPassword === '' || confirmNewPassword === '') {
            setError('Please fill all the fields.');
            return;
        }
        // just minimum eight characters password
        var validpasswordregx = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
        if (!newPassword.match(validpasswordregx)) {
            setError('Password should be minimum eight characters containing lowercase letters, uppercase letters, digits, and special characters.');
            return;
        }
        if (newPassword !== confirmNewPassword) {
            setError('Password does not match.');
            return;
        }

        const username = localStorage.getItem('customer-username');
        const token = localStorage.getItem('customer-token');
        const config = {
            headers: {
                Authorization: `Bearer ${token}`
            }
        };

        const user = await axios.post('/customer/changePassword', { username, oldPassword, newPassword }, config).then(async (res) => {
            console.log(res);
            switch (res.data.responseCode) {
                case "015":
                    toast(
                        'Invalid old password.',
                        {
                            description: 'Enter valid old password.',
                            type: 'error',
                            action: {
                                label: 'Close',
                                onClick: () => toast.dismiss()
                            }
                        }
                    )
                    break;
                case "011":
                    toast(
                        'Password changed successfully.',
                        {
                            type: 'success',
                            action: {
                                label: 'Close',
                                onClick: () => {
                                    toast.dismiss();
                                }
                            }
                        }
                    )
                    router.push('/customer');
                    break;
                default:
                    toast(
                        'Something went wrong.',
                        {
                            description: 'Please try again.',
                            type: 'error',
                            action: {
                                label: 'Close',
                                onClick: () => toast.dismiss()
                            }
                        }
                    )
            }

        }
        )
            .catch((err) => {
                console.log(err);
                console.log("Error!!!!!")
                setError('Something went wrong. Please try again.');
                return;
            }
            );
    }

    return (
        <div>
            <Navbar login={isLoggedIn} />
            <div className="flex justify-center mt-44">
                <Card className={`w-[30%]`}>
                    <CardHeader>
                        <CardTitle className="text-3xl flex justify-center">Update Password</CardTitle>
                        <CardDescription className="flex justify-center">Enter new password to change your password.</CardDescription>
                    </CardHeader>
                    <CardContent>
                        <form onSubmit={handleLogin}>
                            <div className="grid w-full items-center gap-4">
                                <div className="flex flex-col space-y-1.5">
                                    <Label htmlFor="oldpassword">Old Password</Label>
                                    <Input id="oldPassword" type="password" placeholder="Old Password" onChange={handleOldpasswordChange} />
                                </div>
                                <div className="flex flex-col space-y-1.5">
                                    <Label htmlFor="newpassword">New Password</Label>
                                    <Input id="newPassword" type="password" placeholder="New Password" onChange={handleNewPasswordChange} />
                                </div>
                                <div className="flex flex-col space-y-1.5">
                                    <Label htmlFor="confirmpassword">Confirm New Password</Label>
                                    <Input id="confirmNewPassword" type="password" placeholder="Confirm New Password" onChange={handleConfirmPasswordChange} />
                                </div>
                                <div className="flex justify-center h-1">
                                    {error && <div className="text-red-500">*{error}</div>}
                                </div>
                                <div className="flex justify-center mt-16">
                                    <Button className="w-full h-11 text-lg">Change Password</Button>
                                </div>
                            </div>
                        </form>
                    </CardContent>
                    {/* <CardFooter className="flex flex-col justify-center items-center">
                        <div className="mb-2">
                            <a href="/customer/signup">Don't have an account? <span className='underline'>Signup</span></a>
                        </div>
                        <div>
                            <a href="/customer/forgot_password">Forgot password?</a>
                        </div>
                    </CardFooter> */}
                </Card>
            </div>
        </div>
    )
}

export default login

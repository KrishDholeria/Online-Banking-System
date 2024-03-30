import React, { useState } from 'react'
import { Loader2 } from 'lucide-react'
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
import { useCarousel } from '@/components/ui/carousel'
import axios from 'axios'
import { toast } from 'sonner'


export default function step1({ setUser }) {
    const [username, setUsername] = useState('');
    const [account, setAccount] = useState('');
    const [confirmaccount, setConfirmAccount] = useState('');
    const [ifsccode, setIfscCode] = useState('');
    const [error, setError] = useState(null);
    const { scrollNext } = useCarousel();
    const [loading, setLoading] = useState(false);

    const handleUsernameChange = (e) => {
        error && setError(null);
        setUsername(e.target.value);
    }
    const handleAccountChange = (e) => {
        error && setError(null);
        setAccount(e.target.value);
    }
    const handleConfirmAccountChange = (e) => {
        error && setError(null);
        setConfirmAccount(e.target.value);
    }
    const handleIfscCodeChange = (e) => {
        error && setError(null);
        setIfscCode(e.target.value);
    }

    const handlenext1 = async (e) => {
        e.preventDefault();
        setLoading(true);
        if (username === '' || account === '' || confirmaccount === '' || ifsccode === '') {
            setError('Please fill all the fields.');
            setLoading(false);
            return;
        }
        const validusernameregx = /^[a-zA-Z0-9]+$/; //alphanumeric
        if (!username.match(validusernameregx)) {
            setError('Username should be alphanumeric.');
            setLoading(false);
            return;
        }
        // regular expression for 12 digits number
        const validaccountregx = /^\d{12}$/
        if (!account.match(validaccountregx)) {
            setError('Account number must be 12 digits.');
            setLoading(false);
            return;
        }
        if (account !== confirmaccount) {
            setError('Account number and confirm account number do not match.');
            setLoading(false);
            return;
        }
        if (error !== null) {
            setLoading(false);
            return;
        }
        const user = await axios.post('/customer/setusername', {
            userName: username,
            accountNo: account,
            branchCode: ifsccode
        }).then(async (res) => {
            console.log(res);
            switch (res.data.responseCode) {
                case "002":
                    toast(
                        'Account with this account number not found.',
                        {
                            description: 'Enter valid account number.',
                            type: 'error',
                            action: {
                                label: 'Close',
                                onClick: () => toast.dismiss()
                            }
                        }
                    )
                    break;
                case "010":
                    toast(
                        'Invalid IFSC code.',
                        {
                            description: 'Enter a valid IFSC code.',
                            type: 'error',
                            action: {
                                label: 'Close',
                                onClick: () => toast.dismiss()
                            }
                        }
                    )
                    break;
                case "012":
                    toast(
                        'Account already exist.',
                        {
                            description: 'Try login.',
                            type: 'error',
                            action: {
                                label: 'Close',
                                onClick: () => toast.dismiss()
                            }
                        }
                    )
                    break;
                case "008":
                    toast(
                        'This is a weak username.',
                        {
                            description: 'Choose a different username.',
                            type: 'error',
                            action: {
                                label: 'Close',
                                onClick: () => toast.dismiss()
                            }
                        }
                    )
                    break;
                case "009":
                    await axios.get('/customer/sendotp')
                        .then(res => {
                            console.log(res.data);
                            toast(
                                'OTP sent successfully.',
                                {
                                    action: {
                                        label: 'Close',
                                        onClick: () => toast.dismiss()
                                    }
                                }
                            )
                        })
                        .catch(err => {
                            console.log(err);
                        })
                    setUser(res.data);
                    scrollNext();
                    break;
            }
            setLoading(false);
        })
            .catch((err) => {
                console.log(err);
                console.log("Error!!!!!")
                setError('Something went wrong. Please try again.');
                setLoading(false);
                return;
            }
            );
        console.log(user);
        setError(null);
        setLoading(false);
    }

    return (<Card className={`w-full h-[600px] overflow-auto`}>
        <CardHeader>
            <CardTitle className="text-3xl flex justify-center">Welcome to Bank4Ever</CardTitle>
            <CardDescription className="flex justify-center">Enter your credentials to signup</CardDescription>
        </CardHeader>
        <CardContent>
            <form onSubmit={handlenext1}>
                <div className="grid w-full items-center gap-4">
                    <div className="flex flex-col space-y-1.5">
                        <Label htmlFor="username">Username</Label>
                        <Input id="username" placeholder="Username" onChange={handleUsernameChange} />
                    </div>
                    <div>
                        <Label htmlFor="account">Account Number</Label>
                        <Input id="account" type="password" placeholder="Account Number" onChange={handleAccountChange} />
                    </div>
                    <div>
                        <Label htmlFor="confirmaccount">Re-enter Account Number</Label>
                        <Input id="confirmaccount" placeholder="Re-enter Account Number" onChange={handleConfirmAccountChange} />
                    </div>
                    <div>
                        <Label htmlFor="ifsccode">IFSC code</Label>
                        <Input id="ifsccode" placeholder="IFSC code" onChange={handleIfscCodeChange} />
                    </div>
                    <div className="flex justify-center h-1">
                        {error && <div className="text-red-500">*{error}</div>}
                    </div>
                    {
                        (loading && error == null) ? <Button disabled>
                            <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                            Please wait
                        </Button> : <div className="flex justify-center mt-7">
                            <Button className="w-full h-11 text-lg">Next</Button>
                        </div>
                    }

                </div>
            </form>
        </CardContent>
        <CardFooter className="flex justify-center">
            Already have an account? <a href="/customer/login" className='underline'>Login</a>
        </CardFooter>
    </Card>)
}
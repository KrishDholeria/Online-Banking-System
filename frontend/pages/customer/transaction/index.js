import { Button } from "@/components/ui/button"
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { useState, useEffect } from 'react'
import axios from 'axios'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { useRouter } from "next/router"
import { toast } from 'sonner'
import Navbar from "@/components/navbar/navbar"


export default function Transaction() {
    const [beneficiery, setBeneficiery] = useState('');
    const [amount, setAmount] = useState('');
    const [accountNo, setAccountNo] = useState('');
    const [type, setType] = useState('');
    const [activeTab, setActiveTab] = useState('transfer');
    const [otp, setOtp] = useState('OTP');
    const [error, setError] = useState(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [resendDisabled, setResendDisabled] = useState(false);
    const [countdown, setCountdown] = useState(60);
    const router = useRouter();

    const handleResendClick = async () => {

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
        setResendDisabled(true);
        setCountdown(60);

        const intervalId = setInterval(() => {
            setCountdown((prev) => {
                if (prev === 0) {
                    clearInterval(intervalId);
                    setResendDisabled(false);
                    return 60;
                } else {
                    return prev - 1;
                }
            });
        }, 1000);
    };

    useEffect(() => {
        if (countdown === 0) {
            setResendDisabled(false);
        }
    }, [countdown]);

    const getbeneficieries = async () => {
        const token = localStorage.getItem('customer-token');
        const username = localStorage.getItem('customer-username');
        const headers = {
            'Authorization': `Bearer ${token}`
        }
        await axios.get(`customer/getbeneficieries/${username}`, { headers })
            .then(res => {
                setBeneficiery(res.data);
            })
            .catch(err => {
                console.log(err);
            })
    }

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
        // fetch beneficiery
        getbeneficieries();
    }, [])

    const handleAmountChange = (e) => {
        error && setError(null);
        setAmount(e.target.value);
    }
    const handleBeneficieryChange = (e) => {
        error && setError(null);
        setAccountNo(e);
    }
    const handleTypeChange = (e) => {
        error && setError(null);
        setType(e);
    }
    const handleTabChange = (e) => {
        console.log(e);
        setActiveTab(e);
    }
    const handleOtpChange = (e) => {
        error && setError(null);
        setOtp(e.target.value);
    }
    const handleNext = async (e) => {
        e.preventDefault();
        if (amount === '' || accountNo === '' || type === '') {
            setError('Please fill all the fields.');
            return;
        }
        setError(null);
        // check if amount is a number
        if (isNaN(amount)) {
            setError('Amount should be a number.');
            return;
        }
        // check if amount is greater than 0
        if (amount <= 0) {
            setError('Amount should be greater than 0.');
            return;
        }
        // check if amount is less than 100000
        if (amount > 100000) {
            setError('Amount should be less than 100000.');
            return;
        }

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
        console.log('OTP sent');
        setActiveTab("OTP");
        console.log(activeTab);
    }
    const handleVerify = async (e) => {
        e.preventDefault();
        if (otp === '') {
            setError('Please fill the OTP field.');
            return;
        }
        setError(null);
        const token = localStorage.getItem('customer-token');
        const headers = {
            'Authorization': `Bearer ${token}`
        }
        await axios.post('/customer/verifyotp', { otp }, { headers })
            .then(res => {
                console.log(res.data);
                res.status === 203 ?
                    toast(
                        'Invalid OTP.',
                        {
                            description: 'Please enter a valid OTP.',
                            action: {
                                label: 'Close',
                                onClick: () => toast.dismiss()
                            }
                        }
                    )
                    : makeTransaction();
            })
            .catch(err => {
                console.log(err);
            })
        console.log('OTP verified');

    }
    const makeTransaction = async () => {
        toast(
            'OTP verified successfully.',
            {
                action: {
                    label: 'Close',
                    onClick: () => toast.dismiss()
                }
            }
        )
        const username = localStorage.getItem('customer-username');
        const token = localStorage.getItem('customer-token');
        const headers = {
            'Authorization': `Bearer ${token}`
        }
        const res = await axios.post(`/customer/maketransacion/${username}`, { accountNo, amount, transactionType: type }, { headers })
            .then(res => {
                console.log(res.data);
                switch (res.data.responseCode) {
                    case "002":
                        toast(
                            'Account for the beneficiery not found.',
                            {
                                description: 'Please choose a valid beneficiery.',
                                action: {
                                    label: 'Close',
                                    onClick: () => toast.dismiss()
                                }
                            }
                        )
                        router.push('/customer/transcation');
                        break;
                    case "001":
                        toast(
                            'Insufficient balance.',
                            {
                                description: 'Please enter a valid ammount.',
                                action: {
                                    label: 'Close',
                                    onClick: () => toast.dismiss()
                                }
                            }
                        )
                        router.push('/customer/transcation');
                        break;
                    case "003":
                        toast(
                            'Transaction succesful.',
                            {
                                action: {
                                    label: 'Close',
                                    onClick: () => toast.dismiss()
                                }
                            }
                        )
                        router.push('/customer/');
                        break;
                    default:
                        toast(
                            'Transaction failed.',
                            {
                                action: {
                                    label: 'Close',
                                    onClick: () => toast.dismiss()
                                }
                            }
                        )
                        router.push('/customer/transcation');
                        break;
                }
            })
            .catch(err => {
                console.log(err);
            });
    }



    return (
        <div>
            <Navbar login={isLoggedIn} />
            <div className="flex justify-center mt-52">
                {activeTab === 'transfer' && (<Card className="w-[400px]">
                    <CardHeader>
                        <CardTitle>Transfer Money</CardTitle>
                        <CardDescription>
                            Transfer money to another account.
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-2">
                        <div className="space-y-1">
                            <Label htmlFor="beneficiery">Transaction Type</Label>
                            <Select onValueChange={handleTypeChange}>
                                <SelectTrigger>
                                    <SelectValue placeholder="Select Transaction Type" />
                                </SelectTrigger>
                                <SelectContent>
                                    <SelectItem value="NEFT">NEFT</SelectItem>
                                    <SelectItem value="IMPS">IMPS</SelectItem>
                                    <SelectItem value="RTGS">RTGS</SelectItem>
                                </SelectContent>
                            </Select>
                        </div>
                        <div className="space-y-1">
                            <Label htmlFor="beneficiery">Beneficiery</Label>
                            <Select onValueChange={handleBeneficieryChange}>
                                <SelectTrigger>
                                    <SelectValue placeholder="Select Beneficiery" />
                                </SelectTrigger>
                                <SelectContent>
                                    {beneficiery && beneficiery.map((b, i) => {
                                        return <SelectItem key={i} value={b.accountNo}>{b.beneficiaryName}</SelectItem>
                                    })}
                                </SelectContent>
                            </Select>
                        </div>
                        <div className="space-y-1">
                            <Label htmlFor="amount">Amount</Label>
                            <Input id="amount" placeholder="10000" onChange={handleAmountChange} />
                        </div>
                        <div className="h-1">
                            {error && <div className="text-red-500">*{error}</div>}
                        </div>
                    </CardContent>
                    <CardFooter>
                        <Button onClick={handleNext}>Next</Button>
                    </CardFooter>
                </Card>)}
                {activeTab === 'OTP' && (<Card className='w-[400px]'>
                    <CardHeader>
                        <CardTitle>Verify OTP</CardTitle>
                        <CardDescription>
                            Enter the OTP sent to your registered mobile number.
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-2">
                        <div className="space-y-1">
                            <Label htmlFor="otp">Enter OTP</Label>
                            <Input id="otp" type="password" onChange={handleOtpChange} />
                        </div>
                        <Button onClick={handleVerify}>Confirm</Button>
                    </CardContent>
                    <CardFooter className='flex justify-center'>
                        <div className="text-sm text-center">Didn't receive the OTP? {resendDisabled ? `Resend OTP in ${countdown} seconds` : <span className="text-blue-500 cursor-pointer" onClick={handleResendClick}>Resend OTP</span>}</div>
                    </CardFooter>
                </Card>)}
            </div>
        </div>
    )
}

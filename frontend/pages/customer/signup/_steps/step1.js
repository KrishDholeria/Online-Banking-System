import React, { useState } from 'react'
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


export default function step1() {
    const [username, setUsername] = useState('');
    const [account, setAccount] = useState('');
    const [confirmaccount, setConfirmAccount] = useState('');
    const [ifsccode, setIfscCode] = useState('');
    const [error, setError] = useState(null);

    const handleUsernameChange = (e) => {
        setUsername(e.target.value);
    }
    const handleAccountChange = (e) => {
        setAccount(e.target.value);
    }
    const handleConfirmAccountChange = (e) => {
        setConfirmAccount(e.target.value);
    }
    const handleIfscCodeChange = (e) => {
        setIfscCode(e.target.value);
    }

    const handlenext1 = async (e) => {
        e.preventDefault();
        if (username === '' || account === '' || confirmaccount === '' || ifsccode === '') {
            setError('Please fill all the fields.');
            return;
        }
        var validusernameregx = /^[a-zA-Z0-9]+$/; //alphanumeric
        if (!username.match(validusernameregx)) {
            setError('Username should be alphanumeric.');
            return;
        }
        // regular expression for 12 digits number
        var validaccountregx = /^[0-9]{12}$/
        if (!account.match(validaccountregx)) {
            setError('Account number must be 12 digits.');
            return;
        }
        if (account !== confirmaccount) {
            setError('Account number and confirm account number do not match.');
            return;
        }
        setError(null);
    }

    return (<Card className={`w-[450px] h-[580px]`}>
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
                <div className="flex justify-center mt-7">
                    <Button className="w-full h-11 text-lg">Next</Button>
                </div>
            </div>
        </form>
    </CardContent>
    <CardFooter className="flex justify-center">
        Already have an account? <a href="/customer/login" className='underline'>Login</a>
    </CardFooter>
</Card>)
}
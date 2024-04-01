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
import { useCarousel } from '@/components/ui/carousel'
import { ArrowLeft } from "lucide-react"


export default function step1() {
    const [password, setPassword] = useState('');
    const [confirmpassword, setConfirmPassword] = useState('');
    const [error, setError] = useState(null);
    const {scrollNext, scrollPrev} = useCarousel();

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    }
    const handleConfirmPasswordChange = (e) => {
        setConfirmPassword(e.target.value);
    }

    const handlenext1 = async (e) => {
        e.preventDefault();
        if (password === '' || confirmpassword === '') {
            setError('Please enter password.');
            return;
        }
        if (password !== confirmpassword) {
            setError('Password and confirm password do not match.');
            return;
        }
        setError(null);
        scrollNext();
    }

    return (<Card className={`w-full h-[450px] overflow-auto`}>
    <CardHeader>
        <div>
            <ArrowLeft className="w-6 h-6" onClick={() => {scrollPrev()}} />
        </div>
        <CardTitle className="text-3xl flex justify-center">Create Password</CardTitle>
        <CardDescription className="text-center">Create a password for your account. This will be used to login to your account.</CardDescription>
    </CardHeader>
    <CardContent>
        <form onSubmit={handlenext1}>
            <div className="grid w-full items-center gap-4">
                <div className="flex flex-col space-y-1.5">
                    <Label htmlFor="password">Password</Label>
                    <Input id="password" type="password" placeholder="Password" onChange={handlePasswordChange} />
                </div>
                <div className="flex flex-col space-y-1.5">
                    <Label htmlFor="confirmPassword">Confirm Password</Label>
                    <Input id="confirmPassword" type="password" placeholder="Confirm Password" onChange={handleConfirmPasswordChange} />
                </div>
                <div className="flex justify-center h-1">
                    {error && <div className="text-red-500">*{error}</div>}
                </div>
                <div className="flex justify-center mt-7">
                    <Button className="w-full h-11 text-lg">Sign Up</Button>
                </div>
            </div>
        </form>
    </CardContent>
</Card>)
}
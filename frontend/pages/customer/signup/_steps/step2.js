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
    const [otp, setOtp] = useState('');
    const [error, setError] = useState(null);
    const {scrollNext, scrollPrev} = useCarousel();

    const handleOtpChange = (e) => {
        setOtp(e.target.value);
    }

    const handlenext1 = async (e) => {
        e.preventDefault();
        if (otp === '') {
            setError('please enter OTP.');
            return;
        }
        const validotpregx = /^\d{6}$/;
        if (!otp.match(validotpregx)) {
            setError('OTP should be 6 digits.');
            return;
        }
        setError(null);
        scrollNext();
    }

    return (<Card className={`w-full h-[390px] overflow-auto`}>
    <CardHeader>
        <div>
            <ArrowLeft className="w-6 h-6" onClick={() => {scrollPrev()}} />
        </div>
        <CardTitle className="text-3xl flex justify-center">Enter OTP</CardTitle>
        <CardDescription className="text-center">We have sent an OTP to your registered mobile number. Please enter the OTP to continue.</CardDescription>
    </CardHeader>
    <CardContent>
        <form onSubmit={handlenext1}>
            <div className="grid w-full items-center gap-4">
                <div className="flex flex-col space-y-1.5">
                    <Label htmlFor="otp">Enter OTP</Label>
                    <Input id="otp" placeholder="OTP" onChange={handleOtpChange} />
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
        <div className="text-sm text-center">Didn't receive the OTP? <span className="text-blue-500 cursor-pointer">Resend OTP</span></div>
    </CardFooter>
</Card>)
}
import React, { useState, useEffect } from 'react'
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
import axios from 'axios'
import { toast } from "sonner";


export default function step1() {
    const [otp, setOtp] = useState('');
    const [error, setError] = useState(null);
    const [resendDisabled, setResendDisabled] = useState(false);
    const [countdown, setCountdown] = useState(60);
    const {scrollNext, scrollPrev} = useCarousel();

    const handleOtpChange = (e) => {
        setOtp(e.target.value);
    }

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
        if(error){
            return;
        }
        const response = await axios.post("/customer/verifyotp", {otp});
        if(response.status !== 200){
            setError('Invalid OTP');
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
    <div className="text-sm text-center">Didn't receive the OTP? {resendDisabled ? `Resend OTP in ${countdown} seconds` : <span className="text-blue-500 cursor-pointer" onClick={handleResendClick}>Resend OTP</span>}</div>
    </CardFooter>
</Card>)
}
import React, { useState } from 'react'
import { Loader2 } from 'lucide-react'
import {
    Card,
    CardContent,
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
import Link from 'next/link'


export default function Step1({ setUser }) {
    const [username, setUsername] = useState('');
    const [error, setError] = useState(null);
    const { scrollNext } = useCarousel();
    const [loading, setLoading] = useState(false);

    const handleUsernameChange = (e) => {
        error && setError(null);
        setUsername(e.target.value);
    }

    const handlenext1 = async (e) => {
        e.preventDefault();
        setLoading(true);
        if (username === '') {
            setError('Please enter username.');
            setLoading(false);
            return;
        }
        const validusernameregx = /^[a-zA-Z0-9]+$/; //alphanumeric
        if (!username.match(validusernameregx)) {
            setError('Username should be alphanumeric.');
            setLoading(false);
            return;
        }
        if (error !== null) {
            setLoading(false);
            return;
        }
        const user = await axios.get(`/customer/getCustomer/${username}`).then(async (res) => {
            console.log(res);
            switch (res.data.responseCode) {
                case "014":
                    toast(
                        'Invalid username.',
                        {
                            description: 'Enter valid username.',
                            type: 'error',
                            action: {
                                label: 'Close',
                                onClick: () => toast.dismiss()
                            }
                        }
                    )
                    break;
                case "013":
                    await axios.get('/customer/sendotp')
                        .then(res => {
                            console.log(res.data);
                            toast(
                                'OTP sent successfully.',
                                {
                                    type: 'success',
                                    action: {
                                        label: 'Close',
                                        onClick: () => toast.dismiss()
                                    }
                                }
                            )
                            setUser({ userName: username });
                            scrollNext();
                        })
                        .catch(err => {
                            console.log(err);
                        })
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

    return (<Card className={`w-full overflow-auto`}>
        <CardHeader>
            <CardTitle className="text-3xl flex justify-center">Forgot Password</CardTitle>
        </CardHeader>
        <CardContent>
            <form onSubmit={handlenext1}>
                <div className="grid w-full items-center gap-4">
                    <div className="flex flex-col space-y-1.5">
                        <Label htmlFor="username">Username</Label>
                        <Input id="username" placeholder="Username" onChange={handleUsernameChange} />
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
            Remember your password? <Link href="/customer/login" className='underline'> Login</Link>
        </CardFooter>
    </Card>)
}
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
import { Loader2 } from 'lucide-react'
import Link from 'next/link'

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const router = useRouter();

  useEffect(() => {
    localStorage.removeItem('customer-token');
    localStorage.removeItem('customer-username');
  }, []);

  const handleUsernameChange = (e) => {
    setUsername(e.target.value);
  }

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  }

  const handleLogin = async (e) => {
    e.preventDefault();
    if (username === '' || password === '') {
      setError('Please fill all the fields.');
      return;
    }
    var validusernameregx = /^[a-zA-Z0-9]+$/; //alphanumeric
    if (!username.match(validusernameregx)) {
      setError('Username should be alphanumeric.');
      return;
    }
    // just minimum eight characters password
    var validpasswordregx = /^.{8,}$/;
    if (!password.match(validpasswordregx)) {
      setError('Password should be minimum eight characters.');
      return;
    }
    try {
      setLoading(true);
      const response = await axios.post("/auth/login", { username, password });
      console.log(response);
      setError(null);
      localStorage.setItem('customer-token', response.data.token);
      localStorage.setItem('customer-username', username);
      router.push('/customer');
    }
    catch (error) {
      console.log(error.response);
      toast('Invalid credentials.', { type: 'error', action: { label: 'Close', onClick: () => toast.dismiss() } });
    }
    setLoading(false);
    // const response = await axios.post("/auth/login", {username, password});
    // setError(null);
    // router.push('/customer');
  }

  return (
    <div>
      <LoginNavbar login={true} />
      <div className="flex justify-center mt-52">
        <Card className={`w-[30%]`}>
          <CardHeader>
            <CardTitle className="text-3xl flex justify-center">Welcome to Bank4Ever</CardTitle>
            <CardDescription className="flex justify-center">Enter your credentials to login</CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleLogin}>
              <div className="grid w-full items-center gap-4">
                <div className="flex flex-col space-y-1.5">
                  <Label htmlFor="username">UserName</Label>
                  <Input id="username" placeholder="UserName" onChange={handleUsernameChange} />
                </div>
                <div className="flex flex-col space-y-1.5">
                  <Label htmlFor="password">Password</Label>
                  <Input id="password" type="password" placeholder="Password" onChange={handlePasswordChange} />
                </div>
                <div className="flex justify-center h-1">
                  {error && <div className="text-red-500">*{error}</div>}
                </div>
                {
                  (loading && error == null) ? <Button disabled>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Please wait
                  </Button> : <div className="flex justify-center mt-5">
                    <Button className="w-full h-11 text-lg">Login</Button>
                  </div>
                }
              </div>
            </form>
          </CardContent>
          <CardFooter className="flex flex-col justify-center items-center">
            <div className="mb-2">
              <Link href="/customer/signup">Don&apos;t have an account? <span className='underline'>Signup</span></Link>
            </div>
            <div>
              <Link href="/customer/forgot_password">Forgot password?</Link>
            </div>
          </CardFooter>
        </Card>
      </div>
    </div>
  )
}

export default Login

import { useState } from "react"
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
import axios from "axios"



export default function addBeneficiery() {
    const [name, setName] = useState('');
    const [accountNo, setAccountNo] = useState('');
    const [confirmAccountNo, setConfirmAccountNo] = useState('');
    const [ifsc, setIfsc] = useState('');
    const [error, setError] = useState(null);

    const handleNameChange = (e) => {
        error && setError(null);
        setName(e.target.value);
    }
    const handleAccountNoChange = (e) => {
        error && setError(null);
        setAccountNo(e.target.value);
    }
    const handleConfirmAccountNoChange = (e) => {
        error && setError(null);
        setConfirmAccountNo(e.target.value);
    }
    const handleIfscChange = (e) => {
        error && setError(null);
        setIfsc(e.target.value);
    }
    const handleAdd = (e) => {
        e.preventDefault();
        if (name === '' || accountNo === '' || confirmAccountNo === '' || ifsc === '') {
            setError('Please fill all the fields.');
            return;
        }
        // regular expression for 12 digits number
        const validaccountregx = /^\d{12}$/
        if (!accountNo.match(validaccountregx)) {
            setError('Account number must be 12 digits.');
            return;
        }
        if (accountNo !== confirmAccountNo) {
            setError('Account numbers do not match.');
            return;
        }
        // regular expression for 11 digits number
        const validifscregx = /^B4EV0[A-Z0-9]{6}$/
        if (!ifsc.match(validifscregx)) {
            setError('IFSC code is invalid.');
            return;
        }
        setError(null);
        // Add the beneficiery to the database
        const data = {
            beneficiaryName: name,
            accountNo: accountNo,
            branchCode: ifsc
        }
        console.log('data', data);
        const token = localStorage.getItem('token');
        const username = localStorage.getItem('username');
        const headers = {
            'Authorization': `Bearer ${token}`
        }
        const response = axios.post(`/customer/addbeneficiary/${username}`, data, { headers: headers });

    }

    return (
        <div className="flex justify-center mt-40">
            <Card className="w-2/6">
                <CardHeader>
                    <CardTitle>Add Beneficiery</CardTitle>
                    <CardDescription>
                        Add a new beneficiery to your account.
                    </CardDescription>
                </CardHeader>
                <CardContent className="space-y-2">
                    <form onSubmit={handleAdd}>
                        <div className="space-y-1 mt-3">
                            <Label htmlFor="name">Beneficiery Name</Label>
                            <Input id="beneficiery name" placeholder="Pedro Duarte" onChange={handleNameChange} />
                        </div>
                        <div className="space-y-1 mt-3">
                            <Label htmlFor="accountNo">Account Number</Label>
                            <Input id="accountNo" placeholder="Account Number" type="password" onChange={handleAccountNoChange} />
                        </div>
                        <div className="space-y-1 mt-3">
                            <Label htmlFor="confirm accountNo">Confirm Account Number</Label>
                            <Input id="confirmAccountNo" placeholder="Confirm Account Number" onChange={handleConfirmAccountNoChange} />
                        </div>
                        <div className="space-y-1 mt-3">
                            <Label htmlFor="IFSC">IFSC</Label>
                            <Input id="ifsc" placeholder="IFSC Code" onChange={handleIfscChange} />
                        </div>
                        <div className="h-1">
                            {error && <div className="text-red-500">*{error}</div>}
                        </div>
                        <div className="mt-7">
                            <Button>ADD</Button>
                        </div>
                    </form>
                </CardContent>
                <CardFooter>
                </CardFooter>
            </Card>
        </div>
    )
}
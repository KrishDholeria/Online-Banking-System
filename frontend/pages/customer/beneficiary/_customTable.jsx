import {
    Table,
    TableCaption,
    TableHeader,
    TableRow,
    TableHead,
    TableCell,
    TableBody
} from "@/components/ui/table";
import { useState, useEffect, useRef } from 'react'
import { FiEdit, FiTrash2, FiCheck, FiXCircle } from 'react-icons/fi';
import axios from 'axios';
import { useRouter } from "next/router";
import { toast } from 'sonner';
import Loader from "@/components/loader";

export default function CustomTable({ beneficiaries, setBeneficiaries }) {
    const [isEditing, setIsEditing] = useState(null);
    const [rowData, setRowData] = useState();
    const [loading, setLoading] = useState(true);
    const router = useRouter();
    const inputref = useRef(null);
    useEffect(() => {
        const token = localStorage.getItem('customer-token');
        if (!token) {
            router.push('/customer/login');
            return;
        }
        const username = localStorage.getItem('customer-username');
        const headers = {
            'Authorization': `Bearer ${token}`
        }
        axios.get(`customer/getbeneficieries/${username}`, { headers })
            .then(res => {
                console.log(res.data)
                setBeneficiaries(res.data);
                setLoading(false);
            })
            .catch(err => {
                console.log(err);
            })
    }
        , [])

    useEffect(() => {
        if (isEditing !== null) {
            inputref.current.focus();
        }
    }, [isEditing]);

    const handleEdit = (id) => {
        setIsEditing(id);
        setRowData(beneficiaries[id]);
    }
    const handleChange = (e, id) => {
        const { name, value } = e.target;
        console.log(name, value);
        setRowData((prevData) => ({
            ...prevData,
            [name]: value
        }));
    };
    const handleUpdate = async () => {
        const token = localStorage.getItem('customer-token');
        const username = localStorage.getItem('customer-username');
        const headers = {
            "Authorization": `Bearer ${token}`
        }
        await axios.put(`customer/updateBeneficiary/${username}/${beneficiaries[isEditing].accountNo}`, rowData, { headers })
            .then(res => {
                // beneficiaries[isEditing] = rowData;
                console.log(res);
                setIsEditing(null);
                // router.push('/customer/beneficiary');

                switch (res.data.responseCode) {
                    case '002':
                        toast(
                            'Account doesn\'t exist for the given account number.',
                            {
                                description: "Please enter valid account details.",
                                type: 'error',
                                action: {
                                    label: 'Close',
                                    onClick: () => toast.dismiss()
                                }
                            }
                        )
                        break;
                    case "006":
                        toast(
                            'No branch exist for the given IFSC code.',
                            {
                                description: "Please enter a valid IFSC code.",
                                type: 'error',
                                action: {
                                    label: 'Close',
                                    onClick: () => toast.dismiss()
                                }
                            }
                        )
                        break;
                    case "004":
                        toast(
                            'Beneficiery already exist.',
                            {
                                description: "Try adding a different beneficiary. This one already exist.",
                                type: 'error',
                                action: {
                                    label: 'Close',
                                    onClick: () => toast.dismiss()
                                }
                            }
                        )
                        break;
                    case '007':
                        toast(
                            'Beneficiary updated successfully.',
                            {
                                action: {
                                    label: 'Close',
                                    onClick: () => toast.dismiss()
                                }
                            }
                        )
                        setBeneficiaries(res.data.beneficiaryDtoList);
                        // router.push('/customer/beneficiary');
                        break;
                }
            })
            .catch(err => {
                console.log(err);
            })
        setIsEditing(null);
    }
    const handleDelete = () => {
        const token = localStorage.getItem('customer-token');
        const username = localStorage.getItem('customer-username');
        const headers = {
            "Authorization": `Bearer ${token}`
        }
        axios.delete(`customer/deleteBeneficiary/${username}/${beneficiaries[isEditing].accountNo}`, { headers })
            .then(res => {
                beneficiaries.splice(isEditing, 1);
                console.log(beneficiaries);
                setIsEditing(null);
                router.push('/customer/beneficiary');
            })
            .catch(err => {
                console.log(err);
            })
        setIsEditing(null);
    }

    if (loading) {
        return (<Loader/>);
    }

    return (
        <div className="flex justify-center">
            <div className="w-[60%] mt-10">
                <Table className="w-full border-collapse">
                    <TableCaption>A list of your Beneficiaries.</TableCaption>
                    <TableHeader className="bg-gray-200">
                        <TableRow>
                            <TableHead className="w-[30%]">Name</TableHead>
                            <TableHead className="w-[30%]">Branch Code</TableHead>
                            <TableHead className="w-[30%]">Account Number</TableHead>
                            <TableHead className="w-[10%]">Action</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {beneficiaries.map((beneficiary, index) => (
                            <TableRow className="bg-gray-50" key={index}>
                                <TableCell>
                                    {isEditing === index ? (
                                        <input
                                            type="text"
                                            name="beneficiaryName"
                                            value={rowData.beneficiaryName}
                                            onChange={(e) => handleChange(e, index)}
                                            ref={inputref}
                                        />
                                    ) : (
                                        beneficiary.beneficiaryName
                                    )}
                                </TableCell>
                                <TableCell>
                                    {isEditing === index ? (
                                        <input
                                            type="text"
                                            name="branchCode"
                                            value={rowData.branchCode}
                                            onChange={(e) => handleChange(e, index)}
                                        />
                                    ) : (
                                        beneficiary.branchCode
                                    )}
                                </TableCell>
                                <TableCell>
                                    {isEditing === index ? (
                                        <input
                                            type="text"
                                            name="accountNo"
                                            value={rowData.accountNo}
                                            onChange={(e) => handleChange(e, beneficiary.accountNo)}
                                        />
                                    ) : (
                                        beneficiary.accountNo
                                    )}
                                </TableCell>
                                <TableCell>
                                    {isEditing === index ? (
                                        <div className="flex space-x-1">
                                            <FiCheck
                                                className="text-green-500 cursor-pointer mr-2"
                                                onClick={handleUpdate}
                                                size={18}
                                            />
                                            <FiXCircle
                                                className="text-red-500 cursor-pointer "
                                                onClick={() => setIsEditing(null)}
                                                size={18}
                                            />
                                            <FiTrash2
                                                className="text-red-500 cursor-pointer"
                                                onClick={handleDelete}
                                                size={18}
                                            />
                                        </div>
                                    ) : (
                                        <div className="flex space-x-2">
                                            <FiEdit
                                                className="text-blue-500 cursor-pointer"
                                                onClick={() => handleEdit(index)}
                                                size={18}
                                            />
                                        </div>
                                    )
                                    }
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </div>
        </div>
    );
}

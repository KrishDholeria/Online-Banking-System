import { useEffect } from "react"
import CustomTable from "./_customTable"
import { useRouter } from "next/router";



export default function beneficiery() {
    const router = useRouter();
    useEffect(() => {
        const token = localStorage.getItem('customer-token');
        if (!token) {
            router.push('/customer/login');
            return;
        }
    }, [])
    return (
        <div>
            <CustomTable />
        </div>
    )
}
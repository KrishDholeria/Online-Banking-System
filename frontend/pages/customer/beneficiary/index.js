import { useEffect, useState } from "react"
import CustomTable from "./_customTable"
import { useRouter } from "next/router";



export default function beneficiery() {
    const router = useRouter();
    const [beneficiaries, setBeneficiaries] = useState([]);
    useEffect(() => {
        const token = localStorage.getItem('customer-token');
        if (!token) {
            router.push('/customer/login');
            return;
        }
    }, [beneficiaries])
    return (
        <div>
            <CustomTable beneficiaries={beneficiaries} setBeneficiaries={setBeneficiaries} />
        </div>
    )
}
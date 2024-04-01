import { useEffect, useState } from "react"
import CustomTable from "./_customTable"
import { useRouter } from "next/router";
import Navbar from "@/components/navbar/navbar";



export default function beneficiery() {
    const router = useRouter();
    const [beneficiaries, setBeneficiaries] = useState([]);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    useEffect(() => {
        const token = localStorage.getItem('customer-token');
        if (!token) {
            router.push('/customer/login');
            setIsLoggedIn(false);
            return;
        }
        else {
            setIsLoggedIn(true);
        }
    }, [beneficiaries])
    return (
        <div>
            <Navbar login={isLoggedIn} />
            <div>
                <CustomTable beneficiaries={beneficiaries} setBeneficiaries={setBeneficiaries} />
            </div>
        </div>
    )
}
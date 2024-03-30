import { useState, useEffect } from "react";
import axios from "axios";
import jsPDF from 'jspdf';
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import { useRouter } from "next/router";


const StatementPage = () => {
    const [transactions, setTransactions] = useState([]);
    const [loading, setLoading] = useState(true);
    const router = useRouter();

    useEffect(() => {
        const fetchData = async () => {
            const token = localStorage.getItem('customer-token');
            if (!token) {
                router.push('/customer/login');
                return;
            }
            const headers = {
                'Authorization': `Bearer ${token}`
            }
            const username = localStorage.getItem('customer-username');
            try {
                const response = await axios.get(`/customer/transactions/${username}`, {
                    params: { duration: 'last6Months' },
                    headers
                });
                setTransactions(response.data);
                setLoading(false);
            } catch (error) {
                console.error("Error fetching transactions:", error);
                // Handle error
                setLoading(false);
            }
        };
        fetchData();
    }, []);

    const handleDurationChange = async (duration) => {
        setLoading(true);
        const token = localStorage.getItem('customer-token');
        if (!token) {
            router.push('/customer/login');
            return;
        }
        const headers = {
            'Authorization': `Bearer ${token}`
        }
        const username = localStorage.getItem('customer-username');
        try {
            const response = await axios.get(`/customer/transactions/${username}`, {
                params: { duration },
                headers
            });
            setTransactions(response.data);
            setLoading(false);
        } catch (error) {
            console.error("Error fetching transactions:", error);
            setLoading(false);
        }
    };

    const handleDownload = () => {
        const pdf = new jsPDF();
        let y = 10; 

        transactions.forEach(transaction => {
            pdf.text(`Transaction ID: ${transaction.transactionId}`, 10, y);
            pdf.text(`Amount: ${transaction.amount}`, 10, y + 10);
            

            y += 20; 
        });

        pdf.save('transaction_statement.pdf');
    };

    return (
        <div className="flex justify-center mt-40">
            <Card className="w-2/3">
                <CardHeader>
                    <CardTitle>Statement</CardTitle>
                    <CardDescription>
                        Download your transaction statement here.
                    </CardDescription>
                </CardHeader>
                <CardContent className="space-y-2">
                    <div className="flex justify-between items-center">
                        <div>
                            <button onClick={() => handleDurationChange('last6Months')} className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600">Last 6 Months</button>
                            <button onClick={() => handleDurationChange('lastMonth')} className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 ml-2">Last Month</button>
                            <button onClick={() => handleDurationChange('lastWeek')} className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 ml-2">Last Week</button>
                        </div>
                        <div>
                            <button onClick={handleDownload} className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600">Download PDF</button>
                        </div>
                    </div>
                    {loading ? (
                        <p>Loading...</p>
                    ) : (
                        <table className="w-full border-collapse border border-gray-500">
                            <thead>
                                <tr>
                                    <th className="border border-gray-500 px-4 py-2">Transaction ID</th>
                                    <th className="border border-gray-500 px-4 py-2">Amount</th>
                                    {/* Add more table headers if needed */}
                                </tr>
                            </thead>
                            <tbody>
                                {transactions.map(transaction => (
                                    <tr key={transaction.transactionId}>
                                        <td className="border border-gray-500 px-4 py-2">{transaction.transactionId}</td>
                                        <td className="border border-gray-500 px-4 py-2">{transaction.amount}</td>
                                        {/* Add more table data cells if needed */}
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </CardContent>
                <CardFooter></CardFooter>
            </Card>
        </div>
    );
}

export default StatementPage;
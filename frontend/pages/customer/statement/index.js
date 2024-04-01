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
        fetchData('last6Months');
    }, []);

    const fetchData = async (duration) => {
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
            // Handle error
            setLoading(false);
        }
    };

    const handleDurationChange = (duration) => {
        fetchData(duration);
    };

    const handleDownload = () => {
        const pdf = new jsPDF();
        let y = 10;
        let pageHeight = pdf.internal.pageSize.height;
        let currentPage = 1;
        const lineHeight = 10;

        transactions.forEach((transaction, index) => {
            pdf.text(`Transaction ID: ${transaction.refId}`, 10, y);
            pdf.text(`Amount: ${transaction.amount}`, 10, y + lineHeight);
            y += lineHeight * 2;

            if (y > pageHeight - 20 || index === transactions.length - 1) {
                pdf.addPage();
                y = 10;
                currentPage++;
            }
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
                            <button onClick={() => handleDurationChange('last6Months')} className="px-4 py-2 bg-black text-white rounded-md hover:bg-gray-800">Last 6 Months</button>
                            <button onClick={() => handleDurationChange('lastMonth')} className="px-4 py-2 bg-black text-white rounded-md hover:bg-gray-800 ml-2">Last Month</button>
                            <button onClick={() => handleDurationChange('lastWeek')} className="px-4 py-2 bg-black text-white rounded-md hover:bg-gray-800 ml-2">Last Week</button>
                        </div>
                        <div>
                            <button onClick={handleDownload} className="px-4 py-2 bg-black text-white rounded-md hover:bg-gray-800">Download PDF</button>
                        </div>
                    </div>
                    {loading ? (
                        <p>Loading...</p>
                    ) : (
                        <table className="w-full border-collapse border border-black">
                            <thead>
                                <tr>
                                    <th className="border border-black px-4 py-2">Transaction ID</th>
                                    <th className="border border-black px-4 py-2">Amount</th>
                                    {/* table headers */}
                                </tr>
                            </thead>
                            <tbody>
                                {transactions.map(transaction => (
                                    <tr key={transaction.refId}>
                                        <td className="border border-black px-4 py-2">{transaction.refId}</td>
                                        <td className="border border-black px-4 py-2">{transaction.amount}</td>
                                        {/* table data cells */}
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

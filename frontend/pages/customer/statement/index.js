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
import Navbar from "@/components/navbar/navbar";


const StatementPage = () => {
    const [transactions, setTransactions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const router = useRouter();

    useEffect(() => {
        fetchData('last6Months');
    }, []);

    const fetchData = async (duration) => {
        setLoading(true);
        const token = localStorage.getItem('customer-token');
        if (!token) {
            router.push('/customer/login');
            setIsLoggedIn(false);
            return;
        }
        setIsLoggedIn(true);
        const headers = {
            'Authorization': `Bearer ${token}`
        }
        const username = localStorage.getItem('customer-username');
        try {
            const response = await axios.get(`/customer/transactions/${username}`, {
                params: { duration },
                headers
            });
            console.log(response.data);
            setTransactions(response.data.reverse());
            // setTransactions(transactions.reverse())
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

        pdf.setFont("helvetica", "bold");
        pdf.text("Customer Details", 10, y);
        pdf.setFont("helvetica", "normal");
        pdf.text(`Customer Name: ${transactions[0].name}`, 10, y + lineHeight);
        pdf.text(`Account Number: ${transactions[0].accountTo}`, 10, y + lineHeight * 2);
        y += lineHeight * 5;

        pdf.text("Date", 10, y);
        pdf.text("Transaction Details", 50, y);
        pdf.text("Amount", 160, y);
        y += lineHeight * 2;

        transactions.forEach((transaction, index) => {
            const transactionDate = new Date(transaction.date).toLocaleDateString();
            const transactionDetails = transaction.amount < 0 ?
                `TRANSFER TO ${transaction.accountFrom} WITH REF: ${transaction.type}/${transaction.refId}` :
                `TRANSFER FROM ${transaction.accountFrom} WITH REF: ${transaction.type}/${transaction.refId}`;

            const lines = pdf.splitTextToSize(transactionDetails, 100);
            const linesCount = lines.length;
            const transactionDetailsHeight = linesCount * lineHeight;
            const transactionAmountHeight = lineHeight * 2;

            if (y + Math.max(transactionDetailsHeight, transactionAmountHeight) > pageHeight - 20) {
                pdf.addPage();
                y = 10;
                currentPage++;
            }

            pdf.text(transactionDate, 10, y);

            lines.forEach((line, lineIndex) => {
                pdf.text(line, 50, y + (lineIndex * lineHeight));
            });
            pdf.text(`${transaction.amount}`, 160, y);

            y += Math.max(transactionDetailsHeight, transactionAmountHeight) + lineHeight;

            if (y > pageHeight - 20 || index === transactions.length - 1) {
                pdf.addPage();
                y = 10;
                currentPage++;
            }
        });

        pdf.save('transaction_statement.pdf');



    };

    return (
        <div>
            <Navbar login={isLoggedIn} />
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
                                <button onClick={() => handleDurationChange('all')} className="px-4 py-2 bg-black text-white rounded-md hover:bg-gray-800 ml-2">View All</button>
                            </div>
                            <div>
                                <button onClick={handleDownload} className="px-4 py-2 bg-black text-white rounded-md hover:bg-gray-800">Download PDF</button>
                            </div>
                        </div>
                        {loading ? (
                            <p>Loading...</p>
                        ) : (
                            <table className="w-full table-fixed">
                                <thead>
                                    <tr className="bg-gray-200">
                                        <th className="px-4 py-2 w-1/5 text-left">Date</th>
                                        <th className="px-4 py-2 w-4/3 text-left">Transaction Details</th>
                                        <th className="px-4 py-2 w-1/4 text-right">Amount (in Rs.)</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {transactions.map(transaction => {
                                        if (transaction.amount < 0) {
                                            return (<tr key={transaction.refId} className="bg-white hover:bg-gray-100">
                                                <td className="px-4 py-2">{transaction.date} <p>{transaction.time}</p></td>
                                                <td className="px-4 py-2">
                                                    TRANSFER TO {transaction.accountFrom} WITH REF: {transaction.type}/{transaction.refId}
                                                </td>
                                                <td className="px-4 py-2 text-right text-red-500">{transaction.amount}</td>
                                            </tr>)
                                        }
                                        else {
                                            return (<tr key={transaction.refId} className="bg-white hover:bg-gray-100">
                                                <td className="px-4 py-2">{transaction.date} <p>{transaction.time}</p></td>
                                                <td className="px-4 py-2">TRANSFER FROM {transaction.accountFrom} WITH REF: {transaction.type}/{transaction.refId}</td>
                                                <td className="px-4 py-2 text-right text-green-500">{transaction.amount}</td>
                                            </tr>)
                                        }
                                    }
                                    )}
                                </tbody>
                            </table>
                        )}
                    </CardContent>
                    <CardFooter></CardFooter>
                </Card>
            </div>
        </div>
    );
}

export default StatementPage;

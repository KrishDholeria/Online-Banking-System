import { useState, useEffect } from "react";
import axios from "axios";
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
    const [pdfUrl, setPdfUrl] = useState("");
    const router = useRouter();

    useEffect(() => {
        // Fetch the PDF file from the backend
        const token = localStorage.getItem('customer-token');
        if (!token) {
            router.push('/customer/login');
            return;
        }
        const headers = {
            'Authorization': `Bearer ${token}`
        }
        const username = localStorage.getItem('customer-username');
        axios.get(`/customer/statement/${username}`, { responseType: "blob", headers })
            .then((response) => {
                // Convert the blob to URL
                const file = new Blob([response.data], { type: "application/pdf" });
                const fileURL = URL.createObjectURL(file);
                setPdfUrl(fileURL);
            })
            .catch((error) => {
                console.error("Error fetching PDF:", error);
                // Handle error
            });
    }, []);

    const handleDownload = () => {
        const link = document.createElement("a");
        link.href = pdfUrl;
        link.setAttribute("download", "statement.pdf");
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
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
                    {pdfUrl ? (
                        <div>
                            <embed src={pdfUrl} type="application/pdf" width="100%" height="600px" />
                            <div className="mt-7">
                                <button onClick={handleDownload} className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600">Download PDF</button>
                            </div>
                        </div>
                    ) : (
                        <p>Loading...</p>
                    )}
                </CardContent>
                <CardFooter></CardFooter>
            </Card>
        </div>
    );
}

export default StatementPage;

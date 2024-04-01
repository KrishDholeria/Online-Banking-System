
import { useState } from "react";
import axios from "axios";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";

const DownloadStatement = () => {
    const [pdfUrl, setPdfUrl] = useState(null); // Initialize pdfUrl state to null

    const handleDownload = (duration) => {
        axios.get(`/customer/statement/${duration}`, { responseType: "blob" })
            .then((response) => {
                const file = new Blob([response.data], { type: "application/pdf" });
                const fileURL = URL.createObjectURL(file);
                setPdfUrl(fileURL);
                const link = document.createElement("a");
                link.href = fileURL;
                link.setAttribute("download", `statement_${duration}.pdf`);
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            })
            .catch((error) => {
                console.error("Error fetching PDF:", error);
                // Handle error
            });
    };

    return (
        <div className="flex justify-center mt-40">
            <Card className="w-2/3">
                <CardHeader>
                    <CardTitle>Statement</CardTitle>
                    <CardDescription>
                        Choose the duration for downloading your transaction statement.
                    </CardDescription>
                </CardHeader>
                <CardContent className="space-y-2">
                    <div>
                    <button onClick={() => handleDownload("last6months")} className="px-4 py-2 bg-gray-800 text-white rounded-md hover:bg-gray-900">Last 6 Months</button>
                        <button onClick={() => handleDownload("lastmonth")} className="px-4 py-2 bg-gray-800 text-white rounded-md hover:bg-gray-900 ml-4">Last Month</button>
                        <button onClick={() => handleDownload("lastweek")} className="px-4 py-2 bg-gray-800 text-white rounded-md hover:bg-gray-900 ml-4">Last Week</button>
                    </div>
                    {pdfUrl !== null && ( // Render embed only when pdfUrl is not null
                        <div>
                            <embed src={pdfUrl} type="application/pdf" width="100%" height="600px" />
                        </div>
                    )}
                </CardContent>
            </Card>
        </div>
    );
}

export default DownloadStatement;





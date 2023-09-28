import { useEffect, useState } from "react";

function useServerStatus() {
    const [status, setStatus] = useState("...");

    useEffect(() => {
        const interval = setInterval(async () => {
            const response = await fetch("/actuator/health");
            const data = await response.json();
            setStatus(data.status);
        }, 1000)
        return () => clearInterval(interval)
    }, [])

    return status;
}

export function ServerStatus() {
    const status = useServerStatus();
    return <p>Backend server is {status}</p>
}

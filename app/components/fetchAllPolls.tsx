import { useState, useEffect } from "react";

type Poll = {
    id: number,
    question: string,
    createdAt: string,
    validUntil: string,
    voteOptionList: string[]
}

// @ts-ignore
export default function fetchAllPolls({userId}) {
    const [polls, setPolls] = useState<Poll[]>([]);
    async function get() {
        try {
            const res = await fetch("http://localhost:8080/api/getPolls");
            if (res.ok) {
                const val: Poll[] = await res.json();
                setPolls(val);
                console.log(val);
            }
        } catch (e) {
            console.log(e);
        }
        console.log(polls);
    }

    useEffect(() => {
        get();
    }, []);

    useEffect(() => {
        console.log("polls:", polls);
    }, [polls]);

    return (
        <ul className="space-y-4">
            {polls.length === 0 && <li>No polls yet</li>}
            {polls.map((poll) => (
                <li key={poll.id} className="border rounded p-3 bg-white/60">
                    <div className="font-semibold mb-2">{poll.question}</div>
                    <ul className="list-disc ml-6 space-y-1">
                        {poll.voteOptionList &&
                            poll.voteOptionList.map((option, index) => (
                                <li key={`${poll.id}${index}`}>{option}</li>
                            ))
                        }</ul>
                </li>
            ))}
        </ul>
    );
}

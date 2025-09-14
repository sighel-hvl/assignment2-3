import { useState } from "react";

type PollProps = { userId: number };

export default function CreatePoll({ userId }: PollProps) {
    const [question, setQuestion] = useState<string>("");
    const [activeHours, setActiveHours] = useState<number>(0);
    const [options, setOptions] = useState<string[]>(["", ""]);

    function updateOption(index: number, value: string) {
        setOptions((prev) => prev.map((o, i) => (i === index ? value : o)));
    }

    function addOption() {
        setOptions((prev) => [...prev, ""]);
    }

    function removeOption(index: number) {
        setOptions((prev) => prev.filter((_, i) => i !== index));
    }

    async function submitPoll() {
        try {
            const voteOptions = options;
            const validUntil = activeHours;

            const res = await fetch("http://localhost:8080/api/createPoll", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    question,
                    validUntil,
                    userId,
                    voteOptions,
                }),
            });

            if (!res.ok) {
                const text = await res.text();
                console.log(text);
            }

            setQuestion("");
            setActiveHours(0);
            setOptions(["", ""]);
        } catch (e) {
            console.error(e);
        }
    }

    return (
        <>
            <div className={"flex flex-col justify-center items-center"}>
                <p className={"font-[futura] mb-1"}>Question</p>
                <input
                    className={"border rounded-xl p-2 mb-1"}
                    type="text"
                    placeholder="Question"
                    value={question}
                    onChange={(e) => setQuestion(e.target.value)}
                />

                <p className={"font-[futura] mb-1"}>Active hours(from now)</p>
                <input
                    className={"border rounded-xl p-2 mb-2"}
                    type="number"
                    placeholder="Hours"
                    min={1}
                    onChange={(e) => setActiveHours(parseInt(e.target.value))}
                />

                <p className={"font-[futura] mb-1"}>Vote options</p>
                <div className={"w-full flex flex-col gap-2 mb-2"}>
                    {options.map((opt, count) => (
                        <div className={"flex items-center gap-2"}>
                            <input
                                className={"border rounded-xl p-2 flex-1"}
                                type="text"
                                placeholder={"option"}
                                value={opt}
                                onChange={(e) => updateOption(count, e.target.value)}
                            />
                            {options.length > 1 && (
                                <button
                                    type="button"
                                    className={"px-1 py-0.5 rounded bg-[#D1A980] text-white"}
                                    onClick={() => removeOption(count)}
                                >
                                    Remove
                                </button>
                            )}
                        </div>
                    ))}
                    <button
                        type="button"
                        className={"px-3 py-2 rounded bg-[#748873] text-white"}
                        onClick={addOption}
                    >
                        Add option
                    </button>
                    <button className={"bg-[#748873] text-white rounded px-4 py-2"} onClick={submitPoll}>
                        Submit
                    </button>
                </div>


            </div>
        </>
    );
}
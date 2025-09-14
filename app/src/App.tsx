import './App.css'
import { useState } from 'react';
import { create } from "../components/CreateUser.tsx";
import CreatePoll from "../components/CreatePoll.tsx";
import GetPolls from "../components/fetchAllPolls.tsx";

function App() {

    const [username, setUsername] = useState<String>("");
    const [email, setEmail] = useState<String>("");
    const [loggedIn, setLoggedIn] = useState<Boolean>(false);
    const [createMessage, setCreateMessage] = useState<String>("");
    const [userMessage, setUserMessage] = useState<String>("");
    const [userId, setUserId] = useState<Number>();

    async function handleUser(){
        if(username.length > 0 && email.length > 0){
            const res = await create(username, email);

            setUsername("");
            setEmail("");
            if(res){
                setLoggedIn(true);
            }else{
                setLoggedIn(false);
            }
            setCreateMessage("")
        }
       else(
           setCreateMessage("Username or email cannot be empty")
        )
    }

    async function handleLogin(){
        try{
            const res = await fetch(`http://localhost:8080/api/getUser/${email}`);
            setEmail("");
            if (res.status === 404) {
                setLoggedIn(false);
                setUserMessage("User not found!");
                return;
            }
            const val = await res.text();
            if(!val){
                setLoggedIn(false);
                setUserMessage("User not found!");
                console.log(val)
                return;
            }
            const user = JSON.parse(val);
            if(user){
                setUserId(user.id);
                setLoggedIn(true);
                setUserMessage("");
            }
        }catch (e){
            console.log(e);
            setLoggedIn(false);
        }
    }

    return (
    <div className={"w-full h-screen p-0 m-0 flex flex-col items-center bg-[#F8F8F8]"}>
        <div className={"flex justify-between items-center bg-[#E5E0D8] w-[98%] my-4 h-16 rounded-xl shadow-md"}>
            <p className={"text-2xl font-[futura] font-bold text-[#748873] ml-4"}>Funky poll's</p>
            {loggedIn &&
            <button className={"mr-4 font-[futura]"} onClick={() => setLoggedIn(false)}>Log out</button>
            }
        </div>
        {!loggedIn &&
            <div className={"flex flex-row bg-[#E5E0D8] h-full w-[98%] rounded-xl mb-4 p-6"}>
                <div className={"w-1/2 pr-6 flex flex-col items-center justify-center"}>
                    <h2 className={"text-xl font-[futura] mb-4 text-[#374151]"}>Create user</h2>
                    <div className={"flex flex-col justify-center items-center w-full max-w-md"}>
                        <input
                            className={"border rounded px-3 py-2 mb-2 w-full focus:ring-[#748873]"}
                            type="text"
                            placeholder="Username"
                            onChange={(e) => setUsername(e.target.value)}
                        />
                        <input
                            className={"border rounded px-3 py-2 mb-2 w-full"}
                            type="email"
                            placeholder="Email"
                            onChange={(e) => setEmail(e.target.value)}
                        />
                        <button className={"bg-[#748873] text-white rounded px-4 py-2 w-full"} onClick={handleUser}>
                            Create user
                        </button>
                        {createMessage.length > 0 &&
                            <p className={"text-red-900 mt-2"}>{createMessage}</p>
                        }
                    </div>
                </div>
                <div className={"w-px bg-[#C9C3BA]"} />
                <div className={"w-1/2 pl-6 flex flex-col items-center justify-center"}>
                    <h2 className={"text-xl font-[futura] mb-4 text-[#374151]"}>Log in</h2>
                    <div className={"flex flex-col justify-center items-center w-full max-w-md"}>
                        <input
                            className={"border rounded px-3 py-2 mb-2 w-full"}
                            type="email"
                            placeholder="Email"
                            onChange={(e) => setEmail(e.target.value)}
                        />
                        <button
                            className={"bg-[#748873] text-white rounded px-4 py-2 w-full"}
                            onClick={handleLogin}
                        >
                            Log in
                        </button>
                        {userMessage.length > 0 &&
                            <p className={"text-red-900 mt-2"}>{userMessage}</p>
                        }
                    </div>
                </div>
            </div>
        }
        {loggedIn && (
            <div className={"flex flex-row bg-[#E5E0D8] h-full w-[98%] rounded-xl mb-4 p-6"}>
                <div className={"w-1/2 pr-6 flex flex-col items-center justify-center"}>
                        <CreatePoll userId={Number(userId)} />
                </div>
                <div className={"w-px bg-[#C9C3BA]"} />
                <div className={"w-1/2 pl-6 flex flex-col items-center justify-center"}>
                    <p className={"font-[futura] font-bold text-xl"}>Created polls!</p>
                    <GetPolls userId={userId}></GetPolls>
                </div>
            </div>
        )}

    </div>
  )
}

export default App

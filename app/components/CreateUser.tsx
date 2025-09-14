export async function create(username: String, email: String){
    try{
        const res = await fetch("http://localhost:8080/api/createUser", {
            method: "POST",
            headers: {"Content-type": "application/json"},
            body: JSON.stringify({username, email})
        })
        if(res.ok){
            const user = await res.json();
            console.log(user)
            return true;
        }
    }catch (e) {
        console.log(e)
        return false
    }
}
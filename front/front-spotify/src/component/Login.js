import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function LoginPage(props) {

    const [username, setUsername] = useState([]);
    const [password, setPassword] = useState([]);
    const [message, setMessage] = useState("");
    const [requestResponse, setRequestResponse] = useState({})
    let navigate = useNavigate()

    const LOGIN_URL = "http://localhost:8000/login"

    const login = () => {
        return fetch(LOGIN_URL, {
            method: "POST",
            body: JSON.stringify({
                "username":username,
                "password":password
            }),
            headers: new Headers({
                'Content-Type': 'application/json'
            })
        })  .then((response) => response.json())
            .then((data) => setRequestResponse(data));
    }

    useEffect(
        (() => {
            console.log(requestResponse)
            if(requestResponse.jwt == null) {
                setMessage(requestResponse.error)
            } else {
                setMessage("Success!")
                //props.setUser(requestResponse)
                localStorage.setItem("user", JSON.stringify(requestResponse))
                navigate('/userdata')
            }
        }),
        [requestResponse]
    )

    return (
        <div>
            <p>{message}</p>
            <form>
                <label>Enter your username:   </label>
                <input
                    type="text" 
                    value={username}
                    placeholder="username"
                    onChange={(e) => setUsername(e.target.value)}
                />
                <br/>
                <label>Enter your password:   </label>
                <input
                    type="password" 
                    value={password}
                    placeholder="*****"
                    onChange={(e) => setPassword(e.target.value)}
                />
                <br/>
            </form>
            <button onClick={() => {login()}}>Submit</button>
        </div>
    );
}

export default LoginPage;
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

function UserHomePage() {

    const [data, setData] = useState([])

    let navigate = useNavigate()
    const user = JSON.parse(localStorage.getItem("user"))

    let USER_URL = "http://localhost:8000/users"

    const checkUserOnInit = () => {
        console.log("Init")
        console.log(user)

        if(user == null) {
            navigate("/login")
            return
        }
    }

    const fetchData = () => {
        console.log(user)
        let url = USER_URL + "/" + user.id;

        console.log(url)
        return fetch(url, {
                method: "GET",
                headers: new Headers({
                    'Authorization': 'Bearer ' + user.jwt
                })})
            .then((response) => response.json())
            .then((data) => setData(data));
    }

    const onInit = function() {
        checkUserOnInit()
        fetchData()
    }

    useEffect(
        onInit, // <- function that will run on every dependency update
        [] // <-- empty dependency array
    )

    return (
        <div>
            <div>
                <p>Name:{data.username}</p>
                <p>Roles:</p>
                <ul>
                    <li>ROLE_ADMIN</li>
                </ul>
            </div>
            <div>
                <a href="/userdata">See personal data</a>
                <br/>
                <a href="/playlists">See playlists</a>
                <br/>
                <a href="/songs">See songs</a>
                <br/>
                <a href="/artists">See artists</a>
                <br/>
            </div>
        </div>
    );
}
export default UserHomePage;
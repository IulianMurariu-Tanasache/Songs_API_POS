import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import ComponentPage from "../ComponentPage";
import PlaylistsList from "../playlists/PlaylistsList";
import SongList from "../songs/SongsList";
 
function OwnUserDataPage(props) {
    const [data, setData] = useState({})
    const [toDisplay, setToDisplay] = useState(0)

    let navigate = useNavigate()
    const user = JSON.parse(localStorage.getItem("user"))

    const fetchData = () => {
        let url = props.api_url + "/" + user.id;

        console.log(url)
        return fetch(url, {
            method: "GET",
            headers: new Headers({
                'Authorization': 'Bearer ' + user.jwt
            })})
            .then((response) => response.json())
            .then((data) => setData(data));
    }

    const checkUserOnInit = () => {
        console.log("Init")
        console.log(user)

        if(user == null) {
            navigate("/login")
            return
        }
    }

    // Close the dropdown menu if the user clicks outside of it
    const onInit = function() {
        checkUserOnInit()
        fetchData()
    }
    const onComponentDisplay = (component) => {
        if(component == toDisplay) {
            setToDisplay(0)
        } else {
            setToDisplay(component)
        }
    }

    useEffect(
        onInit, // <- function that will run on every dependency update
        [] // <-- empty dependency array
    )

    return (
        <div className="wrap">
            <div id="user-details-panel" className="panel full-page">
                <p>User id: {data.userId}</p>
                <p>Username: {data.username}</p>
                <p>Mail: {data.email}</p>
                <p>First Name: {data.firstName}</p>
                <p>Last Name: {data.lastName}</p>
                <p>Birth Date: {data.birthDate}</p><br/>
                <span className="button" onClick={() => {onComponentDisplay(1)}}>See own playlists</span>
                <span className="button" onClick={() => {onComponentDisplay(2)}}>See own songs</span>
            </div>
            <div>
                { toDisplay == 1 && <ComponentPage
                            api_url="http://localhost:8000/api/playlists"
                            name="Playlists" 
                            filters={["name"]}
                            queryParam={{
                                key:"userId",
                                value:user.id
                            }}
                            listComponent={(data) => <PlaylistsList playlists={data}/>}/>
                }
                { toDisplay == 2 && <ComponentPage //TODO: query songs of artist here
                            api_url="http://localhost:8000/api/songcollection/songs"
                            name="Songs" 
                            filters={["name","genre","type","releaseYear"]}
                            // queryParam={{
                            //     key:"userId",
                            //     value:props.userId + ""
                            // }}
                            listComponent={(data) => <SongList songs={data}/>}/>
                }
            </div>
        </div>
    )

}

export default OwnUserDataPage; 
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function PlaylistDetails(props) {

    const [playlistData, setPlaylistData] = useState(null)
    const [songs, setSongs] = useState([])
    const [canShow, setCanShow] = useState(false)

    let navigate = useNavigate()
    const user = JSON.parse(localStorage.getItem("user"))
    const PLAYLIST_URL = "http://localhost:8000/api/playlists"

    const checkUserOnInit = () => {
        console.log("Init")
        console.log(user)

        if(user == null) {
            navigate("/login")
            return
        }
    }

    const fecthPlaylistData = () => {
        let url = PLAYLIST_URL + "/" + props.playlistId;

        console.log(url)
        return fetch(url, {
                method: "GET",
                headers: new Headers({
                    'Authorization': 'Bearer ' + user.jwt
                })})
            .then((response) => response.json())
            .then((data) => setPlaylistData(data));
    }

    const onInit = () => {
        checkUserOnInit()
        fecthPlaylistData()
    }

    useEffect(
        onInit,
        []
    )

    useEffect(
        (() => {
            if(playlistData != null) 
                setCanShow(true); 
            else 
                setCanShow(false);}),
        [playlistData]
    )

    return (
        <div>
        {   canShow &&
            <div>
                <h3>{playlistData.name}</h3>
                <p>id: {playlistData.id}</p>
                <p>user: {playlistData.userId}</p>
                <p className='remove-break'>songs:</p>
                {
                    playlistData.songs.map((song) => 
                    <li className="inner-list" key={song.id}>
                        <h4 className="remove-break">{song.name}</h4>
                    </li>
                    )
                }
            </div>
        }
        </div>
    )
}

export default PlaylistDetails;
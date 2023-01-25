import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SongsList from "../songs/SongsList";

function ArtistDetails(props) {

    const [artistData, setArtistData] = useState(null)
    const [songs, setSongs] = useState([])
    const [canShow, setCanShow] = useState(false)

    let navigate = useNavigate()
    const user = JSON.parse(localStorage.getItem("user"))
    const ARTIST_URL = "http://localhost:8000/api/songcollection/artists"

    const checkUserOnInit = () => {
        console.log("Init")
        console.log(user)

        if(user == null) {
            navigate("/login")
            return
        }
    }

    const fetchArtistData = () => {
        let url = ARTIST_URL + "/" + props.artistId;

        console.log(url)
        return fetch(url, {
                method: "GET",
                headers: new Headers({
                    'Authorization': 'Bearer ' + user.jwt
                })})
            .then((response) => response.json())
            .then((data) => setArtistData(data));
    }

    const fetchArtistsSongs = () => {
        let url = ARTIST_URL + "/" + props.artistId + "/songs"

        console.log(url)
        return fetch(url, {
                method: "GET",
                headers: new Headers({
                    'Authorization': 'Bearer ' + user.jwt
                })})
            .then((response) => response.json())
            .then((data) => setSongs(data));
    }

    const onInit = () => {
        checkUserOnInit()
        fetchArtistData()
        fetchArtistsSongs()
    }

    useEffect(
        onInit,
        []
    )

    useEffect(
        (() => {
            if(artistData != null) 
                setCanShow(true); 
            else 
                setCanShow(false);}),
        [artistData]
    )

    return (
        <div>
        {   canShow &&
            <div>
                <h3>{artistData.name}</h3>
                <p>uuid: {artistData.uuid}</p>
                <p>songs: </p>
                {<SongsList songs={songs}/>}
            </div>
        }
        </div>
    )
}

export default ArtistDetails;
import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";

function SongsForm(props) {
    let navigate = useNavigate()
    
  const [formData, setFormData] = useState({
    name: '',
    genre: '',
    type: '',
    releaseYear: '',
    artistsUUID: []
  });

  const [message, setMessage] = useState("");

  const handleChange = event => {
    setFormData({ ...formData, [event.target.name]: (event.target.name === "artistsUUID" ? event.target.value.split(",") : event.target.value)
    });
  };

  const user = JSON.parse(localStorage.getItem("user"));

    const checkUserOnInit = () => {
        console.log("Init")
        console.log(user)

        if(user == null) {
            navigate("/login")
            return
        }
    }

    const onInit = () => {
        checkUserOnInit()
    }

    useEffect(
        onInit,
        []
    )

  const postSongData = () => {
    let url = props.url;

    console.log(url)
    return fetch(url, {
            method: "POST",
            headers: new Headers({
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + user.jwt
            }),
            body: JSON.stringify(formData)
        })
        .then((response) => {
            setMessage("Status Code: " + response.status);
            if(response.status == 201)
              navigate(-1)
        })
    
}

  const handleSubmit = (e) => {
    e.preventDefault();
    postSongData();
    console.log(formData);
  }

  return (
    <form onSubmit={handleSubmit}>
      <label>
        Name:
        <input type="text" name="name" onChange={handleChange} value={formData.Name} />
      </label>
      <br />
      <label>
        Song Genre:
        <input type="text" name="genre" onChange={handleChange} value={formData.genre} />
      </label>
      <br />
      <label>
        Music Type:
        <input type="text" name="type" onChange={handleChange} value={formData.type} />
      </label>
      <br />
      <label>
        Release Year:
        <input type="text" name="releaseYear" onChange={handleChange} value={formData.releaseYear} />
      </label>
      <br />
      <label>
        ArtistsUUID
        <input type="text" name="artistsUUID" onChange={handleChange} value={formData.artistsUUID} />
      </label>
      <br />
      <button type="submit">Submit</button>
    </form>
  );
}

export default SongsForm;

import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";

function ArtistForm(props) {
    let navigate = useNavigate()
    
  const [formData, setFormData] = useState({
    name: '',
    active: false
  });

  const [message, setMessage] = useState("");

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
    });
  }

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

  const postArtistData = () => {
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
    postArtistData();
    console.log(formData);
  }

  return (
    <form onSubmit={handleSubmit}>
      <p>{message}</p>
      <label>
        Name:
        <input
          type="text"
          name="name"
          value={formData.name}
          onChange={handleChange}
        />
      </label>
      <br />
      <label>
        Active:
        <input
          type="checkbox"
          name="active"
          checked={formData.active}
          onChange={handleChange}
        />
      </label>
      <br />
      <button type="submit">Submit</button>
    </form>
  );
}

export default ArtistForm;

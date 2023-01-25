import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function Logout() {
  const [data, setData] = useState(null);
  const [error, setError] = useState(null);
  let navigate = useNavigate()

  const logoutClick = () => {
    let url = "http://localhost:8000/users/logout";
    const user = JSON.parse(localStorage.getItem("user"))

    console.log(url)
    return fetch(url, {
            method: "GET",
            headers: new Headers({
                'Authorization': 'Bearer ' + user.jwt
            })})
        .then((response) => {
            if(response.status == 204){
                localStorage.setItem("user", "")
                navigate("/login")
            }
        })
  }

  return (
    <div>
      <button onClick={() => logoutClick()}>Logout</button>
    </div>
  );
}

export default Logout;

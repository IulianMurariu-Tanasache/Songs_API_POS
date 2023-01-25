import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import '../App.css';

function ComponentPage(props) {

    const [page, setPage] = useState(0)
    const [data, setData] = useState([])
    const [filter, setFilter] = useState("");
    const [searchText, setSearchText] = useState("");

    let navigate = useNavigate()
    const user = JSON.parse(localStorage.getItem("user"))

    const capitalizeFirstLetter = (string) => {
        return string.charAt(0).toUpperCase() + string.slice(1);
      }

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
        let url = props.api_url + "?page=" + page + "&items_per_page=10";
        if(props.queryParam != null)
            url = url + "&" + props.queryParam.key + "=" + props.queryParam.value
        if(filter !== "") {
            url = url + "&" + filter + "=" + searchText
        }

        console.log(url)
        return fetch(url, {
                method: "GET",
                headers: new Headers({
                    'Authorization': 'Bearer ' + user.jwt
                })})
            .then((response) => response.json())
            .then((data) => setData(data));
    }

    /* When the user clicks on the button,
    toggle between hiding and showing the dropdown content */
    const onDropdownClick = function() {
      document.getElementById("myDropdown").classList.toggle("show");
    }

    // Close the dropdown menu if the user clicks outside of it
    const onInit = function() {
        checkUserOnInit()
        window.onclick = function(event) {
            if (!event.target.matches('#filterbtn')) {
                var dropdowns = document.getElementsByClassName("dropdown-content");
                var i;
                for (i = 0; i < dropdowns.length; i++) {
                var openDropdown = dropdowns[i];
                if (openDropdown.classList.contains('show')) {
                    openDropdown.classList.remove('show');
                }
                }
            }
        }
        document.getElementById("filterbtn").innerText = "Just get them all"
    }

    const onFilterSelect = function(filterSelected, filterButtonText) {
        setFilter(filterSelected)
        document.getElementById("filterbtn").innerText = filterButtonText
    }

    const onPageChange = () => {
        if(page < 0) {
            setPage(0)
            return
        }
        if(data.length == 0 && page > 0) {
            setPage(0)
            return
        }
        fetchData()
    }

    useEffect(
        onInit, // <- function that will run on every dependency update
        [] // <-- empty dependency array
    )

    useEffect(
        onPageChange,
        [page]
    )

    return (
        <div className="panel full-page list-panel">
            <div className="panel top-panel">
                <p>{props.name}</p>
                <label>Search:</label>
                <input
                    id="searchBar"
                    type="text" 
                    value={searchText}
                    onChange={(e) => setSearchText(e.target.value)}
                />
                <label>Filter by:</label>
                <div className="dropdown">
                    <span onClick={onDropdownClick} id="filterbtn" className="button">Placeholder</span>
                    <div id="myDropdown" className="dropdown-content">
                        <a href="#" onClick={(e) => onFilterSelect("", e.target.innerText)}>Just get them all</a>
                        {
                            props.filters.map(f => (
                                <a key={f} href="#" onClick={(e) => onFilterSelect(f, e.target.innerText)}>{capitalizeFirstLetter(f)}</a>
                            ))
                        }
                    </div>
                </div>
                <span className="button" onClick={fetchData}>Submit</span>
            </div>
            {props.listComponent(data)}
            <br/>
            <div className="bottom">
                <span className="button" onClick={() => navigate(props.add_url)}>Add</span>
                <span className="button" onClick={() => setPage(page - 1)}>prevPage</span>
                <span>{page}</span>
                <span className="button" onClick={() => setPage(page + 1)}>nextPage</span>
            </div>
            
        </div>
    );
}

export default ComponentPage;
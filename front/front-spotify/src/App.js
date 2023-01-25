import './App.css';
import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import LoginPage from './component/Login';
import UserHomePage from './component/UserHomePage';
import SongsList from './component/songs/SongsList';
import ComponentPage from './component/ComponentPage';
import ArtistsList from './component/artists/ArtistsList';
import PlaylistsList from './component/playlists/PlaylistsList';
import OwnUserDataPage from './component/userdata/OwnUserDataPage';
import ArtistForm from './component/artists/ArtistForm';
import Logout from './component/Logout';
import SongsForm from './component/songs/SongsForm';

function App() {
  return (
    <div>
      
        <Router>
        <Logout/>
          <Routes>
              <Route path='/' element={<UserHomePage/>} />
              <Route path='/login' element={<LoginPage/>} />

              <Route path='/songs' element={<ComponentPage
                                                  api_url="http://localhost:8000/api/songcollection/songs"
                                                  name="Songs" 
                                                  filters={["name","genre","type","releaseYear"]}
                                                  listComponent={(data) => <SongsList songs={data}/>}
                                                  add_url="/add-song"
                                                />}/>
              <Route path='/add-song' element={<SongsForm url="http://localhost:8002/api/songcollection/songs"/>} />

              <Route path='/artists' element={<ComponentPage
                                                  api_url="http://localhost:8000/api/songcollection/artists"
                                                  name="Artists" 
                                                  filters={["name"]}
                                                  listComponent={(data) => <ArtistsList artists={data}/>}
                                                  add_url="/add-artist"
                                                />}/>
              <Route path='/add-artist' element={<ArtistForm url="http://localhost:8000/api/songcollection/artists"/>} />

              <Route path='/playlists' element={<ComponentPage
                                                  api_url="http://localhost:8000/api/playlists"
                                                  name="Playlists" 
                                                  filters={["name","userId"]}
                                                  listComponent={(data) => <PlaylistsList playlists={data}/>}
                                                  add_url="/add-playlist"
                                                />}/>
              <Route path='/userdata' element={<OwnUserDataPage 
                                                  api_url="http://localhost:8000/api/userdata"
                                              />} />
          </Routes>
        </Router>
    </div>
  );
}

export default App;

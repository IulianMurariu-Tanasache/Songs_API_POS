import React, { useState } from 'react';
import PlaylistDetails from './PlaylistDetails';

function PlaylistsList({ playlists }){

  const [selectedPlaylist, setSelectedPlaylist] = useState("")

  return(
    <div className="wrap">
        <div className="panel">
          <ul>
            {playlists.map((playlist) => (
              <li key={playlist.id}>
                <h3 className='clickable' onClick={() => setSelectedPlaylist(playlist.id)}>{playlist.name}</h3>
                <span className="spaced-text">of user with id: {playlist.userId}</span>
                <span className="spaced-text">songs: {(() => {if(playlist.songs == null) return "0"; return "" + playlist.songs.length;})()}</span>
              </li>
            ))}
          </ul>
        </div>
        {
          selectedPlaylist !== "" &&
          <div key={selectedPlaylist} className="panel details-panel">
            <span className="button" onClick={() => {setSelectedPlaylist("")}}>Close</span>
            <PlaylistDetails playlistId={selectedPlaylist} />
          </div>
        }
      </div>
  );
}

export default PlaylistsList;

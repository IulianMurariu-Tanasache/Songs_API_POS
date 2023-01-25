import React, { useState } from 'react';
import ArtistDetails from './../artists/ArtistDetails';

function SongList({ songs }){

  const [artistSelected, setArtistSelected] = useState("")

  return (
    <div className="wrap">
      <div className="panel">
        <ul>
          {songs.map((song) => (
            <li key={song.id}>
              <h3>{song.name}</h3>
              <span className="spaced-text">Genre: {song.genre}</span>
              <span className="spaced-text">Type: {song.type}</span>
              <span className="spaced-text">Release Year: {song.releaseYear}</span><br/>
              <p className='remove-break'>by:</p>
              {
                song.artistSet.map((artist) => (
                  <li className='inner-list' key={artist.uuid}>
                    <h4 className='remove-break clickable' onClick={() => setArtistSelected(artist.uuid)}>{artist.name}</h4>
                  </li>
                ))
              }
            </li>
          ))}
        </ul>
      </div>
      {
        artistSelected !== "" &&
        <div key={artistSelected} className="panel details-panel">
          <span className="button" onClick={() => {setArtistSelected("")}}>Close</span>
          <ArtistDetails artistId={artistSelected} />
        </div>
      }
    </div>
  );
}

export default SongList;

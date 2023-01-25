import React, { useState } from 'react';
import ArtistDetails from './ArtistDetails';

function ArtistsList({ artists }) {

  const [artistSelected, setArtistSelected] = useState("")

  console.log(artists);
  return(
    <div className="wrap">
      <div className="panel">
        <ul>
          {artists.map((artist) => (
            <li className="item" key={artist.uuid}>
              <span className="title">{artist.name}</span>
              <span className="button" onClick={() => setArtistSelected(artist.uuid)}>See details</span>
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

export default ArtistsList;

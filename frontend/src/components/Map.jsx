import map from './Map.module.css'

function Map() {
  return (
    <div className={map.contentcontainer}>
      <h1 className={map.header}>Find a Carolina Wings Near You</h1>
      <iframe src="https://www.google.com/maps/d/u/0/embed?mid=1EcYrbRyMPbJPBtHQY_QZfRjLcrQvIIU&ehbc=2E312F&noprof=1" className={map.map}></iframe>
    </div>
  );
}

export default Map;
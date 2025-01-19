import locations from './Locations.module.css';
import Card from './Card.tsx'


function Locations() {
    return (
    <>
      <div className={locations.contentcontainer}>
        <div className={locations.textcontainer}>
          <h1 className={locations.headers}>Choose Your Location</h1>
          <p className={locations.text}>Trying to find the best wings and ribs near you? Carolina Wings and Rib House has four locations ready for any occasion!</p>
          <h1 className={locations.headers}>Click your desired location and start your order now! We're waiting for you with a smile.</h1>
        </div>
        <div className={locations.cardcontainer}>
          <Card location="Red Bank" />
          <Card location="Cayce" />
        </div>
      </div>
    </>

    )
}


export default Locations;

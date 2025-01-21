import locations from './Locations.module.css';
import Card from './Card.tsx'

function Locations() {
    return (
    <>
      <div className={locations.contentcontainer} id="locations">
        <div className={locations.textcontainer}>
          <h1 className={locations.headers}>Choose Your Location</h1>
          <p className={locations.text}>Trying to find the best wings and ribs near you? Carolina Wings and Rib House has four locations ready for any occasion!</p>
          <h1 className={locations.headers}>Click your desired location and start your order now! We're waiting for you with a smile.</h1>
        </div>
        <div className={locations.cardcontainer}>
          <Card location="Red Bank" backgroundImage='redbank.png' cover={'cover'} position={'center'}/>
          <Card location="Cayce" backgroundImage='cayce.png' cover={'cover'} position={'center'}/>
          <Card location="Ballentine" backgroundImage='ballentine.png'/>
          <Card location="Lexington" backgroundImage='lexington.png' />
        </div>
      </div>
    </>

    )
}


export default Locations;

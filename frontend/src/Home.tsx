import './Home.css'
import NavBar from './components/NavBar.tsx'
import GoodEats from './components/GoodEats.tsx'
import Locations from './components/Locations.tsx'

function Home() {
  return (
  <>
    <NavBar /> 
    <GoodEats />
    <Locations />
  </>
  );
}

export default Home;

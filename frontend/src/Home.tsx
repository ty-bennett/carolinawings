import './Home.css'
import NavBar from './components/NavBar.tsx'
import GoodEats from './components/GoodEats.tsx'
import Locations from './components/Locations.tsx'
import About from './components/About.tsx'

function Home() {
  return (
  <>
    <NavBar /> 
    <GoodEats />
    <Locations />
    <About />
  </>
  );
}

export default Home;

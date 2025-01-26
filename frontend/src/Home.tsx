import './Home.css'
import NavBar from './components/NavBar.tsx'
import GoodEats from './components/GoodEats.tsx'
import Locations from './components/Locations.tsx'
import About from './components/About.tsx'
import Sauces from './components/Sauces.tsx'
import GiftCard from './components/GiftCard.tsx'
import Map from './components/Map.tsx'
import Footer from './components/Footer.tsx'

function Home() {
  return (
  <>
    <NavBar /> 
    <GoodEats />
    <Locations />
    <About />
    <Sauces />
    <GiftCard />
    <Map />
    <Footer />
  </>
  );
}

export default Home;

import './Home.css'
import NavBar from '../components/NavBar.jsx'
import GoodEats from '../components/GoodEats.jsx'
import Locations from '../components/Locations.jsx'
import About from '../components/About.jsx'
import Sauces from '../components/Sauces.jsx'
import GiftCard from '../components/GiftCard.jsx'
import Map from '../components/Map.jsx'
import Footer from '../components/Footer.jsx'

const Home = () => {
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

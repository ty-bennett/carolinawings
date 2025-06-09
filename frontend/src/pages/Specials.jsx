import NavBar from "../components/NavBar.jsx";
import catering from "../components/Specials.module.css";
import FoodCarousel from "../components/FoodCarousel.jsx";


const Specials = () => {
  return (
    <>
      <main>
        <NavBar />
        <div className={catering.headercontainer}>
          <h1 className={catering.header}>Lunch Specials</h1>
          <p>Happy Hour 4-7 PM</p>
        </div>
        <div className={catering.carousel}>
          <FoodCarousel />
        </div>
      </main>
    </>
  );
}

export default Specials;
